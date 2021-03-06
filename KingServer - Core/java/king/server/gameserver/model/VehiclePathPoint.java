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
package king.server.gameserver.model;

public class VehiclePathPoint
{
	public int x;
	public int y;
	public int z;
	public int moveSpeed;
	public int rotationSpeed;
	
	public VehiclePathPoint(int _x, int _y, int _z)
	{
		x = _x;
		y = _y;
		z = _z;
		moveSpeed = 350;
		rotationSpeed = 4000;
	}
	
	public VehiclePathPoint(int _x, int _y, int _z, int _m, int _r)
	{
		x = _x;
		y = _y;
		z = _z;
		moveSpeed = _m;
		rotationSpeed = _r;
	}
}