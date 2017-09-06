package ZeuS.Comunidad.EngineForm;

import java.lang.annotation.Repeatable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2SkillLearn;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.PledgeSkillList;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.Config.premiumPersonalData;
import ZeuS.Config.premiumsystem;
import ZeuS.interfase.EmailRegistration;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.cambionombre;
import ZeuS.interfase.central;
import ZeuS.interfase.charPanel;
import ZeuS.interfase.clanNomCambio;
import ZeuS.interfase.special_elemental;
import ZeuS.procedimientos.opera;

public class v_donation {
	
	private static Logger _log = Logger.getLogger(v_donation.class.getName());
	
	private static int[] VectorLocaciones = {
			Inventory.PAPERDOLL_HEAD,
			Inventory.PAPERDOLL_CHEST,
			Inventory.PAPERDOLL_LEGS,
			Inventory.PAPERDOLL_GLOVES,
			Inventory.PAPERDOLL_FEET,
			Inventory.PAPERDOLL_UNDER,
			Inventory.PAPERDOLL_BELT,
			Inventory.PAPERDOLL_LEAR,
			Inventory.PAPERDOLL_REAR,
			Inventory.PAPERDOLL_LFINGER,
			Inventory.PAPERDOLL_RFINGER,
			Inventory.PAPERDOLL_NECK,
			Inventory.PAPERDOLL_LBRACELET,
			Inventory.PAPERDOLL_RHAND,
			Inventory.PAPERDOLL_LHAND,
			};
	
	private static String[] VectorElementos = {
			"Fire",
			"Water",
			"Wind",
			"Earth",
			"Dark",
			"Holy"			
	};
	
	private static String getDonationWindows(L2PcInstance player){
		String retorno = "";
		
		String Grilla = "<table width=230 background=L2UI_CT1.Windows_DF_TooltipBG>"+
		"<tr><td fixwidth=32>%LINK%<br></td>"+
		"<td fixwidth=198><font color=A9F5D0>%NOM%</font><br1><font color=A9BCF5>Cost:</font> <font color=A9D0F5>%COST%</font></td>"+
		"</tr></table>";
		
		String NameMoneda = central.getNombreITEMbyID(general.DONA_ID_ITEM);
		
		String ByPassT = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";%SEC%;0;0;0;0;0";
		//ICONO, NOMBRE, COSTO, BYPASS
		Vector<String> Dona_Section = new Vector<String>();
		Dona_Section.add("icon.skill0516,Change Char Name," + String.valueOf(general.DONATION_CHANGE_CHAR_NAME_COST) + "," + ByPassT.replace("%SEC%", "CHANGE_CHAR_NAME"));
		Dona_Section.add("icon.skill0517,Change Clan Name," + String.valueOf(general.DONATION_CHANGE_CLAN_NAME_COST) + "," + ByPassT.replace("%SEC%", "CHANGE_CLAN_NAME"));
		Dona_Section.add("icon.etc_quest_account_reward_i00,255 Recommends," + String.valueOf(general.DONATION_255_RECOMMENDS) + "," + ByPassT.replace("%SEC%", "255_R"));
		Dona_Section.add("icon.skill1323,Noble," + String.valueOf(general.DONATION_NOBLE_COST) + "," + ByPassT.replace("%SEC%", "NOBLE"));		
		Dona_Section.add("icon.skill1297,Sex CHANGE," + String.valueOf(general.DONATION_CHANGE_SEX_COST) + "," + ByPassT.replace("%SEC%", "SEX"));
		Dona_Section.add("icon.skill0917,AIO Char Normal," + String.valueOf(general.DONATION_AIO_CHAR_SIMPLE_COSTO) + "," + ByPassT.replace("%SEC%", "AIO_NORMAL"));
		Dona_Section.add("icon.skill0837,AIO Char +30," + String.valueOf(general.DONATION_AIO_CHAR_30_COSTO) + "," + ByPassT.replace("%SEC%", "AIO_30"));
		Dona_Section.add("icon.etc_exp_point_i00,Character Level,---," + ByPassT.replace("%SEC%", "CHAR_LEVEL"));
		Dona_Section.add("icon.skill1409,Char Fame,---," + ByPassT.replace("%SEC%", "CHAR_FAME"));
		Dona_Section.add("icon.etc_quest_pkcount_reward_i00,Char Reduce PK,---," + ByPassT.replace("%SEC%", "CHAR_PK"));
		
		for(String t : Dona_Section){
			retorno += Grilla.replace("%COST%",(t.split(",")[2].equals("---") ? "Multiple" : t.split(",")[2] + " " + NameMoneda )).replace("%NOM%", t.split(",")[1]) .replace("%LINK%", cbFormato.getBotonForm(t.split(",")[0], t.split(",")[3]));
		}
		
		return retorno;
	}
	
	private static String getDonationWindows_Secundary(L2PcInstance player){
		String retorno = "<table width=460 border=0>";
		String NameMoneda = central.getNombreITEMbyID(general.DONA_ID_ITEM);
		
		String ByPassT = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";%SEC%;0;0;0;0;0";
		//ICONO, NOMBRE, COSTO, BYPASS
		Vector<String> Dona_Section = new Vector<String>();
		
		Dona_Section.add("icon.skill0371,Clan Level,---," + ByPassT.replace("%SEC%", "CLAN_LEVEL"));
		Dona_Section.add("icon.skill0372,Clan Skill,---," + ByPassT.replace("%SEC%", "CLAN_SKILL"));
		Dona_Section.add("icon.etc_bloodpledge_point_i00,Clan Reputation,---," + ByPassT.replace("%SEC%", "CLAN_REPUTATION"));
		Dona_Section.add("BranchSys.icon.br_scrl_of_ench_wp_s_i00,Enchant Item,---," + ByPassT.replace("%SEC%", "ENCHANT_ITEM"));
		Dona_Section.add("icon.etc_crystal_white_i00,Elemental Item,---," + ByPassT.replace("%SEC%", "ELEMENTAL_ITEM"));
		Dona_Section.add("icon.skill6319,Premium Service,---," + ByPassT.replace("%SEC%", "PREMIUM"));
		
		String Grilla = "<td fixwidth=230><table width=230 background=L2UI_CT1.Windows_DF_TooltipBG>"+
				"<tr><td fixwidth=32>%LINK%<br></td>"+
				"<td fixwidth=198><font color=A9F5D0>%NOM%</font><br1><font color=A9BCF5>Cost:</font> <font color=A9D0F5>%COST%</font></td>"+
				"</tr></table></td>";
		
		int contador=0;
		
		
		for(String t : Dona_Section){
			if(contador==0){
				retorno +="<tr>";
			}
			retorno += Grilla.replace("%COST%",(t.split(",")[2].equals("---") ? "Multiple" : t.split(",")[2] + " " + NameMoneda )).replace("%NOM%", t.split(",")[1]) .replace("%LINK%", cbFormato.getBotonForm(t.split(",")[0], t.split(",")[3]));
			contador++;
			if(contador==2){
				retorno += "</tr>";
				contador=0;
			}
		}

		if(contador==1){
			retorno += "<td fixwidth=230></td></tr>";
		}
		
    	retorno += "</table>";
		
		return retorno;
	}
	
