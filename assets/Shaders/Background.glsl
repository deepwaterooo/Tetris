
attribute vec3 aPosition;

varying vec2 vTexCoord;

void main()
{
	vec2 pos = sign(aPosition.xy);
	gl_Position = vec4(pos, 0.0, 1.0);

	vTexCoord = (vec2(pos.x, -pos.y) + 1.0) * 0.5;
}

// #SplitMarker

precision mediump float;

uniform sampler2D sBaseMap;

varying vec2 vTexCoord;

void main()
{
	gl_FragColor = vec4(texture2D(sBaseMap, vTexCoord).rgb, 1.0);
}
