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
package quests.Q00198_SevenSignsEmbryo;

import quests.Q00197_SevenSignsTheSacredBookOfSeal.Q00197_SevenSignsTheSacredBookOfSeal;

import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2MonsterInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.holders.SkillHolder;
import king.server.gameserver.model.itemcontainer.PcInventory;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.model.quest.QuestState;
import king.server.gameserver.model.quest.State;
import king.server.gameserver.network.NpcStringId;
import king.server.gameserver.network.clientpackets.Say2;
import king.server.gameserver.network.serverpackets.NpcSay;
import king.server.util.Rnd;

/**
 * Seven Signs, Embryo (198)
 * @author Adry_85
 */
public class Q00198_SevenSignsEmbryo extends Quest
{
	// NPCs
	private static final int SHILENS_EVIL_THOUGHTS = 27346;
	private static final int WOOD = 32593;
	private static final int FRANZ = 32597;
	private static final int JAINA = 32617;
	// Items
	private static final int SCULPTURE_OF_DOUBT = 14355;
	private static final int DAWNS_BRACELET = 15312;
	// Misc
	private static final int MIN_LEVEL = 79;
	private boolean isBusy = false;
	// Skill
	private static SkillHolder NPC_HEAL = new SkillHolder(4065, 8);
	
	public Q00198_SevenSignsEmbryo(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addFirstTalkId(JAINA);
		addStartNpc(WOOD);
		addTalkId(WOOD, FRANZ);
		addKillId(SHILENS_EVIL_THOUGHTS);
		registerQuestItems(SCULPTURE_OF_DOUBT);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if ((npc.getNpcId() == SHILENS_EVIL_THOUGHTS) && "despawn".equals(event))
		{
			if (!npc.isDead())
			{
				isBusy = false;
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.NEXT_TIME_YOU_WILL_NOT_ESCAPE));
				npc.deleteMe();
			}
			return super.onAdvEvent(event, npc, player);
		}
		
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "32593-02.html":
			{
				st.startQuest();
				htmltext = event;
				break;
			}
			case "32597-02.html":
			case "32597-03.html":
			case "32597-04.html":
			{
				if (st.isCond(1))
				{
					htmltext = event;
				}
				break;
			}
			case "fight":
			{
				htmltext = "32597-05.html";
				if (st.isCond(1))
				{
					isBusy = true;
					NpcSay ns = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.S1_THAT_STRANGER_MUST_BE_DEFEATED_HERE_IS_THE_ULTIMATE_HELP);
					ns.addStringParameter(player.getName());
					npc.broadcastPacket(ns);
					startQuestTimer("heal", 30000 - Rnd.get(20000), npc, player);
					L2MonsterInstance monster = (L2MonsterInstance) addSpawn(SHILENS_EVIL_THOUGHTS, -23734, -9184, -5384, 0, false, 0, false, npc.getInstanceId());
					monster.broadcastPacket(new NpcSay(monster.getObjectId(), Say2.NPC_ALL, monster.getNpcId(), NpcStringId.YOU_ARE_NOT_THE_OWNER_OF_THAT_ITEM));
					monster.setRunning();
					monster.addDamageHate(player, 0, 999);
					monster.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
					startQuestTimer("despawn", 300000, monster, null);
				}
				break;
			}
			case "heal":
			{
				if (!npc.isInsideRadius(player, 600, true, false))
				{
					NpcSay ns = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.LOOK_HERE_S1_DONT_FALL_TOO_FAR_BEHIND);
					ns.addStringParameter(player.getName());
					npc.broadcastPacket(ns);
				}
				else if (!player.isDead())
				{
					npc.setTarget(player);
					npc.doCast(NPC_HEAL.getSkill());
				}
				startQuestTimer("heal", 30000 - Rnd.get(20000), npc, player);
				break;
			}
			case "32597-08.html":
			case "32597-09.html":
			case "32597-10.html":
			{
				if (st.isCond(2) && st.hasQuestItems(SCULPTURE_OF_DOUBT))
				{
					htmltext = event;
				}
				break;
			}
			case "32597-11.html":
			{
				if (st.isCond(2) && st.hasQuestItems(SCULPTURE_OF_DOUBT))
				{
					st.takeItems(SCULPTURE_OF_DOUBT, -1);
					st.setCond(3, true);
					htmltext = event;
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.WE_WILL_BE_WITH_YOU_ALWAYS));
				}
				break;
			}
			case "32617-02.html":
			{
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg(player);
		if (st == null)
		{
			return htmltext;
		}
		
		return "32617-01.html";
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		final L2PcInstance partyMember = getRandomPartyMember(player, 1);
		if (partyMember == null)
		{
			return null;
		}
		
		final QuestState st = partyMember.getQuestState(getName());
		
		if (npc.isInsideRadius(player, 1500, true, false))
		{
			st.giveItems(SCULPTURE_OF_DOUBT, 1);
			st.playSound(QuestSound.ITEMSOUND_QUEST_FINISH);
			st.setCond(2);
		}
		
		isBusy = false;
		cancelQuestTimers("despawn");
		cancelQuestTimers("heal");
		NpcSay ns = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.S1_YOU_MAY_HAVE_WON_THIS_TIME_BUT_NEXT_TIME_I_WILL_SURELY_CAPTURE_YOU);
		ns.addStringParameter(player.getName());
		npc.broadcastPacket(ns);
		return super.onKill(npc, player, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg(player);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (st.getState())
		{
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
			case State.CREATED:
			{
				if (npc.getNpcId() == WOOD)
				{
					st = player.getQuestState(Q00197_SevenSignsTheSacredBookOfSeal.class.getSimpleName());
					htmltext = ((player.getLevel() >= MIN_LEVEL) && (st != null) && (st.isCompleted())) ? "32593-01.htm" : "32593-03.html";
				}
				break;
			}
			case State.STARTED:
			{
				if (npc.getNpcId() == WOOD)
				{
					if ((st.getCond() > 0) && (st.getCond() < 3))
					{
						htmltext = "32593-04.html";
					}
					else if (st.isCond(3))
					{
						if (player.getLevel() >= MIN_LEVEL)
						{
							st.addExpAndSp(315108090, 34906059);
							st.giveItems(DAWNS_BRACELET, 1);
							st.giveItems(PcInventory.ANCIENT_ADENA_ID, 1500000);
							st.exitQuest(false, true);
							htmltext = "32593-05.html";
						}
						else
						{
							htmltext = "level_check.html";
						}
					}
				}
				else if (npc.getNpcId() == FRANZ)
				{
					switch (st.getCond())
					{
						case 1:
						{
							htmltext = (isBusy) ? "32597-06.html" : "32597-01.html";
							break;
						}
						case 2:
						{
							if (st.hasQuestItems(SCULPTURE_OF_DOUBT))
							{
								htmltext = "32597-07.html";
							}
							break;
						}
						case 3:
						{
							htmltext = "32597-12.html";
							break;
						}
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String args[])
	{
		new Q00198_SevenSignsEmbryo(198, Q00198_SevenSignsEmbryo.class.getSimpleName(), "Seven Signs, Embryo");
	}
}
