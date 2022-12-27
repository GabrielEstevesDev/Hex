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
			System.out.println("2 : J1 VS IA");
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
		int coup=0;
		while(p.FIN()==false) {
			Integer bool = null;
			if(coup==1 && mode==1) {
				do {
					try {
					System.out.println("Entrez 1 si vous voulez changer de coté.");
					System.out.println("Entrez 0 si vous ne voulez pas changer de coté.");
					Scanner sc1 =new Scanner(System.in);
					bool = sc1.nextInt();
					}catch(InputMismatchException e) {
						
					}
					
				}while(bool<0 || bool>1);
				p.inverserPion(bool);
			}
			joueur=p.getJoueur();
			System.out.println("A Joueur " + joueur + " de jouer.");
			if(p.aBseoinCoord()==true) {
				Scanner sc =new Scanner(System.in);
				s1= sc.nextLine();
				if(p.estValide(s1)==false) {
					System.out.println("Veuillez saisir une case valide");
				}
				else if(p.getCase(s1) != Pion.Vide) {
					System.out.println("Veuillez saisir une case vide");
				}
				else {
					p.jouer(s1);
					coup++;
				}
			}
			else{
				p.jouerrobot();
				coup++;
				}
					System.out.println(p);	
			}
			System.out.println("Le joueur "+p.getGagnant()+" a gagnÃ© !");
		
	}
		
	
}