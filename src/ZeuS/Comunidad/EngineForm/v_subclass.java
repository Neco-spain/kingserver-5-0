package ZeuS.Comunidad.EngineForm;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.SubClass;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.SystemMessageId;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.subclass;
import ZeuS.procedimientos.opera;

public class v_subclass extends subclass{
	
	private static final Logger _log = Logger.getLogger(v_subclass.class.getName());
	
	private static final Iterator<SubClass> iterSubClasses(L2PcInstance player)
	{
		return player.getSubClasses().values().iterator();
	}
	
	private static String getBtnChooseSubClassToRemove(L2PcInstance player){
		String MAIN_HTML = "<tr><td width=500>";
		
		int actualClass = player.getClassId().getId();
		
		boolean isBaseClass = (player.getClassId().getId() == player.getBaseClass());
		
		String byPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SubClass.name() + ";deleteSubClass;%IDSUBCLASS%;%IDCLASS%;0;0;0";
		String btnChange = "<button action=\""+ byPass +"\" width=64 height=64 back=\"L2UI_CT1.ICON_DF_CHARACTERTURN_ZOOMOUT\" fore=\"L2UI_CT1.ICON_DF_CHARACTERTURN_ZOOMOUT\">";
		
		String GrillaCambio = "<table width=500 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td width=32>"+
        "<img src=\"%IMAGEN%\" width=32 height=32></td><td width=404><font name=\"hs12\" color=\"CCFFCC\">%NOMBRE% (%TIPO%)</font><br1><font color=\"A6CAF0\">%RAZA%</font></td>"+
        "<td width=64>%BOTON%</td></tr></table><br>";
		
		String Nombre = "";
		String Raza = "";
		String Imagen = "";
		
		String MAIN_HTML_2 = "";
		int i=1;
		for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
		{
			SubClass subClass = subList.next();
			
			Nombre = opera.getClassName(subClass.getClassId());
			Raza = general.classData.get(subClass.getClassId()).get("CLASSBASE");
			Imagen = general.classData.get(subClass.getClassId()).get("IMAGEN");
			
			if(subClass.getClassId() != player.getClassId().getId()){
				MAIN_HTML_2 += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass nº " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", btnChange.replace("%IDCLASS%", String.valueOf(subClass.getClassId())).replace("%IDSUBCLASS%",String.valueOf(subClass.getClassIndex())));
			}else{
				MAIN_HTML_2 += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass nº " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", "");
			}
			i++;
		}
		
		if(MAIN_HTML_2.length()==0){
			MAIN_HTML_2 = "<center>No Have Sub Class</center>";
		}
		
		MAIN_HTML += MAIN_HTML_2 + "</td></tr>";

		return MAIN_HTML;
	}
	
	private static String getBtnChooseSubClass(L2PcInstance player){
		String MAIN_HTML = "<tr><td width=500>";
		
		int actualClass = player.getClassId().getId();
		
		boolean isBaseClass = (player.getClassId().getId() == player.getBaseClass());
		
		String byPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SubClass.name() + ";changeSubC;%IDCLASS%;0;0;0;0";
		String btnChange = "<button action=\""+ byPass +"\" width=64 height=64 back=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\" fore=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\">";
		
		String GrillaCambio = "<table width=500 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td width=32>"+
        "<img src=\"%IMAGEN%\" width=32 height=32></td><td width=404><font name=\"hs12\" color=\"CCFFCC\">%NOMBRE% (%TIPO%)</font><br1><font color=\"A6CAF0\">%RAZA%</font></td>"+
        "<td width=64>%BOTON%</td></tr></table><br>";
		
		String Nombre = opera.getClassName(player.getBaseClass());
		String Raza = general.classData.get(player.getBaseClass()).get("CLASSBASE");
		String Imagen = general.classData.get(player.getBaseClass()).get("IMAGEN");
		
		
		MAIN_HTML += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Main").replace("%RAZA%", Raza).replace("%BOTON%", ( isBaseClass ? "" : btnChange.replace("%IDCLASS%", String.valueOf(0)) ) );
			
		int i=1;
		for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
		{
			SubClass subClass = subList.next();
			
			Nombre = opera.getClassName(subClass.getClassId());
			Raza = general.classData.get(subClass.getClassId()).get("CLASSBASE");
			Imagen = general.classData.get(subClass.getClassId()).get("IMAGEN");
			
			if(subClass.getClassId() != player.getClassId().getId()){
				MAIN_HTML += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass nº " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", btnChange.replace("%IDCLASS%",String.valueOf(subClass.getClassIndex())));
			}else{
				MAIN_HTML += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass nº " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", "");
			}
			i++;
		}
		
		MAIN_HTML += "</td></tr>";

		return MAIN_HTML;
	}
	
	
	
	private static String getBtn(L2PcInstance player, boolean isRemove, String idSubClass, String idClassFromSub){
		Vector<Integer> subClass_Permitidas = new Vector<Integer>();
		subClass_Permitidas = getSubClases(player);
		
		String tipoByPass = isRemove ? "apliRemoveC":"apliSubC";
		
		String ByPass =  "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SubClass.name() + ";"+tipoByPass+";%IDCLASS%;"+ String.valueOf(idSubClass) +";0;0;0";
		String btn = "<button action=\""+ ByPass + "\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_PlusBtn_Red\" fore=\"L2UI_CT1.MiniMap_DF_PlusBtn_Red\">";
		
		String retorno = "";
		
		String btnClases = "<td><table width=240 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td wdith=32><img src=\"%IMAGEN%\" width=32 height=32>"+
        "</td><td width=180><font name=\"hs12\" color=\"CCFFCC\">%NOMBRE%</font><br1><font color=\"A6CAF0\">%RAZA%</font></td><td width=32>"+
        btn + "</td></tr></table></td>";
		
		int contador = 1;
		
		for(int idClass : subClass_Permitidas){
			if(contador == 1){
				retorno += "<tr>";
			}
			if(Integer.valueOf(idClassFromSub)!=idClass){
				String Nombre = opera.getClassName(idClass);
				String Raza = general.classData.get(idClass).get("CLASSBASE");
				String Imagen = general.classData.get(idClass).get("IMAGEN");
				retorno += btnClases.replace("%IDCLASS%", String.valueOf(idClass)).replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%RAZA%", Raza);
				
				contador++;
				
				if(contador == 3){
					retorno += "</tr>";
					contador = 1;
				}
			}
		}
		
		if(contador==2){
			retorno += "<td width=240></td></tr>";
		}
		
		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Seccion){
		return mainHtml(player, Seccion, false, "0","0");
	}
	
	private static String mainHtml(L2PcInstance player, String Seccion, boolean isRemove,String ID_subclassINDEX, String idClass){
		String par = Engine.enumBypass.SubClass.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		String byPass_add = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SubClass.name() + ";add;0;0;0;0;0";
		String byPass_Change = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SubClass.name() + ";change;0;0;0;0;0";
		String byPass_Delete = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SubClass.name() + ";remove;0;0;0;0;0";
		
		String btn_add = "<button value=\"Add Subclase\" action=\""+ byPass_add +"\" width=195 height=40 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">";
		String btn_change = "<button value=\"Change Subclase\" action=\""+ byPass_Change +"\" width=195 height=40 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">";
		String btn_Delete = "<button value=\"Remove Subclase\" action=\""+ byPass_Delete +"\" width=195 height=40 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">";
		
		retorno += "<table width=720 height=200><tr><td width=220><table width=220 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td align=\"CENTER\"><br>"+
        btn_add + btn_change + btn_Delete + "<br></td></tr></table></td><td width=500><table width=500 fixwith=220 height=200 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=0 cellpadding=5>";
		
		if(Seccion.equals("add")){
			retorno += getBtn(player,isRemove,ID_subclassINDEX, idClass);
		}else if(Seccion.equals("change")){
			retorno += getBtnChooseSubClass(player);
		}else if(Seccion.equals("remove")){
			retorno += getBtnChooseSubClassToRemove(player);
		}
		
		retorno += "</table><br><br></td></tr></table></center><br>";

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
		
		if(!parm1.equals("0")){
			if(!player.isInsideZone(ZoneId.PEACE)){
				central.msgbox("You need to be into a peace zone to do this", player);
				return mainHtml(player,"0"); 
			}
		}
		
		if(parm1.equals("0")){
			return mainHtml(player,"0");
		}else if(parm1.equals("add")){
			return mainHtml(player, parm1);
		}else if(parm1.equals("apliSubC")){
			if(opera.canUseCBFunction(player)){
				if(player.getTotalSubClasses() >= Config.MAX_SUBCLASS){
               		central.msgbox("You can not add more subclasses", player);
               		return "";
				}
				if(player.getLevel() < 75){
               		central.msgbox("You level is low for make another subclass. Please come back when your are 75", player);
               		return "";					
				}
				
				if (!player.addSubClass(Integer.valueOf(parm2),player.getTotalSubClasses() + 1)){
					player.sendMessage("The sub class could not be added.");
					return "";
				}
				player.setActiveClass(player.getTotalSubClasses());
				player.sendPacket(SystemMessageId.CLASS_TRANSFER);
				return mainHtml(player,"0");				
			}
		}else if(parm1.equals("change")){
			return mainHtml(player, parm1);
			
		}else if(parm1.equals("changeSubC")){			
			//if(!player.setActiveClass(Integer.valueOf(parm2))){			
			//	player.sendMessage("The sub class could not be changed.");
            //    return "";
			//}
			player.sendPacket(SystemMessageId.SUBCLASS_TRANSFER_COMPLETED);
			return mainHtml(player,"0");
			
		}else if(parm1.equals("remove")){
			return mainHtml(player, parm1);
		}else if(parm1.equals("deleteSubClass")){
			return mainHtml(player,"add",true,parm2,parm3);
		}else if(parm1.equals("apliRemoveC")){
			if(opera.canUseCBFunction(player)){
				if(player.modifySubClass(Integer.valueOf(parm3),Integer.valueOf(parm2))){
					player.stopAllEffects();
					player.setActiveClass(Integer.valueOf(parm3));
					player.sendPacket(SystemMessageId.ADD_NEW_SUBCLASS);
					return mainHtml(player,"0");
				}else{
	                player.sendMessage("The sub class could not be added, you have been reverted to your base class.");
				}
				return mainHtml(player,"0");
			}
		}
		return "";
	}
}
