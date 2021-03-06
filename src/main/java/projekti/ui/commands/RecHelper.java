package projekti.ui.commands;



import projekti.ui.IO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import projekti.domain.*;
import projekti.domain.Book.Properties;
import projekti.language.LanguageKeys;
import projekti.language.Locale;
import projekti.util.Check;


public class RecHelper {
    private IO io;
    private DBHelper db;
    private Locale locale;

    private List<Integer> IDList;

    public RecHelper(IO io, DBHelper db, Locale locale) {
        this.io = io;
        this.db = db;
        this.locale = locale;
    }

    public Recommendation askForRecommendation() throws SQLException {
        Integer ID = selectID();

        Recommendation recommendation = db.retrieve(ID);
        Check.notNull(recommendation, () -> new IllegalArgumentException(locale.get(LanguageKeys.NOTFOUND)));
        return recommendation;
    }

    public String askType() {
        io.println(locale.get(LanguageKeys.TYPELIST));
        return io.getInput().toUpperCase();
    }

    public List<Recommendation> getAllRecommendations() throws SQLException {
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.addAll(db.getDAO().findAll("BOOK"));
        recommendations.addAll(db.getDAO().findAll("BLOG"));
        recommendations.addAll(db.getDAO().findAll("OTHER"));
        return recommendations;
    }

    /**
     * updates the IDList (list of "fake" IDs displayed to the user, used for
     * selecting, updating or deleting a Recommendation)
     * @param allRecommendations list of all Recommendation, already obtained
     * by calling getAllRecommendations()
     */
    public void updateIDList(List<Recommendation> allRecommendations) {
        this.IDList = allRecommendations.stream()
                .map(r -> r.getProperty(Properties.ID).orElse(-1))
                .collect(Collectors.toList());
    }

    /**
     * fetches all Recommendations from the database and updates the IDList
     * (list of "fake" IDs displayed to the user, used for selecting,
     * updating or deleting a Recommendation)
     * @throws SQLException
     */
    public void updateIDList() throws SQLException {
        updateIDList(getAllRecommendations());
    }

    /**
     * returns the list ID (the one used by the user to refer to a specific
     * recommendation) of the recommendation given as a parameter
     * @param recommendation the given recommendation
     * @return the recommendation's list ID
     */
    public Integer getListID(Recommendation recommendation) {
        Integer ID = recommendation.getProperty(Properties.ID).orElse(null);
        return IDList.indexOf(ID);
    }

    public Integer selectID() { //public until all IDList functionality is a part of RecHelper
        io.println(locale.get(LanguageKeys.SELECTIDQUERY));
        io.print("ID: ");
        String id_String = io.getInput();
        Integer ID;
        try {
            ID = Integer.parseInt(id_String);
            if (ID < 0 || ID >= IDList.size()) {
                // just some value that can't be a true ID in the database
                return -1;
            }
            return IDList.get(ID);
        } catch (IllegalArgumentException ex) {
            if (!id_String.isEmpty()) {
                io.println(locale.get(LanguageKeys.NONVALIDID));
            }
            throw ex;
        }
    }

    public boolean confirm(String message) {
        io.println(message);
        String optionString = "y/n";
        io.println(optionString);
        String val = io.getInput();
        if (val.toLowerCase().equals("y")) {
            return true;
        } else if (val.toLowerCase().equals("n")) {
            return false;
        } else {
            String failMessage = locale.get(LanguageKeys.CONFIRMFAIL);
            io.println(failMessage);
            return confirm(message);
        }
    }

    public List<Integer> getIDList() {
        return this.IDList;
    }

    public IO getIO() {
        return this.io;
    }

    public Locale getLocale() {
    	return this.locale;
    }

    public void setLocale(Locale locale) {
    	this.locale = locale;
    }


}