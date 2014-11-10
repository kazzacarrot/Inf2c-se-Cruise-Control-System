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
		Car lastcar = null;
		double speedstore = 0.0;
		
		public void pulse(Car car){
		if (car.engine_sensor.is_engine_on()) {	
			
			if (car.brake_pedal.is_brake_on()) {
				if (car.dashboard.get_start_ccs()) {
					//update
				}
			}
			if (car.dashboard.get_start_ccs()) {
					if (car.speed_sensor.get_speed()>=40.0 && !car.brake_pedal.is_brake_on()) {
						//update
					}
			}
			if (car.dashboard.get_stop_ccs()) {
				//update unconditionally.
				speedstore= car.speed_sensor.get_speed();
			}
			if (car.dashboard.get_resume()) {
				if (speedstore>=40.0) {
					//update
				} else {
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
			
			if (lastcar!=null) {
				if (lastcar.dashboard.get_start_accelerating()&&!car.dashboard.get_start_accelerating()){
				//update for when start accelerating turned off
				}
				if (lastcar.dashboard.get_start_ccs()&&!car.dashboard.get_start_ccs()) {
				//update for when CCS is turned off
				}
				}
		} else {
			//update 0's and falses.
		}
		lastcar = car;
	}
}
