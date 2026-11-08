package com.equiel1703;

public class Launcher {
  public static void main(String[] args) {
    // -- These settings here are to help the fonts and shapes to have a sharp 'retro' feel --

    // Disable RGB subpixel antialiasing
    System.setProperty("prism.lcdtext", "false");
    // Force glyphs to snap to whole integer X/Y pixel coordinates
    System.setProperty("prism.subpixeltext", "false");
    // Prevent fractional desktop DPI scaling from blurring the scene
    System.setProperty("glass.gtk.uiScale", "1.0");
    
    PONTE.main(args);
  }
}
