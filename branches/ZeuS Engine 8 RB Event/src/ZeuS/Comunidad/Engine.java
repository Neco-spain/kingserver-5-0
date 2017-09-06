package ZeuS.Comunidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.skills.Skill;

import ZeuS.Comunidad.EngineForm.C_cmdInfo;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Comunidad.EngineForm.v_AugmentSpecial;
import ZeuS.Comunidad.EngineForm.v_Buffer;
import ZeuS.Comunidad.EngineForm.v_BugReport;
import ZeuS.Comunidad.EngineForm.v_ElementalSpecial;
import ZeuS.Comunidad.EngineForm.v_EnchantSpecial;
import ZeuS.Comunidad.EngineForm.v_FlagFinder;
import ZeuS.Comunidad.EngineForm.v_HeroList;
import ZeuS.Comunidad.EngineForm.v_MyInfo;
import ZeuS.Comunidad.EngineForm.v_PartyFinder;
import ZeuS.Comunidad.EngineForm.v_RaidBossInfo;
import ZeuS.Comunidad.EngineForm.v_RemoveAttribute;
import ZeuS.Comunidad.EngineForm.v_Shop;
import ZeuS.Comunidad.EngineForm.v_Teleport;
import ZeuS.Comunidad.EngineForm.v_Transformations;
import ZeuS.Comunidad.EngineForm.v_Warehouse;
import ZeuS.Comunidad.EngineForm.v_auction_house;
import ZeuS.Comunidad.EngineForm.v_augmentManager;
import ZeuS.Comunidad.EngineForm.v_blacksmith;
import ZeuS.Comunidad.EngineForm.v_clasesStadistic;
import ZeuS.Comunidad.EngineForm.v_donation;
//import ZeuS.Comunidad.EngineForm.v_dropsearch;
import ZeuS.Comunidad.EngineForm.C_gmlist;
import ZeuS.Comunidad.EngineForm.v_partymatching;
import ZeuS.Comunidad.EngineForm.v_profession;
import ZeuS.Comunidad.EngineForm.v_pvppkLog;
import ZeuS.Comunidad.EngineForm.v_subclass;
import ZeuS.Comunidad.EngineForm.v_symbolMaker;
import ZeuS.Comunidad.EngineForm.v_variasopciones;
import ZeuS.Config.general;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.interfase.htmls;
import ZeuS.procedimientos.opera;


public class Engine {

	private static Logger _log = Logger.getLogger(Engine.class.getName());
	
	private static Vector<String> btnImaLink = new Vector<String>();
	
	public static Vector<String> getBtnOption(){
		return btnImaLink;
	}
	
	public enum sRank{
		SHOP,
		TELEPORT,
		SEARCH_DROP,
		SEARCH_NPC
	}
	
