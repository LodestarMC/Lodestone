#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    vec2 uv = texCoord0;

    float y = uv.y;
    float width = (1.-y);
    if (abs(uv.x-0.5)*2. > y)
    {
        discard;
    }
    uv.x -= 0.5*width;
    if (y != 0.){
        uv.x /= y;
    }
    vec4 color = texture(Sampler0, uv) * vertexColor * ColorModulator;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
