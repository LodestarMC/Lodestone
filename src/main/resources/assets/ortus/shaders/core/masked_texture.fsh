#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler1, texCoord0) * texture(Sampler0, texCoord0) * vertexColor;
    fragColor = color * ColorModulator;
}
