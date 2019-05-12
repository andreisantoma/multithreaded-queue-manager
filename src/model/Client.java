package model;

public class Client {
	
	private String name;
	private int processingTime;	
		
	public Client(String name, int processingTime) {
		this.name = name;
		this.processingTime = processingTime;
	}

	public int getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	public String getName() {
		return name;
	}
}
