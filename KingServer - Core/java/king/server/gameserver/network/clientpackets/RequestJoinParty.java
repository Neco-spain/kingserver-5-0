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

import king.server.Config;
import king.server.gameserver.model.BlockList;
import king.server.gameserver.model.L2Party;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.PcCondOverride;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.AskJoinParty;
import king.server.gameserver.network.serverpackets.SystemMessage;
import king.server.gameserver.util.BotPunish;

/**
 * sample 29 42 00 00 10 01 00 00 00 format cdd
 * @version $Revision: 1.7.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestJoinParty extends L2GameClientPacket
{
	private static final String _C__42_REQUESTJOINPARTY = "[C] 42 RequestJoinParty";
	
	private String _name;
	private int _itemDistribution;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
		_itemDistribution = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance requestor = getClient().getActiveChar();
		L2PcInstance target = L2World.getInstance().getPlayer(_name);
		
		if (requestor == null)
		{
			return;
		}
		
		if (target == null)
		{
			requestor.sendPacket(SystemMessageId.FIRST_SELECT_USER_TO_INVITE_TO_PARTY);
			return;
		}
		
		if ((target.getClient() == null) || target.getClient().isDetached())
		{
			requestor.sendMessage("Player is in offline mode.");
			return;
		}
		// Check for bot punishment on target
        if(target.isBeingPunished())
        {
        	// Check conditions
        	if(target.getPlayerPunish().canJoinParty() && target.getBotPunishType() == BotPunish.Punish.PARTYBAN)
        	{
        		target.endPunishment();
        	}
        	else
        	{
        		// Inform the player cannot join party
        		requestor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.USER_REPORTED_AND_CANNOT_JOIN_PARTY));
        		return;
        	}
        		
        }
        
        // Check for bot punishment on requestor
        if(requestor.isBeingPunished())
        {
        	// Check conditions
        	if(requestor.getPlayerPunish().canJoinParty() && requestor.getBotPunishType() == BotPunish.Punish.PARTYBAN)
        	{
        		requestor.endPunishment();
        	}
        	else
        	{
        		SystemMessageId msgId = null;
        		switch(requestor.getPlayerPunish().getDuration())
        		{
        			case 3600:
        				msgId = SystemMessageId.REPORTED_60_MINS_WITHOUT_JOIN_PARTY;
        				break;
        			case 7200:
        				msgId = SystemMessageId.REPORTED_120_MINS_WITHOUT_JOIN_PARTY;
        				break;
        			case 10800:
        				msgId = SystemMessageId.REPORTED_180_MINS_WITHOUT_JOIN_PARTY;
        				break;
        				default:
        		}	
        		requestor.sendPacket(SystemMessage.getSystemMessage(msgId));
        		return;
        	}	
        }   

		if (!requestor.canOverrideCond(PcCondOverride.SEE_ALL_PLAYERS) && target.getAppearance().getInvisible())
		{
			requestor.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return;
		}
		
		SystemMessage sm;
		if (target.isInParty())
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ALREADY_IN_PARTY);
			sm.addString(target.getName());
			requestor.sendPacket(sm);
			return;
		}
		
		if (BlockList.isBlocked(target, requestor))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST);
			sm.addCharName(target);
			requestor.sendPacket(sm);
			return;
		}
		
		if (target == requestor)
		{
			requestor.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return;
		}
		
		if (target.isCursedWeaponEquipped() || requestor.isCursedWeaponEquipped())
		{
			requestor.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}
		
		if (target.isInJail() || requestor.isInJail())
		{
			requestor.sendMessage("You cannot invite a player while is in Jail.");
			return;
		}
		
		if (target.isInOlympiadMode() || requestor.isInOlympiadMode())
		{
			if ((target.isInOlympiadMode() != requestor.isInOlympiadMode()) || (target.getOlympiadGameId() != requestor.getOlympiadGameId()) || (target.getOlympiadSide() != requestor.getOlympiadSide()))
			{
				requestor.sendPacket(SystemMessageId.A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS);
				return;
			}
		}
		
		sm = SystemMessage.getSystemMessage(SystemMessageId.C1_INVITED_TO_PARTY);
		sm.addCharName(target);
		requestor.sendPacket(sm);
		
		if (!requestor.isInParty())
		{
			createNewParty(target, requestor);
		}
		else
		{
			if (requestor.getParty().isInDimensionalRift())
			{
				requestor.sendMessage("You cannot invite a player when you are in the Dimensional Rift.");
			}
			else
			{
				addTargetToParty(target, requestor);
			}
		}
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private void addTargetToParty(L2PcInstance target, L2PcInstance requestor)
	{
		final L2Party party = requestor.getParty();
		// summary of ppl already in party and ppl that get invitation
		if (!party.isLeader(requestor))
		{
			requestor.sendPacket(SystemMessageId.ONLY_LEADER_CAN_INVITE);
			return;
		}
		if (party.getMemberCount() >= 9)
		{
			requestor.sendPacket(SystemMessageId.PARTY_FULL);
			return;
		}
		if (party.getPendingInvitation() && !party.isInvitationRequestExpired())
		{
			requestor.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			return;
		}
		
		if (!target.isProcessingRequest())
		{
			requestor.onTransactionRequest(target);
			// in case a leader change has happened, use party's mode
			target.sendPacket(new AskJoinParty(requestor.getName(), party.getLootDistribution()));
			party.setPendingInvitation(true);
			
			if (Config.DEBUG)
			{
				_log.fine("sent out a party invitation to:" + target.getName());
			}
			
		}
		else
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
			sm.addString(target.getName());
			requestor.sendPacket(sm);
			
			if (Config.DEBUG)
			{
				_log.warning(requestor.getName() + " already received a party invitation");
			}
		}
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private void createNewParty(L2PcInstance target, L2PcInstance requestor)
	{
		if (!target.isProcessingRequest())
		{
			requestor.setParty(new L2Party(requestor, _itemDistribution));
			
			requestor.onTransactionRequest(target);
			target.sendPacket(new AskJoinParty(requestor.getName(), _itemDistribution));
			requestor.getParty().setPendingInvitation(true);
			
			if (Config.DEBUG)
			{
				_log.fine("sent out a party invitation to:" + target.getName());
			}
			
		}
		else
		{
			requestor.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			
			if (Config.DEBUG)
			{
				_log.warning(requestor.getName() + " already received a party invitation");
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__42_REQUESTJOINPARTY;
	}
}
