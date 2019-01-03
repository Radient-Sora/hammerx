package sora.hammerx.proxy;

import mcjty.theoneprobe.ForgeEventHandlers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import sora.hammerx.event.HammerxEventHandler;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event){

    }

    public void init(FMLInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new HammerxEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event){

    }
}
