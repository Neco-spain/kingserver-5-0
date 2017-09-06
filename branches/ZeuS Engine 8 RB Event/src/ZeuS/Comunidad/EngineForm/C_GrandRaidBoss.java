package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.procedimientos.opera;

import com.l2jserver.Config;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
import com.l2jserver.gameserver.model.drops.GeneralDropItem;
import com.l2jserver.gameserver.model.drops.GroupedGeneralDropItem;

public class C_GrandRaidBoss {
	

	private static final Logger _log = Logger.getLogger(C_GrandRaidBoss.class.getName());
	
	// Zaken
	private static final int ZAKEN_MIN_LEVEL_DAYTIME_60 = 55;
	private static final int ZAKEN_MIN_MEMBERS_DAYTIME_60 = 9;
	private static final int ZAKEN_MAX_MEMBERS_DAYTIME_60 = 27;
	private static final int ZAKEN_MIN_LEVEL_DAYTIME_83 = 78;
	private static final int ZAKEN_MIN_MEMBERS_DAYTIME_83 = 9;
	private static final int ZAKEN_MAX_MEMBERS_DAYTIME_83 = 27;
	private static final int ZAKEN_MIN_LEVEL_NIGHT = 55;
	private static final int ZAKEN_MIN_MEMBERS_NIGHT = 56;
	private static final int ZAKEN_MAX_MEMBERS_NIGHT = 350;
	
	// Freya
	private static final int FREYA_MIN_LEVEL = 82;
	private static final int FREYA_MAX_PLAYERS = 27;
	private static final int FREYA_MIN_PLAYERS = 10;
	
	// Frintezza
	private static final int FRINTEZZA_MIN_LEVEL = 80;
	private static final int FRINTEZZA_MIN_PLAYERS = 36;
	private static final int FRINTEZZA_MAX_PLAYERS = 45;
	
	private static String getSeccionInstancia_TD(String Nombre,String MinPlayer,String MaxPlayer,String MinLevel,String JChance,String JDropMin,String JDropMax, String idIcon){
		String Retorno ="<td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32><img src=\""+ idIcon +"\" width=32 height=32><br><br><br><br><br><br><br><br></td><td fixwidth=218><table width=208 cellspacing=0 cellpadding=0 border=0 bgcolor=131358 height=30><tr>"+
			"<td fixwidth=205><font color=04E1DA name=\"hs12\">"+ Nombre +"</font></td></tr></table><table width=205 cellspacing=0 cellpadding=0 border=0><tr>"+
			"<td fixwidth=105><font color=01A9DB>Min. Players:</font></td><td fixwidth=100><font color=00D9DC>"+ MinPlayer +"</font></td></tr><tr>"+
			"<td fixwidth=105><font color=01A9DB>Max. Players:</font></td><td fixwidth=100><font color=00D9DC>"+ MaxPlayer +"</font></td></tr><tr>"+
			"<td fixwidth=105><font color=01A9DB>Min Level:</font></td><td fixwidth=100><font color=00D9DC>"+ MinLevel +"</font></td></tr><tr>"+
			"<td fixwidth=105><font color=01A9DB>Jewel Chance:</font></td><td fixwidth=100><font color=00D9DC>"+ JChance +"%</font></td></tr><tr>"+
			"<td fixwidth=105><font color=01A9DB>Jewel Drop:</font></td><td fixwidth=100><font color=00D9DC>"+ JDropMin +" / "+ JDropMax + " <font color=3F3F3F>(Min/Max)</font></font></td></tr>"+
			"</table><br></td></tr></table></td>";
		
		return Retorno;
	}
	
	private static String getSeccion_TD(String Nombre,String ResTime,String ResDe,String JChance,String JDropMin,String JDropMax, String idIcon){
		String Retorno ="<td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32><img src=\""+ idIcon +"\" width=32 height=32><br><br><br><br><br><br><br></td><td fixwidth=218><table width=208 cellspacing=0 cellpadding=0 border=0 bgcolor=131358 height=30><tr>"+
			"<td fixwidth=205><font color=04E1DA name=\"hs12\">"+ Nombre +"</font></td></tr></table><table width=205 cellspacing=0 cellpadding=0 border=0><tr><td fixwidth=105><font color=01A9DB>Respawn Time:</font></td>"+
            "<td fixwidth=100><font color=00D9DC>"+ ResTime +"</font></td></tr><tr><td fixwidth=105><font color=01A9DB>Respawn Delay:</font></td>"+
            "<td fixwidth=100><font color=00D9DC>+-"+ ResDe +"</font></td></tr><tr><td fixwidth=105><font color=01A9DB>Jewel Chance:</font></td>"+
            "<td fixwidth=100><font color=00D9DC>"+ JChance +"%</font></td></tr><tr><td fixwidth=105><font color=01A9DB>Jewel Drop:</font></td>"+
            "<td fixwidth=100><font color=00D9DC>"+ JDropMin +" / "+ JDropMax + " <font color=3F3F3F>(Min/Max)</font></font></td></tr></table><br></td></tr></table></td>";
		
		return Retorno;
	}
	
