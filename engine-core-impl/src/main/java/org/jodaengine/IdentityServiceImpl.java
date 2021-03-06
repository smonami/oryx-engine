package org.jodaengine;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.resource.AbstractOrganizationUnit;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractPosition;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.IdentityBuilderImpl;
import org.jodaengine.resource.IdentityService;
import org.jodaengine.resource.OrganizationUnit;
import org.jodaengine.resource.Participant;
import org.jodaengine.resource.Position;
import org.jodaengine.resource.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The IdentityServiceImpl is concrete implementation of the {@link IdentityService} that is provided by the engine.
 * This implementation is designed to store all information regarding the organization structure in the engine. Others
 * Identity Service implementation might think about database connections.
 */
public class IdentityServiceImpl implements IdentityService, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Map<UUID, OrganizationUnit> organizationUnits;
    
    private Map<UUID, Position> positions;
    
    private Map<UUID, Participant> participants;
    
    private Map<UUID, Role> roles;
    
    private boolean running = false;
    
    @Override
    public synchronized void start(JodaEngineServices services) {
        
        logger.info("Starting the identity service");
        this.running = true;
    }

    @Override
    public synchronized void stop() {
        
        logger.info("Stopping the identity service");
        this.running = false;
    }
    
    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public IdentityBuilder getIdentityBuilder() {

        return new IdentityBuilderImpl(this);
    }

    @Override
    public Set<AbstractOrganizationUnit> getOrganizationUnits() {

        Set<AbstractOrganizationUnit> setToReturn = new HashSet<AbstractOrganizationUnit>(getOrganizationUnitImpls()
        .values());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a mutable map of orga units.
     * 
     * @return a mutable orga units map.
     */
    public Map<UUID, OrganizationUnit> getOrganizationUnitImpls() {

        if (organizationUnits == null) {
            organizationUnits = new HashMap<UUID, OrganizationUnit>();
        }
        return organizationUnits;
    }

    @Override
    public Set<AbstractPosition> getPositions() {

        Set<AbstractPosition> setToReturn = new HashSet<AbstractPosition>(getPositionImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }
    
    /**
     * Returns a mutable map of positions.
     * 
     * @return a mutable positions map.
     */
    public Map<UUID, Position> getPositionImpls() {

        if (positions == null) {
            positions = new HashMap<UUID, Position>();
        }
        return positions;
    }

    @Override
    public Set<AbstractParticipant> getParticipants() {

        Set<AbstractParticipant> setToReturn = new HashSet<AbstractParticipant>(getParticipantImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a mutable map of participants.
     * 
     * @return a mutable participants map.
     */
    public Map<UUID, Participant> getParticipantImpls() {

        if (participants == null) {
            participants = new HashMap<UUID, Participant>();
        }
        return participants;
    }

    @Override
    public Set<AbstractRole> getRoles() {

        Set<AbstractRole> setToReturn = new HashSet<AbstractRole>(getRoleImpls().values());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Returns a mutable map of roles.
     * 
     * @return a mutable roles map.
     */
    public Map<UUID, Role> getRoleImpls() {

        if (roles == null) {
            roles = new HashMap<UUID, Role>();
        }
        return roles;
    }

    @Override
    public @Nullable AbstractOrganizationUnit getOrganizationUnit(@Nonnull UUID id) {

        return getOrganizationUnitImpls().get(id);
//        return find(getOrganizationUnitImpls(), id);
    }

    @Override
    public @Nullable AbstractPosition getPosition(@Nonnull UUID id) {

        return getPositionImpls().get(id);
//        return find(getPositionImpls(), id);
    }

    @Override
    public @Nullable AbstractParticipant getParticipant(@Nonnull UUID id) throws ResourceNotAvailableException {

        AbstractParticipant participant = getParticipantImpls().get(id);
        if (participant == null) {
            throw new ResourceNotAvailableException(AbstractParticipant.class, id);
        }
        return participant;
//        return find(getParticipants(), id);
    }

    @Override
    public @Nullable AbstractRole getRole(@Nonnull UUID id) throws ResourceNotAvailableException {

        Role roleToReturn = getRoleImpls().get(id);
        if (roleToReturn == null) {
            throw new ResourceNotAvailableException(AbstractRole.class, id);
        }
        return roleToReturn;
    }
}
