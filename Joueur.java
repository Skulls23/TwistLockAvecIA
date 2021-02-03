/**
 * 
 * @author Boireau Florian, De la Jaille Jacquemine, Eyherabide Mattias, Havard Maxime
 * Classe permettant d'avoir toutes les infos sur le joueur courant.
 */
public class Joueur
{
	private String nom;
	
	private int    nbPoints;
	
	private char   couleur;
	
	/**
	 * Nombre de lock jouable par le joueur
	 */
	private int    lockDetenu;
	
	public Joueur(String nom, char couleur) {
		this.nom        = nom;
		this.couleur    = couleur;
		this.lockDetenu = 5;
		this.nbPoints   = 0;
	}
	
	public void setScore(int nbPoints) { this.nbPoints = nbPoints ;}
	
	public char   getCouleur()  { return this.couleur    ;}
	public int    getNbLock()   { return this.lockDetenu ;}
	public int    getNbPoints() { return this.nbPoints   ;}
	public String getNom()      { return this.nom        ;}
	
	public boolean aFini() { return this.lockDetenu <= 0 ;}
	
	public void joueurJoue     () { this.lockDetenu-- ;}
	public void enleverTLockSup() { this.lockDetenu-- ;}
	
	public String toString()
	{
		String s="";
		
		s += "" + this.couleur + "  nbPoints : " + this.nbPoints;
		
		return s;
	}
}
