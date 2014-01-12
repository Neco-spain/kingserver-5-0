/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.network.serverpackets;

import javolution.util.FastList;

import king.server.gameserver.model.base.AcquireSkillType;

public final class AcquireSkillList extends L2GameServerPacket
{
	private final FastList<Skill> _skills;
	private final AcquireSkillType _skillType;
	
	/**
	 * Private class containing learning skill information.
	 */
	private static class Skill
	{
		public int id;
		public int nextLevel;
		public int maxLevel;
		public int spCost;
		public int requirements;
		
		public Skill(int pId, int pNextLevel, int pMaxLevel, int pSpCost, int pRequirements)
		{
			id = pId;
			nextLevel = pNextLevel;
			maxLevel = pMaxLevel;
			spCost = pSpCost;
			requirements = pRequirements;
		}
	}
	
	public AcquireSkillList(AcquireSkillType type)
	{
		_skillType = type;
		_skills = new FastList<>();
	}
	
	public void addSkill(int id, int nextLevel, int maxLevel, int spCost, int requirements)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, spCost, requirements));
	}
	
	@Override
	protected void writeImpl()
	{
		if (_skills == null)
		{
			return;
		}
		
		writeC(0x90);
		writeD(_skillType.ordinal());
		writeD(_skills.size());
		
		for (Skill temp : _skills)
		{
			writeD(temp.id);
			writeD(temp.nextLevel);
			writeD(temp.maxLevel);
			writeD(temp.spCost);
			writeD(temp.requirements);
			if (_skillType == AcquireSkillType.SubPledge)
			{
				writeD(0); // TODO: ?
			}
		}
	}
}