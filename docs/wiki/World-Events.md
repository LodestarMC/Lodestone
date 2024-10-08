# WIP DOCS | Any code here is subject to major change
World Events are designed to solve the biggest issues with entities being that entities will only be ticked/loaded when the chunks they are in are loaded. Instead they are ticked on the level for both the server and client.

# Getting Started
As of Lodestone 1.21-1.7
To create your first world event type you must register it with a custom DeferredRegister. Lodestone has some helper methods to make this process easier for you.
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