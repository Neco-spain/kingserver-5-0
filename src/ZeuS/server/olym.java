package ZeuS.server;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import java.util.logging.Logger;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;

public class olym
{
  private static olym _instance;
  private static final Logger _log = Logger.getLogger(GameServer.class.getName());
  
  public static void _Anun_oponent(L2PcInstance _playerOne, L2PcInstance _playerTwo)
  {
    String TitleMensajeClase = "[Opp. Class]";
    String TitleMensajeNom = "[Opp. Name]";
    if (!general._activated()) {
      return;
    }
    if ((!general.ANNOUCE_CLASS_OPONENT_OLY) && (!general.OLY_ANTIFEED_SHOW_NAME_OPPO)) {
      return;
    }
    CreatureSay cs = null;
    
    String C_O_1 = central.getClassName(_playerOne, _playerOne.getBaseClass());
    String C_O_2 = central.getClassName(_playerOne, _playerTwo.getBaseClass());
    try
    {
      if (general.ANNOUCE_CLASS_OPONENT_OLY)
      {
        cs = new CreatureSay(0, 15, TitleMensajeClase, C_O_2);
        _playerOne.sendPacket(cs);
      }
      if (general.OLY_ANTIFEED_SHOW_NAME_OPPO)
      {
        cs = new CreatureSay(0, 15, TitleMensajeNom, _playerTwo.getName());
        _playerOne.sendPacket(cs);
      }
    }
    catch (Exception a)
    {
      _log.warning("ANNOUCEMENTE OPONENTE->Error anunciar el Oponente del Player" + String.valueOf(_playerTwo.getObjectId()));
    }
    try
    {
      if (general.ANNOUCE_CLASS_OPONENT_OLY)
      {
        cs = new CreatureSay(0, 15, TitleMensajeClase, C_O_1);
        _playerTwo.sendPacket(cs);
      }
      if (general.OLY_ANTIFEED_SHOW_NAME_OPPO)
      {
        cs = new CreatureSay(0, 15, TitleMensajeNom, _playerOne.getName());
        _playerTwo.sendPacket(cs);
      }
    }
    catch (Exception a)
    {
      _log.warning("ANNOUCEMENTE OPONENTE->Error anunciar el Oponente del Player" + String.valueOf(_playerOne.getObjectId()));
    }
  }
  
  public static final olym getInstance()
  {
    return _instance;
  }
}