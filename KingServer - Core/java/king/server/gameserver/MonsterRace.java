package king.server.gameserver;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import king.server.gameserver.datatables.NpcTable;
import king.server.gameserver.idfactory.IdFactory;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.templates.L2NpcTemplate;
import king.server.util.Rnd;

public class MonsterRace
{
	protected static final Logger _log = Logger.getLogger(MonsterRace.class.getName());
	
	private final L2Npc[] _monsters;
	private Constructor<?> _constructor;
	private int[][] _speeds;
	private final int[] _first, _second;
	
	protected MonsterRace()
	{
		_monsters = new L2Npc[8];
		_speeds = new int[8][20];
		_first = new int[2];
		_second = new int[2];
	}
	
	public static MonsterRace getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void newRace()
	{
		int random = 0;
		
		for (int i = 0; i < 8; i++)
		{
			int id = 31003;
			random = Rnd.get(24);
			while (true)
			{
				for (int j = i - 1; j >= 0; j--)
				{
					if (_monsters[j].getTemplate().getNpcId() == (id + random))
					{
						random = Rnd.get(24);
						continue;
					}
				}
				break;
			}
			try
			{
				L2NpcTemplate template = NpcTable.getInstance().getTemplate(id + random);
				_constructor = Class.forName("king.server.gameserver.model.actor.instance." + template.getType() + "Instance").getConstructors()[0];
				int objectId = IdFactory.getInstance().getNextId();
				_monsters[i] = (L2Npc) _constructor.newInstance(objectId, template);
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "", e);
			}
			// _log.info("Monster "+i+" is id: "+(id+random));
		}
		newSpeeds();
	}
	
	public void newSpeeds()
	{
		_speeds = new int[8][20];
		int total = 0;
		_first[1] = 0;
		_second[1] = 0;
		for (int i = 0; i < 8; i++)
		{
			total = 0;
			for (int j = 0; j < 20; j++)
			{
				if (j == 19)
				{
					_speeds[i][j] = 100;
				}
				else
				{
					_speeds[i][j] = Rnd.get(60) + 65;
				}
				total += _speeds[i][j];
			}
			if (total >= _first[1])
			{
				_second[0] = _first[0];
				_second[1] = _first[1];
				_first[0] = 8 - i;
				_first[1] = total;
			}
			else if (total >= _second[1])
			{
				_second[0] = 8 - i;
				_second[1] = total;
			}
		}
	}
	
	/**
	 * @return Returns the monsters.
	 */
	public L2Npc[] getMonsters()
	{
		return _monsters;
	}
	
	/**
	 * @return Returns the speeds.
	 */
	public int[][] getSpeeds()
	{
		return _speeds;
	}
	
	public int getFirstPlace()
	{
		return _first[0];
	}
	
	public int getSecondPlace()
	{
		return _second[0];
	}
	
	private static class SingletonHolder
	{
		protected static final MonsterRace _instance = new MonsterRace();
	}
}