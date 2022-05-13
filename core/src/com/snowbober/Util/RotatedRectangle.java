package com.snowbober.Util;

import com.badlogic.gdx.math.Rectangle;

public class RotatedRectangle {
    private final Rectangle collisionRectangle;
    private final float rotation;
    private final FloatPoint origin;

    public RotatedRectangle(Rectangle rectangle, float theInitialRotation) {
        collisionRectangle = rectangle;
        rotation = theInitialRotation;

        //Calculate the Rectangles origin. We assume the center of the Rectangle will
        //be the point that we will be rotating around and we use that for the origin
        origin = new FloatPoint((int) rectangle.width / 2, (int) rectangle.height / 2);
    }

    /**
     * Used for changing the X and Y position of the RotatedRectangle
     *
     * @param theXPositionAdjustment
     * @param theYPositionAdjustment
     */
    public void changePosition(int theXPositionAdjustment, int theYPositionAdjustment) {
        collisionRectangle.x += theXPositionAdjustment;
        collisionRectangle.y += theYPositionAdjustment;
    }

    /**
     * Check to see if two Rotated Rectangls have collided.
     *
     * @param rectangle
     * @return
     */
    public boolean intersects(RotatedRectangle rectangle) {
        //Calculate the Axis we will use to determine if a collision has occurred
        //Since the objects are rectangles, we only have to generate 4 Axis (2 for
        //each rectangle) since we know the other 2 on a rectangle are parallel.
        FloatPoint[] rectangleAxis = new FloatPoint[4];
        rectangleAxis[0] = FloatPoint.sub(upperRightCorner(), upperLeftCorner());
        rectangleAxis[1] = FloatPoint.sub(upperRightCorner(), lowerRightCorner());
        rectangleAxis[2] = FloatPoint.sub(rectangle.upperLeftCorner(), rectangle.lowerLeftCorner());
        rectangleAxis[3] = FloatPoint.sub(rectangle.upperLeftCorner(), rectangle.upperRightCorner());

        //Cycle through all of the Axis we need to check. If a collision does not occur
        //on ALL of the Axis, then a collision is NOT occurring. We can then exit out 
        //immediately and notify the calling function that no collision was detected. If
        //a collision DOES occur on ALL of the Axis, then there is a collision occurring
        //between the rotated rectangles. We know this to be true by the Seperating Axis Theorem
        for (FloatPoint axis : rectangleAxis) {
            if (!isAxisCollision(rectangle, axis)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if a collision has occurred on an Axis of one of the
     * planes parallel to the Rectangle.
     *
     * @param rectangle
     * @param axis
     * @return
     */
    private boolean isAxisCollision(RotatedRectangle rectangle, FloatPoint axis) {
        //Project the corners of the Rectangle we are checking on to the Axis and
        //get a scalar value of that project we can then use for comparison
        int[] rectangleAScalars = new int[4];
        rectangleAScalars[0] = (generateScalar(rectangle.upperLeftCorner(), axis));
        rectangleAScalars[1] = (generateScalar(rectangle.upperRightCorner(), axis));
        rectangleAScalars[2] = (generateScalar(rectangle.lowerLeftCorner(), axis));
        rectangleAScalars[3] = (generateScalar(rectangle.lowerRightCorner(), axis));

        //Project the corners of the current Rectangle on to the Axis and
        //get a scalar value of that project we can then use for comparison
        int[] rectangleBScalars = new int[4];
        rectangleBScalars[0] = (generateScalar(upperLeftCorner(), axis));
        rectangleBScalars[1] = (generateScalar(upperRightCorner(), axis));
        rectangleBScalars[2] = (generateScalar(lowerLeftCorner(), axis));
        rectangleBScalars[3] = (generateScalar(lowerRightCorner(), axis));

        //Get the Maximum and Minium Scalar values for each of the Rectangles
        int rectangleAMinimum = getMin(rectangleAScalars);
        int rectangleAMaximum = getMax(rectangleAScalars);
        int rectangleBMinimum = getMin(rectangleBScalars);
        int rectangleBMaximum = getMax(rectangleBScalars);

        //If we have overlaps between the Rectangles (i.e. Min of B is less than Max of A)
        //then we are detecting a collision between the rectangles on this Axis
        return (rectangleBMinimum <= rectangleAMaximum && rectangleBMaximum >= rectangleAMaximum) ||
                (rectangleAMinimum <= rectangleBMaximum && rectangleAMaximum >= rectangleBMaximum);
    }

    private int getMin(int[] tab) {
        int min = 0;
        if (tab == null || tab.length == 0) return min;
        min = tab[0];
        for (int x : tab) {
            min = Math.min(min, x);
        }
        return min;
    }

    private int getMax(int[] tab) {
        int max = 0;
        if (tab == null || tab.length == 0) return max;
        max = tab[0];
        for (int x : tab) {
            max = Math.max(max, x);
        }
        return max;
    }

    /**
     * Generates a scalar value that can be used to compare where corners of
     * a rectangle have been projected onto a particular axis.
     *
     * @param rectangleCorner
     * @param axis
     * @return
     */
    private int generateScalar(FloatPoint rectangleCorner, FloatPoint axis) {
        //Using the formula for Vector projection. Take the corner being passed in
        //and project it onto the given Axis
        float numerator = (rectangleCorner.x * axis.x) + (rectangleCorner.y * axis.y);
        float denominator = (axis.x * axis.x) + (axis.y * axis.y);
        float divisionResult = numerator / denominator;
        FloatPoint cornerProjected = new FloatPoint(divisionResult * axis.x, divisionResult * axis.y);

        //Now that we have our projected Vector, calculate a scalar of that projection
        //that can be used to more easily do comparisons
        float scalar = (axis.x * cornerProjected.x) + (axis.y * cornerProjected.y);
        return (int) scalar;
    }

    /**
     * Rotate a point from a given location and adjust using the Origin we
     * are rotating around.
     *
     * @param point
     * @param originV
     * @param rotationVal
     * @return
     */
    private FloatPoint rotatePoint(FloatPoint point, FloatPoint originV, float rotationVal) {
        return new FloatPoint(
                (float) (originV.x + (point.x - originV.x) * Math.cos(rotationVal)
                        - (point.y - originV.y) * Math.sin(rotationVal)),
                (float) (originV.y + (point.y - originV.y) * Math.cos(rotationVal)
                        + (point.x - originV.x) * Math.sin(rotationVal))
        );
    }

    private FloatPoint upperLeftCorner() {
        FloatPoint upperLeft = new FloatPoint(collisionRectangle.x, collisionRectangle.y);
        upperLeft = rotatePoint(upperLeft, FloatPoint.add(upperLeft, origin), rotation);
        return upperLeft;
    }

    private FloatPoint upperRightCorner() {
        FloatPoint upperRight = new FloatPoint(collisionRectangle.x + collisionRectangle.width, collisionRectangle.y);
        upperRight = rotatePoint(upperRight, FloatPoint.add(upperRight, new FloatPoint(-origin.x, origin.y)), rotation);
        return upperRight;
    }

    private FloatPoint lowerLeftCorner() {
        FloatPoint lowerLeft = new FloatPoint(collisionRectangle.x, collisionRectangle.y + collisionRectangle.height);
        lowerLeft = rotatePoint(lowerLeft, FloatPoint.add(lowerLeft, new FloatPoint(origin.x, -origin.y)), rotation);
        return lowerLeft;
    }

    private FloatPoint lowerRightCorner() {
        FloatPoint lowerRight = new FloatPoint(collisionRectangle.x + collisionRectangle.width, collisionRectangle.y + collisionRectangle.height);
        lowerRight = rotatePoint(lowerRight, FloatPoint.add(lowerRight, new FloatPoint(-origin.x, -origin.y)), rotation);
        return lowerRight;
    }

    public float getX() {
        return collisionRectangle.x;
    }

    public float getY() {
        return collisionRectangle.y;
    }

    public float getWidth() {
        return collisionRectangle.width;
    }

    public float getHeight() {
        return collisionRectangle.height;
    }
}
