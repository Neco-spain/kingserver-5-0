package ZeuS.Comunidad.EngineForm;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.procedimientos.opera;

public class v_HeroList {

	private static String getAllClases(String strRace){
		String retorno = "";
		String []Clases = {"Human:icon.skillhuman","Elf:icon.skillelf","Dark Elf:icon.skilldarkelf","Orc:icon.skillorc","Dwarf:icon.skilldwarf","Kamael:icon.skillkamael"};
		
		int contador = 0;
		int ContBarra = 0;
		
		retorno += "<table width=748 border=0 cellspacing=2 background=L2UI_CT1.Windows_DF_TooltipBG height=45><tr>";
		
		for(String Class : Clases){
			String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";" + Engine.enumBypass.HeroList.name() + ";0;"+ Class.split(":")[0] +";0;0;0;0";
			retorno += "<td width=32>"+
             cbFormato.getBotonForm(Class.split(":")[1], ByPass)+"<br></td>"+
			"<td fixwidth=92 align=LEFT>"+
            "<font name=\"hs12\" "+ (Class.split(":")[0].equals(strRace) ?  "color=0080FF":"" ) +">"+Class.split(":")[0]+"</font></td>";
		}
		retorno += "</tr></table><br>";
		
		String btnMaestro = "<button value=\"%NOMBRE%\" width=%TAM% height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter>";

		String btnPvP = btnMaestro.replace("%NOMBRE%", "PvP").replace("%TAM%", "58");
		String btnPk = btnMaestro.replace("%NOMBRE%", "Pk").replace("%TAM%", "58");

		
		retorno += "<table cellpadding=0 cellspacing=0><tr><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Rank\" width=34 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Player Name\" width=188 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Lv\" width=22 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            btnPvP+"</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            btnPk+"</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Base Class\" width=138 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Status\" width=52 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"\" width=62 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td></tr></table><br>";
		
		String TipoConecion = "";
		if(general.getPlayerHeroes(strRace)!=null){
			Iterator itr = general.getPlayerHeroes(strRace).entrySet().iterator();
			
			int ContFila=0;
			String Colores[] = {"232323","141313"};
			
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	int idClase = (int) Entrada.getKey();
		    	HashMap<Integer,String> Base = new HashMap<Integer,String>();
		    	Base = general.getPlayerHeroes(strRace).get(idClase);
				L2PcInstance playerBus = null;
				try{
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
					String ByPassInfo = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.HeroList.name()+ ";showcharinfo;" + strRace + ";"+Base.get(1)+";0;0;0";
					String btnInfoChar = "<button value=\"Char Info\" width=60 height=26 action=\""+ ByPassInfo +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
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
		    	
		    }//end while
		}

		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params, String PalabraBuscar){
		String par = Engine.enumBypass.HeroList.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = "icon.skill6280";
		String Explica = "All hero on our Server";
		String Nombre = "Heroe List";
		retorno += cbFormato.getTituloCentral(Icono,Nombre,Explica,false,"bypass " + general.COMMUNITY_BOARD_PART_EXEC);
		
		retorno += getAllClases(PalabraBuscar);
		
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
			return mainHtml(player,parm1,parm2);
		}else if(parm1.equals("showcharinfo")){
			opera.getInfoPlayer(parm3, player);
			return mainHtml(player,"0",parm2);
		}
		
		//opera.getInfoPlayer(parm2, player);
		return "";
	}
}
