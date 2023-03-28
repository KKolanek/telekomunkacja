package pl.tele;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    public static int row = 8;
    public static int col = 16;

    public static int[][] H = {
            {1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0},
            {1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1}};


    //wczytywanie z postaci zero-jedynkowej
    public static List<Integer> stringToInt(String text) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '1') {
                result.add(1);
            } else {
                result.add(0);
            }
        }
        return result;
    }

    //zamiana ASCII na kod binarny czyli zamiana zwyklego tekstu
    public static List<Integer> charToInt(String text) {
        List<Integer> toInt = new ArrayList<>();
        int sign;

        for (int i = 0; i < text.length(); i++) {
            sign = text.charAt(i);
            for (int j = 0; j < 8; j++) {
                toInt.add((sign % 2));
                sign /= 2;
            }
        }
        return toInt;
    }


    //zamiana odwrotna czyli z kodu binarnego na ASCII
    public static char getChars(List<Integer> list) {
        char sign = 0;
        for (int i = 0; i < 8; i++) {
            sign += list.get(i) * Math.pow(2, i);
        }
        return sign;
    }

    public static int parityBit(List<Integer> text, int rowNum) {
        int c = 0;
        for (int i = 0; i < text.size(); i++) {
            c += H[rowNum][i] * text.get(i);
        }
        c %= 2;
        return c;
    }

    public static void addParityBit(List<Integer> text) {
        for (int i = 0; i < row; i++) {
            text.add(parityBit(text, i));
        }
    }

    public static List<Integer> coding(List<Integer> text) {
        List<Integer> code = new ArrayList<>();
        List<Integer> bits = new ArrayList<>();

        for (int i = 0; i < text.size(); i++) {
            code.add(text.get(i));
            bits.add(text.get(i));

            //wstawianie po kazdym znaku osmiu bitow parzystosci
            if ((i + 1) % 8 == 0) {
                addParityBit(bits);
                for (int j = 0; j < row; j++) {
                    code.add(bits.get(j + 8));
                }
                bits.clear();
            }
        }
        return code;
    }

    public static List<Integer> correction(List<Integer> incorrectMessage, List<Integer> E) {
        List<Integer> toChange = new ArrayList<>(List.copyOf(incorrectMessage));
        //musimy sprawdzic ktora z kolumn jest rowna wektorowi bledow E
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (H[j][i] != E.get(j)) {
                    break;
                }
                if (j == 7) {
                    //zamiana bitu
                    int ch = toChange.get(i);
                    ch ^= 1;
                    toChange.set(i, ch);
                }
            }
        }

        //poprawa dla bledu 2 bitowego
        for (int i = 0; i < col; i++) {
            for (int j = i; j < col; j++) {
                for (int k = 0; k < row; k++) {
                    //sprawdzamy czy suma dwoch kolumn modulo 2 nie jest rowna wartosci wektora bledu
                    if ((H[k][i] ^ H[k][j]) != E.get(k)) {
                        break;
                    }
                    if (k == 7) {
                        //zamiana itowego i jotowego bitu (XOR)
                        int ch1 = toChange.get(i);
                        ch1 ^= 1;
                        int ch2 = toChange.get(j);
                        ch2 ^= 1;
                        toChange.set(i, ch1);
                        toChange.set(j, ch2);
                    }
                }
            }
        }
        return toChange;
    }

    /* iloczyn slowa kodowego i macierzy musi byc rowny 0
       T - wektor wiadomosci nadawanych (bity informacji i bity parzystosci)
       E - wektor bledu (zawiera liczbe 1 w miejsciu gdzie wystepuje blad
       R - wektor wiadomosci odebranych R = T + E

     */
    public static List<Integer> verification(List<Integer> text, int length) {
        boolean verified = true;
        List<Integer> toVerify = new ArrayList<>(List.copyOf(text));

        if (text.size() != length) {
            System.out.println("Niezgodna ilosc bitow");
        }

        List<Integer> E = new ArrayList<>();
        int number;

        //jesli w wetorze bledu wystapi wartosc 1 to musimy skorygowac wiadomosc
        for (int i = 0; i < row; i++) {
            number = parityBit(text, i);
            E.add(number);
            if (number == 1) {
                verified = false;
            }
        }

        if (verified) {
            System.out.println("VERIFIED");
        } else {
            toVerify = correction(toVerify, E);
        }
        return toVerify;
    }


}
