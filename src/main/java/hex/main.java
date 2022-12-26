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
			
		}while(mode<0 || mode>3);
		JouerPartie(mode,taille);
	}	
		
		
		
	private static void JouerPartie(int mode,int taille) {
		Plateau p = new Plateau(taille);
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
					if(mode==1 || mode ==2 && joueur==0) {
						Scanner sc =new Scanner(System.in);
						s1= sc.nextLine();
						if(p.estValide(s1)==false) {
							System.out.println("Veuillez saisir une case valide");
						}
						else if(p.getCase(s1) != Pion.Vide) {
							System.out.println("Veuillez saisir une case vide");
						}
						else {
							
							if(mode==1 || mode ==2 && joueur==0)p.jouer(s1);
							if(mode==2 && joueur==1)p.jouerrobot();
							if(mode==3)p.jouerrobot();
							System.out.println(p);
							coup++;
						}
					}
					//System.out.println(s1);
					
				
			}
			
		if(p.etatJeux()==true) {
			System.out.println("Le joueur "+(joueur)+" a gagnÃ© !");
		}
		else
			System.out.println("Ã‰galitÃ© !!!");
		
	}
		
	
}
