package com.velocity.simpletimestamps.mixin;

import com.velocity.simpletimestamps.SimpleTimestamps;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @ModifyArg(
        method = "addMessage(Lnet/minecraft/text/Text;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V"),
        index = 0
    )
    private Text addTimestamp(Text message) {
        return SimpleTimestamps.addTimestamp(message);
    }
} 