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
package king.server.gameserver.network.communityserver.readpackets;

import java.util.logging.Logger;

import org.netcon.BaseReadPacket;

import king.server.gameserver.datatables.ClanTable;
import king.server.gameserver.model.L2Clan;
import king.server.gameserver.network.communityserver.CommunityServerThread;
import king.server.gameserver.network.communityserver.writepackets.WorldInfo;

/**
 * @authors Forsaiken, Gigiikun
 */
public final class ClanNoticeInfo extends BaseReadPacket
{
	private static Logger _log = Logger.getLogger(ClanNoticeInfo.class.getName());
	private final int _type;
	private final CommunityServerThread _cst;
	
	public ClanNoticeInfo(final byte[] data, int type)
	{
		super(data);
		_cst = null;
		_type = type;
	}
	
	public ClanNoticeInfo(final byte[] data, final CommunityServerThread cst, int type)
	{
		super(data);
		_type = type;
		_cst = cst;
	}
	
	@Override
	public final void run()
	{
		switch (_type)
		{
			case 0:
				int clanId = super.readD();
				L2Clan c = ClanTable.getInstance().getClan(clanId);
				String notice = super.readS();
				c.setNotice(notice);
				boolean noticeEnabled = (super.readC() == 1 ? true : false);
				c.setNoticeEnabled(noticeEnabled);
				break;
			case 1:
				clanId = super.readD();
				c = ClanTable.getInstance().getClan(clanId);
				noticeEnabled = (super.readC() == 1 ? true : false);
				c.setNoticeEnabled(noticeEnabled);
				break;
			case 2:
				clanId = super.readD();
				L2Clan clan = ClanTable.getInstance().getClan(clanId);
				if (clan != null)
				{
					_cst.sendPacket(new WorldInfo(null, clan, WorldInfo.TYPE_SEND_CLAN_NOTICE));
				}
				else
				{
					_log.warning("Can't find clan with id: " + clanId);
				}
				break;
		}
	}
}
