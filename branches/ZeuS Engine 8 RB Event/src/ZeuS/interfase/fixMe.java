package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;


public class fixMe {

	private static final Logger _log = Logger.getLogger(fixMe.class.getName());

	public static void delegar(L2PcInstance player, String params){
		if(params == null){
			getShowWindowsFix(player);
		}else{
			if(params.length()==0){
				getShowWindowsFix(player);
			}else{
				fixIt(params,player);
			}
		}
	}

	private static int getCountItemINBD(String NombreChar){
		int retorno = 0;
		String sqlConsulta = "select count(*) from items where items.owner_id = (select characters.charId from characters where characters.char_name = ?)";
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(sqlConsulta);
			psqry.setString(1, NombreChar);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			retorno = rss.getInt(0);
			conn.close();
		}catch(SQLException e){

		}
		try{
			conn.close();
		}catch(Exception a){

		}
		return retorno;
	}

	private static void fixIt(String NomChar, L2PcInstance player){
		String sqlUpdateLocation = "update characters set characters.x=17527, characters.y=170013, characters.z=-3504 where characters.char_name = ?";
		String sqlUpdateInventario_1 = "update items set items.loc='WAREHOUSE' where items.loc='PAPERDOLL' and items.owner_id = (select characters.charId from characters where characters.char_name = ?)";
		String sqlUpdateInventario_2 = "update items set items.loc='WAREHOUSE' where items.count >= 1000 and items.owner_id = (select characters.charId from characters where characters.char_name = ?)";

		central.msgbox("Moving "+NomChar+" to a save zone", player);
		if(!ejecSQL(NomChar, sqlUpdateLocation)){
			central.msgbox("Moving "+NomChar+" was wrog, please contact the GM/ADM if you char " + NomChar + " it not right", player);
		}else{
			central.msgbox("Moving "+NomChar+" Done!", player);
		}

		central.msgbox("Moving the used clothes to his warehouse", player);
		if(!ejecSQL(NomChar, sqlUpdateInventario_1)){
			central.msgbox("Moving "+NomChar+" clothes wrog, please contact the GM/ADM if you char " + NomChar + " it not right", player);
		}else{
			central.msgbox("Moving "+NomChar+" clothes Done!", player);
		}

		central.msgbox("Moving ramdon item to his warehouse", player);
		if(!ejecSQL(NomChar, sqlUpdateInventario_2)){
			central.msgbox("Moving "+NomChar+" ramdon item's wrog, please contact the GM/ADM if you char " + NomChar + " it not right", player);
		}else{
			central.msgbox("Moving "+NomChar+" ramdon item's Done!", player);
		}

		central.msgbox("Please try to enter you char.", player);

	}

	private static boolean ejecSQL(String NomChar, String strSql){
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean retorno = false;
		try{
			conn  = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strSql);
			psqry.setString(1, NomChar);
			psqry.executeUpdate();
			retorno = true;
		}catch(SQLException e){
			_log.warning("ZeuS fix Char " + NomChar + " error: " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
		return retorno;
	}

	private static void getShowWindowsFix(L2PcInstance player){
		String HTML = "<html noscrollbar><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("Fix Char Windows") + central.LineaDivisora(1);
		Map<Integer, String> allPlayer = opera.getAllPlayerOnThisAccount(player);
		String combo = "<combobox width=250 var=cmbCharacters list=%LISTADO%>";
		String playerToCombo = "";
		String btnDoIt = "<button value=\"Fix it Please\" action=\"bypass -h voice .fixmeCharName $cmbCharacters\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		Iterator itr = allPlayer.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
			if(playerToCombo.length()>0){
				playerToCombo += ";";
			}
			playerToCombo += Entrada.getValue();
	    }
	    if(playerToCombo.length()>0){
	    	HTML += central.LineaDivisora(1) + central.headFormat(msg.FIXME_SELECT_THE_CHAR_HOW_NEED_TO_BE_FIX_IT + combo.replace("%LISTADO%", playerToCombo) + "<br>" + btnDoIt,"LEVEL") + central.LineaDivisora(1);
	    }else{
	    	HTML += central.LineaDivisora(1) + central.headFormat("You dont Have More Char in this Account","LEVEL") + central.LineaDivisora(1);
	    }

	    HTML += central.LineaDivisora(3) + central.LineaDivisora(1) + central.headFormat(msg.FIXME_EXPLAIN) + central.LineaDivisora(1) + central.LineaDivisora(3);

		HTML += "</body></html>";
		opera.enviarHTML(player, HTML);
	}
}
