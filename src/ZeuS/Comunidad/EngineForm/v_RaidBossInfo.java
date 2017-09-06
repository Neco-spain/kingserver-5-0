package ZeuS.Comunidad.EngineForm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;
import ZeuS.procedimientos.ObserveMode;
import ZeuS.procedimientos.opera;

public class v_RaidBossInfo{
	
	private static Logger _log = Logger.getLogger(v_RaidBossInfo.class.getName());

	private static int Maximo = 15;
	
	private static HashMap<Integer,String>PALABRA_BUSCAR = new HashMap<Integer, String>();
	private static HashMap<Integer,Boolean>FROM_MENU = new HashMap<Integer, Boolean>();
	
	private static HashMap<Integer,String>PaginaAntes = new HashMap<Integer, String>();
	
	
	private static String getInfoNPC(L2PcInstance player, int idMonster){
		String retorno = "";
		
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(idMonster);
		
		
		L2Npc Monster = null;
		
		for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(idMonster)){
			if(Monster==null){
				Monster = SpawnLo.getLastSpawn();
			}
		}
		int LevelMonster = Monster.getLevel();
		String Nombre = Monster.getName();
		
		String x = String.valueOf(Monster.getLocation().getX());
		String y = String.valueOf(Monster.getLocation().getY());
		String z = String.valueOf(Monster.getLocation().getZ());
		
		retorno = "<table width=820 background=L2UI_CT1.Windows_DF_TooltipBG  cellpadding=3 cellspacing=3><tr><td>"+
		        "Npc Name: <font name=\"hs12\" color=CCFFCC>"+Nombre+"</font> <font color=5B5B5B>Lv: "+ String.valueOf(LevelMonster) +"</font></td></tr></table>";

		retorno += "<center><br><br><table width=720><tr><td fixwidth=360><table fixwidth=360 background=\"L2UI_CT1.Windows_DF_Drawer_Bg\" cellpadding=5><tr>"+
				"<td><table fixwidth=390 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr><td fixwidth=390 align=RIGHT><font name=\"hs12\"color=\"D1D296\">Basic Information</font></td></tr></table>"+
				"<table fixwidth=390 cellspacing=2 cellpadding=0 bgcolor=\"0C0D29\" height=20>";
		
		int contador = 0;
		
		
/*		
		for(String Datos : v_dropsearch.getMonsterInfo(idMonster,tmpl,Monster)){
			if(contador==0){
				retorno += "<tr>";
			}
			retorno += "<td fixwidth=115 align=LEFT>"+Datos.split(":")[0]+":</td>"+
            "<td fixwidth=100 align=RIGHT><font color=A36947>"+ opera.getFormatNumbers(Datos.split(":")[1]) +"</font></td>";

			contador++;
			
			if(contador==2){
				contador=0;
				retorno += "</tr>";
			}
		}
*/		
		if(contador==1){
			retorno += "<td></td></tr>";
		}
		
		retorno += "</table><br>";
		
		retorno += "<table fixwidth=390 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr><td fixwidth=390 align=RIGHT><font name=\"hs12\"color=\"D1D296\">Stats</font></td></tr>"+
        "</table><table fixwidth=360 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr>";
		
		contador = 0;
/*		
		for(String Datos : v_dropsearch.getMonsterDyes(Monster)){
			if(contador==0){
				retorno += "<tr>";
			}
			retorno += "<td fixwidth=140 align=LEFT>"+Datos.split(":")[0]+":</td>"+
            "<td fixwidth=40 align=RIGHT><font color=A36947>"+ Datos.split(":")[1] +"</font></td>";

			contador++;
			
			if(contador==2){
				contador=0;
				retorno += "</tr>";
			}
		}
*/		
		if(contador==1){
			retorno += "<td></td></tr>";
		}
		
		retorno += "</table><br>";
		
		retorno += "<table fixwidth=390 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr><td fixwidth=390 align=RIGHT><font name=\"hs12\"color=\"D1D296\">Attribute Value Def</font></td></tr>"+
		        "</table><table fixwidth=360 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr>";
				
