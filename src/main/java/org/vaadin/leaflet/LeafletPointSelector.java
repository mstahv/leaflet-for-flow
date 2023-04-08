package org.vaadin.leaflet;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableConsumer;
import java.util.UUID;
import org.github.legioth.field.Field;
import org.github.legioth.field.ValueMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Tag("div")
@JavaScript("//unpkg.com/leaflet@1.3.4/dist/leaflet.js")
@StyleSheet("//unpkg.com/leaflet@1.3.4/dist/leaflet.css")
@JavaScript("./leafletConnector.js")
//@StyleSheet("./leafletCssHacks.css")
public class LeafletPointSelector extends Component implements HasSize, Field<LeafletPointSelector, Point> {

    private final String id = UUID.randomUUID().toString();

    private static final GeometryFactory gf = new GeometryFactory();
    private final ValueMapper<Point> valueMapper;

    public LeafletPointSelector() {
        setId(id);
        valueMapper = Field.init(this, null, p -> {
            sendPointToClient(p);
        });
    }

    protected void sendPointToClient(Point p) {
        runBeforeClientResponse(ui -> getElement().callJsFunction(
                "$connector.setPoint", p.getCoordinate().y, p.getCoordinate().x)
        );
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initConnector();
        if(getValue() != null) {
            sendPointToClient(getValue());
        }
        injectZIndexFix();
    }

    private void initConnector() {
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.leafletConnector.initLazy($0)",
                getElement()));
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    @ClientCallable
    private void updatePosition(double lat, double lon) {
        Point point = gf.createPoint(new Coordinate(lon, lat));
        valueMapper.setModelValue(point, true);
    }

    protected void injectZIndexFix() {
        // Without this a map in overlay gets under map in actual Vaadin content
        // No idea why z index is used in leaflet
        Element style = new Element("style");
        style.setText(".leaflet-pane {z-index: 0 !important;} ");
        UI.getCurrent().getElement().appendChild(style);
    }

}
