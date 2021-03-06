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
package king.server.gameserver.model.skills.funcs;

import king.server.gameserver.datatables.EnchantHPBonusData;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.stats.Env;
import king.server.gameserver.model.stats.Stats;

/**
 * @author Yamaneko
 */
public class FuncEnchantHp extends Func
{
	public FuncEnchantHp(Stats pStat, int pOrder, Object owner, Lambda lambda)
	{
		super(pStat, pOrder, owner);
	}
	
	@Override
	public void calc(Env env)
	{
		if ((cond != null) && !cond.test(env))
		{
			return;
		}
		
		final L2ItemInstance item = (L2ItemInstance) funcOwner;
		if (item.getEnchantLevel() > 0)
		{
			env.addValue(EnchantHPBonusData.getInstance().getHPBonus(item));
		}
	}
}
