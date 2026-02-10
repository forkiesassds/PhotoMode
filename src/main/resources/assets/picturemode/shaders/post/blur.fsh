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

out vec4 fragColor;

vec3 getBlur() {
    //? if >=1.21.6
    vec2 oneTexel = 1.0 / InSize;
    vec3 blur = vec3(0.0);
    int q = int(16 * Intensity);
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

    float blurFactor = clamp(distance(texCoord, vec2(0.5)) * 1.0, 0.0, 1.0);
    vec3 blur = getBlur();

    //Apply Blur
    if (int(16 * Intensity) > 1) {
        color = mix(color, blur, blurFactor);
    }

    fragColor = vec4(color, 1.0);
}