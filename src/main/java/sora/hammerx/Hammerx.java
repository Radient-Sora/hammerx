package sora.hammerx;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sora.hammerx.init.ModItems;
import sora.hammerx.proxy.CommonProxy;

@Mod(modid = Info.MODID, version = Info.VERSION, name = Info.MODNAME, acceptedMinecraftVersions = Info.ACCEPTVERS)
public class Hammerx {

    @Mod.Instance(Info.MODID)
    public static Hammerx Instance;

    @SidedProxy(clientSide = Info.CLIENTPROXY, serverSide = Info.COMMONPROXY)
    public static CommonProxy proxy;
    public static final Logger log = LogManager.getLogger(Info.MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        log.info("Pre-Initialization finished.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        log.info("Initialization finished.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        log.info("Post-Initialization finished.");
    }
    public static final CreativeTabs CREATIVE_TABS = new CreativeTabs(Info.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.WOODEN__HAMMER);
        }
    };
}
