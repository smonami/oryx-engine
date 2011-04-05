package de.hpi.oryxengine.resource;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;

/**
 * Tests the building of {@link Role}s in the organization structure.
 */
public class BuildingRoleTest {

    private IdentityService identityService = null;

    private IdentityBuilder identityBuilder = null;

    private Role adminRole = null;

    @BeforeMethod
    public void beforeMethod() {

        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();

        adminRole = identityBuilder.createRole("Administrators");
    }

    @AfterMethod
    public void tearDown() {

        ServiceFactoryForTesting.clearIdentityService();
    }

    @Test
    public void testRoleCreation() {

        Role secretaryRole = identityBuilder.createRole("Secretaries");

        String failuremessage = "There should be two Role.";
        Assert.assertTrue(identityService.getRoles().size() == 2, failuremessage);
        failuremessage = "The Role (Id='admin', name='Administrators') is not in the IdentityService.";
        Assert.assertTrue(identityService.getRoles().contains(adminRole), failuremessage);
        Assert.assertTrue(identityService.getRoles().contains(secretaryRole));

    }

    @Test
    public void testForUniqueRole() {

        // Try to create a new Role with the same Name
        Role adminRole2 = identityBuilder.createRole("Administrators");

        String failureMessage = "There should still be two Roles, but there are " + identityService.getRoles().size();
        Assert.assertTrue(identityService.getRoles().size() == 2, failureMessage);
        failureMessage = "The new created Role should not be the old one.";
        Assert.assertEquals(adminRole2.getName(), "Administrators", failureMessage);
        Assert.assertNotSame(adminRole, adminRole2);

    }

    @Test
    public void testCreationParticipantRoleRelationship()
    throws Exception {

        Participant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");
        Participant participant2 = identityBuilder.createParticipant("Jannik Streek");

        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID()).participantBelongsToRole(
            participant2.getID(), adminRole.getID());

        Assert.assertTrue(adminRole.getParticipants().size() == 2);
        String failuremessage = "The Participant 'Gerardo Navarro Suarez' should belong to the role 'Administrators'.";
        Assert.assertTrue(participant.getMyRoles().contains(adminRole), failuremessage);
        Assert.assertTrue(adminRole.getParticipants().contains(participant), failuremessage);

        failuremessage = "The Participant 'Jannik Streek' should belong to the role 'Administrators'.";
        Assert.assertTrue(participant.getMyRoles().contains(adminRole), failuremessage);
        Assert.assertTrue(adminRole.getParticipants().contains(participant), failuremessage);
    }

    /**
     * You should not b able to directly manipulate the relationVariable of the Role.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperation() {

        Participant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");

        adminRole.getParticipants().add(participant);
    }

    @Test
    public void testUniqueParticipantRoleRelationship()
    throws Exception {

        Participant participant = identityBuilder.createParticipant("gerardo.navarro-suarez");
        Participant participant2 = identityBuilder.createParticipant("jannik.streek");

        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID())
        // Try to offer the same Position again
        .participantBelongsToRole(participant.getID(), adminRole.getID());

        // As before, there should be only two positions offered by that Role
        String failureMessage = "Identity Service should have 1 Participant Element, but it is "
            + adminRole.getParticipants().size() + " .";
        Assert.assertTrue(adminRole.getParticipants().size() == 1, failureMessage);

        identityBuilder.participantBelongsToRole(participant2.getID(), adminRole.getID()).participantBelongsToRole(participant.getID(),
            adminRole.getID());

        // Now there should be one more
        Assert.assertTrue(adminRole.getParticipants().size() == 2);
    }

    @Test
    public void testChangeParticipantRoleRelationship()
    throws Exception {

        Participant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");

        Role secretaryRole = identityBuilder.createRole("Secretaries");

        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID())
        // Now change the Position to another Role
        .participantBelongsToRole(participant.getID(), secretaryRole.getID());

        // There still should be one Position in the system
        Assert.assertTrue(identityService.getParticipants().size() == 1);
        Assert.assertTrue(identityService.getParticipants().contains(participant));

        String failureMessage = "The Particapant 'Gerardo Navarro Suarez' should belong to"
            + "the Role 'secretaries' and to the Role 'Aministrators'.";
        Assert.assertTrue(participant.getMyRoles().contains(adminRole), failureMessage);
        Assert.assertTrue(participant.getMyRoles().contains(secretaryRole), failureMessage);

        failureMessage = "The Role 'Secretaries' should have only the Participant 'Gerardo Navarro Suarez'.";
        Assert.assertTrue(secretaryRole.getParticipants().size() == 1, failureMessage);
        Assert.assertTrue(secretaryRole.getParticipants().contains(participant), failureMessage);

        failureMessage = "The Role 'Aministrators' should have only the Participant 'Gerardo Navarro Suarez'.";
        Assert.assertTrue(adminRole.getParticipants().size() == 1, failureMessage);
        Assert.assertTrue(adminRole.getParticipants().contains(participant), failureMessage);
    }

    @Test
    public void testDeleteRole()
    throws Exception {

        Participant participant1 = identityBuilder.createParticipant("Gerardo Navarro Suarez");
        Participant participant2 = identityBuilder.createParticipant("Jannik Streek");

        identityBuilder.participantBelongsToRole(participant1.getID(), adminRole.getID()).participantBelongsToRole(participant2.getID(),
            adminRole.getID());

        identityBuilder.deleteRole(adminRole.getID());

        String failureMessage = "The Role 'Administrators' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getRoles().size() == 0, failureMessage);

        for (Participant participant : adminRole.getParticipants()) {
            failureMessage = "The Participant '" + participant.getID()
                + "' should not belong to the Role 'Administrators'.";
            Assert.assertFalse(participant.getMyRoles().contains(adminRole));
        }
    }

    /**
     * Test that the relationship between Participant and Role is removed properly.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDeleteParticipantRoleRelationship()
    throws Exception {

        Participant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");
        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID());

        identityBuilder.participantDoesNotBelongToRole(participant.getID(), adminRole.getID());

        String failureMessage = "The Role 'Administrators' does not contain any participants.";
        Assert.assertTrue(adminRole.getParticipants().size() == 0, failureMessage);

    }
}
