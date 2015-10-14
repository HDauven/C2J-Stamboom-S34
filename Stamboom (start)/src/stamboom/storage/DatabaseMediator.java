package stamboom.storage;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import stamboom.domain.Administratie;
import stamboom.domain.Geslacht;
import stamboom.domain.Gezin;
import stamboom.domain.Persoon;
import stamboom.util.UserLogging;

public class DatabaseMediator implements IStorageMediator {

    private Properties props;
    private Connection conn;
    private SortedMap<Statement, CallableStatement> statements = new TreeMap<>();
    private SortedMap<Integer, Integer> persoonsGezin = new TreeMap<>();
    

    public DatabaseMediator(Properties props) {
        this.props = props;
        try {
            initConnection();
        } catch (SQLException sqle) {
            UserLogging.logAction("SQL Exception: ", sqle.toString());
            sqle.printStackTrace();
        }
    }

    @Override
    public Administratie load() throws IOException {
        //todo opgave 4
        {
            Administratie admin = new Administratie();
            admin = this.getPersonenFromDatabase(admin);
            admin = this.getGezinnenFromDatabase(admin);
            admin = this.addGezinToPersoon(admin);
            return admin;
        }
    }

    private Administratie getPersonenFromDatabase(Administratie admin) {
        CallableStatement cStmt = statements.get(Statement.GET_PERSONEN);
        try {
            cStmt.executeQuery();

            ResultSet rs = cStmt.getResultSet();
            //Personen ophalen
            while (rs.next()) {
                int persNr = rs.getInt("persoonsNummer");
                String achternaam = rs.getString("achternaam");
                String[] vnamen = rs.getString("voornamen").split(" ");
                String tVoegsel = rs.getString("tussenvoegsel");
                Date d = rs.getDate("geboortedatum");
                String geboorteplaats = rs.getString("geboorteplaats");
                Geslacht g = null;
                String geslacht = rs.getString("geslacht");
                int gezinsNr = rs.getInt("ouders");
                if (geslacht.equals("MAN")) {
                    g = Geslacht.MAN;
                }
                if (geslacht.equals("VROUW")) {
                    g = Geslacht.VROUW;
                }
                if(gezinsNr > 0)
                    persoonsGezin.put(persNr, gezinsNr);
                //ouders kan pas na gezinnen toevoegen
                //Nr zal hetzelfde blijven? moet getest worden
                //Persoon toevoegen aan de nieuwe admin
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                //Geslacht geslacht, String[] vnamen, String anaam, String tvoegsel, Calendar gebdat, String gebplaats, Gezin ouderlijkGezin
                admin.addPersoon(g, vnamen, achternaam, tVoegsel, c, geboorteplaats, null);
            }
        } catch (SQLException ex) {
            UserLogging.logAction("SQL Exception: ", ex.toString());
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return admin;
    }

    private Administratie getGezinnenFromDatabase(Administratie admin){
        CallableStatement cStmt = statements.get(Statement.GET_GEZINNEN);
        try {
            //Gezinnen ophalen
            cStmt.executeQuery();

            ResultSet rs = cStmt.getResultSet();

            while (rs.next()) {
                int gezinsNr = rs.getInt("gezinsNummer");
                int o1 = rs.getInt("ouder1");
                int o2 = rs.getInt("ouder2");
                Date huwelijksdatum = rs.getDate("huwelijksdatum");
                Date scheidingsdatum = rs.getDate("scheidingsdatum");
                Gezin g = admin.addOngehuwdGezin(admin.getPersoon(o1), admin.getPersoon(o2));
                Calendar calHuwelijk = Calendar.getInstance();
                Calendar calScheiding = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");             
                
                if(huwelijksdatum != null)
                {
                    System.out.println(sdf.format(huwelijksdatum));
                    calHuwelijk.setTime(huwelijksdatum);
                    g.setHuwelijk(calHuwelijk);
                }
                if(scheidingsdatum != null)
                {
                    System.out.println(sdf.format(scheidingsdatum));
                    calScheiding.setTime(scheidingsdatum);
                    g.setScheiding(calScheiding);
                }
            }
        } catch (SQLException ex) {
            UserLogging.logAction("SQL Exception: ", ex.toString());
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return admin;
    }

            @Override
            public void save
            (Administratie admin) throws IOException {
                //todo opgave 4

                try {
                    //Maak alle tabellen leeg
                    CallableStatement dStmt = statements.get(Statement.DELETE_ENTRIES);
                    dStmt.execute();

                    //Zet alle personen in de database zonder ouderlijk gezin, omdat er nog geen gezinnen in de database staan.
                    for (Persoon p : admin.getPersonen()) {
                        CallableStatement cStmt = statements.get(Statement.CREATE_PERSOON);
                        cStmt.setInt(1, p.getNr());
                        cStmt.setString(2, p.getAchternaam());
                        cStmt.setString(3, p.getVoornamen());
                        cStmt.setString(4, p.getTussenvoegsel());
                        cStmt.setDate(5, new java.sql.Date(p.getGebDat().getTime().getTime()));
                        cStmt.setString(6, p.getGebPlaats());
                        cStmt.setString(7, p.getGeslacht().toString());
                        cStmt.setInt(8, 0);
                        cStmt.execute();
                    }

                    //Voeg alle gezinnen toe
                    for (Gezin g : admin.getGezinnen()) {
                        CallableStatement cStmt = statements.get(Statement.CREATE_GEZIN);
                        cStmt.setInt(1, g.getNr());
                        cStmt.setInt(2, g.getOuder1().getNr());
                        if (g.getOuder2() != null) {
                            cStmt.setInt(3, g.getOuder2().getNr());
                        } else {
                            cStmt.setInt(3, -1);
                        }
                        if (g.getHuwelijksdatum() != null) {
                            cStmt.setDate(4, new java.sql.Date(g.getHuwelijksdatum().getTime().getTime()));
                        } else {
                            cStmt.setDate(4, null);
                        }
                        if (g.getScheidingsdatum() != null) {
                            cStmt.setDate(5, new java.sql.Date(g.getScheidingsdatum().getTime().getTime()));
                        } else {
                            cStmt.setDate(5, null);
                        }
                        cStmt.execute();
                    }

                    //Zet de gezinsnummers van het ouderlijke gezin bij de personen
                    for (Persoon p : admin.getPersonen()) {
                        if (p.getOuderlijkGezin() != null) {
                            CallableStatement cStmt = statements.get(Statement.ADD_OUDERLIJK_GEZIN);
                            cStmt.setInt(1, p.getNr());
                            cStmt.setInt(2, p.getOuderlijkGezin().getNr());
                            cStmt.execute();
                        }
                    }
                } catch (SQLException ex) {
                    UserLogging.logAction("SQL Exception: ", ex.toString());
                    Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            /**
             * Laadt de instellingen, in de vorm van een Properties bestand, en
             * controleert of deze in de correcte vorm is, en er verbinding
             * gemaakt kan worden met de database.
             *
             * @param props
             * @return
             */
            @Override
            public final boolean configure
            (Properties props
            
                ) {
        this.props = props;
                if (!isCorrectlyConfigured()) {
                    System.err.println("props mist een of meer keys");
                    return false;
                }

                try {
                    initConnection();
                    return true;
                } catch (SQLException ex) {
                    UserLogging.logAction("SQL Exception: ", ex.toString());
                    System.err.println(ex.getMessage());
                    this.props = null;
                    return false;
                } finally {
                    closeConnection();
                }
            }

            @Override
            public Properties config
            
                () {
        return props;
            }

            @Override
            public boolean isCorrectlyConfigured
            
                () {
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
        try {
            Class.forName(props.getProperty("driver"));
            //conn = DriverManager.getConnection ("jdbc:mysql://athena.fhict.nl/dbi282758", "i282758_Stef", "Stef82,.");
            //conn = DriverManager.getConnection("jdbc:mysql://stefp.nl/philips1_db?noAccessToProcedureBodies=true", "philips1_user", "Hallo123");
            conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));

            statements.put(Statement.CREATE_PERSOON, conn.prepareCall("{call createPersoon(?, ?, ?, ?, ?, ?, ?, ?)}"));
            statements.put(Statement.CREATE_GEZIN, conn.prepareCall("{call createGezin(?, ?, ?, ?, ?)}"));
            statements.put(Statement.ADD_OUDERLIJK_GEZIN, conn.prepareCall("{call updatePersoon(?, ?)}"));
            statements.put(Statement.DELETE_ENTRIES, conn.prepareCall("{call deleteEntries()}"));
            statements.put(Statement.GET_PERSONEN, conn.prepareCall("{call getPersonen()}"));
            statements.put(Statement.GET_GEZINNEN, conn.prepareCall("{call getGezinnen()}"));
        } catch (ClassNotFoundException | SQLException ex) {
            UserLogging.logAction("SQL Exception: ", ex.toString());
            System.out.println(ex.toString());
        }
    }

    private void closeConnection() {
        try {
            conn.close();
            conn = null;
        } catch (SQLException ex) {
            UserLogging.logAction("SQL Exception: ", ex.toString());
            System.err.println(ex.getMessage());
        }
    }

    private Administratie addGezinToPersoon(Administratie admin) {
        for(Map.Entry<Integer, Integer> entry : this.persoonsGezin.entrySet())
        {
            admin.setOuders(admin.getPersoon(entry.getKey()), admin.getGezin(entry.getValue()));
        }
        return admin;
    }
}
