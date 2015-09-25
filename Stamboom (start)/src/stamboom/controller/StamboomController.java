/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import stamboom.domain.Administratie;
import stamboom.storage.DatabaseMediator;
import stamboom.storage.IStorageMediator;

public class StamboomController {

    private Administratie admin;
    private IStorageMediator storageMediator;

    /**
     * creatie van stamboomcontroller met lege administratie en onbekend
     * opslagmedium
     */
    public StamboomController() {
        admin = new Administratie();
        storageMediator = null;
    }

    public Administratie getAdministratie() {
        return admin;
    }

    /**
     * administratie wordt leeggemaakt (geen personen en geen gezinnen)
     */
    public void clearAdministratie() {
        admin = new Administratie();
    }

    /**
     * administratie wordt in geserialiseerd bestand opgeslagen
     *
     * @param bestand
     * @throws IOException
     */
    public void serialize(File bestand) throws IOException {
        //todo opgave 2
        try {
            FileOutputStream stream = new FileOutputStream(bestand);
            ObjectOutputStream out = new ObjectOutputStream(stream); 
            
            out.writeObject(admin);
            
            out.close();
            stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * administratie wordt vanuit geserialiseerd bestand gevuld
     *
     * @param bestand
     * @throws IOException
     */
    public void deserialize(File bestand) throws IOException {
        //todo opgave 2
        try {
            FileInputStream stream = new FileInputStream(bestand);
            ObjectInputStream in = new ObjectInputStream(stream);
            
            Administratie adminObject = (Administratie) in.readObject();
            admin = adminObject;
            
            in.close();
            stream.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    // opgave 4
    private void initDatabaseMedium() throws IOException {
        if (!(storageMediator instanceof DatabaseMediator)) {
            ////driver, url, username, password opslaan in properties attribuut
            Properties props = new Properties();
            OutputStream output = new FileOutputStream("database.properties");
            props.setProperty("driver", "com.mysql.jdbc.Driver");
            props.setProperty("url", "jdbc:mysql://stefp.nl/philips1_db?noAccessToProcedureBodies=true");
            props.setProperty("username", "philips1_user");
            props.setProperty("password", "Hallo123");
            //store properties in file
            props.store(output, null);
            try (FileInputStream in = new FileInputStream("database.properties")) {
                props.load(in);
            }
            storageMediator = new DatabaseMediator(props);
        }
    }
    
    /**
     * administratie wordt vanuit standaarddatabase opgehaald
     *
     * @throws IOException
     */
    public void loadFromDatabase() throws IOException {
        //todo opgave 4
        
    }

    /**
     * administratie wordt in standaarddatabase bewaard
     *
     * @throws IOException
     */
    public void saveToDatabase() throws IOException {
        //todo opgave 4
        this.initDatabaseMedium();
        this.storageMediator.save(admin);
    }

}
