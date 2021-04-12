package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Créé le 12.04.2021 à 15:03
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public interface Serde<T> {

    public abstract String serialize(T t);

    public abstract T deserialize(String str);

    public static <T> Serde<T> of(Function<T, String> fs, Function<String, T> fd){
        return new Serde<T>() {
            @Override
            public String serialize(T t) {
                return fs.apply(t);
            }

            @Override
            public T deserialize(String str) {
                return fd.apply(str);
            }
        };
    }

    //TODO todo
    public static <T> Serde<T> oneOf(List<Enum> enumList){
        Preconditions.checkArgument(!enumList.isEmpty());
        return new Serde<T>() {
            @Override
            public String serialize(T t) {
                return null;
            }

            @Override
            public T deserialize(String str) {
                return null;
            }
        };
    }

    //TODO todo
    //String c au lieu de char pour permettre de faire des séparateurs plus long que 1 caractère
    public static <T> Serde<T> listOf(Serde<T> serde, String c){
        return new Serde<T>() {
            @Override
            public String serialize(T t) {
                return String.join(c, t.toString());
            }

            @Override
            public T deserialize(String str) {
                return (T) str.split(Pattern.quote(c));
            }
        };
    }
//TODO todo
   /* public static <T Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, char separateur){
        return new Serde<SortedBag<T>>() {
            @Override
            public String serialize(SortedBag<T> ts) {
                return null;
            }

            @Override
            public SortedBag<T> deserialize(String str) {
                return null;
            }

            @Override
            public Serde<SortedBag<SortedBag<T>>> bagOf(Serde<SortedBag<T>> serde, char separateur) {
                return null;
            }
        }
    }*/

}
