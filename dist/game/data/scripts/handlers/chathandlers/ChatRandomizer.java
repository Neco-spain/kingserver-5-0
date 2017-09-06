/*
 * Copyright (C) 2004-2017 L2J DataPack
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
package handlers.chathandlers;

import com.l2jserver.util.Rnd;

/**
 * @author Mobius
 */
public class ChatRandomizer
{
	public static String randomize(String text)
	{
		final StringBuilder textOut = new StringBuilder();
		for (char c : text.toCharArray())
		{
			if ((c > 96) && (c < 123))
			{
				textOut.append(Character.toString((char) Rnd.get(96, 123)));
			}
			else if ((c > 64) && (c < 91))
			{
				textOut.append(Character.toString((char) Rnd.get(64, 91)));
			}
			else if ((c == 32) || (c == 44) || (c == 46))
			{
				textOut.append(c);
			}
			else
			{
				textOut.append(Character.toString((char) Rnd.get(47, 64)));
			}
		}
		return textOut.toString();
	}
}