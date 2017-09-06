package ZeuS.Comunidad;

import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;

public class sendC extends L2GameServerPacket{
	public static void hideComm(){
		
	}

	@Override
	protected void writeImpl() {
		/*Cerrar Tutorial*/
		writeC(0xa9);
	}
}