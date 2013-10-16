import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InfoFrame extends JFrame
{
	private Rob_The_Builder.World world;
	JLabel lblMined;
	JLabel lblRes;
	
	public InfoFrame(Rob_The_Builder.World w)
	{
		super("Information");
		
		world = w;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(100, 100);
		setVisible(true);
		
		JPanel paneInfo = new JPanel();
		JPanel paneInfoText = new JPanel();
		JPanel paneInfoVal = new JPanel();
		
		BoxLayout layoutInfo = new BoxLayout(paneInfo, BoxLayout.Y_AXIS);
		BoxLayout layoutInfoText = new BoxLayout(paneInfoText, BoxLayout.Y_AXIS);
		BoxLayout layoutInfoVal = new BoxLayout(paneInfoVal, BoxLayout.Y_AXIS);
		
		paneInfo.setLayout(layoutInfo);
		paneInfoText.setLayout(layoutInfoText);
		paneInfoVal.setLayout(layoutInfoVal);
		
		JLabel txtMined = new JLabel("Mined energy:", JLabel.LEFT);
		lblMined = new JLabel();
		JLabel txtRes = new JLabel("Unmined energy:", JLabel.LEFT);
		lblRes = new JLabel();
		
		paneInfo.add(txtMined);
		paneInfo.add(lblMined);
		paneInfo.add(txtRes);
		paneInfo.add(lblRes);
		
		add(paneInfo, BorderLayout.WEST);
	}
	
	public void update()
	{
		lblMined.setText(Long.toString(world.getTotalEnergy()));
		lblRes.setText(Long.toString(world.getTotalRes()));
	}
}