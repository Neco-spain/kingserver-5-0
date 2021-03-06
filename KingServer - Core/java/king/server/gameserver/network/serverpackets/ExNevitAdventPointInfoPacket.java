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

import king.server.gameserver.datatables.AdventBonus;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author mochitto
 */
public class ExNevitAdventPointInfoPacket extends L2GameServerPacket
{
	private final int _points;
	
	public ExNevitAdventPointInfoPacket(L2PcInstance player)
	{
		_points = AdventBonus.getInstance().getAdventPoints(player.getObjectId());
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xDF);
		writeD(_points); // 72 = 1%, max 7200 = 100%
	}
}