	private static String getDatoRamdonRespawn(int Minutos){
		if(Minutos < 60){
			return String.valueOf(Minutos) + " Minutes";
		}else{
			return String.valueOf( Minutos / 60 ) + " Hours";
		}
	}
	
	private static String getDatoRespawn(int hour){
		if(hour<48){
			return String.valueOf(hour)+ " Hrs";
		}else{
			return String.valueOf( (hour / 24) ) + " Days";
		}
	}
	private static String get_RamdonTime(String nombre){
		switch(nombre.toUpperCase()){
			case "VALAKAS":
				return getDatoRamdonRespawn(Config.VALAKAS_SPAWN_RANDOM);
			case "QUEEN ANT":
				return getDatoRamdonRespawn(Config.QUEEN_ANT_SPAWN_RANDOM);
			case "CORE":
				return getDatoRamdonRespawn(Config.CORE_SPAWN_RANDOM);
			case "ORFEN":
				return getDatoRamdonRespawn(Config.ORFEN_SPAWN_RANDOM);
			case "BAIUM":
				return getDatoRamdonRespawn(Config.BAIUM_SPAWN_RANDOM);
			case "ANTHARAS":
				return getDatoRamdonRespawn(Config.ANTHARAS_SPAWN_RANDOM);
		}
		return "";
	}
	
	
	private static String get_RespawnTime(String nombre){
		switch(nombre.toUpperCase()){
			case "VALAKAS":
				return getDatoRespawn(Config.VALAKAS_SPAWN_INTERVAL);
			case "QUEEN ANT":
				return getDatoRespawn(Config.QUEEN_ANT_SPAWN_INTERVAL);
			case "CORE":
				return getDatoRespawn(Config.CORE_SPAWN_INTERVAL);
			case "ORFEN":
				return getDatoRespawn(Config.ORFEN_SPAWN_INTERVAL);
			case "BAIUM":
				return getDatoRespawn(Config.BAIUM_SPAWN_INTERVAL);
			case "ANTHARAS":
				return getDatoRespawn(Config.ANTHARAS_SPAWN_INTERVAL);
		}
		return "";
	}
	
	private static String getRbName(String idNPC){
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(Integer.valueOf(idNPC));
		return tmpl.getName();
	}
	
