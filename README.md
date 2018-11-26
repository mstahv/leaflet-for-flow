# LeafletJS for Flow

Vaadin 10+ Java integration for [LeafletJS](https://leafletjs.com). 

Builds will be available from https://vaadin.com/directory 

## Limitations

The current version is still experimental and lacks most features of the v-leaflet add-on for older Vaadin versions. The current version is more of a proof of concept and only contains one component to pick a JTS Point from a map with hard coded OpenStreetMap layer. The LeafletPointSelector implements HasValue<Point> so it can be bound to JTS Point property with Binder.

With this implementation strategy the component will never work with the latest Vaadin Designer nor with html templates, but it seems like a rather simple approach to get something functional done for plain Java developers.

## Development instructions

Starting the test/demo server:
```
mvn jetty:run
```

This deploys demo at http://localhost:8080

