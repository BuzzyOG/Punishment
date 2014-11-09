package me.legitmodern.Punishment.API;

import me.legitmodern.Punishment.Utils.SQL.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Punishment {
    private UUID punished, issuer;
    private PunishmentType type;
    private String reason;
    private long end;
    private int id = DatabaseManager.getInstance().getNewId();
    private String time;
    private boolean active = true;

    public Punishment(UUID punished, UUID issuer, PunishmentType type, String reason, String time, long end) {
        this.punished = punished;
        this.issuer = issuer;
        this.type = type;
        this.reason = reason;
        this.time = time;
        this.end = end;
    }

    /**
     * Get the punished uuid
     *
     * @return Punished UUID
     */
    public UUID getPunished() {
        return this.punished;
    }

    /**
     * Get the issuer of the punishment's UUID
     *
     * @return Issuer of punishment's UUID
     */
    public UUID getIssuer() {
        return this.issuer;
    }

    /**
     * Get the type of the punishment
     *
     * @return Type of the punishment
     */
    public PunishmentType getType() {
        return this.type;
    }

    /**
     * Get the reasoning for the punishment
     *
     * @return Reason for the punishment being issued
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Get the ending of the punishment
     *
     * @return Ending of the punishment
     */
    public long getEnd() {
        return this.end;
    }

    /**
     * Get the id of the punishment
     *
     * @return ID of the punishment
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set the id of the punishment
     *
     * @param id ID of punishment
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the time the punishment was created
     *
     * @return time
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Check if the punishment is active
     *
     * @return true if the punishment is active
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Set the punishment active flag
     *
     * @param flag Punishment active flag
     */
    public void setActive(boolean flag) {
        this.active = flag;
    }

    /**
     * Save the punishment to the database
     */
    public void save() {
        try {
            ResultSet count = DatabaseManager.getInstance().getPreparedStatement("SELECT COUNT(*) FROM punishments WHERE id='" + getId() + "';").executeQuery();
            count.next();

            if (count.getInt(1) == 0) {
                DatabaseManager.getInstance().getPreparedStatement("INSERT INTO punishments (id, punished, issuer, type, reason, time, end, active) VALUES (" + getId() + ", '" + getPunished().toString() + "', '" + getIssuer().toString() + "', '" + getType().getName() + "', '" + getReason() + "', '" + getTime() + "', " + getEnd() + ", " + isActive() + ")").execute();
            } else {
                DatabaseManager.getInstance().getPreparedStatement("UPDATE punishments SET punished='" + getPunished().toString() + "', issuer='" + getIssuer().toString() + "', type='" + getType().toString() + "', reason='" + getReason() + "', type='" + getType().getName() + "', time='" + getTime() + "', end=" + getEnd() + ", active=" + isActive() + " WHERE id=" + getId()).execute();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
