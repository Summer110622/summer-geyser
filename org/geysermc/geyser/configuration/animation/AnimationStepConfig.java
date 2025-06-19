package org.geysermc.geyser.configuration.animation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimationStepConfig {
    @JsonProperty("duration-ticks")
    private int durationTicks = 10;
    @JsonProperty("target-scale")
    private float targetScale = 1.0f;
}
