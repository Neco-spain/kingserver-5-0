/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.network.clientpackets;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import king.server.Config;
import king.server.gameserver.datatables.EnchantItemData;
import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.model.EnchantItem;
import king.server.gameserver.model.EnchantScroll;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.actor.instance.L2WarehouseInstance;
import king.server.gameserver.model.items.L2Armor;
import king.server.gameserver.model.items.L2Item;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.ActionFailed;
import king.server.gameserver.network.serverpackets.EnchantResult;
import king.server.gameserver.network.serverpackets.InventoryUpdate;
import king.server.gameserver.network.serverpackets.ItemList;
import king.server.gameserver.network.serverpackets.MagicSkillUse;
import king.server.gameserver.network.serverpackets.StatusUpdate;
import king.server.gameserver.network.serverpackets.SystemMessage;
import king.server.gameserver.util.Util;
import king.server.util.Rnd;

public final class RequestEnchantItem extends L2GameClientPacket
{
	protected static final Logger _logEnchant = Logger.getLogger("enchant");
	
	private static final String _C__5F_REQUESTENCHANTITEM = "[C] 5F RequestEnchantItem";
	
	private int _objectId = 0;
	private int _supportId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_supportId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		Collection<L2Character> knowns = activeChar.getKnownList().getKnownCharactersInRadius(282);
		if ((activeChar == null) || (_objectId == 0))
		{
			return;
		}
		for (L2Object wh : knowns)
		{
			if (wh instanceof L2WarehouseInstance)
			{
				activeChar.sendMessage("You cannot enchant near warehouse.");
				return;
			}
		}
		if (!activeChar.isOnline() || getClient().isDetached())
		{
			activeChar.setActiveEnchantItem(null);
			return;
		}
		
