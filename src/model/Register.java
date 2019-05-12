package model; 

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Register extends Thread{
	

	private AtomicInteger waitingTime = new AtomicInteger();
	private BlockingQueue<Client>clientQueue;
	private int totalWaitingTime = 0;
	private int numberOfClientsServed = 0;
	private int emptyTime = 0;
	private int serviceTime = 0;
	
	
	public Register() {
		clientQueue = new LinkedBlockingQueue<Client>();
		waitingTime.set(0);
	}
	
	public synchronized void addToQueue(Client c) {
		clientQueue.add(c);
		waitingTime.addAndGet(c.getProcessingTime());
		notifyAll();
	}
	
	public synchronized int removeFromQueue() throws InterruptedException{
		if(clientQueue.size() == 0)
			wait();
		
		Client c = clientQueue.peek();
		//System.out.println("Client " + c.getName() + " is being processed by " + getName());
		notifyAll();
		return c.getProcessingTime();
	}	
	
	@Override
	public void run() {
		try {
			while(true) {
				int x = removeFromQueue();
				numberOfClientsServed++;
				for (int i=0; i<x; i++) {
					Thread.sleep(1000);
					waitingTime.decrementAndGet();
				}
				Logger.log(getName() + ": finished processing client " + clientQueue.peek().getName() + ".");				
				clientQueue.poll();
			}
		}
		catch(InterruptedException e) {
			//e.printStackTrace();
			Logger.log(getName() + " is closing..");
		}		
	}
	
	public void showCurrentState() {		
		if(clientQueue.isEmpty()) {
			Logger.log(getName() + ": currently free... Waiting for new clients.");
		}
		else {
			Logger.log(getName() + ": processing client " + clientQueue.peek().getName() + "..."); 		
		}		
	}
	
	public String getInfo() {
		if(isAlive()) {
			if(clientQueue.isEmpty()) {
				return " currently free... Waiting for new clients.";
			}
			else {
				return " processing client " + clientQueue.peek().getName() + "..."; 		
			}	
		}
		else {
			return " Closed.";
		}
	}
	
	public void addToTotalWaitingTime(int x) {
		totalWaitingTime += x;
	}
	
	public double getAverageWaitingTime() {
		if (totalWaitingTime == 0)
			return 0;
		return (double)totalWaitingTime/numberOfClientsServed;
	}
	
	public void incrementTimes() {
		if(clientQueue.isEmpty())
			emptyTime++;
		else
			serviceTime++;
	}
	
	public int getEmptyTime() {
		return this.emptyTime;
	}
	
	public int getNoOfClients() {
		return clientQueue.size();
	}
	public int getServiceTime() {
		return this.serviceTime;
	}
	
	public int getWaitingTime(){
		return this.waitingTime.get();
	}
}
