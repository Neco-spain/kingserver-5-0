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
package handlers.bypasshandlers;

import java.util.StringTokenizer;
import java.util.logging.Level;

import king.server.gameserver.handler.IBypassHandler;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.holders.SkillHolder;
import king.server.gameserver.model.zone.ZoneId;

/**
 * @author Xaras2
 */
public class ArenaBuff implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"ArenaBuffs", 
		"HPRecovery",
		"CPRecovery",
		"MPRecovery"
	};
	
	private final int[][] BUFFS =
	{
		{ // Fighter Buffs
			6803,
			6804,
			6808,
			6809,
			6811,
			6812
		},
		{ // Mage Buffs
			6804,
			6805,
			6806,
			6807,
			6812
		}
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		final L2Npc npc = (L2Npc) target;
		final StringTokenizer st = new StringTokenizer(command);
		try
		{
			String cmd = st.nextToken();
			
			if (cmd.equalsIgnoreCase(COMMANDS[0]))
			{	
				if (!activeChar.reduceAdena("ArenaBuffs", 2000, activeChar.getLastFolkNPC(), true))
				{
					return false;
				}
				
				for (int skillId : BUFFS[activeChar.isMageClass() ? 1 : 0])
				{
					SkillHolder skill = new SkillHolder(skillId, 1);
					
					if (skill.getSkill() != null)
					{
						npc.setTarget(activeChar);
						npc.doCast(skill.getSkill());
					}
				}
				return true;
			}
			else if (cmd.equalsIgnoreCase(COMMANDS[1]))
			{
				if (activeChar.isInsideZone(ZoneId.PVP)) // Cannot be used while inside the pvp zone
				{
					return false;
				}
				else if (!activeChar.reduceAdena("RestoreHP", 1000, activeChar.getLastFolkNPC(), true))
				{
					return false;
				}
				
				SkillHolder skill = new SkillHolder(6817, 1);
				if (skill.getSkill() != null)
				{
					npc.setTarget(activeChar);
					npc.doCast(skill.getSkill());
				}
				return true;
			}
			else if (cmd.equalsIgnoreCase(COMMANDS[2]))
			{
				if (activeChar.isInsideZone(ZoneId.PVP)) // Cannot be used while inside the pvp zone
				{
					return false;
				}
				else if (!activeChar.reduceAdena("RestoreCP", 1000, activeChar.getLastFolkNPC(), true))
				{
					return false;
				}
				
				SkillHolder skill = new SkillHolder(4380, 1);
				if (skill.getSkill() != null)
				{
					npc.setTarget(activeChar);
					npc.doCast(skill.getSkill());
				}
				return true;
			}
			else if (cmd.equalsIgnoreCase(COMMANDS[3]))
			{
				if (activeChar.isInsideZone(ZoneId.PVP)) // Cannot be used while inside the pvp zone
				{
					return false;
				}
				
				if (!activeChar.reduceAdena("RestoreMP", 1000, activeChar.getLastFolkNPC(), true))
				{
					return false;
				}
				
				activeChar.setCurrentMp(activeChar.getMaxMp());
				return true;
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}
