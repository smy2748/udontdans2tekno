/**
 *  finalMain.java
 *
 *  Main class for Final
 *
 *  Implemented by Stephen Yingling
 */

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.Buffer;

public class finalMain implements GLEventListener, KeyListener
{

    /**
     * buffer info 
     */
    private int vbuffer[];
    private int ebuffer[];
    private int numVerts[];


    //Angles and Lighting
    public float angles[];
    public float lightPos[];
    public int specEx;

    public int specInc=1;
    private float lightInc = .1f;
    private float angleInc = 5.0f;
    public int theta;


    /**
     * program and variable IDs
     */
    public int shaderProgID;

    // texture values
    public textureParams tex;

    /**
     * shape info
     */
    cgShape myShape;

    //Lighting Parameters
    lightingParams myPhong;

    /**
     * my canvas
     */
    GLCanvas myCanvas;

    /**
     * constructor
     *
     * Rotation and light position added by: Stephen Yingling
     */
    public finalMain(GLCanvas G)
    {
        vbuffer = new int [1];
        ebuffer = new int[1];
        numVerts = new int [1];

        //Initialize a rotation
        angles = new float[3];
        angles[0] = -5.0f;
        angles[1] = -5.0f;
        angles[2] = 0.0f;

        //Initialize light position
        lightPos = new float[4];
        lightPos[0] = -1.5f;
        lightPos[1] = -1f;
        lightPos[2] = 1f;
        lightPos[3] = 1;

        specEx = 8;



        myCanvas = G;
        tex = new textureParams();
        myPhong = new lightingParams();

        G.addGLEventListener (this);
        G.addKeyListener (this);
    }

    private void errorCheck (GL2 gl2)
    {
        int code = gl2.glGetError();
        if (code == GL.GL_NO_ERROR) 
            System.err.println ("All is well");
        else
            System.err.println ("Problem - error code : " + code);

    }


    /**
     * Displays a textured shape using the passed in GL2 instance
     * @param gl2 The GL2 instance to use
     *
     * Adapted by: Stephen Yingling
     */
    public void displayTex(GL2 gl2){

        // bind your vertex buffer
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);

