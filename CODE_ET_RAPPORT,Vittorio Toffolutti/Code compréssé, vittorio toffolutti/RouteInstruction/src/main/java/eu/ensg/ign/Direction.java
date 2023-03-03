package eu.ensg.ign;

import java.util.ArrayList;
import java.util.List;

public class Direction {

	/**
	 * Calcul le gisement entre 2 points.
	 * 
	 * @param M1 double[]: les coordonnées d'un premier point.
	 * @param M2 double[]: les coordonnées d'un deuxieme point.
	 * @return double: le gisement en gon entre les 2 points M1 et M2.
	 */
	public static double gisement(double[] M1, double[] M2) {
		double G = 2 * Math.atan((M2[0] - M1[0]) / (M2[1] - M1[1] + distance(M1, M2)));
		G = G * 200 / Math.PI; // Conversion des radians en gons
		if (G < 0) {
			G = G + 400;
		} // Modulo 400 pour laisser en gons
		return G;
	}

	/**
	 * Calcul la distance euclidienne entre deux points.
	 * 
	 * @param M1 double[]: les coordonnées d'un premier point.
	 * @param M2 double[]: les coordonnées d'un deuxieme point.
	 * @return double: la distance entre les 2 points.
	 */
	public static double distance(double[] M1, double[] M2) {
		return Math.sqrt(Math.pow(M2[0] - M1[0], 2) + Math.pow(M2[1] - M1[1], 2));
	}

	/**
	 * Calcul l'angle entre 3 points en utilisant le gisement.
	 * 
	 * @param coord1 double[]: les coordonnées d'un premier point.
	 * @param coord2 double[]: les coordonnées d'un deuxieme point.
	 * @param coord3 double[]: les coordonnées d'un troisieme point.
	 * @return double: l'angle correspondant en gon.
	 */
	public static double angle(double[] coord1, double[] coord2, double[] coord3) {
		double angle = gisement(coord2, coord1) - gisement(coord2, coord3); // Calcul de l'angle
		return (angle + 400) % 400; // Modulo 400
	}

	/**
	 * Calcul l'angle entre une intersection et ses points adjacents en utilisant le
	 * gisement.
	 * 
	 * @param intersection:         les coordonnées d'un premier point représentant
	 *                              une intersection.
	 * @param coordonneesItineraire double[][]: l'ensemble des coordonnées de
	 *                              l'itinéraire calculé.
	 * @return double: l'angle correspondant en gon.
	 */
	public static double angle(double[] intersection, double[][] coordonneesItineraire) {
		double[] pointAv = new double[] { 0, 0 };
		double[] pointAp = new double[] { 0, 0 };
		double angle = 0;

		// Récupère les points adjacents au point d'intersection
		for (int i = 0; i < coordonneesItineraire.length - 1; i++) {
			// Recherche du point d'intersection
			if (coordonneesItineraire[i][0] == intersection[0] && coordonneesItineraire[i][1] == intersection[1]) {
				pointAv[0] = coordonneesItineraire[i - 1][0];
				pointAv[1] = coordonneesItineraire[i - 1][1];
				pointAp[0] = coordonneesItineraire[i + 1][0];
				pointAp[1] = coordonneesItineraire[i + 1][1];
			}
		}
		angle = (gisement(intersection, pointAv) - gisement(intersection, pointAp));

		return (angle + 400) % 400; // Modulo 400
	}

	/**
	 * Donne la direction correspondante afin de compléter une instruction.
	 * 
	 * @param intersection          double[]: les coordonnées d'un point
	 *                              représentant une intersection.
	 * @param coordonneesItineraire double[][]: l'ensemble des coordonnées de
	 *                              l'itinéraire calculé.
	 * @return String: la direction correspondante.
	 */
	public static String getDirection(double[] intersection, double[][] coordonneesItineraire) {

		// Initialisation
		String direction = "Continuez";
		double angle = angle(intersection, coordonneesItineraire);

		// Choix arbitraire des valeurs des angles (en gons)
		if (5 <= angle && angle < 60) {
			direction = "Tournez fortement à droite";
		} else if (60 <= angle && angle < 140) {
			direction = "Tournez à droite";
		} else if (130 <= angle && angle < 170) {
			direction = "Tournez légèrement à droite";
		} else if (170 <= angle && angle < 230) {
			direction = "Continuez tout droit";
		} else if (230 <= angle && angle < 260) {
			direction = "Tournez légèrement à gauche";
		} else if (260 <= angle && angle < 340) {
			direction = "Tournez à gauche";
		} else if (340 <= angle && angle < 295) {
			direction = "Tournez fortement à gauche";
		}
		return direction;

	}

	/**
	 * Donne une indication sur la position d'un repère afin de compléter une
	 * instruction.
	 * 
	 * @param coordPointAvant   double[]: coordonnées du point avant l'intersection.
	 * @param coordIntersection double[]: coordonnées du point de l'intersection.
	 * @param coordRepere       double[]: coordonnées du repère correspondant à
	 *                          l'intersection.
	 * @return String: l'indication correspondante.
	 */
	public static String getDirectionOSM(double[] coordPointAvant, double[] coordIntersection, double[] coordRepere) {
		// Initialisation
		String directionOSM = " au niveau de : ";
		double angle = angle(coordPointAvant, coordIntersection, coordRepere);

		// Choix arbitraire des valeurs des angles (en gons)
		if (100 <= angle && angle < 300) {
			directionOSM = " avant : ";
		}
		if (angle < 100 || angle >= 300) {
			directionOSM = " après : ";
		}
		return directionOSM;
	}

}