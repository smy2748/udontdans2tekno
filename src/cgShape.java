/**
 * cgShape.java
 *
 * Class that includes routines for tessellating a number of basic shapes
 *
 * Students are to supply their implementations for the
 * functions in this file using the function "addTriangle()" to do the 
 * tessellation.
 *
 * Implemented By Stephen Yingling
 */

public class cgShape extends simpleShape
{
    /**
     * constructor
     */
    public cgShape()
    {
    }
    
     
    /**
     * makeDefaultShape - creates a block at arbitrary coordinates
     * 
     *
     */
    public void makeDefaultShape ()
    {
        makeCube(-.25f,-.5f,-.5f,.25f);
    }

    /**
     * Make a cube
     *
     * Implemented by: Stephen Yingling
     * @param lowerFrontX the lower front left x value
     * @param lowerFrontY The lower front left y value
     * @param lowerFrontZ The lower front left z value
     * @param length The length of each side
     */
    public void makeCube (float lowerFrontX, float lowerFrontY, float lowerFrontZ, float length)
    {
        int subdivisions = 10;

        //Make all the points at the corners of the cube
        MyPoint frontLowerLeft = new MyPoint(lowerFrontX,lowerFrontY,lowerFrontZ),
                frontLowerRight = new MyPoint(lowerFrontX+length,lowerFrontY,lowerFrontZ),
                frontUpperRight = new MyPoint(lowerFrontX+length,lowerFrontY+length,lowerFrontZ),
                frontUpperLeft = new MyPoint(lowerFrontX,lowerFrontY+length,lowerFrontZ),
                backLowerLeft = new MyPoint(lowerFrontX,lowerFrontY,lowerFrontZ+length),
                backUpperLeft = new MyPoint(lowerFrontX,lowerFrontY+length,lowerFrontZ+length),
                backLowerRight = new MyPoint(lowerFrontX+length,lowerFrontY,lowerFrontZ+length),
                backUpperRight = new MyPoint(lowerFrontX+length,lowerFrontY+length,lowerFrontZ+length)
                        ;

        //Draw each face

        //Front
        makeQuad(frontLowerLeft,frontLowerRight,frontUpperRight,
                frontUpperLeft, subdivisions,.25f,.5f,false,length);

        //left
        makeQuad(backLowerLeft,frontLowerLeft,frontUpperLeft,
                backUpperLeft, subdivisions,0,.5f,false,length);

        //back
        makeQuad(backLowerRight, backLowerLeft, backUpperLeft,
                backUpperRight, subdivisions,.75f,.5f,true,length);

        //top
        makeQuad(frontUpperLeft, frontUpperRight, backUpperRight,
                backUpperLeft, subdivisions,.25f,.75f,false,length);

        //bottom
        makeQuad(backLowerLeft, backLowerRight, frontLowerRight,
                frontLowerLeft, subdivisions,.25f,.25f,true,length);

        //right
        makeQuad(frontLowerRight, backLowerRight, backUpperRight,
                frontUpperRight, subdivisions,.5f,.5f,true,length);

    }

    /**
     * Makes a quad from the given points with the given subdivisions
     * @param ll - The lower left corner of the quad
     * @param lr - The lower right corner of the quad
     * @param ur - The upper right corner of the quad
     * @param ul - The upper left corner of the quad
     * @param subs - The number of subdivisions for the quad
     * @param textureLLx - The lower left corner of the texture x val
     * @param textureLLy - The lower left corner of the texture y val
     * @param flip - Whether or not to flip the texture
     * @param sideLen - the length of the side (for scaling porpoises)
     * Implemented by: Stephen Yingling
     */
    public void makeQuad(MyPoint ll, MyPoint lr, MyPoint ur, MyPoint ul, int subs,float textureLLx, float textureLLy, boolean flip,float sideLen ){
        float subsize = 1.0f/subs;

        //Subdivide the quad into columns and draw the columns
        for(int i=0; i<subs; i++){
            float curPos = subsize * i;
            MyPoint q = ul.mult(1f-curPos).add(ur.mult(curPos));
            MyPoint r = ll.mult(1f-curPos).add(lr.mult(curPos));

            float iprime = curPos + subsize;
            MyPoint qPrime = ul.mult(1f-iprime).add(ur.mult(iprime));
            MyPoint rprime = ll.mult(1f-iprime).add(lr.mult(iprime));

            makeQuadCol(q,r,qPrime,rprime,subs,textureLLx,textureLLy,ll.getX(),ll.getY(),ll.getZ(), flip, sideLen);
        }
    }

