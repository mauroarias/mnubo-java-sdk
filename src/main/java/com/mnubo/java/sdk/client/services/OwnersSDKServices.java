package com.mnubo.java.sdk.client.services;

import static com.mnubo.java.sdk.client.utils.ValidationUtils.notBlank;
import static com.mnubo.java.sdk.client.utils.ValidationUtils.validNotNull;
import static java.util.Arrays.*;

import java.util.*;

import com.mnubo.java.sdk.client.models.Owner;
import com.mnubo.java.sdk.client.models.result.Result;
import com.mnubo.java.sdk.client.spi.OwnersSDK;

class OwnersSDKServices implements OwnersSDK {

    public static final String OWNER_PATH = "/owners";
    public static final String OWNER_PATH_EXIST = OWNER_PATH + "/exists";
    private final SDKService sdkCommonServices;

    OwnersSDKServices(SDKService sdkCommonServices) {
        this.sdkCommonServices = sdkCommonServices;
    }

    @Override
    public void create(Owner owner) {
        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OWNER_PATH)
                                            .build().toString();

        validNotNull(owner, "Owner body");
        notBlank(owner.getUsername(), "usermame cannot be blank.");

        sdkCommonServices.postRequest(url, Owner.class, owner);
    }

    @Override
    public void claim(String username, String deviceId) {
        notBlank(username, "usermame cannot be blank.");
        notBlank(deviceId, "x_deviceId cannot be blank.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OWNER_PATH)
                                            .pathSegment(username, "objects", deviceId, "claim")
                                            .build().toString();

        sdkCommonServices.postRequest(url);
    }

    @Override
    public void update(Owner owner, String username) {
        notBlank(username, "usermame cannot be blank.");

        validNotNull(owner, "Owner body");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OWNER_PATH)
                                            .pathSegment(username)
                                            .build().toString();

        sdkCommonServices.putRequest(url, owner);
    }

    @Override
    public void delete(String username) {
        notBlank(username, "usermame cannot be blank.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OWNER_PATH)
                                            .pathSegment(username)
                                            .build().toString();

        sdkCommonServices.deleteRequest(url);
    }

    @Override
    public List<Result> createUpdate(List<Owner> owners) {
        validNotNull(owners, "List of owners body");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OWNER_PATH)
                                            .build().toString();

        Result[] results = sdkCommonServices.putRequest(url, owners, Result[].class);
        return results == null ? new ArrayList<Result>() : Arrays.asList(results);
    }

    @Override
    public List<Result> createUpdate(Owner... owners) {
        return createUpdate(asList(owners));
    }

    @Override
    public List<Map<String, Boolean>> ownersExist(List<String> usernames) {
        validNotNull(usernames, "List of the usernames cannot be null.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                .path(OWNER_PATH_EXIST)
                .build().toString();

        List<Map<String,Boolean>> result = new ArrayList<>();
        return sdkCommonServices.postRequest(url, result.getClass(), usernames);
    }

    @Override
    public Boolean ownerExist(String username) {
        notBlank(username, "username cannot be blank.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                .path(OWNER_PATH_EXIST)
                .pathSegment(username)
                .build().toString();

        Map<String, Boolean> results = new HashMap<>();
        results = sdkCommonServices.getRequest(url, results.getClass());
        return results == null || results.size() != 1 || results.get(username) == null ?
                false : results.get(username);
    }
}
