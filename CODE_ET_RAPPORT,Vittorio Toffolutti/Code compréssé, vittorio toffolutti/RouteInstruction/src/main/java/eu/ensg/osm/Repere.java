package eu.ensg.osm;

import java.util.List;
import java.util.ArrayList;

public class Repere {

	/**
	 * Calcule la Distance entre deux points de coordonnées en prenant compte de la
	 * forme géoide de la Terre.
	 * 
	 * @param lat1 double: latitude du premier point
	 * @param lon1 double: longitude du premier point
	 * @param lat2 double: latitude du deuxieme point
	 * @param lon2 double: longitude du deuxieme point
	 * @return double: la distance entre deux points selon leur latitudes et
	 *         longitudes
	 */
	public static double getDistanceFromLatLon(double lat1, double lon1, double lat2, double lon2) {
		int R = 6371; // Radius of the earth in km
		double dLat = (lat2 - lat1) * (Math.PI / 180); // deg2rad below
		double dLon = (lon2 - lon1) * (Math.PI / 180);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos((lat1) * (Math.PI / 180))
				* Math.cos((lat2) * (Math.PI / 180)) * Math.pow(Math.sin(dLon / 2), 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c; // Distance in km
		return d;
	}

	/**
	 * Recupere le meilleur repere possible parmis les toponymes autour d'un point
	 * d'intersection.
	 * 
	 * @param coordonneesIntersection double[]: les coordonnees du point
	 *                                d'intersection.
	 * @param listNom                 List<String>: la liste des noms des toponymes
	 *                                associés au point d'intersection.
	 * @param listCoordonnees         List<List<Double>>: la liste des coordonnes
	 *                                des toponymes associés au point
	 *                                d'intersection.
	 * @return String: le nom du repère choisi.
	 */
	public static String getBonRepere(double[] coordonneesIntersection, List<String> listNom,
			List<List<Double>> listCoordonnees) {
		// Initialisation
		String repere = null;
		double distance = 10000; // choix d'une importante distance pour ne pas interférer avec les tests du
									// minimum.
		double lat1 = coordonneesIntersection[0];
		double lon1 = coordonneesIntersection[1];

		// Calcul des distances
		for (int i = 0; i < listCoordonnees.size(); i++) {

			double lat2 = listCoordonnees.get(i).get(0);
			double lon2 = listCoordonnees.get(i).get(1);
			double distanceCalc = getDistanceFromLatLon(lat1, lon1, lat2, lon2);

			// Choix du reprere le plus proche
			if (distanceCalc < distance) {
				distance = distanceCalc;
				repere = listNom.get(i);
			}
		}
		return repere;
	}

	/**
	 * Recupere les coordonnees du meilleur repere possible parmis les toponymes
	 * autour d'un point d'intersection.
	 * 
	 * @param coordonneesIntersection double[]: les coordonnes du point
	 *                                d'intersection.
	 * @param listCoordonnees         List<List<Double>>: la liste des coordonnées
	 *                                des toponymes aux alentours
	 * @return double[] les coordonnees du repere choisi.
	 */
	public static double[] getBonRepereCoord(double[] coordonneesIntersection, List<List<Double>> listCoordonnees) {

		// Initialisation
		double[] repereCoord = new double[2];
		double distance = 10000;// choix d'une importante distance pour ne pas interférer avec les tests du
		// minimum.
		double lat1 = coordonneesIntersection[0];
		double lon1 = coordonneesIntersection[1];
		// Calcul des distances
		for (int i = 0; i < listCoordonnees.size(); i++) {

			double lat2 = listCoordonnees.get(i).get(0);
			double lon2 = listCoordonnees.get(i).get(1);
			double distanceCalc = getDistanceFromLatLon(lat1, lon1, lat2, lon2);

			// Choix du reprere le plus proche
			if (distanceCalc < distance) {
				distance = distanceCalc;
				repereCoord[0] = lat2;
				repereCoord[1] = lon2;
			}
		}
		return repereCoord;
	}

}
