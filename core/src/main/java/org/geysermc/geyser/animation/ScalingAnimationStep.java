package org.geysermc.geyser.animation;

/**
 * Represents one step in a scaling animation.
 * @param durationTicks The time in server ticks this step should last.
 * @param targetScale The scale the entity should reach by the end of this step.
 */
public record ScalingAnimationStep(int durationTicks, float targetScale) {
}
