package com.velocity.simpletimestamps;

import com.velocity.simpletimestamps.command.TimestampCommand;
import com.velocity.simpletimestamps.config.TimestampConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SimpleTimestamps implements ModInitializer {
	public static final String MOD_ID = "simpletimestamps";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(TimestampCommand::register);
	}

	public static MutableText addTimestamp(Text message) {
		LocalTime now = LocalTime.now();
		String pattern = TimestampConfig.INSTANCE.isUse24HourFormat() 
			? (TimestampConfig.INSTANCE.isShowSeconds() ? "HH:mm:ss" : "HH:mm")
			: (TimestampConfig.INSTANCE.isShowSeconds() ? "hh:mm:ss a" : "hh:mm a");
			
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		String timestamp = now.format(formatter);
		
		return Text.literal("[" + timestamp + "] ")
			.styled(style -> style.withColor(TimestampConfig.INSTANCE.getTimestampColor()))
			.append(message);
	}
}