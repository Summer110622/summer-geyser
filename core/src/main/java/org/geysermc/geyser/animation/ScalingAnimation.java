package org.geysermc.geyser.animation;

import java.util.List;
import java.util.Collections;

/**
 * Defines a complete scaling animation as a sequence of steps.
 * This object is immutable.
 */
public class ScalingAnimation {
    private final List<ScalingAnimationStep> steps;

    public ScalingAnimation(List<ScalingAnimationStep> steps) {
        if (steps == null || steps.isEmpty()) {
            this.steps = Collections.emptyList();
        } else {
            this.steps = List.copyOf(steps); // Make immutable
        }
    }

    public List<ScalingAnimationStep> getSteps() {
        return steps;
    }

    public boolean isEmpty() {
        return steps.isEmpty();
    }
}
