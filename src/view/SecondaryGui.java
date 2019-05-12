package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import controller.*;
import model.Logger;

public class SecondaryGui extends JPanel implements Runnable{
	
	private Manager m;	
	private JTextField[] tfarray = new JTextField[10];
	private JTextField tfTime = new JTextField();
	private JTextField tfPeak = new JTextField();
	private JPanel peakPanel = new JPanel();
	private int noOfRegisters;
	private JButton stopButton = new JButton("Stop");
	private boolean hasToStop = false;
	
	
	
	public SecondaryGui(Manager m) {
		this.m = m;
		this.setPreferredSize(new Dimension(400, 400));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.noOfRegisters = m.getNoOfRegisters();
		
		for(int i=0; i<noOfRegisters; i++) {
			tfarray[i] = new JTextField();
		}
		
		addPanel("Simulation time: ", tfTime);
		
		for(int i=0; i<noOfRegisters; i++) {
			addPanel(i, tfarray[i]);
		}
		
		peakPanel = addPanel("Peak time: ", tfPeak);
		
		this.add(stopButton);
		
	}
	
	//adauga un JPanel pentru fiecare grup label+textfield
	public void addPanel(int index, JTextField tf) {
		JPanel p = new JPanel();
		p.add(new JLabel("Register " + (index+1) + ":"));		
		tf.setColumns(25);
		p.add(tf);
		p.add(Box.createRigidArea(new Dimension(0, 25)));
		this.add(p);
	}
	
	public JPanel addPanel(String text, JTextField tf) {
		JPanel p = new JPanel();
		p.add(new JLabel(text));		
		tf.setColumns(2);
		p.add(tf);
		p.add(Box.createRigidArea(new Dimension(0, 25)));
		this.add(p);
		return p;
	}
	
	public void showGui() {
		JFrame f = new JFrame();
		f.setContentPane(this);
		f.setTitle("Visualization");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setResizable(false);
		f.pack();
	}

	public void run() {
		try {
			
			stopButton.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					hasToStop = true;
					
				}
			});
			for(;;) {
				Thread.sleep(100);
				if(m.isIthRegisterAlive(0)) {
					peakPanel.setVisible(false);
					tfTime.setText(Integer.toString(m.getCurrentSimulationTime()));
					
					for (int i=0; i<noOfRegisters; i++) {
						tfarray[i].setText(m.getIthRegisterInfo(i));
					}
				
					if(hasToStop) {
						throw new InterruptedException();
					}
					Thread.sleep(400);
				}
				else {
					peakPanel.setVisible(true);
					tfPeak.setText(Integer.toString(m.getPeakTime()));
					if(hasToStop) {
						throw new InterruptedException();
					}
					
					Thread.sleep(2000);
					
					for (int i=0; i<noOfRegisters; i++) {
						tfarray[i].setText(m.getIthRegisterInfo(i));
					}
					
					if(hasToStop) {
						throw new InterruptedException();
					}
					
					Thread.sleep(2000);
					
					for (int i=0; i<noOfRegisters; i++) 
						tfarray[i].setText("Average waiting time: " + m.getIthRegisterAvgWTime(i) +" second(s).");
					
					if(hasToStop) {
						throw new InterruptedException();
					}
						
					Thread.sleep(2000); 
					
					
					for (int i=0; i<noOfRegisters; i++) 
						tfarray[i].setText("Service time: " + m.getIthRegisterServiceTime(i) +" second(s).");
					
					if(hasToStop) {
						throw new InterruptedException();
					}
					
					
					Thread.sleep(2000);
					
					for (int i=0; i<noOfRegisters; i++) 
						tfarray[i].setText("Empty time: " + m.getIthRegisterEmptyTime(i) +" second(s).");					
				}
			}
		}
		catch(InterruptedException e2) {
			Logger.log("Visualization frame thread is stopping..");
		}
		
	}

}
