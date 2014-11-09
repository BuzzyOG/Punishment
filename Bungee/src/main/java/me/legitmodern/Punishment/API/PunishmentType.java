package me.legitmodern.Punishment.API;

public enum PunishmentType {
    PERMANENT_BAN("Permanent Ban"), TEMPORARY_BAN("Temporary Ban"), PERMANENT_MUTE("Permanent Mute"), TEMPORARY_MUTE("Temporary Mute"), KICK("Kick"), WARN("Warning");

    private String name;

    PunishmentType(String type) {
        this.name = type;
    }

    /**
     * Get the name of the punishment type
     *
     * @return Name of punishment type
     */
    public String getName() {
        return name;
    }

    /**
     * Get a punishment type from a string
     *
     * @param name Name of punishment type
     * @return Punishment type from string
     */
    public static PunishmentType fromString(String name) {
        for (PunishmentType type : values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}