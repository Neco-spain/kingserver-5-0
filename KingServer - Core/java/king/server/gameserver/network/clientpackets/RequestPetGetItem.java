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

import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.instancemanager.FortSiegeManager;
import king.server.gameserver.instancemanager.MercTicketManager;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.instance.L2PetInstance;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.network.serverpackets.ActionFailed;

public final class RequestPetGetItem extends L2GameClientPacket
{
	private static final String _C__98_REQUESTPETGETITEM = "[C] 98 RequestPetGetItem";
	
	private int _objectId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2World world = L2World.getInstance();
		L2ItemInstance item = (L2ItemInstance) world.findObject(_objectId);
		if ((item == null) || (getActiveChar() == null) || !getActiveChar().hasSummon() || !getActiveChar().getSummon().isPet())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final int castleId = MercTicketManager.getInstance().getTicketCastleId(item.getItemId());
		if (castleId > 0)
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (FortSiegeManager.getInstance().isCombat(item.getItemId()))
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final L2PetInstance pet = (L2PetInstance) getClient().getActiveChar().getSummon();
		if (pet.isDead() || pet.isOutOfControl())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		pet.getAI().setIntention(CtrlIntention.AI_INTENTION_PICK_UP, item);
	}
	
	@Override
	public String getType()
	{
		return _C__98_REQUESTPETGETITEM;
	}
}
