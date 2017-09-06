/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.model.effects;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import king.server.gameserver.GameTimeController;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.model.ChanceCondition;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Summon;
import king.server.gameserver.model.interfaces.IChanceSkillTrigger;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.L2SkillType;
import king.server.gameserver.model.skills.funcs.Func;
import king.server.gameserver.model.skills.funcs.FuncTemplate;
import king.server.gameserver.model.skills.funcs.Lambda;
import king.server.gameserver.model.stats.Env;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.AbnormalStatusUpdate;
import king.server.gameserver.network.serverpackets.ExOlympiadSpelledInfo;
import king.server.gameserver.network.serverpackets.MagicSkillLaunched;
import king.server.gameserver.network.serverpackets.MagicSkillUse;
import king.server.gameserver.network.serverpackets.PartySpelled;
import king.server.gameserver.network.serverpackets.SystemMessage;

public abstract class L2Effect implements IChanceSkillTrigger
{
	protected static final Logger _log = Logger.getLogger(L2Effect.class.getName());
	
	private static final Func[] _emptyFunctionSet = new Func[0];
	
	// member _effector is the instance of L2Character that cast/used the spell/skill that is
	// causing this effect. Do not confuse with the instance of L2Character that
	// is being affected by this effect.
	private final L2Character _effector;
	
	// member _effected is the instance of L2Character that was affected
	// by this effect. Do not confuse with the instance of L2Character that
	// casted/used this effect.
	private final L2Character _effected;
	
	// the skill that was used.
	private final L2Skill _skill;
	
	// the value of an update
	private final Lambda _lambda;
	
	// the current state
	private EffectState _state;
	
	// period, seconds
	private final int _abnormalTime;
	protected int _periodStartTicks;
	protected int _periodFirstTime;
	
	private final EffectTemplate _template;
	
	// function templates
	private final FuncTemplate[] _funcTemplates;
	
	// initial count
	private final int _totalCount;
	// counter
	private int _count;
	
	// abnormal effect mask
	private final AbnormalEffect _abnormalEffect;
	// special effect mask
	private final AbnormalEffect[] _specialEffect;
	// event effect mask
	private final AbnormalEffect _eventEffect;
	// show icon
	private final boolean _icon;
	// is self effect?
	private boolean _isSelfEffect = false;
	// is passive effect?
	private boolean _isPassiveEffect = false;
	
	public boolean preventExitUpdate;
	
