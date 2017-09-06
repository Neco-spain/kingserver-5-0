package ZeuS.interfase;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.procedimientos.opera;
import ZeuS.server.httpResp;

public class changePassword {
	private static final Logger _log = Logger.getLogger(changePassword.class.getName());
	
	private static HashMap<L2PcInstance, HashMap<String, String>> CHANGEPASS_CHAR = new HashMap<L2PcInstance, HashMap<String, String>>();
	
	private static void cleanPass(L2PcInstance player){
		if(CHANGEPASS_CHAR!=null){
			if(CHANGEPASS_CHAR.containsKey(player)){
				CHANGEPASS_CHAR.remove(player);
			}
		}
				
	}
	
	private static String getIDForChangePass(){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(999999);
		return String.valueOf(RandChar);
	}
	
	public static void bypass(L2PcInstance player, String Parametros){
		if(Parametros.length()==0){
			getChangePassWindows(player, "");
			return;
		}
		String params[] = Parametros.split(" ");
		
		if(params.length<4 || params.length>4){
			if(params[0].equals("C1")){
				central.msgbox("Wrog input data", player);
				getChangePassWindows(player, "");
			}else if(params[0].equals("C2")){
				central.msgbox("Wrog input data", player);
			}
		}
		
		//_log.warning("largo=" + String.valueOf(params.length));
		if(params[0].equals("C1")){
			String pass1 = params[1];
			String pass2 = params[2];
			String nomChar = params[3];
			
			if(pass1.length()<4){
				central.msgbox("Password should be Higher than 4 characters", player);
				getChangePassWindows(player, "");
				return;
			}
			
			if(pass1.equals(pass2)){
				cleanPass(player);
				String ID_EMAIL = getIDForChangePass();
				//central.msgbox(ID_EMAIL, player);
				CHANGEPASS_CHAR.put(player, new HashMap<String, String>());
				CHANGEPASS_CHAR.get(player).put("ID", ID_EMAIL);
				CHANGEPASS_CHAR.get(player).put("PASS", pass1);
				
				httpResp.sendCodeForRegistration(opera.getUserMail(player.getAccountName()),ID_EMAIL,player);
				
				getChangePassWindows_EnterCode(player,ID_EMAIL,player.getName());
			}else{
				central.msgbox("The passwords do not match", player);
				getChangePassWindows(player, "");
			}
		}else if(params[0].equals("C2")){
			//C2 $txtcode "+ idSet +" "+ NomChar
			String idCodeToEmail = CHANGEPASS_CHAR.get(player).get("ID");
			String NewPass = CHANGEPASS_CHAR.get(player).get("PASS");
			if(params[1].equals(idCodeToEmail)){
				if(setNewPassword(player, NewPass)){
					_log.warning("Change Pass Account:" + player.getAccountName() + ", character:" + player.getName()+", new password:"+NewPass);
					central.msgbox("New pass for Account " + player.getAccountName()+" is: " + NewPass, player);
				}else{
					central.msgbox("Error to change password",player);
				}
			}else{
				getChangePassWindows_EnterCode(player,idCodeToEmail,player.getName());
			}
		}
	}
	
	private static boolean setNewPassword(L2PcInstance player, String newpass){
		int passUpdated=0;		
		try{
			MessageDigest md = MessageDigest.getInstance("SHA");		
			byte[] password = newpass.getBytes("UTF-8");
			password = md.digest(password);
			String accountName = player.getAccountName();
			// SQL connection
			try (Connection con = ConnectionFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("UPDATE "+general.LOGINSERVERNAME+".accounts SET password=? WHERE login=?"))
			{
				ps.setString(1, Base64.getEncoder().encodeToString(password));
				ps.setString(2, accountName);
				passUpdated = ps.executeUpdate();
			}
		}catch(Exception e){
			
		}
		
//		_log.info("The password for account " + accountName + " has been changed to " + Base64.getEncoder().encodeToString(password));
		if (passUpdated > 0){
			return true;
		}
		return false;
	}
	
	private static void getChangePassWindows_EnterCode(L2PcInstance player, String idSet, String NomChar){
		String txtCode = "<edit type=\"text\" var=\"txtcode\" width=240>";
		String btnAceptar = "<button value=\"CHANGE\" action=\"bypass -h voice .changepCMD C2 $txtcode "+ idSet +" "+ NomChar +"\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";;
		
		String tabla = "<table width=280>" +
						"<tr><td width=280 align=CENTER>Enter Validation Code from Email</td></tr>"+
						"<tr><td width=280 align=CENTER>"+ txtCode +"</td></tr>"+
					"</table><br>";
		
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Change password - Validation Process") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(tabla+"<br>"+btnAceptar) + central.LineaDivisora(1);
		
		html += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, html);
	}
	
	private static void getChangePassWindows(L2PcInstance player, String params){
		if(!EmailRegistration.hasEmailRegister(player)){
			central.msgbox("Sorry, but you do not have your account linked to an email.", player);
			return;
		}
		String pass1 = "<edit type=\"password\" var=\"txtPass1\" width=240>";
		String pass2 = "<edit type=\"password\" var=\"txtPass2\" width=240>";
		String btnAceptar = "<button value=\"CHANGE\" action=\"bypass -h voice .changepCMD C1 $txtPass1 $txtPass2 "+ player.getName() +"\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";;
		
		String tabla = "<table width=280>" +
						"<tr><td width=280 align=CENTER>Enter you new password</td></tr>"+
						"<tr><td width=280 align=CENTER></td></tr>"+
						"<tr><td width=280 align=CENTER>"+ pass1 +"</td></tr>"+
						"<tr><td width=280 align=CENTER></td></tr>"+
						"<tr><td width=280 align=CENTER>Enter you new password again</td></tr>"+
						"<tr><td width=280 align=CENTER>"+ pass2 +"</td></tr>"+
						"<tr><td width=280 align=CENTER></td></tr>"+
						"<tr><td width=280 align=CENTER fixwidth=260>"+ msg.CHANGE_PASS_EXPLAIN +"</td></tr>"+
					"</table><br>";
		
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Change password") + central.LineaDivisora(1);
		html += central.LineaDivisora(1) + central.headFormat(tabla+"<br>" + btnAceptar) + central.LineaDivisora(1);
		html += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, html);
	}
}
