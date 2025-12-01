import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    private String accentColor = "#6c6cffff";
    private boolean isDarkmode = false;

    public Settings() {
        // Keep default settings
    }

    public Settings(String accentColor, boolean isDarkmode) {
        this.accentColor = accentColor;
        this.isDarkmode = isDarkmode;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public boolean isDarkmode() {
        return isDarkmode;
    }

    public void setDarkmode(boolean darkmode) {
        isDarkmode = darkmode;
    }

    public static Settings readSettings(){
        Settings settingsFromJson = new Settings();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("saveData\\settings.json")) {
            settingsFromJson = gson.fromJson(reader, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settingsFromJson;
    }

    public static void writeSettings(Settings pSettings){
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("saveData\\settings.json")) {
            gson.toJson(pSettings, writer);
            System.out.println("Saved settings to \"data/settings.json\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
