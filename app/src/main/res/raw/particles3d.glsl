
uniform mat4 world;
uniform mat4 worldViewProjection;
uniform vec3 worldVelocity;
uniform vec3 worldAcceleration;
uniform float timeRange;
uniform float time;
uniform float frameDuration;
uniform float numFrames;

attribute vec4 color;
attribute vec4 position; // uv, lifeTime, frameStart
attribute vec4 texCoord0; // position.xyz, startTime
attribute vec4 texCoord1; // velocity.xyz, startSize
attribute vec4 texCoord2; // acceleration.xyz, endSize
attribute vec4 texCoord3; // spinStart.x, spinSpeed.y
attribute vec4 texCoord4; // orientation

varying vec4 v_position;
varying vec2 v_texcoord;
varying float v_percentLife;
varying vec4 v_colorMult;

void main() {
	vec4 uvLifeTimeFrameStart = position;
	vec4 positionStartTime = texCoord0;
	vec4 velocityStartSize = texCoord1;
	vec4 accelerationEndSize = texCoord2;
	vec4 spinStartSpinSpeed = texCoord3;
	vec4 orientation = texCoord4;
	vec4 colorMult = color;
	vec2 uv = uvLifeTimeFrameStart.xy;
	float lifeTime = uvLifeTimeFrameStart.z;
	float frameStart = uvLifeTimeFrameStart.w;
	vec3 position = positionStartTime.xyz;
	float startTime = positionStartTime.w;
	vec3 velocity = (world * vec4(velocityStartSize.xyz, 0)).xyz + worldVelocity;
	float startSize = velocityStartSize.w;
	vec3 acceleration = (world * vec4(accelerationEndSize.xyz, 0)).xyz + worldAcceleration;
	float endSize = accelerationEndSize.w;
	float spinStart = spinStartSpinSpeed.x;
	float spinSpeed = spinStartSpinSpeed.y;

	float localTime = mod((time - startTime), timeRange);
	float percentLife = localTime / lifeTime;

	float frame = mod(floor(localTime / frameDuration + frameStart), numFrames);
	float uOffset = frame / numFrames;
	float u = uOffset + (uv.x + 0.5) * (1.0 / numFrames);

	v_texcoord = vec2(u, uv.y + 0.5);
	v_colorMult = colorMult;

	float size = mix(startSize, endSize, percentLife);
	size = (percentLife < 0.0 || percentLife > 1.0) ? 0.0 : size;
	float s = sin(spinStart + spinSpeed * localTime);
	float c = cos(spinStart + spinSpeed * localTime);

	vec4 rotatedPoint = vec4((uv.x * c + uv.y * s) * size, 0.0, (uv.x * s - uv.y * c) * size, 1.0);
	vec3 center = velocity * localTime + acceleration * localTime * localTime + position;
     
	vec4 q2 = orientation + orientation;
	vec4 qx = orientation.xxxw * q2.xyzx;
	vec4 qy = orientation.xyyw * q2.xyzy;
	vec4 qz = orientation.xxzw * q2.xxzz;

	mat4 localMatrix = mat4(
			(1.0 - qy.y) - qz.z, qx.y + qz.w, qx.z - qy.w, 0.0,
			qx.y - qz.w, (1.0 - qx.x) - qz.z, qy.z + qx.w, 0.0,
			qx.z + qy.w, qy.z - qx.w, (1.0 - qx.x) - qy.y, 0.0,
			center.x, center.y, center.z, 1.0);
	rotatedPoint = localMatrix * rotatedPoint;
	gl_Position = worldViewProjection * rotatedPoint;
	v_percentLife = percentLife;
}

// #SplitMarker

precision mediump float;

varying vec4 v_position;
varying vec2 v_texcoord;
varying float v_percentLife;
varying vec4 v_colorMult;

uniform sampler2D rampSampler;
uniform sampler2D colorSampler;

void main() {
	vec4 colorMult = texture2D(rampSampler, vec2(v_percentLife, 0.5)) * v_colorMult;
	vec4 color = texture2D(colorSampler, v_texcoord) * colorMult;
	gl_FragColor = color;
}
