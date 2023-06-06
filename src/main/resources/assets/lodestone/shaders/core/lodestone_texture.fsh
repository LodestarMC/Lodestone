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
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    float brightness = dot(color.rgb, vec3(0.2126, 0.7152, 0.0722));
    color = vec4(color.rgb, brightness);
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
