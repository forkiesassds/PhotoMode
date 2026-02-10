#version 330

in vec2 texCoord;

//? if >=1.21.6 {
layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform PMConfig {
    float Intensity;
};
//? } else {
/*in vec2 oneTexel;

uniform float Intensity;
*///? }

uniform sampler2D InSampler;
uniform sampler2D InDepthSampler;

out vec4 fragColor;

float getDepth(vec2 coord) {
    return texture(InDepthSampler, coord).r;
}

void main() {
    //? if >=1.21.6
    vec2 oneTexel = 1.0 / InSize;
    vec3 color = texture(InSampler, texCoord).rgb;

    float outline = 0.0;
    float size = 4.0 * Intensity;

    outline += clamp(getDepth(texCoord) - getDepth(texCoord + vec2(size * oneTexel.x, 0)), 0.0, 1.0);
    outline += clamp(getDepth(texCoord) - getDepth(texCoord - vec2(size * oneTexel.x, 0)), 0.0, 1.0);
    outline += clamp(getDepth(texCoord) - getDepth(texCoord + vec2(0, size * oneTexel.y)), 0.0, 1.0);
    outline += clamp(getDepth(texCoord) - getDepth(texCoord - vec2(0, size * oneTexel.y)), 0.0, 1.0);

    outline = 1.0 - clamp(outline * 512, 0.0, 1.0);

    color *= outline;

    fragColor = vec4(color, 1.0);
}