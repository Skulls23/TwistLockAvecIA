import java.util.ArrayList;

/**
 * @author Boireau Florian, De la Jaille Jacquemine, Eyherabide Mattias, Havard Maxime <br>
 * Classe principale qui g�re enti�rement le jeu et la une partie. 
 */

public class TwistLock
{
	//Coloration du CUI
	public static final String ANSI_CYAN  = "\u001B[36m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED   = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	
	private        int           nbTour;
	private        Joueur[]      tabJoueur;
	private static Conteneur[][] tab2DConteneur;
	private        TLock[][]     tab2DTLock;
	
	/**
	 * Construit les tableaux de TLock et de Conteneur
	 * @param nbCaseLig
	 * @param nbCaseCol
	 */
	public TwistLock(int nbCaseLig, int nbCaseCol, int nbJoueur, int[][] tabVals)
	{
		tabJoueur    = new Joueur[nbJoueur];
		tabJoueur[0] = new Joueur("Fantasio", 'R');
		tabJoueur[1] = new Joueur("Gaston"  , 'V');
		try
		{
			tabJoueur[2] = new Joueur("Titeuf"    , 'C');
			tabJoueur[3] = new Joueur("Kid Paddle", 'J');
		}catch(Exception e) {}
		
		setupTab2D(tabVals.length, tabVals[0].length, tabVals);
		PlacerConteneurDansTab2DTLock();
		
		nbTour       = 0;
	}
	
	
	
	
	
	/**
	 * Lance les methodes pour creer les tableaux de TLock et de Conteneur
	 * @param y
	 * @param x
	 */
	public void setupTab2D(int y, int x, int[][] tabVals)
	{
		
		setupTab2DConteneur(y, x, tabVals);
		setupTab2DTLock();
	}
	
	/**
	 * Construit les TLock
	 */
	private void setupTab2DTLock()
	{
		tab2DTLock = new TLock[tab2DConteneur.length+1][tab2DConteneur[0].length+1];
		
		if ( Controleur.DEBUG )
			System.out.println("tab2DTLock = " + (tab2DConteneur.length+1) + ":" +
		                       (tab2DConteneur[0].length+1) );
		
		for ( int y = 0; y < tab2DTLock.length; y++ )
			for ( int x = 0; x < tab2DTLock[0].length; x++ )
				tab2DTLock[y][x] = new TLock();
	}
	
	/**
	 * Construit les Conteneur
	 * @param y
	 * @param x
	 */
	private void setupTab2DConteneur(int x, int y, int[][] tabVals)
	{
		tab2DConteneur = new Conteneur[x][y];
		
		for ( int x2 = 0; x2 < tab2DConteneur.length; x2++ )
			for ( int y2 = 0; y2 < tab2DConteneur[0].length; y2++ )
				tab2DConteneur[x2][y2] = new Conteneur(tabVals[x2][y2]);
	}
	
	
	
	
	
	/**
	 * Place les Conteneur dans tab2DTLock
	 */
	private void PlacerConteneurDansTab2DTLock()
	{
		for ( int y = 0; y < tab2DTLock.length; y++)
		{
			for ( int x = 0; x < tab2DTLock[0].length; x++)
			{
				if ( ( y == 0 || y == tab2DTLock.length-1    ) &&
				     ( x == 0 || x == tab2DTLock[0].length-1 )    )
				{
					PlacerConteneurDansCoteTab2DTLock(y, x);
				}
				else if ( y == 0 || y == tab2DTLock.length-1    ||
				          x == 0 || x == tab2DTLock[0].length-1    )
				{
					PlacerConteneurDansBordureTab2DTLock(y, x);
				}
				else
				{
					tab2DTLock[y][x].setTabConteneur( new Conteneur[] 
					{
							tab2DConteneur[y-1][x-1].prendreConteneur(tab2DTLock[y][x]),
							tab2DConteneur[y][x-1].prendreConteneur(tab2DTLock[y][x]),
							tab2DConteneur[y-1][x].prendreConteneur(tab2DTLock[y][x]),
							tab2DConteneur[y][x].prendreConteneur(tab2DTLock[y][x])
					});
				}
			}
		}
		
	}
	
	/**
	 * Place les Conteneurs dans les cot� de tab2DTLock, haut-gauche, bas-gauche, haut-droite, bas-droite
	 * @param y
	 * @param x
	 */
	private void PlacerConteneurDansCoteTab2DTLock(int y, int x)
	{
		if ( y == 0 && x == 0 ) 
			tab2DTLock[y][x].setTabConteneur( new Conteneur[]
			{tab2DConteneur[0][0].prendreConteneur(tab2DTLock[y][x])});
		
		if ( y == 0 && x == tab2DTLock[0].length-1 ) 
			tab2DTLock[y][x].setTabConteneur( new Conteneur[]
			{tab2DConteneur[0][tab2DConteneur[0].length-1].prendreConteneur(tab2DTLock[y][x])});
		
		if ( y == tab2DTLock.length-1 && x == 0 ) 
			tab2DTLock[y][x].setTabConteneur( new Conteneur[]
			{tab2DConteneur[tab2DConteneur.length-1][0].prendreConteneur(tab2DTLock[y][x])});
		
		if ( y == tab2DTLock.length-1 && x == tab2DTLock[0].length-1 ) 
			tab2DTLock[y][x].setTabConteneur(new Conteneur[]
			{tab2DConteneur[tab2DConteneur.length-1][tab2DConteneur[0].length-1].prendreConteneur(tab2DTLock[y][x])});
	}
	
