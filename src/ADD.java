public class ADD extends Command
{
	public ADD(CPU h) {
		super(h, "ADD");
		
		params = new Parameter[2];
		params[0] = new Address();
		params[1] = new Address();
	}
	
	public void execute()
	{
		int[] values = getValues();
		
		//calculated value
		int result = values[1]+values[0];
		
		//address of destination
		int dest = home.getMemAtOffset(home.programCounter, 1);
		
		//save the result
		home.memory[dest] = result;
		
		gotoNextCommand();
	}
}