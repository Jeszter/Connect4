package sk.tuke.gamestudio.game.game;

import java.util.HashMap;
import java.util.Map;

public class SkinData {
    public static final Map<String, Map<String, String>> SKIN_COLORS = new HashMap<>();

    static {
        // PLAYER 1 SKINS
        SKIN_COLORS.put("classicP1", Map.of(
                "light", "#f1c40f", "dark", "#d4ac0d", "border", "#b7950b"
        ));
        SKIN_COLORS.put("violetP1", Map.of(
                "light", "#6c5ce7", "dark", "#4834d4", "border", "#341f97"
        ));
        SKIN_COLORS.put("emeraldP1", Map.of(
                "light", "#2ecc71", "dark", "#27ae60", "border", "#1e8449"
        ));
        SKIN_COLORS.put("obsidianP1", Map.of(
                "light", "#3a3a3a", "dark", "#1c1c1c", "border", "#111111"
        ));
        SKIN_COLORS.put("silverP1", Map.of(
                "light", "#C0C0C0", "dark", "#A9A9A9", "border", "#808080"
        ));
        SKIN_COLORS.put("arcaneP1", Map.of(
                "light", "#B10DC9", "dark", "#6A0DAD", "border", "#8e44ad"
        ));
        SKIN_COLORS.put("cotton-candyP1", Map.of(
                "light", "#FFB6C1", "dark", "#87CEFA", "border", "#ff69b4"
        ));

        // PLAYER 2 SKINS
        SKIN_COLORS.put("classicP2", Map.of(
                "light", "#e74c3c", "dark", "#c0392b", "border", "#922b21"
        ));
        SKIN_COLORS.put("phantomP2", Map.of(
                "light", "#663399", "dark", "#4B0082", "border", "#301934"
        ));
        SKIN_COLORS.put("campfireP2", Map.of(
                "light", "#FFD700", "dark", "#FF4500", "border", "#cc3300"
        ));
        SKIN_COLORS.put("shadowP2", Map.of(
                "light", "#2c3e50", "dark", "#000000", "border", "#1a1a1a"
        ));
        SKIN_COLORS.put("platinumP2", Map.of(
                "light", "#e5e4e2", "dark", "#c0c0c0", "border", "#a9a9a9"
        ));
        SKIN_COLORS.put("sapphireP2", Map.of(
                "light", "#1E90FF", "dark", "#0F52BA", "border", "#0b3d91"
        ));
        SKIN_COLORS.put("rubyP2", Map.of(
                "light", "#C72C41", "dark", "#9B111E", "border", "#800000"
        ));
    }
}
