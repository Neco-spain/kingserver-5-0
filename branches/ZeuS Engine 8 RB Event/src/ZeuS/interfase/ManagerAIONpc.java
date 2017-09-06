package ZeuS.interfase;

import java.util.logging.Logger;

import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

import ZeuS.Config.general;
import ZeuS.event.ClanReputationEvent;
import ZeuS.procedimientos.delegar;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.teamEvent;

public class ManagerAIONpc {

	private static final Logger _log = Logger.getLogger(GameServer.class.getName());

	static NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		private static final ManagerAIONpc _instance = new ManagerAIONpc();
	}
	public static ManagerAIONpc getIntence(){
		return SingletonHolder._instance;
	}

	public void bypass(L2PcInstance player, String command){
		if (command.startsWith("ZeuS "))
        {
               player.sendMessage("x");
        }
        else if (command.equals("main"))
        {
                player.sendMessage("y");
        }
	}
	
	private static boolean CanUseInsideCastle(L2PcInstance player, L2Npc NPC){
		boolean retorno = true;
		if(!player.isGM() && NPC.isInsideZone(ZoneId.CASTLE)){
			if(player.getClan()==null){
				return false;
			}else if(player.getClan().getCastleId() <=0){

				return false;
			}
		}
		
		return retorno;
	}

	public static void showFirstHtml(L2PcInstance player, int obj)
	{
		general.IS_USING_NPC.put(player.getObjectId(),true);
		general.IS_USING_CB.put(player.getObjectId(),false);
		if(!opera.canShowHTMLRad(player)){
			return;
		}
		if(!general._activated()){
			return;
		}
		if(isNpcZeus(player)){
			String Inicial = htmls.firtsHTML(player, general.npcGlobal(player));
			central.sendHtml(player, Inicial);
			return;
		}
		if(isNpcBusqueda(player)){
			String Inicial = desafioBusqueda.getBienvenida(player, getIDNpc(player));
			player.sendPacket(new NpcHtmlMessage(0,Inicial));
			return;
		}
		if(isColiseumNPC(player)){
			opera.enviarHTML(player, teamEvent.getHtmlEventNpc(player));
			return;
		}
		if(isNpcClanEvent(player)){
			ClanReputationEvent.mainWindows(player);
			return;
		}
	}

	public static int getIDNpc(L2PcInstance player){
		L2Object obj = player.getTarget();
		if (obj == null)
		{
			return -1;
		}
		if (obj instanceof L2Npc)
		{
			return opera.getIDNPCTarget(player);
		}
		return -1;
	}

	public static boolean isNpcFromZeus(int NPCID){

		if(isNpcZeus(NPCID)){
			return true;
		}

		if(isNpcBusqueda(NPCID)){
			return true;
		}

		if(general.EVENT_COLISEUM_NPC_ID==NPCID){
			return true;
		}
		
		if(general.EVENT_REPUTATION_CLAN_ID_NPC == NPCID){
			return true;
		}
		

		return false;
	}

	public static boolean isNpcZeuS(int NPCID){
		return isNpcZeus(NPCID);
	}


	protected static boolean isNpcZeus(int NPCID){
		if((NPCID == general.ID_NPC) || (NPCID == general.ID_NPC_CH)){
			return true;
		}

		return false;

	}

	protected static boolean isNpcZeus(L2PcInstance player){
		try{
			if(!general.IS_USING_NPC.get(player.getObjectId()) && general.IS_USING_CB.get(player.getObjectId())){
				return true;
			}
		}catch(Exception a){
			return true;
		}

		L2Object obj = player.getTarget();
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof L2Npc)
		{
			if((opera.getIDNPCTarget(player) == general.ID_NPC) || (opera.getIDNPCTarget(player) == general.ID_NPC_CH)){
				return true;
			}
		}
		return false;

	}

	protected static boolean isZeuSCH(L2PcInstance player){
		L2Object obj = player.getTarget();
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof L2Npc)
		{
			if(opera.getIDNPCTarget(player) == general.ID_NPC_CH){
				return true;
			}
		}
		return false;
	}

	protected static boolean isZeuSALL(L2PcInstance player){
		L2Object obj = player.getTarget();
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof L2Npc)
		{
			if(opera.getIDNPCTarget(player) == general.ID_NPC){
				return true;
			}
		}
		return false;
	}

	private static boolean isNpcBusqueda(int NPCID){
		if(general.DESAFIO_NPC_BUSQUEDAS.contains(NPCID)){
			return true;
		}
		return false;
	}

	private static boolean isNpcBusqueda(L2PcInstance player){
		if(!opera.canShowHTMLRad(player)){
			return false;
		}
		L2Object obj = player.getTarget();
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof L2Npc)
		{
			int idNpc = opera.getIDNPCTarget(player);
			if(isNpcBusqueda(idNpc)){
				return true;
			}

		}
		return false;
	}
	
	private static boolean isNpcClanEvent(L2PcInstance player){
		if(!opera.canShowHTMLRad(player)){
			return false;
		}
		L2Object obj = player.getTarget();
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof L2Npc)
		{
			int idNpc = opera.getIDNPCTarget(player);
			if(idNpc == general.EVENT_REPUTATION_CLAN_ID_NPC){
				return true;
			}

		}
		return false;
	}

	protected static boolean isColiseumNPC (L2PcInstance player){
		L2Object obj = player.getTarget();
		if(!opera.canShowHTMLRad(player)){
			return false;
		}
		if (obj == null)
		{
			return false;
		}


		if (obj instanceof L2Npc)
		{
			L2Npc temp = (L2Npc) obj;
			int idNpc = opera.getIDNPCTarget(player);
			if(general.EVENT_COLISEUM_NPC_ID ==idNpc){
				return true;
			}

		}

		return false;
	}

	public static boolean talkNpc(L2PcInstance player, String parametros)
	{
		if(!general._activated()){
			return true;
		}

		if(!opera.canShowHTMLRad(player)){
			return false;
		}
		
		if(parametros.indexOf("sellBuff")>=0){
			//sellBuff.ByPass(player, parametros);
			return true;
		}
		

		if(isNpcZeus(player) || isColiseumNPC(player) ){
			String ShowHTML = delegar.delegar(parametros, player);
			if(ShowHTML.length()>0) {
				player.sendPacket(new NpcHtmlMessage(0,ShowHTML));
				return true;
			}
		}

		if(isNpcBusqueda(player)){
			String ShowHtml = delegar.delegar_desafio(parametros, player);
			if(ShowHtml.length()>0){
				player.sendPacket(new NpcHtmlMessage(0,ShowHtml));
				return true;
			}
		}
		
		if(isNpcClanEvent(player)){
			String ShowHtml = ClanReputationEvent.delegar(player, parametros);
			if(ShowHtml.length()>0){
				player.sendPacket(new NpcHtmlMessage(0,ShowHtml));
				return true;
			}			
		}

		return true;
	}
}