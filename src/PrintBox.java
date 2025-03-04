public class PrintBox {
    public static void printInBox(String input) {
            int length = input.length();
            String border = "+" + "-".repeat(length + 2) + "+";
            String content = "| " + input + " |";

            System.out.println(border);
            System.out.println(content);
            System.out.println(border);
        }

}
