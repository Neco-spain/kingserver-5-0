package ZeuS.procedimientos;

import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.instancemanager.SiegeManager;
import com.l2jserver.gameserver.instancemanager.TownManager;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SiegeInfo;
import com.l2jserver.gameserver.network.serverpackets.WareHouseDepositList;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.admin.button;
import ZeuS.admin.menu;
import ZeuS.interfase.GrandBossInfo;
import ZeuS.interfase.PlayerConfigPanel;
import ZeuS.interfase.RaidBossInfo;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.buffer;
import ZeuS.interfase.buffer_zeus;
import ZeuS.interfase.bugreport;
import ZeuS.interfase.cambionombre;
import ZeuS.interfase.central;
import ZeuS.interfase.clan;
import ZeuS.interfase.classmaster;
import ZeuS.interfase.colorManager;
import ZeuS.interfase.delevel;
import ZeuS.interfase.desafio;
import ZeuS.interfase.desafioBusqueda;
import ZeuS.interfase.donaManager;
import ZeuS.interfase.dropSearch;
import ZeuS.interfase.flagFinder;
import ZeuS.interfase.htmls;
import ZeuS.interfase.logpvppk;
import ZeuS.interfase.partyFinder;
import ZeuS.interfase.pinCode;
import ZeuS.interfase.pvppk;
import ZeuS.interfase.shop;
import ZeuS.interfase.special_augment;
import ZeuS.interfase.special_elemental;
import ZeuS.interfase.special_enchant;
import ZeuS.interfase.subclass;
import ZeuS.interfase.symbol;
import ZeuS.interfase.teleport;
import ZeuS.interfase.transform;
import ZeuS.interfase.votereward;

public class delegar{

	private static final Logger _log = Logger.getLogger(delegar.class.getName());

	public static String delegar_desafio(String event, L2PcInstance player){
		if(!general._activated()){
			return "";
		}
		if(event.equals("getPremio")){
			L2Object obj = player.getTarget();
			if (obj == null)
			{
				return "";
			}
			if (obj instanceof L2Npc)
			{
				if(desafioBusqueda.getPremios(player, opera.getIDNPCTarget(player) ) ){
					return desafioBusqueda.premiosOK();
				}
			}
			return "";
		}

		return "";
	}

	public static String delegar(String event, L2PcInstance player){
		if (!general._activated()){
			return "";
		}
		
		if(!Bloqueos(event,player)){
			player.sendMessage("Ingrese los datos Solicitados. Acción Cancelada");
			opera.enviarHTML(player,central.ErrorTipeoEspacio());
			return "";
		}

		String strHtml = Subproces(event,player);
		return strHtml;

	}

	private static String Concatenar(String[] strEvent, int Inicio){
		int contador = 0;
		String strConcatenador ="";
		for(String Eventos : strEvent){
			if(contador >Inicio) {
				strConcatenador += " " + Eventos;
			}
			contador++;
		}
		return strConcatenador;
	}

	private static boolean Bloqueos(String event, L2PcInstance player){
		boolean retorno = true;

		event = event.replace(","," ");
		String Prueba1 = event.replace(" ",":");
		String[] Buscar = Prueba1.split(":");

		if(general.DEBUG_CONSOLA_ENTRADAS) {
			_log.warning("::DEBUGING NPC AIO -> " + Prueba1 + " ->Largo=" + String.valueOf(Buscar.length));
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER && player.isGM()) {
			player.sendMessage("::DEBUGING NPC AIO -> " + Prueba1 + " ->Largo=" + String.valueOf(Buscar.length));
		}

		if(Buscar[0].equals("setConfig1")){
			return true;
		}

		if (Buscar[0].equals("bugReportIN") || Buscar[0].equals("MENUDONA_VARIOS")){
			if (Buscar.length <= 3){
				return false;
			}
		}
		if (Buscar[0].equals("crea")){
			if (Buscar.length <= 4){
				return false;
			}
		}

		if (Buscar[0].equals("DropSearch")){
			if ((Buscar.length >=5) || (Buscar.length<=2)){
				return false;
			}
		}
		if(Buscar[0].equals("DESAFIOADD")){
			if ((Buscar.length <=2) || (Buscar.length > 4)){
				return false;
			}
		}
		if (Buscar[0].equals("logpeleas") || Buscar[0].equals("CHANGEPIN")){
			if ((Buscar.length <= 3) || (Buscar.length >4)){
				return false;
			}
		}
		if(Buscar[0].equals("ENVIAR_NOTIFICACION")){
			if(Buscar.length < 4){
				return false;
			}
		}




		return retorno;
	}


