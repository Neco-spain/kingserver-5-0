/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package hellbound.Quarry;

import king.server.Config;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.instancemanager.HellboundManager;
import king.server.gameserver.instancemanager.ZoneManager;
import king.server.gameserver.model.actor.L2Attackable;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.actor.instance.L2QuestGuardInstance;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.model.zone.L2ZoneType;
import king.server.gameserver.network.NpcStringId;
import king.server.gameserver.network.clientpackets.Say2;
import king.server.gameserver.network.serverpackets.NpcSay;

/**
 * @author DS, GKR
 */
public class Quarry extends Quest
{
	private static final int SLAVE = 32299;
	private static final int TRUST = 50;
	private static final int ZONE = 40107;
	// Id, chance (n from 10000)
	protected static final int[][] DROPLIST =
	{
		{
			9628,
			261
		}, // Leonard
		{
			9630,
			175
		}, // Orichalcum
		{
			9629,
			145
		}, // Adamantine
		{
			1876,
			6667
		}, // Mithril ore
		{
			1877,
			1333
		}, // Adamantine nugget
		{
			1874,
			2222
		}
	// Oriharukon ore
	};
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("time_limit"))
		{
			for (L2ZoneType zone : ZoneManager.getInstance().getZones(npc))
			{
				if (zone.getId() == 40108)
				{
					npc.setTarget(null);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
					npc.setAutoAttackable(false);
					npc.setRHandId(0);
					npc.teleToLocation(npc.getSpawn().getLocx(), npc.getSpawn().getLocy(), npc.getSpawn().getLocz());
					return null;
				}
			}
			
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.HUN_HUNGRY));
			npc.doDie(npc);
			return null;
		}
		else if (event.equalsIgnoreCase("FollowMe"))
		{
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_FOLLOW, player);
			npc.setTarget(player);
			npc.setAutoAttackable(true);
			npc.setRHandId(9136);
			npc.setWalking();
			
			if (getQuestTimer("time_limit", npc, null) == null)
			{
				startQuestTimer("time_limit", 900000, npc, null); // 15 min limit for save
			}
			return "32299-02.htm";
		}
		return event;
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		npc.setAutoAttackable(false);
		if (npc instanceof L2QuestGuardInstance)
		{
			((L2QuestGuardInstance) npc).setPassive(true);
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public final String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if (HellboundManager.getInstance().getLevel() != 5)
		{
			return "32299.htm";
		}
        if (player.getQuestState(getName()) == null) 
 	     { 
 	        newQuestState(player); 
 	     } 
 	        return "32299-01.htm"; 
	}
	
	// Let's manage kill points in Engine
	@Override
	public final String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		npc.setAutoAttackable(false);
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public final String onEnterZone(L2Character character, L2ZoneType zone)
	{
		if (character instanceof L2Attackable)
		{
			final L2Attackable npc = (L2Attackable) character;
			if (npc.getNpcId() == SLAVE)
			{
				if (!npc.isDead() && !npc.isDecayed() && (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_FOLLOW))
				{
					if (HellboundManager.getInstance().getLevel() == 5)
					{
						ThreadPoolManager.getInstance().scheduleGeneral(new Decay(npc), 1000);
						try
						{
							npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.THANK_YOU_FOR_THE_RESCUE_ITS_A_SMALL_GIFT));
						}
						catch (Exception e)
						{
							//
						}
					}
				}
			}
		}
		return null;
	}
	
	private final class Decay implements Runnable
	{
		private final L2Npc _npc;
		
		public Decay(L2Npc npc)
		{
			_npc = npc;
		}
		
		@Override
		public void run()
		{
			if ((_npc != null) && !_npc.isDead())
			{
				if (_npc.getTarget() instanceof L2PcInstance)
				{
					for (int[] i : DROPLIST)
					{
						if (getRandom(10000) < i[1])
						{
							((L2Attackable) _npc).dropItem((L2PcInstance) _npc.getTarget(), i[0], (int) Config.RATE_DROP_ITEMS);
							break;
						}
					}
				}
				
				_npc.setAutoAttackable(false);
				_npc.deleteMe();
				_npc.getSpawn().decreaseCount(_npc);
				HellboundManager.getInstance().updateTrust(TRUST, true);
			}
		}
	}
	
	public Quarry(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addSpawnId(SLAVE);
		addFirstTalkId(SLAVE);
		addStartNpc(SLAVE);
		addTalkId(SLAVE);
		addKillId(SLAVE);
		addEnterZoneId(ZONE);
	}
	
	public static void main(String[] args)
	{
		new Quarry(-1, "Quarry", "hellbound");
	}
}
