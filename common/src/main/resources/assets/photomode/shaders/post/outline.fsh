#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D InSampler;
uniform sampler2D InDepthSampler;

uniform float Intensity;

out vec4 fragColor;

float getDepth(vec2 coord) {
    return texture(InDepthSampler, coord).r;
}

void main() {
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