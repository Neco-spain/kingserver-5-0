package ZeuS.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.dressme.dressme;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.interfase.htmls;
import ZeuS.procedimientos.opera;


public class menu {
	private static Logger _log = Logger.getLogger(menu.class.getName());
	String[] cmbBoolean = {"true","false"};

	public  final static String bypassNom = "admin_zeus_config";//ZeuSNPC

	protected final static String[] operadoresNumericos = {"1","2","291","292","293","280","279","271","272","268","254","249","244","221","222","223","224","225",
			"226","227","228","229","230","200","201","202","203","204","205","206","207","208","209","180","181","182","183","184",
			"185","186","187","188","189","175","171","169","166","164","162","160","158","156","154","152","150","148","146","143",
			"146","143","142","139","138","136","134","132","129","128","121","118","116","115","114","113","110","108","103","102","97",
			"90","87","81","80","79","78","77","76","73","4","3","294","295","296","299","308","311","316","317","323","324","350","351",
			"354","356","357","380","382","390","391","392","393","394","395","396","397","398","401","403","405","406","416","417","418","419","458","481",
			"463","464","466","467","468","471","482","484","485","492","493","495","496","497","498","503","504","506","510","516","517","519","523", "524","527","529",
			"543","544","545","546","547","548","549","550","551", "554", "555", "557", "558", "566", "572","573","574","575","576","577","578","579","580","581","582","583","584", "590","591","592"};
	
	public final static String[] LeyendaComunidad = {"GM:38","DEATH:45","HERO:83","C.LEADER:83","V.I.P:83","NORMAL:83","IN_PVP:83","P.STORE:83","KARMA (PK):83","JAILED:83"};
	
	/*
	"<td width=38 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_GM.name()) +">GM</font></td>"+
	"<td width=45 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_DEATH.name()) +">DEATH</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_HERO.name()) +">HERO</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_CLANLEADER.name()) +">C.LEADER</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_VIP.name()) +">V.I.P</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_NORMAL.name()) +">NORMAL</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PVP.name()) +">IN PVP</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PRIVATESTORE.name()) +">P.STORE</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PK.name()) +">KARMA (PK)</font></td>"+
	"<td width=83 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_JAILED.name()) +">JAILED</font></td>"+
	*/
	
	private final static Vector<Integer> VectorOnlyNumeros = new Vector<Integer>();


	private static void CreatedFilter(){
		for(String operador: operadoresNumericos){
			VectorOnlyNumeros.add(Integer.valueOf(operador));
		}
	}

	private static String crearBoton(String Nombre, String Link){
		//return "<button value=\""+Nombre+"\" action=\"bypass -h ZeuSNPC "+Link+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		return "<button value=\""+Nombre+"\" action=\"bypass -h "+ bypassNom +" "+Link+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	}

	public static String getBtnbackConfig(){
		return central.LineaDivisora(2) + central.headFormat("<button value=\"Main Config\" action=\"bypass -h "+ bypassNom +" Config 2 0 0\" width=100 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","WHITE") + central.LineaDivisora(2);
	}

	public static String getConfigMenu(L2PcInstance player){
		if(!opera.isMaster(player)){
			central.msgbox(msg.USTED_NO_PUEDE_INGRESAR_A_ESTA_SECCION, player);
			return "";
		}

		if(VectorOnlyNumeros.size()==0){
			CreatedFilter();
		}

		Vector<String> Botones= new Vector<String>();

		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("ZeuS Engine AIO Config") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + central.headFormat("Select Area to Edit") + central.LineaDivisora(2) + central.LineaDivisora(1);

		//Botones.add(crearBoton("ZeuS Config","setConfig1 general 0 0 0 0 0"));
		Botones.add(crearBoton("ZeuS Config","setConfig1 general 0 0 0 0 0"));
		Botones.add(crearBoton("Acc. AIO General","GetHTMConfig 1 0 0 0 0"));
		Botones.add(crearBoton("Acc. AIO Clan Hall","GetHTMConfig 2 0 0 0 0"));
		Botones.add(crearBoton("Acc. AIO C. Board","GetHTMConfig 3 0 0 0 0"));
		Botones.add(crearBoton("Vote","setConfig1 vote 0 0 0 0 0"));
		Botones.add(crearBoton("Teleport","setConfig1 teleport 0 0 0 0 0"));
		//Botones.add(crearBoton("Donation","setConfig1 donacion 0 0 0 0 0"));
		Botones.add(crearBoton("Premium","setConfig1 premium 0 0 0 0 0"));
		Botones.add(crearBoton("The challenge","setConfig1 desafio 0 0 0 0 0"));
		Botones.add(crearBoton("Drop Search","setConfig1 dropsear 0 0 0 0 0"));
		Botones.add(crearBoton("Go Party Leader","setConfig1 partyfin 0 0 0 0 0"));
		Botones.add(crearBoton("Go Flag","setConfig1 flagfin 0 0 0 0 0"));
		Botones.add(crearBoton("Color Name","setConfig1 colorname 0 0 0 0 0"));
		Botones.add(crearBoton("Augment Special","setConfig1 augmentspe 0 0 0 0 0"));
		Botones.add(crearBoton("Enchant Special","setConfig1 enchantspe 0 0 0 0 0"));
		Botones.add(crearBoton("Elemental Special","setConfig1 elementalspe 0 0 0 0 0"));
		Botones.add(crearBoton("Raid Boss Info","setConfig1 bossinfo 0 0 0 0 0"));
		Botones.add(crearBoton("Miscellaneous","setConfig1 opcvarias 0 0 0 0 0"));
		Botones.add(crearBoton("Change Names","setConfig1 changeName 0 0 0 0 0"));
		Botones.add(crearBoton("Delevel","setConfig1 delevel 0 0 0 0 0"));
		Botones.add(crearBoton("PvP Config","setConfig1 pvpConfig 0 0 0 0 0"));
		Botones.add(crearBoton("PvP Color C.","setConfig1 pvppkColor 0 0 0 0 0"));
		Botones.add(crearBoton("PvP Mens. Ciclos","setConfig1 pvpMensaje 0 0 0 0 0"));
		Botones.add(crearBoton("Raid Annou.","setConfig1 raidconfig 0 0 0 0 0"));
		Botones.add(crearBoton("Buffer","setConfig1 buffer 0 0 0 0 0"));
		Botones.add(crearBoton("Voice Buffer","setConfig1 bufferchar 0 0 0 0 0"));
		Botones.add(crearBoton("Cancel Buff","setConfig1 cancelbuff 0 0 0 0 0"));
		Botones.add(crearBoton("Transform","setConfig1 transform 0 0 0 0 0"));
		Botones.add(crearBoton("Olympics","setConfig1 Olymp 0 0 0 0 0"));
		Botones.add(crearBoton("Antibot","setConfig1 Antibot 0 0 0 0 0"));
		Botones.add(crearBoton("IP Ban","setConfig1 banip 0 0 0 0 0"));
		Botones.add(crearBoton("Over Enchant","setConfig1 overenchant 0 0 0 0 0"));
		Botones.add(crearBoton("Dual Box","setConfig1 dualbox 0 0 0 0 0"));
		Botones.add(crearBoton("Chat Config","setConfig1 ChatConfig 0 0 0 0 0"));		
		Botones.add(crearBoton("Castle Manager","setConfig1 castlema 0 0 0 0 0"));
		Botones.add(crearBoton("Dressme","setConfig1 dressme 0 0 0 0 0"));
		Botones.add(crearBoton("Community B.","setConfig1 comunidad 0 0 0 0 0"));
		Botones.add(crearBoton("CB. Party Finder","setConfig1 cbpartyfinder 0 0 0 0 0"));
		Botones.add(crearBoton("Email Register","setConfig1 emailregister 0 0 0 0 0"));
		Botones.add(crearBoton("Raid Boss Event","setConfig1 RaidBossEvent 0 0 0 0 0"));
		Botones.add(crearBoton("Town War Event","setConfig1 TownWarEvent 0 0 0 0 0"));
		Botones.add(crearBoton("Load Config","setConfig1 loadConfig 0 0 0 0 0"));
		String Grilla ="<table width=260><tr>";
		int Contador =0;
		for(String btn:Botones){
			Contador++;
			Grilla += "<td width=130 align=CENTER>"+btn+"</td>";
			if(((Contador%2)==0) && (Contador!=0)){
				Grilla+= "</tr><tr>";
			}
		}

		if(!Grilla.endsWith("</tr>")){
			Grilla +="</tr>";
		}

		Grilla += "</table>";

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(Grilla) + central.LineaDivisora(1);
		MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";

		return MAIN_HTML;
	}
	private static String setFilaModif(String titulo, String Valor, String Seccion, String idBox){
		String temp = "<table width=260><tr><td width=200 align=LEFT fixwidth=200>"+titulo+"</td><td width=60><button value=\"Modif\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+" "+idBox+" 0 0 0 0\" width=55 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>" +
				"<tr><td width=260 align=LEFT><font color=A4A4A4>" + (Valor.isEmpty() || (Valor==null) ? "":"Actual: "+Valor)+"</font></td></tr></table><br>";
		return central.LineaDivisora(1) + central.headFormat(temp);
	}

	private static String setFilaModif_SinFormarto(String titulo, String Valor, String Seccion, String idBox){
		String temp = "<tr><td width=200 align=LEFT fixwidth=200>"+titulo+"</td><td width=60><button value=\"Modif\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+" "+idBox+" 0 0 0 0\" width=55 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>" +
				"<tr><td width=260 align=LEFT><font color=A4A4A4>" + (Valor.isEmpty() || (Valor==null) ? "":"Actual: "+Valor)+"</font></td></tr>";
		return central.LineaDivisora(1) + central.headFormat(temp);
	}

	private static String getEditComboBox(String titulo, String idINBD, String Seccion, String ListaCombo){
		String temp = central.headFormat(titulo,"LEVEL");
		temp += "<center><table width=260><tr>" +
				"<td width=210 align=LEFT><combobox width=210 var=valor list="+ListaCombo+"></td>" +
				"<td align=center width=55><button value=\"OK\" action=\"bypass -h " + bypassNom + " setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\" width=50 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" +
				"</tr></table></center><br>";
		return central.headFormat(temp);
	}

	private static String getEditBigBox(String titulo, String idINBD, String Seccion){
		String temp = central.headFormat(titulo,"LEVEL");
		temp += "<center><table width=260><tr>" +
				"<td width=210 align=LEFT><multiedit var=\"valor\" width=210 ></td>" +
				"<td align=center width=55><button value=\"OK\" action=\"bypass -h " + bypassNom + " setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\" width=50 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" +
				"</tr></table></center><br>";
		return central.headFormat(temp);
	}


	private static String getEditBox(String titulo, String idINBD, String Seccion){
		String temp = central.headFormat(titulo,"LEVEL");
		temp += "<center><table width=200><tr><td width=145 align=LEFT><edit var=\"valor\" width=110></td><td align=center width=55><button value=\"OK\" action=\"bypass -h " + bypassNom + " setConfig1 setValor "+idINBD+" $valor "+Seccion+" 0 0\" width=50 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></center>";
		return central.headFormat(temp);
	}

	private static String getEditLista_StringInteger(String titulo,String ListaStr, String Seccion, String idINBD, boolean _showOnly){
		
		Vector<Integer> Lista = new Vector<Integer>();
		
		if(ListaStr.length()>0){
			for (String SpitL : ListaStr.split(",")){
				Lista.add(Integer.valueOf(SpitL));
			}
		}
		
		return getEditLista_VectorInteger(titulo,Lista,Seccion,idINBD,_showOnly);
	}	
	
	
	private static String getEditLista_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD, boolean _showOnly){
		String btnBack = "<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+ " 0 0 0 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat(titulo, "LEVEL") + central.LineaDivisora(1);
		HTML += getEditLista_VectorInteger(titulo, Lista, Seccion, idINBD,"1");
		HTML += central.LineaDivisora(1) + central.headFormat(btnBack) + central.LineaDivisora(1);
		HTML += getCerrarTabla();
		return HTML;
	}

	private static String getEditLista_VectorString(String titulo,Vector<String> Lista, String Seccion, String idINBD, boolean _showOnly){
		String btnBack = "<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+ " 0 0 0 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat(titulo, "LEVEL") + central.LineaDivisora(1);
		//HTML += getEditLista_VectorInteger(titulo, Lista, Seccion, idINBD,"1");
		HTML += central.LineaDivisora(1) + central.headFormat(btnBack) + central.LineaDivisora(1);
		HTML += getCerrarTabla();
		return HTML;
	}
	
