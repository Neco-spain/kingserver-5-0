package ZeuS.Comunidad.EngineForm;

import java.util.Collections;
import java.util.HashMap;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;

public class C_gmlist {
	
	private static HashMap<Integer,HashMap<Integer,Integer>> PlayerTime = new HashMap<Integer,HashMap<Integer,Integer>>();
	private static HashMap<Integer,HashMap<Integer,Integer>> PlayerTry = new HashMap<Integer,HashMap<Integer,Integer>>();

	private static String getGMList(L2PcInstance player, String ByPass, String GmID){
		String retorno = "<center><table width=760><tr><td width=740 align=center><img src=\"L2UI.SquareGray\" width=740 height=2><table fixwidth=740 background=L2UI_CT1.Windows_DF_TooltipBG border=0 cellspacing=1 cellpadding=5><tr><td fixwidth=740><font name=hs12 color=B4CBFA>GameMaster</font></td></tr></table><img src=\"L2UI.SquareGray\" width=740 height=2></td></tr>";
		
		String Status_ON = "<font color=6EDC4A>Online</font";
		String Status_OFF = "<font color=FE5858>Offline</font";
		
		for(String prt : general.STAFF_DATA.split(":")){
			String idChar = prt.split(";")[0];
			String Funsion = prt.split(";")[1];
			String Nombre = opera.getPlayerNameById(Integer.valueOf(idChar));
			String Status = "";
			L2PcInstance cha = opera.getPlayerByID(Integer.valueOf(idChar));
			if(cha!=null){
				if(cha.isGM()){
					if(opera.isGmAllVisible(cha)){
						Status = Status_ON;
					}else{
						Status = Status_OFF;
					}
				}else{
					Status = Status_OFF;
				}
			}else{
				Status = Status_OFF;
			}
			
			String Bypass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.gmlist.name() + ";poke;" + idChar + ";0;0;0;0;0";
			
			retorno += "<tr><td width = 760 align=CENTER><br><table fixwidth=760 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=40 cellspacing=1 cellpadding=5>"+
					"<tr><td fixwidth=200>"+
					"<font name=hs12 color=26FFF1>"+ Nombre +"</font></td><td fixwidth=60>"+
					Status + "</td><td fixwidth=420>"+
					"<font color=CACACA>"+ Funsion +"</font></td><td fixwidth=50>"+
					"<button value=\"Poke\" action=\""+ Bypass +"\" width=52 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">"+
					"</td></tr></table></td></tr>";
			
		}
		
        retorno += "</table></center><br><img src=\"L2UI.SquareGray\" width=768 height=2>";
		
		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String retorno = "<html><body><title>GM List</title><center>" +cbFormato.getTituloEngine();
		String Icono = "icon.skill5662";
		String Explica = "<br>All GM on " + general.Server_Name;
		String Nombre = "GM List";
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false);
		if(Params.equals("0")){
			retorno += getGMList(player, "", "0");
		}
		retorno += "</body></html>";
		return retorno;
	}
	
	private static int getTry(L2PcInstance player,int IdGM, boolean erase){
		int Intentos = 1;
		if(PlayerTry!=null){
			if(PlayerTry.containsKey(player.getObjectId())){
				if(PlayerTry.get(player.getObjectId()).containsKey(IdGM)){
					if(erase){
						PlayerTry.get(player.getObjectId()).remove(IdGM);
						return 0;
					}
					Intentos = PlayerTry.get(player.getObjectId()).get(IdGM) + 1;					
				}
			}
		}
		
		if(!PlayerTry.containsKey(player.getObjectId())){
			PlayerTry.put(player.getObjectId(), new HashMap<Integer,Integer>());
		}
		
		PlayerTry.get(player.getObjectId()).put(IdGM, Intentos);
		return PlayerTry.get(player.getObjectId()).get(IdGM) ;
	}
	
	private static void pokeGM(L2PcInstance player, int idGM){
		int MinIntentos = 3;
		int Intentos = getTry(player,idGM,false);
		int MinutosBase = 3;
		int MinutosEspera = Intentos>MinIntentos ? Intentos * MinutosBase : MinutosBase ;
		L2PcInstance GM4Poke = opera.getPlayerbyID(idGM);
		if(GM4Poke!=null){
			if(GM4Poke.isGM()){
				boolean makePoke = false;
				if(PlayerTime!=null){
					int unixTimeNow = opera.getUnixTimeNow();
					int UnixTimeFromPlayer = 0;
					if(PlayerTime.containsKey(player.getObjectId())){
						if(PlayerTime.get(player.getObjectId()).containsKey(idGM)){
							UnixTimeFromPlayer = PlayerTime.get(player.getObjectId()).get(idGM);
							if(unixTimeNow < ( UnixTimeFromPlayer + ( MinutosEspera * 60 ) ) ){
								central.msgbox("You need to wait " + String.valueOf(MinutosEspera) + " minutes to send another Poke. If you try again and again time shall be increased.", player);
								return;
							}
						}
					}
				}
				makePoke = true;
				if(makePoke){
					central.msgbox_Lado("EY! Wake up! " + player.getName() + " try to speak with you.", GM4Poke, "GM");
				}
			}
		}
		
		central.msgbox("You poke was send, but if GM/ADM is connected may answer.", player);
		getTry(player,idGM,true);
		
		if(!PlayerTime.containsKey(player.getObjectId())){
			PlayerTime.put(player.getObjectId(), new HashMap<Integer,Integer>());			
		}
		PlayerTime.get(player.getObjectId()).put(idGM, opera.getUnixTimeNow()) ;
	}
	
	
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		if(parm1.equals("0")){
			return mainHtml(player,parm1);
		}else if(parm1.equals("poke")){
			pokeGM(player, Integer.valueOf(parm2));
		}
		return "";
	}
	
}