	private static String Subproces(String event, L2PcInstance player){
		if(!general._activated()){
			return "";
		}
		
		if(player.isInOlympiadMode()){
			return "";
		}
		if(player.isOlympiadStart()){
			return "";
		}

		String[] eventSplit;
		String evento = null;
		String eventParam1;
		String eventParam2;
		String eventParam3;

		int currentTime = (int) (System.currentTimeMillis()/1000);

		try{
			eventSplit = event.split(" ");
			evento = eventSplit[0];
			eventParam1 = eventSplit[1];
			eventParam2 = eventSplit[2];
			eventParam3 = eventSplit[3];
		}catch(Exception a){
			return htmls.ErrorTipeoEspacio();
		}

		String[] datiAzione;

		int idSchema;
		int idSkill;
		int livello;
		int livelloSkill;



		int ANNOUCSTR =0;
		int EFECTPVP = 0;
		int SHOWSHIFT = 0;
		int SHOWPINWINDOWS =0;
		int SHOWHERO=0;
		int EXPSP=0;
		int TRADE=0;
		int BADBUFF=0;
		int HIDESTORE=0;
		int REFUSAL=0;
		int PARTYMATCHING=0;


		String strHtml = "";

		switch(evento){
			case "ShopDona":
				donaManager.delegar(player,eventParam1,eventParam2,eventParam3);
				break;
			case "getRegOlyEven":
				teamEvent.RegisterOnTeam(player);
				break;
			case "shopBD":
				if(eventParam1.equals("0")){
					strHtml = shop.getShopCentral(player);
				}else if(eventParam1.equals("1")){
					strHtml = shop.getShopSeccion(player, eventParam2);
				}
				break;
			case "transform":
				if(eventParam1.equals("0")){ //Main Meni
					strHtml = transform.mainTransform(player,Integer.valueOf(eventParam2));
					break;
				}else if(eventParam1.equals("1")){
					if(transform.setTransformar(player, Integer.valueOf(eventParam2),1)){
						opera.removeItem(general.TRANSFORMATION_ESPECIALES_PRICE, player);
						central.msgbox("Felicidades, ya estás transformado", player);
						break;
					}
				}else if(eventParam1.equals("2")){
					if(transform.setTransformar(player, Integer.valueOf(eventParam2),2)){
						opera.removeItem(general.TRANSFORMATION_RAIDBOSS_PRICE, player);
						central.msgbox("Felicidades, ya estás transformado", player);
						break;
					}
				}else if(eventParam1.equals("3")){
					if(transform.setTransformar(player, Integer.valueOf(eventParam2),3)){
						opera.removeItem(general.TRANSFORMATION_PRICE, player);
						central.msgbox("Felicidades, ya estás transformado", player);
						break;
					}
				}else if(eventParam1.equals("4")){
					strHtml = transform.mainTransform(player,-1);
					break;
				}
				strHtml = transform.mainTransform(player,0);
				break;

			case "teleportMain":
				strHtml = teleport.CentralHTML(player);
				break;
			case "teleportshow":
				strHtml = teleport.getSegmento(player, Integer.valueOf(eventParam1), eventParam2);
				break;
			case "howiam":
				strHtml = htmls.getInfoContacto();
				break;
			case "setConfig1":
				strHtml = menu.bypass(player, event);
				break;
			case "GetHTMConfig":
				if(!opera.isNumeric(eventParam1)){
					central.msgbox("Error al leer paramtero 1 en Configuracion.", player);
				}
				if(eventParam1.equals("1")){
					strHtml = button.getBtnAll(player);
					break;
				}else if(eventParam1.equals("2")){
					strHtml = button.getBtnCH(player);
					break;
				}else if(eventParam1.equals("3")){
					strHtml = button.getBtnCBE(player);
				}
				break;
			case "status":
				if(!opera.isNumeric(eventParam1)){
					central.msgbox("Error al leer parametro 1", player);
					strHtml ="";
					break;
				}
				button.status(player, Integer.valueOf(eventParam1));
				if(eventParam2.equals("0")){
					strHtml = button.getBtnAll(player);
					break;
				}
				if(eventParam2.endsWith("1")){
					strHtml = button.getBtnCH(player);
				}
				break;
			case "Config":
				if(eventParam1.equals("1")){
					general.loadConfigs();
					central.msgbox("Config Recargada", player);
				}
				if(eventParam1.equals("2")){
					strHtml = menu.getConfigMenu(player);
					break;
				}
				strHtml = htmls.firtsHTML(player, general.npcGlobal(player));
				break;
			case "maincentral":
				strHtml = htmls.firtsHTML(player, general.npcGlobal(player));
				break;
			case "RaidBossInf":
				strHtml = RaidBossInfo.getInfo(player, Integer.valueOf(eventParam1));
				break;
			case "RaidBossInGo":
				if(RaidBossInfo.goRaid(player, eventParam1, eventParam2, eventParam3)){
					opera.removeItem(general.RAIDBOSS_INFO_TELEPORT_PRICE, player);
					central.msgbox("Buena Suerte con tu raid!", player);
					strHtml ="";
				}else{
					String PaginaQuedo ="0";
					try{
						PaginaQuedo = eventSplit[4];
					}catch(Exception a){
						PaginaQuedo = "0";
					}
					strHtml = RaidBossInfo.getInfo(player, Integer.valueOf(PaginaQuedo));
				}
				break;
			case "showGrandBoss":
				strHtml = GrandBossInfo.getInfo();
				break;
			case "reloadscript":
				if(general.IS_USING_NPC.get(player.getObjectId()) && !general.IS_USING_CB.get(player.getObjectId())){
					strHtml = htmls.firtsHTML(player, general.npcGlobal(player));
				}else{
					strHtml = "";
				}
				break;
			case "NOTIFICA_DONACION":
				strHtml = donaManager.MainHtmlNotificacionDonacion(player);
				break;
			case "ENVIAR_NOTIFICACION":
				strHtml = donaManager.EnviarNotificacionBD(player,eventParam1,eventParam2,Concatenar(eventSplit,2));
				break;
			case "indirizza":
				if (eventParam1.equals("gestisci_buff")){
					strHtml = buffer.costruisciSezioneAmministrativa(eventParam2);
					break;
				}
				if(eventParam1.equals("principale")){
					//strHtml = buffer.rebuildMainHtml(player, eventParam2);
					strHtml = buffer_zeus.getMainWindowsBuffer(player);
					break;
				}
				String[] splitEvento = eventParam1.split("_");
				String tipoEvento = splitEvento[0];
				if(tipoEvento.equals("vedi")){
					String tipoBuff = splitEvento[1];
					if(tipoBuff.equals("speciali")) {
						tipoBuff = "special";
					}
					if(tipoBuff.equals("altri")) {
						tipoBuff = "others";
					}
					String pagina ="";
					if(opera.isNumeric(eventParam2)){
						pagina =  eventParam2;
					}else{
						String[] splitParametro = eventParam2.split("_");
						pagina = splitParametro[1];
					}
					strHtml = buffer.costruisciHtml(tipoBuff,Integer.parseInt(pagina), eventParam3);
					break;
				}
				strHtml = "";
				break;
			case "ricarica_script":
				break;
			case "edita_lista_buff":
				String eventParam4 = eventSplit[4];
				int pagina = 0;
				String[] splitParametro =null;
				if (opera.isNumeric(eventParam3)) {
				} else{
					splitParametro = eventParam3.split("_");
					pagina = Integer.valueOf(splitParametro[1]);
				}

				strHtml = buffer.vediListaBuff(eventParam1, eventParam2, pagina, eventParam4);
				break;
			case "edita_buff":
					eventParam4 = eventSplit[4];
					strHtml = buffer.editaBuff(eventParam1, eventParam2, eventParam3, eventParam4);
					buffer_zeus.loadBuff();
					break;
			case "cambia_set":
				eventParam2 = eventParam2.replace("None","0");
				eventParam2 = eventParam2.replace("Mage","1");
				eventParam2 = eventParam2.replace("Fighter","2");
				eventParam2 = eventParam2.replace("Shield","3");
				eventParam2 = eventParam2.replace("All","4");
				eventParam4 = eventSplit[4];
				try {
					strHtml = buffer.aggiornaSet(eventParam1, eventParam2, Integer.valueOf(eventParam3), eventParam4);
					break;
				} catch (NumberFormatException e) {

				}
				buffer_zeus.loadBuff();
				break;
			case "pet_buff":
				if(currentTime > Integer.valueOf(general.BLOQUEO_ACCION.get(player))){
					general.havePetSum.put(player, Integer.valueOf(eventParam1));
					if(general.TIME_OUT) {
						buffer.barraTempo(player, 3, general.BUFFER_TIME_WAIT / 2, 600);
					}
				}
				strHtml = buffer.rebuildMainHtml(player, eventParam2);
				break;
			case "assegna":
				strHtml = buffer.Assegna(player, eventParam1, eventParam2, eventParam3);
				break;
			case "assegna_buff":
				eventParam4 = eventSplit[4];
				strHtml = buffer.Assegna_Buff(player, eventParam1, eventParam2, eventParam3, eventParam4);
				break;
			case "assegna_pacchetto_buff":
				strHtml = buffer.Assegna_Pacchetto_Buff(player, eventParam1);
				break;
			case "pieno":
				strHtml = buffer.Peino(player, eventParam1);
				break;
			case "rimuovi_buff":
				strHtml = buffer.Rimouvi_Buff(player, eventParam1);
				break;
			case "crea_1":
				strHtml =  buffer.creaSchema(eventParam1);
				break;
			case "crea":
					eventParam1 = eventParam1.replace("Me","0");
					eventParam1 = eventParam1.replace("Pet","1");
			 		eventParam4 = eventSplit[4];
					if(eventParam4.length()==0){
						strHtml = buffer.mostraTesto(player, "INFO:", "Please, enter the scheme name!", true, "Return", "principale", eventParam2);
					}else{
						strHtml = buffer.registraSchema(player, Integer.valueOf(eventParam1), eventParam4, eventParam2);
					}
				break;
			case "selezione_schemi":
				strHtml = buffer.ottieniListaOpzioni(Integer.valueOf(eventParam1), eventParam2);
				break;
			case "edita_schemi":
				eventParam4 = eventSplit[4];
				eventParam2 = eventParam2.replace("Improveds","0");
				eventParam2 = eventParam2.replace("Buffs","1");
				eventParam2 = eventParam2.replace("Buffs2","1+");
				eventParam2 = eventParam2.replace("Resists","2");
				eventParam2 = eventParam2.replace("Prophecies","3");
				eventParam2 = eventParam2.replace("Chants","4");
				eventParam2 = eventParam2.replace("Dances","6");
				eventParam2 = eventParam2.replace("Songs","7");
				eventParam2 = eventParam2.replace("Specials","8");
				eventParam2 = eventParam2.replace("Others","9");
				strHtml = buffer.visualizzaBuffSchema(Integer.valueOf(eventParam1), eventParam2, eventParam3, eventParam4);
				break;
			case "aggiungi_buff_schema":
				eventParam4 = eventSplit[4];
				datiAzione = eventParam1.split("_");
				idSchema = Integer.valueOf(datiAzione[0]);
				idSkill = Integer.valueOf(datiAzione[1]);
				livello = Integer.valueOf(datiAzione[2]);
				strHtml = buffer.aggiungiBuffSchema(idSchema, idSkill, livello, eventParam2, Integer.valueOf(eventParam3), eventParam4);
				break;
			case "rimuovi_buff_schema":
				datiAzione = eventParam1.split("_");
				idSchema = Integer.valueOf(datiAzione[0]);
				idSkill = Integer.valueOf(datiAzione[1]);
				livello = Integer.valueOf(datiAzione[2]);
				eventParam4 = eventSplit[4];
				strHtml = buffer.rimuoviBuffSchema(idSchema, idSkill, livello, eventParam2, Integer.valueOf(eventParam3), eventParam4);
				break;
			case "edita_schema":
				strHtml = buffer.editaSchema(player, eventParam1);
				break;
			case "cancella_schema":
				strHtml = buffer.cancellaSchema(player, eventParam1);
				break;
			case "conferma_cancella":
				strHtml = buffer.confermaCancella(Integer.valueOf(eventParam1), eventParam2, eventParam3);
				break;
			case "cancella_definitivo":
				strHtml = buffer.cancellaDefinitivo(player, Integer.valueOf(eventParam1), eventParam2);
				break;
			case "edita_gruppi":
				if(opera.isNumeric(eventParam1)){
					pagina = Integer.valueOf(eventParam1);
				}else{
					splitParametro = eventParam1.split("_");
					pagina = Integer.valueOf(splitParametro[1]);
				}
				strHtml = buffer.editaGruppi(pagina, eventParam2);
				break;
			case "ordina_buff":
				strHtml = buffer.ordinaBuff(eventParam1);
				break;
			case "cambia_gruppo":
				eventParam1 = eventParam1.replace("_"," ");
				String[] datiSkill = eventParam1.split(" ");
				idSkill = Integer.valueOf(datiSkill[0]);
				livelloSkill = Integer.valueOf(datiSkill[1]);
				eventParam4 = eventSplit[4];
				int classe=0;
				String tipo="";
				if(eventParam2.equals("Improved")){
					classe = 0; tipo = "improved";
				}else if (eventParam2.equals("Buff")){
					classe = 1;
					tipo = "buff";
				}else if (eventParam2.equals("Resist")){
					classe = 2;
					tipo = "resist";
				}else if (eventParam2.equals("Prophecy")){
					classe = 3;
					tipo = "prophecy";
				}
				else if (eventParam2.equals("Chant")){
					classe = 4;
					tipo = "chant";
				}else if (eventParam2.equals("Cubic")){
					classe = 5;
					tipo = "cubic";
				}else if (eventParam2.equals("Dance")){
					classe = 6;
					tipo = "dance";
				}else if (eventParam2.equals("Song")){
					classe = 7;
					tipo = "song";
				}else if (eventParam2.equals("Special")){
					classe = 8;
					tipo = "special";
				}else if (eventParam2.equals("Others")){
					classe = 9;
					tipo = "others";
				}
				if(opera.isNumeric(eventParam3)) {
					pagina = Integer.valueOf(eventParam3);
				} else{
					splitParametro = eventParam3.split("_");
					pagina = Integer.valueOf(splitParametro[1]);
				}
				strHtml = buffer.cambiaGruppo(player, idSkill, livelloSkill, classe, tipo, pagina, eventParam4);
				buffer_zeus.loadBuff();
				break;
			case "ordina_lista_buff":
				eventParam4 = eventSplit[4];
				if(opera.isNumeric(eventParam3)) {
					pagina = Integer.valueOf(eventParam3);
				} else{
					splitParametro = eventParam3.split("_");
					pagina = Integer.valueOf(splitParametro[1]);
				}
				strHtml = buffer.ordinaListaBuff(eventParam1, eventParam2, pagina, eventParam4);
				break;
			case "sposta_su":
				eventParam4 = eventSplit[4];
				String[] splitSkill = eventParam1.split("_");
				idSkill = Integer.valueOf(splitSkill[0]);
				livelloSkill = Integer.valueOf(splitSkill[1]);
				strHtml = buffer.spostaOrdine(idSkill, livelloSkill, eventParam2, "su", eventParam3, eventParam4);
				buffer_zeus.loadBuff();
				break;
			case "sposta_giu":
				eventParam4 = eventSplit[4];
				splitSkill = eventParam1.split("_");
				idSkill = Integer.valueOf(splitSkill[0]);
				livelloSkill = Integer.valueOf(splitSkill[1]);
				strHtml = buffer.spostaOrdine(idSkill, livelloSkill, eventParam2, "giu", eventParam3, eventParam4);
				buffer_zeus.loadBuff();
				break;
			case "MENUBUFREPORT":
				strHtml = bugreport.MainMenuBugReport(player);
				break;
			case "bugReportIN":
				strHtml = bugreport.IngresoBugReport(eventParam1,Concatenar(eventSplit,1),player);
				break;
			case "gototeleportBD":
				if(!teleport.goTeleportplayer(Integer.valueOf(eventParam1),player)) {
					//strHtml = teleport.CentralHTML(player)
					strHtml = teleport.getSegmento(player, Integer.valueOf(eventParam2), eventParam3);
				}else{
					strHtml ="";
				}
				break;
			case "gototeleport":
				if(SiegeManager.getInstance().getSiege(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3)) != null){
					player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
					strHtml = "";
					break;
				}
				else if(TownManager.townHasCastleInSiege(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2)) && player.isInsideZone(ZoneId.TOWN)){
					player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
					strHtml = "";
					break;
				}else if (!Config.ALT_GAME_KARMA_PLAYER_CAN_USE_GK && (player.getKarma() > 0)){
					player.sendMessage("Go away, you're not welcome here.");
					strHtml = "";
					break;
				}else if(player.isAlikeDead()){
					strHtml = "";
					break;
				}

