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
package king.server.gameserver.model.actor;

import java.util.Collection;
import java.util.logging.Level;

import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.actor.knownlist.TrapKnownList;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.gameserver.model.items.L2Weapon;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.model.quest.Quest.TrapAction;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.network.serverpackets.AbstractNpcInfo;
import king.server.gameserver.network.serverpackets.L2GameServerPacket;
import king.server.gameserver.network.serverpackets.SocialAction;
import king.server.gameserver.taskmanager.DecayTaskManager;

/**
 * @author nBd
 */
public class L2Trap extends L2Character
{
	protected static final int TICK = 1000; // 1s
	
	protected boolean _isTriggered;
	protected final L2Skill _skill;
	protected final int _lifeTime;
	protected int _timeRemaining;
	protected boolean _hasLifeTime;
	
	/**
	 * @param objectId
	 * @param template
	 * @param lifeTime
	 * @param skill
	 */
	public L2Trap(int objectId, L2NpcTemplate template, int lifeTime, L2Skill skill)
	{
		super(objectId, template);
		setInstanceType(InstanceType.L2Trap);
		setName(template.getName());
		setIsInvul(false);
		
		_isTriggered = false;
		_skill = skill;
		_hasLifeTime = true;
		if (lifeTime != 0)
		{
			_lifeTime = lifeTime;
		}
		else
		{
			_lifeTime = 30000;
		}
		_timeRemaining = _lifeTime;
		if (lifeTime < 0)
		{
			_hasLifeTime = false;
		}
		
		if (skill != null)
		{
			ThreadPoolManager.getInstance().scheduleGeneral(new TrapTask(), TICK);
		}
	}
	
	@Override
	public TrapKnownList getKnownList()
	{
		return (TrapKnownList) super.getKnownList();
	}
	
	@Override
	public void initKnownList()
	{
		setKnownList(new TrapKnownList(this));
	}
	
	@Override
	public boolean isAutoAttackable(L2Character attacker)
	{
		return !canSee(attacker);
	}
	
	public void stopDecay()
	{
		DecayTaskManager.getInstance().cancelDecayTask(this);
	}
	
	@Override
	public void onDecay()
	{
		deleteMe();
	}
	
	/**
	 * @return
	 */
	public final int getNpcId()
	{
		return getTemplate().getNpcId();
	}
	
	@Override
	public boolean doDie(L2Character killer)
	{
		if (!super.doDie(killer))
		{
			return false;
		}
		
		DecayTaskManager.getInstance().addDecayTask(this);
		return true;
	}
	
	@Override
	public void deleteMe()
	{
		decayMe();
		getKnownList().removeAllKnownObjects();
		super.deleteMe();
	}
	
	public synchronized void unSummon()
	{
		if (isVisible() && !isDead())
		{
			if (getWorldRegion() != null)
			{
				getWorldRegion().removeFromZones(this);
			}
			
			deleteMe();
		}
	}
	
	@Override
	public L2ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	@Override
	public L2Weapon getActiveWeaponItem()
	{
		return null;
	}
	
	@Override
	public int getLevel()
	{
		return getTemplate().getLevel();
	}
	
	@Override
	public L2NpcTemplate getTemplate()
	{
		return (L2NpcTemplate) super.getTemplate();
	}
	
	@Override
	public L2ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	@Override
	public L2Weapon getSecondaryWeaponItem()
	{
		return null;
	}
	
	@Override
	public void updateAbnormalEffect()
	{
		
	}
	
	public L2Skill getSkill()
	{
		return _skill;
	}
	
	public L2PcInstance getOwner()
	{
		return null;
	}
	
	public int getKarma()
	{
		return 0;
	}
	
	public byte getPvpFlag()
	{
		return 0;
	}
	
	/**
	 * Checks is triggered
	 * @return True if trap is triggered.
	 */
	public boolean isTriggered()
	{
		return _isTriggered;
	}
	
