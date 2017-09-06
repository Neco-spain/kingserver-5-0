package ZeuS.dressme;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.script.Invocable;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.L2Armor;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.L2Weapon;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.items.type.ArmorType;
import com.l2jserver.gameserver.model.items.type.WeaponType;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;

import ZeuS.Config.general;
import ZeuS.Config.msg;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;



public class dressme {

	private final Logger _log = Logger.getLogger(GameServer.class.getName());
	  //As of freya there are 19 weapon types.
	  private  final int totalWeaponTypes = 19;
	  //As of freya there are 6 armor types.
	  private  final int totalArmorTypes = 6;
	  private final boolean[][] weaponMapping = new boolean[totalWeaponTypes][totalWeaponTypes];
	  private final boolean[][] armorMapping = new boolean[totalArmorTypes][totalArmorTypes];
	  private final String CadenaDressme = "CHEST=%CH%;LEGS=%LGS%;GLOVES=%GLO%;FEET=%FEET%;RHAND=%RH%;LHAND=%LH%;CLOAK=%CLO%";
	  private final int maxDressme = 8;
	  private final String SQL_INSERT = "call sp_dressme(?,?,?,?)";//tipo, idchar,dressposition,sql(Texto)

	  public dressme(){
		  generateMappings();
	  }

	  public String getClassName(L2PcInstance player){
		  return central.getClassName(player, player.getBaseClass());
	  }

		public boolean OlyUserClassNameInCharName(){
			return general.OLY_ANTIFEED_SHOW_IN_NAME_CLASS;
		}

	      private boolean canUserItemInDressme(int idItem,L2PcInstance cha,boolean showMensaje){
	    	  if(general.getDressmeTargetBlockList() !=null){
	    		  if(general.getDressmeTargetBlockList().contains(idItem)){
	    			  central.msgbox("Invalid Dressme Objet: " + central.getNombreITEMbyID(idItem), cha);
	    			  return false;
	    		  }
	    	  }
	    	  return true;
	      }


	  private void reconstruirDressmeToBd(L2PcInstance player, int idDressme){
		  try{
			  HashMap<String, Integer> DMP = general.DRESSME_PLAYER.get(player).get(idDressme);
			  String CadenaEnviar = CadenaDressme.replace("%CH%", String.valueOf(DMP.get("CHEST")));
			  CadenaEnviar = CadenaEnviar.replace("%LGS%", String.valueOf(DMP.get("LEGS")));
			  CadenaEnviar = CadenaEnviar.replace("%GLO%", String.valueOf(DMP.get("GLOVES")));
			  CadenaEnviar = CadenaEnviar.replace("%FEET%", String.valueOf(DMP.get("FEET")));
			  CadenaEnviar = CadenaEnviar.replace("%RH%", String.valueOf(DMP.get("RHAND")));
			  CadenaEnviar = CadenaEnviar.replace("%LH%", String.valueOf(DMP.get("LHAND")));
			  CadenaEnviar = CadenaEnviar.replace("%CLO%", "0");
			  saveInBD(1, player, idDressme, CadenaEnviar);
		  }catch(Exception a){

		  }
	  }

	  public void setNewItemToDressme(L2PcInstance player, String Seccion, int idDressme, int IdItemNuevo){
		  try{
			  general.DRESSME_PLAYER.get(player).get(idDressme).put(Seccion, IdItemNuevo);
			  reconstruirDressmeToBd(player,idDressme);
			  updateCharacter(player);
		  }catch(Exception a){

		  }
	  }

	  private void generateMappings(){
	        for(int i =0; i< weaponMapping.length; i++) {
		  	for(int j = 0; j< weaponMapping.length; j++) {
		  		weaponMapping[i][j]=false;
		  	}
		  }

	        for(int i =0; i< armorMapping.length; i++) {
		  	for(int j = 0; j< armorMapping.length; j++) {
		  		armorMapping[i][j]=false;
		  	}
		  }
	        callRules();
	    }

	  private  void callRules(){
	        //Example: a Virtual sword can mount an Equipped blunt.
	        weaponMapping[WeaponType.SWORD.ordinal()][WeaponType.BLUNT.ordinal()] = true;

	        //Example: a Virtual blunt can mount an Equipped sword.
	        weaponMapping[WeaponType.BLUNT.ordinal()][WeaponType.SWORD.ordinal()] = true;

	        weaponMapping[WeaponType.SWORD.ordinal()][WeaponType.BLUNT.ordinal()] = true;
	        weaponMapping[WeaponType.BLUNT.ordinal()][WeaponType.SWORD.ordinal()] = true;

	        armorMapping[ArmorType.SIGIL.ordinal()][ArmorType.SHIELD.ordinal()] = true;
	        armorMapping[ArmorType.SHIELD.ordinal()][ArmorType.SIGIL.ordinal()] = true;

	        armorMapping[ArmorType.HEAVY.ordinal()][ArmorType.LIGHT.ordinal()] = true;
	        armorMapping[ArmorType.HEAVY.ordinal()][ArmorType.MAGIC.ordinal()] = true;

	        armorMapping[ArmorType.LIGHT.ordinal()][ArmorType.HEAVY.ordinal()] = true;
	        armorMapping[ArmorType.LIGHT.ordinal()][ArmorType.MAGIC.ordinal()] = true;

	        armorMapping[ArmorType.MAGIC.ordinal()][ArmorType.LIGHT.ordinal()] = true;
	        armorMapping[ArmorType.MAGIC.ordinal()][ArmorType.HEAVY.ordinal()] = true;
	    }

	    /**
	      * Checks if the weapon is the same type. If that is true then return
	      * the matching virtual id. Else check the mapping tables if any
	      * rule states that the two different weapon types should be matched.
	      * @param virtual
	      * @param equiped
	      * @param matchId
	      * @param noMatchId
	      * @return
	      */

	  public void dressMe_See(L2PcInstance player, int ToSee){
		  getWindowsDressme(player,false,ToSee);
	  }

	  public void dressMe_See(L2PcInstance player, int ToSee, boolean target){

    	  if(!general.DRESSME_CAN_CHANGE_IN_OLYS || !general.DRESSME_CAN_USE_IN_OLYS){
    		  if(player.isNoble()){
	    			if(player.isOlympiadStart() || player.isInOlympiadMode()){
	    				return;
	    			}
	    		}
    	  }

		  if(target){
			  getWindowsDressmeTarget(player,String.valueOf(ToSee));
		  }else{
			  getWindowsDressme(player,false,ToSee);
		  }
	  }

