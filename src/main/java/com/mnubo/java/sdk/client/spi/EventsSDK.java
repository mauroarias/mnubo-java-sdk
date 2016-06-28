package com.mnubo.java.sdk.client.spi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mnubo.java.sdk.client.models.Event;
import com.mnubo.java.sdk.client.models.result.Result;

/**
 * Event SDK Client. This interface gives access to handle events in specific objects.
 */
public interface EventsSDK {

    /**
     * Allows post events to an unique object. Note that in this case all Event.
     *
     * @param deviceId, device Id to send the events
     * @param events, list the events to be posted.
     * 
     * @return the list of result for all the events with corresponding id, status and
     * message.
     */
    List<Result> send(String deviceId, List<Event> events);

    /**
     * Allows post events to several objects. In this case, the device id is taken
     * directly from each event. Thus, a post will be sent by each event in the list.
     *
     * @param events, list the events to be posted.
     * 
     * @return the list of result for all the events with corresponding id, status and
     * message.
     */
    List<Result> send(List<Event> events);

    /**
     * @see EventsSDK#send(List)
     * @param events
     * @return
     */
    List<Result> send(Event... events);

        /**
     * It checks if certain event exist
     * @param eventIds The list of event Ids to check if exists. ["eventA", "eventb"]
     * @return The list of event ids with an existing boolean, true if it exists, false if it does not exist. [{"eventA" : false},{"eevntb":true}]
     */
    List<Map<String, Boolean>> eventsExist(Set<String> eventIds);

    /**
     * It checks if a specify event exist
     * @param eventId The event Id to check if exists. "eventA"
     * @return Existing boolean value. false
     */
    Boolean isEventExist(String eventId);
}
