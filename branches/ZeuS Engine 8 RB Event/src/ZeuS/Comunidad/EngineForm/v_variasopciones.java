package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.event.ClanReputationEvent;
import ZeuS.interfase.Inicial;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.cambionombre;
import ZeuS.interfase.central;
import ZeuS.interfase.clanNomCambio;
import ZeuS.interfase.donaManager;
import ZeuS.procedimientos.opera;

public class v_variasopciones {
	
	private static Logger _log = Logger.getLogger(v_variasopciones.class.getName());
	private static HashMap<Integer,Boolean> IsOcupado = new HashMap<Integer, Boolean>();
	private static HashMap<Integer,Boolean> IsInDonation = new HashMap<Integer, Boolean>();
	
	public enum loadByPass{
		Lv85,
		Noble,
		Fame,
		ClanLv,
		ReducePK,
		SexChange,
		AIONormal,
		AIONormal30,
		ChangeCharName,
		ChangeClanName
	}
	
	private static Vector<String> getNormalItem(String Pide){
		//3470,10;57,1
		Vector<String>vt = new Vector<String>();
		for(String t : Pide.split(";")){
			String Nombre = central.getNombreITEMbyID(Integer.valueOf(t.split(",")[0]));
			String Cantidad = t.split(",")[1];
			vt.add(opera.getFormatNumbers(Cantidad) + " " + Nombre);
		}
		return vt;
	}
	
	/*
	private static boolean isBusy(L2PcInstance player){
		if(IsOcupado!=null){
			if(IsOcupado.containsKey(player.getObjectId())){
				return IsOcupado.get(player.getObjectId());
			}
		}else{
			
		}
	}*/
	
