package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;
import ZeuS.interfase.transform;
import ZeuS.procedimientos.opera;

public class v_Transformations extends transform{
	
	private static Logger _log = Logger.getLogger(v_Transformations.class.getName());
	
	private static String mainHtml(L2PcInstance player, String Params, int Pagina){
		String par = Engine.enumBypass.Transformation.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += getTransformationAll(player, Pagina, Params);
		
		retorno += "</body></html>";
		return retorno;
	}
	
	private static String getGrillaInfoTranfor(String idImagen, String Nombre, String Tipo, String ByPass){
		String retorno = "<table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG>"+
			"<tr><td fixwith=32>"+cbFormato.getBotonForm(idImagen, ByPass) + "<br><br></td>"+
            "<td fixwidth=218>"+
            "<font color=01A9DB>Name: </font> <font color=0079DC>"+ Nombre +"</font>"+
            "<br1><font color=01A9DB>Type:</font> <font color=00D9DC>"+ Tipo +"</font><br></td></tr></table>";
		return retorno;
	}
	
	private static String getTransformationAll(L2PcInstance player, int pagina, String tipo) {
		HashMap<String,String> Imagenes = new HashMap<String, String>();
		Imagenes.put("Special", "icon.skilltransform2");
		Imagenes.put("RaidBoss", "icon.skilltransform1");
		Imagenes.put("Normal", "icon.skilltransform4");
		
		if(tipo.equals("0")){
			tipo = "Special";
		}
		
		String ByPassMenu = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";list;%TIPO%;0;0;0;0";
		
		String ColorSel = "89F4FF";
		String ColorNoSel = "01A9DB";
		
		String ByPassShowPrice_Special = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";showPriceSpecial;0;0;0;0;0;0";
		String ByPassShowPrice_RaidBoss = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";showPriceRaidBoss;0;0;0;0;0;0";
		String ByPassShowPrice_Normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";showPriceNormal;0;0;0;0;0;0";
		
		String Menu = "<center><table width=750 border=0 cellspacing=1 cellpadding=0><tr><td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32>"+
		cbFormato.getBotonForm(Imagenes.get("Special"), ByPassMenu.replace("%TIPO%", "Special"))+"<br></td><td fixwidth=177>"+
        "<br><font color="+ ( tipo.equals("Special") ? ColorSel : ColorNoSel ) +" name=\"hs12\">Show Specials Transf.</font><br></td><td fixwidth=41><br><button  action=\""+ ByPassShowPrice_Special +"\" width=16 height=16 back=L2UI_CT1.Chatwindow_DF_ItemInfoIcon fore=L2UI_CT1.Chatwindow_DF_ItemInfoIcon></td></tr></table></td><td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32>"+
        cbFormato.getBotonForm(Imagenes.get("RaidBoss"), ByPassMenu.replace("%TIPO%", "RaidBoss"))+"<br></td><td fixwidth=182>"+
        "<font color="+ (tipo.equals("RaidBoss") ? ColorSel : ColorNoSel) +" name=\"hs12\">Show RaidBoss Transf.</font><br></td><td fixwidth=36><br><button  action=\""+ ByPassShowPrice_RaidBoss +"\" width=16 height=16 back=L2UI_CT1.Chatwindow_DF_ItemInfoIcon fore=L2UI_CT1.Chatwindow_DF_ItemInfoIcon></td></tr></table></td><td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32>"+
        cbFormato.getBotonForm(Imagenes.get("Normal"), ByPassMenu.replace("%TIPO%", "Normal"))+"<br></td><td fixwidth=172>"+
        "<font color="+ (tipo.equals("Normal") ? ColorSel : ColorNoSel) +" name=\"hs12\">Show Normal Transf.</font><br></td><td fixwidth=36><br><button  action=\""+ ByPassShowPrice_Normal +"\" width=16 height=16 back=L2UI_CT1.Chatwindow_DF_ItemInfoIcon fore=L2UI_CT1.Chatwindow_DF_ItemInfoIcon></td></tr></table></td></tr></table></center><br><img src=\"L2UI.SquareGray\" width=768 height=2><br1><img src=\"L2UI.SquareGray\" width=768 height=1><br1><img src=\"L2UI.SquareGray\" width=768 height=2>";
		
		String retorno = Menu + "<center><table width=750 height=200 border=0 cellspacing=1 cellpadding=0>";
		
		String []StrTransforSelect = {};
		
		
		if(tipo.equals("Special")){
			StrTransforSelect = Transformaciones_especiales;
			_log.warning("0");
		}else if(tipo.equals("RaidBoss")){
			StrTransforSelect = Transformaciones_RaidBoss;
			_log.warning("1");
		}else if(tipo.equals("Normal")){
			StrTransforSelect = Transformaciones_Varias;
			_log.warning("2");
		}

		int Maximo = 18;
		int Desde = pagina * Maximo;
		int Hasta = Desde + Maximo;
		
		int Contador = 0;
		int ContadorTR = 0;
		boolean haveNext = false;
		
		for(String pT : StrTransforSelect){
			if(Contador >= Desde && Contador < Hasta){
    			if(ContadorTR == 0){
    				retorno += "<tr>";
    			}
    			
    			String ByPass = "bypass "+general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";setTrans;%TIPO%;%IDINTERNO%;"+ String.valueOf(pagina) +";0;0";
    			retorno += "<td fixwidth=250>"+ getGrillaInfoTranfor(Imagenes.get(tipo), pT.split(",")[0] , tipo, ByPass.replace("%TIPO%", tipo).replace("%IDINTERNO%", pT.split(",")[1]) ) +"</td>";
    			ContadorTR++;
    			if(ContadorTR>=3){
    				retorno += "</tr>";
    				ContadorTR=0;
    			}
    		}else if(Contador > Hasta){
    			haveNext = true;
    		}
			Contador++;			
		}
		
		
		if(ContadorTR>0 && ContadorTR<3){
			for(int i=ContadorTR;i<3;i++){
				retorno += "<td fixwidth=250></td>";
			}
			retorno += "</tr>";
		}
		
		
		/*else if(parm1.equals("list")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));*/
		
		retorno += " </table></center><br><img src=\"L2UI.SquareGray\" width=768 height=2>";
		
		if(haveNext || pagina>0 ){
			
			String bypassAntes = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.Transformation.name() + ";list;" + tipo + ";" +String.valueOf(pagina-1)+";0;0;0"; 
			String bypassProx = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.Transformation.name() + ";list;" + tipo + ";" +String.valueOf(pagina+1)+";0;0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">";				
			
			String Grilla = "<center><table fixwith=767 cellspacing=-4 cellpadding=-1 height=38 bgcolor=0E180B><tr><td fixwidth=767 align=CENTER><br><table with=96>"+
            "<tr><td width=32>"+ ( pagina>0 ? btnAntes : "") +"</td>"+
                "<td width=32 align=CENTER><font name=\"hs12\">"+ String.valueOf(pagina+1) +"</font></td>"+
                "<td width=32>"+ (haveNext ? btnProx : "") +"</td></tr></table></td></tr></table></center>";
			
			retorno += Grilla + "<br><img src=\"L2UI.SquareGray\" width=768 height=2>";
		}
		
		
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
		
		HashMap<String,String> Precios = new HashMap<String, String>();
		
		if(parm1.equals("0")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("list")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("setTrans")){
			int idTransForm = Integer.valueOf(parm3);
			int Pagina = Integer.valueOf(parm4);
			
			String IdItemPedir = "";
			if(parm2.equals("Special")){
				if(!general.TRANSFORMATION_ESPECIALES){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return mainHtml(player,parm2,Pagina);
				}
				IdItemPedir = general.TRANSFORMATION_ESPECIALES_PRICE;
			}else if(parm2.equals("RaidBoss")){
				if(!general.TRANSFORMATION_RAIDBOSS){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return mainHtml(player,parm2,Pagina);
				}
				IdItemPedir = general.TRANSFORMATION_RAIDBOSS_PRICE;
			}else if(parm2.equals("Normal")){
				if(!general.BTN_SHOW_TRANSFORMATION_CBE){
					central.msgbox(msg.DISABLED_BY_ADMIN, player);
					return mainHtml(player,parm2,Pagina);					
				}
				IdItemPedir = general.TRANSFORMATION_PRICE;
			}
			
			if(!opera.haveItem(player, IdItemPedir)){
				return mainHtml(player,parm1,Pagina);
			}
			
			if(transformPlayer(player, idTransForm)){
				opera.removeItem(IdItemPedir, player);
			}else{
				return mainHtml(player,parm1,Pagina);
			}
		}else if(parm1.equals("showPriceSpecial")){
			Precios.put("Cost", general.TRANSFORMATION_ESPECIALES_PRICE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.TRANSFORMATION_LVL) + ";Need Noble:" + ( general.TRANSFORMATION_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Special Transformation");
		}else if(parm1.equals("showPriceRaidBoss")){
			Precios.put("Cost", general.TRANSFORMATION_RAIDBOSS_PRICE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.TRANSFORMATION_LVL) + ";Need Noble:" + ( general.TRANSFORMATION_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Raid Boss Transformation");			
		}else if(parm1.equals("showPriceNormal")){
			Precios.put("Cost", general.TRANSFORMATION_PRICE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.TRANSFORMATION_LVL) + ";Need Noble:" + ( general.TRANSFORMATION_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Normal Transformation");			
		}
		return "";
		
		/*
		 * 
		 * 			HashMap<String,String> Precios = new HashMap<String, String>();
			
			if(!general.BUFFCHAR_FOR_FREE){
				Precios.put("Individual Buff", general.BUFFCHAR_COST_INDIVIDUAL);
				Precios.put("Scheme", general.BUFFCHAR_COST_USE);
				Precios.put("Heal", general.BUFFCHAR_COST_HEAL);
				Precios.put("Cancel Buff", general.BUFFCHAR_COST_CANCEL);
			}else{
				Precios.put("Individual Buff", "");
				Precios.put("Scheme", "");
				Precios.put("Heal", "");
				Precios.put("Cancel", "");
			}
			
			
			cbFormato.showItemRequestWindows(player, Precios, "Community Buff");
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 		String ByPassShowPrice_Special = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";showPriceSpecial;0;0;0;0;0;0";
		String ByPassShowPrice_RaidBoss = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";showPriceRaidBoss;0;0;0;0;0;0";
		String ByPassShowPrice_Normal = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.Transformation.name() + ";showPriceNormal;0;0;0;0;0;0";*/
		
	}
}
