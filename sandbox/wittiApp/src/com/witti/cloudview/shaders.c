//This isn't a file that is used directly in the project
//It is for convienence of editing the shaders. The shaders
//Must be stored as strings in the drawers.

//String strVShader = 
precision mediump float;
uniform mat4 u_MVPMatrix;
attribute vec4 a_Position;
uniform float a_time;
uniform float a_max_z;
varying float v_color;
float time;
void main(){
    gl_PointSize = 10.0;
    v_color = vec4(.6,.6,.6+.4*(a_Position.z/a_max_z),.5);
    gl_Position = u_MVPMatrix * a_Position;
}

//String strFShader = 
precision mediump float;
uniform sampler2D u_texture;
varying vec4 v_color;
void main(){
    vec4 tex = texture2D(u_texture, gl_PointCoord);
    gl_FragColor = v_color * tex;
}