	private static String getEditLista_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD){
		return getEditLista_VectorInteger(titulo, Lista, Seccion, idINBD,"0");
	}
	
	private static String getEditLista_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD, String idShow){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>Ingrese Valor</td>" +
				"<td width=120 align=LEFT><edit var=\"idIngr\" width=110></td>" +
				"</tr></table><center>"+
				"<button value=\"add\" action=\"bypass -h " + bypassNom + " setConfig1 setLista $idIngr "+idINBD+" "+Seccion+" 0 "+idShow+"\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML = central.headFormat(temp + getListaIndividual_INTEGER(Lista, Seccion, idINBD));
		return MAIN_HTML;
	}


	private static String getEditListaNPC_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>ID NPC</td>" +
				"<td width=80 align=LEFT></td>" +				
				"<td width=120 align=LEFT><edit var=\"idIngr\" width=110></td>" +
				"</tr></table><center>"+
				"<button value=\"add\" action=\"bypass -h " + bypassNom + " setConfig1 setLista $idIngr "+idINBD+" "+Seccion+" 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML = central.headFormat(temp + getListaIndividual_NPC_INTEGER(Lista, Seccion, idINBD));
		return MAIN_HTML;
	}

	private static String getColorMenu(String titulo,String ListaColor, String Seccion, String idINBD){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>Ingrese Color (HTML)</td>" +
				"<td width=120 align=LEFT><edit var=\"Color\" width=110></td></tr></table><center>"+
				"<button value=\"Add\" action=\"bypass -h " + bypassNom + " setConfig1 setLista $Color "+idINBD+" "+Seccion+" 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";

		MAIN_HTML = central.headFormat(temp + getColores(ListaColor, Seccion, idINBD));
		return MAIN_HTML;
	}

	/*Aqui*/

	private static String getEditCombo(String titulo,String List, String Seccion, String idINBD){
		String Combo = "<combobox width=120 var=cmbSelecc list="+ List + " >";
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>Select</td>" +
				"<td width=120 align=LEFT>"+ Combo +"</td>" +
				"</tr></table><center><br>"+
				"<button value=\"Save\" action=\"bypass -h " + bypassNom + " setConfig1 setCombo $cmbSelecc 0 "+idINBD+" "+Seccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML = central.headFormat(temp);
		return MAIN_HTML;
	}

	private static String getEdititem(String titulo,String premios, String Seccion, String idINBD, boolean _onlyItem){
		String btnBack = "<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+ " 0 0 0 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(titulo,"LEVEL") + central.LineaDivisora(1); 
		MAIN_HTML += getEdititem(titulo,premios,Seccion,idINBD,"1");
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnBack) + central.LineaDivisora(1);
		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getEdititem(String titulo,String premios, String Seccion, String idINBD){
		return getEdititem(titulo,premios,Seccion,idINBD,"0");
	}
	
	private static String getEdititem(String titulo,String premios, String Seccion, String idINBD, String tipoVentana){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>ID Item</td>" +
				"<td width=120 align=LEFT><edit var=\"idItem\" width=110></td>" +
				"</tr><tr>" +
				"<td width=80 align=LEFT>Amount</td>" +
				"<td width=120 align=LEFT><edit var=\"Cantidad\" width=110></td></tr></table><center><br>"+
				"<button value=\"Add\" action=\"bypass -h " + bypassNom + " setConfig1 setProducto $idItem $Cantidad "+idINBD+" "+Seccion+" "+tipoVentana+"\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";

		MAIN_HTML = central.headFormat(temp + getPremiosLista(premios, Seccion, idINBD));
		return MAIN_HTML;
	}

	private static String setFilaBoolean(String Titulo, boolean estado, String Seccion, String idINBD){
		if(!estado){
			String color = "8A2908";
			Titulo = "<font color="+ color +">"+ Titulo +"</font>";
		}
		String Boton = "<button value=\""+ (estado?"YES":"NO") + "\" action=\"bypass -h " + bypassNom + " setConfig1 setstatus "+Seccion+" "+idINBD+" 0 0 0\" width=43 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String HTML = "<table width=270><tr><td width=225 fixwidth=220>"+Titulo+"</td><td width=45>"+Boton+"</td></tr></table>";
		return central.LineaDivisora(1) + central.headFormat(HTML);
	}

	private static String getCerrarTabla(){
		return getBtnbackConfig() + central.getPieHTML() + "</body></html>";
	}


	private static String getConfigRegister(L2PcInstance player,String idBox){
		//('465','REGISTER_EMAIL_ONLINE','false'), ('466','REGISTER_NEW_PLAYER_WAITING_TIME','60'), ('467','REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME','10'), ('468','REGISTER_NEW_PlAYER_TRIES','3')
		String inSeccion = "emailregister";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Email Register")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Email Register Active", general.REGISTER_EMAIL_ONLINE , inSeccion, "465");
		MAIN_HTML += setFilaBoolean("Chat block while data is entered", general.REGISTER_NEW_PLAYER_BLOCK_CHAT , inSeccion, "477");
		MAIN_HTML += setFilaBoolean("Check the email have banned account", general.REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT , inSeccion, "478");

		MAIN_HTML += setFilaModif("Register New Player Waiting Time", String.valueOf(general.REGISTER_NEW_PLAYER_WAITING_TIME ), inSeccion, "466");
		if(idBox.equals("466")){
			MAIN_HTML +=getEditBox("Register New Player Waiting Time (Second's)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Register Re Enter world Waiting Time", String.valueOf(general.REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME ), inSeccion, "467");
		if(idBox.equals("467")){
			MAIN_HTML +=getEditBox("Register Re Enter world Waiting Time (Second's)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Register Max. Tries", String.valueOf(general.REGISTER_NEW_PlAYER_TRIES), inSeccion, "468");
		if(idBox.equals("468")){
			MAIN_HTML +=getEditBox("Register Max. Tries", idBox, inSeccion);
		}

		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}




	private static String getConfigIPBan(L2PcInstance player, String idBox){
		String inSeccion = "banip";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("IPBan Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("BanIp Active", general.BANIP_STATUS , inSeccion, "306");
		MAIN_HTML += setFilaBoolean("BanIp Check Wan IP from Cliente", general.BANIP_CHECK_IP_INTERNET , inSeccion, "304");
		MAIN_HTML += setFilaBoolean("BanIP Check Lan IP from Cliente", general.BANIP_CHECK_IP_RED , inSeccion, "305");
		MAIN_HTML += setFilaBoolean("BanIP Disconnect all player with the<br1>same IP Wan/Lan", general.BANIP_DISCONNECT_ALL_PLAYER , inSeccion, "307");

		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}

	//Botones.add(crearBoton("Load Config","setConfig1 loadConfig 0 0 0 0 0"));

	private static String getConfigLoad(L2PcInstance player, String idBox){
		String inSeccion = "loadConfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Load Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		String BTN_load = "<button value=\"Load\" action=\"bypass -h " + bypassNom + " setConfig1 _loadConfig %LOAD% 0 "+idBox+" "+inSeccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";

		MAIN_HTML += central.LineaDivisora(1)+central.headFormat("Load All Config<br>" + BTN_load.replace("%LOAD%", "load_all")+"<br>","LEVEL") + central.LineaDivisora(1);

		MAIN_HTML += central.LineaDivisora(1)+central.headFormat("Load All Dropsearch Data<br>" + BTN_load.replace("%LOAD%", "load_dropsearch")+"<br>","LEVEL") + central.LineaDivisora(1);
		
		MAIN_HTML += central.LineaDivisora(1)+central.headFormat("Load Raidboss Event autoevent<br>" + BTN_load.replace("%LOAD%", "load_raidbossevent")+"<br>","LEVEL") + central.LineaDivisora(1);

		MAIN_HTML +=  getCerrarTabla();

		return MAIN_HTML;
	}


	/*private static String getConfigPremium(L2PcInstance player, String idBox){
		String inSeccion = "premium";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Premium Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Premium Char Account" , general.PREMIUM_CHAR , inSeccion, "388");
		MAIN_HTML += setFilaBoolean("Premium Clan Account" , general.PREMIUM_CLAN , inSeccion, "389");
		MAIN_HTML += setFilaBoolean("Premium Information Message" , general.PREMIUM_MESSAGE , inSeccion, "511");

		MAIN_HTML += setFilaModif("Premium Char Account EXP", String.valueOf(general.PREMIUM_CHAR_EXP_PORCEN ), inSeccion, "390");
		if(idBox.equals("390")){
			MAIN_HTML +=getEditBox("Premium Char Account EXP (%)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Premium Char Account SP", String.valueOf(general.PREMIUM_CHAR_SP_PORCEN ), inSeccion, "391");
		if(idBox.equals("391")){
			MAIN_HTML +=getEditBox("Premium Char Account SP (%)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Char DROP Item", String.valueOf(general.PREMIUM_CHAR_DROP_ITEM), inSeccion, "484");
		if(idBox.equals("484")){
			MAIN_HTML +=getEditBox("Premium Char DROP Item", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Premium Char Account DROP Adena", String.valueOf(general.PREMIUM_CHAR_DROP_ADENA_PORCEN), inSeccion, "394");
		if(idBox.equals("394")){
			MAIN_HTML +=getEditBox("Premium Char Account DROP Adena", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Char Account DROP Spoil", String.valueOf(general.PREMIUM_CHAR_DROP_SPOIL), inSeccion, "452");
		if(idBox.equals("452")){
			MAIN_HTML +=getEditBox("Premium Char Account DROP Spoil", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Char Account DROP Raid", String.valueOf(general.PREMIUM_CHAR_DROP_RAID), inSeccion, "454");
		if(idBox.equals("454")){
			MAIN_HTML +=getEditBox("Premium Char Account DROP Raid", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Clan Account EXP", String.valueOf(general.PREMIUM_CLAN_EXP_PORCEN ), inSeccion, "392");
		if(idBox.equals("392")){
			MAIN_HTML +=getEditBox("Premium Clan Account EXP (%)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Premium Clan Account SP", String.valueOf(general.PREMIUM_CLAN_SP_PORCEN ), inSeccion, "393");
		if(idBox.equals("393")){
			MAIN_HTML +=getEditBox("Premium Clan Account SP (%)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Premium Clan DROP Item", String.valueOf(general.PREMIUM_CLAN_DROP_ITEM), inSeccion, "485");
		if(idBox.equals("485")){
			MAIN_HTML +=getEditBox("Premium Clan DROP Item", idBox, inSeccion);
		}
		
		
		MAIN_HTML += setFilaModif("Premium Clan Account DROP Adena", String.valueOf(general.PREMIUM_CLAN_DROP_ADENA_PORCEN), inSeccion, "395");
		if(idBox.equals("395")){
			MAIN_HTML +=getEditBox("Premium Clan Account DROP Adena", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Clan Account DROP Spoil", String.valueOf(general.PREMIUM_CLAN_DROP_SPOIL), inSeccion, "453");
		if(idBox.equals("453")){
			MAIN_HTML +=getEditBox("Premium Clan Account DROP Spoil", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Clan Account DROP Raid", String.valueOf(general.PREMIUM_CLAN_DROP_RAID), inSeccion, "455");
		if(idBox.equals("455")){
			MAIN_HTML +=getEditBox("Premium Clan Account DROP Raid", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Days to Give", String.valueOf(general.PREMIUM_DAYS_GIVE ), inSeccion, "396");
		if(idBox.equals("396")){
			MAIN_HTML +=getEditBox("Premium Days to Give", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Char DC Cost", String.valueOf(general.PREMIUM_CHAR_COST_DONATION ), inSeccion, "397");
		if(idBox.equals("397")){
			MAIN_HTML +=getEditBox("Premium Char DC Cost", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Premium Clan DC Cost", String.valueOf(general.PREMIUM_CLAN_COST_DONATION ), inSeccion, "398");
		if(idBox.equals("398")){
			MAIN_HTML +=getEditBox("Premium Clan DC Cost", idBox, inSeccion);
		}

		MAIN_HTML +=  getCerrarTabla();

		return MAIN_HTML;
	}*/


	private static String getConfigBufferChar(L2PcInstance player, String idBox){
		String inSeccion = "bufferchar";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Voice Buffer Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		String btnBuffConfig = "<button value=\"Buff Config\" action=\"bypass -h admin_zeus_buff_voice 0 1 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnBuffConfig) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Voice Buffer Active", general.BUFFCHAR_ACT , inSeccion, "361");
		MAIN_HTML += setFilaBoolean("Can use on Pet", general.BUFFCHAR_PET , inSeccion, "379");
		MAIN_HTML += setFilaBoolean("Can use on Flag Mode", general.BUFFCHAR_CAN_USE_FLAG , inSeccion, "362");
		MAIN_HTML += setFilaBoolean("Can use on PK Mode", general.BUFFCHAR_CAN_USE_PK , inSeccion, "363");
		MAIN_HTML += setFilaBoolean("Can use on Combat Mode", general.BUFFCHAR_CAN_USE_COMBAT_MODE , inSeccion, "364");
		MAIN_HTML += setFilaBoolean("Can use on Siege Zone", general.BUFFCHAR_CAN_USE_SIEGE_ZONE , inSeccion, "365");
		MAIN_HTML += setFilaBoolean("Can use Individual buff", general.BUFFCHAR_CAN_USE_INDIVIDUAL_BUFF , inSeccion, "366");
		MAIN_HTML += setFilaBoolean("Show the modify button scheme", general.BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER , inSeccion, "525");

		MAIN_HTML += setFilaBoolean("Heal for Free", general.BUFFCHAR_HEAL_FOR_FREE , inSeccion, "368");
		MAIN_HTML += setFilaBoolean("Cancel buff for Free", general.BUFFCHAR_CANCEL_FOR_FREE , inSeccion, "369");

		MAIN_HTML += setFilaBoolean("Voice buffer Schemme for free", general.BUFFCHAR_FOR_FREE , inSeccion, "367");
		MAIN_HTML += setFilaBoolean("Voice buffer Individual for free", general.BUFFCHAR_INDIVIDUAL_FOR_FREE , inSeccion, "374");

		MAIN_HTML += setFilaBoolean("Voice Donation Section Active", general.BUFFCHAR_DONATION_SECCION_ACT , inSeccion, "378");
		MAIN_HTML += setFilaBoolean("Voice buffer Remove Donation Item", general.BUFFCHAR_DONATION_SECCION_REMOVE_ITEM , inSeccion, "377");

		//getEditCombo
		MAIN_HTML += setFilaModif("Donation buff Category", general.BUFFCHAR_DONATION_SECCION, inSeccion, "375");
		if(idBox.equals("375")){
			String paraCombo = "";
			Iterator itr = general.BUFF_CHAR_DATA.get("CATE").entrySet().iterator();
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	HashMap<String,String> datos = general.BUFF_CHAR_DATA.get("CATE").get((int)Entrada.getKey()) ;
		    	if(paraCombo.length()>0){
		    		paraCombo += ";";
		    	}
		    	paraCombo += datos.get("NOMCATE");
		    }
		    MAIN_HTML += getEditCombo("Donation Buff Category", paraCombo, inSeccion, idBox);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " Donation Buff", "" , inSeccion, "376");
		if(idBox.equals("376")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.BUFFCHAR_DONATION_SECCION_COST, inSeccion, idBox);
		}


		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " Voice Buffer Schemme", "" , inSeccion, "370");
		if(idBox.equals("370")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.BUFFCHAR_COST_USE, inSeccion, idBox);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " Voice Buffer Heal", "" , inSeccion, "371");
		if(idBox.equals("371")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.BUFFCHAR_COST_USE, inSeccion, idBox);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " Voice Buffer Cancel Buff", "" , inSeccion, "372");
		if(idBox.equals("372")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.BUFFCHAR_COST_USE, inSeccion, idBox);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " Voice Buffer Individual", "" , inSeccion, "373");
		if(idBox.equals("373")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.BUFFCHAR_COST_USE, inSeccion, idBox);
		}

		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}

	
	private static String getConfigChat(L2PcInstance player, String idBox){
		String inSeccion = "ChatConfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Chat Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Chat Shout (!) Block", general.CHAT_SHOUT_BLOCK , inSeccion, "540");
		MAIN_HTML += setFilaModif("Chat Shout Need Level", String.valueOf(general.CHAT_SHOUT_NEED_LEVEL) , inSeccion, "544");
		if(idBox.equals("544")){
			MAIN_HTML+= getEditBox("Chat Shout Need Level (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Shout Need Lifetime", String.valueOf(general.CHAT_SHOUT_NEED_LIFETIME) , inSeccion, "545");
		if(idBox.equals("545")){
			MAIN_HTML+= getEditBox("Chat Shout Need Lifetime (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Shout Need PvP", String.valueOf(general.CHAT_SHOUT_NEED_PVP) , inSeccion, "543");
		if(idBox.equals("543")){
			MAIN_HTML+= getEditBox("Chat Shout Need PvP (-1 = Disabled)", idBox,inSeccion);
		}
		
		
		MAIN_HTML += setFilaBoolean("Chat Trade (+) Block", general.CHAT_TRADE_BLOCK , inSeccion, "541");
		MAIN_HTML += setFilaModif("Chat Trade Need Level", String.valueOf(general.CHAT_TRADE_NEED_LEVEL) , inSeccion, "547");
		if(idBox.equals("547")){
			MAIN_HTML+= getEditBox("Chat Trade Need Level (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Trade Need Lifetime", String.valueOf(general.CHAT_TRADE_NEED_LIFETIME) , inSeccion, "548");
		if(idBox.equals("548")){
			MAIN_HTML+= getEditBox("Chat Trade Need Lifetime (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Trade Need PvP", String.valueOf(general.CHAT_TRADE_NEED_PVP) , inSeccion, "546");
		if(idBox.equals("546")){
			MAIN_HTML+= getEditBox("Chat Trade Need PvP (-1 = Disabled)", idBox,inSeccion);
		}	

		
		
		
		/*
		 *('543','CHAT_SHOUT_NEED_PVP','false'),
		 * ('545','CHAT_SHOUT_NEED_LIFETIME','false'),('546','CHAT_TRADE_NEED_PVP','false'),('547','CHAT_TRADE_NEED_LEVEL','false'),
		 * ('548','CHAT_TRADE_NEED_LIFETIME','false'),('549','CHAT_WISP_NEED_PVP','false'),('550','CHAT_WISP_NEED_LEVEL','false'),
		 * ('551','CHAT_WISP_NEED_LIFETIME','false')
		 * 
		 * */
		
		
		MAIN_HTML += setFilaBoolean("Chat Wisp (\") Block", general.CHAT_WISP_BLOCK , inSeccion, "542");
		MAIN_HTML += setFilaModif("Chat Wisp Need Level", String.valueOf(general.CHAT_WISP_NEED_LEVEL) , inSeccion, "550");
		if(idBox.equals("550")){
			MAIN_HTML+= getEditBox("Chat Wisp Need Level (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Wisp Need Lifetime", String.valueOf(general.CHAT_WISP_NEED_LIFETIME) , inSeccion, "551");
		if(idBox.equals("551")){
			MAIN_HTML+= getEditBox("Chat Wisp Need Lifetime (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Wisp Need PvP", String.valueOf(general.CHAT_WISP_NEED_PVP) , inSeccion, "549");
		if(idBox.equals("549")){
			MAIN_HTML+= getEditBox("Chat Wisp Need PvP (-1 = Disabled)", idBox,inSeccion);
		}
		
		
		return MAIN_HTML;
	}
	

	private static String getConfigDualBox(L2PcInstance player, String idBox) {
		String inSeccion = "dualbox";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Dual Box Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("IP Check", general.MAX_IP_CHECK , inSeccion, "400");
		MAIN_HTML += setFilaModif("Max IP Normal Player allow", String.valueOf(general.MAX_IP_COUNT),inSeccion,"401");
		if(idBox.equals("401")){
			MAIN_HTML+= getEditBox("Max IP Nomal Player allow", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Max IP Premium Player allow", String.valueOf(general.MAX_IP_VIP_COUNT),inSeccion,"403");
		if(idBox.equals("403")){
			MAIN_HTML+= getEditBox("Max IP Premium Player allow", idBox,inSeccion);
		}
		//MAIN_HTML += setFilaBoolean("IP Check Recording Data", general.MAX_IP_RECORD_DATA , inSeccion, "402");
		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;

	}



	private static String getConfigOly(L2PcInstance player, String idBox){
		String inSeccion = "Olymp";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Olympics Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Announce Opponent Class", general.ANNOUCE_CLASS_OPONENT_OLY , inSeccion, "250");
		MAIN_HTML += setFilaBoolean("Show the Opponent Name", general.OLY_ANTIFEED_SHOW_NAME_OPPO , inSeccion, "285");
		
		MAIN_HTML += setFilaBoolean("Show Scheme Buffer Windows", general.OLY_CAN_USE_SCHEME_BUFFER , inSeccion, "597");
		
		MAIN_HTML += setFilaBoolean("Show Class in Title", general.OLY_ANTIFEED_SHOW_IN_NAME_CLASS , inSeccion, "381");
		//OLY_ANTIFEED_SHOW_IN_NAME_CLASS
		MAIN_HTML += setFilaBoolean("Change template from Char in Olys", general.OLY_ANTIFEED_CHANGE_TEMPLATE , inSeccion, "282");
		MAIN_HTML += setFilaBoolean("Show the Name in NPC", general.OLY_ANTIFEED_SHOW_NAME_NPC , inSeccion, "283");
		
		MAIN_HTML += setFilaBoolean("Block Dualbox IP", general.OLY_DUAL_BOX_CONTROL , inSeccion, "528");
		
		MAIN_HTML += setFilaModif("Seconds to inform Opponent class", "", inSeccion, "284");
		if(idBox.equals("284")){
			MAIN_HTML += getEditLista_VectorInteger("Seconds to inform Opponent class",passVectorFromArray_int(general.OLY_SECOND_SHOW_OPPONET),inSeccion,idBox);
		}
		MAIN_HTML += setFilaModif("ID Access to use Oly Admin Command", "", inSeccion, "286");
		if(idBox.equals("286")){
			MAIN_HTML += getEditLista_VectorInteger("ID Access to use Oly Admin Command<br1>Default: 127, 8",passVectorFromArray_int(general.OLY_ACCESS_ID_MODIFICAR_POINT),inSeccion,idBox);
		}
		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}


	private static String getConfigTransform(L2PcInstance player, String idBox){
		String inSeccion = "transform";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Transformations Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean(msg.CONFIG_ONLY_NOBLES, general.TRANSFORMATION_NOBLE, inSeccion, "253");

		MAIN_HTML += setFilaBoolean("Special transformations enabled", general.TRANSFORMATION_ESPECIALES, inSeccion, "256");

		MAIN_HTML += setFilaBoolean("RaidBoss transformations enabled", general.TRANSFORMATION_RAIDBOSS, inSeccion, "257");
		
		MAIN_HTML += setFilaBoolean("Special & RaidBoss Trans. with Time", general.TRANSFORM_TIME, inSeccion, "515");

		MAIN_HTML += setFilaModif("Transformation time (Minutes)", String.valueOf(general.TRANSFORM_TIME_MINUTES), inSeccion, "516");
		if(idBox.equals("516")){
			MAIN_HTML += getEditBox("Transformation time (Minutes)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Transformation Reuse time (Minutes)", String.valueOf(general.TRANSFORM_REUSE_TIME_MINUTES), inSeccion, "517");
		if(idBox.equals("517")){
			MAIN_HTML += getEditBox("Transformation Reuse time (Minutes)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.TRANSFORMATION_LVL), inSeccion, "254");
		if(idBox.equals("254")){
			MAIN_HTML += getEditBox(msg.CONFIG_EXPLICA_MIN_LVL_CAN_USE, idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " normal Transformations", "" , inSeccion, "255");
		if(idBox.equals("255")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.TRANSFORMATION_PRICE, inSeccion, "255");
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " Special Transformations", "" , inSeccion, "259");
		if(idBox.equals("259")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.TRANSFORMATION_ESPECIALES_PRICE, inSeccion, "259");
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_COST_FOR + " Raidboss Transformations", "" , inSeccion, "260");
		if(idBox.equals("260")){
			MAIN_HTML += getEdititem(msg.CONFIG_COST, general.TRANSFORMATION_RAIDBOSS_PRICE, inSeccion, "260");
		}


		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getConfigTownWarEvent(L2PcInstance player, String idBox){
		String inSeccion = "TownWarEvent";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Town War Event")+central.LineaDivisora(2) + central.LineaDivisora(1);
		
		MAIN_HTML += setFilaBoolean( "Town War Autoevent" ,general.EVENT_TOWN_WAR_AUTOEVENT,inSeccion,"553");
		
		MAIN_HTML += setFilaBoolean( "Give PvP Reward from PVP Sytem" ,general.EVENT_TOWN_WAR_GIVE_PVP_REWARD,inSeccion,"559");
		
		MAIN_HTML += setFilaBoolean( "Give Reward to the Top Killer" ,general.EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER,inSeccion,"560");
		
		MAIN_HTML += setFilaBoolean( "Town War Event on Random City" ,general.EVENT_TOWN_WAR_RANDOM_CITY,inSeccion,"563");
		
		MAIN_HTML += setFilaBoolean( "Check Dual Box" ,general.EVENT_TOWN_WAR_DUAL_BOX_CHECK ,inSeccion,"564");
		
		MAIN_HTML += setFilaBoolean( "Block Selected NPC" ,general.EVENT_TOWN_WAR_HIDE_NPC ,inSeccion,"565");
		
		MAIN_HTML += setFilaBoolean( "Can use Buffer" ,general.EVENT_TOWN_WAR_CAN_USE_BUFFER ,inSeccion,"567");
				 
		//('566','EVENT_TOWN_WAR_NPC_ID_HIDE','')

		
		
		
		MAIN_HTML += setFilaModif("Town for the Event: ", general.EVENT_TOWN_WAR_CITY_ON_WAR, inSeccion, "556");
		if(idBox.equals("556")){
			String ZONESS = "GLUDIN,GLUDIO,DION,GIRAN,OREN,H.VILLAGE,ADEN,"
					+ "GODDARD,RUNE,HEINE,SCHUTTGART";
			
			String ComboZones = "";
			for(String Zns : ZONESS.split(",")){
				if(!Zns.equalsIgnoreCase(general.EVENT_TOWN_WAR_CITY_ON_WAR)){
					if(ComboZones.length()>0){
						ComboZones+=";";
					}
					ComboZones += Zns;
				}
			}			
			MAIN_HTML += getEditComboBox("Select Town for the event",idBox,inSeccion,ComboZones);
		}
		
		MAIN_HTML += setFilaModif("Minutes to Start on Server R.", String.valueOf(general.EVENT_TOWN_WAR_MINUTES_START_SERVER),inSeccion,"554");
		if(idBox.equals("554")){
			MAIN_HTML += getEditBox("Minutes to Start on Server R.", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Minutes Interval", String.valueOf(general.EVENT_TOWN_WAR_MINUTES_INTERVAL),inSeccion,"555");
		if(idBox.equals("555")){
			MAIN_HTML += getEditBox("Minutes Interval", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Minutes Duration", String.valueOf(general.EVENT_TOWN_WAR_MINUTES_EVENT),inSeccion,"557");
		if(idBox.equals("557")){
			MAIN_HTML += getEditBox("Minutes Duration", idBox, inSeccion);
		}
		
		
		MAIN_HTML += setFilaModif("Minutes To Wait Start", String.valueOf(general.EVENT_TOWN_WAR_JOIN_TIME),inSeccion,"558");
		if(idBox.equals("558")){
			MAIN_HTML += getEditBox("Minutes o Wait Start", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("General Reward", "", inSeccion, "561");
		if(idBox.equals("561")){
			//MAIN_HTML += getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox);
			opera.enviarHTML(player, getEdititem("Town War General Reward",general.EVENT_TOWN_WAR_REWARD_GENERAL,inSeccion,idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("Top player killing Reward", "", inSeccion, "562");
		if(idBox.equals("562")){
			//MAIN_HTML += getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox);
			opera.enviarHTML(player, getEdititem("Top Player Killing Reward",general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER,inSeccion,idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("ID NPC to Block", "", inSeccion, "566");
		if(idBox.equals("566")){
			opera.enviarHTML(player,getEditLista_StringInteger("ID NPC to Block",general.EVENT_TOWN_WAR_NPC_ID_HIDE,inSeccion,idBox,true));
			return "";
			//MAIN_HTML += getEditListaNPC_VectorInteger("NPC For Search.",general.EVENT_RAIDBOSS_RAID_ID ,inSeccion,idBox);
		}
		
		return MAIN_HTML + getCerrarTabla();
	}
	
	
	
	
	//getConfigRaidBossEvent(player,idBox);
	private static String getConfigRaidBossEvent(L2PcInstance player, String idBox){
		String inSeccion = "RaidBossEvent";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Raid Boss Event")+central.LineaDivisora(2) + central.LineaDivisora(1);
		
		MAIN_HTML += setFilaBoolean( "Raid Boss Autoevent" ,general.EVENT_RAIDBOSS_AUTOEVENT,inSeccion,"508");
		
		MAIN_HTML += setFilaModif("Raid Boss Event Winner Reward", "", inSeccion, "490");
		if(idBox.equals("490")){
			//MAIN_HTML += getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox);
			opera.enviarHTML(player, getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("Raid Boss Event looser Reward", "", inSeccion, "491");
		if(idBox.equals("491")){
			opera.enviarHTML(player,getEdititem("Raid Boss Event Looser Reward",general.EVENT_RAIDBOSS_REWARD_LOOSER,inSeccion,idBox,true));
			return "";
		}		

		MAIN_HTML += setFilaModif("Raid Boss Spawn Position (x,y,z)",general.EVENT_RAIDBOSS_RAID_POSITION,inSeccion, "487");
		if(idBox.equals("487")){
			MAIN_HTML += getEditBigBox("Raid Boss Spawn Position (x,y,z)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Player Spawn Position (x,y,z)",general.EVENT_RAIDBOSS_PLAYER_POSITION,inSeccion, "488");
		if(idBox.equals("488")){
			MAIN_HTML += getEditBigBox("Player Spawn Position (x,y,z)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Min. Level", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_LEVEL),inSeccion,"492");
		if(idBox.equals("492")){
			MAIN_HTML += getEditBox("Raid Boss Min. Level", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Max. Level", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_LEVEL),inSeccion,"493");
		if(idBox.equals("493")){
			MAIN_HTML += getEditBox("Raid Boss Max. Level", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Min. Register Player to begin", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS),inSeccion,"494");
		if(idBox.equals("494")){
			MAIN_HTML += getEditBox("Raid Boss Min. Register Player to begin", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Max. Register Player", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS),inSeccion,"495");
		if(idBox.equals("495")){
			MAIN_HTML += getEditBox("Raid Boss Max. Register Player", idBox, inSeccion);
		}		
		MAIN_HTML += setFilaModif("Raid Boss Join Time (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_JOINTIME),inSeccion,"497");
		if(idBox.equals("497")){
			MAIN_HTML += getEditBox("Raid Boss Join Time (Minutes)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Event Time (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_EVENT_TIME),inSeccion,"498");
		if(idBox.equals("498")){
			MAIN_HTML += getEditBox("Raid Boss Event Time (Minutes)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Raid Boss Second Teleport into the RB Event.", String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS),inSeccion,"504");
		if(idBox.equals("504")){
			MAIN_HTML += getEditBox("Raid Boss Second Teleport into the RB Event.", idBox, inSeccion);
		}		
				
		MAIN_HTML += setFilaModif("Raid Boss Second to return back to City", String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_BACK),inSeccion,"496");
		if(idBox.equals("496")){
			MAIN_HTML += getEditBox("Raid Boss Second to return back to City", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Second to Revive", String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_REVIVE),inSeccion,"503");
		if(idBox.equals("503")){
			MAIN_HTML += getEditBox("Raid Boss Second to come back to the village", idBox, inSeccion);
		}		
		MAIN_HTML += setFilaModif("Raid Boss HTML color Name", general.EVENT_RAIDBOSS_COLORNAME ,inSeccion,"499");
		if(idBox.equals("499")){
			MAIN_HTML += getEditBox("Raid Boss HTML color Name", idBox, inSeccion);
		}
		MAIN_HTML += setFilaBoolean( "Raid Boss immobilize when teleport" ,general.EVENT_RAIDBOSS_PLAYER_INMOBIL,inSeccion,"489");
		MAIN_HTML += setFilaBoolean( "Raid Boss Check Dual Box" ,general.EVENT_RAIDBOSS_CHECK_DUALBOX,inSeccion,"500");
		MAIN_HTML += setFilaBoolean( "Raid Boss Cancel Buff" ,general.EVENT_RAIDBOSS_CANCEL_BUFF,inSeccion,"501");
		MAIN_HTML += setFilaBoolean( "Raid Boss Unsummon pet's" ,general.EVENT_RAIDBOSS_UNSUMMON_PET,inSeccion,"502");
		
		MAIN_HTML += setFilaModif("Raid Boss Event ID NPC", "", inSeccion, "486");
		if(idBox.equals("486")){
			opera.enviarHTML(player,getEditLista_VectorInteger("Raid Boss Event ID NPC",general.EVENT_RAIDBOSS_RAID_ID,inSeccion,idBox,true));
			return "";
			//MAIN_HTML += getEditListaNPC_VectorInteger("NPC For Search.",general.EVENT_RAIDBOSS_RAID_ID ,inSeccion,idBox);
		}
		
		/*MAIN_HTML += setFilaModif("Raid Boss Spawn Hours(HH:MM;HH:MM)",general.EVENT_RAIDBOSS_HOUR_TO_START,inSeccion, "507");
		if(idBox.equals("507")){
			MAIN_HTML += getEditBigBox("Raid Boss Spawn Hours(HH:MM;HH:MM)", idBox, inSeccion);
		}*/
		
		MAIN_HTML += setFilaModif("Raid Boss Event Interval (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_MINUTE_INTERVAL),inSeccion,"523");
		if(idBox.equals("523")){
			MAIN_HTML += getEditBox("Raid Boss Event Interval (Minutes)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Raid Boss Event Interval on Start Server (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER),inSeccion,"524");
		if(idBox.equals("524")){
			MAIN_HTML += getEditBox("Raid Boss Event Interval (Minutes)", idBox, inSeccion);
		}		
		
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigCancelBuff(L2PcInstance player, String idBox){
		String inSeccion = "cancelbuff";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";

		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Cance Buff Recovery System")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean( "Restore Canceled Buff Activated" ,general.RETURN_BUFF,inSeccion,"353");

		MAIN_HTML += setFilaBoolean( "Restore Canceled Buff In Olys" ,general.RETURN_BUFF_IN_OLY,inSeccion,"355");

		MAIN_HTML += setFilaModif("Seconds it takes to restore Canceled Buff", String.valueOf(general.RETURN_BUFF_SECONDS_TO_RETURN),inSeccion,"354");
		if(idBox.equals("354")){
			MAIN_HTML += getEditBox("Seconds it takes to restore Canceled Buff", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minutes it takes to restore Canceled Buff on Olys", String.valueOf(general.RETURN_BUFF_IN_OLY_MINUTES_TO_RETURN),inSeccion,"356");
		if(idBox.equals("356")){
			MAIN_HTML += getEditBox("Minutes it takes to restore Canceled Buff on Olys", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("ID Buff that can not be stolen with Steal Divinity", "", inSeccion, "357");
		if(idBox.equals("357")){
			MAIN_HTML += getEditListaNPC_VectorInteger("ID Buff that can not be stolen with Steal Divinity",general.RETURN_BUFF_NOT_STEALING ,inSeccion,idBox);
		}

		return MAIN_HTML + getCerrarTabla();
	}



	private static String getConfigPKCantidad(L2PcInstance player, String idBox){
		String inSeccion = "pkColorCantidad";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";

		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PK Amount")+central.LineaDivisora(2) + central.LineaDivisora(1);

		int Cantidades[] = {general.PK_AMOUNT_1, general.PK_AMOUNT_2,general.PK_AMOUNT_3,general.PK_AMOUNT_4,general.PK_AMOUNT_5,general.PK_AMOUNT_6,general.PK_AMOUNT_7,general.PK_AMOUNT_8,general.PK_AMOUNT_9,general.PK_AMOUNT_10};

		for(int i=0;i<10;i++){
			MAIN_HTML += setFilaModif("Amount of PK to Title Color " + String.valueOf(i+1), String.valueOf(Cantidades[i]), inSeccion,String.valueOf(i+200));
			if(idBox.equals(String.valueOf(i+200))){
				MAIN_HTML += getEditBox(msg.CONFIG_QUANTITY_FOR + " " + String.valueOf(i+1), idBox, inSeccion);
			}
		}
		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvppkColor 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML;
	}


	private static String getConfigPvPCantidad(L2PcInstance player, String idBox){
		String inSeccion = "pvpColorCantidad";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PvP Amount")+central.LineaDivisora(2) + central.LineaDivisora(1);

		int Cantidades[] = {general.PVP_AMOUNT_1, general.PVP_AMOUNT_2,general.PVP_AMOUNT_3,general.PVP_AMOUNT_4,general.PVP_AMOUNT_5,general.PVP_AMOUNT_6,general.PVP_AMOUNT_7,general.PVP_AMOUNT_8,general.PVP_AMOUNT_9,general.PVP_AMOUNT_10};

		for(int i=0;i<10;i++){
			MAIN_HTML += setFilaModif("Amount of PVP to Name Color" + String.valueOf(i+1), String.valueOf(Cantidades[i]), inSeccion,String.valueOf(i+180));
			if(idBox.equals(String.valueOf(i+180))){
				MAIN_HTML += getEditBox(msg.CONFIG_QUANTITY_FOR + " " + String.valueOf(i+1), idBox, inSeccion);
			}
		}
		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvppkColor 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getConfigCicloMensajes_PK(L2PcInstance player, String idBox){
		String inSeccion = "pvpMensajePK";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PK Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		int CantidadesPK[] = {general.CANTIDAD_CICLO_MENSAJE_PK_1,general.CANTIDAD_CICLO_MENSAJE_PK_2,general.CANTIDAD_CICLO_MENSAJE_PK_3,general.CANTIDAD_CICLO_MENSAJE_PK_4,general.CANTIDAD_CICLO_MENSAJE_PK_5};

		String MensajesPK [] = {general.CICLO_MENSAJE_PK_1,general.CICLO_MENSAJE_PK_2,general.CICLO_MENSAJE_PK_3,general.CICLO_MENSAJE_PK_4,general.CICLO_MENSAJE_PK_5};

		for(int i=0;i<5;i++){
			MAIN_HTML += setFilaModif("Number of pk for Message " + String.valueOf(i+1), String.valueOf(CantidadesPK[i]), inSeccion,String.valueOf(i+226));
			if(idBox.equals(String.valueOf(i+226))){
				MAIN_HTML += getEditBox(msg.CONFIG_ONLY_NUMBER+" "+ String.valueOf(i+1), idBox, inSeccion);
			}
		}

		for(int i=0;i<5;i++){
			MAIN_HTML += setFilaModif(msg.CONFIG_ADD_MENSAJE_POSITION+" " + String.valueOf(i+1), String.valueOf(MensajesPK[i]), inSeccion,String.valueOf(i+231));
			if(idBox.equals(String.valueOf(i+231))){
				MAIN_HTML += getEditBigBox(msg.CONFIG_ADD_MENSAJE_POSITION + " "+ String.valueOf(i+1) + "<br1>Use: %CHAR_NAME% For Player Name<br1>%CANT% For PK Ammount", idBox, inSeccion);
			}
		}


		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvpMensaje 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML;

	}

	private static String getConfigCicloMensajes_PVP(L2PcInstance player, String idBox){
		String inSeccion = "pvpMensajePVP";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PvP Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		int CantidadesPVP[] = {general.CANTIDAD_CICLO_MENSAJE_PVP_1,general.CANTIDAD_CICLO_MENSAJE_PVP_2,general.CANTIDAD_CICLO_MENSAJE_PVP_3,general.CANTIDAD_CICLO_MENSAJE_PVP_4,general.CANTIDAD_CICLO_MENSAJE_PVP_5};

		String MensajesPVP [] = {general.CICLO_MENSAJE_PVP_1,general.CICLO_MENSAJE_PVP_2,general.CICLO_MENSAJE_PVP_3,general.CICLO_MENSAJE_PVP_4,general.CICLO_MENSAJE_PVP_5};

		for(int i=0;i<5;i++){
			MAIN_HTML += setFilaModif("Number of pvp for Message " + String.valueOf(i+1), String.valueOf(CantidadesPVP[i]), inSeccion,String.valueOf(i+221));
			if(idBox.equals(String.valueOf(i+221))){
				MAIN_HTML += getEditBox(msg.CONFIG_ONLY_NUMBER + " "+ String.valueOf(i+1), idBox, inSeccion);
			}
		}

		for(int i=0;i<5;i++){
			MAIN_HTML += setFilaModif(msg.CONFIG_ADD_MENSAJE_POSITION + " " + String.valueOf(i+1), String.valueOf(MensajesPVP[i]), inSeccion,String.valueOf(i+231));
			if(idBox.equals(String.valueOf(i+231))){
				MAIN_HTML += getEditBigBox(msg.CONFIG_ADD_MENSAJE_POSITION + " "+ String.valueOf(i+1) + "<br1>Use: %CHAR_NAME% For Player Name <br1>%CANT% For PvP Ammount", idBox, inSeccion);
			}
		}


		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvpMensaje 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML;

	}


	private static String getConfigRaidAnnoucement(L2PcInstance player, String idBox){
		String inSeccion = "raidconfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Raid Boss Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Announce death and a Raid Boss respawn", general.ANNOUCE_RAID_BOS_STATUS, inSeccion, "241");

		MAIN_HTML += setFilaModif("ID of announcement", String.valueOf(general.RAID_ANNOUCEMENT_ID_ANNOUCEMENT), inSeccion, "244");
		if(idBox.equals("244")){
			MAIN_HTML += getEditBox("Use ID: <br1>Annoucement=10<br1>Party Room Commander=15<br1>Critical Announce=18<br1>Alliance=9", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Message Raid Boss Die", String.valueOf(general.RAID_ANNOUCEMENT_DIED), inSeccion, "242");
		if(idBox.equals("242")){
			MAIN_HTML += getEditBigBox("Input the message when the Raid Boss dies<br1>Use: %RAID_NAME% Raid Boss Name<br1>%DATE% Respawn Date", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Message Raid Boss Respawn", String.valueOf(general.RAID_ANNOUCEMENT_LIFE), inSeccion, "243");
		if(idBox.equals("243")){
			MAIN_HTML += getEditBigBox("Input the Message when Raid Boss Respawn <br1>Use: %RAID_NAME% Raid Boss Name", idBox, inSeccion);
		}


		MAIN_HTML += getCerrarTabla();
		return MAIN_HTML;
	}



	private static String getConfigCicloMensajes(L2PcInstance player, String idBox){
		String inSeccion = "pvpMensaje";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Cycle Message for PVP / PK")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Announce Cycle PvP / PK of the Player", general.MENSAJE_PVP_PK_CICLOS, inSeccion, "220");

		String CicloPvP = "<button value=\"PvP Cycle\" action=\"bypass -h " + bypassNom + " setConfig1 pvpMensajePVP 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String CicloPK = "<button value=\"PK Cycle\" action=\"bypass -h " + bypassNom + " setConfig1 pvpMensajePK 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String botonConf = "<table width=260><tr><td width=130 align=center>"+CicloPvP+"</td><td width=130 align=center>"+CicloPK+"</td></tr></table>";

		MAIN_HTML += central.LineaDivisora(3) + central.headFormat(botonConf) + central.LineaDivisora(3);

		MAIN_HTML += getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getConfigPKColor(L2PcInstance player, String idBox){
		String inSeccion = "pkColor";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PK Color System Config.")+central.LineaDivisora(2) + central.LineaDivisora(1);

		for(int i=210;i<=219;i++){
			MAIN_HTML += setFilaModif("Color Title For "+ String.valueOf(i-209), "<font color="+general.TITLE_COLOR_FOR_ALL[i-210]+">THIS("+general.TITLE_COLOR_FOR_ALL[i-210]+")</font>", inSeccion, String.valueOf(i));
			if(idBox.equals(String.valueOf(i))){
				MAIN_HTML += getEditBox("Input the PK Title Color HTML Code " + String.valueOf(i-209), idBox, inSeccion);
			}
		}
		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvppkColor 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigPVPColor(L2PcInstance player, String idBox){
		String inSeccion = "pvpColor";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PvP Color System Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		for(int i=190;i<=199;i++){
			MAIN_HTML += setFilaModif("Color Name For "+ String.valueOf(i-189), "<font color="+general.NAME_COLOR_FOR_ALL[i-190]+">THIS("+general.NAME_COLOR_FOR_ALL[i-190]+")</font>", inSeccion, String.valueOf(i));
			if(idBox.equals(String.valueOf(i))){
				MAIN_HTML += getEditBox("Input the PvP Name Color HTML Code " + String.valueOf(i - 189), idBox, inSeccion);
			}
		}
		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvppkColor 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigPVPPKColor(L2PcInstance player, String idBox){
		String inSeccion ="pvppkColor";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center><table width=280>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PVP Color Sytem Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		String btnCantidadPVP = "<button value=\"PvP Amount\" action=\"bypass -h " + bypassNom + " setConfig1 pvpColorCantidad 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnCantidadPK = "<button value=\"PK Amount\" action=\"bypass -h " + bypassNom + " setConfig1 pkColorCantidad 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnColorPVP = "<button value=\"PvP Color Name\" action=\"bypass -h " + bypassNom + " setConfig1 pvpColor 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnColorPK = "<button value=\"PK Color Title\" action=\"bypass -h " + bypassNom + " setConfig1 pkColor 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String botonConf = "<table width=260><tr><td width=130 align=center>"+btnCantidadPVP+"</td><td width=130 align=center>"+btnCantidadPK+"</td></tr></table>";
		botonConf += "<table width=260><tr><td width=130 align=center>"+btnColorPVP+"</td><td width=130 align=center>"+btnColorPK+"</td></tr></table>";

		MAIN_HTML += central.LineaDivisora(3) + central.headFormat(botonConf) + central.LineaDivisora(3);

		MAIN_HTML += setFilaBoolean("PvP Color Name System",general.PVP_COLOR_SYSTEM_ENABLED,inSeccion,"178");
		MAIN_HTML += setFilaBoolean("PK Color Title System",general.PK_COLOR_SYSTEM_ENABLED,inSeccion,"179");
		MAIN_HTML += getCerrarTabla();
		return MAIN_HTML;

	}

	private static String getConfigDelevel(L2PcInstance player, String idBox){
		String inSeccion = "delevel";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Delevel Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Delevel Prices", "", inSeccion, "133");
		if(idBox.equals("133")){
			MAIN_HTML += getEdititem("Input Delevel Prices",general.DELEVEL_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Min level can use", String.valueOf(general.DELEVEL_LVL_MAX),inSeccion,"134");
		if(idBox.equals("134")){
			MAIN_HTML += getEditBox("Minimum level that can reach", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean(msg.CONFIG_ONLY_NOBLES,general.DELEVEL_NOBLE,inSeccion,"135");

		MAIN_HTML += setFilaBoolean("Remove Invalid Skill from Player", general.DELEVEL_CHECK_SKILL, inSeccion, "399");

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigChangeName(L2PcInstance player, String idBox){
		String inSeccion = "changeName";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Name Change Char / Clan")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Change Name Cost", "", inSeccion, "125");
		if(idBox.equals("125")){
			MAIN_HTML += getEdititem("Change Name Cost",general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean("Only nobles can change Name", general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE, inSeccion, "127");

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL),inSeccion,"128");
		if(idBox.equals("128")){
			MAIN_HTML += getEditBox("Min level to change name", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Change Clan Name Cost", "", inSeccion, "126");
		if(idBox.equals("126")){
			MAIN_HTML += getEdititem("Change Clan Name Cost",general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Min. Clan LvL to Change Name", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL),inSeccion,"129");
		if(idBox.equals("129")){
			MAIN_HTML += getEditBox("Min. Clan LvL to Change Name", idBox, inSeccion);
		}	

		
		return MAIN_HTML + getCerrarTabla();
	}
	

	private static String getConfigVarios(L2PcInstance player, String idBox){
		String inSeccion = "opcvarias";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Miscellaneous Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Change Gender", general.OPCIONES_CHAR_SEXO, inSeccion, "261");
		MAIN_HTML += setFilaModif("Change Gender Cost", "", inSeccion, "122");
		if(idBox.equals("122")){
			MAIN_HTML += getEdititem("Change Gender Cost",general.OPCIONES_CHAR_SEXO_ITEM_PRICE ,inSeccion,idBox);
		}		

		MAIN_HTML += setFilaBoolean("Noble", general.OPCIONES_CHAR_NOBLE, inSeccion, "262");
		MAIN_HTML += setFilaModif("Noble Cost", "", inSeccion, "123");
		if(idBox.equals("123")){
			MAIN_HTML += getEdititem("Noble Cost",general.OPCIONES_CHAR_NOBLE_ITEM_PRICE ,inSeccion,idBox);
		}		

		MAIN_HTML += setFilaBoolean("LvL 85", general.OPCIONES_CHAR_LVL85, inSeccion, "263");
		MAIN_HTML += setFilaModif("lvl 85 Cost", "", inSeccion, "124");
		if(idBox.equals("124")){
			MAIN_HTML += getEdititem("lvl 85 Cost",general.OPCIONES_CHAR_LVL85_ITEM_PRICE ,inSeccion,idBox);
		}		

		MAIN_HTML += setFilaBoolean("Char AIO Buffer", general.OPCIONES_CHAR_BUFFER_AIO, inSeccion, "264");
		MAIN_HTML += setFilaModif("AIO Buffer Cost", "", inSeccion, "265");
		if(idBox.equals("265")){
			MAIN_HTML += getEdititem("AIO Buffer Cost",general.OPCIONES_CHAR_BUFFER_AIO_PRICE ,inSeccion,idBox);
		}
		
		MAIN_HTML += setFilaBoolean("Char AIO+30 Buffer", general.OPCIONES_CHAR_BUFFER_AIO_30, inSeccion, "585");
		MAIN_HTML += setFilaModif("AIO+30 Buffer Cost", "", inSeccion, "586");
		if(idBox.equals("586")){
			MAIN_HTML += getEdititem("AIO+30 Buffer Cost",general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30 ,inSeccion,idBox);
		}
		
		
		//('585','OPCIONES_CHAR_BUFFER_AIO_30','false'), ('586','OPCIONES_CHAR_BUFFER_AIO_PRICE_30','3470,800')
		
		
		MAIN_HTML += setFilaModif("Min. LvL to Create AIO Buffer", String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL),inSeccion,"268");
		if(idBox.equals("268")){
			MAIN_HTML += getEditBox("Min. LvL to Create AIO Buffer", idBox, inSeccion);
		}		

		MAIN_HTML += setFilaBoolean("Fame", general.OPCIONES_CHAR_FAME, inSeccion, "276");
		MAIN_HTML += setFilaModif("Fame Cost", "", inSeccion, "277");
		if(idBox.equals("277")){
			MAIN_HTML += getEdititem("Famce Cost",general.OPCIONES_CHAR_FAME_PRICE ,inSeccion,idBox);
		}		
		MAIN_HTML += setFilaModif("Fame Ammount to Give", String.valueOf(general.OPCIONES_CHAR_FAME_GIVE),inSeccion,"280");
		if(idBox.equals("280")){
			MAIN_HTML += getEditBox("Fame Ammount to Give", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Fame Noble Only", general.OPCIONES_CHAR_FAME_NOBLE, inSeccion, "278");

		MAIN_HTML += setFilaModif("Min. Level to Fame", String.valueOf(general.OPCIONES_CHAR_FAME_LVL),inSeccion,"279");
		if(idBox.equals("279")){
			MAIN_HTML += getEditBox("Min. Level to Fame", idBox, inSeccion);
		}
		
		//MAIN_HTML += setFilaBoolean("Change Player Name", general.OPCIONES_CHAR_CAMBIO_NOMBRE, inSeccion, "479");//('479','OPCIONES_CHAR_CAMBIO_NOMBRE

		//MAIN_HTML += setFilaBoolean("Change Clan Name", general.OPCIONES_CLAN_CAMBIO_NOMBRE, inSeccion, "480");//('480','OPCIONES_CLAN_CAMBIO_NOMBRE

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigRaidbossInfo(L2PcInstance player, String idBox){
		String inSeccion = "bossinfo";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Raid Boss Info Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("RaidBoss Direct Teleport", general.RAIDBOSS_INFO_TELEPORT , inSeccion, "119");
		
		MAIN_HTML += setFilaBoolean("RaidBoss Observe Mode", general.RAIDBOSS_OBSERVE_MODE , inSeccion, "595");
		
		MAIN_HTML += setFilaBoolean("Just Noble Can use RB Direct Tele.", general.RAIDBOSS_INFO_NOBLE , inSeccion, "120");

		MAIN_HTML += setFilaModif("RB Teleport Cost", "", inSeccion, "117");

		if(idBox.equals("117")){
			MAIN_HTML += getEdititem("RB Teleport Cost",general.RAIDBOSS_INFO_TELEPORT_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Number of RB to display", String.valueOf(general.RAIDBOSS_INFO_LISTA_X_HOJA),inSeccion,"118");
		if(idBox.equals("118")){
			MAIN_HTML += getEditBox("Number of RB to display (Recommends 25)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Min. LvL to use RB Direct Teleport", String.valueOf(general.RAIDBOSS_INFO_LISTA_X_HOJA),inSeccion,"121");
		if(idBox.equals("121")){
			MAIN_HTML += getEditBox("Min. LvL to use RB Direct Teleport", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Raidboss Blacklist (List of Raid that can not be reached from RaidBoss Info teleport)", "", inSeccion, "351");
		if(idBox.equals("351")){
			MAIN_HTML += getEditListaNPC_VectorInteger("RaidBoss Blacklist",general.RAIDBOSS_ID_MOB_NO_TELEPORT ,inSeccion,idBox);
		}

		return MAIN_HTML + getCerrarTabla();
	}




	private static String getConfigColor(L2PcInstance player, String idBox){
		String inSeccion = "colorname";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Color Name Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Color Name Cost", "", inSeccion, "104");
		if(idBox.equals("104")){
			MAIN_HTML += getEdititem("Color Name Cost",general.PINTAR_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Colors", "", inSeccion, "105");
		if(idBox.equals("105")){
			MAIN_HTML += getColorMenu("Input the Name Colors HTML Code", general.PINTAR_MATRIZ, inSeccion, idBox);
		}

		return MAIN_HTML + getCerrarTabla();
	}


	private static String getConfigDesafios(L2PcInstance player, String idBox){
		String inSeccion = "desafio";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("The Challenge Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		String BOTON = "<button value=\"Reward and NPC Found\" action=\"bypass -h " + bypassNom + " DESAFIO 88 0 0\" width=155 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON) + central.LineaDivisora(2);

		MAIN_HTML += setFilaBoolean("lvl 85 Challenge", general.DESAFIO_LVL85, inSeccion, "273");

		MAIN_HTML += setFilaModif("Number of lvl 85 to Win", String.valueOf(general.DESAFIO_MAX_LVL85), inSeccion, "271");
		if(idBox.equals("271")){
			MAIN_HTML += getEditBox("Number of LvL 85 to Win",idBox,inSeccion);
		}

		MAIN_HTML += setFilaModif("Reward for the Firts lvl 85", "", inSeccion, "85");
		if(idBox.equals("85")){
			MAIN_HTML += getEdititem("Reward for the Firts lvl 85",general.DESAFIO_85_PREMIO ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean("Noble Challenge", general.DESAFIO_NOBLE, inSeccion, "274");


		MAIN_HTML += setFilaModif("Number of Noble to Win", String.valueOf(general.DESAFIO_MAX_NOBLE), inSeccion, "272");
		if(idBox.equals("272")){
			MAIN_HTML += getEditBox("Number of Noble to Win",idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Reward for the Firts Noble", "", inSeccion, "86");
		if(idBox.equals("86")){
			MAIN_HTML += getEdititem("Reward for the Firts Noble",general.DESAFIO_NOBLE_PREMIO ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("ID NPC for Search", "", inSeccion, "87");
		if(idBox.equals("87")){
			MAIN_HTML += getEditListaNPC_VectorInteger("NPC For Search.",general.DESAFIO_NPC_BUSQUEDAS,inSeccion,"87");
		}

		MAIN_HTML += setFilaBoolean("Event Lv clan & reputation", general.EVENT_REPUTATION_CLAN, inSeccion, "534");

		MAIN_HTML += setFilaModif("Event Lv clan NPC ID", String.valueOf(general.EVENT_REPUTATION_CLAN_ID_NPC), inSeccion, "535");
		if(idBox.equals("535")){
			MAIN_HTML += getEditBox("Event Lv clan NPC ID",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Level to give", String.valueOf(general.EVENT_REPUTATION_LVL_TO_GIVE), inSeccion, "536");
		if(idBox.equals("536")){
			MAIN_HTML += getEditBox("Event Lv clan Level to give",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Reputation to give", String.valueOf(general.EVENT_REPUTATION_REPU_TO_GIVE), inSeccion, "537");
		if(idBox.equals("537")){
			MAIN_HTML += getEditBox("Event Lv clan Reputation to give",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Min Player", String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER), inSeccion, "538");
		if(idBox.equals("538")){
			MAIN_HTML += getEditBox("Event Lv clan Min Player",idBox,inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Event Lv Need all member's Onlines", general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE, inSeccion, "539");		
		
		return MAIN_HTML + getCerrarTabla();

	}

	private static String getConfigAugmentEspecial(L2PcInstance player, String idBox){
		String inSeccion = "augmentspe";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Select Augment Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Select Augment Passive Cost", "", inSeccion, "341");
		if(idBox.equals("341")){
			MAIN_HTML += getEdititem("Select Augment Passive Cost",general.AUGMENT_SPECIAL_PRICE_PASSIVE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Select Augment Chance Cost", "", inSeccion, "342");
		if(idBox.equals("342")){
			MAIN_HTML += getEdititem("Select Augment Chance Cost",general.AUGMENT_SPECIAL_PRICE_CHANCE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Select Augment Active Cost", "", inSeccion, "343");
		if(idBox.equals("343")){
			MAIN_HTML += getEdititem("Select Augment Active Cost",general.AUGMENT_SPECIAL_PRICE_ACTIVE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Augment Count to Display", String.valueOf(general.AUGMENT_SPECIAL_x_PAGINA), inSeccion, "108");
		if(idBox.equals("108")){
			MAIN_HTML +=getEditBox("Augment Count to Display (Recommends 25)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.AUGMENT_SPECIAL_LVL), inSeccion, "110");
		if(idBox.equals("110")){
			MAIN_HTML +=getEditBox(msg.CONFIG_MIN_LVL_CAN_USE, idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean(msg.CONFIG_ONLY_NOBLES, general.AUGMENT_SPECIAL_NOBLE, inSeccion, "109");


		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigPartyFinder(L2PcInstance player, String idBox){
		//partyfin
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Go Party Leader Config")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += setFilaModif("Go Party Leader Cost", "", "partyfin", "91");
		if(idBox.equals("91")){
			MAIN_HTML += getEdititem("Go Party Leader Cost",general.PARTY_FINDER_PRICE,"partyfin",idBox);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.PARTY_FINDER_CAN_USE_LVL), "partyfin", "97");
		if(idBox.equals("97")){
			MAIN_HTML +=getEditBox(msg.CONFIG_MIN_LVL_CAN_USE, "97", "partyfin");
		}

		
		
		MAIN_HTML += setFilaBoolean("Only for Noble Player's", general.PARTY_FINDER_CAN_USE_ONLY_NOBLE, "partyfin", "530");
		
		MAIN_HTML += setFilaBoolean("Go when Party Leader is Death", general.PARTY_FINDER_GO_LEADER_DEATH, "partyfin", "92");
		MAIN_HTML += setFilaBoolean("Go when Party Leader is Noble", general.PARTY_FINDER_GO_LEADER_NOBLE, "partyfin", "93");
		MAIN_HTML += setFilaBoolean("Go when Party Leader is Flag / PK", general.PARTY_FINDER_GO_LEADER_FLAGPK, "partyfin", "94");
		MAIN_HTML += setFilaBoolean("Go when Party Leader are in Instance", general.PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE, "partyfin", "322");
		MAIN_HTML += setFilaBoolean("Go when Party Leader is on Asedio Zone", general.PARTY_FINDER_GO_LEADER_ON_ASEDIO, "partyfin", "415");
		MAIN_HTML += setFilaBoolean("PK Player Can use it", general.PARTY_FINDER_CAN_USE_PK, "partyfin", "95");
		MAIN_HTML += setFilaBoolean("Flag Player Can use it", general.PARTY_FINDER_CAN_USE_FLAG, "partyfin", "96");
		MAIN_HTML += setFilaBoolean("Use Summon Rulez From Geo D.", general.PARTY_FINDER_USE_NO_SUMMON_RULEZ, "partyfin", "321");

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigFlagFinder(L2PcInstance player, String idBox){
		String inSeccion = "flagfin";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Flag Finder Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Flag finder Cost", "", inSeccion, "98");
		if(idBox.equals("98")){
			MAIN_HTML += getEdititem("Flag Finder Cost",general.FLAG_FINDER_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.FLAG_FINDER_LVL), inSeccion, "102");
		if(idBox.equals("102")){
			MAIN_HTML +=getEditBox(msg.CONFIG_MIN_LVL_CAN_USE, idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum Level of the Flag/PK Player to Sort", String.valueOf(general.FLAG_PVP_PK_LVL_MIN), inSeccion, "103");
		if(idBox.equals("103")){
			MAIN_HTML +=getEditBox("Minimum Level of the Flag/PK Player to Sort", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Minimum PvP of the Flag/PK Player to Sort", String.valueOf(general.FLAG_FINDER_MIN_PVP_FROM_TARGET), inSeccion, "510");
		if(idBox.equals("510")){
			MAIN_HTML +=getEditBox("Minimum PvP of the Flag/PK Player to Sort", idBox, inSeccion);
		}		
		
		
		
		MAIN_HTML += setFilaBoolean("Flag player can use It", general.FLAG_FINDER_CAN_USE_FLAG, inSeccion, "99");
		MAIN_HTML += setFilaBoolean("PK Player can use It", general.FLAG_FINDER_CAN_USE_PK, inSeccion, "100");
		MAIN_HTML += setFilaBoolean("Go flag only for Noble Player", general.FLAG_FINDER_CAN_NOBLE, inSeccion, "101");
		MAIN_HTML += setFilaBoolean("When the pk/pvp Player is inside the castle, can go for him?", general.FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE, inSeccion, "309");
		MAIN_HTML += setFilaBoolean("PK Player Priority?", general.FLAG_FINDER_PK_PRIORITY, inSeccion, "340");
		MAIN_HTML += setFilaBoolean("Check Clan", general.FLAG_FINDER_CHECK_CLAN, inSeccion, "552");
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getHTMLQuestion(){
		String html="<html><title>"+general.TITULO_NPC()+"</title><body>";
		String Pregunta ="<multiedit var=\"question\" width=210 >";
		String Respuesta = "<edit var=\"respuesta\" width=210>";

		String bypass = "bypass -h " + bypassNom + " setConfig1 Antibotasksave $respuesta 0 0 0 $question";
		String btnSave = "<button value=\"Save\" action=\""+bypass+"\" width=90 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		html += central.LineaDivisora(1) + central.headFormat("Add Antibot Question") + central.LineaDivisora(1);

		html += central.LineaDivisora(1) + central.headFormat("Question: " + Pregunta + "<br>") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat("Answer:<br1>The answer should not have blank spaces." + Respuesta + "<br>") + central.LineaDivisora(1);
		html += central.LineaDivisora(2) + central.headFormat(btnSave) + central.LineaDivisora(2);

		html += central.getPieHTML() + "</body></html>";
		return html;
	}

	private static String setAskAntiBot(String idPregunta, String Consulta, String Respuesta, int Pagina){
		//Antibotask
		String bypass = "bypass -h " + bypassNom + " setConfig1 Antibotask "+String.valueOf(Pagina)+" "+idPregunta+" 0 0 0";
		String btnBorrar = "<button value=\"supr\" action=\""+bypass+"\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String html ="";
		html = "<table width=280 border=0 bgcolor=151515><tr>";
		html += "<td width=240><font color=LEVEL>ANSWER: </font>"+Respuesta+"</td><td width=40>"+btnBorrar+"</td>";
		html += "</tr></table>";
		html += "<table width=280 border=0 bgcolor=151515><tr>";
		html += "<td width=280><font color=DF7401>QUESTION: </font>"+Consulta+"</td>";
		html += "</tr></table>"+central.LineaDivisora(2);
		return html;
	}

	private static String getConfigAntiBotAsk(L2PcInstance player, int pagina){
		int MaximoPagina = 6;
		int paso =1;
		int desdeContador = pagina * MaximoPagina;
		int Contador = 0;
		int ContadorIndi = 0;
		Boolean Continua=false;
		String btnBypassNext = "bypass -h " + bypassNom + " setConfig1 Antibotask "+String.valueOf(pagina + 1)+" 0 0 0 0";
		String btnBypassPreview = "bypass -h " + bypassNom + " setConfig1 Antibotask "+String.valueOf(pagina - 1)+" 0 0 0 0";
		String BtnNext = "<button value=\"NEXT\" action=\""+ btnBypassNext +"\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BtnPre = "<button value=\"PREV\" action=\""+ btnBypassPreview +"\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String addQuestionbtn = "<button value=\"Add Question\" action=\"bypass -h " + bypassNom + " setConfig1 Antibotask -20 0 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		//String LinkPregunta = "setConfig1 Antibotask 0 0 0 0 0";
		//String btnPreguntas = "<button value=\"Questions\" action=\"bypass -h ZeuSNPC "+LinkPregunta+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		//return getConfigAntiBotAsk(player, Integer.valueOf(eventParam1));



		//return getConfigAntiBotAsk(player, Integer.valueOf(eventParam1));
		String HTML ="<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		HTML += central.LineaDivisora(1) + central.headFormat("Antibot Question"+addQuestionbtn) + central.LineaDivisora(1);
		/*
		 * 		PREGUNTAS_BOT[Contador][0] = String.valueOf(rss.getInt("id"));
				PREGUNTAS_BOT[Contador][1] = rss.getString("ask");
				PREGUNTAS_BOT[Contador][2] = rss.getString("answer");
		 * */

		for(String[] pregunta: general.PREGUNTAS_BOT){
			if(pregunta[0]!=null){
				if(Contador >= desdeContador){
					if(ContadorIndi< MaximoPagina){
						HTML += setAskAntiBot(pregunta[0], pregunta[1], pregunta[2], pagina);
						ContadorIndi++;
					}else{
						Continua = true;
					}
				}
				Contador ++;
			}
		}

		String grillabtn = "<table width=260><tr>";
		if(pagina>0){
			grillabtn +="<td width=130 align=CENTER>"+BtnPre+"</td>";
		}else{
			grillabtn +="<td width=130 align=CENTER></td>";
		}

		if(Continua){
			grillabtn +="<td width=130 align=CENTER>"+BtnNext+"</td>";
		}else{
			grillabtn +="<td width=130 align=CENTER></td>";
		}

		grillabtn += "</tr></table>";

		HTML += central.LineaDivisora(1) + central.headFormat(grillabtn)+central.LineaDivisora(1);
		HTML += central.getPieHTML() + "</center></body></html>";
		return HTML;
	}
	
	
	
	private static String getConfigAntiBot2(L2PcInstance player, String idBox){
		String inSeccion = "Antibot2";
		
		String LinkConfig = "setConfig1 Antibot 0 0 0 0 0";
		String btnConfig = "<button value=\"Config 1\" action=\"bypass -h " + bypassNom + " "+LinkConfig+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Antibot Config" + btnConfig)+central.LineaDivisora(2) + central.LineaDivisora(1);
/*
		MAIN_HTML += setFilaModif("Total percentage of Item to Remove", String.valueOf(general.ANTIBOT_BORRAR_ITEM_PORCENTAJE), inSeccion, "316");
		if(idBox.equals("316")){
			MAIN_HTML +=getEditBox("Total percentage of Item to Remove", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Opportunities to make mistakes", String.valueOf(general.ANTIBOT_OPORTUNIDADES), inSeccion, "291");
		if(idBox.equals("291")){
			MAIN_HTML +=getEditBox("Opportunities for entering wrong answer", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Second to Resend Antibot check (Closed Verification) ", String.valueOf(general.ANTIBOT_SECONDS_TO_RESEND_ANTIBOT), inSeccion, "482");
		if(idBox.equals("482")){
			MAIN_HTML +=getEditBox("When a player closes the verification window by mistake, the system resends the verification in x seconds", idBox, inSeccion);
		}


		MAIN_HTML += setFilaModif("Minutes of the target inactive for not send antibot", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES), inSeccion, "380");
		if(idBox.equals("380")){
			MAIN_HTML +=getEditBox("Minutes of the target inactive for not send antibot", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum lifetime to use this command (Minutes).", String.valueOf(general.ANTIBOT_ANTIGUEDAD_MINUTOS), inSeccion, "299");
		if(idBox.equals("299")){
			MAIN_HTML +=getEditBox("Minimum lifetime to use this command (Minutes, use 0 for Disabled)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minutes in jail for mistake answer", String.valueOf(general.ANTIBOT_MINUTOS_JAIL), inSeccion, "292");
		if(idBox.equals("292")){
			MAIN_HTML +=getEditBox("Minutes in jail for mistake answer", idBox, inSeccion);
		}*/

		MAIN_HTML += setFilaModif("Number of deaths Mob to activate Antibot System", String.valueOf(general.ANTIBOT_MOB_DEAD_TO_ACTIVATE), inSeccion, "293");
		if(idBox.equals("293")){
			MAIN_HTML +=getEditBox("Number of deaths Mob to activate Antibot System", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minutes between user verification", String.valueOf(general.ANTIBOT_MINUTE_VERIF_AGAIN), inSeccion, "294");
		if(idBox.equals("294")){
			MAIN_HTML +=getEditBox("Minutes between user verification", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Timeout to wait for the answer", String.valueOf(general.ANTIBOT_MINUTOS_ESPERA), inSeccion, "295");
		if(idBox.equals("295")){
			MAIN_HTML +=getEditBox("Timeout to wait for the answer. When the time is up the player will be sent to jail", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum level of the player verifier", String.valueOf(general.ANTIBOT_MIN_LVL), inSeccion, "296");
		if(idBox.equals("296")){
			MAIN_HTML +=getEditBox("Minimum level of the player verifier. (0 = Disabled)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Id Item to Remove from Player", "" ,inSeccion,"317");
		if(idBox.equals("317")){
			opera.enviarHTML(player,getEditLista_VectorInteger("Id Item to Remove from Inventary player",passVectorFromArray_int(general.ANTIBOT_BORRAR_ITEM_ID),inSeccion,idBox,true));
			return "";
		}
		//'526','ANTIBOT_BLACK_LIST'
		MAIN_HTML += setFilaBoolean("Antibot Black List", general.ANTIBOT_BLACK_LIST, inSeccion, "526");

		MAIN_HTML += setFilaModif("Multiplier minutes for the Blacklisted", String.valueOf(general.ANTIBOT_BLACK_LIST_MULTIPLIER) ,inSeccion,"527");
		if(idBox.equals("527")){
			MAIN_HTML +=getEditBox("Multiplier minutes for the Blacklisted", idBox, inSeccion);
		}



		return MAIN_HTML + getCerrarTabla();
	}	
	
	
	
	
	
	

	private static String getConfigAntiBot(L2PcInstance player, String idBox){
		String inSeccion = "Antibot";
		String LinkPregunta = "setConfig1 Antibotask 0 0 0 0 0";
		String btnPreguntas = "<button value=\"Questions\" action=\"bypass -h " + bypassNom + " "+LinkPregunta+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		
		String LinkSecondConfig = "setConfig1 Antibot2 0 0 0 0 0";
		String btnSecondConfig = "<button value=\"Config 2\" action=\"bypass -h " + bypassNom + " "+LinkSecondConfig+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		

		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Antibot Config")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnPreguntas+btnSecondConfig) + central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Antibot command .checkbot activated", general.ANTIBOT_COMANDO_STATUS, inSeccion, "290");
		MAIN_HTML += setFilaBoolean("Reset death mob count when player re login", general.ANTIBOT_RESET_COUNT, inSeccion, "303");
		MAIN_HTML += setFilaBoolean("Automatic Antibot activated", general.ANTIBOT_AUTO, inSeccion, "302");
		MAIN_HTML += setFilaBoolean("Command .checkbot Only Noble", general.ANTIBOT_NOBLE_ONLY, inSeccion, "297");
		MAIN_HTML += setFilaBoolean("Command .checkbot Only Hero", general.ANTIBOT_HERO_ONLY, inSeccion, "298");
		MAIN_HTML += setFilaBoolean("Command .checkbot Only GM", general.ANTIBOT_GM_ONLY, inSeccion, "300");
		MAIN_HTML += setFilaBoolean("Annouce Jail Player", general.ANTIBOT_ANNOU_JAIL, inSeccion, "301");
		MAIN_HTML += setFilaBoolean("Send to jail all player on this IP", general.ANTIBOT_SEND_JAIL_ALL_DUAL_BOX, inSeccion, "505");
		MAIN_HTML += setFilaBoolean("Remove Item's from Inventory when player sent to jail", general.ANTIBOT_BORRAR_ITEM, inSeccion, "315");
		MAIN_HTML += setFilaBoolean("Player can send checkbot in peace zone", general.ANTIBOT_CHECK_INPEACE_ZONE, inSeccion, "318");
		MAIN_HTML += setFilaBoolean("Send all player on IP the annoucement", general.ANTIBOT_SEND_ALL_IP, inSeccion, "404");
		MAIN_HTML += setFilaBoolean("Now send antibo check on dualbox.", general.ANTIBOT_CHECK_DUALBOX, inSeccion, "483");
		

		MAIN_HTML += setFilaModif("Total percentage of Item to Remove", String.valueOf(general.ANTIBOT_BORRAR_ITEM_PORCENTAJE), inSeccion, "316");
		if(idBox.equals("316")){
			MAIN_HTML +=getEditBox("Total percentage of Item to Remove", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Opportunities to make mistakes", String.valueOf(general.ANTIBOT_OPORTUNIDADES), inSeccion, "291");
		if(idBox.equals("291")){
			MAIN_HTML +=getEditBox("Opportunities for entering wrong answer", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Second to Resend Antibot check (Closed Verification) ", String.valueOf(general.ANTIBOT_SECONDS_TO_RESEND_ANTIBOT), inSeccion, "482");
		if(idBox.equals("482")){
			MAIN_HTML +=getEditBox("When a player closes the verification window by mistake, the system resends the verification in x seconds", idBox, inSeccion);
		}


		MAIN_HTML += setFilaModif("Minutes of the target inactive for not send antibot", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES), inSeccion, "380");
		if(idBox.equals("380")){
			MAIN_HTML +=getEditBox("Minutes of the target inactive for not send antibot", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum lifetime to use this command (Minutes).", String.valueOf(general.ANTIBOT_ANTIGUEDAD_MINUTOS), inSeccion, "299");
		if(idBox.equals("299")){
			MAIN_HTML +=getEditBox("Minimum lifetime to use this command (Minutes, use 0 for Disabled)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minutes in jail for mistake answer", String.valueOf(general.ANTIBOT_MINUTOS_JAIL), inSeccion, "292");
		if(idBox.equals("292")){
			MAIN_HTML +=getEditBox("Minutes in jail for mistake answer", idBox, inSeccion);
		}

		/*
		
		MAIN_HTML += setFilaModif("Number of deaths Mob to activate Antibot System", String.valueOf(general.ANTIBOT_MOB_DEAD_TO_ACTIVATE), inSeccion, "293");
		if(idBox.equals("293")){
			MAIN_HTML +=getEditBox("Number of deaths Mob to activate Antibot System", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minutes between user verification", String.valueOf(general.ANTIBOT_MINUTE_VERIF_AGAIN), inSeccion, "294");
		if(idBox.equals("294")){
			MAIN_HTML +=getEditBox("Minutes between user verification", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Timeout to wait for the answer", String.valueOf(general.ANTIBOT_MINUTOS_ESPERA), inSeccion, "295");
		if(idBox.equals("295")){
			MAIN_HTML +=getEditBox("Timeout to wait for the answer. When the time is up the player will be sent to jail", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum level of the player verifier", String.valueOf(general.ANTIBOT_MIN_LVL), inSeccion, "296");
		if(idBox.equals("296")){
			MAIN_HTML +=getEditBox("Minimum level of the player verifier. (0 = Disabled)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Id Item to Remove from Player", "" ,inSeccion,"317");
		if(idBox.equals("317")){
			opera.enviarHTML(player,getEditLista_VectorInteger("Id Item to Remove from Inventary player",passVectorFromArray_int(general.ANTIBOT_BORRAR_ITEM_ID),inSeccion,idBox,true));
			return "";
		}*/


		return MAIN_HTML + getCerrarTabla();
	}


	private static String getConfigElemental(L2PcInstance player, String idBox){
		String inSeccion = "elementalspe";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Select Element Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Select Element Cost", "", inSeccion, "130");
		if(idBox.equals("130")){
			MAIN_HTML += getEdititem("Select Element Cost",general.ELEMENTAL_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean(msg.CONFIG_ONLY_NOBLES, general.ELEMENTAL_NOBLE, inSeccion, "131");

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.ELEMENTAL_LVL), inSeccion, "132");
		if(idBox.equals("132")){
			MAIN_HTML +=getEditBox(msg.CONFIG_MIN_LVL_CAN_USE, idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Elemental Max Power to Weapon", String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_WEAPON), inSeccion, "405");
		if(idBox.equals("405")){
			MAIN_HTML +=getEditBox("Elemental Max Power to Weapon", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Elemental Max Power to Armor", String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_ARMOR), inSeccion, "406");
		if(idBox.equals("406")){
			MAIN_HTML +=getEditBox("Elemental Max Power to Armor", idBox, inSeccion);
		}

		return MAIN_HTML + getCerrarTabla();
	}
/*
	private static String getConfigAugment(L2PcInstance player, String idBox){
		String inSeccion = "flagfin";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Configuración Special Augment")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Precio para Usar el Special Augment", "", inSeccion, "107");
		if(idBox.equals("107")){
			MAIN_HTML += getEdititem("Precio para Usar el Special Augemt",general.AUGMENT_SPECIAL_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Augment por Página", String.valueOf(general.AUGMENT_SPECIAL_x_PAGINA), inSeccion, "107");
		if(idBox.equals("107")){
			MAIN_HTML +=getEditBox("Ingrese la Cantidad de Augment mostrados por Hoja, no se recomienda usar más de 25", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Level del Player para ser usado", String.valueOf(general.AUGMENT_SPECIAL_x_PAGINA), inSeccion, "110");
		if(idBox.equals("110")){
			MAIN_HTML +=getEditBox("Desde el level señalado aquí los Player pueden usarlo", idBox, inSeccion);
		}

		if(idBox.equals("103")){
			MAIN_HTML +=getEditBox("Desde el level ingresado aqui serán sorteados para ir donde ellos.", idBox, inSeccion);
		}
		MAIN_HTML += setFilaBoolean("Solo Nobles pueden usarlo", general.AUGMENT_SPECIAL_NOBLE, inSeccion, "109");

		return MAIN_HTML + getCerrarTabla();
	}
*/
	private static String getPageDisponiblesCommu(){
		
		String paraCombo = "_bbshome;_bbsgetfav;_bbslink;_bbsloc;_bbsclan;_bbsmemo;_bbsmail;_bbsfriends";
		String retorno = "";

		for(String parte : paraCombo.split(";")){
			if(!parte.equals(general.COMMUNITY_BOARD_PARTYFINDER_EXEC) && !parte.equals(general.COMMUNITY_BOARD_GRAND_RB_EXEC) && !parte.equals(general.COMMUNITY_BOARD_PART_EXEC) && !parte.equals(general.COMMUNITY_BOARD_REGION_PART_EXEC) && !parte.equals(general.COMMUNITY_BOARD_ENGINE_PART_EXEC) && !parte.equals(general.COMMUNITY_BOARD_CLAN_PART_EXEC)){
				if(retorno.length()>0){
					retorno += ";";
				}
				retorno += parte;
			}
		}

		return retorno;
	}


	private static String getConfigOverEnchant(L2PcInstance player,String idBox){

		String paraCombo = "";

		for(String cmbStr :  general._ANTI_OVER_TYPE_PUNISH){
			if(paraCombo.length()>1){
				paraCombo += ";";
			}
			paraCombo += cmbStr;
		}

		String inSeccion = "overenchant";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("ZeuS Engine Over Enchant Protection Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Over Enchant Protection", general.ANTI_OVER_ENCHANT_ACT, inSeccion, "456");
		MAIN_HTML += setFilaBoolean("Over Enchant Check before Attack", general.ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK, inSeccion, "457");
		MAIN_HTML += setFilaBoolean("Over Enchant Annoucement", general.ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL, inSeccion, "461");
		MAIN_HTML += setFilaBoolean("Over Enchant Punish All Ip's", general.ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP, inSeccion, "462");

		MAIN_HTML += setFilaModif("Over Enchant Type of Punishment: ", general.ANTI_OVER_TYPE_PUNISH , inSeccion, "460");
		if(idBox.equals("460")){
			MAIN_HTML += getEditComboBox("Type: " + general.ANTI_OVER_TYPE_PUNISH ,idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaModif("Over Enchant Second Between Attack Check", String.valueOf(general.ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK), inSeccion, "458");
		if(idBox.equals("458")){
			MAIN_HTML +=getEditBox("Second Between Attack Check", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Over Enchant Max. Weapon Enchant", String.valueOf(general.ANTI_OVER_ENCHANT_MAX_WEAPON), inSeccion, "463");
		if(idBox.equals("463")){
			MAIN_HTML +=getEditBox("Max. Weapon Enchant", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Over Enchant Max. Armor Enchant", String.valueOf(general.ANTI_OVER_ENCHANT_MAX_ARMOR), inSeccion, "464");
		if(idBox.equals("464")){
			MAIN_HTML +=getEditBox("Max. Armor Enchant", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Over Enchant Annoucement:", String.valueOf(general.ANTI_OVER_ENCHANT_MESJ_PUNISH),inSeccion,"459");
		if(idBox.equals("459")){
			//%TYPE_PUNISHMENT%. Nombre PJ %CHARNAME%
			MAIN_HTML+= getEditBigBox("Over Enchant Punishment Annoucement<br1> Use: %CHARNAME% Player Name<br1>Use: %TYPE_PUNISHMENT% for Punishment Type", idBox, inSeccion);
		}

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigComunidadPartyFinder(L2PcInstance player,String idBox){
		String inSeccion = "cbpartyfinder";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("ZeuS Engine Community Board Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean(".party command enabled", general.COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE , inSeccion, "593");
		
		MAIN_HTML += setFilaModif("Seconds Between player party message", String.valueOf(general.COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE), inSeccion, "590");
		if(idBox.equals("590")){
			MAIN_HTML +=getEditBox("Seconds Between player party message", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Seconds duration of the Party Request", String.valueOf(general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST), inSeccion, "591");
		if(idBox.equals("591")){
			MAIN_HTML +=getEditBox("Seconds duration of the Party Request", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Sec. duration of the Party Request on Board", String.valueOf(general.COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD), inSeccion, "592");
		if(idBox.equals("592")){
			MAIN_HTML +=getEditBox("Sec. duration of the Party Request on Board", idBox, inSeccion);
		}
		return MAIN_HTML + getCerrarTabla();
	}




	private static String getConfigComunidad(L2PcInstance player,String idBox){

		String paraCombo = getPageDisponiblesCommu();

		String inSeccion = "comunidad";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("ZeuS Engine Community Board Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("ZeuS Community Board", general.COMMUNITY_BOARD, inSeccion, "409");
		MAIN_HTML += setFilaModif("Community Page: ", general.COMMUNITY_BOARD_PART_EXEC, inSeccion, "410");
		if(idBox.equals("410")){
			MAIN_HTML += getEditComboBox("Community Page: " + general.COMMUNITY_BOARD_PART_EXEC,idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Region Board", general.COMMUNITY_BOARD_REGION, inSeccion, "411");
		MAIN_HTML += setFilaModif("Region Page: ", general.COMMUNITY_BOARD_REGION_PART_EXEC, inSeccion, "412");
		if(idBox.equals("412")){
			MAIN_HTML += getEditComboBox("Region Page: " + general.COMMUNITY_BOARD_REGION_PART_EXEC,idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Engine Board", general.COMMUNITY_BOARD_ENGINE, inSeccion, "413");
		MAIN_HTML += setFilaModif("Engine Page: ", general.COMMUNITY_BOARD_ENGINE_PART_EXEC, inSeccion, "414");
		if(idBox.equals("414")){
			MAIN_HTML += getEditComboBox("Engine Page: " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC,idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Clan Board", general.COMMUNITY_BOARD_CLAN, inSeccion, "470");
		MAIN_HTML += setFilaModif("Clan Page: ", general.COMMUNITY_BOARD_CLAN_PART_EXEC, inSeccion, "469");
		if(idBox.equals("469")){
			MAIN_HTML += getEditComboBox("Clan Page: " + general.COMMUNITY_BOARD_CLAN_PART_EXEC,idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Grand Raid Boss Board", general.COMMUNITY_BOARD_GRAND_RB, inSeccion, "569");
		MAIN_HTML += setFilaModif("Grand Raid Boss Page: ", general.COMMUNITY_BOARD_GRAND_RB_EXEC, inSeccion, "570");
		if(idBox.equals("570")){
			MAIN_HTML += getEditComboBox("Grand Raid Boss Page: " + general.COMMUNITY_BOARD_GRAND_RB_EXEC,idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Find party Board", general.COMMUNITY_BOARD_PARTYFINDER, inSeccion, "588");
		MAIN_HTML += setFilaModif("Find Party Page: ", general.COMMUNITY_BOARD_PARTYFINDER_EXEC, inSeccion, "589");
		if(idBox.equals("589")){
			MAIN_HTML += getEditComboBox("Find Party Page: " + general.COMMUNITY_BOARD_PARTYFINDER_EXEC,idBox,inSeccion,paraCombo);
		}
		
		MAIN_HTML += setFilaModif("Rows for Page", String.valueOf(general.COMMUNITY_BOARD_ROWS_FOR_PAGE), inSeccion, "416");
		if(idBox.equals("416")){
			MAIN_HTML +=getEditBox("Rows for Page", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Top Player List Count", String.valueOf(general.COMMUNITY_BOARD_TOPPLAYER_LIST), inSeccion, "417");
		if(idBox.equals("417")){
			MAIN_HTML +=getEditBox("Top Player List Count", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Clan List Count", String.valueOf(general.COMMUNITY_BOARD_CLAN_LIST), inSeccion, "418");
		if(idBox.equals("418")){
			MAIN_HTML +=getEditBox("Clan List Count", idBox, inSeccion);
		}

		/*
		MAIN_HTML += setFilaModif("Private Store List Count", String.valueOf(general.COMMUNITY_BOARD_MERCHANT_LIST), inSeccion, "419");
		if(idBox.equals("419")){
			MAIN_HTML +=getEditBox("Private Store List Count", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Clan List Post Count", String.valueOf(general.COMMUNITY_BOARD_CLAN_ROWN_LIST), inSeccion, "471");
		if(idBox.equals("471")){
			MAIN_HTML +=getEditBox("Private Store List Count", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Region Player List", String.valueOf(general.COMMUNITY_BOARD_REGION_PLAYER_ON_LIST), inSeccion, "481");
		if(idBox.equals("481")){
			MAIN_HTML +=getEditBox("Region Player List", idBox, inSeccion);
		}
		MAIN_HTML += setFilaBoolean("Private Store Use teleport to player", general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT, inSeccion, "472");
		MAIN_HTML += setFilaBoolean("Private Store Use teleport Only on Peace Zone", general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE, inSeccion, "475");
		MAIN_HTML += setFilaBoolean("Private Store Use teleport on siege", general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE, inSeccion, "473");
		MAIN_HTML += setFilaBoolean("Private Store Use teleport Inside Castle", general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE, inSeccion, "474");
		MAIN_HTML += setFilaBoolean("Private Store Use teleport Inside to clanhall", general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN, inSeccion, "476");
		 */
		/*MAIN_HTML += setFilaModif("Región Community Region", "", inSeccion, "486");
		if(idBox.equals("486")){
			opera.enviarHTML(player,getEditLista_VectorInteger("Región Community Region",general.EVENT_RAIDBOSS_RAID_ID,inSeccion,idBox,true));
			//opera.enviarHTML(player,getEditLista_VectorInteger("Región Community Region",general.COMMUNITY_REGION_LEGEND ,inSeccion,idBox,true));
			return "";
			//MAIN_HTML += getEditListaNPC_VectorInteger("NPC For Search.",general.EVENT_RAIDBOSS_RAID_ID ,inSeccion,idBox);
		}*/

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigEnchant(L2PcInstance player, String idBox){
		String inSeccion = "enchantspe";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Special Enchant Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Special Enchant Cost", "", inSeccion, "111");
		if(idBox.equals("111")){
			MAIN_HTML += getEdititem("Special Enchant Cost",general.ENCHANT_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean(msg.CONFIG_ONLY_NOBLES, general.ENCHANT_NOBLE, inSeccion, "112");

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.ENCHANT_LVL), inSeccion, "113");
		if(idBox.equals("113")){
			MAIN_HTML +=getEditBox(msg.CONFIG_MIN_LVL_CAN_USE, idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum Enchant Item for Enchanted", String.valueOf(general.ENCHANT_MIN_ENCHANT), inSeccion, "114");
		if(idBox.equals("114")){
			MAIN_HTML +=getEditBox("Minimum Enchant Item for Enchanted", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Maximum Enchant for Enchanted", String.valueOf(general.ENCHANT_MAX_ENCHANT), inSeccion, "115");
		if(idBox.equals("115")){
			MAIN_HTML +=getEditBox("Maximum Enchant for Enchanted", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Variable Summing between Enchant.", String.valueOf(general.ENCHANT_x_VEZ), inSeccion, "116");
		if(idBox.equals("116")){
			MAIN_HTML +=getEditBox("Variable Summing between Enchant", idBox, inSeccion);
		}

		return MAIN_HTML + getCerrarTabla();
	}

	/*private static String getConfigDonacion(L2PcInstance player,String idBox){
		String inSeccion = "donacion";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Donation Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("ID Donation Coin", String.valueOf(general.DONA_ID_ITEM) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM), inSeccion, "76");
		if(idBox.equals("76")){
			MAIN_HTML +=getEditBox("ID Donation Coin", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Lv 85 Cost", String.valueOf(general.DONATION_LV_85_COST), inSeccion, "572");
		if(idBox.equals("572")){
			MAIN_HTML +=getEditBox("Lv 85 Cost", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Noble Cost", String.valueOf(general.DONATION_NOBLE_COST), inSeccion, "573");
		if(idBox.equals("573")){
			MAIN_HTML +=getEditBox("Noble Cost", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Fame Cost", String.valueOf(general.DONATION_FAME_COST), inSeccion, "574");
		if(idBox.equals("574")){
			MAIN_HTML +=getEditBox("Fame Cost", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Fame Amount to Give", String.valueOf(general.DONATION_FAME_AMOUNT), inSeccion, "575");
		if(idBox.equals("575")){
			MAIN_HTML +=getEditBox("Fame Amount to Give", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Clan Lv Cost", String.valueOf(general.DONATION_CLAN_LV_COST), inSeccion, "576");
		if(idBox.equals("576")){
			MAIN_HTML +=getEditBox("Clan Lv Cost", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Clan Lv To Give", String.valueOf(general.DONATION_CLAN_LV_LV), inSeccion, "577");
		if(idBox.equals("577")){
			MAIN_HTML +=getEditBox("Clan Lv To Give", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Reduce PK Count Cost", String.valueOf(general.DONATION_REDUCE_PK_COST), inSeccion, "578");
		if(idBox.equals("578")){
			MAIN_HTML +=getEditBox("Reduce PK Count Cost", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Change Sex Cost", String.valueOf(general.DONATION_CHANGE_SEX_COST), inSeccion, "579");
		if(idBox.equals("579")){
			MAIN_HTML +=getEditBox("Change Sex Cost", idBox, inSeccion);
		}
		

		MAIN_HTML += setFilaModif("AIO Buffer Cost (Simple)", String.valueOf(general.DONATION_AIO_CHAR_SIMPLE_COSTO), inSeccion, "580");
		if(idBox.equals("580")){
			MAIN_HTML +=getEditBox("AIO Buffer Cost (Simple)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("AIO Buffer Cost (Buff Enchant)", String.valueOf(general.DONATION_AIO_CHAR_30_COSTO), inSeccion, "581");
		if(idBox.equals("581")){
			MAIN_HTML +=getEditBox("AIO Buffer Cost (Buff Enchant)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("AIO Buffer Lv Request", String.valueOf(general.DONATION_AIO_CHAR_LV_REQUEST), inSeccion, "582");
		if(idBox.equals("582")){
			MAIN_HTML +=getEditBox("AIO Buffer Lv Request", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Change Name Cost", String.valueOf(general.DONATION_CHANGE_CHAR_NAME_COST), inSeccion, "583");
		if(idBox.equals("583")){
			MAIN_HTML +=getEditBox("Change Name Cost", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Change Clan Name Cost", String.valueOf(general.DONATION_CHANGE_CLAN_NAME_COST), inSeccion, "584");
		if(idBox.equals("584")){
			MAIN_HTML +=getEditBox("Change Clan Name Cost", idBox, inSeccion);
		}


		return MAIN_HTML + getCerrarTabla();
	}*/



	private static String getConfigDropSearch(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Drop Search Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Drop Search Direct Teleport for Free", general.DROP_SEARCH_TELEPORT_FOR_FREE , "dropsear", "84");
		
		MAIN_HTML += setFilaBoolean("Drop Search Observe Mode", general.DROP_SEARCH_OBSERVE_MODE, "dropsear", "596");
		
		MAIN_HTML += setFilaBoolean("Drop Search Can use Teleport to Mob", general.DROP_SEARCH_CAN_USE_TELEPORT , "dropsear", "352");

		MAIN_HTML += setFilaModif("Drop Search Direct Teleport Cost", "", "dropsear", "89");
		if(idBox.equals("89")){
			MAIN_HTML += getEdititem("Drop Search Direct Teleport Cost",general.DROP_TELEPORT_COST,"dropsear",idBox);
		}

		MAIN_HTML += setFilaModif("Drop Search Count to Display", String.valueOf(general.DROP_SEARCH_MOSTRAR_LISTA), "dropsear", "90");
		if(idBox.equals("90")){
			MAIN_HTML +=getEditBox("Count to Display (recommended 25)", "90", "dropsear");
		}

		MAIN_HTML += setFilaBoolean("Drop Search Show ID Item to Players", general.DROP_SEARCH_SHOW_IDITEM_TO_PLAYER , "dropsear", "338");

		MAIN_HTML += setFilaModif("Monster Blacklist (List of mobs that can not be reached from Dropsearch teleport)", "", "dropsear", "350");
		if(idBox.equals("350")){
			MAIN_HTML += getEditListaNPC_VectorInteger("Mob Blacklist",general.DROP_SEARCH_MOB_BLOCK_TELEPORT ,"dropsear","350");
		}

		return MAIN_HTML + getCerrarTabla();

	}

	private static String getConfigTeleport(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Teleport Config")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Use ZeuS Teleport (From BD)", general.TELEPORT_BD, "teleport", "287");
		MAIN_HTML += central.headFormat("<font color=>If you gonna use htm file, need to create a folder into teleport width the name AIONpc-GK. The index file need to called teleport.htm</font>","WHITE");
		MAIN_HTML += setFilaModif("Teleport Cost", "", "teleport", "83");
		if(idBox.equals("83")){
			MAIN_HTML += getEdititem("Teleport Cost",general.TELEPORT_PRICE,"teleport",idBox);
		}
		MAIN_HTML += setFilaBoolean("Teleport for Free", general.FREE_TELEPORT, "teleport", "84");

		MAIN_HTML += setFilaBoolean("Teleport for Free up to Level", general.TELEPORT_FOR_FREE_UP_TO_LEVEL, "teleport", "531");
		
		MAIN_HTML += setFilaModif("Free up to Level", String.valueOf(general.TELEPORT_FOR_FREE_UP_TO_LEVEL_LV),"teleport","532");
		if(idBox.equals("532")){
			MAIN_HTML+= getEditBox("Free up to Level", idBox, "teleport");
		}
		
		MAIN_HTML += setFilaBoolean("Teleport Can be used in combat mode", general.TELEPORT_CAN_USE_IN_COMBAT_MODE, "teleport", "360");
		return MAIN_HTML + getCerrarTabla();
	}

	/*
	 * 	private static String getEditBigBox(String titulo, String idINBD, String Seccion){
			String temp = central.headFormat(titulo,"LEVEL");
			temp += "<center><table width=200><tr><td width=145 align=LEFT><multiedit var=\"valor\" width=110 ></td><td align=center width=55><button value=\"OK\" action=\"bypass -h ZeuSNPC setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\" width=50 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></center><br>";
			return central.headFormat(temp);
		}
	 * */

	private static String getConfigPVP(L2PcInstance player, String idBox){
		String inSeccion  = "pvpConfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("PvP / PK Config")+central.LineaDivisora(2) + central.LineaDivisora(1);


		MAIN_HTML += setFilaBoolean("PVP/PK Log", general.LOG_FIGHT_PVP_PK, inSeccion, "173");

		MAIN_HTML += setFilaBoolean("BSOE can use in PVP", general.ALLOW_BLESSED_ESCAPE_PVP, inSeccion, "176");
		
		MAIN_HTML += setFilaBoolean("BSOE can use in PK Mode", general.CAN_USE_BSOE_PK , inSeccion, "509");

		MAIN_HTML += setFilaBoolean("ToP PvP / PK Graphic Effects", general.PVP_PK_GRAFICAL_EFFECT, inSeccion, "177");

		MAIN_HTML += setFilaBoolean("Top PvP / PK Login Annoucement", general.ANNOUCE_TOP_PPVPPK_ENTER, inSeccion, "245");

		MAIN_HTML += setFilaBoolean("PvP Reward", general.PVP_REWARD, inSeccion, "384");
		
		MAIN_HTML += setFilaBoolean("PvP Reward check dualbox", general.PVP_REWARD_CHECK_DUALBOX, inSeccion, "520");
		
		MAIN_HTML += setFilaBoolean("Party PvP Reward", general.PVP_PARTY_REWARD, inSeccion, "385");

		MAIN_HTML += setFilaModif("Top PvP / PK Login Message", String.valueOf(general.MENSAJE_ENTRADA_PJ_TOPPVPPK),inSeccion,"246");
		if(idBox.equals("246")){
			MAIN_HTML+= getEditBigBox("Top PvP / PK Login Message<br1> Use: %CHAR_NAME% Player Name", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Annouce Karma Player Login", general.ANNOUCE_PJ_KARMA, inSeccion, "247");

		MAIN_HTML += setFilaModif("Karma Amount to Announce", String.valueOf(general.ANNOUCE_PJ_KARMA_CANTIDAD),inSeccion,"249");
		if(idBox.equals("249")){
			MAIN_HTML+= getEditBox("karma Amount to Announce", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Karma Player Login Message", String.valueOf(general.MENSAJE_ENTRADA_PJ_KARMA),inSeccion,"248");
		if(idBox.equals("248")){
			MAIN_HTML+= getEditBigBox("Karma Player Login Message<br1> Use: %CHAR_NAME% Player Name<br1>%KARMA% for Karma", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Annouce Player When kill and get Karma", general.ANNOUNCE_KARMA_PLAYER_WHEN_KILL , inSeccion, "319");
		MAIN_HTML += setFilaModif("Message when PLayer get Karma", String.valueOf(general.ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN ),inSeccion,"320");


		if(idBox.equals("248")){
			MAIN_HTML+= getEditBigBox("Karma Player Login Message<br1> Use: %CHAR_NAME% Player Name<br1>%KARMA% for Karma", idBox, inSeccion);
		}


		MAIN_HTML += setFilaModif("PK Protection (level difference)", String.valueOf(general.PVP_PK_PROTECTION_LVL),inSeccion,"175");
		if(idBox.equals("175")){
			MAIN_HTML+= getEditBox("PK Protection (level difference). 0 = Disabled", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("PK Protection Lifetime (Minutes)", String.valueOf(general.PVP_PK_PROTECTION_LIFETIME_MINUTES),inSeccion,"519");
		if(idBox.equals("519")){
			MAIN_HTML+= getEditBox("PK Protection Lifetime (Minutes). 0 = Disabled", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("PVP/PK Player List", String.valueOf(general.MAX_LISTA_PVP), inSeccion, "3");
		if(idBox.equals("3")){
			MAIN_HTML += getEditBigBox("Input value to Player Display", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("PVP/PK Log Player List", String.valueOf(general.MAX_LISTA_PVP_LOG), inSeccion, "4");
		if(idBox.equals("4")){
			MAIN_HTML += getEditBigBox("Input value to Player Display", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Reward for PvP", "", inSeccion, "386");
		if(idBox.equals("386")){
			opera.enviarHTML(player,getEdititem("Reward for PvP", general.PVP_REWARD_ITEMS , inSeccion, idBox,true));
			return "";
		}

		MAIN_HTML += setFilaModif("Reward for Party PvP", "", inSeccion, "387");
		if(idBox.equals("387")){
			opera.enviarHTML(player,getEdititem("Reward for Party PvP", general.PVP_PARTY_REWARD_ITEMS , inSeccion, idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("PvP Reward party Radius", String.valueOf(general.PVP_REWARD_RANGE), inSeccion, "529");
		if(idBox.equals("529")){
			MAIN_HTML += getEditBigBox("PvP Reward party Radius", idBox, inSeccion);
		}
		
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getArrayIntToString(int[] ArrayIN){
		String Retorno ="";

		for(int Access : ArrayIN) {
			if(Retorno.length()>0){
				Retorno += ", ";
			}
			Retorno += String.valueOf(Access);
		}

		return Retorno;
	}

	private static String getConfigNPCZeuS(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("ZeuS NPC AIO")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += setFilaModif("ID NPC ZeuS", String.valueOf(general.ID_NPC),"general","1");
		if(idBox.equals("1")){
			MAIN_HTML+= getEditBox("ID NPC Central NPC ZeuS AIO", "1","general");
		}
		MAIN_HTML += setFilaModif("ID NPC ZeuS Clan H.", String.valueOf(general.ID_NPC_CH),"general","2");
		if(idBox.equals("2")){
			MAIN_HTML+= getEditBox("ID Clan Hall NPC ZeuS AIO", "2","general");
		}

		MAIN_HTML += setFilaModif("ID NPC ZeuS Coliseum Event", String.valueOf(general.EVENT_COLISEUM_NPC_ID),"general","308");
		if(idBox.equals("308")){
			MAIN_HTML+= getEditBox("ID NPC ZeuS Coliseum Event", idBox,"general");
		}

		MAIN_HTML += setFilaModif("NPC's ZeuS Radius", String.valueOf(general.RADIO_PLAYER_NPC_MAXIMO),"general","382");
		if(idBox.equals("382")){
			MAIN_HTML+= getEditBox("NPC's ZeuS Radius", idBox,"general");
		}

		MAIN_HTML += setFilaModif("ID Access Level", getArrayIntToString(general.get_AccessConfig()), "general", "281");
		if(idBox.equals("281")){
			MAIN_HTML += getEditLista_VectorInteger("Input Access Level (Default: 127 (Freya), 8 (H5)", passVectorFromArray_int(general.get_AccessConfig()), "general", "281");
		}

		MAIN_HTML += setFilaBoolean("Commands .expon & .expoff", general.RATE_EXP_OFF , "general", "172");

		MAIN_HTML += setFilaBoolean("Show my Stat", general.SHOW_MY_STAT, "general", "251");

		MAIN_HTML += setFilaBoolean("Use ZeuS Shop (From BD)",general.SHOP_USE_BD,"general","289");
		MAIN_HTML += central.headFormat("<font color=WHITE>If you gonna use htm file, need to create a folder into merchant width the name AIONPC. The index file need to called 955.htm</font>","WHITE");

		MAIN_HTML += setFilaModif("Enchant Annoucement", "" ,"general","174");
		if(idBox.equals("174")){
			MAIN_HTML += getEditLista_VectorInteger("Ingrese los Enchant a Anunciar",passVectorFromArray_int(general.ENCHANT_ANNOUCEMENT),"general","174");
		}

		MAIN_HTML += setFilaModif("Server Name: ", general.Server_Name,"general","288");
		if(idBox.equals("288")){
			MAIN_HTML+= getEditBigBox("Server Name (Max. 16 characters)", idBox, "general");
		}

		MAIN_HTML += setFilaBoolean("Can trade While are Flag", general.TRADE_WHILE_FLAG , "general", "358");
		MAIN_HTML += setFilaBoolean("Can trade While are PK", general.TRADE_WHILE_PK , "general", "359");

		MAIN_HTML += setFilaBoolean("Show ZeuS new main Windows", general.SHOW_NEW_MAIN_WINDOWS , "general", "339");

		MAIN_HTML += setFilaBoolean("Show ZeuS info when player enter game", general.SHOW_ZEUS_ENTER_GAME , "general", "383");

		MAIN_HTML += setFilaBoolean("User char panel voice command(.charpanel)", general.CHAR_PANEL , "general", "407");

		MAIN_HTML += setFilaBoolean("Fix Char Windows (.fixme)", general.SHOW_FIXME_WINDOWS , "general", "408");

		MAIN_HTML += setFilaModif("Second's between skill enchanting", String.valueOf(general.ANTIFEED_ENCHANT_SKILL_REUSE),"general","506");
		if(idBox.equals("506")){
			MAIN_HTML+= getEditBox("Second's between skill enchanting (0=disabled)", "506","general");
		}

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	private static Vector<Integer> passVectorFromArray_int(String CadenaPasar){
		Vector<Integer> VectorRetorno = new Vector<Integer>();
		for(String _Paso : CadenaPasar.split(",")){
			VectorRetorno.add(Integer.valueOf(_Paso));
		}
		return VectorRetorno;
	}

	private static Vector<Integer> passVectorFromArray_int(int[] VectorPasar){
		Vector<Integer> VectorRetorno = new Vector<Integer>();
		for(int _Paso : VectorPasar){
			VectorRetorno.add(_Paso);
		}
		return VectorRetorno;
	}

	private static String getConfigBuffer_valores(L2PcInstance player, String idBox){

		String inSeccion = "bufferPrecios";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Buffer Cost")+central.LineaDivisora(2) + central.LineaDivisora(1);

		String idPrecio = "148";
		String Precio = String.valueOf(general.BUFFER_IMPROVED_VALOR);
		String Tipo_Buff = "Improved";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "150";
		Precio = String.valueOf(general.BUFFER_BUFF_VALOR);
		Tipo_Buff = "General Buff";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "152";
		Precio = String.valueOf(general.BUFFER_CHANT_VALOR);
		Tipo_Buff = "Chant";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "154";
		Precio = String.valueOf(general.BUFFER_DANCE_VALOR);
		Tipo_Buff = "Dances";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "156";
		Precio = String.valueOf(general.BUFFER_SONG_VALOR);
		Tipo_Buff = "Song";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "158";
		Precio = String.valueOf(general.BUFFER_RESIST_VALOR);
		Tipo_Buff = "Resistencia";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "160";
		Precio = String.valueOf(general.BUFFER_CUBIC_VALOR);
		Tipo_Buff = "Cubic";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "162";
		Precio = String.valueOf(general.BUFFER_PROHECY_VALOR);
		Tipo_Buff = "Profecy";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "164";
		Precio = String.valueOf(general.BUFFER_SPECIAL_VALOR);
		Tipo_Buff = "Special";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		idPrecio = "166";
		Precio = String.valueOf(general.BUFFER_OTROS_VALOR);
		Tipo_Buff = "otros";

		MAIN_HTML += setFilaModif(Tipo_Buff + " Cost", Precio , inSeccion, idPrecio);
		if(idBox.equals(idPrecio)){
			MAIN_HTML += getEditBox(Tipo_Buff + " Cost:", idBox, inSeccion);
		}

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;

	}

	private static String getConfigCastleManager(L2PcInstance player, String idBox){
		String inSeccion = "castlema";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Castle Manager Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Show Giran Manager",general.GIRAN,inSeccion,"329");
		MAIN_HTML += setFilaBoolean("Show Aden Manager",general.ADEN,inSeccion,"330");
		MAIN_HTML += setFilaBoolean("Show Rune Manager",general.RUNE,inSeccion,"331");
		MAIN_HTML += setFilaBoolean("Show Oren Manager",general.OREN,inSeccion,"332");
		MAIN_HTML += setFilaBoolean("Show Dion Manager",general.DION,inSeccion,"333");
		MAIN_HTML += setFilaBoolean("Show Gludio Manager",general.GLUDIO,inSeccion,"334");
		MAIN_HTML += setFilaBoolean("Show Goddard Manager",general.GODDARD,inSeccion,"335");
		MAIN_HTML += setFilaBoolean("Show Schuttgart Manager",general.SCHUTTGART,inSeccion,"336");
		MAIN_HTML += setFilaBoolean("Show Innadril Manager",general.INNADRIL,inSeccion,"337");

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	private static String getConfigBuffer_secciones(L2PcInstance player, String idBox){
		String inSeccion = "bufferSecciones";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Buffer Section")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Improved",general.BUFFER_IMPROVED_SECTION,inSeccion,"147");

		MAIN_HTML += setFilaBoolean("Buff general",general.BUFFER_BUFF_SECTION,inSeccion,"149");

		MAIN_HTML += setFilaBoolean("Chant",general.BUFFER_CHANT_SECTION,inSeccion,"151");

		MAIN_HTML += setFilaBoolean("Dances",general.BUFFER_DANCE_SECTION,inSeccion,"153");

		MAIN_HTML += setFilaBoolean("Song",general.BUFFER_SONG_SECTION,inSeccion,"155");

		MAIN_HTML += setFilaBoolean("Resist",general.BUFFER_RESIST_SECTION,inSeccion,"157");

		MAIN_HTML += setFilaBoolean("Improved",general.BUFFER_IMPROVED_SECTION,inSeccion,"147");

		MAIN_HTML += setFilaBoolean("Cubic",general.BUFFER_CUBIC_SECTION,inSeccion,"159");

		MAIN_HTML += setFilaBoolean("Prophecy",general.BUFFER_PROPHECY_SECTION,inSeccion,"161");

		MAIN_HTML += setFilaBoolean("Special",general.BUFFER_SPECIAL_SECTION,inSeccion,"163");

		MAIN_HTML += setFilaBoolean("Otros",general.BUFFER_OTROS_SECTION,inSeccion,"165");

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	private static String getConfigDressme(L2PcInstance player, String idBox){
		String inSeccion = "dressme";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Dressme Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Dressme Enabled",general.DRESSME_STATUS,inSeccion,"344");

		MAIN_HTML += setFilaBoolean("Dressme Target Enabled",general.DRESSME_TARGET_STATUS,inSeccion,"349");

		MAIN_HTML += setFilaBoolean("Dressme can use in olys",general.DRESSME_CAN_USE_IN_OLYS,inSeccion,"345");

		MAIN_HTML += setFilaBoolean("Dressme can change clothes in Olys",general.DRESSME_CAN_CHANGE_IN_OLYS,inSeccion,"346");

		MAIN_HTML += setFilaBoolean("New Dressme slot for free?",general.DRESSME_NEW_DRESS_IS_FREE ,inSeccion,"347");

		MAIN_HTML += setFilaModif("Cost Dressme new Slot", "" , inSeccion, "348");
		if(idBox.equals("348")){
			MAIN_HTML += getEdititem("Cost Dressme new Slot", general.DRESSME_NEW_DRESS_COST ,inSeccion,idBox);
		}

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	private static String getConfigBuffer(L2PcInstance player, String idBox){
		String inSeccion = "buffer";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Buffer Config")+central.LineaDivisora(2) + central.LineaDivisora(1);

		String btnSecciones = "<button value=\"Section\" action=\"bypass -h " + bypassNom + " setConfig1 bufferSecciones 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnValores = "<button value=\"Cost\" action=\"bypass -h " + bypassNom + " setConfig1 bufferPrecios 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnConfiguracionBuff = "<button value=\"Buff\" action=\"bypass -h " + bypassNom + " indirizza gestisci_buff true 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String botonConf = "<table width=260><tr><td width=130 align=center>"+btnSecciones+"</td><td width=130 align=center>"+btnValores+"</td></tr></table>";
		botonConf += "<table width=260><tr><td width=260 align=center>"+btnConfiguracionBuff+"</td></tr></table>";

		MAIN_HTML += central.LineaDivisora(3) + central.headFormat(botonConf) + central.LineaDivisora(3);


		MAIN_HTML += setFilaModif("ID Item Cost", central.getNombreITEMbyID(general.BUFFER_ID_ITEM) , inSeccion, "136");
		if(idBox.equals("136")){
			MAIN_HTML += getEditBox("ID Item Cost", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif(msg.CONFIG_MIN_LVL_CAN_USE, String.valueOf(general.BUFFER_LVL_MIN), inSeccion, "138");
		if(idBox.equals("138")){
			MAIN_HTML += getEditBox(msg.CONFIG_MIN_LVL_CAN_USE, idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Operation time between", String.valueOf(general.BUFFER_TIME_WAIT), inSeccion, "139");
		if(idBox.equals("139")){
			MAIN_HTML += getEditBox("Operation time between (Second)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Scheme for Player", String.valueOf(general.BUFFER_SCHEMA_X_PLAYER), inSeccion, "146");
		if(idBox.equals("146")){
			MAIN_HTML += getEditBox("Scheme for Player", idBox, inSeccion);
		}


		MAIN_HTML += setFilaModif("Heal Cost", String.valueOf(general.BUFFER_HEAL_VALOR), inSeccion, "169");
		if(idBox.equals("169")){
			MAIN_HTML += getEditBox("Heal Cost(CP, HP y MP)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Remove all buff Cost", String.valueOf(general.BUFFER_REMOVE_BUFF_VALOR), inSeccion, "171");
		if(idBox.equals("171")){
			MAIN_HTML += getEditBox("Remove all buff Cost", idBox, inSeccion);
		}


		MAIN_HTML += setFilaBoolean("Karma Player can use It",general.BUFFER_CON_KARMA,inSeccion,"137");

		MAIN_HTML += setFilaBoolean("Buffer free",general.BUFF_GRATIS,inSeccion,"140");

		MAIN_HTML += setFilaBoolean("Buffer GM Only",general.BUFFER_GM_ONLY,inSeccion,"141");

		MAIN_HTML += setFilaBoolean("Individual selection buff",general.BUFFER_SINGLE_BUFF_CHOICE,inSeccion,"144");

		MAIN_HTML += setFilaBoolean("Scheme System",general.BUFFER_SCHEME_SYSTEM,inSeccion,"145");

		MAIN_HTML += setFilaBoolean("Auto Buff",general.BUFFER_AUTOBUFF,inSeccion,"167");

		MAIN_HTML += setFilaBoolean("Heal",general.BUFFER_HEAL,inSeccion,"168");
		
		MAIN_HTML += setFilaBoolean("Heal on Flag Mode", general.BUFFER_HEAL_CAN_FLAG ,inSeccion,"521");
		
		MAIN_HTML += setFilaBoolean("Heal on Combat Mode", general.BUFFER_HEAL_CAN_IN_COMBAT ,inSeccion,"522");
		
		MAIN_HTML += setFilaBoolean("Remove buff",general.BUFFER_REMOVE_BUFF,inSeccion,"170");

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	private static String getConfigVote(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Vote Reward Config")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += setFilaModif("TopZone Reward", "", "vote", "69");
		if(idBox.equals("69")){
			opera.enviarHTML(player,getEdititem("TopZone Reward",general.VOTO_REWARD_TOPZONE,"vote",idBox,true));
			return "";
		}
		MAIN_HTML += setFilaModif("HopZone Reward", "", "vote", "70");
		if(idBox.equals("70")){
			opera.enviarHTML(player,getEdititem("HopZone Reward",general.VOTO_REWARD_HOPZONE,"vote",idBox,true));
			return "";
		}

		MAIN_HTML += setFilaBoolean("Topzone Vote",general.VOTO_REWARD_ACTIVO_TOPZONE,"vote","71");
		MAIN_HTML += setFilaBoolean("Hopzone Vote",general.VOTO_REWARD_ACTIVO_HOPZONE,"vote","72");
		
		MAIN_HTML += setFilaBoolean("Vote every 12 hrs",general.VOTE_EVERY_12_HOURS,"vote","568");

		MAIN_HTML += setFilaModif("Wait Second to Validated", String.valueOf(general.VOTO_REWARD_SEG_ESPERA), "vote", "73");
		if(idBox.equals("73")){
			MAIN_HTML += getEditBox("Wait Second to Validates", idBox, "vote");
		}
		
		MAIN_HTML += setFilaBoolean("Reward all ppl on the same Network",general.VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER,"vote","533");

		MAIN_HTML += setFilaBoolean("Interact with Zeus, only if they have special voting Item.", general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM , "vote", "310");

		MAIN_HTML += setFilaModif("ID Item for used ZeuS (We recommend using items with lifetime)", central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID) + " (" + String.valueOf(general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID) + ")", "vote", "311");
		if(idBox.equals("311")){
			MAIN_HTML += getEditBox("ID Item for used ZeuS (We recommend using items with lifetime)", idBox, "vote");
		}

		MAIN_HTML += setFilaBoolean("Sell temporary Item, only if they have special voting Item fail.", general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM , "vote", "312");

		MAIN_HTML += setFilaModif("ID Temporary Item for used ZeuS, if they have Special voting Item fail", central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID) + " (" +  String.valueOf(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID) + ")", "vote", "313");
		if(idBox.equals("313")){
			MAIN_HTML += getEditBox("ID Temporary Item for used ZeuS, if they have Special voting Item fail", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("Temporal Item Cost", "" , "vote", "314");
		if(idBox.equals("314")){
			MAIN_HTML += getEdititem("Temporal Item Cost", general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE , "vote", "314");
		}

		MAIN_HTML += setFilaBoolean("Auto Vote Reward System", general.VOTO_AUTOREWARD , "vote", "328");
		
		MAIN_HTML += setFilaBoolean("Auto Vote Reward AFK Check", general.VOTO_REWARD_AUTO_AFK_CHECK , "vote", "514");

		MAIN_HTML += setFilaModif("AutoVote Reward minutes between check votes", String.valueOf(general.VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK ), "vote", "323");
		if(idBox.equals("323")){
			MAIN_HTML += getEditBox("Minutes between check votes", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("AutoVote Rewarding every X Votes", String.valueOf(general.VOTO_REWARD_AUTO_RANGO_PREMIAR), "vote", "324");
		if(idBox.equals("324")){
			MAIN_HTML += getEditBox("AutoVote Rewarding every X Votes", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("Messages to reach the Vote reward", String.valueOf(general.VOTO_REWARD_AUTO_MENSAJE_FALTA), "vote", "325");
		if(idBox.equals("325")){
			MAIN_HTML += getEditBigBox("Input the message<br1>Use: %VOTENEED% Votes Needed<br1>%VOTEACT% current votes<br1>%VOTETOREWARD% to show the Vote to Reward<br1>%SITE% To show the Vote Web Site", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("Messages when the Vote reward is reached", String.valueOf(general.VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA), "vote", "326");
		if(idBox.equals("326")){
			MAIN_HTML += getEditBigBox("Input the message: Used %SITE% To show the Vote Web Site", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("Reward when the Hopzone voting cycle was reached", "", "vote", "512");
		if(idBox.equals("512")){
			opera.enviarHTML(player,getEdititem("Reward when the Hopzone voting cycle was reached", general.VOTO_REWARD_AUTO_REWARD_META_HOPZONE ,"vote",idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("Reward when the Topzone voting cycle was reached", "", "vote", "513");
		if(idBox.equals("513")){
			opera.enviarHTML(player,getEdititem("Reward when the Topzone voting cycle was reached", general.VOTO_REWARD_AUTO_REWARD_META_TOPZONE ,"vote",idBox,true));
			return "";
		}



		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}





	private static String getListaIndividual_INTEGER(Vector<Integer> Lista, String Seccion, String idBox){
		if(Lista.isEmpty()){
			return "";
		}
		String BotonSuprimir ="";
		String Grilla = "";
		String Retorno = "";
		int Contador = 0;
		Grilla = "<table width=260><tr>";
		if(!Lista.isEmpty()){
			for(int _lista : Lista){
				//suprdato
				Contador++;
				BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h "+ bypassNom +" setConfig1 suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				Grilla += "<td width=65 align=center>"+String.valueOf(_lista)+"<br1>"+BotonSuprimir+"</td>";
				if((Contador%4) == 0){
					Grilla += "</tr><tr>" ;
				}
			}
			if(Grilla.endsWith("<tr>") || Grilla.endsWith("</td>")){
				Grilla += "</tr>";
			}
			Grilla += "</table>";
			Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}
		return Retorno;
	}

	private static String getListaIndividual_NPC_INTEGER(Vector<Integer> Lista, String Seccion, String idBox){
		if(Lista.isEmpty()){
			return "";
		}
		String BotonSuprimir ="";
		String Grilla = "";
		String Retorno = "";
		int Contador = 0;
		Grilla = "<table width=260><tr>";
		if(!Lista.isEmpty()){
			for(int _lista : Lista){
				//suprdato
				Contador++;
				BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				Grilla += "<td width=65 align=center>"+String.valueOf(_lista)+"<br1>"+BotonSuprimir+"</td>";
				if((Contador%4) == 0){
					Grilla += "</tr><tr>" ;
				}
			}
			if(Grilla.endsWith("<tr>") || Grilla.endsWith("</td>")){
				Grilla += "</tr>";
			}
			Grilla += "</table>";
			Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}
		return Retorno;
	}

	private static String getListaIndividual(Vector<String> Lista, String Seccion, String idBox){
		if(Lista.isEmpty()){
			return "";
		}
		String BotonSuprimir ="";
		String Grilla = "";
		String Retorno = "";
		for(String _lista : Lista){
			BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 suprdato "+Seccion+" "+idBox+" "+_lista+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			Grilla = "<table width=260><tr><td width=215 align=center>"+_lista+"</td><td width=45>"+BotonSuprimir+"</td></tr></table>";
			Retorno += central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}
		return Retorno;
	}

	private static String getListaIndividual(String Lista, String Seccion, String idBox){
		if(Lista.isEmpty()){
			return "";
		}
		String BotonSuprimir ="";
		String Grilla = "";
		String Retorno = "";
		for(String _lista : Lista.split(",")){
			//suprdato
			BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 suprdato "+Seccion+" "+idBox+" "+_lista+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			Grilla = "<table width=260><tr><td width=215 align=center>"+_lista+"</td><td width=45>"+BotonSuprimir+"</td></tr></table>";
			Retorno += central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}
		return Retorno;
	}


	private static String getColores(String Colores, String Seccion, String idBox){
		try{
			if(Colores.isEmpty()){
				return "";
			}
		}catch(Exception a ){
			_log.warning(a.getMessage() + "->" + Colores);
			return "";
		}

		String Retorno ="";

		String BotonSuprimir ="";
		String Grilla = "<table width=260><tr>";

		int Contador = 0;

		//suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+"
		for(String Color : Colores.split(",")){
			Contador++;
			BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 suprdato "+Seccion+" "+idBox+" "+Color+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			Grilla += "<td width=65 align=center><font color="+Color+">This Color</font><br1>"+BotonSuprimir+"</td>";
			if((Contador%4)==0){
				Grilla += "</tr><tr>";
			}
		}

		if(!Grilla.endsWith("</tr>")){
			Grilla += "</tr>";
		}

		Grilla += "</table>";
		Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);


		return Retorno;

		//HTML_MIENTRAS += "<tr><td width=230 align=center>" + central.getNombreITEMbyID(ID_ITEM) + " ("+ String.valueOf(rss.getInt(4)) +",["+String.valueOf(rss.getInt(3))+"]) </td><td width=50 align=center>"+BOTON_BORRAR_ITEM_FAMILIA+"</td></tr>";
	}

	private static String getPremiosLista(String premios, String Seccion, String idBox){
		try{
			if(premios.isEmpty()){
				return "";
			}
		}catch(Exception a ){
			_log.warning(a.getMessage() + "->" + premios);
			return "";
		}

		String Retorno ="";

		if(!premios.isEmpty()){
			String BotonSuprimir ="";
			String Grilla = "<table width=260>";

			for(String pre : premios.split(";")){
				String[] pre2 = pre.split(",");
				BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 supritem "+Seccion+" "+idBox+" "+pre+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				Grilla += "<tr><td width=215 align=center>"+central.getNombreITEMbyID(Integer.valueOf(pre2[0]))+" ("+pre2[1]+")</td><td width=45>"+BotonSuprimir+"</td></tr>";
			}

			Grilla += "</table>";
			Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}


		return Retorno;

		//HTML_MIENTRAS += "<tr><td width=230 align=center>" + central.getNombreITEMbyID(ID_ITEM) + " ("+ String.valueOf(rss.getInt(4)) +",["+String.valueOf(rss.getInt(3))+"]) </td><td width=50 align=center>"+BOTON_BORRAR_ITEM_FAMILIA+"</td></tr>";
	}
	
	@SuppressWarnings("unused")
	private static String RemoveItemStringInteger(String Listastr, String Remover){
		Vector<Integer> Lista = new Vector<Integer>();
		
		for(String Spi : Listastr.split(",")){
			Lista.add(Integer.valueOf(Spi));
		}
		
		if(Lista == null){
			return "";
		}
		
		if(Lista.isEmpty()){
			return "";
		}
		String Retorno ="";

		if(!Lista.remove(Integer.valueOf(Remover))){
			Retorno = "";
			_log.warning("Error al Remover el ID");
			return Retorno;
		}

		if(Lista.size()>0){
			for(int _IDVector : Lista){
				if(Retorno.length()>0){
					Retorno +=",";
				}
				Retorno += String.valueOf(_IDVector);
			}
		}
		return Retorno;
	}

	private static String RemoveItemVectorInteger(Vector<Integer> Lista, String Remover){
		if(Lista.isEmpty()){
			return "";
		}
		String Retorno ="";

		if(!Lista.remove(Integer.valueOf(Remover))){
			Retorno = "";
			_log.warning("Error al Remover el ID");
			return Retorno;
		}

		if(Lista.size()>0){
			for(int _IDVector : Lista){
				if(Retorno.length()>0){
					Retorno +=",";
				}
				Retorno += String.valueOf(_IDVector);
			}
		}
		return Retorno;
	}

	private static String RemoveListaString(int[] Lista, String Remover){
		if(Lista.length==0){
			return "";
		}
		String Retorno ="";
		try{
			Vector<String> vecPremio = new Vector<String>();
			for(int pre : Lista){
				vecPremio.add(String.valueOf(pre));
			}
			vecPremio.remove(Remover);

			if(vecPremio.size()>0){
				for(String premiosIn : vecPremio){
					if(Retorno.length()>0){
						Retorno +=",";
					}
					Retorno += premiosIn;
				}
			}
		}catch(Exception a){
			Retorno = "";
		}
		return Retorno;
	}

	private static String RemoveListaString(String Lista, String Remover){
		if(Lista.isEmpty()){
			return "";
		}
		String Retorno ="";
		try{
			Vector<String> vecPremio = new Vector<String>();
			for(String pre : Lista.split(",")){
				vecPremio.add(pre);
			}
			vecPremio.remove(Remover);

			if(vecPremio.size()>0){
				for(String premiosIn : vecPremio){
					if(Retorno.length()>0){
						Retorno +=",";
					}
					Retorno += premiosIn;
				}
			}
		}catch(Exception a){
			Retorno = "";
		}
		return Retorno;
	}

	private static String RemovePremio(String Premios, String Remover){
		if(Premios.isEmpty()){
			return "";
		}
		String Retorno ="";
		try{
			Vector<String> vecPremio = new Vector<String>();
			for(String pre : Premios.split(";")){
				vecPremio.add(pre);
			}
			vecPremio.remove(Remover);

			if(vecPremio.size()>0){
				for(String premiosIn : vecPremio){
					if(Retorno.length()>0){
						Retorno +=";";
					}
					Retorno += premiosIn;
				}
			}
		}catch(Exception a){
			Retorno = "";
		}
		return Retorno;
	}

	public static String bypass(L2PcInstance player, String event){

		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning("ADMIN->"+event);
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.msgbox(event, player);
		}

		String[] eventSplit = event.split(" ");
		String evento = eventSplit[1];
		if(eventSplit.length<6){
			central.msgbox_Lado(msg.DEBES_INGREGAR_LOS_DATOS_SOLICITADOS, player);
			return htmlShow(player, evento, "0");
		}

		String eventParam1 = "";
		String eventParam2 = "";
		String eventParam3 = "";
		String eventParam4 = "";
		String eventParam5 = "";

		//return htmls.ErrorTipeoEspacio();

		try{
			eventParam1 = eventSplit[2];
			eventParam2 = eventSplit[3];
			eventParam3 = eventSplit[4];
			eventParam4 = eventSplit[5];
			eventParam5 = eventSplit[6];
		}catch(Exception a ){
			return htmls.ErrorTipeoEspacio_Admin();
		}


		String strHtml = "";
			switch(evento)/*Determinado despues de setConfig1*/{
				case "general":
					return getConfigNPCZeuS(player, eventParam1);
				case "dressme":
					return getConfigDressme(player, eventParam1);
				case "vote":
					return getConfigVote(player, eventParam1);
				case "teleport":
					return getConfigTeleport(player, eventParam1);
				case "dropsear":
					return getConfigDropSearch(player, eventParam1);
				case "partyfin":
					return getConfigPartyFinder(player, eventParam1);
				case "flagfin":
					return getConfigFlagFinder(player, eventParam1);
				case "augmentspe":
					return getConfigAugmentEspecial(player, eventParam1);
				case "enchantspe":
					return getConfigEnchant(player, eventParam1);
				/*case "donacion":
					return getConfigDonacion(player, eventParam1);*/
				case "desafio":
					return getConfigDesafios(player, eventParam1);
				case "colorname":
					return getConfigColor(player, eventParam1);
				case "elementalspe":
					return getConfigElemental(player, eventParam1);
				case "bossinfo":
					return getConfigRaidbossInfo(player, eventParam1);
				case "opcvarias":
					return getConfigVarios(player, eventParam1);
				case "pvppkColor":
					return getConfigPVPPKColor(player, eventParam1);
				case "pvpColorCantidad":
					return getConfigPvPCantidad(player, eventParam1);
				case "pkColorCantidad":
					return getConfigPKCantidad(player, eventParam1);
				case "pvpColor":
					return getConfigPVPColor(player, eventParam1);
				case "pkColor":
					return getConfigPKColor(player, eventParam1);
				case "delevel":
					return getConfigDelevel(player, eventParam1);
				case "pvpConfig":
					return getConfigPVP(player, eventParam1);
				case "pvpMensaje":
					return getConfigCicloMensajes(player, eventParam1);
				case "pvpMensajePVP":
					return getConfigCicloMensajes_PVP(player, eventParam1);
				case "pvpMensajePK":
					return getConfigCicloMensajes_PK(player, eventParam1);
				case "raidconfig":
					return getConfigRaidAnnoucement(player, eventParam1);
				case "buffer":
					return getConfigBuffer(player, eventParam1);
				case "bufferSecciones":
					return getConfigBuffer_secciones(player, eventParam1);
				case "bufferPrecios":
					return getConfigBuffer_valores(player, eventParam1);
				case "transform":
					return getConfigTransform(player, eventParam1);
				case "Olymp":
					return getConfigOly(player,eventParam1);
				case "Antibot":
					return getConfigAntiBot(player, eventParam1);
				case "Antibot2":
					return getConfigAntiBot2(player, eventParam1);
				case "castlema":
					return getConfigCastleManager(player,eventParam1);
				case "loadConfig":
					return getConfigLoad(player, eventParam1);
				case "cancelbuff":
					return getConfigCancelBuff(player, eventParam1);
				/*case "premium":
					return getConfigPremium(player, eventParam1);*/
				case "bufferchar":
					return getConfigBufferChar(player, eventParam1);
				case "comunidad":
					return getConfigComunidad(player,eventParam1);
				case "cbpartyfinder":
					return getConfigComunidadPartyFinder(player, eventParam1);
				case "overenchant":
					return getConfigOverEnchant(player, eventParam1);
				case "emailregister":
					return getConfigRegister(player,eventParam1);
				case "dualbox":
					return getConfigDualBox(player,eventParam1);
				case "ChatConfig":
					return getConfigChat(player, eventParam1);
				case "RaidBossEvent":
					return getConfigRaidBossEvent(player,eventParam1);
				case "TownWarEvent":
					return getConfigTownWarEvent(player, eventParam1);
				case "changeName":
					return  getConfigChangeName(player, eventParam1);
				case "Antibotask":
					if(!eventParam2.equals("0")){
							deleteQuestion(eventParam2);
					}
					if(eventParam1.equals("-20")){
						return getHTMLQuestion();
					}

					return getConfigAntiBotAsk(player, Integer.valueOf(eventParam1));
				case "Antibotasksave":
					String Respuesta ="";
					String Pregunta = "";
					Respuesta = eventParam1;
					if(eventSplit.length>7){
						//6
						String[] PreguntaIN = event.split(" ");
						for(int i=6;i<=(PreguntaIN.length-1);i++){
							Pregunta += PreguntaIN[i] + " ";
						}
						Pregunta.trim();
					}else{
						Pregunta = eventParam5;
					}
					saveQuestion(Pregunta,Respuesta,player);
					return getConfigAntiBotAsk(player, 0);
				case "banip":
					return getConfigIPBan(player,eventParam1);
				case "_loadConfig":
					//String BTN_load = "<button value=\"Add\" action=\"bypass -h " + bypassNom + " setConfig1 _loadConfig %LOAD% 0 "+idBox+" "+inSeccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
					if(eventParam1.equals("load_all")){
						general.loadConfigs();
						central.msgbox("Load Complete", player);
					}else if(eventParam1.equals("load_dropsearch")){
						general.loadDropList();
						central.msgbox("Drop search Load Complete", player);
					}else if(eventParam1.equals("load_raidbossevent")){
						general.loadConfigs(false);
						RaidBossEvent.setNewSearch();
						RaidBossEvent.IntervalEventStart();
						central.msgbox("Load Complete", player);
					}else if(eventParam1.equals("create_droplist")){
						String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>" + central.LineaDivisora(1) + central.headFormat("Create Droplist") + central.LineaDivisora(1) + central.headFormat("This process may take several minutes.<br1> Not recommended for live servers","LEVEL")+central.LineaDivisora(1)+central.getPieHTML()+"</body></html>";
						opera.enviarHTML(player, HTML);
						general.setNewDataTableDropList();
						HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>" + central.LineaDivisora(1) + central.headFormat("Create Droplist") + central.LineaDivisora(1) + central.headFormat("The process is complete","LEVEL")+central.LineaDivisora(1)+central.getPieHTML()+"</body></html>";
						opera.enviarHTML(player, HTML);
					}
					return getConfigLoad(player, "0");
				case "setValor"://eventParam1 => ID EN BD
					if(!opera.isNumeric(eventParam2) && VectorOnlyNumeros.contains(Integer.valueOf(eventParam1))){
						central.msgbox(msg.INGRESE_SOLO_NUMEROS, player);
						return htmlShow(player, eventParam3, eventParam1);
					}
					if(eventParam1.equals("1")){//ID BD NPC ZEUS
						if(Integer.valueOf(eventParam2)==general.ID_NPC_CH){
							central.msgbox("Wrog NPC ID.", player);
							return getConfigNPCZeuS(player, eventParam1);
						}
					}else if(eventParam1.equals("2")){
						if(Integer.valueOf(eventParam2) == general.ID_NPC){
							central.msgbox("Wrog NPC ID.", player);
							return getConfigNPCZeuS(player, eventParam1);
						}
					}else if(eventParam1.equals("316")){
						int TempoVal = Integer.valueOf(eventParam2);
						if((TempoVal<0) || (TempoVal>100)){
							central.msgbox("Put numbers between 1 and 100", player);
							return getConfigNPCZeuS(player, eventParam1);
						}
					}
					setValue(eventParam1,eventParam2,player);

					
					if(eventParam3.equals("comunidad")){
						general._getAllTopPlayersConfig();
					}
					
					return htmlShow(player,eventParam3,eventParam1);

				case "setValorBig":
					//setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\"
					if(eventParam5.isEmpty()){
						central.msgbox(msg.NO_DEJE_CAMPOS_VACIOS, player);
					}
					String Mensaje = getTextoParam5(event);
					setValue(eventParam1,Mensaje,player);

					return htmlShow(player, eventParam3, eventParam1);
				case "setCombo":
					/*Aqui*/
					//setConfig1 setCombo $cmbSelecc 0 "+idINBD+" "+Seccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
					setValue(eventParam3,eventParam1,player);
					return htmlShow(player, eventParam4 , eventParam3);
				case "setProducto":
					//setReward(String idINBD, String Parametro, L2PcInstance player){
					if(!opera.isNumeric(eventParam1) || !opera.isNumeric(eventParam2))/*id item - cantidad*/{
						central.msgbox("EL id del Item y cantidad debe ser numerico", player);
						return htmlShow(player,eventParam4/*Evento*/,eventParam3/*ID IN BD*/);
					}
					if((central.getNombreITEMbyID(Integer.valueOf(eventParam1))==null) || central.getNombreITEMbyID(Integer.valueOf(eventParam1)).equals("none")){
						central.msgbox("EL id del Item ingresado no existe", player);
						return htmlShow(player,eventParam4/*Evento*/,eventParam3/*ID IN BD*/);
					}
					setReward(eventParam3, eventParam1+","+eventParam2, player);
					return htmlShow(player,eventParam4/*Evento*/,eventParam3/*ID IN BD*/);
				case "setLista":
					if(!opera.isNumeric(eventParam1) && opera.searchInArray(operadoresNumericos,eventParam2)){
						central.msgbox(msg.INGRESE_SOLO_NUMEROS, player);
						//return getConfigNPCZeuS(player, eventParam1);
						return htmlShow(player, eventParam3, eventParam2);
					}
					if(eventParam2.equals("317")){
						if(general.ANTIBOT_BORRAR_ITEM_ID.split(",").length>14){
							central.msgbox("You can not add more ID", player);
							return htmlShow(player, eventParam3, eventParam2);
						}
					}
					//setLista $idNpc "+idINBD+" "+Seccion+" 0 0
					setLista(eventParam2, eventParam1, player);
					return htmlShow(player, eventParam3, eventParam2);
				case "setstatus":
					status(player,Integer.valueOf(eventParam2));
					if(eventParam2.equals("344")){
						//dressme desactivar
						dressme.getInstance().setNewConfigDressme();
					}
					return htmlShow(player,eventParam1/*Evento*/,eventParam2/*ID IN BD*/);
				case "suprdato":
					//suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+"
					if(eventParam2.equals("87")){
						setValue(eventParam2, RemoveItemVectorInteger(general.DESAFIO_NPC_BUSQUEDAS,eventParam3),player);
						return htmlShow(player, eventParam1,  eventParam2);
					}else if(eventParam2.equals("105")){
						//setValue(idINBD, Parametro, player);
						setValue(eventParam2,RemoveListaString(general.PINTAR_MATRIZ,eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("174")){
						setValue(eventParam2,RemoveListaString(general.ENCHANT_ANNOUCEMENT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("281")){
						setValue(eventParam2,RemoveListaString(general.get_AccessConfig(), eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("284")){
						setValue(eventParam2,RemoveListaString(general.OLY_SECOND_SHOW_OPPONET, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("286")){
						setValue(eventParam2,RemoveListaString(general.OLY_ACCESS_ID_MODIFICAR_POINT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("317")){
						setValue(eventParam2,RemoveListaString(general.ANTIBOT_BORRAR_ITEM_ID , eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("350")){
						setValue(eventParam2, RemoveItemVectorInteger(general.DROP_SEARCH_MOB_BLOCK_TELEPORT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("351")){
						setValue(eventParam2, RemoveItemVectorInteger(general.RAIDBOSS_ID_MOB_NO_TELEPORT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("357")){
						setValue(eventParam2, RemoveItemVectorInteger(general.RETURN_BUFF_NOT_STEALING, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("486")){
						setValue(eventParam2, RemoveItemVectorInteger(general.EVENT_RAIDBOSS_RAID_ID , eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);						
					}else if(eventParam2.equals("566")){
						setValue(eventParam2, RemoveItemStringInteger(general.EVENT_TOWN_WAR_NPC_ID_HIDE , eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);						
					}
				case "supritem":
					String itemForElim = "";
					//supritem "+Seccion+" "+idBox+" "+pre+"
						if(eventParam2.equals("69")){
							itemForElim = general.VOTO_REWARD_TOPZONE;
						}else if(eventParam2.equals("70")){
							itemForElim = general.VOTO_REWARD_HOPZONE;
						}else if(eventParam2.equals("74")){
							itemForElim = general.VOTO_ITEM_BUFF_ENCHANT_PRICE;
						}else if(eventParam2.equals("83")){
							itemForElim = general.TELEPORT_PRICE;
						}else if(eventParam2.equals("85")){
							itemForElim = general.DESAFIO_85_PREMIO;
						}else if(eventParam2.equals("86")){
							itemForElim = general.DESAFIO_NOBLE_PREMIO;
						}else if(eventParam2.equals("89")){
							itemForElim = general.DROP_TELEPORT_COST;
						}else if(eventParam2.equals("91")){
							itemForElim = general.PARTY_FINDER_PRICE;
						}else if(eventParam2.equals("98")){
							itemForElim = general.FLAG_FINDER_PRICE;
						}else if(eventParam2.equals("104")){
							itemForElim = general.PINTAR_PRICE;
						}else if(eventParam2.equals("106")){
							itemForElim = general.AUGMENT_ITEM_PRICE;
						}else if(eventParam2.equals("107")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE;
						}else if(eventParam2.equals("111")){
							itemForElim = general.ENCHANT_ITEM_PRICE;
						}else if(eventParam2.equals("117")){
							itemForElim = general.RAIDBOSS_INFO_TELEPORT_PRICE;
						}else if(eventParam2.equals("122")){
							itemForElim = general.OPCIONES_CHAR_SEXO_ITEM_PRICE;
						}else if(eventParam2.equals("123")){
							itemForElim = general.OPCIONES_CHAR_NOBLE_ITEM_PRICE;
						}else if(eventParam2.equals("124")){
							itemForElim = general.OPCIONES_CHAR_LVL85_ITEM_PRICE;
						}else if(eventParam2.equals("125")){
							itemForElim = general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE;
						}else if(eventParam2.equals("126")){
							itemForElim = general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE;
						}else if(eventParam2.equals("130")){
							itemForElim = general.ELEMENTAL_ITEM_PRICE;
						}else if(eventParam2.equals("133")){
							itemForElim = general.DELEVEL_PRICE;
						}else if(eventParam2.equals("255")){
							itemForElim = general.TRANSFORMATION_PRICE;
						}else if(eventParam2.equals("259")){
							itemForElim = general.TRANSFORMATION_ESPECIALES_PRICE;
						}else if(eventParam2.equals("260")){
							itemForElim = general.TRANSFORMATION_RAIDBOSS_PRICE;
						}else if(eventParam2.equals("265")){
							itemForElim = general.OPCIONES_CHAR_BUFFER_AIO_PRICE;
						}else if(eventParam2.equals("586")){
							itemForElim = general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30;
						}else if(eventParam2.endsWith("277")){
							itemForElim = general.OPCIONES_CHAR_FAME_PRICE;
						}else if(eventParam2.endsWith("314")){
							itemForElim = general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE;
						}else if(eventParam2.endsWith("341")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
						}else if(eventParam2.endsWith("342")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE_CHANCE;
						}else if(eventParam2.endsWith("343")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
						}else if(eventParam2.endsWith("348")){
							itemForElim = general.DRESSME_NEW_DRESS_COST;
						}else if(eventParam2.endsWith("370")){
							itemForElim = general.BUFFCHAR_COST_USE;
						}else if(eventParam2.endsWith("371")){
							itemForElim = general.BUFFCHAR_COST_HEAL;
						}else if(eventParam2.endsWith("372")){
							itemForElim = general.BUFFCHAR_COST_CANCEL;
						}else if(eventParam2.endsWith("373")){
							itemForElim = general.BUFFCHAR_COST_INDIVIDUAL;
						}else if(eventParam2.endsWith("376")){
							itemForElim = general.BUFFCHAR_DONATION_SECCION_COST;
						}else if(eventParam2.endsWith("386")){
							itemForElim = general.PVP_REWARD_ITEMS;
						}else if(eventParam2.endsWith("387")){
							itemForElim = general.PVP_PARTY_REWARD_ITEMS;
						}else if(eventParam2.endsWith("490")){
							itemForElim = general.EVENT_RAIDBOSS_REWARD_WIN;
						}else if(eventParam2.endsWith("491")){
							itemForElim = general.EVENT_RAIDBOSS_REWARD_LOOSER;
						}else if(eventParam2.endsWith("512")){
							itemForElim = general.VOTO_REWARD_AUTO_REWARD_META_HOPZONE;
						}else if(eventParam2.endsWith("513")){
							itemForElim = general.VOTO_REWARD_AUTO_REWARD_META_TOPZONE;
						}else if(eventParam2.endsWith("561")){
							itemForElim = general.EVENT_TOWN_WAR_REWARD_GENERAL;
						}else if(eventParam2.endsWith("562")){
							itemForElim = general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER;
						}
					if(itemForElim.length()>0){
						saveReward(RemovePremio(itemForElim, eventParam3),eventParam2,player);
					}
					return htmlShow(player,eventParam1,eventParam2);

			}
		return "";
	}

	private static String RemoveItemVectorInteger(
			String eVENT_TOWN_WAR_NPC_ID_HIDE, String eventParam3) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String getTextoParam5(String event){
		int contador = 0;
		String Mensaje = "";
		for (String _Parte : event.split(" ")){
			if(contador>5){
				if(Mensaje.length()>0){
					Mensaje+= " ";
				}
				Mensaje+=_Parte;
			}
			contador++;
		}
		return Mensaje;
	}

	private static void saveQuestion(String Pregunta, String Respuesta, L2PcInstance player){
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			ins = con.prepareStatement("INSERT INTO zeus_antibot(ask, answer) VALUES(?,?)");
			ins.setString(1,Pregunta);
			ins.setString(2,Respuesta);
			try{
				ins.executeUpdate();
				central.msgbox("Question have successfully added", player);
			}catch(SQLException e){

			}
		}catch(SQLException a){

		}
		try{
			con.close();
		}catch(SQLException a){

		}
		general.loadAntiBotQuestion();
	}

	private static void deleteQuestion(String idQuestion){
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = ConnectionFactory.getInstance().getConnection();
			ins = con.prepareStatement("DELETE FROM zeus_antibot WHERE zeus_antibot.id = ?");
			ins.setInt(1, Integer.valueOf(idQuestion));
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
		general.loadAntiBotQuestion();
	}

	private static void saveReward(String premios, String idINBD, L2PcInstance player){
		setValue(idINBD, premios, player);
	}

	private static void saveLista(String premios, String idINBD, L2PcInstance player){
		 setLista(idINBD, premios, player);
	}

	private static String htmlShow(L2PcInstance player, String evento, String idBox){
		return htmlShow(player, evento, idBox,"0");
	}
	
	private static String htmlShow(L2PcInstance player,String evento,String idBox, String showInNewWindows){
		switch(evento){
			case "general":
				return getConfigNPCZeuS(player, idBox);
			case "vote":
				return getConfigVote(player, idBox);
			case "teleport":
				return getConfigTeleport(player, idBox);
			case "dropsear":
				return getConfigDropSearch(player, idBox);
			case "partyfin":
				return getConfigPartyFinder(player, idBox);
			case "flagfin":
				return getConfigFlagFinder(player, idBox);
			case "augmentspe":
				return getConfigAugmentEspecial(player, idBox);
			case "enchantspe":
				return getConfigEnchant(player, idBox);
			/*case "donacion":
				return getConfigDonacion(player, idBox);*/
			case "desafio":
				return getConfigDesafios(player, idBox);
			case "colorname":
				return getConfigColor(player, idBox);
			case "elementalspe":
				return getConfigElemental(player, idBox);
			case "bossinfo":
				return getConfigRaidbossInfo(player, idBox);
			case "opcvarias":
				return getConfigVarios(player, idBox);
			case "delevel":
				return getConfigDelevel(player, idBox);
			case "pvppkColor":
				return getConfigPVPPKColor(player, "0");
			case "pvpColorCantidad":
				return getConfigPvPCantidad(player, idBox);
			case "pkColorCantidad":
				return getConfigPKCantidad(player, idBox);
			case "pvpColor":
				return getConfigPVPColor(player, idBox);
			case "pkColor":
				return getConfigPKColor(player, idBox);
			case "pvpConfig":
				return getConfigPVP(player, idBox);
			case "pvpMensaje":
				return getConfigCicloMensajes(player, idBox);
			case "pvpMensajePVP":
				return getConfigCicloMensajes_PVP(player, idBox);
			case "pvpMensajePK":
				return getConfigCicloMensajes_PK(player, idBox);
			case "raidconfig":
				return getConfigRaidAnnoucement(player, idBox);
			case "buffer":
				return getConfigBuffer(player, idBox);
			case "bufferSecciones":
				return getConfigBuffer_secciones(player, idBox);
			case "bufferPrecios":
				return getConfigBuffer_valores(player, idBox);
			case "transform":
				return getConfigTransform(player, idBox);
			case "Olymp":
				return getConfigOly(player, idBox);
			case "Antibot":
				return getConfigAntiBot(player, idBox);
			case "Antibot2":
				return getConfigAntiBot2(player, idBox);
			case "banip":
				return getConfigIPBan(player,idBox);
			case "castlema":
				return getConfigCastleManager(player,idBox);
			case "dressme":
				return getConfigDressme(player, idBox);
			case "cancelbuff":
				return getConfigCancelBuff(player, idBox);
			case "bufferchar":
				return getConfigBufferChar(player, idBox);
			/*case "premium":
				return getConfigPremium(player, idBox);*/
			case "comunidad":
				return getConfigComunidad(player,idBox);
			case "cbpartyfinder":
				return getConfigComunidadPartyFinder(player, idBox);
			case "overenchant":
				return getConfigOverEnchant(player, idBox);
			case "emailregister":
				return getConfigRegister(player,idBox);
			case "dualbox":
				return getConfigDualBox(player,idBox);
			case "ChatConfig":
				return getConfigChat(player, idBox);
			case "RaidBossEvent":
				return getConfigRaidBossEvent(player,idBox);
			case "TownWarEvent":
				return getConfigTownWarEvent(player, idBox);
			case "changeName":
				return getConfigChangeName(player, idBox);
		}
		return "";
	}

	private static void status(L2PcInstance player, int idProc){
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

	private static void setValue(String idINBD, String Parametro, L2PcInstance player){
			if(!opera.isMaster(player)){
				return;
			}
			String qry = "call sp_zeus_config(2,"+idINBD+",?,'','')";
			Connection conn = null;
			CallableStatement psqry;
			String Respu ="";
			try{
				conn = ConnectionFactory.getInstance().getConnection();
				psqry = conn.prepareCall(qry);
				psqry.setString(1, Parametro);
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

	private static void setLista(String idINBD, String Parametro, L2PcInstance player){
		if(!opera.isMaster(player)){
			return;
		}
		String qry = "call sp_zeus_config(4,"+idINBD+",?,'','')";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			psqry.setString(1, Parametro);
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


	private static void setReward(String idINBD, String Parametro, L2PcInstance player){
		if(!opera.isMaster(player)){
			return;
		}
		String qry = "call sp_zeus_config(3,"+idINBD+",'"+Parametro+"','','')";
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
