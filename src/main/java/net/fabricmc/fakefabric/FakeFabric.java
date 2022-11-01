package net.fabricmc.fakefabric;

import io.github.Andrew6rant.stacker.Stacker;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Mod(FakeFabric.MODID)
public class FakeFabric {
    public static Logger LOGGER = LogManager.getLogger("FakeFabric");
    public static final String MODID = Stacker.MODID;

    public FakeFabric() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInitializeCommon);

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

    }

    public void onInitializeCommon(final FMLCommonSetupEvent event) {

    }

    public static Logger logger() {
        if (LOGGER == null) {
            LOGGER = LogManager.getLogger("FakeFabric");
        }

        return LOGGER;
    }
}
