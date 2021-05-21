package ch.epfl.tchu.gui;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Créé le 10.05.2021 à 16:17
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class ShowMyIpAddress {
    /** Test de serveur permettant de montrer l'adresse ip.
     * @param args ici inutiles
     * @throws IOException si les sockets n'ont pas pu s'ouvrir ou se fermer correctement
     */
    public static void main(String[] args) throws IOException {
        NetworkInterface.networkInterfaces()
                .filter(i -> {
                    try { return i.isUp() && !i.isLoopback(); }
                    catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                })
                .flatMap(NetworkInterface::inetAddresses)
                .filter(a -> a instanceof Inet4Address)
                .map(InetAddress::getCanonicalHostName)
                .forEachOrdered(System.out::println);
    }
}