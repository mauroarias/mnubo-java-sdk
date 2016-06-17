package com.mnubo.java.sdk.client.services;

import static com.mnubo.java.sdk.client.services.OwnersSDKServices.OWNER_PATH;
import static com.mnubo.java.sdk.client.services.OwnersSDKServices.OWNER_PATH_EXIST;
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

import com.mnubo.java.sdk.client.models.Owner;
import com.mnubo.java.sdk.client.models.result.Result;
import com.mnubo.java.sdk.client.models.result.Result.ResultStates;
import com.mnubo.java.sdk.client.spi.OwnersSDK;

public class OwnersSDKServicesTest extends AbstractServiceTest {

    private OwnersSDK ownerClient;

    protected static ResponseEntity httpResponse = mock(ResponseEntity.class);

    @Before
    public void ownerSetup() {
        ownerClient = getClient().getOwnerClient();

        Result[] resultsMockSetup = {
                new Result("idOwnerTest1", ResultStates.success, "", false),
                new Result("idOwnerTest2", ResultStates.error, "Invalid attribute X for the Owner", false),
                new Result("idOwnerTest3", ResultStates.error, "Error Z", false),
                new Result("idOwnerResult4", ResultStates.success, "", false)
        };

        // Mock Call PUT Owners
        when(httpResponse.getBody()).thenReturn(resultsMockSetup);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Result[].class)))
                         .thenReturn(httpResponse);
    }

    @Test
    public void createOwnerThenOk() {

        Owner owner = Owner.builder().withUsername("test").build();

        String url = getClient().getSdkService().getIngestionBaseUri().path(OWNER_PATH).build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/owners",HOST))));

        ownerClient.create(owner);
    }

    @Test
    public void createOwnerNullThenFail() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Owner body cannot be null.");
        ownerClient.create(null);
    }

    @Test
    public void createOwnerUsernameNullThenFail() {

        Owner owner = Owner.builder().withUsername(null).build();

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.create(owner);
    }

    @Test
    public void createOwnerUsernameEmptyThenFail() {

        Owner owner = Owner.builder().withUsername("").build();

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.create(owner);
    }

    @Test
    public void ownerClaimingObjectUsernameNullThenFail() {

        String username = null;
        String deviceId = "deviceId";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.claim(username, deviceId);
    }

    @Test
    public void ownerClaimingObjectUsernameEmptyThenFail() {

        String username = "";
        String deviceId = "deviceId";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.claim(username, deviceId);
    }

    @Test
    public void ownerClaimingObjectDeviceNullThenFail() {

        String username = "username";
        String deviceId = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("deviceId cannot be blank.");
        ownerClient.claim(username, deviceId);
    }

    @Test
    public void ownerClaimingObjectDeviceEmptyThenFail() {

        String username = "username";
        String deviceId = "";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("deviceId cannot be blank.");
        ownerClient.claim(username, deviceId);
    }

    @Test
    public void ownerClaimingObjectThenOk() {

        String username = "username";
        String deviceId = "deviceId";

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OWNER_PATH)
                .pathSegment(username, "objects", deviceId, "claim").build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/owners/%s/objects/%s/claim",HOST, username,
                deviceId))));

        ownerClient.claim(username, deviceId);
    }

    @Test
    public void deleteOwnerThenOk() {

        String username = "username";

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OWNER_PATH).pathSegment(username)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/owners/%s",HOST, username))));

        ownerClient.delete(username);
    }

    @Test
    public void deleteOwnerUsernameNullThenFail() {

        String username = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.delete(username);
    }

    @Test
    public void deleteOwnerUsernameEmptyThenFail() {

        String username = "";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.delete(username);
    }

    @Test
    public void updateOwnerThenOk() {

        String username = "username";

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OWNER_PATH).pathSegment(username)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/owners/%s",HOST, username))));

        ownerClient.update(Owner.builder().build(), username);
    }

    @Test
    public void updateOwnerUsernameNullThenFail() {

        String username = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.update(Owner.builder().withUsername(null).build(), username);
    }

    @Test
    public void updateOwnerUsernameEmptyThenFail() {

        String username = "";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("usermame cannot be blank.");
        ownerClient.update(Owner.builder().withUsername("").build(), username);
    }

    @Test
    public void updateOwnerOwnerNullThenFail() {

        String username = "username";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Owner body cannot be null.");
        ownerClient.update(null, username);
    }

    @Test
    public void createUpdateThenOk() {

        List<Owner> owners = new ArrayList<>();
        owners.add(Owner.builder().withUsername("user1").build());
        owners.add(Owner.builder().withUsername("user2").build());
        owners.add(Owner.builder().withUsername("user3").build());
        owners.add(Owner.builder().withUsername("user4").build());

        String url = getClient().getSdkService().getIngestionBaseUri().path(OWNER_PATH).build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/owners", HOST))));

        List<Result> results = ownerClient.createUpdate(owners);

        validateResult(results);
    }

    @Test
    public void createUpdateWithVarargThenOk() {
        Owner owner = Owner.builder().withUsername("user1").build();

        List<Result> results = ownerClient.createUpdate(owner);

        validateResult(results);
    }

    @Test
    public void existObjectThenOk() {

        String username = "user";

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OWNER_PATH_EXIST).pathSegment(username)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/owners/exists/%s",HOST, username))));

        ownerClient.ownerExist(username);
    }

    @Test
    public void existObjectDeviceIdNullThenFail() {
        String usernames = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("username cannot be blank.");
        ownerClient.ownerExist(usernames);
    }

    @Test
    public void existObjectsThenOk() {

        List<String> username = new ArrayList<String>() {{
            add("username");
        }};

        final String url = getClient().getSdkService().getIngestionBaseUri().path(OWNER_PATH_EXIST)
                .build().toString();

        assertThat(url, is(equalTo(format("https://%s:443/api/v3/owners/exists",HOST))));

        ownerClient.ownersExist(username);
    }

    @Test
    public void existObjectsDeviceIdNullThenFail() {
        List<String> usernames = null;

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("List of the usernames cannot be null.");
        ownerClient.ownersExist(usernames);
    }

    private void validateResult(List<Result> results) {
        assertThat(results.size(), equalTo(4));

        assertThat(results.get(0).getId(), equalTo("idOwnerTest1"));
        assertThat(results.get(1).getId(), equalTo("idOwnerTest2"));
        assertThat(results.get(2).getId(), equalTo("idOwnerTest3"));
        assertThat(results.get(3).getId(), equalTo("idOwnerResult4"));

        assertThat(results.get(0).getResult(), equalTo(ResultStates.success));
        assertThat(results.get(1).getResult(), equalTo(ResultStates.error));
        assertThat(results.get(2).getResult(), equalTo(ResultStates.error));
        assertThat(results.get(3).getResult(), equalTo(ResultStates.success));

        assertThat(results.get(0).getMessage(), equalTo(""));
        assertThat(results.get(1).getMessage(), equalTo("Invalid attribute X for the Owner"));
        assertThat(results.get(2).getMessage(), equalTo("Error Z"));
        assertThat(results.get(3).getMessage(), equalTo(""));
    }
}
