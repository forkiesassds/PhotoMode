#version 150

in vec2 texCoord;

uniform sampler2D InSampler;
uniform float Intensity;

out vec4 fragColor;

void main() {
    vec3 color = texture(InSampler, texCoord).rgb;
    color = mix(color, 1.0 - color, Intensity);

    fragColor = vec4(color, 1.0);
}