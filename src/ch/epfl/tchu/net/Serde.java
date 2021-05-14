package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * serde (de serializer-deserializer), à savoir un objet capable de sérialiser et désérialiser des valeurs d'un type donné.
 *
 * Créé le 12.04.2021 à 15:03
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public interface Serde<T> {

    /**
     * Méthode permettant de crée un serde à partir des méthodes de sérialisation et de désérialisation
     *
     * @param fs  Function de sérialisation
     * @param fd  Function de désérialisation
     * @param <T> Type de l'object à  (dé)sérialiser
     * @return Un Serde correspond à l´objet
     */
    static <T> Serde<T> of(Function<T, String> fs, Function<String, T> fd) {
        return new Serde<>() {
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

    /**
     * Méthode permettant de crée un serde à partir d'une liste contenant tous les valeurs possible de l'objet (enumeration)
     *
     * @param list List comprenant tous les éléments de l'objet
     * @param <T>  Type de l'object à  (dé)sérialiser
     * @return Un Serde correspond à l'objet
     */
    static <T> Serde<T> oneOf(List<T> list) {
        Preconditions.checkArgument(!list.isEmpty());
        return new Serde<>() {
            @Override
            public String serialize(T t) {
                if (t!=null) return String.valueOf(list.indexOf(t));
                else return "";
            }

            @Override
            public T deserialize(String str) {
                if (str.length()>0) return list.get(Integer.parseInt(str));
                else return null;
            }
        };
    }

    /**
     * Méthode permettant de crée un serde de list d'objet à partir d´un serde et d'un caractère séparateur
     *
     * @param serde Serde correspond au type de l´objet à l´intérieur de la liste
     * @param c     caractère séparateur
     * @param <T>   Type de l´objet
     * @return Un Serde correspond à la liste
     */
    static <T> Serde<List<T>> listOf(Serde<T> serde, String c) {
        return new Serde<>() {
            @Override
            public String serialize(List<T> list) {
                List<String> listString = new ArrayList<>();
                for (T t : list) {
                    listString.add(serde.serialize(t));
                }
                return String.join(c, listString);
            }

            @Override
            public List<T> deserialize(String str) {
                List<T> listT = new ArrayList<>();
                for (String s : str.split(Pattern.quote(c), -1)) {
                    T object = serde.deserialize(s);
                    if (object !=null) listT.add(object);
                }
                return listT;
            }
        };
    }

    /**
     * Méthode permettant de crée un serde de SortedBag d'objet à partir d´un serde et d'un caractère séparateur
     *
     * @param serde Serde correspond au type de l´objet à l´intérieur du sortedBag
     * @param c     caractère séparateur
     * @param <T>   Type de l´objet
     * @return Un Serde correspond au SortedBag
     */
    static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, String c) {
        return new Serde<>() {
            @Override
            public String serialize(SortedBag<T> bag) {
                List<String> listString = new ArrayList<>();
                for (T t : bag) {
                    listString.add(serde.serialize(t));
                }
                return String.join(c, listString);
            }

            @Override
            public SortedBag<T> deserialize(String str) {
                SortedBag.Builder<T> bagT = new SortedBag.Builder<>();
                for (String s : str.split(Pattern.quote(c), -1)) {
                    T object = serde.deserialize(s);
                    if (object !=null) bagT.add(object);
                }
                return bagT.build();
            }
        };
    }

    /**
     * Méthode de sérialisation
     *
     * @param t Objet à sérialiser
     * @return String de l'objet sérialisé
     */
    String serialize(T t);

    /**
     * Méthode de désérialisation
     *
     * @param str chaîne correspond à la sérialisation de l'objet
     * @return Retourne l´objet correspond à la sérialisation
     */
    T deserialize(String str);

}
