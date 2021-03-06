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
package handlers.skillhandlers;

import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.handler.ISkillHandler;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.L2SkillType;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.SystemMessage;

/**
 * @author nBd
 */
public class Soul implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.CHARGESOUL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer() || activeChar.isAlikeDead())
		{
			return;
		}
		
		L2PcInstance player = activeChar.getActingPlayer();
		
		int level = player.getSkillLevel(467);
		if (level > 0)
		{
			L2Skill soulmastery = SkillTable.getInstance().getInfo(467, level);
			
			if (soulmastery != null)
			{
				if (player.getSouls() < soulmastery.getNumSouls())
				{
					int count = 0;
					
					if ((player.getSouls() + skill.getNumSouls()) <= soulmastery.getNumSouls())
					{
						count = skill.getNumSouls();
					}
					else
					{
						count = soulmastery.getNumSouls() - player.getSouls();
					}
					
					player.increaseSouls(count);
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SOUL_CANNOT_BE_INCREASED_ANYMORE);
					player.sendPacket(sm);
					return;
				}
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}
