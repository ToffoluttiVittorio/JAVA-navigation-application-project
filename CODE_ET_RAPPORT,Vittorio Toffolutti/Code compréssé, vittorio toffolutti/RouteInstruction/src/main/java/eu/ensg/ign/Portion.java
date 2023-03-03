package eu.ensg.ign;

public class Portion {

	//getters setters distance
	private double distance;
	public double getDistance() { return this.distance; }
	public void setDistance(double distance) { this.distance = distance; }

	//getters setters duration
	private double duration;
	public double getDuration() { return this.duration; }
	public void setDuration(double duration) { this.duration = duration; }

	//getters setters start
	private String start;
	public String getStart() { return this.start; }
	public void setStart(String start) { this.start = start; }

	//getters setters end
	private String end;
	public String getEnd() { return this.end; }
	public void setEnd(String end) { this.end = end; }

	//getters setters steps
	private Steps[] steps;
	public Steps[] getSteps() { return this.steps; }
	public void setSteps(Steps[] steps) { this.steps = steps; }
	

}
