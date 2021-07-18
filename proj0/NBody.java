public class NBody {
	public static double readRadius(String source) {
		In origin = new In(source);
		int N = origin.readInt();
		double radius = origin.readDouble();
		return radius;
	}

	public static Planet[] readPlanets(String source) {
		In origin = new In(source);
		int N = origin.readInt();
		double radius = origin.readDouble();

		Planet[] planets = new Planet[N];
		for (int i = 0; i < N; i++) {
			planets[i] = new Planet(origin.readDouble(), origin.readDouble(), origin.readDouble(), 
				origin.readDouble(), origin.readDouble(), "images/" + origin.readString());
		}
		return planets;
	}

	/** Draw the initial Uni State */
	public static void main(String[] args) {
		StdDraw.enableDoubleBuffering();

		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		double time = 0.0;
		String filename = args[2];
		double radius = NBody.readRadius(filename);
		Planet[] allplanets = NBody.readPlanets(filename);

		StdDraw.setScale(-radius, radius);

		while (time < T) {
			double[] xForces = new double[allplanets.length];
			double[] yForces = new double[allplanets.length];

			for (int i = 0; i < allplanets.length; i++) {
				xForces[i] = allplanets[i].calcNetForceExertedByX(allplanets);
				yForces[i] = allplanets[i].calcNetForceExertedByY(allplanets);
			}

			for (int i = 0; i < allplanets.length; i++) {
				allplanets[i].update(dt, xForces[i], yForces[i]);
			}

			StdDraw.clear();

			/** Draw background img */
			StdDraw.picture(0, 0, "images/starfield.jpg");

			/** Draw all of the planets */
			for (Planet p : allplanets) {
				p.draw();
			}

			StdDraw.show();
			StdDraw.pause(10);

			time += dt;
		}

		StdOut.printf("%d\n", allplanets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < allplanets.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
							allplanets[i].xxPos, allplanets[i].yyPos, allplanets[i].xxVel, allplanets[i].yyVel, allplanets[i].mass,
							allplanets[i].imgFileName);
		}
	}
}