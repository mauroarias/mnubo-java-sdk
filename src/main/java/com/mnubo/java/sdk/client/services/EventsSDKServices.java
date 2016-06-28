package com.mnubo.java.sdk.client.services;

import static com.mnubo.java.sdk.client.Constants.OBJECT_PATH;
import static com.mnubo.java.sdk.client.utils.ValidationUtils.notBlank;
import static com.mnubo.java.sdk.client.utils.ValidationUtils.notNullNorEmpty;
import static com.mnubo.java.sdk.client.utils.ValidationUtils.validNotNull;
import static java.util.Arrays.*;

import java.util.*;

import com.mnubo.java.sdk.client.models.Event;
import com.mnubo.java.sdk.client.models.result.Result;
import com.mnubo.java.sdk.client.spi.EventsSDK;

class EventsSDKServices implements EventsSDK {

    public static final String EVENT_PATH = "/events";
    public static final String EVENT_PATH_SEGMENT = "events";
    public static final String EVENT_PATH_EXITS = EVENT_PATH + "/exists";
    private static final String REPORT_RESULTS_QUERY_PARAM = "report_results";
    private final SDKService sdkCommonServices;

    EventsSDKServices(SDKService sdkCommonServices) {
        this.sdkCommonServices = sdkCommonServices;
    }

    @Override
    public List<Result> send(String deviceId, List<Event> events) {

        notBlank(deviceId, "x_device_Id cannot be blank.");
        notNullNorEmpty(events, "Event list cannot be null or empty.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(OBJECT_PATH)
                                            .pathSegment(deviceId, EVENT_PATH_SEGMENT)
                                            .queryParam(REPORT_RESULTS_QUERY_PARAM, true)
                                            .build().toString();

        return postRequest(url, events);
    }



    @Override
    public List<Result> send(List<Event> events) {

        notNullNorEmpty(events, "Event list cannot be null or empty.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                                            .path(EVENT_PATH)
                                            .queryParam(REPORT_RESULTS_QUERY_PARAM, true)
                                            .build().toString();

        return postRequest(url, events);
    }

    @Override
    public List<Result> send(Event... events) {

        return send(asList(events));
    }

    @Override
    public List<Map<String, Boolean>> eventsExist(Set<String> eventIds) {
        validNotNull(eventIds, "List of the eventIds cannot be null.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                .path(EVENT_PATH_EXITS)
                .build().toString();

        List<Map<String,Boolean>> result = new ArrayList<>();
        return sdkCommonServices.postRequest(url, result.getClass(), eventIds);
    }

    @Override
    public Boolean isEventExist(String eventId) {
        notBlank(eventId, "eventId cannot be blank.");

        final String url = sdkCommonServices.getIngestionBaseUri()
                .path(EVENT_PATH_EXITS)
                .pathSegment(eventId)
                .build().toString();

        Map<String, Boolean> results = new HashMap<>();
        results = sdkCommonServices.getRequest(url, results.getClass());
        return results == null || results.size() != 1 || results.get("exists") == null ?
                false : results.get("exists");
    }

    private List<Result> postRequest(String url, Object object) {
        Result[] results = sdkCommonServices.postRequest(url, Result[].class, object);
        return results == null ? new ArrayList<Result>() : Arrays.asList(results);
    }
}