	public static void createRank(int id, String Tipo){
		String Consulta = "INSERT INTO zeus_rank_acc (zeus_rank_acc.id , zeus_rank_acc.cant , zeus_rank_acc.tip) VALUES (?, 1, ?) "+
		"ON DUPLICATE KEY UPDATE zeus_rank_acc.cant=zeus_rank_acc.cant+1";
		
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			ins = con.prepareStatement(Consulta);
			ins.setInt(1, id);
			ins.setString(2,Tipo);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
		}catch(SQLException a){

		}
		try{
			con.close();
		}catch(SQLException a){

		}		
		
		
		
		
	}
	
	
	public enum enumBypass{
		Buffer,
		Gopartyleader,
		Flagfinder,
		Teleport,
		Shop,
		Warehouse,
		AugmentManager,
		SubClass,
		Profession,
		DropSearch,
		pvppklog,
		Symbolmaker,
		BugReport,
		Transformation,
		RemoveAttri,
		SelectAugment,
		SelectEnchant,
		SelectElemental,
		ClasesStadistic,
		RaidBossInfo,
		Dressme,
		HeroList,
		MyInfo,
		blacksmith,
		gmlist,
		charclanoption,
		partymatching,
		AuctionHouse,
		castleManager,
		commandinfo,
		OlyBuffer,
		donation
	}
	
	
	
	
	
	public static void _Load(){
		//Carga los Botones en el engine system del CB
		btnImaLink.clear();
		btnImaLink.add(String.valueOf(general.BTN_SHOW_BUFFER_CBE)+":icon.skill1297:Buffer:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Buffer;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_BUFFER_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_PARTYFINDER_CBE)+":icon.skill1411:Go Party Leader:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Gopartyleader;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_PARTYFINDER_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_FLAGFINDER_CBE)+":icon.skill5661:Flag Finder:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Flagfinder;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_FLAGFINDER_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_TELEPORT_CBE)+":icon.skillelf:Teleport:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Teleport;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_TELEPORT_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_SHOP_CBE)+":icon.skill5970:Shop:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Shop;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_SHOP_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_WAREHOUSE_CBE)+":icon.pouch_i00:Warehouse:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Warehouse;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_WAREHOUSE_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_AUGMENT_CBE)+":icon.skill4285:Augment Manager:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";AugmentManager;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_AUGMENT_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_SUBCLASES_CBE)+":icon.skill0500:Subclass:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";SubClass;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_SUBCLASES_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_CLASS_TRANSFER_CBE)+":icon.skill0517:Profession:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Profession;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_CLASS_TRANSFER_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_DROP_SEARCH_CBE)+":icon.skill1369:Drop Search:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";DropSearch;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_DROP_SEARCH_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_LOG_PELEAS_CBE)+":icon.skill4416_mercenary:PvP/PK Log:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";pvppklog;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_PVPPK_LIST_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_SYMBOL_MARKET_CBE)+":icon.skill5739:Symbol Maker:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Symbolmaker;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_SYMBOL_MARKET_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_BUG_REPORT_CBE)+":icon.skill6439:Bug Report:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";BugReport;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_BUG_REPORT_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_TRANSFORMATION_CBE)+":icon.skill1520:Transformations:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";Transformation;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_TRANSFORMATION_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_REMOVE_ATRIBUTE_CBE)+":icon.skill0462:Remove Attribute:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";RemoveAttri;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_AUGMENT_SPECIAL_CBE)+":icon.skill4428:Select Augment:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";SelectAugment;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_AUGMENT_SPECIAL_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_ENCANTAMIENTO_ITEM_CBE)+":icon.skill4449:Select Enchant:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";SelectEnchant;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_ELEMENT_ENHANCED_CBE)+":icon.skill6302:Select Elemental:"+ general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";SelectElemental;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_ELEMENT_ENHANCED_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_RAIDBOSS_INFO_CBE)+":icon.skillboss:Raid Boss Info:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";RaidBossInfo;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_RAIDBOSS_INFO_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_BLACKSMITH_CBE)+":icon.skill1564:Blacksmith:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";"+ Engine.enumBypass.blacksmith.name() +";0;0;0;0;0;0:" + "Exchange, Craft, Reseal, and other Options");
		btnImaLink.add(String.valueOf(general.BTN_SHOW_VARIAS_OPCIONES_CBE)+":icon.skill6319:Miscelanius:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";charclanoption;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_VARIAS_OPCIONES_CB);
		btnImaLink.add(String.valueOf(general.DRESSME_STATUS)+":icon.skill0837:Dressme:-h voice .dressme:Dressme");
		btnImaLink.add(String.valueOf(general.BTN_SHOW_PARTYMATCHING_CBE)+":icon.skill6320:Party Matching:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";" + Engine.enumBypass.partymatching.name() + ";0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_PARTYMATCHING_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_AUCTIONHOUSE_CBE)+":icon.etc_ssq_i00:Auctions House:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";" + Engine.enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_AUCTIONSHOUSE_CB);
		btnImaLink.add(String.valueOf(general.BTN_SHOW_CASTLE_MANAGER_CBE)+":icon.etc_bloodpledge_point_i00:Castle Manager:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.castleManager.name() + ";0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_CASTLE_MANAGER_CB);
		

		/***/
			/*** Solo Falses */
		btnImaLink.add("False:icon.skill6280:Clases Stadistic:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.ClasesStadistic.name() + ";0;Human;0;0;0;0:null");
		btnImaLink.add("False:icon.skill0390:Donation:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";0;0;0;0;0;0:null");
		btnImaLink.add("False:icon.skill5662:GM List:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.gmlist.name() + ";0;0;0;0;0;0:null");
		btnImaLink.add("False:icon.skill5081:Oly Buff:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0:null");
		btnImaLink.add("False:icon.skill6274:Commands:" + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0:null");
		/***/
		
	}
	
	public static String getExplica(String Nom){
		return getInfo(Nom).split(":")[4];
	}
	
	public static String getIcono(String Nom){
		return getInfo(Nom).split(":")[1];
	}
	
	public static String getNom(String Nom){
		return getInfo(Nom).split(":")[2];
	}
	
	public static String getLink(String Nom){
		return getInfo(Nom).split(":")[3];
	}
	
	public static String getInfo(String Nom){
		String retorno = "";
		for(String Parte : btnImaLink){
			try{
				if(Parte.split(":")[3].split(";")[1].equalsIgnoreCase(Nom)){
					retorno = Parte;
				}
			}catch(Exception a){
				
			}
		}
		return retorno;
	}
	
	private static String CrearBotonera(){
		String retorno = "<table background=\"L2UI_CT1.Windows_DF_Drawer_Bg\" width=256>";
		_Load();
		String tipoBtn = "<tr><td width=1></td><td><img src=\"%IMA%\" width=32 height=32></td><td><button value=\"%NOM%\" action=\"bypass %LINK%\" width=190 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td><img src=\"%IMA%\" width=32 height=32></td><td width=1></td></tr>";
		
		for(String btnP : btnImaLink){
			boolean canUse = Boolean.parseBoolean(btnP.split(":")[0]);
			if(canUse){
				retorno += tipoBtn.replace("%IMA%", btnP.split(":")[1]).replace("%NOM%", btnP.split(":")[2]).replace("%LINK%", btnP.split(":")[3]);
			}
		}
		
		retorno += "<tr><td width=1></td><td></td><td></td><td></td><td width=1></td></tr>"+
                   "<tr><td width=1></td><td></td><td></td><td></td><td width=1></td></tr>";
		
		retorno += "</table><br>";
		
		return retorno;
	}
	
	
	
	private static String getBotonera_Vacia(){
		String retorno ="<td width=233>"+
                              "<table width=233>"+
                                     "<tr>"+
                                         "<td width=32>"+
                                             "<table width=32>"+
                                                    "<tr>"+
                                                        "<td width=32>"+
                                                        "</td>"+
                                                    "</tr>"+
                                             "</table>"+
                                         "</td>"+
                                         "<td width=167 fixwidth=160>"+
                                             "<table width=167><tr><td><font color=339999></font></td></tr><tr><td></td></tr></table><br>"+
                                         "</td>"+
                                         "<td width=32>"+
                                             "<table width=32>"+
                                                    "<tr>"+
                                                        "<td width=32>"+
                                                        "</td>"+
                                                    "</tr>"+
                                             "</table>"+
                                         "</td>"+
                                     "</tr>"+
                              "</table>"+
                          "</td>";
		
		
		
		return retorno;
	}
	
	
	private static String getBotonera(String Imagen, String Nombre, String Descrip, String Link){
		String retorno ="";/*"<td width=233>"+
                              "<table width=233>"+
                                     "<tr>"+
                                         "<td width=32>"+
                                             "<table width=32>"+
                                                    "<tr>"+
                                                        "<td width=32>"+
                                                            "<img src=\""+Imagen+"\" width=32 height=32>"+
                                                        "</td>"+
                                                    "</tr>"+
                                             "</table>"+
                                         "</td>"+
                                         "<td width=167 fixwidth=160>"+
                                             "<table width=167><tr><td><font color=\"339999\">"+Nombre+"</font></td></tr><tr><td>"+Descrip+"</td></tr></table><br>"+
                                         "</td>"+
                                         "<td width=32>"+
                                             "<table width=32>"+
                                                    "<tr>"+
                                                        "<td width=32>"+
                                                            "<button action=\"bypass "+Link+"\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_PlusBtn_Blue_Down\" fore=\"L2UI_CT1.MiniMap_DF_PlusBtn_Blue\">"+
                                                        "</td>"+
                                                    "</tr>"+
                                             "</table>"+
                                         "</td>"+
                                     "</tr>"+
                              "</table>"+
                          "</td>";*/
		
		retorno += "<td fixwidth=244><table width=244><tr><td width=32>"+
        "<img src=\""+Imagen+"\" width=32 height=32><br><br></td><td fixwidth=180>"+
        "<font color=\"339999\">"+Nombre+"</font><br1>"+Descrip+"</td>"+
        "<td width=32>"+
        "<button action=\"bypass "+Link+"\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_PlusBtn_Blue_Down\" fore=\"L2UI_CT1.MiniMap_DF_PlusBtn_Blue\"><br><br></td></tr></table></td>";
		
		return retorno;
	}
	
	private static String getEngineHTML(L2PcInstance player, String param, int Pagina){
		_Load();
		
		String retorno ="<html><title>ZeuS Engine System - "+ general.Server_Name +"</title><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">ZeuS Engine</font>");
		
		boolean isInOlympiadMode = player.isInOlympiadMode();
		boolean isInRBEvent = RaidBossEvent.isPlayerOnRBEvent(player);
		
		String Colores[] = {"333333","1A1A1A"};
		
		int Contador = 0;
		int forColor = 0;
		
		for(String btnP : btnImaLink){
			boolean canUse = Boolean.parseBoolean(btnP.split(":")[0]);
			if(canUse){
				if(Contador==0){
					retorno+= "<table width=732 border=0 cellpadding=0 bgcolor=\""+ Colores[forColor % 2] +"\"><tr>";
				}
				//general.BTN_SHOW_BUFFER_CBE)+":icon.skill1297:Buffer:"+ general.COMMUNITY_BOARD_ENGINE +";Buffer;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_BUFFER_CB
				retorno += getBotonera(btnP.split(":")[1],btnP.split(":")[2],btnP.split(":")[4],btnP.split(":")[3]);
				Contador++;
				if(Contador>=3){
					Contador=0;
					forColor++;
					retorno += "</tr></table><br><img src=\"L2UI.SquareGray\" width=758 height=2><br>";
				}
			}
		}		
		
		if(Contador>0 && Contador <3){
			for(int i=Contador;i<3;i++){
				retorno +="<td fixwidth=244><table width=244><tr><td width=32><br><br></td><td fixwidth=180><br1></td><td width=32><br><br></td></tr></table></td>";
			}
			retorno += "</tr></table><br><img src=\"L2UI.SquareGray\" width=758 height=2><br>";
		}
		
		retorno += cbManager.getPieCommunidad() + "</center></body></html>";
		
		return retorno;
	}

	private static String _getEngineHTML(L2PcInstance player, String param, int Pagina){
		
		_Load();
		
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">ZeuS Engine</font>");
		
		boolean isInOlympiadMode = player.isInOlympiadMode();
		boolean isInRBEvent = RaidBossEvent.isPlayerOnRBEvent(player);
		
		
		String HTMLEngine_AccessLink = "<table width=720 border=0 cellpadding=0><tr><td width=720 fixwidth=720>";
		
		String InicioStr = "<img src=\"L2UI.SquareGray width=762\" height=1>"+
                    "<table width=699 cellpadding=0 bgcolor=%COLOR% cellspacing=0  height=60>"+
                    "<tr>";
		
		String Colores[] = {"333333","1A1A1A"};
		
		int ConColumnas = 1;
		int forColor=1;
		
		
		for(String btnP : btnImaLink){
			boolean canUse = Boolean.parseBoolean(btnP.split(":")[0]);
			if(canUse){
				if(ConColumnas==1){
					HTMLEngine_AccessLink += InicioStr.replace("%COLOR%", Colores[forColor % 2]);
				}
				//general.BTN_SHOW_BUFFER_CBE)+":icon.skill1297:Buffer:"+ general.COMMUNITY_BOARD_ENGINE +";Buffer;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_BUFFER_CB
				HTMLEngine_AccessLink += getBotonera(btnP.split(":")[1],btnP.split(":")[2],btnP.split(":")[4],btnP.split(":")[3]);
				ConColumnas++;
				if(ConColumnas>3){
					ConColumnas=1;
					forColor++;
					HTMLEngine_AccessLink += "</tr></table>";
				}
			}
		}
		
		if(ConColumnas <=3 && ConColumnas>1){
			String Vacio = getBotonera_Vacia();
			while(ConColumnas <= 3){
				HTMLEngine_AccessLink += Vacio;
				ConColumnas++;
			}
			HTMLEngine_AccessLink += "</tr></table>";
		}
		
		HTMLEngine_AccessLink += "</td></tr></table>";
		
		retorno += HTMLEngine_AccessLink;
		
		
		
		/*String grillaCentral = "<table width=750 gbcolor="+cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name()) +" align=CENTER>"+
								"<tr><td width=750 align=CENTER>"+ (!isInOlympiadMode ? htmls.get_CB_Main_Btn(player, "") : "<font color=8A4B08>You can not use at the Moment</font>") +"</td></tr></table>";
		 */
		//retorno += cbManager._formBodyComunidad(getFastMenu(player));
		
		

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}
	
	
	private static void getAllSkillFromNPC(L2PcInstance player, int idMonster){
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body><center>";
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(idMonster);
		tmpl.getSkills();
		Map<Integer, Skill> skills = new HashMap<>(tmpl.getSkills());
		Iterator<Skill> skillite = skills.values().iterator();
		Skill sk = null;
		Vector<String> MonsterSkill_ACTIVE = new Vector<String>();
		Vector<String> MonsterSkill_PASSIVE = new Vector<String>();
		
		while(skillite.hasNext()){
			sk = skillite.next();
			if(sk.isPassive()){
				MonsterSkill_PASSIVE.add(sk.getName() + ":" + String.valueOf(sk.getLevel()));
			}else if(sk.isPhysical()){
				MonsterSkill_ACTIVE.add(sk.getName() + ":" + String.valueOf(sk.getLevel()));
			}
		}
		
		HTML +="<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=CENTER>"+
        "<font color=70FFB3>"+ tmpl.getName() +"</font><font color=6B6B6B> lv "+ String.valueOf(tmpl.getLevel()) +"</font></td></tr></table><br>";
		
		
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT>"+
        "<font name=hs12 color=8BECFF>Active</font></td></tr></table>";

		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		boolean Empty = true;
		if(MonsterSkill_ACTIVE!=null){
			if(MonsterSkill_ACTIVE.size()>0){
				for(String PtSk : MonsterSkill_ACTIVE){
					HTML += "<table with=270 border=0 bgcolor=1C1C1C>"+
                              "<tr>"+
                                  "<td fixwidth=270 align= LEFT>"+
                                      "<font color=FFFABB>"+ PtSk.split(":")[0] +"</font> <font color=6B6B6B>lv "+ PtSk.split(":")[1] +"</font><br1>"+
                                  "</td>"+
                              "</tr>"+
                             "</table>";
				}
				Empty = false;
			}
		}
		if(Empty){
			HTML += "<table with=270 border=0 bgcolor=1C1C1C>"+
                    "<tr>"+
                        "<td fixwidth=270 align= LEFT>"+
                            "<font color=FFFABB>EMPTY</font><br1>"+
                        "</td>"+
                    "</tr>"+
                   "</table>";			
		}
		Empty = true;
		HTML += "<br></td></tr></table><br>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT>"+
				"<font name=hs12 color=8BECFF>Passive</font></td></tr></table>";

		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		if(MonsterSkill_PASSIVE!=null){
			if(MonsterSkill_PASSIVE.size()>0){
				for(String PtSk : MonsterSkill_PASSIVE){
					HTML += "<table with=270 border=0 bgcolor=1C1C1C>"+
                              "<tr>"+
                                  "<td fixwidth=270 align= LEFT>"+
                                      "<font color=FFFABB>"+ PtSk.split(":")[0] +"</font> <font color=6B6B6B>lv "+ PtSk.split(":")[1] +"</font><br1>"+
                                  "</td>"+
                              "</tr>"+
                             "</table>";
				}
				Empty = false;
			}
		}
		if(Empty){
			HTML += "<table with=270 border=0 bgcolor=1C1C1C>"+
                    "<tr>"+
                        "<td fixwidth=270 align= LEFT>"+
                            "<font color=FFFABB>EMPTY</font><br1>"+
                        "</td>"+
                    "</tr>"+
                   "</table>";			
		}
		
		HTML += "<br></td></tr></table><br>";
		
		HTML += "</center></body></html>";
		central.sendHtml(player, HTML);
	}
	
	
	
	

	public static String delegar(L2PcInstance player, String params){
		String Retorno = "";
		_log.warning(params);
		general.IS_USING_NPC.put(player.getObjectId(),false);
		general.IS_USING_CB.put(player.getObjectId(),true);
		
		
		if(params == null){
			return getEngineHTML(player, "", 0);
		}else if(params.length()<=0){
			return getEngineHTML(player, "", 0);
		}else if(params.equals(general.COMMUNITY_BOARD_ENGINE_PART_EXEC)){
			return getEngineHTML(player, "", 0);
		}

		if(params.contains(";")){
			//0;0;0;0;0;0;0;0
			//_log.warning("Entro" + params);
			String[] Eventos = params.split(";");
			String Event = Eventos[1];
			String parm1 = Eventos[2];
			String parm2 = Eventos[3];
			String parm3 = Eventos[4];
			String parm4 = Eventos[5];
			String parm5 = Eventos[6];
			String parm6 = Eventos[7];
			if(!opera.canUseCBFunction(player)){
				if(!Event.equals(enumBypass.OlyBuffer.name())){
					central.msgbox("Community Services Disabled for the moment", player);
					return "";					
				}
			}
			
			if(Event.equals(enumBypass.Buffer.name())){
				Retorno = v_Buffer.delegar(player, params);
			}else if(Event.equals(enumBypass.Gopartyleader.name())){
				Retorno = v_PartyFinder.bypass(player, params);
			}else if(Event.equals(enumBypass.Flagfinder.name())){
				Retorno = v_FlagFinder.bypass(player, params);
			}else if(Event.equals(enumBypass.Teleport.name())){
				Retorno = v_Teleport.bypass(player, params);
			}else if(Event.equals(enumBypass.Shop.name())){
				Retorno = v_Shop.bypass(player, params);
			}else if(Event.equals(enumBypass.Warehouse.name())){
				Retorno = v_Warehouse.bypass(player, params);
			}else if(Event.equals(enumBypass.AugmentManager.name())){
				Retorno = v_augmentManager.bypass(player, params);
			}else if(Event.equals(enumBypass.SubClass.name())){
				Retorno = v_subclass.bypass(player, params);
			}else if(Event.equals(enumBypass.Profession.name())){
				Retorno = v_profession.bypass(player, params);
			}else if(Event.equals(enumBypass.Symbolmaker.name())){
				Retorno = v_symbolMaker.bypass(player, params);
			}else if(Event.equals(enumBypass.RemoveAttri.name())){
				Retorno = v_RemoveAttribute.bypass(player, params);
			}//else if(Event.equals(enumBypass.DropSearch.name())){
//				Retorno = v_dropsearch.bypass(player, params);
//			}
			 else if(Event.equals(enumBypass.pvppklog.name())){
				Retorno = v_pvppkLog.bypass(player, params);
			}else if(Event.equals(enumBypass.ClasesStadistic.name())){
				Retorno = v_clasesStadistic.bypass(player, params);
			}else if(Event.equals(enumBypass.RaidBossInfo.name())){
				Retorno = v_RaidBossInfo.bypass(player, params);
			}else if(Event.equals(enumBypass.HeroList.name())){
				Retorno = v_HeroList.bypass(player, params);
			}else if(Event.equals(enumBypass.MyInfo.name())){
				Retorno = v_MyInfo.bypass(player, params);
			}else if(Event.equals(enumBypass.BugReport.name())){
				Retorno = v_BugReport.bypass(player, params);
			}else if(Event.equals(enumBypass.SelectElemental.name())){
				Retorno = v_ElementalSpecial.bypass(player, params);
			}else if(Event.equals(enumBypass.SelectEnchant.name())){
				Retorno = v_EnchantSpecial.bypass(player, params);
			}else if(Event.equals(enumBypass.SelectAugment.name())){
				Retorno = v_AugmentSpecial.bypass(player, params);
			}else if(Event.equals(enumBypass.blacksmith.name())){
				Retorno = v_blacksmith.bypass(player, params);
			}else if(Event.equals(enumBypass.charclanoption.name())){
				Retorno = v_variasopciones.bypass(player,params);
			}else if(Event.equals(enumBypass.Transformation.name())){
				Retorno = v_Transformations.bypass(player,params);
			}else if(Event.equals(enumBypass.partymatching.name())){
				Retorno = v_partymatching.bypass(player, params);
			}else if(Event.equals(enumBypass.AuctionHouse.name())){
				Retorno = v_auction_house.bypass(player, params);
			}else if(Event.equals(enumBypass.donation.name())){
				Retorno = v_donation.bypass(player, params);
			}else if(Event.equals(Engine.enumBypass.castleManager.name())){
				String Enviar = htmls.MainHtmlCastleManager(player);
				central.sendHtml(player, Enviar);
				Retorno ="";
			}else if(Event.equals(enumBypass.OlyBuffer.name())){
				Retorno = C_oly_buff.bypass(player, params);
				if(Retorno.length()==0){
					cbFormato.cerrarCB(player);
				}
			}else if(Event.equals(Engine.enumBypass.commandinfo.name())){
				Retorno= C_cmdInfo.bypass(player, params);
			}else if(Event.equals("ShowNPCSkill")){
				getAllSkillFromNPC(player,Integer.valueOf(parm1));
			}else if(Event.equals("gmlist")){
				C_gmlist.bypass(player, params);
			}
		}
		return Retorno;
	}
}
