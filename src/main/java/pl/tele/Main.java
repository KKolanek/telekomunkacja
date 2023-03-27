package pl.tele;

import com.diogonunes.jcolor.AnsiFormat;
import java.io.*;
import java.util.*;
import static com.diogonunes.jcolor.Attribute.*;

public class Main {

    public static void main(String[] args) throws IOException {


        AnsiFormat fInfo = new AnsiFormat(BLUE_TEXT());

        List<Integer> message;
        List<Integer> coded;
        List<Integer> messageWithError;

        FileReader readSendFile = new FileReader("wiadomoscWyslana.txt");
        FileWriter writeCodedFile = new FileWriter("wiadomoscZakodowana.txt");
        FileWriter writeBinaryFile = new FileWriter("wiadomoscBinarnie.txt");

        BufferedReader brSendFile = new BufferedReader(readSendFile);

        //wczytywanie wiadomosci tekstowej do zmiennej text
        StringBuilder text = new StringBuilder();
        String line;
        while((line = brSendFile.readLine()) != null) {
            text.append(line);
        }
        int size = text.length();

        //zamiana na system binarny
        message = Manager.charToInt(text.toString());
        System.out.println("Wiadomosc binarnie: " + text);

        for (int i = 0; i < message.size(); i++) {
            System.out.print(message.get(i));
            writeBinaryFile.write(message.get(i).toString());
            if((i + 1) % 8 == 0) {
                System.out.print(" ");
            }
        }
        System.out.println(fInfo.format("Bity: " + message.size()));

        //Dodawanie bitow parzystosci
        coded = Manager.coding(message);
        int x = 0;

        System.out.println("Zakodowana wiadomosc, " + fInfo.format("Niebieskie")
                + " bity to bity parzystosci");

        //zapisanie wszystkich zakodowanych bitow do pliku 'wiadomosc zakodowana'
        for(int i = 0; i < coded.size(); i++) {
            writeCodedFile.write(coded.get(i).toString());

            if(x % 2 != 0) {
                System.out.print(fInfo.format(String.valueOf(coded.get(i))));
            } else {
                System.out.print(coded.get(i));
            }
            if((i + 1) % 8 == 0) {
                System.out.print(" ");
                x++;
            }
        }

        System.out.println("Bity: " + fInfo.format(String.valueOf(coded.size())));

        brSendFile.close();
        writeCodedFile.close();
        writeBinaryFile.close();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Mozna teraz wprowadzic zmiany w zakodowanym pliku, wcisnij enter po zmianach...");
        scanner.nextLine();

        //Wczytywanie zakodowanego pliku, gdzie mogla nastapic jakas zmiana
        FileReader readCodedFile = new FileReader("wiadomoscZakodowana.txt");
        BufferedReader brCodedFile = new BufferedReader(readCodedFile);
        StringBuilder text2 = new StringBuilder();
        if ((line = brCodedFile.readLine()) != null) {
            text2.append(line);
        }

        brCodedFile.close();

        //zapisanie stanu po zmianie do nowej listy
        messageWithError = Manager.stringToInt(text2.toString());

        System.out.println("Po zmianie: ");
        for(int i = 0; i < messageWithError.size(); i++) {
            System.out.print(messageWithError.get(i));
            if((i+1) % 8 == 0) {
                System.out.print(" ");
            }
        }

        System.out.println(fInfo.format(" Bity: " + messageWithError.size()));

        //kazdy 16 bitowy ciag (1 litera) jest zapisywany do osobnej listy
        List<List<Integer>> listOfWords = new ArrayList<>();

        //ilosc znakow czyli ilosc 16-bitowych wektorow
        int signsNumber = 0;
        StringBuilder response = new StringBuilder();
        List<Integer> everyWord = new ArrayList<>();
        System.out.println("rozmiar złej wiadmości: " + messageWithError.size());

        //podzial calej niepoprawnej wiadomosci na mniejsze 16-bitowe wektory
        for(int i = 0; i < messageWithError.size(); i++) {
            everyWord.add(messageWithError.get(i));
            if((i+1) % 16 == 0) {
                listOfWords.add(signsNumber, List.copyOf(everyWord));
                signsNumber++;
                everyWord.clear();
            }
        }
        messageWithError.clear(); 

        FileWriter saveChangedMessage = new FileWriter("wiadomoscOdebrana.txt");

        //weryfikacja i poprawianie kazdego wektora
        //ktory reprezentuje jeden znak
        // scalanie 16-bitowych wektorow w jeden i zapis do pliku
        for(int i = 0; i < size; i++) {
            listOfWords.set(i, Manager.verification(listOfWords.get(i), coded.size() / size));
            messageWithError.addAll(listOfWords.get(i));
            //zamiana kazdego ze zweryfikowanych 16-bitowych wektorow na pojedyncze znaki
            //i zapisanie ich do stringa
            response.append(Manager.getChars(listOfWords.get(i)));
            saveChangedMessage.write(Manager.getChars(listOfWords.get(i)));
        }


        saveChangedMessage.close();

        System.out.println("Wiadomość poprawiona: ");
        for(int i = 0; i < messageWithError.size(); i++) {
            System.out.print(messageWithError.get(i));
            if((i+1) % 8 == 0) {
                System.out.print(" ");
            }
        }
        System.out.println(fInfo.format("Bity: " + messageWithError.size()));
        System.out.println("Wiadomość odebrana");
    }



}
