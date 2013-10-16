import java.util.*;

public class World
{
	int size;
	
	private Vector<Entity> myEnts = new Vector<Entity>();
	private Vector<Entity> myBabies = new Vector<Entity>();
	
	public World(int s)
	{
		size = s;
	}
	
	public void add(Entity ent)
	{
		//myBabies serves as a queue
		myBabies.add(ent);
	}
	
	public void birth()
	{
		myEnts.addAll(myBabies);
		myBabies.removeAllElements();
	}
	
	public void update()
	{
		for(Entity ent: myEnts)
		{
			ent.update();
		}
	}
	
	public void clean()
	{
		//use an iterator to remove references to dead entities
		//useful here because it can handle things disappearing from
		//its collection as it is moving through the collection
		Iterator itr = myEnts.iterator();
		while(itr.hasNext())
		{
			Entity ent = (Entity)itr.next();
			
			if(ent.dead)
			{
				itr.remove();
			}
		}
	}
}