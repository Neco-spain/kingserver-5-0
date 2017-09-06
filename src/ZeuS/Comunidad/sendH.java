package ZeuS.Comunidad;

import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;


public class sendH extends L2GameServerPacket{
	public static void hideComm(){
		
	}

	@Override
	protected void writeImpl() {
		/*Comunidad Cerrar*/
		writeC(0x7B);
		writeC(0x00);
	}
}
