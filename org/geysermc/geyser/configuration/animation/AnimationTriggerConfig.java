package org.geysermc.geyser.configuration.animation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimationTriggerConfig {
    private String type; // e.g., "interaction", "spawn"
    @JsonProperty("entity-type")
    private String entityType; // Java EntityType name, e.g., "ARMOR_STAND"
    @JsonProperty("interaction-type")
    private String interactionType; // "ATTACK" or "INTERACT", only for "interaction" type
    @JsonProperty("animation-name")
    private String animationName; // Name referencing a key in AnimationDefinitionConfig map
}
