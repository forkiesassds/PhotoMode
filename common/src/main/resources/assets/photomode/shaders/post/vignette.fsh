#version 150

in vec2 texCoord;

uniform sampler2D InSampler;
uniform float Intensity;

out vec4 fragColor;

void main() {
    vec3 color = texture(InSampler, texCoord).rgb;

    float vignette = length(vec2(texCoord - 0.5)) * Intensity;
    vignette = clamp((vignette - 0.3) * 3, 0.0, 1.0);

    color = mix(color, vec3(0.0), vignette);

    fragColor = vec4(color, 1.0);
}