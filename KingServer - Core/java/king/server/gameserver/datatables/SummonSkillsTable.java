package king.server.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import king.server.L2DatabaseFactory;
import king.server.gameserver.model.actor.L2Summon;

public class SummonSkillsTable
{
	private static Logger _log = Logger.getLogger(SummonSkillsTable.class.getName());
	private final Map<Integer, Map<Integer, L2PetSkillLearn>> _skillTrees = new HashMap<>();
	
	protected SummonSkillsTable()
	{
		load();
	}
	
	public void load()
	{
		_skillTrees.clear();
		int npcId = 0;
		int count = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT id FROM npc WHERE type IN ('L2Pet','L2BabyPet','L2SiegeSummon') ORDER BY id"))
		{
			Map<Integer, L2PetSkillLearn> map;
			try (PreparedStatement ps2 = con.prepareStatement("SELECT minLvl, skillId, skillLvl FROM pets_skills where templateId=? ORDER BY skillId, skillLvl"))
			{
				while (rs.next())
				{
					map = new HashMap<>();
					npcId = rs.getInt("id");
					ps2.setInt(1, npcId);
					try (ResultSet skilltree = ps2.executeQuery())
					{
						while (skilltree.next())
						{
							int id = skilltree.getInt("skillId");
							int lvl = skilltree.getInt("skillLvl");
							map.put(SkillTable.getSkillHashCode(id, lvl + 1), new L2PetSkillLearn(id, lvl, skilltree.getInt("minLvl")));
						}
						_skillTrees.put(npcId, map);
					}
					ps2.clearParameters();
					count += map.size();
					_log.fine(getClass().getSimpleName() + ": skill tree for pet " + npcId + " has " + map.size() + " skills");
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, getClass().getSimpleName() + ": Error while creating pet skill tree (Pet ID " + npcId + "): " + e.getMessage(), e);
		}
		_log.info(getClass().getSimpleName() + ": " + count + " skills.");
	}
	
	public int getAvailableLevel(L2Summon cha, int skillId)
	{
		int lvl = 0;
		if (!_skillTrees.containsKey(cha.getNpcId()))
		{
			_log.warning(getClass().getSimpleName() + ": Pet id " + cha.getNpcId() + " does not have any skills assigned.");
			return lvl;
		}
		Collection<L2PetSkillLearn> skills = _skillTrees.get(cha.getNpcId()).values();
		for (L2PetSkillLearn temp : skills)
		{
			if (temp.getId() != skillId)
			{
				continue;
			}
			if (temp.getLevel() == 0)
			{
				if (cha.getLevel() < 70)
				{
					lvl = (cha.getLevel() / 10);
					if (lvl <= 0)
					{
						lvl = 1;
					}
				}
				else
				{
					lvl = (7 + ((cha.getLevel() - 70) / 5));
				}
				
				// formula usable for skill that have 10 or more skill levels
				int maxLvl = SkillTable.getInstance().getMaxLevel(temp.getId());
				if (lvl > maxLvl)
				{
					lvl = maxLvl;
				}
				break;
			}
			else if (temp.getMinLevel() <= cha.getLevel())
			{
				if (temp.getLevel() > lvl)
				{
					lvl = temp.getLevel();
				}
			}
		}
		return lvl;
	}
	
	public List<Integer> getAvailableSkills(L2Summon cha)
	{
		List<Integer> skillIds = new ArrayList<>();
		if (!_skillTrees.containsKey(cha.getNpcId()))
		{
			_log.warning(getClass().getSimpleName() + ": Pet id " + cha.getNpcId() + " does not have any skills assigned.");
			return skillIds;
		}
		Collection<L2PetSkillLearn> skills = _skillTrees.get(cha.getNpcId()).values();
		for (L2PetSkillLearn temp : skills)
		{
			if (skillIds.contains(temp.getId()))
			{
				continue;
			}
			skillIds.add(temp.getId());
		}
		return skillIds;
	}
	
	public static final class L2PetSkillLearn
	{
		private final int _id;
		private final int _level;
		private final int _minLevel;
		
		public L2PetSkillLearn(int id, int lvl, int minLvl)
		{
			_id = id;
			_level = lvl;
			_minLevel = minLvl;
		}
		
		public int getId()
		{
			return _id;
		}
		
		public int getLevel()
		{
			return _level;
		}
		
		public int getMinLevel()
		{
			return _minLevel;
		}
	}
	
	public static SummonSkillsTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final SummonSkillsTable _instance = new SummonSkillsTable();
	}
}