				String retorno ="";
				if(!general.FREE_TELEPORT){
					if(opera.haveItem(player, general.TELEPORT_PRICE)){
						opera.removeItem(general.TELEPORT_PRICE, player);
						player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);
						player.sendMessage("Has sido teletransportado a " + eventParam1 +" "+eventParam2+ " "+eventParam3);
						break;
					}
				}else{
					player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);
					player.sendMessage("Has sido teletransportado a " + eventParam1 +" "+eventParam2+ " "+eventParam3);
					break;
				}
				break;
			case "TranferMenu":
				strHtml = classmaster.classmasterShow(player);
				break;
			case "zeus_buffer_npc":
				strHtml = buffer_zeus.Delegar(player, event);
				break;
			case "deluxbuffer":
				strHtml = buffer_zeus.getMainWindowsBuffer(player);
				//strHtml = buffer.DeluxBuffer(player);
				break;
			case "redirect":
				if(eventParam1.equals("main")){
					strHtml = buffer.rebuildMainHtml(player,"");
					break;
				}
				strHtml = "";
				break;
			case "buffpet":
				buffer.BuffPet(player, currentTime, eventParam1);
				strHtml = buffer.rebuildMainHtml(player,"");
				break;
			case "create":
				strHtml = buffer.Create(player, eventParam1);
				break;
			case "delete":
				strHtml = buffer.Delete(player, eventParam1);
				break;
			case "heal" :
				if(currentTime > general.blockUntilTime.get(player)){
				//if(currentTime > player.blockUntilTime){
					if(opera.haveItem(player, general.BUFFER_ID_ITEM, general.BUFFER_HEAL_VALOR)){
						retorno = "<html><title>"+general.TITULO_NPC()+"</title><body>";
						retorno += central.LineaDivisora(2) + central.headFormat("Teleport") + central.LineaDivisora(2);
						retorno += central.LineaDivisora(2) + central.headFormat("No tienes suficiente adena para usar mis<br1>servicios","WHITE") + central.LineaDivisora(2);
						retorno += "</body></html>";
						strHtml = retorno;
						break;
					}else{
						central.healAll(player, false);
						opera.removeItem(general.BUFFER_ID_ITEM, general.BUFFER_HEAL_VALOR, player);
					}
				}
				strHtml = buffer.rebuildMainHtml(player,"");
				break;
	   		case "chat1":
	   			strHtml = htmls.MainHtml1(player);
				break;
	   		case "chat2":
	   			strHtml = htmls.MainHtml2(player);
				break;
	   		case "chat3":
	   			strHtml = htmls.MainHtml3(player);
				break;
	   		case "chat4":
	   			strHtml = htmls.MainHtml4(player);
				break;
	   		case "chat6":
	   			strHtml = htmls.MainHtml6(player);
				break;
			//case "chat7" :strHtml = htmls.MainHtmlShop();
	   		case "class_master":
   				strHtml = classmaster.classmasterShow(player);
				break;
	   		case "subclass":
				if(currentTime > general.blockUntilTime.get(player)){
					strHtml = subclass.subclassopcions(player, eventParam1, Integer.valueOf(eventParam2), Integer.valueOf(eventParam3));
					break;
				}
				strHtml = htmls.MainHtml3(player);
				break;
	   		case "symbol" :
				strHtml = symbol.optionsymbol(player, eventParam1,Integer.valueOf(eventParam2));
				break;
	   		case "Pintar":
	   			if(opera.haveItem(player, general.PINTAR_PRICE)){
	   				if(colorManager.ColorName(player, eventParam1)){
	   					opera.removeItem(general.PINTAR_PRICE, player);
	   				}
	   			}
   				strHtml = "";
   				break;
	   		case "ptfinder":
	   			strHtml = partyFinder.MainHtmlParty(player);
				break;
	   		case "AUGMENTMNU":
	   			strHtml = htmls.MainMenuAug(eventParam1);
				break;
	   		case "fgfinder":
	   			strHtml = flagFinder.MainHtmlFlagFinder();
				break;
	   		case "Colormenuu":
	   			strHtml = colorManager.ColorMenu(player);
				break;
	   		case "DesafioVerDetaFami":
	   				//Llama a la nueva Forma de Ver las Familias con los premios indicandole el ID de la Familia a ver en param2
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
	   				break;
	   		case "DESAFIOADD":
	   			if (eventParam1.equals("0")){
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,-1);
					break;
	   			}
	   			if(!opera.isNumeric(eventParam2)){
	   				central.msgbox("Solo debes ingresar números en la ID del Item", player);
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
					break;
	   			}
	   			if(!opera.isNumeric(eventParam3)){
	   				central.msgbox("Solo debes ingresar números en la Cantidad", player);
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
					break;
	   			}
	   			desafio.AgregarPremios(player, eventParam1, eventParam2, eventParam3);
   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
				break;
	   		case "DESAFIO":
	   			strHtml = desafio.Desafio(player, eventParam1, eventParam2, eventParam3);
				break;
	   		case "SETBufferDONA":
	   			if(eventParam1.equals("0")){
	   				if(player.getLevel() < 85){
	   					central.msgbox("Debe tener un level igual a 85", player);
	   					strHtml = "";
	   					break;
	   				}
	   				if(player.getName().startsWith("[BUFF]")){
	   					central.msgbox("Tu ya eres un AIO", player);
	   					strHtml = "";
	   					break;
	   				}
	   				
	   				int getDCRequiere = 0;
	   				
	   				try{
	   					getDCRequiere = donaManager.DC_Solicita(player);
	   				}catch(Exception a){
	   					getDCRequiere = -1;
	   					if(!eventParam3.equals("0")){
	   						if(opera.isNumeric(eventParam3)){
	   							getDCRequiere = Integer.valueOf(eventParam3);
	   						}
	   					}
	   				}	   				
	   				
	   				
	   				
	   				if(getDCRequiere > 0){
		   				if(!opera.haveDonationItem(player,getDCRequiere)){
		   					central.msgbox("No tienes los Item necesarios para crear un AIO Buffer", player);
		   					strHtml = "";
		   					break;
		   				}
	   				}
	   				
	   				if(getDCRequiere >0){
		   				opera.removeItem(general.DONA_ID_ITEM, getDCRequiere, player);
		   				if(aioChar.setNewAIO(player,true)){
		   					strHtml = "";
		   				}
	   				}else{
	   					central.msgbox("Error, please contact to any GM", player);
	   				}
	   				//strHtml = aioChar.setNewAIO(player);
					break;
	   			}
	   			if(eventParam1.equals("1")){
	   				strHtml = aioChar.explicaAIO(player);
					break;
	   			}
	   		case "AUGMENTSP":
	   			if(eventParam1.equals("0")){
	   				if(!eventParam2.equals("-1")){
	   					strHtml = special_augment.mainHtmlAugment_Special(player, eventParam2, Integer.valueOf(eventParam3));
	   					break;
	   				}else{
		   				strHtml = special_augment.getMainHtml(player);
		   				break;
	   				}
	   			}


	   			if(eventParam1.equals("1")){
					String[] Seccionado = eventParam2.split("_");
					strHtml = special_augment.mainHTMLAumentoDetalleSkill(player,Integer.valueOf(eventParam3),Seccionado[0],Integer.valueOf(Seccionado[1]));
					break;
	   			}
	   			if(eventParam1.equals("3")){
	   				if(special_augment.CheckToRemoveItemAugment(player, eventParam3, false)){
		   				if(general.AUGMENT_SPECIAL_NOBLE && !player.isNoble()){
		   					central.msgbox("Para usar mis Servicios debes ser Noble.", player);
		   					strHtml = "";
		   					break;
		   				}
		   				if(general.AUGMENT_SPECIAL_LVL > player.getLevel()){
		   					central.msgbox("Para usar mis Servicios debes tener un level superior o igual a " + String.valueOf(general.AUGMENT_SPECIAL_LVL), player);
		   					strHtml = "";
		   					break;
		   				}
		   				if (!special_augment.setAugment(player, eventParam2)){
		   					central.msgbox("Ha ocurrido un problema mientras se aumentaba el arma.", player);
		   					strHtml = "";
		   					break;
		   				}else{
		   					special_augment.CheckToRemoveItemAugment(player, eventParam3, true);
		   					strHtml = "";
		   					break;
		   				}
	   				}
	   			}
	   			break;
	   		case "ENCHANTITEM":
	   			if(eventParam1.equals("0")){
	   				strHtml = special_enchant.mainHtmlEnchantItem(player);
					break;
	   			}
	   			if(eventParam1.equals("1")){
	   				if(!opera.haveItem(player, general.ENCHANT_ITEM_PRICE)){
	   					strHtml = special_enchant.mainHtmlEnchantItem(player);
	   					break;
	   				}
	   				if(special_enchant.AplicarEnchantItem(player, eventParam2, eventParam3)){
	   					opera.removeItem(general.ENCHANT_ITEM_PRICE, player);
	   					break;
	   				}
	   				strHtml = special_enchant.mainHtmlEnchantItem(player);
					break;
	   			}
	   			break;
	   		case "ELEMENTAL":
	   			if(eventParam1.equals("0")){
	   				strHtml = special_elemental.mainHtmlElemental(player,"");
					break;
	   			}
	   			if(eventParam1.equals("1")){
	   				if(!opera.haveItem(player, general.ELEMENTAL_ITEM_PRICE)){
	   					strHtml = special_elemental.mainHtmlElemental(player,eventParam2);
	   					break;
	   				}
	   				if(special_elemental.AplicarElemental(player,eventParam2,eventParam3)){
	   					opera.removeItem(general.ELEMENTAL_ITEM_PRICE, player);
	   				}
	   				strHtml = special_elemental.mainHtmlElemental(player,eventParam2);
					break;
	   			}
				break;
	   		case "OPCIONESVAR":
	   			if(eventParam1.equals("0")){
	   				strHtml = htmls.MainHtmlOpcionesVarias(player);
					break;
	   			}
	   			if(eventParam1.equals("1")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_SEXO_ITEM_PRICE)){
	   					opera.changeSex(player);
	   					opera.removeItem(general.OPCIONES_CHAR_SEXO_ITEM_PRICE, player);
	   				}
	   			}
	   			if(eventParam1.equals("2")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_NOBLE_ITEM_PRICE)){
	   					if(opera.setNoble(player)){
	   						central.msgbox("Usted ahora es Noble", player);
	   						opera.removeItem(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE, player);
	   					}
	   				}
	   			}
	   			if(eventParam1.equals("3")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_LVL85_ITEM_PRICE)){
	   					if(opera.set85(player)){
	   						central.msgbox("Usted tiene ahora lvl 85", player);
	   						opera.removeItem(general.OPCIONES_CHAR_LVL85_ITEM_PRICE, player);
	   					}
	   				}
	   			}

	   			if(eventParam1.equals("4")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_BUFFER_AIO_PRICE)){
	   					if(player.getLevel() < general.OPCIONES_CHAR_BUFFER_AIO_LVL){
	   						central.msgbox("Lo sentimos, debes tener un level superior o igual a " + String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL) + " para ésta operación", player);
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}
	   					if(aioChar.setNewAIO(player,true)){
	   						opera.removeItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE, player);
	   						strHtml = "";
	   						break;
	   					}else{
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						central.msgbox("No es posible crear AIO Buffer.", player);
	   						break;
	   					}
	   				}
	   			}

	   			if(eventParam1.equals("5")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_FAME_PRICE)){
	   					if(general.OPCIONES_CHAR_FAME_NOBLE && !player.isNoble()){
	   						central.msgbox(msg.NECESITAS_SER_NOBLE, player);
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}

	   					if(general.OPCIONES_CHAR_FAME_LVL > player.getLevel()){
   							String Mensaje = msg.NECESITAS_TENER_UN_LEVEL_MAYOR_A_$level.replace("$level", String.valueOf(general.OPCIONES_CHAR_FAME_LVL));
   							central.msgbox(Mensaje, player);
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}

	   					if(!opera.haveItem(player, general.OPCIONES_CHAR_FAME_PRICE)){
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}

						player.setFame(player.getFame() + general.OPCIONES_CHAR_FAME_GIVE);
						opera.removeItem(general.OPCIONES_CHAR_FAME_PRICE, player);
						player.broadcastUserInfo();

	   				}
	   			}

	   			strHtml = htmls.MainHtmlOpcionesVarias(player);
				break;
	   		case "USER_CHANGE_NAME":
	   			if(eventParam1.equals("1")){
		   			if (eventParam2.equals("0")) {
						strHtml = htmls.Menu_CHANGE_NAME_PJ(player, "USER_CHANGE_NAME");
					}
		   			if (eventParam2.equals("1")){
		   				if(cambionombre.CambiarNombrePJ_libre(player)){
		   					if(opera.haveItem(player, general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE)){
		   						if(!general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE || ( general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE && player.isNoble() )){
		   							if(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL <= player.getLevel()){
			   							if(cambionombre.Procedimiento_CambiarNombre_Char(player, eventParam3)){
				   							opera.removeItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE, player);
				   							strHtml = "";
				   							break;
			   							}
		   							}else{
		   								central.msgbox(msg.NECESITAS_SER_LVL_$level_PARA_ESTA_OPERACION.replace("$level", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL)), player);
		   							}
		   						}else{
		   							central.msgbox(msg.NECESITAS_SER_NOBLE, player);
		   						}
		   					}
		   				}
		   				strHtml = htmls.Menu_CHANGE_NAME_PJ(player, "USER_CHANGE_NAME");
						break;
		   			}
	   			}
	   			if(eventParam1.equals("2")){
	   				if(eventParam2.equals("0")){
	   					strHtml = htmls.Menu_CHANGE_NAME_CLAN(player,"USER_CHANGE_NAME");
	   					break;
	   				}
	   				if(eventParam2.equals("1")){
	   					if(player.getClan()!=null){
	   						if(player.isClanLeader()){
			   					if(cambionombre.CambiarNombreClan_libre(player)){
			   						if(opera.haveItem(player, general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE)){
			   							if(cambionombre.Procedimiento_CambiarNombre_Clan(player,eventParam3)){
			   								opera.removeItem(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE, player);
			   								strHtml = "";
			   								break;
			   							}
			   						}
			   					}
	   						}else{
	   							central.msgbox("You'r not the clan leader", player);
	   						}
	   					}else{
	   						central.msgbox("You need to be a clan leader to use this option", player);
	   					}
	   					strHtml = htmls.Menu_CHANGE_NAME_CLAN(player,"USER_CHANGE_NAME");
	   					break;
	   				}
	   			}
				break;
	   		case "MENUDONA_VARIOS":
	   			if (eventParam1.equals("1")){
	   				if(eventParam2.equals("0")){
	   					strHtml = htmls.Menu_CHANGE_NAME_PJ(player, "MENUDONA_VARIOS");
	   					break;
	   				}
	   				if(eventParam2.equals("1")){/*
	   					if(opera.haveDonationItem(player, general.DONA_COST_CAMBIO_NOM_PJ)){
	   						if(cambionombre.Procedimiento_CambiarNombre_Char(player, eventParam3)){
	   							opera.Dona_removeItem(general.DONA_COST_CAMBIO_NOM_PJ, player);
	   							strHtml = donaManager.MainMenuDonation(player,-1,true);
	   							break;
	   						}
	   					}*/
	   				}
	   				strHtml = htmls.Menu_CHANGE_NAME_PJ(player, "MENUDONA_VARIOS");
					break;
	   			}
	   			if (eventParam1.equals("2")){
	   				if(eventParam2.equals("0")){
	   					strHtml = htmls.Menu_CHANGE_NAME_CLAN(player,"MENUDONA_VARIOS");
	   					break;
	   				}
	   				if(eventParam2.equals("1")){
	   					if(player.isClanLeader()){/*
	   						if(opera.haveDonationItem(player, general.DONA_COST_CAMBIO_NOM_CLAN)){
	   							if(cambionombre.Procedimiento_CambiarNombre_Clan(player, eventParam3)){
	   								opera.Dona_removeItem(general.DONA_COST_CAMBIO_NOM_CLAN, player);
	   								strHtml = donaManager.MainMenuDonation(player,-1,true);
	   								break;
	   							}
	   						}*/
	   					}else{
	   						central.msgbox("Solo el Lider de Clan puede cambiar el nombre de este.", player);
	   					}
	   				}
	   				strHtml = htmls.Menu_CHANGE_NAME_CLAN(player,"MENUDONA_VARIOS");
					break;
	   			}
				break;
			case "SET85DONA":
				/*
				if(player.getLevel() < 85){
					if(opera.haveDonationItem(player, general.DONA_COST_85)){
						if(opera.set85(player)) {
							opera.Dona_removeItem(general.DONA_COST_85, player);
						}
					}
				}else{
					central.msgbox("Lo sentimos, tu ya eres 85 por lo cual no puede usar mis servicios.",player);
				}
				strHtml = donaManager.MainMenuDonation(player);*/
				break;
			case "SETNobleDONA":
				/*if(opera.haveDonationItem(player, general.DONA_COST_NOBLE)){
					if(opera.setNoble(player)) {
						opera.Dona_removeItem(general.DONA_COST_NOBLE, player);
					}
				}
				strHtml = donaManager.MainMenuDonation(player);*/
				break;
			case "MenuDonation":
				if(eventParam2.equals("0")){
					if(eventParam3.equals("0")){
						strHtml = donaManager.MainMenuDonation(player,-1,true);
						break;
					}
					if(eventParam3.equals("1")){
						int CreditosDar = opera.haveDonaCreditCoin(player, 0);
						if(CreditosDar >0){
							opera.giveReward(player, general.DONA_ID_ITEM, CreditosDar);
							central.msgbox ("You have " + String.valueOf(CreditosDar) + " " + central.getNombreITEMbyID(general.DONA_ID_ITEM) + " in you Inventory!! :)",player);
						}else{
							central.msgbox("No presentas " + central.getNombreITEMbyID(general.DONA_ID_ITEM) ,player);
						}
						strHtml = donaManager.MainMenuDonation(player);
						break;
					}
				}
				if(eventParam2.equals("2")){
					strHtml = "";
					break;
				}
				if(eventParam2.equals("4")){
					strHtml = donaManager.ExplicaComoDonar();
					break;
				}
				break;
			case "DellvlMenu":
				strHtml = htmls.DelevelMenu(player);
				break;
			case "CHANGEPIN":
				if(pinCode.canChangePIN(player, eventParam1, eventParam2, eventParam3)){
					if(pinCode.changePINCode(player, eventParam1, eventParam3)){
						strHtml = pinCode.getPinCambiado(player, eventParam3);
						break;
					}
				}
				strHtml = PlayerConfigPanel.getConfigPanel_ChangePIN(player);
				break;
			case "ConfigPanel":
				if(eventParam1.equals("0")){
					strHtml = PlayerConfigPanel.getConfigPanel(player);
					break;
				}
				if(eventParam1.equals("1") || eventParam1.equals("2") || eventParam1.equals("3") || eventParam1.equals("4")){
					//if(player.toppvppk_SHOWANNOUCEMENT_ALL()) {
					ANNOUCSTR = (opera.TOP_PVP_PK_ANNOU(player)?1:0);
					EFECTPVP = (opera.TOP_PVP_PK_EFFECT(player)?1:0);
					SHOWSHIFT = (opera.SHOW_MY_STAT_SHIFT(player)?1:0);
					SHOWPINWINDOWS = (opera.SHOW_PIN_WINDOWS(player)?1:0);
					SHOWHERO = (opera.SHOW_HERO_PLAYER(player)?1:0);
					EXPSP = (general.getCharConfigEXPSP(player)?1:0);
					TRADE = (general.getCharConfigTRADE(player)?1:0);
					BADBUFF = (general.getCharConfigBADBUFF(player)?1:0);
					HIDESTORE = (general.getCharConfigHIDESTORE(player)?1:0);
					REFUSAL = ( general.getCharConfigREFUSAL(player) ? 1 : 0 );
					PARTYMATCHING =( general.getCharConfigREFUSAL(player) ? 1 : 0 );
				}

				if(eventParam1.equals("1")){
					central.set_ConfigCHAR(player,EFECTPVP,Integer.valueOf(eventParam2),SHOWSHIFT,SHOWPINWINDOWS,SHOWHERO,EXPSP,TRADE,BADBUFF,HIDESTORE,REFUSAL,PARTYMATCHING);
					strHtml = PlayerConfigPanel.getConfigPanel(player);
					break;
				}
				if(eventParam1.equals("2")){
					//Efectos Top PVP / PK
					central.set_ConfigCHAR(player,Integer.valueOf(eventParam2),ANNOUCSTR,SHOWSHIFT,SHOWPINWINDOWS,SHOWHERO,EXPSP,TRADE,BADBUFF,HIDESTORE,REFUSAL,PARTYMATCHING);
					strHtml = PlayerConfigPanel.getConfigPanel(player);
					break;
				}
				if(eventParam1.equals("3")){
					//er Estado oponente con Shift
					central.set_ConfigCHAR(player,EFECTPVP,ANNOUCSTR,Integer.valueOf(eventParam2),SHOWPINWINDOWS,SHOWHERO,EXPSP,TRADE,BADBUFF,HIDESTORE,REFUSAL, PARTYMATCHING);
					strHtml = PlayerConfigPanel.getConfigPanel(player);
					break;
				}
				if(eventParam1.equals("4")){
					if(eventParam2.equals("2")){
						strHtml = PlayerConfigPanel.getConfigPanel_ChangePIN(player);
						break;
					}
					//PIN WINDOWS
					central.set_ConfigCHAR(player, EFECTPVP,ANNOUCSTR,SHOWSHIFT,Integer.valueOf(eventParam2),SHOWHERO,EXPSP,TRADE,BADBUFF,HIDESTORE,REFUSAL, PARTYMATCHING);
					strHtml = PlayerConfigPanel.getConfigPanel(player);
					break;
				}
			case "TELEPORT_TO_FREE":
				player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);
				strHtml = "";
				break;
			case "VoteReward_new":
				votereward.getVoteWindows(player, true);
				strHtml = "";
				break;
			case "VoteReward":
				if (eventParam1.equals("0")){
					if(eventParam2.equals("0")){
						strHtml = votereward.MainMenuVoteReward(player);
						break;
					}else if(eventParam2.equals("1")){
						strHtml = votereward.MainMenuVoteRewardHopzone(player);
						break;
					}else if(eventParam2.equals("2")){
						strHtml = votereward.MainMenuVoteRewardTopzone(player);
					}else if(eventParam2.equals("3")){
						if(eventParam3.equals("0")){
							strHtml = votereward.MainMenuVoteReward_Pide(player);
						}else if(opera.isNumeric(eventParam3)){
							if(opera.haveItem(player, general.VOTO_ITEM_BUFF_ENCHANT_PRICE)){
								int buffID = Integer.valueOf(eventParam3);
								SkillData.getInstance().getSkill(buffID,315).applyEffects(player,player);
								opera.removeItem(general.VOTO_ITEM_BUFF_ENCHANT_PRICE, player);
							}
						}
					}
				}else if(eventParam1.equals("3")){
					strHtml = votereward.HTMLComoVotar(player);
					break;
				}else if(eventParam1.equals("1")){
					if(votereward.checkVoto(Integer.valueOf(eventParam2), eventParam3, Integer.valueOf(eventSplit[4]), player, ( eventParam3.equals("hopzone") ? 1 : 2 )  )){
						if(eventParam3.equals("hopzone")){
							opera.giveReward(player, general.VOTO_REWARD_HOPZONE);
						}else if(eventParam3.equals("topzone")){
							opera.giveReward(player, general.VOTO_REWARD_TOPZONE);
						}
						central.msgbox_Lado("Tu voto fue contabilizado correctamente.", player);
						central.msgbox("Muchas gracias por el voto en " + eventParam3 + ".", player);
					}else{
					}
				}else if(eventParam1.equals("4")){
					if(opera.haveItem(player, general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE)){
						opera.giveReward(player, general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID, 1);
						opera.removeItem(general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE, player);
					}
					strHtml = votereward.MainMenuVoteReward(player);
					break;
				}
				htmls.firtsHTML(player, general.npcGlobal(player));
				break;
			case "CastleManagerStr":
				if(eventParam1.equals("0")){
					strHtml = htmls.MainHtmlCastleManager(player);
					break;
				}else{
					int castleId = Integer.valueOf(eventParam1);
				    Castle castle = CastleManager.getInstance().getCastleById(castleId);
				    if(castleId != 0){
				    	player.sendPacket(new SiegeInfo(castle));
				    	strHtml = htmls.MainHtmlCastleManager(player);
						break;
				    }
				}
				break;
			case "DropSearch":
				if(eventParam1.equals("0")){
					strHtml = dropSearch.HTMLBusquedaDrop(player);
					break;
				}
				if(eventParam1.equals("1")){
					if(eventParam2.length() >0) {
						strHtml = dropSearch.BusquedaDrop(player,eventParam2,Integer.valueOf(eventParam3));
					}
					break;
				}
				if(eventParam1.equals("2")){
					String[] TextoPosi = eventParam3.split("~");
					strHtml = dropSearch.BusquedaDropListarNPC(player,Integer.valueOf(eventParam2),TextoPosi[0],Integer.valueOf(TextoPosi[1]));
					break;
				}
				break;
			case "DropSearchTele":
				if(eventParam1.equals("0") && eventParam2.equals("0") && eventParam3.equals("0")){
					central.msgbox("Lo sentimos pero no puedes ser teletransportado a este mob",player);
					strHtml = dropSearch.HTMLBusquedaDrop(player);
				}
				if((!general.DROP_SEARCH_TELEPORT_FOR_FREE && opera.haveItem(player, general.DROP_TELEPORT_COST)) || general.DROP_SEARCH_TELEPORT_FOR_FREE){
					if(!general.DROP_SEARCH_TELEPORT_FOR_FREE){
						opera.removeItem(general.DROP_TELEPORT_COST, player);
					}
					player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);
					central.msgbox("Buena suerte en tu Busqueda!", player);
					strHtml = "";
					break;
				}else{
					central.msgbox("No tienes los Item necesarios para usar mis Servicios", player);
				}
				strHtml = dropSearch.HTMLBusquedaDrop(player);
				break;
			case "logpeleas":
				if(eventParam1.equals("0")){
					strHtml = logpvppk.Main_Log_Peleas(player,"");
					break;
				}
				if(eventParam1.equals("1")){
					strHtml = logpvppk.Main_Log_Peleas(player,eventParam2);
					break;
				}
				if(eventParam1.equals("2")){
					strHtml = logpvppk.getAllPVPPK(eventParam2,Integer.valueOf(eventParam3));
					break;
				}
				break;
			case "PKlistoption":
				strHtml = pvppk.PKlistoption(player, eventParam1, eventParam2, eventParam3);
				break;
			case "goFlag":
				if(flagFinder.canuseFlagFinder(player)){
					if(flagFinder.goFlagFinder(player)){
						opera.removeItem(general.FLAG_FINDER_PRICE, player);
						strHtml = "";
						break;
					}
				}
				strHtml = flagFinder.MainHtmlFlagFinder();
				break;
			case "goLeader":
				if(partyFinder.canUse(player)){
					if(partyFinder.goPartyLeader(player)){
						opera.removeItem(general.PARTY_FINDER_PRICE, player);
						break;
					}
				}
				strHtml = partyFinder.MainHtmlParty(player);
				break;
			case "Delevel":
				if(delevel.canUse(player)){
					if(delevel.delevel1(player)){
						opera.removeItem(general.DELEVEL_PRICE, player);
						strHtml = delevel.htmlDelevel(player);
						break;
					}
				}
				strHtml = htmls.DelevelMenu(player);
				break;
			case "getnoble":
	   		case "ReleaseAttribute":
	   			player.sendPacket(new ExShowBaseAttributeCancelWindow(player));
	   			strHtml = "";
				break;
	   		case "SkillList":
	   		case "EnchantSkillList":
	   		case "SafeEnchantSkillList" :
	   		case "UntrainEnchantSkillList" :
	   		case "ChangeEnchantSkillList" :
	   		case "warehouse":
	   			//warehouse DepositC
	   			if(eventParam1.equals("DepositC")){
					player.sendPacket(ActionFailed.STATIC_PACKET);
					player.setActiveWarehouse(player.getClan().getWarehouse());
					player.isInventoryDisabled();
					player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.CLAN));
	   			}else if(eventParam1.equals("WithdrawC")){
					if (Config.L2JMOD_ENABLE_WAREHOUSESORTING_CLAN)
					{
						NpcHtmlMessage msg = new NpcHtmlMessage(Integer.valueOf(general.npcGlobal(player)));
						msg.setFile(player.getHtmlPrefix(), "data/html/mods/WhSortedC.htm");
						msg.replace("%objectId%", general.npcGlobal(player));
						player.sendPacket(msg);
					}
					else
					{
						opera.showWithdrawWindow(player, null, (byte) 0);
					}
	   			}
	   			break;
			case "tranferclassGO":
				if(classmaster.AddProfesion(player, Integer.valueOf(eventParam1))){
					strHtml = classmaster.okTransfer(player);
				}
				break;
	   		case "increaseclan" ://
	   			strHtml = clan.increaseclan(player);
	   			break;
			case "increase_clan":
				clan.increase_clan(player);
				break;
			case "DisbandClan"://
				strHtml = clan.DisbandClan(player);
				break;
			case "dissolve_clan":
				clan.dissolve_clan(player);
				break;
			case "RestoreClan"://
				strHtml = clan.RestoreClan(player);
				break;
			case "recover_clan":
				clan.recover_clan(player);
				break;
			case "giveclanl"://
				strHtml = clan.giveclanl(player);
				break;
			case "change_clan_leader":
				if(eventParam1.isEmpty()){
					central.msgbox("Ingresa el Nombre del nuevo clan de lider", player);
					strHtml = clan.giveclanl(player);
					break;
				}
				clan.change_clan_leader(player, eventParam1);
				break;
			case "learn_clan_skills"://
				strHtml = clan.learn_clan_skills(player);
				break;
			case "createclan"://
				strHtml = clan.createclan(player);
				break;
			case "create_clan"://
				if(eventParam1.isEmpty()){
					central.msgbox("Debe ingresar el nombre clan", player);
					strHtml= clan.createclan(player);
					break;
				}
				clan.create_clan(player, eventParam1);
				break;
			case "createally"://
				strHtml = clan.createally(player);
				break;
			case "create_ally":
				if(eventParam1.isEmpty()){
					central.msgbox("Dene ingresar el Nombre de ally", player);
					strHtml = clan.createally(player);
					break;
				}
				clan.create_ally(player, eventParam1);
				break;
			case "dissolve_ally"://
				clan.dissolve_clan(player);
				strHtml = "";
				break;
		}
		return strHtml;
	}


}

