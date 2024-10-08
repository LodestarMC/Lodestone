A post processing shader is a type of shader applied to the game's render output after the scene has been rendered, but before the final display on the screen. This allows you to modify the final image that is displayed in many ways. You can read up on Minecraft shaders [here](https://docs.google.com/document/d/15TOAOVLgSNEoHGzpNlkez5cryH3hFF3awXL5Py81EMk/edit).

# Basic Post Processing Shaders
Lodestone introduces the [`PostProcessor`](https://github.com/LodestarMC/Lodestone/blob/main/src/main/java/team/lodestar/lodestone/systems/postprocess/PostProcessor.java) class, designed to facilitate the integration of post-processing shaders into Minecraft, allowing you to get started with post processing shaders within minutes.

In this guide I will walk you through creating a shader that will tint your screen pink.

## Setup
To begin, create a new class that extends Lodestone's [`PostProcessor`](https://github.com/LodestarMC/Lodestone/blob/main/src/main/java/team/lodestar/lodestone/systems/postprocess/PostProcessor.java) class, see example below.
```java
package dev.cryptic.aspect.client.shader.lodestone.post;  
  
import com.mojang.blaze3d.vertex.PoseStack;  
import net.minecraft.resources.ResourceLocation;  
import team.lodestar.lodestone.systems.postprocess.PostProcessor;  
  
public class TintPostProcessor extends PostProcessor {  
    @Override  
  public ResourceLocation getPostChainLocation() {  
        return null;  
    }  
  
    @Override  
  public void beforeProcess(PoseStack poseStack) {  
  
    }  
  
    @Override  
  public void afterProcess() {  
  
    }  
}
```
There are a few things you must do to setup    this class, mainly you should modify the `getPostChainLocation()` method to return a new ResourceLocation which contains your mod's ID and the name of the shader. Next you should create a static instance of your post processor making it a singleton class.
```java
public static final TintPostProcessor INSTANCE = new TintPostProcessor();

@Override  
public ResourceLocation getPostChainLocation() {  
    return new ResourceLocation(Aspect.MOD_ID, "tint_post");
}
```
## Register Post Processor
Now you must register your new post processor using [`PostProcessorHandler`](https://github.com/LodestarMC/Lodestone/blob/main/src/main/java/team/lodestar/lodestone/systems/postprocess/PostProcessHandler.java), this will happen in the `FMLClientSetupEvent`, see example below.
```java
@Mod.EventBusSubscriber(modid = Aspect.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)  
public class ClientModEvents  
{  
    @SubscribeEvent  
    public static void onClientSetup(FMLClientSetupEvent event) {   
        PostProcessHandler.addInstance(TintPostProcessor.INSTANCE);  
    }  
}
```
## Create your "Post" JSON file
Now your TintPostProcessor is registered! However nothing will happen just yet because we have not created the post file and program.
Remember how we had to define a ResourceLocation earlier in the method `getPostChainLocation()`?
In my example I used 
```java
new ResourceLocation(Aspect.MOD_ID, "tint_post")
```
which directly correlates with the file location `assets/aspect/shaders/post/tint_post.json` This is your "Post" JSON file and it will hold the instructions for your shader.

Below is a basic Post JSON which takes in the game's render output `minecraft:main`, hands it off to our program `aspect:tint`, and then passes it to Minecraft's `blit` program where it then **replaces** `minecraft:main`.
```json
{
	"targets": [
		"final"
	],
	"passes": [
		{
			"name": "aspect:tint",
			"intarget": "minecraft:main",
			"outtarget": "final"
		},
		{
			"name": "blit",
			"intarget": "final",
			"outtarget": "minecraft:main"
		}
	]
}
```
Now you are done with your "Post" JSON file!

## Shader Programs
When writing our "Post" JSON file it referenced something called `aspect:tint`, this is whats called a program. Essentially it is a function that we pass `minecraft:main` into before it returns the out target of `final`.
Now this is where the real magic happens.
In `assets/aspect/shaders/program/` create a new JSON file called `tint.json`
There are many values in this program file but the main ones we want to pay attention to right now are the `vertex` and `fragment` values. These specify which vertex and fragment shaders the program will use. For now keep the vertex shader as `blit`, we dont need to change that.
```json
{
    "blend": {
        "func": "add",
        "srcrgb": "srcalpha",
        "dstrgb": "1-srcalpha"
  },
    "vertex": "blit",
    "fragment": "aspect:tint",
    "attributes": [ "Position" ],
    "samplers": [
        { "name": "DiffuseSampler" }
    ],
    "uniforms": [
        { "name": "ProjMat", "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
        { "name": "InSize", "type": "float", "count": 2,  "values": [ 1.0, 1.0 ] }
        { "name": "OutSize", "type": "float", "count": 2,  "values": [ 1.0, 1.0 ] }
    ]
}
```
Now that our program has been created we can begin creating our fragment shader (fsh)!
In the same folder as the program `tint.json`, create the fragment shader file `tint.fsh`.
```glsl
#version 150
// The game's render output
uniform sampler2D DiffuseSampler;
// The texture coordinate represented as a 2D vector (x,y)
in vec2 texCoord;
// The output color of each pixel represented as a 4D vector (r,g,b,a)
out vec4 fragColor;

void main() {
	// Extract the original color of the pixel from the DiffuseSampler
	vec4 original = texture(DiffuseSampler, texCoord);
	// Pink!
	vec3 tintColor = vec3(1.0,0.0,1.0)
	// Multiply each rgba value by the tint color.
	vec4 result = original * vec4(tintColor, 1.0);
	
	// Set the fragColor output as the result
	fragColor = result;
}
```
Now when you launch the game everything should be pink!
## Tips
If you want to be able to turn your shader on and off you can use
```java
TintPostProcessor.INSTANCE.setActive(boolean);
```
To toggle the shader on and off you can use
```java
TintPostProcessor.INSTANCE.setActive(!TintPostProcessor.INSTANCE.isActive());
```
If you dont want the shader to be active by default you can add a static block to your `PostProcessor`
```java
public class TintPostProcessor extends PostProcessor {  
    public static final TintPostProcessor INSTANCE = new TintPostProcessor();  
    // Static block added to turn the shader off by default
    static {  
        INSTANCE.setActive(false);  
    }
    
    @Override  
	public ResourceLocation getPostChainLocation() {  
        return new ResourceLocation(Aspect.MODID, "tint");  
    }
    
    @Override  
	public void beforeProcess(PoseStack poseStack) {
	
	}
	
    @Override  
	public void afterProcess() {
	
	}
}
```

# Multi-Instance Post Processing Shaders
Lodestone introduces the [`MultiInstancePostProcessor`](https://github.com/LodestarMC/Lodestone/blob/1.20/src/main/java/team/lodestar/lodestone/systems/postprocess/MultiInstancePostProcessor.java) class, which extends the functionality of the [`PostProcessor`](https://github.com/LodestarMC/Lodestone/blob/main/src/main/java/team/lodestar/lodestone/systems/postprocess/PostProcessor.java) allowing developers to easily create multiple instances of a post-processing effect with just one shader applied. This is done by creating instances of a [`DynamicShaderFxInstance`](https://github.com/LodestarMC/Lodestone/blob/1.20/src/main/java/team/lodestar/lodestone/systems/postprocess/DynamicShaderFxInstance.java) which is then added to your MultiInstancePostProcessor. Then the processor will compile every uniform expressed as floats into a single DataBuffer that is then passed to the shader.

In this guide, we'll walk through creating a multi-instance post-processing shader that applies a glowing effect to specific areas in the world.

For this example you will need to utilize the Depth Buffer, you can follow [this tutorial](https://github.com/LodestarMC/Lodestone/wiki/Post-Processing-Shaders#accessing-the-depth-buffer) for more information.

## Setup

### Create DynamicShaderFxInstance

Start by creating a class that extends `DynamicShaderFxInstance`. This class will represent a single instance of your shader effect and will contain the necessary uniforms: `center`, `color`

```java
package dev.cryptic.aspect.client.shader.lodestone.post.multi.glow;

import org.joml.Vector3f;
import team.lodestar.lodestone.systems.postprocess.DynamicShaderFxInstance;
import java.util.function.BiConsumer;

public class LightingFx extends DynamicShaderFxInstance {
    public Vector3f center;
    public Vector3f color;

    public LightingFx(Vector3f center, Vector3f color) {
        this.center = center;
        this.color = color;
    }

    @Override
    public void writeDataToBuffer(BiConsumer<Integer, Float> writer) {
        writer.accept(0, center.x());
        writer.accept(1, center.y());
        writer.accept(2, center.z());
        writer.accept(3, color.x());
        writer.accept(4, color.y());
        writer.accept(5, color.z());
    }
}
```

### Create MultiInstancePostProcessor

Next, create a class that extends `MultiInstancePostProcessor`. This class will manage all instances of your shader effect.

```java
package dev.cryptic.aspect.client.shader.lodestone.post.multi.glow;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cryptic.aspect.Aspect;
import team.lodestar.lodestone.systems.postprocess.MultiInstancePostProcessor;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;

public class GlowPostProcessor extends MultiInstancePostProcessor<LightingFx> {
    public static final GlowPostProcessor INSTANCE = new GlowPostProcessor();
    private EffectInstance effectGlow;

    @Override
    public ResourceLocation getPostChainLocation() {
        return new ResourceLocation(Aspect.MOD_ID, "glow");
    }
    // Max amount of FxInstances that can be added to the post processor at once
    @Override
    protected int getMaxInstances() {
        return 16;
    }
    
    // We passed in a total of 6 floats/uniforms to the shader inside our LightingFx class so this should return 6, will crash if it doesn't match
    @Override
    protected int getDataSizePerInstance() {
        return 6;
    }

    @Override
    public void init() {
        super.init();
        if (postChain != null) {
            effectGlow = effects[0];
        }
    }

    @Override
    public void beforeProcess(PoseStack viewModelStack) {
        super.beforeProcess(viewModelStack);
        setDataBufferUniform(effectGlow, "DataBuffer", "InstanceCount");
    }
}
```

### Register Your MultiInstancePostProcessor
```java
@Mod.EventBusSubscriber(modid = Aspect.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PostProcessHandler.addInstance(GlowPostProcessor.INSTANCE);
    }
}
```

### Retrieving Uniforms in Shader
Your `glow.json` file should define 2 new uniforms, a DataBuffer and InstanceCount both as `"type": "int"` 
```json
{
  "blend": {
    "func": "add",
    "srcrgb": "srcalpha",
    "dstrgb": "1-srcalpha"
  },
  "vertex": "aspect:glow",
  "fragment": "aspect:glow",
  "attributes": [ "Position" ],
  "samplers": [
      { "name": "DiffuseSampler" }, 
      { "name": "MainDepthSampler" }
  ],
  "uniforms": [
      { "name": "DataBuffer", "type": "int", "count": 1, "values": [ 0 ] }, 
      { "name": "InstanceCount", "type": "int", "count": 1, "values": [ 0 ] }, 
      { "name": "ProjMat", "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
      { "name": "invProjMat", "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
      { "name": "invViewMat", "type": "matrix4x4", "count": 16, "values": [ 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 ] },
      { "name": "cameraPos", "type": "float", "count": 3, "values": [ 0.0, 0.0, 0.0 ] },
      { "name": "InSize", "type": "float", "count": 2, "values": [ 1.0, 1.0 ] }, 
      { "name": "OutSize", "type": "float", "count": 2, "values": [ 1.0, 1.0 ] }
  ]

}
```
Now your `glow.fsh` and/or `glow.vsh` files should be able to access the DataBuffer and InstanceCount uniforms.
Its important to note that the data buffer stores the data for all instances of the shader, so you will need to retrieve it using texelFetch.
![](https://i.gyazo.com/d690184cc2faa24af91acd2fdea8b276.png)
The diagram above is an example of how our `DataBuffer` is structured with two instances of our shader effect.

The first 6 values are the data for the first instance, and the next 6 values are the data for the second instance.

In each instance the first 3 values are the center of the effect, and the next 3 values are the color of the effect.

Knowing this we can define a for loop in our fragment shader to loop through all instances of the effect.
```glsl
for (int instance = 0; instance < InstanceCount; instance++) {
    int index = instance * 6; // Each instance has 6 values
    // 0-2: center, 3-5: color
    vec3 center = fetch3(DataBuffer, index);
    vec3 color = fetch3(DataBuffer, index + 3);
    
    // Per instance logic here
}
```

Now to complete the shader...

```glsl
#version 150

#moj_import <lodestone:common_math.glsl>

// Samplers
uniform sampler2D DiffuseSampler;
uniform sampler2D MainDepthSampler;
// Multi-Instance uniforms
uniform samplerBuffer DataBuffer;
uniform int InstanceCount;
// Matrices needed for world position calculation
uniform mat4 invProjMat;
uniform mat4 invViewMat;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 diffuseColor = texture(DiffuseSampler, texCoord);
    vec3 worldPos = getWorldPos(MainDepthSampler, texCoord, invProjMat, invViewMat, cameraPos);
    // Its important to set the fragColor to the diffuseColor before applying the effect!
    fragColor = diffuseColor;

    float radius = 10.0; // Change this value to modify the falloff of the light, or make it a uniform
    for (int instance = 0; instance < InstanceCount; instance++) {
        int index = instance * 6;// Each instance has 6 values
        // 0-2: center, 3-5: color
        vec3 center = fetch3(DataBuffer, index);
        vec3 color = fetch3(DataBuffer, index + 3);

        float distance = length(worldPos - center);
        if (distance <= radius) {
            float falloff = 1.0 - clamp(distance / radius, 0.0, 1.0);
            fragColor.rgb *= (color * falloff + 1.0);
        }
    }
}
```
Its important that you import Lodestone's [common functions](https://github.com/LodestarMC/Lodestone/blob/1.20/src/main/resources/assets/lodestone/shaders/include/common_math.glsl) in your shader file or else fetch3 and getWorldPos will not work.
```glsl
#moj_import <lodestone:common_math.glsl>
```


### Congratulations! You have made your first Multi-Instance Post Processing Shader!
Spawn a few instances of your shader effect in the world and watch them glow!
```java
Vector3f center = new Vector3f(0, 0, 0);
Vector3f color = new Vector3f(1, 0, 1);
GlowPostProcessor.INSTANCE.addInstance(new LightingFx(center, color));
```

# Accessing the Depth Buffer
A Depth Buffer is a type of image that stores the depth of each pixel in the scene.

This is useful for post processing shaders as it allows you to create effects such as depth of field, fog, and more.
![](https://i.imgur.com/kV4nqnE.png)

Lodestone takes care of copying the Depth Buffer so you can utilize it inside of your post processing shaders. To allow your shader program to access the depth buffer you must add "depthMain" to the targets in your "Post" JSON file.

You will also need to add an auxtarget pointed at depthMain:depth in your "Post" JSON file.
```json
{
    "targets": [
        "final",
        "depthMain"
    ],
    "passes": [
        {
            "name": "aspect:multi/glow",
            "intarget": "minecraft:main",
            "outtarget": "final",
            "auxtargets": [
                {
                    "name": "MainDepthSampler",
                    "id": "depthMain:depth"
                }
            ],
            "uniforms": [
            ]
        },
        {
            "name": "blit",
            "intarget": "final",
            "outtarget": "minecraft:main"
        }
    ]
}
```
Now inside of your Program JSON file, add a new Sampler with the same name as the Depth Sampler specified previously, in this case that would be "MainDepthSampler".
```json
{
  "blend": {
    "func": "add",
    "srcrgb": "srcalpha",
    "dstrgb": "1-srcalpha"
  },
  "vertex": "aspect:glow",
  "fragment": "aspect:glow",
  "attributes": [ "Position" ],
  "samplers": [
      { "name": "DiffuseSampler" }, 
      { "name": "MainDepthSampler" }
  ],
  "uniforms": [
      { "name": "InSize", "type": "float", "count": 2, "values": [ 1.0, 1.0 ] }, 
      { "name": "OutSize", "type": "float", "count": 2, "values": [ 1.0, 1.0 ] }
  ]
}
```
Inside of either your vertex and/or fragment shader you now can call on MainDepthSampler as a sampler2D.
```glsl
uniform sampler2D MainDepthSampler;
```
Now you can use the depth buffer in your shader to create effects such as depth of field, fog, and more!