package de.hpi.oryxengine.factories.process;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;

import de.hpi.oryxengine.exception.IllegalStarteventException;

/**
 * Tests the EcampleProcessDeplyoer class.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ExampleProcessDeployerTest extends AbstractProcessDeployerTest {


    /**
     * {@inheritDoc}
     */
    @BeforeMethod
    @Override
    public void setUp() throws IllegalStarteventException {

        this.deployer = new ExampleProcessDeployer();
        this.uuid = deployer.deploy();
    }
}
