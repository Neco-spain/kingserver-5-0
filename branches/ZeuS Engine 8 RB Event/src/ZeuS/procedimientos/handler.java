package ZeuS.procedimientos;

import java.util.Collection;
import java.util.logging.Logger;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.handler.VoicedCommandHandler;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.sendC;
import ZeuS.Comunidad.sendH;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Comunidad.EngineForm.v_Buffer;
import ZeuS.Comunidad.EngineForm.v_ElementalSpecial;
import ZeuS.Comunidad.EngineForm.v_EnchantSpecial;
import ZeuS.Comunidad.EngineForm.v_RaidBossInfo;
import ZeuS.Comunidad.EngineForm.v_auction_house;
import ZeuS.Comunidad.EngineForm.v_donation;
//import ZeuS.Comunidad.EngineForm.v_dropsearch;
import ZeuS.Comunidad.EngineForm.C_findparty;
import ZeuS.Comunidad.EngineForm.v_partymatching;
import ZeuS.Comunidad.EngineForm.v_pvppkLog;
import ZeuS.Comunidad.EngineForm.v_variasopciones;
import ZeuS.Comunidad.EngineForm.v_variasopciones.loadByPass;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.dressme.dressme;
import ZeuS.event.RaidBossEvent;
import ZeuS.event.TownWarEvent;
import ZeuS.interfase.EmailRegistration;
import ZeuS.interfase.bufferChar;
import ZeuS.interfase.captchaPLayer;
import ZeuS.interfase.central;
import ZeuS.interfase.changePassword;
import ZeuS.interfase.charPanel;
import ZeuS.interfase.fixMe;
import ZeuS.interfase.htmls;
import ZeuS.interfase.pinCode;
import ZeuS.interfase.sellBuff;
import ZeuS.interfase.shop;
import ZeuS.interfase.showMyStat;
import ZeuS.interfase.teleport;
import ZeuS.interfase.votereward;



public class handler implements IVoicedCommandHandler {

	private final Logger _log = Logger.getLogger(handler.class.getName());
	
	public static void registerHandler(){
		VoicedCommandHandler.getInstance().registerHandler(new VoicedHandler());
	}
	public static String []getCommand(){
		return VOICED_COMMANDS;
	}
	
	private static final String[] VOICED_COMMANDS = {"zeus",
		"zeus_buffer",
		"dressmeGetAllSet",
		"dressme_share",
		"dressme_target",
		"dressme_target_indi",
		"dress_add_indi",
		"dressme",
		"dressme_save",
		"dressme_choose",
		"teamevent",
		"checkbot",
		"cancelcheckbot",
		"zeusBoot",
		"z_c_c",
		"z_t_a",
		"z_t_m",
		"z_t",
		"zeusvote",
		"expon",
		"expoff",
		"exp_on",
		"exp_off",			
		"Pin_Ingreso_SERVER",
		"stat",
		"resetpin",
		"setnoble",
		"configpanelchar",
		"zeus_load_plus",
		"zeus_char_plus",
		"ZZPPLL",
		"RegisEmailCMB",
		"CharPnl",
		"charpanel",
		"fixme",
		"fixmeCharName",
		"myinfo",
		"zeus_raid_start",
		"joinraid",
		"leaveraid",
		"acc_register",
		"changepassword",
		"changepCMD",
		"vote",
		"online",
		"makeancientadena",
		"townwar_start",
		"townwar_stop",
		"townwar_check",
		"zeus_cb_buff",
		"Z_Ds_ItEm",
		"Z_Ds_MoNsTeR",
		"Z_DS_pvppkLoG",
		"Z_DS_S_r_B",
		"buffstore",
		"ZeBuSell",
		"ZeOcCto",
		"party",
		"AuCtIoNsCaS",
		"obsr",
		"oly_buff",
		"ZeUsCrEaTESch",
		"ZeDoSecc"
		};	


	private static class VoicedHandler implements IVoicedCommandHandler {
		
		
		private static final Logger _log = Logger.getLogger(sellBuff.class.getName());
		
		
		/**
		 *
		 * @see l2r.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
		 */
		@Override
		public String[] getVoicedCommandList() {
			return VOICED_COMMANDS;
		}