    /**
     * Draw a column of quads
     * * Implemented by Stephen Yingling
     * @param q - The left upper point of the column
     * @param r - The left lower point of the column
     * @param qp - The right upper point of the column
     * @param rp - The right lower point of the column
     * @param numSubs - The number of rows for this column
     * @param txllx - The bootom left corner of the texture x val
     * @param txlly - The bottom left corner of the texture y val
     * @param cx - The lower left corener x val
     * @param cy - The lower left corener y val
     * @param cz - The lower left corener z val
     * @param flip - Whether or not to flip the image
     * @param sideLen - The length of each side
     */
    public void makeQuadCol(MyPoint q, MyPoint r, MyPoint qp, MyPoint rp, int numSubs,
                            float txllx, float txlly,
                            float cx, float cy, float cz,
                            boolean flip, float sideLen){
        float sublength = 1f/numSubs;

        //Calculate and draw two triangles for each row
        for(int i=0; i < numSubs; i++){
            float f = i * sublength;
            MyPoint p1 = q.mult(1-f).add(r.mult(f)),
                    p2= qp.mult(1-f).add(rp.mult(f)),
                    p3 = q.mult(1-(f+sublength)).add(r.mult(f+sublength)),
                    p4 = qp.mult(1-(f+sublength)).add(rp.mult(f+sublength));

            addTriangle(p3,p4,p2,txllx,txlly,cx,cy,cz,flip, sideLen);
            addTriangle(p3, p2, p1, txllx, txlly, cx, cy, cz, flip, sideLen);
        }
    }

    /**
     * Normalizes the value
     *
     * Implemented by Stephen Yingling
     * @param val The value to normalize
     * @param max The max possible values
     * @param min The min possible value
     * @return The normalized value
     */
    public float normalize(float val, float max, float min){
        return (val-min)/(max-min);
    }

    /**
     * A callthrough to the other addTriangle function
     * @param p1 - The first point of the triangle
     * @param p2 - The second point of the triangle
     * @param p3 - The third point of the triangle
     * Implemented by: Stephen Yingling
     */
    public void addTriangle(MyPoint p1, MyPoint p2, MyPoint p3,
                            float txllx, float txlly,
                            float cx, float cy, float cz,
                            boolean flip, float sideLen){

        if(p1.getZ() == p2.getZ() && p2.getZ() == p3.getZ()){
            float xmin = cx, xmax = xmin+sideLen,
                  ymin = cy, ymax = ymin+sideLen;
            if(!flip){
                addTriangle(p1.getX(), p1.getY(), p1.getZ(),.25f*normalize(p1.getX(),xmax,xmin)+txllx,.25f*normalize(p1.getY(), ymax, ymin)+txlly,
                    p2.getX(), p2.getY(), p2.getZ(), .25f*normalize(p2.getX(), xmax, xmin)+txllx,.25f*normalize(p2.getY(), ymax, ymin)+txlly,
                    p3.getX(), p3.getY(), p3.getZ(), .25f*normalize(p3.getX(), xmax, xmin)+txllx, .25f*normalize(p3.getY(), ymax, ymin)+txlly);
            }
            else{
                addTriangle(p1.getX(), p1.getY(), p1.getZ(),-.25f*normalize(p1.getX(), xmax, xmin)+txllx,.25f*normalize(p1.getY(), ymax, ymin)+txlly,
                        p2.getX(), p2.getY(), p2.getZ(), -.25f*normalize(p2.getX(), xmax, xmin)+txllx,.25f*normalize(p2.getY(), ymax, ymin)+txlly,
                        p3.getX(), p3.getY(), p3.getZ(), -.25f*normalize(p3.getX(), xmax, xmin)+txllx, .25f*normalize(p3.getY(), ymax, ymin)+txlly);
            }
        }

        if(p1.getX() == p2.getX() && p2.getX() == p3.getX()){

            float zmin = cz, zmax = cz+sideLen,
                  ymin = cy, ymax = cy+sideLen;

            if(!flip){
                addTriangle(p1.getX(), p1.getY(), p1.getZ(),-.25f*normalize(p1.getZ(), zmax, zmin)+(txllx),.25f*normalize(p1.getY(),ymax,ymin)+txlly,
                        p2.getX(), p2.getY(), p2.getZ(), -.25f*normalize(p2.getZ(),zmax,zmin)+(txllx),.25f*normalize(p2.getY(),ymax,ymin)+txlly,
                        p3.getX(), p3.getY(), p3.getZ(), -.25f*normalize(p3.getZ(),zmax,zmin)+(txllx), .25f*normalize(p3.getY(),ymax,ymin)+txlly);
            }
            else{
                addTriangle(p1.getX(), p1.getY(), p1.getZ(),.25f*normalize(p1.getZ(),zmax,zmin)+(txllx),.25f*normalize(p1.getY(), ymax, ymin)+txlly,
                        p2.getX(), p2.getY(), p2.getZ(), .25f*normalize(p2.getZ(),zmax,zmin)+(txllx),.25f*normalize(p2.getY(),ymax,ymin)+txlly,
                        p3.getX(), p3.getY(), p3.getZ(), .25f*normalize(p3.getZ(),zmax,zmin)+(txllx), .25f*normalize(p3.getY(),ymax,ymin)+txlly);
            }
        }

        if(p1.getY() == p2.getY() && p1.getY() == p3.getY()) {
            float xmin = cx, xmax=cx+sideLen,
                  zmin = cz, zmax=cz+sideLen;
            if(!flip){
                addTriangle(p1.getX(), p1.getY(), p1.getZ(),.25f*normalize(p1.getX(), xmax, xmin)+txllx,.25f*normalize(p1.getZ(), zmax, zmin)+txlly,
                        p2.getX(), p2.getY(), p2.getZ(), .25f*normalize(p2.getX(),xmax,xmin)+txllx,.25f*normalize(p2.getZ(),zmax,zmin)+txlly,
                        p3.getX(), p3.getY(), p3.getZ(), .25f*normalize(p3.getX(),xmax,xmin)+txllx, .25f*normalize(p3.getZ(),zmax,zmin)+txlly);
            }
            else{
                addTriangle(p1.getX(), p1.getY(), p1.getZ(),.25f*normalize(p1.getX(),xmax,xmin)+txllx,-.25f*normalize(p1.getZ(),zmax,zmin)+txlly,
                        p2.getX(), p2.getY(), p2.getZ(), .25f*normalize(p2.getX(),xmax,xmin)+txllx,-.25f*normalize(p2.getZ(),zmax,zmin)+txlly,
                        p3.getX(), p3.getY(), p3.getZ(), .25f*normalize(p3.getX(),xmax,xmin)+txllx, -.25f*normalize(p3.getZ(),zmax,zmin)+txlly);
            }
        }
    }

