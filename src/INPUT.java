public class INPUT extends Command
{
	public INPUT(CPU h) {
		super(h, "INPUT");
		
		params = new Parameter[2];
		params[0] = new Address();	//place to put value
		params[1] = new Constant();	//index of input
	}
	
	public void execute()
	{
		int address = home.getMemAtOffset(home.programCounter, 1);
		int index = home.getMemAtOffset(home.programCounter, 2);
		
		int value = 0;
		if(index<home.inputs.length)
			value = home.inputs[index];
		
		home.setMemAt(address, value);
		
		gotoNextCommand();
	}
}