package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class dropSearch {

	public static final Logger _log = Logger.getLogger(Config.class.getName());

	public static String HTMLBusquedaDrop(L2PcInstance st){
		if(!general._activated()){
			return "";
		}

		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Drop Search") + central.LineaDivisora(2);
		MAIN_HTML += central.ItemNeedShowBox(general.DROP_TELEPORT_COST,"Costo por usar el Teleport");
		String INFO_IMPORTANTE = msg.DROP_SEARCH_MENSAJE_ESPACIO;
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.DROP_SEARCH_MENSAJE+INFO_IMPORTANTE+"<br1><edit var=\"txtDropName\" width=150><br>","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Search\" action=\"bypass -h ZeuSNPC DropSearch 1 $txtDropName 0 \" width=100 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br>" + central.BotonGOBACKZEUS() + central.getPieHTML()+"</body></html><br>";
		return MAIN_HTML;
	}

	
	protected static boolean ItemisInDropList(int idItem){
		if(general.DROP_LIST_ITEM==null){
			return false;
		}
		if(!general.DROP_LIST_ITEM.containsKey(idItem)){
			return false;
		}
		return true;
	}


	public static String BusquedaDrop(L2PcInstance st, String TextoBuscar,int Desde){
		if(!general._activated()){
			return "";
		}
		if(TextoBuscar.equals("'")){
			central.msgbox(msg.ERROR_TIPEO,st);
			return central.ErrorTipeoEspacio();
		}
		int DROP_LISTA_ACTUAL = Desde;
		String BotonRegresar = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DropSearch 0 0 0\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		TextoBuscar = TextoBuscar.replace("_"," ");
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Drop Search<br1><font color=LEVEL>" + TextoBuscar + "</font>"+BotonRegresar) + central.LineaDivisora(2);
		int CantidadItem = 0;
		Vector<String> ItemSeleccionados = opera.getAllItemByName(TextoBuscar,true);

		int DesdeItemNum = DROP_LISTA_ACTUAL * general.DROP_SEARCH_MOSTRAR_LISTA;
		int ContadorItem = 1;
		int HastaItemNum = 0;
		boolean Continua = false;

		if((ItemSeleccionados.size()>0)){
			if((DesdeItemNum < ItemSeleccionados.size())){
				for(String NomItem : ItemSeleccionados){
					if(ItemisInDropList(Integer.valueOf(NomItem.split("@")[1]))){
						ContadorItem++;
						if(ContadorItem>=DesdeItemNum){
							if(HastaItemNum <= general.DROP_SEARCH_MOSTRAR_LISTA){
								HastaItemNum++;
								String BOTON = "<button value=\"" + NomItem.split("@")[0] +"\" action=\"bypass -h ZeuSNPC DropSearch 2 "+ NomItem.split("@")[1] +" "+ TextoBuscar.replace(" ", "_") +"~0 \" width=260 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
								MAIN_HTML += central.headFormat("ID Item:" + NomItem.split("@")[1] + BOTON ,"848484") + central.LineaDivisora(2);
							}else{
								Continua = true;
							}
						}
					}
				}
			}
		}else{
			MAIN_HTML +=  central.LineaDivisora(2) + central.headFormat("No Item Find ("+ TextoBuscar +")","B43104") + central.LineaDivisora(2);
		}

		TextoBuscar = TextoBuscar.replace(" ","_");

		MAIN_HTML += "<table width=280><tr>";

		if((DROP_LISTA_ACTUAL * general.DROP_SEARCH_MOSTRAR_LISTA) != 0){
			int DROP_LISTA_ACTUAL_ANT = DROP_LISTA_ACTUAL - 1;
			MAIN_HTML += "<td align=left width = 130 ><button value=\"<- Back\" action=\"bypass -h ZeuSNPC DropSearch 1 " + TextoBuscar + " " + String.valueOf(DROP_LISTA_ACTUAL_ANT) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		} else {
			MAIN_HTML += "<td align=left width = 130></td>";
		}

		if (Continua) {
			DROP_LISTA_ACTUAL = DROP_LISTA_ACTUAL + 1;
			MAIN_HTML += "<td align=right width = 130 ><button value=\"Next ->\" action=\"bypass -h ZeuSNPC DropSearch 1 " + TextoBuscar + " " + String.valueOf(DROP_LISTA_ACTUAL) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		} else {
			MAIN_HTML += "<td align=right width = 130></td>";
		}



		MAIN_HTML += "</tr></table>" + central.LineaDivisora(2) + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}


	public static String BusquedaDropInBD(L2PcInstance st, String TextoBuscar,int Desde){
		if(!general._activated()){
			return "";
		}
		if(TextoBuscar.equals("'")){
			central.msgbox(msg.ERROR_TIPEO,st);
			return central.ErrorTipeoEspacio();
		}
		int DROP_LISTA_ACTUAL = Desde;
		String BotonRegresar = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DropSearch 0 0 0\" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		TextoBuscar = TextoBuscar.replace("_"," ");
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Drop Search<br1><font color=LEVEL>" + TextoBuscar + "</font>"+BotonRegresar) + central.LineaDivisora(2);
		int CantidadItem = 0;
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "call sp_buscar_drop(1,?,"+String.valueOf(DROP_LISTA_ACTUAL * general.DROP_SEARCH_MOSTRAR_LISTA)+","+String.valueOf(general.DROP_SEARCH_MOSTRAR_LISTA)+")";
		CallableStatement psqry = conn.prepareCall(qry);
		psqry.setString(1,"%"+TextoBuscar+"%");
		ResultSet rss = psqry.executeQuery();
		TextoBuscar = TextoBuscar.replace(" ","_");

		while (rss.next()){
			if(!rss.getString(1).equals("err")){
				String BOTON = "<button value=\"" + rss.getString(2) +"\" action=\"bypass -h ZeuSNPC DropSearch 2 "+ rss.getString(1) +" "+ TextoBuscar +"~0 \" width=260 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				if(general.DROP_SEARCH_SHOW_IDITEM_TO_PLAYER || st.isGM()){
					MAIN_HTML += central.headFormat("ID Item:" + rss.getString(1) + BOTON ,"848484") + central.LineaDivisora(2);
				}
				if (CantidadItem == 0) {
					CantidadItem = Integer.valueOf(rss.getString(4));
				}
			} else {
				MAIN_HTML +=  central.LineaDivisora(2) + central.headFormat(rss.getString(2),"B43104") + central.LineaDivisora(2);
			}
		}
		conn.close();
		}catch(SQLException e){

		}

		TextoBuscar = TextoBuscar.replace(" ","_");

		MAIN_HTML += "<table width=280><tr>";

		if((DROP_LISTA_ACTUAL * general.DROP_SEARCH_MOSTRAR_LISTA) != 0){
			int DROP_LISTA_ACTUAL_ANT = DROP_LISTA_ACTUAL - 1;
			MAIN_HTML += "<td align=left width = 130 ><button value=\"<- Back\" action=\"bypass -h ZeuSNPC DropSearch 1 " + TextoBuscar + " " + String.valueOf(DROP_LISTA_ACTUAL_ANT) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		} else {
			MAIN_HTML += "<td align=left width = 130></td>";
		}

		DROP_LISTA_ACTUAL++;
		if ((DROP_LISTA_ACTUAL * general.DROP_SEARCH_MOSTRAR_LISTA) < CantidadItem) {
			MAIN_HTML += "<td align=right width = 130 ><button value=\"Next ->\" action=\"bypass -h ZeuSNPC DropSearch 1 " + TextoBuscar + " " + String.valueOf(DROP_LISTA_ACTUAL) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		} else {
			MAIN_HTML += "<td align=right width = 130></td>";
		}
		MAIN_HTML += "</tr></table>" + central.LineaDivisora(2) + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}


	protected static boolean canUseTeleportBtn(int IDMob){
		if(general.DROP_SEARCH_MOB_BLOCK_TELEPORT.contains(IDMob)){
			return false;
		}
		return true;
	}




	/*
	 *
	 *
	 * 						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("NAME", rss.getString(4));
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TYPE", rss.getString(1));
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("LEVEL", String.valueOf(rss.getInt(5)));
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CATEGORY", DROP[1]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MIN", DROP[2]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MAX", DROP[3]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CHANCE", DROP[4]);
						DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TELEPORT", rss.getString(7));*/

	public static String BusquedaDropListarNPC(L2PcInstance st,int IDDrop, String NombreDrop, int PosicionLista){
		if(!general._activated()){
			return "";
		}
		String BotonRegresar = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DropSearch 1 " + NombreDrop + " 0 \" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		NombreDrop = NombreDrop.replace("_"," ");
		String NomItem = central.getNombreITEMbyID(IDDrop);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Monster List with Drop<br1><font color=LEVEL>" + NomItem +"</font>"+BotonRegresar) + central.LineaDivisora(2) + central.LineaDivisora(2);
		NombreDrop = NombreDrop.replace(" ","_");

		int IniciarDesde= PosicionLista * general.DROP_SEARCH_MOSTRAR_LISTA;

		boolean Siguente = false;
		if(general.DROP_LIST_ITEM.containsKey(IDDrop)){
			HashMap<Integer,HashMap<String,String>> ItemdMob = general.DROP_LIST_ITEM.get(IDDrop);
			Iterator itr = general.DROP_LIST_ITEM.get(IDDrop).entrySet().iterator();
			int Contador = 0,Registros=0;
			String BOTON ="";
			String INFODROP = "";
			while(itr.hasNext() && (!Siguente)){
				Map.Entry InforMob =(Map.Entry)itr.next();
				if(Contador>= IniciarDesde){
					if(Registros < general.DROP_SEARCH_MOSTRAR_LISTA){
						HashMap<String,String> MobData = general.DROP_LIST_ITEM.get(IDDrop).get(InforMob.getKey());

						if((!MobData.get("TYPE").equalsIgnoreCase("L2GrandBoss")) && (general.DROP_SEARCH_CAN_USE_TELEPORT && canUseTeleportBtn((int)InforMob.getKey()))){
							BOTON = "<button value=\"" + MobData.get("NAME") + " (lv"+ MobData.get("LEVEL") +")" +"\" action=\"bypass -h ZeuSNPC DropSearchTele "+MobData.get("TELEPORT").split(",")[0]+" "+MobData.get("TELEPORT").split(",")[1]+" "+MobData.get("TELEPORT").split(",")[2]+" \" width=280 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
						}else{
							BOTON = "<br1><font color=\"FF8000\">" + MobData.get("NAME") +"</font><br1>";
						}
						if(MobData.get("CATEGORY").equals("-1")){
							INFODROP = "<font color=F4FA58>C.Min: " + MobData.get("MIN") + " - C.Max:" + MobData.get("MAX") + "<br1>Chance: " + MobData.get("CHANCE") + " (spoil)</font>";
						}else{
							INFODROP = "C.Min: " + MobData.get("MIN") + " - C.Max: " + MobData.get("MAX") + "<br1>Chance: " + MobData.get("CHANCE");
						}
						MAIN_HTML += central.headFormat(INFODROP + BOTON,"848484") + central.LineaDivisora(2);
						Registros++;
					}else{
						Siguente=true;
					}
				}
				Contador++;
			}
		}else{
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Drop info not Found") + central.LineaDivisora(1);
		}

		MAIN_HTML += "<table width=280><tr>";

		int POSICION_EN_LISTA_N = 0;
		if(PosicionLista > 0){
			POSICION_EN_LISTA_N = PosicionLista - 1;
			MAIN_HTML += "<td align=left width = 130 ><button value=\"<-Back\" action=\"bypass -h ZeuSNPC DropSearch 2 "+ String.valueOf(IDDrop) +" "+ NombreDrop +"~" + String.valueOf(POSICION_EN_LISTA_N) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		}
		if (Siguente){
			POSICION_EN_LISTA_N = PosicionLista + 1;
			MAIN_HTML += "<td align=right width = 130 ><button value=\"Next->\" action=\"bypass -h ZeuSNPC DropSearch 2 "+ String.valueOf(IDDrop) +" "+ NombreDrop +"~" + String.valueOf(POSICION_EN_LISTA_N) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		}
		MAIN_HTML += "</tr></table>"+central.getPieHTML()+"</body></html>";


		return MAIN_HTML;
	}

	public static String BusquedaDropListarNPC_2(L2PcInstance st,int IDDrop, String NombreDrop, int PosicionLista){
		if(!general._activated()){
			return "";
		}
		int POSICION_EN_LISTA = PosicionLista;
		String BotonRegresar = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DropSearch 1 " + NombreDrop + " 0 \" width=50 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String Coordenadas;
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		NombreDrop = NombreDrop.replace("_"," ");
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Mob List with Drop<br1><font color=LEVEL>" + NombreDrop+"</font>"+BotonRegresar) + central.LineaDivisora(2) + central.LineaDivisora(2);
		NombreDrop = NombreDrop.replace(" ","_");
		int ContadorAlto = 0;
		boolean Siguente = false;
		String INFODROP ="";
		try{
		String qry = "call sp_buscar_drop(2,'" + IDDrop + "',"+String.valueOf(POSICION_EN_LISTA * general.DROP_SEARCH_MOSTRAR_LISTA)+","+String.valueOf(general.DROP_SEARCH_MOSTRAR_LISTA + 1)+")";
		Connection conn = ConnectionFactory.getInstance().getConnection();
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();

		while (rss.next()){
			if(ContadorAlto < general.DROP_SEARCH_MOSTRAR_LISTA){
				ContadorAlto++;
				Coordenadas = "0,0,0";
				if(!rss.getString(4).equals("err")) {
					Coordenadas = rss.getString(4);
				}

				String CoordenadasXYZ[] = Coordenadas.split(",");
				String InforNPC[] = rss.getString(2).split(";");
				String BOTON ="";
				if(general.DROP_SEARCH_CAN_USE_TELEPORT && canUseTeleportBtn(rss.getInt(1))){
					BOTON = "<button value=\"" + rss.getString(3) +"\" action=\"bypass -h ZeuSNPC DropSearchTele "+CoordenadasXYZ[0]+" "+CoordenadasXYZ[1]+" "+CoordenadasXYZ[2]+" \" width=280 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				}else{
					BOTON = "<br1><font color=\"FF8000\"" + rss.getString(3)+"<br1>";
				}
				for (String Info : InforNPC){
					String splitInfo[] = Info.split(",");
					if(splitInfo[0].equals("-"+String.valueOf(IDDrop)+"-")) {
						INFODROP = "C.Min:" + splitInfo[2] + " - C.Max:" + splitInfo[3] + "<br1>Chance:" + splitInfo[4];
					}
				}
				MAIN_HTML += central.headFormat(INFODROP + BOTON,"848484") + central.LineaDivisora(2);
			} else {
				Siguente = true;
			}
		}
		MAIN_HTML += "<table width=280><tr>";
		
		try{
			conn.close();
		}catch(Exception a){
			
		}			
		
		}catch(SQLException e){

		}
		
		int POSICION_EN_LISTA_N = 0;
		if(POSICION_EN_LISTA > 0){
			POSICION_EN_LISTA_N = POSICION_EN_LISTA - 1;
			MAIN_HTML += "<td align=left width = 130 ><button value=\"<-Back\" action=\"bypass -h ZeuSNPC DropSearch 2 "+ String.valueOf(IDDrop) +" "+ NombreDrop +"~" + String.valueOf(POSICION_EN_LISTA_N) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		}
		if (Siguente){
			POSICION_EN_LISTA_N = POSICION_EN_LISTA + 1;
			MAIN_HTML += "<td align=right width = 130 ><button value=\"Next->\" action=\"bypass -h ZeuSNPC DropSearch 2 "+ String.valueOf(IDDrop) +" "+ NombreDrop +"~" + String.valueOf(POSICION_EN_LISTA_N) + " \" width=60 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		}
		MAIN_HTML += "</tr></table>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

}
