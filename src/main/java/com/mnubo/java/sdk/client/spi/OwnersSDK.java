package com.mnubo.java.sdk.client.spi;

import com.mnubo.java.sdk.client.models.Owner;

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

}
