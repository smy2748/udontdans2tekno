/**
 * simpleShape.java
 *
 * Class that represents a shape to be tessellated; cgShape, which includes
 * all student code, is derived from this class.
 *
 * Of note is the protected method addTriangles() which is what students
 * should use to define their tessellations.
 *
 */

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

public class simpleShape
{
    /**
     * our vertex points
     */
    private Vector<Float> points;
    
    /**
     * our array elements
     */
    private Vector<Short> elements;
    private short nVerts;
    
    /**
     * our normals
     */
    private Vector<Float> normals;
    
    /**
     * our texture coords 
     */
    private Vector<Float> uv;
    
	/**
	 * constructor
	 */
	public simpleShape()
	{
        points = new Vector<Float>();
        elements = new Vector<Short>();
        normals = new Vector<Float>();
        uv = new Vector<Float>();
        nVerts = 0;
	}
    
    /**
     * add a triangle to the shape
     */
    protected void addTriangle (float x0, float y0, float z0, float u0, float v0,
                                float x1, float y1, float z1, float u1, float v1,
                                float x2, float y2, float z2, float u2, float v2)
    {
        points.add (new Float(x0));
        points.add (new Float(y0));
        points.add (new Float(z0));
        points.add (new Float(1.0f));
        elements.add (new Short(nVerts));
        nVerts++;
        
        points.add (new Float(x1));
        points.add (new Float(y1));
        points.add (new Float(z1));
        points.add (new Float(1.0f));
        elements.add (new Short(nVerts));
        nVerts++;
        
        points.add (new Float(x2));
        points.add (new Float(y2));
        points.add (new Float(z2));
        points.add (new Float(1.0f));
        elements.add (new Short(nVerts));
        nVerts++;
        
        // calculate the normal
        float ux = x1 - x0;
        float uy = y1 - y0;
        float uz = z1 - z0;
        
        float vx = x2 - x0;
        float vy = y2 - y0;
        float vz = z2 - z0;
        
        float nx = (uy * vz) - (uz * vy);
        float ny = (uz * vx) - (ux * vz);
        float nz = (ux * vy) - (uy * vx);
        
        // Attach the normal to all 3 vertices
        for (int i=0; i < 3; i++) {
            normals.add (new Float (nx));
            normals.add (new Float (ny));
            normals.add (new Float (nz));
        }
        
        // Attach the texture coords
        uv.add (new Float (u0));
        uv.add (new Float (v0));
        uv.add (new Float (u1));
        uv.add (new Float (v1));
        uv.add (new Float (u2));
        uv.add (new Float (v2));
    }
    
    /**
     * clear the shape
     */
    public void clear()
    {
        points= new Vector<Float>(); 
        elements = new Vector<Short>();
        normals = new Vector<Float>();
        normals = new Vector<Float>();
        nVerts = 0;
    }
    
    public Buffer getNormals ()
    {
        float v[] = new float[normals.size()];
        for (int i=0; i < normals.size(); i++) {
            v[i] = (normals.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap (v);
    }
    
    public Buffer getVertices ()
    {
        float v[] = new float[points.size()];
        for (int i=0; i < points.size(); i++) {
            v[i] = (points.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap (v);
    }
    
    public Buffer getUV ()
    {
        float v[] = new float[uv.size()];
        for (int i=0; i < uv.size(); i++) {
            v[i] = (uv.elementAt(i)).floatValue();
        }
        return FloatBuffer.wrap (v);
    }
    
    public Buffer getElements ()
    {
        short e[] = new short[elements.size()];
        for (int i=0; i < elements.size(); i++) {
            e[i] = (elements.elementAt(i)).shortValue();
        }

        return ShortBuffer.wrap (e);
    }
    
    public short getNVerts()
    {
        return nVerts;
    }
	
	
}
