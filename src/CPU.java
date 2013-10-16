public class CPU
{
	public int memory[];
	public int programCounter;
	
	public boolean flags[];
	public int inputs[];
	public int outputs[];

	public Command[] commands = {
		new ADD(this),		//0
		new SUB(this),		//1
		new MULT(this),		//2
		new DIV(this),		//3
		new AND(this),		//4
		new OR(this),		//5
		new NOR(this),		//6
		new NOT(this),		//7
		new MOV(this),		//8
		new GOTO(this),		//9
		new RJMP(this),		//10
		new SKIP_GR(this),	//11
		new SKIP_LESS(this),//12
		new SKIP_EQ(this),	//13
		new SKIP_NEQ(this),	//14
		new INPUT(this),	//15
		new OUTPUT(this),	//16
		new SKIP_FLAG(this),//17
		new CLR_FLAG(this)	//18
	};
	
	public CPU(int s, int numIn, int numOut, int numFlags)
	{
		memory = new int[s];
		programCounter = 0;
		
		inputs = new int[numIn];
		outputs = new int[numOut];
		flags = new boolean[numFlags];
	}
	
	public void executeNext()
	{
		int instruction = memory[programCounter];
		commands[instruction].execute();			//moves forward automatically
		
		if(programCounter>=memory.length)
			programCounter %= memory.length;
		
		if(programCounter<0)
		{
			if(Math.abs(programCounter)>=memory.length)
				programCounter = memory.length-programCounter%memory.length;
			else
				programCounter = memory.length+programCounter;
		}
	}
	
	public void printMem()
	{
		for(int i=0; i<memory.length; i++)
		{
			if(i==programCounter)
				System.out.println(">"+memory[i]);
			else
				System.out.println("_"+memory[i]);
		}
	}
	
	//loops around both ways
	public int getMemAtOffset(int start, int offset)
	{
		return getMemAt(start+offset);
	}
	
	public int getMemAt(int address)
	{
		if(address>0)
		{
			return memory[address%memory.length];
		} else {
			if(Math.abs(address)>=memory.length)
				return memory[memory.length-Math.abs(address)%memory.length];
			else
				return memory[memory.length+address];
		}
	}
	
	public void setMemAt(int address, int value)
	{
		if(address>0)
		{
			memory[address%memory.length] = value;
		} else {
			if(Math.abs(address)>=memory.length)
				memory[memory.length-Math.abs(address)%memory.length] = value;
			else
				memory[memory.length+address] = value;
		}
	}
	
	public int getInput(int i)
	{
		if(i<inputs.length)
			return inputs[i];
		else
			return 0;
	}
	
	public void setOutput(int i, int val)
	{
		if(i<outputs.length)
			outputs[i] = val;
	}
	
	public static void loadData(CPU target, int[] data)
	{
		if(data.length>target.memory.length)
		{
			for(int i=0; i<target.memory.length; i++)
			{
				target.memory[i] = data[i];
			}
		} else {
			for(int i=0; i<data.length; i++)
			{
				target.memory[i] = data[i];
			}
		}
	}
}