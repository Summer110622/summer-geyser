package org.geysermc.geyser.entity.definitions;

import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.entity.EntityDefinitions; // Assuming this is where definitions are registered
import org.geysermc.geyser.entity.type.model.CustomModelPlayerEntity;
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType; // For Java entity type
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type.IntEntityMetadata;


public class CustomModelPlayerDefinition {

    // This is a placeholder for how it might be registered.
    // The actual Java EntityType to map from would be decided (e.g., Marker, ArmorStand with specific tag).
    // For now, let's imagine a hypothetical EntityType.CUSTOM_MODEL_MARKER

    // public static final EntityDefinition<CustomModelPlayerEntity> DEFINITION =
    //         EntityDefinition.builder(CustomModelPlayerEntity::new) // Pass the constructor
    //                 .entityType(EntityType.MARKER) // EXAMPLE: What Java entity type this might map from
    //                 .identifier("minecraft:player") // CRUCIAL: Bedrock identifier
    //                 .height(1.8f) // Player-like default height
    //                 .width(0.6f)  // Player-like default width
    //                 .offset(1.62f) // Player-like default offset
    //                 // No specific Java metadata translators needed unless we map a Java metadata
    //                 // to the customModelId. For now, customModelId is passed in constructor.
    //                 .build(false); // 'false' if not a direct mapping from a standard Java EntityType

    /**
     * This method would be called during Geyser's startup to register definitions.
     * The key challenge is how to decide that a specific Java entity instance
     * should use THIS definition instead of its default one (e.g. default Marker definition).
     * This might involve a custom dispatcher or checking entity NBT/tags.
     *
     * For now, this class just illustrates the *structure* of the definition.
     * The actual registration logic is more complex.
     *
     * One approach for providing customModelId via metadata:
     * If the Java marker entity had an IntMetadata at a specific index (e.g., index 20)
     * that we designated as 'custom_model_id_index'.
     *
     * .addTranslator(MetadataType.INT, (entity, metadata) -> {
     *      if (metadata.getId() == CUSTOM_MODEL_ID_METADATA_INDEX) {
     *          entity.setCustomModelId(String.valueOf(metadata.getValue()));
     *          // And then update EntityDataTypes.SKIN_ID or VARIANT
     *          entity.getDirtyMetadata().put(EntityDataTypes.SKIN_ID, metadata.getValue());
     *      }
     *  })
     */

    public static EntityDefinition<CustomModelPlayerEntity> buildDefinition(String javaEntityIdentifierToMapFrom) {
        // A factory method that might be used by a more dynamic registration system.
        // The 'javaEntityIdentifierToMapFrom' isn't directly used by EntityDefinition builder
        // but signals intent for what this definition is for.

        // For demonstration, let's assume customModelId is passed via constructor for now.
        // If it were from metadata, a translator would be added.

        return EntityDefinition.builder((session, entityId, geyserId, uuid, definition, position, motion, yaw, pitch, headYaw) ->
                    new CustomModelPlayerEntity(session, entityId, geyserId, uuid, definition, position, motion, yaw, pitch, headYaw, "default_model_id") // Default ID
                )
                .identifier("minecraft:player") // Bedrock identifier
                .height(1.8f) // Player-like height
                .width(0.6f)  // Player-like width
                .offset(1.62f) // Player-like offset (eye height)
                // .type(EntityType.MARKER) // If we were strictly mapping one Java type
                .build(false); // Not a standard mapping, so don't auto-register against a specific EntityType here
    }
}
