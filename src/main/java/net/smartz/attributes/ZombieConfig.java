package net.smartz.attributes;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;

public class ZombieConfig {

    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue PROGRESSION_ENABLED;
    public static final ForgeConfigSpec.DoubleValue MAX_HEALTH;
    public static final ForgeConfigSpec.DoubleValue FOLLOW_RANGE;
    public static final ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE;
    public static final ForgeConfigSpec.DoubleValue MOVEMENT_SPEED;
    public static final ForgeConfigSpec.DoubleValue ATTACK_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ATTACK_KNOCKBACK;
    public static final ForgeConfigSpec.DoubleValue ARMOR;
    public static final ForgeConfigSpec.DoubleValue ARMOR_TOUGHNESS;
    public static final ForgeConfigSpec.DoubleValue SPAWN_REINFORCEMENTS_CHANCE;

    public static final ForgeConfigSpec.DoubleValue SPEED_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue HEALTH_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue DAMAGE_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue FOLLOW_RANGE_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue KNOCKBACK_RESISTANCE_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue ATTACK_KNOCKBACK_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue ARMOR_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue ARMOR_TOUGHNESS_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue SPAWN_REINFORCEMENTS_CHANCE_PER_DAY;
    public static final ForgeConfigSpec.IntValue START_DAY;

    public static final ForgeConfigSpec.BooleanValue SHIELD_ENABLED;
    public static final ForgeConfigSpec.DoubleValue SHIELD_CHANCE;
    public static final ForgeConfigSpec.BooleanValue SHIELD_BLOCK_ENABLED;
    public static final ForgeConfigSpec.IntValue SHIELD_BLOCK_TIME;
    public static final ForgeConfigSpec.IntValue SHIELD_BLOCK_TIME_PER_DAY;
    public static final ForgeConfigSpec.DoubleValue SHIELD_BLOCK_CHANCE;
    public static final ForgeConfigSpec.DoubleValue SHIELD_BLOCK_CHANCE_PER_DAY;
    public static final ForgeConfigSpec.IntValue SHIELD_REACTION_DELAY;
    public static final ForgeConfigSpec.DoubleValue SHIELD_REACTION_DELAY_PER_DAY;

    public static final ForgeConfigSpec.BooleanValue FLINT_ENABLED;
    public static final ForgeConfigSpec.DoubleValue FLINT_CHANCE;
    public static final ForgeConfigSpec.IntValue FLINT_COOLDOWN;
    public static final ForgeConfigSpec.DoubleValue FLINT_RANGE;

    public static final ForgeConfigSpec.BooleanValue BLOCK_BREAK_ENABLED;

    public static final ForgeConfigSpec.DoubleValue BLOCK_BREAK_SPEED;

    public static final ForgeConfigSpec.IntValue BLOCK_BREAK_RANGE;

    public static final ForgeConfigSpec.DoubleValue PICKAXE_CHANCE;

    public static final ForgeConfigSpec.DoubleValue WOODEN_PICKAXE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue STONE_PICKAXE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue IRON_PICKAXE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue GOLD_PICKAXE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue DIAMOND_PICKAXE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue NETHERITE_PICKAXE_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>>
            UNBREAKABLE_BLOCKS;

    public static final ForgeConfigSpec.BooleanValue SPECIAL_ZOMBIE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue SPECIAL_ZOMBIE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue SPECIAL_ZOMBIE_SPEED;

    public static final ForgeConfigSpec.BooleanValue SPECIAL_ZOMBIE_DODGE_ENABLED;
    public static final ForgeConfigSpec.DoubleValue SPECIAL_ZOMBIE_DODGE_CHANCE;
    public static final ForgeConfigSpec.DoubleValue SPECIAL_ZOMBIE_DODGE_SPEED;
    public static final ForgeConfigSpec.IntValue SPECIAL_ZOMBIE_DODGE_COOLDOWN;
    public static final ForgeConfigSpec.IntValue SPECIAL_ZOMBIE_DODGE_DURATION;
    public static final ForgeConfigSpec.DoubleValue SPECIAL_ZOMBIE_DODGE_DISTANCE;
    public static final ForgeConfigSpec.BooleanValue SPECIAL_ZOMBIE_PARTICLE;

