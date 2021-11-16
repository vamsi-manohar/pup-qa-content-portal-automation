package Base;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesFileReader {

	public Properties getProperty() {
		FileInputStream file = null;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("resources/data.properties"));
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		}
		return prop;
	}
}
