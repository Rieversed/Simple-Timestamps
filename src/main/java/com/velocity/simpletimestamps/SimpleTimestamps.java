package com.velocity.simpletimestamps;

import com.velocity.simpletimestamps.command.TimestampCommand;
import com.velocity.simpletimestamps.config.TimestampConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimpleTimestamps implements ClientModInitializer {
	public static final String MOD_ID = "simpletimestamps";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> 
			TimestampCommand.register(dispatcher, registryAccess));
	}

	public static MutableText addTimestamp(Text message) {
		LocalTime now = LocalTime.now();
		String pattern = TimestampConfig.INSTANCE.isUse24HourFormat() 
				? (TimestampConfig.INSTANCE.isShowSeconds() ? "HH:mm:ss" : "HH:mm")
				: (TimestampConfig.INSTANCE.isShowSeconds() ? "hh:mm:ss a" : "hh:mm a");
				
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String timestamp = now.format(formatter);
		
		MutableText timestampText = Text.literal("[" + timestamp + "] ")
			.styled(style -> style.withColor(TimestampConfig.INSTANCE.getTimestampColor()));
		
		if (TimestampConfig.INSTANCE.isColorWholeMessage()) {
			return timestampText.append(message.copy().styled(style -> 
				style.withColor(TimestampConfig.INSTANCE.getTimestampColor())));
		} else {
			return timestampText.append(message);
		}
	}
}