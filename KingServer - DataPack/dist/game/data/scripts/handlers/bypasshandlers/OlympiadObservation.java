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
package handlers.bypasshandlers;

import java.util.logging.Level;

import king.server.gameserver.handler.IBypassHandler;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2OlympiadManagerInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.TvTEvent;
import king.server.gameserver.model.olympiad.Olympiad;
import king.server.gameserver.model.olympiad.OlympiadGameManager;
import king.server.gameserver.model.olympiad.OlympiadGameTask;
import king.server.gameserver.model.olympiad.OlympiadManager;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.ExOlympiadMatchList;

/**
 * @author DS
 */
public class OlympiadObservation implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"watchmatch",
		"arenachange"
	};
	
	@Override
	public final boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		try
		{
			final L2Npc olymanager = activeChar.getLastFolkNPC();
			
			if (command.startsWith(COMMANDS[0])) // list
			{
				activeChar.sendPacket(new ExOlympiadMatchList());
			}
			else
			{
				if ((olymanager == null) || !(olymanager instanceof L2OlympiadManagerInstance))
				{
					return false;
				}
				
				if (!activeChar.inObserverMode() && !activeChar.isInsideRadius(olymanager, 300, false, false))
				{
					return false;
				}
				
				if (OlympiadManager.getInstance().isRegisteredInComp(activeChar))
				{
					activeChar.sendPacket(SystemMessageId.WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME);
					return false;
				}
				
				if (!Olympiad.getInstance().inCompPeriod())
				{
					activeChar.sendPacket(SystemMessageId.THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS);
					return false;
				}
				
				if (!TvTEvent.isInactive() && TvTEvent.isPlayerParticipant(activeChar.getObjectId()))
				{
					activeChar.sendMessage("You can not observe games while registered for TvT");
					return false;
				}
				
				final int arenaId = Integer.parseInt(command.substring(12).trim());
				final OlympiadGameTask nextArena = OlympiadGameManager.getInstance().getOlympiadTask(arenaId);
				if (nextArena != null)
				{
					activeChar.enterOlympiadObserverMode(nextArena.getZone().getSpawns().get(0), arenaId);
					activeChar.setInstanceId(OlympiadGameManager.getInstance().getOlympiadTask(arenaId).getZone().getInstanceId());
				}
			}
			return true;
			
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	@Override
	public final String[] getBypassList()
	{
		return COMMANDS;
	}
}
