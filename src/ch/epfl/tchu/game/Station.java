package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * Created at 13:40 on 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */

public final class Station {
    private final int id;
    private final String name;

    /** Constructeur de Stations.
     * @param id Le numéro de la station. DOIT être unique.
     * @param name Le nom de la station. Peut avoir des doublons pour les gares de pays.
     */
    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;
    }

    /**
     * @return Renvoie le nom de la Station. Préférer name() si possible.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * @return Renvoie l'identification unique de la Station.
     */
    public int id() {
        return id;
    }

    /**
     * @return Renvoie le nom de la Station.
     */
    public String name() {
        return name;
    }

}
