package ZeuS.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;


public class button {
	private static String[] estado = {"<font color=DF3A01>DISABLED</font>","<font color=00FF40>ENABLED</font>"};

	private static final Logger _log = Logger.getLogger(Config.class.getName());

	public static String getBtnAll(L2PcInstance player){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Control Access<br1>ZeuS NPC AIO General")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += "<table width=270 border=0 bgcolor=151515>";
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Vote\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 7 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_VOTE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Buffer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 8 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_BUFFER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Teleport\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 9 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_TELEPORT ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Shop\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 10 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SHOP ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Warehouse\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 11 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_WAREHOUSE ? estado[1]: estado[0] );
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment/Deaugment\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 12 0 0\" width=130 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_AUGMENT ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Sub Class\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 13 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SUBCLASES ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Class Transfer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 14 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CLASS_TRANSFER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Config Panel\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 15 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CONFIG_PANEL ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Drop S.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 16 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DROP_SEARCH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 17 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_PVPPK_LIST ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Log PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 18 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_LOG_PELEAS ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Castle Manager\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 19 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CASTLE_MANAGER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"The Challenge\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 20 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DESAFIO ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Simbol Maker\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 21 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SYMBOL_MARKET ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Clan & Ally\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 22 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CLANALLY ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Party L.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 23 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_PARTYFINDER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Flag\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 24 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_FLAGFINDER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Color Name\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 25 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_COLORNAME ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Delevel\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 26 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DELEVEL ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"G. Boss Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 36 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_GRAND_BOSS_STATUS ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Raid. B. Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 37 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_RAIDBOSS_INFO ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"S. Atribute\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 27 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_REMOVE_ATRIBUTE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Bug Report\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 28 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_BUG_REPORT ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Name Change PJ\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 30 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CAMBIO_NOMBRE_PJ ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"N. Change Clan\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 31 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CAMBIO_NOMBRE_CLAN ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Opc. Varias\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 32 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_VARIAS_OPCIONES ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Transform\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 252 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_TRANSFORMATION? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Elemental It.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 33 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_ELEMENT_ENHANCED ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Enchant Item\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 34 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_ENCANTAMIENTO_ITEM ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment Special\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 35 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_AUGMENT_SPECIAL ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Donation\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 29 0 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DONATION ? estado[1]: estado[0]);
		MAIN_HTML += "</table>";
		MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + menu.getBtnbackConfig() +  central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}




	public static String getBtnCBE(L2PcInstance player){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Control Access<br1>ZeuS CB Engine")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += "<table width=280 border=0 bgcolor=151515>";
		//MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Vote\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 420 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_VOTE_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Buffer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 421 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_BUFFER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Teleport\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 422 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_TELEPORT_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Shop\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 423 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SHOP_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Warehouse\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 424 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_WAREHOUSE_CBE ? estado[1]: estado[0] );
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment/Deaugment\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 425 2 0\" width=130 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_AUGMENT_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Sub Class\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 426 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SUBCLASES_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Class Transfer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 427 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CLASS_TRANSFER_CBE ? estado[1]: estado[0]);
		//MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Config Panel\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 428 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CONFIG_PANEL_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Drop S.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 429 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DROP_SEARCH_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 430 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_PVPPK_LIST_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Log PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 431 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_LOG_PELEAS_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Castle Manager\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 432 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CASTLE_MANAGER_CBE ? estado[1]: estado[0]);
		//MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"El Desafio\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 433 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DESAFIO_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Simbol Maker\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 434 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SYMBOL_MARKET_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Clan & Ally\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 435 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CLANALLY_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Party L.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 436 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_PARTYFINDER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Flag\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 437 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_FLAGFINDER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Color Name\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 438 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_COLORNAME_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Delevel\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 439 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DELEVEL_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"G. Boss Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 449 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_GRAND_BOSS_STATUS_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Raid. B. Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 450 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_RAIDBOSS_INFO_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"S. Atribute\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 440 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_REMOVE_ATRIBUTE_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Bug Report\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 441 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_BUG_REPORT_CBE ? estado[1]: estado[0]);
		//MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"C.Nombre pj\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 443 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE ? estado[1]: estado[0]);
		//MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"C. Nom. Clan\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 444 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Miscellaneous\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 445 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_VARIAS_OPCIONES_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Transform\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 451 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_TRANSFORMATION_CBE? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Elemental It.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 446 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_ELEMENT_ENHANCED_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Enchant Item\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 447 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_ENCANTAMIENTO_ITEM_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment Special\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 448 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_AUGMENT_SPECIAL_CBE ? estado[1]: estado[0]);
		//MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Donation\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 442 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DONATION_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Blacksmith\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 571 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_BLACKSMITH_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Party Matching\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 587 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_PARTYMATCHING_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Auction House\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 594 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_AUCTIONHOUSE_CBE ? estado[1]: estado[0]);
		
		
		//('594','BTN_SHOW_AUCTIONHOUSE_CBE','true')
		
		
		MAIN_HTML += "</table>";
		MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + menu.getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}




	public static String getBtnCH(L2PcInstance player){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Control Access<br1>ZeuS NPC AIO Clan Hall")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += "<table width=280 border=0 bgcolor=151515>";
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Vote\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 38 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_VOTE_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Buffer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 39 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_BUFFER_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Teleport\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 40 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_TELEPORT_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Shop\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 41 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SHOP_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Warehouse\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 42 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_WAREHOUSE_CH ? estado[1]: estado[0] );
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment/Deaugment\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 43 1 0\" width=130 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_AUGMENT_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Sub Class\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 44 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SUBCLASES_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Class Transfer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 45 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CLASS_TRANSFER_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Config Panel\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 46 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CONFIG_PANEL_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Drop S.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 47 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DROP_SEARCH_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 48 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_PVPPK_LIST_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Log PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 49 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_LOG_PELEAS_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Castle Manager\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 50 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CASTLE_MANAGER_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"El Desafio\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 51 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DESAFIO_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Simbol Maker\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 52 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_SYMBOL_MARKET_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Clan & Ally\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 53 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CLANALLY_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Party L.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 54 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_PARTYFINDER_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Flag\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 55 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_FLAGFINDER_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Color Name\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 56 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_COLORNAME_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Delevel\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 57 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DELEVEL_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"G. Boss Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 67 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_GRAND_BOSS_STATUS_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Raid. B. Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 68 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_RAIDBOSS_INFO_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"S. Atribute\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 58 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_REMOVE_ATRIBUTE_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Bug Report\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 59 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_BUG_REPORT_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"C.Nombre pj\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 61 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CAMBIO_NOMBRE_PJ_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"C. Nom. Clan\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 62 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Miscellaneous\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 63 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_VARIAS_OPCIONES_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Transform\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 258 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_TRANSFORMATION_CH? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Elemental It.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 64 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_ELEMENT_ENHANCED_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Enchant Item\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 65 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_ENCANTAMIENTO_ITEM_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment Special\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 66 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_AUGMENT_SPECIAL_CH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Donation\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 60 1 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_DONATION_CH ? estado[1]: estado[0]);
		MAIN_HTML += "</table>";
		MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + menu.getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	public static void status(L2PcInstance player, int idProc){
		if(!opera.isMaster(player)){
			return;
		}
		String qry = "call sp_zeus_config(1,"+idProc+",'','','')";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET ADMIN BUTTON->"+e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET ADMIN BUTTON->"+a.getMessage());
		}

		if(Respu.equals("cor")){
			general.loadConfigs();
		}

	}

}
