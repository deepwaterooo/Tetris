
uniform mat4 uViewMatrix;
uniform mat4 uWorldViewInvMatrix;
uniform mat4 uWorldViewProjMatrix;
uniform vec3 uLightDirection;

attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec3 aTangent;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;
varying vec3 vLightDirection;

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
}

// #SplitMarker

precision mediump float;

uniform vec4 uDiffuse;

uniform sampler2D sGlassMap;

varying vec2 vTexCoord;
varying vec3 vLightDirection;

void main() {
	vec3 normal = normalize((texture2D(sGlassMap, vTexCoord).xyz * 2.0) - 1.0);
	vec3 lightDirection = normalize(vLightDirection);

	float nDotL = dot(normal, lightDirection);

	vec4 diffuse = uDiffuse * (0.8 + 0.2 * max(0.0, nDotL));

	gl_FragColor = diffuse;
}
