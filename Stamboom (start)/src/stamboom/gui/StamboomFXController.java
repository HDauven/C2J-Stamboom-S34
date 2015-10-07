/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import stamboom.controller.StamboomController;
import stamboom.domain.Geslacht;
import stamboom.domain.Gezin;
import stamboom.domain.Persoon;
import stamboom.util.StringUtilities;

/**
 *
 * @author frankpeeters
 */
public class StamboomFXController extends StamboomController implements Initializable {

    //MENUs en TABs
    @FXML MenuBar menuBar;
    @FXML MenuItem miNew;
    @FXML MenuItem miOpen;
    @FXML MenuItem miSave;
    @FXML CheckMenuItem cmDatabase;
    @FXML MenuItem miClose;
    @FXML Tab tabPersoon;
    Tab tabGezin;
    Tab tabPersoonInvoer;
    @FXML Tab tabGezinInvoer;

    //PERSOON
    @FXML ComboBox<Persoon> cbPersonen;
    @FXML TextField tfPersoonNr;
    @FXML TextField tfVoornamen;
    @FXML TextField tfTussenvoegsel;
    @FXML TextField tfAchternaam;
    @FXML TextField tfGeslacht;
    @FXML TextField tfGebDatum;
    @FXML TextField tfGebPlaats;
    @FXML ComboBox<Gezin> cbOuderlijkGezin;
    @FXML ListView lvAlsOuderBetrokkenBij;
    @FXML Button btStamboom;

    //INVOER GEZIN
    @FXML ComboBox cbOuder1Invoer;
    @FXML ComboBox cbOuder2Invoer;
    @FXML TextField tfHuwelijkInvoer;
    @FXML TextField tfScheidingInvoer;
    @FXML Button btOKGezinInvoer;
    @FXML Button btCancelGezinInvoer;

    @FXML
    private Insets x1;
    @FXML
    private Insets x3;
    @FXML
    private TextField tbNewVoornamen;
    @FXML
    private TextField tbNewTussenvoegsel;
    @FXML
    private TextField tbNewAchternaam;
    @FXML
    private TextField tbNewGebDat;
    @FXML
    private ComboBox<Geslacht> cbNewGeslacht;
    @FXML
    private TextField tbNewGebPlaats;
    @FXML
    private ComboBox<Gezin> cbNewOuderlijkGezin;
    @FXML
    private TextField tbOuder2;
    @FXML
    private TextField tbHuwelijkOp;
    @FXML
    private TextField tbOuder1;
    @FXML
    private TextField tbGezinsNr;
    @FXML
    private ComboBox<Gezin> cbGezinnen;
    @FXML
    private TextField tbScheidingOp;
    @FXML
    private ListView<Persoon> lvKinderen;
    @FXML
    private TextField tbGrootouder1a;
    @FXML
    private TextField tbGrootouder2a;
    @FXML
    private TextField tbGrootouder1b;
    @FXML
    private TextField tbGrootouder2b;
    @FXML
    private TextField tbOuder1a;
    @FXML
    private TextField tbOuder2a;

