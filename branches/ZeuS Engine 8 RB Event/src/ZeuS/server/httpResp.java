package ZeuS.server;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.logging.Logger;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class httpResp
{
  private static Logger _log = Logger.getLogger(httpResp.class.getName());
  public static boolean isConect = true;
  private static int VOTOSHOPZONE = 0;
  private static int VOTOSTOPZONE = 0;
  
  public static String getInfoSerial()
  {
    return getAnswerSerial();
  }
  
  public static int getTopZoneVote()
  {
    return VOTOSTOPZONE;
  }
  
  public static int getHopZoneVote()
  {
    return VOTOSHOPZONE;
  }
  
  public static int getVoteTopZoneNow()
  {
    int NewVoteCounter = -1;
    try
    {
      InputStreamReader isr = null;
      BufferedReader br = null;
      URLConnection con = new URL(general.WEB_TOP_ZONE_SERVER).openConnection();
      con.addRequestProperty("User-Agent", "L2TopZone");
      isr = new InputStreamReader(con.getInputStream());
      br = new BufferedReader(isr);
      boolean got = false;
      String line;
      while ((line = br.readLine()) != null) {
        if ((line.contains("<i class=\"fa fa-fw fa-lg fa-thumbs-up\">")) && (!got))
        {
          got = true;
          int votes = Integer.valueOf(line.split("</i>")[1].replace("</span></small></h3>", "")).intValue();
          VOTOSTOPZONE = votes;
          NewVoteCounter = votes;
          got = true;
        }
      }
      br.close();
      isr.close();
      if (got) {
        return NewVoteCounter;
      }
    }
    catch (Exception e)
    {
      _log.warning("ZeuS Error-> Get Topzone Votes-> " + e.getMessage());
    }
    VOTOSTOPZONE = -1;
    return NewVoteCounter;
  }
  
  public static boolean haveVoteTopZone()
  {
    if (!general.VOTO_REWARD_ACTIVO_TOPZONE)
    {
      VOTOSTOPZONE = -1;
      return false;
    }
    try
    {
      InputStreamReader isr = null;
      BufferedReader br = null;
      URLConnection con = new URL(general.WEB_TOP_ZONE_SERVER).openConnection();
      con.addRequestProperty("User-Agent", "L2TopZone");
      isr = new InputStreamReader(con.getInputStream());
      br = new BufferedReader(isr);
      boolean got = false;
      String line;
      while ((line = br.readLine()) != null) {
        if ((line.contains("<i class=\"fa fa-fw fa-lg fa-thumbs-up\">")) && (!got))
        {
          got = true;
          int votes = Integer.valueOf(line.split("</i>")[1].replace("</span></small></h3>", "")).intValue();
          VOTOSTOPZONE = votes;
          got = true;
        }
      }
      br.close();
      isr.close();
      if (got) {
        return true;
      }
    }
    catch (Exception e)
    {
      _log.warning("ZeuS Error-> Get Topzone Votes-> " + e.getMessage());
    }
    VOTOSTOPZONE = -1;
    return false;
  }
  
  public static int getHopZoneVoteNow()
  {
    String Split = "<div class=\"serv-view float-right\">";
    try
    {
      InputStreamReader isr = null;
      BufferedReader br = null;
      
      String hopzoneUrl = general.WEB_HOP_ZONE_SERVER;
      boolean got = false;
      int NewVoteCounter = -1;
      if (!hopzoneUrl.endsWith(".html")) {
        hopzoneUrl = hopzoneUrl + ".html";
      }
      URLConnection con = new URL("http://google.cl").openConnection();
      isr = new InputStreamReader(con.getInputStream());
      br = new BufferedReader(isr);
      
      con = new URL(hopzoneUrl).openConnection();
      con.addRequestProperty("User-L2Hopzone", "Mozilla/4.76");
      isr = new InputStreamReader(con.getInputStream());
      br = new BufferedReader(isr);
      
      String line = "";
      while ((line = br.readLine()) != null) {
        if ((line.contains("no steal make love")) || (line.contains("no votes here")) || (line.contains("bang, you don't have votes")) || (line.contains("la vita e bella")))
        {
          int votes = Integer.valueOf(line.split(">")[2].replace("</span", "")).intValue();
          NewVoteCounter = votes;
          VOTOSHOPZONE = votes;
          got = true;
        }
      }
      br.close();
      isr.close();
      if (got) {
        return NewVoteCounter;
      }
    }
    catch (IOException e)
    {
      _log.warning("ZeuS Error-> Get Hopzone Votes-> " + e.getMessage());
    }
    return -1;
  }
  
  public static boolean haveVoteHopZone()
  {
    if (!general.VOTO_REWARD_ACTIVO_HOPZONE)
    {
      VOTOSHOPZONE = -1;
      return false;
    }
    String Split = "<div class=\"serv-view float-right\">";
    try
    {
      InputStreamReader isr = null;
      BufferedReader br = null;
      
      String hopzoneUrl = general.WEB_HOP_ZONE_SERVER;
      boolean got = false;
      if (!hopzoneUrl.endsWith(".html")) {
        hopzoneUrl = hopzoneUrl + ".html";
      }
      URLConnection con = new URL(hopzoneUrl).openConnection();
      con.addRequestProperty("User-L2Hopzone", "Mozilla/4.76");
      isr = new InputStreamReader(con.getInputStream());
      br = new BufferedReader(isr);
      String line;
      while ((line = br.readLine()) != null) {
        if ((line.contains("no steal make love")) || (line.contains("no votes here")) || (line.contains("bang, you don't have votes")) || (line.contains("la vita e bella")))
        {
          int votes = Integer.valueOf(line.split(">")[2].replace("</span", "")).intValue();
          VOTOSHOPZONE = votes;
          got = true;
        }
      }
      br.close();
      isr.close();
      if (got) {
        return true;
      }
    }
    catch (IOException e)
    {
      _log.warning("ZeuS Error-> Get Hopzone Votes-> " + e.getMessage());
    }
    VOTOSHOPZONE = -1;
    return false;
  }
  
  public static boolean sendEmailDonation(L2PcInstance player, String IDDonacionFromBD)
  {
    int idServer = opera.getServerID(player);
    String WebPage = general.GET_NAME_VAR_DIR_WEB + "?" + general.GET_NAME_VAR_TYPE + "=SEND_EMAIL_DONATION&" + general.GET_NAME_VAR_IDDONACION + "=" + IDDonacionFromBD + "&" + general.GET_NAME_VAR_SERVER_ID + "=" + String.valueOf(idServer);
    _log.warning(WebPage);
    try
    {
      URL oracle = new URL(WebPage);
      URLConnection yc = oracle.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
      
      String Respuesta = "";
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        Respuesta = Respuesta + inputLine.trim();
      }
      in.close();
      Respuesta = Respuesta.trim();
      if (Respuesta.indexOf("COR;DONATION") > 0) {
        return true;
      }
    }
    catch (IOException e)
    {
      _log.warning("httpResponce error: " + e + "  Web:" + WebPage);
      return false;
    }
    return true;
  }
  
  public static boolean sendCodeForRegistration(String Email, String Code, L2PcInstance player)
  {
    central.msgbox("Sending email to " + Email + ". Please wait a moment.", player);
    String WebPage = general.GET_NAME_VAR_DIR_WEB + "?" + general.GET_NAME_VAR_TYPE + "=SEND_EMAIL_REGISTER_WITH_CODE&" + general.GET_NAME_VAR_EMAIL + "=" + Email + "&" + general.GET_NAME_VAR_CODE + "=" + Code;
    try
    {
      URL oracle = new URL(WebPage);
      URLConnection yc = oracle.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
      
      String Respuesta = "";
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        Respuesta = Respuesta + inputLine.trim();
      }
      in.close();
      Respuesta = Respuesta.trim();
      if (Respuesta.length() > 2) {
        try
        {
          if (Respuesta.indexOf("COR;REGISTER") > 0)
          {
            central.msgbox("The email was sent to " + Email + ". Check it", player);
          }
          else
          {
            central.msgbox("The email was not sent to " + Email + ".", player);
            return false;
          }
        }
        catch (Exception a)
        {
          return false;
        }
      }
    }
    catch (IOException e)
    {
      _log.warning("httpResponce error: " + e + "  Web:" + WebPage);
    }
    return true;
  }
 /*
  public static void getPJPlus()
  {
    general.PLUS_C.clear();
    String Respuesta = "";
    URLConnection yc;
    BufferedReader in;
    try
    {
      URL oracle = new URL("http://184.107.204.162/~sitionet/ZeuS.php?c=0");
      yc = oracle.openConnection();
      in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
      
      Respuesta = "";
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        Respuesta = Respuesta + inputLine.trim();
      }
      in.close();
      Respuesta = Respuesta.trim();
      isConect = true;
    }
    catch (IOException a) {}catch (Exception e) {}
    if (Respuesta.length() > 10)
    {
      e = Respuesta.split(";");yc = e.length;
      for (in = 0; in < yc; in++)
      {
        String prim = e[in];
        String[] Seg = prim.split(",");
        if (Seg[0].equals("IP"))
        {
          general.IP_PLUS_C = Seg[1];
        }
        else
        {
          if (!general.PLUS_C.containsKey(Seg[0])) {
            general.PLUS_C.put(Seg[0], new HashMap<>());
          }
          ((HashMap)general.PLUS_C.get(Seg[0])).put("ACC", Seg[0]);
          ((HashMap)general.PLUS_C.get(Seg[0])).put("CHAR", Seg[1]);
        }
      }
    }
  }
 */
  protected static String getAnswerSerial()
  {
    //String serial = general.getSerial();
    //String Respuesta = "false";
	  String Respuesta;
    //if (serial.length() == 0) {
    //  return "false";
    //}
   // try
   // {
   //   URL oracle = new URL("http://184.107.204.162/~sitionet/ZeuS.php?s=" + serial);
    //  URLConnection yc = oracle.openConnection();
     // BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
     // 
     // Respuesta = "";
    //  String inputLine;
    //  while ((inputLine = in.readLine()) != null) {
     //   Respuesta = Respuesta + inputLine.trim();
     // }
    //  in.close();
     // Respuesta = Respuesta.trim();
      isConect = true;
   // }
   // catch (IOException a)
   // {
   //   _log.warning(a.getMessage());
   //   isConect = false;
   //   Reconect();
    //  return "false";
   // }
   // catch (Exception e)
   // {
   //   isConect = false;
   //   _log.warning(e.getMessage());
   //   Reconect();
   //   return "false";
   // }
   // return Respuesta;
      return Respuesta = "true";
  }
  /*
  private static void Reconect()
  {
    _log.warning(":::::: ZeuS server connection Fail, retry in 60 second ::::::");
    ThreadPoolManager.getInstance().scheduleGeneral(new AutoCheck(), 60000L);
  }
  
  public static final boolean SendToWeb(String WebPage)
  {
    try
    {
      URL oracle = new URL(WebPage);
      URLConnection yc = oracle.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
      
      String Respuesta = "";
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        Respuesta = Respuesta + inputLine.trim();
      }
      in.close();
      Respuesta = Respuesta.trim();
      return Boolean.valueOf(Respuesta).booleanValue();
    }
    catch (IOException e)
    {
      _log.warning("httpResponce error: " + e + "  Web:" + WebPage);
    }
    return false;
  }
 */
  public static final int getVotes_SINUSO(int webCapturar)
  {
    try
    {
      URL oracle = new URL("http://p-venta.cl/pv/getvotos.php?w=" + String.valueOf(webCapturar));
      URLConnection yc = oracle.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
      
      String inputLine = in.readLine();
      in.close();
      int Retorno = -1;
      try
      {
        Retorno = Integer.valueOf(inputLine).intValue();
      }
      catch (Exception a)
      {
        _log.warning("Vote Problem " + a.getMessage());
      }
      return Retorno;
    }
    catch (IOException e)
    {
      _log.warning("AutoVoteRewardHandler: " + e);
    }
    return 0;
  }
 /* 
  private static class AutoCheck
    implements Runnable
  {
    public void run()
    {
      String fakeAnswer = httpResp.getAnswerSerial();
      httpResp._log.warning(":::::: try connect to ZeuS Server. ::::::");
      if ((httpResp.isConect) && (fakeAnswer.equals("false"))) {
        httpResp._log.warning(":::::: Wrog ZeuS Serial ::::::");
      } else if ((httpResp.isConect) && (fakeAnswer.equals("true"))) {
        general.loadConfigs();
      }
    }
  }
  */
  public static httpResp getInstance()
  {
    return SingletonHolder._instance;
  }
  
  private static class SingletonHolder
  {
    protected static final httpResp _instance = new httpResp();
  }
}