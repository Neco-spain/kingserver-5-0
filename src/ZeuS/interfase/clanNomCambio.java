package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;

public class clanNomCambio {

		private static final Logger _log = Logger.getLogger(GameServer.class.getName());

		public static boolean changeNameClan(String newName, L2PcInstance Player)

		{
			if(!general._activated()){
				return false;
			}
			if (Player.getClan() == null)
			{
				Player.sendMessage(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION);
			}
			else
			{
				try
				{
					if (!Player.isClanLeader())
					{
					Player.sendMessage(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION);
						return false;
					}
					else if (null != ClanTable.getInstance().getClanByName(newName))
					{
						Player.sendMessage(msg.CLAN_NAME_$name_YA_EXISTE.replace("$name", newName));
						return false;
					}
					else if (!newName.matches("^[a-zA-Z0-9]+$"))
					{
						Player.sendMessage(msg.NOMBRE_INCORRECTO);
					}
					else
					{
						Player.getClan().setName(newName);

						try (Connection con = ConnectionFactory.getInstance().getConnection();
							PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET clan_name=? WHERE clan_id=?"))
						{
							statement.setString(1, newName);
							statement.setInt(2, Player.getClan().getId());
							statement.execute();
							statement.close();
						}
						catch (Exception e)
						{
						_log.info("Error updating clan name for player " + Player.getName() + ". Error: " + e);
							return false;
						}
							Player.sendMessage(msg.CLAN_NAME_CAMBIADO_CORRECTO_A_$name.replace("$name", newName));
						Player.getClan().broadcastClanStatus();
						Player.broadcastUserInfo();
						Player.getClan().broadcastToOnlineMembers(null);
					}
				}
			catch (Exception e)
				{
					Player.sendMessage(msg.NOMBRE_INCORRECTO);
					return false;
				}
			}
			return true;
		}
}
