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
	
	double CCSspeed = 0.0;
	double currentSpeedValue;
	double speedStore = 0.0;
	
	public void pulse(Car car){
		currentSpeedValue = car.speed_sensor.get_speed();    //store current car speed in a readable variable, as it's frequently called.
		
		if (car.engine_sensor.is_engine_on()) {	 // First conditional, if engine is not on, we do not want to do anything at all.
	
			if (car.dashboard.get_stop_ccs()) {  //If the Stop CCS button is pressed, turn off the CCS
				car.dashboard.set_start_ccs(false);
				car.dashboard.set_start_accelerating(false);
				speedStore= car.speed_sensor.get_speed();
				car.dashboard.set_stop_ccs(false);
				CCSspeed=0.0;
			}
			
			if (car.dashboard.get_stop_accelerating()) { // If the stop accelerating button is pressed, stop accelerating
					car.dashboard.set_start_accelerating(false);
			}
			if (car.dashboard.get_start_ccs()) {  // If the start CCS button is pressed, Turn on the CCS, Set the throttle to maintain current speed.
				if (car.speed_sensor.get_speed()>=40.0 && !car.brake_pedal.is_brake_on()) {
					if (CCSspeed<=0.0) {
						car.throttle.setThrottlePosition(currentSpeedValue/50);		// If the CCS hasn't been set, set it!
						CCSspeed=currentSpeedValue;
					} else {
						car.throttle.setThrottlePosition(CCSspeed/50);	// If the CCS has been set, use that set value to set the throttle!
					}
				}
			}
			if (car.brake_pedal.is_brake_on()) {	 // If brake pedal is pressed,	turn of CCS and release throttle
				car.dashboard.set_start_ccs(false);
				car.dashboard.set_start_accelerating(false);
				car.throttle.setThrottlePosition(0.0);
				speedStore= car.speed_sensor.get_speed();
				CCSspeed=0.0;
		}
			if (car.dashboard.get_start_accelerating() && car.dashboard.get_start_ccs()) { // If CCS is on, and Start Accelerating button is on
				car.throttle.setThrottlePosition((CCSspeed+7.2)/50);						// Set throttle to value congruent to + 2m/s per second
			}
			if (car.dashboard.get_resume()) {		// If resume buttton is pressed, If speed store is set, set throttle to speed store.
				if (speedStore>=40.0 && !car.brake_pedal.is_brake_on()) {
					car.throttle.setThrottlePosition(speedStore/50);
					CCSspeed = speedStore;
				} else {
					if (car.speed_sensor.get_speed()>=40.0 && !car.brake_pedal.is_brake_on()) { // If no speedstore is set, if car >40 km/hr and brake not on, Turn on CCS
						if (CCSspeed<=0.0) {
							car.throttle.setThrottlePosition(currentSpeedValue/50);
							CCSspeed=currentSpeedValue;
						} else {
							car.throttle.setThrottlePosition(CCSspeed/50);
						}
					}
				}
			}
			
		}
	}
}
