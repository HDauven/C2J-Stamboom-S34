/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import stamboom.domain.Administratie;
import stamboom.domain.Gezin;
import stamboom.domain.Persoon;

/**
 * FXML Controller class
 *
 * @author Stef
 */
public class ShowStamboomController implements Initializable {

    @FXML
    private Label lblPersoon;
    @FXML
    private Label lblGrootouder1;
    @FXML
    private Label lblGrootouder2;
    @FXML
    private TextField tbOuder1;
    @FXML
    private TextField tbOuder2;
    @FXML
    private ComboBox<Gezin> cbGezinnen;

    @FXML
    private TextField tbGrootouder1a;
    @FXML
    private TextField tbGrootouder2a;
    @FXML
    private TextField tbGrootouder1b;
    @FXML
    private TextField tbGrootouder2b;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setMyData(Persoon p, Administratie admin) {
        lblPersoon.setText(p.getNaam());
        Persoon o1 = p.getOuderlijkGezin().getOuder1();
        Persoon o2 = p.getOuderlijkGezin().getOuder2();
        //Ouder.setText() als de ouder niet null is
        if (o1 != null) {
            tbOuder1.setText(o1.toString());
            if (o1.getOuderlijkGezin() != null) {
                //Ouder 1 kan niet null zijn, dus geen if-statement
                tbGrootouder1a.setText(o1.getOuderlijkGezin().getOuder1().toString());
                if (o1.getOuderlijkGezin().getOuder2() != null) {
                    tbGrootouder1b.setText(o1.getOuderlijkGezin().getOuder2().toString());
                }
            }
        }
        if (o2 != null) {
            tbOuder2.setText(o2.toString());
            if (o2.getOuderlijkGezin() != null) //Ouder 1 kan niet null zijn, dus geen if-statement
            {
                tbGrootouder2a.setText(o2.getOuderlijkGezin().getOuder1().toString());
            }
            if (o2.getOuderlijkGezin().getOuder2() != null) {
                tbGrootouder2b.setText(o2.getOuderlijkGezin().getOuder2().toString());
            }
        }

        cbGezinnen.setItems(admin.getGezinnen());
    }

}
