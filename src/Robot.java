public class Robot extends Entity
{
	//actions
	private static final int DO_NOTHING		= 0;
	private static final int M_FORWARD		= 1;
	private static final int M_BACKWARD		= 2;
	private static final int T_LEFT			= 3;
	private static final int T_RIGHT		= 4;
	private static final int SHOOT			= 5;
	private static final int COMMUNICATE	= 6;
	private static final int DRILL			= 7;
	private static final int BUILD_WALL		= 8;
	private static final int SCAN			= 9;
	private static final int GET_RANDOM		= 10;
	
	//outputs
	private static final int OUT_SCAN_X		= 1;
	private static final int OUT_SCAN_Y		= 2;
	private static final int OUT_MESSAGE	= 3; 
	
	//inputs
	private static final int IN_TOUCH			= 0;
	private static final int IN_RANGE			= 1;
	private static final int IN_SCANNED_TYPE	= 2;
	private static final int IN_MESSAGE			= 3;
	private static final int IN_RANDOM			= 4;
	
	//flags
	public static final int F_HURT			= 0;
	public static final int F_MESSAGE		= 1;
	public static final int F_ENERGY		= 2;
	public static final int F_T_LEFT		= 3; 
	public static final int F_T_RIGHT		= 4;
	public static final int F_M_FORWARD		= 5;
	public static final int F_M_BACKWARD	= 6;
	public static final int F_SHOOT			= 7;
	public static final int F_DRILL			= 8;
	
	//attributes
	private static final int MAX_HEALTH		= 100;
	private static final int DAMAGE			= 10;
	private static final int SCAN_RANGE		= 8;
	
	private static final int SHOOT_PENALTY	= 1;
	private static final int BUILD_PENALTY	= 5;
	private static final int BABY_PENALTY	= 10;
	
	private CPU processor;
	private int energy;
	public boolean pause, highlight;

	public Robot(Rob_The_Builder.World w, int xPos, int yPos, int e)
	{
		x = xPos;
		y = yPos;
		direction = DIR_UP;
		
		health = MAX_HEALTH;
		energy = e;
		
		processor = new CPU(128, 5, 5, 9);
		
		//simple program
		int[] control_prog = {
			9, 8,
			T_LEFT, T_RIGHT, M_FORWARD, M_BACKWARD, SHOOT, DRILL,
			17, F_T_LEFT,		//skip if not turn left
			9, 34,
			17, F_T_RIGHT,		//skip if not turn right
			9, 41,
			17, F_M_FORWARD,	//skip if not move forward
			9, 48,
			17, F_M_BACKWARD,	//skip if not move backward
			9, 55,
			17, F_SHOOT,		//skip if not shoot
			9, 62,
			17, F_DRILL,		//skip if not drill
			9, 69,
			9, 8,				//goto start
			
			16, 2, 0,			//turn left
			18, F_T_LEFT,
			9, 8,				//goto start
			
			16, 3, 0,			//turn right
			18, F_T_RIGHT,
			9, 8,				//goto start
			
			16, 4, 0,			//move forward
			18, F_M_FORWARD,
			9, 8,				//goto start
			
			16, 5, 0,			//move backward
			18, F_M_BACKWARD,
			9, 8,				//goto start
			
			16, 6, 0,			//shoot
			18, F_SHOOT,
			9, 8,				//goto start
			
			16, 7, 0,			//drill
			18, F_DRILL,
			9, 8,				//goto start
		};
		
		CPU.loadData(processor, control_prog);
		
		space = w;
		space.setBlock(x, y, TYPE_OCCUPIED);
		
		sprite = space.getSprite();
		sprite.loadSprite("sprites/robot.vsp");
	}
	
	public void update()
	{
		if(!pause)
		{
			//update inputs
			processor.inputs[IN_TOUCH] = space.getBlock(dirToX(direction), dirToY(direction));	//block type in front
			processor.inputs[IN_RANGE] = range(x, y, dirToX(direction), dirToY(direction));		//distance in front
			
			//update processor
			processor.executeNext();
			
			//update outputs
			switch(processor.outputs[0])
			{
				case M_FORWARD:		moveForward();	break;
				case M_BACKWARD:	moveBackward();	break;
				case T_LEFT:		turnLeft();		break;
				case T_RIGHT:		turnRight();	break;
				case SHOOT:			shoot();		break;
				case COMMUNICATE:	communicate();	break;
				case DRILL:			drill();		break;
				case BUILD_WALL:	build();		break;
				case SCAN:			scan();			break;
			}
			
			//clear processor outputs
			for(int i=0; i<processor.outputs.length; i++)
			{
				processor.outputs[i] = 0;
			}
		}
		
		if(energy<=0)
		{
			space.setBlock(x, y, TYPE_WALL);
			space.kill(this);
		}
		
		if(health<=0)
		{
			space.setBlock(x, y, TYPE_EMPTY);
			space.setResAt(x, y, energy);
			space.kill(this);
		}
	}
	
	public static void copyMem(CPU source, CPU destination)
	{
		if(source.memory.length>destination.memory.length)
		{
			for(int i=0; i<destination.memory.length; i++)
			{
				destination.memory[i] = source.memory[i];
			}
		} else {
			for(int i=0; i<source.memory.length; i++)
			{
				destination.memory[i] = source.memory[i];
			}
		}
	}
	
	public CPU getCPU()
	{
		return processor;
	}
	
	public void shoot()
	{
		space.addEnt(new Bullet(space, x+dirToX(direction), y+dirToY(direction), direction, DAMAGE));
		energy -= SHOOT_PENALTY;
	}
	
	public void communicate()
	{
		space.addEnt(new Packet(space, x+dirToX(direction), y+dirToY(direction), direction, processor.outputs[OUT_MESSAGE]));
	}
	
	public void drill()
	{
		int total = space.getResAt(x+dirToX(direction), y+dirToY(direction));
		if(total>0)
		{
			energy++;
			space.setResAt(x+dirToX(direction), y+dirToY(direction), total-1);
			processor.flags[F_ENERGY] = true;
		}
	}
	
	public void build()
	{
		if(energy>=BUILD_PENALTY)
		{
			if(space.getBlock(x+dirToX(direction), y+dirToY(direction))==TYPE_EMPTY)
			{
				space.setBlock(x+dirToX(direction), y+dirToY(direction), TYPE_WALL);
				energy -= BUILD_PENALTY;
			}
		}
	}
	
	public void procreate()
	{
		if(energy>=BABY_PENALTY)
		{
			if(space.getBlock(x+dirToX(direction), y+dirToY(direction))==TYPE_EMPTY)
			{
				Robot baby = new Robot(space, x+dirToX(direction), y+dirToY(direction), BABY_PENALTY);
				Robot.copyMem(processor, baby.getCPU());
				space.addEnt(baby);
				energy -= BABY_PENALTY;
			}
		}
	}
	
	public void scan()
	{
		int xPos = processor.outputs[OUT_SCAN_X];
		int yPos = processor.outputs[OUT_SCAN_Y];
		
		int type;
		if(xPos>-SCAN_RANGE&&xPos<SCAN_RANGE&&yPos>-SCAN_RANGE&&yPos<SCAN_RANGE)
			type = space.getBlock(x+xPos, y+yPos);
		else
			type = -1;
		
		processor.inputs[IN_SCANNED_TYPE] = type;
	}
	
	private int range(int xPos, int yPos, int xVel, int yVel)
	{
		int distance = 0;
		while(space.getBlock(xPos, yPos)==TYPE_EMPTY)
		{
			xPos += xVel;
			yPos += yVel;
			distance++;
		}
		
		return distance;
	}
	
	public void hurt(int amount)
	{
		super.hurt(amount);
		processor.flags[F_HURT] = true;
	}
	
	public void message(int val)
	{
		processor.inputs[IN_MESSAGE] = val;
		processor.flags[F_MESSAGE] = true;
	}
	
	public int getEnergy()
	{
		return energy;
	}
}