	/**
	 * Place les Conteneurs dans la bordure de tab2DTLock
	 * @param y
	 * @param x
	 */
	private void PlacerConteneurDansBordureTab2DTLock(int y, int x)
	{
		if ( y == 0 )
			tab2DTLock[y][x].setTabConteneur( new Conteneur[] 
			{
					tab2DConteneur[0][x-1].prendreConteneur(tab2DTLock[y][x]), 
					tab2DConteneur[0][x].prendreConteneur(tab2DTLock[y][x])
			});
		
		if ( x == 0 )
			tab2DTLock[y][x].setTabConteneur( new Conteneur[] 
			{
					tab2DConteneur[y-1][0].prendreConteneur(tab2DTLock[y][x]), 
					tab2DConteneur[y][0].prendreConteneur(tab2DTLock[y][x])
			});
		
		if ( y == tab2DTLock.length-1 )
			tab2DTLock[y][x].setTabConteneur( new Conteneur[] 
			{
					tab2DConteneur[tab2DConteneur.length-1][x-1].prendreConteneur(tab2DTLock[y][x]), 
					tab2DConteneur[tab2DConteneur.length-1][x].prendreConteneur(tab2DTLock[y][x])
			});
		
		if ( x == tab2DTLock[0].length-1 )
			tab2DTLock[y][x].setTabConteneur( new Conteneur[] 
			{ 
					tab2DConteneur[y-1][tab2DConteneur[0].length-1].prendreConteneur(tab2DTLock[y][x]), 
					tab2DConteneur[y][tab2DConteneur[0].length-1].prendreConteneur(tab2DTLock[y][x])
			});	
	}
	
	
	
	
	
	/**
	 * Affecte un TLock a un joueur
	 * @param y
	 * @param x
	 * @param car
	 */
	private void setTLockAUnJoueur(int lig, int col, char car )
	{
		if ( Controleur.DEBUG ) System.out.println(lig + ":" + col);
		
		if ( lig >= 0 && lig < tab2DTLock.length    &&
		     col >= 0 && col < tab2DTLock[0].length &&
		     tab2DTLock[lig][col].getDetenuPar() == ' ' )
		{
			
			tab2DTLock[lig][col].setDetenuPar(car);
			if ( Controleur.DEBUG ) System.out.println("fait");
		}
		else
		{
			getJoueur(car).enleverTLockSup();
		}
	}
	
	/**
	 * Retourne le nom du gagnant
	 */
	public String quiGagne()
	{
		int numJoueurPlusHaut = -1;
		int numPointsPlusHaut = 0;
		
		
		
		//Parcours pour connaitre le gagnant
		for(int i=0; i<tabJoueur.length;i++)
		{
			if(tabJoueur[i].getNbPoints() > numPointsPlusHaut)
			{
				numJoueurPlusHaut = i;
				numPointsPlusHaut = tabJoueur[i].getNbPoints();
			}
		}
		
		ArrayList<Joueur> alAutreGagnant = new ArrayList<Joueur>();
		alAutreGagnant.add(tabJoueur[numJoueurPlusHaut]);
		
		//Parcours pour connaitre les �galit�s avec le gagnant
		for(int i=0; i<tabJoueur.length;i++)
		{
			if(tabJoueur[i].getNbPoints() == numPointsPlusHaut && i != numJoueurPlusHaut)
			{
				alAutreGagnant.add(tabJoueur[i]);
			}
		}
		
		//en cas d'egalit� a 0 points
		if(numJoueurPlusHaut == -1)
			return "�GALIT� A 0 POINTS! PERSONNE NE GAGNE!";
		
		//Preparation du nom de tout les gagnants
		String s = alAutreGagnant.get(0).getNom() + "";
		try
		{
			for(int i=1; i<alAutreGagnant.size();i++)
				s += " ET " + alAutreGagnant.get(0).getNom();
		}catch(Exception e) {}
		
		
		return s + " GAGNE LA PARTIE AVEC " + numPointsPlusHaut + " POINTS! BRAVO!";
		
		
	}
	
