package team.lodestar.lodestone.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


@Environment(EnvType.CLIENT)
@Mixin(ClientSuggestionProvider.class)
public interface ClientSuggestionProviderMixin {
    @Accessor
    Minecraft getMinecraft();
}
