package de.suzufa.screwbox.playground.debo.enemies.slime;

import static de.suzufa.screwbox.playground.debo.Asset.asset;
import static de.suzufa.screwbox.playground.debo.Asset.spriteAsset;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TimeoutComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.Asset;
import de.suzufa.screwbox.playground.debo.Preload;
import de.suzufa.screwbox.playground.debo.components.CastShadowComponent;
import de.suzufa.screwbox.playground.debo.components.KillZoneComponent;
import de.suzufa.screwbox.playground.debo.components.KilledFromAboveComponent;

public class SlimeDeadState implements EntityState {

    static {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Preload
    static Asset<Sprite> sprite = spriteAsset("bla");
    static Asset<Sound> sound = asset(() -> Sound.fromFile("bla"));

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(final Entity entity, Engine engine) {
        entity.get(SpriteComponent.class).sprite = SlimeResources.DEAD_SPRITE.newInstance();
        entity.remove(KillZoneComponent.class);
        entity.remove(CastShadowComponent.class);
        entity.remove(CastShadowComponent.class);
        entity.remove(KilledFromAboveComponent.class);
        entity.get(PhysicsBodyComponent.class).ignoreCollisions = true;
        entity.add(new TimeoutComponent(Time.now().plusSeconds(2)));
        engine.audio().playEffect(SlimeResources.KILL_SOUND);
    }

    @Override
    public EntityState update(final Entity entity, Engine engine) {
        return this;
    }

}