    public static final ForgeConfigSpec.IntValue ZOMBIE_NORMAL_CHANCE;
    public static final ForgeConfigSpec.IntValue ZOMBIE_DODGE_CHANCE;
    public static final ForgeConfigSpec.IntValue ZOMBIE_SHIELD_CHANCE;
    public static final ForgeConfigSpec.IntValue ZOMBIE_FLINT_CHANCE;
    public static final ForgeConfigSpec.IntValue ZOMBIE_BREAK_BLOCK_CHANCE;

    static {

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("zombie");

// ============================================================
// ZOMBIE ATTRIBUTES
// ============================================================

        MAX_HEALTH = builder
                .comment("Zombie maximum health. Higher values make zombies harder to kill.")
                .defineInRange(
                        "maxHealth",
                        20.0D,
                        1.0D,
                        1024.0D
                );

        FOLLOW_RANGE = builder
                .comment("Range in blocks where the Zombie can detect and follow targets.")
                .defineInRange(
                        "followRange",
                        32.0D,
                        0.0D,
                        2048.0D
                );

        KNOCKBACK_RESISTANCE = builder
                .comment("Zombie resistance to knockback. 0.0 = no resistance, 1.0 = immune to knockback.")
                .defineInRange(
                        "knockbackResistance",
                        0.0D,
                        0.0D,
                        1.0D
                );

        MOVEMENT_SPEED = builder
                .comment("Zombie movement speed.")
                .defineInRange(
                        "movementSpeed",
                        0.30D,
                        0.0D,
                        1024.0D
                );

        ATTACK_DAMAGE = builder
                .comment("Damage dealt by Zombie attacks.")
                .defineInRange(
                        "attackDamage",
                        2.0D,
                        0.0D,
                        2048.0D
                );

        ATTACK_KNOCKBACK = builder
                .comment("Additional horizontal knockback caused by Zombie attacks.")
                .defineInRange(
                        "attackKnockback",
                        0.0D,
                        0.0D,
                        5.0D
                );

        ARMOR = builder
                .comment("Zombie armor points. Higher values reduce incoming damage.")
                .defineInRange(
                        "armor",
                        0.0D,
                        0.0D,
                        30.0D
                );

        ARMOR_TOUGHNESS = builder
                .comment("Zombie armor toughness. Higher values improve protection against strong attacks.")
                .defineInRange(
                        "armorToughness",
                        0.0D,
                        0.0D,
                        20.0D
                );

        SPAWN_REINFORCEMENTS_CHANCE = builder
                .comment("Chance for a Zombie to summon reinforcements when attacked. 0.0 = 0%, 1.0 = 100%.")
                .defineInRange(
                        "spawnReinforcementsChance",
                        0.0D,
                        0.0D,
                        1.0D
                );

        builder.pop();

        builder.push("progression");

// ============================================================
// PROGRESSION
// ============================================================

        START_DAY = builder
                .comment("The day when Zombie progression starts.")
                .defineInRange(
                        "startDay",
                        1,
                        0,
                        100000
                );

        PROGRESSION_ENABLED = builder
                .comment("Enable or disable Zombie attribute progression based on world days.")
                .define(
                        "enabled",
                        true
                );

        SPEED_PER_DAY = builder
                .comment("Additional Zombie movement speed gained per day.")
                .defineInRange(
                        "speedPerDay",
                        0.002D,
                        0.0D,
                        10.0D
                );

        HEALTH_PER_DAY = builder
                .comment("Additional Zombie maximum health gained per day.")
                .defineInRange(
                        "healthPerDay",
                        0.1D,
                        0.0D,
                        100.0D
                );

        DAMAGE_PER_DAY = builder
                .comment("Additional Zombie attack damage gained per day.")
                .defineInRange(
                        "damagePerDay",
                        0.05D,
                        0.0D,
                        100.0D
                );

        FOLLOW_RANGE_PER_DAY = builder
                .comment("Additional Zombie follow range gained per day.")
                .defineInRange(
                        "followRangePerDay",
                        0.2D,
                        0.0D,
                        100.0D
                );

        KNOCKBACK_RESISTANCE_PER_DAY = builder
                .comment("Additional Zombie knockback resistance gained per day.")
                .defineInRange(
                        "knockbackResistancePerDay",
                        0.01D,
                        0.0D,
                        1.0D
                );

        ATTACK_KNOCKBACK_PER_DAY = builder
                .comment("Additional Zombie attack knockback gained per day.")
                .defineInRange(
                        "attackKnockbackPerDay",
                        0.01D,
                        0.0D,
                        5.0D
                );

        ARMOR_PER_DAY = builder
                .comment("Additional Zombie armor gained per day.")
                .defineInRange(
                        "armorPerDay",
                        0.05D,
                        0.0D,
                        30.0D
                );

        ARMOR_TOUGHNESS_PER_DAY = builder
                .comment("Additional Zombie armor toughness gained per day.")
                .defineInRange(
                        "armorToughnessPerDay",
                        0.05D,
                        0.0D,
                        20.0D
                );

        SPAWN_REINFORCEMENTS_CHANCE_PER_DAY = builder
                .comment("Additional reinforcement summon chance gained per day. 0.01 = 1% per day.")
                .defineInRange(
                        "spawnReinforcementsChancePerDay",
                        0.005D,
                        0.0D,
                        1.0D
                );

        builder.pop();

        builder.push("shield");


// ============================================================
// SHIELD
// ============================================================

        SHIELD_ENABLED = builder
                .comment("Enable or disable the Zombie shield skill.")
                .define(
                        "shield_enabled",
                        true
                );

        SHIELD_CHANCE = builder
                .comment("Legacy shield chance. 0.0 = 0%, 1.0 = 100%.")
                .defineInRange(
                        "shield_chance",
                        0.20D,
                        0.0D,
                        1.0D
                );

        SHIELD_BLOCK_ENABLED = builder
                .comment("Enable or disable the Zombie shield blocking behavior.")
                .define(
                        "shield_blockEnabled",
                        true
                );

        SHIELD_BLOCK_TIME = builder
                .comment("Base duration in ticks that the Zombie keeps its shield raised.")
                .defineInRange(
                        "shield_blockTime",
                        60,
                        1,
                        200
                );

        SHIELD_BLOCK_TIME_PER_DAY = builder
                .comment("Additional shield blocking time gained per day. 20 ticks = 1 second.")
                .defineInRange(
                        "shield_blockTimePerDay",
                        2,
                        0,
                        200
                );

        SHIELD_BLOCK_CHANCE = builder
                .comment("Base chance for the Zombie to block an incoming attack. 1.0 = 100%.")
                .defineInRange(
                        "shield_blockChance",
                        1.0D,
                        0.0D,
                        1.0D
                );

        SHIELD_BLOCK_CHANCE_PER_DAY = builder
                .comment("Additional shield block chance gained per day.")
                .defineInRange(
                        "shield_blockChancePerDay",
                        0.01D,
                        0.0D,
                        1.0D
                );

        SHIELD_REACTION_DELAY = builder
                .comment("Base reaction delay before the Zombie raises its shield, in ticks.")
                .defineInRange(
                        "shield_reactionDelay",
                        10,
                        0,
                        100
                );

        SHIELD_REACTION_DELAY_PER_DAY = builder
                .comment("Change in shield reaction delay per day.")
                .defineInRange(
                        "shield_reactionDelayPerDay",
                        0.1D,
                        0.0D,
                        100.0D
                );

        builder.pop();

        builder.push("flint");

// ============================================================
// FLINT AND STEEL
// ============================================================

        FLINT_ENABLED = builder
                .comment("Enable or disable the Zombie Flint and Steel skill.")
                .define(
                        "flintAndSteel_enabled",
                        true
                );

        FLINT_CHANCE = builder
                .comment("Chance for a Zombie to use Flint and Steel. 0.0 = 0%, 1.0 = 100%.")
                .defineInRange(
                        "flintAndSteel_chance",
                        0.20D,
                        0.0D,
                        1.0D
                );

        FLINT_COOLDOWN = builder
                .comment("Cooldown between Flint and Steel attacks, in ticks. 20 ticks = 1 second.")
                .defineInRange(
                        "flintAndSteel_cooldown",
                        40,
                        1,
                        200
                );

        FLINT_RANGE = builder
                .comment("Maximum distance in blocks at which the Zombie can ignite the player.")
                .defineInRange(
                        "flintAndSteel_range",
                        2.0D,
                        1.0D,
                        2.0D
                );

        builder.pop();

        builder.push("destroyblock");

// ============================================================
// BLOCK BREAKING
// ============================================================

        UNBREAKABLE_BLOCKS = builder
                .comment(
                        "List of blocks that Zombies are not allowed to destroy. " +
                                "Use Minecraft resource IDs such as minecraft:bedrock."
                )
                .defineList(
                        "blockBreak_unbreakableBlocks",
                        List.of(
                                "minecraft:bedrock",
                                "minecraft:end_portal",
                                "minecraft:end_portal_frame",
                                "minecraft:command_block",
                                "minecraft:chain_command_block",
                                "minecraft:repeating_command_block"
                        ),
                        value -> value instanceof String
                );

        BLOCK_BREAK_ENABLED = builder
                .comment("Enable or disable the Zombie block breaking skill.")
                .define(
                        "blockBreak_enabled",
                        true
                );

        BLOCK_BREAK_SPEED = builder
                .comment("Zombie block breaking speed multiplier. Higher values make Zombies break blocks faster.")
                .defineInRange(
                        "blockBreak_speed",
                        1.0D,
                        0.1D,
                        100.0D
                );

        BLOCK_BREAK_RANGE = builder
                .comment("Maximum distance in blocks from the Zombie at which it can break a block.")
                .defineInRange(
                        "blockBreak_range",
                        2,
                        1,
                        5
                );

        PICKAXE_CHANCE = builder
                .comment("Chance for a Zombie to spawn with a pickaxe when using the block breaking skill.")
                .defineInRange(
                        "blockBreak_pickaxeChance",
                        0.30D,
                        0.0D,
                        1.0D
                );

        WOODEN_PICKAXE_CHANCE = builder
                .comment("Chance to select a wooden pickaxe.")
                .defineInRange(
                        "blockBreak_pickaxe_woodenChance",
                        0.40D,
                        0.0D,
                        1.0D
                );

        STONE_PICKAXE_CHANCE = builder
                .comment("Chance to select a stone pickaxe.")
                .defineInRange(
                        "blockBreak_pickaxe_stoneChance",
                        0.30D,
                        0.0D,
                        1.0D
                );

        IRON_PICKAXE_CHANCE = builder
                .comment("Chance to select an iron pickaxe.")
                .defineInRange(
                        "blockBreak_pickaxe_ironChance",
                        0.20D,
                        0.0D,
                        1.0D
                );

        GOLD_PICKAXE_CHANCE = builder
                .comment("Chance to select a golden pickaxe.")
                .defineInRange(
                        "blockBreak_pickaxe_goldChance",
                        0.05D,
                        0.0D,
                        1.0D
                );

        DIAMOND_PICKAXE_CHANCE = builder
                .comment("Chance to select a diamond pickaxe.")
                .defineInRange(
                        "blockBreak_pickaxe_diamondChance",
                        0.04D,
                        0.0D,
                        1.0D
                );

        NETHERITE_PICKAXE_CHANCE = builder
                .comment("Chance to select a netherite pickaxe.")
                .defineInRange(
                        "blockBreak_pickaxe_netheriteChance",
                        0.01D,
                        0.0D,
                        1.0D
                );

        builder.pop();

        builder.push("dodge");

// ============================================================
// SPECIAL ZOMBIE
// ============================================================

        SPECIAL_ZOMBIE_ENABLED = builder
                .comment("Enable or disable special Zombies.")
                .define(
                        "specialZombie_enabled",
                        true
                );

        SPECIAL_ZOMBIE_DODGE_ENABLED = builder
                .comment("Enable or disable the special Zombie dodge behavior.")
                .define(
                        "specialZombie_dodge_enabled",
                        true
                );

        SPECIAL_ZOMBIE_CHANCE = builder
                .comment("Legacy chance for a Zombie to become a special Zombie. 0.0 = 0%, 1.0 = 100%.")
                .defineInRange(
                        "specialZombie_chance",
                        0.50D,
                        0.0D,
                        1.0D
                );

        SPECIAL_ZOMBIE_SPEED = builder
                .comment("Movement speed of special Zombies.")
                .defineInRange(
                        "specialZombie_speed",
                        0.35D,
                        0.0D,
                        2.0D
                );

        SPECIAL_ZOMBIE_DODGE_CHANCE = builder
                .comment("Chance for a special Zombie to dodge when the player is looking at it. 0.0 = 0%, 1.0 = 100%.")
                .defineInRange(
                        "specialZombie_dodgeChance",
                        0.70D,
                        0.0D,
                        1.0D
                );

        SPECIAL_ZOMBIE_DODGE_SPEED = builder
                .comment("Speed used by the special Zombie while moving to a dodge position.")
                .defineInRange(
                        "specialZombie_dodgeSpeed",
                        1.8D,
                        0.1D,
                        5.0D
                );

        SPECIAL_ZOMBIE_DODGE_COOLDOWN = builder
                .comment("Cooldown between dodge actions, in ticks. 20 ticks = 1 second.")
                .defineInRange(
                        "specialZombie_dodgeCooldown",
                        10,
                        1,
                        200
                );

        SPECIAL_ZOMBIE_DODGE_DURATION = builder
                .comment("Duration of a dodge action, in ticks. 20 ticks = 1 second.")
                .defineInRange(
                        "specialZombie_dodgeDuration",
                        10,
                        1,
                        40
                );

        SPECIAL_ZOMBIE_PARTICLE = builder
                .comment("Show visual particles around special Zombies.")
                .define(
                        "specialZombie_particle",
                        true
                );

        SPECIAL_ZOMBIE_DODGE_DISTANCE = builder
                .comment("Base distance in blocks that a special Zombie moves sideways when dodging.")
                .defineInRange(
                        "specialZombie_dodgeDistance",
                        3.0D,
                        1.0D,
                        6.0D
                );

        builder.pop();

        builder.push("skill");

// ============================================================
// SKILL SELECTION
// ============================================================

        ZOMBIE_NORMAL_CHANCE = builder
                .comment("Weight for normal Zombies with no special skill.")
                .defineInRange(
                        "skills_normalChance",
                        30,
                        0,
                        100
                );

        ZOMBIE_DODGE_CHANCE = builder
                .comment("Weight for Zombies with the dodge skill.")
                .defineInRange(
                        "skills_dodgeChance",
                        20,
                        0,
                        100
                );

        ZOMBIE_SHIELD_CHANCE = builder
                .comment("Weight for Zombies with the shield skill.")
                .defineInRange(
                        "skills_shieldChance",
                        15,
                        0,
                        100
                );

        ZOMBIE_FLINT_CHANCE = builder
                .comment("Weight for Zombies with the Flint and Steel skill.")
                .defineInRange(
                        "skills_flintChance",
                        15,
                        0,
                        100
                );

        ZOMBIE_BREAK_BLOCK_CHANCE = builder
                .comment("Weight for Zombies with the block breaking skill.")
                .defineInRange(
                        "skills_breakBlockChance",
                        10,
                        0,
                        100
                );

        builder.pop();


        SPEC = builder.build();
    }
}