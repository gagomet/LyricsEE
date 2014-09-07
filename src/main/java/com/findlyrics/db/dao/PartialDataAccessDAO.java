package com.findlyrics.db.dao;

import com.findlyrics.db.model.Song;
import com.findlyrics.exceptions.DbConnectionException;
import com.findlyrics.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Padonag on 07.09.2014.
 */
public class PartialDataAccessDAO {


    public PartialDataAccessDAO() {
    }

    Statement stmt;
    private int noOfRecords;

    public List<Song> getSongsPart(
            int offset,
            int noOfRecords, String lyrics) {
        String readyQuery = "%"+lyrics+"%";
        String query = "SELECT SQL_CALC_FOUND_ROWS * FROM songs WHERE songs.lyrics LIKE '"+ readyQuery + "' LIMIT "
                + offset + ", " + noOfRecords;
        List<Song> list = new ArrayList<Song>();

        try {
            Connection connection = ConnectionManager.getInstance().getConnection();

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Song song = new Song();
                song.setArtistId(rs.getLong("artist_id"));
                song.setTitle(rs.getString("song_name"));
                song.setLyrics(rs.getString("lyrics"));
                list.add(song);
            }
            rs.close();

            rs = stmt.executeQuery("SELECT FOUND_ROWS()");
            if (rs.next())
                this.noOfRecords = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DbConnectionException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public int getNoOfRecords() {
        return noOfRecords;
    }

}
