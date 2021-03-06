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
package king.server.gameserver.network.clientpackets;

import king.server.gameserver.instancemanager.FortManager;
import king.server.gameserver.model.entity.Fort;
import king.server.gameserver.network.L2GameClient;
import king.server.gameserver.network.serverpackets.ExShowFortressSiegeInfo;

/**
 * @author KenM
 */
public class RequestFortressSiegeInfo extends L2GameClientPacket
{
	private static final String _C__D0_3F_REQUESTFORTRESSSIEGEINFO = "[C] D0:3F RequestFortressSiegeInfo";
	
	@Override
	protected void readImpl()
	{
		// trigger
	}
	
	@Override
	protected void runImpl()
	{
		L2GameClient client = getClient();
		if (client != null)
		{
			for (Fort fort : FortManager.getInstance().getForts())
			{
				if ((fort != null) && fort.getSiege().getIsInProgress())
				{
					client.sendPacket(new ExShowFortressSiegeInfo(fort));
				}
			}
		}
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
	
	@Override
	public String getType()
	{
		return _C__D0_3F_REQUESTFORTRESSSIEGEINFO;
	}
}
