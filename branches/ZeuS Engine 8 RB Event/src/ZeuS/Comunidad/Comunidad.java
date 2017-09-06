package ZeuS.Comunidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.data.xml.impl.EnchantItemData;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.TradeItem;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.PcInventory;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

import ZeuS.Comunidad.cbManager.enumColor;
import ZeuS.Comunidad.Engine.enumBypass;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Comunidad.EngineForm.C_gmlist;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class Comunidad {
	
	
	
	private static Logger _log = Logger.getLogger(Comunidad.class.getName());
	private final static String btnMain = "<button value=\"MAIN\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + "\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
	public static HashMap<L2PcInstance,Integer> LastIdModifMensaje = new HashMap<L2PcInstance, Integer>();
	public static HashMap<L2PcInstance,Integer> PaginaVerAllPlayer = new HashMap<L2PcInstance, Integer>();
	private static HashMap<Integer,Vector<String>> ItemForSearch = new HashMap<Integer, Vector<String>>();
	public static HashMap<Integer,String> textToSearch = new HashMap<Integer, String>();
	public static HashMap<Integer, Vector<String>> BotonesBusqueda = new HashMap<Integer, Vector<String>>();
	public static HashMap<Integer,Integer> idBusquedaPS = new HashMap<Integer, Integer>();

	private static String getStatus(boolean estado){
		return estado ? "Enabled" : "Disabled";
	}
	
	private static String[]CENTRAL_ACCESS = {
			Engine.enumBypass.ClasesStadistic.name(),
			Engine.enumBypass.RaidBossInfo.name(),
			Engine.enumBypass.donation.name(),
			Engine.enumBypass.Buffer.name(),
			Engine.enumBypass.Gopartyleader.name(),
			Engine.enumBypass.Flagfinder.name(),
			Engine.enumBypass.Teleport.name(),
			Engine.enumBypass.Shop.name(),
			Engine.enumBypass.Warehouse.name(),
			Engine.enumBypass.AugmentManager.name(),
			Engine.enumBypass.SubClass.name(),
			Engine.enumBypass.Profession.name(),
			Engine.enumBypass.DropSearch.name(),
			Engine.enumBypass.pvppklog.name(),
			Engine.enumBypass.Symbolmaker.name(),
			Engine.enumBypass.BugReport.name(),
			Engine.enumBypass.Transformation.name(),
			Engine.enumBypass.RemoveAttri.name(),
			Engine.enumBypass.SelectAugment.name(),
			Engine.enumBypass.SelectEnchant.name(),
			Engine.enumBypass.SelectElemental.name(),
			Engine.enumBypass.blacksmith.name(),
			Engine.enumBypass.charclanoption.name(),
			Engine.enumBypass.Dressme.name(),
			Engine.enumBypass.partymatching.name(),
			Engine.enumBypass.AuctionHouse.name(),
			Engine.enumBypass.castleManager.name(),
			Engine.enumBypass.gmlist.name(),
			Engine.enumBypass.OlyBuffer.name(),
			Engine.enumBypass.commandinfo.name()
	};
	
	public static int getSizeMainOption(){
		return CENTRAL_ACCESS.length;
	}


	private static HashMap<String, String> getAllProperties(){
		HashMap<String, String> propiedades = new HashMap<String, String>();
		propiedades.put("Exp:", String.valueOf(Config.RATE_XP));
		propiedades.put("Sp:", String.valueOf(Config.RATE_SP));
		propiedades.put("Party Exp:", String.valueOf(Config.RATE_PARTY_XP));
		propiedades.put("Party Sp:", String.valueOf(Config.RATE_PARTY_SP));
		//propiedades.put("Adena:", String.valueOf(Config.RATE_DROP_ITEMS_ID.get(PcInventory.ADENA_ID)));

		propiedades.put("Drop:", String.valueOf(Config.RATE_DEATH_DROP_CHANCE_MULTIPLIER));
		propiedades.put("Spoil:", String.valueOf(Config.RATE_CORPSE_DROP_CHANCE_MULTIPLIER));
		propiedades.put("Manor:", String.valueOf(Config.RATE_DROP_MANOR));
		propiedades.put("Quest Drop:", String.valueOf(Config.RATE_QUEST_DROP));
		propiedades.put("Quest Reward:", String.valueOf(Config.RATE_QUEST_REWARD));
		propiedades.put("Quest Adena:", String.valueOf(Config.RATE_QUEST_REWARD_ADENA));
		propiedades.put("Karma Exp Lost:", String.valueOf(Config.RATE_KARMA_EXP_LOST));
		propiedades.put("Weight Limit:", String.valueOf(Config.ALT_WEIGHT_LIMIT));
		propiedades.put("Maximum Slot for No Dwarf:", String.valueOf(Config.INVENTORY_MAXIMUM_NO_DWARF));
		propiedades.put("Maximum Slot for Dwarf:", String.valueOf(Config.INVENTORY_MAXIMUM_DWARF));
		propiedades.put("Maximum Slot WH for No Dwarf:", String.valueOf(Config.WAREHOUSE_SLOTS_NO_DWARF));
		propiedades.put("Maximum Slot WH for Dwarf:", String.valueOf(Config.WAREHOUSE_SLOTS_DWARF));

		propiedades.put("F. to get's Fame Fortress:(Min)", String.valueOf(Config.FORTRESS_ZONE_FAME_TASK_FREQUENCY / 60));
		propiedades.put("F. to get's Fame Castle:(Min)", String.valueOf(Config.CASTLE_ZONE_FAME_TASK_FREQUENCY / 60));

		propiedades.put("Fame Fortress Siege Zone:", String.valueOf(Config.FORTRESS_ZONE_FAME_AQUIRE_POINTS));
		propiedades.put("Fame Castle Zone:", String.valueOf(Config.CASTLE_ZONE_FAME_AQUIRE_POINTS));

		propiedades.put("Starting Adena:", String.valueOf(Config.STARTING_ADENA));
		propiedades.put("Buff:", String.valueOf(Config.BUFFS_MAX_AMOUNT) +" Buff's & " + String.valueOf(Config.DANCES_MAX_AMOUNT)+ " Dances");
		propiedades.put("Max Alliance:", String.valueOf(Config.ALT_MAX_NUM_OF_CLANS_IN_ALLY));
		propiedades.put("Max Subclases:", String.valueOf(Config.MAX_SUBCLASS));
		propiedades.put("Subclases base Level:", String.valueOf(Config.BASE_SUBCLASS_LEVEL));
		propiedades.put("Subclases Max Level:", String.valueOf(Config.MAX_SUBCLASS_LEVEL));
		/*propiedades.put("Max. Run Speed:", String.valueOf(Config.MAX_RUN_SPEED));
		propiedades.put("Max. P. Crit. Rate:", String.valueOf(Config.MAX_PCRIT_RATE));
		propiedades.put("Max. M. Crit. Rate:", String.valueOf(Config.MAX_MCRIT_RATE));
		propiedades.put("Max. P. Speed:", String.valueOf(Config.MAX_PATK_SPEED));
		propiedades.put("Max. M. Speed:", String.valueOf(Config.MAX_MATK_SPEED));*/
		propiedades.put("Unstuck Seg:", String.valueOf(Config.UNSTUCK_INTERVAL));
		propiedades.put("Day Before Join a Clan:", String.valueOf(Config.ALT_CLAN_JOIN_DAYS));
		propiedades.put("Delete Char After Days:", String.valueOf(Config.DELETE_DAYS));
		propiedades.put("Antibot Target:", getStatus(general.ANTIBOT_COMANDO_STATUS));
		propiedades.put("Antibot Automatic:", getStatus(general.ANTIBOT_AUTO));
		propiedades.put("Dressme:", getStatus(general.DRESSME_STATUS));
		propiedades.put("Dressme target:", getStatus(general.DRESSME_TARGET_STATUS));
		propiedades.put("Char panel:", getStatus(general.CHAR_PANEL));
		propiedades.put("Voice Buffer:", getStatus(general.BUFFCHAR_ACT));
		propiedades.put("Premium Char:", getStatus(general.PREMIUM_CHAR));
		propiedades.put("Premium Clan:", getStatus(general.PREMIUM_CLAN));

		if(general.getCommunityServerInfo()!=null){
			if(general.getCommunityServerInfo().size()>0){
				for(String p_in : general.getCommunityServerInfo()){
					propiedades.put(p_in.split(":")[0]+":" , p_in.split(":")[1]);
				}
			}
		}

		return propiedades;
	}

	private static String getSearchDrop(L2PcInstance player, int pagina, String parametros, boolean ListarItemEncontrados){


		/*
		 * 			case "STORE_SEARCH_IN_PS":
					Retorno = getCraftStoresOnLine(player,Integer.valueOf(parm1),parm2,true);
					break;
		 * */



		String btnNext = "<button value=\"Next\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_CAM_PAG;"+ String.valueOf(pagina+1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPrevi = "<button value=\"Prev.\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_CAM_PAG;"+ String.valueOf(pagina-1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int MaximoItemToShow = 300;
		int ItemsPorGrilla = 26;
		int desde = pagina * ItemsPorGrilla;
		int hasta = (desde + ItemsPorGrilla);
		boolean haveNext=false;


		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Item Search</font>" + btnMain);
		String btnSearch = "<button value=\"Search\" width=100 height=24 action=\"Write Z_SEARCH_ITEM Set _ txtSearch txtSearch txtSearch\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String txtItemBuscar = "<edit var=\"txtSearch\" width=180>";

		String grillaControlBusqueda = "<table width=280><tr>" +
										"<td width=100 align=CENTER>"+ txtItemBuscar +"</td>"+
										"<td width=180 align=CENTER>"+btnSearch+"</td>"+
										"</tr></table>";

		retorno += cbManager._formBodyComunidad(grillaControlBusqueda, cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name()));
		if(parametros!=null){
			if(parametros.length()>0){
				if(ItemForSearch!=null){
					if(ItemForSearch.containsKey(player.getObjectId())){
						if(ItemForSearch.get(player.getObjectId())!=null){
							if(ItemForSearch.get(player.getObjectId()).size()>MaximoItemToShow){
								central.msgbox("Please be more specific. We found " + ItemForSearch.get(player.getObjectId()).size() + " item's." , player);
							}else{
								if(ListarItemEncontrados){
									///				case "STORE_SEARCH_CAM_PAG":
									//general.COMMUNITY_BOARD_PART_EXEC+";STORE_SEARCH_1;0;"+arg5+";0;0";
									BotonesBusqueda.put(player.getObjectId(),new Vector<String>());
									int Contador = 0;
									for(String NomItem : ItemForSearch.get(player.getObjectId())){
										if((Contador >= desde) && (Contador < hasta)){
											String BOTON = "<button value=\"" + NomItem.split("@")[0] +"\" action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_IN_PS;"+ String.valueOf(pagina) +";"+ NomItem.split("@")[1] +";0;0\" width=360 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
											BotonesBusqueda.get(player.getObjectId()).add(BOTON);
										}else if((Contador > hasta) && !haveNext){
											haveNext = true;
										}
										Contador++;
									}
								}else{

								}
							}
						}
					}
				}
			}
		}

		if(ListarItemEncontrados){
			if(BotonesBusqueda!=null){
				String grillaControles = "<table width=720><tr><td width=60>"+ ( pagina>0 ? btnPrevi : "" ) +"</td><td width=600 align=CENTER>Page "+ String.valueOf(pagina + 1) +"</td><td width=60>"+ ( haveNext ? btnNext : "" ) +"</td></tr></table>";
				if(BotonesBusqueda.containsKey(player.getObjectId())){
					retorno += cbManager._formTituloComunidad("<center><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Searching Type: "+ textToSearch.get(player.getObjectId()) +"</font></center>", false, cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VERDE.name()));
					String GrillaTemp = "<table width=720 align=CENTER bgcolor="+cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name())+">";
					int contadorColum = 0;
					for(String Botones : BotonesBusqueda.get(player.getObjectId())){
					if(contadorColum==0){
							GrillaTemp+="<tr>";
						}
						GrillaTemp+="<td width=360 align=CENTER>"+ Botones +"</td>";
						contadorColum++;
						if(contadorColum>=2){
							contadorColum=0;
							GrillaTemp+="</tr>";
						}
					}
					if(contadorColum==1){
						GrillaTemp+="<td width=360></td></tr>";
					}
					GrillaTemp += "</table><br>"+grillaControles;
					retorno += cbManager._formBodyComunidad(GrillaTemp);
				}
			}
		}

		retorno += cbManager.getPieCommunidad() + "</body></html>";
		return retorno;
	}

	private static String getAllPlayerFromClan(L2PcInstance player, int Pagina, int idClan, String NombreClan, String lvlClan){

		String btnNext = "<button value=\"Next\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN_ALL_MORE;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ lvlClan +";"+ String.valueOf(idClan) +"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPrevi = "<button value=\"Prev.\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN_ALL_MENUS;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ lvlClan +";"+ String.valueOf(idClan) +"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnNombreClan = "<button value=\"Back\" width=100 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN_SEE;" + String.valueOf(Pagina) + ";"+ String.valueOf(idClan) +";"+NombreClan+";"+lvlClan+"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int MaximoPlayer = 50;

		int Inicio = MaximoPlayer * PaginaVerAllPlayer.get(player);
		int Termino = MaximoPlayer * (PaginaVerAllPlayer.get(player) + 1);

		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">All Player's from "+ NombreClan +" Clan</font>" + btnMain + btnNombreClan);
		String Tabla = "<table width=750 align=CENTER bgColor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) +">" ;
		String consulta = "SELECT characters.char_name FROM characters WHERE characters.clanid = ? ORDER BY characters.char_name ASC LIMIT " + String.valueOf(Inicio) + " , " + String.valueOf(MaximoPlayer + 2);

		String[] Colores = { cbManager.getFontColor(enumColor.Celeste.name()), cbManager.getFontColor(enumColor.Naranjo_Claro.name()), cbManager.getFontColor(enumColor.Verde.name())};
		int contadorColores = 0;

		Connection conn = null;
		PreparedStatement psqry = null;
		int Contador = 1;
		int MaximoColumnas = 5;
		String tamañoColumnas = String.valueOf(750 / MaximoColumnas);
		boolean haveNext = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, idClan);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if(contadorColores <= MaximoPlayer){
					if(Contador == 1){
						Tabla += "<tr>";
					}
					Tabla += "<td align=CENTER width="+tamañoColumnas+"><a action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHARINFO;"+ String.valueOf(Pagina) +";"+ rss.getString(1) +";"+String.valueOf(idClan)+";"+ NombreClan + "-" + lvlClan +"\">"+
					"<font color="+ Colores[contadorColores%3] +">"+rss.getString(1) +"</font></a></td>";
					if(Contador == MaximoColumnas){
						Tabla += "</tr>";
						Contador = 0;
					}
					Contador++;
				}else{
					haveNext = true;
				}
				contadorColores++;
			}
			if(Contador==0){
				Tabla +="</table>";
			}else{
				for(int i=Contador;i<=MaximoColumnas;i++){
					Tabla += "<td width="+tamañoColumnas+"></td>";
				}
				Tabla += "</tr></table>";
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}


		String tablaBotones = "<table width=720><tr><td width=23>"+( PaginaVerAllPlayer.get(player)>0 ? btnPrevi : "") + "</td><td width=674 align=CENTER>Page "+ String.valueOf(PaginaVerAllPlayer.get(player) + 1)  +"</td><td width=23>"+ (haveNext ? btnNext : "") +"</td></tr></table>";


		retorno += cbManager._formBodyComunidad(Tabla + tablaBotones, cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()));

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static void setDeleteReportWindows(L2PcInstance player, int idBugReport){
		String strMySql = "DELETE FROM zeus_bug_report WHERE id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			psqry.setInt(1, idBugReport);
			psqry.executeUpdate();
			psqry.close();
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
	}
	
	private static void setReadBugReport(int idBugReport){
		Connection conn = null;
		PreparedStatement psqry = null;
		_log.warning("IDBUGREPORT:" + idBugReport);
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry=conn.prepareStatement("UPDATE zeus_bug_report SET zeus_bug_report.leido = 'SI' WHERE zeus_bug_report.ID = ? ");
			try{
				psqry.setInt(1, idBugReport);
				psqry.executeUpdate();
				psqry.close();
			}catch(SQLException e){
				_log.warning("ERROR E1:" + e.getMessage());
			}
		}catch (SQLException ee) {
			_log.warning("ERROR EE:" + ee.getMessage());
		}
		try{
			conn.close();
		}catch (SQLException e2) {
			_log.warning("ERROR E2:" + e2.getMessage());
		}		
	}

	private static void getBugReportWindows(L2PcInstance player, int idBugReport){
		String retorno = "<html><title>ZeuS Engine Bug Report</title><body>";
		retorno += central.LineaDivisora(1)+central.headFormat("Bug Report",cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name())) + central.LineaDivisora(1) + central.LineaDivisora(3);
		String formatoCabezera = "<table width=270 align=center bgColor=%COLOR%><tr><td width=135 align=CENTER>%TITULO%</td><td width=135 align=CENTER>%DATO%</td></table>" + central.LineaDivisora(1);
		String formatoMensaje = "<table width=270 align=center bgColor=%COLOR%><tr><td width=270 align=CENTER fixwidth=270>%DATO%</td></table>" + central.LineaDivisora(1);
		String[] ColorG = { cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name()), cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) };
		String strMySql = "SELECT * FROM zeus_bug_report WHERE id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			psqry.setInt(1, idBugReport);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					L2PcInstance chaReport = opera.getPlayerbyName(rss.getString(5));
					boolean playerOnline = false;
					if(chaReport!=null){
						if(chaReport.isOnline()){
							if(!chaReport.getClient().isDetached()){
								playerOnline = true;
							}
						}
					}
					retorno += formatoCabezera.replace("%DATO%", rss.getString(5)).replace("%COLOR%", ColorG[0]).replace("%TITULO%", "CHAR NAME:");
					retorno += formatoCabezera.replace("%DATO%", ( playerOnline ? "YES" : "NO" ) ).replace("%COLOR%", ColorG[1]).replace("%TITULO%", "ONLINE:");
					retorno += formatoCabezera.replace("%DATO%", rss.getString(4)).replace("%COLOR%", ColorG[0]).replace("%TITULO%", "DATE:");
					retorno += formatoMensaje.replace("%DATO%", "REPORT").replace("%COLOR%", ColorG[1]);
					retorno += formatoMensaje.replace("%DATO%", rss.getString(3)).replace("%COLOR%", ColorG[0]);
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		setReadBugReport(idBugReport);

		retorno += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, retorno);
	}

	private static String getBugReport(L2PcInstance player, int Pagina){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Bug Report's</font>" + btnMain);

		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";BUG_REPORT;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";BUG_REPORT;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String colorHead = cbManager.getFontColor(cbManager.enumColor.Verde.name());

		String Tabla = "<table width=720 align=center bgcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name())+">" +
						"<tr>"+
							"<td width=18 align=CENTER><font color="+colorHead+">Nº</font></td>"+
							"<td width=170 align=CENTER><font color="+colorHead+">DATE</font></td>"+
							"<td width=110 align=CENTER><font color="+colorHead+">TYPE</font></td>"+
							"<td width=130 align=CENTER><font color="+colorHead+">PLAYER</font></td>"+
							"<td width=262 align=CENTER><font color="+colorHead+">REPORT</font></td>"+
							"<td width=30 align=CENTER><font color="+colorHead+">SEE</font></td>"+
						"</tr>";

		int MaximoLineas = 10;
		int Desde = MaximoLineas * Pagina;
		boolean haveNext = false;
		int Contador = 1;

		String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";BUG_REPORT_SEE;" + String.valueOf(Pagina) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnBorrarNoticia = "<button value=\"Spr\" width=35 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";BUG_REPORT_SUPRIMIR;" + String.valueOf(Pagina) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		boolean isMasterAccess = opera.isMaster(player);

		String[] ColoresFilas = { cbManager.getFontColor(cbManager.enumColor.Celeste.name()),cbManager.getFontColor(cbManager.enumColor.Gris.name()) };
		String Consulta = "select * from zeus_bug_report order by fechaIngreso DESC limit " + String.valueOf(Desde) + ", " + String.valueOf(MaximoLineas + 1);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(Contador <= MaximoLineas){
						String DescripTemporal = rss.getString(3).substring(0, ( rss.getString(3).length()>40 ? 40 : rss.getString(3).length() ) );
						Tabla += "<tr>" +
									"<td width=18 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+String.valueOf(Contador + Desde)+"</font></td>"+
									"<td width=170 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+rss.getString(4)+"</font></td>"+
									"<td width=110 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+rss.getString(2)+"</font></td>"+
									"<td width=130 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+rss.getString(5)+"</font></td>"+
									"<td width=262 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+DescripTemporal+"</font></td>"+
									"<td width=30 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+btnVerNoticia.replace("%IDVER%", String.valueOf(rss.getString(1))) + ( isMasterAccess ? btnBorrarNoticia.replace("%IDVER%", String.valueOf(rss.getInt(1))) : "" ) +"</font></td>"+
								"</tr>";
					}else{
						haveNext = true;
					}
					Contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		Tabla += "</table><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Bug Report Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		Tabla += TablaControles;

		retorno += cbManager._formBodyComunidad(Tabla, cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()));

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}








	private static String getServerInformationRate(L2PcInstance player, int pagina){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Server Config</font>" + btnMain);
		HashMap<String, String> propiedades = getAllProperties();
		String btnNext = "<button value=\"Next\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RATE;"+ String.valueOf(pagina+1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPrevi = "<button value=\"Prev.\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RATE;"+ String.valueOf(pagina-1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int propiedadesSorportadas = 19 * 3; //57
		int desde = pagina * propiedadesSorportadas;//57*0=0: 57*1=57
		int hasta = desde + propiedadesSorportadas;//57+0=57: 57+57=114
		boolean haveNext = false;

		String Tabla = "<table width=770 align=center bgcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name())+"><tr>";
		Iterator itr = propiedades.entrySet().iterator();
		int Contador = 0;
		int Separador = 3;
		int ContadorPasadas = 0;
	    while(itr.hasNext() && !haveNext){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	if(ContadorPasadas>=desde){
		    	if((Contador%Separador) == 0){
		    		Contador = 0;
		    		Tabla+= "</tr><tr>";
		    	}
		    	Tabla += "<td width=255 align=CENTER fixwidth=258><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">"+ Entrada.getKey().toString() +"</font> <font color="+ cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) +">"+ Entrada.getValue().toString() +"</font></td>";
		    	Contador++;
	    	}
	    	ContadorPasadas++;
	    	if(ContadorPasadas>=hasta){
	    		haveNext = true;
	    	}
	    }

	    if(propiedadesSorportadas == propiedades.size()){
	    	haveNext= false;
	    }

	    if(Contador <3){
	    	for(int i=Contador;i<=Separador;i++){
	    		Tabla+="<td></td>";
	    	}
	    	Tabla+="</tr>";
	    }else{
	    	Tabla+="</tr>";
	    }
		Tabla += "</table><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (pagina > 0 ? btnPrevi : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Server Config Page "+ String.valueOf(pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnNext : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		Tabla += TablaControles;

		retorno += cbManager._formBodyComunidad(Tabla, cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()));

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	//AQUI

	private static String getExplica(L2PcInstance player, String parte, int PaginaVer){

		int MaximoVer = 50;
		int Desde = PaginaVer*MaximoVer;
		
		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";"+parte+"_MOD;0;%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";"+parte+"_DEL;0;%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnNew = "<button value=\"New\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";"+parte+"_NEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String grilla = "<table width=500><tr><td width=100 align=CENTER>"+ btnModificar +"</td><td width=100 align=CENTER>"+ btnEliminar +"</td></tr></table>";


		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">"+parte+"</font>" + btnMain + ( player.isGM() ? btnNew : "" ));
		String ParteBuscar = "";

		String TablaInformacion = "<table width=750 align=CENTER bgcolor="+ cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) +"><tr><td width=20></td><td width=730 align=LEFT><font color="+cbManager.getFontColor(cbManager.enumColor.Verde.name())+">%TITULO%</font></td></tr></table>";
		TablaInformacion += "<table width=750 bgcolor="+cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name())+"><tr><td width=80></td><td width=670><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">%MENSAJE%</font></td></tr></table>";

		if(parte.equalsIgnoreCase("FEATURES")){
			ParteBuscar = "FEATURES";
		}else if(parte.equalsIgnoreCase("EVENTS")){
			ParteBuscar = "EVENTS";
		}else if(parte.equalsIgnoreCase("PLAYGAME")){
			ParteBuscar = "PLAYGAME";
		}

		Connection conn = null;
		PreparedStatement psqry = null;
		
		boolean Continua = false;
		int contador = 0;

		String Consulta = "SELECT * FROM zeus_annoucement WHERE tipo=? LIMIT ?,?";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			psqry.setString(1, ParteBuscar);
			psqry.setInt(2, Desde);
			psqry.setInt(3, MaximoVer);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(contador>= MaximoVer){
						Continua =true;
					}
					if(!Continua){
						String Grilla2 = grilla.replace("%IDMENSAJE%", String.valueOf(rss.getInt(1)));
						retorno += TablaInformacion.replace("%TITULO%", rss.getString(2)).replace("%MENSAJE%", rss.getString(3) + ( player.isGM() ? Grilla2 + "<br>" : "" ) );
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getMensajeParaLeer(L2PcInstance player, int IdMensaje, int IdPagina){

		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_MODIF;" + String.valueOf(IdPagina-1) + ";"+ String.valueOf(IdMensaje) +";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_DELETE;" + String.valueOf(IdPagina-1) + ";"+String.valueOf(IdMensaje)+";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String strTableConfig = "<table width=180><tr><td width=90>"+btnModificar+"</td><td width=90>"+btnEliminar+"</td></tr></table>";

		String btnMainBack = "<button value=\"Back\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO;" + String.valueOf(IdPagina-1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno = "<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Annoucement Reading</font>" + btnMainBack + ( player.isGM() ? strTableConfig : "" ));

		String sqlMysql = "select * from zeus_annoucement where id=?";

		Connection conn = null;
		PreparedStatement psqry = null;

		String NombreGM ="", Titulo="", Mensaje="", Fecha="";

		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(sqlMysql);
			psqry.setInt(1, IdMensaje);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					NombreGM = rss.getString(4);
					Titulo = rss.getString(2);
					Mensaje = rss.getString(3);
					Fecha = rss.getString(5);
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		String Grilla = "<table width=700 align=CENTER>" +
						"<tr><td width=720 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">" + NombreGM + "</font></td></tr>" +
						"<tr><td width=720 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">" + Fecha + "</font></td></tr>" +
						"<tr><td width=720 align=CENTER fixwidth=700><font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">" + Titulo + "</font></td></tr>" +
						"<tr><td width=720 align=CENTER fixwidth=700></td></tr>" +
						"<tr><td width=720 align=CENTER fixwidth=700><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">" + Mensaje + "</font></td></tr>" +
						"</table>";

		retorno += cbManager._formBodyComunidad(Grilla);

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getAllRules(L2PcInstance player, int Pagina){

		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RULES_MODIF;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RULES_DELETE;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String strTableConfig = "<table width=180><tr><td width=90>"+btnModificar+"</td><td width=90>"+btnEliminar+"</td></tr></table>";

		String Retorno = "";
		int PorPagina = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RULES;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RULES;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
						"<tr>" +
						"<td width=30 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Nº</font></td>" +
						"<td width=700 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Rules</font></td>" +
						"</tr></table>";
		TablaSTR += "<table width=750 align=CENTER>";

		//String []ColoresGrilla = {cbManager.getFontColor(cbManager.enumColor.Gris.name()), cbManager.getFontColor(cbManager.enumColor.Verde.name()) };
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		//String strMySql = "select * from zeus_annoucement where zeus_annoucement.tipo = 'RULES' order by zeus_annoucement.fecha DESC LIMIT " + String.valueOf(Pagina * PorPagina) + "," + String.valueOf(((Pagina+1) * PorPagina) + 1) ;
		String strMySql = "select * from zeus_annoucement where zeus_annoucement.tipo = 'RULES' order by zeus_annoucement.fecha DESC LIMIT " + String.valueOf(Pagina * PorPagina) + "," + String.valueOf(PorPagina + 2) ;
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean haveNext = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;

			while (rss.next() && !haveNext){
				try{
					if(contador <= PorPagina){
						String GrillaModif = strTableConfig.replace("%IDMENSAJE%", String.valueOf(rss.getInt(1)));
						if(!player.isGM()){
							GrillaModif = "";
						}
						TablaSTR += "<tr>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (contador + ( Pagina * PorPagina  ) ) +"</font></td>" +
								"<td width=700 align=CENTER fixwidth=690><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(3) +"</font><br1>"+GrillaModif+"</td>" +
								"</tr>";
					}else{
						haveNext = true;
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table><br><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Rules Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		Retorno = TablaSTR + TablaControles;

		return Retorno;
	}

	private static String getAllTopPlayer(L2PcInstance player, int pagina, String Parametros){



		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Top Players</font>" + btnMain);

		HashMap<Integer, String> TipoBusquedas = new HashMap<Integer, String>();

		//_log.warning(Consulta);

		String btnMaestro = "<button value=\"%NOMBRE%\" width=%TAM% height=16 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";TOPPLAYER;%ACCION%;0;0;0\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter>";
		//String btnMaestro = "<button value=\"%NOMBRE%\" width=%TAM% height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";TOPPLAYER;%ACCION%;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnPvP = btnMaestro.replace("%NOMBRE%", "PvP").replace("%TAM%", "58").replace("%ACCION%", ( pagina==2 ? "3" : "2"));
		String btnPk = btnMaestro.replace("%NOMBRE%", "Pk").replace("%TAM%", "58").replace("%ACCION%", ( pagina==0 ? "1" : "0"));
		//String btnOnline = btnMaestro.replace("%NOMBRE%", "Online").replace("%TAM%", "110").replace("%ACCION%", ( pagina==0 ? "1" : "0"));

		
		retorno += "<center><table cellpadding=0 cellspacing=0><tr><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Rank\" width=34 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Player Name\" width=188 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Lv\" width=22 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            btnPvP+"</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            btnPk+"</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Base Class\" width=138 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Status\" width=52 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"\" width=62 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td></tr></table><br>";

		String Colores[] = {"232323","141313"};
		//int contador = 1;
		String TipoConecion = "";
		for(int ContFila=0;ContFila<general.COMMUNITY_BOARD_TOPPLAYER_LIST;ContFila++){
			try{
				HashMap<Integer, String> Base = general.getTopPpl(pagina).get(ContFila);
				L2PcInstance playerBus = null;
				try{
					playerBus = opera.getPlayerbyName(Base.get(1));
					if(playerBus == null){
						TipoConecion = "<font color=676767>Offline</font>";
					}else{
						if(playerBus.getClient().isDetached()){
							if(playerBus.isInCraftMode()){
								TipoConecion = "<font color=676767>Off. Cr</font>";
							}else if(playerBus.isInStoreMode()){
								TipoConecion = "<font color=676767>Off. St</font>";;
							}
						}else{
							TipoConecion =  "<font color=5EFDA6>Online</font>";
						}
					}
				}catch(Exception a){
					TipoConecion = "<font color=676767>Offline</font>";
				}
				String btnInfoChar = "<button value=\"Char Info\" width=60 height=26 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";TOPPLAYER_SHOW_PPL;"+ String.valueOf(pagina) +";"+ Base.get(1) +";0;0\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
				retorno += "<table cellpadding=0 cellspacing=0 border=0 bgcolor="+Colores[ContFila % 2]+"><tr><td fixwidth=38 align=CENTER>"+
		                (ContFila==0 ? "<img src=\"L2UI_CT1.clan_DF_clanwaricon_bluecrownleader\" width=32 height=32>" : String.valueOf(ContFila + 1)) + "</td><td fixwidth=192 align=CENTER>"+
		                (ContFila==0 ? "<font name=\"hs12\" color=54CFFF>"+ Base.get(1) + "</font>" : "<font color=E9BF85>"+ Base.get(1) + "</font>") + "</td><td fixwidth=26 align=CENTER>"+
		                 String.valueOf(Base.get(2)) + "</td><td fixwidth=59 align=CENTER>"+
		                "<font color=FD5EFB>"+ Base.get(4) +"</font></td><td fixwidth=65 align=CENTER>"+
		                "<font color=FE7A7A>"+ Base.get(5)  +"</font></td><td fixwidth=140 align=CENTER>"+
		                "<font color=E9BF85>"+ opera.getClassName(Integer.valueOf(Base.get(3))) +"</font></td><td fixwidth=58 align=CENTER>"+
		                TipoConecion + "</td><td fixwidth=62>"+ btnInfoChar + "</td></tr></table>";
			}catch(Exception a){
				
			}
		}

		retorno += "</center>"+ cbManager.getPieCommunidad() +"</body></html>";

		return retorno;
	}

	private static Vector<String>getClanFromAlly(int idAlly){
		Vector<String> ClanesSave = new Vector<String>();
		String consulta = "select clan_data.clan_name, clan_data.clan_id, clan_data.clan_level from clan_data where clan_data.ally_id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, idAlly);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					ClanesSave.add(rss.getString(1)+";"+String.valueOf(rss.getInt(2))+";"+String.valueOf(rss.getString(3)));
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return ClanesSave;
	}

	private static int getIdAllyFromClan(int clanId){
		int retorno = 0;
		String consulta = "select ally_id from clan_data where clan_data.clan_id = " + String.valueOf(clanId);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getInt(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static String getFortressFromClan(int idClan){
		String retorno = "None";
		String consulta = "select fort.name from fort where fort.owner = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getString(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static String getCastleFromClan(int idClan){
		String retorno = "None";
		String consulta = "SELECT castle.name FROM castle INNER JOIN clan_data ON clan_data.hasCastle = castle.id where clan_data.clan_id = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getString(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static int getCountAllPplFromClan(int idClan){
		int retorno = 0;
		String consulta = "SELECT count(*) FROM characters WHERE characters.clanid = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getInt(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static int getCountHeroesFromClan(int idClan){
		int retorno = 0;
		String consulta = "SELECT count(*) FROM characters WHERE characters.charId IN (SELECT heroes.charId FROM heroes WHERE playerd = 1 ) AND characters.clanid = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getInt(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}


	private static String getAnnoucementByID(int idAnnou, boolean getMensaje){
		String retorno = "None";
		String consulta = "select strmensaje,strtitle from zeus_annoucement where id = " + String.valueOf(idAnnou);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(getMensaje){
						retorno = rss.getString(1);
					}else{
						retorno = rss.getString(2);
					}
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static void setDeleteAnnoucement(int idMensaje){
		String consulta = "delete from zeus_annoucement where id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, idMensaje);
			psqry.executeUpdate();
			psqry.close();
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

	}

	private static String getClanHallFromClan(int idClan){
		String retorno = "None";
		String consulta = "select chanhall.name, clanhall.location from chanhall where chanhall.ownerId = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getString(1) + "("+ rss.getString(2) +")";
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static Vector<String> getAllClanWar(int idClan){
		Vector<String> retorno = new Vector<String>();
		Vector<Integer> ClanesAgregados = new Vector<Integer>();
		ClanesAgregados.add(-1);
		String consulta = "SELECT clan_wars.clan1,"+
							"clan_wars.clan2,"+
							"(SELECT CONCAT(clan_data.clan_name, \" (Lv\", clan_data.clan_level ,\")\") FROM clan_data WHERE clan_data.clan_id = clan_wars.clan1),"+
							"(SELECT CONCAT(clan_data.clan_name, \" (Lv\", clan_data.clan_level ,\")\") FROM clan_data WHERE clan_data.clan_id = clan_wars.clan2),"+
							"(SELECT CONCAT(clan_data.clan_name,\";\",clan_data.clan_level) FROM clan_data WHERE clan_data.clan_id = clan_wars.clan1),"+
							"(SELECT CONCAT(clan_data.clan_name,\";\",clan_data.clan_level) FROM clan_data WHERE clan_data.clan_id = clan_wars.clan2)"+
							" FROM clan_wars WHERE (clan_wars.wantspeace1 = 0 AND clan_wars.wantspeace2 = 0) AND clan_wars.clan1 = "+ String.valueOf(idClan) +" OR clan_wars.clan2 = "+String.valueOf(idClan);

		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(!ClanesAgregados.contains(rss.getInt(1)) && !ClanesAgregados.contains(rss.getInt(2))){
						if(rss.getInt(1)!=idClan){
							retorno.add(String.valueOf(rss.getInt(1))+";"+rss.getString(3)+";"+rss.getString(5));
							ClanesAgregados.add(rss.getInt(1));
						}else{
							retorno.add(String.valueOf(rss.getInt(2))+";"+rss.getString(4)+";"+rss.getString(6));
							ClanesAgregados.add(rss.getInt(2));
						}
					}
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}


	private static String getInfoClan(L2PcInstance player, int Pagina, String Parametros, int IdClan, String NombreClan, int lvClan){

		String isVipClan = opera.isClanPremium_BD(String.valueOf(IdClan)) ? "<br1><font color="+ cbManager.getFontColor(cbManager.enumColor.Azul.name()) +">--VIP Clan--</font>" : "";

		String btnRetroceder = "<button value=\"Back\" width=120 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN;"+ String.valueOf(Pagina + 1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">"+NombreClan+"("+ String.valueOf(lvClan) +") Clan Information</font>" + isVipClan + btnRetroceder);
		String btnNombreClan = "<button value=\"%NOMBRE%\" width=150 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN_SEE;" + String.valueOf(Pagina) + ";%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnGetAllPlayers = "<button value=\"Gets all Player\" width=155 height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLANALLPLAYER;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ String.valueOf(lvClan) +";"+ String.valueOf(IdClan) +"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		//CLANALLPLAYER

		int idAlly = getIdAllyFromClan(IdClan);

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Friends Alliance</font>",false);

		if(idAlly>0){
			Vector<String> ClanesFromAlly = getClanFromAlly(idAlly);
			if((ClanesFromAlly !=null) && (ClanesFromAlly.size()>1)){
				String grillaTemporal = "<table with=650 align=center>";
				int contadorFilas = 0;
				for(String clann : ClanesFromAlly){
					String[] infoClan = clann.split(";");
					if(contadorFilas==0){
						grillaTemporal += "<tr>";
					}
					if(IdClan != Integer.valueOf(infoClan[1])){
						grillaTemporal += "<td width=216 align=CENTER>"+ btnNombreClan.replace("%NOMBRE%", infoClan[0]).replace("%IDCLAN%", infoClan[1]).replace("%LVLCLAN%", infoClan[2]) +"</td>";
					}
					contadorFilas++;
					if(contadorFilas == 3){
						contadorFilas = 0;
						grillaTemporal += "</tr>";
					}
				}
				if(contadorFilas!=0){
					for(int i=contadorFilas;i<=3;i++){
						grillaTemporal += "<td></td>";
					}
					grillaTemporal += "</tr>";
				}
				grillaTemporal += "</table>";
				retorno += cbManager._formBodyComunidad(grillaTemporal,cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name()));
			}else{
				retorno += cbManager._formBodyComunidad("Without Friends Alliance",cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name()));
			}
		}else{
			retorno += cbManager._formBodyComunidad("Without Friends Alliance",cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name()));
		}

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Residences</font>",false);

		String grillaResidence = "<table width=750 align=center><tr>" +
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Clan Hall:</font> "+ getClanHallFromClan(IdClan) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Castle:</font> "+ getCastleFromClan(IdClan) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Fortress:</font> "+ getFortressFromClan(IdClan) +"</td>"+
								"</tr></table>";

		retorno += cbManager._formBodyComunidad(grillaResidence,cbManager.getFontColor(enumColor.COLOR_GRILLA_AZUL.name()));

		int allMembers = getCountAllPplFromClan(IdClan), OnLineMembers = 0, HeroesMember = getCountHeroesFromClan(IdClan);

		for(L2PcInstance ppl : opera.getAllPlayerOnWorld()){
			if(ppl.getClan()!=null){
				if(ppl.getClan().getId() == IdClan){
					OnLineMembers++;
				}
			}
		}

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Members</font>",false);

		grillaResidence = "<table width=750 align=center><tr>" +
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">All Members:</font> "+ String.valueOf(allMembers) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Online Members:</font> "+ String.valueOf(OnLineMembers) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Heroes Members:</font> "+ String.valueOf(HeroesMember) +"</td></tr>"+
								"<tr><td width=250></td><td width=250 align=CENTER>"+btnGetAllPlayers+"</td><td width=250></td></tr>"+
								"</table>";

		retorno += cbManager._formBodyComunidad(grillaResidence,cbManager.getFontColor(enumColor.COLOR_GRILLA_VERDE.name()));

		Vector<String> ClanesWar = getAllClanWar(IdClan);

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Clan Wars</font>",false);

		if(ClanesWar != null){
			if(ClanesWar.size()>0){
				String btnNombreClan2 = "<button value=\"%TITULO%\" width=150 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN_SEE;" + String.valueOf(Pagina) + ";%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
				String grillaTemporal = "<table with=650 align=center>";
				int contadorFilas = 0;
				for(String clanInfo:ClanesWar){
					String[] infoClan = clanInfo.split(";");
					if(contadorFilas==0){
						grillaTemporal += "<tr>";
					}
					//IDCLAN;TITULO_BTN;CLAN_NAME;CLAN_LEVEL
					grillaTemporal += "<td width=216 align=CENTER>"+ btnNombreClan2.replace("%TITULO%", infoClan[1]).replace("%NOMBRE%", infoClan[2]).replace("%IDCLAN%", infoClan[0]).replace("%LVLCLAN%", infoClan[3]) +"</td>";
					contadorFilas++;
					if(contadorFilas == 3){
						contadorFilas = 0;
						grillaTemporal += "</tr>";
					}
				}
				if(contadorFilas!=0){
					for(int i=contadorFilas;i<=3;i++){
						grillaTemporal += "<td></td>";
					}
					grillaTemporal += "</tr>";
				}
				grillaTemporal += "</table>";
				retorno += cbManager._formBodyComunidad(grillaTemporal,cbManager.getFontColor(enumColor.COLOR_GRILLA_ROJO.name()));
			}else{
				retorno += cbManager._formBodyComunidad("In peace",cbManager.getFontColor(enumColor.COLOR_GRILLA_ROJO.name()));
			}
		}else{
			retorno += cbManager._formBodyComunidad("In peace",cbManager.getFontColor(enumColor.COLOR_GRILLA_ROJO.name()));
		}

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getAllClan(L2PcInstance player, int Pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Clans</font>" + btnMain);
		String btnNombreClan = "<button value=\"%NOMBRE%\" width=150 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN_SEE;" + String.valueOf(Pagina - 1) + ";%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int PorPagina = general.COMMUNITY_BOARD_CLAN_LIST;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
						"<tr>" +
						"<td width=30 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Lv</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Name</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Alliance</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Leader</font></td>" +
						"<td width=80 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Members</font></td>" +
						"<td width=30 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Heroes</font></td>" +
						"</tr></table>";
		TablaSTR += "<table width=750 align=CENTER>";

		String strMySql = "SELECT clan_data.clan_id, clan_data.clan_level, clan_data.clan_name, IFNULL(clan_data.ally_name,\"\"), clan_data.leader_id, clan_data.reputation_score,"+
				"(SELECT count(*) FROM characters WHERE characters.clanid = clan_data.clan_id),"+
				"(SELECT characters.char_name FROM characters WHERE characters.charId = clan_data.leader_id),"+
				"(SELECT count(*) FROM characters WHERE characters.charId IN (SELECT heroes.charId FROM heroes) AND characters.clanid = clan_data.clan_id) "+
				"FROM clan_data ORDER BY clan_data.clan_level DESC LIMIT " + String.valueOf(Pagina * PorPagina) + "," + String.valueOf(((Pagina+1) * PorPagina) + 1);

		//String []ColoresGrilla = {cbManager.getFontColor(cbManager.enumColor.Gris.name()), cbManager.getFontColor(cbManager.enumColor.Verde.name()) };
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean haveNext = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;

			while (rss.next() && !haveNext){
				try{
					if(contador <= PorPagina){
						TablaSTR += "<tr>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(2)) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + btnNombreClan.replace("%NOMBRE%", rss.getString(3)).replace("%IDCLAN%", String.valueOf(rss.getInt(1))).replace("%LVLCLAN%", String.valueOf(rss.getInt(2))) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(4) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(8) +"</font></td>" +
								"<td width=80 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(7)) +"</font></td>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(9)) +"</font></td>" +
								"</tr>";
					}else{
						haveNext = true;
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table><br><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Clan Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		retorno += cbManager._formBodyComunidad(TablaSTR + TablaControles);

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getAllTopPlayerHeroes(L2PcInstance player, int pagina, String Parametros){

		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";HEROES;" + String.valueOf(pagina - 1) + ";"+Parametros+";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";HEROES;" + String.valueOf(pagina + 1) + ";"+Parametros+";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int MaximoVer = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		String desde = String.valueOf(pagina * general.COMMUNITY_BOARD_ROWS_FOR_PAGE);

		String retorno = "<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Heroes</font>" + btnMain);
		HashMap<Integer, String> TipoBusquedas = new HashMap<Integer, String>();
		TipoBusquedas.put(0, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.onlinetime DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(1, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.onlinetime ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Menor a Mayor
		TipoBusquedas.put(2, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pkkills DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(3, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pkkills ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Menor a Mayor
		TipoBusquedas.put(4, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pvpkills DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(5, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pvpkills ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Menor a Mayor
		TipoBusquedas.put(6, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.char_name DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(7, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.char_name ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor

		String Consulta = "SELECT characters.char_name," +
				"characters.level," +
				"characters.base_class," +
				"characters.pvpkills," +
				"characters.pkkills,"+
				"IFNULL((SELECT CONCAT(clan_data.clan_name, \"(Lv \", clan_data.clan_level ,\")\") FROM clan_data WHERE clan_data.clan_id = characters.clanid),\"\"),"+
				"characters.online,"+
				"characters.onlinetime,"+
				"(SELECT heroes.count FROM heroes WHERE heroes.charId = characters.charId),"+
				"characters.classid"+
				" FROM characters " + TipoBusquedas.get(Integer.valueOf(Parametros));

		//_log.warning(Consulta);

		String btnMaestro = "<button value=\"%NOMBRE%\" width=%TAM% height=23 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";HEROES;0;%ACCION%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnPvP = btnMaestro.replace("%NOMBRE%", "PvP").replace("%TAM%", "70").replace("%ACCION%", ( Integer.valueOf(Parametros)==4 ? "5" : "4"));
		String btnPk = btnMaestro.replace("%NOMBRE%", "Pk").replace("%TAM%", "70").replace("%ACCION%", ( Integer.valueOf(Parametros)==2 ? "3" : "2"));
		String btnOnline = btnMaestro.replace("%NOMBRE%", "Online").replace("%TAM%", "110").replace("%ACCION%", (Integer.valueOf(Parametros)==0 ? "1" : "0"));
		String btnNombre = btnMaestro.replace("%NOMBRE%", "Name").replace("%TAM%", "170").replace("%ACCION%", (Integer.valueOf(Parametros)==6 ? "7" : "6"));



		String grillaMostrar = "<table width=705 align=CENTER border=0><tr>"+
								"<td align=center width=170>"+btnNombre+"</td>"+
								"<td align=center width=25>"+btnMaestro.replace("%NOMBRE%", "Lv").replace("%TAM%", "25").replace("%ACCION%", "0")+"</td>"+
								"<td align=center width=70>"+btnPvP+"</td>"+
								"<td align=center width=70>"+btnPk+"</td>"+
								"<td align=center width=160>"+btnMaestro.replace("%NOMBRE%", "Clan").replace("%TAM%", "160").replace("%ACCION%", "0")+"</td>"+
								"<td align=center width=110>"+btnOnline+"</td>"+
								"<td align=center width=100>"+btnMaestro.replace("%NOMBRE%", "Status").replace("%TAM%", "100").replace("%ACCION%", "0")+"</td></tr>";

		Connection conn = null;

		boolean haveNext = false;

		int contador = 1;

		try{
			conn  = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			String []ColoresGrilla = new String[2];
			ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
			ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());

			String TipoConecion = "";

			while (rss.next()){
				try{
					L2PcInstance playerBus = null;
					try{
						playerBus = opera.getPlayerbyName(rss.getString(1));
						if(playerBus == null){
							TipoConecion = "OFFLINE";
						}else{
							if(playerBus.getClient().isDetached()){
								if(playerBus.isInCraftMode()){
									TipoConecion = "OFF. CRAFT";
								}else if(playerBus.isInStoreMode()){
									TipoConecion = "OFF. STORE";
								}
							}else{
								TipoConecion = "ONLINE";
							}
						}
					}catch(Exception a){
						TipoConecion = "OFFLINE";
					}

					if(contador <= general.COMMUNITY_BOARD_ROWS_FOR_PAGE){

						//";HEROES;0;%ACCION%;0;0\"

						String NombreCharConLink = "<a action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";HEROES_SHOW_PPL;"+ String.valueOf(pagina) +";" + Parametros + ";" + rss.getString(1) +";0\"><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(1) + " ("+ String.valueOf(rss.getInt(9)) +")</font></a>";

						grillaMostrar += "<tr>" +
								"<td width=170 align=CENTER>"+ NombreCharConLink +"<br><font color="+ ColoresGrilla[contador%2] +">" + opera.getClassName(rss.getInt(3))  +"</font><br></td>" +
								"<td width=25 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(2)) +"</font><br></td>" +
								"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(4)) +"</font><br></td>" +
								"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(5)) +"</font><br></td>" +
								"<td width=160 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(6) +"</font><br></td>" +
								"<td width=110 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getTiempoON(rss.getInt(8)) +"</font><br></td>" +
								"<td width=100 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + TipoConecion +"</font><br></td>" +
								"</tr>";
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
		grillaMostrar += "</table>";

		if(contador>general.COMMUNITY_BOARD_ROWS_FOR_PAGE){
			haveNext = true;
		}


		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Heroes Page "+ String.valueOf(pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";



		retorno += cbManager._formBodyComunidad(grillaMostrar + TablaControles);
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	private static String getAllChangeLog(L2PcInstance player, int Pagina){
		String Retorno = "";

		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANGELOG_MODIF;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANGELOG_DELETE;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String strTableConfig = "<table width=180><tr><td width=90>"+btnModificar+"</td><td width=90>"+btnEliminar+"</td></tr></table>";

		int PorPagina = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANGELOG;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANGELOG;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
						"<tr>" +
						"<td width=30 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Nº</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Date</font></td>" +
						"<td width=585 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Change</font></td>" +
						"</tr></table>";
		TablaSTR += "<table width=750 align=CENTER>";

		//String []ColoresGrilla = {cbManager.getFontColor(cbManager.enumColor.Gris.name()), cbManager.getFontColor(cbManager.enumColor.Verde.name()) };
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		String strMySql = "select * from zeus_annoucement where zeus_annoucement.tipo = 'CHANGELOG' order by zeus_annoucement.fecha DESC LIMIT " + String.valueOf(Pagina * PorPagina) + "," + String.valueOf(PorPagina + 1) ;
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean haveNext = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;
			while (rss.next() && !haveNext){
					try{
						if(contador <= PorPagina){
							String GrillaModifMensaje = "<br1>" + strTableConfig.replace("%IDMENSAJE%", String.valueOf(rss.getInt(1)));
							if(!player.isGM()){
								GrillaModifMensaje = "";
							}

							TablaSTR += "<tr>" +
									"<td width=30 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (contador + ( Pagina * PorPagina  ) ) +"</font></td>" +
									"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(5) +"</font></td>" +
									"<td width=585 align=CENTER fixwidth=580><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(3) +"</font>"+GrillaModifMensaje+"</td>" +
									"</tr>";
						}else{
							haveNext = true;
						}
						contador++;
					}catch(SQLException e){
						_log.warning("ZeuS cb Error -> " + e.getMessage());
					}
				}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table><br><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change Log Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		Retorno = TablaSTR + TablaControles;

		return Retorno;
	}

	private static String getCraftStoresOnLine(L2PcInstance player, int pagina, String parametros, boolean isForSearch){


		int desde = pagina * general.COMMUNITY_BOARD_MERCHANT_LIST;
		int Hasta = (pagina * general.COMMUNITY_BOARD_MERCHANT_LIST) + general.COMMUNITY_BOARD_MERCHANT_LIST;

		String btnBackToSearch = "";
		if(isForSearch){
			desde = Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST;
			Hasta = (Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST) + general.COMMUNITY_BOARD_MERCHANT_LIST;
			btnBackToSearch = "<button value=\"Back\" width=100 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_1;" + parametros.split(";")[1] + ";"+ textToSearch.get(player.getObjectId()) +";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		}

		String btnSearch = "<button value=\"Search\" width=100 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH;" + String.valueOf(pagina) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnAtras = "";
		String btnSiguente = "";


		/*		case "STORE_SEARCH_IN_PS":
					Retorno = getCraftStoresOnLine(player,Integer.valueOf(parm3),parm2+";"+parm1,true);
		*/

		if(!isForSearch){
			btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CRAFTERPRIVATE;" + String.valueOf(pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
			btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CRAFTERPRIVATE;" + String.valueOf(pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		}else{
			//Integer.valueOf(parm3) parm2+";"+parm1
			btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_IN_PS;" + parametros.split(";")[1] + ";"+parametros.split(";")[0]+";"+ String.valueOf(pagina - 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
			btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_IN_PS;" + parametros.split(";")[1] + ";"+parametros.split(";")[0]+";"+ String.valueOf(pagina + 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		}

		String btnNameChar = "<button value=\"%NAME%\" width=133 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CRAFTERPRIVATE_GO;" + String.valueOf(pagina) + ";%NAME%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores</font>" + btnMain + btnSearch + btnBackToSearch);
		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
				"<tr>" +
				"<td width=60 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Type</font></td>" +
				"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Name</font></td>" +
				"<td width=555 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">List</font></td>" +
				"</tr></table>";
		String[] Colores = {cbManager.getFontColor(enumColor.COLOR_GRILLA_AZUL.name()), cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name())};
		int Contador = 0;
		int ContadorPagina = 1;

		int contadorBusqueda = 1;

		boolean HaveNext = false;
		boolean CreateTableItem = !isForSearch ? true : false;
		boolean continuarPorJunta = isForSearch;
		for(L2PcInstance cha: opera.getAllPlayerOnWorld()){
			if(cha.isInStoreMode()){
				if((ContadorPagina > desde) || continuarPorJunta){
					if((ContadorPagina <= Hasta) || continuarPorJunta){
										boolean isOnline = true;
										if(cha.getClient().isDetached()){
											isOnline = false;
										}
										TablaSTR = "<table width=750 align=CENTER>";
										Contador++;
										TradeItem[] itemSell = cha.getSellList().getItems();
										TradeItem[] itemBuy = cha.getBuyList().getItems();
										TradeItem[] itemSelected = null;
										String tipoVS = "";
										boolean isPackageSell = false;
										String Productos = "<table width=555>";

										boolean isSellSelected = true;

										if(general.isSellMerchant.containsKey(cha.getObjectId())){
											if(!general.isSellMerchant.get(cha.getObjectId())){
												isSellSelected = false;
											}
										}

										if(isSellSelected){
											if(itemSell !=null ){
												if(itemSell.length>0){
													itemSelected = itemSell;
													if(!cha.getSellList().isPackaged()){
														tipoVS = "Sell";
													}else{
														tipoVS = "P. Sell";
														isPackageSell = true;
													}
												}
											}
										}else{
											if(itemBuy != null){
												if(itemBuy.length>0){
													itemSelected = itemBuy;
													tipoVS = "Buy";
												}
											}
										}

										if(itemSelected==null){
											if(itemSell==null){
												if(itemBuy.length>0){
													itemSelected = itemBuy;
													isPackageSell = false;
													tipoVS = "Buy";
												}
											}else{
												if(itemSell.length==0){
													tipoVS = "Buy";
													itemSelected = itemBuy;
												}else{
													if(!cha.getSellList().isPackaged()){
														tipoVS = "Sell";
														isPackageSell = false;
													}else{
														tipoVS = "P. Sell";
														isPackageSell = true;
													}
													itemSelected = itemSell;
												}
											}
										}

										Long total = 0L;

										if(isForSearch){
											//CreateTableItem
										}
										if(isForSearch){
											CreateTableItem = false;
										}
										for(TradeItem item : itemSelected){
											boolean isEnchantable = (item.getItem().isEnchantable()==1 ? true : false);
											int EnchatItem = item.getEnchant();
											if(!isEnchantable){
												EnchatItem = 0;
											}
											if(isForSearch){
												if(item.getItem().getId()== Integer.valueOf(parametros.split(";")[0])){
													CreateTableItem = true;
												}
											}

											String itemPrice = opera.getFormatNumbers(item.getPrice());
											Productos += "<tr><td width=32>" + opera.getIconImgFromItem(item.getItem().getId()) + "</td>"+
														"<td width=500 align=LEFT>"+item.getItem().getName() + ( EnchatItem>0 ? " +" + String.valueOf(EnchatItem) : "" ) + " (Amount " + String.valueOf(item.getCount()) + (!isPackageSell ? " / $" + itemPrice : "") +")</td></tr>";
											total += item.getPrice();
										}


										Productos += "</table>";
										String strOnline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">Online</font>";
										String strOffline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Rojo.name()) + ">Offline</font>";
										String seccionNombre = "";
										if(general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT){
											seccionNombre = btnNameChar.replace("%NAME%", cha.getName()) +"<br1>"+ ( isOnline ? strOnline : strOffline );
										}else{
											seccionNombre = "<font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + ">"+cha.getName()+"</font>" +"<br1>" + ( isOnline ? strOnline : strOffline );
										}
										TablaSTR += "<tr>" +
												"<td width=60 align=CENTER>"+ tipoVS +"</td>" +
												"<td width=135 align=CENTER>"+ seccionNombre + "</td>" +
												"<td width=555 align=CENTER>"+ Productos +"</td>" +
												"</tr>";
										if(isPackageSell){
											TablaSTR += "<tr><td width=60></td><td width=135></td><td width=555 align=CENTER>TOTAL: "+ opera.getFormatNumbers(total) + "</td></tr>";
										}
										TablaSTR += "</table><br>";
										if(CreateTableItem){
											retorno += cbManager._formBodyComunidad(TablaSTR,Colores[Contador%2]);
											contadorBusqueda++;
											if(contadorBusqueda>=general.COMMUNITY_BOARD_MERCHANT_LIST){
												continuarPorJunta=false;
											}
										}
					}else if(ContadorPagina>Hasta){
						HaveNext = true;
					}
				}
				if(CreateTableItem){
					ContadorPagina++;
				}
			}
		}


		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores Page "+ String.valueOf(pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( HaveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		retorno += cbManager._formBodyComunidad(TablaControles);

		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}



	private static String getCraftStoresOnLineSearch(L2PcInstance player, int pagina, String parametros, boolean buscar){


		int desde = Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST;
		int Hasta = (Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST) + general.COMMUNITY_BOARD_MERCHANT_LIST;
		String btnBackToSearch = "<button value=\"Back\" width=100 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_1;" + parametros.split(";")[1] + ";"+ textToSearch.get(player.getObjectId()) +";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSearch = "<button value=\"Search\" width=100 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH;" + String.valueOf(pagina) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnAtras = "";
		String btnSiguente = "";

		btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_IN_PS;" + String.valueOf(pagina) + ";"+parametros.split(";")[0]+";"+ String.valueOf(Integer.valueOf(parametros.split(";")[1]) - 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STORE_SEARCH_IN_PS;" + String.valueOf(pagina) + ";"+parametros.split(";")[0]+";"+ String.valueOf(Integer.valueOf(parametros.split(";")[1]) + 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnNameChar = "<button value=\"%NAME%\" width=133 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CRAFTERPRIVATE_GO;" + String.valueOf(pagina) + ";%NAME%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores</font>" + btnMain + btnSearch + btnBackToSearch);
		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
				"<tr>" +
				"<td width=60 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Type</font></td>" +
				"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Name</font></td>" +
				"<td width=555 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">List</font></td>" +
				"</tr></table>";
		String[] Colores = {cbManager.getFontColor(enumColor.COLOR_GRILLA_AZUL.name()), cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name())};
		int Contador = 0;
		int ContadorPagina = 1;
		boolean HaveNext = false;
		boolean CreateTableItem = false;
		boolean seguirBuscando = true;
		for(L2PcInstance cha: opera.getAllPlayerOnWorld()){
			if(cha.isInStoreMode()){
				if(seguirBuscando){
						boolean isOnline = true;
						if(cha.getClient().isDetached()){
							isOnline = false;
						}
						TablaSTR = "<table width=750 align=CENTER>";
						TradeItem[] itemSell = cha.getSellList().getItems();
						TradeItem[] itemBuy = cha.getBuyList().getItems();
						TradeItem[] itemSelected = null;
						String tipoVS = "";
						boolean isPackageSell = false;
						String Productos = "<table width=555>";

						boolean isSellSelected = true;

						if(general.isSellMerchant.containsKey(cha.getObjectId())){
							if(!general.isSellMerchant.get(cha.getObjectId())){
								isSellSelected = false;
							}
						}

						if(isSellSelected){
							if(itemSell !=null ){
								if(itemSell.length>0){
									itemSelected = itemSell;
									if(!cha.getSellList().isPackaged()){
										tipoVS = "Sell";
									}else{
										tipoVS = "P. Sell";
										isPackageSell = true;
									}
								}
							}
						}else{
							if(itemBuy != null){
								if(itemBuy.length>0){
									itemSelected = itemBuy;
									tipoVS = "Buy";
								}
							}
						}

						if(itemSelected==null){
							if(itemSell==null){
								if(itemBuy.length>0){
									itemSelected = itemBuy;
									isPackageSell = false;
									tipoVS = "Buy";
								}
							}else{
								if(itemSell.length==0){
									tipoVS = "Buy";
									itemSelected = itemBuy;
								}else{
									if(!cha.getSellList().isPackaged()){
										tipoVS = "Sell";
										isPackageSell = false;
									}else{
										tipoVS = "P. Sell";
										isPackageSell = true;
									}
									itemSelected = itemSell;
								}
							}
						}

						Long total = 0L;
						CreateTableItem = false;
						for(TradeItem item : itemSelected){
							boolean isEnchantable = (item.getItem().isEnchantable()==1 ? true : false);
							int EnchatItem = item.getEnchant();
							if(!isEnchantable){
								EnchatItem = 0;
							}
							if(item.getItem().getId()== Integer.valueOf(parametros.split(";")[0])){
								CreateTableItem = true;
							}
							String itemPrice = opera.getFormatNumbers(item.getPrice());
							Productos += "<tr><td width=32>" + opera.getIconImgFromItem(item.getItem().getId()) + "</td>"+
										"<td width=500 align=LEFT>"+item.getItem().getName() + ( EnchatItem>0 ? " +" + String.valueOf(EnchatItem) : "" ) + " (Amount " + String.valueOf(item.getCount()) + (!isPackageSell ? " / $" + itemPrice : "") +")</td></tr>";
							total += item.getPrice();
						}


						Productos += "</table>";
						String strOnline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">Online</font>";
						String strOffline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Rojo.name()) + ">Offline</font>";
						String seccionNombre = "";
						if(general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT){
							seccionNombre = btnNameChar.replace("%NAME%", cha.getName()) +"<br1>"+ ( isOnline ? strOnline : strOffline );
						}else{
							seccionNombre = "<font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + ">"+cha.getName()+"</font>" +"<br1>" + ( isOnline ? strOnline : strOffline );
						}
						TablaSTR += "<tr>" +
								"<td width=60 align=CENTER>"+ tipoVS +"</td>" +
								"<td width=135 align=CENTER>"+ seccionNombre + "</td>" +
								"<td width=555 align=CENTER>"+ Productos +"</td>" +
								"</tr>";
						if(isPackageSell){
							TablaSTR += "<tr><td width=60></td><td width=135></td><td width=555 align=CENTER>TOTAL: "+ opera.getFormatNumbers(total) + "</td></tr>";
						}
						TablaSTR += "</table><br>";
						if(CreateTableItem){
							if((ContadorPagina>desde) && (ContadorPagina<=Hasta)){
								retorno += cbManager._formBodyComunidad(TablaSTR,Colores[Contador%2]);
								Contador++;
							}else if(ContadorPagina>Hasta){
								seguirBuscando = false;
								HaveNext=true;
							}
							ContadorPagina++;
						}
				}
			}
		}


		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Integer.valueOf(parametros.split(";")[1]) > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores Page "+ String.valueOf((Integer.valueOf(parametros.split(";")[1])) + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( HaveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		retorno += cbManager._formBodyComunidad(TablaControles);

		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getCastillos(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Castles</font>" + btnMain);

		String Consulta = "SELECT "+
						"castle.`name`,"+
						"castle.siegeDate,"+
						"castle.regTimeOver,"+
						"castle.regTimeEnd,"+
						"castle.taxPercent,"+
						"IFNULL((SELECT clan_data.clan_name FROM clan_data WHERE clan_data.hasCastle = castle.id),\"Empty\"),"+
						"IFNULL((SELECT clan_data.clan_level FROM clan_data WHERE clan_data.hasCastle = castle.id),0),"+
						"IFNULL((SELECT clan_data.ally_name FROM clan_data WHERE clan_data.hasCastle = castle.id),\"None\"),"+
						"IFNULL((SELECT clan_data.clan_id FROM clan_data WHERE clan_data.hasCastle = castle.id),0) "+
						"FROM "+
						"castle";

		String btnNombreClan = "<button value=\"%TITULO%\" width=135 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN_SEE;-99;%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String grillaMostrar = "<table width=727 align=CENTER border=0>" +
								"<tr>"+
								"<td align=center width=70><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Castle</font></td>"+
								"<td align=center width=135><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Clan</font></td>"+
								"<td align=center width=135><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Ally Name</font></td>"+
								"<td align=center width=100><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Siege Date</font></td>"+
								"<td align=center width=70><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Reg. Period</font></td>"+
								"<td align=center width=100><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Reg. Time Over</font></td>"+
								"<td align=center width=50><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Tax</font></td>"+
								"</tr>";


		Connection conn = null;
		try{
			conn  = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;

			String []ColoresGrilla = new String[2];
			ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
			ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
			while (rss.next()){
				try{
					int idClan = rss.getInt(9);
					String btnClan = "None";
					if(idClan>0){
						btnClan = btnNombreClan.replace("%TITULO%", rss.getString(6) + "(lv"+ String.valueOf(rss.getInt(7)) +")" ).replace("%NOMBRE%", rss.getString(1)).replace("%IDCLAN%", String.valueOf(rss.getInt(9))).replace("%LVLCLAN%", String.valueOf(rss.getInt(7)));
					}
					grillaMostrar += "<tr>" +
							"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(1) +"</font></td>" +
							"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + btnClan  +"</font></td>" +
							"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (idClan>0 ? rss.getString(8) : "None") +"</font></td>" +
							"<td width=100 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getDateFromUnixTime(rss.getLong(2)) +"</font></td>" +
							"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (rss.getString(3).equals("false") ? "Over" : "Started") +"</font></td>" +
							"<td width=100 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getDateFromUnixTime(rss.getLong(4)) +"</font></td>" +
							"<td width=50 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(5)) +"</font></td>" +
							"</tr>";
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		grillaMostrar += "</table>";

		retorno += cbManager._formBodyComunidad(grillaMostrar);
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;

	}

	private static String getTitulosMensaje(int Pagina){
		String Retorno = "";
		int PorPagina = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		//String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		String btnVerNoticia = "<button width=32 height=32 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_CT1.MiniMap_DF_PlusBtn_Blue_Down fore=L2UI_CT1.MiniMap_DF_PlusBtn_Blue>";
		
		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
						"<tr>" +
						"<td width=30 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Nº</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">GM</font></td>" +
						"<td width=128 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Title</font></td>" +
						"<td width=395 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Annoucement</font></td>" +
						"<td width=36 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">See</font></td>" +
						"</tr></table>";
		TablaSTR += "<table width=750 align=CENTER>";

		//String []ColoresGrilla = {cbManager.getFontColor(cbManager.enumColor.Gris.name()), cbManager.getFontColor(cbManager.enumColor.Verde.name()) };
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		String strMySql = "select * from zeus_annoucement where zeus_annoucement.tipo = 'ANNOUCEMENT' order by zeus_annoucement.fecha DESC LIMIT " + String.valueOf(Pagina * PorPagina) + "," + String.valueOf(PorPagina + 1) ;
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean haveNext = false;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;

			while (rss.next() && !haveNext){
				try{
					if(contador <= PorPagina){
						TablaSTR += "<tr>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (contador + ( Pagina * PorPagina  ) ) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(4) +"</font></td>" +
								"<td width=128 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(2).substring(0, ( rss.getString(2).length()>35 ? 35 : rss.getString(2).length() ) ) +"</font></td>" +
								"<td width=395 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(3).substring(0, ( rss.getString(3).length()>50 ? 50 : rss.getString(3).length() ) ) +"</font></td>" +
								"<td width=36 align=LEFT>"+ btnVerNoticia.replace("%IDVER%", String.valueOf(rss.getInt(1))) +"</td>" +
								"</tr>";
					}else{
						haveNext = true;
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table><br><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Annoucement Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		Retorno = TablaSTR + TablaControles;

		return Retorno;
	}

	public static String getRules(L2PcInstance player, int pagina, String Parametros){
		String btnNuevo = "<button value=\"New\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RULES_NEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Rules</font>" + btnMain + ( player.isGM() ? btnNuevo : "" ));
		retorno += cbManager._formBodyComunidad(getAllRules(player, pagina));
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String getChangeLog(L2PcInstance player, int pagina, String Parametros){
		String btnNuevo = "<button value=\"New\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANGELOGNEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change Log</font>" + btnMain + ( player.isGM() ? btnNuevo : "" ));
		retorno += cbManager._formBodyComunidad(getAllChangeLog(player,pagina));
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setPageNewMensajesChangeLog(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">New Change Log</font>");
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_CHANGELOG Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Change:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setPageNewRules(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">New Rules</font>");
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_RULES Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Rules:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setModifNewRules(L2PcInstance player, int pagina, String Parametros){
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RULES;" + String.valueOf(pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Update Rules</font>"+btnBack);
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_RULES_MOD Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Rules:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setModifMensajesChangeLog(L2PcInstance player, int pagina, String Parametros){
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANGELOG;" + String.valueOf(pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Update Change Log</font>"+btnBack);
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_CHANGELOG_MOD Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Change:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setModifMensajesVarios(L2PcInstance player, int pagina, String Parametros, String Lugar){
		//String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";"+Lugar+";0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change "+ Lugar +"</font>"+btnBack);
		String Titulo = "<edit var=\"txtTitle\" width=150>";;
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_"+Lugar+"_MOD Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:(%TITULO%)</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Annoucement:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	public static String setPageModifMensajesAnnou(L2PcInstance player, int pagina, String Parametros){
		//String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_VER;" + String.valueOf(pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change Annoucement</font>"+btnBack);
		String Titulo = "<edit var=\"txtTitle\" width=150>";;
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_ANNOUCEMENT_MOD Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:(%TITULO%)</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Annoucement:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	//AQUI
	public static String setPageNewVariosMensajes(L2PcInstance player, int pagina, String Parametros, String Seccion){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">"+Seccion+"</font>");
		String Titulo = "<edit var=\"txtTitle\" width=150>";;
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_"+Seccion+" Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Data:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	public static String setPageNewMensajesAnnou(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">New Annoucement</font>");
		String Titulo = "<edit var=\"txtTitle\" width=150>";
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_ANNOUCEMENT Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Annoucement:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String getPageStaff(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Server Staff</font>" + btnMain);

		String grillaStaff = "<table width=460 align=CENTER><tr>"+
							"<td width=15 align=CENTER>Nº</td>" +
							"<td width=180 align=CENTER>Name</td>"+
							"<td width=180 align=CENTER>Class</td>"+
							"<td width=90 align=CENTER>Status</td></tr>";


		String Consulta = "SELECT characters.charId, characters.char_name, characters.`level`, characters.classid, characters.base_class, characters.`online` FROM characters WHERE characters.accesslevel > 0";

		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;
			String []ColoresGrilla = new String[2];
			ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
			ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
			boolean showGM = true;
			while (rss.next()){
				if(rss.getInt(6)==1){
					L2PcInstance playerByName = opera.getPlayerbyName(rss.getString(2));
					if(playerByName!=null){
						showGM = opera.isGmAllVisible(playerByName, player);
					}else{
						showGM = false;
					}
				}else{
					showGM = false;
				}

				try{
					grillaStaff += "<tr>" +
							"<td width=15 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + contador +"</font></td>" +
							"<td width=180 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(2) +"</font></td>" +
							"<td width=180 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getClassName(rss.getInt(5)) +"</font></td>" +
							"<td width=90 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (showGM ? "ONLINE" : "OFFLINE")  + "</font></td>" +
							"</tr>";
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}


		grillaStaff += "</table>";

		retorno += cbManager._formBodyComunidad(grillaStaff) + ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	private static boolean canUseTeleporPrivateStore(L2PcInstance privateStore, L2PcInstance player){

		if(player.getKarma()>0){
			central.msgbox(msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_PK, player);
			return false;
		}
		if(player.isDead()){
			central.msgbox(msg.NO_PUEDES_USAR_MIS_SERVICIOS_ESTANDO_MUERTO, player);
			return false;
		}
		if(player.isInCombat() || player.isFishing() || player.isFlying() || player.isInOlympiadMode() || ((player.getPvpFlag()>0)
				|| player.isInDuel() || player.isJailed() || player.isInCraftMode() || player.isInStoreMode() || player.isIn7sDungeon()
				|| player.isInStance() || player.isOnEvent() || player.isInsideZone(ZoneId.SIEGE) )){
			central.msgbox("You can not use my services right now", player);
			return false;
		}

		if(privateStore.isInsideZone(ZoneId.NO_SUMMON_FRIEND)){
			central.msgbox(privateStore.getName()+" are inside a no summon area", player);
			return false;
		}

		if(privateStore.isInsideZone(ZoneId.SIEGE) && !general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE){
			central.msgbox( privateStore.getName()  + " are inside a siege zone" , player);
			return false;
		}

		if(privateStore.isInsideZone(ZoneId.CASTLE) && !general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE){
			if(player.getClan()==null){
				central.msgbox( privateStore.getName()  + " are inside a Castle" , player);
				return false;
			}
			if(player.getClan().getCastleId()<=0){
				central.msgbox( privateStore.getName()  + " are inside a Castle" , player);
				return false;
			}
			if(privateStore.getClan()!=null){
				if(player.getClan().getCastleId() != privateStore.getClan().getCastleId()){
					central.msgbox( privateStore.getName()  + " are inside a Castle" , player);
					return false;
				}
			}else{
				central.msgbox( privateStore.getName()  + " are inside a Castle" , player);
				return false;
			}
		}

		if(privateStore.isInsideZone(ZoneId.CLAN_HALL) && !general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN){
			if((privateStore.getClan()!=null) && (player.getClan() !=null)){
				if(privateStore.getClan()!= player.getClan()){
					central.msgbox( privateStore.getName()  + " are inside a Clanhall" , player);
					return false;
				}
			}else{
				central.msgbox( privateStore.getName()  + " are inside a Clanhall" , player);
				return false;
			}
		}

		if(!privateStore.isInsideZone(ZoneId.PEACE) && general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE){
			central.msgbox(privateStore.getName()  + " are not inside a peace Zone" , player);
			return false;
		}

		return true;
	}




	public static String getPageMensajes(L2PcInstance player, int pagina, String Parametros){
		String btnNuevo = "<button value=\"New\" width=60 height=24 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNONEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Annoucement</font>" + btnMain + (player.isGM() ? btnNuevo : ""));
		retorno += cbManager._formBodyComunidad(getTitulosMensaje(pagina));
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}
	
	private static String getInfoAccess(String NombreSeccion){
		for(String t : Engine.getBtnOption()){
			if(t.indexOf(NombreSeccion)>0){
				return t;
			}
		}
		return "";
	}

	private static String getFastMenu(L2PcInstance player){
		/*String ByPass_GoFlag = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Flagfinder.name() + ";FromMain;0;0;0;0;0";
		String ByPass_PartyLeader = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Gopartyleader.name() + ";FromMain;0;0;0;0;0";
		String ByPass_Buffer = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Buffer.name()+";FromMain;0;0;0;0;0";
		String ByPass_ClasesStadistic = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";" + Engine.enumBypass.ClasesStadistic.name() + ";0;Human;0;0;0;0";
		String ByPass_RaidBoss = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";" + Engine.enumBypass.RaidBossInfo.name() + ";FromMain;0;0;0;0;0";
		String ByPass_Donation = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";0;0;0;0;0;0";
		String ByPass_Teleport = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Teleport.name() +";FromMain;0;0;0;0;0";*/
		String retorno = "<table width=\"770\" border=0 cellpadding=-1 bgcolor=\"161414\"><tr>";
		//btnImaLink.add(String.valueOf(general.BTN_SHOW_BUFFER_CBE)+":icon.skill1297:Buffer:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_BUFFER_CB);
		for(int Data : general.COMMUNITY_MAIN_ACCESS){
			String NombreSeccion = CENTRAL_ACCESS[Data];
			String DataIn = getInfoAccess(NombreSeccion);
			retorno += "<td width=110 align=CENTER>"+
					 cbFormato.getBotonForm(DataIn.split(":")[1], "bypass " + DataIn.split(":")[3])+
	                DataIn.split(":")[2]+
	            "</td>";
		}
		/*
        "<td width=110 align=CENTER>"+
                "<button action=\""+ ByPass_GoFlag +"\" width=32 height=32 back=\"L2UI_CT1.PVP_DF_TriggerBtn\" fore=\"L2UI_CT1.PVP_DF_TriggerBtn\">"+
                "Go Flag"+
            "</td><td width=110 align=\"CENTER\">"+
                "<button action=\""+ ByPass_PartyLeader +"\" width=32 height=32 back=\"L2UI_CH3.partymatchicon\" fore=\"L2UI_CH3.partymatchicon\">"+
                "Go Party Leader"+
            "</td><td width=110 align=\"CENTER\">"+
                cbFormato.getBotonForm("icon.skill1297", ByPass_Buffer) +
                "Buffer"+
            "</td><td width=110 align=\"CENTER\">"+
                cbFormato.getBotonForm("icon.skill6280", ByPass_ClasesStadistic) +
                "Clases Stadistic"+
            "</td><td width=110 align=\"CENTER\">"+
                cbFormato.getBotonForm("icon.skillboss", ByPass_RaidBoss)+
                "Raid Boss"+
            "</td><td width=110 align=\"CENTER\">"+
                "<button action=\""+ByPass_Donation+"\" width=32 height=32 back=\"L2UI_CT1.MiniGame_DF_Icon_Wind\" fore=\"L2UI_CT1.MiniGame_DF_Icon_Wind\">"+
                "Donation"+
            "</td><td width=110 align=\"CENTER\">"+
            	cbFormato.getBotonForm("icon.skillelf",ByPass_Teleport)+
                "Teleport"+
        "</td>"*/
        retorno += "</tr></table>";
		return retorno;
	}
	
	private static String getLastAnnoucement(){
		String retorno ="<center><table width=480 border=0 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=480 align=CENTER><font name=hs12 color=FFC441>Last Annoucement</font></td></tr></table>";
		
		String Mensaje = "", Fecha = "", Nombre = "", IdAnnoucement = "";
		String Consulta = "SELECT zeus_annoucement.id, zeus_annoucement.strtitle, zeus_annoucement.strmensaje, zeus_annoucement.strgmnombre, zeus_annoucement.fecha, zeus_annoucement.tipo FROM zeus_annoucement WHERE zeus_annoucement.tipo = 'ANNOUCEMENT' ORDER BY zeus_annoucement.fecha DESC LIMIT 1";
		
		


		
		Connection conn = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			if(rss.next()){
				Mensaje = rss.getString("strmensaje");
				Fecha = rss.getString("fecha").split(" ")[0];
				Nombre = rss.getString("strgmnombre");
				IdAnnoucement = String.valueOf(rss.getInt("id"));
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}
		
		int LargoPermitido = 300;
		String ByPass = "bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO_VER;1;"+ IdAnnoucement +";0;0";
		String showmoreLink = "<a action=\""+ ByPass +"\"><font color=377AFF>...Show More</font></a>";		
		
		
		if(Mensaje.length()==0){
			return "";
		}		
		
		
		if(Mensaje.length()>LargoPermitido){
			Mensaje = Mensaje.substring(1,LargoPermitido-30) + showmoreLink;
		}
		
		if(Mensaje.length()==0){
			return "";
		}
		
		retorno += "<table width=480 border=0 background=L2UI_CT1.Windows_DF_TooltipBG height=150>"+
                         "<tr>"+
                             "<td fixwidth=480 align=CENTER>"+
                                 "<table width=460 border=0 background=L2UI_CT1.Windows_DF_TooltipBG>"+
                                        "<tr>"+
                                             "<td fixwidth=460 align=RIGHT>"+
                                                 Mensaje +
                                             "</td>"+
                                        "</tr>"+
                                 "</table>"+
                                 "<table width=460 border=0 background=L2UI_CT1.Windows_DF_TooltipBG>"+
                                        "<tr>"+
                                             "<td fixwidth=460 align=RIGHT>"+
                                                 "<font color=3E738B>Posted on "+ Fecha +" by</font> <font color=41C4FF>"+ Nombre +"</font>"+
                                             "</td>"+
                                        "</tr>"+
                                 "</table><br>"+
                             "</td>"+
                         "</tr>"+
                  "</table></center>";
		
		return retorno;
	}
	
	public static String getMenuCentral(L2PcInstance player){
		
		
		
		String Central = "<table width=750><tr><td width=270>%COL1%</td><td width=460>%COL2%</td></tr></table>";
		
		boolean isExpSpBlock = general.getCharConfigEXPSP(player);
		boolean isTradeRefusal = !general.getCharConfigTRADE(player);
		boolean isBadBuffProtec = general.getCharConfigBADBUFF(player);
		boolean isHideStore = general.getCharConfigHIDESTORE(player);
		boolean isPublicStat = general.getCharConfigSHOWSTAT(player);
		boolean isRefusal = general.getCharConfigREFUSAL(player);
		boolean isPartyMatching = general.getCharConfigPartyMatching(player);
		
		String Activo = "<img src=\"L2UI_CT1.Gauge_DF_SSQ_Dawn\" width=70 height=20>";
		String Desactivado = "<img src=\"L2UI_CT1.Gauge_DF_SSQ_Dusk\" width=70 height=20>";
		
		String Co = "<table width=260><tr><td>%DATO%</td></tr></table>";
		
		String btnChange = "<button value=\"Change\" width=55 height=20 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANG;ID_CH;0;0;0\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		
		String ByPassSeeMyInfo = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.MyInfo.name() + ";0;0;0;0;0;0";
		
		String btnSeeMyInfo = "<button value=\"My Info\" width=200 height=32 action=\""+ ByPassSeeMyInfo +"\" back=\"L2UI_CT1.OlympiadWnd_DF_Watch\" fore=\"L2UI_CT1.OlympiadWnd_DF_Watch\">";
		
		String Co1 = "<table width=260 bgcolor=161619>"+
				"<tr>"+
					"<td align=CENTER fixwidth=260><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +" align=CENTER>Character Control Panel</font></td>"+
				"</tr></table>"+
				"<table bgcolor=161619 width=260>"+
				"<tr><td width=120 align=RIGHT><font color="+ cbManager.getFontColor(cbManager.enumColor.Amarillo.name()) +">Block Exp/Sp:</font></td><td width=60>"+btnChange.replace("ID_CH", "expsp") + "</td><td width=80 align=LEFT>" + (!isExpSpBlock ? Activo : Desactivado) +"</td></tr>"+
				"<tr><td width=120 align=RIGHT><font color="+ cbManager.getFontColor(cbManager.enumColor.Amarillo.name()) +">Trade Refusal:</font></td><td width=60>"+btnChange.replace("ID_CH", "tradec")+ "</td><td width=80 align=LEFT>" + (!isTradeRefusal ? Activo : Desactivado) + "</td></tr>"+
				"<tr><td width=120 align=RIGHT><font color="+ cbManager.getFontColor(cbManager.enumColor.Amarillo.name()) +">Badbuff Protection:</font></td><td width=60>"+btnChange.replace("ID_CH","noBuff")+ "</td><td width=80 align=LEFT>" + (isBadBuffProtec ? Activo : Desactivado) +"</td></tr>"+
				"<tr><td width=120 align=RIGHT><font color="+ cbManager.getFontColor(cbManager.enumColor.Amarillo.name()) +">Hide Stores:</font></td><td width=60>"+btnChange.replace("ID_CH", "hideStore")+ "</td><td width=80 align=LEFT>" + (isHideStore ? Activo : Desactivado) + "</td></tr>"+
				"<tr><td width=120 align=RIGHT><font color="+ cbManager.getFontColor(cbManager.enumColor.Amarillo.name()) +">Public Self Stats:</font></td><td width=60>"+btnChange.replace("ID_CH","showmystat")+ "</td><td width=80 align=LEFT>" + (isPublicStat ? Activo : Desactivado) +"</td></tr>"+
				"<tr><td width=120 align=RIGHT><font color="+ cbManager.getFontColor(cbManager.enumColor.Amarillo.name()) +">Refusal Mode:</font></td><td width=60>"+btnChange.replace("ID_CH", "Refusal")+ "</td><td width=80 align=LEFT>" + (isRefusal ? Activo : Desactivado) +"</td></tr>"+
				"<tr><td width=120 align=RIGHT><font color="+ cbManager.getFontColor(cbManager.enumColor.Amarillo.name()) +">P.Matching Refusal:</font></td><td width=60>"+btnChange.replace("ID_CH", "partymatching")+ "</td><td width=80 align=LEFT>" + (isPartyMatching ? Activo : Desactivado) +"</td></tr>"+
				"</table><table width=260 bgcolor=161619>"+
				"<tr>"+
					"<td align=CENTER fixwidth=260>"+ btnSeeMyInfo +"</td>"+
				"</tr></table>";
		
		Co = Co.replace("%DATO%", Co1);
		
		
		String B1 = "<table width=490><tr>" +
					"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_ANNOUCEMENT +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";ANNO;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
					"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_CHANGE_LOG +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHANGELOG;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
					"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_RULES +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RULES;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
					"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_STAFF +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";STAFF;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
					"</tr></table><br1>";

		B1 += /*cbManager._formLine(450) + */"<br1>" ;
		
		B1 += "<table width=490><tr>" +
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_SV_CONFIG +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";RATE;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_FEATURES +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";FEATURES;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_EVENTS +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";EVENTS;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_PLAYGAME +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";PLAYGAME;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"</tr></table><br1>";
		
		B1 += /*cbManager._formLine(450) + */"<br1>" ;
		
		B1 += "<table width=490><tr>" +
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_TOP_PLAYER +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";TOPPLAYER;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_HEROES +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";"+ enumBypass.HeroList.name() +";0;Human;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_CLAN +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLAN;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"<td width=122 align=CENTER><button value=\""+ msg.CB_BTN_CASTLE +"\" width=113 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CASTLE;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
				"</tr></table><br>";
		
		B1 += getLastAnnoucement();
		
		
		String Titulo = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		String Respuesta = cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name());
		
		String A1 = "<table width=770 bgcolor=040416><tr>"+
					"<td width=130 align=CENTER><font color="+ Titulo +">Online Player: </font><font color="+Respuesta+">"+ String.valueOf(opera.getOnlinePlayer()) +"</font></td>"+
					"<td width=130 align=CENTER><font color="+ Titulo +">Offtraders: </font><font color="+Respuesta+">"+String.valueOf(opera.getOffliners())+"</font></td>"+
					"<td width=290 align=CENTER><font color="+ Titulo +">Last Restarted: </font><font color="+Respuesta+">"+general.getBeginDate()+"</font></td>"+
					"<td with=220 align=LEFT><font color="+ Titulo +">Time: </font><font color="+Respuesta+">"+opera.getHoraActual()+"</font></td>"+
					"</tr></table>";
		
		String A3 = cbManager._formLine(790) + getFastMenu(player) + cbManager._formLine(790);
		
		String B2 = cbManager._formLine(790) + "<br1>" + A1 + cbManager._formLine(790) + "<br1>" +A3 ;
		
		String CantidadSinLeer = "";
		String btnSoloGM = "";
		
		if(player.isGM()){
			CantidadSinLeer = String.valueOf( cbManager.getNoReadBugReport());
			btnSoloGM = "<button value=\"BUG REPORT ("+CantidadSinLeer+")\" width=180 height=28 action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";BUG_REPORT;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";			
		}
		
		B2 += cbManager._formLine(790) + "<br1>" + cbManager._formLine(790) ;
		B2 += "<table width=770>" +
				"<tr><td width=770 align=CENTER>"+ getMemoryStatusServer(player) +"</td></tr>";
		if(btnSoloGM.length()>0){
			B2 += "<tr><td width=770 align=CENTER>"+ btnSoloGM +"</td></tr>";
		}
		
		B2 += "</table>";
				

		String Retorno = Central.replace("%COL1%", Co).replace("%COL2%", B1);
		
		Retorno += "<br>" + B2;
		
		return Retorno;
	}



	public static String delegar(L2PcInstance player, String params){
		String Retorno = "";

		if(params == null){
			return getComunidad(player, "", 0);
		}

		if(params.contains(";")){
			_log.warning("Community-> " + params);
			String[] Eventos = params.split(";");
			String Event = Eventos[1];
			String parm1 = Eventos[2];
			String parm2 = Eventos[3];
			String parm3 = Eventos[4];
			String parm4 = Eventos[5];
			switch (Event){
				case "CHANG":
					opera.setPlayerConfig(parm1, player);
					Retorno = getComunidad(player, "", 0); 
					break;
				case "ANNO":
					Retorno = getPageMensajes(player,Integer.valueOf(parm1),"");
					break;
				case "ANNONEW":
					Retorno = setPageNewMensajesAnnou(player, 0 , "");
					break;
				case "ANNO_VER":
					Retorno = getMensajeParaLeer(player, Integer.valueOf(parm2),Integer.valueOf(parm1));
					break;
				case "ANNO_MODIF":
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String AnnModif = setPageModifMensajesAnnou(player, Integer.valueOf(parm1),"");
					String Annuncio = getAnnoucementByID(Integer.valueOf(parm2),true);
					String Titulo = getAnnoucementByID(Integer.valueOf(parm2),false);
					AnnModif = AnnModif.replace("%TITULO%", Titulo).replace("%IDVER%", parm2);
					cbManager.send1001(AnnModif, player);
					cbManager.send1002(player, Annuncio, " ", "0");
					Retorno = "";
					break;
				case "ANNO_DELETE":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getPageMensajes(player,0,"");
					break;
				case "CHANGELOG":
					Retorno = getChangeLog(player, Integer.valueOf(parm1), "");
					break;
				case "CHANGELOG_MODIF":
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String ChangeLogMensaje = getAnnoucementByID(Integer.valueOf(parm2),true);
					String ChangeLogModif = setModifMensajesChangeLog(player, Integer.valueOf(parm1),"");
					cbManager.send1001(ChangeLogModif, player);
					cbManager.send1002(player, ChangeLogMensaje, " ", "0");
					break;
				case "CHANGELOG_DELETE":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getChangeLog(player, 0, "");
					break;
				case "CHANGELOGNEW":
					Retorno = setPageNewMensajesChangeLog(player,Integer.valueOf(parm1),"");
					break;
				case "RULES":
					Retorno = getRules(player,Integer.valueOf(parm1),"");
					break;
				case "RULES_NEW":
					Retorno = setPageNewRules(player, Integer.valueOf(parm1), "");
					break;
				case "RULES_MODIF":
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String RulesMensaje = getAnnoucementByID(Integer.valueOf(parm2),true);
					String RulesModif = setModifNewRules(player, Integer.valueOf(parm1),"");
					cbManager.send1001(RulesModif, player);
					cbManager.send1002(player, RulesMensaje, " ", "0");
					break;
				case "RULES_DELETE":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getRules(player,0,"");
					break;
				case "STAFF":
					Retorno = getPageStaff(player,Integer.valueOf(parm1),"");
					break;
				case "TOPPLAYER_SHOW_PPL":
					opera.getInfoPlayer(parm2, player);
					break;
				case "TOPPLAYER":
					Retorno = getAllTopPlayer(player,Integer.valueOf(parm1),"");
					break;
				case "HEROES_SHOW_PPL":
					opera.getInfoPlayer(parm3, player);
				case "HEROES":
					Retorno = getAllTopPlayerHeroes(player,Integer.valueOf(parm1),parm2);
					break;
				case "CLAN":
					PaginaVerAllPlayer.put(player, 0);
					if(Integer.valueOf(parm1)>=0){
						Retorno = getAllClan(player,Integer.valueOf(parm1),"");
					}else{
						Retorno = getCastillos(player,Integer.valueOf(parm1),"");
					}
					break;
				case "CLAN_SEE":
					PaginaVerAllPlayer.put(player, 0);
					Retorno = getInfoClan(player, Integer.valueOf(parm1), "", Integer.valueOf(parm2), parm3, Integer.valueOf(parm4));
					break;
				case "CLAN_ALL_MORE":
					PaginaVerAllPlayer.put(player, PaginaVerAllPlayer.get(player) + 1);
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm4), parm2, parm3);
					break;
				case "CLAN_ALL_MENUS":
					PaginaVerAllPlayer.put(player, PaginaVerAllPlayer.get(player) - 1);
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm4), parm2, parm3);
					break;
				case "CLANALLPLAYER":
					//action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CLANALLPLAYER;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ String.valueOf(lvClan) +";"+ String.valueOf(IdClan) +"\"
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm4), parm2, parm3);
					break;
				case "CHARINFO":
					opera.getInfoPlayer(parm2, player);
					//<a action=\"bypass " + general.COMMUNITY_BOARD_PART_EXEC + ";CHARINFO;"+ String.valueOf(Pagina) +";"+ player.getName() +";"+String.valueOf(idClan)+";"+ NombreClan + "-" + lvlClan +"\">"
					//getAllPlayerFromClan(player, Pagina, idClan, NombreClan, lvlClan)
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm3), parm4.split("-")[0], parm4.split("-")[1]);
					break;
				case "CASTLE":
					Retorno = getCastillos(player,Integer.valueOf(parm1),"");
					break;
				case "CRAFTERPRIVATE":
					Retorno = getCraftStoresOnLine(player,Integer.valueOf(parm1),"",false);
					break;
				case "CRAFTERPRIVATE_GO":
					L2PcInstance playerToGo = opera.getPlayerbyName(parm2);
					if(playerToGo!=null){
						if(playerToGo != player){
							if(canUseTeleporPrivateStore(playerToGo,player)){
								Location gotoDir = playerToGo.getLocation();
								player.teleToLocation(gotoDir, true);
							}
						}
					}
					Retorno = getCraftStoresOnLine(player,Integer.valueOf(parm1),"",false);
					break;
				case "RATE":
					Retorno = getServerInformationRate(player, Integer.valueOf(parm1));
					break;
				case "FEATURES_NEW":
				case "EVENTS_NEW":
				case "PLAYGAME_NEW":
					Retorno = setPageNewVariosMensajes( player,0,"", Event.replace("_NEW", "") );
					break;
				case "FEATURES":
				case "EVENTS":
				case "PLAYGAME":
					Retorno = getExplica(player,Event,0);
					break;
				case "FEATURES_MOD":
				case "EVENTS_MOD":
				case "PLAYGAME_MOD":
					String MensajesVarios = setModifMensajesVarios(player,0,"",Event.replace("_MOD", ""));
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String strMensaje = getAnnoucementByID(Integer.valueOf(parm2),true);
					String strTitulo = getAnnoucementByID(Integer.valueOf(parm2),false);
					String strAnnModif = MensajesVarios.replace("%TITULO%", strTitulo).replace("%IDVER%", parm2);
					cbManager.send1001(strAnnModif, player);
					cbManager.send1002(player, strMensaje, " ", "0");
					Retorno = "";
					break;
				case "FEATURES_DEL":
				case "EVENTS_DEL":
				case "PLAYGAME_DEL":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getExplica(player, Event.replace("_DEL", ""),0);
					break;
				case "STORE_SEARCH_1":
					textToSearch.put(player.getObjectId(), parm2);
					ItemForSearch.put(player.getObjectId(), opera.getAllItemByName(parm2, true));
					Retorno = getSearchDrop(player,Integer.valueOf(parm1),parm2,true);
					break;
				case "STORE_SEARCH_CAM_PAG":
					//general.COMMUNITY_BOARD_PART_EXEC+";STORE_SEARCH_1;0;"+arg5+";0;0";
					Retorno = getSearchDrop(player,Integer.valueOf(parm1),parm2,true);
					break;
				case "STORE_SEARCH":
					//getSearchDrop(L2PcInstance player, int pagina, String parametros, boolean ListarItemEncontrados){
					Retorno = getSearchDrop(player,Integer.valueOf(parm1),parm2,false);
					//;STORE_SEARCH;" + String.valueOf(pagina) + ";0;0;0
					break;
				case "STORE_SEARCH_IN_PS":
					Retorno = getCraftStoresOnLineSearch(player,Integer.valueOf(parm1),parm2+";"+parm3,true);
					break;
				case "BUG_REPORT_SEE":
					getBugReportWindows(player,Integer.valueOf(parm2));
				case "BUG_REPORT":
					Retorno = getBugReport(player,Integer.valueOf(parm1));
					break;
				case "BUG_REPORT_SUPRIMIR":
					setDeleteReportWindows(player, Integer.valueOf(parm2));
					Retorno = getBugReport(player,Integer.valueOf(parm1));
					break;
			}
		}else{
			Retorno = Comunidad.getComunidad(player, params, 0);
		}
		return Retorno;
	}
	
	public static String getMemoryStatusServer(L2PcInstance cha){
		/*if(!cha.isGM()){
			return "";
		}*/
		
		String Verde = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		String Naranjo = cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name());
		
		double max = Runtime.getRuntime().maxMemory() / 1048576; // maxMemory is the upper
		double allocated = Runtime.getRuntime().totalMemory() / 1048576; // totalMemory the
		double cached = Runtime.getRuntime().freeMemory() / 1048576; // freeMemory the
		double used = allocated - cached; // really used memory
		double useable = max - used; // allocated, but non-used and non-allocated memory
		
		DecimalFormat df = new DecimalFormat(" (0.0000'%')");
		DecimalFormat df2 = new DecimalFormat(" # 'MB'");
		
		String html = "<font color="+ Verde +">Total Server Memory Ram:</font><font color="+ Naranjo +">" + String.valueOf(df2.format(max)) + "</font>"; 
		html += "   <font color="+ Verde +">Used Memory Ram:</font><font color="+ Naranjo +">" + String.valueOf(df2.format(allocated) + df.format((allocated / max) * 100)) + "</font>";
		html += "   <font color="+ Verde +">Free Memory Ram:</font><font color="+ Naranjo +">" + String.valueOf(df2.format(useable) + df.format((useable / max) * 100)) + "</font>";
		
		return html;
	}

	public static String getComunidad(L2PcInstance player, String accion, int pagina){
		String retorno ="<html><title>ZeuS Community Board - "+ general.Server_Name +"</title><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Server Community Board</font>");
		retorno += cbManager._formBodyComunidad(getMenuCentral(player));
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}
	
	

}
