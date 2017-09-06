package ZeuS.Comunidad.EngineForm;

import java.util.Vector;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.classmaster;

public class v_profession {
	
	private static final boolean validateClassId(ClassId oldCID, ClassId newCID)
	{
		if ((newCID == null) || (newCID.getRace() == null))
		{
			return false;
		}

		if (oldCID.equals(newCID.getParent()))
		{
			return true;
		}

		if (Config.ALLOW_ENTIRE_TREE && newCID.childOf(oldCID))
		{
			return true;
		}

		return false;
	}
	
	private static final int getMinLevel(int level)
	{
		switch (level)
		{
			case 0:
				return 20;
			case 1:
				return 40;
			case 2:
				return 76;
			default:
				return Integer.MAX_VALUE;
		}
	}
	
	private static String getProfesiones(L2PcInstance player){
		int level = player.getClassId().level() + 1;

		String retorno ="<center><table width=500 fixwith=220 height=200 background=L2UI_CT1.Windows_DF_Drawer_Bg cellspacing=0 cellpadding=5><tr><td width=500>";
		
		String bypass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Profession.name() +";add;%IDCLASS%;0;0;0;0";
		
		String botonera = "<table width=500 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td width=32>"+
				"<img src=\"%IMAGEN%\" width=32 height=32></td><td width=404><font name=\"hs12\" color=\"CCFFCC\">%CLASS%</font><br1><font color=\"A6CAF0\">%RAZA%</font>"+
				"</td><td width=64><button action=\""+ bypass +"\" width=64 height=64 back=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\" fore=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\"></td></tr></table>";
		
		final ClassId currentClassId = player.getClassId();
		final int minLevel = getMinLevel(currentClassId.level());
		int jobLevel=player.getClassId().level();
		
		boolean haveClass = false;

		if ((player.getLevel() >= minLevel) || Config.ALLOW_ENTIRE_TREE)
		{
			for (ClassId cid : ClassId.values())
			{
				if ((cid == ClassId.inspector) && (player.getTotalSubClasses() < 2))
				{
					continue;
				}
				if (validateClassId(currentClassId, cid) && (cid.level() == level))
				{
					haveClass = true;
					String Imagen = general.classData.get(cid.getId()).get("IMAGEN");
					String Nombre = general.classData.get(cid.getId()).get("CLASSBASE");
					retorno += botonera.replace("%IDCLASS%",String.valueOf(cid.getId())).replace("%IMAGEN%", Imagen).replace("%CLASS%", central.getClassName(player, cid.getId())).replace("%RAZA%", Nombre);
				}
			}
		}
		
		if(!haveClass){
			String Mensaje = "";
			if((jobLevel ==0) && (level < 20)) {
				Mensaje = "Come back here when you reach level 20.<br1>No more Class Changes Available";
			} else if((jobLevel <=1) && (level < 40)) {
				Mensaje = "Come back here when you reach level 40.<br1>No more Class Changes Available";
			} else if((jobLevel <=2) && (level < 76)) {
				Mensaje = "Come back here when you reach level 76.<br1>No more Class Changes Available";
			} else {
				Mensaje = "No more Class Changes Available";
			}

			retorno += "<table width=500 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td fixwidth=480 width=500 align=CENTER>"+
			            "<font name=\"hs12\" color=\"CCFFCC\">"+ Mensaje +"</font></td></tr></table>";
		}
		
		retorno += "</td></tr></table></center><br>";
		
		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.Profession.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += getProfesiones(player);
		
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
		}else if(parm1.equals("add")){
			if(classmaster.AddProfesion(player, Integer.valueOf(parm2))){
				return mainHtml(player,"0");	
			}
		}
		return "";
	}
}
