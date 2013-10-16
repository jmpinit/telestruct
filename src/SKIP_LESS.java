public class SKIP_LESS extends Command
{
	public SKIP_LESS(CPU h) {
		super(h, "SKIP IF LESS");
		
		params = new Parameter[2];
		params[0] = new Address();
		params[1] = new Address();
	}
	
	public void execute()
	{
		int[] values = getValues();
		
		if(values[1]<values[0])
		{
			gotoNextCommand();
			int instruction = home.memory[home.programCounter];
			home.commands[instruction].gotoNextCommand();
		}
			
		gotoNextCommand();
	}
}