	  private int getIDUsingItem(L2PcInstance player, int Parte){
		  int Retorno = -1;
		  if(player.getInventory().getPaperdollItem(Parte)!= null){
			  Retorno = player.getInventory().getPaperdollItem(Parte).getItem().getId();
			  /**IMPORTANtE**/
			  /**
			   * Para L2Reunion se debe cambiar a
			   * Retorno = player.getInventory().getPaperdollItem(Parte).getItem().getDisplayId();
			   *
			   * */
		  }
		  return Retorno;
	  }

	  private boolean isEnabledDressmeID(L2PcInstance player, int idDressme){
		  if(general.DRESSME_PLAYER.containsKey(player)){
			  return general.DRESSME_PLAYER.get(player).get(idDressme).size()>0;
		  }
		  return false;
	  }

	  private int getIDDressmeUsingPlayer(L2PcInstance player){
		  if(general.DRESSME_PLAYER.containsKey(player)){
			  return general.DRESSME_PLAYER.get(player).get(9).get("USED");
		  }else{
			  return 0;
		  }
	  }

	  private int countDressmeInPlayer(L2PcInstance player){
		  int ContadorDressme = 0;

		  if(general.DRESSME_PLAYER.containsKey(player)){
			  for(int i=1;i<=8;i++){
				  if(general.DRESSME_PLAYER.get(player).get(i).size()>0){
					  ContadorDressme++;
				  }
			  }
		  }

		  return ContadorDressme;
	  }

	  private boolean haveDressmeInPlayer(L2PcInstance player){
		  boolean retorno = false;
		  if(general.DRESSME_PLAYER.containsKey(player)){
			  for(int i=1;i<=8;i++){
				  if( general.DRESSME_PLAYER.get(player).get(i).size()>0 ){
					  return true;
				  }
			  }
		  }

		  return retorno;
	  }

	  public void setItemToDressmeFromTarget(L2PcInstance player, String Params){
		  //"CHEST=%CH%;LEGS=%LGS%;GLOVES=%GLO%;FEET=%FEET%;RHAND=%RH%;LHAND=%LH%;CLOAK=%CLO%";
		  if(Params==null){
			  return;
		  }

		  int IDItem=0, IDDressme=0;
		  String ParteIncor = "";
		  if(Params.split(" ").length==3){
			  IDItem = Integer.valueOf(Params.split(" ")[1]);
			  IDDressme = Integer.valueOf(Params.split(" ")[2]);
			  ParteIncor = Params.split(" ")[0];
		  }

		  
	    	if(!canUserItemInDressme(IDItem,player,true)){
	    		IDItem=0;
	    		return;
	    	}
		  
		  String CreatedNewDressme = CadenaDressme.replace("%CH%", "0").replace("%GLO%", "0").replace("%FEET%", "0").replace("%RH%", "0").replace("%LH%", "0").replace("%CLO%", "0");
		  if(!haveDressmeInPlayer(player)){
			  if(!general.DRESSME_NEW_DRESS_IS_FREE){
				  if(opera.haveItem(player, general.DRESSME_NEW_DRESS_COST)){
					  saveInBD(1, player, IDDressme, CreatedNewDressme);
					  getDressmePlayer(player);
					  updateCharacter(player);
				  }else{
					  return;
				  }
			  }
		  }else{
			  if(!isEnabledDressmeID(player, IDDressme)){
				  if(!general.DRESSME_NEW_DRESS_IS_FREE){
					  if(opera.haveItem(player, general.DRESSME_NEW_DRESS_COST)){
						  saveInBD(1, player, IDDressme, CreatedNewDressme);
						  getDressmePlayer(player);
						  updateCharacter(player);
					  }else{
						  return;
					  }
				  }
			  }else{
				  setNewItemToDressme(player, ParteIncor, IDDressme, IDItem);
				  central.msgbox(msg.DRESSME_PART_FROM_YOU_TARGET_IS_NOW_ADDED_TO_YOU_DRESSME, player);
			  }
		  }
	  }

	  public void setAllDressmeFromTarger(L2PcInstance player, String Params){
		  String[] DressSplit = Params.split("-");
		  if(!general.DRESSME_TARGET_STATUS){
			  return;
		  }

		  if(!general.DRESSME_PLAYER.containsKey(player)){
			  return;
		  }
		  int DressmeIDActual = getIDDressmeUsingPlayer(player);
		  if(!general.DRESSME_PLAYER.get(player).containsKey(DressmeIDActual)){
			  return;
		  }
		  HashMap<String, Integer> DressmeToChange = general.DRESSME_PLAYER.get(player).get(DressmeIDActual);
		  /*
		  DressmeToChange.put("CHEST", Integer.valueOf(DressSplit[0]));
		  DressmeToChange.put("LEGS", Integer.valueOf(DressSplit[1]));
		  DressmeToChange.put("GLOVES", Integer.valueOf(DressSplit[2]));
		  DressmeToChange.put("FEET", Integer.valueOf(DressSplit[3]));
		  DressmeToChange.put("RHAND", Integer.valueOf(DressSplit[4]));
		  DressmeToChange.put("LHAND", Integer.valueOf(DressSplit[5]));
		  */

		  int idChest, idLegs, idGloves, idFeet, idRhand, idLhand;

		  idChest = Integer.valueOf(DressSplit[0]);
		  idLegs = Integer.valueOf(DressSplit[1]);
		  idGloves = Integer.valueOf(DressSplit[2]);
		  idFeet = Integer.valueOf(DressSplit[3]);
		  idRhand = Integer.valueOf(DressSplit[4]);
		  idLhand = Integer.valueOf(DressSplit[5]);

		  if(!canUserItemInDressme(idChest, player, true)){
			  idChest = 0;
		  }
		  if(!canUserItemInDressme(idLegs, player, true)){
			  idLegs = 0;
		  }
		  if(!canUserItemInDressme(idGloves, player, true)){
			  idGloves = 0;
		  }
		  if(!canUserItemInDressme(idFeet, player, true)){
			  idFeet = 0;
		  }
		  if(!canUserItemInDressme(idRhand, player, true)){
			  idRhand = 0;
		  }
		  if(!canUserItemInDressme(idLhand, player, true)){
			  idLhand = 0;
		  }

		  DressmeToChange.put("CHEST", idChest);
		  DressmeToChange.put("LEGS", idLegs);
		  DressmeToChange.put("GLOVES", idGloves);
		  DressmeToChange.put("FEET", idFeet);
		  DressmeToChange.put("RHAND", idRhand);
		  DressmeToChange.put("LHAND", idLhand);


		  reconstruirDressmeToBd(player,DressmeIDActual);
		  updateCharacter(player);
		  central.msgbox(msg.DRESSME_TARGET_SAVE_IT_INTO_DRESSME_ID_$id.replace("$id", String.valueOf(DressmeIDActual)), player);
	  }