		contador = 0;
/*		
		for(String Datos : v_dropsearch.getMonsterElements(Monster)){
			if(contador==0){
				retorno += "<tr>";
			}
			retorno += "<td fixwidth=140 align=LEFT>"+Datos.split(":")[0]+":</td>"+
            "<td fixwidth=40 align=RIGHT><font color=A36947>"+ Datos.split(":")[1] +"</font></td>";

			contador++;
			
			if(contador==2){
				contador=0;
				retorno += "</tr>";
			}
		}
*/		
		if(contador==1){
			retorno += "<td></td></tr>";
		}
		retorno += "</table><br>";

		retorno += "</td>";
		
		String bypass_ShowDrop = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" +Engine.enumBypass.RaidBossInfo.name()+";showDrop;"+ String.valueOf(idMonster) +";0;0;0;0;0" ;
		String bypass_ShowSkill = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+ ";ShowNPCSkill;" + String.valueOf(idMonster)+";0;0;0;0;0";
		String bypass_TeleportMe = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.RaidBossInfo.name()+";teleport;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		String bypass_ObserveMode = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.RaidBossInfo.name()+";Observe;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		
		retorno += "<td fixwidth=360><table width=360 background=\"L2UI_CT1.Windows_DF_Drawer_Bg\"><tr><td><table fixwidth=360 cellpadding=5><tr><td fixwidth=180 align=CENTER>"+
        "<button value=\"Show Drop\" action=\""+ bypass_ShowDrop +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td fixwidth=180 align=CENTER>"+
        "<button value=\"Show Skill\" action=\""+ bypass_ShowSkill +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><table fixwidth=360 cellpadding=5><tr><td fixwidth=360 align=CENTER>"+
        "<button value=\"Teleport Me\" action=\""+ bypass_TeleportMe +"\" width=250 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>"+
        "<button value=\"I Want to Observe\" action=\""+ bypass_ObserveMode +"\" width=250 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br></td></tr></table></td>";
		
