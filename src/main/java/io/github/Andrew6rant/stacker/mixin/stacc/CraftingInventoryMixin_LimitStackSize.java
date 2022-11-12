package io.github.Andrew6rant.stacker.mixin.stacc;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;

//import net.fabricmc.loader.api.FabricLoader;

@Mixin(CraftingInventory.class)
public abstract class CraftingInventoryMixin_LimitStackSize implements Inventory {
	private static final int STACC_SIZE_LIMIT;
	static {
		if(ModList.get().isLoaded("fastbench")) {
			STACC_SIZE_LIMIT = 1_000_000;
		} else {
			STACC_SIZE_LIMIT = 1_000;
		}
	}

	@Override
	public int getMaxCountPerStack() {
		return STACC_SIZE_LIMIT;
	}
}
