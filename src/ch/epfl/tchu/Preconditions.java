package ch.epfl.tchu;

/**
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */

public final class Preconditions {
    private Object IllegalArgumentException;

    private Preconditions() {}

    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}