package king.server.gameserver.events;

import java.io.File;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import king.server.gameserver.engines.DocumentParser;
import king.server.gameserver.events.io.Out;

public class Config extends DocumentParser
{
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final Config _instance = new Config();
	}
	
	public static Config getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private final Logger _log = Logger.getLogger(Config.class.getName());
	private final FastMap<Integer, FastMap<String, String>> config;
	private final FastMap<Integer, FastMap<String, FastMap<Integer, int[]>>> positions;
	private final FastMap<Integer, FastMap<String, int[]>> colors;
	private final FastMap<Integer, FastMap<String, FastList<Integer>>> restrictions;
	private final FastMap<Integer, FastMap<String, String>> messages;
	private final FastMap<Integer, FastMap<String, FastMap<Integer, Integer>>> rewards;
	
	private Config()
	{
		config = new FastMap<>();
		positions = new FastMap<>();
		colors = new FastMap<>();
		restrictions = new FastMap<>();
		messages = new FastMap<>();
		rewards = new FastMap<>();
		load();
	}
	
	private void addColor(int id, String owner, int[] color)
	{
		if (!colors.containsKey(id))
		{
			colors.put(id, new FastMap<String, int[]>());
		}
		
		colors.get(id).put(owner, color);
	}
	
	private void addMessage(int id, String name, String value)
	{
		if (!messages.containsKey(id))
		{
			messages.put(id, new FastMap<String, String>());
		}
		
		messages.get(id).put(name, value);
	}
	
	private void addPosition(int id, String owner, int x, int y, int z, int radius)
	{
		
		if (!positions.containsKey(id))
		{
			positions.put(id, new FastMap<String, FastMap<Integer, int[]>>());
		}
		if (!positions.get(id).containsKey(owner))
		{
			positions.get(id).put(owner, new FastMap<Integer, int[]>());
		}
		
		positions.get(id).get(owner).put(positions.get(id).get(owner).size() + 1, new int[]
		{
			x,
			y,
			z,
			radius
		});
	}
	
	private void addProperty(int id, String propName, String value)
	{
		if (!config.containsKey(id))
		{
			config.put(id, new FastMap<String, String>());
		}
		
		config.get(id).put(propName, value);
	}
	
	private void addRestriction(int id, String type, String ids)
	{
		if (!restrictions.containsKey(id))
		{
			restrictions.put(id, new FastMap<String, FastList<Integer>>());
		}
		
		FastList<Integer> idlist = new FastList<>();
		StringTokenizer st = new StringTokenizer(ids, ",");
		while (st.hasMoreTokens())
		{
			idlist.add(Integer.parseInt(st.nextToken()));
		}
		
		restrictions.get(id).put(type, idlist);
	}
	
	private void addReward(int id, String owner, Integer itemId, Integer itemAmmount)
	{
		if (!rewards.containsKey(id))
		{
			rewards.put(id, new FastMap<String, FastMap<Integer, Integer>>());
		}
		
		if (!rewards.get(id).containsKey(owner))
		{
			rewards.get(id).put(owner, new FastMap<Integer, Integer>());
		}
		
		rewards.get(id).get(owner).put(itemId, itemAmmount);
	}
	
	public boolean getBoolean(int event, String propName)
	{
		if (!(config.containsKey(event)))
		{
			_log.warning("[Event Manager]: Try to get a property of a non existing event: ID: " + event);
			return false;
		}
		
		if (config.get(event).containsKey(propName))
		{
			return Boolean.parseBoolean(config.get(event).get(propName));
		}
		_log.warning("[Event Manager]: Try to get a non existing property: " + propName);
		return false;
		
	}
	
	public int[] getColor(int event, String owner)
	{
		if (!(colors.containsKey(event)))
		{
			_log.warning("[Event Manager]: Try to get a color of a non existing event: ID: " + event);
			return new int[]
			{
				255,
				255,
				255
			};
		}
		
		if (colors.get(event).containsKey(owner))
		{
			return colors.get(event).get(owner);
		}
		_log.warning("[Event Manager]: Try to get a non existing color: " + owner);
		return new int[]
		{
			255,
			255,
			255
		};
	}
	
	public int getInt(int event, String propName)
	{
		if (!(config.containsKey(event)))
		{
			_log.warning("[Event Manager]: Try to get a property of a non existing event: ID: " + event);
			return -1;
		}
		
		if (config.get(event).containsKey(propName))
		{
			return Integer.parseInt(config.get(event).get(propName));
		}
		_log.warning("[Event Manager]: Try to get a non existing property: " + propName);
		return -1;
	}
	
	protected String getMessage(int event, String name)
	{
		if (!(messages.containsKey(event)))
		{
			_log.warning("[Event Manager]: Try to get a message of a non existing event: ID: " + event);
			return null;
		}
		
		if (messages.get(event).containsKey(name))
		{
			return messages.get(event).get(name);
		}
		_log.warning("[Event Manager]: Try to get a non existing message: " + name);
		return null;
		
	}
	
	public int[] getPosition(int event, String owner, int num)
	{
		if (!positions.containsKey(event))
		{
			_log.warning("[Event Manager]: Try to get a position of a non existing event: ID: " + event);
			return new int[] {};
		}
		if (!positions.get(event).containsKey(owner))
		{
			_log.warning("[Event Manager]: Try to get a position of a non existing owner: " + owner);
			return new int[] {};
		}
		if (!positions.get(event).get(owner).containsKey(num) && (num != 0))
		{
			_log.warning("[Event Manager]: Try to get a non existing position: " + num);
			return new int[] {};
		}
		
		if (num == 0)
		{
			return positions.get(event).get(owner).get(Out.random(positions.get(event).get(owner).size()) + 1);
		}
		return positions.get(event).get(owner).get(num);
	}
	
	public FastList<Integer> getRestriction(int event, String type)
	{
		if (!(restrictions.containsKey(event)))
		{
			_log.warning("[Event Manager]: Try to get a restriction of a non existing event: ID: " + event);
			return null;
		}
		if (restrictions.get(event).containsKey(type))
		{
			return restrictions.get(event).get(type);
		}
		_log.warning("[Event Manager]: Try to get a non existing restriction: " + type);
		return null;
	}
	
	public FastMap<Integer, Integer> getRewards(int event, String owner)
	{
		if (!(rewards.containsKey(event)))
		{
			_log.warning("[Event Manager]: Try to get the rewards of a non existing event: ID: " + event);
			return null;
		}
		
		if (!(rewards.get(event).containsKey(owner)))
		{
			_log.warning("[Event Manager]: Failed to get the rewards of " + event + " event: " + owner);
			return null;
		}
		
		return rewards.get(event).get(owner);
	}
	
	public String getString(int event, String propName)
	{
		if (!(config.containsKey(event)))
		{
			_log.warning("[Event Manager]: Try to get a property of a non existing event: ID: " + event);
			return null;
		}
		
		if (config.get(event).containsKey(propName))
		{
			return config.get(event).get(propName);
		}
		_log.warning("Event: Try to get a non existing property: " + propName);
		return null;
	}
	
	@Override
	public void load()
	{
		config.clear();
		positions.clear();
		colors.clear();
		restrictions.clear();
		messages.clear();
		rewards.clear();
		parseDirectory(new File("./config/Events"));
		_log.info("Phoenix Engine 2.0: Sistema de eventos iniciado.");
	}
	
	@Override
	protected void parseDocument()
	{
		NamedNodeMap attrs;
		for (Node n = getCurrentDocument().getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("events".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("event".equalsIgnoreCase(d.getNodeName()))
					{
						attrs = d.getAttributes();
						int eventId = parseInt(attrs, "id");
						
						for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
						{
							if ("property".equalsIgnoreCase(c.getNodeName()))
							{
								attrs = c.getAttributes();
								String name = parseString(attrs, "name");
								String value = parseString(attrs, "value");
								addProperty(eventId, name, value);
							}
							
							if ("position".equalsIgnoreCase(c.getNodeName()))
							{
								attrs = c.getAttributes();
								String owner = parseString(attrs, "owner");
								String x = parseString(attrs, "x");
								String y = parseString(attrs, "y");
								String z = parseString(attrs, "z");
								String radius = parseString(attrs, "radius");
								addPosition(eventId, owner, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z), Integer.parseInt(radius));
								
							}
							
							if ("color".equalsIgnoreCase(c.getNodeName()))
							{
								attrs = c.getAttributes();
								String owner = parseString(attrs, "owner");
								int r = parseInt(attrs, "r");
								int g = parseInt(attrs, "g");
								int b = parseInt(attrs, "b");
								addColor(eventId, owner, new int[]
								{
									r,
									g,
									b
								});
							}
							
							if ("restriction".equalsIgnoreCase(c.getNodeName()))
							{
								attrs = c.getAttributes();
								String type = parseString(attrs, "type");
								String ids = parseString(attrs, "ids");
								addRestriction(eventId, type, ids);
							}
							
							if ("message".equalsIgnoreCase(c.getNodeName()))
							{
								attrs = c.getAttributes();
								String name = parseString(attrs, "name");
								String value = parseString(attrs, "value");
								addMessage(eventId, name, value);
							}
							
							if ("reward".equalsIgnoreCase(c.getNodeName()))
							{
								attrs = c.getAttributes();
								
								String owner = parseString(attrs, "owner");
								String list = parseString(attrs, "list");
								StringTokenizer st = new StringTokenizer(list, ";");
								
								while (st.hasMoreTokens())
								{
									StringTokenizer stsub = new StringTokenizer(st.nextToken(), ",");
									Integer item = Integer.valueOf(stsub.nextToken());
									Integer ammount = Integer.valueOf(stsub.nextToken());
									addReward(eventId, owner, item, ammount);
								}
							}
						}
					}
				}
			}
		}
	}
}