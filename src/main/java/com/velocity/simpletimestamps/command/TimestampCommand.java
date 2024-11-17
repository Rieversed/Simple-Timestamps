package com.velocity.simpletimestamps.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.velocity.simpletimestamps.config.TimestampConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TimestampCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("simpleTS")
            .then(CommandManager.literal("color")
                .then(CommandManager.argument("color", StringArgumentType.word())
                    .executes(context -> {
                        String colorName = StringArgumentType.getString(context, "color").toUpperCase();
                        try {
                            Formatting color = Formatting.valueOf(colorName);
                            if (!color.isColor() && color != Formatting.RESET) {
                                sendColorList(context.getSource());
                                return 0;
                            }
                            TimestampConfig.INSTANCE.setTimestampColor(color);
                            context.getSource().sendFeedback(() -> Text.literal("Timestamp color set to " + colorName)
                                .styled(style -> style.withColor(color)), false);
                        } catch (IllegalArgumentException e) {
                            sendColorList(context.getSource());
                        }
                        return 1;
                    })))
            .then(CommandManager.literal("seconds")
                .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(context -> {
                        boolean value = BoolArgumentType.getBool(context, "enabled");
                        TimestampConfig.INSTANCE.setShowSeconds(value);
                        context.getSource().sendFeedback(() -> Text.literal("Seconds are now " + (value ? "shown" : "hidden")), false);
                        return 1;
                    })))
            .then(CommandManager.literal("24hFormat")
                .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(context -> {
                        boolean value = BoolArgumentType.getBool(context, "enabled");
                        TimestampConfig.INSTANCE.setUse24HourFormat(value);
                        context.getSource().sendFeedback(() -> Text.literal("Using " + (value ? "24-hour" : "12-hour") + " format"), false);
                        return 1;
                    }))));
    }

    private static void sendColorList(ServerCommandSource source) {
        source.sendError(Text.literal("Available colors:"));
        StringBuilder colors = new StringBuilder();
        for (Formatting format : Formatting.values()) {
            if (format.isColor() || format == Formatting.RESET) {
                colors.append(format).append(format.getName()).append("§r, ");
            }
        }
        source.sendError(Text.literal(colors.substring(0, colors.length() - 2)));
    }
} 