	private static void showWindowsPrice(L2PcInstance player, String ByPassSeccion, boolean showDonation){
		
		Vector<String>Requerimientos = new Vector<String>();
		Vector<String>NormalItem = new Vector<String>();
		Vector<String>DonationCoin = new Vector<String>();
		
		
		if(ByPassSeccion.equals(loadByPass.Lv85.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_LVL85_ITEM_PRICE);
			Requerimientos.add("NO HAVE");
			//DonationCoin.add(String.valueOf(general.DONATION_LV_85_COST));
		}else if(ByPassSeccion.equals(loadByPass.Noble.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE);
			Requerimientos.add("NO HAVE");
			//DonationCoin.add(String.valueOf(general.DONATION_NOBLE_COST));
		}else if(ByPassSeccion.equals(loadByPass.Fame.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_FAME_PRICE);
			Requerimientos.add("NO HAVE");
			//DonationCoin.add(String.valueOf(general.DONATION_FAME_COST));
		}else if(ByPassSeccion.equals(loadByPass.ClanLv.name())){
			NormalItem.add("To Give");
			NormalItem.add("Clan Level " + String.valueOf(general.EVENT_REPUTATION_LVL_TO_GIVE));
			NormalItem.add("Clan Reputation " + String.valueOf(general.EVENT_REPUTATION_REPU_TO_GIVE));
			Requerimientos.add("Min Player:" + String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER));
			Requerimientos.add("All members online:" + (general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE ? "Yes" : "No") );
			//DonationCoin.add(String.valueOf(general.DONATION_CLAN_LV_COST));
		}else if(ByPassSeccion.equals(loadByPass.ReducePK.name())){
			
		}else if(ByPassSeccion.equals(loadByPass.SexChange.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_SEXO_ITEM_PRICE);
			Requerimientos.add("NO HAVE");
			//DonationCoin.add(String.valueOf(general.DONATION_CHANGE_SEX_COST));
		}else if(ByPassSeccion.equals(loadByPass.AIONormal.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE);
			Requerimientos.add("Min Level to Create: " + String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL));
			//DonationCoin.add(String.valueOf(general.DONATION_AIO_CHAR_SIMPLE_COSTO));
		}else if(ByPassSeccion.equals(loadByPass.AIONormal30.name())){
			
		}else if(ByPassSeccion.equals(loadByPass.ChangeCharName.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE);
			Requerimientos.add("Only Noble: " + ( general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE ? "Yes" : "No" ));
			Requerimientos.add("Min Level: " + String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL));
			//DonationCoin.add(String.valueOf(general.DONATION_CHANGE_CHAR_NAME_COST));
		}else if(ByPassSeccion.equals(loadByPass.ChangeClanName.name())){
			NormalItem = getNormalItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE);
			Requerimientos.add("Min Clan Level: " + String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL));
			//DonationCoin.add(String.valueOf(general.DONATION_CHANGE_CLAN_NAME_COST));
		}
		
		
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=CENTER><font color=70FFB3>You Needs</font></td></tr></table>";

		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		
		String ColorBG[] = {"1C1C1C","353535"};
		int Contador = 0;
		
		for(String T : Requerimientos){
			HTML += "<table with=270 border=0 bgcolor="+ ColorBG[Contador%2] +"><tr><td fixwidth=270 align= LEFT><font color=FFFABB>"+ T +"</font><br1></td></tr></table>";
		}
		
		HTML += "<br></td></tr></table><br>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT><font name=hs12 color=8BECFF>Normal Items Request</font></td></tr></table>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		
		for(String T : NormalItem){
			HTML += "<table with=270 border=0 bgcolor="+ ColorBG[Contador%2] +"><tr><td fixwidth=270 align= LEFT><font color=FFFABB>"+ T +"</font><br1></td></tr></table>";
		}
		
		HTML += "<br></td></tr></table><br>";
		
		if(showDonation){
			
			HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT><font name=hs12 color=8BECFF>Donation Coins</font></td></tr></table>";
	
			HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
			
			for(String T : DonationCoin){
				HTML += "<table with=270 border=0 bgcolor="+ ColorBG[Contador%2] +"><tr><td fixwidth=270 align= LEFT><font color=FFFABB>"+ T +"</font><br1></td></tr></table>";
			}
			
			HTML += "<br></td></tr></table>";
		}
		
		HTML += central.getPieHTML(false) + "</body></html>";
		
		central.sendHtml(player, HTML);
		
	}
	
	
	private static String getBoxText_TD(L2PcInstance player, String varText, String Titulo, String Explica, String ByPassDoItNormal,String ByPassDoItDonation, String ByPassInfo, String IdImagen){
		String retorno = "<td fixwidth=220><table width=220 background=L2UI_CT1.Windows_DF_TooltipBG cellpadding=0 cellspacing=3><tr><td fixwidth=32><img src=\""+ IdImagen +"\" width=32 height=32><br><br><br><br><br><br><br><br><br></td><td fixwidth=188 align=LEFT><table width=188 cellpadding=1 cellspacing=1><tr>"+
		"<td fixwidth=188 align=LEFT height=30><table width=188 cellpadding=0 cellspacing=0 border=0><tr><td fixwidth=16 align=LEFT><button action=\""+ ByPassInfo +"\" width=16 height=16 back=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\" fore=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\"></td><td fixwidth=172 align=LEFT height=30>"+
        "<font name=hs12 color=LEVEL>"+ Titulo +"</font></td></tr></table><br1>"+
        Explica + "<br1><center><edit var=\""+ varText +"\" width=150><button value=\"Do It!!\" action=\""+ ByPassDoItNormal +"\" width=150 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1><button value=\"Do It By Donation!\" action=\""+ ByPassDoItDonation +"\" width=150 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center></td></tr></table></td></tr></table></td>";
		return retorno;
	}
	
	private static String getBoxTextWriteByPass_TD(L2PcInstance player, String varText, String Titulo, String Explica, String ByPassDoItNormal,String ByPassDoItDonation, String ByPassInfo, String IdImagen, boolean isDonation){
		String retorno = "<td fixwidth=220><table width=220 background=L2UI_CT1.Windows_DF_TooltipBG cellpadding=0 cellspacing=3><tr><td fixwidth=32><img src=\""+ IdImagen +"\" width=32 height=32><br><br><br><br><br><br><br><br></td><td fixwidth=188 align=LEFT><table width=188 cellpadding=1 cellspacing=1><tr>"+
		"<td fixwidth=188 align=LEFT height=30><table width=188 cellpadding=0 cellspacing=0 border=0><tr><td fixwidth=16 align=LEFT><button action=\""+ ByPassInfo +"\" width=16 height=16 back=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\" fore=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\"></td><td fixwidth=172 align=LEFT height=30>"+
        "<font name=hs12 color=LEVEL>"+ Titulo +"</font></td></tr></table><br1>"+
        Explica + "<br1><center><edit var=\""+ varText +"\" width=150>"+
        ( !isDonation ? "<button value=\"Do It Now!!\" action=\""+ ByPassDoItNormal +"\" width=150 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" : "<button value=\"Do It Now!!!!\" action=\""+ ByPassDoItDonation +"\" width=150 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + "</center></td></tr></table></td></tr></table></td>";
		return retorno;
	}
	
	private static String getBoxSimple_TD(L2PcInstance player, String Titulo, String Explica, String ByPassDoItNormal,String ByPassDoItDonation, String ByPassInfo, String IdImagen, boolean isDonation){
		String retorno = "<td fixwidth=220><table width=220 background=L2UI_CT1.Windows_DF_TooltipBG cellpadding=0 cellspacing=3><tr><td fixwidth=32><img src=\""+ IdImagen +"\" width=32 height=32><br><br><br><br><br><br><br><br></td><td fixwidth=188 align=LEFT><table width=188 cellpadding=1 cellspacing=1><tr>"+
		"<td fixwidth=188 align=LEFT height=30><table width=188 cellpadding=0 cellspacing=0 border=0><tr><td fixwidth=16 align=LEFT><button action=\""+ ByPassInfo +"\" width=16 height=16 back=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\" fore=\"L2UI_CT1.Chatwindow_DF_ItemInfoIcon\"></td><td fixwidth=172 align=LEFT height=30>"+
        "<font name=hs12 color=LEVEL>"+ Titulo +"</font></td></tr></table><br1>"+
        Explica + "<br1><center>"+
        (!isDonation ? "<button value=\"Do It Now!!\" action=\""+ ByPassDoItNormal +"\" width=150 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" : "<button value=\"Do It By Now!!!!\" action=\""+ ByPassDoItDonation +"\" width=150 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + "</center></td></tr></table></td></tr></table></td>";
		return retorno;
	}
	
	
	
	
	private static String getMainWindows(L2PcInstance player, boolean isDonation){
		String InicioTabla = "<table width=720 cellpadding=1 cellspacing=1><tr>";
		
		String retorno = InicioTabla;
		
		String ByPassLv85_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Lv85.name() +";normal;0;0;0;0;0";
		String ByPassLv85_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Lv85.name() +";dona;0;0;0;0;0";
		String ByPassLv85_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.Lv85.name() + ";0;0;0;0;0";
		String ByPassLv85_ima = "icon.skill0219";
		
		String ByPassNoble_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Noble.name() +";normal;0;0;0;0;0";
		String ByPassNoble_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Noble.name() +";dona;0;0;0;0;0";
		String ByPassNoble_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.Noble.name() + ";0;0;0;0;0";
		String ByPassNoble_ima = "icon.skill1323";
		
		String ByPassFame_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Fame.name() +";normal;0;0;0;0;0";
		String ByPassFame_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Fame.name() +";dona;0;0;0;0;0";
		String ByPassFame_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.Fame.name() + ";0;0;0;0;0";
		String ByPassFame_ima = "icon.energy_condenser_i01";
		
		String ByPassClan_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ClanLv.name() +";normal;0;0;0;0;0";
		String ByPassClan_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ClanLv.name() +";dona;0;0;0;0;0";
		String ByPassClan_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ClanLv.name() + ";0;0;0;0;0";
		String ByPassClan_ima = "icon.etc_bloodpledge_point_i00";
		
		String ByPassReducePK_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ReducePK.name() +";normal;0;0;0;0;0";
		String ByPassReducePK_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ReducePK.name() +";dona;0;0;0;0;0";
		String ByPassReducePK_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ReducePK.name() + ";0;0;0;0;0";
		String ByPassReducePK_ima = "icon.etc_quest_pkcount_reward_i00";
		
		String ByPassSex_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.SexChange.name() +";normal;0;0;0;0;0";
		String ByPassSex_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.SexChange.name() +";dona;0;0;0;0;0";
		String ByPassSex_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.SexChange.name() + ";0;0;0;0;0";
		String ByPassSex_ima = "icon.skill6280";
		
		String ByPassAIO_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal.name() +";normal;0;0;0;0;0";
		String ByPassAIO_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal.name() +";dona;0;0;0;0;0";
		String ByPassAIO_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.AIONormal.name() + ";0;0;0;0;0";
		String ByPassAIO_ima = "icon.skill0514";
		
		String ByPassAIO30_normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal30.name() +";normal;0;0;0;0;0";
		String ByPassAIO30_dona = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal30.name() +";dona;0;0;0;0;0";
		String ByPassAIO30_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.AIONormal30.name() + ";0;0;0;0;0";
		String ByPassAIO30_ima = "icon.skill0514";
		
		String ByPassCCName_normal = "Write Z_CHANGE_CHAR_NAME_NORMAL Set _ txtCCName txtCCName txtCCName";
		String ByPassCCName_dona = "Write Z_CHANGE_CHAR_NAME_DONA Set _ txtCCName txtCCName txtCCName";
		String ByPassCCName_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ChangeCharName.name() + ";0;0;0;0;0";
		String ByPassCCName_ima = "icon.skill0516";
		
		String ByPassCCLName_normal = "Write Z_CHANGE_CLAN_NAME_NORMAL Set _ txtCCLName txtCCLName txtCCLName";
		String ByPassCCLName_dona = "Write Z_CHANGE_CLAN_NAME_DONA Set _ txtCCLName txtCCLName txtCCLName";
		String ByPassCCLName_explica = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.charclanoption.name() + ";ShowInfo;" + loadByPass.ChangeClanName.name() + ";0;0;0;0;0";
		String ByPassCCLName_ima = "icon.skill0517";
		
		
		retorno += getBoxSimple_TD(player, "Lv85", "Make you Lv 85!!", ByPassLv85_normal, ByPassLv85_dona, ByPassLv85_explica, ByPassLv85_ima, isDonation);
		retorno += getBoxSimple_TD(player, "Noble", "Make you Noble!!", ByPassNoble_normal , ByPassNoble_dona, ByPassNoble_explica, ByPassNoble_ima, isDonation);
		retorno += getBoxSimple_TD(player, "Fame", "Get Some Fame!!", ByPassFame_normal , ByPassFame_dona, ByPassFame_explica, ByPassFame_ima, isDonation);
		
		retorno += "</tr></table>"+InicioTabla+"<tr>";
		
		retorno += getBoxSimple_TD(player, "Clan Lv", "Level up your Clan!!", ByPassClan_normal , ByPassClan_dona, ByPassClan_explica, ByPassClan_ima, isDonation);
		retorno += getBoxSimple_TD(player, "Reduce PK", "Reduce you PK Counter", ByPassReducePK_normal , ByPassReducePK_dona, ByPassReducePK_explica, ByPassReducePK_ima, isDonation);
		retorno += getBoxSimple_TD(player, "Sex Change", "Change your Char Sex", ByPassSex_normal , ByPassSex_dona, ByPassSex_explica, ByPassSex_ima, isDonation);
		
		retorno += "</tr></table>"+InicioTabla+"<tr>";
		
		retorno += getBoxSimple_TD(player, "AIO Buffer Char", "Create AIO Buffer Char!!", ByPassAIO_normal , ByPassAIO_dona, ByPassAIO_explica, ByPassAIO_ima, isDonation);
		retorno += getBoxSimple_TD(player, "AIO Buffer Char +30", "Create AIO Buffer Char +30!!", ByPassAIO30_normal , ByPassAIO30_dona, ByPassAIO30_explica, ByPassAIO30_ima, isDonation);
		retorno += getBoxTextWriteByPass_TD(player,"txtCCName", "Change Your Name","Change Your Name!!", ByPassCCName_normal , ByPassCCName_dona, ByPassCCName_explica, ByPassCCName_ima, isDonation);
		
		retorno += "</tr></table>"+InicioTabla+"<tr>";
		
		retorno += getBoxTextWriteByPass_TD(player,"txtCCLName", "Change Clan Name", "Change Clan Name!!", ByPassCCLName_normal , ByPassCCLName_dona, ByPassCCLName_explica, ByPassCCLName_ima, isDonation);
		retorno += "<td fixwidth=220></td>";
		retorno += "<td fixwidth=220></td>";
		retorno += "</tr></table><br><br>";
		
		return retorno;
	}
	
	
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.charclanoption.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += getMainWindows(player, IsInDonation.get(player.getObjectId()));
		
		retorno += "</body></html>";
		return retorno;
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
			IsInDonation.put(player.getObjectId(),false);
			return mainHtml(player,params);
		}else if(parm1.equals("1")){
			IsInDonation.put(player.getObjectId(),true);
			return mainHtml(player,params);			
		}else if(parm1.equals("ShowInfo")){
			showWindowsPrice(player, parm2,false);
		}else if(parm1.equals(loadByPass.Lv85.name())){
			int LvPlayer = player.getLevel();
			if(LvPlayer>=85){
				central.msgbox("You already have level 85.", player);
				return "";
			}
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_LVL85){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";
				}else{
					if(opera.haveItem(player, general.OPCIONES_CHAR_LVL85_ITEM_PRICE)){
						opera.removeItem(general.OPCIONES_CHAR_LVL85_ITEM_PRICE, player);
						opera.set85(player);
						cbFormato.cerrarCB(player);
					}else{
						return "";
					}
				}
			}else if(parm2.equals("dona")){
				/*if(general.DONATION_LV_85_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";
				}
				if(!opera.haveDonationItem(player, general.DONATION_LV_85_COST)){
					return "";
				}else{
					opera.removeItem(general.DONA_ID_ITEM,general.DONATION_LV_85_COST,player);
					opera.set85(player);
					cbFormato.cerrarCB(player);
				}
				*/
			}
		}else if(parm1.equals(loadByPass.Noble.name())){
			if(player.isNoble()){
				central.msgbox("You are noble!", player);
				return "";
			}
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_NOBLE){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";
				}
				if(player.isNoble()){
					central.msgbox("You are noble!", player);
					return "";
				}
				if(opera.haveItem(player, general.OPCIONES_CHAR_NOBLE_ITEM_PRICE)){
					opera.removeItem(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE, player);
					opera.setNoble(player);
					cbFormato.cerrarCB(player);
				}else{
					return "";
				}
				
			}else if(parm2.equals("dona")){
				/*if(general.DONATION_NOBLE_COST==0){
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
					cbFormato.cerrarCB(player);
				}else{
					return "";
				}*/
			}
		}else if(parm1.equals(loadByPass.Fame.name())){
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_FAME){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";					
				}else{
					if(general.OPCIONES_CHAR_FAME_NOBLE && !player.isNoble()){
						central.msgbox(msg.NECESITAS_SER_NOBLE_PARA_ESTA_OPERACION, player);
						return "";
					}
					if(general.OPCIONES_CHAR_FAME_LVL> player.getLevel()){
						central.msgbox(msg.NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION.replace("$level", String.valueOf(general.OPCIONES_CHAR_FAME_LVL)), player);
						return "";
					}
					if(opera.haveItem(player, general.OPCIONES_CHAR_FAME_PRICE)){
						opera.removeItem(general.OPCIONES_CHAR_FAME_PRICE, player);
						player.setFame( player.getFame() + general.OPCIONES_CHAR_FAME_GIVE );
						player.broadcastStatusUpdate();
						player.broadcastInfo();
						central.msgbox("You acquired "+ String.valueOf(general.OPCIONES_CHAR_FAME_GIVE) +" of fame" , player);
						return "";
					}else{
						return "";
					}
				}
			}else if(parm2.equals("dona")){
				/*if(general.DONATION_FAME_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";					
				}else{
					if(opera.haveDonationItem(player, general.DONATION_FAME_COST)){
						opera.removeItem(general.DONA_ID_ITEM, general.DONATION_FAME_COST, player);
						player.setFame(player.getFame() + general.DONATION_FAME_AMOUNT);
						player.broadcastStatusUpdate();
						player.broadcastInfo();
						central.msgbox("You acquired "+ String.valueOf(general.DONATION_FAME_AMOUNT) +" of fame" , player);
						return "";						
					}else{
						return "";
					}
				}*/
			}
		}else if(parm1.equals(loadByPass.ClanLv.name())){
			if(parm2.equals("normal")){
				if(!general.EVENT_REPUTATION_CLAN){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";						
				}
				if(!ClanReputationEvent.GiveReward(player)){
					return "";
				}else{
					cbFormato.cerrarCB(player);
				}
				return "";
			}else if(parm2.equals("dona")){
			/*	if(general.DONATION_CLAN_LV_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";						
				}
				if(player.getClan()!=null){
					if(!player.isClanLeader()){
						central.msgbox("Just the Clan leader can lvl up the clan", player);
					}else{
						if(opera.haveDonationItem(player, general.DONATION_CLAN_LV_COST)){
							player.getClan().changeLevel(general.DONATION_CLAN_LV_LV);
							player.getClan().updateClanInDB();
							player.getClan().broadcastClanStatus();
							ClanReputationEvent.RewardEfect(player);
							central.msgbox("Congratulation!!! You clan has been increased", player);
							cbFormato.cerrarCB(player);
						}
					}
				}else{
					central.msgbox("You need a clan for do this", player);
				}
				return "";*/
			}
		}else if(parm1.equals(loadByPass.ReducePK.name())){
			if(parm2.equals("normal")){
				
			}else if(parm2.equals("dona")){
				
			}
		}else if(parm1.equals(loadByPass.SexChange.name())){
			if(parm2.equals("normal")){
				if(general.OPCIONES_CHAR_SEXO){
					if(opera.haveItem(player, general.OPCIONES_CHAR_SEXO_ITEM_PRICE)){
						opera.removeItem(general.OPCIONES_CHAR_SEXO_ITEM_PRICE, player);
						opera.changeSex(player);
					}
				}else{
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";						
				}
				return "";
			}else if(parm2.equals("dona")){
				if(general.DONATION_CHANGE_SEX_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";
				}
				if(opera.haveDonationItem(player, general.DONATION_CHANGE_SEX_COST)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_SEX_COST, player);
					opera.changeSex(player);
				}
				return "";
			}
		}else if(parm1.equals(loadByPass.AIONormal.name())){
			if(player.getName().startsWith("[BUFF]")){
				central.msgbox("You cant do this again", player);
				return "";
			}
			
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_BUFFER_AIO){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";					
				}
				if(!opera.haveItem(player, general.OPCIONES_CHAR_BUFFER_AIO_PRICE)){
					return "";
				}
				
				if(general.OPCIONES_CHAR_BUFFER_AIO_LVL > player.getLevel()){
					central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL)), player);
					return "";
				}

				if(aioChar.setNewAIO(player, true)){
					opera.removeItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE, player);
					cbFormato.cerrarCB(player);
				}
				return "";
				
			}else if(parm2.equals("dona")){
			/*	if(general.DONATION_AIO_CHAR_SIMPLE_COSTO==0){
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
				return "";*/
			}
		}else if(parm1.equals(loadByPass.AIONormal30.name())){
			if(player.getName().startsWith("[BUFF]")){
				central.msgbox("You cant do this again", player);
				return "";
			}			
			if(parm2.equals("normal")){
				if(!general.OPCIONES_CHAR_BUFFER_AIO_30){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";					
				}
				if(!opera.haveItem(player, general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30)){
					return "";
				}
				
				if(general.OPCIONES_CHAR_BUFFER_AIO_LVL > player.getLevel()){
					central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL)), player);
					return "";
				}
				if(aioChar.setNewAIO(player, false)){
					opera.removeItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30, player);
					cbFormato.cerrarCB(player);
				}
				return "";
				
			}else if(parm2.equals("dona")){/*
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
				}*/
				return "";
			}
		}else if(parm1.equals(loadByPass.ChangeCharName.name())){
			if(parm2.equals("normal")){
				if(!general.BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";						
				}
				if(!opera.haveItem(player, general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE)){
					return "";
				}
				if(general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE && !player.isNoble()){
					central.msgbox(msg.NECESITAS_SER_NOBLE_PARA_ESTA_OPERACION, player);
					return "";
				}
				if(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL > player.getLevel()){
					central.msgbox(msg.NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION.replace("$level", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL)), player);
					return "";
				}
				
				if(!opera.isValidName(parm3)){
					central.msgbox(msg.NOMBRE_INGRESADO_NO_ES_VALIDO,player);
					return "";
				}
				
				if(cambionombre.Procedimiento_CambiarNombre_Char(player, parm3)){
					opera.removeItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE, player);
					cbFormato.cerrarCB(player);
				}
				
				
			}else if(parm2.equals("dona")){
				if(general.DONATION_CHANGE_CHAR_NAME_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";					
				}
				if(!opera.isValidName(parm3)){
					central.msgbox(msg.NOMBRE_INGRESADO_NO_ES_VALIDO,player);
					return "";
				}
				
				if(!opera.haveDonationItem(player, general.DONATION_CHANGE_CHAR_NAME_COST)){
					return "";
				}
				
				if(cambionombre.Procedimiento_CambiarNombre_Char(player, parm3)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_CHAR_NAME_COST, player);
					cbFormato.cerrarCB(player);
				}
			}
		}else if(parm1.equals(loadByPass.ChangeClanName.name())){
			if(parm2.equals("normal")){
				if(!general.BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";						
				}
				
				if(!player.isClanLeader()){
					central.msgbox(msg.SOLO_LIDER_DE_CLAN_PUEDE_REALIZAR_ESTA_OPERACION, player);
					return "";
				}
				if(player.getClan().getLevel() <= general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL){
					central.msgbox(msg.NECESITAS_TENER_UN_LEVEL_DE_CLAN_IGUAL_O_SUPERIOR_A_$level.replace("$level", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL)), player);
					return "";
				}
				
				if(!opera.haveItem(player, general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE)){
					return "";
				}
				
				if(clanNomCambio.changeNameClan(parm3, player)){
					opera.removeItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE, player);
					cbFormato.cerrarCB(player);
				}
				
			}else if(parm2.equals("dona")){
				if(general.DONATION_CHANGE_CLAN_NAME_COST==0){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return "";	
				}
				if(!opera.haveDonationItem(player, general.DONATION_CHANGE_CLAN_NAME_COST)){
					return "";
				}
				
				if(clanNomCambio.changeNameClan(parm3, player)){
					opera.removeItem(general.DONA_ID_ITEM, general.DONATION_CHANGE_CLAN_NAME_COST, player);
					cbFormato.cerrarCB(player);
				}
			}
		}
		return "";
	}
}
