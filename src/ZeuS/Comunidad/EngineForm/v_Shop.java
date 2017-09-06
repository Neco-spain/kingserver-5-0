package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.shop;

public class v_Shop extends shop{
	
	private static Logger _log = Logger.getLogger(v_Shop.class.getName());
	
	private static String mainHtml(L2PcInstance player, String IdCategoria){
		String par = Engine.enumBypass.Shop.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += "<br><center>" + mainMenuCentral(IdCategoria,player) + "</center>";
		
		retorno += "<br></body></html>";
		return retorno;
	}
	
	private static String mainMenuCentral_C(String idCategoria, L2PcInstance player){
		String retorno = "<table width=760 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td></td></tr><tr><td width=760 align=center>";
		

		String CategoriaBase = getidSubcate(idCategoria);
		
		
		String byPassBack = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";Shop;secc;"+CategoriaBase+";0;0;0;0";
		
		String GrillaBack = "<table width=102><tr><td width=50><img src=L2UI_CT1.Inventory_DF_Btn_RotateRight width=40 height=32></td>"+
		"<td width=64><button value=\"Back\" action=\""+ byPassBack +"\" width=94 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>"+
        "<td width=64><img src=L2UI_CT1.Inventory_DF_Btn_RotateLeft width=40 height=32></td></tr></table><br>";
		
		String btnCentral = "<td><table cellspacing=1 cellpadding=3 width=190 background=L2UI_CT1.Windows_DF_TooltipBG>"+
        "<tr><td width=32><img src=\"%IMAGEN%\" width=32 height=32><br></td><td width=188 align=\"CENTER\">"+
        "<button value=\"%NOMBRE%\" action=\"%BYPASS%\" width=188 height=26 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>"+
        "</td></tr></table></td>";

		retorno += GrillaBack + "<table width=190 cellspacing=0  cellpadding=-1>";
		
		int contador = 1;
		
		Iterator itr = general.SHOP_DATA.entrySet().iterator();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    	  	//Entrada.getKey().toString()
    	  	//Entrada.getValue().toString()
    		HashMap<String, String>Temporal = new HashMap<String,String>();
    		Temporal = (HashMap<String, String>) Entrada.getValue();
    		if(Temporal.get(general.shopData.ID_SEC.name()).equals(idCategoria)){
    			if(contador==1){
    				retorno +="<tr>";
    			}
    			
    			retorno += btnCentral.replace("%BYPASS%", getByPass(Temporal,idCategoria,false)).replace("%IMAGEN%", getImagenItem(Integer.valueOf(Temporal.get(general.shopData.ID_ITEMSHOW.name())))).replace("%NOMBRE%", Temporal.get(general.shopData.NOM.name()));
    			contador++;
    			if(contador>2){
    				contador=1;
    				retorno +="</tr>";
    			}
    		}
	    }
		
		if(contador==2){
			retorno +="</tr>";
		}
		
		retorno += "</table><br><br></td></tr></table>";
		
		
		
		return retorno;
	}
	
	private static String mainMenuCentral(String idCategoria, L2PcInstance player){
		if(!idCategoria.equals("-1")){
			return mainMenuCentral_C(idCategoria, player);
		}
		String retorno ="<table width=760 height=200 background=L2UI_CT1.Windows_DF_Drawer_Bg>";
		Iterator itr = general.SHOP_DATA.entrySet().iterator();
		int contador = 1;
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    	  	//Entrada.getKey().toString()
    	  	//Entrada.getValue().toString()
    		HashMap<String, String>Temporal = new HashMap<String,String>();
    		Temporal = (HashMap<String, String>) Entrada.getValue();
    		if(Temporal.get(general.shopData.ID_SEC.name()).equals(idCategoria)){
    			String bypass = getByPass(Temporal,general.npcGlobal(player,true),false);
    			String btnShop = "<td width=253 align=center><table width=220 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td align=CENTER><table width=190 cellpadding=0><tr><td width=32>"+
                "<img src=\"" + Temporal.get(general.shopData.IMA.name())  +"\" width=32 height=32><br></td><td width=188 align=\"CENTER\">"+
                "<font color=\"FFCC00\">"+ Temporal.get(general.shopData.DESCRIP.name()) +"</font></td></tr></table>"+
                "<button value=\""+ Temporal.get(general.shopData.NOM.name()) +"\" action=\""+ bypass +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_BuyEtc\" fore=\"L2UI_CT1.OlympiadWnd_DF_BuyEtc\">"+
                "</td></tr></table><br></td>";
    			if(contador==1){
    				retorno += "<tr>";
    			}
    			
    			//player.addBypass2(bypass.replace("bypass ", ""));
    			
    			retorno += btnShop;
    			contador++;
    			if(contador>=4){
    				contador=1;
    				retorno += "</tr>";
    			}
			}
	    }
	    
	    String btnShop_vacio = "<td width=253 align=center><table width=220 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td align=CENTER><table width=190 cellpadding=0><tr>"+
        "<td width=32></td><td width=188></td></tr></table></td></tr></table><br></td>";

	    if(contador>1 && contador <4){
	    	for(int i=contador;i<4;i++){
	    		retorno+=btnShop_vacio;
	    	}
	    	retorno += "</tr>";
	    }

	    retorno +="</table><br>";
		
		
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
			return mainHtml(player,"-1");
		}else if(parm1.equals("multisell")){
			showMultisell(Integer.valueOf(parm2),player,true);
			//return mainHtml(player,params);
		}else if(parm1.equals("exc_multisell")){
			showMultisell(Integer.valueOf(parm2),player,false);
		}else if(parm1.equals("buy")){
			showBuylist(player, Integer.valueOf(parm2));
		}else if(parm1.equals("secc")){
			return mainHtml(player,parm2);
		}
		return "";
	}
}
