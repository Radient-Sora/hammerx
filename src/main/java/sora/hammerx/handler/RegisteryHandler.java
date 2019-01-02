package sora.hammerx.handler;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sora.hammerx.Info;
import sora.hammerx.init.ModItems;

@Mod.EventBusSubscriber(modid = Info.MODID)
public class RegisteryHandler {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().register(ModItems.WOODEN__HAMMER);
        event.getRegistry().register(ModItems.STONE__HAMMER);
        event.getRegistry().register(ModItems.IRON_HAMMER);
        event.getRegistry().register(ModItems.GOLD_HAMMER);
        event.getRegistry().register(ModItems.DIAMOND_HAMMER);

        //ORE DICT ITEMS
        event.getRegistry().register(ModItems.COPPER_HAMMER);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static  void rendModel(ModelRegistryEvent event){
        regModel(ModItems.WOODEN__HAMMER);
        regModel(ModItems.STONE__HAMMER);
        regModel(ModItems.IRON_HAMMER);
        regModel(ModItems.GOLD_HAMMER);
        regModel(ModItems.DIAMOND_HAMMER);

        //ORE DICT ITEMS
        regModel(ModItems.COPPER_HAMMER);

    }

    public static void regModel(Item item){
        ModelLoader.setCustomModelResourceLocation(item,0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
