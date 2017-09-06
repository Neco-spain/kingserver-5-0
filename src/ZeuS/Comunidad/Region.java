package ZeuS.Comunidad;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Logger;

import com.l2jserver.gameserver.data.xml.impl.AdminData;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.Config.general;
import ZeuS.interfase.sellBuff;
import ZeuS.procedimientos.opera;


public class Region {
	private static final int Columnas = 5;

	private static HashMap<Integer,Boolean> ShowAllGlobePlayer = new HashMap<Integer,Boolean>();
	private final static Logger _log = Logger.getLogger(Region.class.getName());




	private static String getLeyendaColores(){
		String Retorno ="<table width=736 bgcolor=1C1C1C><tr>" +
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_GM.name()) +">GM</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_DEATH.name()) +">DEATH</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_HERO.name()) +">HERO</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_CLANLEADER.name()) +">C.LEADER</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_NORMAL.name()) +">NORMAL</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PVP.name()) +">FLAG</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PRIVATESTORE.name()) +">P.STORE</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_BUFFSTORE.name()) +">B.STORE</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PK.name()) +">KARMA (PK)</font></td>"+
						"<td width=73 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_JAILED.name()) +">JAILED</font></td>"+
						"</tr></table>";
		return Retorno;
	}



	private static boolean canAddToList(L2PcInstance player){
		if(!player.isGM()){
			return true;
		}
		return opera.isGmAllVisible(player);
	}

	private static final Comparator<L2PcInstance> PLAYER_NAME_COMPARATOR = new Comparator<L2PcInstance>()
	{
		@Override
		public int compare(L2PcInstance p1, L2PcInstance p2)
		{
			return p1.getName().compareToIgnoreCase(p2.getName());
		}
	};

	private static String getServerInfo(int paginaVer, L2PcInstance playerOnCB){
		String retorno = "";
		boolean isGm = playerOnCB.isGM();
		int onlinePlayer = L2World.getInstance().getAllPlayersCount();
		int onlineGM = AdminData.getInstance().getAllGms(isGm).size();
		int onlineGMInvi = AdminData.getInstance().getAllGms(true).size();

		String Mirando = "";

		if(ShowAllGlobePlayer.get(playerOnCB.getObjectId())){
			Mirando = "ALL";
		}else{
			Mirando = "REGION";
		}

		if(!playerOnCB.isGM()){
			onlinePlayer = onlinePlayer - onlineGMInvi;
		}

		String btnNext = "<button value=\"Next\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_REGION_PART_EXEC + ";"+Mirando+";"+ String.valueOf(paginaVer+1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPrevi = "<button value=\"Prev.\" width=60 height=23 action=\"bypass " + general.COMMUNITY_BOARD_REGION_PART_EXEC + ";"+Mirando+";"+ String.valueOf(paginaVer-1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";


		int onlineShop = 0;
		int onlineInThisRegion = 0;
		Vector<String> playerConColor = new Vector<String>();
		int contador = 0;
		int desde = paginaVer * general.COMMUNITY_BOARD_REGION_PLAYER_ON_LIST;
		int hasta = desde + general.COMMUNITY_BOARD_REGION_PLAYER_ON_LIST;
		int contadorMini = 0;
		boolean haveNext = false;
		String GrillaPJ = "<tr>";

		if(ShowAllGlobePlayer.get(playerOnCB.getObjectId())){
			Vector<String> PlayerNameOnlineWorld = opera.getAllNamePlayerOnWorld();
			Collections.sort(PlayerNameOnlineWorld);
			for(String playerName : PlayerNameOnlineWorld){
				L2PcInstance player = opera.getPlayerbyName(playerName);
				if(player!=null){
					if((player.isGM() && opera.isGmAllVisible(player,playerOnCB)) || !player.isGM()){
						if(player.isInCraftMode() || player.isInStoreMode() || player.getClient().isDetached()){
							onlineShop++;
						}
						if(opera.isInGeoZone(player, playerOnCB)){
							onlineInThisRegion++;
						}
						if(!haveNext){
							if(canAddToList(player) || playerOnCB.isGM()){
								if(contador >= desde){
									if(contador < hasta){
										if(contadorMini == Columnas){
											GrillaPJ += "</tr><tr>";
											contadorMini=0;
										}
										GrillaPJ += "<td width=70 align=CENTER><font color="+ getColorForPlayer(player) +"><a action=\"bypass " + general.COMMUNITY_BOARD_REGION_PART_EXEC + ";CHARINFO;"+ String.valueOf(paginaVer) +";"+ player.getName() +";0;0\">"+ player.getName() +"</a></font></td>";
										contadorMini++;
									}else if(contador>=hasta){
										haveNext = true;
									}
								}
								contador++;
							}
						}
					}
				}
			}
		}else{
			Vector<String> PlayerNameOnlineRegion = opera.getAllNamePlayerOnRegion(playerOnCB);
			Collections.sort(PlayerNameOnlineRegion);
			for(String playerName : PlayerNameOnlineRegion){
				L2PcInstance player = opera.getPlayerbyName(playerName);
				if(player!=null){
					if((player.isGM() && opera.isGmAllVisible(player)) || !player.isGM()){
						if(player.isInCraftMode() || player.isInStoreMode() || player.getClient().isDetached()){
							onlineShop++;
						}
						if(opera.isInGeoZone(player, playerOnCB)){
							onlineInThisRegion++;
						}
						if(canAddToList(player) || playerOnCB.isGM()){
							if(contador >= desde){
								if(contador < hasta){
									if(contadorMini == Columnas){
										GrillaPJ += "</tr><tr>";
										contadorMini=0;
									}
									GrillaPJ += "<td width=70 align=CENTER><font color="+ getColorForPlayer(player) +"><a action=\"bypass " + general.COMMUNITY_BOARD_REGION_PART_EXEC + ";CHARINFO;"+ String.valueOf(paginaVer) +";"+ player.getName() +";0;0\">"+ player.getName() +"</a></font></td>";
									contadorMini++;
								}else if(contador>=hasta){
									haveNext = true;
								}
							}
							contador++;
						}
					}
				}
			}
		}

		if(contadorMini<5){
			for(int i=contadorMini;i<=5;i++){
				GrillaPJ += "<td width=70></td>";
			}
		}

		if(!GrillaPJ.endsWith("</tr>")){
			GrillaPJ += "</tr>";
		}

		GrillaPJ = "<table width=750 align=CENTER>" + GrillaPJ + "</table>";

		String DatosOnline = "<table width=750 align=CENTER><tr>" +
							"<td width=80 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_NORMAL.name()) +">Player Online</font></td>" +
							"<td width=28 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_NORMAL.name()) +">"+ String.valueOf(onlinePlayer) +"</font></td>" +
							"<td width=80 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_GM.name()) +">GM's Online</font></td>" +
							"<td width=28 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_GM.name()) +">"+ String.valueOf(onlineGM) +"</font></td>" +
							"<td width=80 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PRIVATESTORE.name()) +">Offliners</font></td>" +
							"<td width=28 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.COLOR_PRIVATESTORE.name()) +">"+ String.valueOf(onlineShop) +"<font></td>" +
							"<td width=100 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Player in this Area</font></td>" +
							"<td width=100 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">"+ String.valueOf(onlineInThisRegion) +"</font></td>" +
							"</tr></table>";
		DatosOnline += "<table width=750 align=CENTER><tr>" +
						"<td width=255></td>"+
						"<td width=120 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Server Online Time</font></td>" +
						"<td width=120 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">"+ opera.getTiempoON(opera.getUnixTimeNow() - general.ServerStartUnixTime) +"</font></td>" +
						"<td width=255></td>"+
						"</tr></table>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (paginaVer > 0 ? btnPrevi : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Region Page "+ String.valueOf(paginaVer + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnNext : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		retorno += cbManager._formBodyComunidad(DatosOnline) + getLeyendaColores() + cbManager._formLine(790) + GrillaPJ + "<br>" + TablaControles;
		return retorno;
	}



	private static String getColorForPlayer(L2PcInstance player){

		if(sellBuff.isBuffSeller(player)){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_BUFFSTORE.name());
		}else if(player.isInStoreMode() || player.isInCraftMode()){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_PRIVATESTORE.name());
		}else if(player.getKarma()>0){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_PK.name());
		}else if(player.isJailed()){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_JAILED.name());
		}else if(player.getPvpFlag()>0){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_PVP.name());
		}else if(player.isDead()){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_DEATH.name());
		}else if(player.isGM()){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_GM.name());
		}else if(player.isHero()){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_HERO.name());
		}/*else if(general.isPremium(player, true) || general.isPremium(player, false)){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_VIP.name());
		}else if(player.isClanLeader()){
			return cbManager.getFontColor(cbManager.enumColor.COLOR_CLANLEADER.name());
		}*/

		return cbManager.getFontColor(cbManager.enumColor.Blanco.name());
	}

	public static String getRegion(L2PcInstance player, String Accion, int Pagina){
		String btnAllRegion = "<button value=\"Show All World\" width=150 height=24 action=\"bypass " + general.COMMUNITY_BOARD_REGION_PART_EXEC + ";ALL;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnThisRegion = "<button value=\"Show This Zone\" width=150 height=24 action=\"bypass " + general.COMMUNITY_BOARD_REGION_PART_EXEC + ";REGION;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String GrillaBotones = "<table width=300 align=CENTER><tr><td width=150>"+btnAllRegion+"</td><td width=150>"+btnThisRegion+"</td></tr></table>";

		if(ShowAllGlobePlayer==null){
			ShowAllGlobePlayer.put(player.getObjectId(), true);
		}else{
			if(!ShowAllGlobePlayer.containsKey(player.getObjectId())){
				ShowAllGlobePlayer.put(player.getObjectId(), true);
			}
		}

		//general.COMMUNITY_BOARD_REGION_PART_EXEC + ";CHARINFO;"+ String.valueOf(paginaVer) +";"+ player.getName() +";0;0

		if(Accion!=null){
			if(Accion.length()>0){
				String[]param = Accion.split(";");
				if(param.length>1){
					if(param[1].equals("REGION")){
						ShowAllGlobePlayer.put(player.getObjectId(), false);
					}else if(param[1].equals("ALL")){
						ShowAllGlobePlayer.put(player.getObjectId(), true);
					}else if(param[1].equals("CHARINFO")){
						opera.getInfoPlayer(param[3], player);
					}

					Pagina = Integer.valueOf(param[2]);
				}
			}
		}

		String parte = ShowAllGlobePlayer.get(player.getObjectId()) ? "World View" : "Region View" ;

		String retorno = "<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">Global Server Statistics - "+ parte +"</font><br1>"+GrillaBotones);
		retorno += "<br>" + cbManager._formBodyComunidad(getServerInfo(Pagina,player));
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

}
