import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Boireau Florian, De la Jaille Jacquemine, Eyherabide Mattias, Havard Maxime <br>
 */
public class IA
{
    public static void main (final String args[])
    {
        try
        {
            
            Controleur     controleur           = null; //Fausse initialisation, pour eviter les may not have been initialized
            TLock[][]      tab2DLocksControleur = null; //Fausse initialisation, pour eviter les may not have been initialized

            Scanner        sc   = new Scanner(System.in);
            DatagramSocket ds   = new DatagramSocket();
            char           coul = ' ';
            int port;
            
            String         message;
            DatagramPacket io;
            int[][]        tabVals;

            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {port = 8080;}

            //envoi du nom de l'equipe
            message = "Equipe 16";
            io = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("localhost"), port) ;
            ds.send(io);

            while(! ds.isClosed())
            {
                final DatagramPacket dp = new DatagramPacket(new byte[700], 700);
                ds.receive(dp);
                message = new String(dp.getData());
                System.out.println(message);

                if(message.contains("Vous etes le Joueur")){coul = message.split("\\(")[1].split("\\)")[0].charAt(0);} // on prend le premier char de notre couleur



                /***************************/
                /*    CODE CREATION MAP    */
                /***************************/


                if(message.contains("MAP="))
                {
                    final String total = message.split("MAP=")[1]; //ce qui suit MAP=

                    //SETUP DU TABLEAU DE INT
                    final String[] parties = total.split("\\|");
                    String[] cases   = parties[0].split(":");

                    tabVals = new int[parties.length-1][cases.length]; //-1 car on a les dizaines de char invisible(pas des ' ') venant de .getData()
                    
                    for(int i=0; i<parties.length-1; i++)
                    {
                        
                        cases = parties[i].split(":");
                        for(int j=0; j<cases.length; j++)
                        {
                            tabVals[i][j] = Integer.parseInt(cases[j]);
                        }
                        //System.out.println("\n");
                    }

                    /*//AFFICHAGE TABLEAU DE INT
                    for(int i=0; i<tabVals.length; i++)
                    {
                        for(int j=0; j<tabVals[i].length; j++)
                            System.out.print(tabVals[i][j] + ":");
                        System.out.print("|");
                    }
                    System.out.println("\n");*/

                    //CREATION DU TABLEAU DE CONTENEUR ET DE LOCK VIA CONTROLEUR
                    controleur = new Controleur(tabVals);
                    //System.out.println(controleur.toStringConteneur());
                }



                /***************************/
                /*     CODE ENVOI COUP     */
                /***************************/



                /**
                 * NE PAS EFFACER!
                 * 
                 * Ici c'est l'ia qui choisi
                 * Si il reste de lock conteneur non remplis, on les prend
                 * Sinon on voit pour essayer de faire perdre un maximum de points a l'adversaire
                 * On additionne aux points a gagner les valeurs des conteneurs non colorés adjacent
                 * On parcours tout les choix possible comme cela et a chaque fois
                 * les var ...choisi sont les variables qui contiendra les points les plus haut possible ainsi que les coordonnées du lock permettant de gagner ces points
                 * 
                 * 4F1 = 4F
                 * 4F2 = 4G
                 * 4F3 = 5G
                 * 4F4 = 5F
                 * facile
                 * 
                 * 3B = 2A3 ou 2B4 ou 3A2 ou 3B1
                 * Pour cela il faut donc faire des tests si la case 2B existe par exemple, si oui, c'est bon
                 * on peut faire un try catch, try() dans 2B4 si ca marche pas catch() 2A3, ca marchera forcement (normalement)
                 * 
                 * Quand on envoie le message au serveur, on oublie pas de jouer avec controleur, pour mettre a jour le tableau de TLock
                 */
                if(message.contains("10-"))
                {
                    tab2DLocksControleur = controleur.getTabLock();

                    int ligChoisie   = 1;
                    int colChoisie   = 1;
                    int pointsChoisi = 0;

                    int pointsActuels;
                    int pointsEnleve;

                    int nbTLR;
                    int nbTLV;

                    Conteneur[] tabConteneur;

                    for(int i=0; i<tab2DLocksControleur.length; i++)
                    {
                        for(int j=0; j<tab2DLocksControleur[0].length; j++)
                        {
                            if(tab2DLocksControleur[i][j].getDetenuPar() == ' ') //on ne peux pas piquer un TLock FONCTIONNEL
                                try
                                {
                                    pointsActuels = 0;
                                    pointsEnleve  = 0;
                                    

                                    tabConteneur = tab2DLocksControleur[i][j].getTabConteneur(); //on stock les conteneurs pour prendre les points de tout les conteneurs ' '
                                    
                                    for(int k=0; k<tabConteneur.length; k++)
                                    {
                                        if(tabConteneur[k].getCouleur() == ' ')
                                        pointsActuels += tabConteneur[k].getPoints();
                                        else
                                            //si nous somme rouge et que la case est vert, on va colorier sa case et gagner la moitié des points pour inciter le bot a jouer de cette  facon si possible
                                            if(tabConteneur[k].getCouleur() != coul)
                                                pointsEnleve += tabConteneur[k].getPoints();
                                    }

                                    //affiche en coordonné tableau (de 0 a x) les coordonnees du point actuel et en version humaine (de 1 a x-1) le point avec le score le plus haut
                                    /*
                                    System.out.println("VTN : " + i + (char)(j+65) + "(" + j + ")" + "  pour : " + pointsActuels + 
                                                        "\t pts a avoir : " + pointsChoisi + " lig+1 : " + ligChoisie + " col : " + (char)(colChoisie+65) + "(" + colChoisie + ")");
                                    */
                                    //System.out.println("lig : " + i + "\tcol ; " + j + "\tgain : " + pointsActuels + "\tvol : " + pointsEnleve + "\ttotal : " + (int)(pointsActuels+pointsEnleve));
                                    if(pointsActuels > pointsChoisi || pointsActuels + pointsEnleve > pointsChoisi)
                                    {
                                        //System.out.println("NOUVEAU MAX");
                                        ligChoisie   = i+1; //ainsi la ligne 0 pour le tableau devient 1 pour le serveur
                                        colChoisie   = j;
                                        pointsChoisi = pointsActuels+pointsEnleve;
                                    }
                                }catch (final Exception e){}
                        }
                    }

                    



                    message = "";
                    boolean ligUp = false;

                    //transformation de 1C TLOCK en 1C1 CONTENEUR
                    if(ligChoisie == 1)//evite de sortir du tableau
                        message += ligChoisie + "";
                    else
                    {
                        message += (ligChoisie-1) + "";
                        ligUp    = true;
                    }


                    if(colChoisie == tab2DLocksControleur[0].length-1 && ligUp == false)
                        message += (char)(colChoisie+65-1) + "2";
                    else
                        if(colChoisie == tab2DLocksControleur[0].length-1 && ligUp == true)
                            message += (char)(colChoisie+65-1) + "3"; //-1 car 0+65 = 
                        else
                            if(ligUp)
                                message += (char)(colChoisie+65) + "4"; //-1 car 0+65 = A
                            else
                                if(! ligUp)
                                    message += (char)(colChoisie+65) + "1"; //-1 car 0+65 = A



                    controleur.faireSonTour(ligChoisie-1, colChoisie);
                    System.out.println("\n\n" + controleur.toStringConteneur()); //affiche les conteneurs et leurs coul dans le tableau de TLock

                    //System.out.println("VTL : " + ligChoisie + (char)(colChoisie+65) + "(" + colChoisie + ")" + "   =>   VCTN : " + message); //affiche la ligne et la col(versionTL) ainsi que les coordonnée versionConteneur


                    io = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName("localhost"), port) ;
                    ds.send(io);
                    //Thread.sleep(5000);//pause pour voir les coups se jouer
                }



                /***************************/
                /*   CODE ERREUR ADVERSE   */
                /***************************/



                if (message.contains("20:coup") ) 
                {
                    final String coords = message.split(":")[2].substring(0,3);
                    
                    //System.out.println("coords Initiales : " + coords);
                    
                    int  ligneRecue =  Character.getNumericValue(coords.charAt(0));
                    char colRecue   = coords.charAt(1);
                    int  pointRecu  = Character.getNumericValue(coords.charAt(2));
                    
                    //System.out.println("lig : " + ligneRecue + "\nCol : " +  colRecue + "\npoint : " + pointRecu);
                    
                    switch(pointRecu)
                    {
                        case 2 : colRecue   += 1; break;
                        case 3 : ligneRecue += 1; colRecue += 1; break;
                        case 4 : ligneRecue += 1; break;
                    }
                    
                    //System.out.println("Ancienne coords : " + coords + " Nouvelle coords : " + (int)(ligneRecue-1) + (int)(colRecue-65) +"\n");
                    
                    controleur.faireSonTour(ligneRecue-1,(int)(colRecue-65));
                    
                    //System.out.println(controleur.toStringConteneur());
                }



                /***************************/
                /*   CODE ERREUR ADVERSE   */
                /***************************/



                if(message.contains("22:coup"))
                {
                    controleur.tourEnPlus();
                }



                if(message.contains("88-"))
                {
                    System.exit(0);
                }
            }
        }catch(final Exception e){e.printStackTrace();}
    }
}