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
