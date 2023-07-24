#version 150

uniform sampler2D Sampler0;
uniform float LumiTransparency;
uniform float GameTime;
uniform float TimeOffset;
uniform float Speed;
uniform float Intensity;
uniform float XFrequency;
uniform float YFrequency;
uniform vec4 UVCoordinates;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

vec4 transformColor(vec4 initialColor, float lumiTransparent) {
    initialColor = lumiTransparent == 1. ? vec4(initialColor.xyz, (0.21 * initialColor.r + 0.71 * initialColor.g + 0.07 * initialColor.b)) : initialColor;
    return initialColor * vertexColor * ColorModulator;
}

void main() {
    float time = GameTime * Speed + TimeOffset;
    vec2 uv = texCoord0;
    vec2 uCap = vec2(UVCoordinates.x, UVCoordinates.y);
    vec2 vCap = vec2(UVCoordinates.z, UVCoordinates.w);

    uv.x += cos(uv.y*XFrequency+time)/Intensity;
    uv.y += sin(uv.x*YFrequency+time)/Intensity;

    uv.x = clamp(uv.x, uCap.x, uCap.y);
    uv.y = clamp(uv.y, vCap.x, vCap.y);
    vec4 color = transformColor(texture(Sampler0, uv), LumiTransparency);
    fragColor = color;
}