    /**
     * A class to represent a point in 3D space
     * Created by: Stephen Yingling
     */
    public class MyPoint{
        protected float x;
        protected float y;
        protected float z;

        /**
         * Make a point
         * @param x - The x value of the point
         * @param y - The y value of the point
         * @param z - The z value of the point
         * Implemented by: Stephen Yingling
         */
        public MyPoint(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Adds the value of one point to this point and returns a new point
         * containg the result
         * @param other - The point to add to this one
         * @return A point containing the point addition of this point and the other one
         * Implemented by: Stephen Yingling
         */
        public MyPoint add(MyPoint other){

            return new MyPoint(this.x + other.getX(), this.y + other.getY(), this.z + other.getZ());
        }

        /**
         * Multiply this point by a scalar value
         * @param f - The number to multiply the point by
         * @return A new point with the new values
         *
         * Implemented by: Stephen Yingling
         */
        public MyPoint mult(float  f){
            return new MyPoint(x * f, y*f, z*f);
        }

        /**
         * Determine the midpoint between this point and another
         * @param other The other point
         * @return A new point representing the midpoint
         * Implemented by: Stephen Yingling
         */
        public MyPoint midPoint(MyPoint other){
            float nx = (other.getX()+x)/2f,
                    ny = (other.getY()+y)/2f,
                    nz = (other.getZ()+z)/2f;

            return new MyPoint(nx,ny,nz);
        }

        /**
         * Gets the magnitude of this point
         * @return The magnitude of this point
         * Implemented by: Stephen Yingling
         */
        public float getMagnitude(){
            return (float)Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
        }

        /**
         *
         * @return The x value
         * Implemented by: Stephen Yingling
         */
        public float getX() {
            return x;
        }

        /**
         *
         * @return The y value
         * Implemented by: Stephen Yingling
         */
        public float getY() {
            return y;
        }


        /**
         *
         * @return The z value
         * Implemented by: Stephen Yingling
         */
        public float getZ() {
            return z;
        }

        /**
         * Sets the z value to the specified value
         * @param z - The new z value
         * Implemented by: Stephen Yingling
         */
        public void setZ(float z) {
            this.z = z;
        }
    }
}
