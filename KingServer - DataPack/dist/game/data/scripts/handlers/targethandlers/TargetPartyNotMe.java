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
import king.server.gameserver.model.actor.L2Summon;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.targets.L2TargetType;
import king.server.gameserver.util.Util;

/**
 * @author UnAfraid
 */
public class TargetPartyNotMe implements ITargetTypeHandler
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
		
		L2PcInstance player = null;
		
		if (activeChar.isSummon())
		{
			player = ((L2Summon) activeChar).getOwner();
			targetList.add(player);
		}
		else if (activeChar.isPlayer())
		{
			player = activeChar.getActingPlayer();
			if (activeChar.getSummon() != null)
			{
				targetList.add(activeChar.getSummon());
			}
		}
		
		if (activeChar.getParty() != null)
		{
			List<L2PcInstance> partyList = activeChar.getParty().getMembers();
			
			for (L2PcInstance partyMember : partyList)
			{
				if (partyMember == null)
				{
					continue;
				}
				else if (partyMember == player)
				{
					continue;
				}
				else if (!partyMember.isDead() && Util.checkIfInRange(skill.getSkillRadius(), activeChar, partyMember, true))
				{
					if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
					{
						break;
					}
					
					targetList.add(partyMember);
					
					if ((partyMember.getSummon() != null) && !partyMember.getSummon().isDead())
					{
						targetList.add(partyMember.getSummon());
					}
				}
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_PARTY_NOTME;
	}
}
