#version 150

in vec2 texCoord;

uniform sampler2D InSampler;
uniform float Intensity;

out vec4 fragColor;

vec3 toSepia(vec3 color) {
    color.r = color.r * 0.393 + color.g * 0.769 + color.b * 0.189;
    color.g = color.r * 0.349 + color.g * 0.686 + color.b * 0.168;
    color.b = color.r * 0.272 + color.g * 0.534 + color.b * 0.131;

    return color;
}

void main() {
    vec3 color = texture(InSampler, texCoord).rgb;
    vec3 sepia = toSepia(color);

    color = mix(color, sepia, Intensity);

    fragColor = vec4(color, 1.0);
}