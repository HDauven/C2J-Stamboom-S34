/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.storage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import stamboom.domain.Administratie;

public class DatabaseMediator implements IStorageMediator {

    private Properties props;
    private Connection conn;

    public DatabaseMediator(Properties props) {
        this.props = props;
    }

    @Override
    public Administratie load() throws IOException {
        //todo opgave 4
        return null;
    }

    @Override
    public void save(Administratie admin) throws IOException {
        //todo opgave 4
        
    }

    /**
     * Laadt de instellingen, in de vorm van een Properties bestand, en controleert
     * of deze in de correcte vorm is, en er verbinding gemaakt kan worden met
     * de database.
     * @param props
     * @return
     */
    @Override
    public final boolean configure(Properties props) {
        this.props = props;
        if (!isCorrectlyConfigured()) {
            System.err.println("props mist een of meer keys");
            return false;
        }

        try {
            initConnection();
            return true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            this.props = null;
            return false;
        } finally {
            closeConnection();
        }
    }

    @Override
    public Properties config() {
        return props;
    }

    @Override
    public boolean isCorrectlyConfigured() {
        if (props == null) {
            return false;
        }
        if (!props.containsKey("driver")) {
            return false;
        }
        if (!props.containsKey("url")) {
            return false;
        }
        if (!props.containsKey("username")) {
            return false;
        }
        if (!props.containsKey("password")) {
            return false;
        }
        return true;
    }

    private void initConnection() throws SQLException {
        //opgave 4
        try
        {
            Class.forName(props.getProperty("driver"));
            //conn = DriverManager.getConnection ("jdbc:mysql://athena.fhict.nl/dbi282758", "i282758_Stef", "Stef82,.");
            //conn = DriverManager.getConnection("jdbc:mysql://stefp.nl/philips1_db?noAccessToProcedureBodies=true", "philips1_user", "Hallo123");
            conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));

        } catch (ClassNotFoundException | SQLException ex)
        {
            System.out.println(ex.toString());
        }
    }

    private void closeConnection() {
        try {
            conn.close();
            conn = null;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
