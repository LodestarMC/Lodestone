# Getting Started
To register an OBJ model, you first need to create an OBJModelRegistry class to hold your models.
Below is the basic setup for registering an OBJ model. The model file should be located in `assets/mymod/models/my_model.obj`.
```java
@EventBusSubscriber(modid = "mymod", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OBJModelRegistry {
    
    // Simple OBJ Model, file is located in mymod:models/my_model.obj
    public static final ObjModel myModel = new ObjModel(ResourceLocation.fromNamespaceAndPath("mymod", "my_model"));


    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
        LodestoneOBJModels.registerModel(myModel);
    }
}
```

Now that you have registered your model, you can render it in any renderer you like by calling
```java
ObjModelRegistry.myModel.render(poseStack, renderType, bufferSource)
```

# Multi Level of Detail (LOD) Models
Lodestone also supports models with levels of details. To create a model with multiple levels of detail, you can use the `MultiLODModel` class.
Instead of directly taking a resourcelocation, you need to specify a [LODStrategy](https://github.com/LodestarMC/Lodestone/blob/1.21/src/main/java/team/lodestar/lodestone/systems/model/obj/lod/LODStrategy.java) which will will determine the logic for when to switch between LODs.
As of Lodestone 1.7.0, there are two built-in LOD strategies: `Distance` and `Graphics`

The `Distance` strategy will switch between LODs based on the distance from the camera to the model.

The `Graphics` strategy will switch between LODs based on the graphics settings of the game (Fast, Fancy, Fabulous).

In the example below, we create a model with 4 LODs, each with a different distance from the camera.
```java
@EventBusSubscriber(modid = "mymod", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OBJModelRegistry {
    
    
    public static final MultiLODModel LOD_MODEL = new MultiLODModel(LODStrategy.Distance(builder -> {
        builder.create(5.0f, ResourceLocation.fromNamespaceAndPath("mymod", "my_model_0"));
        builder.create(10.0f, ResourceLocation.fromNamespaceAndPath("mymod", "my_model_1"));
        builder.create(20.0f, ResourceLocation.fromNamespaceAndPath("mymod", "my_model_2"));
        builder.create(40.0f, ResourceLocation.fromNamespaceAndPath("mymod", "my_model_3"));
    }));


    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
        LodestoneOBJModels.registerModel(LOD_MODEL);
    }
}
```
Rendering the model is the same as before, but now the model will automatically switch between LODs based on the strategy you provided in the constructor.

# Model modifiers (Lodestone 1.7.0+)
#### Note: Model modifiers are very new and everything here is subject to major change.
#### Model modifers are also only currently unsupported in MultiLODModels.

To create a model with a modifier, you can use the `ObjModel.Builder` class.
```java
@EventBusSubscriber(modid = "mymod", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OBJModelRegistry {


    public static final ObjModel myModel = ObjModel.Builder.of(LodestoneLib.lodestonePath("my_model"))
            .withModifiers(queue -> {
                queue.queueModifier(new TriangulateModifier());
                queue.queueModifier(new ScaleModifier(0.5f));
            })
            .build();


    @SubscribeEvent
    public static void registerModels(FMLClientSetupEvent event) {
        LodestoneOBJModels.registerModel(myModel);
    }
}
```


# A lot of this stuff is subject to change, and I will be updating this page as I go along.