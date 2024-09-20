package dev.enchantshields.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Makes vanilla shield enchantable with an enchantability of 14.
 */
@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {

  public ShieldItemMixin(Settings settings) {
    super(settings);
  }

  @Override
  public boolean isEnchantable(ItemStack item) {
    return !item.hasEnchantments();
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getEnchantability() {
    return 14;
  }
}