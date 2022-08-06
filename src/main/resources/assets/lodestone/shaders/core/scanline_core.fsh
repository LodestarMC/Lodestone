#version 330

uniform sampler2D Sampler0; // texture to overlay on

in vec2 texCoord; // texture coordinate of fragment

uniform vec2 InSize; // input texture size
uniform float GameTime; // game time
uniform float density; // how many lines
uniform float opacityLine; // line opacity
uniform float opacityNoise; // self explanatory
uniform float flickering; // how much flickering

out vec4 fragColor; // output to framebuffer

float random(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float blend(const in float x, const in float y){
    return (x < 0.5) ? (2.0 * x * y) : (1.0 - 2.0 * (1.0 - x) * (1.0 - y));
}

vec3 blend(const in vec3 x, const in vec3 y, const in float a){
    return vec3(blend(x.r, y.r, a), blend(x.g, y.g, a), blend(x.b, y.b, a));
}

void main() {
    vec2 uv = texCoord/InSize;
    vec3 col = texture(Sampler0, uv).rgb;

    float count = InSize.y/density;
    vec2 lines = vec2(sin(uv.y * count, cos(uv.y * count)));
    vec3 scans = vec3(lines.x, lines.y, lines.x);

    col += col * scans * opacityLine;
    col += col * vec3(random(uv * Time)) * opacityNoise;
    col += fragColor * sin(110.0 * Time) + flickering;

    fragColor = vec4(col, 1.0);
}