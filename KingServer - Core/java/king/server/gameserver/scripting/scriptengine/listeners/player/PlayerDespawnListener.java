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
package king.server.gameserver.scripting.scriptengine.listeners.player;

import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.clientpackets.RequestRestart;
import king.server.gameserver.scripting.scriptengine.impl.L2JListener;

/**
 * @author TheOne
 */
public abstract class PlayerDespawnListener extends L2JListener
{
	public PlayerDespawnListener()
	{
		register();
	}
	
	public abstract void onDespawn(L2PcInstance player);
	
	@Override
	public void register()
	{
		L2PcInstance.addDespawnListener(this);
		RequestRestart.addDespawnListener(this);
	}
	
	@Override
	public void unregister()
	{
		L2PcInstance.removeDespawnListener(this);
		RequestRestart.removeDespawnListener(this);
	}
}
