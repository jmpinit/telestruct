public class GOTO extends Command
{
	public GOTO(CPU h) {
		super(h, "GOTO");
		
		params = new Parameter[1];
		params[0] = new Constant();
	}
	
	public void execute()
	{
		int address = home.getMemAtOffset(home.programCounter, 1);
		
		//move to position
		home.programCounter = address;
	}
}