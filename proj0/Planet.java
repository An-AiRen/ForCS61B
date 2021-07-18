public class Planet {
	/** Basic version */
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	private static double G = 6.67e-11;

	public Planet(double xP, double yP, double xV,
              double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	/** calculates the distance between two Planets */
	public double calcDistance(Planet p) {
		double sqD = Math.pow(this.xxPos - p.xxPos, 2) + Math.pow(this.yyPos - p.yyPos, 2);
		return Math.sqrt(sqD);
	}

	/** return the force exerted on this planet by given planet */
	public double calcForceExertedBy(Planet exerter) {
		return G * this.mass * exerter.mass / Math.pow(calcDistance(exerter), 2);
	}

	public double calcForceExertedByX(Planet exerter) {
		return calcForceExertedBy(exerter) * (exerter.xxPos - this.xxPos) / calcDistance(exerter);
	}

	public double calcForceExertedByY(Planet exerter) {
		return calcForceExertedBy(exerter) * (exerter.yyPos - this.yyPos) / calcDistance(exerter);
	}

	/** calculate net X and net Y force */
	public double calcNetForceExertedByX(Planet[] allPlanets) {
		double netX = 0.0;
		for (int i = 0; i < allPlanets.length; i++) { //normal version
			if (this.equals(allPlanets[i])) {
				continue;
			}
			netX += calcForceExertedByX(allPlanets[i]);
		}
		return netX;
	}

	public double calcNetForceExertedByY(Planet[] allPlanets) {
		double netY = 0.0;
		for (Planet p : allPlanets) { //enhanced for loops
			if (this.equals(p)) {
				continue;
			}
			netY += calcForceExertedByY(p);
		}
		return netY;
	}

	/** update: how much the forces exerted on the planet will cause that planet to accelerate */
	public void update(double dt, double xF, double yF) {
		double xa = xF / this.mass;
		double ya = yF / this.mass;

		this.xxVel += dt * xa;
		this.yyVel += dt * ya;

		this.xxPos += this.xxVel * dt;
		this.yyPos += this.yyVel * dt;
	}

	/** draw planets */
	public void draw() {
		StdDraw.picture(this.xxPos, this.yyPos, this.imgFileName);
	}
}