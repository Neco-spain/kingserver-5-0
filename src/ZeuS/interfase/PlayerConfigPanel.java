package ZeuS.interfase;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;

public class PlayerConfigPanel {

	public static void setConfig(L2PcInstance player, int annoucement, int efectpvppk, int showmystat,int showpinwindows, int showHero){
		//central.set_ConfigPVPPK(player,efectpvppk,annoucement,showmystat,showpinwindows,showHero);
		central.set_ConfigCHAR(player, efectpvppk, annoucement, showmystat, showpinwindows, showHero,
				( general.getCharConfigEXPSP(player) ? 1 : 0 ), ( general.getCharConfigTRADE(player) ? 1 : 0 ),
				( general.getCharConfigBADBUFF(player) ? 1 : 0 ), ( general.getCharConfigHIDESTORE(player) ? 1 : 0 ), ( general.getCharConfigREFUSAL(player) ? 1 : 0 ), ( general.getCharConfigPartyMatching(player) ? 1 : 0 ) );
	}

	public static void setHeroDona(L2PcInstance player, boolean estado){
		int ANNOUCSTR = opera.TOP_PVP_PK_ANNOU(player) ? 1 : 0;
		int EFECTPVP = opera.TOP_PVP_PK_EFFECT(player) ? 1 : 0;
		int SHOWSHIFT = opera.SHOW_MY_STAT_SHIFT(player) ? 1 : 0;
		int SHOWPINWINDOWS = opera.SHOW_PIN_WINDOWS(player) ? 1 : 0;
		int SHOWHERO = opera.SHOW_HERO_PLAYER(player) ? 1 : 0;
		setConfig(player,ANNOUCSTR,EFECTPVP,SHOWSHIFT,SHOWPINWINDOWS,(estado ? 1 : 0));
	}

	public static void delegar(L2PcInstance player, String comando, String parametros){
		if(parametros==null){
			opera.enviarHTML(player, getConfigPanel(player,true));
			return;
		}else{
			String[] params = parametros.split(" ");
			if(comando.equals("ConfigPanel")){
				int ANNOUCSTR = opera.TOP_PVP_PK_ANNOU(player) ? 1 : 0;
				int EFECTPVP = opera.TOP_PVP_PK_EFFECT(player) ? 1 : 0;
				int SHOWSHIFT = opera.SHOW_MY_STAT_SHIFT(player) ? 1 : 0;
				int SHOWPINWINDOWS = opera.SHOW_PIN_WINDOWS(player) ? 1 : 0;
				int SHOWHERO = opera.SHOW_HERO_PLAYER(player) ? 1 : 0;

				if(params[0].equals("1")){
					//Annoucement entrada del TOP PVP PK
					setConfig(player,Integer.valueOf(params[1]),EFECTPVP,SHOWSHIFT,SHOWPINWINDOWS,SHOWHERO);
				}else if(parametros.equals("2")){
					//Efectos Top PVP / PK
					setConfig(player, ANNOUCSTR ,Integer.valueOf(params[1]),SHOWSHIFT,SHOWPINWINDOWS,SHOWHERO);
				}else if(parametros.equals("3")){
					//ver Estado oponente con Shift
					setConfig(player, ANNOUCSTR , EFECTPVP ,Integer.valueOf(params[1]),SHOWPINWINDOWS,SHOWHERO);
				}else if(parametros.equals("4")){
					//PIN WINDOWS
					setConfig(player, ANNOUCSTR , EFECTPVP ,  SHOWSHIFT ,Integer.valueOf(params[1]),SHOWHERO);
				}
			}

		}
	}

	public static String getPinCambiado(L2PcInstance st, String numPinNuevo){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Config Panel - " + st.getName()) + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("PIN CODE CHANGE (4 Digit)","LEVEL") + central.LineaDivisora(2) + "<br1>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Pin changed.<br1> Ypur new Pin: <font color=LEVEL>" + numPinNuevo + "</font>","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"CONFIG MENU\" action=\"bypass -h ZeuSNPC ConfigPanel 0 0 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","") + central.LineaDivisora(2);
		MAIN_HTML += "<br><br><br><br>" + central.BotonGOBACKZEUS() + central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String getConfigPanel_ChangePIN(L2PcInstance st){
		return getConfigPanel_ChangePIN(st, false);
	}

