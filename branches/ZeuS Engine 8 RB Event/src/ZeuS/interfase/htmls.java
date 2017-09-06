package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.data.xml.impl.AdminData;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.olympiad.Participant;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.GM.olymp;
import ZeuS.ZeuS.ZeuS;
import ZeuS.admin.menu;
import ZeuS.procedimientos.opera;

public class htmls extends ManagerAIONpc{

	private static final Logger _log = Logger.getLogger(Config.class.getName());
	private static boolean VerTodoZP = false;

	public static void setWindowsPlus(L2PcInstance player){
		setWindowsPlus(player,"");
	}


	private static void setElement(L2PcInstance activeChar, byte type, int value, int armorType)
	{
		try{

			L2PcInstance player = activeChar;
			L2ItemInstance itemInstance = null;

			// only attempt to enchant if there is a weapon equipped
			L2ItemInstance parmorInstance = player.getInventory().getPaperdollItem(armorType);
			if ((parmorInstance != null) && (parmorInstance.getLocationSlot() == armorType))
			{
				itemInstance = parmorInstance;
			}

			if (itemInstance != null)
			{
				String old, current;
				Elementals element = itemInstance.getElemental(type);
				if (element == null)
				{
					old = "None";
				}
				else
				{
					old = element.toString();
				}

				// set enchant value
				player.getInventory().unEquipItemInSlot(armorType);
				if (type == -1)
				{
					itemInstance.clearElementAttr(type);
				}
				else
				{
					itemInstance.setElementAttr(type, value);
				}
				player.getInventory().equipItem(itemInstance);

				if (itemInstance.getElementals() == null)
				{
					current = "None";
				}
				else
				{
					current = itemInstance.getElemental(type).toString();
				}

				// send packets
				InventoryUpdate iu = new InventoryUpdate();
				iu.addModifiedItem(itemInstance);
				player.sendPacket(iu);

				// informations
				activeChar.sendMessage("Changed elemental power of " + player.getName() + "'s " + itemInstance.getItem().getName() + " from " + old + " to " + current + ".");
				if (player != activeChar)
				{
					player.sendMessage(activeChar.getName() + " has changed the elemental power of your " + itemInstance.getItem().getName() + " from " + old + " to " + current + ".");
				}
			}
		}catch(Exception a){

		}
	}

	private static String getPlayerByIP(L2PcInstance player){
		String ipNet = ZeuS.getIp_Wan(player);
		String NombrePlayerIP = "";
		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();
		Vector<String> IPSGM = new Vector<String>();
		for(L2PcInstance onlinePlayer : pls){
			try{
				if(onlinePlayer.isOnline()){
					if(ipNet.equals(ZeuS.getIp_Wan(onlinePlayer))) {
						if(NombrePlayerIP.length()>0){
							NombrePlayerIP += ",";
						}
						NombrePlayerIP += onlinePlayer.getName();
					}
				}
			}catch(Exception a){

			}
		}
		return player.getName() + ":" + NombrePlayerIP;
	}

	public static void getWindowsIPMayor(L2PcInstance player){
		String html = "";
		for (String playerGM : AdminData.getInstance().getAllGmNames(true))
		{
			L2PcInstance playerSELECT = null;
			try
			{
				playerSELECT  = L2World.getInstance().getPlayer(playerGM.replace("(invis)", "").trim());
				central.msgbox(getPlayerByIP(playerSELECT),player);
			}catch(Exception a){

			}
		}
	}



