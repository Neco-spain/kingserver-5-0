package king.server.gameserver.events.model;

import javolution.text.TextBuilder;
import king.server.gameserver.events.AbstractEvent;

public abstract class EventStatus
{
	protected AbstractEvent event;
	protected TextBuilder sb = new TextBuilder();
	public abstract String generateList();
}