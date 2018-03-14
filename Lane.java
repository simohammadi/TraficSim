package traficsim;

import java.util.ArrayList;

/**
 * Represents a lane as an array of Vehicles. A position in the lane may be occupied
 * by a vehicle or free (contains null). Vehicles enter the lane at the high index of 
 * the array end exit from index 0.
 */
public class Lane {

	private Vehicle[] theLane;

	/**
	 * Constructs a lane with a specified capacity.
	 * @param length The length (capacity) of the lane in number of vehicles
	 */
	public Lane(int length) {
		theLane = new Vehicle[length];
	}

	/**
	 * A string representation of the lane.
	 * @return The string representation
	 */
	public String toString() {
		
		ArrayList<Character> temp = new ArrayList<Character>();
		

		for (int i = 0; i < this.theLane.length; i++) {
			if (theLane[i] == null){
				temp.add('.');
			}
			else {
				temp.add(theLane[i].getDestination());
			}
		}

		return "<" + temp.toString() + ">";
	}

	/**
	 * Advances all except the first vehicle one position provided the 
	 * target position is free. The process starts in the low end (i. e.
	 * at index 1).
	 * <p>
	 * <b>Example:</b> The follwing two lines shows the result of
	 * a call to <code>toString</code> before and after a call to 
	 * <code>step()</code>
	 * <pre>
	 *    &lt;XX  X   X X X  XX&gt;
	 *    &lt;XX X   X X X  XX &gt;
	 * </pre>
	 * 
	 */
	public void step() {

		for (int i = 0; i < this.theLane.length - 1; i++) {

			if (this.theLane[i] == null && this.theLane[i+1] != null) {
				this.theLane[i]=this.theLane[i+1]; 
				this.theLane[i+1] = null;
			}

		}

	}

	/**
	 * Removes the first vehicle (index 0) from the lane and makes it empty.
	 * @return The removed vehicle or <code>null</code> if the position was empty
	 */
	public Vehicle removeFirst() {

		if (this.theLane == null) {
			return this.theLane[0];
		}
		
		else {
			Vehicle temp = this.theLane[0];
			this.theLane[0]=null;
			return temp;
		}


	}

	/**
	 * Access method for the vehicle in the first position.
	 * @return A reference to the vehicle in the first position
	 */
	public Vehicle getFirst() {
		return this.theLane[0];
	}

	/**
	 * Checks if the last position is free.
	 * @return <code>true</code> if the last position is free (null) 
	 * else <code>false<code>.
	 */
	public boolean lastFree() {
		if (this.theLane[this.theLane.length-1] == null) {
			return true;
		}
		else {
			return false;
		}

	}

	/**
	 * Put a vehicle in the last position.
	 * @param v Vehicle to be put in the last position
	 * @throws RuntimeException if the last position is not free
	 */
	public void putLast(Vehicle v) {
		if (this.theLane[this.theLane.length-1] == null){
			this.theLane[this.theLane.length-1] = v; 
		}
		else {
			throw new RuntimeException("The last possition is occupied");
		}
	}


	/**
	 * Counts the number of Vehicles on the lane.
	 * @return The number of Vehicles 
	 */
	public int numberOfVehicles() {
		
		int count = 0;
		
		for (int i=0; i < theLane.length; i++){
			if (theLane[i] != null){
				count++;
			}
		}

		return count;
	}

	/**
	 * Demonstrates the use of the the class and it's methods.
	 */
	public static void main(String[] args) { 
		Lane l1 = new Lane(2);
		Vehicle v = new Vehicle('S');
		l1.putLast(v);
		l1.step();
		System.out.println(l1.toString());
		System.out.println(l1.getFirst().getTime());
	}
}
