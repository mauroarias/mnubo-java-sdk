# mnubo Java SDK

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [mnubo Java SDK](#mnubo-java-sdk)
- [Introduction](#introduction)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation & Configuration](#installation-&-configuration)
  - [Maven](#maven)
  - [Download source code](#download-source-code)
    - [Configuration](#configuration)
- [Usage](#usage)
  - [Getting a "MnuboSDKClient" (client) instance](#getting-a-mnubosdkclient-client-instance)
    - [Basic Configuration](#basic-configuration)
    - [Advanced Configuration](#advanced-configuration)
  - [Creating owners](#creating-owners)
  - [Updating owners](#updating-owners)
  - [Deleting owners](#deleting-owners)
  - [Claiming an object](#claiming-an-object)
  - [Creating objects](#creating-objects)
  - [Updating objects](#updating-objects)
  - [Deleting objects](#deleting-objects)
  - [Sending Events](#sending-events)
        - [To send multiple events to a single object:](#to-send-multiple-events-to-a-single-object)
    - [To send multiple events to multiple objects:](#to-send-multiple-events-to-multiple-objects)
  - [Searching](#searching)
    - [Sending search query](#sending-search-query)
  - [Retrieving datasets](#retrieving-datasets)
- [References](#references)
- [Configuring the example](#configuring-the-example)
  - [Running the example](#running-the-example)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->




Introduction
============

This is a Java opinionated version of the original [API documentation](https://sop.mtl.mnubo.com/apps/doc/?i=t).

Architecture
============
Use class `MnuboSDKFactory` to get a client instance for ObjectsSDK, OwnersSDK, EventsSDK or SearchSDK.  Then the appropriate client API.

Current SDK support only synchronous call.


Prerequisites
=============

- Using this java SDK required JAVA 1.7 or higher.
- The SDK has been built build maven 3.2.3.


Installation & Configuration
============================

Include the mnubo client in your Java application using:

Maven
-----
Add the following into your pom.xml:
```
<dependency>
    <groupId>com.mnubo</groupId>
    <artifactId>java-sdk-client</artifactId>
    <scope>compile</scope>
    <version>1.4.1</version>
</dependency>
```

Download source code
---------------------
Download the source code and include it in your Java Application project.

### Configuration
The following parameters must be configured before using the mnubo client:

    - **hostname**.- mnubo's server name, for example: ```rest.sandbox.mnubo.com```.
    - **consumer-key**.- Your unique client identity which is provided by mnubo.
    - **consumer-secret**.- Your secret key which is used in conjunction with the consumer key to access the mnubo server. This key is provided by mnubo.


Usage
=====

Getting a "MnuboSDKClient" (client) instance
--------------------------------------------
To get a client instance use the **"MnuboSDKFactory" Class**. Note that you only need one client instance. We provide multithreading support and a pool of connection.

There are two ways to obtain a client instance:

### Basic Configuration
Using the basic method three mandatory parameters are required – Host, consumer key and consumer secret. Please see the example below:


```
//Example:
//Configure constants
private final String HOST = "rest.sandbox.mnubo.com";
private final String CONSUMER_KEY = "your consumer key!!!";
private final String CONSUMER_SECRET = "your consumer SECRET!!!";

//Obtain a client instance using default values.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );
```

### Advanced Configuration
Using this method you configure mandatory and optional parameters in a properties file. If you wish to use this method please contact mnubo.

Creating owners
---------------
To create an owner you need to:
1. Request an OwnersSDK interface using the mnubo client instance.
2. Build an owner.

This example describes how to create an Owner:
```
//Request a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Owners client interface
OwnersSDK mnuboOwnersClient = mnuboClient.getOwnerClient();

//build the owner
Owner Owner2Create = Owner.builder()
                          .withUsername("john.smith@mycompany.com")
                          .withPassword("dud7#%^ddd_J")
                          .withRegistrationDate(DateTime.parse("2015-01-01T00:00:00+04:00"))
                          .withAddedAttribute("age", 35)
                          .withAddedAttribute("gender", "male")
                          .withAddedAttribute("height", 1.80)
                          .build();

//create the owner
mnuboOwnersClient.create( Owner2Create );
```

You can also create an owner using a JSON file:

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Owners client interface
OwnersSDK mnuboOwnersClient = mnuboClient.getOwnerClient();

//read the Owner from the JSON file
String owner2BePosted = readingFile( "myOwnerFile.json" );

//deserialise the JSON file
Owner owner2Create = SDKMapperUtils.readValue( owner2BePosted , Owner.class );

//create the owner
mnuboOwnersClient.create( owner2Create );
```

The JSON file "myOwnerFile.json" is as follows:

```
{
  "username":"john.smith@mycompany.com",
  "x_password":"dud7#%^ddd_J",
  "x_registration_date":"2015-01-01T00:00:00+04:00",
  "age":35,
  "gender":"male",
  "height":1.80
}
```

Updating owners
---------------
To update an owner you need to:
1. Request an OwnersSDK interface using the mnubo client instance.
2. Build an owner with the parameters to change.

This example describes how to update an Owner:
```
//Request a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Owners client interface
OwnersSDK mnuboOwnersClient = mnuboClient.getOwnerClient();

//build the owner, including only the parameters to update. Remember "username" & "password" cannot change
Owner Owner2Update = Owner.builder()
                          .withRegistrationDate(DateTime.parse("2015-01-02T00:00:00+04:00"))
                          .withAddedAttribute("age", 38)
                          .build();

//update the owner
mnuboOwnersClient.update( Owner2Update, "john.smith@mycompany.com" );
```

You can also update an owner using a JSON file:

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Owners client interface
OwnersSDK mnuboOwnersClient = mnuboClient.getOwnerClient();

//read the Owner from the JSON file
String owner2BePosted = readingFile( "myOwnerFile.json" );

//deserialise the JSON file
Owner owner2Update = SDKMapperUtils.readValue( owner2BePosted , Owner.class );

//update the owner
mnuboOwnersClient.update( owner2Update, "john.smith@mycompany.com" );
```

The JSON file "myOwnerFile.json" is as follows. remember only parameters to change:

```
{
  "x_registration_date":"2015-01-02T00:00:00+04:00",
  "age":38,
}
```

Deleting owners
---------------

This example describes how to delete an Owner:
```
//Request a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Owners client interface
OwnersSDK mnuboOwnersClient = mnuboClient.getOwnerClient();

//delete the owner
mnuboOwnersClient.delete( "john.smith@mycompany.com" );
```

Claiming an object
------------------

This example describes how to link an Object with an Owner:
```
//Request a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Owners client interface
OwnersSDK mnuboOwnersClient = mnuboClient.getOwnerClient();

//claim the object
mnuboOwnersClient.claim( "john.smith@mycompany.com", "my_device_Id" );
```

Creating objects
----------------------
To create an object:
1. Request an ObjectSDK interface from the mnubo client instance.
2. Build an object.

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Object client interface
ObjectsSDK mnuboObjectClient = mnuboClient.getObjectClient();

//build the object
SmartObject object2Create = SmartObject.builder()
                                       .withDeviceId("connect_alpha.6hv135nw00393.1234567")
                                       .withObjectType("gateway")
                                       .withOwner("john.smith@mycompany.com")
                                       .withRegistrationDate(DateTime.now())
                                       .withAddedAttribute("partnerid", "connect_alpha")
                                       .withAddedAttribute("business_line", "connect")
                                       .withAddedAttribute("siteid", "6hv135nw00393")
                                       .withAddedAttribute("site_description", "My connected House")
                                       .build();

//create the object
mnuboObjectClient.create( object2Create );
```

You can also create an object using a JSON file

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Object client interface
ObjectsSDK mnuboObjectClient = mnuboClient.getObjectClient();

//read the Object from the JSON file
String object2BePosted = readingFile( "myObjectFile.json" );

//deserialise the JSON file
SmartObject object2Create = SDKMapperUtils.readValue( object2BePosted , SmartObject.class );

//create the object
mnuboObjectClient.create( object2Create );
```

The JSON file "myObjectFile.json" is as follows:

```
{
    "x_device_id" : "connect_alpha.6hv135nw00393.1234567",
    "x_object_type" : "gateway",
    "x_registration_date":"2015-01-27T08:01:01.000Z",
    "partnerid" : "connect_alpha",
    "business_line" : "connect",
    "siteid" : "6hv135nw00393",
    "site_description" : "My connected House",
    "x_owner":
    {
           "username" : "john.smith@mycompany.com"
    }
}
```

Updating objects
----------------
To update an object:
1. Request an ObjectSDK interface from the mnubo client instance.
2. Build an object with parameters to change.
3. Note that an owner cannot be added during update

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Object client interface
ObjectsSDK mnuboObjectClient = mnuboClient.getObjectClient();

//build the object, only with parameters to change.
SmartObject object2Update = SmartObject.builder()
                                       .withDeviceId("connect_beta.6hv135nw00393.1234567")
                                       .withObjectType("gateway1")
                                       .withAddedAttribute("site_description", "My connected building")
                                       .build();

//update the object
mnuboObjectClient.update( object2Update, "connect_alpha.6hv135nw00393.1234567" );
```

You can also update an object using a JSON file

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Object client interface
ObjectsSDK mnuboObjectClient = mnuboClient.getObjectClient();

//read the Object from the JSON file
String object2BePosted = readingFile( "myObjectFile.json" );

//deserialise the JSON file
SmartObject object2Update = SDKMapperUtils.readValue( object2BePosted , SmartObject.class );

//update the object
mnuboObjectClient.update( object2Update, "connect_alpha.6hv135nw00393.1234567" );
```

The JSON file "myObjectFile.json" is as follows:

```
{
    "x_device_id" : "connect_beta.6hv135nw00393.1234567",
    "x_object_type" : "gateway1",
    "site_description" : "My connected building",
}
```

Deleting objects
----------------

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Object client interface
ObjectsSDK mnuboObjectClient = mnuboClient.getObjectClient();

//delete the object
mnuboObjectClient.delete( "connect_alpha.6hv135nw00393.1234567" );
```

Sending Events
--------------
To send events:
1. Request an OwnersSDK interface from the mnubo client instance.
2. Build an event.

##### To send multiple events to a single object:

```
//private String objectID = "mythermostat0301424";

//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Event client interface
EventsSDK mnuboEventClient = mnuboClient.getEventClient();

//build the events
List<Event> event2Send = new ArrayList<Event>();
Event event1 = Event.builder()
                    .withEventType("thermostat_temperature")
                    .withEventID(UUID.fromString("aac652a3-955e-4ac4-b185-b7c2f44111cc"))
                    .withTimestamp(DateTime.now())
                    .withAddedTimeseries("temperature", 20)
                    .withAddedTimeseries("errorcode", "")
                    .withAddedTimeseries("varname", "temperature")
                    .build();
event2Send.add(event1);
Event event2 = Event.builder()
                    .withEventType("thermostat_temperature")
                    .withEventID(UUID.fromString("583acbba-8a4f-4503-ac49-bc5a8e4f7577"))
                    .withTimestamp(DateTime.now())
                    .withAddedTimeseries("temperature", 22)
                    .withAddedTimeseries("cause_type", "normal")
                    .withAddedTimeseries("varname", "temperature")
                    .build();
event2Send.add(event2);

//send the event
mnuboEventClient.send( objectID, event2Send );
```

You can also send multiple events to a single object using a JSON file:

```
//private String objectID = "mythermostat0301424";

//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Event client interface
EventsSDK mnuboEventClient = mnuboClient.getEventClient();

//read the event from the JSON file
String event2BeSent = readingFile( "myEvents.json" );

//deserialise the JSON file
EventValues event2Send = SDKMapperUtils.readValue( event2BeSent , Events.class );

//send the event
mnuboEventClient.send( objectID, event2Send );
```

The JSON file " myEventsByObjectFile.json " is as follows:

```
[
    {
        "x_event_type" : "thermostat_temperature",
        "event_id": "11111111-2222-3333-4444-555555555555",
        "thermostat_temperature": 20,
        "errorcode": "",
        "varname": "temperature",
    },
    {
        "x_event_type" : "thermostat_temperature",
        "event_id": "11111111-2222-3333-6666-555555555555",
        "thermostat_temperature": 22,
        "cause_type": "normal",
        "varname": "temperature",
    }
]
```
### To send multiple events to multiple objects:

```
//private String objectID = "mythermostat0301424";

//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Event client interface
EventsSDK mnuboEventClient = mnuboClient.getEventClient();

//build the events
List<Event> event2Send = new ArrayList<Event>();
Event event1 = Event.builder()
                    .withEventType("thermostat_temperature")
                    .withSmartObject("connect_alpha.6hv135nw00393.81")
                    .withTimestamp(DateTime.now())
                    .withAddedTimeseries("temperature", 20)
                    .withAddedTimeseries("errorcode", "")
                    .withAddedTimeseries("varname", "temperature")
                    .build();
event2Send.add(event1);
Event event2 = Event.builder()
                    .withEventType("light_dimmer.internal")
                    .withSmartObject("connect_alpha.6hv135nw00393.82")
                    .withEventID(UUID.fromString("d7284e6b-cf62-4d70-bf79-6ff81af97d7f"))
                    .withTimestamp(DateTime.now())
                    .withAddedTimeseries("light_dimmer", 0)
                    .withAddedTimeseries("cause_type", "internal")
                    .withAddedTimeseries("varname", "level")
                    .withAddedTimeseries("varfunction", "light-dimmer")
                    .build();
event2Send.add(event2);
Event event3 = Event.builder()
                    .withEventType("mask_masked.internal")
                    .withSmartObject("connect_alpha.6hv135nw00393.83")
                    .withTimestamp(DateTime.now())
                    .withAddedTimeseries("mask_masked", "Not Masked")
                    .withAddedTimeseries("cause_type", "internal")
                    .withAddedTimeseries("varname", "mask-state")
                    .build();
event2Send.add(event3);

//send the event
mnuboEventClient.send( event2Send );
```

You can also send multiple events to multiple objects using a JSON file:

```
//get a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//get an Event client interface
EventsSDK mnuboEventClient = mnuboClient.getEventClient();

//read event from the JSON file
String event2Besent = readingFile( "myEvents.json" );

//deserialise the JSON file
EventValues event2Send = SDKMapperUtils.readValue( event2Besent , EventValues.class );

//send the event
mnuboEventClient.send( event2Send );
```

The JSON file "myEvents.json" is as follows:

```
[
    {
        "x_object":
        {
            "x_device_id" :"connect_alpha.6hv135nw00393.81"
        },
        "x_event_type": "thermostat_temperature",
        "event_id": "11111111-2222-3333-4444-555555555555",
        "thermostat_temperature": 20,
        "cause_type": null,
        "errorcode": "",
        "varname": "temperature",
    },
    {
        "x_object":
        {
            "x_device_id" :"connect_alpha.6hv135nw00393.82"
        },
        "x_event_type": "light_dimmer.internal",
        "event_id": "11111111-2222-3333-6666-555555555555",
        "light_dimmer": 0,
        "cause_type": "internal",
        "errorcode": null,
        "varname": "level",
        "varfunction": "light-dimmer",
    },
    {
        "x_object":
        {
            "x_device_id" :"connect_alpha.6hv135nw00393.83"
        },
        "x_event_type": "mask_masked.internal",
        "x_timestamp": "2014-10-30T21:46:10+00:00",
        "event_id": "11111111-2222-1111-6666-555555555555",
        "mask_masked": "Not Masked",
        "cause_type": "internal",
        "errorcode": null,
        "varname": "mask-state",
    }
]
```

Searching
---------

The SDK support sending search query to namespace datasets:
1. Request an SearchSDK interface using the mnubo client instance.
2. Provide your MQL query as a string.

See [mnubo documentation](https://sop.mtl.mnubo.com/apps/doc/?i=t) for details on MQL format.

### Sending search query

This example describes how to send search query:
```
//Request a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//Get a Search client interface
SearchSDK mnuboSearchClient = mnuboClient.getSearchClient();

//build the query
String query = "{ \"from\": \"event\", \"select\": [ {\"value\": \"speed\"} ] }";

//Send search
ResultSet resultSet = mnuboSearchClient.search( query );
```

The `ResultSet` can be used to extract the column definitions read and the row values.
`ResultSet` implement `Iterable<Row>` interface which allow iterating on the result values (`Row`).
Column definitions are packaged in a `ColumnDefinition`.
`ColumnDefinitions` can be extracted from the `ResultSet` or from each `Row`.

```
// Get all the column with label and Type
List<ColumnDefinition> columns = resultSet.getColumnDefinitions();

// Get all the Search Rows (including column definitions)
List<SearchRow> searchRows = resultSet.getRowsData();

// Get all the column with label and Type from a Search Rows <line number>
List<ColumnDefinition> columns = searchRows.get(0).getColumnDefinitions();

// Iterator can be used to got through rs (rows)

    while (resultSet.iterator().hasNext()) {
        SearchRow searchRow = resultSet.iterator().next();

        // Get Column Definitions for the Search Row
        List<ColumnDefinition> columnDefinitions = searchRow.getColumnDefinitions();
        
        // You need to find the dataType to get the corresponding value
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String datatype = columnDefinition.getDataType();
        }
        
        // Get data of the row for a specific column :
        // The search Row method use the datatype (not the High Level Type)
        // For example if the datatype is "STRING" and we want the data of the column "column1":
        String stringData = searchRow.getString("column1");
        
        // Other Example with when datatype is "DOUBLE" and we want the data of the column "column2":
        Double doubleData = searchRow.getDouble("column2");
    }
```

Retrieving datasets
-------------------
To Get the datasets:
1. Request an SearchSDK interface using the mnubo client instance.

This example describes how to create an request to get the DataSets:
```
//Request a mnubo client using the basic method.
MnuboSDKClient mnuboClient = MnuboSDKFactory.getClient( HOST , CONSUMER_KEY , CONSUMER_SECRET );

//Get a Search client interface
SearchSDK mnuboSearchClient = mnuboClient.getSearchClient();

//Get the list of DataSet
List<DataSet> datasets = mnuboSearchClient.getDatasets( query );
```

You can use the data return by the Basic Search Query.

```
    for(DataSet dataset: datasets) {
        String datasetDescription = dataset.getDescription();
        String datasetDisplayName = dataset.getDisplayName();
        String datasetKey = dataset.getKey();
        
        for(Field field: dataset.getFields()) {
            ContainerType containerType = field.getContainerType();
            String fieldDescription = field.getDescription();
            String fieldDisplayName = field.getDisplayName();
            String fieldHighLevelType = field.getHighLevelType();
            String fieldKey = field.getKey();
        }
    }
```



References
==========

[java](http://www.oracle.com/technetwork/java/index.html)

[maven](https://maven.apache.org/)

[mnubo documentation](https://sop.mtl.mnubo.com/apps/doc/?i=t)


Configuring the example
=======================

- Go to the file 'SdkClientIntegrationTest' in '...example/src/test/java/com/mnubo/java/sdk/client/integration/' folder
- Remove or comment out the 'Ignore' annotation.
- Assign your personal consumer key to the field 'CONSUMER_KEY' as: CONSUMER_KEY = "consumerKeyProvidedByMnubo";
- Assign your personal consumer secret to the field 'CONSUMER_SECRET' as: CONSUMER_SECRET = "consumerSecretProvidedByMnubo";
- Add at least one Owner's attribute to the fields 'OWNER_ATTRIBUTES' and 'OWNER_ATTRIBUTES_2_UPDATE' this can be the sames or not.
- Add at least one Object's attribute to the fields 'OBJECT_ATTRIBUTES' and 'OBJECT_ATTRIBUTES_2_UPDATE' this can be the sames or not.
- Add at least one Timeserie to the fields 'TIMESERIES_2_POST' and 'EXTRA_TIMESERIES_2_POST' this can be the sames or not.


Running the example
-------------------

- Go to '...example/' folder
- Invoke 'mvn package'.