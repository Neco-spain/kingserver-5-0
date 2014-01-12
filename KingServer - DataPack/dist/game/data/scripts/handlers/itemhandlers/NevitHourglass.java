package handlers.itemhandlers;

import king.server.gameserver.model.actor.L2Playable;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.SystemMessage;

public class NevitHourglass extends ItemSkills {
        
        @Override
        public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
        {
                
                if (!(playable instanceof L2PcInstance))
                {
                        playable.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ITEM_NOT_FOR_PETS));
                        return false;
                }
                
                L2PcInstance activeChar = (L2PcInstance)playable;
                if( activeChar.RecoBonusActive() )
                {
                        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
                        sm.addItemName(item.getItemId());
                        activeChar.sendPacket(sm);
                        return false;
                }
                
                return super.useItem(playable, item, forceUse);
        }
        

}