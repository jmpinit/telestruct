public class Packet extends Entity
{
	int number;
	
	public Packet(Rob_The_Builder.World w, int xPos, int yPos, int dir, int n)
	{
		x = xPos;
		y = yPos;
		direction = dir;
		
		number = n;
		
		sprite = space.getSprite();
		sprite.loadSprite("sprites/packet.vsp");
		
		space = w;
		
		//check if spawned on top of something
		if(space.getBlock(x, y)==TYPE_OCCUPIED)
		{
			Entity ent = space.getEntAt(x, y);
			if(ent!=null)
				ent.message(number);
			
			space.setBlock(x, y, TYPE_EMPTY);
			space.kill(this);
		} else {
			space.setBlock(x, y, TYPE_OCCUPIED);
		}
	}
	
	public void update()
	{
		if(space.getBlock(x+dirToX(direction), y+dirToY(direction))==TYPE_EMPTY)
		{
			moveForward();
		} else {
			if(space.getBlock(x+dirToX(direction), y+dirToY(direction))==TYPE_OCCUPIED)
				space.getEntAt(x+dirToX(direction), y+dirToY(direction)).message(number);
			space.kill(this);
		}
	}
	
	public void message(int v) {}
}