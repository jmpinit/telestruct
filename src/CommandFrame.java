import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CommandFrame extends JFrame implements ActionListener
{
	private Robot robby;
	
	JToggleButton btnPlay;
	JToggleButton btnPause;
	
	public CommandFrame(Robot r)
	{
		super("Commander");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(100, 100);
		setVisible(true);
		
		ImageIcon icoPlay = new ImageIcon("icons\\play.png");
		ImageIcon icoPause = new ImageIcon("icons\\pause.png");
		
		btnPlay = new JToggleButton(icoPlay, false);
		btnPause = new JToggleButton(icoPause, true);
		
		JToolBar barControls = new JToolBar(SwingConstants.VERTICAL);
		barControls.add(btnPlay);
		barControls.add(btnPause);
		
		add(barControls, BorderLayout.WEST);
		
		//add button listeners
		btnPlay.addActionListener(this);
		btnPause.addActionListener(this);
		
		btnPlay.setActionCommand("Play");
		btnPause.setActionCommand("Pause");
		
		switchTarget(r);
	}
	
	public void switchTarget(Robot newRobby)
	{
		if(robby!=null)
			robby.highlight = false;
			
		robby = newRobby;
		robby.highlight = true;
		
		if(robby.pause)
		{
			btnPlay.setSelected(false);
			btnPause.setSelected(true);
		} else {
			btnPlay.setSelected(true);
			btnPause.setSelected(false);
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		String command = event.getActionCommand();
		
		if(command=="Play")
		{
			robby.pause = false;
			
			btnPlay.setSelected(true);
			btnPause.setSelected(false);
		} else if(command=="Pause")
		{
			robby.pause = true;
		
			btnPlay.setSelected(false);
			btnPause.setSelected(true);
		}
	}
}