	private static void sendNotificacionDonacion(L2PcInstance player){
		if(!general._activated()){
			return;
		}
		
		if(!EmailRegistration.hasEmailRegister(player)){
			central.msgbox(msg.DONATION_YOU_NEED_EMAIL_REGISTRATION_INPUT_$command.replace("$command", ".acc_register"), player);
			return;
		}
		
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(msg.DONATION_BTN_NOTIFY_DONATION) + central.LineaDivisora(2);
		
		

		String EmailUsuario = opera.getUserMail(player.getAccountName());
		
		String MensajePublicar = msg.DONATION_WINDOWS_THANKS.replace("%EMAIL", EmailUsuario != null ? EmailUsuario : "NO HAVE" ) ;
		
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(MensajePublicar,"LEVEL") + central.LineaDivisora(1);

		String TEXTO_DESCRIP = "<multiedit var=\"MEMOSTR\" width=250 height=80><br>";
		String COMBO_TIPO_DONACION = "<combobox width=200 var=TIPODONACION list="+ general.DONATION_TYPE_LIST +"><br>";
		String TEXTO_MONTO_DONADO = "<edit type=\"text\" var=\"MONTODONADO\" width=150><br>";

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Seleccione Medio de Donación" + COMBO_TIPO_DONACION,"WHITE") + central.LineaDivisora(1);
		MAIN_HTML += central.headFormat("Ingrese el Monto Donado" + TEXTO_MONTO_DONADO,"WHITE") + central.LineaDivisora(1);
		MAIN_HTML += central.headFormat("Ingrese datos como Número de transacción,<br1>hora, fecha. Mientras más Datos Informados<br1>Más rapido serán liberados las Donation Coin." + TEXTO_DESCRIP,"WHITE") + central.LineaDivisora(1);

		MAIN_HTML += "<center><button value=\"Notificar\" action=\"bypass -h ZeuSNPC ENVIAR_NOTIFICACION $MONTODONADO $TIPODONACION $MEMOSTR\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML += "<br><br><br></center>"+central.getPieHTML()+"</body></html>";

		central.sendHtml(player, MAIN_HTML);
	}	
	
	private static String mainHtml(L2PcInstance player, String TipoBusqueda,int Pagina){
		String retorno = "<html><title>Donation Market</title><body>";
		String Icono = "icon.skill0390";
		String Explica = "<br>"+general.DONATION_EXPLAIN_HOW_DO_IT;
		String Nombre = general.Server_Name + " Donation Market";
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false);
		
		String ByPass_Notify = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";SEND_NOTY_WINDOWS;0;0;0;0;0;0";
		retorno += "<img src=L2UI.SquareGray width=744 height=2><table width=744><tr><td fixwidth=200 align=center>"+
		"<button value=\"Notify your Donation\" action=\""+ ByPass_Notify +"\" width=180 height=35 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td>"+
		"<td fixwidth=544 align=LEFT>"+ general.DONATION_EASY_NOTIFICATION +"</td></tr></table><img src=L2UI.SquareGray width=744 height=2>";	
		
		
		final NpcHtmlMessage html = new NpcHtmlMessage();
		html.setFile(player.getHtmlPrefix(), "config/cbPersonal.htm");
		
		String PersonalData = html.getHtml().replace("<html><body>", "").replace("</body></html>", "");
		
		retorno += "<table width=750 border=0><tr><td fixwidth=250><table width=250><tr><td fixwidth=250>"+
		"<table width=220><tr><td>"+ getDonationWindows(player) + "</td></tr></table></td></tr></table></td>"+
        "<td fixwidth=500>"+ PersonalData +"<br1>"+ getDonationWindows_Secundary(player) +"</td></tr></table><br>";
		
