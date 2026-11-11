package com.example.modid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.modid.proxy.IProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, updateJSON = "@{MOD_UPDATE_JSON}")
public class ExampleMod {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    @SidedProxy(modId = Reference.MOD_ID, clientSide = "@{CLIENT_PROXY}", serverSide = "@{SERVER_PROXY}") public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Hello from {}!", Reference.MOD_NAME);
        proxy.preInit(event);
    }

}
