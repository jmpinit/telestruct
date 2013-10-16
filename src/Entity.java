public abstract class Entity
{
	public static final int DIR_LEFT = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_RIGHT = 2;
	public static final int DIR_DOWN = 3;
	
	//world block types
	protected static final int TYPE_EMPTY = Rob_The_Builder.World.TYPE_EMPTY;
	protected static final int TYPE_WALL = Rob_The_Builder.World.TYPE_WALL;
	protected static final int TYPE_RES = Rob_The_Builder.World.TYPE_RES;
	protected static final int TYPE_OCCUPIED = Rob_The_Builder.World.TYPE_OCCUPIED;
	
	protected Rob_The_Builder.World space;
	protected int x, y;
	protected int direction;
	protected int health;
	protected boolean dead = false;
	protected Rob_The_Builder.VectorSprite sprite;
	
	abstract void update();
	
	public void render()
	{
		sprite.render();
	}
	
	public static int dirToX(int dir)
	{
		if(dir==DIR_LEFT)
			return -1;
		if(dir==DIR_RIGHT)
			return 1;
		return 0;
	}
	
	public static int dirToY(int dir)
	{
		if(dir==DIR_UP)
			return -1;
		if(dir==DIR_DOWN)
			return 1;
		return 0;
	}
	
	public void hurt(int amount)
	{
		health -= amount;
		
		if(health<=0)
			dead = true;
	}
	
	abstract void message(int val);
	
	public void moveForward()
	{
		space.setBlock(x, y, TYPE_EMPTY);
		
		switch(direction)
		{
			case DIR_LEFT:
				if(space.getBlock(x-1, y)==TYPE_EMPTY)
					x--;
				break;
			case DIR_UP:
				if(space.getBlock(x, y-1)==TYPE_EMPTY)
					y--;
				break;
			case DIR_RIGHT:
				if(space.getBlock(x+1, y)==TYPE_EMPTY)
					x++;
				break;
			case DIR_DOWN:
				if(space.getBlock(x, y+1)==TYPE_EMPTY)
					y++;
				break;
		}
		
		space.setBlock(x, y, TYPE_OCCUPIED);
	}
	
	public void moveBackward()
	{
		space.setBlock(x, y, TYPE_EMPTY);
		
		switch(direction)
		{
			case DIR_RIGHT:
				if(space.getBlock(x-1, y)==TYPE_EMPTY)
					x--;
				break;
			case DIR_DOWN:
				if(space.getBlock(x, y-1)==TYPE_EMPTY)
					y--;
				break;
			case DIR_LEFT:
				if(space.getBlock(x+1, y)==TYPE_EMPTY)
					x++;
				break;
			case DIR_UP:
				if(space.getBlock(x, y+1)==TYPE_EMPTY)
					y++;
				break;
		}
		
		space.setBlock(x, y, TYPE_OCCUPIED);
	}
	
	public void turnRight() { direction++; if(direction>3) direction = 0; }
	public void turnLeft() { direction--; if(direction<0) direction = 3; }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getDir() { return direction; }
}