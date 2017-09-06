package ZeuS.Comunidad.EngineForm;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.Engine.enumBypass;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.special_augment;
import ZeuS.procedimientos.handler;
import ZeuS.procedimientos.opera;


/*AUGMENT_DATA.get(rss.getString("tipo")).put(rss.getInt("id"), new HashMap<String,String>());
  AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("ID_AUGMT_SERVER", String.valueOf(rss.getInt("idaugmentGame")));
  AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_DESCRIPT", String.valueOf(rss.getString("aug_descrip")));
  AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_SKILL", String.valueOf(rss.getInt("aug_skill")));
  AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_SKILL_DESCRIP", String.valueOf(rss.getString("skill_descrip")));
  AUGMENT_DATA.get(rss.getString("tipo")).get(rss.getInt("id")).put("AUGMENT_SKILL_LEVEL", String.valueOf(rss.getInt("skill_level")));*/

public class v_AugmentSpecial extends special_augment{
	private final static Logger _log = Logger.getLogger(v_AugmentSpecial.class.getName());
	
	
	private static void getAugmentWindows(L2PcInstance player, String Tipo, String idAugment_Interno){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Augment Info - Skill") + central.LineaDivisora(2);
		String BOTON_ACCEPTAR ="";
		
		String Descrip = general.AUGMENT_DATA.get(Tipo).get(Integer.valueOf(idAugment_Interno)).get("AUGMENT_SKILL_DESCRIP");
		String Level = general.AUGMENT_DATA.get(Tipo).get(Integer.valueOf(idAugment_Interno)).get("AUGMENT_SKILL_LEVEL");
		String IdAugment = general.AUGMENT_DATA.get(Tipo).get(Integer.valueOf(idAugment_Interno)).get("ID_AUGMT_SERVER");
		
		
		MAIN_HTML +=central.LineaDivisora(1) + central.headFormat("Tipo: <font color=LEVEL>"+Tipo+"</font>","") + central.LineaDivisora(1);
		MAIN_HTML +=central.LineaDivisora(1) + central.headFormat("Skill Descrip: <font color=LEVEL>"+Descrip+"</font>","") + central.LineaDivisora(1);
		MAIN_HTML +=central.LineaDivisora(1) + central.headFormat("Skill Level: <font color=LEVEL>"+Level+"</font>","") + central.LineaDivisora(1);
		String ItemToShow = "";
		if(Tipo.equals("Active")){
			ItemToShow  = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
		}else if(Tipo.equals("Passive")){
			ItemToShow = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
		}else if(Tipo.equals("Chance")){
			ItemToShow = general.AUGMENT_SPECIAL_PRICE_CHANCE;
		}
		MAIN_HTML += central.ItemNeedShowBox(ItemToShow);
		
		//setAugment
		String ByPassAceptar = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SelectAugment + ";setAugment;"+ IdAugment +";"+ Tipo +";0;0;0;0";
		BOTON_ACCEPTAR = "<center><button value=\"Obtener\" action=\""+ ByPassAceptar +"\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML +=central.LineaDivisora(1) + central.LineaDivisora(2) + central.headFormat(BOTON_ACCEPTAR,"") + central.LineaDivisora(1) +central.LineaDivisora(1);
		MAIN_HTML += "<br1><br1><br1><br1><br1>"+central.BotonGOBACKZEUS()+"</center></body></html>";
		central.sendHtml(player, MAIN_HTML);
	}
	
	
	
	
	
	private static String getGrillaInfoAugment(String idImagen, String SkillName, String Tipo, String ByPass){
		String retorno = "<table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG>"+
			"<tr><td fixwith=32>"+cbFormato.getBotonForm(idImagen, ByPass) + "<br><br></td>"+
            "<td fixwidth=218>"+
            "<font color=01A9DB>It. Skill:</font> <font color=0079DC>"+ SkillName +"</font>"+
            "<br1><font color=01A9DB>Type:</font> <font color=00D9DC>"+ Tipo +"</font><br></td></tr></table>";
		return retorno;
	}
	
