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
package handlers.chathandlers;

import king.server.Config;
import king.server.gameserver.handler.IChatHandler;
import king.server.gameserver.model.PartyMatchRoom;
import king.server.gameserver.model.PartyMatchRoomList;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.CreatureSay;
import king.server.gameserver.util.Util;

/**
 * A chat handler
 * @author Gnacik
 */
public class ChatPartyMatchRoom implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		14
	};
	
	/**
	 * Handle chat type 'partymatchroom'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text)
	{
		if (activeChar.isInPartyMatchRoom())
		{
			PartyMatchRoom _room = PartyMatchRoomList.getInstance().getPlayerRoom(activeChar);
			if (_room != null)
			{
				if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
				{
					activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
					return;
				}
				
				CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
				for (L2PcInstance _member : _room.getPartyMembers())
				{
					_member.sendPacket(cs);
				}
			}
		}
	}
	
	/**
	 * Returns the chat types registered to this handler
	 */
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
	
	public static void main(String[] args)
	{
		new ChatPartyMatchRoom();
	}
}