/**
 * This is the main class that you need to implement. You only have
 * a single method to implement, but of course that may be easier if
 * you define some auxiliary methods.
 */
public class CruiseControlSystem implements ICruiseControlSystem {
	/*
	 * Students may add any private fields or methods that they deem
	 * necessary. Public ones should not be necessary (there is no
	 * rule against it, but you should not be changing the support code
	 * and the rest of the code knows only about this).
	 */
	
	public boolean isCCSOn;
	public boolean isAccelerating;
	public double  storedSpeedValue;
	public double currentSpeedValue;
	public double Throttle_Position;
	private double speedStore;
	public Car lastcar;

	public static void main(Car car){
		storedSpeedValue = 40.0; // makes sense to initialise the stored speed to the minimum speed allowed to turn on the CCS, 40km/h   
  
      
    
	public void pulse(Car car){
        // Things should only happen if the engine is running
		if (car.engine_sensor.is_engine_on()) {	
            // if the brake is pressed
			if (car.brake_pedal.is_brake_on()) {
                //if the CCS is turned on
				if (car.dashboard.get_start_ccs()) {
                    // turn off the CCS and give the driver control of the car
					car.dashboard.set_start_ccs(false);
					car.throttle.setThrottlePosition(0.0);
				}
			}
            // if the CCS is turned on
			if (car.dashboard.get_start_ccs()) {
                // 
				if (car.speed_sensor.get_speed()>=40.0 && !car.brake_pedal.is_brake_on()) {
					//update
					}
			}
			if (car.dashboard.get_stop_ccs()) {
				//update unconditionally.
				speedStore= car.speed_sensor.get_speed();
			}
			if (car.dashboard.get_resume()) {
				if (speedStore>=40.0) {
					//update
				} 
                else {
					if (car.speed_sensor.get_speed()>=40.0) {
						//update
					}
				}
			}
			if (car.dashboard.get_stop_accelerating()) {
				//update 
			}
			if (car.dashboard.get_start_accelerating()) {
				//update
			}
			//These methods are for when something on in lastcar is now turned off
			if (lastcar!=null) {
				if (lastcar.dashboard.get_start_accelerating()&&!car.dashboard.get_start_accelerating()){

					//update for when start accelerating turned off
				}
				if (lastcar.dashboard.get_start_ccs()&&!car.dashboard.get_start_ccs()) {
					//update for when CCS is turned off

				//update for when start accelerating turned off
				}
				if (lastcar.dashboard.get_start_ccs()&&!car.dashboard.get_start_ccs()) {
				//update for when CCS is turned off
				}

			}
		} 
        // if the engine is not on
        else {
			//update 0's and falses.
		}
		//We can use this to identify if no changes are made, If no changes are made during a pulse, The ouput is "-"
		if (lastcar!=null) {
			if (car.accelerator_pedal.get_accelerator()==lastcar.accelerator_pedal.get_accelerator()) {
			//	car.accelerator_pedal Dunno, how but update state to "-"
			}
			// Another if statment for every test if last car is same as current car.
		}
		lastcar = car;   
	}
}
