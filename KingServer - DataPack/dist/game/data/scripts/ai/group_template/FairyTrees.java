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
package ai.group_template;

import ai.npc.AbstractNpcAI;

import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.model.actor.L2Attackable;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.util.Util;
import king.server.util.Rnd;

/**
 * Fairy Trees AI
 * @author Charus
 */
public class FairyTrees extends AbstractNpcAI
{
	private static final int[] MOBS =
	{
		27185,
		27186,
		27187,
		27188
	};
	
	private FairyTrees(String name, String descr)
	{
		super(name, descr);
		registerMobs(MOBS, QuestEventType.ON_KILL);
		addSpawnId(27189);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		int npcId = npc.getNpcId();
		if (Util.contains(MOBS, npcId))
		{
			for (int i = 0; i < 20; i++)
			{
				L2Attackable newNpc = (L2Attackable) addSpawn(27189, npc.getX(), npc.getY(), npc.getZ(), 0, false, 30000);
				L2Character originalKiller = isSummon ? killer.getSummon() : killer;
				newNpc.setRunning();
				newNpc.addDamageHate(originalKiller, 0, 999);
				newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalKiller);
				if (Rnd.nextBoolean())
				{
					L2Skill skill = SkillTable.getInstance().getInfo(4243, 1);
					if ((skill != null) && (originalKiller != null))
					{
						skill.getEffects(newNpc, originalKiller);
					}
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new FairyTrees(FairyTrees.class.getSimpleName(), "ai");
	}
}
