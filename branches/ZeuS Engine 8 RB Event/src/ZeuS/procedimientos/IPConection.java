package ZeuS.procedimientos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.server.Anunc;

public class IPConection {

	private static final Logger _log = Logger.getLogger(IPConection.class.getName());
	private static final String SQL_INSERT = "INSERT INTO zeus_connection() values (?,?,?,?,?,NOW(),?)";

	private static int SecondToCloseNewAccount = 15;

	//private static HashMap<String, HashMap<String,HashMap<String,L2PcInstance>>> PlayerConec = new HashMap<String, HashMap<String,HashMap<String,L2PcInstance>>>();
	private static HashMap<String, HashMap<String,Integer>> PlayerConec = new HashMap<String, HashMap<String,Integer>>();
	private static HashMap<String,Vector<L2PcInstance>> playerConec_char = new HashMap<String, Vector<L2PcInstance>>();
	public static boolean RegistroPlayer_IPCheck(L2PcInstance player){
		String ip_wan = ZeuS.getIp_Wan(player);
		String ip_lan = ZeuS.getIp_pc(player);
		String idDos = ip_wan + ip_lan;
		resetChar(idDos);

		if(!general.MAX_IP_CHECK){
			return true;
		}


		int ipCountPlayer = getNumberConecPlayer(player);

		int MaximoPermitido = general.MAX_IP_COUNT;

		if(general.isPremium(player, true)){
			MaximoPermitido = general.MAX_IP_VIP_COUNT;
		}

		if(ipCountPlayer >= MaximoPermitido){
			central.msgbox("MAX IP ALLOW " + String.valueOf(MaximoPermitido), player);
			Anunc.Anunciar_All_Char_IP(player, msg.MAXIMUM_ALLOWD_IP_ARE_$countAllow_CHAR_$player_ARE_DISCONNECTED.replace("$countAllow", String.valueOf(general.MAX_IP_COUNT)).replace("$player",player.getName()), "IP CHECK");
			ThreadPoolManager.getInstance().executeGeneral(new closeClient(SecondToCloseNewAccount * 1000, player));
			insertIntoBD(player,"DISCONNECTED");
			return false;
		}

		/*if(PlayerConec==null){
			PlayerConec.put(ip_wan, new HashMap<String, Integer>());
		}else if(!PlayerConec.containsKey(ip_wan)){
			PlayerConec.put(ip_wan, new HashMap<String, Integer>());
		}

		if(!PlayerConec.get(ip_wan).containsKey(ip_lan)){
			PlayerConec.get(ip_wan).put(ip_lan, 1);
		}else{
			PlayerConec.get(ip_wan).put(ip_lan, PlayerConec.get(ip_wan).get(ip_lan) + 1);
		}*/

		if(!playerConec_char.containsKey(idDos)){
			playerConec_char.put(idDos, new Vector<L2PcInstance>());
		}
		if(!playerConec_char.get(idDos).contains(player)){
			playerConec_char.get(idDos).add(player);
		}

		insertIntoBD(player,"CONNECTED");

		return true;
	}
	private static void resetChar(String idDos){
		Vector<L2PcInstance>ParaBorrar = new Vector<L2PcInstance>();

		if(playerConec_char==null){
			return;
		}

		if(playerConec_char.containsKey(idDos)){
			for(L2PcInstance cha : playerConec_char.get(idDos)){
				if(!cha.isOnline() || cha.getClient().isDetached()){
					ParaBorrar.add(cha);
				}
			}
		}
		if(ParaBorrar!=null){
			for(L2PcInstance cha : ParaBorrar){
				playerConec_char.get(idDos).remove(cha);
			}
		}
	}
	private static int getNumberConecPlayer(L2PcInstance player){
		int Retorno = 0;
		String ip_wan = ZeuS.getIp_Wan(player);
		String ip_lan = ZeuS.getIp_pc(player);
		String idDos = ip_wan + ip_lan;
		if(playerConec_char.containsKey(idDos)){
			Retorno = playerConec_char.get(idDos).size();
		}
		return Retorno;
	}

	private static void insertIntoBD(L2PcInstance player, String estado){
		String ip_wan = ZeuS.getIp_Wan(player);
		String ip_lan = ZeuS.getIp_pc(player);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = ConnectionFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(SQL_INSERT);
			psqry.setString(1, ip_wan);
			psqry.setString(2, ip_lan);
			psqry.setString(3, player.getAccountName());
			psqry.setString(4, player.getName());
			psqry.setString(5, estado);
			psqry.setInt(6, player.getObjectId());
			psqry.execute();
		}catch(SQLException a){
			_log.warning("ZeuS - IP Insert Error: "+ a.getMessage() + " " + String.valueOf(a.getErrorCode()));
		}

		try{
			conn.close();
		}catch(Exception a){

		}
	}
}
class closeClient implements Runnable{
	private int _time;
	L2Character _player;

	public closeClient (int time, L2PcInstance player){
		_time = time;
		_player = player;
	}

	@Override
	public void run(){
		if (_time > 0){
			ThreadPoolManager.getInstance().scheduleGeneral(this, 1000);
			_time-=1000;
		}

		if (_time==0){
			if(_player!=null){
				L2PcInstance cha = (L2PcInstance)_player;
				if(cha.isOnline()){
					try{
						cha.logout();
					}catch(Exception a){

					}
					try{
						cha.getClient().closeNow();
					}catch(Exception a){

					}
				}
			}
		}
	}
}
