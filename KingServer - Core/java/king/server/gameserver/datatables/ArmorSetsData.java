package king.server.gameserver.datatables;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import king.server.gameserver.engines.DocumentParser;
import king.server.gameserver.model.L2ArmorSet;
import king.server.gameserver.model.holders.SkillHolder;

public final class ArmorSetsData extends DocumentParser
{
	private static final Map<Integer, L2ArmorSet> _armorSets = new HashMap<>();
	
	/**
	 * Instantiates a new armor sets data.
	 */
	protected ArmorSetsData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		_armorSets.clear();
		parseDirectory("data/stats/armorsets");
		_log.info(getClass().getSimpleName() + ": " + _armorSets.size() + " Armor sets.");
	}
	
	@Override
	protected void parseDocument()
	{
		NamedNodeMap attrs;
		L2ArmorSet set;
		for (Node n = getCurrentDocument().getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("set".equalsIgnoreCase(d.getNodeName()))
					{
						set = new L2ArmorSet();
						for (Node a = d.getFirstChild(); a != null; a = a.getNextSibling())
						{
							attrs = a.getAttributes();
							switch (a.getNodeName())
							{
								case "chest":
								{
									set.addChest(parseInt(attrs, "id"));
									break;
								}
								case "feet":
								{
									set.addFeet(parseInt(attrs, "id"));
									break;
								}
								case "gloves":
								{
									set.addGloves(parseInt(attrs, "id"));
									break;
								}
								case "head":
								{
									set.addHead(parseInt(attrs, "id"));
									break;
								}
								case "legs":
								{
									set.addLegs(parseInt(attrs, "id"));
									break;
								}
								case "shield":
								{
									set.addShield(parseInt(attrs, "id"));
									break;
								}
								case "skill":
								{
									int skillId = parseInt(attrs, "id");
									int skillLevel = parseInt(attrs, "level");
									set.addSkill(new SkillHolder(skillId, skillLevel));
									break;
								}
								case "shield_skill":
								{
									int skillId = parseInt(attrs, "id");
									int skillLevel = parseInt(attrs, "level");
									set.addShieldSkill(new SkillHolder(skillId, skillLevel));
									break;
								}
								case "enchant6skill":
								{
									int skillId = parseInt(attrs, "id");
									int skillLevel = parseInt(attrs, "level");
									set.addEnchant6Skill(new SkillHolder(skillId, skillLevel));
									break;
								}
								case "con":
								{
									set.addCon(parseInt(attrs, "val"));
									break;
								}
								case "dex":
								{
									set.addDex(parseInt(attrs, "val"));
									break;
								}
								case "str":
								{
									set.addStr(parseInt(attrs, "val"));
									break;
								}
								case "men":
								{
									set.addMen(parseInt(attrs, "val"));
									break;
								}
								case "wit":
								{
									set.addWit(parseInt(attrs, "val"));
									break;
								}
								case "int":
								{
									set.addInt(parseInt(attrs, "val"));
									break;
								}
							}
						}
						_armorSets.put(set.getChestId(), set);
					}
				}
			}
		}
	}
	
	/**
	 * Checks if is armor set.
	 * @param chestId the chest Id to verify.
	 * @return {@code true} if the chest Id belongs to a registered armor set, {@code false} otherwise.
	 */
	public boolean isArmorSet(int chestId)
	{
		return _armorSets.containsKey(chestId);
	}
	
	/**
	 * Gets the sets the.
	 * @param chestId the chest Id identifying the armor set.
	 * @return the armor set associated to the give chest Id.
	 */
	public L2ArmorSet getSet(int chestId)
	{
		return _armorSets.get(chestId);
	}
	
	/**
	 * Gets the single instance of ArmorSetsData.
	 * @return single instance of ArmorSetsData
	 */
	public static ArmorSetsData getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final ArmorSetsData _instance = new ArmorSetsData();
	}
}