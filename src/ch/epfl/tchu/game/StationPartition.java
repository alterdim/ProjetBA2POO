package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Fichier créé à 14:03 le 08/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class StationPartition implements StationConnectivity{
    private final int[] partition;

    @Override
    public boolean connected(Station s1, Station s2) {
        return Arrays.asList(partition, true).contains(s1.id()) && Arrays.asList(partition, true).contains(s2.id());
    }

    private StationPartition(int[] partition) {
        this.partition = partition;
    }

    public final static class Builder {
        private final int[] flatPartition;

        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount>=0 );
            flatPartition = new int[stationCount];
            for (int i=0; i<flatPartition.length; i++) {
                flatPartition[i] = i;
            }
        }

        public Builder connect(Station s1, Station s2) {
            int rep1 = this.representative(s1.id());
            int rep2 = this.representative(s2.id());
            flatPartition[rep1] = rep2;
            return this;
        }

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
