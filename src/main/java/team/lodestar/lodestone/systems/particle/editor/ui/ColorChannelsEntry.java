package team.lodestar.lodestone.systems.particle.editor.ui;

import net.minecraft.util.Mth;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class ColorChannelsEntry extends Entry {
    private final IntSupplier getter;
    private final IntConsumer setter;

    public ColorChannelsEntry(String label, IntSupplier getter, IntConsumer setter) {
        super(label, false, false);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public String value() {
        return "";
    }

    public int channelValue(int channel) {
        return switch (channel) {
            case 0 -> getter.getAsInt() >> 16 & 0xFF;
            case 1 -> getter.getAsInt() >> 8 & 0xFF;
            default -> getter.getAsInt() & 0xFF;
        };
    }

    public String channelLabel(int channel) {
        return label + " " + switch (channel) {
            case 0 -> "R";
            case 1 -> "G";
            default -> "B";
        };
    }
//might wanna need this in a future but idk
    public Entry channelEntry(int channel) {
        return new Entry(channelLabel(channel), true, false) {
            @Override
            public String value() {
                return String.valueOf(channelValue(channel));
            }

            @Override
            public void commit(String value) {
                setChannel(channel, Integer.parseInt(value));
            }
        };
    }

    public void nudgeChannel(int channel, int direction) {
        setChannel(channel, channelValue(channel) + direction);
    }

    private void setChannel(int channel, int rawValue) {
        int shift = switch (channel) {
            case 0 -> 16;
            case 1 -> 8;
            default -> 0;
        };
        int color = getter.getAsInt();
        int value = Mth.clamp(rawValue, 0, 255);
        int mask = 0xFF << shift;
        setter.accept(color & ~mask | value << shift);
    }
}
