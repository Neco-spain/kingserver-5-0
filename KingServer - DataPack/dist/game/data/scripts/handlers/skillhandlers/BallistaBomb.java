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

import king.server.gameserver.handler.ISkillHandler;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2FortBallistaInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.L2SkillType;
import king.server.util.Rnd;

public class BallistaBomb implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.BALLISTA
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		L2Object[] targetList = skill.getTargetList(activeChar);
		
		if ((targetList == null) || (targetList.length == 0))
		{
			return;
		}
		L2Character target = (L2Character) targetList[0];
		if (target instanceof L2FortBallistaInstance)
		{
			if (Rnd.get(3) == 0)
			{
				target.setIsInvul(false);
				target.reduceCurrentHp(target.getMaxHp() + 1, activeChar, skill);
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}