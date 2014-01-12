package king.server.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import king.server.Config;
import king.server.L2DatabaseFactory;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.util.L2FastMap;

public class CharNameTable
{
	private static Logger _log = Logger.getLogger(CharNameTable.class.getName());
	
	private final Map<Integer, String> _chars = new L2FastMap<>();
	private final Map<Integer, Integer> _accessLevels = new L2FastMap<>();
	
	protected CharNameTable()
	{
		if (Config.CACHE_CHAR_NAMES)
		{
			loadAll();
		}
	}
	
	public static CharNameTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public final void addName(L2PcInstance player)
	{
		if (player != null)
		{
			addName(player.getObjectId(), player.getName());
			_accessLevels.put(player.getObjectId(), player.getAccessLevel().getLevel());
		}
	}
	
	private final void addName(int objectId, String name)
	{
		if (name != null)
		{
			if (!name.equals(_chars.get(objectId)))
			{
				_chars.put(objectId, name);
			}
		}
	}
	
	public final void removeName(int objId)
	{
		_chars.remove(objId);
		_accessLevels.remove(objId);
	}
	
	public final int getIdByName(String name)
	{
		if ((name == null) || name.isEmpty())
		{
			return -1;
		}
		
		Iterator<Entry<Integer, String>> it = _chars.entrySet().iterator();
		
		Map.Entry<Integer, String> pair;
		while (it.hasNext())
		{
			pair = it.next();
			if (pair.getValue().equalsIgnoreCase(name))
			{
				return pair.getKey();
			}
		}
		
		if (Config.CACHE_CHAR_NAMES)
		{
			return -1;
		}
		
		int id = -1;
		int accessLevel = 0;
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT charId,accesslevel FROM characters WHERE char_name=?"))
		{
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					id = rs.getInt(1);
					accessLevel = rs.getInt(2);
				}
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Could not check existing char name: " + e.getMessage(), e);
		}
		
		if (id > 0)
		{
			_chars.put(id, name);
			_accessLevels.put(id, accessLevel);
			return id;
		}
		
		return -1; // not found
	}
	
	public final String getNameById(int id)
	{
		if (id <= 0)
		{
			return null;
		}
		
		String name = _chars.get(id);
		if (name != null)
		{
			return name;
		}
		
		if (Config.CACHE_CHAR_NAMES)
		{
			return null;
		}
		
		int accessLevel = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT char_name,accesslevel FROM characters WHERE charId=?"))
		{
			ps.setInt(1, id);
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					name = rset.getString(1);
					accessLevel = rset.getInt(2);
				}
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Could not check existing char id: " + e.getMessage(), e);
		}
		
		if ((name != null) && !name.isEmpty())
		{
			_chars.put(id, name);
			_accessLevels.put(id, accessLevel);
			return name;
		}
		
		return null; // not found
	}
	
	public final int getAccessLevelById(int objectId)
	{
		if (getNameById(objectId) != null)
		{
			return _accessLevels.get(objectId);
		}
		
		return 0;
	}
	
	public synchronized boolean doesCharNameExist(String name)
	{
		boolean result = true;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?"))
		{
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery())
			{
				result = rs.next();
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Could not check existing charname: " + e.getMessage(), e);
		}
		return result;
	}
	
	public int accountCharNumber(String account)
	{
		int number = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(char_name) FROM characters WHERE account_name=?"))
		{
			ps.setString(1, account);
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					number = rset.getInt(1);
				}
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Could not check existing char number: " + e.getMessage(), e);
		}
		return number;
	}
	
	private void loadAll()
	{
		String name;
		int id = -1;
		int accessLevel = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT charId,char_name,accesslevel FROM characters"))
		{
			while (rs.next())
			{
				id = rs.getInt(1);
				name = rs.getString(2);
				accessLevel = rs.getInt(3);
				_chars.put(id, name);
				_accessLevels.put(id, accessLevel);
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Could not load char name: " + e.getMessage(), e);
		}
		_log.info(getClass().getSimpleName() + ": " + _chars.size() + " char names.");
	}
	
	private static class SingletonHolder
	{
		protected static final CharNameTable _instance = new CharNameTable();
	}
}