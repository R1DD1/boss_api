package me.moteloff.bossapi.database;

import me.moteloff.bossapi.BossAPI;

import java.sql.*;

public class DatabaseConstructor {
    private static final String url = BossAPI.getInstance().getConfig().getString("database_path");

    public DatabaseConstructor() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS bosses (\n"
                    + " id integer PRIMARY KEY,\n"
                    + " name text NOT NULL,\n"
                    + " kill_time text NOT NULL,\n"
                    + " top_players text NOT NULL\n"
                    + ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertData(String bossName, String killTime, String topPlayers) {
        String sql = "INSERT INTO bosses(name, kill_time, top_players) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bossName);
            pstmt.setString(2, killTime);
            pstmt.setString(3, topPlayers);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectAllData() {
        String sql = "SELECT * FROM bosses";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("kill_time") + "\t" +
                        rs.getString("top_players"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}