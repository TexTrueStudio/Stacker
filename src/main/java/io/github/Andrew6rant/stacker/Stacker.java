package io.github.Andrew6rant.stacker;

import io.github.Andrew6rant.stacker.mixin.ItemAccess;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
//import net.fabricmc.api.ModInitializer;
import net.fabricmc.fakefabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.util.registry.Registry.ITEM_KEY;
@Mod(Stacker.MODID)
public class Stacker {
	public static final String MODID = "stacker";
	private static final Logger LOGGER = LogManager.getLogger("Stacker");
	private static Stacker stacker;
	static ConfigHolder<StackerConfig> stackerConfig;

	public Stacker() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInitialize);

		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

	}

	//@Override
	public void onInitialize(final FMLCommonSetupEvent event) {
		stacker = this;
		stackerConfig = AutoConfig.register(StackerConfig.class, GsonConfigSerializer::new);
		stackerConfig.registerSaveListener((configHolder, stackerConfig1) -> {
			loadStacker("save");
			return ActionResult.success(true);
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> loadStacker("load"));
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> loadStacker("reload"));

	}
	public static void loadStacker(String configMsg) {
		LOGGER.info("Stacker: Attempting to "+configMsg+" config...");
		Set<String> invalidSet = new HashSet<>();
		for (Item item : Registry.ITEM) {
			if (!item.isDamageable()) {
				Stacker.setMax(item, stackerConfig.getConfig().maxStacker);
			}
			Stacker.setMax(item, Stacker.overrideItem(item, stackerConfig.getConfig().itemOverride, invalidSet));
		}
		if (invalidSet.size() > 0) {
			LOGGER.error("Stacker: Invalid override entries!");
			LOGGER.warn("Stacker: The following entries were invalid:");
			for (String invalid : invalidSet) {
				LOGGER.warn("Stacker: \""+invalid+"\"");
			}
			LOGGER.warn("Stacker: Make sure to use the format, \"mod:item:max_stack\", or \"#tag:item:max_stack\".");
		}

		LOGGER.info(configMsg.equals("save") ? "Stacker: Config saved!": "Stacker: Config "+configMsg+"ed!");
	}
	public static void setMax(Item item, int max) {
		if (max >0) {
			((ItemAccess) item).setMaxCount(max);
		}
	}
	public StackerConfig getStackerConfig() {
		return stackerConfig.getConfig();
	}
	public static Stacker getStacker() {
		return stacker;
	}
	public static boolean isValid(String overrideEntry, String[] splitEntry, Set<String> invalidSet) {
		if (splitEntry.length != 3) {
			invalidSet.add(overrideEntry);
			return false;
		}
		try {
			int max = Integer.parseInt(splitEntry[2]);
		} catch (NumberFormatException e) {
			invalidSet.add(overrideEntry);
			return false;
		}
		return true;
	}
	public static Integer overrideItem(Item item, List<String> overrideList, Set<String> invalidSet) {
		for(String overrideEntry : overrideList) {
			if (overrideEntry.startsWith("#")) {
				String[] splitEntry = overrideEntry.trim().substring(1).split(":"); // split into three parts: tag id, item name, max count
				if (isValid(overrideEntry, splitEntry, invalidSet)) {
					List<TagKey<Item>> itemStream = item.getRegistryEntry().streamTags().collect(Collectors.toList());
					for (TagKey<Item> tagKey : itemStream) {
						if (item.getRegistryEntry().isIn(TagKey.of(ITEM_KEY, new Identifier(splitEntry[0], splitEntry[1])))) {
							return Integer.parseInt(splitEntry[2]);
						}
					}
				}
			} else {
				String[] splitEntry = overrideEntry.trim().split(":"); // split into three parts: tag id, item name, max count
				if (isValid(overrideEntry, splitEntry, invalidSet)) {
					if (Registry.ITEM.getId(item).toString().equalsIgnoreCase(splitEntry[0] + ":" + splitEntry[1])) {
						return Integer.parseInt(splitEntry[2]);
					}
				}
			}
		}
		return 0;
	}
}
