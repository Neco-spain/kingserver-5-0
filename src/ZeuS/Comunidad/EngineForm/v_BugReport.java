package ZeuS.Comunidad.EngineForm;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.msg;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class v_BugReport {
	
	private static String getMainWindows(){
		String Bypass = "Write Z_BUG_REPORT_NEW Set _ cmbTipo txtMensaje txtMensaje";
		String retorno = "<center><table width=750 background=L2UI_CT1.Windows_DF_TooltipBG cellpadding = 2 cellspacing=3><tr><td fixwidth=750 align=CENTER>"+
        "<font name=\"hs12\">Select Type Report:</font></td></tr><tr><td fixwidth=750 align=CENTER>"+
        "<combobox width=185 var=\"cmbTipo\" list=\""+ msg.BUG_REPORT_LISTA_REPORTES +"\"><br><br></td></tr><tr><td fixwidth=750>"+
        "<font name=\"hs12\" align=CENTER>Enter the report Please.</font></td></tr><tr><td fixwidth=750 align=CENTER>"+
        "<multiedit var=\"txtMensaje\" width=720 height=120><br></td></tr><tr><td fixwidth=750 align=CENTER>"+
        "<button value=\"Report It Please\" action=\""+ Bypass +"\" width=148 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"+
        "</td></tr></table></center>";
		
		return retorno;
		
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.BugReport.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
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
}
