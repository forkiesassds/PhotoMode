#version 150

in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D InSampler;
uniform sampler2D InDepthSampler;

uniform float Intensity;

out vec4 fragColor;

vec3 getBlur() {
    vec3 blur = vec3(0.0);
    int q = 12;
    float qh = q / 2.0 - 0.5;
    float allStrengths = 0.0;

    for (int i = 0; i < q; i++) {
        for (int j = 0; j < q; j++) {
            float strength = 1 - sin(length(vec2(i - qh, j - qh)) / qh);

            blur += texture(InSampler, texCoord + vec2(i - qh, j - qh) * oneTexel).rgb * strength;

            allStrengths += strength;
        }
    }
    blur /= allStrengths;
    return blur;
}

void main() {
    vec3 color = texture(InSampler, texCoord).rgb;
    float depth = texture(InDepthSampler, texCoord).r;

    depth = (depth - (0.45 + 0.1 * (1.0 - Intensity))) * 20;

    vec3 blur = getBlur();
    color = mix(color, blur, clamp(depth, 0.0, 1.0));

    //color = vec3(depth);
    //color = blur;

    fragColor = vec4(color, 1.0);
}