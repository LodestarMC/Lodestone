#version 330

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;
uniform float GameTime;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec3 ChunkOffset;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;

void main() {
    vec3 position = Position + ChunkOffset;
    float animation = GameTime * 4000.0;
    float xs = 0.0;
    float zs = 0.0;
    if (UV0.x >= 288.0 / 1024.0 && UV0.x <= 304.0 / 1024.0) {
        if (UV0.y >= 64.0 / 1024.0 && UV0.y <= 80.0 / 1024.0) {
            xs = sin(position.x + animation);
            zs = cos(position.z + animation);
        }
    }
    if (UV0.x >= 144.0 / 1024.0 && UV0.x <= 160.0 / 1024.0) {
        if (UV0.y >= 72.0 / 1024.0 && UV0.y <= 288.0 / 1024.0) {
            xs = sin(position.x + animation);
            zs = cos(position.z + animation);
        }
    }
    if (UV0.x >= 16.0 / 1024.0 && UV0.x <= 32.0 / 1024.0) {
        if (UV0.y >= 368.0 / 1024.0 && UV0.y <= 384.0 / 1024.0) {
            xs = sin(position.x + animation);
            zs = cos(position.z + animation);
        }
    }
    if (UV0.x >= 160.0 / 1024.0 && UV0.x <= 172.0 / 1024.0) {
        if (UV0.y >= 96.0 / 1024.0 && UV0.y <= 112.0 / 1024.0) {
            xs = sin(position.x + animation);
            zs = cos(position.z + animation);
        }
    }
    if (UV0.x >= 256.0 / 1024.0 && UV0.x <= 272.0 / 1024.0) {
        if (UV0.y >= 48.0 / 1024.0 && UV0.y <= 64.0 / 1024.0) {
            xs = sin(position.x + animation);
            zs = cos(position.z + animation);
        }
    }

    gl_Position = ProjMat * ModelViewMat * (vec4(position, 1.0) + vec4(xs / 32.0, 0.0, zs / 32.0, 0.0));

    vertexDistance = length((ModelViewMat * vec4(Position + ChunkOffset, 1.0)).xyz);
    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
