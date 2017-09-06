package ZeuS.procedimientos;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.handler.AdminCommandHandler;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.olympiad.Participant;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.GM.olymp;
import ZeuS.admin.button;
import ZeuS.admin.menu;
import ZeuS.interfase.bufferChar;
import ZeuS.interfase.captchaPLayer;
import ZeuS.interfase.central;
import ZeuS.interfase.desafio;
import ZeuS.interfase.donaManager;
import ZeuS.interfase.htmls;
import ZeuS.interfase.ipblock;
import ZeuS.interfase.shop;
import ZeuS.interfase.teleport;

public class adminHandler implements IAdminCommandHandler
{

	public static void registerHandler(){
		AdminCommandHandler.getInstance().registerHandler(new adminHandlerOpera());
	}

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	private static class adminHandlerOpera implements IAdminCommandHandler{

		private static boolean canUseCommandOlyPoint(L2PcInstance activeChar){
			int AccessLevel = Arrays.binarySearch(general.OLY_ACCESS_ID_MODIFICAR_POINT, activeChar.getAccessLevel().getLevel() );
			if(AccessLevel < 0){
				return false;
			}
			return true;
		}

		private static final String[] ADMIN_COMMANDS =
		{
			"admin_zeus_help",
			"admin_oly_ban",
			"admin_oly_unban",
			"admin_oly_reset_point",
			"admin_oly_point",
			"admin_oly_help",
			"admin_zeus_tele_help",
			"admin_zeus_tele_manual",
			"admin_zeus_tele_auto",
			"admin_zeus_tele_main_manual",
			"admin_zeus_tele_main_auto",
			"admin_zeus_tele",
			"admin_zeus_shop",
			"admin_z_c_c",
			"admin_zeus_op",
			"admin_zeus_ipblock",
			"admin_zeus_banip",
			"admin_zeus_config",
			"admin_zeus_botzone",
			"admin_zeus_recallAll",
			"admin_zeus_recallAllforce",
			"admin_zeus_buff_voice",
			"admin_zeus",
			"admin_zeus_bot_cancel",
			"admin_zeus_dona"
		};

		private static Vector<String> admExplicaCommand = new Vector<String>();

		private static void setExplicaCommand(){
			if(admExplicaCommand.size()==0){
				admExplicaCommand.add("//oly_ban: Ban player from Olys");
				admExplicaCommand.add("//oly_unban: Unban player from Olys");
				admExplicaCommand.add("//oly_reset_point: Reset the Player Olympiad Point to 0");
				admExplicaCommand.add("//oly_point: Give or remove Point to Target player. //oly_point -2 = Remove 2 point from target, //oly_point 2 = give 2 point to target");
				admExplicaCommand.add("//zeus_tele: Show the Teleport Config Menu");
				admExplicaCommand.add("//zeus_shop: Show the Shop Config Menu");
				admExplicaCommand.add("//zeus_ipblock: Show the IP Ban Config Windows");
				admExplicaCommand.add("//zeus_banip: IP Ban to the target Player");
				admExplicaCommand.add("//zeus_botzone: Send a boot check to all char in you zone");
				admExplicaCommand.add("//zeus_recallAll: Recall all char to you location whith radio. //zeus_recall 200");
				admExplicaCommand.add("//zeus_recallAllforce: Recall all char (force all to come) to you location whith radio and. //zeus_recall 200");
				admExplicaCommand.add("//zeus_buff_voice: Show buffer windows config");
				admExplicaCommand.add("//zeus_bot_cancel: Remove antibot windows from target name. //zeus_bot_cancel tester");
				admExplicaCommand.add("//zeus_dona: Donation Config section");
			}
		}

		@Override
		public boolean useAdminCommand(String command, L2PcInstance activeChar)
		{
			//captchaPLayer.cancelbotCheck(activeChar, params);
			setExplicaCommand();
			String strHtml = "";
			if(command.startsWith("admin_zeus_don") || command.equals("admin_zeus_dona")){
				donaManager.ByPass(activeChar, command);
				return true;
			}else if(command.startsWith("admin_zeus_bot_cancel")){
				if(command.split(" ").length>1){
					captchaPLayer.cancelbotCheck(activeChar, command.split(" ")[1]);
				}else{
					captchaPLayer.cancelbotCheck(activeChar, null);
				}
				return true;
			}else if(command.equals("admin_zeus")){
				//htmls.showWindowsKnowZeusAdmin(activeChar);
				String EnviarBypass = general.COMMUNITY_BOARD_ENGINE_PART_EXEC+";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0";
				cbManager.separateAndSend(Engine.delegar(activeChar, EnviarBypass),activeChar);				
				return true;
			}else if(command.startsWith("admin_zeus_buff_voice")){
				bufferChar.adminDelegar(activeChar,command);
			}else if(command.startsWith("admin_zeus_recallAll")){
				opera.summonAll(activeChar,command);
				return true;
			}else if(command.startsWith("admin_zeus_recallAllforce")){
				opera.summonAll(true, activeChar,command);
				return true;
			}else if(command.equals("admin_zeus_botzone")){
				captchaPLayer.sendCheckBootZone(activeChar);
				return true;
			}else if(command.equals("admin_zeus_config")){
				String html = menu.getConfigMenu(activeChar);
				if(html.length()>0){
					activeChar.sendPacket(new NpcHtmlMessage(0,html));
				}
			}else if(command.startsWith("admin_zeus_config")){
				String html = "";
				if(command.split(" ")[1].equals("statusCBTN")){
					if(!opera.isNumeric( command.split(" ")[2] )){
						central.msgbox("Error al leer parametro 1", activeChar);
						strHtml ="";
						return true;
					}
					button.status(activeChar, Integer.valueOf(command.split(" ")[2]));
					if(command.split(" ")[3].equals("0")){
						strHtml = button.getBtnAll(activeChar);
					}else if(command.split(" ")[3].equals("1")){
						strHtml = button.getBtnCH(activeChar);
					}
					else if(command.split(" ")[3].equals("2")){
						strHtml = button.getBtnCBE(activeChar);
					}
					activeChar.sendPacket(new NpcHtmlMessage(0,strHtml));
					return true;
				}else if(command.split(" ")[1].equals("GetHTMConfig") ){
					if(command.split(" ")[2].equals("1") ){
						strHtml = button.getBtnAll(activeChar);
					}else if(command.split(" ")[2].equals("2")){
						strHtml = button.getBtnCH(activeChar);
					}else if(command.split(" ")[2].equals("3")){
						strHtml = button.getBtnCBE(activeChar);
					}

					activeChar.sendPacket(new NpcHtmlMessage(0,strHtml));
					return true;
				}else if(command.endsWith("admin_zeus_config Config 2 0 0") ){
					String html2 = menu.getConfigMenu(activeChar);
					activeChar.sendPacket(new NpcHtmlMessage(0,html2));
					return true;
				}else if(command.split(" ")[1].equals("DESAFIO") ){
		   			strHtml = desafio.Desafio(activeChar, command.split(" ")[2], command.split(" ")[3], command.split(" ")[4]);
		   			activeChar.sendPacket(new NpcHtmlMessage(0,strHtml));
					return true;
				}else if(command.split(" ")[1].equals("DESAFIOADD")){
		   			if (command.split(" ")[2].equals("0")){
		   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,-1);
		   				return true;
		   			}
		   			if(!opera.isNumeric(command.split(" ")[3])){
		   				central.msgbox("Solo debes ingresar números en la ID del Item", activeChar);
		   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,Integer.parseInt(command.split(" ")[2]));
		   				return true;
		   			}
		   			if(!opera.isNumeric(command.split(" ")[4])){
		   				central.msgbox("Solo debes ingresar números en la Cantidad", activeChar);
		   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,Integer.parseInt(command.split(" ")[2]));
		   				return true;
		   			}
		   			desafio.AgregarPremios(activeChar, command.split(" ")[2], command.split(" ")[3], command.split(" ")[4]);
	   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,Integer.parseInt(command.split(" ")[2]));
	   				return true;
				}else if( (command.split(" ")[1].equals("indirizza") && command.split(" ")[2].equals("gestisci_buff")) ||
						command.split(" ")[1].equals("cambia_gruppo") || command.split(" ")[1].equals("edita_gruppi") ||
						command.split(" ")[1].equals("cambia_set") || command.split(" ")[1].equals("edita_lista_buff") ||
						command.split(" ")[1].equals("edita_buff") || command.split(" ")[1].equals("ordina_buff") ||
						command.split(" ")[1].equals("ordina_lista_buff") || command.split(" ")[1].equals("sposta_su") ||
						command.split(" ")[1].equals("sposta_giu")
						){
						String passDelegar = "";
						for(String parte : command.split(" ")){
							if(!parte.equalsIgnoreCase("admin_zeus_config")){
								passDelegar += parte + " ";
							}
						}
						opera.enviarHTML(activeChar, delegar.delegar(passDelegar.trim(), activeChar));
						return true;
				}else{
					int pri = 0;
					String newCommand = "";
					for (String coma  : command.split(" ")){
						if(pri==1){
							newCommand += coma + " ";
						}else{
							pri=1;
						}
					}

					html = menu.bypass(activeChar,newCommand.trim());
				}
				if(html.length()>0){
					activeChar.sendPacket(new NpcHtmlMessage(0,html));
				}
				return true;
			}else if(command.startsWith("admin_zeus_banip")){
				ipblock.bypass(command, activeChar);
			}else if(command.equals("admin_zeus_ipblock")){
				ipblock.sendhtmlBLockIP(activeChar, 0);
			}else if(command.startsWith("admin_zeus_op")){
				indelegar(command,activeChar);
			}else if(command.startsWith("admin_zeus_shop")){
				shop.ShopByPass(activeChar,command);
			}else if(command.equalsIgnoreCase("admin_zeus_tele")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.CentralHTML(activeChar,true);
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_main_manual")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleportMain(activeChar, "");
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_main_auto")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleportMain(activeChar, "",true);
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));

				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_manual")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleport(activeChar, "");
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_auto")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleport(activeChar, "",true);
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.startsWith("admin_zeus_tele_help")){
				String Concatenacion = "";
				for(String Comando: ADMIN_COMMANDS){
					if(Concatenacion.length()!=0){
						Concatenacion+=", ";
					}
					if(Comando.startsWith("admin_zeus_tele")){
						Concatenacion += Comando.replace("admin_", "//");
					}
				}
				central.msgbox(Concatenacion, activeChar);
			}else if (command.startsWith("admin_oly_ban")){
				StringTokenizer st = new StringTokenizer(command, " ");
				command = st.nextToken();

				if (st.hasMoreTokens())
				{
					L2PcInstance player = null;
					String playername = st.nextToken();
					try
					{
						player = L2World.getInstance().getPlayer(playername);
					}
					catch (Exception e)
					{
					}

					if (player != null)
					{
						olymp.banOlys(activeChar, player);
						return true;
					}
					activeChar.sendMessage("The player " + playername + " is not online");
					return false;
				}
				else if ((activeChar.getTarget() != null) && (activeChar.getTarget() instanceof L2PcInstance))
				{
					olymp.banOlys(activeChar, (L2PcInstance) activeChar.getTarget());
					return true;
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}
			}
			else if (command.startsWith("admin_oly_unban"))
			{
				StringTokenizer st = new StringTokenizer(command, " ");
				command = st.nextToken();

				if (st.hasMoreTokens())
				{
					L2PcInstance player = null;
					String playername = st.nextToken();
					try
					{
						player = L2World.getInstance().getPlayer(playername);
					}
					catch (Exception e)
					{
					}

					if (player != null)
					{
						olymp.unbanOlys(activeChar, player);
						return true;
					}
					activeChar.sendMessage("The player " + playername + " is not online");
					return false;
				}
				else if ((activeChar.getTarget() != null) && (activeChar.getTarget() instanceof L2PcInstance))
				{
					olymp.unbanOlys(activeChar, (L2PcInstance) activeChar.getTarget());
					return true;
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}
			}
			else if (command.startsWith("admin_oly_reset_point"))
			{
				if(!canUseCommandOlyPoint(activeChar)){
					central.msgbox("You do not have access to this command", activeChar);
					return false;
				}
				L2Object target = activeChar.getTarget();
				if(target instanceof L2PcInstance ){
					if(((L2PcInstance) target).isNoble()){
						L2PcInstance playerTarget = (L2PcInstance)target;
						Participant par = new Participant(playerTarget,1);
						olymp.point(par, true);
						central.msgbox_Lado("GM " + activeChar.getName() + " as reset your Olympics Points", playerTarget);
						central.msgbox("Points have been reset to " + playerTarget.getName() , activeChar);
					}else{
						activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
						return false;
					}
				}else{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}

			}
			else if (command.startsWith("admin_oly_point"))
			{
				if(!canUseCommandOlyPoint(activeChar)){
					central.msgbox("You do not have access to this command", activeChar);
					return false;
				}
				int pointGive = 0;
				StringTokenizer st = new StringTokenizer(command, " ");
				command = st.nextToken();
				String Puntos = "";
				if (st.hasMoreTokens()){
					Puntos = st.nextToken();
					if(!central.isNumeric(Puntos)){
						central.msgbox(msg.INGRESE_SOLO_NUMEROS, activeChar);
						return false;
					}
					pointGive = Integer.valueOf(Puntos);
				}else{
					central.msgbox("Need to include the points to give or take", activeChar);
					return false;
				}


				L2Object target = activeChar.getTarget();
				if(target instanceof L2PcInstance ){
					if(((L2PcInstance) target).isNoble()){
						L2PcInstance playerTarget = (L2PcInstance)target;
						Participant par = new Participant(playerTarget,1);
						olymp.point(par, pointGive);
						central.msgbox_Lado("GM " + activeChar.getName() + " has put "+ String.valueOf(pointGive) +" Olympiad points", playerTarget);
						central.msgbox("You put "+ String.valueOf(pointGive) + " Olmpiad points to " + playerTarget.getName(),activeChar);
					}else{
						activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
						return false;
					}
				}else{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}
			}else if(command.startsWith("admin_zeus_help")){
				for(String Comando: admExplicaCommand){
					central.msgbox(Comando, activeChar);
				}

			}
			return true;
		}
		@Override
		public String[] getAdminCommandList()
		{
			return ADMIN_COMMANDS;
		}
	}

	protected static void indelegar(String Comando, L2PcInstance player){
		if(Comando.startsWith("admin_zeus_op")){
			String[] Params = Comando.split(" ");
			if(Params.length>1){
				if(Params[1].equals("ipblock")){
					ipblock.bypass(Comando, player);
				}else if(Params[1].equals("teleaddmain1")){
					String HTML = teleport.adminTeleportMain(player, "",true);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("teleaddmain2")){
					String HTML = teleport.adminTeleportMain(player, "");
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("teleadd1")){
					String HTML = teleport.adminTeleport(player, "",true);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("teleadd2")){
					String HTML = teleport.adminTeleport(player, "");
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("telesection1")){
					String HTML = teleport.adminTeleport(player,"",true,false,Params[2]);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("telesection2")){
					String HTML = teleport.adminTeleport(player,"",false,false,Params[2]);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("tele_update")){
					teleport.bypassMain_edit(Comando, player);
				}else if(Params[1].endsWith("coliseevent")){
					teamEvent.bypass(Comando, player);
				}else if(Params[1].endsWith("buffvoice")){
					bufferChar.adminDelegar(player, Comando);
				}





				/*
				 * 			}else if(command.equalsIgnoreCase("admin_zeus_tele_manual")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleport(activeChar, "");
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_auto")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleport(activeChar, "",true);
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}*/

			}
		}
	}
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String[] getAdminCommandList() {
		// TODO Auto-generated method stub
		return null;
	}

}