import java.util.ArrayList;

/**
 * @author Boireau Florian, De la Jaille Jacquemine, Eyherabide Mattias, Havard Maxime
 * Permet de contruire le conteneur de points.
 */
public class Conteneur
{
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RED   = "\u001B[31m";
	public static final String ANSI_RESET = "\u001B[0m" ;
	
	/**
	 * nombre de points que vaut ce conteneur
	 */
	private final int  POINTS;
	private       int  valeur; 
	private       char couleur = ' ';

	private ArrayList<TLock> alTL = new ArrayList<TLock>();
	
	public Conteneur(TLock t)
	{
		POINTS = (int) (Math.random()*50)+5;
		valeur = 0;
	}

	public Conteneur(int i)
	{
		POINTS = i;
		valeur = 0;
	}
	
	/**
	 * Ajoute val a la valeur actuelle du conteneur
	 * @param val
	 * @return
	 */
	public void setCouleur(char couleur)
	{
		this.couleur = couleur;
	}

	public Conteneur prendreConteneur(TLock tl)
	{
		this.alTL.add(tl);
		return this;
	}
	
	public int getPoints() { return POINTS ;}
	
	public char getCouleur()
	{
		return couleur;
	}

	public int getNbTLRouge()
	{
		int nbR=0;
		for(int i=0; i<alTL.size(); i++)
		{
			if(alTL.get(i).getDetenuPar() == 'R')
				nbR++;
		}
		return nbR;
	}

	public int getNbTLVert()
	{
		int nbV=0;
		for(int i=0; i<alTL.size(); i++)
		{
			if(alTL.get(i).getDetenuPar() == 'V')
				nbV++;
		}
		return nbV;
	}
	
	public String toString()
	{
		return String.format("%02d", POINTS);
	}
}