#version 120

varying vec2 texCo;
uniform sampler2D tex;

uniform float isTex;

// Light color
uniform vec4 lightColor;

// Diffuse reflection color
uniform vec4 diffuseColor;
uniform vec4 specColor;
uniform int specExp;

// Vectors "attached" to vertex and get sent to fragment shader
varying vec3 lPos;
varying vec3 vPos;
varying vec3 vNorm;

void main()
{ 
    // replace with proper texture function
    //if tex mapping

    if(isTex >.5){
        gl_FragColor = texture2D(tex, texCo);
    }

    else{

    // calculate your vectors
        vec3 L = normalize (lPos - vPos);
        vec3 N = normalize (vNorm);

        //Vectors for Specular Shading
        vec3 R = normalize (reflect(-L,N));
        vec3 V = normalize (-vPos);

         // calculate components
        vec4 diffuse = lightColor * diffuseColor * (dot(N, L));
        vec4 specular = lightColor* specColor * pow(max( dot(R,V), 0.0 ),specExp);

        // set the final color
        gl_FragColor = diffuse + specular ;
        }
} 
