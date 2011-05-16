package org.jodaengine.repository;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jodaengine.RepositoryService;
import org.jodaengine.RepositoryServiceImpl;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.RawProcessDefintionImporter;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionImpl;

import java.util.UUID;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Test implementation for {@link RepositoryService} class.
 * 
 * @author Jan Rehwaldt
 */
public class ProcessRepositoryTest {

    private static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-002a-0000-00000000002a");

    private RepositoryService repository = null;

    /**
     * Setup method.
     */
    @BeforeMethod
    public void setUp() {

        this.repository = new RepositoryServiceImpl();
    }

    /**
     * Tear down method.
     */
    @AfterMethod
    public void tearDown() {

        this.repository = null;
    }

    /**
     * Tests the throwing of an appropriate exception.
     * 
     * @throws DefinitionNotFoundException
     *             expected
     */
    @Test(expectedExceptions = DefinitionNotFoundException.class)
    public void testNoDefinitionException()
    throws DefinitionNotFoundException {

        assertFalse(this.repository.containsProcessDefinition(EMPTY_UUID));
        this.repository.getProcessDefinition(EMPTY_UUID);
    }

    /**
     * Tests the addition of a definition.
     * 
     * @throws DefinitionNotFoundException
     *             fails test
     */
    @Test
    public void testAddDefinition()
    throws DefinitionNotFoundException {

        final ProcessDefinition def = new ProcessDefinitionImpl(EMPTY_UUID, null, null, null);
        assertFalse(this.repository.containsProcessDefinition(EMPTY_UUID));

        ProcessDefinitionImporter processDefinitionImporter = new RawProcessDefintionImporter(def);
        this.repository.getDeploymentBuilder().deployProcessDefinition(processDefinitionImporter);

        assertTrue(this.repository.containsProcessDefinition(EMPTY_UUID));
        assertEquals(def, this.repository.getProcessDefinition(EMPTY_UUID));
    }

    /**
     * Tests the deletion of a definition.
     * 
     * @throws DefinitionNotFoundException
     *             fails test
     */
    @Test
    public void testDeleteDefinition()
    throws DefinitionNotFoundException {

        final ProcessDefinition def = new ProcessDefinitionImpl(EMPTY_UUID, null, null, null);
        assertFalse(this.repository.containsProcessDefinition(EMPTY_UUID));

        ProcessDefinitionImporter processDefinitionImporter = new RawProcessDefintionImporter(def);
        this.repository.getDeploymentBuilder().deployProcessDefinition(processDefinitionImporter);

        assertTrue(this.repository.containsProcessDefinition(EMPTY_UUID));
        this.repository.deleteProcessDefinition(EMPTY_UUID);
        assertFalse(this.repository.containsProcessDefinition(EMPTY_UUID));
    }
}