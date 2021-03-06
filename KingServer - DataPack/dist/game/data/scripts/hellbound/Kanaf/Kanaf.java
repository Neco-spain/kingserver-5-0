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
package hellbound.Kanaf;

import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.quest.Quest;

/**
 * @author GKR
 */
public class Kanaf extends Quest
{
	private static final int KANAF = 32346;
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("info"))
		{
			return "32346-0" + getRandom(1, 3) + ".htm";
		}
		
		return null;
	}
	
	public Kanaf(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(KANAF);
		addTalkId(KANAF);
	}
	
	public static void main(String[] args)
	{
		new Kanaf(-1, "Kanaf", "hellbound");
	}
}
