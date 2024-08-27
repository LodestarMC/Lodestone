package team.lodestar.lodestone.registry.common;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.attachment.EntityAttachment;
import team.lodestar.lodestone.attachment.PlayerAttachment;
import team.lodestar.lodestone.attachment.WorldEventAttachment;

import java.util.function.Supplier;

public class LodestoneAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LodestoneLib.LODESTONE);

    public static final Supplier<AttachmentType<WorldEventAttachment>> WORLD_EVENT_DATA = ATTACHMENT_TYPES.register(
            "world_event_data", () -> AttachmentType.serializable(WorldEventAttachment::new).build()
    );

    public static final Supplier<AttachmentType<PlayerAttachment>> PLAYER_DATA = ATTACHMENT_TYPES.register(
            "player_data", () -> AttachmentType.serializable(PlayerAttachment::new).build()
    );

    public static final Supplier<AttachmentType<EntityAttachment>> ENTITY_DATA = ATTACHMENT_TYPES.register(
            "entity_data", () -> AttachmentType.serializable(EntityAttachment::new).build()
    );
}
