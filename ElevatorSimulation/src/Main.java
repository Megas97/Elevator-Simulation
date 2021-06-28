public class Main {
	public static void main (String[] arg) {
		int maxPassengers = 1;
		int numberOfFloors = 3;
		Actions actions = new Actions(maxPassengers);
		Elevator elevator = new Elevator(numberOfFloors, actions);
        elevator.start();
    }
}