	public static String getConfigPanel_ChangePIN(L2PcInstance st, boolean voiceCommand){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Config Panel - " + st.getName()) + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("PIN CODE CHANGE (4 Digit)","LEVEL") + central.LineaDivisora(2) + "<br1>";
		String TEXTO_PIN_ACTUAL = "<edit type=\"seguridad\" var=\"PIN_ACTUAL\" width=150>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Actual PIN" + TEXTO_PIN_ACTUAL + "<br>","WHITE") + central.LineaDivisora(2);
		String TEXTO_PIN_NUEVO = "<edit type=\"seguridad\" var=\"PIN_1\" width=150>";
		String TEXTO_PIN_NUEVO2 = "<edit type=\"seguridad\" var=\"PIN_2\" width=150>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("New PIN Code (4 Digit)" + TEXTO_PIN_NUEVO + "<br>Repeat new PIN CODE" + TEXTO_PIN_NUEVO2 + "<br>","WHITE") + central.LineaDivisora(2) + "<br>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"CHANGE PIN CODE\" action=\"bypass -h ZeuSNPC CHANGEPIN $PIN_ACTUAL $PIN_1 $PIN_2\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button value=\"Back\" action=\"bypass -h ZeuSNPC ConfigPanel 0 0 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","") + central.LineaDivisora(2);
		MAIN_HTML += central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}


	public static String getConfigPanel(L2PcInstance st){
		return getConfigPanel(st, false);
	}

	public static String getConfigPanel(L2PcInstance st, boolean voiceCommand){

		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Config Panel - " + st.getName()) + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("TOP 5 PvP / PK Config","LEVEL") + central.LineaDivisora(2) + "<br1>";

		//if(st.toppvppk_SHOWANNOUCEMENT_ALL()) {
		String DescripEfecto = msg.CONFIG_PANEL_ANUN_ENRADA;

		if(!opera.TOP_PVP_PK_ANNOU(st)){
			DescripEfecto = "<font color =A26D64>"+DescripEfecto+"</font>";
		}

		if(opera.TOP_PVP_PK_ANNOU(st)){
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_DESACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 1 0 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",DescripEfecto,0);
		} else {
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_ACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 1 1 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",DescripEfecto,0);
		}

		DescripEfecto = msg.CONFIG_PANEL_EFECTOS_TOP_PVPPK;

		if(!opera.TOP_PVP_PK_EFFECT(st)){
			DescripEfecto = "<font color =A26D64>"+DescripEfecto+"</font>";
		}

		//if (st.toppvppk_CAN_SHOW_EFECT()) {
		if(opera.TOP_PVP_PK_EFFECT(st)){
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_DESACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 2 0 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",DescripEfecto,0);
		} else {
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_ACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 2 1 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",DescripEfecto,0);
		}

		MAIN_HTML += "<br1>" + central.LineaDivisora(2) + central.headFormat(msg.CONFIG_PANEL_EXPICA_STAT,"WHITE") + central.LineaDivisora(2) + "<br1>";

		DescripEfecto = msg.CONFIG_PANEL_STAT;

		if(!opera.SHOW_MY_STAT_SHIFT(st)){
			DescripEfecto = "<font color =A26D64>"+DescripEfecto+"</font>";
		}

		if(opera.SHOW_MY_STAT_SHIFT(st)){
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_DESACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 3 0 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",DescripEfecto,0);
		} else {
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_ACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 3 1 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",DescripEfecto,0);
		}

		MAIN_HTML += "<br1>" + central.LineaDivisora(2) + central.headFormat(msg.CONFIG_PANEL_EXPLICA_PIN,"LEVEL") + central.LineaDivisora(2) + "<br1>";

		//if(st.showPinWindows()) {
		if(opera.SHOW_PIN_WINDOWS(st)){
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_DESACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 4 0 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","PIN SECURITY WINDOWS",0);
		} else {
			MAIN_HTML += central.BotonCentral("<button value=\""+msg.BTN_ACTIVAR+"\" action=\"bypass -h ZeuSNPC ConfigPanel 4 1 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","<font color=A26D64>PIN SECURITY WINDOWS</font>",0);
		}
		MAIN_HTML += central.BotonCentral("<button value=\"CHANGE\" action=\"bypass -h ZeuSNPC ConfigPanel 4 2 0\" width=98 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","Change PIN Code",0);

		MAIN_HTML += central.BotonGOBACKZEUS() + "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}
}
