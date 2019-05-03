package de.systemticks.dlt.uart2ip.conf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ConfigManager {

    private static Gson deserializer = new GsonBuilder().registerTypeAdapter(Config.class, new ConfigDeserializer()).create();
    private static Logger logger = LoggerFactory.getLogger(ConfigManager.class);	

	public static Config getConfiguration() 
	{
		logger.info("Load Configuration");
        Config conf;
		try {
			conf = deserializer.fromJson(new BufferedReader(new FileReader("dlt_uart2ip.conf")), Config.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {			
			logger.error(e.getMessage());
			logger.error("Initialize from config file failed. Start with default configuration instead");
			conf = new Config();
		}
		return conf;
	}
	
}
