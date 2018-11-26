package org.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import org.vaadin.leaflet.LeafletPointSelector;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Route
public class DemoView extends Div {

    protected LeafletPointSelector map;

    public DemoView() {
        map = new LeafletPointSelector();
        map.setHeight("400px");
        add(map);

        Button b2 = new Button("Show content", e -> {
            Notification.show(map.getValue().toString());
        });
        add(b2);
        Button b4 = new Button("Set value Point(50, 10)", e -> {
            Point createPoint = new GeometryFactory().createPoint(new Coordinate(10, 50));
            map.setValue(createPoint);
        });
        add(b4);
        
        
        Button b3 = new Button("Show in Dialog", e -> {
            Dialog dialog = new Dialog();
            final LeafletPointSelector leafletMap = new LeafletPointSelector();
            leafletMap.setHeight("300px");
            leafletMap.setWidth("500px");
            dialog.add(leafletMap);
            dialog.open();
        });
        add(b3);

        
    }
}
