package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.resource.worklist.RoleWorklist;
import de.hpi.oryxengine.resource.worklist.Worklist;

/**
 * Implementation of the {@link Role} interface.
 * 
 * @author Gerardo Navarro Suarez
 */
public class RoleImpl extends ResourceImpl<Role> implements Role {

    /** The participants. */
    private Set<ParticipantImpl> participants;

    /**
     * The Default Constructor for the Rolempl.
     * 
     * @param roleName
     *            - the name of the Role
     */
    public RoleImpl(String roleName) {

        super(roleName, ResourceType.ROLE);
    }

    @Override
    public Role getSuperRole() {

        return null;
    }

    // @Override
    // public Role setSuperRole(Role superRole) {
    //
    // return null;
    // }

    @Override
    public Set<Participant> getParticipants() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipantImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the participant implementations.
     *
     * @return the participant implementations
     */
    protected Set<ParticipantImpl> getParticipantImpls() {

        if (participants == null) {
            participants = new HashSet<ParticipantImpl>();
        }
        return participants;
    }
    
    @Override
    public Worklist getWorklist() {

        if (worklist == null) {

            worklist = new RoleWorklist(this);
        }
        return worklist;
    }
    
    /**
     * Translates a Role into a corresponding RoleImpl object.
     * 
     * Furthermore some constrains are checked.
     * 
     * @param roleId
     *            - a Role object
     * @return roleImpl - the casted Role object
     */
    public static RoleImpl asRoleImpl(UUID roleId) {

        if (roleId == null) {
            throw new DalmatinaRuntimeException("The Role parameter is null.");
        }
        RoleImpl roleImpl = (RoleImpl) ServiceFactory.getIdentityService().getRole(roleId);
        if (roleImpl == null) {
            
            throw new DalmatinaRuntimeException("There exists no Role with the id " + roleId + ".");
        }
        return roleImpl;
    }

}
