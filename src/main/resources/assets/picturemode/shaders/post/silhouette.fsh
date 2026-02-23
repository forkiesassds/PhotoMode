#version 330

in vec2 texCoord;

uniform sampler2D InSampler;
uniform sampler2D InDepthSampler;
//? if >=1.21.6 {
layout(std140) uniform PMConfig {
    float Intensity;
};
//? } else {
//uniform float Intensity;
//? }

out vec4 fragColor;

void main() {
    vec3 color = texture(InSampler, texCoord).rgb;
    float depth = texture(InDepthSampler, texCoord).r;

    depth = (depth - 0.45) * 10;

    color = mix(color, vec3(depth), Intensity);

    fragColor = vec4(color, 1.0);
}