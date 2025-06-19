package org.geysermc.geyser.animation;

import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.geysermc.geyser.configuration.GeyserConfiguration;
import org.geysermc.geyser.configuration.animation.AnimationDefinitionConfig;
import org.geysermc.geyser.configuration.animation.AnimationStepConfig;

public class AnimationManager {
    private final Long2ObjectMap<ActiveScalingAnimation> activeScalingAnimations = new Long2ObjectOpenHashMap<>();
    private final Map<String, ScalingAnimation> definedAnimations = new HashMap<>();

    public AnimationManager(GeyserConfiguration config) {
        loadAnimationsFromConfig(config);
    }

    private void loadAnimationsFromConfig(GeyserConfiguration config) {
        if (config.getAnimations() == null) return;
        for (Map.Entry<String, AnimationDefinitionConfig> entry : config.getAnimations().entrySet()) {
            String name = entry.getKey();
            AnimationDefinitionConfig defConfig = entry.getValue();
            if (defConfig.getSteps() != null) {
                List<ScalingAnimationStep> steps = defConfig.getSteps().stream()
                        .map(stepConfig -> new ScalingAnimationStep(stepConfig.getDurationTicks(), stepConfig.getTargetScale()))
                        .collect(Collectors.toList());
                this.definedAnimations.put(name, new ScalingAnimation(steps));
            }
        }
    }

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
     * Starts a new scaling animation for an entity by name.
     * If an animation is already running for the entity, it's replaced.
     * @param session The GeyserSession the entity belongs to.
     * @param entity The entity to animate.
     * @param animationName The name of the ScalingAnimation definition.
     */
    public void startScalingAnimation(GeyserSession session, Entity entity, String animationName) {
        ScalingAnimation animation = definedAnimations.get(animationName);
        if (entity == null || animation == null || animation.isEmpty()) {
            // session.getGeyser().getLogger().debug("Animation not found or empty: " + animationName);
            return;
        }
        // The rest of startScalingAnimation remains the same
        float initialScale = entity.getDirtyMetadata().getOrDefault(EntityDataTypes.SCALE, 1.0f);
        ActiveScalingAnimation activeAnimation = new ActiveScalingAnimation(entity.getGeyserId(), animation, initialScale);
        activeScalingAnimations.put(entity.getGeyserId(), activeAnimation);
    }

    /**
     * Starts a new scaling animation for an entity using a direct animation object.
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

        ActiveScalingAnimation activeAnimation = new ActiveScalingAnimation(entity.getGeyserId(), animation, initialLScale);
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
