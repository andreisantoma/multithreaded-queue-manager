package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.*;

import javax.swing.*;

public class Gui extends JPanel{

	String[] labels = {"Simulation time: ", "No. of clients: ", "No. of queues: ", "Min. processing time: ", 
					   "Max. processing time: ", "Min. arriving interval: ", "Max. arriving interval: "};
	
	private JTextField[] tfarray = new JTextField[7];
	private JButton simulationButton = new JButton("Start simulation");
	private Manager m;
	
	
	public Gui() {
		this.setPreferredSize(new Dimension(300, 400));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		for(int i=0; i<7; i++) {
			tfarray[i] = new JTextField();
		}
		
		for(int i=0; i<7; i++) {
			addPanel(labels[i], tfarray[i]);
		}
		
		this.add(simulationButton);
		
		simulationButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					m = new Manager(Integer.parseInt(tfarray[0].getText()), 
							Integer.parseInt(tfarray[1].getText()), 
							Integer.parseInt(tfarray[2].getText()), 
							Integer.parseInt(tfarray[3].getText()), 
							Integer.parseInt(tfarray[4].getText()),
							Integer.parseInt(tfarray[5].getText()), 
							Integer.parseInt(tfarray[6].getText()));
					
					SecondaryGui sGui = new SecondaryGui(m);
					sGui.showGui();
					m.start();
					Thread t = new Thread(sGui);
					t.start();
					
				}
				catch(Exception e2) {
					JOptionPane.showMessageDialog(null, "Verificati datele de intrare", "Eroare", JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
		});
		
	}
	
	//adauga un JPanel pentru fiecare grup label+textfield
	public void addPanel(String text, JTextField tf) {
		JPanel p = new JPanel();
		p.add(new JLabel(text));		
		tf.setColumns(10);
		p.add(tf);
		p.add(Box.createRigidArea(new Dimension(0, 25)));
		this.add(p);
	}
	
	public void showGui() {
		JFrame f = new JFrame();
		f.setContentPane(this);
		f.setTitle("Queueing simulation");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.pack();
	}
	
}
