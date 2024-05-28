#version 150

#moj_import <fog.glsl>
#moj_import <lodestone:common_math.glsl>

uniform sampler2D Sampler0;
uniform sampler2D SceneDepthBuffer;
uniform float LumiTransparency;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform mat4 InvProjMat;
uniform vec2 ScreenSize;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in float pixelDepthClip;

out vec4 fragColor;

vec4 transformColor(vec4 initialColor, float lumiTransparent) {
    initialColor = lumiTransparent == 1. ? vec4(initialColor.xyz, (0.21 * initialColor.r + 0.71 * initialColor.g + 0.07 * initialColor.b)) : initialColor;
    return initialColor * vertexColor * ColorModulator;
}

vec4 applyFog(vec4 initialColor, float fogStart, float fogEnd, vec4 fogColor, float vertexDistance) {
    return linear_fog(vec4(initialColor.rgb, initialColor.a*linear_fog_fade(vertexDistance, fogStart, fogEnd)), vertexDistance, fogStart, fogEnd, vec4(fogColor.rgb, initialColor.r));
}

void main() {
    vec4 color = transformColor(texture(Sampler0, texCoord0), LumiTransparency);
    fragColor = applyFog(color, FogStart, FogEnd, FogColor, vertexDistance);

    vec2 screenUV = gl_FragCoord.xy/ScreenSize;

    float sceneDepthClip = getDepth(SceneDepthBuffer, screenUV);

    vec3 sceneViewSpace = viewSpaceFromDepth(SceneDepthBuffer, screenUV, InvProjMat);
    vec3 pixelViewSpace = viewSpaceFromDepthFloat(pixelDepthClip, screenUV, InvProjMat);

    float spacing = pixelViewSpace.z - sceneViewSpace.z;
    float fade = clamp(spacing * 0.5, 0.0, 1.0);

    fragColor.a *= fade;
}