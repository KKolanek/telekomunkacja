import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kodowanie {
    int l;
    Plik plik = new Plik();
    int [][] wynik;
    int [][] macierz_H_4_12={
            {0, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0},
            {1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0},
            {1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0},
            {1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1}};

    int [][] macierz_H_8_16 = {
            {1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1}};

    public int[][] koduj(int blad) throws FileNotFoundException {
        File file = new File("tekst.txt");
        l = (int) file.length();
        byte[] tablica = new byte[0];
        {
            try {
                tablica = plik.odczytajPlik(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(blad==1){
            wynik= new int[tablica.length][12];
        }
        else {
            wynik = new int[tablica.length][16];
        }
        for(int i =0; i<tablica.length;i++){
            int [] bity = new int[8];
            bity=byteToBit(tablica[i]);
            for(int j=0;j<8;j++){
                wynik[i][j]=bity[j];
            }
            for(int k=8;k<wynik[i].length;k++){
                wynik[i][k]=0;
            }
        }
        return wynik;
    }

    //Funkcja zamieniająca bajt na tablicę bitów
    public int[] byteToBit (int one) {
        if(one<0){
            one=256+one;
        }
        int[] bits = new int[8];
        int i = bits.length - 1;
        while (one != 0) {
            bits[i] = one % 2;
            one /= 2;
            i--;
        }
        return bits;
    }

    //Funkcja ktora zwraca nam macierz zakodowaną
    public int [][] mnozenieMacierzy(int [][] macierz, int blad){
        int pom;
        int pom2;
        int [][] tab;
        if(blad==1) {pom=12;pom2=4;tab=macierz_H_4_12;}
        else {pom=16;pom2=8;tab=macierz_H_8_16;}
        int [][] mnozenie = new int[pom2][3];
        for(int i = 0;i<pom2;i++){
            for(int j = 0;j<l;j++){
                int suma = 0;
                for(int k = 0;k<pom;k++){
                    suma+=macierz[j][k] * tab[i][k];
                }
                mnozenie[i][j]=suma;
            }
        }
        for(int i = 0;i<pom2;i++){
            for(int j = 0;j<l;j++){
                mnozenie[i][j]=mnozenie[i][j]%2;
            }
        }
        return mnozenie;
    }

    //funkcja zapisujaca zakodowaną wiadomość do pliku
    public int[][] kodowanie(int blad) throws IOException {
        File file = new File("wiadomosc.txt");
        int pom;
        int [][] tablica=koduj(blad);
        int [][] mnozenie=mnozenieMacierzy(tablica,blad);
        if(blad==1){
            pom=12;
        }
        else{
            pom=16;
        }
        for(int i = 0; i < l; i++){
            for(int j = 8; j < pom;j++){
                tablica[i][j]=mnozenie[j-8][i];
            }
        }
        byte[] wynik = zamianaBajty(tablica);
        System.out.println("Zakodowano plik jako : ");
        for(int i = 0; i < tablica.length; i++){
            System.out.println(Arrays.toString(tablica[i]));
        }
        FileOutputStream output = new FileOutputStream(file);
        plik.writeFile(output,wynik);
        return tablica;
    }

    //Funkcja zamieniająca tablicę bitów na bajty
    public static byte[] zamianaBajty(int[][] tab){
        int [] wymiar = new int[tab.length*tab[0].length];
        for(int i = 0;i<tab.length;i++){
            for(int j = 0;j<tab[0].length;j++){
                wymiar[i*tab[0].length+j]=tab[i][j];
            }
        }
        List<Byte> lista = new ArrayList<Byte>();
        for(int i=0;i<wymiar.length;i+=8){
            byte bajt=0;
            for(int j=0;j<8;j++){
                if(i+j<wymiar.length)
                    bajt=(byte) ((bajt<<1)|wymiar[i+j]);
            }
            lista.add(bajt);
        }
        byte [] wynik=new byte[lista.size()];
        for(int i = 0; i< lista.size();i++){
            wynik[i]=lista.get(i);
        }
        return wynik;
    }
}
