public class Passenger extends Thread {
	@SuppressWarnings("unused")
	private String name;
	private Actions actions;
	private boolean hasEntered = false;
	private int fromFloor;
	private int toFloor;
	
	Passenger(String name, Actions actions) {
		this.name = name;
		super.setName(name);
		this.actions = actions;
	}
	
	public void setEnteredState(boolean state) {
		this.hasEntered = state;
	}
	
	public void setFromFloor(int floor) {
		this.fromFloor = floor;
	}
	
	public void setToFloor(int floor) {
		this.toFloor = floor;
	}
	
	public boolean getEnteredState() {
		return this.hasEntered;
	}
	
	public int getFromFloor() {
		return this.fromFloor;
	}
	
	public int getToFloor() {
		return this.toFloor;
	}
	
	public void run() {
		actions.takeElevator(this);
	}
}