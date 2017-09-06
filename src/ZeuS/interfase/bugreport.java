package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;

public class bugreport {

	
	
	public static String IngresoBugReport(String tipo, String mensaje, L2PcInstance st){
		if(!general._activated()){
			return "";
		}
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		Connection conn = null;
		try{
		conn = ConnectionFactory.getInstance().getConnection();
		String qry = "call sp_bugg_ingreso(1,?,?,?)";
		CallableStatement psqry = conn.prepareCall(qry);
		psqry.setString(1, tipo);
		psqry.setString(2, mensaje);
		psqry.setString(3, st.getName());
		ResultSet rss = psqry.executeQuery();
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Bug Report") + central.LineaDivisora(2);
		while (rss.next()) {
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.BUG_REPORT_MENSAJE_$player.replace("$player",st.getName()) , "LEVEL") + central.LineaDivisora(2);
		}
		}catch(SQLException e){

		}

		try{
			conn.close();
		}catch(SQLException a ){

		}

		MAIN_HTML += "</body></html>";
		return MAIN_HTML;
	}

	public static String MainMenuBugReport(L2PcInstance st){
		if(!general._activated()){
			return "";
		}
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Bug Report") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.BUG_REPORT_MENSAJE_INICIAL,"LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + "<table width=250><tr><td><font color=\"LEVEL\">Select Type</font></td>";
		MAIN_HTML += "<td><combobox width=105 var=type list="+msg.BUG_REPORT_LISTA_REPORTES+"></td>";
		MAIN_HTML += "</tr></table><br>";
		MAIN_HTML += "<multiedit var=\"msg\" width=250 height=50><br>"+central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Report It\" action=\"bypass -h ZeuSNPC bugReportIN $type $msg 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		MAIN_HTML += "<br><br><br>"+central.BotonGOBACKZEUS()+"</center></body></html>";
		return MAIN_HTML;
	}

}
