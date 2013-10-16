public abstract class Entity
{
	protected boolean dead = false;

	protected int xPos;
	protected int yPos;
	protected int depth;
	
	abstract void update();
}