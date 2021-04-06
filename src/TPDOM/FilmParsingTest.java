package TPDOM;

public class FilmParsingTest {

	public static void main(String[] args) {
		FilmParsing parser = new FilmParsing("documents/dvd.xml");
		parser.afficherDOM();
		parser.listerActeurs();
		parser.listerDatesRetourDVD();
		parser.requetesXPath();
		parser.louerFilme("The Godfather", "Devon", "Yannick", "Île-de-France, France", "06/04/2021");
		parser.listerDatesRetourDVD(); // Pour afficher "06/04/2021" afin de confirmer la modification du DOM
	}
}
