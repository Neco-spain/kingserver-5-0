package ZeuS.procedimientos;

import java.util.Vector;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Comunidad.EngineForm.C_findparty;

public class itemLink {
	private static int ContadorRetroceso = -2099999999;
	private static Vector<Integer> v_findParty = new Vector<Integer>();
	private static Vector<Integer> v_auctionoffline = new Vector<Integer>();
	
	public enum sectores{
		V_FIND_PARTY,
		V_AUCTION_OFFLINE
	}
	
	public static boolean isLinkFromZeuS(L2PcInstance charRequestLink, int idLink){
		if(v_findParty!=null){
			if(v_findParty.contains(idLink)){
				C_findparty.bypass(charRequestLink, idLink);
				return true;
			}
		}else if(v_auctionoffline!=null){
			if(v_auctionoffline.contains(idLink)){
				return true;
			}
		}
		return false;
	}
	
	public static int getNewIdLink(String Sector){
		int retorno =0;
		switch(Sector){
			case "V_FIND_PARTY":
				retorno = ContadorRetroceso++;
				v_findParty.add(retorno);
				return retorno;
			case "V_AUCTION_OFFLINE":
				retorno = ContadorRetroceso++;
				v_auctionoffline.add(retorno);
				return retorno;
		}
		return ContadorRetroceso;
	}
	
}