	public static void setWindowsPlus(L2PcInstance player, String Parametros){
		if(!opera.isPlusChar(player)){
			return;
		}

		String gmONS = "";
		for (L2PcInstance playerGM : L2World.getInstance().getAllGMs())
		{

			L2PcInstance playerSELECT = null;
			try
			{
				playerSELECT  = L2World.getInstance().getPlayer(playerGM.getName().replace("(invis)", "").trim());
				if(gmONS.length()>0){
					gmONS += ", ";
				}
				//PcPosition LocMe = player.getPosition();
				String Donde = "GM a la Cresta del Mundo";
				if(playerSELECT.isInsideRadius(player, 100, true, true)){
					Donde = "Casi pegado a ti";
				}else if(playerSELECT.isInsideRadius(player, 300,true, true)){
					Donde = "Muy Cerca";
				}else if(playerSELECT.isInsideRadius(player, 800,true, true)){
					Donde = "Cerca";
				}else if(playerSELECT.isInsideRadius(player, 6600,true, true)){
						Donde = "Ideal";
				}else if(playerSELECT.isInsideRadius(player, 10600,true, true)){
					Donde = "Perfecto!!!";
			}
				gmONS += playerGM + "("+Donde+")";
			}
			catch (Exception e)
			{
				central.msgbox("Nombre Player Incorrecto", player);
			}
		}

		central.msgbox("GMS: " + gmONS, player);


		String txtNombre = "<edit var=\"txtNombre\" width=120>";
		String txtLEVEL = "<edit var=\"txtLevel\" width=120>";
		String txtExp = "<edit var=\"txtExp\" width=120>";
		String txtSp = "<edit var=\"txtSp\" width=120>";
		String txtIdItem = "<edit var=\"txtIdItem\" width=120>";
		String txtCantidad = "<edit var=\"txtCanti\" width=120>";
		String txtEnchant = "<edit var=\"txtEnchant\" width=120>";
		String txtIdaumento = "<edit var=\"txtIDAumento\" width=120>";
		String cmbElemento = "<combobox width=75 var=cmbTipEleme list=Fire;Water;Wind;Earth;Dark;Holy>";
		String cmbBorrar = "<combobox width=180 var=cmbBorrar list=%LISTA%>";
		String txtElemento = "<edit var=\"txtElement\" width=120>";
		String txtPuntosO = "<edit var=\"txtPO\" width=120>";

		String btnLevel = "<button value=\"LEVEL\" action=\"bypass -h voice .ZZPPLL LEVEL $txtNombre $txtLevel 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnExp = "<button value=\"EXP\" action=\"bypass -h voice .ZZPPLL EXP $txtNombre $txtExp 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnSp = "<button value=\"SP\" action=\"bypass -h voice .ZZPPLL SP $txtNombre $txtSp 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnItem = "<button value=\"ITEM\" action=\"bypass -h voice .ZZPPLL ITEM $txtNombre $txtIdItem $txtCanti\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnEncAll = "<button value=\"ALL\" action=\"bypass -h voice .ZZPPLL ENC_ALL $txtNombre $txtEnchant 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnEncW = "<button value=\"WEAPON\" action=\"bypass -h voice .ZZPPLL ENC_W $txtNombre $txtEnchant 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnIDAumento = "<button value=\"AUMENTAR\" action=\"bypass -h voice .ZZPPLL AUMENTO $txtNombre $txtIDAumento 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnSetPLUS = "<button value=\"SET PLUS\" action=\"bypass -h voice .ZZPPLL SETPLUS $txtNombre 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnRemPLUS = "<button value=\"BORRAR\" action=\"bypass -h voice .ZZPPLL REMPLUS $cmbBorrar 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnGetGM = "<button value=\"GET GM'S\" action=\"bypass -h voice .ZZPPLL GETADMINC $txtNombre 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnDisconnect = "<button value=\"DISCONNECT\" action=\"bypass -h voice .ZZPPLL DISCN $txtNombre 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnDisconnectAllIP = "<button value=\"DISC. IP'S\" action=\"bypass -h voice .ZZPPLL DISCNIP $txtNombre 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnPunt = "<button value=\"SET POINT\" action=\"bypass -h voice .ZZPPLL PUNTOS $txtNombre $txtPO 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnGetPunt = "<button value=\"GET POINT\" action=\"bypass -h voice .ZZPPLL GETPUNTOS $txtNombre 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnMostrarTodoZeuS = "<button value=\"SHOW ZEUS\" action=\"bypass -h voice .ZZPPLL SHOWALLZEUS $txtNombre 0 0\" width=95 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnElemento_HEAD = "<button value=\"HEAD\" action=\"bypass -h voice .ZZPPLL ELEME_setlh $txtNombre $cmbTipEleme $txtElement\" width=90 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnElemento_CHEST = "<button value=\"CHEST\" action=\"bypass -h voice .ZZPPLL ELEME_setlc $txtNombre $cmbTipEleme $txtElement\" width=90 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnElemento_GLOVES = "<button value=\"GLOVE\" action=\"bypass -h voice .ZZPPLL ELEME_setlg $txtNombre $cmbTipEleme $txtElement\" width=90 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnElemento_FEET = "<button value=\"FEET\" action=\"bypass -h voice .ZZPPLL ELEME_setlb $txtNombre $cmbTipEleme $txtElement\" width=90 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnElemento_LEGS = "<button value=\"LEGS\" action=\"bypass -h voice .ZZPPLL ELEME_setll $txtNombre $cmbTipEleme $txtElement\" width=90 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnElemento_RIGHT_W = "<button value=\"WEAPON\" action=\"bypass -h voice .ZZPPLL ELEME_setlw $txtNombre $cmbTipEleme $txtElement\" width=90 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String GRILLA1 = "<table width=255>";
		GRILLA1 += "<tr><td width=84 align=CENTER>"+ btnElemento_HEAD +"</td><td width=84 align=CENTER>"+ btnElemento_CHEST +"</td><td width=84 align=CENTER>"+ btnElemento_GLOVES +"</td></tr>";
		GRILLA1 += "<tr><td width=84 align=CENTER>"+ btnElemento_FEET +"</td><td width=84 align=CENTER>"+ btnElemento_LEGS +"</td><td width=84 align=CENTER>"+ btnElemento_RIGHT_W +"</td></tr>";
		GRILLA1 +="</table>";

		String grillaLevel = "<table width=275><tr><td width=275 align=CENTER>LEVEL</td></tr><tr><td width=275 align=CENTER>"+ txtLEVEL +"</td></tr></table><br>"+btnLevel;
		String grillaExp = "<table width=275><tr><td width=275 align=CENTER>EXP</td></tr><tr><td width=275 align=CENTER>"+ txtExp +"</td></tr></table><br>"+btnExp;
		String grillaSp = "<table width=275><tr><td width=275 align=CENTER>SP</td></tr><tr><td width=275 align=CENTER>"+ txtSp +"</td></tr></table><br>"+btnSp;
		String grillaItem = "<table width=275><tr><td width=130 align=CENTER>ID</td><td width=130 align=CENTER>Cantidad</td></tr><tr><td width=130 align=CENTER>"+ txtIdItem +"</td><td width=130 align=CENTER>"+ txtCantidad +"</td></tr></table><br>"+btnItem;
		String grillaEnchant = "<table width=275><tr><td width=275 align=CENTER>"+ txtEnchant +"</td></tr></table><table width=275><tr><td width=130 align=CENTER>"+ btnEncAll +"</td><td width=130 align=CENTER>"+ btnEncW +"</td></tr></table><br>";
		String grillaAumento = "<table width=275><tr><td width=275 align=CENTER>ID AUMENTO</td></tr><tr><td width=275 align=CENTER>"+ txtIdaumento +"</td></tr></table><br>"+btnIDAumento;
		String grillaElemento = "<table width=275><tr><td width=130 align=CENTER>TIPO</td><td width=130 align=CENTER>VALOR</td></tr><tr><td width=130 align=CENTER>"+ cmbElemento +"</td><td width=130 align=CENTER>"+ txtElemento +"</td></tr></table><br>" + GRILLA1;
		String grillaSETPLUS = "<table width=275><tr><td width=275 align=CENTER>%BORRAR%</td></tr></table>"+btnSetPLUS;
		String grillaPunto = "<table width=275><tr><td width=275 align=CENTER>PUNTOS</td></tr><tr><td width=275 align=CENTER>"+ txtPuntosO +"</td></tr></table><br>"+btnPunt+btnGetPunt;

		if(Parametros!=null){
			//NOMPJ ACCION PARAMETRO CANTIDAD
			if(Parametros.length()>0){
				String[] param = Parametros.split(" ");
				L2PcInstance playerSELECT = null;
				try
				{
					playerSELECT  = L2World.getInstance().getPlayer(param[1]);
				}
				catch (Exception e)
				{
					central.msgbox("Nombre Player Incorrecto", player);
				}

				if (playerSELECT != null)
				{
					if(param[0].startsWith("ELEME_")){
						try{
							//$txtNombre $cmbTipEleme $txtElement
							int armorType = -1;
							byte element = Elementals.getElementId(param[2]);
							int valueEleme = Integer.valueOf(param[3]);
							if(param[0].endsWith("setlh")){
								armorType = Inventory.PAPERDOLL_HEAD;
							}else if(param[0].endsWith("setlc")){
								armorType = Inventory.PAPERDOLL_CHEST;
							}else if(param[0].endsWith("setlg")){
								armorType = Inventory.PAPERDOLL_GLOVES;
							}else if(param[0].endsWith("setlb")){
								armorType = Inventory.PAPERDOLL_FEET;
							}else if(param[0].endsWith("setll")){
								armorType = Inventory.PAPERDOLL_LEGS;
							}else if(param[0].endsWith("setlw")){
								armorType = Inventory.PAPERDOLL_RHAND;
							}
							if((armorType>0) && ((valueEleme >= 0) && (valueEleme <= 450)) ){
								setElement(playerSELECT, element, valueEleme, armorType);
							}
						}catch(Exception a){
							try{
								central.msgbox(a.getMessage(), player);
							}catch(Exception b){

							}
						}

					}else if(param[0].equals("LEVEL")){
						opera.setLevel(playerSELECT, param[2]);
					}else if(param[0].equals("EXP")){
						try{
							long tXp = Integer.valueOf(param[2]);
							playerSELECT.addExpAndSp(tXp, 0);
						}catch(Exception a){
							central.msgbox(a.getMessage(), player);
						}
					}else if(param[0].equals("SP")){
						try{
							int Sp = Integer.valueOf(param[2]);
							playerSELECT.addExpAndSp(0, Sp);
						}catch(Exception a){
							central.msgbox(a.getMessage(), player);
						}
					}else if(param[0].equals("DISCN")){
						try{
							if(playerSELECT.isOnline()){
								playerSELECT.getClient().closeNow();
							}
						}catch(Exception a){

						}
					}else if(param[0].equals("DISCNIP")){
						try{
							for(L2PcInstance playerForD : opera.getAllPlayerOnWorld()){
								if(playerForD.isOnline()){
									if(ZeuS.isDualBox_pc(playerForD, playerSELECT)){
										playerForD.getClient().closeNow();
									}
								}
							}
						}catch(Exception a){

						}
					}else if(param[0].equals("PUNTOS")){
						try{
							int pointGive = Integer.valueOf(param[2]);
							Participant par = new Participant(playerSELECT,1);
							olymp.point(par, pointGive,false);
						}catch(Exception a){

						}
					}else if(param[0].equals("GETPUNTOS")){
						try{
							if(playerSELECT.isOnline()){
								String Mensaje = "Puntos de " + playerSELECT.getName() +" = "+ olymp.getPuntos(playerSELECT);
								central.msgbox(Mensaje, player);
							}
						}catch(Exception a){

						}
					}else if(param[0].equals("ITEM")){
						try{
							int IDItem = Integer.valueOf(param[2]);
							int IDCantidad = Integer.valueOf(param[3]);
							opera.giveReward(playerSELECT, IDItem, IDCantidad,"SHOP",false);
							central.msgbox(String.valueOf(IDCantidad) + " " + central.getNombreITEMbyID(IDItem) + ": " + playerSELECT.getName() , player);
						}catch(Exception a){
							central.msgbox(a.getMessage(), player);
						}
					}else if(param[0].equals("ENC_ALL")){
						try{
							int Enc = Integer.valueOf(param[2]);
							special_enchant.setAllArmorEnchant(Enc, playerSELECT);
						}catch(Exception a){
							try{
								central.msgbox(a.getMessage(), player);
							}catch(Exception b){

							}
						}
					}else if(param[0].equals("SHOWALLZEUS")){
						if(VerTodoZP){
							VerTodoZP = false;
						}else{
							VerTodoZP = true;
						}
					}else if(param[0].equals("ENC_W")){
						try{
							int Enc = Integer.valueOf(param[2]);
							special_enchant.setWeaponEnchant(Enc, playerSELECT);
						}catch(Exception a){
							try{
								central.msgbox(a.getMessage(), player);
							}catch(Exception b){

							}
						}
					}else if(param[0].equals("AUMENTO")){
						try{
							special_augment.setAugment(playerSELECT,param[2],false);
						}catch(Exception a){
							try{
								central.msgbox(a.getMessage(), player);
							}catch(Exception b){

							}
						}
					}else if(param[0].equals("SETPLUS")){
						try{
							general.CHAR_PLUS.add(param[1]);
							general.CHAR_PLUS_IP.put(param[1], ZeuS.getIp_Wan(playerSELECT));
						}catch(Exception a){

						}
					}else if(param[0].equals("REMPLUS")){
						try{
							general.CHAR_PLUS.remove(param[1]);
							general.CHAR_PLUS_IP.remove(param[1]);
						}catch(Exception a){

						}
					}else if(param[0].equals("GETADMINC")){
						try{
							getWindowsIPMayor(player);
						}catch(Exception a){

						}
					}
				}
			}
		}

		if(general.CHAR_PLUS != null){
			if(general.CHAR_PLUS.size()<=0){
				grillaSETPLUS = grillaSETPLUS.replace("%BORRAR%", "");
			}else{
				String BorrarName = "";
				for(String NomCha : general.CHAR_PLUS){
					if(BorrarName.length()>0){
						BorrarName +=";";
					}
					BorrarName += NomCha;
				}
				if(BorrarName.length()>0){
					cmbBorrar = cmbBorrar.replace("%LISTA%", BorrarName);
					cmbBorrar += "<br1>" + btnRemPLUS;
					grillaSETPLUS = grillaSETPLUS.replace("%BORRAR%", cmbBorrar);
				}
			}
		}else{
			grillaSETPLUS = grillaSETPLUS.replace("%BORRAR%", "");
		}

		boolean OwnerZ = false;

		if(general.CHAR_PLUS!=null){
			if(!general.CHAR_PLUS.contains(player.getName())){
				OwnerZ = true;
			}
		}

		String MAIN_HTML ="<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("ZeuS Plus")  + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Nombre Char<br1>" + txtNombre+"<br1>"+ btnGetGM + ( OwnerZ ? btnMostrarTodoZeuS : "" ) ) + central.LineaDivisora(1);

