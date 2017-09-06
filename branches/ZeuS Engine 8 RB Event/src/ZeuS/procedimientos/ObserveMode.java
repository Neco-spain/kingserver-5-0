package ZeuS.procedimientos;

import java.awt.TrayIcon.MessageType;
import java.util.HashMap;

import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.L2Party.messageType;
import com.l2jserver.gameserver.model.actor.instance.L2CubicInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.network.serverpackets.ObservationMode;


public class ObserveMode{
	
	private static HashMap<L2PcInstance,Boolean> PlayerIsInObserveMode = new HashMap<L2PcInstance,Boolean>();
	
	public static void setObserveMode(L2PcInstance player,boolean p1){
		PlayerIsInObserveMode.put(player, p1);
	}
	
	public static boolean isInObserveMode(L2PcInstance player){
		if(PlayerIsInObserveMode!=null){
			if(PlayerIsInObserveMode.containsKey(player)){
				return PlayerIsInObserveMode.get(player);
			}
		}
		return false;
	}
	public static void EnterObserveMode(L2PcInstance player, Location loc){
		if (player.hasSummon())
		{
			player.getSummon().unSummon(player);
		}
		
		player.getEffectList().stopSkillEffects(true, AbnormalType.HIDE);
		
		
		if (!player.getCubics().isEmpty())
		{
			for (L2CubicInstance cubic : player.getCubics().values())
			{
				cubic.stopAction();
				cubic.cancelDisappear();
			}
			player.getCubics().clear();
		}
		
		if (player.getParty() != null)
		{
			player.getParty().removePartyMember(player, messageType.Expelled);
		}
		
		if (player.isSitting())
		{
			player.standUp();
		}
		
		player.setLastLocation();
		player._observerMode = true;
		player.setTarget(null);
		player.setIsParalyzed(true);
		player.startParalyze();
		player.setIsInvul(true);
		player.setInvisible(true);
		player.sendPacket(new ObservationMode(loc));
		player.teleToLocation(loc, false);
		player.broadcastUserInfo();
		player._isChatBlock = true;
	}
}
