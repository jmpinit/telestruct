import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControllerFrame extends JFrame implements ActionListener
{
	private Robot robby;
	
	JButton btnForward, btnBackward, btnLeft, btnRight, btnShoot, btnDrill;
	
	public ControllerFrame(Robot r)
	{
		super("Controller");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(75, 250);
		setVisible(true);
		
		btnForward = new JButton("Forward");
		btnBackward = new JButton("Backward");
		btnLeft = new JButton("Left");
		btnRight = new JButton("Right");
		btnShoot = new JButton("Shoot");
		btnDrill = new JButton("Drill");
		
		JToolBar barControls = new JToolBar(SwingConstants.VERTICAL);
		barControls.add(btnForward);
		barControls.add(btnBackward);
		barControls.add(btnLeft);
		barControls.add(btnRight);
		barControls.add(btnShoot);
		barControls.add(btnDrill);
		
		add(barControls, BorderLayout.WEST);
		
		//add button listeners
		btnForward.addActionListener(this);
		btnBackward.addActionListener(this);
		btnLeft.addActionListener(this);
		btnRight.addActionListener(this);
		btnShoot.addActionListener(this);
		btnDrill.addActionListener(this);
		
		btnForward.setActionCommand("Forward");
		btnBackward.setActionCommand("Backward");
		btnLeft.setActionCommand("Left");
		btnRight.setActionCommand("Right");
		btnShoot.setActionCommand("Shoot");
		btnDrill.setActionCommand("Drill");
		
		switchTarget(r);
	}
	
	public void switchTarget(Robot newRobby)
	{
		if(robby!=null)
			robby.highlight = false;
			
		robby = newRobby;
		robby.highlight = true;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		String command = event.getActionCommand();
		
		if(command=="Forward")
		{
			robby.getCPU().flags[Robot.F_M_FORWARD] = true;
		} else if(command=="Backward")
		{
			robby.getCPU().flags[Robot.F_M_BACKWARD] = true;
		} else if(command=="Left")
		{
			robby.getCPU().flags[Robot.F_T_LEFT] = true;
		} else if(command=="Right")
		{
			robby.getCPU().flags[Robot.F_T_RIGHT] = true;
		} else if(command=="Shoot")
		{
			robby.getCPU().flags[Robot.F_SHOOT] = true;
		} else if(command=="Drill")
		{
			robby.getCPU().flags[Robot.F_DRILL] = true;
		}
	}
}