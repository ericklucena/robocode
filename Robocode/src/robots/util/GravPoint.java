package robots.util;

import java.awt.geom.Point2D;

public class GravPoint {
    public Point2D location;
    public double power;
    
    public GravPoint(Point2D location, double power) {
        this.location = location;
        this.power = power;
    }
}