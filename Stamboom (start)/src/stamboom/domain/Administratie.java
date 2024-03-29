package stamboom.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Administratie implements Serializable {

    //************************datavelden*************************************
    private int nextGezinsNr;
    private int nextPersNr;
    private final List<Persoon> personen;
    private final List<Gezin> gezinnen;
    private transient ObservableList<Persoon> observablePersonen;
    private transient ObservableList<Gezin> observableGezinnen;

    //***********************constructoren***********************************
    /**
     * er wordt een lege administratie aangemaakt. personen en gezinnen die in
     * de toekomst zullen worden gecreeerd, worden (apart) opvolgend genummerd
     * vanaf 1
     */
    public Administratie() {
        //todo opgave 1
        // zet beide integer variabelen op de waarde 1
        nextGezinsNr = 1;
        nextPersNr = 1;
        // Initialiseert de persoon en gezinnen lijst.
        this.personen = new ArrayList<>();
        this.gezinnen = new ArrayList<>();
        observablePersonen = FXCollections.observableList(personen);
        observableGezinnen = FXCollections.observableList(gezinnen);
    }

    //**********************methoden****************************************
    /**
     * er wordt een persoon met de gegeven parameters aangemaakt; de persoon
     * krijgt een uniek nummer toegewezen, en de persoon is voortaan ook bij het
     * (eventuele) ouderlijk gezin bekend. Voor de voornamen, achternaam en
     * gebplaats geldt dat de eerste letter naar een hoofdletter en de
     * resterende letters naar kleine letters zijn geconverteerd; het
     * tussenvoegsel is in zijn geheel geconverteerd naar kleine letters;
     * overbodige spaties zijn verwijderd
     *
     * @param geslacht
     * @param vnamen vnamen.length>0; alle strings zijn niet leeg
     * @param anaam niet leeg
     * @param tvoegsel mag leeg zijn
     * @param gebdat
     * @param gebplaats niet leeg
     * @param ouderlijkGezin mag de waarde null (=onbekend) hebben
     *
     * @return de nieuwe persoon. Als de persoon al bekend was (op basis van
     * combinatie van getNaam(), geboorteplaats en geboortedatum), wordt er null
     * geretourneerd.
     */
    public Persoon addPersoon(Geslacht geslacht, String[] vnamen, String anaam,
            String tvoegsel, Calendar gebdat,
            String gebplaats, Gezin ouderlijkGezin) {
        //todo opgave 1
        if (vnamen.length == 0) {
            throw new IllegalArgumentException("ten minste 1 voornaam");
        }

        if (geslacht == null) {
            throw new IllegalArgumentException("voer geslacht in");
        }

        if (gebdat == null) {
            throw new IllegalArgumentException("geboortedatum mag niet leeg zijn");
        }

        //for (String voornaam : vnamen) {
        //    if (voornaam.trim().isEmpty()) {
        //        throw new IllegalArgumentException("lege voornaam is niet toegestaan");
        //    } else {
        //        // Format de voornamen
        //        voornaam = voornaam.substring(0,1).toUpperCase() + voornaam.substring(1).toLowerCase();
        //    }
        //}
        for (int i = 0; i < vnamen.length; i++) {
            if (vnamen[i].trim().isEmpty()) {
                throw new IllegalArgumentException("lege voornaam is niet toegestaan");
            } else {
                // Format de voornamen
                vnamen[i] = vnamen[i].trim();
                vnamen[i] = vnamen[i].substring(0, 1).toUpperCase() + vnamen[i].substring(1).toLowerCase();
            }
        }

        if (anaam.trim().isEmpty()) {
            throw new IllegalArgumentException("lege achternaam is niet toegestaan");
        } else {
            // format de achternaam
            anaam = anaam.trim();
            anaam = anaam.substring(0, 1).toUpperCase() + anaam.substring(1).toLowerCase();
        }

        if (gebplaats.trim().isEmpty()) {
            throw new IllegalArgumentException("lege geboorteplaats is niet toegestaan");
        } else {
            // format de geboorteplaats
            gebplaats = gebplaats.trim();
            gebplaats = gebplaats.substring(0, 1).toUpperCase() + gebplaats.substring(1).toLowerCase();
        }

        Persoon persoon = new Persoon(nextPersNr, vnamen, anaam, tvoegsel.toLowerCase(),
                gebdat, gebplaats, geslacht, ouderlijkGezin);
        for (Persoon p : this.personen) {
            if (p.getNaam().equals(persoon.getNaam())
                    && p.getGebDat().get(Calendar.DATE) == persoon.getGebDat().get(Calendar.DATE)
                    && p.getGebDat().get(Calendar.MONTH) == persoon.getGebDat().get(Calendar.MONTH)
                    && p.getGebDat().get(Calendar.YEAR) == persoon.getGebDat().get(Calendar.YEAR)
                    && p.getGebPlaats().equals(persoon.getGebPlaats())) {
                return null;
            }
        }

        this.observablePersonen.add(persoon);
        nextPersNr++;

        if (ouderlijkGezin != null) {
            ouderlijkGezin.breidUitMet(persoon);
        }

        return persoon;
    }

    /**
     * er wordt, zo mogelijk (zie return) een (kinderloos) ongehuwd gezin met
     * ouder1 en ouder2 als ouders gecreeerd; de huwelijks- en scheidingsdatum
     * zijn onbekend (null); het gezin krijgt een uniek nummer toegewezen; dit
     * gezin wordt ook bij de afzonderlijke ouders geregistreerd;
     *
     * @param ouder1
     * @param ouder2 mag null zijn
     *
     * @return het nieuwe gezin. null als ouder1 = ouder2 of als een van de
     * volgende voorwaarden wordt overtreden: 1) een van de ouders is op dit
     * moment getrouwd 2) het koppel vormt al een ander gezin
     */
    public Gezin addOngehuwdGezin(Persoon ouder1, Persoon ouder2) {
        if (ouder1 == ouder2) {
            return null;
        }

        if (ouder1.getGebDat().compareTo(Calendar.getInstance()) > 0) {
            return null;
        }
        if (ouder2 != null && ouder2.getGebDat().compareTo(Calendar.getInstance()) > 0) {
            return null;
        }

        Calendar nu = Calendar.getInstance();
        System.out.println(ouder1.isGetrouwdOp(nu));
        System.out.println(ongehuwdGezinBestaat(ouder1, ouder2));
        if (ouder1.isGetrouwdOp(nu) || 
                (ouder2 != null && ouder2.isGetrouwdOp(nu))
                || ongehuwdGezinBestaat(ouder1, ouder2)) {
            return null;
        }

        Gezin gezin = new Gezin(nextGezinsNr++, ouder1, ouder2);
        this.observableGezinnen.add(gezin);

        ouder1.wordtOuderIn(gezin);
        if (ouder2 != null) {
            ouder2.wordtOuderIn(gezin);
        }

        return gezin;
    }

    /**
     * Als het ouderlijk gezin van persoon nog onbekend is dan wordt persoon een
     * kind van ouderlijkGezin, en tevens wordt persoon als kind in dat gezin
     * geregistreerd. Als de ouders bij aanroep al bekend zijn, verandert er
     * niets
     *
     * @param persoon
     * @param ouderlijkGezin
     * @return of ouderlijk gezin kon worden toegevoegd.
     */
    public boolean setOuders(Persoon persoon, Gezin ouderlijkGezin) {
        return persoon.setOuders(ouderlijkGezin);
    }

    /**
     * als de ouders van dit gezin gehuwd zijn en nog niet gescheiden en datum
     * na de huwelijksdatum ligt, wordt dit de scheidingsdatum. Anders gebeurt
     * er niets.
     *
     * @param gezin
     * @param datum
     * @return true als scheiding geaccepteerd, anders false
     */
    public boolean setScheiding(Gezin gezin, Calendar datum) {
        return gezin.setScheiding(datum);
    }

    /**
     * registreert het huwelijk, mits gezin nog geen huwelijk is en beide ouders
     * op deze datum mogen trouwen (pas op: het is niet toegestaan dat een ouder
     * met een toekomstige (andere) trouwdatum trouwt.)
     *
     * @param gezin
     * @param datum de huwelijksdatum
     * @return false als huwelijk niet mocht worden voltrokken, anders true
     */
    public boolean setHuwelijk(Gezin gezin, Calendar datum) {
        return gezin.setHuwelijk(datum);
    }

    /**
     *
     * @param ouder1
     * @param ouder2
     * @return true als dit koppel (ouder1,ouder2) al een ongehuwd gezin vormt
     */
    boolean ongehuwdGezinBestaat(Persoon ouder1, Persoon ouder2) {
        return ouder1.heeftOngehuwdGezinMet(ouder2) != null;
    }

    /**
     * als er al een ongehuwd gezin voor dit koppel bestaat, wordt het huwelijk
     * voltrokken, anders wordt er zo mogelijk (zie return) een (kinderloos)
     * gehuwd gezin met ouder1 en ouder2 als ouders gecreeerd; de
     * scheidingsdatum is onbekend (null); het gezin krijgt een uniek nummer
     * toegewezen; dit gezin wordt ook bij de afzonderlijke ouders
     * geregistreerd;
     *
     * @param ouder1
     * @param ouder2
     * @param huwdatum
     * @return null als ouder1 = ouder2 of als een van de ouders getrouwd is
     * anders het gehuwde gezin
     */
    public Gezin addHuwelijk(Persoon ouder1, Persoon ouder2, Calendar huwdatum) {
        //todo opgave 1
        //Calendar now = Calendar.getInstance();       
        Gezin nieuwGezin = null;

        // Controleert of de ouders gelijk aan elkaar zijn en of ze 18+ zijn op
        // de datum dat het huwelijk wordt voltrokken
        if (ouder1 == ouder2)
            return null;
                //|| (now.get(Calendar.YEAR) - ouder1.getGebDat().get(Calendar.YEAR) < 18)
                //|| (now.get(Calendar.YEAR) - ouder2.getGebDat().get(Calendar.YEAR) < 18)
        if((huwdatum.get(Calendar.YEAR) - ouder1.getGebDat().get(Calendar.YEAR) < 18)
                || (huwdatum.get(Calendar.YEAR) - ouder2.getGebDat().get(Calendar.YEAR) < 18)) {
            //throw exception test
            return null;
            //return null;
        }

        // Gaat de lijst met gezinnen af en controleert of een van de ouders al
        // gehuwd is op de meegegeven datum
        for (Gezin g : this.gezinnen) {
            // Controle voor ouder 1
            if (g.getOuder1() == ouder1
                    || g.getOuder2() == ouder1) {
                if (g.getHuwelijksdatum() != null
                        && (g.getScheidingsdatum() == null
                        || huwdatum.before(g.getScheidingsdatum()))) {
                    return null;
                }
            }
            // Controle voor ouder 2
            if (g.getOuder1() == ouder2
                    || g.getOuder2() == ouder2) {
                if (g.getHuwelijksdatum() != null
                        && (g.getScheidingsdatum() == null
                        || huwdatum.before(g.getScheidingsdatum()))) {
                    return null;
                }
            }
        }

        // Gaat de lijst met personen af om te kijken of een van de ouders al een
        // ongehuwd gezin heeft met de ander, anders wordt er een nieuw gezin 
        // aangemaakt
        for (Persoon p : this.personen) {
            if (p.equals(ouder1)) {
                nieuwGezin = ouder1.heeftOngehuwdGezinMet(ouder2);
                if (nieuwGezin != null) {
                    ouder1.heeftOngehuwdGezinMet(ouder2).setHuwelijk(huwdatum);
                } else {
                    nieuwGezin = new Gezin(this.nextGezinsNr, ouder1, ouder2);
                    nieuwGezin.setHuwelijk(huwdatum);
                    this.nextGezinsNr++;
                    ouder1.wordtOuderIn(nieuwGezin);
                    ouder2.wordtOuderIn(nieuwGezin);
                    this.observableGezinnen.add(nieuwGezin);
                }
            }
        }
        return nieuwGezin;
    }

    /**
     *
     * @return het aantal geregistreerde personen
     */
    public int aantalGeregistreerdePersonen() {
        return nextPersNr - 1;
    }

    /**
     *
     * @return het aantal geregistreerde gezinnen
     */
    public int aantalGeregistreerdeGezinnen() {
        return nextGezinsNr - 1;
    }

    /**
     *
     * @param nr
     * @return de persoon met nummer nr, als die niet bekend is wordt er null
     * geretourneerd
     */
    public Persoon getPersoon(int nr) {
        //todo opgave 1
        //aanname: er worden geen personen verwijderd
        Persoon result = null;

        for (Persoon persoon : this.personen) {
            if (persoon.getNr() == nr) {
                result = persoon;
                return result;
            }
        }
        return result;
    }

    /**
     * @param achternaam
     * @return alle personen met een achternaam gelijk aan de meegegeven
     * achternaam (ongeacht hoofd- en kleine letters)
     */
    public ArrayList<Persoon> getPersonenMetAchternaam(String achternaam) {
        //todo opgave 1
        List<Persoon> result = new ArrayList<>();

        for (Persoon persoon : this.personen) {
            if (persoon.getAchternaam().toLowerCase() == null
                    ? achternaam.toLowerCase() == null
                    : persoon.getAchternaam()
                    .toLowerCase().equals(achternaam.toLowerCase())) {
                result.add(persoon);
            }
        }
        return (ArrayList) result;
    }

    /**
     *
     * @return de geregistreerde personen
     */
    public ObservableList<Persoon> getPersonen() {
        // todo opgave 1
        return FXCollections.unmodifiableObservableList(observablePersonen);
    }

    /**
     *
     * @param vnamen
     * @param anaam
     * @param tvoegsel
     * @param gebdat
     * @param gebplaats
     * @return de persoon met dezelfde initialen, tussenvoegsel, achternaam,
     * geboortedatum en -plaats mits bekend (ongeacht hoofd- en kleine letters),
     * anders null
     */
    public Persoon getPersoon(String[] vnamen, String anaam, String tvoegsel,
            Calendar gebdat, String gebplaats) {
        //todo opgave 1
        Persoon result = null;

        for (Persoon persoon : this.personen) {
            String initialen = "";
            for (String s : vnamen) {
                if (s.length() >= 1) {
                    initialen += s.substring(0, 1).toUpperCase() + ".";
                }
            }

            //Beginnen met hoofdletters
            anaam = anaam.substring(0, 1).toUpperCase() + anaam.substring(1);
            gebplaats = gebplaats.substring(0, 1).toUpperCase() + gebplaats.substring(1);

            if (persoon.getInitialen().equals(initialen.trim())
                    && persoon.getAchternaam().equals(anaam)
                    && persoon.getTussenvoegsel().equals(tvoegsel)
                    && persoon.getGebDat().get(Calendar.DATE) == gebdat.get(Calendar.DATE)
                    && persoon.getGebDat().get(Calendar.MONTH) == gebdat.get(Calendar.MONTH)
                    && persoon.getGebDat().get(Calendar.YEAR) == gebdat.get(Calendar.YEAR)
                    && persoon.getGebPlaats().equals(gebplaats)) {

                result = persoon;
                return result;
            }
        }

        return result;
    }

    /**
     *
     * @return de geregistreerde gezinnen
     */
    public ObservableList<Gezin> getGezinnen() {
        return FXCollections.unmodifiableObservableList(observableGezinnen);
    }

    /**
     *
     * @param gezinsNr
     * @return het gezin met nummer nr. Als dat niet bekend is wordt er null
     * geretourneerd
     */
    public Gezin getGezin(int gezinsNr) {
        // aanname: er worden geen gezinnen verwijderd
        // Logica fout, 1 <= gezinnen.size werd gezinsNr <= gezinnen.size
        if (gezinnen != null && 1 <= gezinsNr && gezinsNr <= gezinnen.size()) {
            return gezinnen.get(gezinsNr - 1);
        }
        return null;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        observablePersonen = FXCollections.observableList(personen);
        observableGezinnen = FXCollections.observableList(gezinnen);
    }
}