	private static String getAugmentAll(L2PcInstance player, int pagina, String tipo){

		HashMap<String,String> Imagenes = new HashMap<String, String>();
		Imagenes.put("Active", "icon.skilltransform2");
		Imagenes.put("Passive", "icon.skilltransform1");
		Imagenes.put("Chance", "icon.skilltransform4");
		
		String ByPassMenu = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SelectAugment.name() + ";list;%TIPO%;0;0;0;0";
		
		String ColorSel = "89F4FF";
		String ColorNoSel = "01A9DB";
		
		String ByPassCostActive = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.SelectAugment + ";showPriceActive;0;0;0;0;0;0";
		String ByPassCostPassive = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.SelectAugment + ";showPricePassive;0;0;0;0;0;0";
		String ByPassCostChance = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.SelectAugment + ";showPriceChance;0;0;0;0;0;0";
		
		String Menu = "<center><table width=750 border=0 cellspacing=1 cellpadding=0><tr><td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32>"+
		cbFormato.getBotonForm(Imagenes.get("Active"), ByPassMenu.replace("%TIPO%", "Active"))+"<br></td><td fixwidth=155>"+
        "<br><font color="+ ( tipo.equals("Active") ? ColorSel : ColorNoSel ) +" name=\"hs12\">Show Actives Skills</font><br></td><td fixwidth=40><br><button  action=\""+ ByPassCostActive +"\" width=16 height=16 back=L2UI_CT1.Chatwindow_DF_ItemInfoIcon fore=L2UI_CT1.Chatwindow_DF_ItemInfoIcon><br></td></tr></table></td><td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32>"+
        cbFormato.getBotonForm(Imagenes.get("Passive"), ByPassMenu.replace("%TIPO%", "Passive"))+"<br></td><td fixwidth=160>"+
        "<font color="+ (tipo.equals("Passive") ? ColorSel : ColorNoSel) +" name=\"hs12\">Show Passives Skills</font><br></td><td fixwidth=35><br><button  action=\""+ ByPassCostPassive +"\" width=16 height=16 back=L2UI_CT1.Chatwindow_DF_ItemInfoIcon fore=L2UI_CT1.Chatwindow_DF_ItemInfoIcon><br></td></tr></table></td><td fixwidth=250><table fixwidth=250 bgcolor=000000 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwith=32>"+
        cbFormato.getBotonForm(Imagenes.get("Chance"), ByPassMenu.replace("%TIPO%", "Chance"))+"<br></td><td fixwidth=160>"+
        "<font color="+ (tipo.equals("Chance") ? ColorSel : ColorNoSel) +" name=\"hs12\">Show Chances Skills</font></td><td fixwidth=40><br><button  action=\""+ ByPassCostChance +"\" width=16 height=16 back=L2UI_CT1.Chatwindow_DF_ItemInfoIcon fore=L2UI_CT1.Chatwindow_DF_ItemInfoIcon><br></td></tr></table></td></tr></table></center><br><img src=\"L2UI.SquareGray\" width=768 height=2><br1><img src=\"L2UI.SquareGray\" width=768 height=1><br1><img src=\"L2UI.SquareGray\" width=768 height=2>";
		
		String retorno = Menu + "<center><table width=750 height=200 border=0 cellspacing=1 cellpadding=0>";

		int Maximo = 18;
		int Desde = pagina * Maximo;
		int Hasta = Desde + Maximo;
		
		int Contador = 0;
		int ContadorTR = 0;
		boolean haveNext = false;
		
	
		Iterator itr = general.AUGMENT_DATA.entrySet().iterator();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	String TipoAumento = Entrada.getKey().toString();
	    	if((tipo.length()>0 && tipo.equals(TipoAumento)) || (tipo.equals("0"))){
	    		Iterator itr2 = general.AUGMENT_DATA.get(TipoAumento).entrySet().iterator();
	    		while(itr2.hasNext() && !haveNext){
			    		Map.Entry Entrada2 = (Map.Entry)itr2.next();
			    		int ID_Interno = (int) Entrada2.getKey();
			    		if(Contador >= Desde && Contador < Hasta){
			    			String SkillName = general.AUGMENT_DATA.get(TipoAumento).get(ID_Interno).get("AUGMENT_SKILL_DESCRIP");
			    			if(SkillName.indexOf(":")>=0){
			    				SkillName = SkillName.split(":")[0];
			    			}
			    			
			    			if(ContadorTR == 0){
			    				retorno += "<tr>";
			    			}
			    			
			    			/*		}else if(parm1.equals("getinfo")){
			//tipo idinterno Pagina*/
			    			
			    			
			    			String ByPass = "bypass "+general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + Engine.enumBypass.SelectAugment.name() + ";getinfo;%TIPO%;%IDINTERNO%;"+ String.valueOf(pagina) +";0;0";
			    			retorno += "<td fixwidth=250>"+ getGrillaInfoAugment(Imagenes.get(TipoAumento), SkillName, TipoAumento, ByPass.replace("%TIPO%", tipo).replace("%IDINTERNO%", String.valueOf(ID_Interno)) ) +"</td>";
			    			
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
	    	}
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
			
			String bypassAntes = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.SelectAugment.name() + ";list;" + tipo + ";" +String.valueOf(pagina-1)+";0;0;0"; 
			String bypassProx = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.SelectAugment.name() + ";list;" + tipo + ";" +String.valueOf(pagina+1)+";0;0;0";
			
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

	private static String mainHtml(L2PcInstance player, String TipoBusqueda,int Pagina){
		String par = Engine.enumBypass.SelectAugment.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += getAugmentAll(player, Pagina, TipoBusqueda);
		
		retorno += "</body></html>";
		return retorno;
	}
	
	public static String bypass(L2PcInstance player, String params){
		HashMap<String,String> Precios = new HashMap<String, String>();
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,"Active",0);
		}else if(parm1.equals("list")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("getinfo")){
			getAugmentWindows(player, parm2, parm3);
			return mainHtml(player,parm2,Integer.valueOf(parm4));
		}else if(parm1.equals("setAugment")){
			if(setAugment(player,parm2,true)){
				String ItemToRemove = "";
				if(parm3.equals("Active")){
					ItemToRemove  = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
				}else if(parm3.equals("Passive")){
					ItemToRemove = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
				}else if(parm3.equals("Chance")){
					ItemToRemove = general.AUGMENT_SPECIAL_PRICE_CHANCE;
				}				
				opera.removeItem(ItemToRemove, player);
				cbFormato.cerrarCB(player);
				central.sendHtml(player, "<html><title>Augment Select</title><body><center><font color=LEVEL name=HS12>DONE!!</font></center></body></html>");
			}else{
				
			}
		}else if(parm1.equals("showPriceActive")){
			Precios.put("Cost", general.AUGMENT_SPECIAL_PRICE_ACTIVE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.AUGMENT_SPECIAL_LVL) + ";Need Noble:" + ( general.AUGMENT_SPECIAL_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Active Augment");			
		}else if(parm1.equals("showPricePassive")){
			Precios.put("Cost", general.AUGMENT_SPECIAL_PRICE_PASSIVE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.AUGMENT_SPECIAL_LVL) + ";Need Noble:" + ( general.AUGMENT_SPECIAL_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Active Passive");
		}else if(parm1.equals("showPriceChance")){
			Precios.put("Cost", general.AUGMENT_SPECIAL_PRICE_CHANCE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.AUGMENT_SPECIAL_LVL) + ";Need Noble:" + ( general.AUGMENT_SPECIAL_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Active Chance");			
		}
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * String ByPassCostActive = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.SelectAugment + ";showPriceActive;0;0;0;0;0;0";
		String ByPassCostPassive = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.SelectAugment + ";showPricePassive;0;0;0;0;0;0";
		String ByPassCostChance = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC + ";" + enumBypass.SelectAugment + ";showPriceChance;0;0;0;0;0;0";
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
		 * */
		
		return "";
	}
	
}