        // bind your element array buffer
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);

        // set up your attribute variables
        gl2.glUseProgram (shaderProgID);
        long dataSize = myShape.getNVerts() * 4l * 4l;
        int  vPosition = gl2.glGetAttribLocation (shaderProgID, "vPosition");
        gl2.glEnableVertexAttribArray ( vPosition );
        gl2.glVertexAttribPointer (vPosition, 4, GL.GL_FLOAT, false,
                0, 0l);
        int  vTex = gl2.glGetAttribLocation (shaderProgID, "vTexCoord");
        gl2.glEnableVertexAttribArray ( vTex );
        gl2.glVertexAttribPointer (vTex, 2, GL.GL_FLOAT, false,
                0, dataSize);

        // setup uniform variables for texture
        tex.setUpTextures (shaderProgID, gl2);

        theta = gl2.glGetUniformLocation (shaderProgID, "theta");
        gl2.glUniform3fv (theta, 1, angles, 0);

        // draw your shapes
        gl2.glDrawElements ( GL.GL_TRIANGLES, numVerts[0],
                GL.GL_UNSIGNED_SHORT, 0l);
    }

    /**
     * Displays a shaded shape using the passed in GL2 instance
     * @param gl2 The GL2 instance to use
     *
     * Adapted by: Stephen Yingling
     */
    public void displayShaded(GL2 gl2){
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);

        // bind your element array buffer
        gl2.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);

        // set up your attribute variables
        gl2.glUseProgram(shaderProgID);
        long dataSize = myShape.getNVerts() * 4l * 4l;
        int  vPosition = gl2.glGetAttribLocation (shaderProgID, "vPosition");
        gl2.glEnableVertexAttribArray ( vPosition );
        gl2.glVertexAttribPointer (vPosition, 4, GL.GL_FLOAT, false,
                0, 0l);
        int  vNormal = gl2.glGetAttribLocation (shaderProgID, "vNormal");
        gl2.glEnableVertexAttribArray ( vNormal );
        gl2.glVertexAttribPointer(vNormal, 3, GL.GL_FLOAT, false,
                0, dataSize);


        myPhong.setUpPhong (shaderProgID, gl2);

        // draw your shapes
        gl2.glDrawElements ( GL.GL_TRIANGLES, numVerts[0],
                GL.GL_UNSIGNED_SHORT, 0l);
    }
    /**
     * Called by the drawable to initiate OpenGL rendering by the client.
     * Creates the wooden blocks, then the colored (shaded) ones.
     *
     * Implemented by Stephen Yingling
     */
    public void display(GLAutoDrawable drawable)
    {

        // get GL
        GL2 gl2 = (drawable.getGL()).getGL2();
        // clear your frame buffers
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

        //Make wooen blocks
        createWoodBlocks(gl2);
        displayTex(gl2);


        //Prepare for Shading
        myPhong.setLightpos(lightPos);
        myPhong.setSpecularExponent(specEx);

        //Make red block
        float diffuse[] = {1f,0f,0f,1f};
        myPhong.setDiffuse(diffuse);
        createRedBlock(gl2);
        displayShaded(gl2);



        //blue Block
        float[] diffuseBlue = {0f,0f,1f,0f};
        myPhong.setDiffuse(diffuseBlue);
        createBlueBlock(gl2);
        displayShaded(gl2);

        //Green Block
        float[] diffuseGreen = {0f,1f,0f,0f};
        myPhong.setDiffuse(diffuseGreen);
        createGreenBlock(gl2);
        displayShaded(gl2);

        //Yellow Block
        float[] diffuseYellow = {1f,1f,0f,0f};
        myPhong.setDiffuse(diffuseYellow);
        createYellowBlock(gl2);
        displayShaded(gl2);

    }


    /**
     * Notifies the listener to perform the release of all OpenGL 
     * resources per GLContext, such as memory buffers and GLSL 
     * programs.
     */
    public void dispose(GLAutoDrawable drawable)
    {

    }

    /**
     * Called by the drawable immediately after the OpenGL context is
     * initialized. 
     */
    public void init(GLAutoDrawable drawable)
    {
        // get the gl object
        GL2 gl2 = drawable.getGL().getGL2();

        // Load shaders
        shaderSetup myShaders = new shaderSetup();
        shaderProgID = myShaders.readAndCompile (gl2, "vshader.glsl",
                "fshader.glsl");
        if (shaderProgID == 0) {
            System.err.println (
	      myShaders.errorString(myShaders.shaderErrorCode)
	    );
            System.exit (1);
        }

        // Other GL initialization
        gl2.glEnable (GL.GL_DEPTH_TEST);
        gl2.glEnable (GL.GL_CULL_FACE);
        gl2.glCullFace ( GL.GL_BACK );
        gl2.glFrontFace(GL.GL_CCW);
        gl2.glClearColor (1.0f, 1.0f, 1.0f, 1.0f);
        gl2.glDepthFunc (GL.GL_LEQUAL);
        gl2.glClearDepth (1.0f);

        // initially create a new Shape


        // Load textures
        tex.loadTexture ("woodtex.png");
    }


    /**
     * Called by the drawable during the first repaint after the component
     * has been resized. 
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                     int height)
    {

    }

    /**
     * Creates the red block in the bottom left of the image
     * @param gl2 The GL2 instance to use
     *
     *Implemented by: Stephen Yingling
     */
    public void createRedBlock(GL2 gl2){
        myShape = new cgShape();

        myShape.makeCube(-.5f,-.5f,-.5f,.25f);

        bpcShade(gl2);
    }

    /**
     * Creates the blue block
     * @param gl2 The GL2 instance to use
     *
     *Implemented by: Stephen Yingling
     */
    public void createBlueBlock(GL2 gl2){
        myShape = new cgShape();

        myShape.makeCube(0f,0f,-.5f,.25f);

        bpcShade(gl2);
    }

    /**
     * Creates the yellow block
     * @param gl2 The GL2 instance to use
     *
     *Implemented by: Stephen Yingling
     */
    public void createYellowBlock(GL2 gl2){
        myShape = new cgShape();

        myShape.makeCube(-.25f,-.25f,-.5f,.25f);

        bpcShade(gl2);
    }

    /**
     * Creates the green block
     * @param gl2 The GL2 instance to use
     *
     *Implemented by: Stephen Yingling
     */
    public void createGreenBlock(GL2 gl2){
        myShape = new cgShape();

        myShape.makeCube(.25f,.25f,-.5f,.25f);

        bpcShade(gl2);
    }

    /**
     * Creates the wooden blocs
     *
     * Implemented by: Stephen Yingling
     */
    public void createWoodBlocks(GL2 gl2)
    {
        float blockGap = .005f;
        // clear the old shape
        myShape = new cgShape();

        // clear the old shape
        myShape.clear();

        // Bottom Row

        myShape.makeDefaultShape();
        myShape.makeCube(0,-.5f,-.5f,.25f);
        myShape.makeCube(.25f,-.5f,-.5f,.25f);

        //Next row
        myShape.makeCube(0,-.25f,-.5f,.25f);
        myShape.makeCube(.25f,-.25f,-.5f,.25f);


        //Next-to-top row
        myShape.makeCube(.25f,0f,-.5f,.25f);

        boilerplateCreationCode(gl2);
    }

    /**
     * Boilerplate code for creating shaded shapes
     * @param gl2
     *
     * Adapted by: Stephen Yingling
     */
     public void bpcShade(GL2 gl2){

        // get your vertices and elements
        Buffer points = myShape.getVertices();
        Buffer elements = myShape.getElements();
        Buffer normals = myShape.getNormals();

        // set up the vertex buffer
        int bf[] = new int[1];
        gl2.glGenBuffers(1, bf, 0);
        vbuffer[0] = bf[0];
        long vertBsize = myShape.getNVerts() * 4l * 4l;
        long ndataSize = myShape.getNVerts() * 3l * 4l;
        gl2.glBindBuffer(GL.GL_ARRAY_BUFFER, vbuffer[0]);
        gl2.glBufferData(GL.GL_ARRAY_BUFFER, vertBsize + ndataSize,
                null, GL.GL_STATIC_DRAW);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, 0, vertBsize, points);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, vertBsize, ndataSize,
                normals);

        gl2.glGenBuffers (1, bf, 0);
        ebuffer[0] = bf[0];
        long eBuffSize = myShape.getNVerts() * 2l;
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);
        gl2.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, eBuffSize, elements,
                GL.GL_STATIC_DRAW);

        numVerts[0] = myShape.getNVerts();
    }

    /**
     * Boilerplate code to create textured objects
     * @param gl2 The GL2 instance to use
     *
     *Adapted by: Stephen Yingling
     */
    public void boilerplateCreationCode(GL2 gl2){
        // get your vertices and elements
        Buffer points = myShape.getVertices();
        Buffer elements = myShape.getElements();
        Buffer texCoords = myShape.getUV();

        // set up the vertex buffer
        int bf[] = new int[1];
        gl2.glGenBuffers (1, bf, 0);
        vbuffer[0] = bf[0];
        long vertBsize = myShape.getNVerts() * 4l * 4l;
        long tdataSize = myShape.getNVerts() * 2l * 4l;
        gl2.glBindBuffer ( GL.GL_ARRAY_BUFFER, vbuffer[0]);
        gl2.glBufferData ( GL.GL_ARRAY_BUFFER, vertBsize + tdataSize,
                null, GL.GL_STATIC_DRAW);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, 0, vertBsize, points);
        gl2.glBufferSubData ( GL.GL_ARRAY_BUFFER, vertBsize, tdataSize,
                texCoords);

        gl2.glGenBuffers (1, bf, 0);
        ebuffer[0] = bf[0];
        long eBuffSize = myShape.getNVerts() * 2l;
        gl2.glBindBuffer ( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[0]);
        gl2.glBufferData ( GL.GL_ELEMENT_ARRAY_BUFFER, eBuffSize,elements,
                GL.GL_STATIC_DRAW);

        numVerts[0] = myShape.getNVerts();
    }

    /**
     * Because I am a Key Listener...we'll only respond to key presses
     */
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

    /** 
     * Invoked when a key has been pressed.
     */
    public void keyPressed(KeyEvent e)
    {
        // Get the key that was pressed
        char key = e.getKeyChar();

        // Respond appropriately
        switch( key ) {
            case 'x': lightPos[0] -= lightInc; break;
            case 'y': lightPos[1] -= lightInc; break;
            case 'z': lightPos[2] -= lightInc; break;
            case 'X': lightPos[0] += lightInc; break;
            case 'Y': lightPos[1] += lightInc; break;
            case 'Z': lightPos[2] += lightInc; break;
            case 's': specEx -= specInc; break;
            case 'S': specEx += specInc; break;
            case 'q': case 'Q':
            System.exit( 0 );
            break;
        }

        // do a redraw
        myCanvas.display();
    }


    /**
     * main program
     */
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        // create your tessMain
        finalMain myMain = new finalMain(canvas);



        Frame frame = new Frame("CG - Texture Assignment");
        frame.setSize(512, 512);
        frame.add(canvas);
        frame.setVisible(true);



        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
