# What Are World Events?
World events are tickable instanced objects which are saved in a level capability, which means they are unique per dimension.
They can exist on the client and are ticked separately. World Events on the client can have renderers attached to them.

# Getting Started With World Events
As of Lodestone 1.21-1.7, World Events are now registered using a DeferredRegister.
Lodestone has some helper methods to make this process easier for you.
```java
public class WorldEventTypeRegistry {  
    private static final DeferredRegister<WorldEventType> WORLD_EVENT_TYPES = LodestoneWorldEventTypeRegistry.createRegister("mymod");  
    private static final Registry<WorldEventType> REGISTRY = LodestoneWorldEventTypeRegistry.makeRegistry(WORLD_EVENT_TYPES);
}
```
Like any DeferredRegister, you must initialize it with the Event Bus
```java
public class WorldEventTypeRegistry {  
    private static final DeferredRegister<WorldEventType> WORLD_EVENT_TYPES = LodestoneWorldEventTypeRegistry.createRegister("mymod");  
    private static final Registry<WorldEventType> REGISTRY = LodestoneWorldEventTypeRegistry.makeRegistry(WORLD_EVENT_TYPES);
      
    public static void init(IEventBus eventBus) {  
        WORLD_EVENT_TYPES.register(eventBus);  
    }  
  
}
```
Now to make your first WorldEventType you can use a [WorldEventType.Builder](https://github.com/LodestarMC/Lodestone/blob/1.21/src/main/java/team/lodestar/lodestone/systems/worldevent/WorldEventType.java#L45) which will automatically register any [WorldEventRenderer](https://github.com/LodestarMC/Lodestone/blob/1.21/src/main/java/team/lodestar/lodestone/systems/worldevent/WorldEventRenderer.java) you attach to it with [WorldEventType.Builder#clientSynced](https://github.com/LodestarMC/Lodestone/blob/1.21/src/main/java/team/lodestar/lodestone/systems/worldevent/WorldEventType.java#L74C27-L74C71).
```java
public class WorldEventTypeRegistry {
    private static final DeferredRegister<WorldEventType> WORLD_EVENT_TYPES = LodestoneWorldEventTypeRegistry.createRegister("mymod");
    private static final Registry<WorldEventType> REGISTRY = LodestoneWorldEventTypeRegistry.makeRegistry(WORLD_EVENT_TYPES);

    public static final Supplier<WorldEventType> EXAMPLE_WORLD_EVENT = WORLD_EVENT_TYPES.register("example_event", () ->
            WorldEventType.Builder.of(ExampleWorldEvent::new, ResourceLocation.fromNamespaceAndPath("mymod", "example_event"))
                    .clientSynced(null) // Null = no renderer but still sync to client
                    .build()
    );

    public static void init(IEventBus eventBus) {
        WORLD_EVENT_TYPES.register(eventBus);
    }

}
```