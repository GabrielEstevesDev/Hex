package main.java.hex;

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
	private boolean Fin =false;
	private boolean EtatFin; //état de la partie
	private int nbCaseJoue;
	private int joueur = 0; // prochain � jouer
	private Pion [] p;
	private ConcurrentHashMap<XY,Pion> tmp=new ConcurrentHashMap<XY, Pion>();
	
	public Plateau(int taille) {
		assert taille > 0 && taille <= TAILLE_MAX;
		t = new Pion [taille][taille];
		this.nbCaseJoue=0;
		this.coord=new ConcurrentHashMap<XY, Pion>();
		for (int lig = 0; lig < taille(); ++lig)
			for (int col = 0; col < taille(); ++col)
				t[col][lig] = Pion.Vide;
		p = new Pion[2];
		p[0] = Pion.Croix;
		p[1]=Pion.Rond;
	}
	
	private void suivant() {
		joueur = (joueur +1) % NB_JOUEURS;
	}
	public void inverserPion(int b) {
		if(b==1) {
			p[1] = Pion.Croix;
			p[0]=Pion.Rond;
			suivant();
		}
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
		this.nbCaseJoue++;
	}
	
	public void jouerrobot() {
		Pion pion = Pion.values()[joueur];
		int col,lig;
		do {
			 col = (int) (0 + (Math.random() * (this.t.length - 0)));
			lig = (int) (0 + (Math.random() * (this.t.length - 0)));
		}while(t[col][lig]!=Pion.Vide );
		System.out.println("col = "+col+" lig = "+lig+" Pion = "+pion);
		t[col][lig] = pion;
		this.estFinie();
		suivant();
		this.nbCaseJoue++;
		}
	
	public boolean etatJeux() {
		return this.EtatFin;
	}
	
	public boolean FIN() {
		return this.Fin;
		
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


	
	public Plateau(int taille, String pos) {
		assert taille > 0 && taille <= TAILLE_MAX;
		t = new Pion [taille][taille];
		this.nbCaseJoue=0;
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
		//System.out.println("joueur "+joueur);
		Pion pion = p[joueur];
		this.coord.clear();
		//System.out.println("estFinie");
			if(pion==Pion.Croix) {
					//System.out.println("Croix");
					this.TrouverCroix(pion);
				}
				if(pion==Pion.Rond) {
					//System.out.println("Rond");
					this.TrouverRond(pion);
				}
		this.chercher();
		if(this.gagner()==true || this.egalite()==true ) 
			this.Fin=true;
		//System.out.println(this.coord.size()+" FIn taille "+this.taille());
		/*this.coord.forEach((key, value) ->{
			System.out.println("x = "+key.y+" y = "+key.x +" Pion = "+value);
		});*/
		
		
		
		
			/*this.coord.forEach((key, value) ->{
				boolean a;
				System.out.println("x = "+key.x+" y = "+key.y +" Pion = "+value);
				if(key.x==this.taille()-1 && value==Pion.Croix ) {
					a=true;
				}
			});*/
	}
	
	private boolean egalite() {
		//System.out.println("casejoue = "+this.nbCaseJoue+" nbCase = "+((this.taille()*this.taille())));
		if(this.nbCaseJoue==(this.taille()*this.taille())) {
			this.EtatFin=false;
			return true;
		}
		System.out.println("etat = "+this.EtatFin);
		return false;
	}

	public boolean gagner() {
		for(int x=0;x<this.taille();x++) {
			XY finCroix =new XY(x,this.taille()-1);
			//System.out.println("Fin Croix" +this.coord.containsKey(finCroix)+" x "+(this.taille()-1)+ " y "+x);
			if(this.coord.containsKey(finCroix) && this.coord.get(finCroix)==Pion.Croix) {
				this.EtatFin=true;
				return true;
			}
			XY finRond =new XY(this.taille()-1,x);
			//System.out.println("Fin Rond" +this.coord.containsKey(finRond)+" x "+x+ " y "+(this.taille()-1));
			if(this.coord.containsKey(finRond) && this.coord.get(finRond)==Pion.Rond) {
				this.EtatFin=true;
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
					this.tmp.put(h, p);
				}
				/*while(this.chercher(0,j, p)) {
					this.coord.forEach((key, value) ->{
						this.chercher(key.x, key.y, p);
					});
				}*/
			}
		}
	}
	
	public void TrouverRond(Pion p){
		for(int j=0;j<this.taille();j++) {
			if(this.t[0][j]==p) {
				XY h = new XY(0,j);
				if(!this.coord.containsKey(h)) {
					this.coord.put(h, p);
					this.tmp.put(h, p);
					}
				}
			}
	}
	public void chercher() {
			while(!tmp.isEmpty()) {
				tmp.forEach( (key, value) ->{
				//System.out.println("Origine : x = "+key.y+" y = "+key.x +" Pion = "+value);
				for(int x=key.x-1;x<=key.x+1;x++) {
					if(x>=0 && x<=this.taille()-1) {
						if(x==key.x-1) {
							for(int y=key.y;y<=key.y+1;y++) {
								if(y>=0 && y<=this.taille()-1) {
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) {
										//System.out.println("trouvé 1 x = "+x+" y = "+y);
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
										//System.out.println("trouvé 2 x = "+x+" y = "+y);
										tmp.put(h, value);
									}
								}
							}
							
						}
						if(x==key.x+1) {
							//System.out.println("key.y "+key.y);
							for(int y=key.y-1;y<=key.y;y++) {
								if(y>=0 && y<=this.taille()-1) {
									//System.out.println("key.y "+key.y);
									XY h = new XY(x,y);
									if(this.t[x][y]==value && !this.coord.containsKey(h)) {
										//System.out.println("trouvé 3 x = "+x+" y = "+y);
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
			/*if(this.t[i-1][j]==Pion.Croix && this.t[i-1][j+1]==Pion.Croix &&
					this.t[i][j+1]==Pion.Croix && this.t[i][j+1]==Pion.Croix && 
					this.t[i+1][j]==Pion.Croix && this.t[i+1][j-1]==Pion.Croix ) {
				ConcurrentHashMap
			}*/
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
