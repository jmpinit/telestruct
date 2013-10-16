public class RJMP extends Command
{
	public RJMP(CPU h) {
		super(h, "RJMP");
		
		params = new Parameter[1];
		params[0] = new Constant();
	}
	
	public void execute()
	{
		int relAddress = home.getMemAtOffset(home.programCounter, 1);
		
		//move to position
		home.programCounter += relAddress;
	}
}