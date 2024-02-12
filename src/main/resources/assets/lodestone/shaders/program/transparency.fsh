#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D LodestoneTranslucentSampler;
uniform sampler2D LodestoneTranslucentDepthSampler;
uniform sampler2D LodestoneAdditiveSampler;
uniform sampler2D LodestoneAdditiveDepthSampler;

in vec2 texCoord;

#define NUM_LAYERS 3

vec4 color_layers[NUM_LAYERS];
float depth_layers[NUM_LAYERS];
int active_layers = 0;

out vec4 fragColor;

void try_insert( vec4 color, float depth ) {
    if ( color.a == 0.0 ) {
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

vec4 blend( vec4 dst, vec4 src ) {
    return ( dst * ( 1.0 - src.a ) ) + src.rgba;
}
vec4 additive( vec4 color) {
    return vec4(color.rgb, (0.21 * color.r + 0.71 * color.g + 0.07 * color.b));
}

void main() {
    color_layers[0] = vec4( texture( DiffuseSampler, texCoord ).rgb, 1.0 );
    depth_layers[0] = texture( DiffuseDepthSampler, texCoord ).r;
    active_layers = 1;

    try_insert(texture(LodestoneTranslucentSampler, texCoord), texture(LodestoneTranslucentDepthSampler, texCoord).r );
    try_insert(additive(texture(LodestoneAdditiveSampler, texCoord)), texture(LodestoneAdditiveDepthSampler, texCoord).r);

    vec4 texelAccum = color_layers[0].rgba;
    for ( int ii = 1; ii < active_layers; ++ii ) {
        texelAccum = blend(texelAccum, color_layers[ii]);
    }
    fragColor = vec4(depth_layers[0], depth_layers[0], depth_layers[0], 1.0);
}
