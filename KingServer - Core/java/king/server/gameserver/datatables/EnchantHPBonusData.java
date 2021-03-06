package king.server.gameserver.datatables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import king.server.gameserver.engines.DocumentParser;
import king.server.gameserver.model.items.L2Item;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.skills.funcs.FuncTemplate;
import king.server.gameserver.model.skills.funcs.LambdaConst;
import king.server.gameserver.model.stats.Stats;

public class EnchantHPBonusData extends DocumentParser
{
	private final Map<Integer, List<Integer>> _armorHPBonuses = new HashMap<>();
	
	private static final float fullArmorModifier = 1.5f; // TODO: Move it to config!
	
	/**
	 * Instantiates a new enchant hp bonus data.
	 */
	protected EnchantHPBonusData()
	{
		load();
	}
	
	@Override
	protected void parseDocument()
	{
		for (Node n = getCurrentDocument().getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equals(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("enchantHP".equals(d.getNodeName()))
					{
						List<Integer> bonuses = new ArrayList<>();
						for (Node e = d.getFirstChild(); e != null; e = e.getNextSibling())
						{
							if ("bonus".equals(e.getNodeName()))
							{
								bonuses.add(Integer.valueOf(e.getTextContent()));
							}
						}
						_armorHPBonuses.put(parseInteger(d.getAttributes(), "grade"), bonuses);
					}
				}
			}
		}
		
		if (!_armorHPBonuses.isEmpty())
		{
			final ItemTable it = ItemTable.getInstance();
			L2Item item;
			// Armors
			final Collection<Integer> armorIds = it.getAllArmorsId();
			for (Integer itemId : armorIds)
			{
				item = it.getTemplate(itemId);
				if ((item != null) && (item.getCrystalType() != L2Item.CRYSTAL_NONE))
				{
					switch (item.getBodyPart())
					{
						case L2Item.SLOT_CHEST:
						case L2Item.SLOT_FEET:
						case L2Item.SLOT_GLOVES:
						case L2Item.SLOT_HEAD:
						case L2Item.SLOT_LEGS:
						case L2Item.SLOT_BACK:
						case L2Item.SLOT_FULL_ARMOR:
						case L2Item.SLOT_UNDERWEAR:
						case L2Item.SLOT_L_HAND:
							item.attach(new FuncTemplate(null, null, "EnchantHp", Stats.MAX_HP, 0x60, new LambdaConst(0)));
							break;
						default:
							break;
					}
				}
			}
			
			// Shields
			final Collection<Integer> shieldIds = it.getAllWeaponsId();
			for (Integer itemId : shieldIds)
			{
				item = it.getTemplate(itemId);
				if ((item != null) && (item.getCrystalType() != L2Item.CRYSTAL_NONE))
				{
					switch (item.getBodyPart())
					{
						case L2Item.SLOT_L_HAND:
							item.attach(new FuncTemplate(null, null, "EnchantHp", Stats.MAX_HP, 0x60, new LambdaConst(0)));
							break;
						default:
							break;
					}
				}
			}
		}
	}
	
	@Override
	public void load()
	{
		_armorHPBonuses.clear();
		parseDatapackFile("data/stats/enchantHPBonus.xml");
		_log.info(getClass().getSimpleName() + ": " + _armorHPBonuses.size() + " Enchant HP Bonuses!");
	}
	
	/**
	 * Gets the HP bonus.
	 * @param item the item
	 * @return the HP bonus
	 */
	public final int getHPBonus(L2ItemInstance item)
	{
		final List<Integer> values = _armorHPBonuses.get(item.getItem().getItemGradeSPlus());
		if ((values == null) || values.isEmpty() || (item.getOlyEnchantLevel() <= 0))
		{
			return 0;
		}
		
		final int bonus = values.get(Math.min(item.getOlyEnchantLevel(), values.size()) - 1);
		if (item.getItem().getBodyPart() == L2Item.SLOT_FULL_ARMOR)
		{
			return (int) (bonus * fullArmorModifier);
		}
		return bonus;
	}
	
	/**
	 * Gets the single instance of EnchantHPBonusData.
	 * @return single instance of EnchantHPBonusData
	 */
	public static final EnchantHPBonusData getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final EnchantHPBonusData _instance = new EnchantHPBonusData();
	}
}