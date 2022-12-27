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
	private ConcurrentHashMap<XY,Pion> coord;
	private Pion[][] t;
	private boolean estFinie;
	private int joueur = 0; // prochain � jouer
	private Pion [] p;
	private boolean j1;
	private boolean j2;
	private Integer gagnant;
	private int nbCoups;
	private int mode;
	
	public Plateau(int taille, int mode) {
		assert taille > 0 && taille <= TAILLE_MAX;
		t = new Pion [taille][taille];
		this.gagnant=null;
		this.estFinie=false;
		this.nbCoups=0;
		this.mode=mode;
		this.coord=new ConcurrentHashMap<XY, Pion>();
		for (int lig = 0; lig < taille(); ++lig)
			for (int col = 0; col < taille(); ++col)
				t[col][lig] = Pion.Vide;
		p = new Pion[2];
		p[0] = Pion.Croix;
		p[1]=Pion.Rond;
		if(mode==1) {
			this.j1=true;
			this.j2=true;
		}
		else if(mode==2) {
			this.j1=false;
			this.j2=true;
		}
		else {
			this.j1=false;
			this.j2=false;
		}
	}
	
	private void suivant() {
		joueur = (joueur +1) % NB_JOUEURS;
	}
	public int getMode() {
		return mode;
	}
	public boolean inverserPion() {
		if(this.getJoueur()==2 && nbCoups==1) { 
			p[1] = Pion.Croix;
			p[0]=Pion.Rond;
			suivant();
			return true;
		}
		return false;
	}
	public int getJoueur() {
		return joueur+1;
	}
	public void jouer(String coord) {
		Pion pion = p[joueur];
		int col = getColonne (coord);
		int lig = getLigne(coord);
		t[col][lig] = pion;
		this.estFinie();
		suivant();
		nbCoups++;
	}
	public Pion getPionJ1() {
		return p[0];
	}
	public Pion getPionJ2() {
		return p[1];
	}
	public boolean getJ1() {
		return this.j1;
	}
	public boolean getJ2() {
		return this.j2;
	}
	
	public int getCoups() {
		return nbCoups;
	}
	public boolean jCourantEstJoueur() {
		if(joueur==0)return j1;
		return j2;
	}
	
	public void jouerrobot() {
		Pion pion = p[joueur];
		int col,lig;
		do {
			 col = (int) (0 + (Math.random() * (this.t.length - 0)));
			lig = (int) (0 + (Math.random() * (this.t.length - 0)));
		}while(t[col][lig]!=Pion.Vide );
		t[col][lig] = pion;
		this.estFinie();
		suivant();
		nbCoups++;
		}
	
	public boolean FIN() {
		return this.estFinie;
		
	}
	public static int getTaille(String pos) {
		int taille = (int) Math.sqrt(pos.length());
		assert taille * taille == pos.length();
		return taille;
	}

	public boolean estValide(String coord) {
		if ( coord.length() !=2 && coord.length() !=3)
			return false;
		try {
            Double num = Double.parseDouble(coord.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }
		int col = getColonne (coord);
		int lig = getLigne(coord);
		if (col <0 || col >= taille())
			return false;
		if (lig <0 || lig >= taille())
			return false;
		
		return true;
	}
	
	public boolean estVide(String coord) {
		if(this.getCase(coord)!=Pion.Vide)
			return false;
		return true;
	}
	
	public Pion getCase(String coord) {
		assert estValide(coord);
		int col = getColonne (coord);
		int lig = getLigne(coord);
		return t[col][lig];
	}

	private int getColonne(String coord) {
		
		return coord.toUpperCase().charAt(0) - PREMIERE_COLONNE; // ex 'B' -'A' == 1
	}
	
	private int getLigne(String coord) {
		String s= coord.substring(1);
		int i=Integer.parseInt(s) +'0';
		return  i - PREMIERE_LIGNE; // ex '2' - '1' == 1
	}
	
	public int getGagnant() {
		return gagnant;
	}


	
	public Plateau(int taille, String pos) {
		assert taille > 0 && taille <= TAILLE_MAX;
		t = new Pion [taille][taille];
		this.coord=new ConcurrentHashMap<XY, Pion>();
		String[] lignes = decouper(pos);
		
		for (int lig = 0; lig < taille(); ++lig)
			for (int col = 0; col < taille(); ++col)
				t[col][lig] = 
				  Pion.get(lignes[lig].charAt(col));
		
		if (getNb(Pion.Croix) != getNb(Pion.Rond) &&
			getNb(Pion.Croix) != (getNb(Pion.Rond)+1) &&
					getNb(Pion.Croix) != (getNb(Pion.Rond)-1))
			throw new IllegalArgumentException(
					"position non valide");
	}
	
	

	public int getNb(Pion pion) {
		int nb = 0;
		for (Pion [] ligne : t)
			for (Pion p : ligne)
				if (p == pion)
					++nb;
		return nb;
	}

	public int taille() {
		return t.length;
	}
	
	
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

	public static String[] decouper(String pos) {
		int taille = getTaille(pos);
		String[] lignes = new String[taille];
		for (int i = 0; i <taille; ++i)
			lignes[i] = pos.substring(i*taille,
					(i+1)*taille);
		return lignes;
		
	}
	
	public void estFinie() {
		Pion pion = p[joueur];
		this.coord.clear();
			if(pion==Pion.Croix) {
					this.TrouverCroix(pion);
				}
				if(pion==Pion.Rond) {
					this.TrouverRond(pion);
				}
		this.chercher();
		if(this.gagner()==true ) {
			this.estFinie=true;
			this.gagnant=this.joueur+1;
			}
	}
	public boolean gagner() {
		for(int x=0;x<this.taille();x++) {
			XY finCroix =new XY(x,this.taille()-1);
			if(this.coord.containsKey(finCroix) && this.coord.get(finCroix)==Pion.Croix) {
				return true;
			}
			XY finRond =new XY(this.taille()-1,x);
			if(this.coord.containsKey(finRond) && this.coord.get(finRond)==Pion.Rond) {
				return true;
			}
		}
		return false;
	}
	
	public void TrouverCroix(Pion p){
		for(int j=0;j<this.taille();j++) {
			if(this.t[j][0]==p) {
				XY h = new XY(j,0);
				if(!this.coord.containsKey(h)) {
					this.coord.put(h, p);
				}
			}
		}
	}
	
	public void TrouverRond(Pion p){
		for(int j=0;j<this.taille();j++) {
			if(this.t[0][j]==p) {
				XY h = new XY(0,j);
				if(!this.coord.containsKey(h)) {
					this.coord.put(h, p);
					}
				}
			}
	}
	public void chercher() {
		ConcurrentHashMap<XY,Pion> tmp=new ConcurrentHashMap<XY, Pion>();
		this.coord.forEach( (key, value) ->{
			tmp.put(key, value);
		});
			
			while(!tmp.isEmpty()) {
				tmp.forEach( (key, value) ->{
				for(int x=key.x-1;x<=key.x+1;x++) {
					if(x>=0 && x<=this.taille()-1) {
						if(x==key.x-1) {
							for(int y=key.y;y<=key.y+1;y++) {
								if(y>=0 && y<=this.taille()-1) {
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) {
										tmp.put(h, value);
									}
								}
							}
							
						}
						if(x==key.x) {
							for(int y=key.y-1;y<=key.y+1;y++) {
								if(y>=0 && y<=this.taille()-1 && y!=key.y) {
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) {
										tmp.put(h, value);
									}
								}
							}
							
						}
						if(x==key.x+1) {
							for(int y=key.y-1;y<=key.y;y++) {
								if(y>=0 && y<=this.taille()-1) {
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) {
										tmp.put(h, value);
									}
								}
							}
							
						}
					}
						
				}
				this.coord.put(key, value);
				tmp.remove(key);
			});
			}
	}
	
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
