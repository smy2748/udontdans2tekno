/**
 *
 * textureParams.java
 *
 * Simple class for setting up the textures for the textures
 * Assignment.
 *
 * Students are to complete this class.
 *
 * Implemented by: Stephen Yingling
 */

import com.jogamp.opengl.util.texture.*;

import javax.media.opengl.*;
import javax.media.opengl.fixedfunc.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class textureParams
{
    
	/**
	 * constructor
	 */
	public textureParams()
	{
        
	}
    
    /**
     * This functions loads texture data to the GPU.
     *
     * You will need to write this function, and maintain all of the values needed
     * to be sent to the various shaders.
     *
     * @param filename - The name of the texture file.
     *
     */
    public void loadTexture (String filename)
    {
        Texture tex_id;

        try{
            InputStream stream = new FileInputStream(filename);
            tex_id = TextureIO.newTexture(stream,false,"png");
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    
    /**
     * This functions sets up the parameters
     * for texture use.
     *
     * You will need to write this function, and maintain all of the values needed
     * to be sent to the various shaders.
     *
     * @param program - The ID of an OpenGL (GLSL) program to which parameter values
     *    are to be sent
     *
     * @param gl2 - GL2 object on which all OpenGL calls are to be made
     *
     */
    public void setUpTextures (int program, GL2 gl2)
    {
        int isTex = gl2.glGetUniformLocation(program,"isTex");

        gl2.glUniform1f(isTex,1);
    }
}
