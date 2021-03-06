/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.events.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import king.server.gameserver.events.Config;
import king.server.gameserver.events.container.EventContainer;
import king.server.gameserver.events.io.Out;
import king.server.gameserver.events.model.ManagerNpcHtml;

/**
 * @author Rizel
 */
public class Scheduler
{
	
	private class SchedulerTask implements Runnable
	{
		
		@Override
		public void run()
		{
			currentCal = Calendar.getInstance();
			Integer hour = currentCal.get(Calendar.HOUR_OF_DAY);
			Integer mins = currentCal.get(Calendar.MINUTE);
			
			for (Integer[] element : scheduleList)
			{
				if (element[0].equals(hour) && element[1].equals(mins))
				{
					if (element[2].equals(0))
					{
						EventContainer.getInstance().createRandomEvent();
					}
					else
					{
						EventContainer.getInstance().createEvent(element[2]);
					}
				}
			}
		}
		
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final Scheduler _instance = new Scheduler();
	}
	
	public static final Scheduler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	Calendar currentCal;
	
	final FastList<Integer[]> scheduleList;
	
	@SuppressWarnings("synthetic-access")
	private Scheduler()
	{
		scheduleList = new FastList<>();
		makeList();
		Out.tpmScheduleGeneralAtFixedRate(new SchedulerTask(), 1, 50000);
	}
	
	private String loadFile()
	{
		String beolvasott = "";
		String str;
		try
		{
			@SuppressWarnings("resource")
			BufferedReader bf = new BufferedReader(new FileReader("config/Events/EventScheduler.txt"));
			while ((str = bf.readLine()) != null)
			{
				beolvasott += str;
			}
		}
		catch (IOException e)
		{
			System.out.println("Error on reading the scheduler file!");
			return "";
		}
		return beolvasott;
	}
	
	private void makeList()
	{
		StringTokenizer st = new StringTokenizer(loadFile(), ";");
		while (st.hasMoreTokens())
		{
			StringTokenizer sti = new StringTokenizer(st.nextToken(), ":");
			Integer ora = Integer.parseInt(sti.nextToken());
			Integer perc = Integer.parseInt(sti.nextToken());
			Integer event = Integer.parseInt(sti.nextToken());
			scheduleList.add(new Integer[]
			{
				ora,
				perc,
				event
			});
		}
	}
	
	public void scheduleList(Integer player)
	{
		TextBuilder builder = new TextBuilder();
		
		int count = 0;
		
		builder.append("<center><table width=470 bgcolor=4f4f4f><tr><td width=70><font color=ac9775>Scheduler</font></td></tr></table><br>");
		
		for (Integer[] event : scheduleList)
		{
			count++;
			builder.append("<center><table width=270 " + ((count % 2) == 1 ? "" : "bgcolor=4f4f4f") + "><tr><td width=30><font color=ac9775>" + (event[0] < 10 ? "0" + event[0] : event[0]) + ":" + (event[1] < 10 ? "0" + event[1] : event[1]) + "</font></td><td width=210><font color=9f9f9f>" + Config.getInstance().getString(event[2], "eventName") + "</font></td></tr></table>");
		}
		Out.html(player, new ManagerNpcHtml(builder.toString()).string());
	}
}
