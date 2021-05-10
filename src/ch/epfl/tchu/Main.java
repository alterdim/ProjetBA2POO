package ch.epfl.tchu;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String string ="3-3-3";
//        String[] i = string.split(Pattern.quote("-"));
        String[] i = Pattern.quote(string).split("-");
        System.out.println(Arrays.asList(i));
    }
}
