public class Address extends Parameter
{
	public int getVal(CPU home, int val)
	{
		return home.memory[val];
	}
}