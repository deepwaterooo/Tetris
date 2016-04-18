
#define BATCH_SIZE 20

uniform mat4 uWorldMatrix;
uniform mat4 uWorldViewProjMatrix;
uniform vec3 uLightDirection;
uniform vec4 uCameraPositionWorld;
uniform vec3 uTranslations[BATCH_SIZE];
//uniform vec4 uOrientations[BATCH_SIZE];

attribute vec3 aPosition;
attribute vec3 aNormal;
attribute vec2 aTexCoord;
attribute float aBatchPos;

varying vec3 vNormal;
varying vec2 vTexCoord;
varying vec3 vLightDirectionInv;
varying vec3 vViewDirectionInv;

//mat4 computeLocalMatrix(vec3 translation, vec4 orientation) {
//	vec4 q2 = orientation + orientation;
//	vec4 qx = orientation.xxxw * q2.xyzx;
//	vec4 qy = orientation.xyyw * q2.xyzy;
//	vec4 qz = orientation.xxzw * q2.xxzz;
//	return mat4(
//		(1.0 - qy.y) - qz.z, qx.y + qz.w, qx.z - qy.w, 0.0,
//		qx.y - qz.w, (1.0 - qx.x) - qz.z, qy.z + qx.w, 0.0,
//		qx.z + qy.w, qy.z - qx.w, (1.0 - qx.x) - qy.y, 0.0,
//		translation.x, translation.y, translation.z, 1.0);
//}

void main() {
	int batchPos = int(aBatchPos);
	vec3 translation = uTranslations[batchPos];
//	vec4 orientation = uOrientations[batchPos];
//	mat4 localMatrix = computeLocalMatrix(translation, orientation);

//	vec4 localPosition = localMatrix * vec4(aPosition, 1.0);
//	vec4 localNormal = localMatrix * vec4(aNormal, 0.0);
	vec4 localPosition = vec4(translation + aPosition, 1.0);
	vec4 localNormal = vec4(aNormal, 0.0);

	gl_Position = uWorldViewProjMatrix * localPosition;

	vTexCoord = aTexCoord;

	vNormal = (uWorldMatrix * localNormal).xyz;

	vLightDirectionInv = -uLightDirection;

	vec3 objectPosition = (uWorldMatrix * localPosition).xyz;
	vViewDirectionInv = -(objectPosition - uCameraPositionWorld.xyz);
}

// #SplitMarker

precision mediump float;

uniform float uAmbient;
uniform vec4 uSpecular;
uniform float uSpecularPower;

uniform sampler2D sBaseMap;

varying vec3 vNormal;
varying vec2 vTexCoord;
varying vec3 vLightDirectionInv;
varying vec3 vViewDirectionInv;

void main() {
	vec3 normal = normalize(vNormal);
	vec3 lightDirectionInv = normalize(vLightDirectionInv);
	vec3 viewDirectionInv = normalize(vViewDirectionInv);
	
	float nDotL = dot(normal, lightDirectionInv);
	vec3 reflection = normalize(((2.0 * normal) * nDotL) - lightDirectionInv);
	float rDotV = max(0.0, dot(reflection, viewDirectionInv));

	vec4 baseColor = texture2D(sBaseMap, vTexCoord);

	vec4 ambient = baseColor * vec4(uAmbient, uAmbient, uAmbient, 1.0);
	vec4 diffuse = baseColor * max(0.0, nDotL);
	vec4 specular = uSpecular * pow(rDotV, uSpecularPower);

	gl_FragColor = ambient + diffuse + specular;
}