	private static String []RaidBoss = {
		"29028:6657",
		"29068:6656",
		"29001:6660",
		"29020:6658",
		"29014:6661",
		"29006:6662"
		};

/*
	private static HashMap<String,String> getChanceJewel(int npc, int idJewel){
		HashMap<String,String>Retorno = new HashMap<String, String>();
		L2NpcTemplate tmpl = NpcData.getInstance().getTemplate(npc);
		if (tmpl.getDropLists() != null){
			for (GroupedGeneralDropItem cat  : tmpl.getDropLists()){
				for(GeneralDropItem DropIn : cat.getItems()){
					if(DropIn.getItemId() == idJewel){
						double dChance = DropIn.getChance();
						long MinCant = DropIn.getMin();
						long MaxCant = DropIn.getMax();
						String Chance = String.valueOf((100 * dChance)/1000000.0);
						Retorno.put("CHANCE", Chance);
						Retorno.put("MINCANT",String.valueOf(MinCant));
						Retorno.put("MAXCANT",String.valueOf(MaxCant));
					}
				}
			}			
		}
		return Retorno;
	}
*/	
	private static String getMainWindows(){
		String retorno = "<center><table width=750 border=0 cellspacing=1 cellpadding=0>";
		int contador = 0;
		
		for(String parte : RaidBoss){
			String IdNpc = parte.split(":")[0];
			String IdJewel = parte.split(":")[1];
			String Name = getRbName(String.valueOf(IdNpc));
			String InteRespawn = get_RespawnTime(Name);
			String InteRamdon = get_RamdonTime(Name);
			
//			HashMap<String, String> Tem = getChanceJewel(Integer.valueOf(IdNpc), Integer.valueOf(IdJewel));
			
//			String Chance = Tem.get("CHANCE");
//			String MinCa = Tem.get("MINCANT");
//			String MaxCa = Tem.get("MAXCANT");
			
			if(contador==0){
				retorno += "<tr>";
			}
			
			String getIcono = opera.getIconImgFromItem(Integer.valueOf(IdJewel),true);
			
//			retorno += getSeccion_TD(Name, InteRespawn, InteRamdon, Chance, MinCa, MaxCa, getIcono);
			
			contador++;
			if(contador>=3){
				contador =0;
				retorno += "</tr>";
			}
		}
		
		if(contador>0 && contador<3){
			for(int i = contador;i<3;i++){
				retorno += "<td fixwidth=250></td>";
			}
		}
		
		retorno += "<tr>";
	
		
/*		
		HashMap<String, String> Tem = getChanceJewel(Integer.valueOf(29176), Integer.valueOf(6659));
		String Chance = Tem.get("CHANCE");
		String MinCa = Tem.get("MINCANT");
		String MaxCa = Tem.get("MAXCANT");
		retorno += getSeccionInstancia_TD("Zaken Day lv60", String.valueOf(ZAKEN_MIN_MEMBERS_DAYTIME_60), String.valueOf(ZAKEN_MAX_MEMBERS_DAYTIME_60), String.valueOf(ZAKEN_MIN_LEVEL_DAYTIME_60), Chance, MaxCa, MinCa, opera.getIconImgFromItem(6659, true));

		Tem = getChanceJewel(Integer.valueOf(29022), Integer.valueOf(6659));
		Chance = Tem.get("CHANCE");
		MinCa = Tem.get("MINCANT");
		MaxCa = Tem.get("MAXCANT");
		retorno += getSeccionInstancia_TD("Zaken Night lv60", String.valueOf(ZAKEN_MIN_MEMBERS_NIGHT), String.valueOf(ZAKEN_MAX_MEMBERS_NIGHT), String.valueOf(ZAKEN_MIN_LEVEL_NIGHT), Chance, MaxCa, MinCa, opera.getIconImgFromItem(6659, true));		

		Tem = getChanceJewel(Integer.valueOf(29176), Integer.valueOf(6659));
		Chance = Tem.get("CHANCE");
		MinCa = Tem.get("MINCANT");
		MaxCa = Tem.get("MAXCANT");
		retorno += getSeccionInstancia_TD("Zaken Night lv83", String.valueOf(ZAKEN_MIN_MEMBERS_DAYTIME_83), String.valueOf(ZAKEN_MAX_MEMBERS_DAYTIME_83), String.valueOf(ZAKEN_MIN_LEVEL_DAYTIME_83), Chance, MaxCa, MinCa, opera.getIconImgFromItem(6659, true));
		
		retorno += "</tr><tr>";

		Tem = getChanceJewel(Integer.valueOf(29179), Integer.valueOf(16025));
		Chance = Tem.get("CHANCE");
		MinCa = Tem.get("MINCANT");
		MaxCa = Tem.get("MAXCANT");
		retorno += getSeccionInstancia_TD("Freya Easy", String.valueOf(FREYA_MIN_PLAYERS), String.valueOf(FREYA_MAX_PLAYERS), String.valueOf(FREYA_MIN_LEVEL), Chance, MaxCa, MinCa, opera.getIconImgFromItem(16025, true));		
		
		
		Tem = getChanceJewel(Integer.valueOf(29180), Integer.valueOf(16026));
		Chance = Tem.get("CHANCE");
		MinCa = Tem.get("MINCANT");
		MaxCa = Tem.get("MAXCANT");
		retorno += getSeccionInstancia_TD("Freya Hard", String.valueOf(FREYA_MIN_PLAYERS), String.valueOf(FREYA_MAX_PLAYERS), String.valueOf(FREYA_MIN_LEVEL), Chance, MaxCa, MinCa, opera.getIconImgFromItem(16026, true));		
		
		Tem = getChanceJewel(Integer.valueOf(29047), Integer.valueOf(8191));
		Chance = Tem.get("CHANCE");
		MinCa = Tem.get("MINCANT");
		MaxCa = Tem.get("MAXCANT");
		retorno += getSeccionInstancia_TD("Frintezza", String.valueOf(FRINTEZZA_MIN_PLAYERS), String.valueOf(FRINTEZZA_MAX_PLAYERS), String.valueOf(FRINTEZZA_MIN_LEVEL), Chance, MaxCa, MinCa, opera.getIconImgFromItem(8191, true));
		
		
		retorno += "</tr></table></center><br><br>";
		*/
		return retorno;
	}
	
	
	private static String mainHtml(L2PcInstance player, String Params){
		String retorno = "<html><title>Grand Raid Boss Info System</title><body><center>" +cbFormato.getTituloEngine();
		String Icono = "icon.skill5081";
		String Explica = "<br>Grand Raid Boss Info";
		String Nombre = "Grand Raid Boss";
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false);
		
		retorno += getMainWindows();
		
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
			return mainHtml(player,params);
		}
		return "";
	}

	public static String delegar(L2PcInstance player, String linkComunidad) {
		return mainHtml(player, linkComunidad);
	}	
	
	
}
