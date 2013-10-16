public class SKIP_NEQ extends Command
{
	public SKIP_NEQ(CPU h) {
		super(h, "SKIP IF NOT EQUAL");
		
		params = new Parameter[2];
		params[0] = new Address();
		params[1] = new Address();
	}
	
	public void execute()
	{
		int[] values = getValues();
		
		if(values[1]!=values[0])
		{
			gotoNextCommand();
			int instruction = home.memory[home.programCounter];
			home.commands[instruction].gotoNextCommand();
		}
			
		gotoNextCommand();
	}
}