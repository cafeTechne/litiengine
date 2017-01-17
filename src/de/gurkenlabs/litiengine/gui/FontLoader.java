/***************************************************************
 * Copyright (c) 2014 - 2015 , gurkenlabs, All rights reserved *
 ***************************************************************/
package de.gurkenlabs.litiengine.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Logger;

import de.gurkenlabs.util.io.FileUtilities;

// TODO: Auto-generated Javadoc
/**
 * The Class FontLoader.
 */
public class FontLoader {
  private static final Logger log = Logger.getLogger(FontLoader.class.getName());
  /** The Constant fonts. */
  private static final HashMap<String, Font> fonts = new HashMap<String, Font>();

  /**
   * Gets the font.
   *
   * @param fontName
   *          the font name
   * @param fontSize
   *          the font size
   * @return the font
   */
  public static Font load(final String fontName) {
    if (fonts.containsKey(fontName)) {
      return fonts.get(fontName);
    }

    String[] f = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (String font : f) {
      String name = FileUtilities.getFileName(fontName);
      if (font.equals(name)) {
        return new Font(name, Font.PLAIN, 8);
      }
    }
    try {
      final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

      InputStream fontStream = FileUtilities.getGameResource(fontName);
      if (fontStream == null) {
        log.severe("font '" + fontName + "' could not be loaded");
      }

      final Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
      ge.registerFont(font);
      fonts.put(fontName, font);
      return font;
    } catch (final FontFormatException e) {
      final StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      final String stacktrace = sw.toString();
      log.severe(stacktrace);
      e.printStackTrace();
    } catch (final IOException e) {
      final StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      final String stacktrace = sw.toString();
      log.severe(stacktrace);
      e.printStackTrace();
    }
    return null;
  }
}
