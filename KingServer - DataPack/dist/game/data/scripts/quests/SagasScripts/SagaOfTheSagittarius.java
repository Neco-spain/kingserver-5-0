/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.SagasScripts;

/**
 * Saga of the Sagittarius (82)
 * @author Emperorc
 */
public class SagaOfTheSagittarius extends SagasSuperClass
{
	public static String qn1 = "82_SagaOfTheSagittarius";
	public static int qnu = 82;
	public static String qna = "Saga of the Sagittarius";
	
	public SagaOfTheSagittarius()
	{
		super(qnu, qn1, qna);
		NPC = new int[]
		{
			30702,
			31627,
			31604,
			31640,
			31633,
			31646,
			31647,
			31650,
			31654,
			31655,
			31657,
			31641
		};
		Items = new int[]
		{
			7080,
			7519,
			7081,
			7497,
			7280,
			7311,
			7342,
			7373,
			7404,
			7435,
			7105,
			0
		};
		Mob = new int[]
		{
			27296,
			27231,
			27305
		};
		qn = qn1;
		classid = new int[]
		{
			92
		};
		prevclass = new int[]
		{
			0x09
		};
		X = new int[]
		{
			191046,
			46066,
			46066
		};
		Y = new int[]
		{
			-40640,
			-36396,
			-36396
		};
		Z = new int[]
		{
			-3042,
			-1685,
			-1685
		};
		Text = new String[]
		{
			"PLAYERNAME! Pursued to here! However, I jumped out of the Banshouren boundaries! You look at the giant as the sign of power!",
			"... Oh ... good! So it was ... let's begin!",
			"I do not have the patience ..! I have been a giant force ...! Cough chatter ah ah ah!",
			"Paying homage to those who disrupt the orderly will be PLAYERNAME's death!",
			"Now, my soul freed from the shackles of the millennium, Halixia, to the back side I come ...",
			"Why do you interfere others' battles?",
			"This is a waste of time.. Say goodbye...!",
			"...That is the enemy",
			"...Goodness! PLAYERNAME you are still looking?",
			"PLAYERNAME ... Not just to whom the victory. Only personnel involved in the fighting are eligible to share in the victory.",
			"Your sword is not an ornament. Don't you think, PLAYERNAME?",
			"Goodness! I no longer sense a battle there now.",
			"let...",
			"Only engaged in the battle to bar their choice. Perhaps you should regret.",
			"The human nation was foolish to try and fight a giant's strength.",
			"Must...Retreat... Too...Strong.",
			"PLAYERNAME. Defeat...by...retaining...and...Mo...Hacker",
			"....! Fight...Defeat...It...Fight...Defeat...It..."
		};
		registerNPCs();
	}
}
