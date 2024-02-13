#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D LodestoneTranslucentSampler;
uniform sampler2D LodestoneTranslucentDepthSampler;
uniform sampler2D LodestoneTranslucentParticleSampler;
uniform sampler2D LodestoneTranslucentParticleDepthSampler;
uniform sampler2D LodestoneAdditiveSampler;
uniform sampler2D LodestoneAdditiveDepthSampler;
uniform sampler2D LodestoneAdditiveParticleSampler;
uniform sampler2D LodestoneAdditiveParticleDepthSampler;

in vec2 texCoord;

#define NUM_LAYERS 5

vec4 color_layers[NUM_LAYERS];
float depth_layers[NUM_LAYERS];
int active_layers = 1;

out vec4 fragColor;

void try_insert(vec4 color, float depth ) {
    if (color.a == 0) {
        return;
    }

    color_layers[active_layers] = color;
    depth_layers[active_layers] = depth;

    int jj = active_layers++;
    int ii = jj - 1;
    while ( jj > 0 && depth_layers[jj] > depth_layers[ii] ) {
        float depthTemp = depth_layers[ii];
        depth_layers[ii] = depth_layers[jj];
        depth_layers[jj] = depthTemp;

        vec4 colorTemp = color_layers[ii];
        color_layers[ii] = color_layers[jj];
        color_layers[jj] = colorTemp;

        jj = ii--;
    }
}


float linearizeDepth(float depth) {
    float n = 1.0;
    float f = 100.0;
    return (2.0 * n) / (f + n - depth * (f - n));
}

vec4 blend(vec4 dst, vec4 src) {
    return dst * (1.0 - src.a) + src.rgba;
}
vec4 additive(vec4 color) {
    return vec4(color.rgb, (0.21 * color.r + 0.71 * color.g + 0.07 * color.b));
}

void main() {
    vec4 mainTexture = texture(DiffuseSampler, texCoord);
    float mainDepth = texture(DiffuseDepthSampler, texCoord).r;

    vec4 translucentTexture = texture(LodestoneTranslucentSampler, texCoord);
    float translucentDepth = texture(LodestoneTranslucentDepthSampler, texCoord).r;
    translucentDepth = mainDepth == translucentDepth ? 0 : translucentTexture.a == 0 ? 0 : translucentDepth;

    vec4 translucentParticleTexture = texture(LodestoneTranslucentParticleSampler, texCoord);
    float translucentParticleDepth = texture(LodestoneTranslucentParticleDepthSampler, texCoord).r;
    translucentParticleDepth = mainDepth == translucentParticleDepth ? 0 : translucentParticleTexture.a == 0 ? 0 : translucentParticleDepth;

    vec4 additiveTexture = additive(texture(LodestoneAdditiveSampler, texCoord));
    float additiveDepth = texture(LodestoneAdditiveDepthSampler, texCoord).r;
    additiveDepth = mainDepth == additiveDepth ? 0 : additiveTexture.a == 0 ? 0 : additiveDepth;

    vec4 additiveParticleTexture = additive(texture(LodestoneAdditiveParticleSampler, texCoord));
    float additiveParticleDepth = texture(LodestoneAdditiveParticleDepthSampler, texCoord).r;
    additiveParticleDepth = mainDepth == additiveParticleDepth ? 0 : additiveParticleTexture.a == 0 ? 0 : additiveParticleDepth;

    color_layers[0] = mainTexture;
    depth_layers[0] = mainDepth;

    try_insert(translucentTexture, translucentDepth);
    try_insert(translucentParticleTexture, translucentParticleDepth);
    try_insert(additiveTexture, additiveDepth);
    try_insert(additiveParticleTexture, additiveParticleDepth);

    vec4 texelAccum = color_layers[0].rgba;
    for ( int ii = 1; ii < active_layers; ++ii ) {
        texelAccum = blend(texelAccum, color_layers[ii]);
    }
    fragColor = texelAccum;
}
