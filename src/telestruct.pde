import java.util.*;

private static final int DRAG_SPEED = 5;

//windows
MemoryFrame memFrame;
CommandFrame cmdFrame;
ControllerFrame ctrlFrame;
InfoFrame infoFrame;

int dragStartX, dragStartY;
int startCamX, startCamY;

World world;
TerrainGenerator god;

Robot rob;

void setup()
{
	size(640, 480);
	
	world = new World(512, 512);
	god = new TerrainGenerator(world);
	
	int numWalls = (int)Math.sqrt(world.getSizeX()*world.getSizeY())/2;
	int numResBlocks = (int)Math.sqrt(world.getSizeX()*world.getSizeY())/4;
	
	god.shotgun(numWalls, World.TYPE_WALL);
	god.shotgun(numResBlocks, World.TYPE_RES);
	
	//first player robot
	rob = new Robot(world, 2, 2, 100);
	
	world.addEnt(rob);
	
	memFrame = new MemoryFrame(rob.getCPU());
	cmdFrame = new CommandFrame(rob);
	ctrlFrame = new ControllerFrame(rob);
	infoFrame = new InfoFrame(world);
}

void draw()
{
	world.update();
	infoFrame.update();
	
	background(0);
	world.render();
}

void mousePressed()
{
	dragStartX = mouseX;
	dragStartY = mouseY;
	startCamX = world.getCamX();
	startCamY = world.getCamY();
	
	int selX = world.getWorldX(mouseX);
	int selY = world.getWorldY(mouseY);
	
	Entity ent = world.getEntAt(selX, selY);
	
	if(ent!=null)
	{
		if(ent.getClass()==Robot.class)
			cmdFrame.switchTarget((Robot)ent);
	}
}

void mouseDragged()
{
	int distX = mouseX-dragStartX;
	int distY = mouseY-dragStartY;
	world.setCamPos(startCamX-distX/DRAG_SPEED, startCamY-distY/DRAG_SPEED);
}

class TerrainGenerator
{
	World space;
	
	public TerrainGenerator(World w)
	{
		space = w;
	}
	
	//fills the world with clusters of the type
	public void shotgun(int shots, int type)
	{
		for(int i=0; i<shots; i++)
		{
			makeCluster((int)(Math.random()*space.getSizeX()), (int)(Math.random()*space.getSizeY()), type);
		}
	}
	
	public void makeCluster(int x, int y, int type)
	{
		for(int i=0; i<Math.random()*5+1; i++)
		{
			makeCircle((int)(Math.random()*10)-5+x, (int)(Math.random()*10)-5+y, (int)(Math.random()*5)+3, type);
		}
	}
	
	public void makeCircle(int x, int y, int r, int type)
	{
		for(int yDraw=-r; yDraw<r; yDraw++)
		{
			for(int xDraw=-r; xDraw<r; xDraw++)
			{
				if(dist(0, 0, xDraw, yDraw)<r)
					space.setBlock(x+xDraw, y+yDraw, type);
			}
		}
	}
}

class World
{
	private static final int BLOCK_SIZE = 20;
	
	private VectorSprite SPRITE_WALL;
	private VectorSprite SPRITE_RES;
	private VectorSprite SPRITE_ROB;
	
	public final static int DEFAULT_ENERGY = 100;
	
	public final static int TYPE_EMPTY = 0;
	public final static int TYPE_WALL = 1;
	public final static int TYPE_RES = 2;
	public final static int TYPE_OCCUPIED = 3;
	
	private int sizeX, sizeY;
	private float zoom;
	private int camX, camY;
	
	private int[][] blocks;
	private int[][] res;
	private Vector<Entity> entities;
	private Vector<Entity> victims;
	private Vector<Entity> babies;
	
