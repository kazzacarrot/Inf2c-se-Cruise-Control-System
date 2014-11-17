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
		currentSpeedValue = car.speed_sensor.get_speed();
		if (car.engine_sensor.is_engine_on()) {	
	
			if (car.brake_pedal.is_brake_on()) {		
					car.dashboard.set_start_ccs(false);
					car.dashboard.set_start_accelerating(false);
					speedStore= car.speed_sensor.get_speed();
					CCSspeed=0.0;
			}
			if (car.dashboard.get_stop_ccs()) {
				car.dashboard.set_start_ccs(false);
				car.dashboard.set_start_accelerating(false);
				speedStore= car.speed_sensor.get_speed();
				car.dashboard.set_stop_ccs(false);
				CCSspeed=0.0;
			}
			if (car.dashboard.get_stop_accelerating()) {
					car.dashboard.set_start_accelerating(false);
			}
			if (car.dashboard.get_start_ccs()) {
				if (car.speed_sensor.get_speed()>=40.0 && !car.brake_pedal.is_brake_on()) {
					if (CCSspeed<=0.0) {
						car.throttle.setThrottlePosition(currentSpeedValue/50);
						CCSspeed=currentSpeedValue;
					} else {
						car.throttle.setThrottlePosition(CCSspeed/50);
					}
				}
			}
			if (car.dashboard.get_start_accelerating() && car.dashboard.get_start_ccs()) {
				car.throttle.setThrottlePosition((CCSspeed+7.2)/50);
			}
			if (car.dashboard.get_resume()) {
				if (speedStore>=40.0 && !car.brake_pedal.is_brake_on()) {
					car.throttle.setThrottlePosition(speedStore/50);
					CCSspeed = speedStore;
				} else {
					if (car.speed_sensor.get_speed()>=40.0 && !car.brake_pedal.is_brake_on()) {
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
