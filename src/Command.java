public abstract class Command
{
	CPU home;
	
	protected String name;
	protected Parameter[] params;
	
	public Command(CPU h, String n)
	{
		home = h;
		name = n;
	}
	
	abstract void execute();
	
	public void gotoNextCommand()
	{
		home.programCounter += params.length+1;
	}
	
	public int[] getValues()
	{
		int[] values = new int[params.length];
		for(int i=0; i<params.length; i++)
		{
			int memVal = home.getMemAtOffset(home.programCounter, i+1);
			values[i] = params[i].getVal(home, memVal);
		}
		
		return values;
	}
}