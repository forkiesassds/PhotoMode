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
//in vec2 oneTexel;
//
//uniform float Intensity;
//? }

uniform sampler2D InSampler;

out vec4 fragColor;

vec3 getBlur(float r){
    //? if >=1.21.6
    vec2 oneTexel = 1.0 / InSize;
    float x, y, xx, yy, rr = r * r, dx, dy, w, w0;
    w0 = 0.3780 / pow(r, 1.975);
    vec2 p;
    vec4 col = vec4(0.0, 0.0, 0.0, 0.0);
    for (x = -r, p.x = (texCoord.x) + (x * oneTexel.x); x <= r; x++, p.x += oneTexel.x) {
        xx = x * x;
        for (y = -r, p.y = (texCoord.y) + (y * oneTexel.y); y <= r; y++, p.y += oneTexel.y) {
            yy = y * y;
            if (xx + yy <= rr) {
                w = w0 * exp((-xx - yy) / (2.0 * rr));
                col += texture(InSampler, p) * w;
            }
        }
    }
    return vec3(col.r, col.g, col.b);
}

void main() {
    vec3 color = texture(InSampler, texCoord).rgb;

    float blurFactor = clamp(distance(texCoord, vec2(0.5)) * 4 * Intensity, 0.0, 1.0);
    vec3 blur = getBlur(blurFactor * 15);

    //Apply Blur
    if (Intensity != 0.0) {
        color = mix(color, blur, blurFactor);
    }

    fragColor = vec4(color, 1.0);
}