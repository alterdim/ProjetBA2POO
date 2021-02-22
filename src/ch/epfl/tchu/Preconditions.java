package ch.epfl.tchu;

public final class Preconditions {
    private Object IllegalArgumentException;

    private Preconditions() {}

    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}