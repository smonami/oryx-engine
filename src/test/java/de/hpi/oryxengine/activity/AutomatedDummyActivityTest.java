package de.hpi.oryxengine.activity;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hpi.oryxengine.activity.AbstractActivityImpl.State;
import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;


public class AutomatedDummyActivityTest {
	
	private PrintStream tmp;
	private final ByteArrayOutputStream out = new ByteArrayOutputStream();
	private String s = "I'm dumb";
	private AutomatedDummyActivity a;
	
	@Before
	public void setUp() throws Exception {
		
		tmp = System.out;
		System.setOut(new PrintStream(out));
		a = new AutomatedDummyActivity(s);
	}
	
	@Test
	public void testActivityInitialization(){
		assertNotNull("It should not be null since it should be instantiated correctly", a);
	}
	
	@Test
	public void testStateAfterActivityInitalization(){
		assertEquals("It should have the state Initialized", State.INIT, a.getState());
	}
	
	@Test
	public void testExecuteOutput(){
		a.execute();
		assertEquals("It should print out the given string when executed", s, out.toString().trim());		
	}
	
	@Test
	public void testStateAfterExecution(){
		a.execute();
		assertEquals("It should have the state Initialized", State.TERMINATED, a.getState());
	}
	
	@After
	public void tearDown(){
		System.setOut(tmp);
	}
}