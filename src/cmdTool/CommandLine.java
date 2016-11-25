package cmdTool;

import semantics.exception.InvalidParameterException;
import semantics.exception.NoTokenFoundException;
import semantics.translate.NLTranslator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CommandLine {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        NLTranslator translator = new NLTranslator("git", "compile");
        Scanner scanner = new Scanner(System.in);
        String command = null;
        while (true) {
            System.out.print(ANSI_GREEN + "If you want to use superman, please enter 1, else 0: " + ANSI_RESET);
            String opt = scanner.nextLine();
            System.out.print(ANSI_GREEN + "\nPlease input the command line: " + ANSI_RESET);
            if (opt.equals("1")) {
                try {
                    command = translator.translate(scanner.nextLine());
                } catch (NoTokenFoundException | InvalidParameterException e) {
                    System.out.println(ANSI_RED + e.getMessage() + ANSI_RESET);
                }
            } else {
                command = scanner.nextLine();
            }
            System.out.println(String.format(ANSI_GREEN + "\nPlease tell me whether you want to execute: "+ ANSI_RED + "%s" + ANSI_RESET + ANSI_RESET, command));

            System.out.print(ANSI_GREEN + "\nIf yes, please enter 1. If no, please enter 0: " + ANSI_RESET);
            boolean flag = Integer.parseInt(scanner.nextLine()) == 1;
            if (flag) {
                System.out.println(ANSI_BLUE + "\nExecuting the command line now..." + ANSI_RESET);
                if (!execute(command)) {
                    System.out.println();
                    continue;
                }
            } else {
                System.out.println(ANSI_WHITE + "\nDo you want to exit? If so, please enter 'Exit', else enter any instructions" + ANSI_RESET);
            }
            if (scanner.nextLine().equals("Exit")) break;
        }
    }

    private static boolean execute(String line) {
        Runtime rt = Runtime.getRuntime();
        Process pr;
        try {
            String[] commands = line.split("\"");
            String[] first = commands[0].split(" ");
            String[] run = new String[first.length + 1];
            if (commands.length == 2) {
                int i;
                for (i = 0; i < first.length; i++) {
                    run[i] = first[i];
                }
                run[i] = commands[1];
                pr = rt.exec(run);
            } else {
                pr = rt.exec(line);
            }
        } catch (IOException e) {
            System.out.print(ANSI_RED + e.getMessage() + ANSI_RESET);
            return false;
        }
        BufferedReader stdInput = null, stdError = null;
        if (pr.getInputStream() != null) {
            stdInput = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
        }
        if (pr.getInputStream() != null) {
            stdError = new BufferedReader(new
                    InputStreamReader(pr.getErrorStream()));
        }
        if (stdInput != null) {
            System.out.println(ANSI_PURPLE + "\nHere is the standard output of the command:" + ANSI_RESET);
            print(stdInput, true);
        }

        if (stdError != null) {
            print(stdError, false);
            return false;
        }
        return true;
    }

    private static void print(BufferedReader std, boolean notError) {
        try {
            String s = std.readLine();
            while (s != null) {
                if (notError) System.out.println(ANSI_CYAN  + s + ANSI_RESET);
                else System.out.println(ANSI_RED + s + ANSI_RESET);
                s = std.readLine();
            }
        } catch (IOException e) {
            System.out.print(ANSI_RED + e.getMessage() + ANSI_RESET);
        }
    }
}