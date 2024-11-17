package com.velocity.simpletimestamps.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimestampConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("simpletimestamps");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("simpletimestamps.json").toFile();
    
    private Formatting timestampColor = Formatting.GRAY;
    private boolean showSeconds = true;
    private boolean use24HourFormat = true;
    private boolean colorWholeMessage = false;
    
    public static TimestampConfig INSTANCE = load();
    
    public Formatting getTimestampColor() {
        return timestampColor;
    }
    
    public void setTimestampColor(Formatting color) {
        this.timestampColor = color;
        save();
    }
    
    public boolean isShowSeconds() {
        return showSeconds;
    }
    
    public void setShowSeconds(boolean showSeconds) {
        this.showSeconds = showSeconds;
        save();
    }
    
    public boolean isUse24HourFormat() {
        return use24HourFormat;
    }
    
    public void setUse24HourFormat(boolean use24HourFormat) {
        this.use24HourFormat = use24HourFormat;
        save();
    }

    public boolean isColorWholeMessage() {
        return colorWholeMessage;
    }

    public void setColorWholeMessage(boolean colorWholeMessage) {
        this.colorWholeMessage = colorWholeMessage;
        save();
    }
    
    public static TimestampConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, TimestampConfig.class);
            } catch (IOException e) {
                LOGGER.error("Failed to load config", e);
            }
        }
        return new TimestampConfig();
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }
} 