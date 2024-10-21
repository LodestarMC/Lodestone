#version 150

#moj_import <lodestone:common_math.glsl>

uniform sampler2D Sampler0;
uniform float LumiTransparency;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

float easeInOutPow(float x, float power) {
    if (x < 0.5) {
        return 0.5 * pow(2.0 * x, power);
    } else {
        return 1.0 - 0.5 * pow(-2.0 * x + 2.0, power);
    }
}

void main() {
    vec2 uv = texCoord0;
    float y = uv.y;
    float width = y;
    if (y > 0.6) {
        float delta = pow((y - 0.6) / 0.4, 2.0);
        width = uv.y * (1.0-delta);
    }
    if (abs(uv.x-0.5)*2. > width) {
        discard;
    }

    uv.x -= 0.5*(1.-width);
    if (width != 0.){
        uv.x /= width;
    }

    if (y > 0.7) {
        float delta = pow((uv.y - 0.7) / 0.3, 4.0);
        float power = 1.0+delta*2.0;
        float cut = delta * 0.5;
        float difference = -delta/2.0;
        if (uv.x <= 0.5) {
            difference = -difference;
        }
        uv.x -= difference * delta;
        uv.x = easeInOutPow(uv.x,power);
    }
    vec4 color = transformColor(texture(Sampler0, texCoord0), LumiTransparency, vertexColor, ColorModulator);
    fragColor = applyFog(color, FogStart, FogEnd, FogColor, vertexDistance);
}
