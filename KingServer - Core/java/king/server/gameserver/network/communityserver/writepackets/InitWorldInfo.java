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
package king.server.gameserver.network.communityserver.writepackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastList;

import org.netcon.BaseWritePacket;

import king.server.L2DatabaseFactory;
import king.server.gameserver.datatables.ClanTable;
import king.server.gameserver.instancemanager.CastleManager;
import king.server.gameserver.model.L2Clan;
import king.server.gameserver.model.StatsSet;
import king.server.gameserver.model.entity.Castle;

/**
 * @authors Forsaiken, Gigiikun
 */
public final class InitWorldInfo extends BaseWritePacket
{
	public static final byte TYPE_INFO = 0;
	public static final byte TYPE_PLAYER = 1;
	public static final byte TYPE_CLAN = 2;
	public static final byte TYPE_CASTLE = 3;
	private static Logger _log = Logger.getLogger(InitWorldInfo.class.getName());
	
	public InitWorldInfo(StatsSet[] players, L2Clan[] clans, final byte type, int info)
	{
		super.writeC(0x01);
		super.writeC(0x00);
		super.writeD(type);
		
		int i;
		switch (type)
		{
			case TYPE_INFO:
				super.writeD(info);
				break;
			case TYPE_CLAN:
				super.writeD(info != -1 ? info : clans.length);
				i = 0;
				for (L2Clan c : clans)
				{
					if (c == null)
					{
						continue;
					}
					if (i++ == info)
					{
						break;
					}
					super.writeD(c.getClanId());
					super.writeS(c.getName());
					super.writeD(c.getLevel());
					if (c.getLeader() == null)
					{
						writeD(0);
						writeS("");
						_log.info("Clan Id: " + c.getClanId() + " has null clan leader!");
					}
					else
					{
						super.writeD(c.getLeader().getObjectId());
						super.writeS(c.getLeader().getName());
					}
					super.writeD(c.getMembersCount());
					super.writeC((c.isNoticeEnabled() ? 1 : 0));
					// Alliance info:
					super.writeS(c.getAllyName());
					final List<L2Clan> clanAllies = ClanTable.getInstance().getClanAllies(c.getAllyId());
					super.writeD(clanAllies.size());
					for (L2Clan allies : clanAllies)
					{
						super.writeD(allies.getClanId());
					}
				}
				break;
			case TYPE_PLAYER:
				super.writeD(info != -1 ? info : players.length);
				i = 0;
				for (StatsSet p : players)
				{
					if (i++ == info)
					{
						break;
					}
					super.writeD(p.getInteger("charId"));
					super.writeS(p.getString("char_name"));
					super.writeS(p.getString("account_name"));
					super.writeD(p.getInteger("level"));
					super.writeD(p.getInteger("clanid"));
					super.writeD(p.getInteger("accesslevel"));
					super.writeC(p.getInteger("online"));
					FastList<Integer> list = FastList.newInstance();
					try (Connection con = L2DatabaseFactory.getInstance().getConnection();
						PreparedStatement statement = con.prepareStatement("SELECT friendId FROM character_friends WHERE charId=?"))
					{
						statement.setInt(1, p.getInteger("charId"));
						try (ResultSet rset = statement.executeQuery())
						{
							while (rset.next())
							{
								list.add(rset.getInt("friendId"));
							}
						}
					}
					catch (Exception e)
					{
						_log.log(Level.SEVERE, "Error restoring friend data for Community Board transfer.", e);
					}
					super.writeD(list.size());
					for (int j : list)
					{
						super.writeD(j);
					}
					FastList.recycle(list);
				}
				break;
			case TYPE_CASTLE:
				List<Castle> castles = CastleManager.getInstance().getCastles();
				writeD(castles.size());
				_log.info("Transfering " + castles.size() + " castles data to CB server.");
				for (Castle castle : castles)
				{
					writeD(castle.getCastleId());
					writeS(castle.getName());
					writeD(castle.getOwnerId());
					writeD(castle.getTaxPercent());
					writeD((int) (castle.getSiege().getSiegeDate().getTimeInMillis() / 1000));
				}
				break;
		}
	}
}
