package ZeuS.interfase;


import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.L2Henna;
import com.l2jserver.gameserver.network.serverpackets.HennaEquipList;
import com.l2jserver.gameserver.network.serverpackets.HennaRemoveList;

import ZeuS.Config.general;

public class symbol {

	public static String optionsymbol(L2PcInstance st, String cases,int num){
		String MAIN_HTML = "";
		if(cases.equals("draws")){
			/*Freya
			L2HennaInstance[] henna = HennaTreeTable.getInstance().getAvailableHenna(st.getClassId());
			st.sendPacket(new HennaEquipList(st, henna));
			*/
			HennaEquipList hel = new HennaEquipList(st);
			st.sendPacket(hel);
		}
		if(cases.equals("deletes")){
			boolean hasHennas = false;
			int i=0;
			if(num != 0) {
				st.removeHenna(num);
			}
			while(i <= 2){
				i++;
				L2Henna henna = st.getHenna(i);
				if(henna != null) {
					hasHennas = true;
				}
			}
			if(!hasHennas){
				MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
				MAIN_HTML += central.LineaDivisora(2)  + central.headFormat("Symbol Maker - delete symbol")  + central.LineaDivisora(2);
				MAIN_HTML += central.LineaDivisora(3) + central.headFormat("You don't have any symbol to remove!","LEVEL") + central.LineaDivisora(3);
				MAIN_HTML += central.BotonGOBACKZEUS() + central.getPieHTML() + "</center></body></html>";
			}else{
				HennaRemoveList hel = new HennaRemoveList(st);
				st.sendPacket(hel);
			}
		}
			return MAIN_HTML;
	}
}
