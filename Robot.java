import java.util.*;

public class Robot extends Entity
{
	private static final int MEM_SIZE = 64; //lines of code
	private static final int PORT_SIZE = 4;

	Program myProg;
	
	int pc;
	
	public Robot(int x, int y, int d)
	{
		xPos = x;
		yPos = y;
		depth = d;
		
		pc = 0;
		
		myProg = Program.getProgramFromFile("test.prg");
	}
	
	public void update()
	{
		byte line = myProg.getLine(pc);
		
		switch(line[0])
		{
			case 1:		//GOTO
				pc += 
				break;
			case 2:		//SET
				break;
			case 3:		//ADD
				break;
			case 4:		//SUB
				break;
			case 5:		//MULT
				break;
			case 6:		//DIV
				break;
			case 7:		//IF_EQ
				break;
			case 8:		//IF_GR
				break;
			case 9:		//IF_LT
				break;
			case 10:	//IF_GREQ
				break;
			case 11:	//IF_LTEQ
				break;
			case 12:	//FUNCTION
				break;
			case 255:	//REBOOT
		}
	}
}