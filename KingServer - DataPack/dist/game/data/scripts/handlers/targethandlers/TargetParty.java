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
package handlers.targethandlers;

import java.util.List;

import javolution.util.FastList;

import king.server.gameserver.handler.ITargetTypeHandler;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.targets.L2TargetType;

/**
 * @author UnAfraid
 */
public class TargetParty implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		if (onlyFirst)
		{
			return new L2Character[]
			{
				activeChar
			};
		}
		
		targetList.add(activeChar);
		
		final int radius = skill.getSkillRadius();
		
		L2PcInstance player = activeChar.getActingPlayer();
		if (activeChar.isSummon())
		{
			if (L2Skill.addCharacter(activeChar, player, radius, false))
			{
				targetList.add(player);
			}
		}
		else if (activeChar.isPlayer())
		{
			if (L2Skill.addSummon(activeChar, player, radius, false))
			{
				targetList.add(player.getSummon());
			}
		}
		
		if (activeChar.isInParty())
		{
			// Get a list of Party Members
			for (L2PcInstance partyMember : activeChar.getParty().getMembers())
			{
				if ((partyMember == null) || (partyMember == player))
				{
					continue;
				}
				
				if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
				{
					break;
				}
				
				if (L2Skill.addCharacter(activeChar, partyMember, radius, false))
				{
					targetList.add(partyMember);
				}
				
				if (L2Skill.addSummon(activeChar, partyMember, radius, false))
				{
					targetList.add(partyMember.getSummon());
				}
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_PARTY;
	}
}