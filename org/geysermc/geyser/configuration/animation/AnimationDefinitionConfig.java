package org.geysermc.geyser.configuration.animation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class AnimationDefinitionConfig {
    // This name will be the key in the map, e.g., "pulse_fast"
    // Not a field in the class itself, but used as map key in main config.
    private List<AnimationStepConfig> steps = new ArrayList<>();
}
