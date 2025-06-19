package org.geysermc.geyser.entity.type.model;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket; // Changed from AddEntityPacket
import org.geysermc.geyser.entity.EntityDefinition;
import org.geysermc.geyser.entity.type.Entity; // Base class
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.skin.SkinManager; // For potential skin geometry data if needed
// Imports needed for AddPlayerPacket fields
import org.cloudburstmc.protocol.bedrock.data.PlayerPermission;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.cloudburstmc.protocol.bedrock.data.command.CommandPermission;
import org.geysermc.geyser.entity.type.player.PlayerEntity; // For BASE_ABILITY_LAYER

import java.util.UUID;

/**
 * Represents an entity that appears as a player on Bedrock but uses a custom model
 * defined in a resource pack, referenced by a skin/model ID.
 */
public class CustomModelPlayerEntity extends Entity {

    // Store the custom model identifier (e.g., a string or int for SKIN_ID or VARIANT)
    private int customModelVariantId;

    public CustomModelPlayerEntity(GeyserSession session, int entityId, long geyserId, UUID uuid,
                                 EntityDefinition<?> definition, Vector3f position, Vector3f motion,
                                 float yaw, float pitch, float headYaw, int customModelVariantId) {
        super(session, entityId, geyserId, uuid, definition, position, motion, yaw, pitch, headYaw);
        this.customModelVariantId = customModelVariantId;
        // Ensure some player-like flags are set if necessary
        // For example, players are generally not affected by gravity in the same way as some objects
        // setFlag(EntityFlag.HAS_GRAVITY, false); // Example, might need adjustment
    }

    @Override
    protected void initializeMetadata() {
        super.initializeMetadata();

        // Set the custom model variant ID
        dirtyMetadata.put(EntityDataTypes.VARIANT, this.customModelVariantId);

        // Player-like entities might need specific flags or default metadata
        dirtyMetadata.put(EntityDataTypes.NAMETAG_ALWAYS_SHOW, (byte) 1);
        setFlag(EntityFlag.CAN_SHOW_NAME, true);
    }

    @Override
    public void spawnEntity() {
        // This entity should be spawned using AddPlayerPacket to appear as a player
        AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
        addPlayerPacket.setUuid(this.uuid != null ? this.uuid : UUID.randomUUID()); // Needs a UUID
        // Username could be configurable or based on the custom model ID, or even empty if not needed
        addPlayerPacket.setUsername("model_" + this.customModelVariantId);
        addPlayerPacket.setRuntimeEntityId(this.geyserId);
        addPlayerPacket.setUniqueEntityId(this.geyserId); // Should be same as runtime ID for players

        // Adjust position by definition offset if needed, like PlayerEntity does, or handle centrally
        // Vector3f spawnPosition = position.sub(0, definition.offset(), 0); // Typical player offset
        // For now, use raw position, offset can be tuned in definition
        addPlayerPacket.setPosition(this.position);
        addPlayerPacket.setRotation(getBedrockRotation());
        addPlayerPacket.setMotion(this.motion);

        // Default Bedrock fields for AddPlayerPacket
        // Many of these won't matter for a static model but are required by the packet
        addPlayerPacket.setHand(SkinManager.EMPTY_ITEM); // Empty hand
        addPlayerPacket.getAdventureSettings().setCommandPermission(CommandPermission.ANY); // Default
        addPlayerPacket.getAdventureSettings().setPlayerPermission(PlayerPermission.MEMBER); // Default
        addPlayerPacket.setDeviceId(""); // Empty
        addPlayerPacket.setPlatformChatId(""); // Empty
        addPlayerPacket.setGameType(GameType.SURVIVAL); // Doesn't really matter for a model
        addPlayerPacket.setAbilityLayers(PlayerEntity.BASE_ABILITY_LAYER); // Use PlayerEntity's default

        // Apply initial metadata (including our custom SKIN_ID/VARIANT)
        dirtyMetadata.apply(addPlayerPacket.getMetadata());
        addPlayerPacket.getMetadata().putFlags(this.flags); // Apply flags

        setFlagsDirty(false);
        valid = true;
        session.sendUpstreamPacket(addPlayerPacket);

        // session.getGeyser().getLogger().debug("Spawned CustomModelPlayerEntity " + customModelId + " as player.");
    }

    // Getter for customModelId if needed elsewhere
    public int getCustomModelVariantId() {
        return customModelVariantId;
    }

    // Setter if the model ID can be changed post-spawn (might require re-sending metadata)
    public void setCustomModelVariantId(int modelVariantId) {
        if (this.customModelVariantId != modelVariantId) {
            this.customModelVariantId = modelVariantId;
            dirtyMetadata.put(EntityDataTypes.VARIANT, this.customModelVariantId);
            updateBedrockMetadata(); // Send the update to the client
        }
    }
}