		retorno +="</tr></table><br><br></td></tr></table></center>";
		
		
		return retorno;
	}
	
	
	
	private static HashMap<Integer,HashMap<String,String>> loadRaidInfo(int pagina, String nombre){
		int Desde = pagina * Maximo;

		HashMap<Integer,HashMap<String,String>> RaidInfo = new HashMap<Integer,HashMap<String,String>>();

		String Consulta = "call sp_get_raidboss_info(2,"+ String.valueOf(Desde) +","+ String.valueOf(Maximo + 1) +",'"+ nombre +"')";
		Connection conn = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(Consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				RaidInfo.put(rss.getInt(2), new HashMap<String,String>());
				RaidInfo.get(rss.getInt(2)).put("NOMBRE", rss.getString(1).split("-")[0]);
				RaidInfo.get(rss.getInt(2)).put("LEVEL", rss.getString(1).split("-")[1]);
				RaidInfo.get(rss.getInt(2)).put("X", rss.getString(4));
				RaidInfo.get(rss.getInt(2)).put("Y", rss.getString(5));
				RaidInfo.get(rss.getInt(2)).put("Z", rss.getString(6));
				RaidInfo.get(rss.getInt(2)).put("BOSSID", rss.getString(2));
				RaidInfo.get(rss.getInt(2)).put("RESPAWN", rss.getString(3));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{try{conn.close();}catch(SQLException a){}}
		return RaidInfo;
	}
	
	
	private static String getGrillaRb(int pagina,String buscar){
		String retorno = "<table width=640 border=0 cellpadding=0 cellspacing=0><tr><td><button value=\"Raid Boss Name\" action=\"\" width=340 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\">"+
				"</td><td><button value=\"Level\" action=\"\" width=60 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td><td><button value=\"Status\" action=\"\" width=150 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\">"+
				"</td><td><button value=\"\" action=\"\" width=90 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td></tr></table>";
		
		
		String []Colores = {"3B240B","3B170B"};
		
		// player, parametro, idnpc, pagina
		String Bypass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.RaidBossInfo.name() + ";shownpcinfo;%IDNPC%;" + String.valueOf(pagina) + ";0;0;0" ;
		int Contador = 0;
		boolean HaveMore = false;
		Iterator itr = loadRaidInfo(pagina,buscar).entrySet().iterator();
		while(itr.hasNext() && !HaveMore){
			if(Contador < Maximo){
				Map.Entry Info_E =(Map.Entry)itr.next();
				HashMap<String,String> RAID = (HashMap<String, String>) Info_E.getValue();
				String Nombre = RAID.get("NOMBRE");
				String Level = RAID.get("LEVEL");
				String X = RAID.get("X");
				String Y = RAID.get("Y");
				String Z = RAID.get("Z");
				String BOSSID = RAID.get("BOSSID");
				String RESPAWN = RAID.get("RESPAWN");
				
				String EstadoRb = RESPAWN.equalsIgnoreCase("0") ? "<font color=A9F5A9>Alive</font>" :  "<font color=F78181>Death</font>" ; 
				
				retorno += "<table width=640 border=0 cellpadding=0 cellspacing=0 bgcolor="+ Colores[Contador % 2] +" height=43><tr><td fixwidth=340 align=CENTER>"+
	            "<font color=81F7F3>"+ Nombre +"</font>"+ ( !RESPAWN.equalsIgnoreCase("0") ? "<br1>"+RESPAWN : "" ) + "</td>"+
	            "<td fixwidth=60 align=CENTER>"+ Level +"</td><td fixwidth=150 align=CENTER>"+
	            "<font color=A9F5A9>"+ EstadoRb +"</font></td>"+
	            "<td fixwidth=90 align=CENTER><button value=\"Information\" action=\""+ Bypass.replace("%IDNPC%", BOSSID) +"\" width=90 height=38 back=\"L2UI_CT1.Button_DF_Calculator_Over\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
	            "</td></tr></table>";
				Contador++;
			}else{
				HaveMore = true;
			}
		}
		
		String bypassAntes = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.RaidBossInfo + ";Listar;" +String.valueOf(pagina-1)+";0;0;0;0"; 
		String bypassProx = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.RaidBossInfo + ";Listar;" +String.valueOf(pagina+1)+";0;0;0;0";
		
		String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">";
		String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">";
		
		if(HaveMore || pagina>0){
			retorno += "<table fixwith=620 cellspacing=-1 cellpadding=-1 bgcolor=\""+Colores[Contador % 2]+"\" height=35><tr><td width=70 fixwidth=70 align=CENTER></td><td width=570 fiwidth=570 align=CENTER>"+
            "<table><tr><td>"+
			(pagina > 0 ? btnAntes : "") +
            "</td><td width=50 align=CENTER><font name=\"hs12\"> "+ String.valueOf(pagina + 1) +" </font></td><td>"+
            (HaveMore ? btnProx : "")+
            "</td></tr></table></td><td width=100 fixwidth=100 height=32></td></tr></table>";
		}
		
		
		retorno += "</center>";
		
		return retorno;
	}
	
	
	private static String getChance(String Dato){
		long lg;
		lg = Long.parseLong(Dato.substring(0,Dato.length()-2));
		return String.valueOf((100 * lg) / 1000000.0);
	}	
	
	
	
	
/*	private static String getMonsterDropList(int idMonster, int pagina){
		
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(idMonster);

		Vector<String>vTemporal = new Vector<String>();
		
		String Retorno = "<center><table fixwidth=690 border=0 cellpadding=0 cellspacing=0>";
		
		
		if (tmpl.getDropData() != null)
		{
			for (L2DropCategory cat : tmpl.getDropData()){
				for(L2DropData DropIn : cat.getAllDrops()){
					String ttt = String.valueOf(DropIn.getId())+":"+String.valueOf(DropIn.getMinDrop())+":"+String.valueOf(DropIn.getMaxDrop())+":"+String.valueOf(DropIn.getChance())+":"+String.valueOf(cat.getCategoryType());
					vTemporal.add(ttt);
				}
			}
		}
		
		
		String GrillaDrop = "<td width=230><table width=230 background=L2UI_CT1.Windows_DF_TooltipBG height=75><tr><td fixwidth=32>"+
			"<table width=32 cellspacing=0><tr><td><img src=\"%IDIMG%\" width=32 height=32></td></tr></table></td><td>"+
			"<table fixwidth=198 border=0 cellspacing=1><tr><td fixwidth=218><font color=B3C624>%NOMBRE%</font></td></tr>"+
			"<tr><td fixwidth=218><font color=2ECCFA>%DROPMIN%</font> | <font color=2E9AFE>%DROPMAX%</font></td></tr>"+
			"<tr><td fixwidth=218><font color=C6D6DA>Chance: </font><font color=8BC7D9>%PORCENTAJE% %TIPODROP%</font></td></tr></table><br></td></tr></table></td>";
		
		
		int max=15;
		int contador=0;
		int contDrop = 0;
		int Desde = pagina * max;
		int Hasta = Desde + max;
		boolean haveNext = false;
		
		for(String Dat : vTemporal){
			if(contDrop>=Desde && contDrop<Hasta){
				String ID_Item = Dat.split(":")[0];
				String Nombre = central.getNombreITEMbyID(Integer.valueOf(ID_Item));
				String IMAGEN_ID = opera.getTemplateItem(Integer.valueOf(ID_Item));
				String MIN = opera.getFormatNumbers(Dat.split(":")[1]);
				String MAX = opera.getFormatNumbers(Dat.split(":")[2]);
				String CHANCE = getChance(Dat.split(":")[3]);
				String CATE = Dat.split(":")[4].equals("-1") ? "SWEET" : "DROP";
				if(contador==0){
					Retorno +="<tr>";
				}
				
				Retorno += GrillaDrop.replace("%IDIMG%", IMAGEN_ID).replace("%NOMBRE%", Nombre).replace("%DROPMIN%", MIN).replace("%DROPMAX%", MAX).replace("%PORCENTAJE%", CHANCE).replace("%TIPODROP%", CATE);
				contador++;
				if(contador==3){
					Retorno += "</tr>";
					contador=0;
				}
			}
			contDrop++;
			if(contDrop>Hasta){
				haveNext=true;
			}			
		}
		
		if(contador>0 && contador<3){
			for(int i=contador;i<3;i++){
				Retorno += "<td fixwidth=32><table width=32 cellspacing=0><tr><td></td></tr></table></td><td>"+
						"<table fixwidth=198 border=0 cellspacing=1><tr><td fixwidth=218><font color=B3C624></font></td></tr><tr><td fixwidth=218><font color=2ECCFA></font><font color=2E9AFE></font></td></tr>"+
						"<tr><td fixwidth=218><font color=C6D6DA></font><font color=8BC7D9></font></td></tr></table><br></td>";
			}
			Retorno += "</tr>";
		}
		
		Retorno += "</table></center>";
		
		
		if(haveNext || pagina>0 ){
			
			String bypassAntes = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.DropSearch.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina-1)+";0;0;0"; 
			String bypassProx = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.DropSearch.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina+1)+";0;0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">";				
			
			String Grilla = "<center><table fixwith=767 cellspacing=-4 cellpadding=-1 height=38 bgcolor=0E180B><tr><td fixwidth=767 align=CENTER><br><table with=96>"+
            "<tr><td width=32>"+ ( pagina>0 ? btnAntes : "") +"</td>"+
                "<td width=32 align=CENTER><font name=\"hs12\">"+ String.valueOf(pagina+1) +"</font></td>"+
                "<td width=32>"+ (haveNext ? btnProx : "") +"</td></tr></table></td></tr></table></center>";
			
			Retorno += Grilla;
		}		
		return Retorno;
	}
		
	*/
	
	
	
	
	
	
	
	
	
	
	private static String getBackBtn(String ByPass,int Pagina,L2PcInstance player,String Busqueda){
		if(ByPass.equals("shownpcinfo")){
			String ByPassWhere = FROM_MENU.get(player.getObjectId()) ? "FromMain" : "Listar";
			return "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.RaidBossInfo.name() + ";" + ByPassWhere + ";" +  String.valueOf(Pagina) + ";0;0;0;0";
		}else if(ByPass.equals("showDrop")){
			return "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.RaidBossInfo.name() + ";shownpcinfo;" + Busqueda  + ";" + PaginaAntes.get(player.getObjectId()) + ";0;0;0;0";
		}
		return "";
	}
	
	private static String mainHtml(L2PcInstance player, String Params,String Buscar, int pagina){
		String par = Engine.enumBypass.RaidBossInfo.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		
		//getTituloCentral(String Icono, String Nombre, String Explica, String ByPassVolverAtras, boolean VolverEngine, String ByPassVolverOtro, boolean ShowBackBtn ){
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica, getBackBtn(Params, pagina, player, Buscar), !FROM_MENU.get(player.getObjectId()),"bypass " + general.COMMUNITY_BOARD_PART_EXEC,true);
		
		String ByPassVoice = "bypass -h voice .Z_DS_S_r_B $txtNombre";
		
		if(Params.equals("0") || Params.equals("FromMain")){
			retorno += cbFormato.getBoxSearch("Raid Boss Search", "Enter the raid boss name to search it", ByPassVoice, "txtNombre", "icon.skill8364");
			retorno += getGrillaRb(pagina,PALABRA_BUSCAR.get(player.getObjectId()));
		}else if(Params.equals("Listar")){
			retorno += cbFormato.getBoxSearch("Raid Boss Search", "Enter the raid boss name to search it", ByPassVoice, "txtNombre", "icon.skill8364");
			retorno += getGrillaRb(pagina,PALABRA_BUSCAR.get(player.getObjectId()));
		}else if(Params.equals("shownpcinfo")){
			PaginaAntes.put(player.getObjectId(), String.valueOf(pagina));
			retorno += getInfoNPC(player, Integer.valueOf(Buscar));
		}//else if(Params.equals("showDrop")){
//			retorno += getMonsterDropList(Integer.valueOf(Buscar), pagina);
//		}
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
		//
		if(parm1.equals("0")){
			FROM_MENU.put(player.getObjectId(), false);
			PALABRA_BUSCAR.put(player.getObjectId(), "");
			return mainHtml(player,parm1,"", Integer.valueOf(parm2));
		}else if(parm1.equals("Listar")){
			return mainHtml(player,parm1,"",Integer.valueOf(parm2));
		}else if(parm1.equals("FromMain")){
			FROM_MENU.put(player.getObjectId(), true);
			PALABRA_BUSCAR.put(player.getObjectId(), "");
			return mainHtml(player,parm1,"", Integer.valueOf(parm2));			
		}else if(parm1.equals("shownpcinfo")){
			// player, parametro, idnpc, pagina
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3)) ;
		}else if(parm1.equals("showDrop")){//mainHtml(player, parm1, parm2, Integer.valueOf(parm3));
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("Observe")){
			if(!general.RAIDBOSS_OBSERVE_MODE){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			if(player.inObserverMode()){
				central.msgbox("You need to close the Actual Observe mode to enter into another", player);
				return "";
			}
			Integer x =  Integer.valueOf(parm3);
			Integer y = Integer.valueOf(parm4);
			Integer z = Integer.valueOf(parm5);			
			Location loc = new Location(x, y, z);
			ObserveMode.EnterObserveMode(player, loc);
			return "";
			
		}else if(parm1.equals("teleport")){
			String idMonster = parm2;
			Integer x =  Integer.valueOf(parm3);
			Integer y = Integer.valueOf(parm4);
			Integer z = Integer.valueOf(parm5);

			if(!general.RAIDBOSS_INFO_TELEPORT){
				central.msgbox(msg.DISABLED_BY_ADMIN, player);
				return "";
			}
			
			if(general.RAIDBOSS_INFO_NOBLE && !player.isNoble()){
				central.msgbox(msg.NECESITAS_SER_NOBLE, player);
				return "";
			}
			
			if(player.getLevel() < general.RAIDBOSS_INFO_LVL){
				central.msgbox(msg.NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION.replace("$level", String.valueOf(general.RAIDBOSS_INFO_LVL)), player);
				return "";
			}
			
			if(general.RAIDBOSS_ID_MOB_NO_TELEPORT.contains(Integer.valueOf(idMonster))){
				central.msgbox("Monster or Raid Bosss is blocked to use Teleport", player);
				return "";
			}
			
			
			opera.removeItem(general.RAIDBOSS_INFO_TELEPORT_PRICE, player);
			player.teleToLocation(x, y, z, true);
			cbFormato.cerrarCB(player);			
		}
		return "";
	}
	
	public static void bypassVoice(L2PcInstance player, String Buscar){
		String enviar = "";
		PALABRA_BUSCAR.put(player.getObjectId(), Buscar);
		enviar = mainHtml(player, "Listar", Buscar, 0);
		cbManager.separateAndSend(enviar, player);
	}
}
