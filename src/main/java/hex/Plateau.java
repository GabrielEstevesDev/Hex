package main.java.hex;

import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Plateau {
	private final static int TAILLE_MAX = 26;
	private final static int NB_JOUEURS = 2;
	private final static int PREMIERE_COLONNE = 'A';
	private final static int PREMIERE_LIGNE = '1';
	// le premier joueur relie la premiere et la derniere ligne
	// le second joueur relie la premiere et la derniere colonne
	private ConcurrentHashMap<XY,Pion> coord; //Chemin de Pion
	private Pion[][] t; //Tableau representant le Plateau
	private boolean estFinie; //etat de fin de la partie
	private int joueur = 0; // prochain � jouer
	private Pion [] p; //tableau pour connaître le pion du joueur en cours
	private boolean j1; //etat du joueur 1 true = joueur false = robot
	private boolean j2; //etat du joueur 2 true = joueur false = robot
	private Integer gagnant; //Valeur qui indique le gagnant de la partie
	private int nbCoups; //Nombre de coups dans la partie
	private int mode; //mode de jeu de la partie
	
	
	/*Fonction qui instancie une Partie Hex*/
	public Plateau(int taille, int mode) { 
		assert taille > 0 && taille <= TAILLE_MAX; // nous verifions que la taille est correct
		t = new Pion [taille][taille]; //Ensuite nous initialisons chaques variables utile à la partie
		this.gagnant=null;
		this.estFinie=false;
		this.nbCoups=0;
		this.mode=mode;
		this.coord=new ConcurrentHashMap<XY, Pion>();
		for (int lig = 0; lig < taille(); ++lig) //Nous remplissons le tableau de pion vide
			for (int col = 0; col < taille(); ++col)
				t[col][lig] = Pion.Vide;
		p = new Pion[2];
		p[0] = Pion.Croix; //Nous affectons au tableau les pions pour chaque joueurs
		p[1]=Pion.Rond;
		if(mode==1) { //si mode = 1  alors il y a deux joueurs
			this.j1=true;
			this.j2=true;
		}
		else if(mode==2) { //si mode = 1  alors il y a un joueur et une IA
			this.j1=false;
			this.j2=true;
		}
		else { //sinon il n'y a que des IA
			this.j1=false;
			this.j2=false;
		}
	}
	
	/*Methode qui permet de passer au joueur suivant*/
	private void suivant() { 
		joueur = (joueur +1) % NB_JOUEURS;
	}
	
	/*Methode qui permet de recuperer le mode de la partie*/
	public int getMode() {
		return mode;
	}
	
	/*Fonction qui echange les pions entre les joueurs*/
	public boolean inverserPion() {
		if(this.getJoueur()==2 && nbCoups==1) { //Si le joueur courant est le numero 2 et que c'est le 2eme coup de la partie
			p[1] = Pion.Croix; //Nous echangeons donc les pions entre les joueurs
			p[0]=Pion.Rond;
			suivant(); //et nous passons au joueur suivant
			return true;
		}
		return false;
	}
	
	/*Methode qui permet de recuperer le joueur courant de la partie*/
	public int getJoueur() {
		return joueur+1;
	}
	
	/*Fonction qui place un pion sur le plateau avec des cooronees*/
	public void jouer(String coord) {
		Pion pion = p[joueur]; //Nous recuperons le pion du joueurs courant
		int col = getColonne (coord); //nous recuperons les lignes et les collones des coordonnees
		int lig = getLigne(coord);
		t[col][lig] = pion; //nous placons le Pion
		this.estFinie(); //Nous verifions si la partie est finis
		suivant(); //nous passons au joueur suivant
		nbCoups++; //nous indiquons qu'un coup a été joué
	}
	
	/*Methode qui permet de recuperer le pion de J1*/
	public Pion getPionJ1() {
		return p[0];
	}
	
	/*Methode qui permet de recuperer le pion de J2*/
	public Pion getPionJ2() {
		return p[1];
	}
	
	/*Méthode qui permet de recuperer J1*/
	public boolean getJ1() {
		return this.j1;
	}
	
	/*Méthode qui permet de recuperer J2*/
	public boolean getJ2() {
		return this.j2;
	}
	
	/*Methode qui permet de recuperer les nombre de coup de la partie*/
	public int getCoups() {
		return nbCoups;
	}
	
	/*Methode qui permet de savoir qui est le joueur courant*/
	public boolean jCourantEstJoueur() {
		if(joueur==0)return j1;
		return j2;
	}
	
	
	/*Fonction qui place un pion sur le plateau avec des cooronees aleatoires*/
	public void jouerrobot() {
		Pion pion = p[joueur];
		int col,lig;
		do {
			 col = (int) (0 + (Math.random() * (this.t.length - 0)));
			lig = (int) (0 + (Math.random() * (this.t.length - 0)));
		}while(t[col][lig]!=Pion.Vide ); //Tant que nous n'avons pas trouve de bonnes coordonnees nous continuons a chercher
		t[col][lig] = pion;
		this.estFinie();
		suivant();
		nbCoups++;
		}
	
	
	/*Methode qui permet de savoir l'etat de la partie*/
	public boolean FIN() {
		return this.estFinie;
		
	}
	
	/*Fonction qui retourne la taille du plateau de jeu*/
	public static int getTaille(String pos) {
		int taille = (int) Math.sqrt(pos.length());
		assert taille * taille == pos.length();
		return taille;
	}

	/*Fonction qui verifie les coordonnees envoyee*/
	public boolean estValide(String coord) {
		if ( coord.length() !=2 && coord.length() !=3) //Nous exluons toutes valeurs qui depasserais la	longueur autorise
			return false;
		try {
            Double num = Double.parseDouble(coord.substring(1)); //nous verifions qu'il y a bien un nombre dans les coordonnees
        } catch (NumberFormatException e) {
            return false;
        }
		int col = getColonne (coord);
		int lig = getLigne(coord);
		if (col <0 || col >= taille()) //nous verifions que la colonne et la ligne obtenu sont bien comprises dans le tableau
			return false;
		if (lig <0 || lig >= taille())
			return false;
		
		return true;
	}
	
	/*Methode qui verifie que la coordonnee est bien une case vide*/
	public boolean estVide(String coord) {
		if(this.getCase(coord)!=Pion.Vide)
			return false;
		return true;
	}
	
	/*Methode qui retourne une case à partir de coordonnees*/
	public Pion getCase(String coord) {
		assert estValide(coord); // nous verifions d'abord si les coordonnees sont valide
		int col = getColonne (coord); //ensuite nous calculons les lignes et les colonnes
		int lig = getLigne(coord);
		return t[col][lig]; //nous renvoyons la case du tableau correspondant
	}

	/*Methode qui retourne une colonne à partir de coordonnees*/
	private int getColonne(String coord) {
		return coord.toUpperCase().charAt(0) - PREMIERE_COLONNE; // ex 'B' -'A' == 1
	}
	
	
	/*Methode qui retourne une ligne à partir de coordonnees*/
	private int getLigne(String coord) {
		String s= coord.substring(1);
		int i=Integer.parseInt(s) +'0';
		return  i - PREMIERE_LIGNE; // ex '2' - '1' == 1
	}
	
	/*Methode qui retourne le gagnant de la partie*/
	public int getGagnant() {
		return gagnant;
	}


	/*Fonction qui instancie une Partie Hex à partir */
	public Plateau(int taille, String pos) {
		assert taille > 0 && taille <= TAILLE_MAX;
		t = new Pion [taille][taille];
		this.coord=new ConcurrentHashMap<XY, Pion>();
		String[] lignes = decouper(pos); //nous recuperons une liste de symboles par lignes
		
		for (int lig = 0; lig < taille(); ++lig)
			for (int col = 0; col < taille(); ++col)
				t[col][lig] = 
				  Pion.get(lignes[lig].charAt(col)); //Pour chaque de symbole nous creons un pion correspondant s'il existe 
		
		if (getNb(Pion.Croix) != getNb(Pion.Rond) &&
			getNb(Pion.Croix) != (getNb(Pion.Rond)+1) &&
					getNb(Pion.Croix) != (getNb(Pion.Rond)-1))
			throw new IllegalArgumentException(
					"position non valide");
	}
	
	
	/*Retourne le nombre de pion dans le plateau en fonction du pion demande*/
	public int getNb(Pion pion) {
		int nb = 0;
		for (Pion [] ligne : t)
			for (Pion p : ligne)
				if (p == pion)
					++nb;
		return nb;
	}

	/*Methode qui retourne la taille du tableau*/
	public int taille() {
		return t.length;
	}
	
	/*Methode qui retourne le nombre d'espace necessaire pour un bon affichage du tostring*/
	private String espaces(int n) {
		String s = "";
		for(int i = 0; i < n; ++i)
			s+= " ";
		return s;
	}
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < taille(); ++i)
			s+=" "+(char)( 'A' + i);
		s+='\n';
		for (int lig = 0; lig < taille(); ++lig) {
			s+= lig+1 + espaces (lig);
			for (int col = 0; col < taille(); ++col)
				s+= " "+ t[col][lig];
			s+='\n';
		}
		return s;
	}

	/*Fonction qui permet de renvoyer un tableau de pion par ligne à partir d'un string*/
	public static String[] decouper(String pos) {
		int taille = getTaille(pos);
		String[] lignes = new String[taille];
		for (int i = 0; i <taille; ++i)
			lignes[i] = pos.substring(i*taille,
					(i+1)*taille);
		return lignes;
		
	}
	
	/*Fonction qui determine si la partie est fini*/
	public void estFinie() {
		Pion pion = p[joueur]; //nous recuperons le pion du joueur courant
		this.coord.clear(); //nous vidons le chemin
			if(pion==Pion.Croix) { //nous cherchons à differents endroit en fonction du pion
					this.TrouverCroix(pion);
				}
				if(pion==Pion.Rond) {
					this.TrouverRond(pion);
				}
		this.chercher(); //nous remplissons le chemin
		if(this.gagner()==true ) { //si la partie est fini
			this.estFinie=true; //nous modifions l'etat de la partie
			this.gagnant=this.joueur+1; //nous indiquons le gagnant
			}
	}
	
	/*Fonction qui determine si la partie est gagnee*/
	public boolean gagner() {
		for(int x=0;x<this.taille();x++) { //Pour chaque pion nous verifions si les conditions de victoires sont remplis
			XY finCroix =new XY(x,this.taille()-1);
			if(this.coord.containsKey(finCroix) && this.coord.get(finCroix)==Pion.Croix) { //Pour les croix : s'il y une croix a la dernière ligne dans le chemin trouve
				return true;
			}
			XY finRond =new XY(this.taille()-1,x);
			if(this.coord.containsKey(finRond) && this.coord.get(finRond)==Pion.Rond) {//Pour les ronds : s'il y un rond a la dernière colonne dans le chemin trouve
				return true;
			}
		}
		return false;
	}
	
	/*Fonction qui indique si nous pouvons commnencer a chercher */
	public void TrouverCroix(Pion p){
		for(int j=0;j<this.taille();j++) {
			if(this.t[j][0]==p) {
				XY h = new XY(j,0);
				if(!this.coord.containsKey(h)) { //ajoute une position dans le chemin s'il a trouve une croix sur la première ligne
					this.coord.put(h, p);
				}
			}
		}
	}
	
	public void TrouverRond(Pion p){
		for(int j=0;j<this.taille();j++) {
			if(this.t[0][j]==p) {
				XY h = new XY(0,j);
				if(!this.coord.containsKey(h)) { //ajoute une position dans le chemin s'il a trouve un rond sur la première colonne
					this.coord.put(h, p);
					}
				}
			}
	}
	public void chercher() {
		ConcurrentHashMap<XY,Pion> tmp=new ConcurrentHashMap<XY, Pion>(); //nous instancions un chemin tmp
		this.coord.forEach( (key, value) ->{
			tmp.put(key, value); //nous lui ajoutons la position qui permet de commencer le chemin
		});
			
			while(!tmp.isEmpty()) { //tant que nous n'avons pas exploiter chaque position dans tmp
				tmp.forEach( (key, value) ->{ //alors nous cherchons dans chaque case autour de la position courante
				for(int x=key.x-1;x<=key.x+1;x++) {
					if(x>=0 && x<=this.taille()-1) {
						if(x==key.x-1) {
							for(int y=key.y;y<=key.y+1;y++) {
								if(y>=0 && y<=this.taille()-1) {
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) { //si nous trouvons une autre case du même pion et qu'll n'est pas le chemin
										tmp.put(h, value); //nous l'ajoutons au chemin tmp
									}
								}
							}
							
						}
						if(x==key.x) {
							for(int y=key.y-1;y<=key.y+1;y++) {
								if(y>=0 && y<=this.taille()-1 && y!=key.y) {
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) { //si nous trouvons une autre case du même pion et qu'll n'est pas le chemin
										tmp.put(h, value);//nous l'ajoutons au chemin tmp
									}
								}
							}
							
						}
						if(x==key.x+1) {
							for(int y=key.y-1;y<=key.y;y++) {
								if(y>=0 && y<=this.taille()-1) {
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) { //si nous trouvons une autre case du même pion et qu'll n'est pas le chemin
										tmp.put(h, value);//nous l'ajoutons au chemin tmp
									}
								}
							}
							
						}
					}
						
				}
				this.coord.put(key, value);  //une fois exploite nous l'ajoutons au chemin principale
				tmp.remove(key); //et nous le retirons de tmp
			});
			}
	}
	
	/*Classe XY de coordonnees*/
	public static class XY {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			XY other = (XY) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		private int x;
		private int y;
		public XY(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}



}