		/**
		 *
		 * @see l2r.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String,
		 *      l2r.gameserver.model.actor.instance.L2PcInstance,
		 *      java.lang.String)
		 */
		@Override
		public boolean useVoicedCommand(String command,	L2PcInstance activeChar, String params){
			if(activeChar!=null){
				general._byPassVoice.put(activeChar.getObjectId(), opera.getUnixTimeL2JServer());
			}
			if(command.equals("ZeDoSecc")){
				if(params!=null){
					v_donation.bypass_voice(activeChar, params);
					return true;
				}
			}else if(command.equals("ZeUsCrEaTESch")){
				String EnviarVacio = general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0";
				String EnviarConTexto = general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.OlyBuffer.name() + ";CREATE_SCHEME;%TEXTO%;0;0;0;0";
				String EnviarHTML = "";
				if(params==null){
					EnviarHTML = EnviarVacio;
				}else{
					if(params.trim().length()==0){
						EnviarHTML = EnviarVacio;
					}else{
						EnviarHTML = EnviarConTexto.replace("%TEXTO%", params);
					}
				}
				cbManager.separateAndSend(C_oly_buff.bypass(activeChar,EnviarHTML), activeChar);
			}else if(command.equals("oly_buff")){
				if(!general.OLY_CAN_USE_SCHEME_BUFFER){
					central.msgbox(msg.DISABLED_BY_ADMIN, activeChar);
					return false;
				}
				
				if(!activeChar.isNoble()){
					central.msgbox("This command is only for noble player", activeChar);
					return true;
				}
				if(opera.canUseCBFunction(activeChar)){
					String Enviar = C_oly_buff.bypass(activeChar, general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0");
					cbManager.separateAndSend(Enviar, activeChar);
				}else{
					central.msgbox("This command are blocked on this Area, Zone or Time", activeChar);
				}
			}else if(command.equals("AuCtIoNsCaS")){
				v_auction_house.voiceByPass(activeChar, params);
			}else if(command.equals("party")){
				C_findparty.sendRequestToAll(activeChar,params);
			}else if(command.equals("ZeOcCto")){
				if(params!=null){
					if(params.length()>0){
						if(params.split(";").length>1){
							if(params.split(";")[1].equals(Engine.enumBypass.SelectElemental.name())){
								String Retorno = v_ElementalSpecial.bypass(activeChar, params);
								cbManager.separateAndSend(Retorno, activeChar);
							}else if(params.split(";")[1].equals(Engine.enumBypass.SelectEnchant.name())){
								String Retorno = v_EnchantSpecial.bypass(activeChar, params);
								cbManager.separateAndSend(Retorno, activeChar);								
							}
						}else{
							if(params.split(" ").length>=2){
								if(params.split(" ")[0].equals(v_variasopciones.loadByPass.ChangeCharName.name())){
									_log.warning("Entro->" + params);
								}
							}
						}
					}
				}
			}else if(command.equals("ZeBuSell")){
				if(params!=null){
					if(params.length()>0){
						sellBuff.showMainWindows(activeChar, false, params);
						return true;
					}
				}
			}else if(command.equals("buffstore")){
				if(sellBuff.isBuffSeller(activeChar)){
					sellBuff.setStopSellBuff(activeChar);
					return true;
				}else{
					//sellBuff.setBuffSell(activeChar, 500L);
					sellBuff.showMainWindows(activeChar, true, "");
					return true;
				}
			}else if(command.equals("Z_DS_S_r_B")){
				if(params==null){
					v_RaidBossInfo.bypassVoice(activeChar, "");
					return true;
				}
				v_RaidBossInfo.bypassVoice(activeChar, params);
			}else if(command.equals("Z_DS_pvppkLoG")){
				if(params==null){
					return false;
				}
				v_pvppkLog.bypassVoice(activeChar, params);
			}//else if(command.equals("Z_Ds_ItEm")){
			//	if(params==null){
			//		return false;
			//	}				
			//	if(params.length()>0){
			//		String retornoStr = v_dropsearch.mainHtml(activeChar, "buscarDrop", params,0);
			//		cbManager.separateAndSend(retornoStr,activeChar);
			//	}
			//}
			//else if (command.endsWith("Z_Ds_MoNsTeR")){
			//	if(params==null){
			//		return false;
			//	}
			//	if(params.length()>0){
			//		//String retornoStr = v_dropsearch.mainHtml(activeChar, "SearchMonster", params,0);
			//		v_dropsearch.showByAction(activeChar,"SearchMonster",params);
			//	}				
			//}
			else if(command.equals("zeus_cb_buff")){
				//zeus_cb_buff $cmbScheme 0 0 0 0
				if(params==null){
					return false;
				}
				v_Buffer.delegar_voice(activeChar, params);
			}else if(command.equals("townwar_start")){
				if(activeChar.isGM()){
					TownWarEvent.getInstance().startEvent(false);
				}
			}else if(command.equals("townwar_stop")){
				if(activeChar.isGM()){
					TownWarEvent.getInstance().endEvent(false);
				}
			}else if(command.equals("makeancientadena")){
				long Ancient_a_Dar = 0l;
				boolean Transformo = false;
				if(opera.haveItem(activeChar, 6360)){
					Transformo=true;
					int BlueSealStone = (int) opera.ContarItem(activeChar, 6360);
					Ancient_a_Dar = ( BlueSealStone * 3 );
					opera.giveReward(activeChar, 5575, (int) Ancient_a_Dar);
					opera.removeItem(6360, BlueSealStone, activeChar);
				}
				if(opera.haveItem(activeChar, 6361)){
					Transformo=true;
					int GreenSealStone = (int) opera.ContarItem(activeChar, 6361);
					Ancient_a_Dar = ( GreenSealStone * 5 );
					opera.giveReward(activeChar, 5575, (int) Ancient_a_Dar);
					opera.removeItem(6361, GreenSealStone, activeChar);					
				}
				if(opera.haveItem(activeChar, 6362)){
					Transformo=true;
					int RedSealStone = (int) opera.ContarItem(activeChar, 6362);
					Ancient_a_Dar = ( RedSealStone * 10 );
					opera.giveReward(activeChar, 5575, (int) Ancient_a_Dar);
					opera.removeItem(6362, RedSealStone, activeChar);					
				}
				if(Transformo){
					central.msgbox("Process Finish", activeChar);
				}else{
					central.msgbox("U dont have Seal Stone", activeChar);
				}
			}else if(command.equals("online")){
				if(!general.COMMUNITY_BOARD_REGION){
					int onlinePlayer = opera.getOnlinePlayer();
					central.msgbox("-----------------------------", activeChar);
					central.msgbox( "Number of Player(s): " + String.valueOf(onlinePlayer + general.PLAYER_BASE_TO_SHOW) , activeChar);
					central.msgbox("-----------------------------", activeChar);
				}else{
					central.msgbox("To see this information please use alt+b Region Option.",activeChar);
				}
			}else if(command.equals("vote")){
				votereward.getVoteWindows(activeChar, true);
				return true;
			}else if(command.equals("changepCMD")){
				changePassword.bypass(activeChar, params);
				return true;
			}else if(command.equals("changepassword")){
				changePassword.bypass(activeChar, "");
				return true;
			}else if(command.equals("acc_register")){
				EmailRegistration.getRegistrationWindos(activeChar, true);
				return true;
			}else if(command.equals("leaveraid")){
				RaidBossEvent.removePlayer(activeChar);
				return true;
			}else if(command.equals("joinraid")){
				RaidBossEvent.addPlayer(activeChar);
				return true;
			}else if(command.equals("zeus_raid_start")){
				if(!activeChar.isGM()){
					return false;
				}
				RaidBossEvent.autoEvent();
				return true;
			}else if(command.equals("myinfo")){
				charPanel.getCharInfo(activeChar);
				return true;
			}else if(command.equals("fixmeCharName")){
				fixMe.delegar(activeChar, params);
				return true;
			}else if(command.equals("fixme")){
				fixMe.delegar(activeChar, "");
				return true;
			}else if(command.equals("charpanel")){
				if(!general.CHAR_PANEL){
					central.msgbox("Char Panel disabled", activeChar);
					return true;
				}
				charPanel.delegar(activeChar, "");
				return true;
			}else if(command.equals("CharPnl")){
				charPanel.delegar(activeChar, params);
				return true;
			}else if(command.equals("RegisEmailCMB")){
				EmailRegistration.baypass(activeChar, params);
				return true;
				/*
			}else if(command.equals("ZZPPLL")){
				htmls.setWindowsPlus(activeChar, params);
				return true;
			}else if(command.equals("zeus_char_plus")){
				htmls.setWindowsPlus(activeChar);
				return true;
			}else if(command.equals("zeus_load_plus")){
				if(opera.isPlusHTML(activeChar,true)){
					httpResp.getPJPlus();
				}
				return true;*/
			}else if(command.equals("zeus")){
				//htmls.showWindowsKnowZeus(activeChar);
				String EnviarBypass = general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0";
				cbManager.separateAndSend(Engine.delegar(activeChar, EnviarBypass),activeChar);
				return true;
			}else if(command.equals("configpanelchar")){

			}else if(command.startsWith("zeus_buffer")){
				bufferChar.delegar(activeChar, command, params);
			}else if(command.endsWith("dressmeGetAllSet")){
				dressme.getInstance().setAllDressmeFromTarger(activeChar, params);
			}else if(command.endsWith("dressme_share")){
				dressme.getInstance().setShareDressme(activeChar);
			}else if(command.endsWith("dressme_target_indi")){
				dressme.getInstance().setItemToDressmeFromTarget(activeChar, params);
			}else if(command.endsWith("dressme_target")){
				int idtoShow = 1;
				if(params!=null){
					if(params.length()>=2){
						if(opera.isNumeric(params.split(" ")[2])){
							idtoShow = Integer.valueOf(params.split(" ")[2]);
						}
					}
				}
				dressme.getInstance().dressMe_See(activeChar, idtoShow, true);
			}else if(command.endsWith("dress_add_indi")){
				try{
					dressme.getInstance().setNewItemToDressme(activeChar, params.split(" ")[0], Integer.valueOf(params.split(" ")[2]), Integer.valueOf(params.split(" ")[1]));
				}catch(Exception a){
					dressme.getInstance().dressMe_See(activeChar, 1);
					return true;
				}
				dressme.getInstance().dressMe_See(activeChar, Integer.valueOf(params.split(" ")[2]));
			}else if(command.endsWith("dressme")){
				int ShowWind = 1;
				if(params!=null){
					if(params.length()>0){
						if(opera.isNumeric(params)){
							ShowWind = Integer.valueOf(params);
							if((ShowWind<1) || (ShowWind > 8)){
								ShowWind = 1;
							}
						}
					}
				}
				dressme dressmeCommand = dressme.getInstance();
				dressmeCommand.dressMe_See(activeChar, ShowWind);
			}/*else if(command.startsWith("dressme") && !command.startsWith("dressme_choose")){
				_log.warning("Delegado: "+command);
				_log.warning("Parametros: " + params);
				int ShowWind = 1;
				if(params.length()>0){
					_log.warning("Entro despues del lengh" +  String.valueOf(params.length()));
					if(opera.isNumeric(params)){
						_log.warning("Entro al isNumeric");
						ShowWind = Integer.valueOf(params);
						_log.warning("->"+ String.valueOf(ShowWind));
						if((ShowWind<1) || (ShowWind > 8)){
							ShowWind = 1;
						}
					}
				}
				_log.warning("Seleccionado->"+ String.valueOf(ShowWind));
				dressme dressmeCommand = dressme.getInstance();
				dressmeCommand.dressMe_See(activeChar, ShowWind);
			}*/else if(command.startsWith("dressme_save")){
				dressme dressmeCommand = dressme.getInstance();
				dressmeCommand.dressMe_command(activeChar, params);
			}else if(command.startsWith("dressme_choose")){
				dressme dressmeCommand = dressme.getInstance();
				dressmeCommand.dressme_choose(params,activeChar);
				//dressme.getInstance().dressme_choose(activeChar, params);
			}else if(command.equals("teamevent")){
				if(opera.isMaster(activeChar)){
					teamEvent.getHTMLAdminConfig(activeChar);
				}
			}else if(command.startsWith("cancelcheckbot")){
				captchaPLayer.cancelbotCheck(activeChar, params);
			}else if(command.startsWith("checkbot")){
				captchaPLayer.bypass(activeChar, command + " " + params);
			}else if(command.startsWith("zeusBoot")){
				captchaPLayer.bypass(activeChar, command + " " + params);
			}else if(command.equalsIgnoreCase("z_c_c")){
				if(opera.isMaster(activeChar)){
					shop.ShopByPass(activeChar, command);
				}
			}else if(command.equalsIgnoreCase("z_t_a")){
				if(opera.isMaster(activeChar)){
					teleport.bypassMain_edit(params, activeChar);
				}
			}else if(command.equalsIgnoreCase("z_t_m")){
				if(opera.isMaster(activeChar)){
					teleport.bypassMain(params, activeChar);
				}
			}else if(command.equalsIgnoreCase("z_t")){
				if(opera.isMaster(activeChar)) {
					teleport.bypassTeleport(params, activeChar);
				}
			}else if (command.equalsIgnoreCase("zeusvote")) {

			}else if (command.equalsIgnoreCase("expon") || command.equalsIgnoreCase("exp_on")) {
				if(!general.RATE_EXP_OFF){
					central.msgbox_Lado("Command disabled by Admin", activeChar);
					return true;
				}
				general.getInstance().setConfigExpSp(activeChar,true);
				central.msgbox_Lado("Experiencia activada", activeChar);
			}else if(command.equalsIgnoreCase("expoff") || command.equalsIgnoreCase("exp_off")){
				if(!general.RATE_EXP_OFF){
					central.msgbox_Lado("Command disabled by Admin", activeChar);
					return true;
				}
				general.getInstance().setConfigExpSp(activeChar,false);
				central.msgbox_Lado("Experiencia desactivada", activeChar);
			}else if(command.equalsIgnoreCase("Pin_Ingreso_SERVER") || command.startsWith("Pin_Ingreso_SERVER")){
				pinCode.inPin(activeChar,params);
			}else if(command.equals("stat")){
				L2Object target = activeChar.getTarget();
				if(target instanceof L2PcInstance ){
					showMyStat.showMyStat((L2PcInstance) target, activeChar);
				}else{
					central.msgbox(msg.EL_TARGET_NO_ES_PLAYER, activeChar);
				}
			}else if(command.equals("resetpin")){
				if(opera.isMaster(activeChar)){
					L2Object target = activeChar.getTarget();
					if(target instanceof L2PcInstance ){
						if(!pinCode.ResetPINCode((L2PcInstance) target)){
							central.msgbox("Error reset pin.", activeChar);
						}else{
							central.msgbox(msg.GM_RESET_PIN, (L2PcInstance)target);
							central.msgbox("Reset Pin OK", activeChar);
						}
					}else{
						central.msgbox(msg.EL_TARGET_NO_ES_PLAYER, activeChar);
					}
				}
			}else if(command.equals("setnoble")){
				if(opera.isMaster(activeChar)){
					L2Object target = activeChar.getTarget();
					if(target instanceof L2PcInstance){
						L2PcInstance targetChar = (L2PcInstance) target;
						if(!targetChar.isNoble()){
							targetChar.setNoble(true);
							central.msgbox(msg.EL_PLAYER_$player_ES_NOBLE.replace("$player", targetChar.getName()), activeChar);
							central.msgbox(msg.TU_ERES_NOBLE, targetChar);
						}else{
							targetChar.setNoble(false);
							central.msgbox(msg.EL_PLAYER_$player_YA_NO_ES_NOBLE.replace("$player", targetChar.getName()), activeChar);
							central.msgbox(msg.TU_NO_ERES_NOBLE, targetChar);
						}
					}else{
						central.msgbox(msg.EL_TARGET_NO_ES_PLAYER, activeChar);
					}
				}
			}
			return true;
		}
	}


	@Override
	public String[] getVoicedCommandList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean useVoicedCommand(String arg0, L2PcInstance arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}
}
