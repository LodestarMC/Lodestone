package team.lodestar.lodestone.modules.toolkit.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

public class ClientTooltipComponentWrapper implements TooltipComponent {

    private final ClientTooltipComponent component;

    private ClientTooltipComponentWrapper(ClientTooltipComponent component) {
        this.component = component;
    }

    public static void registerFactory(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ClientTooltipComponentWrapper.class, ClientTooltipComponentWrapper::unwrap);
    }

    public static ClientTooltipComponentWrapper wrap(ClientTooltipComponent component) {
        return new ClientTooltipComponentWrapper(component);
    }

    public ClientTooltipComponent unwrap() {
        return component;
    }
}
