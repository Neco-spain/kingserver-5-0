package king.server.gameserver.network.serverpackets;

import king.server.gameserver.network.serverpackets.L2GameServerPacket;

public class Okoli extends L2GameServerPacket
{

    public Okoli(int id, int state)
    {
        _areaID = id;
        _state = state;
    }

    @Override
	protected final void writeImpl()
    {
        writeC(254);
        writeH(193);
        writeD(_areaID);
        writeD(_state);
    }

	private final int _areaID;
    private final int _state;
}