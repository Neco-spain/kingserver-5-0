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
package king.server.log.formatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import king.server.Config;
import king.server.util.StringUtil;
import king.server.util.Util;

/**
 * This class ...
 * @version $Revision: 1.1.4.2 $ $Date: 2005/03/27 15:30:08 $
 */
public class ConsoleLogFormatter extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		final StringBuilder output = new StringBuilder(500);
		// output.append(record.getLevel().getName());
		// output.append(_);
		// output.append(record.getLoggerName());
		// output.append(_);
		StringUtil.append(output, record.getMessage(), Config.EOL);
		
		if (record.getThrown() != null)
		{
			try
			{
				StringUtil.append(output, Util.getStackTrace(record.getThrown()), Config.EOL);
			}
			catch (Exception ex)
			{
			}
		}
		return output.toString();
	}
}