	public World(int x, int y)
	{
		zoom = 1;
		sizeX = x;
		sizeY = y;
		
		blocks = new int[sizeX][sizeY];
		res = new int[sizeX][sizeY];
		
		entities = new Vector<Entity>();
		babies = new Vector<Entity>();
		victims = new Vector<Entity>();
		
		SPRITE_WALL = new VectorSprite();
		SPRITE_RES = new VectorSprite();
		
		SPRITE_WALL.loadSprite("sprites/wall.vsp");
		SPRITE_RES.loadSprite("sprites/res.vsp");
	}
	
	public void update()
	{
		for(Entity ent: entities)
		{
			ent.update();
		}
		
		//birth babies
		for(Entity baby: babies)
		{
			entities.add(baby);
		}
		babies.clear();
		
		//remove dead ents
		for(Entity victim: victims)
		{
			entities.remove(victim);
		}
		victims.clear();
	}
	
	public void drawMap()
	{
		background(0);
		for(int y=0; y<sizeY; y++)
		{
			for(int x=0; x<sizeX; x++)
			{
				if(res[x][y]>0)
				{
					stroke(0, 0, 255);
					point(x, y);
				} else if(blocks[x][y]!=TYPE_EMPTY)
				{
					stroke(255);
					point(x, y);
				}
			}
		}
	}
	
	public void render()
	{
		//draw the blocks
		strokeWeight(1);
		stroke(255);
		fill(255);
		
		scale(zoom);
		
		for(int y=0; y<height/(BLOCK_SIZE*zoom)+1; y++)
		{
			for(int x=0; x<width/(BLOCK_SIZE*zoom)+1; x++)
			{
				switch(getBlock(camX+x, camY+y))
				{
					case TYPE_WALL:	SPRITE_WALL.render();	break;
					case TYPE_RES:
						noStroke();
						fill(getResAt(camX+x, camY+y));
						rect(-BLOCK_SIZE/2, -BLOCK_SIZE/2, BLOCK_SIZE, BLOCK_SIZE);
						stroke(255);
						SPRITE_RES.render();
						break;
				}
				
				translate(BLOCK_SIZE, 0);
			}
			translate(-width/zoom-BLOCK_SIZE, BLOCK_SIZE);
		}
		translate(0, -height/zoom-BLOCK_SIZE);	//back to start
		
		//draw the entities
		for(Entity ent: entities)
		{
			int x = ent.getX();
			int y = ent.getY();
			int direction = ent.getDir();
			
			int endX = camX+(int)(width/(BLOCK_SIZE*zoom)+1);
			int endY = camY+(int)(width/(BLOCK_SIZE*zoom)+1);
			
			if(x>camX&&x<endX&&y>camY&&y<endY)
			{
				strokeWeight(1);
				if(ent.getClass()==Robot.class)
					if(((Robot)ent).highlight)
						strokeWeight(2);
					
				translate((ent.getX()-camX)*BLOCK_SIZE, (ent.getY()-camY)*BLOCK_SIZE);
				rotate(angleFromDir(direction));
				ent.render();
				rotate(-angleFromDir(direction));
				translate(-(ent.getX()-camX)*BLOCK_SIZE, -(ent.getY()-camY)*BLOCK_SIZE);
			}
		}
	}
	
	private float angleFromDir(int dir)
	{
		float angle = 0;
		
		switch(dir)
		{
			case Robot.DIR_LEFT:	angle = -PI/2;	break;
			case Robot.DIR_RIGHT:	angle = PI/2;	break;
			case Robot.DIR_DOWN:	angle = PI;	break;
		}
		
		return angle;
	}
	
	public int getBlock(int x, int y)
	{
		if(x>=0&&x<sizeX&&y>=0&&y<sizeY)
		{
			if(res[x][y]>0)
				return TYPE_RES;
			else
				return blocks[x][y];
		} else {
			return TYPE_WALL;
		}
	}
	
	public void setBlock(int x, int y, int type)
	{
		if(x>=0&&x<sizeX&&y>=0&&y<sizeY)
		{
			if(type==TYPE_RES)
			{
				res[x][y] = DEFAULT_ENERGY;
			} else {
				blocks[x][y] = type;
			}
		}
	}
	
