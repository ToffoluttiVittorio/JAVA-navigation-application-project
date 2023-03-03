package eu.ensg.ign;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Itineraire {

	/**
	 * Calcule l'itinéraire entre deux points de coordonnées grâce au service de
	 * calcul du Géoportail de l'IGN.
	 * 
	 * @param depart  double[]: les coordonnées du point de départ.
	 * @param arrivee double[]: les coordonnées du point d'arrivée.
	 * @return Resulat: l'itinéraire calculé
	 */
	public static Resultat CalculItineraire(double[] depart, double[] arrivee) {

		// Initialisation
		double departLon = depart[0];
		double departLat = depart[1];
		double arriveeLon = arrivee[0];
		double arriveeLat = arrivee[1];

		String departString = Double.toString(departLon) + "," + Double.toString(departLat);
		String arriveeString = Double.toString(arriveeLon) + "," + Double.toString(arriveeLat);

		String url = "https://wxs.ign.fr/calcul/geoportail/itineraire/rest/1.0.0/route?resource=bdtopo-pgr"
				+ "&profile=pedestrian&optimization=fastest" + "&start=" + departString + "&end=" + arriveeString
				+ "&intermediates=&constraints={\"constraintType\":\"banned\",\"key\":\"wayType\",\"operator\":\"=\",\"value\":\"tunnel\"}"
				+ "&geometryFormat=geojson&crs=EPSG:4326&getSteps=true&getBbox=true&waysAttributes=nature";
		String txtJson = HttpClientIgn.request(url);
//		System.out.println(url);

		// On désérialise le fichier JSON en classe JAVA.
		Gson gson = new GsonBuilder().create();

		Resultat itineraire = gson.fromJson(txtJson, Resultat.class);
		return itineraire;
	}

	/**
	 * Calcule les coordonnées des points de l'itinéraire donné.
	 * 
	 * @param itineraire Resultat: l'itinéraire calculé.
	 * @return double[][]: l'array des coordonnées des points correspondants.
	 */
	public static double[][] PointsItineraire(Resultat itineraire) {
		// Initialisation
		double[][] pointsItineraire = itineraire.getGeometry().getCoordinates();

		// On choisit ici de recréer un array afin de faciliter la manipulation de ces
		// éléments.
		double[][] pointsItineraireArray = new double[pointsItineraire.length][2];
		int i = 0;

		for (double[] point : pointsItineraire) {
			pointsItineraireArray[i][0] = point[0];
			pointsItineraireArray[i][1] = point[1];
			i = i + 1;
		}
		return pointsItineraireArray;
	}

	/**
	 * Extrait les points aux extrémités de l'itinéraire donnée.
	 * 
	 * @param itineraire Resultat: l'itinéraire calculé.
	 * @return double[][]: l'array des coordonnées des deux extrémités.
	 */
	public static double[][] PointsExtremites(Resultat itineraire) {
		double[][] pointsItineraire = PointsItineraire(itineraire);
		double[] pointDepart = pointsItineraire[0]; // Le premier point de l'itinéraire
		double[] pointArrivee = pointsItineraire[pointsItineraire.length - 1]; // Le dernier point de l'itinéraire

		return new double[][] { pointDepart, pointArrivee };
	}

	/**
	 * Extrait les coordonnées des différents steps provenant de l'itinéraire donné.
	 * 
	 * @param itineraire Resultat: l'itinéraire calculé.
	 * @return double[][]: l'array des coordonnées des différents steps.
	 */
	public static double[][] StepsItineraire(Resultat itineraire) {
		Steps[] steps = null;

		// Utilisation du getter Portions
		List<Portion> portions = itineraire.getPortions();
		for (Portion portion : portions) {
			steps = portion.getSteps(); // Utilisation du getter Steps
		}

		double[][] Coordonnees = null;
		for (Steps step : steps) {
			Coordonnees = step.getGeometry().getCoordinates(); // Utilisation des getter Geometry et Coordinates
		}
		return Coordonnees;
	}

}
