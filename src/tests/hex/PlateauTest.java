package tests.hex;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.java.hex.IPlateau;
import sources.hex.Plateau;

class PlateauTest {

	@Test
	void test() {
		final int taille = 4;
		IPlateau p = new Plateau(taille,1);
		assertEquals(taille, p.taille());
		
		System.out.println(p);
		assertEquals(
				" A B C D\n" + 
				"1 . . . .\n" + 
				"2  . . . .\n" + 
				"3   . . . .\n" + 
				"4    . . . .\n", p.toString());
	}

}
