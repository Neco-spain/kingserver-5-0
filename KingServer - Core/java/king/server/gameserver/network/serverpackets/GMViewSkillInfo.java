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

import java.util.Collection;

import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.skills.L2Skill;

public class GMViewSkillInfo extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	private final Collection<L2Skill> _skills;
	
	public GMViewSkillInfo(L2PcInstance cha)
	{
		_activeChar = cha;
		_skills = _activeChar.getAllSkills();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x97);
		writeS(_activeChar.getName());
		writeD(_skills.size());
		
		boolean isDisabled = (_activeChar.getClan() != null) ? (_activeChar.getClan().getReputationScore() < 0) : false;
		
		for (L2Skill skill : _skills)
		{
			writeD(skill.isPassive() ? 1 : 0);
			writeD(skill.getLevel());
			writeD(skill.getDisplayId());
			writeC(isDisabled && skill.isClanSkill() ? 1 : 0);
			writeC(SkillTable.getInstance().isEnchantable(skill.getDisplayId()) ? 1 : 0);
		}
	}
}