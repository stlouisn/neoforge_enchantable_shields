package me.spzla.beehivetooltip.mixin;

import me.spzla.beehivetooltip.BeehiveTooltipClient;
import me.spzla.beehivetooltip.config.BeehiveTooltipConfig;
import me.spzla.beehivetooltip.config.HealthDisplayEnum;
import me.spzla.beehivetooltip.config.TooltipDisplayModeEnum;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.List;

@Mixin(BeehiveBlock.class)
public abstract class BeehiveBlockMixin extends BlockWithEntity {
    @Shadow @Final public static IntProperty HONEY_LEVEL;
    @Shadow @Final public static int FULL_HONEY_LEVEL;

    private Formatting defaultFormatting = Formatting.YELLOW;

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

        BlockStateComponent blockState = stack.get(DataComponentTypes.BLOCK_STATE);
        if (config.honeyLevel && blockState != null && !blockState.isEmpty()) {
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

        if (config.displayMode.equals(TooltipDisplayModeEnum.COMPACT)) {
            MutableText text = Text.translatable("beehivetooltip.compact", count, MAX_COUNT);
            text.formatted(defaultFormatting);
            tooltip.add(text);
        } else if (config.displayMode.equals(TooltipDisplayModeEnum.EXTENDED)) {
            tooltip.add((Text.translatable("beehivetooltip.extended.title")).formatted(defaultFormatting));
            for (BeehiveBlockEntity.BeeData bee: beeData) {
                NbtCompound nbt = bee.entityData().copyNbt();

                String customName = nbt.getString("CustomName");
                MutableText beeName;
                if (!customName.isBlank()) {
                    beeName = Text.literal(customName).formatted(Formatting.ITALIC);
                } else {
                    beeName = Text.translatable("entity.minecraft.bee");
                }

                float health = nbt.getFloat("Health");

                MutableText text = Text.literal(config.unicodeMode ? "\uD83D\uDC1D ": "- ")
                        .append(beeName);

                if (config.healthDisplay == HealthDisplayEnum.COMPACT) {
                    text.append(Text.translatable("beehivetooltip.healthdisplay.compact", (int) health));
                }

                text.formatted(defaultFormatting);
                tooltip.add(text);

                if (config.healthDisplay == HealthDisplayEnum.EXTENDED) {
                    MutableText healthText = Text.translatable("beehivetooltip.healthdisplay.extended",
                            Text.literal(String.join("", Collections.nCopies((int) Math.floor(health / 2), "♥"))).formatted(Formatting.RED),
                            Text.literal(String.join("", Collections.nCopies((int) Math.ceil((10f - health) / 2), "♡"))).formatted(Formatting.DARK_GRAY));
                    tooltip.add(healthText);
                }
            }
        }
    }
}