	/**
	 * Checks trap visibility
	 * @param cha - checked character
	 * @return True if character can see trap
	 */
	public boolean canSee(L2Character cha)
	{
		return false;
	}
	
	/**
	 * Reveal trap to the detector (if possible)
	 * @param detector
	 */
	public void setDetected(L2Character detector)
	{
		detector.sendPacket(new AbstractNpcInfo.TrapInfo(this, detector));
	}
	
	/**
	 * Check if target can trigger trap
	 * @param target
	 * @return
	 */
	protected boolean checkTarget(L2Character target)
	{
		return L2Skill.checkForAreaOffensiveSkills(this, target, _skill, false);
	}
	
	protected class TrapTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				if (!_isTriggered)
				{
					if (_hasLifeTime)
					{
						_timeRemaining -= TICK;
						if (_timeRemaining < (_lifeTime - 15000))
						{
							SocialAction sa = new SocialAction(getObjectId(), 2);
							broadcastPacket(sa);
						}
						if (_timeRemaining < 0)
						{
							switch (getSkill().getTargetType())
							{
								case TARGET_AURA:
								case TARGET_FRONT_AURA:
								case TARGET_BEHIND_AURA:
									trigger(L2Trap.this);
									break;
								default:
									unSummon();
							}
							return;
						}
					}
					
					for (L2Character target : getKnownList().getKnownCharactersInRadius(_skill.getSkillRadius()))
					{
						if (!checkTarget(target))
						{
							continue;
						}
						
						trigger(target);
						return;
					}
					
					ThreadPoolManager.getInstance().scheduleGeneral(new TrapTask(), TICK);
				}
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "", e);
				unSummon();
			}
		}
	}
	
	/**
	 * Trigger trap
	 * @param target
	 */
	public void trigger(L2Character target)
	{
		_isTriggered = true;
		broadcastPacket(new AbstractNpcInfo.TrapInfo(this, null));
		setTarget(target);
		
		if (getTemplate().getEventQuests(Quest.QuestEventType.ON_TRAP_ACTION) != null)
		{
			for (Quest quest : getTemplate().getEventQuests(Quest.QuestEventType.ON_TRAP_ACTION))
			{
				quest.notifyTrapAction(this, target, TrapAction.TRAP_TRIGGERED);
			}
		}
		
		ThreadPoolManager.getInstance().scheduleGeneral(new TriggerTask(), 300);
	}
	
	protected class TriggerTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				doCast(_skill);
				ThreadPoolManager.getInstance().scheduleGeneral(new UnsummonTask(), _skill.getHitTime() + 300);
			}
			catch (Exception e)
			{
				unSummon();
			}
		}
	}
	
	protected class UnsummonTask implements Runnable
	{
		@Override
		public void run()
		{
			unSummon();
		}
	}
	
	@Override
	public void sendInfo(L2PcInstance activeChar)
	{
		if (_isTriggered || canSee(activeChar))
		{
			activeChar.sendPacket(new AbstractNpcInfo.TrapInfo(this, activeChar));
		}
	}
	
	@Override
	public void broadcastPacket(L2GameServerPacket mov)
	{
		Collection<L2PcInstance> plrs = getKnownList().getKnownPlayers().values();
		for (L2PcInstance player : plrs)
		{
			if ((player != null) && (_isTriggered || canSee(player)))
			{
				player.sendPacket(mov);
			}
		}
	}
	
	@Override
	public void broadcastPacket(L2GameServerPacket mov, int radiusInKnownlist)
	{
		Collection<L2PcInstance> plrs = getKnownList().getKnownPlayers().values();
		for (L2PcInstance player : plrs)
		{
			if (player == null)
			{
				continue;
			}
			if (isInsideRadius(player, radiusInKnownlist, false, false))
			{
				if (_isTriggered || canSee(player))
				{
					player.sendPacket(mov);
				}
			}
		}
	}
}