	//returns energy in resource block
	public int getResAt(int x, int y)
	{
		if(x>=0&&x<sizeX&&y>=0&&y<sizeY)
		{
			return res[x][y];
		} else {
			return 0;
		}
	}
	
	//make a resource block with specified energy
	public void setResAt(int x, int y, int energy)
	{
		if(x>=0&&x<sizeX&&y>=0&&y<sizeY)
		{
			res[x][y] = energy;
		}
	}
	
	public Entity getEntAt(int x, int y)
	{
		for(Entity ent: entities)
		{
			if(ent.getX()==x&&ent.getY()==y)
				return ent;
		}
		
		return null;
	}
	
	public VectorSprite getSprite() { return new VectorSprite(); }
	
	public void addEnt(Entity ent)
	{
		babies.add(ent);
	}
	
	public void kill(Entity victim)
	{
		victims.add(victim);
	}
	
	public long getTotalEnergy()
	{
		long total = 0;
		for(Entity ent: entities)
		{
			if(ent.getClass()==Robot.class)
				total += ((Robot)ent).getEnergy();
		}
		
		return total;
	}
	
	public long getTotalRes()
	{
		long total = 0;
		for(int y=0; y<sizeY; y++)
		{
			for(int x=0; x<sizeY; x++)
			{
				total += res[x][y];
			}
		}
		
		return total;
	}
	
	//screen position to world position
	public int getWorldX(int x)
	{
		return camX+(int)((x+(BLOCK_SIZE*zoom)/2)/(BLOCK_SIZE*zoom));
	}
	
	public int getWorldY(int y)
	{
		return camY+(int)((y+(BLOCK_SIZE*zoom)/2)/(BLOCK_SIZE*zoom));
	}
	
	public int getCamX() { return camX; }
	public int getCamY() { return camY; }
	
	public void setCamPos(int x, int y)
	{
		camX = x;
		camY = y;
	}
	
	public void setZoom(float z)
	{
		zoom = z;
	}
	
	public int getSizeX() { return sizeX; }
	public int getSizeY() { return sizeY; }
}

class VectorSprite
{
	Vector<VecStruct> structures;
	
	public VectorSprite()
	{
		structures = new Vector<VecStruct>();
	}
	
	public void render()
	{
		for(VecStruct struct: structures)
		{
			if(struct.visible)
				struct.render();
		}
	}
	
	public void loadSprite(String filename)
	{
		structures = new Vector<VecStruct>();
		
		String[] raw = loadStrings(filename);
		
		VecStruct currentStruct = new VecStruct();
		
		for(int i=0; i<raw.length; i++)
		{
			if(raw[i].charAt(0)=='n')
			{
				String[] nameParts = raw[i].split(":");
				currentStruct.name = nameParts[1];
				
				if(i!=0)
				{
					structures.add(currentStruct);
					currentStruct = new VecStruct();
				}
			} else {
				String parts[] = raw[i].split(",");
				
				Line ln = new Line();
				
				ln.x1 = Integer.parseInt(parts[0]);
				ln.y1 = Integer.parseInt(parts[1]);
				ln.x2 = Integer.parseInt(parts[2]);
				ln.y2 = Integer.parseInt(parts[3]);
				
				currentStruct.addLine(ln);
			}
		}
		
		structures.add(currentStruct);
	}
	
	public void setVisible(String name, boolean val)
	{
		for(VecStruct struct: structures)
		{
			if(struct.name==name)
				struct.visible = val;
		}
	}
	
	class VecStruct
	{
		Vector<Line> lines;
		
		public String name = "default";
		public boolean visible;
		
		public VecStruct()
		{
			lines = new Vector<Line>();
			visible = true;
		}
		
		public void render()
		{
			beginShape(LINES);
			for(Line ln: lines)
			{
				vertex(ln.x1, ln.y1);
				vertex(ln.x2, ln.y2);
			}
			endShape();
		}
		
		public void addLine(Line ln)
		{
			lines.add(ln);
		}
	}
	
	class Line
	{
		public int x1, y1, x2, y2;
	}
}
