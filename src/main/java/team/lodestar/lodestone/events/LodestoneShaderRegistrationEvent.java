package team.lodestar.lodestone.events;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface LodestoneShaderRegistrationEvent {
    Event<Register> EVENT = EventFactory.createArrayBacked(Register.class, callbacks -> {
        return (manager, shaderList) -> {
            for (var callback : callbacks) {
                callback.register(manager, shaderList);
            }
        };
    });

    @FunctionalInterface
    interface Register {
        void register(ResourceProvider provider, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList) throws IOException;
    }
}
