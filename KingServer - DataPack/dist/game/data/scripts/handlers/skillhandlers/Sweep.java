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
import king.server.gameserver.model.actor.L2Attackable;
import king.server.gameserver.model.actor.L2Attackable.RewardItem;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.skills.L2SkillType;
import king.server.gameserver.model.skills.l2skills.L2SkillSweeper;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.StatusUpdate;
import king.server.gameserver.network.serverpackets.SystemMessage;

/**
 * @author _drunk_, Zoey76
 */
public class Sweep implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.SWEEP
	};
	private static final int maxSweepTime = 15000;
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		final L2PcInstance player = activeChar.getActingPlayer();
		
		RewardItem[] items = null;
		L2Attackable target;
		L2SkillSweeper sweep;
		SystemMessage sm;
		boolean canSweep;
		boolean isSweeping;
		for (L2Object tgt : targets)
		{
			if (!tgt.isL2Attackable())
			{
				continue;
			}
			target = (L2Attackable) tgt;
			
			canSweep = target.checkSpoilOwner(player, true);
			canSweep &= target.checkCorpseTime(player, maxSweepTime, true);
			canSweep &= player.getInventory().checkInventorySlotsAndWeight(target.getSpoilLootItems(), true, false);
			
			if (canSweep)
			{
				isSweeping = false;
				synchronized (target)
				{
					if (target.isSweepActive())
					{
						items = target.takeSweep();
						isSweeping = true;
					}
				}
				if (isSweeping)
				{
					if ((items == null) || (items.length == 0))
					{
						continue;
					}
					for (RewardItem ritem : items)
					{
						if (player.isInParty())
						{
							player.getParty().distributeItem(player, ritem, true, target);
						}
						else
						{
							player.addItem("Sweep", ritem.getItemId(), ritem.getCount(), player, true);
						}
					}
				}
			}
			target.endDecayTask();
			
			sweep = (L2SkillSweeper) skill;
			if (sweep.getAbsorbAbs() != -1)
			{
				int restored = 0;
				double absorb = 0;
				final StatusUpdate su = new StatusUpdate(activeChar);
				final int abs = sweep.getAbsorbAbs();
				if (sweep.isAbsorbHp())
				{
					absorb = ((activeChar.getCurrentHp() + abs) > activeChar.getMaxHp() ? activeChar.getMaxHp() : (activeChar.getCurrentHp() + abs));
					restored = (int) (absorb - activeChar.getCurrentHp());
					activeChar.setCurrentHp(absorb);
					
					su.addAttribute(StatusUpdate.CUR_HP, (int) absorb);
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED);
				}
				else
				{
					absorb = ((activeChar.getCurrentMp() + abs) > activeChar.getMaxMp() ? activeChar.getMaxMp() : (activeChar.getCurrentMp() + abs));
					restored = (int) (absorb - activeChar.getCurrentMp());
					activeChar.setCurrentMp(absorb);
					
					su.addAttribute(StatusUpdate.CUR_MP, (int) absorb);
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_MP_RESTORED);
				}
				activeChar.sendPacket(su);
				sm.addNumber(restored);
				activeChar.sendPacket(sm);
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}
