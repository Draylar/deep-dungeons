package draylar.dd.api;

import java.util.Random;

public class Vel2D {

    private final double x;
    private final double z;

    public Vel2D(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    /**
     * Returns a {@link Vel2D} with x and z values in the range of [-1, 1].
     * @param random  random seed
     * @return        {@link Vel2D} with random value in [-1, 1] for x and z
     */
    public static Vel2D getRandom(Random random) {
        return new Vel2D(1 - random.nextDouble() * 2, 1 - random.nextDouble() * 2);
    }
}
