package me.anutley.titan.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        try {
            final File dbFile = new File("database.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        config.setJdbcUrl("jdbc:sqlite:database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        ds = new HikariDataSource(config);

        createGuildSettingsTable();
        createTagsTable();
        createPingProtectionSettingsTable();
        createPingProtectionDataTable();
        createWelcomeTable();
        createLeaveTable();
        createWarnsTable();
    }

    private SQLiteDataSource() { }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void createGuildSettingsTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "guild_admin_role VARCHAR(20)," +
                    "guild_mod_role VARCHAR(20)," +
                    "tag_management_role VARCHAR(20)," +
                    "mute_role VARCHAR(20)," +
                    "lockdown BOOLEAN NOT NULL DEFAULT false" +
                    ");");

            LOGGER.info("Guild settings table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTagsTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS tags (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "trigger VARCHAR (100) NOT NULL," +
                    "title VARCHAR(256) NOT NULL," +
                    "content VARCHAR(4096)," +
                    "colour VARCHAR (6)," +
                    "thumbnail VARCHAR(500)" +
                    ");");

            LOGGER.info("Tags table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPingProtectionSettingsTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS ping_protection_settings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "enabled BOOLEAN DEFAULT TRUE NOT NULL," +
                    "roles VARCHAR(10000)," +
                    "threshold INTEGER DEFAULT 10 NOT NULL," +
                    "action VARCHAR(10) DEFAULT 'kick' NOT NULL" +
                    ");");

            LOGGER.info("Ping protection settings table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPingProtectionDataTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS ping_protection_data (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "member_id VARCHAR(20) NOT NULL," +
                    "illegal_ping_count INTEGER NOT NULL DEFAULT 0" +
                    ");");

            LOGGER.info("Ping protection data table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createWelcomeTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS welcomes(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "enabled BOOLEAN NOT NULL DEFAULT true," +
                    "channel_id VARCHAR(20)," +
                    "message VARCHAR(500) NOT NULL DEFAULT 'Welcome %user% to %guild_name%'" +
                    ");");

            LOGGER.info("Welcome table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createLeaveTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS leave(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "enabled BOOLEAN NOT NULL DEFAULT true," +
                    "channel_id VARCHAR(20)," +
                    "message VARCHAR(500) NOT NULL DEFAULT '%user% has left %guild_name%'" +
                    ");");

            LOGGER.info("Leave table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createWarnsTable() {
        try (final Statement statement = getConnection().createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS warnings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "user_id VARCHAR(20) NOT NULL," +
                    "moderator_id VARCHAR(20) NOT NULL," +
                    "content VARCHAR(250) NOT NULL" +
                    ");");

            LOGGER.info("Warnings table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
