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
package handlers.actionhandlers;

import king.server.gameserver.handler.AdminCommandHandler;
import king.server.gameserver.handler.IActionHandler;
import king.server.gameserver.handler.IAdminCommandHandler;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.L2Object.InstanceType;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.serverpackets.MyTargetSelected;
import king.server.gameserver.network.serverpackets.ValidateLocation;

public class L2SummonActionShift implements IActionHandler
{
	@Override
	public boolean action(L2PcInstance activeChar, L2Object target, boolean interact)
	{
		if (activeChar.isGM())
		{
			if (activeChar.getTarget() != target)
			{
				// Set the target of the L2PcInstance activeChar
				activeChar.setTarget(target);
				
				// Send a Server->Client packet MyTargetSelected to the L2PcInstance activeChar
				activeChar.sendPacket(new MyTargetSelected(target.getObjectId(), 0));
			}
			
			// Send a Server->Client packet ValidateLocation to correct the L2PcInstance position and heading on the client
			activeChar.sendPacket(new ValidateLocation((L2Character) target));
			
			IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler("admin_summon_info");
			if (ach != null)
			{
				ach.useAdminCommand("admin_summon_info", activeChar);
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.L2Summon;
	}
}