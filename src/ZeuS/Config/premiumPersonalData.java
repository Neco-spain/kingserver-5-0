package ZeuS.Config;

import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2ClanMember;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;
import ZeuS.server.Anunc;

public class premiumPersonalData {
	private String ACCOUNT_OR_CLANID;
	private int BEGIN_DATE;
	private int END_DATE;
	protected boolean ISACTIVE;
	protected boolean ISACCOUNT;
	private int ID_PREMIUM;
	
	public boolean isActive(){
		if(this.END_DATE <= opera.getUnixTimeNow()){
			return false;
		}
		return true;
	}
	
	public int getBegin(){
		return this.BEGIN_DATE;
	}
	
	public int getEnd(){
		return this.END_DATE;
	}
	
	public int getIdPremiumUse(){
		return this.ID_PREMIUM;
	}
	public premiumPersonalData(String _Account_or_ClanID, int _BeginDate, int _EndDate, boolean _isAccount, int _idPremium){
		this.ACCOUNT_OR_CLANID = _Account_or_ClanID;
		this.BEGIN_DATE = _BeginDate;
		this.END_DATE = _EndDate;
		if(_EndDate > opera.getUnixTimeNow()){
			this.ISACTIVE = true;
		}else{
			this.ISACTIVE = false;
		}
		this.ISACCOUNT = _isAccount;
		this.ID_PREMIUM = _idPremium;
	}
	
	public class EndPremiumClock implements Runnable{
		public EndPremiumClock(){
		}
		@Override
		public void run(){
			try{
				if(END_DATE <= opera.getUnixTimeNow()){
					ISACTIVE = false;
					if(ISACCOUNT){
						if(opera.isCharInGame(Integer.valueOf(ACCOUNT_OR_CLANID))){
							L2PcInstance ppl = opera.getPlayerbyID(Integer.valueOf(ACCOUNT_OR_CLANID));
							central.msgbox_Lado("Premium Account is Over.", ppl, "PREMIUM");
						}
					}else{
						L2Clan clan = ClanTable.getInstance().getClan(Integer.valueOf(ACCOUNT_OR_CLANID));
						for(L2ClanMember ppl : clan.getMembers()){
							if(!ppl.isOnline()){
								continue;
							}
							L2PcInstance ppl2 = ppl.getPlayerInstance();
							central.msgbox_Lado("Premium Clan is Over", ppl2, "PREMIUM_CLAN");
						}
					}
				}
			}catch(Exception a){

			}

		}
	}
}
