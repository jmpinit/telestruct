public class NOT extends Command
{
	public NOT(CPU h) {
		super(h, "NOT");
		
		params = new Parameter[1];
		params[0] = new Address();
	}
	
	public void execute()
	{
		int[] values = getValues();
		
		//calculated value
		int result = ~values[0];
		
		//address of destination
		int dest = home.getMemAtOffset(home.programCounter, 1);
		
		//save the result
		home.memory[dest] = result;
		
		gotoNextCommand();
	}
}