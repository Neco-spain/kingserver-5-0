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
package ai.npc.DragonVortex;

import ai.npc.AbstractNpcAI;

import king.server.gameserver.model.Location;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * Dragon Vortex AI
 * @author UnAfraid, improved by Adry_85
 */
public class DragonVortex extends AbstractNpcAI
{
	private static final int VORTEX = 32871;
	
	private static final int[] RAIDS =
	{
        25718, // Emerald Horn
        25719, // Dust Rider
        25720, // Bleeding Fly
        25721, // Blackdagger Wing
        25722, // Shadow Summoner
        25723, // Spike Slasher
        25724, // Muscle Bomber
	};
	
	private static final int LARGE_DRAGON_BONE = 17248;
	
	private static final int DESPAWN_DELAY = 1800000; // 30min
	
	private DragonVortex(String name, String descr)
	{
		super(name, descr);
		addStartNpc(VORTEX);
		addFirstTalkId(VORTEX);
		addTalkId(VORTEX);
		addKillId(RAIDS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if ("Spawn".equals(event))
		{
			int raid = RAIDS[getRandom(RAIDS.length)];
			
			if (hasQuestItems(player, LARGE_DRAGON_BONE))
			{
				takeItems(player, LARGE_DRAGON_BONE, 1);
				addSpawn(raid, new Location(player.getX() + getRandom(100), player.getY() + getRandom(100), player.getZ(), player.getHeading()), true, DESPAWN_DELAY);
			}
			else
			{
				return "32871-no.html";
			}
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new DragonVortex(DragonVortex.class.getSimpleName(), "ai/npc/");
	}
}
