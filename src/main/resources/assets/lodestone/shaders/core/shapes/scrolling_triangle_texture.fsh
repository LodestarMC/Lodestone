#version 150

#moj_import <lodestone:common_math.glsl>

uniform sampler2D Sampler0;
uniform float LumiTransparency;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform float GameTime;
uniform float Speed;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec2 uv = texCoord0;
    float y = uv.y;
    float width = (1.-y);
    if (abs(uv.x-0.5)*2. > y) {
        discard;
    }
    uv.x -= 0.5*width;
    if (y != 0.) {
        uv.x /= y;
    }
    uv.y += GameTime*Speed;
    vec4 color = transformColor(texture(Sampler0, texCoord0), LumiTransparency, vertexColor, ColorModulator);
    fragColor = applyFog(color, FogStart, FogEnd, FogColor, vertexDistance);
}
