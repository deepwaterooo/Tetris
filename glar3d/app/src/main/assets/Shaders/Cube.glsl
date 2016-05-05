
uniform mat4 uViewMatrix;
uniform mat4 uWorldViewMatrix;
uniform mat4 uWorldViewProjMatrix;
uniform vec3 uLightDirection;
uniform vec4 uEyePosition;

attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec2 aTexCoord;

varying vec3 vNormal;
varying vec2 vTexCoord;
varying vec3 vLightDirection;
varying vec3 vViewDirection;

void main() {
	gl_Position = uWorldViewProjMatrix * vec4(aPosition, 1.0);
	
	vTexCoord = aTexCoord;
	
	vNormal = (uWorldViewMatrix * vec4(aNormal, 0.0)).xyz;
	
	vLightDirection = (uViewMatrix * vec4(-uLightDirection, 0.0)).xyz;
	
	vec3 objectPosition = (uWorldViewMatrix * vec4(aPosition, 1.0)).xyz;
	vViewDirection = uEyePosition.xyz - objectPosition;
}

// #SplitMarker

precision mediump float;

uniform float uAmbient;
uniform vec4 uSpecular;
uniform float uSpecularPower;

uniform sampler2D sBaseMap;

varying vec3 vNormal;
varying vec2 vTexCoord;
varying vec3 vLightDirection;
varying vec3 vViewDirection;

void main() {
	vec4 baseColor = texture2D(sBaseMap, vTexCoord);
	
	vec3 normal = normalize(vNormal);
	vec3 lightDirection = normalize(vLightDirection);
	vec3 viewDirection = normalize(vViewDirection);
	
	float nDotL = dot(normal, lightDirection);
	vec3 reflection = normalize(((2.0 * normal) * nDotL) - lightDirection);
	float rDotV = max(0.0, dot(reflection, viewDirection));
	
	vec4 ambient = vec4(uAmbient, uAmbient, uAmbient, 1.0) * baseColor;
	vec4 diffuse = max(0.0, nDotL) * baseColor;
	vec4 specular = uSpecular * pow(rDotV, uSpecularPower);
	
	gl_FragColor = ambient + diffuse + specular;
}
