package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import Base.TestBase;

public class ConfigReader extends TestBase{

	public static String getValue(String env,String key) {
		Properties properties = new Properties();
		FileInputStream fileinputstream = null;
		String configFilePath= System.getProperty("user.dir")+"\\src\\main\\java\\configuration\\"
				+env.toUpperCase()+"-config.properties";
		try {
			fileinputstream = new FileInputStream(configFilePath);
			properties.load(fileinputstream);
			fileinputstream.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
	
}
