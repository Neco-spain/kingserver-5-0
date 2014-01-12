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
package handlers.effecthandlers;

import king.server.gameserver.model.ShotType;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.effects.EffectTemplate;
import king.server.gameserver.model.effects.L2Effect;
import king.server.gameserver.model.effects.L2EffectType;
import king.server.gameserver.model.items.L2Item;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.stats.Env;
import king.server.gameserver.model.stats.Formulas;
import king.server.gameserver.model.stats.Stats;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.StatusUpdate;
import king.server.gameserver.network.serverpackets.SystemMessage;

/**
 * @author UnAfraid
 */
public class Heal extends L2Effect
{
	public Heal(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.HEAL;
	}
	
	@Override
	public boolean onStart()
	{
		L2Character target = getEffected();
		L2Character activeChar = getEffector();
		if ((target == null) || target.isDead() || target.isDoor())
		{
			return false;
		}
		
		double amount = calc();
		double staticShotBonus = 0;
		int mAtkMul = 1;
		boolean sps = getSkill().isMagic() && activeChar.isChargedShot(ShotType.SPIRITSHOTS);
		boolean bss = getSkill().isMagic() && activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		
		if (((sps || bss) && (activeChar.isPlayer() && activeChar.getActingPlayer().isMageClass())) || activeChar.isSummon())
		{
			staticShotBonus = getSkill().getMpConsume(); // static bonus for spiritshots
			
			if (bss)
			{
				mAtkMul = 4;
				staticShotBonus *= 2.4; // static bonus for blessed spiritshots
			}
			else
			{
				mAtkMul = 2;
			}
		}
		else if ((sps || bss) && activeChar.isNpc())
		{
			staticShotBonus = 2.4 * getSkill().getMpConsume(); // always blessed spiritshots
			mAtkMul = 4;
		}
		else
		{
			// no static bonus
			// grade dynamic bonus
			final L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
			if (weaponInst != null)
			{
				switch (weaponInst.getItem().getItemGrade())
				{
					case L2Item.CRYSTAL_S84:
						mAtkMul = 4;
						break;
					case L2Item.CRYSTAL_S80:
						mAtkMul = 2;
						break;
				}
			}
			// shot dynamic bonus
			if (bss)
			{
				mAtkMul *= 4; // 16x/8x/4x s84/s80/other
			}
			else
			{
				mAtkMul += 1; // 5x/3x/1x s84/s80/other
			}
		}
		
		if (!getSkill().isStaticHeal())
		{
			amount += staticShotBonus + Math.sqrt(mAtkMul * activeChar.getMAtk(activeChar, null));
			amount *= target.calcStat(Stats.HEAL_EFFECTIVNESS, 100, null, null) / 100;
			// Healer proficiency (since CT1)
			amount *= activeChar.calcStat(Stats.HEAL_PROFICIENCY, 100, null, null) / 100;
			// Extra bonus (since CT1.5)
			if (!getSkill().isStatic())
			{
				amount += target.calcStat(Stats.HEAL_STATIC_BONUS, 0, null, null);
			}
			
			// Heal critic, since CT2.3 Gracia Final
			if (!getSkill().isStatic() && Formulas.calcMCrit(activeChar.getMCriticalHit(target, getSkill())))
			{
				amount *= 3;
			}
		}
		
		amount = Math.min(amount, target.getMaxRecoverableHp() - target.getCurrentHp());
		
		// Prevent negative amounts
		if (amount < 0)
		{
			amount = 0;
		}
		
		target.setCurrentHp(amount + target.getCurrentHp());
		StatusUpdate su = new StatusUpdate(target);
		su.addAttribute(StatusUpdate.CUR_HP, (int) target.getCurrentHp());
		target.sendPacket(su);
		
		if (target.isPlayer())
		{
			if (getSkill().getId() == 4051)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.REJUVENATING_HP);
				target.sendPacket(sm);
			}
			else
			{
				if (activeChar.isPlayer() && (activeChar != target))
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_C1);
					sm.addString(activeChar.getName());
					sm.addNumber((int) amount);
					target.sendPacket(sm);
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED);
					sm.addNumber((int) amount);
					target.sendPacket(sm);
				}
			}
		}
		activeChar.setChargedShot(bss ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS, false);
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}
