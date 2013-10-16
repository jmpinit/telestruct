import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MemoryFrame extends JFrame implements ActionListener
{
	JLabel lblAddress, lblValue, lblMacro;
	TextField txtAddress, txtValue, txtMacro;
	JButton btnSet, btnMacro;
	
	private CPU processor;
	int address;
	
	public MemoryFrame(CPU p)
	{
		super("Memory");
		
		processor = p;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(100, 100);
		setVisible(true);
		
		//create labels
		lblAddress = new JLabel("Address:");
		lblValue = new JLabel("Value:");
		lblMacro = new JLabel("Macro:");
		
		//create text fields
		txtAddress = new TextField("0");
		txtValue = new TextField();
		txtMacro = new TextField();
		
		//create buttons
		btnSet = new JButton("Set");
		btnMacro = new JButton("Macro");
		btnSet.addActionListener(this);
		btnMacro.addActionListener(this);
		btnSet.setActionCommand("Set");
		btnMacro.setActionCommand("Macro");
		
		//create panel and layout
		JPanel paneEdit = new JPanel();
		GridLayout layout = new GridLayout(0,4);
		paneEdit.setLayout(layout);
		
		paneEdit.add(lblAddress);
		paneEdit.add(txtAddress);
		paneEdit.add(lblValue);
		paneEdit.add(txtValue);
		paneEdit.add(lblMacro);
		paneEdit.add(txtMacro);
		paneEdit.add(btnMacro);
		paneEdit.add(btnSet);
		
		layout.setHgap(4);
		layout.setVgap(4);
		layout.layoutContainer(paneEdit);
		
		add(paneEdit);
		
		updateFields();
	}
	
	public void updateFields()
	{
		//address = Integer.parseInt(txtAddress.getText());
		
		if(address>=0&&address<processor.memory.length)
		{
			txtValue.setText(Integer.toString(processor.memory[address]));
		} else {
			txtValue.setText("invalid address");
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		String command = event.getActionCommand();
		
		if(command=="Set")
		{
			address = Integer.parseInt(txtAddress.getText());
			int value = Integer.parseInt(txtValue.getText());
			if(address>=0&&address<processor.memory.length)
			{
				processor.memory[address] = value;
			}
		} else if(command=="Macro")
		{
			
		}
	}
}