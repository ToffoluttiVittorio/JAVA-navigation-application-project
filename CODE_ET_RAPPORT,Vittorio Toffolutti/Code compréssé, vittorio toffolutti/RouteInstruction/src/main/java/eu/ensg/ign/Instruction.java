package eu.ensg.ign;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import eu.ensg.osm.ListeToponymes;
import eu.ensg.osm.Repere;

public class Instruction {

	/**
	 * Récupère l'instruction correspondant au numéro du step dans l'itinéraire
	 * donné.
	 * 
	 * @param stepNumber int: numéro du step.
	 * @param itineraire Resultat: l'itinéraire calculé.
	 * @return String l'instruction correspondant au numéro du step
	 * @throws ParserConfigurationException
	 */
	public static String GetInstruction(int stepNumber, Resultat itineraire) throws ParserConfigurationException {

		// Initialisation
		double[][] pointsItineraire = Itineraire.PointsItineraire(itineraire);
		double[][] pointsIntersection = Intersection.PointsIntersection(itineraire);
		double[] intersection = pointsIntersection[stepNumber];

		String direction = Direction.getDirection(intersection, pointsItineraire);
		String repere = null;
		String instruction = null;

		// Condition permettant d'éviter les cas où nous devons aller tout droit
		// De cette manière, la méthode de recherche du bon repère parmis les Toponymes
		// est évitée au maximum afin de gagner en rapidité d'exécution.
		if (direction != "Continuez tout droit") {
			ListeToponymes listeToponymes = new ListeToponymes(new ArrayList(), new ArrayList());
			listeToponymes.recuperationListeToponymes(intersection);
			repere = Repere.getBonRepere(intersection, listeToponymes.getListNom(), listeToponymes.getListCoordonnee());
			List<List<Double>> listCoordonnee = listeToponymes.getListCoordonnee();

			double[] coordRepere = Repere.getBonRepereCoord(intersection, listCoordonnee);
			double[] coordPointAvant = new double[2];

			for (int i = 0; i < pointsItineraire.length - 1; i++) {
				if (pointsItineraire[i][0] == intersection[0] && pointsItineraire[i][1] == intersection[1]) {
					coordPointAvant[0] = pointsItineraire[i - 1][0];
					coordPointAvant[1] = pointsItineraire[i - 1][1];
				}
			}

			String directionOSM = Direction.getDirectionOSM(coordPointAvant, intersection, coordRepere);
			if (repere == null) {
				instruction = direction + " au prochain croisement"; // affiche ceci si aucun repere n'est trouvé aux
																		// alentours
			} else {
				instruction = direction + directionOSM + repere; // renvoie l'instruction complete comprenant la
																	// direction, la position du repere et le repere.
			}

		} else {
			instruction = direction; // renvoie uniquement la direction "Continuez tout droit"
		}
		return instruction;
	}

	/**
	 * Récupère l'ensemble des instructions de tous les steps de l'itinéraire donné.
	 * 
	 * @param itineraire Resultat: l'itinéraire calculé.
	 * @return String[]: l'ensemble des instructions correspondants.
	 * @throws ParserConfigurationException
	 */
	public static String[] GetListInstruction(Resultat itineraire) throws ParserConfigurationException {

		//Initialisation
		double[][] pointsIntersection = Intersection.PointsIntersection(itineraire);
		String[] listInstruction = new String[pointsIntersection.length];

		//Implémente toutes les instructions dans la liste
		for (int i = 0; i < pointsIntersection.length; i++) {

			String instruction = GetInstruction(i, itineraire);
			listInstruction[i] = instruction;
		}
		return listInstruction;
	}

}