	/**
	 * Permet de lancer le tour d'un joueur
	 * @param y
	 * @param x
	 */
	public void faireSonTour(int lig, int col, int nbJoueurs)
	{
		//if(tabJoueur[nbTour%nbJoueurs].getNbLock() > 0)
		//{
			setTLockAUnJoueur(lig, col, tabJoueur[nbTour%nbJoueurs].getCouleur());
			tabJoueur[nbTour%nbJoueurs].joueurJoue();
		//}
		nbTour++;
	}
	
	/**
	 * Compte le score de chaque joueur
	 */
	public void score()
	{
		for ( int cpt = 0; cpt < tabJoueur.length; cpt++)
		{
			int score=0;
			
			for ( int y=0; y < tab2DConteneur.length; y++ )
				for ( int x=0; x < tab2DConteneur[0].length; x++ )
					if ( tab2DConteneur[y][x].getCouleur() == tabJoueur[cpt].getCouleur() )
						score += tab2DConteneur[y][x].getPoints() ;
			
			tabJoueur[cpt].setScore(score);
		}
	}
	
	
	
	public String toString()
	{
		String s="";
		
		s += "     ";
		for ( int x=0; x < tab2DTLock[0].length; x++ )
		{
			s += ":" + (char)('A'+x) + ":  ";
		}
		s += "\n";
		
		for (int y=0; y < tab2DTLock.length-1; y++ )
		{
			s += ":" + String.format("%2d", y ) + ": ";
			
			for ( int x=0; x < tab2DTLock[0].length; x++ )
			{
				s += tab2DTLock[y][x] + "  ";
			}
			
			if ( y == 3 )
			{
				s += "\t Joueur 1 : " + tabJoueur[0].getNbPoints();
				s += "    Nombre de Lock : "     + tabJoueur[0].getNbLock();
			}
			if ( y == 5 )
			{
				s += "\t Joueur 2 : " + tabJoueur[1].getNbPoints();
				s += "    Nombre de Lock : "       + tabJoueur[1].getNbLock();
			}
		
			
			s += "\n     ";
			for ( int x=0; x < tab2DConteneur[0].length && y < tab2DConteneur.length; x++ )
			{
				s += "   " + tab2DConteneur[y][x];
				
			}
			s += "\n";
		}
		
		s += ":" + String.format("%2d", tab2DTLock.length-1 ) + ": ";
		for ( int x=0; x < tab2DTLock[0].length; x++ )
		{
			s += tab2DTLock[tab2DTLock.length-1][x];
			s += "  ";
		}
		
		return s;
	}
	
	
	public boolean tabLockComplet()
	{
		boolean bRes = true;
		for(int i = 0; i<tab2DTLock.length; i++)
			for(int j = 0; j<tab2DTLock[0].length; j++)
				if(tab2DTLock[i][j].getDetenuPar() == ' ')
					bRes = false;
		return bRes;
	}
	
	public void tourEnPlus()
	{
		nbTour++;
	}
	
	public Joueur getJoueur(char c)
	{
		if(c == 'R') return tabJoueur[0];
		if(c == 'V') return tabJoueur[1];
		if(c == 'C') return tabJoueur[2];
		if(c == 'J') return tabJoueur[3];
		return null;
	}
	
	public Conteneur[][] getTabContainer()             { return tab2DConteneur                  ;}
	public TLock[][]     getTabLock()                  { return tab2DTLock                      ;}
	public int           getNumJoueur()                { return nbTour%tabJoueur.length + 1     ;}
	public int           getJoueurScore(int nJoueur)   { return tabJoueur[nJoueur].getNbPoints();}
	public int           getJoueurNbLock(int nJoueur)  { return tabJoueur[nJoueur].getNbLock()  ;}
	public char          getJoueurCouleur(int nJoueur) { return tabJoueur[nJoueur].getCouleur() ;}
	public String        getJoueurNom(int nJoueur)     { return tabJoueur[nJoueur].getNom()     ;}
	
	public boolean estFini()
	{
		boolean bRes = true;
		for(int i = 0; i<tabJoueur.length; i++)
		{
			if(tabJoueur[i].getNbLock() > 0)
				bRes = false;
		}
		return bRes || tabLockComplet();
	}

	/**
	 * Retourne sous forme de String les valeurs de tout les conteneurs
	 * @return String
	 */
	public String toStringConteneur()
	{
		String ret = "";
		for(int i = 0; i<tab2DConteneur.length; i++)
		{
			for(int j = 0; j<tab2DConteneur[i].length; j++)
			{
				String coul = "";
				if(tab2DConteneur[i][j].getCouleur() == ' ') coul = " ";
				if(tab2DConteneur[i][j].getCouleur() == 'R') coul = "R";
				if(tab2DConteneur[i][j].getCouleur() == 'V') coul = "V";

				if(j == tab2DConteneur[i].length-1)
					ret+= tab2DConteneur[i][j].getPoints() + coul + "|\n";
				else
					ret+= tab2DConteneur[i][j].getPoints() + coul + ":";
			}
		}
		return ret;
	}
	
}
