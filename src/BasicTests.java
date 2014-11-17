import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;

/**
 * Test that the CCS behaves as expected at least for the basic tests.
 * You should extend this class with your own tests.
 * 
 */
public class BasicTests {

    /**
     * Temporarily captures the output to the standard output stream, then
     * restores the standard output stream once complete.
     * 
     * @param args
     *            arguments to pass to main function of class to be tested
     * @return output result of calling main function of class to be tested
     */
    private String captureOutputOfMain(String args[]) {
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        try {
            CommandLine.main(args);
        } catch (IOException e) {
			e.printStackTrace();
		}
        finally {
            System.setOut(originalOut);
        }
        return outputStream.toString().trim();
    }
    
    /**
     * A simple function to run the a list of input states defined as an
     * array of strings to retrieve a list of output states.
     */
    private List<OutputState> run_input_states(String[] input_lines){
    	List<InputState> test_input_states = StateInput.input_states_from_strings(input_lines);
    	
    	Timer timer = new Timer(new CruiseControlSystem());
    	return timer.pulse_from_input(test_input_states);
    }
    
    /**
     * Runs the a list of input states defined as strings to produce the
     * list of output states and then simply selects the final output state.
     */
    private OutputState get_final_state(String[] input_lines){
    	List<OutputState> output_states = run_input_states(input_lines);
    	return output_states.get(output_states.size() - 1);
    }
    
    @Test
    public void test_command_line(){
    	String expected = "true 50.000000 0.000000 0.500000 false false false false false 0.500000\n"
    			+ "true 50.000000 0.000000 0.500000 true false false false false 1.000000";
    	String[] arguments = { "test-input-files/simple-input.text" };
    	String actual_output = this.captureOutputOfMain(arguments);
    	assertTrue(expected.equals(actual_output));
    }
    
