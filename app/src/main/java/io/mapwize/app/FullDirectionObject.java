package io.mapwize.app;

import io.mapwize.mapwizeformapbox.model.Direction;
import io.mapwize.mapwizeformapbox.model.DirectionPoint;

class FullDirectionObject {

    Direction direction;
    DirectionPoint from;
    DirectionPoint to;

    FullDirectionObject(Direction direction, DirectionPoint from, DirectionPoint to) {
        this.direction = direction;
        this.from = from;
        this.to = to;
    }
}
