#version 150

#moj_import <lodestone:common_math.glsl>

uniform sampler2D Sampler0;
uniform sampler2D SceneDepthBuffer;

uniform float LumiTransparency;
uniform float DepthFade;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform mat4 InvProjMat;
uniform vec2 ScreenSize;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 viewSpacePos;

out vec4 fragColor;


void main() {
    vec2 screenUV = gl_FragCoord.xy/ScreenSize;

    vec4 color = transformColor(texture(Sampler0, texCoord0), LumiTransparency, vertexColor, ColorModulator);
    fragColor = applyFog(color, FogStart, FogEnd, FogColor, vertexDistance);

    float sceneDepthClip = getDepth(SceneDepthBuffer, screenUV);
    vec3 sceneViewSpace = viewSpaceFromDepth(sceneDepthClip, screenUV, InvProjMat);

    float depthFade = applyDepthFade(sceneViewSpace.z, viewSpacePos.z, DepthFade);

    fragColor.a *= depthFade;
}