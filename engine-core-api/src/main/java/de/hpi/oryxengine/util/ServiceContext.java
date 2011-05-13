package de.hpi.oryxengine.util;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.RepositoryServiceInside;
import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.navigator.NavigatorInside;

/**
 * This context provides basic functionality. It holds all relevant services that our system provides.
 */
public interface ServiceContext extends Attributable {

    /**
     * Gets the {@link RepositoryServiceInside repositoryService}.
     * 
     * @return the {@link RepositoryServiceInside}
     */
    RepositoryServiceInside getRepositiory();

    /**
     * Gets the {@link IdentityService identityService}.
     * 
     * @return the {@link IdentityService}
     */
    IdentityService getIdentityService();

    /**
     * Gets the {@link CorrelationManager correlationService}.
     * 
     * @return the {@link CorrelationManager}
     */
    CorrelationManager getCorrelationService();

    /**
     * Gets the {@link NavigatorInside navigatorService}.
     * 
     * @return the {@link NavigatorInside}
     */
    NavigatorInside getNavigatorService();
}
