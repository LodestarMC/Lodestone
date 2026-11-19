package team.lodestar.lodestone.modules.toolkit.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

public class ClientTooltipComponentWrapper implements TooltipComponent {

    private final ClientTooltipComponent component;

    private ClientTooltipComponentWrapper(ClientTooltipComponent component) {
        this.component = component;
    }

    public static ClientTooltipComponentWrapper wrap(ClientTooltipComponent component) {
        return new ClientTooltipComponentWrapper(component);
    }

    public ClientTooltipComponent unwrap() {
        return component;
    }

    @ApiStatus.Internal
    public static Optional<ClientTooltipComponent> unwrap(TooltipComponent component) {
        if (component instanceof ClientTooltipComponentWrapper wrapper) {
            return Optional.ofNullable(wrapper.unwrap());
        }
        return Optional.empty();
    }
}
