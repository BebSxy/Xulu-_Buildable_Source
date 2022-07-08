//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Jorge\Desktop\Minecraft-Deobfuscator3000-master\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraft.util.text.TextFormatting
 */
package me.alpha432.oyvey.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class MessageManager {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static String prefix = (Object)TextFormatting.LIGHT_PURPLE + "[" + (Object)TextFormatting.DARK_PURPLE + "Xulu+" + (Object)TextFormatting.LIGHT_PURPLE + "]" + (Object)TextFormatting.RESET;

    public static void sendClientMessage(String message, boolean forcePermanent) {
        if (MessageManager.mc.player != null) {
            try {
                TextComponentString e = new TextComponentString(prefix + " " + message);
                int i = forcePermanent ? 0 : 12076;
                MessageManager.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion((ITextComponent)e, i);
            }
            catch (NullPointerException nullpointerexception) {
                nullpointerexception.printStackTrace();
            }
        }
    }
}

