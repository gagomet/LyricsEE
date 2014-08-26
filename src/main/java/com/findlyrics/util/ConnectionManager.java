package com.findlyrics.util;

import com.findlyrics.exceptions.DbConnectionException;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
* Created by Padonag on 21.07.2014.
*/
public class ConnectionManager {
    private static final Logger log = Logger.getLogger(ConnectionManager.class);
    private static Connection connection;
    private static volatile ConnectionManager instance;

    private ConnectionManager()  {
        Context ctx = null;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("jdbc/lyricsbase");
            connection = ds.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static class Holder  {
        private static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    public Connection getConnection() throws DbConnectionException{
      if(connection == null){
          throw new DbConnectionException("Db connection is dead!");
      }
        return connection;
    }

    public static ConnectionManager getInstance() {
        return Holder.INSTANCE;
    }
}
