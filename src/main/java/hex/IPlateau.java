package main.java.hex;


public interface IPlateau {

	/**
	 * Fonction inverserPion() permettant d'inverser les pions des joueurs entre eux
	 * Selon la règle du jeu de Hex, le 2ème joueur a le droit de pouvoir échanger ses pions
	 * avec le premier joueur suite au premier coup du tout premier joueur
	 * return : boolean
	 * */
	boolean inverserPion();

	/**
	 * Fonction jouer(String coord) permettant de faire jouer un joueur avec la coordonée
	 * entrée en paramètre
	 * Tant que la partie n'est pas finie, c'est au joueur suivant.
	 * param[in] : String coord
	 * */

	void jouer(String coord);

	boolean jCourantEstJoueur();

	/**
	Fonction jouerrobot() permettant de faire jouer l'IA en choisissant des valeurs aléatoires
	 selon le déroulement de la partie et les possibilités
	*/
	void jouerrobot();

	boolean FIN();

	/**
	 * Fonction estValide(String coord) permettant de savoir si la coordonnée entrée en paramètre
	 * est bien une coordonnée valide (Si le premier caractère est bien un int qui est compris entre 1 et la taille du plateau
	 * Et si le deuxième caractère est bien une lettre.
	 * return : boolean
	 * */
	boolean estValide(String coord);


	int getCoups();

	int getMode();

	int getJoueur();

	boolean estVide(String s1);

	Integer getGagnant();



}
