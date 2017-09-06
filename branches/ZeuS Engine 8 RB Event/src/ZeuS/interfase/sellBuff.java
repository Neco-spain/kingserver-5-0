package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.procedimientos.opera;

import com.l2jserver.Config;
import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.LoginServerThread;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.skills.targets.L2TargetType;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.L2GameClient.GameClientState;

public class sellBuff {
	
	private static final Logger _log = Logger.getLogger(sellBuff.class.getName());
	
	private static Vector<Integer>BuffBlock = new Vector<Integer>();
	
	private static final String ColorVendedor = "AEB404";
	
	private static String INSERT_ZEUS_BUFFSTORE = "INSERT INTO zeus_buffstore VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static HashMap<L2PcInstance,Boolean>SellBuff = new HashMap<L2PcInstance,Boolean>();
	private static HashMap<Integer,Long>BuffPrice = new HashMap<Integer, Long>();
	private static HashMap<Integer,String> TitulosOriginales = new HashMap<Integer, String>();
	private static HashMap<Integer,String> TituloColorOriginal = new HashMap<Integer, String>();
	private static HashMap<Integer,String> NombreColorOriginal = new HashMap<Integer, String>();
	private static HashMap<Integer,String> WANIPVENDEDOR = new HashMap<Integer, String>();
	private static HashMap<Integer,HashMap<String,Boolean>> ConfigPersonal = new HashMap<Integer, HashMap<String,Boolean>>();
	private static HashMap<Integer,ArrayList<Skill>> PersonalBuffer = new HashMap<Integer, ArrayList<Skill>>();
	private static HashMap<Integer,L2PcInstance>SelectedCharBuffer = new HashMap<Integer, L2PcInstance>();
	
	private static HashMap<Integer,Boolean> BuffChar = new HashMap<Integer, Boolean>();
	
	public static void SetSelectedCharBuffer(int a, L2PcInstance b){
		SelectedCharBuffer.put(a, b);
	}
	
	
	private static HashMap<Integer,String> FItemRequest = new HashMap<Integer, String>();
	
	public static int getNameColorBefore(L2PcInstance player){
		try{
			if(isBuffSeller(player)){
				return Integer.valueOf(NombreColorOriginal.get(player.getObjectId()));
			}
		}catch(Exception a){
			
		}
		return player.getAppearance().getNameColor();
	}
	
	public static String getTitleBefore(L2PcInstance player){
		try{
			if(isBuffSeller(player)){
				return TitulosOriginales.get(player.getObjectId());
			}
		}catch(Exception a){
			
		}
		
		return player.getTitle();
	}
	
	public static int getTitleColorBefore(L2PcInstance player){
		try{
			if(isBuffSeller(player)){
				return Integer.valueOf(TituloColorOriginal.get(player.getObjectId()));
			}
		}catch(Exception a){
			
		}
		
		return player.getAppearance().getTitleColor();
	}
	
	private static String getRequestItemName(L2PcInstance player){
		return FItemRequest.get(player.getObjectId()).replace("_", " ");
	}
	
	private static int getRequestItemID(L2PcInstance player){
		String ItemName = FItemRequest.get(player.getObjectId());
		if(ItemName.equalsIgnoreCase("adena")){
			return 57;
		}
		
		if(general.BUFFSTORE_ITEMS_REQUEST!=null){
			for(String t : general.BUFFSTORE_ITEMS_REQUEST){
				if(t.split(":")[1].equalsIgnoreCase(ItemName)){
					return Integer.valueOf(t.split(":")[0]);
				}
			}
		}		
		
		return 57;
		
	}
	
