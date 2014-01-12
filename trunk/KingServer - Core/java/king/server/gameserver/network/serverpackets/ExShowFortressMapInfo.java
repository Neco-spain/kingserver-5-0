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

import king.server.gameserver.instancemanager.FortSiegeManager;
import king.server.gameserver.instancemanager.FortSiegeManager.SiegeSpawn;
import king.server.gameserver.model.L2Spawn;
import king.server.gameserver.model.entity.Fort;

/**
 * @author KenM
 */
public class ExShowFortressMapInfo extends L2GameServerPacket
{
	private final Fort _fortress;
	
	public ExShowFortressMapInfo(Fort fortress)
	{
		_fortress = fortress;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x7D);
		
		writeD(_fortress.getFortId());
		writeD(_fortress.getSiege().getIsInProgress() ? 1 : 0); // fortress siege status
		writeD(_fortress.getFortSize()); // barracks count
		
		FastList<SiegeSpawn> commanders = FortSiegeManager.getInstance().getCommanderSpawnList(_fortress.getFortId());
		if ((commanders != null) && (commanders.size() != 0) && _fortress.getSiege().getIsInProgress())
		{
			switch (commanders.size())
			{
				case 3:
				{
					for (SiegeSpawn spawn : commanders)
					{
						if (isSpawned(spawn.getNpcId()))
						{
							writeD(0);
						}
						else
						{
							writeD(1);
						}
					}
					break;
				}
				case 4: // TODO: change 4 to 5 once control room supported
				{
					int count = 0;
					for (SiegeSpawn spawn : commanders)
					{
						count++;
						if (count == 4)
						{
							writeD(1); // TODO: control room emulated
						}
						if (isSpawned(spawn.getNpcId()))
						{
							writeD(0);
						}
						else
						{
							writeD(1);
						}
					}
					break;
				}
			}
		}
		else
		{
			for (int i = 0; i < _fortress.getFortSize(); i++)
			{
				writeD(0);
			}
		}
	}
	
	/**
	 * @param npcId
	 * @return
	 */
	private boolean isSpawned(int npcId)
	{
		boolean ret = false;
		for (L2Spawn spawn : _fortress.getSiege().getCommanders())
		{
			if (spawn.getNpcid() == npcId)
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
}
