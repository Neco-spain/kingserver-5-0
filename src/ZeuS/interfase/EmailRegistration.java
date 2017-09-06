package ZeuS.interfase;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;
import ZeuS.server.httpResp;

public class EmailRegistration {
	private static HashMap<Integer,String> charCode = new HashMap<Integer,String>();
	private static final Logger _log = Logger.getLogger(EmailRegistration.class.getName());

	private static Vector<Integer> CheckingList = new Vector<Integer>();
	private static HashMap<Integer,Boolean> OnEmailWindow = new HashMap<Integer, Boolean>();
	private static HashMap<Integer,String> EmailForAccount = new HashMap<Integer, String>();

	private static HashMap<Integer, Integer> IntentosDePlayer = new HashMap<Integer, Integer>();

	private static String getIDForEmailRegis(L2PcInstance player){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(999999);
		charCode.put(player.getObjectId(), String.valueOf(RandChar));
		return String.valueOf(RandChar);
	}


	public static boolean hasEmailRegister(L2PcInstance player){
		boolean retorno = true;
		String EmailAccount = opera.getUserMail(player.getAccountName());
		if(EmailAccount==null){
			retorno = false;
		}else{
			if(EmailAccount.length()<=0){
				retorno = false;
			}
		}
		return retorno;
	}
	
	public static void baypass(L2PcInstance player, String Params){
		if(Params==null){
			getRegistrationWindos(player, true);
		}else{
			if(Params.split(" ").length==3){
				if(Params.split(" ")[0].equals("SEND_EMAIL_CODE")){
					String Email1 = Params.split(" ")[1], Email2 = Params.split(" ")[2];
					if(!Email1.equals(Email2)){
						getRegistrationWindos(player,true);
						central.msgbox("User email not match, try again", player);
						return;
					}

					if(general.REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT){
						if(opera.emailHasBannedAccount(player, Email1)){
							opera.setBanToAccChar(player, "Register Email Check");
							return;
						}
					}

					if(httpResp.sendCodeForRegistration(Params.split(" ")[1], getIDForEmailRegis(player), player)){
						EmailForAccount.put(player.getObjectId(),Params.split(" ")[1]);
						getRegistrationWindos(player,false);
					}
				}else if(Params.split(" ")[0].equals("CHECK_EMAIL_CODE")){
					String CodeFromWeb = Params.split(" ")[1];
					if(charCode.get(player.getObjectId()).equals(CodeFromWeb)){
						if(opera.setUserEmailtoBD(player,EmailForAccount.get(player.getObjectId()))){
							EmailForAccount.remove(player.getObjectId());
							charCode.remove(player.getObjectId());
							opera.setImmbileChar(player, false);
							player._isChatBlock = false;
							central.msgbox(msg.ACCOUNT_THE_EMAIL_HAS_BEEN_SUCCESFULLY_UPDATED, player);
						}
					}else{
						central.msgbox("Wrog validation code, enter again", player);
						getRegistrationWindos(player,false);
					}
				}
			}else{
				central.msgbox("Wrog Input Data", player);
				getRegistrationWindos(player, true);
			}
		}
	}

	public static void sendEmailToChar(L2PcInstance player, String Email){

	}

	public static void isRegisterUser(L2PcInstance player){

		if(!general.REGISTER_EMAIL_ONLINE){
			return;
		}

		if(player != null){
			if(!player.getClient().isDetached()){
				if(!hasEmailRegister(player)){
					OnEmailWindow.put(player.getObjectId(), false);
					if(CheckingList!=null){
						if(CheckingList.contains(player.getObjectId())){
							OnEmailWindow.put(player.getObjectId(), false);
							ThreadPoolManager.getInstance().scheduleGeneral(new CheckAutomatic(player), general.REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME * 1000);
							return;
						}
					}
					ThreadPoolManager.getInstance().scheduleGeneral(new CheckAutomatic(player), general.REGISTER_NEW_PLAYER_WAITING_TIME * 1000);
					CheckingList.add(player.getObjectId());
				}
			}
		}
	}

