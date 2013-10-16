public class CLR_FLAG extends Command
{
	public CLR_FLAG(CPU h) {
		super(h, "CLEAR FLAG");
		
		params = new Parameter[1];
		params[0] = new Constant();
	}
	
	public void execute()
	{
		int index = home.getMemAtOffset(home.programCounter, 1);
		
		if(index>=0&&index<home.flags.length)
		{
			home.flags[index] = false;
		}
			
		gotoNextCommand();
	}
}