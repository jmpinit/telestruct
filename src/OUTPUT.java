public class OUTPUT extends Command
{
	public OUTPUT(CPU h) {
		super(h, "OUTPUT");
		
		params = new Parameter[2];
		params[0] = new Address();	//place to take value from
		params[1] = new Constant();	//index of output
	}
	
	public void execute()
	{
		int address = home.getMemAtOffset(home.programCounter, 1);
		int value = params[0].getVal(home, address);
		int index = home.getMemAtOffset(home.programCounter, 2);
		
		if(index<home.outputs.length)
			home.outputs[index] = value;
		
		gotoNextCommand();
	}
}