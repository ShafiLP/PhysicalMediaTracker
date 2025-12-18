import com.google.gson.Gson;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    private String accentColor = "#6c6cffff";
    private boolean isDarkmode = false;
    private String fontType = "Arial";
    private int fontSize = 12;
    private boolean rowContrast = true;
    private int uiScale = 3;
    private int coverResolution = 200;
    private boolean autoSave = true;

    private String pathToData = "data/saveData/data.json";

    public Settings() {
        // Keep default settings
    }

    public Settings(String accentColor, boolean isDarkmode,  Font font) {
        this.accentColor = accentColor;
        this.isDarkmode = isDarkmode;
        this.fontType = font.getFontName();
        this.fontSize = font.getSize();
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

    public Font getFont() {
        return new Font(fontType, Font.PLAIN, fontSize);
    }

    public void setFont(Font font) {
        this.fontType = font.getFontName();
        this.fontSize = font.getSize();
    }

    public boolean getRowContrast() {
        return rowContrast;
    }

    public void setRowContrast(boolean rowContrast) {
        this.rowContrast = rowContrast;
    }

    public int getUiScale() {
        return uiScale;
    }

    public void setUiScale(int uiScale) {
        this.uiScale = uiScale;
    }

    public int getCoverResolution() {
        return coverResolution;
    }

    public void setCoverResolution(int coverResolution) {
        this.coverResolution = coverResolution;
    }

    public boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getDataPath() {
        return pathToData;
    }

    public void setDataPath(String pathToData) {
        this.pathToData = pathToData;
    }

    public static Settings readSettings(){
        Settings settingsFromJson = new Settings();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("data\\settings.json")) {
            settingsFromJson = gson.fromJson(reader, Settings.class);
            Log.info("Loaded settings from \"data\\settings.json\".");
        } catch (IOException e) {
            Log.error(e.getMessage());
        }
        return settingsFromJson;
    }

    public static void writeSettings(Settings pSettings){
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("data\\settings.json")) {
            gson.toJson(pSettings, writer);
            Log.info("Saved settings to \"data/settings.json\".");
        } catch (IOException e) {
            Log.error(e.getMessage());
        }
    }
}
