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
package king.server.gameserver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastList;
import javolution.util.FastMap;

import king.server.L2DatabaseFactory;
import king.server.gameserver.datatables.UITable;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.ActionKey;

/**
 * @author mrTJO
 */
public class L2UIKeysSettings
{
	protected static final Logger _log = Logger.getLogger(L2UIKeysSettings.class.getName());
	
	private final L2PcInstance _player;
	
	Map<Integer, List<ActionKey>> _storedKeys;
	Map<Integer, List<Integer>> _storedCategories;
	
	boolean _saved = true;
	
	public L2UIKeysSettings(L2PcInstance player)
	{
		_player = player;
		loadFromDB();
	}
	
	public void storeAll(Map<Integer, List<Integer>> catMap, Map<Integer, List<ActionKey>> keyMap)
	{
		_saved = false;
		_storedCategories = catMap;
		_storedKeys = keyMap;
	}
	
	public void storeCategories(Map<Integer, List<Integer>> catMap)
	{
		_saved = false;
		_storedCategories = catMap;
	}
	
	public Map<Integer, List<Integer>> getCategories()
	{
		return _storedCategories;
	}
	
	public void storeKeys(Map<Integer, List<ActionKey>> keyMap)
	{
		_saved = false;
		_storedKeys = keyMap;
	}
	
	public Map<Integer, List<ActionKey>> getKeys()
	{
		return _storedKeys;
	}
	
	public void loadFromDB()
	{
		getCatsFromDB();
		getKeysFromDB();
	}
	
	/**
	 * Save Categories and Mapped Keys into GameServer DataBase
	 */
	public void saveInDB()
	{
		String query;
		int playerId = _player.getObjectId();
		
		if (_saved)
		{
			return;
		}
		
		query = "REPLACE INTO character_ui_categories (`charId`, `catId`, `order`, `cmdId`) VALUES ";
		for (int category : _storedCategories.keySet())
		{
			int order = 0;
			for (int key : _storedCategories.get(category))
			{
				query += "(" + playerId + ", " + category + ", " + (order++) + ", " + key + "),";
			}
		}
		query = query.substring(0, query.length() - 1) + "; ";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(query))
		{
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception: saveInDB(): " + e.getMessage(), e);
		}
		
		query = "REPLACE INTO character_ui_actions (`charId`, `cat`, `order`, `cmd`, `key`, `tgKey1`, `tgKey2`, `show`) VALUES";
		for (List<ActionKey> keyLst : _storedKeys.values())
		{
			int order = 0;
			for (ActionKey key : keyLst)
			{
				query += key.getSqlSaveString(playerId, order++) + ",";
			}
		}
		query = query.substring(0, query.length() - 1) + ";";
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(query))
		{
			statement.execute();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception: saveInDB(): " + e.getMessage(), e);
		}
		_saved = true;
	}
	
	public void getCatsFromDB()
	{
		
		if (_storedCategories != null)
		{
			return;
		}
		
		_storedCategories = new FastMap<>();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM character_ui_categories WHERE `charId` = ? ORDER BY `catId`, `order`");
			stmt.setInt(1, _player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				int cat = rs.getInt("catId");
				int cmd = rs.getInt("cmdId");
				insertCategory(cat, cmd);
			}
			stmt.close();
			rs.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception: getCatsFromDB(): " + e.getMessage(), e);
		}
		
		if (_storedCategories.size() < 1)
		{
			_storedCategories = UITable.getInstance().getCategories();
		}
	}
	
	public void getKeysFromDB()
	{
		if (_storedKeys != null)
		{
			return;
		}
		
		_storedKeys = new FastMap<>();
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM character_ui_actions WHERE `charId` = ? ORDER BY `cat`, `order`");
			stmt.setInt(1, _player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next())
			{
				int cat = rs.getInt("cat");
				int cmd = rs.getInt("cmd");
				int key = rs.getInt("key");
				int tgKey1 = rs.getInt("tgKey1");
				int tgKey2 = rs.getInt("tgKey2");
				int show = rs.getInt("show");
				insertKey(cat, cmd, key, tgKey1, tgKey2, show);
			}
			stmt.close();
			rs.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception: getKeysFromDB(): " + e.getMessage(), e);
		}
		
		if (_storedKeys.size() < 1)
		{
			_storedKeys = UITable.getInstance().getKeys();
		}
	}
	
	public void insertCategory(int cat, int cmd)
	{
		if (_storedCategories.containsKey(cat))
		{
			_storedCategories.get(cat).add(cmd);
		}
		else
		{
			List<Integer> tmp = new FastList<>();
			tmp.add(cmd);
			_storedCategories.put(cat, tmp);
		}
	}
	
	public void insertKey(int cat, int cmdId, int key, int tgKey1, int tgKey2, int show)
	{
		ActionKey tmk = new ActionKey(cat, cmdId, key, tgKey1, tgKey2, show);
		if (_storedKeys.containsKey(cat))
		{
			_storedKeys.get(cat).add(tmk);
		}
		else
		{
			List<ActionKey> tmp = new FastList<>();
			tmp.add(tmk);
			_storedKeys.put(cat, tmp);
		}
	}
	
	public boolean isSaved()
	{
		return _saved;
	}
}
