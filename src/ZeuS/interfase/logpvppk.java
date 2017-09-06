package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;


public class logpvppk {

	public static String getAllPVPPK(String NombreChar,int Desde){
		if(!general._activated()){
			return "";
		}
		String MAIN_HTML;
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Log de PvP/PK de "+NombreChar) + central.LineaDivisora(2);
		String BOTON_ATRAS = "<button value=\"Back\" action=\"bypass -h ZeuSNPC logpeleas 0 0 0 \" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		int Contador = 0;
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			String qry = "call sp_lista_log_pvp(1,?," + String.valueOf(general.MAX_LISTA_PVP_LOG + 1) + "," + String.valueOf(Desde*general.MAX_LISTA_PVP_LOG) + ")";
			CallableStatement psqry = conn.prepareCall(qry);
			psqry.setString(1,NombreChar);
			ResultSet rss = psqry.executeQuery();
			MAIN_HTML += "<table width=280 bgcolor=0B3B0B><tr><td width=80 align=center>Winner</td><td width=80 align=center>Looser</td><td width=30 align=center>Tipo</td><td width=40 align=center>Count</td></tr></table>"+central.LineaDivisora(2);
			while (rss.next()){
				if (!rss.getString(1).equals("err")){
					Contador++;
					if (Contador <= general.MAX_LISTA_PVP_LOG){
						MAIN_HTML += "<table width=280 bgcolor=2E2E2E><tr><td width=80 align=center>"+rss.getString(1)+"</td><td width=80 align=center>"+rss.getString(2)+"</td><td width=30 align=center>"+rss.getString(4)+"</td><td width=40 align=center>"+String.valueOf(rss.getInt(3))+"</td></tr></table>"+central.LineaDivisora(2);
					}
				}else{
					MAIN_HTML +=  central.LineaDivisora(2) + central.headFormat(rss.getString(2),"8A2908") + central.LineaDivisora(2);
				}
			}
		}
		catch (SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException e){

		}


		String BOTON_SIGUENTE = "";
		String BOTON_ANTERIOR = "";
		if(Contador > general.MAX_LISTA_PVP_LOG){
			BOTON_SIGUENTE = "<button value=\"Next->\" action=\"bypass -h ZeuSNPC logpeleas 2 "+NombreChar+" "+ String.valueOf(Desde + 1)+"\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}
		if(Desde > 0){
			BOTON_ANTERIOR = "<button value=\"<-Back\" action=\"bypass -h ZeuSNPC logpeleas 2 "+NombreChar+" "+ String.valueOf(Desde - 1)+"\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}

		String BOTON_LISTADOS = "<table width=240 ><tr><td width=120 align=CENTER>"+BOTON_ANTERIOR+"</td><td width=120 align=CENTER>"+BOTON_SIGUENTE+"</td></tr></table>";


		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON_LISTADOS+BOTON_ATRAS,"LEVEL") + central.LineaDivisora(2);

		MAIN_HTML += "</body></html>";
		return MAIN_HTML;
	}




	public static String Main_Log_Peleas(L2PcInstance st, String Parametro){
		if(!general._activated()){
			return "";
		}
		String TXTIDITEM = "<edit var=\"txtNomChar\" width=150>";
		String Retornar = "";
		String BOTON = "<button value=\"Search\" action=\"bypass -h ZeuSNPC logpeleas 1 $txtNomChar 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BOTON_LIMPIAR = "<button value=\"Clean\" action=\"bypass -h ZeuSNPC logpeleas 0 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BOTON_BUSCAR_CHAR = "<button value=\"See\" action=\"bypass -h ZeuSNPC logpeleas 2 $cmbChares 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BOTONOPCIONES = "<table width=240><tr><td width=120 align=CENTER>"+BOTON_LIMPIAR+"</td><td width=120 align=CENTER>"+BOTON_BUSCAR_CHAR+"</td></tr></table>";

		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("PvP / PK Log") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.LOGPVP_MENSAJE,"LEVEL") + central.LineaDivisora(2) + "<br1>";
		if(Parametro.length()==0){
			MAIN_HTML += central.headFormat(msg.LOGPVP_INGRESE_NOMBRE_O_PARCIAL+TXTIDITEM +"<br1>"+BOTON+"<br1>"+central.LineaDivisora(2),"GREEN");
			if((st.getPvpKills() > 0) || (st.getPkKills() > 0)){
				String BOTON_VerMiLog = "<button value=\"See my log\" action=\"bypass -h ZeuSNPC logpeleas 2 "+st.getName() +" 0\" width=130 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON_VerMiLog,"LEVEL") + central.LineaDivisora(2);
			}
		}
		if (Parametro.length()>0){
			int Contador = 0;
			try{
				Connection conn = ConnectionFactory.getInstance().getConnection();
				String qry = "call sp_log_fight(-100,15,?)";
				CallableStatement psqry = conn.prepareCall(qry);
				psqry.setString(1,"%"+Parametro+"%");
				ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					if(rss.getString(1).equals("err")){
						MAIN_HTML += central.LineaDivisora(2) + central.headFormat(rss.getString(2)+"<br>"+BOTON_LIMPIAR,"LEVEL") + central.LineaDivisora(2);
					}else{
						Contador++;
						if (Retornar.length()>0) {
							Retornar+= ";" + rss.getString(1);
						} else {
							Retornar+= rss.getString(1);
						}
					}
				}
				try{
					conn.close();
				}catch(SQLException a){

				}
			}catch(SQLException e){

			}

			if(Retornar.length()>0){
				String COMBO_FAMILIAS = "<combobox width=180 var=cmbChares list="+Retornar+">";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Select Char"+COMBO_FAMILIAS+"<br1>"+BOTONOPCIONES,"LEVEL") + central.LineaDivisora(2);
			}
			if (Contador == 1) {
				return getAllPVPPK(Retornar,0);
			}
		}
		MAIN_HTML += "<br><br><br><br><br><br><br>" + central.BotonGOBACKZEUS() + "</body></html>";
		return MAIN_HTML;
	}

}
