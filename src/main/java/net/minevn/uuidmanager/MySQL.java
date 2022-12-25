package net.minevn.uuidmanager;

import java.sql.*;
import java.util.Objects;
import java.util.logging.Level;

public class MySQL {
    //region Constructor
    private Connection sql;
    private UuidManager main;
    private static MySQL _instance;


    public MySQL(UuidManager main, String host, String dbname, String user, String pwd, int port) {
        try {
            main.getLogger().info("Dang ket noi den MySQL...");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Class.forName("com.mysql.jdbc.Driver");
            }
            sql = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, user, pwd);
            this.main = main;
            _instance = this;
            main.getLogger().info("Ket noi den MySQL thanh cong.");
        } catch (Exception ex) {
            main.getLogger().log(Level.SEVERE, "Khong the ket noi MySQL", ex);
            _instance = null;
        }
    }

    // cleaner
    private void cleanup(ResultSet result, Statement statement) {
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                main.getLogger().log(Level.SEVERE, "SQLException on cleanup", e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                main.getLogger().log(Level.SEVERE, "SQLException on cleanup", e);
            }
        }
    }
    //endregion
    //region Player data
    private static final String PLAYER_DATA_SAVE = "insert into bedrock_players(name, isbedrock) values(@name:=?, @isbedrock:=?) on duplicate key update isbedrock = @isbedrock";
    private static final String PLAYER_DATA_GET = "select * from bedrock_players where name = ?";
    //end region

    //region proxy data
    private static final String PROXY_DATA_SAVE = "insert into players_count(type, counts) values(@type:=?, @counts:=?) on duplicate key update counts = @counts";
    private static final String PROXY_DATA_GET = "select * from players_count where type = ?";

    //end region
    public String getPlayerData(String username) {
        PreparedStatement ps = null;
        ResultSet r = null;
        String type = "0";
        try {
            ps = sql.prepareStatement(PLAYER_DATA_GET);
            ps.setString(1, username);
            r = ps.executeQuery();
            if (r.next()) {
                Boolean isBedrock = r.getBoolean("isbedrock");
                if (isBedrock) {
                    type = "1"; //isBedrock
                } else {
                    type = "2"; //isJava
                }
            }
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "Khong the lay thong tin cua " + username, e);
        } finally {
            cleanup(r, ps);
        }
        return type;
    }

    public void setPlayerData(String username, Boolean isBedrock) {
        PreparedStatement ps = null;
        try {
            ps = sql.prepareStatement(PLAYER_DATA_SAVE);
            ps.setString(1, username);
            ps.setBoolean(2, isBedrock);
            ps.executeUpdate();
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "Khong the lay thong tin cua " + username, e);
        } finally {
            cleanup(null, ps);
        }
    }
    //end region

    //proxy data
    public int getProxyData(String type) {
        PreparedStatement ps = null;
        ResultSet r = null;
        int result = 0;
        try {
            ps = sql.prepareStatement(PROXY_DATA_GET);
            ps.setString(1, type);
            r = ps.executeQuery();
            if (r.next()) {
                result = r.getInt("counts");
            }
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "Khong the lay thong tin cua proxy", e);
        } finally {
            cleanup(r, ps);
        }
        return result;
    }

    public void setProxyData(String type, int counts) {
        PreparedStatement ps = null;
        try {
            ps = sql.prepareStatement(PROXY_DATA_SAVE);
            ps.setString(1, type);
            ps.setInt(2, counts);
            ps.executeUpdate();
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "Khong the lay thong tin cua cua proxy", e);
        } finally {
            cleanup(null, ps);
        }
    }


    public static MySQL getInstance() {
        return Objects.requireNonNull(_instance);
    }
}