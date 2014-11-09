package me.legitmodern.Punishment.Utils.SQL;

import me.legitmodern.Punishment.Main;
import me.legitmodern.Punishment.API.Punishment;
import me.legitmodern.Punishment.API.PunishmentType;
import me.legitmodern.Punishment.Utils.XMLUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager extends DatabaseConnection {

    public DatabaseManager(DatabaseConnectionFactory factory) {
        super(factory);
    }

    private static DatabaseManager instance = new DatabaseManager(
            DatabaseConnectionFactory.builder()
                    .withHost(XMLUtils.getNodeAtPath("settings.MySQL.Address", Main.getInstance().configFile, 0).getTextContent())
                    .withPort(Integer.valueOf(XMLUtils.getNodeAtPath("settings.MySQL.Database", Main.getInstance().configFile, 0).getTextContent()))
                    .withDatabase(XMLUtils.getNodeAtPath("settings.MySQL.Database", Main.getInstance().configFile, 0).getTextContent())
                    .withUsername(XMLUtils.getNodeAtPath("settings.MySQL.Username", Main.getInstance().configFile, 0).getTextContent())
                    .withPassword(XMLUtils.getNodeAtPath("settings.MySQL.Password", Main.getInstance().configFile, 0).getTextContent())
    );

    public static DatabaseManager getInstance() {
        return instance;
    }

    /**
     * Check the punishments database
     */
    public void checkDatabase() {
        try {
            this.getPreparedStatement("CREATE TABLE IF NOT EXISTS punishments (id INTEGER, punished VARCHAR(255), issuer VARCHAR(255), type VARCHAR(255), reason TEXT, time VARCHAR(255), end LONG, active BOOL)").execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a new id for a punishment
     *
     * @return New id for a new punishment
     */
    public int getNewId() {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT COUNT(*) FROM punishments").executeQuery();
            rs.next();

            return rs.getInt(1) + 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get a punishment by it's id
     *
     * @param id ID of punishment to get
     * @return Punishment by it's id
     */
    public Punishment getPunishment(int id) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT * FROM punishments WHERE id=" + id + ";").executeQuery();

            if (rs.next()) {
                Punishment punishment = new Punishment(
                        UUID.fromString(rs.getString("punished")),
                        UUID.fromString(rs.getString("issuer")),
                        PunishmentType.fromString(rs.getString("type")),
                        rs.getString("reason"),
                        rs.getString("time"),
                        rs.getLong("end")
                );
                punishment.setId(rs.getInt("id"));
                punishment.setActive(rs.getBoolean("active"));
                return punishment;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the punishments for a player
     *
     * @param player UUID of player to get punishments for
     * @return Player's punishments
     */
    public Punishment[] getPunishments(UUID player) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT * FROM punishments WHERE punished='" + player.toString() + "';").executeQuery();
            List<Punishment> punishments = new ArrayList<>();
            while (rs.next()) {
                Punishment punishment = new Punishment(
                        UUID.fromString(rs.getString("punished")),
                        UUID.fromString(rs.getString("issuer")),
                        PunishmentType.fromString(rs.getString("type")),
                        rs.getString("reason"),
                        rs.getString("time"),
                        rs.getLong("end")
                );
                punishment.setId(rs.getInt("id"));
                punishment.setActive(rs.getBoolean("active"));
                punishments.add(punishment);
            }

            return punishments.toArray(new Punishment[punishments.size()]);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Punishment[0];
    }

    /**
     * Check if a player has an active ban
     *
     * @param player UUID of player
     * @return true if player has a ban active
     */
    public boolean hasBanActive(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_BAN || punishment.getType() == PunishmentType.TEMPORARY_BAN) && punishment.isActive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a player's active ban
     *
     * @param player UUID of player
     * @return Player's active ban if they have one
     */
    public Punishment getActiveBan(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_BAN || punishment.getType() == PunishmentType.TEMPORARY_BAN) && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    /**
     * Check if a player has an active mute
     *
     * @param player UUID of player
     * @return true if player has a mute active
     */
    public boolean hasMuteActive(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_MUTE || punishment.getType() == PunishmentType.TEMPORARY_MUTE) && punishment.isActive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a player's active ban
     *
     * @param player UUID of player
     * @return Player's active ban if they have one
     */
    public Punishment getActiveMute(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_MUTE || punishment.getType() == PunishmentType.TEMPORARY_MUTE) && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    /**
     * Get the latest punishment of a player
     *
     * @param player UUID of player to get punishment for
     * @return Latest punishment of player
     */
    public Punishment getLatestPunishment(UUID player) {
        Punishment[] punishments = getPunishments(player);
        return punishments[punishments.length - 1];
    }


    /* TODO: SQL query that connects with permission table + makes sure the player being punished isn't a staff.  (Also used to message all staff when player is banned?)
    public boolean isStaff(ProxiedPlayer player) {

    }
    */
}