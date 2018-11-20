package projekti.UI;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import projekti.db.BookDAO;
import projekti.domain.Book;

public class TUI {
    private BookDAO bookDao; // voidaan antaa konstruktorin parametrina
    private IO io;
    public TUI(BookDAO bd, IO io) { 
        bookDao = bd;
        this.io = io;
    }

    public void run() throws SQLException {
        io.print("Tervetuloa lukuvinkkiapplikaatioon!\n \n");
        io.print("Tuetut toiminnot:\n ");
        io.print("\tnew \tlisää uusi lukuvinkki \n");
        io.print("\tall \tlistaa kaikki lukuvinkit \n");
        
        io.print("\tend \tsulkee ohjelman \n");
        String input = "";
        while (!input.equals("end")) {
            io.print("\ntoiminto: \n");
            input = io.GetInput();
            switch (input) { // kaikki toiminnot voidaan refaktoroida omaksi metodikseen myöhemmin
                case "new": //luodaan uusi vinkki tietokantaan
                    //Käyttäjä valitsee vinkin tyyypin, nyt vain kirjat tuettu
                    createBook();
                    continue;

                case "all": //listataan kaikki vinkit tietokannasta;
                    List<Book> books = bookDao.findAll();
                    // tulostusasun voisi määrittää kirjan toStringnä
                    books.forEach(s -> io.print(s.getAuthor() + ": " + s.getTitle() + ", ISBN: " + s.getISBN() + "\n"));
                    continue;

                case "end": 
                    io.print("\nlopetetaan ohjelman suoritus");
                    continue;
            }

            io.print("\nei tuettu toiminto \n");
        }   
    }
    private void createBook() throws SQLException {
        System.out.print("kirjailija: ");
        String author = io.GetInput();

        System.out.print("nimi: ");
        String title = io.GetInput();

        System.out.print("ISBN: ");
        String ISBN = io.GetInput();

        Book book = new Book(author, title, ISBN);

        if (!bookDao.create(book).equals(null)) {
            io.print("\nuusi vinkki lisätty \n");
        } else {
            io.print("\nvinkkiä ei lisätty \n");
        }
        // oletetaan toistaiseksi, että onnistuu. Daon kanssa ongelmia. io.print("\nuutta vinkkiä ei lisätty");
        
    }
}