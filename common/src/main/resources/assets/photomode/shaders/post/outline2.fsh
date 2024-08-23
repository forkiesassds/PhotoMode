#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D InDepthSampler;
uniform float Intensity;

out vec4 fragColor;

float getDepth(vec2 coord) {
    return texture(InDepthSampler, coord).r;
}

void main() {
    float outline = 0.0;

    float size = 4.0 * Intensity;

    outline += clamp(getDepth(texCoord) - getDepth(texCoord + vec2(size * oneTexel.x, 0)), 0.0, 1.0);
    outline += clamp(getDepth(texCoord) - getDepth(texCoord - vec2(size * oneTexel.x, 0)), 0.0, 1.0);
    outline += clamp(getDepth(texCoord) - getDepth(texCoord + vec2(0, size * oneTexel.y)), 0.0, 1.0);
    outline += clamp(getDepth(texCoord) - getDepth(texCoord - vec2(0, size * oneTexel.y)), 0.0, 1.0);

    outline = 1.0 - clamp(outline * 512, 0.0, 1.0);

    fragColor = vec4(vec3(outline), 1.0);
}