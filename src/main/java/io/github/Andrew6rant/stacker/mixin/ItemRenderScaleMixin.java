package io.github.Andrew6rant.stacker.mixin;

import io.github.Andrew6rant.stacker.Stacker;
//import io.github.Andrew6rant.stacker.StackerConfig;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
//import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Pseudo
@Mixin(targets = "net.devtech.stacc.ItemCountRenderHandler")
public class ItemRenderScaleMixin {
    @Inject(method = "scale", at = @At("RETURN"), cancellable = true)
    private void forceScale(CallbackInfoReturnable<Float> cir) {
        float intToFloat = (float) (Stacker.getStacker().getStackerConfig().itemCountScaleInt+50)/100;
        if (Stacker.getStacker().getStackerConfig().fontOverride) {
            cir.setReturnValue(cir.getReturnValue());
        } else {
            cir.setReturnValue(intToFloat);
        }
    }
}
