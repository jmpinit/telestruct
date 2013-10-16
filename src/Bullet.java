public class Bullet extends Entity
{
	private int damage;
	
	public Bullet(Rob_The_Builder.World w, int xPos, int yPos, int dir, int d)
	{
		x = xPos;
		y = yPos;
		direction = dir;
		
		damage = d;
		
		space = w;
		
		sprite = space.getSprite();
		sprite.loadSprite("sprites/packet.vsp");
		
		//check if spawned on top of something
		if(space.getBlock(x, y)==TYPE_OCCUPIED)
		{
			space.getEntAt(x, y).hurt(damage);
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
			{
				Entity ent = space.getEntAt(x+dirToX(direction), y+dirToY(direction));
				if(ent!=null)
					ent.hurt(damage);
			}
			space.setBlock(x, y, TYPE_EMPTY);
			space.kill(this);
		}
	}
	
	public void message(int v)
	{
		//ha ha HA
		//you cannot argue with bullet
	}
}