		retorno += cbManager.getPieCommunidad() + "</body></html>";
		return retorno;
	}
	
	private static void sendHtmRequest(L2PcInstance player, String Comando){
		String html = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		String Bypass = "bypass -h voice .ZeDoSecc "+ Comando +" $txtNewName";
		
		String Titulo = "";
		
		if(Comando.equals("CHANGE_CHAR_NAME")){
			Titulo = "Change Player Name";
			
		}else{
			Titulo = "Change Clan Name";
		}
		
		html +="<img src=L2UI.SquareGray width=280 height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER>"+
		"<font color=81BEF7 name=hs12>"+ Titulo +"</font></td></tr></table><img src=L2UI.SquareGray width=280 height=2><br><img src=L2UI.SquareGray width=280 height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER><font color=FAAC58 name=hs12>Input the New Name</font>"+
		"</td></tr><tr><td fixwidth=280 align=CENTER><edit var=\"txtNewName\" width=150><br></td></tr><tr><td fixwidth=280 align=CENTER>"+
		"<button value=\"Change it!\" action=\""+ Bypass +"\" width=100 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><img src=L2UI.SquareGray width=280 height=2>";
	
		html += "</center>" + central.getPieHTML(false) + "</body></html>";
		central.sendHtml(player, html);
	}
	
	
	
	private static String getElementalList(L2PcInstance player, L2ItemInstance It){

		String Retorno = "";
		
		for(String El : VectorElementos){
			int IDItemEnchant =0;
			switch(El){
				case "Fire":
					IDItemEnchant = 9558;
					break;
				case "Water":
					IDItemEnchant = 9559;
					break;
				case "Wind":
					IDItemEnchant = 9561;
					break;
				case "Earth":
					IDItemEnchant = 9560;
					break;
				case "Dark":
					IDItemEnchant = 9562;
					break;
				case "Holy":
					IDItemEnchant = 9563;
					break;
			}
			if(Retorno.length()>0){
				Retorno += ";";
			}
			
			if(special_elemental.CanUseStoneOnThis(player, It.getObjectId(), IDItemEnchant)){
				Retorno += El;
			}
		}
		
		return Retorno;
	}
	
	private static void getArmorElementalWindows(L2PcInstance player){
		boolean armorActive = true;
		boolean weaponActive = true;
		
		String cmbEnchantArmor = "";
		String cmbEnchantWeapon = "";
		
		String ValEnchantArmor = "";
		String ValEnchantWeapon = "";
		
		int MaxArmorEnchant =0;
		int MaxWeaponEnchant =0;
		
		if(general.DONATION_ELEMENTAL_ITEM_ARMOR==null){
			armorActive = false;
		}else{
			Iterator itr = general.DONATION_ELEMENTAL_ITEM_ARMOR.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int Enchant = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(cmbEnchantArmor.length()>0){
					cmbEnchantArmor+= ";";
					ValEnchantArmor+= " , ";
				}
				if(Enchant > MaxArmorEnchant){
					MaxArmorEnchant = Enchant;
				}
				cmbEnchantArmor += String.valueOf(Enchant);
				ValEnchantArmor += "<font color=FE642E>+"+ String.valueOf(Enchant) +"</font> = <font color=FE9A2E>"+ String.valueOf(CantidadDona) +"</font>";
			}			
		}
		
		if(general.DONATION_ELEMENTAL_ITEM_WEAPON==null){
			weaponActive = false;
		}else{
			Iterator itr = general.DONATION_ELEMENTAL_ITEM_WEAPON.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int Enchant = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(cmbEnchantWeapon.length()>0){
					cmbEnchantWeapon+= ";";
					ValEnchantWeapon+= " , ";
				}
				if(Enchant > MaxWeaponEnchant){
					MaxWeaponEnchant = Enchant;
				}
				cmbEnchantWeapon += String.valueOf(Enchant);
				ValEnchantWeapon += "<font color=FE642E>+"+ String.valueOf(Enchant) +"</font> = <font color=FE9A2E>"+ String.valueOf(CantidadDona) +"</font>";
			}			
		}
		
		if(!armorActive && !weaponActive){
			return;
		}
		
		String htm = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		
		//<font color=FE642E>+4</font> = <font color=FE9A2E>3</font> , <font color=FE642E>+8</font> = <font color=FE9A2E>6</font>
		
		htm += "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER><font color=81BEF7 name=hs12>Enchant Level</font></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2><br><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";

		if(armorActive){
			htm += "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER><font color=FAAC58>Enchant Armor Cost</font></td></tr><tr><td fixwidth=280 align=CENTER>"+
	        ValEnchantArmor + "</td></tr></table>";
		}
		
		if(weaponActive){
			htm += "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER><font color=FAAC58>Enchant Weapon Cost</font></td></tr><tr><td fixwidth=280 align=CENTER>"+
			ValEnchantWeapon + "</td></tr></table>";
		}
		
		String BypassArmor = "bypass -h voice .ZeDoSecc En_E_Armor %IDSLOT% %ID_OBJECT% $cmbEn_%ID% $cmbEnVal_%ID%";
		String bypassWeapon = "bypass -h voice .ZeDoSecc En_E_Weapon %IDSLOT% %ID_OBJECT% $cmbEn_%ID% $cmbEnVal_%ID%";
		
		String GrillaArmor = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
		"<img src=\"%ID_IMA%\" width=32 height=32><br></td><td fixwidth=196>"+
        "<font color=819FF7>%NOM_ITEM%</font> <font color=81BEF7>%ENCHANT%</font><br1><table width=190 border=0><tr>"+
        "<td fixwidth=52><font color=F7BE81>-E. Enc: </font></td>"+
        "<td fixwidth=68><combobox width=60 var=\"cmbEn_%ID%\" list=\"%TYPE_ELEMENTAL%\" ></td>"+
        "<td fixwidth=78><combobox width=50 var=\"cmbEnVal_%ID%\" list=\""+ cmbEnchantArmor +"\" ></td></tr></table><br></td><td fixwidth=52 align=CENTER>"+
        "<button value=\"Do It!\"  action=\""+ BypassArmor + "\" width=48 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";

		
		String GrillaWeapon = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
				"<img src=\"%ID_IMA%\" width=32 height=32><br></td><td fixwidth=196>"+
		        "<font color=819FF7>%NOM_ITEM%</font> <font color=81BEF7>%ENCHANT%</font><br1><table width=190 border=0><tr>"+
		        "<td fixwidth=52><font color=F7BE81>-E. Enc: </font></td>"+
		        "<td fixwidth=68><combobox width=60 var=\"cmbEn_%ID%\" list=\"%TYPE_ELEMENTAL%\" ></td>"+
		        "<td fixwidth=78><combobox width=50 var=\"cmbEnVal_%ID%\" list=\""+  cmbEnchantWeapon +"\" ></td></tr></table><br></td><td fixwidth=52 align=CENTER>"+
		        "<button value=\"Do It!\"  action=\""+ bypassWeapon + "\" width=48 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";		

		int cont = 0;
		
		for(int Info : VectorLocaciones){
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				L2ItemInstance It = player.getInventory().getPaperdollItem(Info);
				
				if(It.isEnchantable()==0 || !It.isElementable()){
					continue;
				}		
				
				String TypeElementalStr = getElementalList(player,It);
				
				String I_Ima = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String I_Name = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				I_Name = ( I_Name.length() > 24 ? I_Name.substring(0, 24) + "..." : I_Name );
				int I_Enchant = player.getInventory().getPaperdollItem(Info).getEnchantLevel();
				if(It.isWeapon()){
					if(I_Enchant >= MaxWeaponEnchant){
						continue;
					}
					htm += GrillaWeapon.replace("%TYPE_ELEMENTAL%", TypeElementalStr).replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));
				}else{
					if(I_Enchant >= MaxArmorEnchant){
						continue;
					}
					htm += GrillaArmor.replace("%TYPE_ELEMENTAL%", TypeElementalStr).replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));					
				}
				cont++;
			}
		}
		
		
		
		htm += "</center>" + central.getPieHTML(false) +"</body></html>";
		central.sendHtml(player, htm);
	}
	
	private static void updatePremiumTable(){
		String DeleteInfo = "DELETE FROM zeus_premium WHERE end_date <= ?";
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(DeleteInfo))
			{
				// Remove or update a L2PcInstance skill from the character_skills table of the database
				statementt.setInt(1, opera.getUnixTimeNow());
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}	
	}	
	
	private static boolean savePremiumOnBd(L2PcInstance player, int idPremium){
		if(general.getPremiumServices()==null){
			central.msgbox("No Premium Data", player);
			return false;
		}
		if(!general.getPremiumServices().containsKey(idPremium)){
			central.msgbox("Premium Data not available now", player);
			return false;
		}
		premiumsystem t = general.getPremiumServices().get(idPremium);
		
		if(!t.IsAccount()){
			if(player.getClan()==null){
				central.msgbox("You dont have Clan to get this Premium Clan System", player);
				return false;
			}
			if(player.isClanLeader()){
				central.msgbox("You dont need to be the clan leader to get this Premium Clan System", player);
				return false;				
			}
		}
		
		String TipoPremium = t.IsAccount() ? "ACCOUNT" : "CLAN";
		String IdPpl_Clan = t.IsAccount() ? player.getAccountName() : String.valueOf(player.getClan().getId());
		
		int Days = t.getDays();
		int DiasUnixDar = Days * 86400;
		int NowUnix = opera.getUnixTimeNow();
		
		updatePremiumTable();
		
		String consulta = "INSERT INTO zeus_premium(id,start_date,end_date,tip,idPremium) "+
			"values(?,?,?,?,?) "+
			"ON DUPLICATE KEY UPDATE end_date = end_date + ?";
		
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setString(1, IdPpl_Clan);
			psqry.setInt(2, NowUnix);
			psqry.setInt(3, NowUnix + DiasUnixDar);
			psqry.setString(4, TipoPremium);
			psqry.setInt(5, idPremium);
			psqry.setInt(6, DiasUnixDar);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e){
				_log.warning("Error Premium Save Info->" + e.getMessage());
				central.msgbox("Error on create Premium", player);
				return false;
			}
		}catch(SQLException a){
			_log.warning("Error Premium Creating Info->" + a.getMessage());
			central.msgbox("Error on create Premium", player);
			return false;			
		}
		try{
			conn.close();
		}catch(SQLException a){

		}
		
		int intTermino = NowUnix + DiasUnixDar;
		int intInicio = NowUnix;
		
		if(t.IsAccount()){
			if(opera.isPremium_Player(player)){
				intTermino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd() + DiasUnixDar;
				intInicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
			}
		}else{
			if(opera.isPremium_Clan(player)){
				intTermino = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getEnd() + DiasUnixDar;
				intInicio = general.getPremiumDataFromPlayerOrClan( String.valueOf( player.getClanId() ) ).getBegin();
			}
		}
		
		
		premiumPersonalData Pd = new premiumPersonalData(IdPpl_Clan,intInicio,intTermino,t.IsAccount(),t.getIDPremium());
		
		general.setNewPremiumPersonalData(IdPpl_Clan, Pd);
		
		return true;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static void getArmorEnchantWindows(L2PcInstance player){
		boolean armorActive = true;
		boolean weaponActive = true;
		
		String cmbEnchantArmor = "";
		String cmbEnchantWeapon = "";
		
		String ValEnchantArmor = "";
		String ValEnchantWeapon = "";
		
		int MaxArmorEnchant =0;
		int MaxWeaponEnchant =0;
		
		if(general.DONATION_ENCHANT_ITEM_ARMOR==null){
			armorActive = false;
		}else{
			Iterator itr = general.DONATION_ENCHANT_ITEM_ARMOR.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int Enchant = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(cmbEnchantArmor.length()>0){
					cmbEnchantArmor+= ";";
					ValEnchantArmor+= " , ";
				}
				if(Enchant > MaxArmorEnchant){
					MaxArmorEnchant = Enchant;
				}
				cmbEnchantArmor += String.valueOf(Enchant);
				ValEnchantArmor += "<font color=FE642E>+"+ String.valueOf(Enchant) +"</font> = <font color=FE9A2E>"+ String.valueOf(CantidadDona) +"</font>";
			}			
		}
		
		if(general.DONATION_ENCHANT_ITEM_WEAPON==null){
			weaponActive = false;
		}else{
			Iterator itr = general.DONATION_ENCHANT_ITEM_WEAPON.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int Enchant = (int) Entrada.getKey();
				int CantidadDona = (int) Entrada.getValue();
				if(cmbEnchantWeapon.length()>0){
					cmbEnchantWeapon+= ";";
					ValEnchantWeapon+= " , ";
				}
				if(Enchant > MaxWeaponEnchant){
					MaxWeaponEnchant = Enchant;
				}
				cmbEnchantWeapon += String.valueOf(Enchant);
				ValEnchantWeapon += "<font color=FE642E>+"+ String.valueOf(Enchant) +"</font> = <font color=FE9A2E>"+ String.valueOf(CantidadDona) +"</font>";
			}			
		}
		
		if(!armorActive && !weaponActive){
			return;
		}
		
		String htm = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		
		//<font color=FE642E>+4</font> = <font color=FE9A2E>3</font> , <font color=FE642E>+8</font> = <font color=FE9A2E>6</font>
		
		htm += "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER><font color=81BEF7 name=hs12>Enchant Level</font></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2><br><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";

		if(armorActive){
			htm += "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER><font color=FAAC58>Enchant Armor Cost</font></td></tr><tr><td fixwidth=280 align=CENTER>"+
	        ValEnchantArmor + "</td></tr></table>";
		}
		
		if(weaponActive){
			htm += "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER><font color=FAAC58>Enchant Weapon Cost</font></td></tr><tr><td fixwidth=280 align=CENTER>"+
			ValEnchantWeapon + "</td></tr></table>";
		}
		
		String BypassArmor = "bypass -h voice .ZeDoSecc EnArmor %IDSLOT% %ID_OBJECT% $cmbEn_%ID%";
		String bypassWeapon = "bypass -h voice .ZeDoSecc EnWeapon %IDSLOT% %ID_OBJECT% $cmbEn_%ID%";
		
		String GrillaArmor = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
        "<img src=\"%ID_IMA%\" width=32 height=32><br></td><td fixwidth=196>"+
        "<font color=819FF7>%NOM_ITEM%</font> <font color=81BEF7>%ENCHANT%</font><br1><table width=190 border=0><tr><td fixwidth=52>"+
        "<font color=F7BE81>- Ench: </font></td><td fixwidth=138><combobox width=60 var=\"cmbEn_%ID%\" list=\""+ cmbEnchantArmor +"\" ></td></tr></table><br></td><td fixwidth=52 align=CENTER>"+
        "<button value=\"Do It!\"  action=\""+ BypassArmor +"\" width=48 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";
		
		String GrillaWeapon = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
        "<img src=\"%ID_IMA%\" width=32 height=32><br></td><td fixwidth=196>"+
        "<font color=819FF7>%NOM_ITEM%</font> <font color=81BEF7>%ENCHANT%</font><br1><table width=190 border=0><tr><td fixwidth=52>"+
        "<font color=F7BE81>- Ench: </font></td><td fixwidth=138><combobox width=60 var=\"cmbEn_%ID%\" list=\""+ cmbEnchantWeapon +"\" ></td></tr></table><br></td><td fixwidth=52 align=CENTER>"+
        "<button value=\"Do It!\"  action=\""+ bypassWeapon +"\" width=48 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";		

		int cont = 0;
		
		for(int Info : VectorLocaciones){
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				L2ItemInstance It = player.getInventory().getPaperdollItem(Info);
				
				if(It.isEnchantable()==0){
					continue;
				}				
				
				String I_Ima = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String I_Name = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				I_Name = ( I_Name.length() > 24 ? I_Name.substring(0, 24) + "..." : I_Name );
				int I_Enchant = player.getInventory().getPaperdollItem(Info).getEnchantLevel();
				if(It.isWeapon()){
					if(I_Enchant >= MaxWeaponEnchant){
						continue;
					}
					htm += GrillaWeapon.replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));
				}else{
					if(I_Enchant >= MaxArmorEnchant){
						continue;
					}
					htm += GrillaArmor.replace("%ID%", String.valueOf(cont))  .replace("%ID_OBJECT%", String.valueOf(It.getObjectId())).replace("%IDSLOT%", String.valueOf(Info)).replace("%ENCHANT%", (I_Enchant>0 ? "+" + String.valueOf(I_Enchant) : "")).replace("%NOM_ITEM%", I_Name).replace("%ID_IMA%", (I_Ima==null ? "": I_Ima));					
				}
				cont++;
			}
		}
		
		
		
		htm += "</center>" + central.getPieHTML(false) +"</body></html>";
		central.sendHtml(player, htm);
	}
	
	private static void getPremiumShowAllWindows(L2PcInstance player){
		String html = "<html><title>" + general.TITULO_NPC() + "</title><body><central>";
		
		html += "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280 align=CENTER>"+
		"<font color=81BEF7 name=hs12>Premiun Account & Clan</font></td></tr></table><img src=\"L2UI.SquareGray\" width=280 height=2><br><img src=\"L2UI.SquareGray\" width=280 height=2>";
		
		String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";PREMIUM_SEE;%IDPRE%;0;0;0;0;0";
		
		String grilla = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
		"<img src=\"%IMAGEN%\" width=32 height=32><br><br><br></td><td fixwidth=196>"+
        "<font color=819FF7>%NAME% - :</font> <font color=81BEF7>%DAYS% days</font><br1>"+
        "<font color=F7BE81>- Cost: </font><font color=F5DA81>%DC% Donation Coin</font><br1>"+
        "<font color=F7BE81>- Applicable to: </font><font color=F5DA81>%APPLI%</font><br>"+
        "</td><td fixwidth=52 align=CENTER>"+
        "<br><br><button action=\""+ByPass+"\" width=16 height=16 back=L2UI_CT1.PostWnd_DF_Btn_List fore=L2UI_CT1.PostWnd_DF_Btn_List></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";
		

		Iterator itr = general.getPremiumServices().entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int idPremium = (int) Entrada.getKey();
			premiumsystem PremiumInfo = (premiumsystem) Entrada.getValue();
			if(!PremiumInfo.isEnabled()){
				continue;
			}
			html += grilla.replace("%IDPRE%", String.valueOf(idPremium)).replace("%APPLI%", PremiumInfo.getAplicableA()) .replace("%DC%", String.valueOf(PremiumInfo.getCost())) .replace("%DAYS%", String.valueOf(PremiumInfo.getDays())) .replace("%NAME%", PremiumInfo.getName()) .replace("%IMAGEN%", PremiumInfo.getIcono());
		}
		html += "</central>" + central.getPieHTML() + "</body></html>";
		central.sendHtml(player, html);
	}
	
	private static void getPremiumShowInfoWindows(L2PcInstance player, int idPremium){
		if(general.getPremiumServices()==null){
			return;
		}
		if(!general.getPremiumServices().containsKey(idPremium)){
			return;
		}
		
		premiumsystem p = general.getPremiumServices().get(idPremium);
		
		String ByPassBack = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";PREMIUM;0;0;0;0;0;0";//
		String ByPassBuy = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.donation.name() + ";PREMIUM_BUY;%IDPREMIUM%;0;0;0;0;0";//
		
		String html = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		
		html += "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
		"<img src=\""+p.getIcono() +"\" width=32 height=32><br><br><br><br></td><td fixwidth=196>"+
        "<font color=819FF7 name=hs12>"+ p.getName() +"</font><br1>"+
        "<font color=F7BE81>- Applicable to: </font><font color=F5DA81>"+ p.getAplicableA() +"</font><br1>"+
        "<font color=F7BE81>- Days: </font><font color=F5DA81>"+ String.valueOf(p.getDays()) +"</font><br1>"+
        "<font color=F7BE81>- Cost: </font><font color=F5DA81>"+ String.valueOf(p.getCost()) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM) +"</font><br1>"+
        "</td><td fixwidth=52 align=CENTER></td></tr></table>";
		
		html += "<table width=285 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=142 align=CENTER><font color=81BEF7 name=hs12>Bonus & Rate</font></td><td fixwidth=142 align=CENTER><font color=81BEF7 name=hs12>Final Result</font></td></tr></table>";
		
		html += "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=280>";
		
		
		String Grilla = "<table width=280 bgcolor=151515><tr><td fixwidth=140 align=CENTER>"+
        "<font color=F7BE81>%NOMBRE%</font> <font color=F5DA81>+%VAL_ORIGINAL%%</font></td><td fixwidth=140 align=CENTER>"+
        "<font color=FAAC58>%FINAL_RESULT%</font></td></tr></table>";
		
		
		for(String d : p.getInfoShow()){
			String Nombre = d.split(":")[0];
			String ValOfi = d.split(":")[1];
			String tG = Grilla.replace("%NOMBRE%", Nombre.replace("_", " ")).replace("%VAL_ORIGINAL%", ValOfi);
			String FinalValue = "";
			switch(Nombre.toLowerCase()){
				case "exp":
					FinalValue = "x" + String.valueOf(p.getexp(true));
					break;
				case "sp":
					FinalValue = "x" + String.valueOf(p.getsp(true));
					break;
				case "adena":
					FinalValue = "x" + String.valueOf(p.getadena(true));
					break;
				case "drop":
					FinalValue = "x" + String.valueOf(p.getDrop(true));
					break;
				case "spoil":
					FinalValue = "x" + String.valueOf(p.getSpoil(true));
					break;
				case "epaulette":
					FinalValue = "Drop +%" + String.valueOf(p.getEpaulette());
					break;	
				case "craft":
					FinalValue = "Recipe +%" + String.valueOf(p.getCraft());
					break;
				case "mw_craft":
					FinalValue = "Recipe +%" + String.valueOf(p.get_mwCraft());
					break;
			}
			html += tG.replace("%FINAL_RESULT%", FinalValue);;
		}
		
		html += "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=140 align=CENTER>"+
		"<button value=\"Buy\" action=\""+ ByPassBuy.replace("%IDPREMIUM%", String.valueOf(idPremium)) +"\" width=120 height=28 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td><td fixwidth=140 align=CENTER>"+
		"<button value=\"Back\" action=\""+ ByPassBack +"\" width=120 height=28 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td></tr></table><br>"; 
		
		
		html += "<br></td></tr></table>";
		html += "</center>" + central.getPieHTML() + "</body></html>";
		central.sendHtml(player, html);
	}
	
	private static void PrivateHtmlRequest(L2PcInstance player, String Command, HashMap<Integer,Integer> Op){
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body><center>";
		HashMap<Integer,String>ID_IMA_ICON = new HashMap<Integer,String>();

		String ByPass = "bypass -h voice .ZeDoSecc "+ Command +" %DATA_NUM% %CANTIDAD%";

		String TituloSeccion = "";
		
		String TituloShowGrilla = "";
		String NombreDonation = central.getNombreITEMbyID(general.DONA_ID_ITEM);
		
		boolean RepiteImagen = true;
		String ImagenRepite = "";
		switch(Command){
			case "CHAR_LEVEL":
				ImagenRepite =  "icon.etc_exp_point_i00";
				TituloShowGrilla = "Character Level:";
				TituloSeccion = "Character Level";
				break;
			case "CHAR_FAME":
				ImagenRepite =  "icon.skill1409";
				TituloShowGrilla = "Character Fame:";
				TituloSeccion = "Character Fame";
				break;
			case "CHAR_PK":
				ImagenRepite =  "icon.etc_quest_pkcount_reward_i00";
				TituloShowGrilla = "Reduce PK:";
				TituloSeccion = "Reduce PK";
				break;
			case "CLAN_LEVEL":
				ImagenRepite =  "";
				RepiteImagen = false;
				ID_IMA_ICON.put(1, "icon.skill0376");
				ID_IMA_ICON.put(2, "icon.skill0379");
				ID_IMA_ICON.put(3, "icon.skill0372");
				ID_IMA_ICON.put(4, "icon.skill0389");
				ID_IMA_ICON.put(5, "icon.skill0391");
				ID_IMA_ICON.put(6, "icon.skill0374");
				ID_IMA_ICON.put(7, "icon.skill0390");
				ID_IMA_ICON.put(8, "icon.skill0378");
				ID_IMA_ICON.put(9, "icon.skill0388");
				ID_IMA_ICON.put(10, "icon.skill0375");
				ID_IMA_ICON.put(11, "icon.skill0373");
				TituloShowGrilla = "Clan Level:";
				TituloSeccion = "Clan Level";
				break;
			case "CLAN_SKILL":
				ImagenRepite =  "";
				RepiteImagen = false;
				ID_IMA_ICON.put(1, "icon.skill0376");
				ID_IMA_ICON.put(2, "icon.skill0379");
				ID_IMA_ICON.put(3, "icon.skill0372");
				ID_IMA_ICON.put(4, "icon.skill0389");
				ID_IMA_ICON.put(5, "icon.skill0391");
				ID_IMA_ICON.put(6, "icon.skill0374");
				ID_IMA_ICON.put(7, "icon.skill0390");
				ID_IMA_ICON.put(8, "icon.skill0378");
				ID_IMA_ICON.put(9, "icon.skill0388");
				ID_IMA_ICON.put(10, "icon.skill0375");
				ID_IMA_ICON.put(11, "icon.skill0373");
				TituloShowGrilla = "Clan Skill:";
				TituloSeccion = "Clan Skills";				
				break;
			case "CLAN_REPUTATION":
				TituloShowGrilla = "Reputation:";
				TituloSeccion = "Clan Reputation";
				ImagenRepite = "icon.etc_bloodpledge_point_i00";
				break;
		}
		html += "<center><img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr>"+
        "<td fixwidth=280 align=CENTER><font color=81BEF7 name=hs12>"+ TituloSeccion +"</font></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2><br>";
		
		
		String Grilla = "<img src=\"L2UI.SquareGray\" width=\"280\" height=2><table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
		"<img src=\"%IDIMG%\" width=32 height=32><br></td><td fixwidth=196>"+
		"<font color=819FF7>"+ TituloShowGrilla +"</font> <font color=81BEF7>%DATA_NUM%</font><br1>"+
        "<font color=F7BE81>- Cost: </font><font color=F5DA81>%CANTIDAD% "+ NombreDonation +"</font><br></td>"+
        "<td fixwidth=52 align=CENTER><button value=\"Do It!\" action=\""+ ByPass +"\" width=48 height=32 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";
		
		Iterator itr = Op.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int Valor = (int) Entrada.getKey();
			int CantidadDona = (int) Entrada.getValue();
			html += Grilla.replace("%IDIMG%", ( RepiteImagen ? ImagenRepite : ID_IMA_ICON.get(Valor) )).replace("%CANTIDAD%", String.valueOf(CantidadDona)).replace("%DATA_NUM%", String.valueOf(Valor));
		}
		html += "</center>" + central.getPieHTML() + "</body></html>";
		central.sendHtml(player, html);
	}
	
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,"",0);
		}else if(parm1.equals("SEND_NOTY_WINDOWS")){
			sendNotificacionDonacion(player);
			return "";
		}else if(parm1.equals("CHANGE_CHAR_NAME") || parm1.equals("CHANGE_CLAN_NAME")){
			sendHtmRequest(player, parm1);
		}else if(parm1.equals("255_R")){
			if(player.getRecomHave()<255){
				if(opera.haveDonationItem(player, general.DONATION_255_RECOMMENDS)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_255_RECOMMENDS, player);
					player.setRecomLeft( 255 - player.getRecomHave());
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();					
					cbFormato.cerrarCB(player);
				}
			}else{
				central.msgbox("You have full recos", player);
			}
		}else if(parm1.equals("NOBLE")){
			if(general.DONATION_NOBLE_COST==0){
			central.msgbox(msg.DISABLED_BY_ADMIN, player);
			return "";					
			}
			if(player.isNoble()){
				central.msgbox("You are noble!", player);
				return "";
			}
			if(opera.haveDonationItem(player, general.DONATION_NOBLE_COST)){
				opera.removeItem(general.DONA_ID_ITEM, general.DONATION_NOBLE_COST, player);
				opera.setNoble(player);
				player.broadcastUserInfo();
				player.broadcastStatusUpdate();				
				cbFormato.cerrarCB(player);
			}else{
				return "";
			}			
		}else if(parm1.equals("SEX")){
			if(general.DONATION_CHANGE_SEX_COST==0){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			if(opera.haveDonationItem(player, general.DONATION_CHANGE_SEX_COST)){
				opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_SEX_COST, player);
				opera.changeSex(player);
				player.broadcastUserInfo();
				player.broadcastStatusUpdate();
				cbFormato.cerrarCB(player);
			}
			return "";			
		}else if(parm1.equals("AIO_NORMAL")){
			if(general.DONATION_AIO_CHAR_SIMPLE_COSTO==0){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";	
			}
			
			if(general.DONATION_AIO_CHAR_LV_REQUEST>player.getLevel()){
				central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.DONATION_AIO_CHAR_LV_REQUEST)), player);
				return "";					
			}
			if(opera.haveDonationItem(player, general.DONATION_AIO_CHAR_SIMPLE_COSTO)){
				if(aioChar.setNewAIO(player, true)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_AIO_CHAR_SIMPLE_COSTO, player);
					cbFormato.cerrarCB(player);
				}
			}
			return "";
		}else if(parm1.equals("AIO_30")){
			if(general.DONATION_AIO_CHAR_30_COSTO ==0){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";	
			}
			
			if(general.DONATION_AIO_CHAR_LV_REQUEST>player.getLevel()){
				central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.DONATION_AIO_CHAR_LV_REQUEST)), player);
				return "";					
			}
			if(opera.haveDonationItem(player, general.DONATION_AIO_CHAR_30_COSTO)){
				if(aioChar.setNewAIO(player, false)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_AIO_CHAR_30_COSTO, player);
					cbFormato.cerrarCB(player);
				}
			}
			return "";
		}else if(parm1.equals("CHAR_LEVEL")){
			PrivateHtmlRequest(player,parm1,general.DONATION_CHARACTERS_LEVEL);
		}else if(parm1.equals("CHAR_FAME")){
			PrivateHtmlRequest(player,parm1,general.DONATION_CHARACTERS_FAME_POINT);
		}else if(parm1.equals("CHAR_PK")){
			PrivateHtmlRequest(player,parm1,general.DONATION_CHARACTERS_PK_POINT);
		}else if(parm1.equals("CLAN_LEVEL")){
			PrivateHtmlRequest(player,parm1,general.DONATION_CLAN_LEVEL);
		}else if(parm1.equals("CLAN_SKILL")){
			PrivateHtmlRequest(player,parm1,general.DONATION_CLAN_SKILL);
		}else if(parm1.equals("CLAN_REPUTATION")){
			PrivateHtmlRequest(player,parm1,general.DONATION_CLAN_REPUTATION);
		}else if(parm1.equals("ENCHANT_ITEM")){
			getArmorEnchantWindows(player);
		}else if(parm1.equals("ELEMENTAL_ITEM")){
			getArmorElementalWindows(player);
		}else if(parm1.equals("PREMIUM")){
			getPremiumShowAllWindows(player);
		}else if(parm1.equals("PREMIUM_SEE")){
			getPremiumShowInfoWindows(player,Integer.valueOf(parm2));
			return "";
		}else if(parm1.equals("PREMIUM_BUY")){
			int idSelectedPremium = Integer.valueOf(parm2);
			if(opera.isPremium_Player(player)){
				premiumPersonalData PA = general.getPremiumDataFromPlayerOrClan(player.getAccountName());
				premiumsystem PA_OrinalData = general.getPremiumServices().get(idSelectedPremium);
				if(PA.isActive()){
					if(idSelectedPremium != PA.getIdPremiumUse()){
						central.msgbox("We are sorry, but u cant buy another Premium Account, you need to wait to the end date to buy another", player);
						return "";
					}
				}
				if(PA_OrinalData==null){
					central.msgbox("Premium Data is not avaible know.", player);
					return "";
				}
				int DC = PA_OrinalData.getCost();
				if(!opera.haveDonationItem(player, DC)){
					return "";
				}
				if(savePremiumOnBd(player, idSelectedPremium)){
					opera.removeItem(general.DONA_ID_ITEM, DC, player);
					central.msgbox("Congratulation, you Premium " + PA_OrinalData.getAplicableA() + " - " + PA_OrinalData.getName() + " has More Days!." , player);
					charPanel.getCharInfo(player);
					cbFormato.cerrarCB(player);					
				}else{
					return "";
				}
			}else if(opera.isPremium_Clan(player)){
				premiumPersonalData PA = general.getPremiumDataFromPlayerOrClan( String.valueOf(player.getClanId()) );
				premiumsystem PA_OrinalData = general.getPremiumServices().get(idSelectedPremium);
				if(PA.isActive()){
					if(idSelectedPremium != PA.getIdPremiumUse()){
						central.msgbox("We are sorry, but u cant buy another Premium Clan, you need to wait to the end date to buy another", player);
						return "";
					}
				}
				if(PA_OrinalData==null){
					central.msgbox("Premium Data is not avaible know.", player);
					return "";
				}
				int DC = PA_OrinalData.getCost();
				if(!opera.haveDonationItem(player, DC)){
					return "";
				}
				if(savePremiumOnBd(player, idSelectedPremium)){
					opera.removeItem(general.DONA_ID_ITEM, DC, player);
					central.msgbox("Congratulation, you Premium " + PA_OrinalData.getAplicableA() + " - " + PA_OrinalData.getName() + " has More Days!." , player);
					charPanel.getCharInfo(player);
					cbFormato.cerrarCB(player);					
				}else{
					return "";
				}				
			}else{
				premiumsystem PA_OrinalData = general.getPremiumServices().get(idSelectedPremium);
				int DC = PA_OrinalData.getCost();
				if(!opera.haveDonationItem(player, DC)){
					return "";
				}
				if(savePremiumOnBd(player, idSelectedPremium)){
					opera.removeItem(general.DONA_ID_ITEM, DC, player);
					central.msgbox("Congratulation, you Premium " + PA_OrinalData.getAplicableA() + " - " + PA_OrinalData.getName() + " data its now on." , player);
					charPanel.getCharInfo(player);
					cbFormato.cerrarCB(player);
					return "";
				}
			}
		}
		return mainHtml(player,"",0);
	}
	
	private static void setElement(L2PcInstance player, byte type, int value, int armorType)
	{
		// get the target
		
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
		}
	}
	
	
	private static int getDCFromHashMap (HashMap<Integer,Integer>DC, int val, L2PcInstance player){
		if(DC!=null){
			if(DC.containsKey(val)){
				if(DC.get(val)>0){
					return DC.get(val);
				}else{
					central.msgbox("Disable Option.", player);
				}
			}
		}
		central.msgbox("Try Again. Maybe the Adm/gm change some value here", player);
		return -1;
	}
	
	
	public static void bypass_voice(L2PcInstance player, String params){
		_log.warning("->>>" + params);
			if(params.split(" ")[0].equals("En_E_Armor")){
				int Parte = Integer.valueOf(params.split(" ")[1]);
				int IdObjeto = Integer.valueOf(params.split(" ")[2]);
				byte element = Elementals.getElementId(params.split(" ")[3]);
				int EnchantPower = Integer.valueOf(params.split(" ")[4]);
				
				int dc = getDCFromHashMap(general.DONATION_ELEMENTAL_ITEM_ARMOR ,EnchantPower,player);
				if(dc>0){
					boolean doIt = true;
					L2ItemInstance It = player.getInventory().getPaperdollItem(Parte);
					
					String I_Name = It.getName();
					
					if(It.isEnchantable()==0){
						central.msgbox("This item cant enchant. You change the Item", player);
						doIt=false;
					}else if(It.getObjectId()!=IdObjeto){
						central.msgbox("You change the Item. Enchanting Fail.", player);
						doIt=false;						
					}
					
					int Att = It.getElementDefAttr(element);
					
					if(EnchantPower <= Att){
						central.msgbox("Worg Elemental Power", player);
						doIt = false;
					}

					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
						return;
					}
					
					if(doIt){
						opera.removeItem(general.DONA_ID_ITEM, dc, player);
						setElement(player,element,EnchantPower,Parte);
						central.msgbox("Congratulations, your "+ I_Name +" has a new Elemental power, "+ params.split(" ")[3] + " " + String.valueOf(EnchantPower),player);
					}
					getArmorEnchantWindows(player);			
				}
			}else if(params.split(" ")[0].equals("En_E_Weapon")){
				int Parte = Integer.valueOf(params.split(" ")[1]);
				int IdObjeto = Integer.valueOf(params.split(" ")[2]);
				byte element = Elementals.getElementId(params.split(" ")[3]);
				int EnchantPower = Integer.valueOf(params.split(" ")[4]);
				
				int dc = getDCFromHashMap(general.DONATION_ELEMENTAL_ITEM_WEAPON ,EnchantPower,player);
				if(dc>0){
					boolean doIt = true;
					L2ItemInstance It = player.getInventory().getPaperdollItem(Parte);
					
					String I_Name = It.getName();
					
					if(It.isEnchantable()==0){
						central.msgbox("This item cant enchant. You change the Item", player);
						doIt=false;
					}else if(It.getObjectId()!=IdObjeto){
						central.msgbox("You change the Item. Enchanting Fail.", player);
						doIt=false;						
					}

					int Att = It.getAttackElementPower();
					
					if(EnchantPower <= Att){
						central.msgbox("Worg Elemental Power", player);
						doIt = false;
					}
					
					
					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
					}
					
					if(doIt){
						opera.removeItem(general.DONA_ID_ITEM, dc, player);
						setElement(player,element,EnchantPower,Parte);
						central.msgbox("Congratulations, your "+ I_Name +" has a new Elemental power, "+ params.split(" ")[3] + " " + String.valueOf(EnchantPower),player);
					}
					getArmorEnchantWindows(player);
				}				
			}else if(params.split(" ")[0].equals("EnWeapon")){
				int Slot = Integer.valueOf(params.split(" ")[1]);
				int idObj = Integer.valueOf(params.split(" ")[2]);
				int Enchant = Integer.valueOf(params.split(" ")[3]);
				int dc = getDCFromHashMap(general.DONATION_ENCHANT_ITEM_WEAPON,Enchant,player);
				if(dc>0){
					boolean doIt = true;
					L2ItemInstance It = player.getInventory().getPaperdollItem(Slot);
					
					if(It.isEnchantable()==0){
						central.msgbox("This item cant enchant. You change the Item", player);
						doIt=false;
					}else if(It.getEnchantLevel() == Enchant){
						central.msgbox("You have the same Selected Enchant on you item", player);
						doIt=false;
					}else if(It.getObjectId()!=idObj){
						central.msgbox("You change the Item. Enchanting Fail.", player);
						doIt=false;						
					}

					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
						return;
					}
					
					if(doIt){
						opera.removeItem(general.DONA_ID_ITEM, dc, player);
						It.setEnchantLevel( Enchant );
						player.broadcastUserInfo();
						player.getInventory().reloadEquippedItems();
						String I_Name = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Slot).getItem().getId());
						central.msgbox("Congratulations, your "+ I_Name +" has a new enchant of "+String.valueOf(Enchant),player);
					}
					getArmorEnchantWindows(player);
				}
				
			}else if(params.split(" ")[0].equals("EnArmor")){
				int Slot = Integer.valueOf(params.split(" ")[1]);
				int idObj = Integer.valueOf(params.split(" ")[2]);
				int Enchant = Integer.valueOf(params.split(" ")[3]);
				int dc = getDCFromHashMap(general.DONATION_ENCHANT_ITEM_ARMOR,Enchant,player);
				if(dc>0){
					
					L2ItemInstance It = player.getInventory().getPaperdollItem(Slot);
					boolean doIt = true;
					if(It.isEnchantable()==0){
						central.msgbox("This item cant enchant. You change the Item", player);
						doIt=false;
					}else if(It.getEnchantLevel() == Enchant){
						central.msgbox("You have the same Selected Enchant on you item", player);
						doIt=false;
					}else if(It.getObjectId()!=idObj){
						central.msgbox("You change the Item. Enchanting Fail.", player);
						doIt=false;						
					}

					if(!opera.haveDonationItem(player, dc)){
						doIt=false;						
						return;
					}
					
					if(doIt){
						opera.removeItem(general.DONA_ID_ITEM, dc, player);
						It.setEnchantLevel( Enchant );
						player.broadcastUserInfo();
						player.getInventory().reloadEquippedItems();
						String I_Name = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Slot).getItem().getId());
						central.msgbox("Congratulations, your "+ I_Name +" has a new enchant of "+String.valueOf(Enchant),player);
					}
					getArmorEnchantWindows(player);
				}
			}else if(params.split(" ")[0].equals("CHANGE_CHAR_NAME")){
				if(general.DONATION_CHANGE_CHAR_NAME_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return;				
				}
				if(!opera.isValidName(params.split(" ")[1])){
					central.msgbox(msg.NOMBRE_INGRESADO_NO_ES_VALIDO,player);
					return;
				}
				
				if(!opera.haveDonationItem(player, general.DONATION_CHANGE_CHAR_NAME_COST)){
					return;
				}
				
				if(cambionombre.Procedimiento_CambiarNombre_Char(player, params.split(" ")[1])){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_CHAR_NAME_COST, player);
					cbFormato.cerrarCB(player);
				}				
			}else if(params.split(" ")[0].equals("CHANGE_CLAN_NAME")){
				if(general.DONATION_CHANGE_CLAN_NAME_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return;	
				}
				if(!opera.haveDonationItem(player, general.DONATION_CHANGE_CLAN_NAME_COST)){
					return;
				}
				if(!opera.isValidName(params.split(" ")[1])){
					central.msgbox(msg.NOMBRE_INGRESADO_NO_ES_VALIDO,player);
					return;
				}				
				
				if(clanNomCambio.changeNameClan(params.split(" ")[1], player)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_CLAN_NAME_COST, player);
					cbFormato.cerrarCB(player);
				}				
			}else if(params.split(" ")[0].equals("CHAR_LEVEL")){
				int LevelRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromHashMap(general.DONATION_CHARACTERS_LEVEL,LevelRequest,player);
				if(dc>0){
					if(player.getLevel()>=LevelRequest){
						central.msgbox("Wrog Level. Please Check", player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					if(LevelRequest!=85){
						opera.setLevel(player,LevelRequest);
					}else{
						opera.set85(player);
					}
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();
					central.msgbox("Congrats!! Your new level is " + LevelRequest, player);
				}
				
			}else if(params.split(" ")[0].equals("CHAR_FAME")){
				int FameRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromHashMap(general.DONATION_CHARACTERS_FAME_POINT,FameRequest,player);
				if(dc>0){
					if(player.getFame() == Config.MAX_PERSONAL_FAME_POINTS || ( (player.getFame() + FameRequest) > Config.MAX_PERSONAL_FAME_POINTS )){
						central.msgbox("Wrog Fame. Please Check", player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM,dc,player);
					player.setFame( player.getFame() + FameRequest );
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();
					central.msgbox("Congrats!! Your adquire " + FameRequest + " of Fame.", player);
				}
			}else if(params.split(" ")[0].equals("CHAR_PK")){
				int pkReduceRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromHashMap(general.DONATION_CHARACTERS_PK_POINT,pkReduceRequest,player);
				if(dc>0){
					if(player.getPkKills()==0 || ( (player.getPkKills() - pkReduceRequest)< 0 )){
						central.msgbox("Wrog Pk Reduce Quantity", player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM,dc,player);
					player.setPkKills( player.getPkKills() - pkReduceRequest );
					player.broadcastUserInfo();
					player.broadcastStatusUpdate();
					central.msgbox("Congrats!! Your pk has reduce to " + player.getPkKills(), player);
				}
			}else if(params.split(" ")[0].equals("CLAN_LEVEL")){
				int clanLvl = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromHashMap(general.DONATION_CLAN_LEVEL,clanLvl,player);
				if(dc>0){
					if(!player.isClanLeader()){
						central.msgbox("Only the clan Leade can lv the clan", player);
						return;
					}
					if(player.getClan().getLevel() >= clanLvl){
						central.msgbox("Wrog Clan Lv.", player);
						return;						
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					player.getClan().setLevel(clanLvl);
					player.getClan().broadcastClanStatus();
					player.getClan().broadcastToOnlineMembers(null);
					central.msgbox("Congrats!! Your clan has increase to " + clanLvl, player);
				}
			}else if(params.split(" ")[0].equals("CLAN_REPUTATION")){
				int clanReput = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromHashMap(general.DONATION_CLAN_REPUTATION,clanReput,player);
				if(dc>0){
					if(player.getClan()==null){
						central.msgbox("You need to be in a Clan", player);
						return;
					}
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					player.getClan().addReputationScore(clanReput, true);
					player.getClan().broadcastClanStatus();
					player.getClan().broadcastToOnlineMembers(null);
					central.msgbox("Congrats!! You adquire " + clanReput + " Reputation", player);
				}				
			}else if(params.split(" ")[0].equals("CLAN_SKILL")){
				int lvlClanRequest = Integer.valueOf(params.split(" ")[1]);
				int dc = getDCFromHashMap(general.DONATION_CLAN_SKILL,lvlClanRequest,player);
				if(dc>0){
					if(player.getClan()==null){
						central.msgbox("You need to be a Clan leader to this action", player);
						return;
					}
					if(!player.isClanLeader()){
						central.msgbox("You need to be a Clan leader to this action", player);
						return;						
					}
					if(player.getClan().getLevel() < lvlClanRequest){
						central.msgbox("Wrog input Data", player);
						return;
					}
					
					if(!opera.haveDonationItem(player, dc)){
						return;
					}
					
					opera.removeItem(general.DONA_ID_ITEM, dc, player);
					
					final L2Clan clan = player.getClan();
					final Map<Integer, L2SkillLearn> _skills = SkillTreesData.getInstance().getMaxPledgeSkills(clan, true);
					int Contador = 0;
					for (L2SkillLearn s : _skills.values())
					{
						if(lvlClanRequest >= s.getGetLevel()){
							final Skill oldSkill = clan.getSkills().get(s.getSkillId());
							if ((oldSkill == null) || (oldSkill.getLevel() < s.getSkillLevel())){
								clan.addNewSkill(SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel()));
								Contador++;
							}
						}
					}
					
					// Notify target and active char
					clan.broadcastToOnlineMembers(new PledgeSkillList(clan));
					for (L2PcInstance member : clan.getOnlineMembers(0))
					{
						member.sendSkillList();
					}
					//activeChar.sendMessage("You gave " + skills.size() + " skills to " + player.getName() + "'s clan " + clan.getName() + ".");
					central.msgbox("Congrats!! Your clan received " + Contador + " skills.", player);					
				}
			}
	}
}
