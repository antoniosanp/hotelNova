package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static AppConfig instance;
    private final Properties dbProps = new Properties();
    private final Properties appProps = new Properties();

    private void load(String filename, Properties target) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (is == null) throw new RuntimeException("No se encontró " + filename);
            target.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Error cargando " + filename, e);
        }
    }

    private AppConfig() {
        load("database.properties", dbProps);
        load("config.properties", appProps);
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) instance = new AppConfig();
            }
        }
        return instance;
    }

    // DB getters
    public String getDbUrl()      { return dbProps.getProperty("db.url"); }
    public String getDbUser()     { return dbProps.getProperty("db.user"); }
    public String getDbPassword() { return dbProps.getProperty("db.password"); }
    public String getDbDriver()   { return dbProps.getProperty("db.driver"); }

    // App getters
    public String getAppName()    { return appProps.getProperty("app.name"); }
    public int getHoraCheckIn()   { return parseInt("horaCheckIn", 15); }
    public int getHoraCheckOut()  { return parseInt("horaCheckOut", 12); }
    //public String getViewType()   { return appProps.getProperty("view.type", "console"); }
    public double getIva() {
        try {
            return Double.parseDouble(appProps.getProperty("iva", "0.19"));
        } catch (NumberFormatException e) {
            return 0.19;
        }
    }

    private int parseInt(String key, int fallback) {
        try {
            return Integer.parseInt(appProps.getProperty(key, String.valueOf(fallback)));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
