package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import king.server.Config;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.handler.IAdminCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.CrazyRates;


public class AdminCrazy implements IAdminCommandHandler
{
 private static String[] _adminCommands =
 {
 "admin_crazy_event"
 };

 private enum CommandEnum
 {
 admin_crazy_event
 }

 public void run()
 {
 CrazyRates.EventManager(); //Caminho da class que executara o evento
 }

 @Override
 public boolean useAdminCommand(String command, L2PcInstance activeChar)
 {
 /*
 if(!AdminCommandAccessRights.getInstance().hasAccess(command, activeChar.getAccessLevel())){
 return false;
 }

 if(Config.GMAUDIT)
 {
 Logger _logAudit = Logger.getLogger("gmaudit");
 LogRecord record = new LogRecord(Level.INFO, command);
 record.setParameters(new Object[]
 {
 "GM: " + activeChar.getName(), " to target [" + activeChar.getTarget() + "] "
 });
 _logAudit.log(record);
 }
 */

 StringTokenizer st = new StringTokenizer(command);

 CommandEnum comm = CommandEnum.valueOf(st.nextToken());

 if(comm == null)
 return false;
 long flush2 = 0;

 switch(comm){
 case admin_crazy_event:{

 boolean no_token = false;

 if(st.hasMoreTokens()){ //char_name not specified
 String doublerate = st.nextToken();
 int rate = Integer.parseInt(doublerate);
 Config.RateMultipler = rate;
 /**if(1==1) */
 {

 if (st.hasMoreTokens()) //time
 {
 String time = st.nextToken();

 try{
 int value = Integer.parseInt(time);
 Config.time_crazyrate = value;

 ThreadPoolManager.getInstance().scheduleGeneral(new StartEvent(), flush2);


 if(value>0){


 }else{

 activeChar.sendMessage("Time must be bigger then 0!");
 return false;
 }

 }catch(NumberFormatException e){
 activeChar.sendMessage("Time must be a number!");
 return false;
 }

 }else{
 no_token = true;
 }

 }

 }else{

 no_token=true;

 }

 if(no_token){
 activeChar.sendMessage("Usage: //crazy_event <rate Multiplique> [time](In Minutes)");
 return false;
 }

 }
 }

 return true;

 }
 public class StartEvent implements Runnable
 {

  @Override
  public void run()
  {
  CrazyRates.EventManager();

  }

 }
 
 @Override
 public String[] getAdminCommandList()
 {
 return _adminCommands;
 }
}
