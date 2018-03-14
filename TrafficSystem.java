package traficsim;

import java.util.ArrayList;
import java.util.Properties;

import progtek.Measurements;

import java.io.*;
/**
 * Defines the components and behaviour of s specific traffic system
 */
public class TrafficSystem {
	// These instance variables are set by a call to the given loadProperties method.
	// Use the given file "properties.txt" as parameter.
	private int laneLength;      // Length of the first lane
	private int laneWSLength;    // Length of the lanes in front of the signals
	private int lightPeriod;     // Period for the signals
	private int lightWestGreen;  // Green period for the westbound light
	private int lightSouthGreen; // Green period for the southbaoun light

	// Instance variable for the different lanes, lights and the queue
	private Lane lane;
	private VehicleGenerator vg;
	private ArrayList<Vehicle> queue;
	private Light lightWest;
	private Light lightSouth;
	private Lane laneS;
	private Lane laneW;

	// Instance variables for statistics

	private Measurements timesWest;
	private Measurements timesSouth;
	private int exitWest;
	private int exitSouth;
	private double queueTime;
	private double blocked;
	private int arrive;


	// Constructor

	public TrafficSystem() {

		this.loadProperties("properties.txt");
		this.vg = new VehicleGenerator("probabilities.txt");
		this.lane = new Lane(this.laneLength);
		this.queue = new ArrayList<Vehicle>();
		this.lightSouth = new Light(this.lightPeriod, this.lightSouthGreen);
		this.lightWest = new Light(this.lightPeriod, this.lightWestGreen);
		this.laneS = new Lane(this.laneWSLength);
		this.laneW = new Lane (this.laneWSLength);
		this.timesWest  = new Measurements(20);
		this.timesSouth = new Measurements(20);
		this.exitSouth = 0;
		this.exitWest = 0;
		this.queueTime = 0;
		this.blocked = 0;
		this.arrive = 0;


	}


	// Methods

	/**
	 * Advances the whole traffic system one timestep. Makes use
	 * of components step methods
	 */
	public void step() {

		//traffic lights

		if (lightSouth.isGreen() && laneS.getFirst() != null){
			timesSouth.add(Simulation.getTime()-laneS.getFirst().getTime());
			laneS.removeFirst();
			exitSouth++;
		}
		laneS.step();

		if (lightWest.isGreen() && laneW.getFirst() != null){
			timesWest.add(Simulation.getTime()-laneW.getFirst().getTime());
			laneW.removeFirst();
			exitWest++;
		}
		laneW.step();
		//---------------------------------------------

		//from main to south and west


		if (laneS.lastFree() && lane.getFirst() != null){
			if (lane.getFirst().getDestination() == 'S'){
				laneS.putLast(lane.getFirst());
				lane.removeFirst();
				arrive++;
			}

		}


		if (laneW.lastFree() && lane.getFirst() != null){
			if (lane.getFirst().getDestination() == 'W') {
				laneW.putLast(lane.getFirst());
				lane.removeFirst();
				arrive++;
			}
		}


		//-----------------------------------------------

		//check for block

		if (laneS.lastFree() == false && lane.getFirst() != null && lane.getFirst().getDestination() == 'S' ){

			blocked++;

		}

		if (laneW.lastFree() == false && lane.getFirst() != null && lane.getFirst().getDestination() == 'W'){
			blocked++;
		}
		//---------------------------------



		lane.step();
		Vehicle v = vg.step();

		if (v != null) {
			queue.add(v);

		}


		if (lane.lastFree() && !queue.isEmpty()){
			lane.putLast(queue.get(0));
			queue.remove(0);
		}

		if (!queue.isEmpty()){
			queueTime++;
		}

		lightSouth.step();
		lightWest.step();
	}

	/**
	 * Compute the number of vehicles in the system
	 * @return The number of vehicles in the system
	 */ 
	public int numberInSystem() {

		return lane.numberOfVehicles() + laneW.numberOfVehicles() + laneS.numberOfVehicles();
	}


	/**
	 * Prints currently collected statistics
	 */  
	public void printStatistics() {

		int left = arrive - numberInSystem();

		double simTime = (double) Simulation.getTime();
		/*double queuePercent = queueTime/simTime;
		double blockedPercent = blocked/simTime;*/

		System.out.println("Arrived at E         : " + arrive);
		System.out.println("Total left           :  " + left );
		System.out.println("Number in the system : " + numberInSystem());
		System.out.println(" ");
		System.out.println("Exit west");
		System.out.println("number: " + exitWest);
		System.out.println("mean: " + timesWest.mean());
		System.out.println("min: " + timesWest.min());
		System.out.println("max: " + timesWest.max());
		System.out.println(" ");
		System.out.println("Exit south");
		System.out.println("number: " + exitSouth);
		System.out.println("mean: " + timesSouth.mean());
		System.out.println("min: " + timesSouth.min());
		System.out.println("max: " + timesSouth.max());
		System.out.println("");
		System.out.println("Percent time step with block: " + 100*blocked/simTime);
		System.out.println("Percent time step with queue: " + 100*queueTime/simTime);




	}


	/**
	 * Prints the current situation using toString-methods in 
	 * lights and lanes
	 */
	public void print() {

		System.out.println("(" + lightWest.toString() + ")" +laneW.toString() + lane.toString());
		System.out.println("(" + lightSouth.toString() + ")" + laneS.toString() + " Q " + queue.toString());


	}


	/**
	 * Prints the simulation parameters and arrival probabilities used in 
	 * this run
	 */
	public void printSetup() {

		System.out.println("laneLength      : " + laneLength);
		System.out.println("laneWSLength    :  " + laneWSLength);
		System.out.println("lightPeriod     : " + lightPeriod);
		System.out.println("lightWestGreen  :  " + lightWestGreen);
		System.out.println("lightSouthGreen :  " + lightSouthGreen);
		System.out.println("Traffic periods and probabilities: " );
		vg.print();
	}


	/** 
	 * Reads the lane lengths and the properties of the lights from a 
	 * property file
	 * 
	 * @param filename The file containing the properties
	 * * 
	 * The the property file should define 
	 * <ul>
	 * <li> length of the first lane</li>
	 * <li> length of the lanes in front of the traffic lights</li>
	 * <li> traffic light period (same for both lights)</li>
	 * <li> green light period for each of the lights</li> 
	 * </ul>
	 * <p>
	 * <b>Example of file contents:</b>
	 * <pre>
	 *    laneLength      : 10
	 *    laneWSLength    :  8
	 *    lightPeriod     : 14
	 *    lightWestGreen  :  6
	 *    lightSouthGreen :  4
	 * </pre>
	 * 
	 */
	public void loadProperties(String filename) {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader(filename));
			this.laneLength = Integer.parseInt(prop.getProperty("laneLength"));
			this.laneWSLength = Integer.parseInt(prop.getProperty("laneWSLength"));      
			this.lightPeriod = Integer.parseInt(prop.getProperty("lightPeriod"));
			this.lightWestGreen = Integer.parseInt(prop.getProperty("lightWestGreen"));
			this.lightSouthGreen = Integer.parseInt(prop.getProperty("lightSouthGreen"));     
		} catch (IOException ioe) {
			System.out.println("*** File " + filename + " could not be loaded");
			System.exit(0);
		}
	}
}