#version 330
//? if >=26.3
//#extension GL_ARB_separate_shader_objects : require

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

    //? if >=26.2
    //depth = 1.0 - depth;
    depth = (depth - 0.45) * 10;

    color = mix(color, vec3(depth), Intensity);

    fragColor = vec4(color, 1.0);
}