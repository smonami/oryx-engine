package de.hpi.oryxengine.process.structure;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class ConditionTest.
 */
public class ConditionTest {
    
    /** The condition. */
    private Condition condition = null;
    
    /** The instance. */
    private Token token = null;
    
    /** The context. */
    private ProcessInstanceContext context = null;
  

  /**
   * Test false condition on variable.
   */
  @Test
  public void testFalseConditionOnVariable() {
      assertFalse(condition.evaluate(token), "Condition was true but should be false.");
  }
  
  /**
  * Test set false.
  */
  @Test
  public void testSetFalse() {
    condition.setFalse();
    assertFalse(condition.evaluate(token), "Set false didnt happened.");
  }
  
  /**
   * Test true condition on variable.
   */
  @Test
  public void testTrueConditionOnVariable() {
      when(context.getVariable("a")).thenReturn(1);
      assertTrue(condition.evaluate(token), "Condition was not true.");
  }
  
  /**
   * Before test.
   */
  @BeforeMethod
  public void beforeMethod() {
      token = mock(Token.class);
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("a", 1);
      condition = new ConditionImpl(map);
      context = mock(ProcessInstanceContext.class);
      when(token.getContext()).thenReturn(context);
  }

  /**
   * After test.
   */
  @AfterMethod
  public void afterMethod() {
      condition = null;
  }

}
