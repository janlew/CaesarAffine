package pl.edu.ug;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String PLAIN = "plain.txt";
    public static final String CRYPTO = "crypto.txt";
    public static final String DECRYPT = "decrypt.txt";
    public static final String KEY = "key.txt";
    public static final String EXTRA = "extra.txt";
    public static final String KEY_FOUND = "key-found.txt";

    private static final int ALPHABET_SIZE = 'z' - 'a' + 1;

    public static void main(String[] args) throws Exception {
        if (args[0].equals("-c")) {
            if (args[1].equals("-e")) {
                encodeCezar();
            } else if (args[1].equals("-d")) {
                decodeCezar(KEY);
            } else if (args[1].equals("-j")) {
                searchKeyCezar();
            } else if (args[1].equals("-k")) {
                searchKeyCezarWO();
            }
        } else if (args[0].equals("-a")) {
            if (args[1].equals("-e")) {
                encodeAfiniczny();
            } else if (args[1].equals("-d")) {
                decodeAfiniczny(KEY);
            } else if (args[1].equals("-j")) {
                searchKeyAfiniczny();
            } else if (args[1].equals("-k")) {
                searchKeyAfinicznyWO();
            }
        }
    }

    public static String readFile(String name) {
        try {
            File file = new File(name);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                scanner.useDelimiter("\\Z");
                String data = scanner.next();
                return data;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Nie można otworzyć pliku.");
        }
        return null;
    }

    public static void writeFile(String name, String text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(name));
        writer.write(text);
        writer.close();
    }

    public static int nwd(int pierwsza, int druga) {
        while (pierwsza != druga) {
            if (pierwsza > druga) {
                pierwsza = pierwsza - druga;
            } else {
                druga = druga - pierwsza;
            }
        }
        return pierwsza;
    }

    public static boolean checkKeyC() {
        try {
            int key = Integer.parseInt(readFile(KEY));
            if (key < 1 || key > 25) {
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int[] getKeyA(String keyFile) throws Exception {
        File file = new File(keyFile);
        Scanner scanner = new Scanner(file);
        int a[] = new int[10];
        int i = 0;
        String str;
        while (scanner.hasNext()) {
            str = scanner.next();
            try {
                int key = Integer.parseInt(str);
                a[i] = key;
                ++i;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (i > 2 || i < 2) {
            return null;
        }
        if (nwd(a[0], 26) != 1) {
            return null;
        }
        return a;
    }

    public static void encodeCezar() throws Exception {
        String text = readFile(PLAIN);
        char[] chars = text.toCharArray();

        if (checkKeyC() == true) {
            int key = Integer.parseInt(readFile(KEY));
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                char lowerUpper;

                if (Character.isLowerCase(c)) {
                    lowerUpper = 'a';
                } else if (Character.isUpperCase(c)) {
                    lowerUpper = 'A';
                } else {
                    continue;
                }

                c -= lowerUpper;
                c += key;
                c %= ALPHABET_SIZE;
                c += lowerUpper;
                chars[i] = c;
            }
            writeFile(CRYPTO, String.valueOf(chars));
        } else {
            System.out.println("Podano niepoprawny klucz! Podaj jedna liczbe");
        }
    }

    public static void decodeCezar(String keyFile) throws Exception {
        String text = readFile(CRYPTO);
        char[] chars = text.toCharArray();

        if (checkKeyC() == true) {
            int key = Integer.parseInt(readFile(keyFile));

            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                char lowerUpper;

                if (Character.isLowerCase(c)) {
                    lowerUpper = 'a';
                } else if (Character.isUpperCase(c)) {
                    lowerUpper = 'A';
                } else {
                    continue;
                }

                c -= lowerUpper;
                if (c - key < 0) {
                    c += ALPHABET_SIZE;
                }
                c -= key;
                c %= ALPHABET_SIZE;
                c += lowerUpper;
                chars[i] = c;
            }
            writeFile(DECRYPT, String.valueOf(chars));
        } else {
            System.out.println("Podano niepoprawny klucz! Podaj jedna liczbe");
        }
    }

    public static void searchKeyCezar() throws Exception {
        String textC = readFile(CRYPTO);
        char[] charsC = textC.toCharArray();
        String textD = readFile(EXTRA);
        char[] charsD = textD.toCharArray();
        int key = -1;
        int tempKey = -1;
        try {
            if (charsC.length > 0 && charsD.length > 0) {
                for (int i = 0; i < charsD.length; i++) {
                    key = (charsC[i] - charsD[i]) % ALPHABET_SIZE;
                    if (key < 0) {
                        key += ALPHABET_SIZE;
                    }
                    if (tempKey < key) {
                        tempKey = key;
                    }
                    if (key == 0) {
                        key = tempKey;
                    }
                }
                writeFile(KEY_FOUND, String.valueOf(key));
                decodeCezar(KEY_FOUND);

            } else {
                System.out.println("Puste pliki");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Extra dłuższe od crypto");
        }
    }

    public static void searchKeyCezarWO() throws Exception {
        String text = readFile(CRYPTO);
        char[] chars = text.toCharArray();
        String results = "";

        if (chars.length > 0) {
            for (int key = 1; key < 26; key++) {
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];
                    char lowerUpper;

                    if (Character.isLowerCase(c)) {
                        lowerUpper = 'a';
                    } else if (Character.isUpperCase(c)) {
                        lowerUpper = 'A';
                    } else {
                        continue;
                    }

                    c -= lowerUpper;
                    if (c - key < 0) {
                        c += ALPHABET_SIZE;
                    }
                    c -= key;
                    c %= ALPHABET_SIZE;
                    c += lowerUpper;
                    chars[i] = c;
                }
                results += String.valueOf(chars) + "\n";
            }
            writeFile(PLAIN, results);
        }
    }

    public static void encodeAfiniczny() throws Exception {
        String text = readFile(PLAIN);
        char[] chars = text.toCharArray();

        if (getKeyA(KEY) != null) {
            int tab[] = getKeyA(KEY);
            int a = tab[0];
            int b = tab[1];

            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                char lowerUpper;

                if (Character.isLowerCase(c)) {
                    lowerUpper = 'a';
                } else if (Character.isUpperCase(c)) {
                    lowerUpper = 'A';
                } else {
                    continue;
                }

                c -= lowerUpper;
                c *= a;
                c += b;
                c %= ALPHABET_SIZE;
                c += lowerUpper;
                chars[i] = c;
            }
            writeFile(CRYPTO, String.valueOf(chars));
        } else {
            System.out.println("Zły klucz, podaj dwie liczby (a, b) oddzielone spacją, NWD(a, 26) musi być 1");
        }
    }

    public static void decodeAfiniczny(String keyFile) throws Exception {
        int x = 1;
        String text = readFile(CRYPTO);
        char[] chars = text.toCharArray();

        if (getKeyA(keyFile) != null) {
            int tab[] = getKeyA(keyFile);
            int a = tab[0];
            int b = tab[1];

            while ((a * x) % ALPHABET_SIZE != 1) {
                x++;
            }

            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                char lowerUpper;

                if (Character.isLowerCase(c)) {
                    lowerUpper = 'a';
                } else if (Character.isUpperCase(c)) {
                    lowerUpper = 'A';
                } else {
                    continue;
                }

                c -= lowerUpper;
                c -= b;
                c += ALPHABET_SIZE;
                c *= x;
                c %= ALPHABET_SIZE;
                c += lowerUpper;
                chars[i] = c;
            }
            writeFile(DECRYPT, String.valueOf(chars));
        } else {
            System.out.println("Zły klucz, podaj dwie liczby oddzielone spacją");
        }
    }

    public static void searchKeyAfiniczny() throws Exception {
        String extra = readFile(EXTRA);
        char[] charsE = extra.toCharArray();
        String crypto = readFile(CRYPTO);
        char[] charsC = crypto.toCharArray();
        int[][] results = new int[2][2];
        String key = "";
        int xOpp = 1;

        if (charsE.length >= 2) {

            if (Character.isLowerCase(charsE[0])) {
                results[0][0] = charsE[0] - 'a';
                results[0][1] = charsC[0] - 'a';
            } else if (Character.isUpperCase(charsE[0])) {
                results[0][0] = charsE[0] - 'A';
                results[0][1] = charsC[0] - 'A';
            }

            if (Character.isLowerCase(charsE[1])) {
                results[1][0] = charsE[1] - 'a';
                results[1][1] = charsC[1] - 'a';
            } else if (Character.isUpperCase(charsE[1])) {
                results[1][0] = charsE[1] - 'A';
                results[1][1] = charsC[1] - 'A';
            }

            int x = results[0][0] - results[1][0];
            if (x < 0) {
                x += ALPHABET_SIZE;
            }
            int y = results[0][1] - results[1][1];
            if (y < 0) {
                y += ALPHABET_SIZE;
            }

            xOpp = opposite(x);

            if (xOpp != 0) {
                int a = (y * xOpp) % ALPHABET_SIZE;
                int b = (results[0][1] - ((a * results[0][0]) % ALPHABET_SIZE) + ALPHABET_SIZE) % ALPHABET_SIZE;
                if (a != 0) {
                    key += String.valueOf(a);
                    key += " " + b;
                } else {
                    System.out.println("złe a");
                }
            } else {
                System.out.println("nie da sie znalezc liczby przeciwnej do x w mod26");
            }
            writeFile(KEY_FOUND, key);
            decodeAfiniczny(KEY_FOUND);
        } else {
            System.out.println("za malo danych");
        }
    }

    public static void searchKeyAfinicznyWO() throws Exception {
        String result = "";
        int a, aOpp;
        String text = readFile(CRYPTO);
        List<Integer> list = new ArrayList<>();
        char[] chars = text.toCharArray();
        int count = 0;

        for (int i = 1; i < ALPHABET_SIZE; i++) {
            int opp = opposite(i);
            if (nwd(i, ALPHABET_SIZE) == 1 && (i * opp) % ALPHABET_SIZE == 1) {
                list.add(i);
            }
        }

        for (int j = 0; j < list.size(); j++) {
            a = list.get(j);
            aOpp = opposite(a);
            for (int b = 0; b < ALPHABET_SIZE; b++) {
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];
                    char lowerUpper;

                    if (Character.isLowerCase(c)) {
                        lowerUpper = 'a';
                    } else if (Character.isUpperCase(c)) {
                        lowerUpper = 'A';
                    } else {
                        continue;
                    }

                    c -= lowerUpper;
                    c -= b;
                    c += ALPHABET_SIZE;
                    c *= aOpp;
                    c %= ALPHABET_SIZE;
                    c += lowerUpper;
                    chars[i] = c;
                }
                result += String.valueOf(chars) + "\n";
            }
        }
        writeFile(PLAIN, result);
    }

    public static int opposite(int n) {
        for (int i = 1; i < ALPHABET_SIZE; i++) {
            if ((n * i) % ALPHABET_SIZE == 1) {
                return i;
            }
        }
        return 0;
    }

}

