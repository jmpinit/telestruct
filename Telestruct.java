public class Telestruct
{
	public static void main(String args[])
	{
		World gameworld = new World(50);
		gameworld.add(new Robot(10, 10, 0));
		gameworld.birth();
	}
}