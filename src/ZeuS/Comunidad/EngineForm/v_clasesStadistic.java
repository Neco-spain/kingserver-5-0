package ZeuS.Comunidad.EngineForm;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class v_clasesStadistic {
	
	private static Logger _log = Logger.getLogger(v_clasesStadistic.class.getName());
	
	private static String WidthxPorcentaje(int idClase){
		int MaximoWidth = 160;
		int Cantidad = 0;
		String Race = general.classData.get(idClase).get("CLASSBASE");

		int UniversoPlayer = 0;
		
		if(general.getRaceCount().containsKey(Race)){
			UniversoPlayer = general.getRaceCount().get(Race);
		}
		
		try{
			Cantidad = general.getClasesCount().get(idClase);
		}catch(Exception a){

		}
		
		int NuevoWidth = ( UniversoPlayer>0 ? Math.round(( MaximoWidth * Cantidad ) / UniversoPlayer) : 0);
		
		return String.valueOf(NuevoWidth);
		
	}
	
	private static String getAllClases(String strRace){
		String retorno = "";
		String []Clases = {"Human:icon.skillhuman","Elf:icon.skillelf","Dark Elf:icon.skilldarkelf","Orc:icon.skillorc","Dwarf:icon.skilldwarf","Kamael:icon.skillkamael"};
		
		int contador = 0;
		int ContBarra = 0;
		
		String []Barras={"L2UI_CT1.Gauge_DF_Attribute_Water","L2UI_CT1.Gauge_DF_Attribute_Fire","L2UI_CT1.Gauge_DF_Attribute_Earth","L2UI_CT1.Gauge_DF_Attribute_Wind","L2UI_CT1.Gauge_DF_Attribute_Dark","L2UI_CT1.Gauge_DF_Attribute_Divine"};
		String []BarraFondo={"L2UI_CT1.Gauge_DF_Attribute_Water_bg","L2UI_CT1.Gauge_DF_Attribute_Fire_bg","L2UI_CT1.Gauge_DF_Attribute_Earth_bg","L2UI_CT1.Gauge_DF_Attribute_Wind_bg","L2UI_CT1.Gauge_DF_Attribute_Dark_bg","L2UI_CT1.Gauge_DF_Attribute_Divine_bg"};
		retorno += "<table width=748 border=0 cellspacing=2 background=L2UI_CT1.Windows_DF_TooltipBG height=45><tr>";
		
		for(String Class : Clases){
			String ByPass = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC +";" + Engine.enumBypass.ClasesStadistic.name() + ";0;"+Class.split(":")[0]+";0;0;0;0";
			retorno += "<td width=32>"+
             cbFormato.getBotonForm(Class.split(":")[1], ByPass)+"<br></td>"+
			"<td fixwidth=92 align=LEFT>"+
            "<font name=\"hs12\" "+ (Class.split(":")[0].equals(strRace) ?  "color=0080FF":"" ) +">"+Class.split(":")[0]+"</font></td>";
		}
		retorno += "</tr></table><table width=748 border=0 background=L2UI_CT1.Windows_DF_TooltipBG>";
			
		Iterator itr = general.classData.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idClase = (int) Entrada.getKey();
	    	if(general.classData.get(idClase).containsKey("CLASSBASE")){
			    	if(general.classData.get(idClase).get("CLASSBASE").equalsIgnoreCase(strRace)){
				    	if(contador==0){
				    		retorno += "<tr>";
				    	}
				    	retorno += "<td fixwidth=187 align=RIGHT>"+
				    			"<font color=\"FFFF99\">"+ opera.getClassName(idClase) + "</font><br1>"+
	               				"<table width=160 background="+ BarraFondo[ContBarra] +" cellspacing=0 cellpadding=0>"+
				                      "<tr>"+
				                          "<td fixwidth=160 height=18 align=RIGHT>"+
					                      "<img src=\""+ Barras[ContBarra] +"\" width="+WidthxPorcentaje(idClase)+" height=10>"+
				                          "</td>"+
				                      "</tr>"+
				               "</table><br></td>"+				    			
				    			//"<img src=\""+ Barras[ContBarra] +"\" width="+ WidthxPorcentaje(idClase) +" height=8><br></td>";
				    	
				    	contador++;
				    	ContBarra++;
				    	if(contador>=4){
				    		retorno += "</tr>";
				    		contador =0;
				    	}
				    	if(ContBarra >= Barras.length){
				    		ContBarra=0;
				    	}
			    	}
	    	}
	    }//end while
	    
	    if(contador>0 && contador <4){
	    	for(int i=contador;i<4;i++){
	    		retorno += "<td fixwidth=187 align=RIGHT><br1><br></td>";		    		
	    	}
	    	retorno +="</tr>";
	    }
	    retorno += "</table>";
		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params, String PalabraBuscar){
		String par = Engine.enumBypass.Flagfinder.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = "icon.skill6280";
		String Explica = "All you need to know about Clases Creation";
		String Nombre = "Clases Stadistic";
		retorno += cbFormato.getTituloCentral(Icono,Nombre,Explica,false,"bypass " + general.COMMUNITY_BOARD_PART_EXEC);  //cbFormato.getTituloCentral(Icono, Nombre, Explica);
		
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
		}
		return "";
	}
}
