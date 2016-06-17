package com.mnubo.java.sdk.client.services;

import static com.mnubo.java.sdk.client.Constants.OBJECT_PATH;
import static com.mnubo.java.sdk.client.services.ObjectsSDKServices.OBJECT_PATH_EXITS;
import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.mnubo.java.sdk.client.models.SmartObject;
import com.mnubo.java.sdk.client.models.result.Result;
import com.mnubo.java.sdk.client.models.result.Result.ResultStates;
import com.mnubo.java.sdk.client.spi.ObjectsSDK;

public class ObjectsSDKServicesTest extends AbstractServiceTest {
    private ObjectsSDK objectClient;

    protected static ResponseEntity httpResponse = mock(ResponseEntity.class);

    @Before
    public void objectSetup() {
        objectClient = getClient().getObjectClient();

        Result[] resultsMockSetup = {
                new Result("idObjectTest1", ResultStates.success, "", false),
                new Result("idObjectResult2", ResultStates.error, "Invalid attribute X for the Object", false),
                new Result("idObjectTest3", ResultStates.success, "", false),
                new Result("idObjectTest4", ResultStates.error, "Error Y", false)
        };

        // Mock Call PUT Objects
        when(httpResponse.getBody()).thenReturn(resultsMockSetup);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Result[].class)))
                         .thenReturn(httpResponse);
    }

    @Test
    public void createObjectThenOk() {

        SmartObject object = SmartObject.builder().withObjectType("type").withDeviceId("device").build();

        String url = getClient().getSdkService().getIngestionBaseUri().path(OBJECT_PATH).build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/objects",HOST))));

        objectClient.create(object);
    }

    @Test
    public void createObjectNullThenFail() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Object body cannot be null.");
        objectClient.create(null);
    }

    @Test
    public void createObjectObjectTypeNullThenFail() {

        SmartObject object = SmartObject.builder().withObjectType(null).withDeviceId("deviceId").build();

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("x_object_Type cannot be blank.");
        objectClient.create(object);
    }

    @Test
    public void createObjectObjectTypeEmptyThenFail() {

        SmartObject object = SmartObject.builder().withObjectType("").withDeviceId("deviceId").build();

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("x_object_Type cannot be blank.");
        objectClient.create(object);
    }

    @Test
    public void createObjectDeviceIdNullThenFail() {

        SmartObject object = SmartObject.builder().withObjectType("type").withDeviceId(null).build();

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("x_device_Id cannot be blank.");
        objectClient.create(object);
    }

    @Test
    public void createObjectDeviceIdEmptyThenFail() {

        SmartObject object = SmartObject.builder().withObjectType("type").withDeviceId("").build();

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("x_device_Id cannot be blank.");
        objectClient.create(object);
    }

    @Test
    public void deleteObjectThenOk() {

        String deviceId = "deviceId";

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OBJECT_PATH).pathSegment(deviceId)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/objects/%s",HOST, deviceId))));

        objectClient.delete(deviceId);
    }

    @Test
    public void deleteObjectDeviceIdNullThenFail() {

        String deviceId = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("device_Id cannot be blank.");
        objectClient.delete(deviceId);
    }

    @Test
    public void deleteObjectDeviceIdEmptyThenFail() {

        String deviceId = "";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("device_Id cannot be blank.");
        objectClient.delete(deviceId);
    }

    @Test
    public void updateObjectThenOk() {

        String deviceId = "deviceId";

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OBJECT_PATH).pathSegment(deviceId)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/objects/%s",HOST, deviceId))));

        objectClient.update(SmartObject.builder().build(), deviceId);
    }

    @Test
    public void updateObjectDeviceIdNullThenFail() {

        String deviceId = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("device_Id cannot be blank.");
        objectClient.update(SmartObject.builder().withDeviceId(null).build(), deviceId);
    }

    @Test
    public void updateObjectDeviceIdEmptyThenFail() {

        String deviceId = "";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("device_Id cannot be blank.");
        objectClient.update(SmartObject.builder().withDeviceId("").build(), deviceId);
    }

    @Test
    public void updateObjectOwnerNullThenFail() {

        String deviceId = "deviceId";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Object body cannot be null.");
        objectClient.update(null, deviceId);
    }

    @Test
    public void createUpdateThenOk() {

        List<SmartObject> objects = new ArrayList<>();
        objects.add(SmartObject.builder().withObjectType("type").withDeviceId("device1").build());
        objects.add(SmartObject.builder().withObjectType("type").withDeviceId("device2").build());
        objects.add(SmartObject.builder().withObjectType("type").withDeviceId("device3").build());
        objects.add(SmartObject.builder().withObjectType("type").withDeviceId("device4").build());

        String url = getClient().getSdkService().getIngestionBaseUri().path(OBJECT_PATH).build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/objects", HOST))));

        List<Result> results = objectClient.createUpdate(objects);

        validateResult(results);
    }

    @Test
    public void createUpdateWithVarargThenOk() {
        SmartObject smartObject = SmartObject.builder().withObjectType("type").withDeviceId("device1").build();

        List<Result> results = objectClient.createUpdate(smartObject);

        validateResult(results);
    }

    @Test
    public void existObjectThenOk() {

        String deviceId = "deviceId";

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OBJECT_PATH_EXITS).pathSegment(deviceId)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/objects/exists/%s",HOST, deviceId))));

        objectClient.objectExist(deviceId);
    }

    @Test
    public void existObjectDeviceIdNullThenFail() {
        String deviceId = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("deviceId cannot be blank.");
        objectClient.objectExist(deviceId);
    }

    @Test
    public void existObjectsThenOk() {

        List<String> deviceId = new ArrayList<String>() {{
            add("deviceId");
        }};

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OBJECT_PATH_EXITS)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/objects/exists",HOST))));

        objectClient.objectsExist(deviceId);
    }

    @Test
    public void existObjectsDeviceIdNullThenFail() {
        List<String> deviceId = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("List of the deviceIds cannot be null.");
        objectClient.objectsExist(deviceId);
    }

    private void validateResult(List<Result> results) {
        assertThat(results.size(), equalTo(4));

        assertThat(results.get(0).getId(), equalTo("idObjectTest1"));
        assertThat(results.get(1).getId(), equalTo("idObjectResult2"));
        assertThat(results.get(2).getId(), equalTo("idObjectTest3"));
        assertThat(results.get(3).getId(), equalTo("idObjectTest4"));

        assertThat(results.get(0).getResult(), equalTo(ResultStates.success));
        assertThat(results.get(1).getResult(), equalTo(ResultStates.error));
        assertThat(results.get(2).getResult(), equalTo(ResultStates.success));
        assertThat(results.get(3).getResult(), equalTo(ResultStates.error));

        assertThat(results.get(0).getMessage(), equalTo(""));
        assertThat(results.get(1).getMessage(), equalTo("Invalid attribute X for the Object"));
        assertThat(results.get(2).getMessage(), equalTo(""));
        assertThat(results.get(3).getMessage(), equalTo("Error Y"));
    }
}
