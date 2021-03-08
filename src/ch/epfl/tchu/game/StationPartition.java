package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Arrays;

/**
 * Fichier créé à 14:03 le 08/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class StationPartition implements StationConnectivity{
    private final int[] partition;

    /** Vérifie si deux stations font partie de la même partition de connectivité.
     * @param s1 Station à vérifier 1
     * @param s2 Station à vérifier 2
     * @return True si les deux stations font partie de la partition, false sinon.
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        return Arrays.asList(partition, true).contains(s1.id()) && Arrays.asList(partition, true).contains(s2.id());
    }

    private StationPartition(int[] partition) {
        this.partition = partition;
    }

    /**
     * Builder de StationPartition. Utilise une structure de disjoint set pour relier les stations connectées entre elles.
     */
    public final static class Builder {
        private final int[] flatPartition;

        /**Constructeur de StationConnectivityBuilder
         * @param stationCount le nombre de stations qui feront partie de la partition.
         */
        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount>=0 );
            flatPartition = new int[stationCount];
            for (int i=0; i<flatPartition.length; i++) {
                flatPartition[i] = i;
            }
        }

        /** "Fusionne" deux sous-partitions de stations et choisit un nouveau représentant arbitrairement.
         * @param s1 Station 1 à connecter
         * @param s2 Station 2 à connecter
         * @return le builder, dont la partition a été mise à jour.
         */
        public Builder connect(Station s1, Station s2) {
            int rep1 = this.representative(s1.id());
            int rep2 = this.representative(s2.id());
            flatPartition[rep1] = rep2;
            return this;
        }

        /** Build la StationPartition et en profite pour aplatir la représentation.
         * @return la StationPartition.
         */
        public StationPartition build() {
            for (int i : flatPartition) {
                flatPartition[i] = representative(i);
            }
            return new StationPartition(flatPartition);
        }

        private int representative(int stationId) {
            if (flatPartition[stationId] == stationId) {
                return stationId;
            }
            else {
                return representative(flatPartition[stationId]);
            }

        }
    }


}
