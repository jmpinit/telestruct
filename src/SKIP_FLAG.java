public class SKIP_FLAG extends Command
{
	public SKIP_FLAG(CPU h) {
		super(h, "SKIP IF FLAG NOT SET");
		
		params = new Parameter[1];
		params[0] = new Constant();
	}
	
	public void execute()
	{
		int index = home.getMemAtOffset(home.programCounter, 1);
		
		if(index>=0&&index<home.flags.length)
		{
			if(!home.flags[index])
			{
				int instruction = home.memory[home.programCounter];
				home.commands[instruction].gotoNextCommand();
			}
		}
			
		gotoNextCommand();
	}
}