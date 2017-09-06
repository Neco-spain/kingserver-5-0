package ZeuS.Comunidad.EngineForm;

import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class C_cmdInfo {
	
	private static Vector<String>cmd_char = new Vector<String>();
	private static Vector<String>cmd_ZeuS_Adm = new Vector<String>();
	
	private static Logger _log = Logger.getLogger(C_cmdInfo.class.getName());
	
	private static void getAllCommands(L2PcInstance player){
		if(cmd_ZeuS_Adm==null || cmd_ZeuS_Adm.size()==0){
			cmd_ZeuS_Adm.add("//zeus_config : ZeuS Config");
			cmd_ZeuS_Adm.add("//zeus_tele : Teleport Config");
			cmd_ZeuS_Adm.add("//zeus_shop : Shop Config");
			cmd_ZeuS_Adm.add("//oly_ban [name] : ban the target player from olys.");
			cmd_ZeuS_Adm.add("//oly_unban [name] : unban the target player from olys.");
			cmd_ZeuS_Adm.add("//oly_reset_point [name] : Remove all oly point from you target.");
			cmd_ZeuS_Adm.add("//oly_point [name] : give or remove oly point from you target (Positive Numbers give points, Negatives remove point ).");
			
		}
		if(cmd_char==null || cmd_char.size()==0){
			cmd_char.add(".zeus : Give all command's");
			cmd_char.add(".oly_buff : Give you the option to create a Buff Scheme to Olys.");
			cmd_char.add(".charpanel : Personal Configuration Character Panel");
			cmd_char.add(".vote : You can vote and get an personal reward.");
			cmd_char.add(".acc_register : You can link your account to an email.");
			cmd_char.add(".changepassword : You can change you password only if you have an email");
			//cmd_char.add(".zeus_buffer : Personal Buffer");
			cmd_char.add(".exp_on & .exp_off : To get or block exp/sp");
			cmd_char.add(".dressme : You can Change your Visual dress to another");
			cmd_char.add(".dressme_target : You can copy the Visual dress from you target if he/she wants");
			cmd_char.add(".dressme_share : You can share you actual dressme to another player");
			cmd_char.add(".checkbot : You can send an Check bot to another player on target");
			cmd_char.add(".stat : You can see the Personal Stat from you target if he/she wants");
			cmd_char.add(".fixme : You can try to fix one of you char on this account");
			cmd_char.add(".myinfo : You can get you personal vip information and Login's");
			cmd_char.add(".makeancientadena : Transfor you seal stone to Ancient Adena.");
			cmd_char.add(".party[Message] : Request party member on all world.");
			cmd_char.add(".buffstore : Create an private Buff Store, where you can sell your buff.");
			cmd_char.add(".joinraid : To join or know when the raidboss event comes");
			cmd_char.add(".leaveraid : To remove raidboss participation");
			cmd_char.add(".engage : You can send a marriage proposal to you target");
			cmd_char.add(".gotolove : You can go where you husband/wife is");
			cmd_char.add(".divorce : You can divorce with you husband or Wife");
			cmd_char.add(".deposit : You can create Gold Bar with adena");
			cmd_char.add(".withdraw : You can create adena with Gold Bar");
			cmd_char.add(".duel : Send to you target a duel invitation");
			cmd_char.add("/clanpenalty : Get information about the clan Penalty");
			cmd_char.add("/attacklist : Get information about clans you've declared war");
			cmd_char.add("/underattacklist : Get information about clans that have declared war on your clan");
			cmd_char.add("/warlist : Get information about the declared war");
			cmd_char.add("/instancezone : Get information about instance zone reused time");
			cmd_char.add("/olympiadstat : Get Information about you olympiad stat or you noble target");
			cmd_char.add("/mybirthday : Get Information about you creation date");
			cmd_char.add("/unstuck : Teleport your self to the nearest town/zone");
			cmd_char.add("/channelcreate : Create command channel");
			cmd_char.add("/channeldelete : Delete command channel");
			cmd_char.add("/channelinvite : Invite the target player party to you command channel");
			cmd_char.add("/channelkick : You can kick an player from your command channel");
			cmd_char.add("/channelleave : Leave command channel");
			cmd_char.add("/channelinfo : Get information about your command channel");
			cmd_char.add("/friendinvite : You can add you friend to your personal friend list");
			cmd_char.add("/friendlist : You can get all you friend list");
			cmd_char.add("/frienddel : You can remove a friend from your list");
			cmd_char.add("/block : You can block another player (chat, party, duel, trade)");
			cmd_char.add("/unblock : You can unblock an player from you blocklist");
			cmd_char.add("/blocklist : You can get all you blocked players");
			cmd_char.add("/gmlist : You can get info about the Adm/Gm of this server");
			cmd_char.add("/gm : Send and personal petition to an Adm/GM");
			cmd_char.add("/gmcancel : Cancel the personal petition");
		}
	}
	
	private static Vector<String> getAllCommandToShow(L2PcInstance player){
		Vector<String> T = new Vector<String>();
		
		if(player.isGM()){
			for(String g : cmd_ZeuS_Adm){
				T.add(g);
			}
		}
		
		for(String CM : cmd_char){
			T.add(CM);
		}

		return T;
	}
	
	private static String getAllCommand(L2PcInstance player, int pagina){
		getAllCommands(player);
		String retorno = "";
		
		int maximo = 15;
		int desde = pagina * maximo;
		int hasta = desde + maximo;
		int contador = 0;
		boolean haveNext = false;
		
		for(String t : getAllCommandToShow(player)){
			if(contador >= desde && contador < hasta){
				retorno += "<img src=L2UI.SquareGray width=744 height=2><table width=730 border=0 cellpadding=1><tr>"+
					"<td fixwidth=180 align=RIGHT><font color=FE9A2E>"+ t.split(":")[0] +" : </font></td>"+
					"<td fixwidth=550 align=LEFT><font color=04B4AE>"+ t.split(":")[1] +"</font></td>"+
					"</tr></table><img src=L2UI.SquareGray width=744 height=2>";
			}else if(contador > hasta){
				haveNext = true;
			}
			contador++;
		}
		
		
		String bypassAntes = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.commandinfo.name() + ";0;"+ String.valueOf(pagina - 1) +";0;0;0;0"; 
		String bypassProx = "bypass " + general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.commandinfo.name() + ";0;"+ String.valueOf(pagina + 1) +";0;0;0;0";
		
		String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">";
		String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">";
		
		
		retorno += "<br><table width=730 border=0 cellpadding=1><tr>"+
           "<td fixwidth=315 align=RIGHT>"+ (pagina > 0 ? btnAntes : "") +"</td>"+
           "<td fixwidth=100 align=CENTER><font color=04B4AE name=hs12>"+ String.valueOf(pagina + 1) +"</font></td>"+
           "<td fixwidth=315 align=LEFT>"+ (haveNext ? btnProx : "") +"</td></tr></table>";
		
		
		
		return retorno;
	}
	
	private static String mainHtml(L2PcInstance player, String Params, int Pagina){
		String retorno = "<html><body><title>Commands Info</title><center>" +cbFormato.getTituloEngine();
		String Icono = "icon.skill6274";
		String Explica = "<br>All Commands you can use on " + general.Server_Name;
		String Nombre = "Commands Info";
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false);

		retorno += getAllCommand(player, Pagina);
		
		retorno += cbManager.getPieCommunidad() +  "</body></html>";
		return retorno;
	}
	public static String bypass(L2PcInstance player, String params){
		_log.warning("entro");
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		if(parm1.equals("0")){
			return mainHtml(player,parm1,Integer.valueOf(parm2));
		}
		return "";
	}
}
