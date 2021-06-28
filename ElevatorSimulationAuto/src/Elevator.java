import java.awt.*;
import javax.swing.*;

public class Elevator extends Thread {
	private int numOfFloors; // Number of floors
    private JFrame cabin; // JFrame for the cabin
    private JFrame floors[]; // JFrame for each floor
    private JTextField curFloorCabin; // Current elevator place in the cabin
    private JTextField cabinDoor; // Cabin door
    private JCheckBox cabinButtons[]; // Check boxes in the elevator
    private boolean goingUp = true; // Direction of the elevator
    private JTextField curFloorFloors[]; // Current elevator place on the floors
    private JCheckBox floorButtons[]; // Check boxes on the floors
    private JTextField floorDoors[]; // Doors on the floors
    private Actions actions;
    private int curPassengerID = 1; // Current passenger number that is used in the thread name
    private JTextField curPassengersCabin; // Current passenger number inside the cabin
    private JTextField curPassengersFloors[]; // Current passenger number on each floor
    
    Elevator(int numOfFloors, Actions actions) {
        this.numOfFloors = numOfFloors;
        this.actions = actions;
        
        cabin = new JFrame("Cabin");
        cabin.setBounds(10, 10, 300, 300);
        cabin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel up = new JPanel(new FlowLayout()); // Up panel on the cabin
        curFloorCabin = new JTextField("0", 3);
        curFloorCabin.setEditable(false);
        curFloorCabin.setHorizontalAlignment(JTextField.CENTER);
        up.add("North", curFloorCabin);
        cabin.add("North", up);
        
        JPanel rght = new JPanel(new GridLayout(numOfFloors, 1)); // Right panel on the cabin
        cabinButtons = new JCheckBox[numOfFloors];
        for (int i = numOfFloors - 1; i >= 0; i--) {
        	cabinButtons[i] = new JCheckBox("F" + i);
        	cabinButtons[i].setEnabled(false);
            rght.add(cabinButtons[i]);
        }
        cabin.add("East", rght);
        
        cabinDoor = new JTextField("Door Closed");
        cabinDoor.setBackground(Color.red);
        cabinDoor.setEditable(false);
        cabinDoor.setHorizontalAlignment(JTextField.CENTER);
        cabin.add("Center", cabinDoor);
        
        JPanel left = new JPanel(new GridLayout(numOfFloors, 1)); // Left panel on the cabin
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        curPassengersCabin = new JTextField("0", 3);
        curPassengersCabin.setEditable(false);
        curPassengersCabin.setHorizontalAlignment(JTextField.CENTER);
        left.add("West", curPassengersCabin);
        cabin.add("West", left);
        
        cabin.add("South", new JPanel());
        
        cabin.setVisible(true);
        
        floors = new JFrame[numOfFloors];
        JPanel floorUpPanels[] = new JPanel[numOfFloors]; // Up panels on the floors
        curFloorFloors = new JTextField[numOfFloors];
        JPanel floorRightPanels[] = new JPanel[numOfFloors]; // Right panels on the floors
        floorButtons = new JCheckBox[numOfFloors];
        JPanel floorLeftPanels[] = new JPanel[numOfFloors]; // Left panels on the floors
        curPassengersFloors = new JTextField[numOfFloors];
        floorDoors = new JTextField[numOfFloors];
        
        for(int i = 0; i < numOfFloors; i++) {
        	floors[i] = new JFrame("Floor " + i);
        	floors[i].setBounds(300 * (i + 1) + 10, 10, 300, 300);
        	floors[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
        	floorUpPanels[i] = new JPanel(new FlowLayout());
            curFloorFloors[i] = new JTextField("0", 3);
            curFloorFloors[i].setEditable(false);
            curFloorFloors[i].setHorizontalAlignment(JTextField.CENTER);
            floorUpPanels[i].add("North", curFloorFloors[i]);
            floors[i].add("North", floorUpPanels[i]);
            
            floorRightPanels[i] = new JPanel(new GridLayout(1, 1));
            floorButtons[i] = new JCheckBox("Call");
            floorButtons[i].setEnabled(false);
            floorRightPanels[i].add(floorButtons[i]);
            floors[i].add("East", floorRightPanels[i]);
            
            floorLeftPanels[i] = new JPanel(new GridLayout(1, 1));
            floorLeftPanels[i].setLayout(new BoxLayout(floorLeftPanels[i], BoxLayout.Y_AXIS));
            curPassengersFloors[i] = new JTextField("0", 3);
            curPassengersFloors[i].setEditable(false);
            curPassengersFloors[i].setHorizontalAlignment(JTextField.CENTER);
            floorLeftPanels[i].add("West", curPassengersFloors[i]);
            floors[i].add("West", floorLeftPanels[i]);
            
            floorDoors[i] = new JTextField("Door Closed");
            floorDoors[i].setBackground(Color.red);
            floorDoors[i].setEditable(false);
            floorDoors[i].setHorizontalAlignment(JTextField.CENTER);
            floors[i].add("Center", floorDoors[i]);
            
            floors[i].add("South", new JPanel());
            
            floors[i].setVisible(true);
        }
    }
    
    public void run() {
    	while (true) {
    		int curFloor = Integer.parseInt(curFloorCabin.getText());
    		try {
    			openDoorAndCreatePassenger(curFloor);
    			sleep(5000);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		curFloor = Integer.parseInt(curFloorCabin.getText());
            openDoorAndCreatePassenger(curFloor);
            moveElevatorUpAndDown(curFloor);
    	}
    }
    
    public void moveElevatorUpAndDown(int curFloor) {
        if (goingUp) {
        	curFloorCabin.setText(++curFloor + "");
            if (curFloor == (numOfFloors - 1)) {
            	goingUp = false;
            	System.out.println("\tElevator direction changed to: down");
            }
        }
        else {
        	curFloorCabin.setText(--curFloor + "");
            if (curFloor == 0) {
            	goingUp = true;
            	System.out.println("\tElevator direction changed to: up");
            }
        }
        int randomFloorOutside = (int)Math.floor(Math.random() * (numOfFloors));
        floorButtons[randomFloorOutside].setSelected(true);
    }
    
    public void openDoorAndCreatePassenger(int curFloor) {
    	actions.currentFloor = curFloor;
        for (int j = 0; j < numOfFloors; j++) {
        	curFloorFloors[j].setText(curFloorCabin.getText());
        	
        	if (curFloor == j) {
        		
        		// If the floor is checked => open & close both floor and cabin doors
        		if (floorButtons[curFloor].isSelected() || cabinButtons[curFloor].isSelected()) {
        			floorDoors[curFloor].setText("Door Opened");
                    floorDoors[curFloor].setBackground(Color.green);
                    cabinDoor.setText("Door Opened");
                    cabinDoor.setBackground(Color.green);
                    System.out.println("\tElevator door opened on floor: " + curFloor);
        		}
        		
        		// If floor button is pressed on floor => create new Passenger
        		if (floorButtons[curFloor].isSelected()) {
        			boolean createNewPassenger = true;
            		for (int k = 0; k < actions.waitingList.size(); k++) {
            			if ((actions.waitingList.get(k).getFromFloor() == curFloor) && (actions.curPassengers < actions.maxPassengers)) {
            				actions.takeElevator(actions.waitingList.get(k));
            				createNewPassenger = false;
        					cabinButtons[actions.waitingList.get(k).getToFloor()].setSelected(true);
            			}
            		}
            		if (createNewPassenger == true) {
            			Passenger passenger = new Passenger("Passenger " + curPassengerID++, actions);
                		passenger.setFromFloor(curFloor);
                		int randomFloorInside = (int)Math.floor(Math.random() * (numOfFloors));
                		while (randomFloorInside == curFloor) {
                			randomFloorInside = (int)Math.floor(Math.random() * (numOfFloors));
                		}
                		passenger.setToFloor(randomFloorInside);
                		passenger.start();
                		cabinButtons[passenger.getToFloor()].setSelected(true);
            		}
        		}
        		
        		// If cabin button is pressed in cabin => Passenger should leave the elevator
        		if (cabinButtons[curFloor].isSelected()) {
        			actions.leaveElevator();
        		}
        		
        		if (floorButtons[curFloor].isSelected() || cabinButtons[curFloor].isSelected()) {
        			try {
        				curFloorCabin.setText(curFloor + "");
        				curPassengersCabin.setText(actions.curPassengers + "");
        				for (int i = 0; i < numOfFloors; i++) {
        					curFloorFloors[i].setText(curFloorCabin.getText());
        		        	curPassengersFloors[i].setText(actions.curPassengers + "");
        				}
    					sleep(2000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
                    floorDoors[curFloor].setText("Door Closed");
                    floorDoors[curFloor].setBackground(Color.red);
                    cabinDoor.setText("Door Closed");
                    cabinDoor.setBackground(Color.red);
                    floorButtons[curFloor].setSelected(false);
                    cabinButtons[curFloor].setSelected(false);
                    System.out.println("\tElevator door closed on floor: " + curFloor);
        		}
        	}
        	curPassengersFloors[j].setText(actions.curPassengers + "");
        }
        curPassengersCabin.setText(actions.curPassengers + "");
    }
}