package sun.security.ec.point;

/**
 * An interface for mutable points on an elliptic curve over a finite field.
 */
public interface MutablePoint extends Point {

    MutablePoint setValue(AffinePoint p);
    MutablePoint setValue(Point p);
    MutablePoint conditionalSet(Point p, int set);

}
