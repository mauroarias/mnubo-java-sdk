package com.mnubo.java.sdk.client.services;

import static com.mnubo.java.sdk.client.Constants.OBJECT_PATH;
import static com.mnubo.java.sdk.client.utils.ValidationUtils.notBlank;
import static com.mnubo.java.sdk.client.utils.ValidationUtils.validNotNull;
import static java.util.Arrays.*;

import java.util.*;

import com.mnubo.java.sdk.client.models.SmartObject;
import com.mnubo.java.sdk.client.models.result.Result;
import com.mnubo.java.sdk.client.spi.ObjectsSDK;

class ObjectsSDKServices implements ObjectsSDK {
    public static final String OBJECT_PATH_EXITS = OBJECT_PATH + "/exists";

    private final SDKService sdkCommonServices;

    ObjectsSDKServices(SDKService sdkCommonServices) {
        this.sdkCommonServices = sdkCommonServices;
    }

    @Override
    public void create(SmartObject object) {
        validNotNull(object, "Object body");
        notBlank(object.getObjectType(), "x_object_Type cannot be blank.");
        notBlank(object.getDeviceId(), "x_device_Id cannot be blank.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OBJECT_PATH)
                                            .build().toString();

        sdkCommonServices. postRequest(url, SmartObject.class, object);
    }

    @Override
    public void update(SmartObject object, String deviceId) {
        notBlank(deviceId, "x_device_Id cannot be blank.");
        validNotNull(object, "Object body");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OBJECT_PATH)
                                            .pathSegment(deviceId)
                                            .build().toString();

        sdkCommonServices.putRequest(url, object);
    }

    @Override
    public void delete(String deviceId) {
        notBlank(deviceId, "x_device_Id cannot be blank.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OBJECT_PATH)
                                            .pathSegment(deviceId)
                                            .build().toString();

        sdkCommonServices.deleteRequest(url);
    }

    @Override
    public List<Result> createUpdate(List<SmartObject> objects) {
        validNotNull(objects, "List of smart objects body");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OBJECT_PATH)
                                            .build().toString();

        Result[] results = sdkCommonServices.putRequest(url, objects, Result[].class);
        return results == null ? new ArrayList<Result>() : Arrays.asList(results);
    }

    @Override
    public List<Result> createUpdate(SmartObject... objects) {
        return createUpdate(asList(objects));
    }

    @Override
    public List<Map<String, Boolean>> objectsExist(List<String> deviceIds) {
        validNotNull(deviceIds, "List of the deviceIds cannot be null.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                .path(OBJECT_PATH_EXITS)
                .build().toString();

        List<Map<String,Boolean>> result = new ArrayList<>();
        return sdkCommonServices.postRequest(url, result.getClass(), deviceIds);
    }

    @Override
    public Boolean objectExist(String deviceId) {
        notBlank(deviceId, "deviceId cannot be blank.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                .path(OBJECT_PATH_EXITS)
                .pathSegment(deviceId)
                .build().toString();

        Map<String, Boolean> results = new HashMap<>();
        results = sdkCommonServices.getRequest(url, results.getClass());
        return results == null || results.size() != 1 || results.get(deviceId) == null ?
                false : results.get(deviceId);
    }
}