	private static void removeDBInfo(int idPlayer){
		String DeleteInfo = "DELETE FROM zeus_buffstore WHERE idChar=?";
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(DeleteInfo))
			{
				// Remove or update a L2PcInstance skill from the character_skills table of the database
				statementt.setInt(1, idPlayer);
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}		
	}
	
	private static void removeBDInfo(L2PcInstance player){
		removeDBInfo(player.getObjectId());
	}
	
	private static void saveInfoINDB(L2PcInstance player){
		try (Connection con = ConnectionFactory.getInstance().getConnection()){
			try (PreparedStatement ps = con.prepareStatement(INSERT_ZEUS_BUFFSTORE))
			{
				ps.setInt(1, player.getObjectId());
				ps.setInt(2, getRequestItemID(player));
				ps.setDouble(3, BuffPrice.get(player.getObjectId()));
				ps.setString(4, player.getTitle());
				ps.setString(5, ConfigPersonal.get(player.getObjectId()).get("CLAN").toString());
				ps.setString(6, ConfigPersonal.get(player.getObjectId()).get("WAN").toString());
				ps.setString(7, ConfigPersonal.get(player.getObjectId()).get("FRIEND").toString());
				ps.setInt(8, player.getLocation().getX());
				ps.setInt(9, player.getLocation().getY());
				ps.setInt(10, player.getLocation().getZ());
				ps.setDouble(11, opera.getUnixTimeL2JServer());
				ps.setString(12, ZeuS.getIp_Wan(player));
				ps.execute();
			} catch(Exception a){
				}
		}catch(Exception b){
			
		}
	}
	
	public static void restoreOffline(){
		String Consulta = "SELECT * FROM zeus_buffstore";
		int contador = 0;
		try (Connection con = ConnectionFactory.getInstance().getConnection();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(Consulta))
			{
			
			while (rs.next())
			{
				
				long time = rs.getLong("time");
				if (Config.OFFLINE_MAX_DAYS > 0)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(time);
					cal.add(Calendar.DAY_OF_YEAR, Config.OFFLINE_MAX_DAYS);
					if (cal.getTimeInMillis() <= System.currentTimeMillis())
					{
						removeDBInfo(rs.getInt("idChar"));
						continue;

					}
				}
				
				L2PcInstance player = null;
				L2GameClient client = new L2GameClient(null);
				client.setDetached(true);
				player = L2PcInstance.load(rs.getInt("idChar"));
				client.setActiveChar(player);
				player.setOnlineStatus(true, false);
				client.setAccountName(player.getAccountNamePlayer());
				client.setState(GameClientState.IN_GAME);
				player.setClient(client);
				player.spawnMe(rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
				LoginServerThread.getInstance().addGameServerLogin(player.getAccountName(), client);				
				if (Config.OFFLINE_SET_NAME_COLOR)
				{
					player.getAppearance().setNameColor(Config.OFFLINE_NAME_COLOR);
				}
				player.setOnlineStatus(true, true);
				player.restoreEffects();
				player.broadcastUserInfo();
				WANIPVENDEDOR.put(player.getObjectId(), rs.getString("ipwan"));
				
				//startBuff $cmbItem $cmbBuffClan $cmbFreeConecc $cmbBuffFriend $txtPrice $txtTitulo
				
				String cmbItemRequest = central.getNombreITEMbyID(rs.getInt("idRequest")).replace(" ", "_");
				String ByClan = rs.getString("clan").equals("true") ? "Yes" : "No";
				String ByWan = rs.getString("wan").equals("true") ? "Yes" : "No";
				String ByFriend = rs.getString("friend").equals("true") ? "Yes" : "No";
				String ByPrice = String.valueOf(rs.getDouble("price")).replace(".0", "");
				String byTitulo = rs.getString("title");
				
				String byPassEnviar = "startBuff "+ cmbItemRequest + " "+ ByClan + " "+ ByWan + " "+ ByFriend +" "+ ByPrice + " " + byTitulo;
				//showMainWindows(L2PcInstance player, boolean getMainWindows,String Params)
				showMainWindows(player, false, byPassEnviar);
				contador++;
				
			}
			
		}catch(Exception a){
			
		}
		if(contador>0){
			_log.info("ZeuS -> Loading " + String.valueOf(contador) + " offline buff store");
		}
	}
	
	private static String getItemForRequest(){
		//<combobox width=90 var=\"cmbBuffClan\" list=\"No;Yes\">
		//ID_NOMBRE
		//BUFFSTORE_ITEMS_REQUEST.add(t+"_"+central.getNombreITEMbyID(Integer.valueOf(t)));
		String retorno = "";
		if(general.BUFFSTORE_ITEMS_REQUEST!=null){
			for(String t : general.BUFFSTORE_ITEMS_REQUEST){
				if(retorno.length()>0){
					retorno += ";";
				}
				retorno += t.split(":")[1];
			}
		}
		
		if(retorno.length()==0){
			retorno = "Adena";
		}
		return retorno;
	}
	
	private static void setBlockbuff(){
		if(BuffBlock!=null){
			BuffBlock.clear();
		}
		BuffBlock.add(970);
		BuffBlock.add(357);
		BuffBlock.add(1323);
		BuffBlock.add(327);
		BuffBlock.add(1325);
		BuffBlock.add(1326);
		BuffBlock.add(1327);
	}
	
	public static void showMainWindows(L2PcInstance player, boolean getMainWindows,String Params){
		
		String ByPassBtn1 = "bypass -h voice .ZeBuSell configBuff 0 0 0 0";
		String ByPassBtn2 = "bypass -h voice .ZeBuSell startBuff $cmbItem $cmbBuffClan $cmbFreeConecc $cmbBuffFriend $txtPrice $txtTitulo";
		
		String retorno = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		retorno += central.LineaDivisora(2) + central.headFormat("Buff Store Manager") + central.LineaDivisora(2);
		retorno += "<center><br>";
		
		String Grilla = "";
		
		boolean enviar = false;
		
		if(getMainWindows){
			Grilla = "<table height=250><tr><td></td>"+
	           "<td fixwidth=280 align=CENTER>"+
	               "This system Allows to you sell your <font color=LEVEL>buff</font> to another other players easily.<br1>"+
	               "It behaves almost exactly like a <font color=LEVEL>private store</font>, but <font color=LEVEL>instead of items your can sell/buy buffs</font>.<br1>"+
	               "You can set the <font color=LEVEL>personal configuration</font> like, <font color=LEVEL>price</font>, <font color=LEVEL>title</font>, and <font color=LEVEL>another features</font><br1>"+
	           "</td><td></td></tr><tr><td></td><td align=center>"+
	           "<button value=\"Create Buff Store\" action=\""+ByPassBtn1+"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\" fore=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\">"+
	           "</td><td></td></tr></table><br>";
			enviar = true;
		}else{
			if(Params.split(" ")[0].equals("configBuff")){
				Grilla = "<table width=280 background=L2UI_CT1.Windows_DF_Drawer_Bg_Darker border=0><tr><td align=CENTER>Here you can set a private Buff Store.<br>Other players will be able to purchase or get for free your buffs directly from you when clicking on you.<br>"+
	            "</td></tr>"+
				"<tr><td align=CENTER>Title:<br1></td></tr>"+
	            "<tr><td align=CENTER><multiedit var=\"txtTitulo\" width=180 height=20 ><br></td></tr>"+
				
				"<tr><td align=CENTER>Price:<br1></td></tr>"+
	            "<tr><td align=CENTER><edit var=\"txtPrice\" type=number width=180><br></td></tr>"+
				
				"<tr><td align=CENTER>Item Request:<br1></td></tr>"+
				"<tr><td align=CENTER><combobox width=190 var=\"cmbItem\" list=\""+ getItemForRequest() +"\"><br></td></tr>"+
				
				"<tr><td align=CENTER>Free buff for my Clan<br1></td></tr>"+
	            "<tr><td align=CENTER><combobox width=90 var=\"cmbBuffClan\" list=\"No;Yes\"><br></td></tr>"+
	            
				"<tr><td align=CENTER>"+
				"Free buff for my Friends<br1></td></tr><tr><td align=CENTER>"+
				"<combobox width=90 var=\"cmbBuffFriend\" list=\"No;Yes\"><br></td></tr>"+
	            
	            "<tr><td align=CENTER>"+
	            "Free buff for my IP Connection<br1>"+
	            ZeuS.getIp_Wan(player)+"<br1></td></tr><tr><td align=CENTER>"+
	            "<combobox width=90 var=\"cmbFreeConecc\" list=\"Yes;No\"><br></td></tr><tr><td align=CENTER>"+
	            "<button value=\"Start Now!\" action=\""+ ByPassBtn2 +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\" fore=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\">"+
	            "</td></tr></table><br><br>";
				enviar = true;
			}else if(Params.split(" ")[0].equals("startBuff")){
				boolean canBuffClan = false;
				boolean canBuffIPLan = false;
				boolean canBuffFriend = false;
				String ItemRequest = "";
				String Price = "100";
				String Titulo = "";
				
				if(Params.split(" ")[0].equals("startBuff")){
					ItemRequest = Params.split(" ")[1];
					canBuffClan = Params.split(" ")[2].equals("Yes");
					canBuffIPLan = Params.split(" ")[3].equals("Yes");
					canBuffFriend = Params.split(" ")[4].equals("Yes");
					Price = Params.split(" ")[5];
					for(int i=6;i<Params.split(" ").length;i++){
						Titulo += Params.split(" ")[i]+" ";
					}
					int LargoTitulo = Titulo.trim().length();
					Titulo = Titulo.substring(0, (LargoTitulo>=24 ? 23 : LargoTitulo) ).trim();
					long l = Long.parseLong(Price);
					ConfigPersonal.put(player.getObjectId(), new HashMap<String,Boolean>());
					ConfigPersonal.get(player.getObjectId()).put("CLAN", canBuffClan);
					ConfigPersonal.get(player.getObjectId()).put("WAN", canBuffIPLan);
					ConfigPersonal.get(player.getObjectId()).put("FRIEND", canBuffFriend);
					FItemRequest.put(player.getObjectId(), ItemRequest);
					TitulosOriginales.put(player.getObjectId(), player.getTitle());
					BuffPrice.put(player.getObjectId(), l);
					if(!player.getClient().isDetached()){
						WANIPVENDEDOR.put(player.getObjectId(), ZeuS.getIp_Wan(player));
					}
					setBuffSell(player,Titulo);
					//getAllBuffForSell(player);555
					retorno = "";
				}
			}else if(Params.split(" ")[0].equals("changeToBuff")){
				if(BuffChar!=null){
					if(BuffChar.containsKey(player.getObjectId())){
						boolean t = BuffChar.get(player.getObjectId());
						BuffChar.remove( player.getObjectId());
						BuffChar.put( player.getObjectId() , !t);
					}else{
						BuffChar.put( player.getObjectId() , true);
					}
				}else{
					BuffChar.put( player.getObjectId() , true);
				}
				int Pagina = Integer.valueOf(Params.split(" ")[1]);
				L2PcInstance playerBuffer = SelectedCharBuffer.get(player.getObjectId());
				if(isSellBuff(playerBuffer,true)){
					getBuffForSell(playerBuffer,player,Pagina);
					//SelectedCharBuffer.put(playerComprador.getObjectId(), player);
				}				
			}else if(Params.split(" ")[0].equals("showBuffer")){
				int Pagina = Integer.valueOf(Params.split(" ")[1]);
				L2PcInstance playerBuffer = SelectedCharBuffer.get(player.getObjectId());
				if(isSellBuff(playerBuffer,true)){
					getBuffForSell(playerBuffer,player,Pagina);
					//SelectedCharBuffer.put(playerComprador.getObjectId(), player);
				}				
			}else if(Params.split(" ")[0].equals("GiveBuff")){
				//player.doCast(GIFT_OF_VITALITY.getSkill()); player.doSimultaneousCast(JOY_OF_VITALITY.getSkill());
				int IDSKILL = 0;
				int IDLEVEL = 0;
				int PAGINA = 0;
				IDSKILL = Integer.valueOf(Params.split(" ")[1]);
				IDLEVEL = Integer.valueOf(Params.split(" ")[2]);
				PAGINA = Integer.valueOf(Params.split(" ")[3]);
				
				L2PcInstance playerBuffer = SelectedCharBuffer.get(player.getObjectId());
				giveBuff(IDSKILL,IDLEVEL,playerBuffer,player);
				
				if(isSellBuff(playerBuffer,true)){
					getBuffForSell(playerBuffer,player,PAGINA);
				}
				
				return;
			}
		}
		
		retorno += central.LineaDivisora(1) + central.headFormat(Grilla) + central.LineaDivisora(1);
		retorno += central.getPieHTML(false) + "</center></body></html>";
		if(enviar){
			central.sendHtml(player, retorno);
		}
	}
	
	private static boolean AreSameClan(L2PcInstance ppl1, L2PcInstance ppl2){
		if(ppl1.getClan()!=null && ppl2.getClan()!=null){
			if(ppl1.getClan() == ppl2.getClan()){
				return true;
			}
		}
		return false;
	}
	
	private static boolean AreFriend(L2PcInstance ppl1, L2PcInstance ppl2){
		//List<Integer> FriendsPpl = ppl1.getFriendList();
		Set<Integer> FriendsPpl = ppl1.getFriends();
		
		for(Integer Amigos : FriendsPpl){
			if(Amigos.equals(ppl2.getObjectId())){
				return true;
			}
		}
		
		return false;
	}
	
	private static void giveBuff(int IdSkill, int IdLevel, L2PcInstance Vendedor, L2PcInstance Comprador){

		boolean FreeBuffClan = ConfigPersonal.get(Vendedor.getObjectId()).get("CLAN");
		boolean FreeWAN = ConfigPersonal.get(Vendedor.getObjectId()).get("WAN");
		boolean FreeFriend = ConfigPersonal.get(Vendedor.getObjectId()).get("FRIEND");
		Long Precio = BuffPrice.get(Vendedor.getObjectId());
		
		boolean ForFree = false;
		
		boolean AreSameWan = ZeuS.getIp_Wan(Comprador).equalsIgnoreCase(WANIPVENDEDOR.get(Vendedor.getObjectId()));
		
		int IDItemToQuest = getRequestItemID(Vendedor);
		
		if(FreeBuffClan && AreSameClan(Vendedor, Comprador)){
			ForFree = true;
		}else if(FreeWAN && AreSameWan){
			ForFree = true;
		}else if(FreeFriend && AreFriend(Vendedor,Comprador)){
			ForFree = true;
		}
		if(!ForFree){
			if(!opera.haveItem(Comprador,IDItemToQuest, Precio,true)){
				return;
			}else{
				opera.removeItem(IDItemToQuest, Precio, Comprador);
				opera.giveReward(Vendedor,IDItemToQuest,Precio);
			}
		}
		
		if(!Comprador.isInsideRadius(Vendedor.getLocation(), general.RADIO_PLAYER_NPC_MAXIMO, true, true)){
			return;
		}
		
		
		boolean buffChar = BuffChar.get(Comprador.getObjectId());
		
		if(!buffChar && Comprador.getSummon()==null){
			central.msgbox("You dont have any summon to buff. For the Next time you gonna buff you Char", Comprador);
			BuffChar.remove(Comprador.getObjectId());
			BuffChar.put(Comprador.getObjectId(), true);
			return;
		}
		
		
		Skill BuffDar = SkillData.getInstance().getSkill(IdSkill, IdLevel);
		if(buffChar){
			Vendedor.setTarget(Comprador);
			SkillData.getInstance().getSkill(IdSkill, IdLevel).applyEffects(Vendedor, Comprador);
		}else{
			Vendedor.setTarget(Comprador.getSummon());
			SkillData.getInstance().getSkill(IdSkill, IdLevel).applyEffects(Vendedor, Comprador.getSummon());
		}
		Vendedor.doCast(BuffDar);
	}
	
	public static boolean isBuffSeller(L2PcInstance player){
		if(SellBuff!=null){
			if(SellBuff.containsKey(player)){
				return true;
			}
		}
		return false;
	}
	
	private static void getAllBuffForSell(L2PcInstance playerBuffer){
		
		if(PersonalBuffer!=null){
			if(PersonalBuffer.containsKey(playerBuffer.getObjectId())){
				return;
			}
		}
		
		
		
		setBlockbuff();
		
		Collection<Skill> skills = playerBuffer.getAllSkills();
        ArrayList<Skill> ba = new ArrayList<Skill>();
        
	    for(Skill s : skills){
	    	if(s == null){
	    		continue;
	    	}
	    	
	    	Vector<L2TargetType> NoPermitidos = new Vector<L2TargetType>();
	    	
	    	NoPermitidos.add(L2TargetType.SELF );
	    	NoPermitidos.add(L2TargetType.OWNER_PET);
	    	
	    	if(NoPermitidos.contains(s.getTargetType())){
	    		continue;
	    	}
	    	
	    	if(general.BUFF_STORE_BUFFPROHIBITED!=null){
	    		if(general.BUFF_STORE_BUFFPROHIBITED.size()>0){
	    			if(general.BUFF_STORE_BUFFPROHIBITED.contains(s.getId())){
	    				continue;
	    			}
	    		}
	    	}
	    	
	    	if(s.getAbnormalType() == AbnormalType.BUFF && s.isActive() &&  !BuffBlock.contains(s.getId())){
	    		ba.add(s);
	    	}
	    }
	    
	    PersonalBuffer.put(playerBuffer.getObjectId(), ba);
	}
	
	public static void setBuffSell(L2PcInstance player,String Titulo) {
		SellBuff.put(player, true);
		
		TitulosOriginales.put(player.getObjectId(), player.getTitle());
		TituloColorOriginal.put(player.getObjectId(), String.valueOf(player.getAppearance().getTitleColor()));
		NombreColorOriginal.put(player.getObjectId(), String.valueOf(player.getAppearance().getNameColor()));
		
		player.sitDown();
		player.setTitle(Titulo);
		player.getAppearance().setTitleColor(Integer.decode("0x" + ColorVendedor));
		if(!player.getClient().isDetached()){
			player.getAppearance().setNameColor(Integer.decode("0x" + ColorVendedor));
		}
		player.broadcastInfo();
		player.broadcastUserInfo();
		player.broadcastTitleInfo();
		if(!player.getClient().isDetached()){
			saveInfoINDB(player);
		}
		opera.setBlockSaveInDB(player);
	}
	
	public static void setStopSellBuff(L2PcInstance player){
		if(SellBuff!=null){
			if(SellBuff.containsKey(player)){
				try{
					SellBuff.remove(player);
					BuffPrice.remove(player.getObjectId());
					player.setTitle(TitulosOriginales.get(player.getObjectId()));
					TitulosOriginales.remove(player.getObjectId());
					
					player.getAppearance().setTitleColor(Integer.decode(TituloColorOriginal.get(player.getObjectId())));
					TituloColorOriginal.remove(player.getObjectId());
					
					player.getAppearance().setNameColor(Integer.decode(NombreColorOriginal.get(player.getObjectId())));
					NombreColorOriginal.remove(player.getObjectId());
				}catch(Exception a){
					
				}
				//player.setAntiFeedProtection(false);
			}
			
			try{
				if(PersonalBuffer!=null){
					if(PersonalBuffer.containsKey(player.getObjectId())){
						PersonalBuffer.remove(player.getObjectId());
					}
				}
			}catch(Exception a){
				
			}
			
			player.standUp();
			player.broadcastInfo();
			player.broadcastUserInfo();
			opera.setUnBlockSaveInDB(player);
		}
		
		removeBDInfo(player);
	}
	
	private static String getEnchantSkill(String enchant){
		if(enchant.length()>1){
			return enchant.substring(1, enchant.length());
		}
		return "0";
	}
	
	public static void getBuffForSell(L2PcInstance playerBuffer, L2PcInstance playerCompra, int Pagina){
		
		getAllBuffForSell(playerBuffer);
		
		
		if(!playerCompra.isInsideRadius(playerBuffer, general.RADIO_PLAYER_NPC_MAXIMO , true, true)){
			return;
		}
		
		
		if(playerBuffer==null){
			return;
		}
		
		if(playerBuffer == playerCompra){
			return;
		}
		
		if(BuffChar==null){
			BuffChar.put(playerCompra.getObjectId(), true);
		}else{
			if(!BuffChar.containsKey(playerCompra.getObjectId())){
				BuffChar.put(playerCompra.getObjectId(), true);
			}
		}
		
		boolean BuffPlayer = BuffChar.get(playerCompra.getObjectId());
		boolean haveSummon = ( playerCompra.getSummon() == null? false : true );
		
		
		if(!BuffPlayer && !haveSummon){
			BuffChar.remove(playerCompra.getObjectId());
			BuffChar.put(playerCompra.getObjectId(), true);
		}
		
		
		String btnBuffPlayer = "<button value=\"Buff Char\" width=95 height=20 action=\"bypass -h voice .ZeBuSell changeToBuff " + String.valueOf(Pagina) + " 0 0 0\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
		String btnBuffSummon = "<button value=\"Buff Summon\" width=95 height=20 action=\"bypass -h voice .ZeBuSell changeToBuff " + String.valueOf(Pagina) + " 0 0 0\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
		
		String html = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		
		html += central.LineaDivisora(2) + central.headFormat(playerBuffer.getName() + " - Buff Store") + central.LineaDivisora(2);
		html += "<center>";
		
		html += "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td align=CENTER>"+
               "Class: <font color=\"DD9B22\">"+ opera.getClassName(playerBuffer.getClassId().getId()) +"</font>  Level: <font color=\"DD9B22\">"+ String.valueOf(playerBuffer.getLevel()) +"</font>"+
       "</td></tr><tr>"+
       "<td align=CENTER>"+
       "Buff Price: <font color=\"CCFFCC\">"+ opera.getFormatNumbers(String.valueOf(BuffPrice.get(playerBuffer.getObjectId())).replace(".0", "")) +"</font><br1>"+
       "Item Request: <font color=\"CCFFCC\">"+ FItemRequest.get(playerBuffer.getObjectId()).replace("_", " ") +"</font>"+
       ( haveSummon ?  ( BuffPlayer ? btnBuffPlayer : btnBuffSummon ) + "<br1>"   : "<br1>" ) +
       "</td></tr></table>";
		
		int Contador = 0;
		int Maximo = 6;
		
		int Desde = Maximo * Pagina;
		int Hasta = Desde + Maximo;
		
		boolean haveNext=false;
		
	    for(Skill p : PersonalBuffer.get(playerBuffer.getObjectId())){
	    	String Imagen = "Icon.skill"+ opera.getIconSkill(p.getId());
	    	String ByPass = "bypass -h voice .ZeBuSell GiveBuff %IDBUFF% %IDLEVEL% " + String.valueOf(Pagina);
	    	
	    	if(!general.BUFF_STORE_BUFFPROHIBITED.contains(p.getId())){
	    		if(Contador>=Desde && Contador < Hasta){
		    		html += "<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=0><table width=280><tr><td align=CENTER fixwidth=32>"+
	                opera.getBotonForm(Imagen, ByPass.replace("%IDLEVEL%",String.valueOf(p.getLevel())).replace("%IDBUFF%", String.valueOf(p.getId()))) + "</td><td fixwidth=248>"+
	                "<font color=FAAC58>"+p.getName() + " - <font color=936028>Level</font> <font color=A9E2F3>" + String.valueOf(/*p.getLevel()*/p.getAbnormalLvl()) + "</font> <br1>"+
	                "<font color=97F776>Enchant: </font>"+ getEnchantSkill(String.valueOf(p.getLevel())) +"+</td></tr></table>";
		    	}else if(Contador>Hasta){
		    		haveNext = true;
		    	}
		    	Contador++;	    		
	    	}
	    }
	    
	    if(haveNext || Pagina>0){
	    	String ByPass_Prev = "bypass -h voice .ZeBuSell showBuffer " + String.valueOf(Pagina - 1) + " 0 0 0";
	    	String ByPass_Next = "bypass -h voice .ZeBuSell showBuffer " + String.valueOf(Pagina + 1) + " 0 0 0";
	    	
	    	String BtnPrev = "<button action=\""+ ByPass_Prev +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\">";
	    	String BtnNext = "<button action=\""+ ByPass_Next +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\">";
	    	
	    	String Controles = "<table width=280><tr>"+
            "<td fixwidth=93 align=RIGHT>" + ( Pagina>0 ? BtnPrev : "" ) +"</td><td width=93 align=CENTER >"+
            "<font name=\"hs12\" color=76F7D1>"+ String.valueOf(Pagina + 1) +"</font></td>"+
            "<td fixwidth=93 align=LEFT>" + (haveNext ? BtnNext : "") + "</td></tr></table>";
	    	html += Controles;
	    }
	    
	    html += central.getPieHTML(false) + "</center></body></html>";
		central.sendHtml(playerCompra, html);
	}
	
	public static boolean isSellBuff(L2PcInstance playerBuyer){
		return isSellBuff(playerBuyer,false);
	}
	
	public static boolean isSellBuff(L2PcInstance playerBuyer, boolean isVendor){
		
		L2PcInstance playerSell = null;
		if(!isVendor){
			if(playerBuyer.getTarget()==null){
				return false;
			}
			
			L2Object TargetObject = playerBuyer.getTarget();
			
			if(!(TargetObject instanceof L2PcInstance)){
				return false;
			}
			playerSell = (L2PcInstance) TargetObject;
		}else{
			playerSell = playerBuyer;
		}
		
		if(SellBuff!=null){
			if(SellBuff.containsKey(playerSell)){
				return true;
			}
		}
		return false;
	}
	
	public static void ByPass(L2PcInstance player, String Params, L2PcInstance playerComprador){
		if(isSellBuff(player)){
			SelectedCharBuffer.put(playerComprador.getObjectId(), player);
			getBuffForSell(player,playerComprador,0);
		}
	}
	
}
