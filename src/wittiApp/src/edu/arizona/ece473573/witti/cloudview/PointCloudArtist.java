//ECE 573 Project
//Team: Witty
//Date: 4/17/14
//Authors: Alex Warren

package edu.arizona.ece473573.witti.cloudview;

import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

import edu.arizona.ece473573.witti.R;

import edu.arizona.ece473573.witti.activities.DisplayActivity;

/**
 * PointCloudArtist draws pointcloud with openGL.
 *
 * Initializes shaders, texture, and program for rendering
 * a point cloud. Contains draw function.
 * 
 * @author Alex Warren
 */
public class PointCloudArtist {
    private static final String CAT_TAG = "WITTI_PointCloudArtist";

    private DisplayActivity mDisplayActivity;

    //Handles for openGL objects
    private int mProgId;
    private int mTexId;
    private int mPositionHandle;
    private int mTextureHandle;
    private int mTimeHandle;
    private int mMaxZHandle;
    private int mMVPHandle;

    private float mMaxZ; //Will be used to visual effects TODO


    public PointCloudArtist(DisplayActivity display){
        mDisplayActivity = display;
    }

    /**
     * Draws PointCloud as a points with a texture.
     * 
     * @param mMVPMatrix combined model view projection matrix
     */
    public void draw(float[] mMVPMatrix){
        //Get the current PointCloud
        PointCloud pc = mDisplayActivity.mSequence.getCurrentFrame();
        if (pc == null){
            //Log.v(CAT_TAG, "Null point cloud at draw");
            return;
        }
        FloatBuffer vertex_buffer = pc.mVertexBuffer;
        
        //Utils.checkGlError(CAT_TAG, "Draw, Before Use Program");
        GLES20.glUseProgram(mProgId);
        //Utils.checkGlError(CAT_TAG, "Use program");
        
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexId);
        //Utils.checkGlError(CAT_TAG, "Bind Texture");
        
        GLES20.glUniform1i(mTextureHandle, 0);
        //Utils.checkGlError(CAT_TAG, "Texture handle");
        
        vertex_buffer.position(0);
        //TODO make constants for 3 and 4
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, vertex_buffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //Utils.checkGlError(CAT_TAG, "Buffer");

        GLES20.glUniform1f(mTimeHandle, 0f);
        GLES20.glUniform1f(mMaxZHandle, mMaxZ);
        GLES20.glUniformMatrix4fv(mMVPHandle, 1, false, mMVPMatrix, 0);
        //Utils.checkGlError(CAT_TAG, "Uniforms");
        
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vertex_buffer.capacity()/3);
        //Utils.checkGlError(CAT_TAG, "Draw");
    }

    /**
     * Initializes the shaders and registers them with openGL.
     */
    protected void initializeShaders(){
        //Utils.checkGlError(CAT_TAG, "Before Initialize");
        Log.v(CAT_TAG, "initializeShaders");
        //Vertex Shader
        //This gets called for each point.
        //gl_ indicates a output of the shader
        String strVShader = 
            "precision mediump float;" +
            "uniform mat4 u_MVPMatrix;" +
            "attribute vec4 a_Position;" +
            "uniform float a_time;" +
            "uniform float a_max_z;" +
            "varying vec4 v_color;" +
            "float time;" +
            "void main(){" +
                "gl_PointSize = 10.0;" +
                "v_color = vec4(.6,.6,.6+.4*(a_Position.z/a_max_z),1);" +
                "gl_Position = u_MVPMatrix * a_Position;" +
            "}";

        //Fragment Shader, used for interpolating between vertices, or
        //in this case around the particle
        String strFShader = 
            "precision mediump float;" +
            "uniform sampler2D u_texture;" +
            "varying vec4 v_color;" +
            "void main(){" +
                "vec4 tex = texture2D(u_texture, gl_PointCoord);" +
                "gl_FragColor = v_color * tex;" +
                "gl_FragColor.w = .7;" +
            "}";
        
        
        //Load everything and get handles

        mProgId = Utils.LoadProgram(strVShader, strFShader);
        //Utils.checkGlError(CAT_TAG, "After program load");
        mTexId = Utils.LoadTexture(mDisplayActivity.mCloudSurfaceView, R.drawable.particle);
        //Utils.checkGlError(CAT_TAG, "After loading texture");
        
        mPositionHandle = GLES20.glGetAttribLocation(mProgId, "a_Position");
        //Utils.checkGlError(CAT_TAG, "THIS Handle");
        mTextureHandle = GLES20.glGetUniformLocation(mProgId, "u_texture");
        mMVPHandle = GLES20.glGetUniformLocation(mProgId, "u_MVPMatrix");
        mTimeHandle = GLES20.glGetAttribLocation(mProgId, "a_time");
        mMaxZHandle = GLES20.glGetUniformLocation(mProgId, "a_max_z");
        //Utils.checkGlError(CAT_TAG, "After getting handles");
    }
}