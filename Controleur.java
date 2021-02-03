import javax.lang.model.util.ElementScanner6;
import javax.swing.JOptionPane;

/**
 * @author Boireau Florian, De la Jaille Jacquemine, Eyherabide Mattias, Havard Maxime
 * <br>
 * La classe Controleur permet de faire le lien entre le metier ({@linkplain TwistLock}) et l'ihm ({@linkplain Gui}) <br>
 * Ce lien ce fait grace aux diff�rents accesseurs : <ul> <li> getHauteurFrame() </li> <li> getLargeurFrame() </li> <li> getNumJoueur </li>
 * <li> etc... </li> <ul> <br>
 *
 */
public class Controleur {

	/**
	 * Permet d'activer ou non le mode d�bug. Peut �tre utile pour les d�veloppeur. (Changer le boolean en "true").
	 */
	public static final boolean DEBUG = false;

	/**
	 * Permet de faire un lien vers le metier. En locurence ici {@linkplain TwistLock}.
	 */
	private TwistLock jeu;
	
	/**
	 * Permet de faire un lien vers le metier. En locurence ici {@linkplain Gui}.
	 */
	//private Cui    ihm;
	
	private int nbJoueurs;

	private int[][] tabVals;

	public Controleur(int[][] tabVals)
	{
		nbJoueurs    = 2;
		this.tabVals = tabVals;
		creerTwistLock();
		
		
		
		//this.ihm.lancerJeu();
	}

	public void creerTwistLock()
	{
		this.jeu = new TwistLock(tabVals.length, tabVals[0].length, nbJoueurs, tabVals);
	}

	
	
	/*************************/
	/*   ACCESSEURS METIER   */
	/*************************/
	
	
	
	// Information sur les joueurs.
	public int    getNumJoueur()                   { return jeu.getNumJoueur()             ; }
	public int    getScoreJoueur  (int numJoueur)  { return jeu.getJoueurScore(numJoueur)  ; }
	public int    getNbLockJoueur (int numJoueur)  { return jeu.getJoueurNbLock(numJoueur) ; }
	public String getNomJoueur    (int numJoueur)  { return jeu.getJoueurNom(numJoueur)    ; }
	public char   getCouleurJoueur(int numJoueur)  { return jeu.getJoueurCouleur(numJoueur); }
	
	// Information sur la grille.
	public String        getGrille()        { return jeu.toString();         }
	public Conteneur[][] getTabContainer()  { return jeu.getTabContainer();  }
	public TLock[][]     getTabLock()       { return jeu.getTabLock();       }
	public String        toStringConteneur(){ return jeu.toStringConteneur();}

	public void score() { jeu.score(); }
	
	/**
	 * Permet de jouer un tour, en appelant la m�thode faireSonTour dans le metier {@linkplain TwistLock}, et de mettre
	 * ensuite � jour l'IHM {@linkplain Gui}.
	 * @param ligne
	 * @param colonne
	 */
	public void faireSonTour(int ligne, int colonne)
	{
		this.jeu.faireSonTour(ligne, colonne, nbJoueurs);
	}

	public void tourEnPlus(){this.jeu.tourEnPlus();}
	public boolean estFini()
	{
		if(jeu.estFini())
		{
			return true;
		}
		return false;
	}
	public String quiGagne(){return jeu.quiGagne();}
	

	public String stringTwistLockConteneur(){return jeu.toStringConteneur();}

	

	/*
	 * Lance le programme
	 */
	//public static void main(String[] args) { new Controleur(); }
}