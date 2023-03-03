package eu.ensg.portail;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import eu.ensg.exemple.Main;
import eu.ensg.ign.Instruction;
import eu.ensg.ign.Intersection;
import eu.ensg.ign.Itineraire;
import eu.ensg.ign.Resultat;

public class Window extends JFrame {

	// Initialisation
	static boolean initial = false;

	static int stepNumber = 0;
	static int sizeX = 1200;
	static int sizeY = 800;

	static double departLon = -1.63063562222024;
	static double departLat = 49.63867049347336;
	static double arriveeLon = -1.6287875175476074;
	static double arriveeLat = 49.6384546008293;

	double departLonSimp = Math.round(departLon * 10000.0) / 10000.0;
	double departLatSimp = Math.round(departLat * 10000.0) / 10000.0;
	double arriveeLonSimp = Math.round(arriveeLon * 10000.0) / 10000.0;
	double arriveeLatSimp = Math.round(arriveeLat * 10000.0) / 10000.0;

	static double[] departCoord = new double[] { departLon, departLat };
	static double[] arriveeCoord = new double[] { arriveeLon, arriveeLat };

	static Resultat itineraire = Itineraire.CalculItineraire(departCoord, arriveeCoord);
	double[][] pointsIntersection = Intersection.PointsIntersection(itineraire);

	String[] listInstruction = Instruction.GetListInstruction(itineraire);

	double distanceTrajet = itineraire.getPortions().get(0).getDistance();
	double distanceTrajetKm = Math.round(distanceTrajet / 1000 * 100.0) / 100.0;

	double tempsTrajet = itineraire.getPortions().get(0).getDuration();
	int tempsTrajetHeure = (int) tempsTrajet / 60;
	int tempsTrajetMinute = (int) tempsTrajet - tempsTrajetHeure * 60;

	double distanceStep = itineraire.getPortions().get(0).getSteps()[stepNumber].getDistance();
	double distanceStepM = Math.round(distanceStep * 100.0) / 100.0;

	double tempsStep = itineraire.getPortions().get(0).getSteps()[stepNumber].getDuration();
	int tempsStepMinute = (int) tempsStep;

	/**
	 * Constructeur permettant d'adapter l'interface graphique au systeme
	 * d'exploitation.
	 * 
	 * @throws ParserConfigurationException
	 */
	public Window() throws ParserConfigurationException {
		try {
			String os = System.getProperty("os.name").toLowerCase();
			// For windows os
			if (os.contains("windows")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}
			// For linux os
			if ((os.contains("linux")) || (os.contains("unix"))) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			}
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		PropertiesWindow(); // Appelle les propriétés de l'interface graphique

	}

	/**
	 * Initialise l'interface graphique notamment en y ajoutant la classe MapPanel.
	 * 
	 * @throws ParserConfigurationException
	 */
	private void PropertiesWindow() throws ParserConfigurationException {

		// parametres de la fenetre
		this.setSize(sizeX, sizeY);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Route instructions");

		// ajout du mapPanel
		final MapPanel mapPanel = new MapPanel();
		this.setContentPane(mapPanel);

		// parametres du mapPanel
		mapPanel.setZoom(16); // set some zoom level (1-18 are valid)
		double lon = pointsIntersection[stepNumber][0];
		double lat = pointsIntersection[stepNumber][1];
		Point position = mapPanel.computePosition(new Point2D.Double(lon, lat));
		Point positionCenter = new Point(position.x - sizeX / 2, position.y - sizeY / 2);
		mapPanel.setCenterPosition(positionCenter); // sets to the computed position
		mapPanel.repaint(); // if already visible trigger a repaint here

		mapPanel.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
//				System.out.println("Les coordonnées graphiques de la souris: " + p.x + "," + p.y);
				// mapPanel.repaint();
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		WindowContainer(); // Appelle le contenu de la fenetre

	}

