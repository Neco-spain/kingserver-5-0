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

import king.server.Config;
import king.server.gameserver.ai.CtrlEvent;
import king.server.gameserver.instancemanager.InstanceManager;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.actor.knownlist.PlayableKnownList;
import king.server.gameserver.model.actor.stat.PlayableStat;
import king.server.gameserver.model.actor.status.PlayableStatus;
import king.server.gameserver.model.actor.templates.L2CharTemplate;
import king.server.gameserver.model.effects.EffectFlag;
import king.server.gameserver.model.effects.L2Effect;
import king.server.gameserver.model.effects.L2EffectType;
import king.server.gameserver.model.entity.Instance;
import king.server.gameserver.model.quest.QuestState;
import king.server.gameserver.model.skills.L2Skill;

/**
 * This class represents all Playable characters in the world.<br>
 * L2Playable:
 * <ul>
 * <li>L2PcInstance</li>
 * <li>L2Summon</li>
 * </ul>
 */
public abstract class L2Playable extends L2Character
{
	private L2Character _lockedTarget = null;
	private String _lastTownName = null;
	private int _hitmanTarget = 0;
	
	/**
	 * Constructor of L2Playable.<br>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>Call the L2Character constructor to create an empty _skills slot and link copy basic Calculator set to this L2Playable</li>
	 * </ul>
	 * @param objectId Identifier of the object to initialized
	 * @param template The L2CharTemplate to apply to the L2Playable
	 */
	public L2Playable(int objectId, L2CharTemplate template)
	{
		super(objectId, template);
		setInstanceType(InstanceType.L2Playable);
		setIsInvul(false);
	}
	
	@Override
	public PlayableKnownList getKnownList()
	{
		return (PlayableKnownList) super.getKnownList();
	}
	
	@Override
	public void initKnownList()
	{
		setKnownList(new PlayableKnownList(this));
	}
	
	@Override
	public PlayableStat getStat()
	{
		return (PlayableStat) super.getStat();
	}
	
	@Override
	public void initCharStat()
	{
		setStat(new PlayableStat(this));
	}
	
	@Override
	public PlayableStatus getStatus()
	{
		return (PlayableStatus) super.getStatus();
	}
	
	@Override
	public void initCharStatus()
	{
		setStatus(new PlayableStatus(this));
	}
	
	@Override
	public boolean doDie(L2Character killer)
	{
		// killing is only possible one time
		synchronized (this)
		{
			if (isDead())
			{
				return false;
			}
			// now reset currentHp to zero
			setCurrentHp(0);
			setIsDead(true);
		}
		
		/**
		 * This needs to stay here because it overrides L2Character.doDie() and does not call for super.doDie()
		 */
		if (!super.fireDeathListeners(killer))
		{
			return false;
		}
		
		// Set target to null and cancel Attack or Cast
		setTarget(null);
		
		// Stop movement
		stopMove(null);
		
		// Stop HP/MP/CP Regeneration task
		getStatus().stopHpMpRegeneration();
		
		// Stop all active skills effects in progress on the L2Character,
		// if the Character isn't affected by Soul of The Phoenix or Salvation
		if (isPhoenixBlessed())
		{
			if (getCharmOfLuck())
			{
				stopCharmOfLuck(null);
			}
			if (isNoblesseBlessed())
			{
				stopNoblesseBlessing(null);
			}
		}
		// Same thing if the Character isn't a Noblesse Blessed L2Playable
		else if (isNoblesseBlessed())
		{
			stopNoblesseBlessing(null);
			
			if (getCharmOfLuck())
			{
				stopCharmOfLuck(null);
			}
		}
		else if (!isInTownWarEvent() || Config.TW_LOSE_BUFFS_ON_DEATH)
		{
			stopAllEffectsExceptThoseThatLastThroughDeath();
		}
		
		// Send the Server->Client packet StatusUpdate with current HP and MP to all other L2PcInstance to inform
		broadcastStatusUpdate();
		
		if (getWorldRegion() != null)
		{
			getWorldRegion().onDeath(this);
		}
		
		// Notify Quest of L2Playable's death
		L2PcInstance actingPlayer = getActingPlayer();
		if (!actingPlayer.isNotifyQuestOfDeathEmpty())
		{
			for (QuestState qs : actingPlayer.getNotifyQuestOfDeath())
			{
				qs.getQuest().notifyDeath((killer == null ? this : killer), this, qs);
			}
		}
		// Notify instance
		if (getInstanceId() > 0)
		{
			final Instance instance = InstanceManager.getInstance().getInstance(getInstanceId());
			if (instance != null)
			{
				instance.notifyDeath(killer, this);
			}
		}
		
		if (killer != null)
		{
			L2PcInstance player = killer.getActingPlayer();
			
			if (player != null)
			{
				player.onKillUpdatePvPKarma(this);
			}
		}
		
		// Notify L2Character AI
		getAI().notifyEvent(CtrlEvent.EVT_DEAD);
		
		return true;
	}
	
