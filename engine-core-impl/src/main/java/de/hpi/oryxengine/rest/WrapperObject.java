package de.hpi.oryxengine.rest;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class WrapperObject.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public class WrapperObject {
    
    private WorklistItemAction action;
    private String participantId;
    
    /**
     * Gets the action.
     *
     * @return the action
     */
    @JsonProperty
    public WorklistItemAction getAction() {
    
        return action;
    }

    /**
     * Sets the action.
     *
     * @param action the new action
     */
    @JsonProperty
    public void setAction(WorklistItemAction action) {
    
        this.action = action;
    }

    /**
     * Gets the partcipant id.
     *
     * @return the partcipant id
     */
    @JsonProperty
    public String getParticipantId() {
    
        return participantId;
    }

    /**
     * Sets the partcipant id.
     *
     * @param partcipantId the new partcipant id
     */
    @JsonProperty
    public void setPartcipantId(String participantId) {
    
        this.participantId = participantId;
    }

    /**
     * Returns a concrete resource object holding the value of the specified String.
     * The argument is interpreted as json.
     * 
     * @param json json formated object
     * @return a concrete resource
     * @throws IOException failed to parse the json
     */
    public static @Nonnull WrapperObject valueOf(@Nonnull String json)
    throws IOException {
        System.out.println("valueOf called");
        // TODO we do not have access to the ServiceFactory, so we need a new ObjectMapper every time
        WrapperObject returnValue = (WrapperObject) new ObjectMapper().readValue(json, WrapperObject.class);
        System.out.println("valueOf returning");
        return returnValue;
    }
    

}
