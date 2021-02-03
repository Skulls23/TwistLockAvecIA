

public class TLock
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED   = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    
    /**
     * A qui appartient ce lock (V ou R)
     */
    private char        detenuPar;
    private Conteneur[] tabConteneur;

    /**
     * Construit le Lock, il est par defaut defini comme appartenant a ' ', donc personne
     */
    public TLock()
    {
        detenuPar = ' ';
    }
    
    public void setTabConteneur(Conteneur[] tab) { tabConteneur = tab ;}
    
    /**
     * Permet de definir a qui appartient ce Lock
     * @param car
     */
    public void setDetenuPar(char car ) 
    {
        int nbTLR;
        int nbTLV;
        switch (detenuPar)
        {
            case 'V' : 
                //on parcous les conteneurs autour
                for ( int i=0; i < tabConteneur.length; i++)
                {
                    if(car == 'R')
                    {
                        nbTLR = tabConteneur[i].getNbTLRouge()+1;
                        nbTLV = tabConteneur[i].getNbTLVert();
                    }
                    else
                    {
                        nbTLR = tabConteneur[i].getNbTLRouge();
                        nbTLV = tabConteneur[i].getNbTLVert()+1;
                    }
                    
                    //si c'est une couleur sur une couleur autre que la couleur voulue, elle devient blanche
                    if(tabConteneur[i].getCouleur() != ' ' && tabConteneur[i].getCouleur() != car && nbTLR == nbTLV)
                        tabConteneur[i].setCouleur(' ');
                    else
                        if(nbTLR > nbTLV)
                            tabConteneur[i].setCouleur(car);
                }
            break;

            case 'R' : 
                for ( int i=0; i < tabConteneur.length; i++)
                {
                    if(car == 'R')
                    {
                        nbTLR = tabConteneur[i].getNbTLRouge()+1;
                        nbTLV = tabConteneur[i].getNbTLVert();
                    }
                    else
                    {
                        nbTLR = tabConteneur[i].getNbTLRouge();
                        nbTLV = tabConteneur[i].getNbTLVert()+1;
                    }

                    if(tabConteneur[i].getCouleur() != ' ' && nbTLR == nbTLV)
                        tabConteneur[i].setCouleur(' ');
                    else
                        if(nbTLR > nbTLV)
                            tabConteneur[i].setCouleur(car);
                }
            break;
            
            case 'C' : 
                for ( int i=0; i < tabConteneur.length; i++)
                {
                    if(tabConteneur[i].getCouleur() != ' ' && tabConteneur[i].getCouleur() != car)
                        tabConteneur[i].setCouleur(' ');
                    else
                        tabConteneur[i].setCouleur(car);
                }
            break;
            
            case 'J' : 
                for ( int i=0; i < tabConteneur.length; i++)
                {
                    if(tabConteneur[i].getCouleur() != ' ' && tabConteneur[i].getCouleur() != car)
                        tabConteneur[i].setCouleur(' ');
                    else
                        tabConteneur[i].setCouleur(car);
                }
            break;
                
            case ' ' :
                for ( int i=0; i < tabConteneur.length; i++)
                {
                    if(car == 'R')
                    {
                        nbTLR = tabConteneur[i].getNbTLRouge()+1;
                        nbTLV = tabConteneur[i].getNbTLVert();

                        if(tabConteneur[i].getCouleur() != ' ' && tabConteneur[i].getCouleur() != car && nbTLR == nbTLV)
                        tabConteneur[i].setCouleur(' ');
                        else
                            if(nbTLR > nbTLV)
                                tabConteneur[i].setCouleur(car);
                    }
                    else
                    {
                        nbTLR = tabConteneur[i].getNbTLRouge();
                        nbTLV = tabConteneur[i].getNbTLVert()+1;

                        if(tabConteneur[i].getCouleur() != ' ' && tabConteneur[i].getCouleur() != car && nbTLR == nbTLV)
                        tabConteneur[i].setCouleur(' ');
                        else
                            if(nbTLR < nbTLV)
                                tabConteneur[i].setCouleur(car);
                    }

                    
                }
        }
        
        detenuPar = car ;
    }

    public int simulerCoup(char joueur)
    {
        int points = 0;
        for ( int cpt = 0; cpt < tabConteneur.length; cpt++)
        {
            if ( tabConteneur[cpt].getCouleur() == joueur)
            {
                points += tabConteneur[cpt].getPoints();
            }
        }

        
        char proprietaireInitial = detenuPar;
        setDetenuPar(joueur);
        
        int nbPoints = 0;
        for (int cpt=0; cpt < tabConteneur.length; cpt++)
        {
            if ( tabConteneur[cpt].getCouleur() == joueur)
            {
                nbPoints += tabConteneur[cpt].getPoints();
            }
        }
        
        setDetenuPar(proprietaireInitial);
        return nbPoints - points;
    }
    
    
    public char        getDetenuPar()    { return detenuPar    ;}
    public Conteneur[] getTabConteneur() { return tabConteneur ;}
    
    public String toString()
    {
        return "[" + detenuPar + "]";
    }
}