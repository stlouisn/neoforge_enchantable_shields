package me.spzla.beehivetooltip.mixin;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BlockWithEntity {

    @Shadow @Final
    public static IntProperty HONEY_LEVEL;

    @Shadow @Final
    public static int FULL_HONEY_LEVEL;

    @Unique
    private final Formatting defaultFormatting = Formatting.YELLOW;

    protected BeehiveBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);

        List<BeehiveBlockEntity.BeeData> beeData = stack.getOrDefault(DataComponentTypes.BEES, List.of());

        int count = beeData.size();
        final int MAX_COUNT = 3;

        BlockStateComponent blockState = stack.get(DataComponentTypes.BLOCK_STATE);

        if (blockState != null && !blockState.isEmpty()) {
            int honeyLevel = Integer.parseInt(blockState.properties().getOrDefault("honey_level", "0"));
            MutableText text = Text.translatable("beehivetooltip.honeylevel", honeyLevel, FULL_HONEY_LEVEL);
            text.formatted(defaultFormatting);
            tooltip.add(text);
        }

        if (count == 0) {
            MutableText text = Text.translatable("beehivetooltip.empty");
            text.formatted(defaultFormatting);
            tooltip.add(text);
            return;
        }

        MutableText text = Text.translatable("beehivetooltip.count", count, MAX_COUNT);
        text.formatted(defaultFormatting);
        tooltip.add(text);

    }
}
