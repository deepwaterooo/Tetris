
uniform mat4 uViewMatrix;
uniform mat4 uWorldViewMatrix;
uniform mat4 uWorldViewInvMatrix;
uniform mat4 uWorldViewProjMatrix;
uniform vec3 uLightDirection;
uniform vec4 uEyePosition;

attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec3 aTangent;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;
varying vec3 vLightDirection;
varying vec3 vViewDirection;

void main() {
	gl_Position = uWorldViewProjMatrix * vec4(aPosition, 1.0);

	vTexCoord = aTexCoord;

	vec3 tangent = normalize((vec4(aTangent, 0.0) * uWorldViewInvMatrix).xyz);
	vec3 normal = normalize((vec4(aNormal, 0.0) * uWorldViewInvMatrix).xyz);
	vec3 binormal = cross(tangent, normal);

	vec3 lightDirection = (uViewMatrix * vec4(-uLightDirection, 0.0)).xyz;

	vLightDirection.x = dot(tangent, lightDirection);
	vLightDirection.y = dot(binormal, lightDirection);
	vLightDirection.z = dot(normal, lightDirection);

	vec3 objectPosition = (uWorldViewMatrix * vec4(aPosition, 1.0)).xyz;
	vec3 viewDirection = uEyePosition.xyz - objectPosition;

	vViewDirection.x = dot(tangent, viewDirection);
	vViewDirection.y = dot(binormal, viewDirection);
	vViewDirection.z = dot(normal, viewDirection);
}

// #SplitMarker

precision mediump float;

uniform vec4 uSpecular;
uniform float uSpecularPower;

uniform sampler2D sGlassMap;

varying vec2 vTexCoord;
varying vec3 vLightDirection;
varying vec3 vViewDirection;

void main() {
	vec3 normal = normalize((texture2D(sGlassMap, vTexCoord).xyz * 2.0) - 1.0);
	vec3 lightDirection = normalize(vLightDirection);
	vec3 viewDirection = normalize(vViewDirection);

	float nDotL = dot(normal, lightDirection);
	vec3 reflection = normalize(((2.0 * normal) * nDotL) - lightDirection);
	float rDotV = max(0.0, dot(reflection, viewDirection));

	vec4 specular = uSpecular * pow(rDotV, uSpecularPower);

	gl_FragColor = specular;
}
