package ch.epfl.tchu;

/**
 *
 * Précondition et exception
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Preconditions {

    /**
     * Constructeur par défaut privé pour ne pas être instancié
     */
    private Preconditions() {}

    /**
     * Lève une excpetion
     * @param shouldBeTrue paramètre controler
     * @throws IllegalArgumentException si la condition n'est pas satisfaite
     */
    public static void checkArgument(boolean shouldBeTrue) {
        if (!shouldBeTrue) {
            throw new IllegalArgumentException();
        }
    }
}