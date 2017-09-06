/*
 * Copyright (C) 2004-2017 L2J Server
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
package com.l2jserver.gameserver.model.actor.knownlist;

import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2EvilGuardInstance;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Destro
 */
public class EvilGuardKnownList extends AttackableKnownList
{
	private static Logger _log = Logger.getLogger(EvilGuardKnownList.class.getName());
	
	public EvilGuardKnownList(L2EvilGuardInstance activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public boolean addKnownObject(L2Object object)
	{
		if (!super.addKnownObject(object))
		{
			return false;
		}
		
		if (object instanceof L2PcInstance)
		{
			// Check if the object added is a L2PcInstance that owns Karma
			L2PcInstance player = (L2PcInstance) object;
			
			if (player.isGood())
			{
				if (Config.DEBUG)
				{
					_log.fine(getActiveChar().getObjectId() + ": PK " + player.getObjectId() + " entered scan range");
				}
				
				// Set the L2GuardInstance Intention to AI_INTENTION_ACTIVE
				if (getActiveChar().getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE)
				{
					getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
				}
			}
		}
		else if ((Config.GUARD_ATTACK_AGGRO_MOB && getActiveChar().isInActiveRegion()) && (object instanceof L2MonsterInstance))
		{
			// Check if the object added is an aggressive L2MonsterInstance
			L2MonsterInstance mob = (L2MonsterInstance) object;
			
			if (mob.isAggressive())
			{
				if (Config.DEBUG)
				{
					_log.fine(getActiveChar().getObjectId() + ": Aggressive mob " + mob.getObjectId() + " entered scan range");
				}
				
				// Set the L2GuardInstance Intention to AI_INTENTION_ACTIVE
				if (getActiveChar().getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE)
				{
					getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null);
				}
			}
		}
		
		return true;
	}
	
	@Override
	protected boolean removeKnownObject(L2Object object, boolean forget)
	{
		if (!super.removeKnownObject(object, forget))
		{
			return false;
		}
		
		// Check if the aggression list of this guard is empty.
		if (getActiveChar().getAggroList().isEmpty())
		{
			// Set the L2GuardInstance to AI_INTENTION_IDLE
			if (getActiveChar().hasAI() && !getActiveChar().isWalker())
			{
				getActiveChar().getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null);
			}
		}
		
		return true;
	}
	
	@Override
	public final L2EvilGuardInstance getActiveChar()
	{
		return (L2EvilGuardInstance) super.getActiveChar();
	}
}
