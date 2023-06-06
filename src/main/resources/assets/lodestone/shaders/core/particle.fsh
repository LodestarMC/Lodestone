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

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    color = color.rgb == vec3(0, 0, 0) ? vec4(0,0,0,0) : color;
    fragColor = linear_fog(vec4(color.rgb, color.a*linear_fog_fade(vertexDistance, FogStart, FogEnd)), vertexDistance, FogStart, FogEnd, vec4(FogColor.rgb, color.r));
}
