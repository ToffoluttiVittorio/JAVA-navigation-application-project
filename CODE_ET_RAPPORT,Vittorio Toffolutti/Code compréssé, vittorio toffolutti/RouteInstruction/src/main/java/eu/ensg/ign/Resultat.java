package eu.ensg.ign;

import java.util.List;

public class Resultat {

	//getters setters profile
	private String profile;
	public void setProfile(String profile) { this.profile = profile; }
	public String getProfile() { return this.profile; }

	//getters setters portions
	private List<Portion> portions;
	public List<Portion> getPortions() { return this.portions; }
	public void setPortions(List<Portion> portions) { this.portions = portions; }

	//getters setters geometry
	private Geometry geometry;
	public Geometry getGeometry() { return this.geometry; }
	public void setGeometry(Geometry geometry) { this.geometry = geometry; }
}
