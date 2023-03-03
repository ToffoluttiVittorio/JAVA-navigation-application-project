package eu.ensg.ign;

import java.util.List;

public class Intersection {

	/**
	 * Renvoie l'ensemble des coordonnées des points d'intersection de l'itinéraire
	 * calculé.
	 * 
	 * @param itineraire Resultat: l'itinéraire calculé.
	 * @return double[][]: l'ensemble des coordonnées correspondant.
	 */
	public static double[][] PointsIntersection(Resultat itineraire) {

		// initialisation
		Steps[] steps = null;
		
		// getters setters portions
		List<Portion> portions = itineraire.getPortions();
		for (Portion portion : portions) {
			steps = portion.getSteps();
		}

		double[][] pointsIntersection = new double[steps.length][];

		// Pour chaque step, on prend pour point d'intersection le dernier point de son
		// step correspondant.
		int i = 0;
		for (Steps step : steps) {
			double[][] coordonneesStep = step.getGeometry().getCoordinates();
			pointsIntersection[i] = coordonneesStep[coordonneesStep.length - 1];
			i = i + 1; // incrémentation
		}

		// On choisit ici de recréer un array afin de faciliter la manipulation de ces
		// éléments.
		double[][] pointsIntersectionArray = new double[pointsIntersection.length][2];
		int j = 0;

		for (double[] point : pointsIntersection) {
			pointsIntersectionArray[j][0] = point[0];
			pointsIntersectionArray[j][1] = point[1];
			j = j + 1;
		}
		return pointsIntersectionArray;
	}

}