		if (activeChar.isProcessingTransaction() || activeChar.isInStoreMode())
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_ENCHANT_WHILE_STORE);
			activeChar.setActiveEnchantItem(null);
			return;
		}
		
		if (activeChar.getActiveTradeList() != null)
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			activeChar.sendMessage("YOU_CANNOT_ENCHANT_WHILE_TRADING");
			return;
		}
		
		if (activeChar.getActiveTradeList() != null)
		{
			activeChar.cancelActiveTrade();
			activeChar.sendMessage("YOUR_TRADE_WAS_CANCELED");
			return;
		}
		
		if (activeChar.isInCraftMode())
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while Crafting");
			return;
		}
		
		if (activeChar.isTeleporting())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while You Teleporting");
			return;
			
		}
		
		if (activeChar.isDead())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while You Are Dead");
			return;
			
		}
		if (activeChar.isSleeping())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while You Are In Sleep");
			return;
			
		}
		if (activeChar.isParalyzed())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while You Are In Para");
			return;
			
		}
		
		if (activeChar.isCastingNow())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while Casting");
			return;
			
		}
		
		if (activeChar.isMoving())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while moving");
			return;
			
		}
		
		if (activeChar.isProcessingTransaction())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while trading");
			return;
			
		}
		
		if (activeChar.isStunned())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while stunned");
			return;
			
		}
		
		if (activeChar.isMounted())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while -beep-ted");
			return;
			
		}
		
		if (activeChar.isFakeDeath())
		{
			
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while fake death");
			return;
			
		}
		
		if (activeChar.isInJail())
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while in jail");
			return;
		}
		
		if (activeChar.isCursedWeaponEquipped())
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while cursed weapon");
			return;
		}
		
		if (activeChar.isInWater())
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while in water");
			return;
		}
		
		if (activeChar.isFlying())
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while flying");
			return;
		}
		
		if (activeChar.isSitting())
		{
			activeChar.setActiveEnchantItem(null);
			activeChar.sendMessage("Can't enchant while sitting");
			return;
		}
		
		L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		L2ItemInstance scroll = activeChar.getActiveEnchantItem();
		L2ItemInstance support = activeChar.getActiveEnchantSupportItem();
		
		if ((item == null) || (scroll == null))
		{
			activeChar.setActiveEnchantItem(null);
			return;
		}
		
		// template for scroll
		EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		
		// scroll not found in list
		if (scrollTemplate == null)
		{
			return;
		}
		
		// template for support item, if exist
		EnchantItem supportTemplate = null;
		if (support != null)
		{
			if (support.getObjectId() != _supportId)
			{
				activeChar.setActiveEnchantItem(null);
				return;
			}
			supportTemplate = EnchantItemData.getInstance().getSupportItem(support);
		}
		
		// first validation check
		if (!scrollTemplate.isValid(item, supportTemplate))
		{
			activeChar.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2, 0, 0));
			return;
		}
		
		// fast auto-enchant cheat check
		if ((activeChar.getActiveEnchantTimestamp() == 0) || ((System.currentTimeMillis() - activeChar.getActiveEnchantTimestamp()) < 2000))
		{
			Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " use autoenchant program ", Config.DEFAULT_PUNISH);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2, 0, 0));
			return;
		}
		
		// attempting to destroy scroll
		scroll = activeChar.getInventory().destroyItem("Enchant", scroll.getObjectId(), 1, activeChar, item);
		if (scroll == null)
		{
			activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant with a scroll he doesn't have", Config.DEFAULT_PUNISH);
			activeChar.setActiveEnchantItem(null);
			activeChar.sendPacket(new EnchantResult(2, 0, 0));
			return;
		}
		
		// attempting to destroy support if exist
		if (support != null)
		{
			support = activeChar.getInventory().destroyItem("Enchant", support.getObjectId(), 1, activeChar, item);
			if (support == null)
			{
				activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
				Util.handleIllegalPlayerAction(activeChar, "Player " + activeChar.getName() + " tried to enchant with a support item he doesn't have", Config.DEFAULT_PUNISH);
				activeChar.setActiveEnchantItem(null);
				activeChar.sendPacket(new EnchantResult(2, 0, 0));
				return;
			}
		}
		
		synchronized (item)
		{
			double chance = scrollTemplate.getChance(item, supportTemplate);
			
			L2Skill enchant4Skill = null;
			L2Item it = item.getItem();
			
			// last validation check
			if ((item.getOwnerId() != activeChar.getObjectId()) || (item.isEnchantable() == 0) || (chance < 0))
			{
				activeChar.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
				activeChar.setActiveEnchantItem(null);
				activeChar.sendPacket(new EnchantResult(2, 0, 0));
				return;
			}
			
			if (Rnd.get(100) < chance)
			{
				// success
				item.setEnchantLevel(item.getEnchantLevel() + 1);
				item.updateDatabase();
				activeChar.sendPacket(new EnchantResult(0, 0, 0));
				showEnchantAnimation(activeChar, item.getEnchantLevel());
				
				if (Config.LOG_ITEM_ENCHANTS)
				{
					LogRecord record = new LogRecord(Level.INFO, "Success");
					record.setParameters(new Object[]
					{
						activeChar,
						item,
						scroll,
						support,
						chance
					});
					record.setLoggerName("item");
					_logEnchant.log(record);
				}
				
				// announce the success
				int minEnchantAnnounce = item.isArmor() ? 6 : 7;
				int maxEnchantAnnounce = item.isArmor() ? 0 : 15;
				if ((item.getEnchantLevel() == minEnchantAnnounce) || (item.getEnchantLevel() == maxEnchantAnnounce))
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_SUCCESSFULY_ENCHANTED_A_S2_S3);
					sm.addCharName(activeChar);
					sm.addNumber(item.getEnchantLevel());
					sm.addItemName(item);
					activeChar.broadcastPacket(sm);
					
					L2Skill skill = SkillTable.FrequentSkill.FIREWORK.getSkill();
					if (skill != null)
					{
						activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, skill.getId(), skill.getLevel(), skill.getHitTime(), skill.getReuseDelay()));
					}
				}
				
				if ((it instanceof L2Armor) && (item.getEnchantLevel() == 4) && activeChar.getInventory().getItemByObjectId(item.getObjectId()).isEquipped())
				{
					enchant4Skill = ((L2Armor) it).getEnchant4Skill();
					if (enchant4Skill != null)
					{
						// add skills bestowed from +4 armor
						activeChar.addSkill(enchant4Skill, false);
						activeChar.sendSkillList();
					}
				}
			}
			else
			{
				if (scrollTemplate.isSafe())
				{
					// safe enchant - remain old value
					activeChar.sendPacket(SystemMessageId.SAFE_ENCHANT_FAILED);
					activeChar.sendPacket(new EnchantResult(5, 0, 0));
					showEnchantAnimation(activeChar, 0);
					
					if (Config.LOG_ITEM_ENCHANTS)
					{
						LogRecord record = new LogRecord(Level.INFO, "Safe Fail");
						record.setParameters(new Object[]
						{
							activeChar,
							item,
							scroll,
							support,
							chance
						});
						record.setLoggerName("item");
						_logEnchant.log(record);
					}
				}
				else
				{
					// unequip item on enchant failure to avoid item skills stack
					if (item.isEquipped())
					{
						if (item.getEnchantLevel() > 0)
						{
							SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EQUIPMENT_S1_S2_REMOVED);
							sm.addNumber(item.getEnchantLevel());
							sm.addItemName(item);
							activeChar.sendPacket(sm);
						}
						else
						{
							SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISARMED);
							sm.addItemName(item);
							activeChar.sendPacket(sm);
						}
						
						L2ItemInstance[] unequiped = activeChar.getInventory().unEquipItemInSlotAndRecord(item.getLocationSlot());
						InventoryUpdate iu = new InventoryUpdate();
						for (L2ItemInstance itm : unequiped)
						{
							iu.addModifiedItem(itm);
						}
						
						activeChar.sendPacket(iu);
						activeChar.broadcastUserInfo();
					}
					
					if (scrollTemplate.isBlessed())
					{
						// blessed enchant - clear enchant value
						activeChar.sendPacket(SystemMessageId.BLESSED_ENCHANT_FAILED);
						
						item.setEnchantLevel(0);
						item.updateDatabase();
						activeChar.sendPacket(new EnchantResult(3, 0, 0));
						showEnchantAnimation(activeChar, 0);
						
						if (Config.LOG_ITEM_ENCHANTS)
						{
							LogRecord record = new LogRecord(Level.INFO, "Blessed Fail");
							record.setParameters(new Object[]
							{
								activeChar,
								item,
								scroll,
								support,
								chance
							});
							record.setLoggerName("item");
							_logEnchant.log(record);
						}
					}
					else
					{
						// enchant failed, destroy item
						int crystalId = item.getItem().getCrystalItemId();
						int count = item.getCrystalCount() - ((item.getItem().getCrystalCount() + 1) / 2);
						if (count < 1)
						{
							count = 1;
						}
						
						L2ItemInstance destroyItem = activeChar.getInventory().destroyItem("Enchant", item, activeChar, null);
						if (destroyItem == null)
						{
							// unable to destroy item, cheater ?
							Util.handleIllegalPlayerAction(activeChar, "Unable to delete item on enchant failure from player " + activeChar.getName() + ", possible cheater !", Config.DEFAULT_PUNISH);
							activeChar.setActiveEnchantItem(null);
							activeChar.sendPacket(new EnchantResult(2, 0, 0));
							
							if (Config.LOG_ITEM_ENCHANTS)
							{
								LogRecord record = new LogRecord(Level.INFO, "Unable to destroy");
								record.setParameters(new Object[]
								{
									activeChar,
									item,
									scroll,
									support,
									chance
								});
								record.setLoggerName("item");
								_logEnchant.log(record);
							}
							return;
						}
						
						L2ItemInstance crystals = null;
						if (crystalId != 0)
						{
							crystals = activeChar.getInventory().addItem("Enchant", crystalId, count, activeChar, destroyItem);
							
							SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
							sm.addItemName(crystals);
							sm.addItemNumber(count);
							activeChar.sendPacket(sm);
						}
						
						if (!Config.FORCE_INVENTORY_UPDATE)
						{
							InventoryUpdate iu = new InventoryUpdate();
							if (destroyItem.getCount() == 0)
							{
								iu.addRemovedItem(destroyItem);
							}
							else
							{
								iu.addModifiedItem(destroyItem);
							}
							
							if (crystals != null)
							{
								iu.addItem(crystals);
							}
							
							if (scroll.getCount() == 0)
							{
								iu.addRemovedItem(scroll);
							}
							else
							{
								iu.addModifiedItem(scroll);
							}
							
							activeChar.sendPacket(iu);
						}
						else
						{
							activeChar.sendPacket(new ItemList(activeChar, true));
						}
						
						L2World world = L2World.getInstance();
						world.removeObject(destroyItem);
						if (crystalId == 0)
						{
							activeChar.sendPacket(new EnchantResult(4, 0, 0));
						}
						else
						{
							activeChar.sendPacket(new EnchantResult(1, crystalId, count));
							showEnchantAnimation(activeChar, 0);
						}
						
						if (Config.LOG_ITEM_ENCHANTS)
						{
							LogRecord record = new LogRecord(Level.INFO, "Fail");
							record.setParameters(new Object[]
							{
								activeChar,
								item,
								scroll,
								support,
								chance
							});
							record.setLoggerName("item");
							_logEnchant.log(record);
						}
					}
				}
			}
			
			StatusUpdate su = new StatusUpdate(activeChar);
			su.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
			activeChar.sendPacket(su);
			
			InventoryUpdate iu = new InventoryUpdate();
			if (scroll.getCount() == 0)
			{
				iu.addRemovedItem(scroll);
			}
			else
			{
				iu.addModifiedItem(scroll);
			}
			
			if (item.getCount() == 0)
			{
				iu.addRemovedItem(item);
			}
			else
			{
				iu.addModifiedItem(item);
			}
			activeChar.sendPacket(iu);
			activeChar.broadcastUserInfo();
			activeChar.setActiveEnchantItem(null);
		}
	}
	
	public static void showEnchantAnimation(L2PcInstance player, int enchantLevel)
	{
		enchantLevel = Math.min(enchantLevel, 20);
		final int skillId = 23096 + enchantLevel;
		// player.doCast(SkillTable.getInstance().getInfo(skillId, 1));
		final MagicSkillUse msu = new MagicSkillUse(player, player, skillId, 1, 1, 1);
		player.broadcastPacket(msu);
	}
	
	@Override
	public String getType()
	{
		return _C__5F_REQUESTENCHANTITEM;
	}
}
