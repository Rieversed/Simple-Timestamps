package com.velocity.simpletimestamps.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.velocity.simpletimestamps.config.TimestampConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class TimestampCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("simpleTS")
            .then(ClientCommandManager.literal("color")
                .then(ClientCommandManager.argument("color", StringArgumentType.word())
                    .suggests((context, builder) -> suggestColors(builder))
                    .executes(context -> {
                        String colorName = StringArgumentType.getString(context, "color").toUpperCase();
                        try {
                            Formatting color = Formatting.valueOf(colorName);
                            if (!color.isColor() && color != Formatting.RESET) {
                                sendColorList(context.getSource());
                                return 0;
                            }
                            TimestampConfig.INSTANCE.setTimestampColor(color);
                            context.getSource().sendFeedback(Text.literal("Timestamp color set to " + colorName)
                                .styled(style -> style.withColor(color)));
                        } catch (IllegalArgumentException e) {
                            sendColorList(context.getSource());
                        }
                        return 1;
                    })))
            .then(ClientCommandManager.literal("colorWholeMessage")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(context -> {
                        boolean value = BoolArgumentType.getBool(context, "enabled");
                        TimestampConfig.INSTANCE.setColorWholeMessage(value);
                        context.getSource().sendFeedback(Text.literal("Message coloring is now " + 
                            (value ? "enabled" : "disabled")));
                        return 1;
                    })))
            .then(ClientCommandManager.literal("seconds")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(context -> {
                        boolean value = BoolArgumentType.getBool(context, "enabled");
                        TimestampConfig.INSTANCE.setShowSeconds(value);
                        context.getSource().sendFeedback(Text.literal("Seconds are now " + (value ? "shown" : "hidden")));
                        return 1;
                    })))
            .then(ClientCommandManager.literal("24hFormat")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(context -> {
                        boolean value = BoolArgumentType.getBool(context, "enabled");
                        TimestampConfig.INSTANCE.setUse24HourFormat(value);
                        context.getSource().sendFeedback(Text.literal("Using " + (value ? "24-hour" : "12-hour") + " format"));
                        return 1;
                    }))));
    }

    private static CompletableFuture<Suggestions> suggestColors(SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();
        Arrays.stream(Formatting.values())
            .filter(format -> format.isColor() || format == Formatting.RESET)
            .map(format -> format.getName().toLowerCase())
            .filter(name -> name.startsWith(input))
            .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static void sendColorList(FabricClientCommandSource source) {
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