#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform vec4 DarkColor;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

vec4 lerp(float value, vec4 colorone, vec4 colortwo)
{
	return (colorone + value*(colortwo-colorone));
}

void main() {
    vec2 uv = texCoord0;

    vec4 textureColor = texture(Sampler0, uv);
    float luminosity = 0.299*textureColor.x + 0.587*textureColor.y + 0.114*textureColor.z;
    vec4 finalColor = lerp(luminosity, DarkColor, vertexColor);

    fragColor = finalColor*textureColor*textureColor.w*ColorModulator;
}
