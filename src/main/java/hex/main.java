package main.java.hex;

import java.util.InputMismatchException;
import java.util.Scanner;

public class main {
	public static void main(String[] args) {
		System.out.println("Bienvenu dans le jeu Hex !");
		
		int taille=0;
		int mode =0;
		do {
			try {
			System.out.println("Veuillez choisir une taille de plateau entre 1 et 26: ");
			Scanner sc1 =new Scanner(System.in);
			taille = sc1.nextInt();
			}catch(InputMismatchException e) {
				
			}
			
		}while(taille<1 || taille>26);
		
		do {
			try {
			System.out.println("Veuillez choisir un mode de jeu parmis la liste ci-dessous : ");
			System.out.println("1 : J1 VS J2");
			System.out.println("2 : IA VS J1");
			System.out.println("3 : IA VS IA");
			Scanner sc1 =new Scanner(System.in);
			mode = sc1.nextInt();
			}catch(InputMismatchException e) {
				
			}
			
		}while(mode<1 || mode>3);
		JouerPartie(mode,taille);
	}	
		
		
		
	private static void JouerPartie(int mode,int taille) {
		Plateau p = new Plateau(taille,mode);
		int joueur=0;
		String s1 = "" ;
		while(p.FIN()==false) {
			Integer bool = null;
			if(p.getCoups()==1 && (p.getMode()==1 || p.getMode()==2)) {
				do {
					try {
					System.out.println("Entrez 1 si vous voulez changer de coté.");
					System.out.println("Entrez 0 si vous ne voulez pas changer de coté.");
					Scanner sc1 =new Scanner(System.in);
					bool = sc1.nextInt();
					}catch(InputMismatchException e) {
						
					}
					
				}while(bool<0 || bool>1);
				if(bool==1)p.inverserPion();
			}
			joueur=p.getJoueur();
			System.out.println("A Joueur " + joueur + " de jouer.");
			if(p.jCourantEstJoueur()) {
				do {
					Scanner sc =new Scanner(System.in);
					s1= sc.nextLine();
					if(p.estValide(s1)==false || p.estVide(s1)==false)
						System.out.println("Veuillez saisir une case valide");
				}while(p.estValide(s1)==false || p.estVide(s1)==false);
					p.jouer(s1);
				}
			else{
				p.jouerrobot();
				}
					System.out.println(p);	
			}
			System.out.println("Le joueur "+p.getGagnant()+" a gagné !");
		
	}
		
	
}