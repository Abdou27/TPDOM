package TPDOM;

public class FilmParsingTest {
	public static void main(String[] args) {
		FilmParsing parser = new FilmParsing("documents/dvd.xml");
		
		parser.afficherDOM();
		
		parser.listerActeurs();
		
		parser.listerDatesRetourDVD();
		
		parser.requetesXPath();
		
		// Cr�er un nouveau noeud "rent"
		parser.louerFilme("The Godfather", "Devon", "Yannick", "�le-de-France, France", "06/04/2021"); 
		
		// Supprimer le noeud "rent" et cr�er un nouveau
		parser.louerFilme("The Lord of the Rings: The Return of the King", "Devon", "Yannick", "�le-de-France, France", "06/04/2021");
		
		// Afficher "06/04/2021" afin de confirmer la modification du DOM
		parser.listerDatesRetourDVD(); 
	}
}
