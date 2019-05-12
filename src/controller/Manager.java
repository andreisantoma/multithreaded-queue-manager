package controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;
import model.*;
import view.Gui;

public class Manager extends Thread{
	
	private int simTime;
	private List<Client> clients;
	private List<Register> registers;
	private int minArrivingInterval;
	private int maxArrivingInterval;
	private AtomicInteger time = new AtomicInteger(1);
	private int peakTime = -1;
	
	
	public Manager(int t, int noOfClients, int noOfQueues, int minProcessingTime, int maxProcessingTime, int minArrivingInterval, int maxArrivingInterval) {
		this.simTime = t;
		
		clients = generateClients(noOfClients, minProcessingTime, maxProcessingTime);
		registers = new ArrayList<Register>();
		Register r;
		
		for (int i=0; i<noOfQueues; i++) {
			r = new Register();
			r.setName("Register" + (i+1));
			registers.add(r);
		}
		
		this.minArrivingInterval = minArrivingInterval;
		this.maxArrivingInterval = maxArrivingInterval;		
		
	}
	
	@Override
	public void run() {
		
		int maxsum = -1, sum = 0;
		
		Logger.log("");
		Logger.log("");
		Logger.log("Starting simulation...");
		Logger.log(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
		Logger.log("");
		startProcessing();
		int i = 0, interval = minArrivingInterval + (int)(Math.random()*((maxArrivingInterval-minArrivingInterval)+1)), k = 1;
		while(time.get() <= simTime) {
			try {
				Logger.log("------------------------------------Current simulation time: " + time + "------------------------------------");
				//System.out.println("------------------------------------Current simulation time: " + time + "------------------------------------");
				if(k == interval) {
					if(i<clients.size()) {
						Register f = getOptimalRegister();
						f.addToQueue(clients.get(i));
						Logger.log("Client " + clients.get(i).getName() + 
								"(" + clients.get(i).getProcessingTime() + " second(s) required service time) will be processed by "
								+ f.getName() + ". They have to wait for " + 
								(f.getWaitingTime()-clients.get(i).getProcessingTime()) + " second(s) before being served.");
						
						f.addToTotalWaitingTime(f.getWaitingTime()-clients.get(i).getProcessingTime());
						i++;
						k = 1;
						interval = minArrivingInterval + (int)(Math.random()*((maxArrivingInterval-minArrivingInterval)+1));
					}
				}
				else {
					k++;
				}
				
				sum = 0;
				for(Register it : registers) {
					it.showCurrentState();
					it.incrementTimes();
					sum += it.getNoOfClients();
					
				}
				
				if(sum > maxsum) {
					maxsum = sum;
					peakTime = time.get();
				}
				
				sleep(1000);
				time.incrementAndGet();
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		
		Logger.log("");
		Logger.log("Done");
		Logger.log("");
		NumberFormat formatter = new DecimalFormat("#0.00");
		for(Register it : registers) {
			Logger.log("Average waiting time for " + it.getName() + " was: " + 
					formatter.format(it.getAverageWaitingTime()) + " second(s). Service time was: " 
					+ it.getServiceTime() + " second(s). Empty time was: " + it.getEmptyTime() + " second(s).");
			it.interrupt();			
		}
		
		Logger.log("Peak time was achieved at: " + peakTime + " second(s)");
		
	}
	
	public void startProcessing() {
		for(Register it : registers) {
			it.start();
		}
	}
	
	public int getCurrentSimulationTime() {
		return time.get();
	}
	
	private Register getOptimalRegister() {
		Register f = new Register();
		int min = Integer.MAX_VALUE;
		
		for (Register p : registers) {
			int x = p.getWaitingTime();
			if (x < min) {
				f = p;
				min = x;
			}
		}		
		return f;
	}
	
	public static List<Client> generateClients(int n, int min, int max) {
		List<Client> list = new ArrayList<Client>();
		int name = 65, processingTime;
		for(int i=0; i<n; i++) {
			processingTime = min + (int)(Math.random()*((max-min)+1));
			list.add(new Client(Character.toString(name), processingTime));
			name++;
		}		
		return list;
	}
	
	public String getIthRegisterInfo(int i) {
		return registers.get(i).getInfo();
	}
	
	public int getNoOfRegisters() {
		return registers.size();
	}
	
	public boolean isIthRegisterAlive(int i) {
		return registers.get(i).isAlive();
	}
	
	public int getPeakTime() {
		return peakTime;
	}
	
	public String getIthRegisterAvgWTime(int i) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(registers.get(i).getAverageWaitingTime());
	}
	
	public String getIthRegisterServiceTime(int i) {
		return Integer.toString(registers.get(i).getServiceTime());
	}
	
	public String getIthRegisterEmptyTime(int i) {
		return Integer.toString(registers.get(i).getEmptyTime());
	}
	
	public static void main(String[] args) {
		Gui g = new Gui();
		g.showGui();
	}
}
