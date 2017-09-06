package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class pinCode {
	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	public static void setPinStatusGeneral(L2PcInstance cha, boolean valor){
		//HashMap<L2PcInstance, HashMap<String, Boolean>> configChar = general.getConfigChar();
		if(!general.getConfigChar().containsKey(cha)){
			general.getConfigChar().put(cha, new HashMap<String,Boolean>());
		}
		general.getConfigChar().get(cha).put("PINSTATUS", valor);
	}

	public static boolean getPinStatus(L2PcInstance cha){
		return general.getConfigChar().get(cha).get("PINSTATUS");
	}

	public static boolean inPin(L2PcInstance activeChar, String texto){
		try
		{
			if (texto == null)
			{
				texto = "";
			}

			if (texto.equals(""))
			{
				if (!ErrorIngreso(activeChar))
				{
					activeChar.getClient().closeNow();
				}
				else
				{
					showWindow(activeChar);
				}
				return true;
			}
			String Pin1 = texto;
			if (isPin(activeChar.getObjectId(), Pin1))
			{
				if(!opera.isMaster(activeChar)){
					 activeChar.getAppearance().setGhostMode(false);
				}
				activeChar.setIsParalyzed(false);
				//activeChar.isActivePIN = true;
				setPinStatusGeneral(activeChar,true);
				central.msgbox_Lado("Right pin.", activeChar);
				general.setPinCountChar(activeChar, 0);
				return true;
			}
			//activeChar.isActivePIN = false;
			setPinStatusGeneral(activeChar,false);
			if (!ErrorIngreso(activeChar))
			{
				activeChar.getClient().closeNow();
			}
			else
			{
				showWindow(activeChar);
			}
			return true;
		}
		catch (Exception e)
		{
			_log.warning(e.getMessage());
		}
		return true;
	}

	protected static boolean isPin(int _charId, String PIN)
	{
		String Respuesta = "";
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("call sp_pin(1,?,?,'')"))
			{
			ps.setInt(1, _charId);
			ps.setString(2, PIN);
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					Respuesta = rs.getString(1);
				}
			}
			}
		catch (SQLException e)
		{
			_log.severe("Error Consulta PIN: " + e.getMessage());
		}
		if (Respuesta.equals("err") || (Respuesta == ""))
		{
			return false;
		}
		return true;

	}


	private static boolean ErrorIngreso(L2PcInstance activeChar)
	{
		//activeChar.PIN_VECES_ERROR++;
		general.setPinCountChar(activeChar);

		if (general.getPinCountChar(activeChar) >= 3)
		{
			general.setPinCountChar(activeChar,0);
			activeChar.sendMessage("Pin failure. Account Closed");
			return false;
		}
		activeChar.sendMessage("Pin error, try Number" + String.valueOf(general.getPinCountChar(activeChar)) + " of " + String.valueOf(3));
		return true;
	}

	private static void showWindow(L2PcInstance player){
		if(player.getClan()!= null){
			if(player.getClan().isNoticeEnabled()){
				ShowAnuncio_PIN(true, player);
			}else{
				ShowAnuncio_PIN(false, player);
			}
		}else{
			ShowAnuncio_PIN(false, player);
		}
	}

	public static boolean ShowAnuncio_PIN(boolean isClanWindows, L2PcInstance player)
	{
		if(!opera.SHOW_PIN_WINDOWS(player) || !general._activated()){
			return false;
		}
		NpcHtmlMessage notice = new NpcHtmlMessage(1);
		if (!isClanWindows)
		{
			// ServerNews
			notice.setFile(player.getHtmlPrefix(), "data/html/servnews.htm");
			notice.replace("%name%", player.getName());
			notice.replace("<body>", "<body>" + getPinWindows());
		}
		else
		{
			// ClanInfo
			notice.setFile(player.getHtmlPrefix(), "data/html/clanNotice.htm");
			notice.replace("%name%", player.getName());
			notice.replace("%clan_name%", player.getClan().getName());
			notice.replace("%notice_text%", player.getClan().getNotice());
			notice.replace("<body>", "<body>" + getPinWindows()+"<br><br>");
		}
		//player.isActivePIN = false;
		setPinStatusGeneral(player,false);
		player.getAppearance().setGhostMode(true); 
		player.setIsParalyzed(true);
		player.broadcastUserInfo();

		notice.disableValidation();

		if (notice != null)
		{
			player.sendPacket(notice);
		}
		return true;
	}

	private static String getPinWindows(){
		String Retorno = "Please enter your PIN Code<br1>" +
				"<edit type=\"seguridad\" var=\"PIN_INGRESO\" width=150>" +
				"<button value=\" Check \" action=\"bypass -h voice .Pin_Ingreso_SERVER $PIN_INGRESO\" width=100 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		return central.LineaDivisora(1) + central.headFormat(Retorno,"63AA1C") + central.LineaDivisora(1);
	}


	public static boolean canChangePIN(L2PcInstance st, String eventParam1, String eventParam2, String eventParam3){
		if(!opera.isNumeric(eventParam1)){
			central.msgbox(msg.PIN_ONLY_NUMERIC, st);
			return false;
		}
		if((eventParam1.length()>4) || (eventParam1.length()<4)){
			central.msgbox(msg.PIN_LENGTH_4, st);
			return false;
		}

		if(!opera.isNumeric(eventParam2) || !opera.isNumeric(eventParam3)){
			central.msgbox(msg.PIN_ONLY_NUMERIC, st);
			return false;
		}

		if( (eventParam2.length()!=4) || (eventParam3.length() !=4) ){
			central.msgbox(msg.PIN_LENGTH_4, st);
			return false;
		}
		if(!eventParam2.equals(eventParam3)){
			central.msgbox(msg.PIN_NO_COINCIDEN, st);
			return false;
		}
		return true;
	}



	public static boolean ResetPINCode(L2PcInstance player){
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		String qry = "CALL sp_pin(3," + String.valueOf(player.getObjectId()) + ",'','')";
		boolean Respuesta = true;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			try{
				while (rss.next()){
					if(rss.getString(1).equals("err")){
						central.msgbox(rss.getString(2),player);
						Respuesta = false;
					}else{
						central.msgbox(rss.getString(2),player);
						Respuesta = true;
					}
				}
			}catch(SQLException e){
				conn.close();
				return false;
			}
			conn.close();
		}catch(SQLException a){
			try {
				conn.close();
			} catch (SQLException e) {
			}
			return false;
		}

		return Respuesta;
	}


	public static boolean changePINCode(L2PcInstance player,String ClaveActual, String ClaveNuevo){
		Connection conn = null;
		CallableStatement psqry = null;
		ResultSet rss = null;
		String qry = "CALL sp_pin(2," + String.valueOf(player.getObjectId()) + ",'" +  ClaveActual + "','" + ClaveNuevo + "')";
		boolean Respuesta = true;

		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			try{
				while (rss.next()){
					if(rss.getString(1).equals("err")){
						central.msgbox(rss.getString(2),player);
						Respuesta = false;
					}else{
						central.msgbox(rss.getString(2),player);
						Respuesta = true;
					}
				}
			}catch(SQLException e){
				conn.close();
				return false;
			}
			conn.close();
		}catch(SQLException a){
			try {
				conn.close();
			} catch (SQLException e) {
			}
			return false;
		}

		return Respuesta;
	}

	public static String getPinCambiado(L2PcInstance st, String numPinNuevo){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Config Panel - " + st.getName()) + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Change PIN Code (4 digit)","LEVEL") + central.LineaDivisora(2) + "<br1>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Your PIN successfully Changed.<br1> Your new PIN Code: <font color=LEVEL>" + numPinNuevo + "</font>","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"CONFIG MENU\" action=\"bypass -h ZeuSNPC ConfigPanel 0 0 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","") + central.LineaDivisora(2);
		MAIN_HTML += "<br><br><br><br>" + central.BotonGOBACKZEUS()+"</body></html>";
		return MAIN_HTML;
	}

}
