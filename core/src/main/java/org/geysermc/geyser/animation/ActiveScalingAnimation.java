package org.geysermc.geyser.animation;

import org.geysermc.geyser.entity.type.Entity;
import org.geysermc.geyser.session.GeyserSession;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;

public class ActiveScalingAnimation {
    private final long entityGeyserId;
    private final ScalingAnimation animation;
    private int currentStepIndex;
    private int ticksElapsedInStep;
    private float scaleAtStartOfCurrentStep;

    public ActiveScalingAnimation(long entityGeyserId, ScalingAnimation animation, float initialScale) {
        this.entityGeyserId = entityGeyserId;
        this.animation = animation;
        this.currentStepIndex = 0;
        this.ticksElapsedInStep = 0;
        this.scaleAtStartOfCurrentStep = initialScale;
    }

    /**
     * Advances the animation by one tick.
     *
     * @param session The GeyserSession.
     * @return true if the animation has completed, false otherwise.
     */
    public boolean tick(GeyserSession session) {
        if (animation.isEmpty() || currentStepIndex >= animation.getSteps().size()) {
            return true; // Animation is empty or already finished
        }

        Entity entity = session.getEntityCache().getEntityByGeyserId(this.entityGeyserId);
        if (entity == null || !entity.isValid()) {
            // Entity is no longer valid, stop animation
            return true;
        }

        ScalingAnimationStep currentStep = animation.getSteps().get(currentStepIndex);
        ticksElapsedInStep++;

        float progress = (float) ticksElapsedInStep / currentStep.durationTicks();
        progress = Math.min(1.0f, progress); // Cap progress at 1.0

        float newScale = lerp(scaleAtStartOfCurrentStep, currentStep.targetScale(), progress);
        entity.getDirtyMetadata().put(EntityDataTypes.SCALE, newScale);
        entity.updateBedrockMetadata();

        if (ticksElapsedInStep >= currentStep.durationTicks()) {
            // Current step finished, move to next step
            currentStepIndex++;
            ticksElapsedInStep = 0;
            scaleAtStartOfCurrentStep = currentStep.targetScale(); // Start of next step is target of current
            if (currentStepIndex >= animation.getSteps().size()) {
                // All steps finished
                return true;
            }
        }
        return false; // Animation still ongoing
    }

    private static float lerp(float start, float end, float progress) {
        return start + progress * (end - start);
    }

    public long getEntityGeyserId() {
        return entityGeyserId;
    }
}
