#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    float brightness = dot(color.rgb, vec3(0.2126, 0.7152, 0.0722));
    color = vec4(color.rgb, brightness);
    fragColor = color;
}
