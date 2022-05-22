#version 330

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform float Brightness; // the higher the brighter the glow
uniform int KernelSize; // the higher the taller the blur (and thus glow)
uniform float BlurDeviation; // standard deviation of blur. higher, blurrier

out vec4 fragColor;

float gaussian(float x)
{
	float s = 2 * BlurDeviation * BlurDeviation;
	return 1.0 / sqrt(s * 3.14) * exp(-(x * x / s));
}

void main()
{
	vec4 total;

	for (int i = -KernelSize; i <= KernelSize; i++)
	total += texture(DiffuseSampler, texCoord + vec2(0.0, oneTexel.y * i)) * gaussian(i);

	outColor = total * Brightness;
}