    @Test
    public void test_start_ccs() { //Test CCS button turns on under correct conditions
    	// Create input such that the CCS should be started and such that
    	// the throttle position should be set by the CCS in at least one
    	// pulse.
    	String[] input_lines = {"true 50.0 0.0 0.5 false false false false false",
    							 "- - - - true - - - -"};
    	OutputState final_state = get_final_state(input_lines);
    	assertTrue(final_state.get_throttle_position() == 1.0);
    }
    @Test
    public void test_start_ccsBelow40() { //Test ccs does not turn on if speed is less than 40
    	// Create input such that the CCS should be started and such that
    	// the throttle position should be set by the CCS in at least one
    	// pulse.
    	String[] input_lines = {"true 30.0 0.0 0.5 false false false false false",
    							 "- - - - true - - - -"};
    	OutputState final_state = get_final_state(input_lines);
    	assertTrue(final_state.get_throttle_position() == 0.5);
    }
    @Test
    public void test_start_ccsBelow40andBrakeOn() { //Test CCS does not turn on if speed is less than 40 and brake is on
    	// Create input such that the CCS should be started and such that
    	// the throttle position should be set by the CCS in at least one
    	// pulse.
    	String[] input_lines = {"true 30.0 1.0 0.5 false false false false false",
    							 "- - - - true - - - -"};
    	OutputState final_state = get_final_state(input_lines);
    	assertTrue(final_state.get_throttle_position() == 0.5);
    }
    @Test
    public void test_start_ccsButBrakeOn() { //Test CCS does not turn on if brake is on
    	// Create input such that the CCS should be started and such that
    	// the throttle position should be set by the CCS in at least one
    	// pulse.
    	String[] input_lines = {"true 50.0 1.0 0.5 false false false false false",
    							 "- - - - true - - - -"};
    	OutputState final_state = get_final_state(input_lines);
    	assertTrue(final_state.get_throttle_position() == 0.5);
    }
    @Test
    public void test_start_accelerating(){ //Test the CCS accelerates at 2m/s per second
    	String[] input_lines = { "true 50.0 0.0 1.0 false false false false false",
				 			     "- - - - true - - - -",
				 				 "- - - - - - true - -"};
    	OutputState final_state = get_final_state(input_lines);
    	// The speed of the car is 50km/h so we should set the throttle position
    	// to a position which reflects 57.2km/h (because 7.2km/h = 2m/s)
    	assertTrue(final_state.get_throttle_position() == 1.1440000000000001);
    }
    @Test
    public void test_start_acceleratingWithoutCCSOn(){ //Test CCS wont accelerate via the accelerate button if CCS is off
    	String[] input_lines = { "true 50.0 0.0 1.0 false false false false false",
				 				 "- - - - - - true - -"};
    	OutputState final_state = get_final_state(input_lines);
    	// The speed of the car is 50km/h so we should set the throttle position
    	// to a position which reflects 57.2km/h (because 7.2km/h = 2m/s)
    	assertTrue(final_state.get_throttle_position() == 1.0);
    }
    @Test
    public void test_stop_ccs_ByBrake() { // Test the CCS switches off if brake pedal is pressed
    	String[] input_lines = { "true 50.0 0.0 1.0 false false false false false",
			     "- - - - true - - - -",
				 "- - 1.0 0.0 - - - - -"};
    	OutputState final_state = get_final_state(input_lines);
    	assertTrue(final_state.get_throttle_position() == 0.0);
    }
    @Test
    public void stop_ccs_ByButton() { //Test the CCS switches off if the stop ccs button is pressed
    	String[] input_lines = { "true 50.0 0.0 1.0 false false false false false",
			     "- - - - true - - - -",
				 "- - - 0.0 - true - - -"};
    	OutputState final_state = get_final_state(input_lines);
    	assertTrue(final_state.get_throttle_position() == 0.0);
    }
    @Test
    public void test_stop_accelerating_ByBrake() { //Test the CCS stops accelerating if brake pedal is pressed
    	String[] input_lines = { "true 50.0 0.0 1.0 false false false false false",
			     "- - - - true - - - -",
				 "- - - - - - true - -",
			     "- - 1.0 0.0 - - - - -"};
    	OutputState final_state = get_final_state(input_lines);
    	assertTrue(final_state.get_throttle_position() == 0.0);
    }
    @Test
    public void test_stop_accelerating_ByButton() {//Test the CCS stops accelerating stop accelerating button is pressed
    	String[] input_lines = { "true 75.0 0.0 1.0 false false false false false",
			     "- - - - true - - - -",
				 "- - - - - - true - -",
			     "- - - 0.0 - - - true -"};
   	OutputState final_state = get_final_state(input_lines);
   	assertTrue(final_state.get_throttle_position() == 1.5);
    }
    @Test
    public void test_resume() { // Test if the resume button is pressed, car returns to previously stored CCS speed
    	String[] input_lines = { "true 75.0 0.0 1.0 false false false false false",
			     "- - - - true - - - -",
				 "- - - - - true - - -",
			     "- 50.0 - - - - - - true",
			     "- - - - - - - - -"};
   	OutputState final_state = get_final_state(input_lines);
   	assertTrue(final_state.get_throttle_position() == 1.5);
    }
    @Test
    public void test_resumeButBrakeOn() {  //Test if the resume button is pressed the car does not turn on CCS as brake is on also.
    	String[] input_lines = { "true 75.0 0.0 1.0 false false false false false",
			     "- - - - true - - - -",
				 "- - - - - true - - -",
			     "- 50.0 1.0 - - - - - true",
			     "- - - - - - - - -"};
   	OutputState final_state = get_final_state(input_lines);
   	assertTrue(final_state.get_throttle_position() == 1.0);
    }
    @Test
    public void test_resumeButLess40() {  // Test if the resume button is pressed, the car does not turn on as no stored speed and carspeed < 40km/hr
    	String[] input_lines = { "true 75.0 0.0 1.0 false false false false false",
			     "- 30.0 - - - - - - true",
			     "- - - - - - - - -"};
   	OutputState final_state = get_final_state(input_lines);
   	assertTrue(final_state.get_throttle_position() == 1.0);
    }
    @Test
    public void test_engineoff() { //Test to see if engine is off, ccs remains off if button is pressed.
    	String[] input_lines = { "false 50.0 0.0 1.0 false false false false false",
			     "- - - 0.0 true - - - -",};
   	OutputState final_state = get_final_state(input_lines);
   	assertTrue(final_state.get_throttle_position() == 0.0);
    }
    @Test
    public void test_unexpectedTerrain () {  //test wether the CCS reacts approprietly to carspeeds unexpected (Via terrain)
    	String[] input_lines = { "true 50.0 0.0 1.0 false false false false false",
			     "- - - 0.0 true - - - -",
			     "- 42.7 - - - - - - -"};
  	OutputState final_state = get_final_state(input_lines);
  	assertTrue(final_state.get_throttle_position() == 1.0);
    }
    @Test
    public void test_resumeWithoutStored () {  //Test wether the resume function works without a storedspeed, correctly turning on CCS
    	String[] input_lines = { "true 75.0 0.0 1.0 false false false false true",
			     "- - - 0.0 - - - - -",};
  	OutputState final_state = get_final_state(input_lines);
  	assertTrue(final_state.get_throttle_position() == 1.5);
    }
}
