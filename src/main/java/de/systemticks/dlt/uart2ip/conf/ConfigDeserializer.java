package de.systemticks.dlt.uart2ip.conf;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ConfigDeserializer implements JsonDeserializer<Config>{

	@Override
	public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		
        final JsonObject jsonObject = json.getAsJsonObject();
        final Config config = new Config();

        JsonObject uart = jsonObject.get("uart").getAsJsonObject();
        config.setComPort(uart.get("port").getAsString());
        config.setBaudRate(uart.get("baudRate").getAsInt());
        config.setDataBits(uart.get("dataBits").getAsInt());
        config.setStopBits(uart.get("stopBits").getAsInt());
        config.setParity(uart.get("parity").getAsString());

        JsonObject dlt = jsonObject.get("dltserver").getAsJsonObject();
        config.setServerPort(dlt.get("port").getAsInt());
        
        config.setTmpFile(jsonObject.get("tmpFile").getAsString());
        
		return config;
	}

}
