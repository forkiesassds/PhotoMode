#version 150

in vec2 texCoord;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform float intensity;

out vec4 fragColor;

void main() {
	vec3 color = texture(DiffuseSampler, texCoord).rgb;
	float depth = texture(DiffuseDepthSampler, texCoord).r;

	depth = (depth - 0.45) * 10;

	color = mix(color, vec3(depth), intensity);

	fragColor = vec4(color, 1.0);
}