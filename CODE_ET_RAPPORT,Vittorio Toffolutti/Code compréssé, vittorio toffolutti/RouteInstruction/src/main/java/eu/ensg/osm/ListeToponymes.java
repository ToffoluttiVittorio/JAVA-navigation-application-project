package eu.ensg.osm;

import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.ensg.ign.Portion;
import eu.ensg.osm.HttpClientOsm;

public class ListeToponymes {

	// Choix arbitraire de la taille de la Bbox afin d'obtenir les reperes aux
	// alentours d'un point de coordonnées.
	private double sizeBbox = 0.00150;

	List<String> listNom = new ArrayList();
	List<List<Double>> listCoordonnee = new ArrayList();

	// getters Setters listNom et listCoordonnee
	public ListeToponymes(List listnom, List list2coord) {
		this.listNom = listNom;
		this.listCoordonnee = listCoordonnee;
	}

	public List<String> getListNom() {
		return this.listNom;
	}

	public List<List<Double>> getListCoordonnee() {
		return this.listCoordonnee;
	}

	/**
	 * Récupération des Toponymes autour des coordonnees d'un point grâce aux
	 * getters setters
	 * 
	 * @param coordonnees double[]: coordonnes d'un point donné.
	 * @throws ParserConfigurationException
	 */
	public void recuperationListeToponymes(double[] coordonnees) throws ParserConfigurationException {

		// correspond à un carré de coté 2 x sizeBbox
		double E = coordonnees[0] + sizeBbox;
		double O = coordonnees[0] - sizeBbox;
		double S = coordonnees[1] - sizeBbox;
		double N = coordonnees[1] + sizeBbox;

		String dataRequest = "<osm-script>" + "<union>" + "<query type=\"node\">" + "<bbox-query e=\"" + E + "\" n=\""
				+ N + "\" s=\"" + S + "\" w=\"" + O + "\" />" + "</query>" + "</union>" + "<print mode=\"meta\"/>"
				+ "</osm-script>";
		String xmldata = HttpClientOsm.getOsmXML(dataRequest);
		// System.out.println(xmldata);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		try {
			Document doc = builder.parse(new ByteArrayInputStream(xmldata.getBytes()));
			doc.getDocumentElement().normalize();
			Element root = (Element) doc.getElementsByTagName("osm").item(0);

			int nbNoeuds = root.getElementsByTagName("node").getLength();
			for (int i = 0; i < nbNoeuds; i++) {

				Element elem = (Element) root.getElementsByTagName("node").item(i);

				// On récupère son ID
				long id = Long.valueOf(elem.getAttribute("id"));

				// on récupère sa géométrie
				double lat = Double.valueOf(elem.getAttribute("lat"));
				double lon = Double.valueOf(elem.getAttribute("lon"));

				for (int j = 0; j < elem.getElementsByTagName("tag").getLength(); j++) {
					Element tagElem = (Element) elem.getElementsByTagName("tag").item(j);
					String cle = tagElem.getAttribute("k");
					String val = tagElem.getAttribute("v");

					if (cle.equals("name")) {
						List<Double> listcoord = new ArrayList<Double>();
						listcoord.add(lon);
						listcoord.add(lat);
						this.listNom.add(val);
						this.listCoordonnee.add(listcoord);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