	public static void getRegistrationWindos(L2PcInstance player, boolean emailSeccion){
		if(hasEmailRegister(player)){
			central.msgbox(msg.ACCOUNT_YOUR_ACCOUNT_IS_ALREADY_ASOCIATED_TO_AN_EMAIL_$email.replace("$email", opera.getUserMail(player.getAccountName())),player);
			return;
		}
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("ZeuS Registration System","LEVEL") + central.LineaDivisora(1);
		if(emailSeccion){
			IntentosDePlayer.put(player.getObjectId(), 0);
			String cajaTexto = "<multiedit var=\"txtEmail\" width=260 height=24>";
			String cajaTexto2 = "<multiedit var=\"txtEmail2\" width=260 height=24>";
			String btnAceptar = "<button value=\""+msg.ACCOUNT_BUTTON_SEND_ME_A_VALIDATION_CODE+"\" action=\"bypass -h voice .RegisEmailCMB SEND_EMAIL_CODE $txtEmail $txtEmail2\" width=260 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			String grilla = "<table with= 270>" +
					"<tr><td align=CENTER width=270 fixwidth=250>"+msg.ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL+"</td></tr>" +
					"<tr><td align=CENTER>"+cajaTexto+"</td></tr>" +
					"<tr><td align=CENTER></td></tr>" +
					"<tr><td align=CENTER></td></tr>" +					
					"<tr><td align=CENTER width=270 fixwith=250>"+ msg.ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_AGAIN +"</td></tr>" +
					"<tr><td align=CENTER>"+cajaTexto2+"</td></tr>" +
					"<tr><td align=CENTER></td></tr>" +
					"<tr><td align=CENTER>"+btnAceptar+"</td></tr>" +
					"</table>";
			HTML += central.LineaDivisora(1) + central.headFormat(grilla) + central.LineaDivisora(1);
		}else{
			IntentosDePlayer.put(player.getObjectId(), IntentosDePlayer.get(player.getObjectId()) + 1);
			if(IntentosDePlayer.get(player.getObjectId()) <= general.REGISTER_NEW_PlAYER_TRIES){
				String cajaClave = "<edit type=\"text\" var=\"txtEmailPass\" width=260>";
				String btnAceptar = "<button value=\""+ msg.ACCOUNT_BUTTON_CHECK_BUTTON +"\" action=\"bypass -h voice .RegisEmailCMB CHECK_EMAIL_CODE $txtEmailPass 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				String grilla = "<table with= 270>" +
						"<tr><td align=CENTER>"+msg.ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_CODE+"</td></tr>" +
						"<tr><td align=CENTER>"+cajaClave+"</td></tr>" +
						"<tr><td align=CENTER></td></tr>" +
						"<tr><td align=CENTER>"+btnAceptar+"</td></tr>" +
						"</table>";
				HTML += grilla;
			}else{
				IntentosDePlayer.put(player.getObjectId(), 0);
				try{
					EmailForAccount.remove(player.getObjectId());
				}catch(Exception a){

				}
				try{
					charCode.remove(player.getObjectId());
				}catch(Exception a){

				}
				player.getClient().closeNow();
				return;
			}
		}

		HTML += central.getPieHTML() + "</body></html>";
		central.sendHtml(player, HTML);
	}

	public static class CheckAutomatic implements Runnable{
		L2PcInstance activeChar;
		public CheckAutomatic(L2PcInstance player){
			activeChar = player;
		}
		@Override
		public void run(){
			if(!OnEmailWindow.get(activeChar.getObjectId())){
				if(general.REGISTER_EMAIL_ONLINE){
					getRegistrationWindos(activeChar, true);
					opera.setImmbileChar(activeChar, true);
					if(general.REGISTER_NEW_PLAYER_BLOCK_CHAT){
						activeChar._isChatBlock = true;
					}
				}
			}
		}
	}

}
