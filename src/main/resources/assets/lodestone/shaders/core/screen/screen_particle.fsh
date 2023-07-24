#version 150

uniform sampler2D Sampler0;
uniform float LumiTransparency;

uniform vec4 ColorModulator;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

vec4 transformColor(vec4 initialColor, float lumiTransparent) {
    initialColor = lumiTransparent == 1. ? vec4(initialColor.xyz, (0.21 * initialColor.r + 0.71 * initialColor.g + 0.07 * initialColor.b)) : initialColor;
    return initialColor * vertexColor * ColorModulator;
}

void main() {
    vec4 color = transformColor(texture(Sampler0, texCoord0), LumiTransparency);
    fragColor = color;
}
