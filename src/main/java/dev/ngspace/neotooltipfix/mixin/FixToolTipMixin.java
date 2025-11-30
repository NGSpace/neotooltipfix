package dev.ngspace.neotooltipfix.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.ngspace.neotooltipfix.Helper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.ResourceLocation;

@Mixin(GuiGraphics.class)
public abstract class FixToolTipMixin {

    @Shadow
    public abstract int guiWidth();

    @ModifyVariable(method = "renderTooltip", at = @At(value = "HEAD"), index = 2, argsOnly = true)
    public List<ClientTooltipComponent> makeListMutable(List<ClientTooltipComponent> value) {
        return new ArrayList<>(value);
    }

    @Inject(method = "renderTooltip", at = @At(value = "HEAD"))
    public void fix(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, ResourceLocation id, CallbackInfo ci) {
        Helper.newFix(components, textRenderer, x, guiWidth());
    }

    @ModifyVariable(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;pushMatrix()Lorg/joml/Matrix3x2fStack;"), index = 12)
    public int modifyRenderX(int value, Font textRenderer, List<ClientTooltipComponent> components, int x) {
        return Helper.shouldFlip(components, textRenderer, x);
    }
}