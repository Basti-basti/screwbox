package de.suzufa.screwbox.playground.debo.specials.player;

import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.tiled.Tileset;

@Deprecated
public class PlayerResources {

    private PlayerResources() {
    } // hide constructor

    private static final Tileset TILESET = Tileset.fromJson("tilesets/specials/player.json");

    public static final Sprite RUNNING_SPRITE = TILESET.findByName("running");
    public static final Sprite JUMPING_SPRITE = TILESET.findByName("jumping");
    public static final Sprite IDLE_SPRITE = TILESET.findByName("idle");
    public static final Sprite DIGGING_SPRITE = TILESET.findByName("digging");

    public static final Sound OUCH_SOUND = Sound.fromFile("sounds/ouch.wav");
    public static final Sound BLUPP_SOUND = Sound.fromFile("sounds/blupp.wav");
    public static final Sound ZISCH_SOUND = Sound.fromFile("sounds/zisch.wav");
    public static final Sound JUMP_SOUND = Sound.fromFile("sounds/jump.wav");

}
