package test.java.hex;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.InputMismatchException;

import org.junit.jupiter.api.Test;

import main.java.hex.Pion;
import main.java.hex.Plateau;
import main.java.hex.Plateau.XY;

class PlateauTest {
	private String pos1 = ".X..XOXXOO.OX..."; 
	private String[] lignes1_rep = {
			".X..", 
			"XOXX",
			"OO.O",
			"X..."
	};
	private String display1_rep = 
			" A B C D\n" + 
			"1 . X . .\n" + 
			"2  X O X X\n" + 
			"3   O O . O\n" + 
			"4    X . . .\n";

	@Test
	void test() {
		final int taille = 4;
		Plateau p = new Plateau(taille,1);
		assertEquals(taille, p.taille());
		assertEquals(
				" A B C D\n" + 
				"1 . . . .\n" + 
				"2  . . . .\n" + 
				"3   . . . .\n" + 
				"4    . . . .\n", p.toString());
		
	
		// jouer un coup en B2
		p.jouer("B2");
		assertEquals(Pion.Croix, p.getCase("B2"));
		System.out.println(p);
		
		assertFalse(p.estValide("**"));
		assertFalse(p.estValide("A5"));
		assertFalse(p.estValide("Z2"));
		assertFalse(p.estValide("Z222"));
		System.out.println(p);
		
		
	}
	
	@Test
	public void testerPositions() {
		testerPosition(pos1, lignes1_rep, display1_rep);
	}

	private void testerPosition(String pos, String[] lignes_rep, String display_rep) {
		String[] lignes;
		lignes  = Plateau.decouper(pos);
		int taille = Plateau.getTaille(pos);
		
		for (int i = 0; i< taille; ++i)
			assertEquals(lignes_rep[i], lignes[i]);
		
		Plateau p = new Plateau(taille, pos);
		assertEquals(display_rep, p.toString());
		XY z= new XY(1,2);
		XY y= new XY(2,1);
		assertFalse(z==y);
		
		HashMap<XY,Pion> t =new HashMap<XY, Pion>();
		t.put(y, Pion.Rond);
		if(!t.containsKey(z))
			t.put(z, Pion.Croix);
		assertEquals(t.size(),2);
	}
	@Test
	public void testeretatPartie() {
		int taille1 = 4;
		Plateau p1 = new Plateau(taille1,1);
		int i=1;
		while(p1.FIN()==false) {
			p1.jouer("A"+i);
			p1.jouer("B"+i);
			i++;
		}
		System.out.println(p1);
		assertTrue(p1.FIN());
		
	}
	@Test
	public void Gagnant() {
		int taille1 = 4;
		Plateau p1 = new Plateau(taille1,1);
		int i=1;
		while(p1.FIN()==false) {
			p1.jouer("A"+i);
			p1.jouer("B"+i);
			i++;
		}
		assertEquals(p1.getGagnant(),1);
		
	}
	
	@Test
	public void TestMode() {
		int taille1 = 4;
		Plateau p1 = new Plateau(taille1,1);
		assertTrue(p1.getJ1());
		assertTrue(p1.getJ2());
		assertEquals(p1.getMode(),1);
		p1=new Plateau(taille1,2);
		assertFalse(p1.getJ1()); //joueur 1 est un robot
		assertTrue(p1.getJ2());
		assertEquals(p1.getMode(),2);
		p1=new Plateau(taille1,3);
		assertFalse(p1.getJ1()); //joueur 1 est un robot
		assertFalse(p1.getJ2());
		assertEquals(p1.getMode(),3);
	}
	
	@Test
	public void testInverserPion() {
		int taille1 = 2;
		Plateau p1 = new Plateau(taille1,1);
		assertEquals( p1.getPionJ1(),Pion.Croix);
		assertEquals( p1.getPionJ2(),Pion.Rond);
		p1.jouer("A1");
		assertEquals(p1.getCoups(),1);
		assertTrue(p1.inverserPion());
		assertFalse(p1.inverserPion());
		assertEquals(p1.getPionJ1(),Pion.Rond);
		assertEquals(p1.getPionJ2(),Pion.Croix);
	}
}
