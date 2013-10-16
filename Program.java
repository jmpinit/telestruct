import java.util.*;
import java.io.*;

public class Program
{
	List code;

	public Program()
	{
		code = new ArrayList();
	}
	
	public void addLine(byte[] line)
	{
		code.add(line);
	}
	
	public byte[] getLine(int line)
	{
		return code.get(line);
	}
	
	public static Program getProgramFromFile(String source)
	{
		Program prog = new Program();
		
		System.out.println("loading");
		
		try{		
			//set up file interface
			FileInputStream file = new FileInputStream(source);
			
			//read file
			int length = file.available();
			byte[] data = new byte[length];
			file.read(data);
			
			file.close();
			
			//extract lines
			String text = new String(data);
			String[] lines = text.split("\n");
			
			System.out.println("extracted lines");
			
			//parse memory construct
			prog = new Program();
			
			System.out.println("created program shell");
			for(int i=0; i<lines.length; i++) {
				String line = lines[i].replaceAll("\\W","");
				
				byte[] line_vals = new byte[line.length()];
				for(int k=0; k<line.length(); k++)
				{
					//prog.addToLine(i, (byte)(line.charAt(k)));
					char current = line.charAt(k);
					if(current>='0'&&current<='9')
					{
						current -= '0';
						byte val = (byte)current;
						line_vals[k] = val;
						System.out.print(val+",");
					}
				}
				
				prog.addLine(line_vals);
				System.out.println();
			}
		} catch(Exception e) {
			System.out.println(e.toString());
			System.out.println("failed");
		}
		
		return prog;
	}
}