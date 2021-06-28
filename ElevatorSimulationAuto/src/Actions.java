import java.util.ArrayList;
import java.util.List;

public class Actions {
	public int maxPassengers; // Max number of passengers in elevator
	public int curPassengers = 0; // Current number of passengers in elevator
	public List<Passenger> passengersList = new ArrayList<Passenger>(); // List that contains all passengers currently inside the elevator
	public List<Passenger> waitingList = new ArrayList<Passenger>(); // List that contains all people who are waiting to get in the elevator
	public int currentFloor;
	
	public Actions(int maxPassengers) {
		this.maxPassengers = maxPassengers;
	}
	
	public synchronized void takeElevator(Passenger passenger) {
		while (this.curPassengers >= this.maxPassengers) {
			System.out.println(passenger.getName() + " is waiting for the elevator to become free on floor " + passenger.getFromFloor());
			if (!waitingList.contains(passenger)) {
				waitingList.add(passenger);
			}
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (passenger.getFromFloor() == currentFloor) {
			this.curPassengers++;
			System.out.println(passenger.getName() + " has entered the elevator at floor " + passenger.getFromFloor() + " and will get off at floor " + passenger.getToFloor());
			passenger.setEnteredState(true);
			if (waitingList.contains(passenger)) {
				waitingList.remove(passenger);
			}
			if (!passengersList.contains(passenger)) {
				passengersList.add(passenger);
			}
		}
    }
    
    public synchronized void leaveElevator() {
    	if (this.curPassengers > 0) {
    		this.curPassengers--;
    		for (int i = 0; i < passengersList.size(); i++) {
    			if (passengersList.get(i).getEnteredState() == true) {
    				System.out.println(passengersList.get(i).getName() + " has left the elevator at floor " + currentFloor);
    				passengersList.remove(i);
    			}
    		}
    	}
    	notifyAll();
    }
}