	public boolean checkIfPvP(L2Character target)
	{
		if (target == null)
		{
			return false; // Target is null
		}
		if (target == this)
		{
			return false; // Target is self
		}
		if (!target.isPlayable())
		{
			return false; // Target is not a L2Playable
		}
		
		final L2PcInstance player = getActingPlayer();
		if (player == null)
		{
			return false; // Active player is null
		}
		
		if (player.getKarma() != 0)
		{
			return false; // Active player has karma
		}
		
		final L2PcInstance targetPlayer = target.getActingPlayer();
		if (targetPlayer == null)
		{
			return false; // Target player is null
		}
		
		if (targetPlayer == this)
		{
			return false; // Target player is self
		}
		if (targetPlayer.getKarma() != 0)
		{
			return false; // Target player has karma
		}
		if (targetPlayer.getPvpFlag() == 0)
		{
			return false;
		}
		
		return true;
		// Even at war, there should be PvP flag
		// if(
		// player.getClan() == null ||
		// targetPlayer.getClan() == null ||
		// (
		// !targetPlayer.getClan().isAtWarWith(player.getClanId()) &&
		// targetPlayer.getWantsPeace() == 0 &&
		// player.getWantsPeace() == 0
		// )
		// )
		// {
		// return true;
		// }
		// return false;
	}
	
	/**
	 * Return True.
	 */
	@Override
	public boolean isAttackable()
	{
		return true;
	}
	
	// Support for Noblesse Blessing skill, where buffs are retained
	// after resurrect
	public final boolean isNoblesseBlessed()
	{
		return _effects.isAffected(EffectFlag.NOBLESS_BLESSING);
	}
	
	public final void stopNoblesseBlessing(L2Effect effect)
	{
		if (effect == null)
		{
			stopEffects(L2EffectType.NOBLESSE_BLESSING);
		}
		else
		{
			removeEffect(effect);
		}
		updateAbnormalEffect();
	}
	
	// Support for Soul of the Phoenix and Salvation skills
	public final boolean isPhoenixBlessed()
	{
		return _effects.isAffected(EffectFlag.PHOENIX_BLESSING);
	}
	
	public final void stopPhoenixBlessing(L2Effect effect)
	{
		if (effect == null)
		{
			stopEffects(L2EffectType.PHOENIX_BLESSING);
		}
		else
		{
			removeEffect(effect);
		}
		
		updateAbnormalEffect();
	}
	
	/**
	 * @return True if the Silent Moving mode is active.
	 */
	public boolean isSilentMoving()
	{
		return _effects.isAffected(EffectFlag.SILENT_MOVE);
	}
	
	// for Newbie Protection Blessing skill, keeps you safe from an attack by a chaotic character >= 10 levels apart from you
	public final boolean getProtectionBlessing()
	{
		return _effects.isAffected(EffectFlag.PROTECTION_BLESSING);
	}
	
	/**
	 * @param effect
	 */
	public void stopProtectionBlessing(L2Effect effect)
	{
		if (effect == null)
		{
			stopEffects(L2EffectType.PROTECTION_BLESSING);
		}
		else
		{
			removeEffect(effect);
		}
		
		updateAbnormalEffect();
	}
	
	// Charm of Luck - During a Raid/Boss war, decreased chance for death penalty
	public final boolean getCharmOfLuck()
	{
		return _effects.isAffected(EffectFlag.CHARM_OF_LUCK);
	}
	
	public final void stopCharmOfLuck(L2Effect effect)
	{
		if (effect == null)
		{
			stopEffects(L2EffectType.CHARM_OF_LUCK);
		}
		else
		{
			removeEffect(effect);
		}
		
		updateAbnormalEffect();
	}
	
	@Override
	public void updateEffectIcons(boolean partyOnly)
	{
		_effects.updateEffectIcons(partyOnly);
	}
	
	public boolean isLockedTarget()
	{
		return _lockedTarget != null;
	}
	
	public L2Character getLockedTarget()
	{
		return _lockedTarget;
	}
	
	public void setLockedTarget(L2Character cha)
	{
		_lockedTarget = cha;
	}
	
	public void setLastTownName(String lastTownName)
	{
		_lastTownName = lastTownName;
	}
	
	public String getLastTownName()
	{
		return _lastTownName;
	}
	
	public void setHitmanTarget(int hitmanTarget)
	{
		_hitmanTarget = hitmanTarget;
	}
	
	public int getHitmanTarget()
	{
		return _hitmanTarget;
	}
	
	L2PcInstance transferDmgTo;
	
	public void setTransferDamageTo(L2PcInstance val)
	{
		transferDmgTo = val;
	}
	
	public L2PcInstance getTransferingDamageTo()
	{
		return transferDmgTo;
	}
	
	public abstract int getKarma();
	
	public abstract byte getPvpFlag();
	
	public abstract boolean useMagic(L2Skill skill, boolean forceUse, boolean dontMove);
	
	public abstract void store();
	
	public abstract void storeEffect(boolean storeEffects);
	
	public abstract void restoreEffects();
	
	@Override
	public boolean isPlayable()
	{
		return true;
	}
}
