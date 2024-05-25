package me.spzla.beehivetooltip.mixin;

import me.spzla.beehivetooltip.BeehiveTooltipClient;
import me.spzla.beehivetooltip.config.BeehiveTooltipConfig;
import me.spzla.beehivetooltip.config.TooltipDisplayModeEnum;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BlockWithEntity {
    protected BeehiveBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        BeehiveTooltipConfig config = BeehiveTooltipClient.getConfig();
        if (!config.enabled) return;

        List<BeehiveBlockEntity.BeeData> beeData = stack.getOrDefault(DataComponentTypes.BEES, List.of());

        int count = beeData.size();
        final int MAX_COUNT = 3;

        if (count == 0) {
            MutableText text = Text.translatable("beehivetooltip.empty");
            text.formatted(Formatting.GRAY);
            tooltip.add(text);
            return;
        }

        if (config.displayMode.equals(TooltipDisplayModeEnum.COMPACT)) {
            MutableText text = Text.translatable("beehivetooltip.compact", count, MAX_COUNT);
            text.formatted(Formatting.GRAY);
            tooltip.add(text);
        } else if (config.displayMode.equals(TooltipDisplayModeEnum.EXTENDED)) {
            tooltip.add((Text.translatable("beehivetooltip.extended.title")).formatted(Formatting.GRAY));
            for (BeehiveBlockEntity.BeeData bee: beeData) {
                NbtElement customName = bee.entityData().copyNbt().get("CustomName");
                MutableText text = (MutableText) Text.of(
                        String.format("%s %s",
                                config.beeMode ? "\uD83D\uDC1D": "-",
                                customName != null ? customName.toString() : "Bee"));
                text.formatted(Formatting.GRAY);
                tooltip.add(text);
            }
        }
    }
}