    //opgave 4
    private boolean withDatabase;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private static final String FILE = "admin.data";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initComboboxes();
        //Testwaarden
        String[] vnamen = new String[1];
        vnamen[0] = "Stef";
        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c.setTime(sdf.parse("02-01-1994"));
            c2.setTime(sdf.parse("03-03-1995"));

        } catch (ParseException ex) {
            Logger.getLogger(StamboomFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        getAdministratie().addPersoon(Geslacht.MAN, vnamen, "Philipsen", "", c, "Ysselsteyn", null);
        vnamen[0] = "Henkie";
        getAdministratie().addPersoon(Geslacht.VROUW, vnamen, "Janssen", "", c, "Ysselsteyn", null);
        getAdministratie().addOngehuwdGezin(getAdministratie().getPersoon(1), getAdministratie().getPersoon(0));
        getAdministratie().addPersoon(Geslacht.MAN, vnamen, "Philipsen", "", c2, "Ysselsteyn", getAdministratie().getGezin(1));
        //getAdministratie().getGezin(1).

        withDatabase = false;
    }

    private void initComboboxes() {
        //todo opgave 3
        cbPersonen.setItems(getAdministratie().getPersonen());
        cbOuder1Invoer.setItems(getAdministratie().getPersonen());
        cbOuder2Invoer.setItems(getAdministratie().getPersonen());
        cbOuderlijkGezin.setItems(getAdministratie().getGezinnen());
        cbNewGeslacht.getItems().clear();
        cbNewGeslacht.getItems().add(Geslacht.MAN);
        cbNewGeslacht.getItems().add(Geslacht.VROUW);
        cbNewOuderlijkGezin.setItems(getAdministratie().getGezinnen());
        //cbGezinnen.getItems().clear();
        cbGezinnen.setItems(getAdministratie().getGezinnen());
        setTestwaarden();
    }

    //Methode kan weg na het testen.
    public void setTestwaarden() {
        //Testwaarden
        tbNewVoornamen.setText("abc def");
        tbNewAchternaam.setText("ghi");
        tbNewGebDat.setText("01-01-1960");
        tbNewGebPlaats.setText("Nederland");
    }

    @FXML
    public void selectPersoon(Event evt) {
        Persoon persoon = (Persoon) cbPersonen.getSelectionModel().getSelectedItem();
        showPersoon(persoon);
    }

    private void showPersoon(Persoon persoon) {
        if (persoon == null) {
            clearTabPersoon();
        } else {
            tfPersoonNr.setText(persoon.getNr() + "");
            tfVoornamen.setText(persoon.getVoornamen());
            tfTussenvoegsel.setText(persoon.getTussenvoegsel());
            tfAchternaam.setText(persoon.getAchternaam());
            tfGeslacht.setText(persoon.getGeslacht().toString());
            tfGebDatum.setText(StringUtilities.datumString(persoon.getGebDat()));
            tfGebPlaats.setText(persoon.getGebPlaats());
            if (persoon.getOuderlijkGezin() != null) {
                cbOuderlijkGezin.getSelectionModel().select(persoon.getOuderlijkGezin());
            } else {
                cbOuderlijkGezin.getSelectionModel().clearSelection();
            }

            //todo opgave 3
            lvAlsOuderBetrokkenBij.setItems(persoon.getAlsOuderBetrokkenIn());
        }
    }

    @FXML
    public void setOuders(Event evt) {
        if (tfPersoonNr.getText().isEmpty()) {
            return;
        }
        Gezin ouderlijkGezin = (Gezin) cbOuderlijkGezin.getSelectionModel().getSelectedItem();
        if (ouderlijkGezin == null) {
            return;
        }

        int nr = Integer.parseInt(tfPersoonNr.getText());
        Persoon p = getAdministratie().getPersoon(nr);
        if (getAdministratie().setOuders(p, ouderlijkGezin)) {
            showDialog("Success", ouderlijkGezin.toString()
                    + " is nu het ouderlijk gezin van " + p.getNaam());
        }

    }

    @FXML
    public void selectGezin(Event evt) {
        // todo opgave 3
        Gezin g = (Gezin) cbGezinnen.getSelectionModel().getSelectedItem();
        this.showGezin(g);
    }

    private void showGezin(Gezin gezin) {
        // todo opgave 3
        tbGezinsNr.setText(String.valueOf(gezin.getNr()));
        tbOuder1.setText(gezin.getOuder1().toString());
        if (gezin.getOuder2() != null) {
            tbOuder2.setText(gezin.getOuder2().toString());
        }
        if (gezin.getHuwelijksdatum() != null) {
            tbHuwelijkOp.setText(gezin.getHuwelijksdatum().getTime().toString());
        }
        if (gezin.getScheidingsdatum() != null) {
            tbScheidingOp.setText(sdf.format(gezin.getScheidingsdatum().getTime().toString()));
        }

        lvKinderen.setItems(gezin.getKinderen());
    }

    @FXML
    public void setHuwdatum(Event evt) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(tbHuwelijkOp.getText()));
            Gezin g = cbGezinnen.getSelectionModel().getSelectedItem();
            g.setHuwelijk(c);
            System.out.println("Huwelijk voltrokken.");
        } catch (ParseException ex) {
            Logger.getLogger(StamboomFXController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void setScheidingsdatum(Event evt) {
        // todo opgave 3
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(tbScheidingOp.getText()));
            Gezin g = cbGezinnen.getSelectionModel().getSelectedItem();
            g.setScheiding(c);
            System.out.println("Gescheiden");
        } catch (ParseException ex) {
            Logger.getLogger(StamboomFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void cancelPersoonInvoer(Event evt) {
        // todo opgave 3
        clearTabPersoonInvoer();
    }

    @FXML
    public void okPersoonInvoer(Event evt) throws ParseException {
        // todo opgave 3
        if(cbNewGeslacht.getSelectionModel().getSelectedItem() == null)
        {
            showDialog("Error", "Selecteer een geslacht.");
            return;
        }
        Geslacht g = cbNewGeslacht.getSelectionModel().getSelectedItem();
        
        String[] vnamen = tbNewVoornamen.getText().split(" ");
        String anaam = tbNewAchternaam.getText();
        String tvoegsel = tbNewTussenvoegsel.getText();
        Calendar gebDat = Calendar.getInstance();
        gebDat.setTime(sdf.parse(tbNewGebDat.getText()));
        String gebPlaats = tbNewGebPlaats.getText();
        Gezin ouderlijkGezin = cbNewOuderlijkGezin.getSelectionModel().getSelectedItem();

        if (getAdministratie().addPersoon(g, vnamen, anaam, tvoegsel, gebDat, gebPlaats, ouderlijkGezin) != null) {
            clearTabPersoonInvoer();
        } else {
            showDialog("Error", "Persoon kon niet worden toegevoegd. Controleer je ingevoerde gegevens.");
        }
    }

    @FXML
    public void okGezinInvoer(Event evt) {
        Persoon ouder1 = (Persoon) cbOuder1Invoer.getSelectionModel().getSelectedItem();
        if (ouder1 == null) {
            showDialog("Warning", "eerste ouder is niet ingevoerd");
            return;
        }
        Persoon ouder2 = (Persoon) cbOuder2Invoer.getSelectionModel().getSelectedItem();
        Calendar huwdatum;
        try {
            huwdatum = StringUtilities.datum(tfHuwelijkInvoer.getText());
        } catch (IllegalArgumentException exc) {
            showDialog("Warning", "huwelijksdatum :" + exc.getMessage());
            return;
        }
        Gezin g;
        if (huwdatum != null) {
            g = getAdministratie().addHuwelijk(ouder1, ouder2, huwdatum);
            if (g == null) {
                showDialog("Warning", "Invoer huwelijk is niet geaccepteerd");
            } else {
                Calendar scheidingsdatum;
                try {
                    scheidingsdatum = StringUtilities.datum(tfScheidingInvoer.getText());
                    if (scheidingsdatum != null) {
                        getAdministratie().setScheiding(g, scheidingsdatum);
                    }
                } catch (IllegalArgumentException exc) {
                    showDialog("Warning", "scheidingsdatum :" + exc.getMessage());
                }
            }
        } else {
            g = getAdministratie().addOngehuwdGezin(ouder1, ouder2);
            if (g == null) {
                showDialog("Warning", "Invoer ongehuwd gezin is niet geaccepteerd");
            }
        }

        clearTabGezinInvoer();
    }

    @FXML
    public void cancelGezinInvoer(Event evt) {
        clearTabGezinInvoer();
    }

    @FXML
    public void showStamboom(Event evt) {
        // todo opgave 3
        this.clearStamboom();
        Persoon p = cbPersonen.getSelectionModel().getSelectedItem();
        if (p == null) {
            System.out.println("Selecteer een persoon.");
            return;
        }

        if (p.getOuderlijkGezin() == null) {
            System.out.println("Geen ouderlijk gezin.");
            return;
        }
        Persoon o1 = p.getOuderlijkGezin().getOuder1();
        Persoon o2 = p.getOuderlijkGezin().getOuder2();
        //Ouder.setText() als de ouder niet null is
        if (o1 != null) {
            tbOuder1a.setText(o1.toString());
            if (o1.getOuderlijkGezin() != null) {
                //Ouder 1 kan niet null zijn, dus geen if-statement
                tbGrootouder1a.setText(o1.getOuderlijkGezin().getOuder1().toString());
                if (o1.getOuderlijkGezin().getOuder2() != null) {
                    tbGrootouder1b.setText(o1.getOuderlijkGezin().getOuder2().toString());
                }
            }
        }
        if (o2 != null) {
            tbOuder2a.setText(o2.toString());
            if (o2.getOuderlijkGezin() != null) //Ouder 1 kan niet null zijn, dus geen if-statement
            {
                tbGrootouder2a.setText(o2.getOuderlijkGezin().getOuder1().toString());
            }
            if (o2.getOuderlijkGezin().getOuder2() != null) {
                tbGrootouder2b.setText(o2.getOuderlijkGezin().getOuder2().toString());
            }
        }
    }

    @FXML
    public void createEmptyStamboom(Event evt) {
        this.clearAdministratie();
        clearTabs();
        initComboboxes();
    }

    @FXML
    public void openStamboom(Event evt) {
        // todo opgave 3
        try {
            if (withDatabase == true) {
                this.createEmptyStamboom(evt);
                this.loadFromDatabase();
            } else {
                this.createEmptyStamboom(evt);
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(
                        //new FileChooser.ExtensionFilter("All admin", "*.*"),
                        new FileChooser.ExtensionFilter("DATA", "*.data")
                );
                fc.setTitle("Open Resource File");
                File f = fc.showOpenDialog(this.getStage());
                this.deserialize(f);
            }
        } catch (IOException ex) {
            Logger.getLogger(StamboomFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.initComboboxes();
    }

    @FXML
    public void saveStamboom(Event evt) {
        // todo opgave 3
        try {
            if (withDatabase == true) {
                this.saveToDatabase();
            } else {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().addAll(
                        //new FileChooser.ExtensionFilter("All admin", "*.*"),
                        new FileChooser.ExtensionFilter("DATA", "*.data")
                );
                fc.setTitle("Open Resource File");
                File f = fc.showSaveDialog(this.getStage());
                //this.serialize(new File(FILE));
                this.serialize(f);
            }
        } catch (IOException ex) {
            Logger.getLogger(StamboomFXController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void closeApplication(Event evt) {
        saveStamboom(evt);
        getStage().close();
    }

    @FXML
    public void configureStorage(Event evt) {
        withDatabase = cmDatabase.isSelected();

    }

    @FXML
    public void selectTab(Event evt) {
        Object source = evt.getSource();
        if (source == tabPersoon) {
            clearTabPersoon();
        } else if (source == tabGezin) {
            clearTabGezin();
        } else if (source == tabPersoonInvoer) {
            clearTabPersoonInvoer();
        } else if (source == tabGezinInvoer) {
            clearTabGezinInvoer();
        }
    }

    private void clearTabs() {
        clearTabPersoon();
        clearTabPersoonInvoer();
        clearTabGezin();
        clearTabGezinInvoer();
    }

    private void clearTabPersoonInvoer() {
        //todo opgave 3
        cbNewGeslacht.getSelectionModel().selectFirst();
        tbNewVoornamen.setText("");
        tbNewAchternaam.setText("");
        tbNewTussenvoegsel.setText("");
        tbNewGebDat.setText("");
        tbNewGebPlaats.setText("");
        cbNewOuderlijkGezin.getSelectionModel().clearSelection();
        setTestwaarden();
    }

    private void clearTabGezinInvoer() {
        //todo opgave 3

    }

    private void clearTabPersoon() {
        cbPersonen.getSelectionModel().clearSelection();
        tfPersoonNr.clear();
        tfVoornamen.clear();
        tfTussenvoegsel.clear();
        tfAchternaam.clear();
        tfGeslacht.clear();
        tfGebDatum.clear();
        tfGebPlaats.clear();
        cbOuderlijkGezin.getSelectionModel().clearSelection();
        lvAlsOuderBetrokkenBij.setItems(FXCollections.emptyObservableList());
    }

    private void clearTabGezin() {
        // todo opgave 3

    }

    private void showDialog(String type, String message) {
        Stage myDialog = new Dialog(getStage(), type, message);
        myDialog.show();
    }

    private Stage getStage() {
        return (Stage) menuBar.getScene().getWindow();
    }

    private void clearStamboom() {
        tbOuder1a.clear();
        tbOuder2a.clear();
        tbGrootouder1a.clear();
        tbGrootouder1b.clear();
        tbGrootouder2a.clear();
        tbGrootouder2b.clear();
    }

    @FXML
    private void showAbout(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Info.fxml"));
            root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