	  private void getWindowsDressmeTarget(L2PcInstance player, String selectedDressmeId){

		  if(!general.DRESSME_TARGET_STATUS){
			  central.msgbox(msg.DRESSME_TARGET_COMMAND_IS_DISABLED_BY_GM, player);
			  return;
		  }

		  L2Object targetChaObj = player.getTarget();
		  if(!(targetChaObj instanceof L2PcInstance)){
			  central.msgbox(msg.THE_SELECT_OBJECT_IS_NOT_A_PLAYER, player);
			  return;
		  }

		  L2PcInstance playerTarget = (L2PcInstance)targetChaObj;

		  if(player.equals(playerTarget)){
			  return;
		  }

		  if(!general.DRESSME_SHARE.get(playerTarget)){
			  central.msgbox(msg.DRESSME_TARGET_THE_TARGET_PLAYER_$targetName_NOT_SHARE_THEY_DRESSME.replace("$targetName", playerTarget.getName()),player);
			  central.msgbox_Lado(msg.DRESSME_TARGET_THE_PLAYER_$name_WANTS_YOU_DRESSME.replace("$name", player.getName()), playerTarget,"DRESSME");
			  return;
		  }

		  String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		  HTML += central.LineaDivisora(1) + central.headFormat("Get Dressme Target") + central.LineaDivisora(1);

		  if(!haveDressmeInPlayer(player)){
			  String btnPutThis = "<button value=\"Go to Dressme Menu\" action=\"bypass -h voice .dressme\" width=160 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			  HTML += central.LineaDivisora(1) + central.headFormat(msg.DRESSME_YOU_NEED_TO_HAVE_ANY_DRESSME_ADDED+btnPutThis,"LEVEL")+ central.LineaDivisora(1);
			  if(!general.DRESSME_NEW_DRESS_IS_FREE){
				  HTML += central.LineaDivisora(1) + central.headFormat(msg.DRESSME_THE_COST_FOR_NEW_DRESSME);
				  HTML += central.ItemNeedShowBox(general.DRESSME_NEW_DRESS_COST) + central.LineaDivisora(1);
			  }
			  HTML += central.getPieHTML(false) + "</body></html>";
			  central.sendHtml(player, HTML);
			  return;
		  }

		  int idDressmeUsingTarget = getIDDressmeUsingPlayer(playerTarget);
		  int idDressmeUsingMe = getIDDressmeUsingPlayer(player);

		  if(idDressmeUsingMe==0){
			  HTML += central.LineaDivisora(1) + central.headFormat(msg.DRESSME_YOU_NEED_TO_PUT_ON_A_DRESSME,"LEVEL") + central.LineaDivisora(1);
			  HTML += central.getPieHTML(false) + "</body></html>";
			  central.sendHtml(player, HTML);
			  return;
		  }

		  //isEnabledDressmeID

		  String btnPutThis = "<button value=\"Add Part\" action=\"bypass -h voice .dressme_target_indi %PARTE% %ID_COPIA% "+ String.valueOf(idDressmeUsingMe) + "\" width=90 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		  String NoItem = ItemTable.getInstance().getTemplate(3883).getIcon();
		  String Chest=null,Legs=null,Gloves=null,Feet=null,Rhand=null,Lhand=null;
		  int ChestID = 0, LegsID = 0, GlovesID = 0, FeetID = 0, RhandID = 0, LhandID = 0;
		  HTML += central.LineaDivisora(1) + central.headFormat("<font color=LEVEL>You are looking at the<br1>"+playerTarget.getName()+"<br1>Dressme.</font><br1>") + central.LineaDivisora(1);
		  HTML += central.LineaDivisora(1) + central.headFormat("You're using the DressMe ID <font color=LEVEL>" + String.valueOf(idDressmeUsingMe) + "</font>" + central.LineaDivisora(1));
		  if(haveDressmeInPlayer(playerTarget)){
			  if( idDressmeUsingTarget > 0){
				  HashMap<String, Integer> dressmeUsing = general.DRESSME_PLAYER.get(playerTarget).get(idDressmeUsingTarget);
				  ChestID = dressmeUsing.get("CHEST");
				  LegsID = dressmeUsing.get("LEGS");
				  GlovesID = dressmeUsing.get("GLOVES");
				  FeetID = dressmeUsing.get("FEET");
				  RhandID = dressmeUsing.get("RHAND");
				  LhandID = dressmeUsing.get("LHAND");
			  }else{
				  ChestID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_CHEST);
				  LegsID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_LEGS);
				  GlovesID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_GLOVES);
				  FeetID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_FEET);
				  RhandID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_RHAND);
				  LhandID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_LHAND);
			  }
		  }else{
			  ChestID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_CHEST);
			  LegsID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_LEGS);
			  GlovesID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_GLOVES);
			  FeetID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_FEET);
			  RhandID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_RHAND);
			  LhandID = getIDUsingItem(playerTarget,Inventory.PAPERDOLL_LHAND);
		  }

		  String AllIdSet = String.valueOf(ChestID < 0 ? 0 : ChestID)+"-"+
				  			String.valueOf(LegsID < 0 ? 0 : LegsID)+"-"+
				  			String.valueOf(GlovesID < 0 ? 0 : GlovesID)+"-"+
				  			String.valueOf(FeetID < 0 ? 0 : FeetID)+"-"+
				  			String.valueOf(RhandID < 0 ? 0 : RhandID)+"-"+
				  			String.valueOf(LhandID < 0 ? 0 : LhandID);

		  Chest = opera.getTemplateItem(ChestID);
		  Legs = opera.getTemplateItem(LegsID);
		  Gloves = opera.getTemplateItem(GlovesID);
		  Feet = opera.getTemplateItem(FeetID);
		  Rhand = opera.getTemplateItem(RhandID);
		  Lhand = opera.getTemplateItem(LhandID);

		  String ActualChest="", ActualLegs="", ActualGloves="", ActualFeet="", ActualRhand="", ActualLhand="";
		  //%PARTE% %ID_COPIA%
		  if(Chest!=null){
			  ActualChest = btnPutThis.replace("%PARTE%", "CHEST").replace("%ID_COPIA%", String.valueOf(ChestID));
		  }
		  if(Legs!=null){
			  ActualLegs = btnPutThis.replace("%PARTE%", "LEGS").replace("%ID_COPIA%", String.valueOf(LegsID));
		  }
		  if(Gloves!=null){
			  ActualGloves = btnPutThis.replace("%PARTE%", "GLOVES").replace("%ID_COPIA%", String.valueOf(GlovesID));
		  }
		  if(Feet!=null){
			  ActualFeet = btnPutThis.replace("%PARTE%", "FEET").replace("%ID_COPIA%", String.valueOf(FeetID));
		  }
		  if(Rhand!=null){
			  ActualRhand = btnPutThis.replace("%PARTE%", "RHAND").replace("%ID_COPIA%", String.valueOf(RhandID));
		  }
		  if(Lhand!=null){
			  ActualLhand = btnPutThis.replace("%PARTE%", "LHAND").replace("%ID_COPIA%", String.valueOf(LhandID));
		  }

		  String btnGetAllDressme = "<button value=\"Get all Dressme\" action=\"bypass -h voice .dressmeGetAllSet "+ AllIdSet +"\" width=160 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		  String PARA_ITEM = "<table with=280>";
		  PARA_ITEM += "<tr><td width=92 align=CENTER>Chest</td><td width=92 align=CENTER>Legs</td><td width=92 align=CENTER>Gloves</td></tr>";
		  PARA_ITEM += "<tr><td width=92 align=CENTER><img src="+ ( Chest==null ? NoItem : Chest ) +" width=32 height=32></td>";
		  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Legs==null ? NoItem : Legs ) +" width=32 height=32></td>";
		  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Gloves==null ? NoItem : Gloves ) +" width=32 height=32></td></tr>";
		  PARA_ITEM += "<tr><td width=92>"+ ActualChest +"</td><td width=92>"+ ActualLegs +"</td><td width=92>"+ ActualGloves +"</td></tr>";
		  PARA_ITEM += "<tr><td width=92></td><td width=92></td><td width=92></td></tr>";

		  PARA_ITEM += "<tr><td width=92 align=CENTER>Feet</td><td width=92 align=CENTER>R. Hand</td><td width=92 align=CENTER>L. Hand</td></tr>";
		  PARA_ITEM += "<tr><td width=92 align=CENTER><img src="+ ( Feet==null ? NoItem : Feet ) +" width=32 height=32></td>";
		  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Rhand==null ? NoItem : Rhand ) +" width=32 height=32></td>";
		  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Lhand==null ? NoItem : Lhand ) +" width=32 height=32></td></tr>";
		  PARA_ITEM += "<tr><td width=92>"+ ActualFeet +"</td><td width=92>"+ ActualRhand +"</td><td width=92>"+ ActualLhand +"</td></tr></table>";
		  PARA_ITEM += "<br1>"+btnGetAllDressme;
		  HTML += central.LineaDivisora(1) + central.headFormat(PARA_ITEM+"<br>") + central.LineaDivisora(1);

		  HTML += "<br1>"+central.BotonGOBACKZEUS()+central.getPieHTML(false)+"</center></body></html>";

		  central.sendHtml(player, HTML);
	  }

	  private void getWindowsDressme(L2PcInstance player, boolean CopyTarger, int DressmeToShow){
		  if(!general.DRESSME_STATUS){
			  central.msgbox("Dressme command disabled by GM", player);
			  return;
		  }
		  String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		  HTML += central.LineaDivisora(1) + central.headFormat("Dressme Configuration") + central.LineaDivisora(1);
		  String COMBO_Dressme = "<combobox width=38 var=cmbIdDressme list=" + DressmeToShow;
		  String btnCrearNuevoDressme = "<button value=\"Created Dressme\" action=\"bypass -h voice .dressme_save $cmbIdDressme\" width=150 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		  String btnSeeDressme = "<button value=\"See Dressme\" action=\"bypass -h voice .dressme $cmbIdDressme\" width=150 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		  String btnChooseDressme = "<button value=\"Choose This Dressme\" action=\"bypass -h voice .dressme_choose $cmbIdDressme\" width=150 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		  String btnDisabledDressme = "<button value=\"Disabled Dressme\" action=\"bypass -h voice .dressme_choose 0\" width=150 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		  String btnPutThis = "<button value=\"Add Actual\" action=\"bypass -h voice .dress_add_indi $parte $nuevoId "+ String.valueOf(DressmeToShow) +"\" width=90 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		  for(int i=1;i<=8;i++){
			  if(DressmeToShow!=i){
				  COMBO_Dressme += ";" + String.valueOf(i);
			  }
		  }
		  COMBO_Dressme += ">";

		  HTML += central.LineaDivisora(1) + central.headFormat("Select dressme"+COMBO_Dressme+btnSeeDressme+"<br>")+central.LineaDivisora(1);

		  //int[] VectorLocaciones = {Inventory.PAPERDOLL_HEAD, Inventory.PAPERDOLL_CHEST, Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_RHAND, Inventory.PAPERDOLL_LHAND, Inventory.PAPERDOLL_UNDER, Inventory.PAPERDOLL_BELT, Inventory.PAPERDOLL_LEAR,Inventory.PAPERDOLL_REAR, Inventory.PAPERDOLL_LFINGER, Inventory.PAPERDOLL_RFINGER, Inventory.PAPERDOLL_NECK, Inventory.PAPERDOLL_LBRACELET };

		  boolean dressmeSelectedEnabled = general.DRESSME_PLAYER.get(player).get(DressmeToShow).size()>0;
		  boolean haveDressme = general.DRESSME_PLAYER.get(player).containsKey(1);
		  if(haveDressme){
			  if(dressmeSelectedEnabled){
				  HashMap<String, Integer> dressmeToSee = general.DRESSME_PLAYER.get(player).get(DressmeToShow);
				  String NoItem = ItemTable.getInstance().getTemplate(3883).getIcon();
				  String Chest = opera.getTemplateItem(dressmeToSee.get("CHEST"));
				  String Legs = opera.getTemplateItem(dressmeToSee.get("LEGS"));
				  String Gloves = opera.getTemplateItem(dressmeToSee.get("GLOVES"));
				  String Feet = opera.getTemplateItem(dressmeToSee.get("FEET"));
				  String Rhand = opera.getTemplateItem(dressmeToSee.get("RHAND"));
				  String Lhand = opera.getTemplateItem(dressmeToSee.get("LHAND"));

				  String ActualChest = (String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_CHEST)).equals("-1") ? "" : btnPutThis.replace("$parte", "CHEST").replace("$nuevoId", String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_CHEST))));
				  String ActualLegs = (String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_LEGS)).equals("-1") ? "" : btnPutThis.replace("$parte", "LEGS").replace("$nuevoId", String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_LEGS))));
				  String ActualGloves = (String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_GLOVES)).equals("-1") ? "" : btnPutThis.replace("$parte", "GLOVES").replace("$nuevoId", String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_GLOVES))));
				  String ActualFeet = (String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_FEET)).equals("-1") ? "" : btnPutThis.replace("$parte", "FEET").replace("$nuevoId", String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_FEET))));
				  String ActualRhand = (String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_RHAND)).equals("-1") ? "" : btnPutThis.replace("$parte", "RHAND").replace("$nuevoId", String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_RHAND))));
				  String ActualLhand = (String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_LHAND)).equals("-1") ? "" : btnPutThis.replace("$parte", "LHAND").replace("$nuevoId", String.valueOf(getIDUsingItem(player,Inventory.PAPERDOLL_LHAND))));

				  String PARA_ITEM = "<table with=280>";
				  PARA_ITEM += "<tr><td width=92 align=CENTER>Chest</td><td width=92 align=CENTER>Legs</td><td width=92 align=CENTER>Gloves</td></tr>";
				  PARA_ITEM += "<tr><td width=92 align=CENTER><img src="+ ( Chest==null ? NoItem : Chest ) +" width=32 height=32></td>";
				  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Legs==null ? NoItem : Legs ) +" width=32 height=32></td>";
				  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Gloves==null ? NoItem : Gloves ) +" width=32 height=32></td></tr>";
				  PARA_ITEM += "<tr><td width=92>"+ ActualChest +"</td><td width=92>"+ ActualLegs +"</td><td width=92>"+ ActualGloves +"</td></tr>";
				  PARA_ITEM += "<tr><td width=92></td><td width=92></td><td width=92></td></tr>";

				  PARA_ITEM += "<tr><td width=92 align=CENTER>Feet</td><td width=92 align=CENTER>R. Hand</td><td width=92 align=CENTER>L. Hand</td></tr>";
				  PARA_ITEM += "<tr><td width=92 align=CENTER><img src="+ ( Feet==null ? NoItem : Feet ) +" width=32 height=32></td>";
				  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Rhand==null ? NoItem : Rhand ) +" width=32 height=32></td>";
				  PARA_ITEM += "<td width=92 align=CENTER><img src="+ ( Lhand==null ? NoItem : Lhand ) +" width=32 height=32></td></tr>";
				  PARA_ITEM += "<tr><td width=92>"+ ActualFeet +"</td><td width=92>"+ ActualRhand +"</td><td width=92>"+ ActualLhand +"</td></tr></table>";
				  HTML += central.LineaDivisora(1) + central.headFormat(PARA_ITEM+btnChooseDressme+btnDisabledDressme+"<br>") + central.LineaDivisora(1);
			  }else{
				  HTML += central.LineaDivisora(1) + central.headFormat("Dressme ID Empty" + btnCrearNuevoDressme+btnDisabledDressme+"<br>") + central.LineaDivisora(1);
			  }
		  }else{
			  HTML += central.LineaDivisora(1) + central.headFormat(msg.DRESSME_YOU_DONT_HAVE_DRESSME_CONFIG,"LEVEL") + central.LineaDivisora(1);
		  }

		  HTML += central.LineaDivisora(1) + central.headFormat(msg.DRESSME_HOW_TO_USED_$dressmeSave_$dressmeChoose_$dressme_$dressmeCopy.replace("$dressmeSave", ".dressme_save").replace("$dressmeChoose", ".dressme_choose").replace("$dressme", ".dressme").replace("$dressmeCopy", ".dressme_share") ,"LEVEL");

		  if(!general.DRESSME_NEW_DRESS_IS_FREE){
			  HTML += central.LineaDivisora(1) + central.headFormat(msg.DRESSME_THE_COST_FOR_NEW_DRESSME) + central.ItemNeedShowBox(general.DRESSME_NEW_DRESS_COST) +central.LineaDivisora(1);
		  }


		  HTML += "<br1>"+central.BotonGOBACKZEUS()+central.getPieHTML(false)+"</center></body></html>";

		  opera.enviarHTML(player, HTML);
	  }

	  private boolean shareDressme(L2PcInstance player){
		  return general.DRESSME_SHARE.get(player);
	  }

	  public void setShareDressme(L2PcInstance player){
		  if(general.DRESSME_SHARE.get(player)){
			  general.DRESSME_SHARE.put(player, false);
			  central.msgbox_Lado("Dressme share is now OFF", player, "DRESSME");
		  }else{
			  general.DRESSME_SHARE.put(player, true);
			  central.msgbox_Lado("Dressme share is now ON", player, "DRESSME");
		  }
	  }

	  public void setNewConfigDressme(){
		  if(!general.DRESSME_STATUS){
		    Iterator itr = general.DRESSME_PLAYER.entrySet().iterator();
		    while(itr.hasNext()){
		  	  Map.Entry Player = (Map.Entry)itr.next();
		  	  L2PcInstance PlayerChar = (L2PcInstance)Player.getKey();
		  	  if(general.DRESSME_PLAYER.get(PlayerChar).containsKey(9)){
		  		  if(!general.DRESSME_PLAYER.get(PlayerChar).get(9).get("USED").equals(0)){
		        	  saveInBD(3,PlayerChar, 9, "0");
		        	  setUsingDressme(PlayerChar,0);
	        	  	updateCharacter(PlayerChar);
	        	  	PlayerChar.sendMessage("Your original clothes are activated by the game master. Dressme Disabled for all user");
		  		  }
		  	  }
		    }
		  }
	  }

	  private int weaponMatching(L2PcInstance player, WeaponType virtual, WeaponType equiped, int matchId, int noMatchId){
	    	if(general.DRESSME_PLAYER.get(player).get(9).get("USED").equals(0)){
	    		return noMatchId;
	    	}

	    	if(!general.DRESSME_CAN_USE_IN_OLYS){
	    		if(player.isNoble()){
	    			if(player.isOlympiadStart() || player.isInOlympiadMode()){
	    				return noMatchId;
	    			}
	    		}
	    	}

	        if(virtual == equiped) {
		  	return matchId;
		  }

	        if(weaponMapping[virtual.ordinal()][equiped.ordinal()] == true)
	        {
	            return matchId;
	        }

	        return noMatchId;
	    }
	      /**
	        * Checks if the armor is the same type. If that is true then return
	        * the matching virtual id. Else check the mapping tables if any
	        * rule states that the two different armor types should be matched.
	        * @param virtual
	        * @param equiped
	        * @param matchId
	        * @param noMatchId
	        * @return
	        */
	      private  int armorMatching(L2PcInstance player, ArmorType virtual, ArmorType equiped, int matchId , int noMatchId){
		    	if(general.DRESSME_PLAYER.get(player).get(9).get("USED").equals(0)){
		    		return noMatchId;
		    	}

		    	if(!general.DRESSME_CAN_USE_IN_OLYS){
		    		if(player.isNoble()){
		    			if(player.isOlympiadStart() || player.isInOlympiadMode()){
		    				return noMatchId;
		    			}
		    		}
		    	}


	          if(virtual == equiped){
	        	  return matchId;
	          }


	          if(armorMapping[virtual.ordinal()][equiped.ordinal()] == true){
		  	return matchId;
	          }
	          return noMatchId;
	      }
	      public void setAllNewDreesme(L2PcInstance playerInstance, boolean isTarget, int PositionDress){
	    	int armaR, armaL, glove, chest, legs, feet,cloak;
	    	armaR = playerInstance.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND);
	    	armaL = playerInstance.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND);
	    	chest = playerInstance.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST);
	    	glove = playerInstance.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES);
	    	legs = getVirtualPantsID(playerInstance);
	    	feet = playerInstance.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET);
	    	cloak = playerInstance.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CLOAK);
	    	//private final String CadenaDressme = "CHEST=%CH%;LEGS=%LGS%;GLOVES=%GLO%;FEET=%FEET%;RHAND=%RH%;LHAND=%LH%;CLOAK=%CLO%";

	    	/*
	    	if(!canUserItemInDressme(armaR,playerInstance,true)){
	    		armaR=0;
	    	}
	    	if(!canUserItemInDressme(armaL,playerInstance,true)){
	    		armaL=0;
	    	}
	    	if(!canUserItemInDressme(chest,playerInstance,true)){
	    		chest=0;
	    	}
	    	if(!canUserItemInDressme(glove,playerInstance,true)){
	    		glove=0;
	    	}
	    	if(!canUserItemInDressme(legs,playerInstance,true)){
	    		legs=0;
	    	}
	    	if(!canUserItemInDressme(feet,playerInstance,true)){
	    		feet=0;
	    	}
	    	if(!canUserItemInDressme(cloak,playerInstance,true)){
	    		cloak=0;
	    	}*/


	    	String toSave = CadenaDressme;
	    	toSave = toSave.replace("%CH%", String.valueOf(chest)).replace("%LGS%", String.valueOf(legs)).replace("%GLO%", String.valueOf(glove)).replace("%FEET%", String.valueOf(feet)).replace("%RH%", String.valueOf(armaR)).replace("%LH%", String.valueOf(armaL)).replace("%CLO%", String.valueOf(cloak));
	    	saveInBD(1,playerInstance, PositionDress, toSave);

	      }

	      private boolean saveInBD(int tipo, L2PcInstance player, int DressPosition, String SQLString){
	    	  boolean retorno = true;
	    	  //SQL_INSERT = "call sp_dressme(?,?,?,?)";//tipo, idchar,dressposition,sql(Texto)
	    	Connection conn = null;
	  		try {
	  		  conn = ConnectionFactory.getInstance().getConnection();
	  		  CallableStatement psqry = conn.prepareCall(SQL_INSERT);
	  		  psqry.setInt(1,tipo);
	  		  psqry.setInt(2,player.getObjectId());
	  		  psqry.setInt(3,DressPosition);
	  		  psqry.setString(4, SQLString);
	  		  ResultSet rss = psqry.executeQuery();
	  		  while (rss.next()){
	  		  	if(rss.getString(1).equals("err")) {
	  		  		retorno=false;
	  		  	}
	  		  }
	  		} catch (SQLException e) {
	  		  e.printStackTrace();
	  		}

	  		try{
	  		  conn.close();
	  		}catch(Exception a){

	  		}

	    	  return retorno;
	      }

	      private int getIDItem_dressme(L2PcInstance player, String parte){
	    	  int retorno = 0;
	    	  try{
	    		  int Usando = general.DRESSME_PLAYER.get(player).get(9).get("USED");
	    		  retorno = general.DRESSME_PLAYER.get(player).get(Usando).get(parte);
	    	  }catch(Exception a){

	    	  }
	    	  return retorno;
	      }


	      private  int getVirtualPantsID(L2PcInstance actor){
	    	  int Retorno = 0;
		      int chestId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST);
		      int pantsId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS);
		      if((chestId != 0) && (pantsId==0)){
		    	  Retorno = chestId;
		      }else{
		    	  Retorno = pantsId;
		      }
		      return Retorno;
	      }
	      
	      
	      public int getVirtualSlot(L2PcInstance player, int idSlot){
	    	  switch(idSlot){
	    	  	case Inventory.PAPERDOLL_GLOVES:
	    	  		return getVirtualGloves(player);
	    	  	case Inventory.PAPERDOLL_CHEST:
	    	  		return getVirtualChest(player);
	    	  	case Inventory.PAPERDOLL_LEGS:
	    	  		return getVirtualLegs(player);
	    	  	case Inventory.PAPERDOLL_FEET:
	    	  		return getVirtualFeets(player);
	    	  	case Inventory.PAPERDOLL_CLOAK:
	    	  		return getCloak(player);
	    	  	case Inventory.PAPERDOLL_RHAND:
	    	  		return getRHAND(player);
	    	  	case Inventory.PAPERDOLL_LHAND:
	    	  		return getLHAND(player);
	    	  }
	    	  return -100;
	      }
	      
	      
		  //TODO: Merge the armor getters in one function.
		  public int getVirtualGloves(L2PcInstance player){
			  L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
		      if(equipedItem == null){
		    	  return 0;
		      }
		      //ClassCastException wont happen unless some jackass changes the values from the database.
		      try{
			      L2Armor equipedGloves = (L2Armor)player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES).getItem();
			      int equipedGlovesId = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES);
			      int glovesTextureId = getIDItem_dressme(player,"GLOVES");
			      L2Armor virtualGloves = (L2Armor)ItemTable.getInstance().getTemplate(glovesTextureId);
			      if(glovesTextureId != 0){
			    	  return armorMatching(player,virtualGloves.getItemType(), equipedGloves.getItemType(),glovesTextureId, equipedGlovesId);
			      }else{
			    	  return equipedGlovesId;
			      }
		      }catch(Exception a){
		    	  return player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES);
		      }
		  }

	          public int getVirtualChest(L2PcInstance player){
	              L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
	              if(equipedItem == null){
	            	  return 0;
	              }
	              //ClassCastException wont happen unless some jackass changes the values from the database.
	              try{
		              L2Armor equipedChest = (L2Armor)player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST).getItem();
		              int equipedChestId = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST);
		              int chestTextureId = getIDItem_dressme(player,"CHEST");
		              L2Armor virtualChest = (L2Armor)ItemTable.getInstance().getTemplate(chestTextureId);
		              int retorno = 0;
		              if(chestTextureId != 0) {
		            	  retorno = armorMatching(player,virtualChest.getItemType(), equipedChest.getItemType(),chestTextureId, equipedChestId);
		              }else{
		            	  retorno = equipedChestId;
		              }
		              return retorno;
	              }catch(Exception a){
	            	  return player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST);
	              }
	          }

	          /**
	            * This is a brain fu**er handling the pants since they are
	            * also part of a fullbody armor.
	            * @param actor
	            * @return
	            */
	          public int getVirtualLegs(L2PcInstance player){
	        	  try{
		              L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEGS);
		              //Here comes the tricky part. If pants are null, then check for a fullbody armor.
		              if(equipedItem == null){
		            	  equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
		              }
		              if(equipedItem == null){
		            	  return 0;
		              }
		              int pantsTextureId = getIDItem_dressme(player,"LEGS");
		              L2Armor equipedPants = (L2Armor) equipedItem.getItem();
		              if((equipedPants.getBodyPart() != L2Item.SLOT_FULL_ARMOR) && (equipedPants.getBodyPart() != L2Item.SLOT_LEGS)){
		            	  return 0;
		              }
		              int equipedPantsId = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS);
		              L2Armor virtualPants = (L2Armor)ItemTable.getInstance().getTemplate(pantsTextureId);

		              if(pantsTextureId != 0){
		            	  return armorMatching(player,virtualPants.getItemType(), equipedPants.getItemType(),pantsTextureId, equipedPantsId);
		              }else{
		            	  return equipedPantsId;
		              }
	        	  }catch(Exception a){
	        		  return player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS);
	        	  }
	          }

	          public int getVirtualFeets(L2PcInstance player){
	              L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET);
	              if(equipedItem == null){
	            	  return 0;
	              }
	              try{
	              //ClassCastException wont happen unless some jackass changes the values from the database.
	              L2Armor equipedBoots = (L2Armor)player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET).getItem();
	              int equipedBootsId = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET);
	              int bootsTextureId = getIDItem_dressme(player,"FEET");
	              L2Armor virtualGloves = (L2Armor)ItemTable.getInstance().getTemplate(bootsTextureId);
	              if(bootsTextureId != 0){
	            	  return armorMatching(player,virtualGloves.getItemType(), equipedBoots.getItemType(),bootsTextureId, equipedBootsId);
	              }else{
	            	  return equipedBootsId;
	              }
	              }catch(Exception a){
	            	  return player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET);
	              }
	          }

	          public int getLHAND(L2PcInstance player)
	          {
	              L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
	              int equipedItemId = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND);

	              int weaponLHANDId = getIDItem_dressme(player,"LHAND");
	              L2Item virtualItem = ItemTable.getInstance().getTemplate(weaponLHANDId);

	              if((equipedItem == null) || (weaponLHANDId == 0)) {
		  		return equipedItemId;
	              }

	              //Only check same weapon types. Virtual replacement should not happen between armor/weapons.
	              if((equipedItem.getItem() instanceof L2Weapon) && (virtualItem instanceof L2Weapon))
	              {
	                  L2Weapon weapon = (L2Weapon) equipedItem.getItem();
	                  L2Weapon virtualweapon = (L2Weapon)virtualItem;

	                  return weaponMatching(player,virtualweapon.getItemType(), weapon.getItemType(), weaponLHANDId, equipedItemId);
	              }
	              else if((equipedItem.getItem() instanceof L2Armor) && (virtualItem instanceof L2Armor))
	              {
	                  L2Armor armor = (L2Armor) equipedItem.getItem();
	                  L2Armor virtualarmor = (L2Armor)virtualItem;

	                  return armorMatching(player,virtualarmor.getItemType(), armor.getItemType(), weaponLHANDId, equipedItemId);
	              }
	              return equipedItemId;

	          }

	          private void checkIsDressmeAreLoad(L2PcInstance player){
	        	  if(general.DRESSME_PLAYER.containsKey(player)){
	        		  return;
	        	  }

	        	  getDressmePlayer(player);

	          }

	          public int getRHAND(L2PcInstance player)
	          {
	        	  checkIsDressmeAreLoad(player);
	              int weaponRHANDId = getIDItem_dressme(player,"RHAND");
	              L2ItemInstance equipedItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
	              int equipedItemId = player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND);
	              L2Item virtualItem = ItemTable.getInstance().getTemplate(weaponRHANDId);

	              if((equipedItem == null) || (weaponRHANDId == 0)) {
		  		return equipedItemId;
		  	}

	              //Only check same weapon types. Virtual replacement should not happen between armor/weapons.
	              if((equipedItem.getItem() instanceof L2Weapon) && (virtualItem instanceof L2Weapon))
	              {
	                  L2Weapon weapon = (L2Weapon) equipedItem.getItem();
	                  L2Weapon virtualweapon = (L2Weapon)virtualItem;

	                  return weaponMatching(player,virtualweapon.getItemType(), weapon.getItemType(), weaponRHANDId, equipedItemId);
	              }
	              else if((equipedItem.getItem() instanceof L2Armor) && (virtualItem instanceof L2Armor))
	              {
	                  L2Armor armor = (L2Armor) equipedItem.getItem();
	                  L2Armor virtualarmor = (L2Armor)virtualItem;

	                  return armorMatching(player,virtualarmor.getItemType(), armor.getItemType(), weaponRHANDId, equipedItemId);
	              }
	              return equipedItemId;
	          }



	          public int getCloak(L2PcInstance player)
	          {
	              if(getIDItem_dressme(player, "LHAND") == 1) {
		  		return 0;
		  	} else {
		  		return player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CLOAK);
		  	}

	          }

	          public void getDressmePlayer(L2PcInstance player){
	        	  int idChar = player.getObjectId();
	    	    	Connection conn = null;

	    	    	if(!general.DRESSME_SHARE.containsKey(player)){
	    	    		general.DRESSME_SHARE.put(player, false);
	    	    	}
	    	    	general.DRESSME_SHARE.put(player, false);

	    	  		try {
	    	  		  conn = ConnectionFactory.getInstance().getConnection();
	    	  		  CallableStatement psqry = conn.prepareCall(SQL_INSERT);
	    	  		  psqry.setInt(1,2);
	    	  		  psqry.setInt(2,idChar);
	    	  		  psqry.setInt(3,-1);
	    	  		  psqry.setString(4, "");
	    	  		  ResultSet rss = psqry.executeQuery();
	    	  		  if(rss.next()){
	    	  		  	if(!rss.getString(1).equals("err")){
		    	  		  	for(int i=1;i<=8;i++){
		    	  		  		if(!general.DRESSME_PLAYER.containsKey(player)){
		    	  		  		  general.DRESSME_PLAYER.put(player, new HashMap<Integer,HashMap<String,Integer>>());
		    	  		  		}
		    	  		  		if(!general.DRESSME_PLAYER.get(player).containsKey(i)){
		    	  		  		  general.DRESSME_PLAYER.get(player).put(i, new HashMap<String,Integer>());
		    	  		  		}

		    	  		  		if(rss.getString(i).length()>0){
			      	  		  		for(String Cadena : rss.getString(i).split(";")){
			      	  		  		  general.DRESSME_PLAYER.get(player).get(i).put(Cadena.split("=")[0], Integer.valueOf(Cadena.split("=")[1]));
			      	  		  		}
		    	  		  		}
		    	  		  	}
		    	  		  	if(!general.DRESSME_PLAYER.get(player).containsKey(9)){
		    	  		  		general.DRESSME_PLAYER.get(player).put(9, new HashMap<String,Integer>());
		    	  		  	}
		    	  		  	if(general.DRESSME_STATUS){
		    	  		  		general.DRESSME_PLAYER.get(player).get(9).put("USED", rss.getInt(9));
		    	  		  	}else{
		    	  		  		general.DRESSME_PLAYER.get(player).get(9).put("USED", 0);
		    	  		  	}
	    	  		  	}
	    	  		  }
	    	  		} catch (SQLException e) {
	    	  		  e.printStackTrace();
	    	  		}

	    	  		try{
	    	  		  conn.close();
	    	  		}catch(Exception a){

	    	  		}
	          }

	          private void setUsingDressme(L2PcInstance player, int DressmeID){
	        	  if(!general.DRESSME_PLAYER.containsKey(player)){
	        		  general.DRESSME_PLAYER.put(player, new HashMap<Integer,HashMap<String,Integer>>());
	        	  }
	        	  if(!general.DRESSME_PLAYER.get(player).containsKey(9)){
	        		  general.DRESSME_PLAYER.get(player).put(9, new HashMap<String,Integer>());
	        	  }
	        	  general.DRESSME_PLAYER.get(player).get(9).put("USED", DressmeID);
	          }

	          public void dressme_choose(String Parametro,L2PcInstance player){

	        	  if(!general.DRESSME_CAN_CHANGE_IN_OLYS || !general.DRESSME_CAN_USE_IN_OLYS){
	        		  if(player.isNoble()){
	    	    			if(player.isOlympiadStart() || player.isInOlympiadMode()){
	    	    				return;
	    	    			}
	    	    		}
	        	  }

	        	  if(!general.DRESSME_STATUS){
	        		  central.msgbox(msg.DRESSME_DISABLED, player);
	        		  return;
	        	  }
	        	  if(!opera.isNumeric(Parametro)){
	        	  central.msgbox(msg.DRESSME_ONLY_NUMERIC_TO_SHOW_DRESSME, player);
	        	  return;
	        	  }
	        	  int dressmeSelector = Integer.valueOf(Parametro);
	        	  if((dressmeSelector > maxDressme) || (dressmeSelector < 0)){
	        	  central.msgbox(msg.DRESSME_ONLY_HAVE_$maximo_TO_CHOOSE.replace("$maximo",String.valueOf(maxDressme)), player);
	        	  return;
	        	  }

	        	  if(dressmeSelector==0){
	        	  saveInBD(3,player, 9, "0");
	        	  setUsingDressme(player,0);
        	  		  updateCharacter(player);
        	  		  player.sendMessage("Your original clothes are active");
        	  		  return;
	        	  }

	        	  try{
	        	  if(general.DRESSME_PLAYER.get(player).get(dressmeSelector).size()>0){
		        	  saveInBD(3,player, 9, String.valueOf(dressmeSelector));
		        	  setUsingDressme(player,dressmeSelector);
	        	  	updateCharacter(player);
	        	  	player.sendMessage("You've changed your clothes to ID " + Parametro);
	        	  	return;
	        	  }else{
	        	  	player.sendMessage("The selected clothes ID is empty " + Parametro);
	        	  	return;
	        	  }
	        	  }catch(Exception a){
	        	  central.msgbox(msg.DRESSME_CHOOSE_WRONG, player);
	        	  }

	          }

	          public void dressMe_command(L2PcInstance activeChar, String Parametro){
	        	  if(!general.DRESSME_STATUS){
	        		  central.msgbox(msg.DRESSME_DISABLED, activeChar);
	        		  return;
	        	  }

	        	  if(!general.DRESSME_CAN_CHANGE_IN_OLYS || !general.DRESSME_CAN_USE_IN_OLYS){
	        		  if(activeChar.isNoble()){
	    	    			if(activeChar.isOlympiadStart() || activeChar.isInOlympiadMode()){
	    	    				return;
	    	    			}
	    	    		}
	        	  }
	        	  
	        	  if(Parametro==null){
	        		  return;
	        	  }

	        	  if(!opera.isNumeric(Parametro)){
	        		  central.msgbox(msg.DRESSME_ONLY_NUMERIC_TO_SHOW_DRESSME, activeChar);
	        		  return;
	        	  }

	        	  int dressmeSelector = Integer.valueOf(Parametro);

	        	  if((dressmeSelector > maxDressme) || (dressmeSelector < 0)){
		        	  central.msgbox(msg.DRESSME_ONLY_HAVE_$maximo_TO_CHOOSE.replace("$maximo",String.valueOf(maxDressme)), activeChar);
		        	  return;
	        	  }
	        	  if(!general.DRESSME_NEW_DRESS_IS_FREE){
	        		  if(general.DRESSME_PLAYER.get(activeChar).get(dressmeSelector).size()<=0){
	        			  if(!opera.haveItem(activeChar, general.DRESSME_NEW_DRESS_COST)){
	        				  central.msgbox("You dont have the necesary item to create a new Dressme", activeChar);
	        				  return;
	        			  }
	        		  }
	        	  }
	        	  setAllNewDreesme(activeChar, false, dressmeSelector);
	        	  getDressmePlayer(activeChar);
	        	  updateCharacter(activeChar);
	        	  if(!general.DRESSME_NEW_DRESS_IS_FREE){
	        		  opera.removeItem(general.DRESSME_NEW_DRESS_COST, activeChar);
	        	  }
	              activeChar.sendMessage("You clothes are save. You ID for this Clothes are " + Parametro);
	            }
	          public void updateCharacter(L2PcInstance activeChar){
	                InventoryUpdate iu = new InventoryUpdate();
	                activeChar.sendPacket(iu);
	                activeChar.broadcastUserInfo();
	                InventoryUpdate iu2 = new InventoryUpdate();
	                activeChar.sendPacket(iu2);
	                activeChar.broadcastUserInfo();
	          }
	      	public static dressme getInstance()
	    	{
	    	    return SingletonHolder._instance;
	    	}


	    	@SuppressWarnings("synthetic-access")
	    	private static class SingletonHolder{
	    	    protected static final dressme _instance = new dressme();
	    	}
}
