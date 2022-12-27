package sources.hex;

public class Plateau {
	private final static int CROIX = -1;
	private final static int ROND = 1;
	private final static int VIDE = 0;
	private final static int TAILLE_MAX = 26;
	
	private int[][] t;
	

	public Plateau(int taille) {
		assert taille>0 && taille <=26;
		this.t = new int[taille][taille];
		for (int i = 0;i<t.length;i++) {
			for (int j = 0;j<t[0].length;j++) {
				t[i][j] = VIDE;
			}
		}
	}
	
	public int taille() {
		return t.length;
	}
	
	public static String espace(int i) {
		String s="";
		for (int j=0;j<i;j++) {
			s+=" ";
		}
		return s;
	}
	public String symbole(int i) {
		if (i == VIDE)
			return ".";
		else if(i==CROIX)
			return "X";
		else if(i==ROND)
			return "O";
		return " ";
			
	}
	@Override
	public String toString() {
		String s = "";
		for(int i=0; i<t.length;i++) {
			s+= " " +(char)('A'+i);
		}
		s+="\n";
		for (int i = 0;i<t.length;i++) {
			s+=i+1 + espace(i+1);
			for (int j=0;j<t.length;j++) {
				s+= symbole(this.t[i][j]);
				if(j<t.length-1)
					s+=" ";
			}
			s+="\n";
		}
		return s;
	}



}
