# AutoRemote Persistence

This service allows you to feed item data to [AutoRemote web site](http://autoremote).

This persistence service supports only writing information, and so features such as `restoreOnStartup` and sitemap Chart widgets cannot be used with this service.

## Prerequisites

You need a AutoRemote API key and data feed to put data to. Each item being persisted represents a separate feed.

To connect openHAB to autoremote, you have to [create a _custom device_](http://autoremote/devices/add/custom/#navbar) in autoremote. Follow the wizard there ("Get started"). Give your device a name that makes sense to you, and where the form says "Device is" choose "Other". Leave "Sending data" as "HTTP Posting" and "Receiving Data" as "HTTP Polling". Add as many feeds as you have items to display, choosing the option "Input: data flows from device to Sen.se" for each feed.

Under [Devices](http://autoremote/devices/), you should see the device that you have just created. If you click on "Profile & Settings" you will see, amongst other things, a list of the input feeds that you created. If you click on an input feed you will see all its data, including a _feed ID_ (currently a 5-digit integer).

Note the feed ID of each input feed that you want to connect to an item in openHAB.  You will need this information to configure the file `persistence/sense.persist`.

## Configuration

This service can be configured in the file `services/autoremote.cfg`.

| Property | Default | Required | Description |
|----------|---------|:--------:|-------------|
| apikey   |         |   Yes    | your AutoRemote API key |

All item- and event-related configuration is done in the file `persistence/autoremote.persist`.  Choose whatever strategies make sense in your application. This is totally independent of autoremote.

In the `Items` section, you will create the connection between openHAB and autoremote. The syntax of the items section is as follows:

persistence/sense.persist

```
Items {
    <itemlist1> [-> "<alias1>"] : [strategy = <strategy1>, <strategy2>, ...]
    <itemlist2> [-> "<alias2>"] : [strategy = <strategyX>, <strategyY>, ...]
    ...
}
```

The important values for autoremote are the `alias`es. Substitute `<alias1>`, `<alias2>`, etc. with a feed ID from autoremote and the corresponding items will be sent to that feed.

The following example shows a very simple `autoremote.persist` which illustrates this:

```
Strategies {
	everyDay	: "0 0 0 * * ?"
	default		= everyChange, everyDay, restoreOnStartup
}

Items {
	Temperature_Item	-> "xxxxx"	:
	// where "Temperature_Item" is an OpenHAB item
	// and "xxxxx" is an autoremote feed ID enclosed in double quotes
	// Values from Temperature_Item are sent to autoremote feed "xxxxx"
	Humidity_Item		-> "yyyyy"	:
	// where "Humidity_Item" is an OpenHAB item
	// and "yyyyy" is an autoremote feed ID enclosed in double quotes
	// Values from Humidity_Item are sent to autoremote feed "yyyyy"
}
```

