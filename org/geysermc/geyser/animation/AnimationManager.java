package org.geysermc.geyser.animation;

import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Iterator;

public class AnimationManager {
    private final Long2ObjectMap<ActiveScalingAnimation> activeScalingAnimations = new Long2ObjectOpenHashMap<>();

    /**
     * Called periodically to update all active animations.
     * Should be called by GeyserSession's tick or a global tick handler.
     */
    public void tick(GeyserSession session) {
        if (activeScalingAnimations.isEmpty()) {
            return;
        }

        Iterator<Long2ObjectMap.Entry<ActiveScalingAnimation>> iterator = activeScalingAnimations.long2ObjectEntrySet().iterator();
        while (iterator.hasNext()) {
            Long2ObjectMap.Entry<ActiveScalingAnimation> entry = iterator.next();
            ActiveScalingAnimation activeAnimation = entry.getValue();
            if (activeAnimation.tick(session)) {
                iterator.remove(); // Animation finished or entity invalid
            }
        }
    }

    /**
     * Starts a new scaling animation for an entity.
     * If an animation is already running for the entity, it's replaced.
     * @param session The GeyserSession the entity belongs to.
     * @param entity The entity to animate.
     * @param animation The ScalingAnimation definition.
     */
    public void startScalingAnimation(GeyserSession session, Entity entity, ScalingAnimation animation) {
        if (entity == null || animation == null || animation.isEmpty()) {
            return;
        }

        // Read current scale from metadata if available, otherwise default to 1.0f
        // EntityDataTypes.SCALE should ideally always be there after initialization.
        float initialScale = entity.getDirtyMetadata().getOrDefault(EntityDataTypes.SCALE, 1.0f);

        ActiveScalingAnimation activeAnimation = new ActiveScalingAnimation(entity.getGeyserId(), animation, initialScale);
        activeScalingAnimations.put(entity.getGeyserId(), activeAnimation);
    }

    /**
     * Stops a scaling animation for a specific entity.
     * @param entityGeyserId The Geyser ID of the entity.
     */
    public void stopScalingAnimation(long entityGeyserId) {
        activeScalingAnimations.remove(entityGeyserId);
    }
}
