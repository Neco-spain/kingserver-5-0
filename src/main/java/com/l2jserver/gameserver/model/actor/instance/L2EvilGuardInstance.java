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
package com.l2jserver.gameserver.model.actor.instance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.Config;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.enums.InstanceType;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.L2WorldRegion;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.knownlist.EvilGuardKnownList;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.events.EventDispatcher;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.character.npc.OnNpcFirstTalk;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;
import com.l2jserver.util.Rnd;

/**
 * @author Destro
 */
public final class L2EvilGuardInstance extends L2Attackable
{
	private static final Logger LOG = LoggerFactory.getLogger(L2EvilGuardInstance.class);
	
	public L2EvilGuardInstance(L2NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.L2EvilGuardInstance);
	}
	
	@Override
	public final EvilGuardKnownList getKnownList()
	{
		return (EvilGuardKnownList) super.getKnownList();
	}
	
	@Override
	public void initKnownList()
	{
		setKnownList(new EvilGuardKnownList(this));
	}
	
	@Override
	public boolean isAutoAttackable(L2Character attacker)
	{
		return attacker instanceof L2MonsterInstance;
	}
	
	@Override
	public void onSpawn()
	{
		setIsNoRndWalk(true);
		super.onSpawn();
		
		// check the region where this mob is, do not activate the AI if region is inactive.
		L2WorldRegion region = L2World.getInstance().getRegion(getX(), getY());
		if ((region != null) && (!region.isActive()))
		{
			getAI().stopAITask();
		}
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		return "data/html/guard/" + pom + ".htm";
	}
	
	@Override
	public void onAction(L2PcInstance player, boolean interact)
	{
		if (!canTarget(player))
		{
			return;
		}
		
		// Check if the L2PcInstance already target the L2GuardInstance
		if (getObjectId() != player.getTargetId())
		{
			// Set the target of the L2PcInstance player
			player.setTarget(this);
		}
		else if (interact)
		{
			// Check if the L2PcInstance is in the _aggroList of the L2GuardInstance
			if (isInAggroList(player))
			{
				if (Config.DEBUG)
				{
					LOG.debug("{}: Attacked guard {}", player.getObjectId(), getObjectId());
				}
				
				// Set the L2PcInstance Intention to AI_INTENTION_ATTACK
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, this);
			}
			else
			{
				// Calculate the distance between the L2PcInstance and the L2NpcInstance
				if (!canInteract(player))
				{
					// Set the L2PcInstance Intention to AI_INTENTION_INTERACT
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this);
				}
				else
				{
					// Send a Server->Client packet SocialAction to the all L2PcInstance on the _knownPlayer of the L2NpcInstance
					// to display a social action of the L2GuardInstance on their client
					broadcastPacket(new SocialAction(getObjectId(), Rnd.nextInt(8)));
					
					player.setLastFolkNPC(this);
					
					// Open a chat window on client with the text of the L2GuardInstance
					if (hasListener(EventType.ON_NPC_QUEST_START))
					{
						player.setLastQuestNpcObject(getObjectId());
					}
					
					if (hasListener(EventType.ON_NPC_FIRST_TALK))
					{
						EventDispatcher.getInstance().notifyEventAsync(new OnNpcFirstTalk(this, player), this);
					}
					else
					{
						showChatWindow(player, 0);
					}
				}
			}
		}
		// Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}