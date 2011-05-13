package de.hpi.oryxengine.factories.process;

import org.testng.annotations.BeforeMethod;

import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;

/**
 * Tests the EcampleProcessDeplyoer class. {@inheritDoc}
 */
public class HeavyComputationProcessDeployerTest extends AbstractProcessDeployerTest {

    /**
     * {@inheritDoc}
     * @throws ResourceNotAvailableException 
     */
    @Override
    @BeforeMethod
    public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException {

        this.deployer = new HeavyComputationProcessDeployer();
        this.uuid = deployer.deploy(engineServices);
    }
}
