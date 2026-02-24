package team.lodestar.lodestone.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.systems.screenshake.*;
import team.lodestar.lodestone.handlers.screenshake.ScreenshakeHandler;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

import java.util.function.*;

public class ScreenshakePayload extends OneSidedPayloadData {

    public final ScreenshakeInstance instance;

    public ScreenshakePayload(RegistryFriendlyByteBuf byteBuf) {
        this(ScreenshakeInstance.STREAM_CODEC.decode(byteBuf));
    }

    public ScreenshakePayload(Consumer<ScreenshakeBuilder> constructor) {
        ScreenshakeBuilder builder = ScreenshakeBuilder.create();
        constructor.accept(builder);
        this.instance = builder.build();
    }

    public ScreenshakePayload(ScreenshakeInstance instance) {
        this.instance = instance;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        ScreenshakeHandler.addScreenshake(instance);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf byteBuf) {
        ScreenshakeInstance.STREAM_CODEC.encode(byteBuf, instance);
    }
}
