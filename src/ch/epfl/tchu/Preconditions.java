package ch.epfl.tchu;

public final class Preconditions {
    private Object IllegalArgumentException;

    private Preconditions() {}

    void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}