	/**
	 * Partie principale de l'interface graphique, contenant la plupart des objets
	 * swing.
	 * 
	 * @throws ParserConfigurationException
	 */
	private void WindowContainer() throws ParserConfigurationException {

		// Panel Informations (Départ,Arrivée,Distance,Temps)

		final JLabel depart = new JLabel();
		depart.setBounds(sizeX - 420, 15, 150, 40);
		depart.setFont(new Font("", Font.BOLD, 12));
		depart.setText("  Départ : " + departLonSimp + "," + departLatSimp);
		this.add(depart);

		final JLabel arrivee = new JLabel();
		arrivee.setBounds(sizeX - 280, 15, 150, 40);
		arrivee.setFont(new Font("", Font.BOLD, 12));
		arrivee.setText("  Arrivée : " + arriveeLonSimp + "," + arriveeLatSimp);
		this.add(arrivee);

		final JLabel distance = new JLabel();
		distance.setBounds(sizeX - 400, 35, 140, 40);
		distance.setFont(new Font("", Font.BOLD, 11));
		distance.setText("  Distance : " + distanceTrajetKm + " km");
		this.add(distance);

		final JLabel temps = new JLabel();
		temps.setBounds(sizeX - 270, 35, 140, 40);
		temps.setFont(new Font("", Font.BOLD, 11));
		temps.setText("  Temps : " + tempsTrajetHeure + "h" + tempsTrajetMinute + "min");
		this.add(temps);

		final JPanel panel = new JPanel();
		panel.setBounds(sizeX - 430, 20, 390, 50);
		panel.setBackground(new Color(200, 200, 200, 150));

		// Panel contenant les Instructions

		final JLabel initLabel = new JLabel();
		initLabel.setBounds(150, 520, 900, 300);
		initLabel.setFont(new Font("", Font.BOLD, 30));
		initLabel.setText("Veuillez entrer des coordonnées de Départ et d'Arrivée.");
		initLabel.setForeground(new Color(255, 255, 255, 200));
		initLabel.setHorizontalAlignment(SwingConstants.CENTER);
		initLabel.setVisible(true);
		this.add(initLabel);

		final JLabel stepLabel = new JLabel();
		stepLabel.setBounds(250, 610, 700, 50);
		stepLabel.setFont(new Font("", Font.BOLD, 22));
		stepLabel.setText("Etape " + (int) (stepNumber + 1) + "/" + pointsIntersection.length);
		stepLabel.setForeground(Color.black);
		stepLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stepLabel.setVisible(false);
		this.add(stepLabel);

		final JLabel instructionLabel = new JLabel();
		instructionLabel.setBounds(250, 665, 700, 50);
		instructionLabel.setFont(new Font("", Font.BOLD, 20));
		instructionLabel.setText(listInstruction[stepNumber]);
//		instructionLabel.setText(instruction);
		instructionLabel.setForeground(Color.black);
		instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionLabel.setVisible(false);
		this.add(instructionLabel);

		final JLabel instructionInfoLabel = new JLabel();
		instructionInfoLabel.setBounds(250, 640, 700, 50);
		instructionInfoLabel.setFont(new Font("", Font.PLAIN, 18));
		instructionInfoLabel.setText("Dans " + distanceStepM + " metres.");
		instructionInfoLabel.setForeground(Color.black);
		instructionInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionInfoLabel.setVisible(false);
		this.add(instructionInfoLabel);

		final JPanel instructionPanel = new JPanel();
		instructionPanel.setBounds(250, 620, 700, 100);
		instructionPanel.setBackground(new Color(200, 200, 200, 180));
		instructionPanel.setVisible(false);
		this.add(instructionPanel);

		// Bouttons controlant la gestion des étapes

		final JButton previousStep = new JButton();
		previousStep.setBounds(50, 620, 150, 100);
		previousStep.setContentAreaFilled(false);
		;
		previousStep.setText("Précédent");
		previousStep.setFont(new Font("", Font.BOLD, 20));
		previousStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (stepNumber > 0) {
					stepNumber = stepNumber - 1;
					distanceStep = itineraire.getPortions().get(0).getSteps()[stepNumber].getDistance();
					distanceStepM = Math.round(distanceStep * 100.0) / 100.0;

					stepLabel.setText("Etape " + (int) (stepNumber + 1) + "/" + pointsIntersection.length);
					instructionLabel.setText(listInstruction[stepNumber]);
					instructionInfoLabel.setText("Dans " + distanceStepM + " metres :");
				}
			}
		});
		previousStep.setVisible(false);
		this.add(previousStep);

		final JPanel previousStepPanel = new JPanel();
		previousStepPanel.setBounds(50, 620, 150, 100);
		previousStepPanel.setBackground(new Color(200, 200, 200, 180));
		previousStepPanel.setFont(new Font("", Font.BOLD, 20));
		previousStepPanel.setVisible(false);
		this.add(previousStepPanel);

		final JButton nextStep = new JButton();
		nextStep.setBounds(sizeX - 200, 620, 150, 100);
		nextStep.setContentAreaFilled(false);
		;
		nextStep.setText("Suivant");
		nextStep.setFont(new Font("", Font.BOLD, 20));
		nextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (stepNumber < pointsIntersection.length - 2) {
					stepNumber = stepNumber + 1;
					distanceStep = itineraire.getPortions().get(0).getSteps()[stepNumber].getDistance();
					distanceStepM = Math.round(distanceStep * 100.0) / 100.0;

//	        		try {instruction = Instruction.GetInstruction(stepNumber, itineraire);} 
//	        		catch (ParserConfigurationException e1) {e1.printStackTrace();}
					stepLabel.setText("Etape " + (int) (stepNumber + 1) + "/" + pointsIntersection.length);
					instructionLabel.setText(listInstruction[stepNumber]);
//					instructionLabel.setText(instruction);
					instructionInfoLabel.setText("Dans " + distanceStepM + " metres :");
				} else if (stepNumber == pointsIntersection.length - 2) {
					stepNumber = stepNumber + 1;
					distanceStep = itineraire.getPortions().get(0).getSteps()[stepNumber].getDistance();
					distanceStepM = Math.round(distanceStep * 100.0) / 100.0;

					stepLabel.setText("Etape " + (int) (stepNumber + 1) + "/" + pointsIntersection.length);
					instructionLabel.setText("Vous êtes arrivés");
//					instructionLabel.setText(instruction);
					instructionInfoLabel.setText("Dans " + distanceStepM + " metres :");
				} else if (stepNumber == pointsIntersection.length - 1) {
					stepNumber = stepNumber + 1;

					stepLabel.setText("Arrivée");
					instructionLabel.setText("Vous êtes arrivés à destination.");
					instructionInfoLabel.setText("Félicitations");
				}
			}
		});
		nextStep.setVisible(false);
		this.add(nextStep);

		final JPanel nextStepPanel = new JPanel();
		nextStepPanel.setBounds(sizeX - 200, 620, 150, 100);
		nextStepPanel.setBackground(new Color(200, 200, 200, 180));
		nextStepPanel.setFont(new Font("", Font.BOLD, 20));
		nextStepPanel.setVisible(false);
		this.add(nextStepPanel);

		// Panel permettant de Modifier les coordonnées de Départ et d'Arrivée

		final JLabel modifierDepart = new JLabel();
		modifierDepart.setBounds(sizeX - 360, 90, 50, 20);
		modifierDepart.setFont(new Font("", Font.BOLD, 12));
		modifierDepart.setText("Départ :");
		modifierDepart.setVisible(false);
		this.add(modifierDepart);

		final JTextField modifierDepartTextLon = new JTextField();
		modifierDepartTextLon.setBounds(sizeX - 300, 90, 120, 20);
		modifierDepartTextLon.setVisible(false);
		this.add(modifierDepartTextLon);

		final JTextField modifierDepartTextLat = new JTextField();
		modifierDepartTextLat.setBounds(sizeX - 170, 90, 120, 20);
		modifierDepartTextLat.setVisible(false);
		this.add(modifierDepartTextLat);

		// Modifier l'arrivee

		final JLabel modifierArrivee = new JLabel();
		modifierArrivee.setBounds(sizeX - 360, 130, 50, 20);
		modifierArrivee.setFont(new Font("", Font.BOLD, 12));
		modifierArrivee.setText("Arrivée :");
		modifierArrivee.setVisible(false);
		this.add(modifierArrivee);

		final JTextField modifierArriveeTextLon = new JTextField();
		modifierArriveeTextLon.setBounds(sizeX - 300, 130, 120, 20);
		modifierArriveeTextLon.setVisible(false);
		this.add(modifierArriveeTextLon);

		final JTextField modifierArriveeTextLat = new JTextField();
		modifierArriveeTextLat.setBounds(sizeX - 170, 130, 120, 20);
		modifierArriveeTextLat.setVisible(false);
		this.add(modifierArriveeTextLat);

		// Modifier Remplissage Automatique

		final JLabel coordLabel1 = new JLabel();
		final JLabel coordLabel2 = new JLabel();
		final JLabel coordLabel3 = new JLabel();
		coordLabel1.setBounds(sizeX - 360, 165, 300, 20);
		coordLabel2.setBounds(sizeX - 340, 230, 300, 20);
		coordLabel3.setBounds(sizeX - 340, 250, 300, 20);
		coordLabel1.setText("Remplir Automatiquement :");
		coordLabel2.setText("Cliquez sur la carte pour obtenir les coordonnées");
		coordLabel3.setText("puis choisissez de remplir le Départ ou l'Arrivée");
		coordLabel1.setFont(new Font("", Font.BOLD, 14));
		coordLabel2.setFont(new Font("", Font.BOLD, 12));
		coordLabel3.setFont(new Font("", Font.BOLD, 12));
		coordLabel1.setVisible(false);
		coordLabel2.setVisible(false);
		coordLabel3.setVisible(false);
		this.add(coordLabel1);
		this.add(coordLabel2);
		this.add(coordLabel3);

		final JButton coordDepartButton = new JButton();
		coordDepartButton.setBounds(sizeX - 350, 195, 60, 30);
		coordDepartButton.setText("Depart");
		coordDepartButton.setFont(new Font("", Font.BOLD, 7));
		coordDepartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifierDepartTextLon.setText(Double.toString(MapPanel.coordMapLon));
				modifierDepartTextLat.setText(Double.toString(MapPanel.coordMapLat));
			}
		});
		coordDepartButton.setVisible(false);
		this.add(coordDepartButton);

		final JButton coordArriveeButton = new JButton();
		coordArriveeButton.setBounds(sizeX - 280, 195, 60, 30);
		coordArriveeButton.setText("Arrivee");
		coordArriveeButton.setFont(new Font("", Font.BOLD, 7));
		coordArriveeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifierArriveeTextLon.setText(Double.toString(MapPanel.coordMapLon));
				modifierArriveeTextLat.setText(Double.toString(MapPanel.coordMapLat));
			}
		});
		coordArriveeButton.setVisible(false);
		this.add(coordArriveeButton);

		final JPanel modifierPanel = new JPanel();
		modifierPanel.setBounds(sizeX - 370, 75, 330, 250);
		modifierPanel.setBackground(new Color(200, 200, 200, 100));
		modifierPanel.setVisible(false);

		// Le Bouton permettant d'afficher le panel de modification

		final JButton modifierValider = new JButton();

		final JToggleButton modifier = new JToggleButton();
		modifier.setBounds(sizeX - 130, 25, 80, 40);
		modifier.setFont(new Font("", Font.BOLD, 10));
		modifier.setText("Modifier");
		modifier.setForeground(new Color(0, 0, 0, 255));
		final ItemListener itemListener = new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					MapPanel.drawCross = true;
					modifierDepartTextLon.setText(Double.toString(departLon));
					modifierDepartTextLat.setText(Double.toString(departLat));
					modifierArriveeTextLon.setText(Double.toString(arriveeLon));
					modifierArriveeTextLat.setText(Double.toString(arriveeLat));
					modifierPanel.setVisible(true);
					modifierDepart.setVisible(true);
					modifierDepartTextLon.setVisible(true);
					modifierDepartTextLat.setVisible(true);
					modifierArrivee.setVisible(true);
					modifierArriveeTextLon.setVisible(true);
					modifierArriveeTextLat.setVisible(true);
					coordLabel1.setVisible(true);
					coordLabel2.setVisible(true);
					coordLabel3.setVisible(true);
					coordDepartButton.setVisible(true);
					coordArriveeButton.setVisible(true);
					modifierValider.setVisible(true);
					modifier.setText("Retour");
				} else {
					MapPanel.drawCross = false;
					modifierPanel.setVisible(false);
					modifierDepart.setVisible(false);
					modifierDepartTextLon.setVisible(false);
					modifierDepartTextLat.setVisible(false);
					modifierArrivee.setVisible(false);
					modifierArriveeTextLon.setVisible(false);
					modifierArriveeTextLat.setVisible(false);
					coordLabel1.setVisible(false);
					coordLabel2.setVisible(false);
					coordLabel3.setVisible(false);
					coordDepartButton.setVisible(false);
					coordArriveeButton.setVisible(false);
					modifierValider.setVisible(false);
					modifier.setText("Modifier");
				}
			}
		};
		modifier.addItemListener(itemListener);
		this.add(modifier);

		// Boutton de Validation

		modifierValider.setBounds(sizeX - 350, 280, 290, 30);
		modifierValider.setText("Valider");
		modifierValider.setForeground(new Color(12, 199, 13));
		modifierValider.setFont(new Font("", Font.BOLD, 10));
		modifierValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initial = true;
				stepLabel.setVisible(true);
				instructionLabel.setVisible(true);
				instructionInfoLabel.setVisible(true);
				instructionPanel.setVisible(true);
				previousStep.setVisible(true);
				previousStepPanel.setVisible(true);
				nextStep.setVisible(true);
				nextStepPanel.setVisible(true);
				initLabel.setVisible(false);

				departLon = Double.parseDouble(modifierDepartTextLon.getText());
				departLat = Double.parseDouble(modifierDepartTextLat.getText());
				arriveeLon = Double.parseDouble(modifierArriveeTextLon.getText());
				arriveeLat = Double.parseDouble(modifierArriveeTextLat.getText());
				departCoord = new double[] { departLon, departLat };
				arriveeCoord = new double[] { arriveeLon, arriveeLat };
				itineraire = Itineraire.CalculItineraire(departCoord, arriveeCoord);
				pointsIntersection = Intersection.PointsIntersection(itineraire);
				distanceTrajet = itineraire.getPortions().get(0).getDistance();
				tempsTrajet = itineraire.getPortions().get(0).getDuration();
				try {
					listInstruction = Instruction.GetListInstruction(itineraire);
				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				}

				departLonSimp = Math.round(departLon * 10000.0) / 10000.0;
				departLatSimp = Math.round(departLat * 10000.0) / 10000.0;
				arriveeLonSimp = Math.round(arriveeLon * 10000.0) / 10000.0;
				arriveeLatSimp = Math.round(arriveeLat * 10000.0) / 10000.0;
				distanceTrajetKm = Math.round(distanceTrajet / 1000 * 100.0) / 100.0;
				tempsTrajetHeure = (int) tempsTrajet / 60;
				tempsTrajetMinute = (int) tempsTrajet - tempsTrajetHeure * 60;
				distanceStep = itineraire.getPortions().get(0).getSteps()[stepNumber].getDistance();
				distanceStepM = Math.round(distanceStep * 100.0) / 100.0;
				tempsStep = itineraire.getPortions().get(0).getSteps()[stepNumber].getDuration();
				tempsStepMinute = (int) tempsStep;

				stepNumber = 0;
				depart.setText("  Départ : " + departLonSimp + "," + departLatSimp);
				arrivee.setText("  Arrivée : " + arriveeLonSimp + "," + arriveeLatSimp);
				distance.setText("  Distance : " + distanceTrajetKm + " km");
				temps.setText("  Temps : " + tempsTrajetHeure + "h" + tempsTrajetMinute + "min");
				stepLabel.setText("Etape " + (int) (stepNumber + 1) + "/" + pointsIntersection.length);
				instructionLabel.setText(listInstruction[stepNumber]);
				instructionInfoLabel.setText("Dans " + distanceStepM + " metres :");
			}
		});
		modifierValider.setVisible(false);
		this.add(modifierValider);

		this.add(modifierPanel);

		this.add(panel);

	}
}
