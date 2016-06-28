package com.mnubo.java.sdk.client.spi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mnubo.java.sdk.client.models.Owner;
import com.mnubo.java.sdk.client.models.result.Result;

/**
 * Owner SDK Client. This interface gives access to handle Owners.
 */
public interface OwnersSDK {

    /**
     * Allows create owners.
     *
     * @param owner, Owner bean to be created.
     */
    void create(Owner owner);

    /**
     * Allows an owner claim an Object.
     *
     * @param username, Owner's username how claims the object.
     * @param deviceId, Object's deviceId of the Object claimed.
     */
    void claim(String username, String deviceId);

    /**
     * Allows update an existing owner.
     *
     * @param owner, Owner bean to be updated. note that "username" and "password" fields will be ignored.
     * @param username, Owner's "username" to be updated.
     *
     */
    void update(Owner owner, String username);

    /**
     * Allows delete an existing owner.
     *
     * @param username, Owner's "username" to be deleted.
     */
    void delete(String username);

    /**
     * Add or update a list of owners.  
     * If an owner doesn't exist, it will be created, otherwise it will be updated.
     *
     * @param owners, list of Owners bean to be created or updated.
     * 
     * @return the list of result for all the owners with corresponding id, status and
     * message.
     */
    List<Result> createUpdate(List<Owner> owners);

    /**
     * @see OwnersSDK#createUpdate(List)
     * @param owners
     * @return
     */
    List<Result> createUpdate(Owner... owners);

    /**
     * It checks if certain owners exist
     * @param usernames The list of usernames to check if exists. ["userA", "userb"]
     * @return The list of usernames with an existing boolean, true if it exists, false if it does not exist. [{"userA" : false},{"userb":true}]
     */
    List<Map<String, Boolean>> ownersExist(Set<String> usernames);

    /**
     * It checks if a specify owner exists
     * @param username The username to check if exists. "userA"
     * @return Existing boolean value. false
     */
    Boolean isOwnerExist(String username);
}
