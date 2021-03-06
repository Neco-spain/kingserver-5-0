package king.server.gameserver.datatables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import king.server.Config;
import king.server.L2DatabaseFactory;
import king.server.gameserver.model.L2TeleportLocation;

public class TeleportLocationTable
{
	private static Logger _log = Logger.getLogger(TeleportLocationTable.class.getName());
	
	private final Map<Integer, L2TeleportLocation> _teleports = new HashMap<>();
	
	protected TeleportLocationTable()
	{
		reloadAll();
	}
	
	public void reloadAll()
	{
		_teleports.clear();
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT id, loc_x, loc_y, loc_z, price, fornoble, itemId FROM teleport"))
		{
			L2TeleportLocation teleport;
			while (rs.next())
			{
				teleport = new L2TeleportLocation();
				
				teleport.setTeleId(rs.getInt("id"));
				teleport.setLocX(rs.getInt("loc_x"));
				teleport.setLocY(rs.getInt("loc_y"));
				teleport.setLocZ(rs.getInt("loc_z"));
				teleport.setPrice(rs.getInt("price"));
				teleport.setIsForNoble(rs.getInt("fornoble") == 1);
				teleport.setItemId(rs.getInt("itemId"));
				
				_teleports.put(teleport.getTeleId(), teleport);
			}
			_log.info(getClass().getSimpleName() + ": " + _teleports.size() + " Teleport Location Templates.");
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, getClass().getSimpleName() + ": Error loading Teleport Table.", e);
		}
		
		if (Config.CUSTOM_TELEPORT_TABLE)
		{
			int _cTeleCount = _teleports.size();
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement s = con.createStatement();
				ResultSet rs = s.executeQuery("SELECT id, loc_x, loc_y, loc_z, price, fornoble, itemId FROM custom_teleport"))
			{
				L2TeleportLocation teleport;
				while (rs.next())
				{
					teleport = new L2TeleportLocation();
					teleport.setTeleId(rs.getInt("id"));
					teleport.setLocX(rs.getInt("loc_x"));
					teleport.setLocY(rs.getInt("loc_y"));
					teleport.setLocZ(rs.getInt("loc_z"));
					teleport.setPrice(rs.getInt("price"));
					teleport.setIsForNoble(rs.getInt("fornoble") == 1);
					teleport.setItemId(rs.getInt("itemId"));
					
					_teleports.put(teleport.getTeleId(), teleport);
				}
				_cTeleCount = _teleports.size() - _cTeleCount;
				if (_cTeleCount > 0)
				{
					_log.info(getClass().getSimpleName() + ": " + _cTeleCount + " Custom Teleport Location Templates.");
				}
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, getClass().getSimpleName() + ": Error while creating custom teleport table " + e.getMessage(), e);
			}
		}
	}
	
	/**
	 * @param id
	 * @return
	 */
	public L2TeleportLocation getTemplate(int id)
	{
		return _teleports.get(id);
	}
	
	public static TeleportLocationTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final TeleportLocationTable _instance = new TeleportLocationTable();
	}
}