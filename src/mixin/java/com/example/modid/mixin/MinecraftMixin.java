package com.example.modid.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.modid.ExampleMod;

import net.minecraft.client.Minecraft;

/** Client-only example. Mixin AP maps the method selector into the generated refmap. */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public int displayWidth;

    @Inject(method = "createDisplay", at = @At("HEAD"))
    private void onCreateDisplay(CallbackInfo ci) {
        ExampleMod.LOGGER.info("{} client Mixin is active (width {})", com.example.modid.Reference.MOD_NAME,
            displayWidth);
    }

}