	protected final class EffectTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				_periodFirstTime = 0;
				_periodStartTicks = GameTimeController.getGameTicks();
				scheduleEffect();
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}
	
	private ScheduledFuture<?> _currentFuture;
	
	/** The Identifier of the stack group */
	private final String _abnormalType;
	
	/** The position of the effect in the stack group */
	private final byte _abnormalLvl;
	
	private boolean _inUse = false;
	private boolean _startConditionsCorrect = true;
	
	/**
	 * For special behavior. See Formulas.calcEffectSuccess
	 */
	private double _effectPower;
	private L2SkillType _effectSkillType;
	
	/**
	 * <font color="FF0000"><b>WARNING: scheduleEffect no longer inside constructor</b></font><br>
	 * So you must call it explicitly
	 * @param env
	 * @param template
	 */
	protected L2Effect(Env env, EffectTemplate template)
	{
		_state = EffectState.CREATED;
		_skill = env.getSkill();
		_template = template;
		_effected = env.getTarget();
		_effector = env.getCharacter();
		_lambda = template.lambda;
		_funcTemplates = template.funcTemplates;
		_count = template.counter;
		_totalCount = _count;
		
		// Support for retail herbs duration when _effected has a Summon
		int temp = template.abnormalTime;
		if (((_skill.getId() > 2277) && (_skill.getId() < 2286)) || ((_skill.getId() >= 2512) && (_skill.getId() <= 2514)))
		{
			final L2Summon summon = _effected.getSummon();
			if ((summon != null) && summon.isServitor())
			{
				temp /= 2;
			}
		}
		
		if (env.isSkillMastery())
		{
			temp *= 2;
		}
		
		_abnormalTime = temp;
		_abnormalEffect = template.abnormalEffect;
		_specialEffect = template.specialEffect;
		_eventEffect = template.eventEffect;
		_abnormalType = template.abnormalType;
		_abnormalLvl = template.abnormalLvl;
		_periodStartTicks = GameTimeController.getGameTicks();
		_periodFirstTime = 0;
		_icon = template.icon;
		_effectPower = template.effectPower;
		_effectSkillType = template.effectType;
		
		// Commented out by DrHouse:
		// scheduleEffect can call onStart before effect is completely initialized on constructor (child classes constructor)
		// scheduleEffect();
	}
	
	/**
	 * Special constructor to "steal" buffs. Must be implemented on every child class that can be stolen.<br>
	 * <font color="FF0000"><b>WARNING: scheduleEffect no longer inside constructor</b></font><br>
	 * So you must call it explicitly.
	 * @param env
	 * @param effect
	 */
	protected L2Effect(Env env, L2Effect effect)
	{
		_template = effect._template;
		_state = EffectState.CREATED;
		_skill = env.getSkill();
		_effected = env.getTarget();
		_effector = env.getCharacter();
		_lambda = _template.lambda;
		_funcTemplates = _template.funcTemplates;
		_count = effect.getCount();
		_totalCount = _template.counter;
		_abnormalTime = _template.abnormalTime;
		_abnormalEffect = _template.abnormalEffect;
		_specialEffect = _template.specialEffect;
		_eventEffect = _template.eventEffect;
		_abnormalType = _template.abnormalType;
		_abnormalLvl = _template.abnormalLvl;
		_periodStartTicks = effect.getPeriodStartTicks();
		_periodFirstTime = effect.getTime();
		_icon = _template.icon;
		
		// Commented out by DrHouse:
		// scheduleEffect can call onStart before effect is completly initialized on constructor (child classes constructor)
		// scheduleEffect();
	}
	
	public int getCount()
	{
		return _count;
	}
	
	public int getTotalCount()
	{
		return _totalCount;
	}
	
	public void setCount(int newcount)
	{
		_count = Math.min(newcount, _totalCount); // sanity check
	}
	
	public void setFirstTime(int newFirstTime)
	{
		_periodFirstTime = Math.min(newFirstTime, _abnormalTime);
		_periodStartTicks -= _periodFirstTime * GameTimeController.TICKS_PER_SECOND;
	}
	
	public boolean getShowIcon()
	{
		return _icon;
	}
	
	public int getAbnormalTime()
	{
		return _abnormalTime;
	}
	
	public int getTime()
	{
		return (GameTimeController.getGameTicks() - _periodStartTicks) / GameTimeController.TICKS_PER_SECOND;
	}
	
	/**
	 * Returns the elapsed time of the task.
	 * @return Time in seconds.
	 */
	public int getTaskTime()
	{
		if (_count == _totalCount)
		{
			return 0;
		}
		return (Math.abs((_count - _totalCount) + 1) * _abnormalTime) + getTime() + 1;
	}
	
	public boolean getInUse()
	{
		return _inUse;
	}
	
	public boolean setInUse(boolean inUse)
	{
		_inUse = inUse;
		if (_inUse)
		{
			_startConditionsCorrect = onStart();
		}
		else
		{
			onExit();
		}
		
		return _startConditionsCorrect;
	}
	
	public String getAbnormalType()
	{
		return _abnormalType;
	}
	
	public byte getAbnormalLvl()
	{
		return _abnormalLvl;
	}
	
	public final L2Skill getSkill()
	{
		return _skill;
	}
	
	public final L2Character getEffector()
	{
		return _effector;
	}
	
	public final L2Character getEffected()
	{
		return _effected;
	}
	
	public boolean isSelfEffect()
	{
		return _isSelfEffect;
	}
	
	public void setSelfEffect()
	{
		_isSelfEffect = true;
	}
	
	public boolean isPassiveEffect()
	{
		return _isPassiveEffect;
	}
	
	public void setPassiveEffect()
	{
		_isPassiveEffect = true;
	}
	
	public final double calc()
	{
		Env env = new Env();
		env.setCharacter(_effector);
		env.setTarget(_effected);
		env.setSkill(_skill);
		return _lambda.calc(env);
	}
	
	private final synchronized void startEffectTask()
	{
		if (_abnormalTime > 0)
		{
			stopEffectTask();
			final int initialDelay = Math.max((_abnormalTime - _periodFirstTime) * 1000, 5);
			if (_count > 1)
			{
				_currentFuture = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new EffectTask(), initialDelay, _abnormalTime * 1000);
			}
			else
			{
				_currentFuture = ThreadPoolManager.getInstance().scheduleEffect(new EffectTask(), initialDelay);
			}
		}
		if (_state == EffectState.ACTING)
		{
			if (isSelfEffectType())
			{
				_effector.addEffect(this);
			}
			else
			{
				_effected.addEffect(this);
			}
		}
	}
	
	/**
	 * Stop the L2Effect task and send Server->Client update packet.<br>
	 * <B><U>Actions</U>:</B>
	 * <ul>
	 * <li>Cancel the effect in the the abnormal effect map of the L2Character</li>
	 * <li>Stop the task of the L2Effect, remove it and update client magic icon</li>
	 * </ul>
	 */
	public final void exit()
	{
		exit(false);
	}
	
	public final void exit(boolean preventUpdate)
	{
		preventExitUpdate = preventUpdate;
		_state = EffectState.FINISHING;
		scheduleEffect();
	}
	
	/**
	 * Stop the task of the L2Effect, remove it and update client magic icon.<br>
	 * <B><U>Actions</U>:</B>
	 * <ul>
	 * <li>Cancel the task</li>
	 * <li>Stop and remove L2Effect from L2Character and update client magic icon</li>
	 * </ul>
	 */
	public final synchronized void stopEffectTask()
	{
		if (_currentFuture != null)
		{
			// Cancel the task
			_currentFuture.cancel(false);
			// ThreadPoolManager.getInstance().removeEffect(_currentTask);
			
			_currentFuture = null;
			
			if (isSelfEffectType() && (getEffector() != null))
			{
				getEffector().removeEffect(this);
			}
			else if (getEffected() != null)
			{
				getEffected().removeEffect(this);
			}
		}
	}
	
	/**
	 * @return effect type
	 */
	public abstract L2EffectType getEffectType();
	
	/**
	 * Notify started.
	 * @return {@code true} if all the start conditions are meet, {@code false} otherwise
	 */
	public boolean onStart()
	{
		if (_abnormalEffect != AbnormalEffect.NULL)
		{
			getEffected().startAbnormalEffect(_abnormalEffect);
		}
		if (_specialEffect != null)
		{
			getEffected().startSpecialEffect(_specialEffect);
		}
		if ((_eventEffect != AbnormalEffect.NULL) && getEffected().isPlayer())
		{
			getEffected().getActingPlayer().startEventEffect(_eventEffect);
		}
		return true;
	}
	
	/**
	 * Cancel the effect in the the abnormal effect map of the effected L2Character.
	 */
	public void onExit()
	{
		if (_abnormalEffect != AbnormalEffect.NULL)
		{
			getEffected().stopAbnormalEffect(_abnormalEffect);
		}
		if (_specialEffect != null)
		{
			getEffected().stopSpecialEffect(_specialEffect);
		}
		if ((_eventEffect != AbnormalEffect.NULL) && getEffected().isPlayer())
		{
			getEffected().getActingPlayer().stopEventEffect(_eventEffect);
		}
	}
	
	/**
	 * @return true for continuation of this effect
	 */
	public abstract boolean onActionTime();
	
	public final void scheduleEffect()
	{
		switch (_state)
		{
			case CREATED:
			{
				_state = EffectState.ACTING;
				
				if (_skill.isPvpSkill() && _icon && getEffected().isPlayer())
				{
					SystemMessage smsg = SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
					smsg.addSkillName(_skill);
					getEffected().sendPacket(smsg);
				}
				
				if (_abnormalTime != 0)
				{
					startEffectTask();
					return;
				}
				// effects not having count or period should start
				_startConditionsCorrect = onStart();
			}
			case ACTING:
			{
				if (_count > 0)
				{
					_count--;
					if (getInUse())
					{
						// effect has to be in use
						if (onActionTime() && _startConditionsCorrect && (_count >= 0))
						{
							return; // false causes effect to finish right away
						}
					}
					else if (_count > 0)
					{
						// do not finish it yet, in case reactivated
						return;
					}
				}
				_state = EffectState.FINISHING;
			}
			case FINISHING:
			{
				// If the time left is equal to zero, send the message
				if ((_count == 0) && _icon && getEffected().isPlayer())
				{
					SystemMessage smsg3 = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_WORN_OFF);
					smsg3.addSkillName(_skill);
					getEffected().sendPacket(smsg3);
				}
				// if task is null - stopEffectTask does not remove effect
				if ((_currentFuture == null) && (getEffected() != null))
				{
					getEffected().removeEffect(this);
				}
				// Stop the task of the L2Effect, remove it and update client magic icon
				stopEffectTask();
				
				// Cancel the effect in the the abnormal effect map of the L2Character
				if (getInUse() || !((_count > 1) || (_abnormalTime > 0)))
				{
					if (_startConditionsCorrect)
					{
						onExit();
					}
				}
				
				if (_skill.getAfterEffectId() > 0)
				{
					L2Skill skill = SkillTable.getInstance().getInfo(_skill.getAfterEffectId(), _skill.getAfterEffectLvl());
					if (skill != null)
					{
						getEffected().broadcastPacket(new MagicSkillUse(_effected, skill.getId(), skill.getLevel(), 0, 0));
						getEffected().broadcastPacket(new MagicSkillLaunched(_effected, skill.getId(), skill.getLevel()));
						skill.getEffects(getEffected(), getEffected());
					}
				}
			}
		}
	}
	
	public Func[] getStatFuncs()
	{
		if (_funcTemplates == null)
		{
			return _emptyFunctionSet;
		}
		
		final ArrayList<Func> funcs = new ArrayList<>(_funcTemplates.length);
		
		Env env = new Env();
		env.setCharacter(_effector);
		env.setTarget(_effected);
		env.setSkill(_skill);
		
		Func f;
		for (FuncTemplate t : _funcTemplates)
		{
			f = t.getFunc(env, this); // effect is owner
			if (f != null)
			{
				funcs.add(f);
			}
		}
		
		if (funcs.isEmpty())
		{
			return _emptyFunctionSet;
		}
		return funcs.toArray(new Func[funcs.size()]);
	}
	
	public final void addIcon(AbnormalStatusUpdate mi)
	{
		if (_state != EffectState.ACTING)
		{
			return;
		}
		
		final ScheduledFuture<?> future = _currentFuture;
		final L2Skill sk = getSkill();
		if (_totalCount > 1)
		{
			if (sk.isStatic())
			{
				mi.addEffect(sk.getDisplayId(), getLevel(), sk.getBuffDuration() - (getTaskTime() * 1000));
			}
			else
			{
				mi.addEffect(sk.getDisplayId(), getLevel(), -1);
			}
		}
		else if (future != null)
		{
			mi.addEffect(sk.getDisplayId(), getLevel(), (int) future.getDelay(TimeUnit.MILLISECONDS));
		}
		else if (_abnormalTime == -1)
		{
			mi.addEffect(sk.getDisplayId(), getLevel(), _abnormalTime);
		}
	}
	
	public final void addPartySpelledIcon(PartySpelled ps)
	{
		if (_state != EffectState.ACTING)
		{
			return;
		}
		
		final ScheduledFuture<?> future = _currentFuture;
		final L2Skill sk = getSkill();
		if (future != null)
		{
			ps.addPartySpelledEffect(sk.getDisplayId(), getLevel(), (int) future.getDelay(TimeUnit.MILLISECONDS));
		}
		else if (_abnormalTime == -1)
		{
			ps.addPartySpelledEffect(sk.getDisplayId(), getLevel(), _abnormalTime);
		}
	}
	
	public final void addOlympiadSpelledIcon(ExOlympiadSpelledInfo os)
	{
		if (_state != EffectState.ACTING)
		{
			return;
		}
		
		final ScheduledFuture<?> future = _currentFuture;
		final L2Skill sk = getSkill();
		if (future != null)
		{
			os.addEffect(sk.getDisplayId(), getLevel(), (int) future.getDelay(TimeUnit.MILLISECONDS));
		}
		else if (_abnormalTime == -1)
		{
			os.addEffect(sk.getDisplayId(), getLevel(), _abnormalTime);
		}
	}
	
	public int getLevel()
	{
		return getSkill().getLevel();
	}
	
	public int getPeriodStartTicks()
	{
		return _periodStartTicks;
	}
	
	public EffectTemplate getEffectTemplate()
	{
		return _template;
	}
	
	public double getEffectPower()
	{
		return _effectPower;
	}
	
	public L2SkillType getSkillType()
	{
		return _effectSkillType;
	}
	
	public boolean canBeStolen()
	{
		// TODO: Unhardcode skillId
		return (!effectCanBeStolen() || (getEffectType() == L2EffectType.TRANSFORMATION) || getSkill().isPassive() || getSkill().isToggle() || getSkill().isDebuff() || getSkill().isHeroSkill() || getSkill().isGMSkill() || (getSkill().isStatic() && ((getSkill().getId() != 2274) && (getSkill().getId() != 2341))) || !getSkill().canBeDispeled()) ? false : true;
	}
	
	/**
	 * @return {@code true} if effect itself can be stolen, {@code false} otherwise
	 */
	protected boolean effectCanBeStolen()
	{
		return false;
	}
	
	/**
	 * Return bit flag for current effect
	 * @return int flag
	 */
	public int getEffectFlags()
	{
		return EffectFlag.NONE.getMask();
	}
	
	@Override
	public String toString()
	{
		return "L2Effect [_skill=" + _skill + ", _state=" + _state + ", _period=" + _abnormalTime + "]";
	}
	
	public boolean isSelfEffectType()
	{
		return false;
	}
	
	public void decreaseForce()
	{
	}
	
	public void increaseEffect()
	{
	}
	
	public int getForceEffect()
	{
		return 0;
	}
	
	public boolean isBuffEffect()
	{
		return false;
	}
	
	public boolean isDebuffEffect()
	{
		return false;
	}
	
	@Override
	public boolean triggersChanceSkill()
	{
		return false;
	}
	
	@Override
	public int getTriggeredChanceId()
	{
		return 0;
	}
	
	@Override
	public int getTriggeredChanceLevel()
	{
		return 0;
	}
	
	@Override
	public ChanceCondition getTriggeredChanceCondition()
	{
		return null;
	}

	public int getId()
	{
		return getSkill().getId();
	}	
}