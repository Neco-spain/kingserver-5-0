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
package king.server.gameserver.model.skills.l2skills;

import king.server.gameserver.datatables.NpcTable;
import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.idfactory.IdFactory;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.L2Spawn;
import king.server.gameserver.model.StatsSet;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Trap;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.actor.instance.L2TrapInstance;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.gameserver.model.skills.L2Skill;

public class L2SkillTrap extends L2SkillSummon
{
	private int _triggerSkillId = 0;
	private int _triggerSkillLvl = 0;
	protected L2Spawn _trapSpawn;
	
	/**
	 * @param set
	 */
	public L2SkillTrap(StatsSet set)
	{
		super(set);
		_triggerSkillId = set.getInteger("triggerSkillId");
		_triggerSkillLvl = set.getInteger("triggerSkillLvl");
	}
	
	public int getTriggerSkillId()
	{
		return _triggerSkillId;
	}
	
	@Override
	public void useSkill(L2Character caster, L2Object[] targets)
	{
		if (caster.isAlikeDead() || !caster.isPlayer())
		{
			return;
		}
		
		if (getNpcId() == 0)
		{
			return;
		}
		
		L2PcInstance activeChar = caster.getActingPlayer();
		
		if (activeChar.inObserverMode())
		{
			return;
		}
		
		if (activeChar.isMounted())
		{
			return;
		}
		
		if ((_triggerSkillId == 0) || (_triggerSkillLvl == 0))
		{
			return;
		}
		
		L2Trap trap = activeChar.getTrap();
		if (trap != null)
		{
			trap.unSummon();
		}
		
		L2Skill skill = SkillTable.getInstance().getInfo(_triggerSkillId, _triggerSkillLvl);
		
		if (skill == null)
		{
			return;
		}
		
		L2NpcTemplate TrapTemplate = NpcTable.getInstance().getTemplate(getNpcId());
		trap = new L2TrapInstance(IdFactory.getInstance().getNextId(), TrapTemplate, activeChar, getTotalLifeTime(), skill);
		trap.setCurrentHp(trap.getMaxHp());
		trap.setCurrentMp(trap.getMaxMp());
		trap.setIsInvul(true);
		trap.setHeading(activeChar.getHeading());
		activeChar.setTrap(trap);
		trap.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
	}
}
