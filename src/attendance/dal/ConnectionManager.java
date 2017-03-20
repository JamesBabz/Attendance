/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendance.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
/**
 *
 * @author James
 */
public class ConnectionManager {

    //Connects to the DB specified in the config file
    
    private static final String DB_CONFIG_FILE = "DBConfig.cfg";
    private final SQLServerDataSource ds;

    public ConnectionManager() throws IOException
    {
        Properties props = new Properties();
        props.load(new FileReader(DB_CONFIG_FILE));

        ds = new SQLServerDataSource();
        ds.setServerName(props.getProperty("SERVER"));
        ds.setDatabaseName(props.getProperty("DATABASE"));
        ds.setPortNumber(Integer.parseInt(props.getProperty("PORT")));
        ds.setUser(props.getProperty("USER"));
        ds.setPassword(props.getProperty("PASSWORD"));
    }

    public Connection getConnection() throws SQLServerException
    {
        return ds.getConnection();
    }



}
