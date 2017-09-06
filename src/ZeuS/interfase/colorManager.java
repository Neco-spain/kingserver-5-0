package ZeuS.interfase;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;


public class colorManager {

	public static boolean ColorName(L2PcInstance player, String ColorPintar){
		return pintar.ColorNombre(player,ColorPintar);
	}

	public static String ColorMenu(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Color Manager") + central.LineaDivisora(2);
		MAIN_HTML += msg.COLOR_MANAGER_MENSAJE;
		//MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Tu necesitarás " + central.ItemNeedShow(general.PINTAR_PRICE)  + "<br1> para pintar tu Nombre. Solo debes<br1>escoger el Color.","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += central.ItemNeedShowBox(general.PINTAR_PRICE);
		int i=0;int ii=0;

		String MAIN_HTML_G ="";

		for(String selecColor : general.PINTAR_MATRIZ.split(",")){
			if((i==0) || ((i%3) == 0)){
				if(i>0){
					MAIN_HTML_G += "</tr></table>";
				}
				MAIN_HTML_G += "<table width=279 bgcolor=\"151515\"><tr>";
			}
			MAIN_HTML_G += "<td width=93><center><a action=\"bypass -h ZeuSNPC Pintar " + selecColor + " 0 0\"><font color=\"" + selecColor + "\">This One!</font></a></center></td>";
			i++;
		}

		if(!MAIN_HTML_G.endsWith("</tr></table>")){
			MAIN_HTML_G += "</tr></table>";
		}

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(MAIN_HTML_G) + central.LineaDivisora(2);

		MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

}
