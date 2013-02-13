/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.LanguageAPIExamplePlugin;

import me.xhawk87.LanguageAPI.Language;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * LanguageWrapper
 * 
 * This can be used in exactly the same way as the Language class from the 
 * LanguageAPI plugin, however it will still work (in the default language only)
 * if the LanguageAPI.jar is not installed.
 *
 * @author XHawk87
 */
public class LanguageWrapper {

    /**
     * Your plugin
     */
    private Plugin plugin;
    /**
     * A generalised object for holding the language library if it exists
     */
    private Object langObj;

    /**
     * Creates a new LanguageWrapper for the LanguageAPI plugin
     * 
     * @param plugin Your plugin
     */
    public LanguageWrapper(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * A wrapper for the Language.get method. It checks if the LanguageAPI is
     * installed, and if it is not, returns the default string instead.
     * 
     * @param key The template key
     * @param template The default template for the plugin
     * @param params The parameters to be inserted
     * @return The formatted string
     */
    public String get(String key, String template, Object... params) {
        if (langObj == null) {
            if (Bukkit.getPluginManager().isPluginEnabled("LanguageAPI")) {
                langObj = new Language(plugin);
            }
        }
        if (langObj != null) {
            Language language = (Language) langObj;
            return language.get(key, template, params);
        } else {
            return compile(template, params);
        }
    }

    /**
     * Taken directly from the Language class of the LanguageAPI plugin.
     *
     * Inserts the given parameters into the template at the correct locations
     * and returns the formatted string
     *
     * @param template The string template
     * @param params The dynamic data to be inserted
     * @return The formatted string
     * @throws IllegalArgumentException If template tries to reference a
     * parameter index that does not exist
     */
    private static String compile(String template, Object[] params) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);
            if (c == '{') {
                try {
                    int endIndex = template.indexOf('}', i);
                    if (endIndex != -1) {
                        int param = Integer.parseInt(template.substring(i + 1, endIndex));
                        if (param >= params.length) {
                            throw new IllegalArgumentException();
                        }
                        sb.append(params[param].toString());
                        i = endIndex;
                        continue;
                    }
                } catch (NumberFormatException ex) {
                    // then read it as it is
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
