package utilities;

import java.util.Scanner;

public class InputOutput {

    private static final Scanner input = new Scanner(System.in);

    public static String inputString(String messageToUser) {
        System.out.print(messageToUser + ":");

        return input.nextLine();
    }

    public static int inputInt(String messageToUser) {
        System.out.print(messageToUser + ":");

        int inputResult = input.nextInt();
        input.nextLine();

        return inputResult;
    }

    public static double inputDouble(String messageToUser) {
        System.out.print(messageToUser + ":");

        double inputResult = input.nextDouble();
        input.nextLine();

        return inputResult;
    }

    public static boolean inputBoolean(String messageToUser) {
        System.out.print(messageToUser + ":");

        boolean inputResult = input.nextBoolean();
        input.nextLine();

        return inputResult;
    }

    public static float inputFloat(String messageToUser) {
        System.out.print(messageToUser + ":");

        float inputResult = input.nextFloat();
        input.nextLine();

        return inputResult;
    }

    public static String line() {
        return "--------------------------------------------------------------------------------------------------------\n";
    }

    public static String shortLine() {
        return "----------------------------------------------------\n";
    }

    public static void closeScanner() {
        input.close();
    }
}
