package util;

public class Util {
    public static void printInBox(String text) {
        int length = text.length();
        String horizontalBorder = "+" + "-".repeat(length + 2) + "+";

        System.out.println(horizontalBorder);
        System.out.println("| " + text + " |");
        System.out.println(horizontalBorder);
    }
}
