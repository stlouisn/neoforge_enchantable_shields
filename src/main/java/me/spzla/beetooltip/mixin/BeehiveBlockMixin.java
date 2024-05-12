package me.spzla.beetooltip.mixin;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

        List<BeehiveBlockEntity.BeeData> beeData = stack.getOrDefault(DataComponentTypes.BEES, List.of());

        int count = beeData.size();
        final int MAX_COUNT = 3;

        MutableText text = (MutableText) Text.of(String.format("Bees: %d/%d", count, MAX_COUNT));
        text.formatted(Formatting.GRAY);
        tooltip.add(text);
    }
}
