package king.server.gameserver.network.serverpackets;

import king.server.gameserver.network.NpcStringId;

public abstract class NpcStringContainer extends L2GameServerPacket
{
   private final NpcStringId _npcString;
   private final String[] _parameters = new String[5];

   protected NpcStringContainer(NpcStringId npcString, String... arg)
   {
       _npcString = npcString;
       System.arraycopy(arg, 0, _parameters, 0, arg.length);
   }

   protected void writeElements()
   {
       writeD(_npcString.getId());
       for(String st : _parameters)
       {
           writeS(st);
       }
   }
}