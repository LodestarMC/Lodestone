#version 150

#moj_import <fog.glsl>
#moj_import <projection.glsl>
#moj_import <lodestone:commoner.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat3 IViewRotMat;
uniform mat4 ProjMat;
uniform int FogShape;

out vec4 vertexColor;
out float vertexDistance;
out vec2 texCoord0;
out float pixelDepth;
out vec4 screenProjUV;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    pixelDepth = getDepthFromClipSpace(gl_Position);
    screenProjUV = projection_from_position(gl_Position);

    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    vertexDistance = fog_distance(ModelViewMat, IViewRotMat * Position, FogShape);

    texCoord0 = UV0;
}