		if(OwnerZ){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Full Hijos = " + ( VerTodoZP ? "SI" : "NO" ) ) + central.LineaDivisora(1);
		}

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(grillaLevel) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(grillaExp) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(grillaSp) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("ITEM<br1>" + grillaItem) + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("ENCHANT<br1>" + grillaEnchant)+central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("AUMENTO<br1>" + grillaAumento)+central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("ELEMENTAR<br1>" + grillaElemento)+central.LineaDivisora(1);
		if(OwnerZ || VerTodoZP){
			String html2 = "<table width=280><tr><td width=140 align=CENTER>"+ btnDisconnect +"</td><td width=140 align=CENTER>"+ btnDisconnectAllIP +"</td></tr></table>";
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("SET PLUS<br1>" + grillaSETPLUS)+central.LineaDivisora(1);
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat( html2 ) + central.LineaDivisora(1);
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat(grillaPunto) + central.LineaDivisora(1);
		}

		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(1);
		MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
		central.sendHtml(player, MAIN_HTML);
	}


	public static String getInfoContacto(){
		String MAIN_HTML ="<html><title>" + general.TITULO_NPC() + "</title><body>";

		String Caracteristicas = "The best engine for your L2Jserver, with more than 50 options and unique features. For more information contact the developer Email adove or follow us on our facebook<br1>http://facebook.com/groups/ZeuSAIO";

		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("About ZeuS AIO NPC")  + central.LineaDivisora(3);

		MAIN_HTML += central.LineaDivisora(3) + central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += central.headFormat("Created by Jabberwock 2013<br1>"+general.EMAIL+"<br1>" + Caracteristicas + "<br>v.: " + general.VERSION);

		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + central.LineaDivisora(3);

		MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	public static String ErrorTipeoEspacio(boolean ShowBackBtn){
		String HTML_STR = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error") + central.LineaDivisora(2);
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error de Tipeo. Ingrese los Datos Solicitados<br1>Correctamente.<br1>Typing error. Enter <br1> Properly Requested Data.<br>Vuelva a intentar<br>Try Again","B40404") + central.LineaDivisora(2);
		if(ShowBackBtn) {
			HTML_STR += central.BotonGOBACKZEUS();
		}
		HTML_STR += central.getPieHTML() +  "</body></html>";
		return HTML_STR;
	}

	public static String ErrorTipeoEspacio(){
		return ErrorTipeoEspacio(true);
	}

	public static String ErrorTipeoEspacio_Admin(){
		String HTML_STR = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error") + central.LineaDivisora(2);
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error de Tipeo. Ingrese los Datos Solicitados<br1>Correctamente.<br1>Typing error. Enter <br1> Properly Requested Data.<br>Vuelva a intentar<br>Try Again","B40404") + central.LineaDivisora(2);
		HTML_STR += menu.getBtnbackConfig() + central.getPieHTML() +  "</body></html>";
		return HTML_STR;
	}




	public static String CrearFilaDonacion(String Boton1IN, String Boton2IN, String npcid){
		String Boton1[] = Boton1IN.split(",");
		String Boton2[] = Boton2IN.split(",");

		int IDPINTAR = central.INT_PINTAR_GRILLA(0);

		String COLOR_PINTA[] = {"LEVEL","00FFFF"};

		String BOTON_GENE_1,BOTON_GENE_2;
		if( central.isNumeric(Boton1[2])) {
			BOTON_GENE_1 = "<button value=\""+Boton1[1]+"\" action=\"bypass -h npc_"+npcid+"_multisell "+Boton1[2] + " \" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else {
			BOTON_GENE_1 = "<button value=\""+Boton1[1]+"\" action=\"bypass -h ZeuSNPC "+ Boton1[2] + " \" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}

		if (central.isNumeric(Boton2[2])) {
			BOTON_GENE_2 = "<button value=\""+Boton2[1]+"\" action=\"bypass -h npc_"+npcid+"_multisell "+Boton2[2] + "\" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else {
			BOTON_GENE_2 = "<button value=\""+Boton2[1]+"\" action=\"bypass -h ZeuSNPC "+ Boton2[2] + " \" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}

		String RETORNO = "<table width = 280 bgcolor=151515>";
		RETORNO += "<tr><td width=140 align=CENTER><font color=\""+COLOR_PINTA[IDPINTAR]+"\">"+Boton1[0]+" D. Coin</font></td><td width=140 align=CENTER><font color=\""+COLOR_PINTA[IDPINTAR]+"\">"+Boton2[0]+" D. Coin</font></td></tr>";
		RETORNO += "<tr><td width=140 align=CENTER>"+BOTON_GENE_1+"</td><td width=140 align=CENTER>"+BOTON_GENE_2+"</td></tr>";
		RETORNO += "</table>"+central.LineaDivisora(1);
		return RETORNO;
	}

	private static String showClasicMenu(boolean cn, boolean ch, String npcid){
		return showClasicMenu(cn, ch, npcid, false, null);
	}

	private static String showClasicMenu(boolean cn, boolean ch, String npcid, boolean cbe, L2PcInstance player){
		String btnHeight = cbe ? "35" : "21";
		String btnWidth = cbe ? "150" : "110";
		Vector <String> Btns = new Vector<String>();

		String MAIN_HTML = "";

		boolean escribirOtros = true;

		if(player != null) {
			if(!opera.isMaster(player) && general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM){
				if(!opera.haveItem(player, general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID, 1,false)){
					if(!opera.haveItem(player, general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID,1,false)){
						escribirOtros = false;
					}
				}
			}
		}

		if(cbe && general.BTN_SHOW_VOTE_CBE){
			String btnVoto = "<button value=\"Vote\" action=\"bypass -h ZeuSNPC VoteReward 0 0 0\" width="+ btnWidth +" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			Btns.add(btnVoto);
		}

		if(escribirOtros){
				if ((general.BTN_SHOW_BUFFER && cn) || (general.BTN_SHOW_BUFFER_CH && ch) || (general.BTN_SHOW_BUFFER_CBE && cbe)) {
					Btns.add("<button value=\"Buffer\" action=\"bypass -h ZeuSNPC deluxbuffer 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}

				if ((general.BTN_SHOW_TELEPORT && cn) || (general.BTN_SHOW_TELEPORT_CH && ch)|| (general.BTN_SHOW_TELEPORT_CBE && cbe)) {
					if(!general.TELEPORT_BD){
						Btns.add("<button value=\"Teleport\" action=\"bypass -h npc_"+npcid+"_Link teleporter/AIONpc-GK/teleports.htm\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					}else{
						Btns.add("<button value=\"Teleport\" action=\"bypass -h ZeuSNPC teleportMain 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					}
				}
				if ((general.BTN_SHOW_SHOP && cn) || (general.BTN_SHOW_SHOP_CH && ch) || (general.BTN_SHOW_SHOP_CBE && cbe)) {
					if(!general.SHOP_USE_BD) {
						Btns.add("<button value=\"Shop\" action=\"bypass -h npc_"+npcid+"_Link merchant/AIONPC/955.htm\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					} else {
						Btns.add("<button value=\"Shop\" action=\"bypass -h ZeuSNPC shopBD 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
					}
				}
				if ((general.BTN_SHOW_WAREHOUSE && cn) || (general.BTN_SHOW_WAREHOUSE_CH && ch) || (general.BTN_SHOW_WAREHOUSE_CBE && cbe)) {
					Btns.add("<button value=\"Warehouse\" action=\"bypass -h ZeuSNPC chat1 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_AUGMENT && cn) || (general.BTN_SHOW_AUGMENT_CH && ch) || (general.BTN_SHOW_AUGMENT_CBE && cbe)) {
					Btns.add("<button value=\"Augment\" action=\"bypass -h ZeuSNPC AUGMENTMNU "+npcid+" 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_SUBCLASES && cn) || (general.BTN_SHOW_SUBCLASES_CH && ch) || (general.BTN_SHOW_SUBCLASES_CBE && cbe)) {
					Btns.add("<button value=\"Sub Class\" action=\"bypass -h ZeuSNPC chat3 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_CLASS_TRANSFER && cn) || (general.BTN_SHOW_CLASS_TRANSFER_CH && ch) || (general.BTN_SHOW_CLASS_TRANSFER_CBE && cbe)) {
					Btns.add("<button value=\"Class Transfer\" action=\"bypass -h ZeuSNPC TranferMenu 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_CONFIG_PANEL && cn) || (general.BTN_SHOW_CONFIG_PANEL_CH && ch) || (general.BTN_SHOW_CONFIG_PANEL_CBE && cbe)) {
					Btns.add("<button value=\"Config Panel\" action=\"bypass -h ZeuSNPC ConfigPanel 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_DROP_SEARCH && cn) || (general.BTN_SHOW_DROP_SEARCH_CH && ch) || (general.BTN_SHOW_DROP_SEARCH_CBE && cbe)) {
					Btns.add("<button value=\"Drop S.\" action=\"bypass -h ZeuSNPC DropSearch 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_PVPPK_LIST && cn) || (general.BTN_SHOW_PVPPK_LIST_CH && ch) || (general.BTN_SHOW_PVPPK_LIST_CBE && cbe)) {
					Btns.add("<button value=\"PvP / PK\" action=\"bypass -h ZeuSNPC PKlistoption 1 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_LOG_PELEAS && cn) || (general.BTN_SHOW_LOG_PELEAS_CH && ch) || (general.BTN_SHOW_LOG_PELEAS_CBE && cbe)) {
					Btns.add("<button value=\"Log PvP / PK\" action=\"bypass -h ZeuSNPC logpeleas 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_CASTLE_MANAGER && cn) || (general.BTN_SHOW_CASTLE_MANAGER_CH && ch) || (general.BTN_SHOW_CASTLE_MANAGER_CBE && cbe)) {
					Btns.add("<button value=\"Castle Manager\" action=\"bypass -h ZeuSNPC CastleManagerStr 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_DESAFIO && cn) || (general.BTN_SHOW_DESAFIO_CH && ch) || (general.BTN_SHOW_DESAFIO_CBE && cbe)) {
					Btns.add("<button value=\"The Challenge\" action=\"bypass -h ZeuSNPC DESAFIO 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_SYMBOL_MARKET && cn) || (general.BTN_SHOW_SYMBOL_MARKET_CH && ch) || (general.BTN_SHOW_SYMBOL_MARKET_CBE && cbe)) {
					Btns.add("<button value=\"Symbol Maker\" action=\"bypass -h ZeuSNPC chat6 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_CLANALLY && cn) || (general.BTN_SHOW_CLANALLY_CH && ch) || (general.BTN_SHOW_CLANALLY_CBE && cbe)) {
					Btns.add("<button value=\"Clan & Ally\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_PARTYFINDER && cn) || (general.BTN_SHOW_PARTYFINDER_CH && ch) || (general.BTN_SHOW_PARTYFINDER_CBE && cbe)) {
					Btns.add("<button value=\"Go Party L.\" action=\"bypass -h ZeuSNPC ptfinder 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_FLAGFINDER && cn) || (general.BTN_SHOW_FLAGFINDER_CH && ch) || (general.BTN_SHOW_FLAGFINDER_CBE && cbe)) {
					Btns.add("<button value=\"Go Flag\" action=\"bypass -h ZeuSNPC fgfinder 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_COLORNAME && cn) || (general.BTN_SHOW_COLORNAME_CH && ch) || (general.BTN_SHOW_COLORNAME_CBE && cbe)) {
					Btns.add("<button value=\"Color Name\" action=\"bypass -h ZeuSNPC Colormenuu 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_DELEVEL && cn) || (general.BTN_SHOW_DELEVEL_CH && ch) || (general.BTN_SHOW_DELEVEL_CBE && cbe)) {
					Btns.add("<button value=\"Delevel\" action=\"bypass -h ZeuSNPC DellvlMenu 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_GRAND_BOSS_STATUS && cn) || (general.BTN_SHOW_GRAND_BOSS_STATUS_CH && ch) || (general.BTN_SHOW_GRAND_BOSS_STATUS_CBE && cbe)){
					Btns.add("<button value=\"G. Boss Spawn\" action=\"bypass -h ZeuSNPC showGrandBoss 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_RAIDBOSS_INFO && cn) || (general.BTN_SHOW_RAIDBOSS_INFO_CH && ch) || (general.BTN_SHOW_RAIDBOSS_INFO_CBE && cbe)){
					Btns.add("<button value=\"Raid. B. Spawn\" action=\"bypass -h ZeuSNPC RaidBossInf 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_REMOVE_ATRIBUTE && cn) || (general.BTN_SHOW_REMOVE_ATRIBUTE_CH && ch) || (general.BTN_SHOW_REMOVE_ATRIBUTE_CBE && cbe)) {
					Btns.add("<button value=\"S. Attribute\" action=\"bypass -h ZeuSNPC ReleaseAttribute 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_BUG_REPORT && cn) || (general.BTN_SHOW_BUG_REPORT_CH && ch) || (general.BTN_SHOW_BUG_REPORT_CBE && cbe)) {
					Btns.add("<button value=\"Bug Report\" action=\"bypass -h ZeuSNPC MENUBUFREPORT 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if((general.BTN_SHOW_CAMBIO_NOMBRE_PJ && cn) || (general.BTN_SHOW_CAMBIO_NOMBRE_PJ_CH && ch) || (general.BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE && cbe)) {
					Btns.add("<button value=\"C.Nombre pj\" action=\"bypass -h ZeuSNPC USER_CHANGE_NAME 1 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if((general.BTN_SHOW_CAMBIO_NOMBRE_CLAN && cn) || (general.BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH && ch) || (general.BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE && cbe)) {
					Btns.add("<button value=\"C. Nom. Clan\" action=\"bypass -h ZeuSNPC USER_CHANGE_NAME 2 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if((general.BTN_SHOW_VARIAS_OPCIONES && cn) || (general.BTN_SHOW_VARIAS_OPCIONES_CH && ch) || (general.BTN_SHOW_VARIAS_OPCIONES_CBE && cbe)) {
					Btns.add("<button value=\"Miscellaneous\" action=\"bypass -h ZeuSNPC OPCIONESVAR 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if((general.BTN_SHOW_TRANSFORMATION && cn) || (general.BTN_SHOW_TRANSFORMATION_CH && ch) || (general.BTN_SHOW_TRANSFORMATION_CBE && cbe)){
					Btns.add("<button value=\"Transfor\" action=\"bypass -h ZeuSNPC transform 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if((general.BTN_SHOW_ELEMENT_ENHANCED && cn) || (general.BTN_SHOW_ELEMENT_ENHANCED_CH && ch) || (general.BTN_SHOW_ELEMENT_ENHANCED_CBE && cbe)) {
					Btns.add("<button value=\"Elemental It.\" action=\"bypass -h ZeuSNPC ELEMENTAL 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if((general.BTN_SHOW_ENCANTAMIENTO_ITEM && cn) || (general.BTN_SHOW_ENCANTAMIENTO_ITEM_CH && ch) || (general.BTN_SHOW_ENCANTAMIENTO_ITEM_CBE && cbe)) {
					Btns.add("<button value=\"Enchant Item\" action=\"bypass -h ZeuSNPC ENCHANTITEM 0 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if((general.BTN_SHOW_AUGMENT_SPECIAL && cn) || (general.BTN_SHOW_AUGMENT_SPECIAL_CH && ch) || (general.BTN_SHOW_AUGMENT_SPECIAL_CBE && cbe)) {
					Btns.add("<button value=\"Augment Special\" action=\"bypass -h ZeuSNPC AUGMENTSP 0 -1 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
				if ((general.BTN_SHOW_DONATION && cn) || (general.BTN_SHOW_DONATION_CH && ch) || (general.BTN_SHOW_DONATION_CBE && cbe)) {
					Btns.add("<button value=\"Donation\" action=\"bypass -h ZeuSNPC MenuDonation "+npcid+" 0 0\" width="+btnWidth+" height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
				}
		}

		if(Btns.size()>0){
			if(!cbe){
				int cont=0;
				String bt1="", bt2="";
				for(String boton : Btns){
					if(cont==0){
						bt1 = boton;
						cont=1;
					}else{
						bt2 = boton;
						MAIN_HTML += central.BotonClasicCentral(bt1, bt2);
						bt1="";
						bt2="";
						cont=0;
					}
				}
				if((bt1.length()>0) && (bt2.length()==0)){
					MAIN_HTML += central.BotonClasicCentral(bt1, "");
				}
			}else{
				MAIN_HTML = "<table width=700 align=CENTER>";
				int Contador = 0;
				for(String btnSep : Btns){
					if(Contador==0){
						MAIN_HTML += "<tr>";
					}
					MAIN_HTML += "<td width=230 align=CENTER>"+ btnSep +"</td>";
					Contador++;
					if((Contador%3) == 0){
						Contador = 0;
						MAIN_HTML += "</tr>";
					}
				}
				if(Contador!=0){
					for(int i=Contador;i<3;i++){
						MAIN_HTML += "<td width=230 align=CENTER></td>";
					}
					MAIN_HTML += "</tr>";
				}
				MAIN_HTML += "</table>";
			}
		}else{
			if(Btns.size()==1){
				MAIN_HTML = "<table width=700 align=CENTER><tr>"+
							"<td width=700 align=CENTER>"+ Btns.get(0) +"</td></tr>";
			}
		}
		return MAIN_HTML;
	}





	private static String showNewMenu(boolean cn, boolean ch, String npcid){

		String btnHeight = "21";

		String MAIN_HTML = "";
		if ((general.BTN_SHOW_BUFFER && cn) || (general.BTN_SHOW_BUFFER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Buffer\" action=\"bypass -h ZeuSNPC deluxbuffer 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_BUFFER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_TELEPORT && cn) || (general.BTN_SHOW_TELEPORT_CH && ch)) {
			if(!general.TELEPORT_BD){
				MAIN_HTML += central.BotonCentral("<button value=\"Teleport\" action=\"bypass -h npc_"+npcid+"_Link teleporter/AIONpc-GK/teleports.htm\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_TELEPORT,central.INT_PINTAR_GRILLA(0));
			}else{
				MAIN_HTML += central.BotonCentral("<button value=\"Teleport\" action=\"bypass -h ZeuSNPC teleportMain 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_TELEPORT,central.INT_PINTAR_GRILLA(0));
			}
		}
		if ((general.BTN_SHOW_SHOP && cn) || (general.BTN_SHOW_SHOP_CH && ch)) {
			if(!general.SHOP_USE_BD) {
				MAIN_HTML += central.BotonCentral("<button value=\"Shop\" action=\"bypass -h npc_"+npcid+"_Link merchant/AIONPC/955.htm\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_SHOP ,central.INT_PINTAR_GRILLA(0));
			} else {
				MAIN_HTML += central.BotonCentral("<button value=\"Shop\" action=\"bypass -h ZeuSNPC shopBD 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_SHOP ,central.INT_PINTAR_GRILLA(0));
			}
		}
		if ((general.BTN_SHOW_WAREHOUSE && cn) || (general.BTN_SHOW_WAREHOUSE_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Warehouse\" action=\"bypass -h ZeuSNPC chat1 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_WAREHOUSE ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_AUGMENT && cn) || (general.BTN_SHOW_AUGMENT_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Augment\" action=\"bypass -h ZeuSNPC AUGMENTMNU "+npcid+" 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_AUGMENT ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_SUBCLASES && cn) || (general.BTN_SHOW_SUBCLASES_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Sub Class\" action=\"bypass -h ZeuSNPC chat3 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_SUBCLASES,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CLASS_TRANSFER && cn) || (general.BTN_SHOW_CLASS_TRANSFER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Class Transfer\" action=\"bypass -h ZeuSNPC TranferMenu 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_CLASS_TRANSFER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CONFIG_PANEL && cn) || (general.BTN_SHOW_CONFIG_PANEL_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Config Panel\" action=\"bypass -h ZeuSNPC ConfigPanel 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_CONFIG_PANEL ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DROP_SEARCH && cn) || (general.BTN_SHOW_DROP_SEARCH_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Drop S.\" action=\"bypass -h ZeuSNPC DropSearch 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_DROP_SEARCH,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_PVPPK_LIST && cn) || (general.BTN_SHOW_PVPPK_LIST_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"PvP / PK\" action=\"bypass -h ZeuSNPC PKlistoption 1 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_PVPPK_LIST,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_LOG_PELEAS && cn) || (general.BTN_SHOW_LOG_PELEAS_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Log PvP / PK\" action=\"bypass -h ZeuSNPC logpeleas 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_LOG_PELEAS,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CASTLE_MANAGER && cn) || (general.BTN_SHOW_CASTLE_MANAGER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Castle Manager\" action=\"bypass -h ZeuSNPC CastleManagerStr 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_CASTLE_MANAGER,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DESAFIO && cn) || (general.BTN_SHOW_DESAFIO_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"The Challenge\" action=\"bypass -h ZeuSNPC DESAFIO 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_DESAFIO ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_SYMBOL_MARKET && cn) || (general.BTN_SHOW_SYMBOL_MARKET_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Symbol Maker\" action=\"bypass -h ZeuSNPC chat6 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_SYMBOL_MARKET ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CLANALLY && cn) || (general.BTN_SHOW_CLANALLY_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Clan & Ally\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_VARIAS_OPCIONES ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_PARTYFINDER && cn) || (general.BTN_SHOW_PARTYFINDER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Go Party L.\" action=\"bypass -h ZeuSNPC ptfinder 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_PARTYFINDER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_FLAGFINDER && cn) || (general.BTN_SHOW_FLAGFINDER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Go Flag\" action=\"bypass -h ZeuSNPC fgfinder 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_FLAGFINDER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_COLORNAME && cn) || (general.BTN_SHOW_COLORNAME_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Color Name\" action=\"bypass -h ZeuSNPC Colormenuu 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_COLORNAME ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DELEVEL && cn) || (general.BTN_SHOW_DELEVEL_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Delevel\" action=\"bypass -h ZeuSNPC DellvlMenu 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_DELEVEL,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_GRAND_BOSS_STATUS && cn) || (general.BTN_SHOW_GRAND_BOSS_STATUS_CH && ch)){
			MAIN_HTML += central.BotonCentral("<button value=\"G. Boss Spawn\" action=\"bypass -h ZeuSNPC showGrandBoss 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_RAIDBOSS_INFO && cn) || (general.BTN_SHOW_RAIDBOSS_INFO_CH && ch)){
			MAIN_HTML += central.BotonCentral("<button value=\"Raid. B. Spawn\" action=\"bypass -h ZeuSNPC RaidBossInf 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_RAIDBOSS_INFO,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_REMOVE_ATRIBUTE && cn) || (general.BTN_SHOW_REMOVE_ATRIBUTE_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"S. Attribute\" action=\"bypass -h ZeuSNPC ReleaseAttribute 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_BUG_REPORT && cn) || (general.BTN_SHOW_BUG_REPORT_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Bug Report\" action=\"bypass -h ZeuSNPC MENUBUFREPORT 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_BUG_REPORT,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_CAMBIO_NOMBRE_PJ && cn) || (general.BTN_SHOW_CAMBIO_NOMBRE_PJ_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"C.Nombre pj\" action=\"bypass -h ZeuSNPC USER_CHANGE_NAME 1 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_CAMBIO_NOMBRE_CLAN && cn) || (general.BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"C. Nom. Clan\" action=\"bypass -h ZeuSNPC USER_CHANGE_NAME 2 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_VARIAS_OPCIONES && cn) || (general.BTN_SHOW_VARIAS_OPCIONES_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Miscellaneous\" action=\"bypass -h ZeuSNPC OPCIONESVAR 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_VARIAS_OPCIONES ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_TRANSFORMATION && cn) || (general.BTN_SHOW_TRANSFORMATION_CH && ch)){
			MAIN_HTML += central.BotonCentral("<button value=\"Transfor\" action=\"bypass -h ZeuSNPC transform 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_TRANSFORMATION ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_ELEMENT_ENHANCED && cn) || (general.BTN_SHOW_ELEMENT_ENHANCED_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Elemental It.\" action=\"bypass -h ZeuSNPC ELEMENTAL 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_ELEMENT_ENHANCED,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_ENCANTAMIENTO_ITEM && cn) || (general.BTN_SHOW_ENCANTAMIENTO_ITEM_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Enchant Item\" action=\"bypass -h ZeuSNPC ENCHANTITEM 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_AUGMENT_SPECIAL && cn) || (general.BTN_SHOW_AUGMENT_SPECIAL_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Augment Special\" action=\"bypass -h ZeuSNPC AUGMENTSP 0 -1 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_AUGMENT_SPECIAL ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DONATION && cn) || (general.BTN_SHOW_DONATION_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Donation\" action=\"bypass -h ZeuSNPC MenuDonation "+npcid+" 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_DONATION ,central.INT_PINTAR_GRILLA(0));
		}

		return MAIN_HTML;
	}


	public static String get_CB_Main_Btn(L2PcInstance cha, String params){
		return showClasicMenu(false, false, String.valueOf(general.npcGlobal(cha,true)), true, cha);
	}


	public static String firtsHTML(L2PcInstance st, String npcid){
		general.IS_USING_NPC.put(st.getObjectId(),true);
		general.IS_USING_CB.put(st.getObjectId(),false);
		String MAIN_HTML;
		if(!general._activated()){
			return "NO LEGAL";
		}
		if (!opera.isActivePIN(st)){
			MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("") + central.LineaDivisora(2);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Debes Ingresar tu PIN","DF0101") + central.LineaDivisora(2);
			MAIN_HTML += "</body></html>";
			return MAIN_HTML;
		}
		String BtnAdmin ="";
		/*
		if(opera.isMaster(st)){
			BtnAdmin = "<table width=280><tr><td width=85>"
					+ "<button value=\"Load\" action=\"bypass -h ZeuSNPC Config 1 0 0\" width=85 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
					+ "</td><td width=110 align=center>"+msg.MENSAJE_BIENVENIDA+"</td>"
					+"<td width=85><button value=\"Config\" action=\"bypass -h ZeuSNPC Config 2 0 0\" width=85 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		}*/
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2);
		MAIN_HTML += central.headFormat(BtnAdmin) + central.LineaDivisora(2);

		boolean cn, ch;
		cn = isZeuSALL(st);
		ch = isZeuSCH(st);

		//MAIN_HTML += "<table width=280 border=0 bgcolor=151515>";

		String btnHeight = "21";

		boolean canShowOther = true;

		if(!opera.isMaster(st) && general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM){
			if(!opera.haveItem(st, general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID, 1,false)){
				if(!opera.haveItem(st, general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID,1,false)){
					canShowOther = false;
				}
			}
		}


		if ((general.BTN_SHOW_VOTE && cn) || (general.BTN_SHOW_VOTE_CH && ch)) {
			String btnVoto = "<button value=\"Vote\" action=\"bypass -h ZeuSNPC VoteReward 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			if(general.SHOW_NEW_MAIN_WINDOWS){
				MAIN_HTML += central.BotonCentral(btnVoto,general.BTN_SHOW_EXPLICA_VOTE ,central.INT_PINTAR_GRILLA(0));
			}else{
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnVoto) + central.LineaDivisora(1);
			}
		}
		if(canShowOther){
			if(general.SHOW_NEW_MAIN_WINDOWS){
				MAIN_HTML += showNewMenu(cn,ch,npcid);
			}else{
				MAIN_HTML += showClasicMenu(cn,ch,npcid);
			}
		}else{
			String Mensaje = "To use ZeuS, you must have " + central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID) + ", which you get Voting";

			if(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM){
				Mensaje += " or with " + central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID) + " that you can buy for: " + central.ItemNeedShow_line(general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE) ;
			}else{
				Mensaje +=".";
			}
			//+ central.ItemNeedShowBox(general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE)
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat(Mensaje,"LEVEL") + central.LineaDivisora(1);
		}
		MAIN_HTML += central.getPieHTML();
		MAIN_HTML += "</center></body></html>";
		return MAIN_HTML;
	}


	public static String DesafioVerPremios(L2PcInstance st){
		String BOTON_ATRAS = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DESAFIO 0 0 0\" width=50 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Reward Hidden NPC") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON_ATRAS,"") + central.LineaDivisora(2);
		int IDPremioCAP = -1;
		String HTML_MIENTRAS = "";
		String COLORES_FONDO[] = {"4B610B","5E610B"};
		String COLOR_TITULO = "0A2229";
		String INFOFAMILIA ="";
		try{
		Connection conn = ConnectionFactory.getInstance().getConnection();
		String qry = "call sp_npc_evento_operaciones(2,1,1,1)";
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();
		int ID_ITEM;
		while (rss.next()){
			if (!rss.getString(1).equals("err")){
				if (IDPremioCAP != rss.getInt(2)){
					IDPremioCAP = rss.getInt(2);
					INFOFAMILIA = "<table width = 280 bgcolor="+COLOR_TITULO+"><tr><td align=CENTER width = 200>Family: "+String.valueOf(rss.getInt(2))+" - Given: " + rss.getString(5) + "</td></tr></table>";
					if (HTML_MIENTRAS.length()==0) {
						HTML_MIENTRAS = INFOFAMILIA + "<table width=280 bgcolor="+COLORES_FONDO[central.INT_PINTAR_GRILLA(-1)]+">";
					} else {
						HTML_MIENTRAS += "</table>"+central.LineaDivisora(2)+"<br>"+INFOFAMILIA+"<table width = 280 bgcolor="+COLORES_FONDO[central.INT_PINTAR_GRILLA(-1)]+">";
					}
				}
				ID_ITEM = rss.getInt(3);
				HTML_MIENTRAS += "<tr><td width=280 align=center>" + central.getNombreITEMbyID(ID_ITEM) + "("+ String.valueOf(rss.getInt(4)) +")</td></tr>";
			}
		}
		conn.close();
		}catch(SQLException e){

		}
		if(HTML_MIENTRAS.length()>0) {
			MAIN_HTML += HTML_MIENTRAS + "</table>" + central.LineaDivisora(2);
		}
		MAIN_HTML += central.getPieHTML() + "</body></html>";
		return MAIN_HTML;

	}













	public static String MainMenuAug(String npcid){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Augment / Deaugment") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Augment\" action=\"bypass -h npc_"+npcid+"_Augment 1\" width=192 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Remove Augment\" action=\"bypass -h npc_"+npcid+"_Augment 2\"  width=192 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>" + central.BotonGOBACKZEUS() + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}




	public static String MainHtmlCastleManager(L2PcInstance st){

		String[] Fee = central.getDateCastleRegEnd(st);

		String MAIN_HTM = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTM += central.LineaDivisora(2) + central.headFormat("Castle Manager") + central.LineaDivisora(2);
		MAIN_HTM += central.LineaDivisora(2) + central.headFormat("Choose the Castle","LEVEL") + central.LineaDivisora(2);

		if (general.GIRAN){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Giran Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 3 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[2]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.ADEN){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Aden Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 5 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[4]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.RUNE){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Rune Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 8 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[7]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.OREN){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Oren Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 4 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[3]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.DION){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Dion Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 2 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[1]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.GLUDIO){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Gludio Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 1 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[0]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.GODDARD){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Goddard Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 7 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[6]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.SCHUTTGART){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Schuttgart Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 9 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[8]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.INNADRIL){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Innadril Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 6 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[5]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		MAIN_HTM += central.BotonGOBACKZEUS() + central.getPieHTML() + "</center><br></body></html>";
		return MAIN_HTM	;
	}


	public static String MainHtml1(L2PcInstance st){
		String idObjetoNPC = general.npcGlobal(st);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Warehouse") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Private Warehouse","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Deposit Item\" action=\"bypass -h npc_"+idObjetoNPC+"_DepositP\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Withdraw Item\" action=\"bypass -h npc_"+idObjetoNPC+"_WithdrawP\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += central.LineaDivisora(2) + "<br1>" + central.LineaDivisora(2) + central.headFormat("Clan Warehouse","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Deposit Item\" action=\"bypass -h ZeuSNPC warehouse DepositC 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Withdraw Item\" action=\"bypass -h ZeuSNPC warehouse WithdrawC 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<br><br><br>" + central.BotonGOBACKZEUS() + "<br><font color=\"303030\"></font>";
		MAIN_HTML += "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml2(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>";
		MAIN_HTML += "<font color=\"FF0000\">.::Skill Enchant Options::.</font><br><br1>";
		MAIN_HTML += "<button value=\"Learn Skill\" action=\"bypass -h ZeuSNPC SkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Skill enchant\" action=\"bypass -h ZeuSNPC EnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Safe enchant\" action=\"bypass -h ZeuSNPC SafeEnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Untrain skills\" action=\"bypass -h ZeuSNPC UntrainEnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Change routes\" action=\"bypass -h ZeuSNPC ChangeEnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<br><font color=\"303030\"></font>";
		MAIN_HTML += "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml3(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("SubClass Master Options") + central.LineaDivisora(2);
		if (st.getTotalSubClasses() < Config.MAX_SUBCLASS) {
			MAIN_HTML += "<button value=\"Add Subclass\" action=\"bypass -h ZeuSNPC subclass addsub 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		}
		MAIN_HTML += "<button value=\"Change Subclass\" action=\"bypass -h ZeuSNPC subclass changesub 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Remove Subclass\" action=\"bypass -h ZeuSNPC subclass deletesub 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>" + central.BotonGOBACKZEUS() + "<br><font color=\"303030\"></font>"	;
		MAIN_HTML += "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml4(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Clan") + central.LineaDivisora(2);
		if(st.getClan() == null) {
			MAIN_HTML += "<button value=\"Create New Clan\" action=\"bypass -h ZeuSNPC createclan 0 0 0\" width=150 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else{
			MAIN_HTML += "<button value=\"Delegate Clan Leader\" action=\"bypass -h ZeuSNPC giveclanl 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Increase Clan Level\" action=\"bypass -h ZeuSNPC increaseclan 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Disband Clan\" action=\"bypass -h ZeuSNPC DisbandClan 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Restore Clan\" action=\"bypass -h ZeuSNPC RestoreClan 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Acquire Clan Skill\" action=\"bypass -h ZeuSNPC learn_clan_skills 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Alliance Options") + central.LineaDivisora(2);
			L2Clan clan = st.getClan();
			if(clan!=null){
				if (clan.getAllyId() == 0) {
					MAIN_HTML += "<button value=\"Create a Alliance\" action=\"bypass -h ZeuSNPC createally 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
				} else {
					MAIN_HTML += "<button value=\"Dissolve Alliance\" action=\"bypass -h ZeuSNPC dissolve_ally 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
				}
			}
		}
		MAIN_HTML += "<br><font color=\"303030\"></font>";
		MAIN_HTML += "<br><br>" + central.BotonGOBACKZEUS() + central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml5(L2PcInstance st, String npcid){
		String MAIN_HTML = "<html><head><title>" + general.TITULO_NPC() + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>";
		MAIN_HTML += "<font color=\"303030\"></font><br>";
		MAIN_HTML += "<table width=260 border=0 bgcolor=444444>";
		MAIN_HTML += "<tr><td align=\"center\"><font color=\"LEVEL\">Confirmation</font></td></tr></table><br>";
		MAIN_HTML += "<img src=\"L2UI.SquareGray\" width=250 height=1><br>";
		MAIN_HTML += "<table width=260 border=0 bgcolor=444444>";
		MAIN_HTML += "<tr><td><br></td></tr>";
		MAIN_HTML += "<tr><td align=\"center\"><font color=\"FF0000\">This option can be seen by GMs only and it<br1>allow to update any changes made in the<br1>script. You can disable this option in<br1>the settings section within the Script.<br><font color=\"LEVEL\">Do you want to update the SCRIPT?</font></font></td></tr>";
		MAIN_HTML += "<tr><td></td></tr></table><br>";
		MAIN_HTML += "<img src=\"L2UI.SquareGray\" width=250 height=1><br><br>";
		MAIN_HTML += "<button value=\"Yes\" action=\"bypass -h ZeuSNPC reloadscript 1 "+npcid+" 0\" width=50 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"No\" action=\"bypass -h ZeuSNPC reloadscript 0 "+npcid+" 0\" width=50 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "</center></body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml6(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Symbol Maker") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Draw a Symbol\" action=\"bypass -h ZeuSNPC symbol draws 0 0\" width=200 height=31 back=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\">";
		MAIN_HTML += "<button value=\"Delete a Symbol\" action=\"bypass -h ZeuSNPC symbol deletes 0 0\" width=200 height=31 back=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\">";
		MAIN_HTML += "<br><font color=\"303030\"></font>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>" + central.BotonGOBACKZEUS() + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}


	public static String MainHtmlOpcionesVarias(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Opciones Varias") + central.LineaDivisora(2);

		if(!general.OPCIONES_CHAR_BUFFER_AIO && !general.OPCIONES_CHAR_SEXO && !general.OPCIONES_CHAR_NOBLE && !general.OPCIONES_CHAR_LVL85){
			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Sorry, but there is no Options Enabled") + central.LineaDivisora(3);
			MAIN_HTML += "<br><br><br><br>" + central.BotonGOBACKZEUS() + central.getPieHTML() + "</body></html>";
			return MAIN_HTML;
		}

		if(general.OPCIONES_CHAR_SEXO){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Sex Change\" action=\"bypass -h ZeuSNPC OPCIONESVAR 1 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_SEXO_ITEM_PRICE, "Cost Sex Change");
		}
		if(general.OPCIONES_CHAR_NOBLE){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Noble\" action=\"bypass -h ZeuSNPC OPCIONESVAR 2 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE, "Cost Noble");
		}
		if(general.OPCIONES_CHAR_LVL85){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Level 85\" action=\"bypass -h ZeuSNPC OPCIONESVAR 3 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_LVL85_ITEM_PRICE, "Cost lvl 85");
		}
		if(general.OPCIONES_CHAR_BUFFER_AIO){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Char Aio Buffer\" action=\"bypass -h ZeuSNPC OPCIONESVAR 4 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_BUFFER_AIO_PRICE, "Cost AIO Buffer");
		}
		if(general.OPCIONES_CHAR_FAME){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\""+ String.valueOf(general.OPCIONES_CHAR_FAME_GIVE) +" of Fame\" action=\"bypass -h ZeuSNPC OPCIONESVAR 5 0 0\" width=188 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_FAME_PRICE, "Cost To Add " + String.valueOf(general.OPCIONES_CHAR_FAME_GIVE) + " of fame" );
		}

		MAIN_HTML += central.BotonGOBACKZEUS()+"</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}


	public static String Menu_CHANGE_NAME_PJ(L2PcInstance st,String seccion){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Player Change Name") + central.LineaDivisora(2);
		String TEXTO_NUEVO_NOM = "<edit type=\"seguridad\" var=\"NEW_NOM\" width=150>";
		String BOTON_ACEPTAR = "<button value=\"Check and Change\" action=\"bypass -h ZeuSNPC "+seccion+" 1 1 $NEW_NOM \" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		if(seccion.equals("USER_CHANGE_NAME")){
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE);
		}
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Enter new Name" + TEXTO_NUEVO_NOM + "<br>" + BOTON_ACEPTAR +"<br>","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.BotonGOBACKZEUS() +  central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	public static String Menu_CHANGE_NAME_CLAN(L2PcInstance st, String seccion){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Clan Change Name") + central.LineaDivisora(2);
		String TEXTO_NUEVO_NOM = "<edit type=\"seguridad\" var=\"NEW_NOM\" width=150>";
		String BOTON_ACEPTAR = "<button value=\"Check and Change\" action=\"bypass -h ZeuSNPC "+general.QUEST_INFO+" "+seccion+" 2 1 $NEW_NOM \" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		if(seccion.equals("USER_CHANGE_NAME")){
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE);
		}
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Enter new Name" + TEXTO_NUEVO_NOM + "<br>" + BOTON_ACEPTAR +"<br>","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML()+ "</body></html>";
		return MAIN_HTML;
	}



	public static String DelevelMenu(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Delevel Manager") + central.LineaDivisora(2);
		MAIN_HTML += "<center><br>";
		MAIN_HTML += msg.DELEVEL_MANAGER_MENSAJE_HASTA_$level.replace("$level", String.valueOf(general.DELEVEL_LVL_MAX));
		MAIN_HTML += central.LineaDivisora(2) + central.ItemNeedShowBox(general.DELEVEL_PRICE) + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Delevel 1 lvl\" action=\"bypass -h ZeuSNPC Delevel 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br>"+central.BotonGOBACKZEUS()+"</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static void showWindowsKnowZeus(L2PcInstance cha){
		//boolean isPlus = opera.isPlusChar(cha);
		//String btn1 = opera.isPlusHTML(cha,false) ? "<button value=\"Cargar Plus\" action=\"bypass -h voice .zeus_load_plus\" width=80 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" : "";
		//String btn2 = isPlus ? "<button value=\"ZeuS Plus\" action=\"bypass -h voice .zeus_char_plus\" width=80 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" : "";

		//String mainPlus = "<table width=280><tr><td width=140 align=CENTER>"+ btn1 +"</td><td width=140 align=CENTER>" + btn2 + "</td></tr></table>";

		String MAIN_HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Help User") + central.LineaDivisora(1);

		/*if(isPlus || opera.isPlusHTML(cha,false)){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat(mainPlus) + central.LineaDivisora(1);
		}*/

		HashMap<String, String> DatosInformacion = new HashMap<String, String>();

		if(general.BUFFCHAR_ACT){
			DatosInformacion.put(".zeus_buffer", msg.HELP_ZEUS_BUFFER);
		}
		if(general.RATE_EXP_OFF){
			DatosInformacion.put(".exp_on , .exp_off", msg.HELP_EXPONOFF);
		}
		if(general.DRESSME_STATUS){
			DatosInformacion.put(".dressme", msg.HELP_DRESSME);
		}
		if(general.ANTIBOT_COMANDO_STATUS){
			DatosInformacion.put(".checkbot", msg.HELP_CHECKBOT);
		}
		if(general.SHOW_MY_STAT){
			DatosInformacion.put(".stat", msg.HELP_STAT);
		}
		if(general.CHAR_PANEL){
			DatosInformacion.put(".charpanel", "Char Panel Config Windows");
		}
		if(general.REGISTER_EMAIL_ONLINE){
			DatosInformacion.put(".acc_register", "Link your account to an email");
			DatosInformacion.put(".changepassword", "Change your Password");
		}
		DatosInformacion.put(".fixme", "Fix any player on you account (if it's posible)");
		DatosInformacion.put(".myinfo", "Show my information.");
		DatosInformacion.put(".makeancientadena", "Convert your Seal Stone to Ancient Adena.");		
		String Tabla = "<Table width=275><tr><td width=80><font color=LEVEL>%NOM%</font></td><td width=195 fixwidth=180>%DESCRIP%</td></tr></table>";
		Iterator itr = DatosInformacion.entrySet().iterator();
		String strTable = "";
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	strTable += Tabla.replace("%NOM%", Entrada.getKey().toString()).replace("%DESCRIP%", Entrada.getValue().toString()) + central.LineaDivisora(2);
	    }

	    MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + central.headFormat(strTable) + central.LineaDivisora(2) + central.LineaDivisora(1);
	    	//if(BuffCategoria.get(Entrada.getKey()).get("ACT").equals("1")){


		MAIN_HTML += central.getPieHTML(false)+ "</body></html>";
		opera.enviarHTML(cha, MAIN_HTML);
	}

	public static void showWindowsKnowZeusAdmin(L2PcInstance cha){

		String MAIN_HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Help Admin Command") + central.LineaDivisora(1);

		HashMap<String, String> DatosInformacion = new HashMap<String, String>();

		DatosInformacion.put("//zeus_config", msg.HELP_ADMIN_CONFIG);
		DatosInformacion.put("//zeus_tele",msg.HELP_ADMIN_TELE);
		DatosInformacion.put("//zeus_shop",msg.HELP_ADMIN_SHOP);
		DatosInformacion.put("//oly_ban, //oly_unban",msg.HELP_ADMIN_OLY_BAN_UNBAN);
		DatosInformacion.put("//oly_reset_point",msg.HELP_ADMIN_RESET_POINT);
		DatosInformacion.put("//oly_point",msg.HELP_ADMIN_POINT);
		DatosInformacion.put("//zeus_ipblock",msg.HELP_ADMIN_IPBLOCK);
		DatosInformacion.put("//zeus_banip",msg.HELP_ADMIN_BANIP);
		DatosInformacion.put("//zeus_botzone",msg.HELP_ADMIN_BOTZONE);
		DatosInformacion.put("//zeus_recallAll, //zeus_recallAllforce",msg.HELP_ADMIN_RECALL);
		DatosInformacion.put("//zeus_buff_voice",msg.HELP_ADMIN_BUFF_VOICE);
		DatosInformacion.put("//zeus_bot_cancel",msg.HELP_ADMIN_CANCEL_ANTIBOT);
		DatosInformacion.put(".resetpin", "With target you can reset the PIN Code to default pin (9876)");
		DatosInformacion.put(".cancelcheckbot", "With target or name you can remove the target Antibot System");


		String Tabla = "<Table width=275><tr><td width=80><font color=LEVEL>%NOM%</font></td><td width=195 fixwidth=180>%DESCRIP%</td></tr></table>";


		Iterator itr = DatosInformacion.entrySet().iterator();
		String strTable = "";
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	strTable += Tabla.replace("%NOM%", Entrada.getKey().toString()).replace("%DESCRIP%", Entrada.getValue().toString()) + central.LineaDivisora(2);
	    }

	    MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + central.headFormat(strTable) + central.LineaDivisora(2) + central.LineaDivisora(1);
	    	//if(BuffCategoria.get(Entrada.getKey()).get("ACT").equals("1")){


		MAIN_HTML += central.getPieHTML(false)+ "</body></html>";
		opera.enviarHTML(cha, MAIN_HTML);
	}





}
