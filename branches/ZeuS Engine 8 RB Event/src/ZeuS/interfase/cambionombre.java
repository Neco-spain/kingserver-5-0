package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;


public class cambionombre {

	public static boolean CambiarNombrePJ_libre(L2PcInstance player){

		if(general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE && !player.isNoble()){
			central.msgbox(msg.NECESITAS_SER_NOBLE, player);
			return false;
		}
		if(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL > player.getLevel()){
			central.msgbox( msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL)) , player);
			return false;
		}

		return true;
	}

	public static boolean CambiarNombreClan_libre(L2PcInstance player){
		if(!player.isClanLeader()){
			central.msgbox(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION, player);
			return false;
		}
		if(player.getClan().getLevel() <= general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL){
			central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_DE_CLAN_IGUAL_O_SUPERIOR_A_$level.replace("$level", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL)), player);
			return false;
		}
		return true;
	}






	public static boolean Procedimiento_CambiarNombre_Clan(L2PcInstance st,String NombreNuevo){
		if(opera.isValidName(NombreNuevo)){
			if(clanNomCambio.changeNameClan(NombreNuevo,st)) {
				return true;
			}
		}else{
			central.msgbox(msg.NOMBRE_INGRESADO_NO_ES_VALIDO,st);
		}
		return false;
	}


	public static boolean Procedimiento_CambiarNombre_Char(L2PcInstance st,String NombreNuevo){
		if(!opera.isValidName(NombreNuevo)){
			central.msgbox(msg.NOMBRE_INGRESADO_NO_ES_VALIDO,st);
			return false;
		}
		String qry = "call sp_existe_nombre(1,'"+ NombreNuevo +"','" + st.getName() +"')";
		Connection conn = null;
		String NombreValido = "";
		String NombreMensaje = "";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			try{
				while (rss.next()){
					NombreValido = rss.getString(1);
					NombreMensaje = rss.getString(2);
					if(NombreValido.equals("err")) {
						central.msgbox(rss.getString(2),st);
					}
				}
			}catch(SQLException a){
				conn.close();
			}
		}catch(SQLException e ){

		}

		try{
			conn.close();
		}catch(SQLException a ){

		}

		if(NombreValido.equals("cor")){
			st.setName(NombreNuevo);
			st.storeMe();
			central.msgbox (msg.CAMBIO_DE_NOMBRE_EXITOSO,st);
			st.broadcastUserInfo();
			return true;
		}
		if(NombreValido.equals("cor1")){
			st.setName(NombreMensaje);
			st.storeMe();
			central.msgbox (msg.CAMBIO_DE_NOMBRE_EXITOSO,st);
			st.broadcastUserInfo();
			return true;
		}
		return false;
	}
}
