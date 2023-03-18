import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Dekodowanie {
        Plik plik = new Plik();
        Kodowanie kodowanie = new Kodowanie();

        //Funkcja odczytująca zakodowaną wiadomość i zwracająca ją jako tablicę bitów
        public int [][] dekodowanie(int blad) throws IOException {
            File file = new File("wiadomosc.txt");
            byte [] wiadomosc;
            int [][] bledy;
            wiadomosc=plik.odczytajPlik(file);
            int [][] wynik = new int[wiadomosc.length][8];
            int [][] wynikKoncowy = new int[(wiadomosc.length+1)/2][16];
            for(int i = 0; i < wiadomosc.length; i++){
                int [] bity = new int[8];
                bity=kodowanie.byteToBit(wiadomosc[i]);
                for(int j=0;j<8;j++){
                    wynik[i][j]=bity[j];
                }
            }

            for(int i = 0; i< (wiadomosc.length+1)/2;i++){
                for(int j=0;j<8;j++){
                    wynikKoncowy[i][j]=wynik[i*2][j];
                }
                if(blad==1&&i==2) {
                    for (int j = 8; j < 16; j++) {
                        wynikKoncowy[i][j] = 0;
                    }
                }
                else{
                    for(int j=8;j<16;j++) {
                        wynikKoncowy[i][j] = wynik[i * 2 + 1][j - 8];
                    }
                }

            }
            return wynikKoncowy;
        }

        //Funkcja ktora zwraca macierz bledu
        public int [][] mnozenie(int blad) throws IOException {
            int [][] wynik = dekodowanie(blad);
            int [][] tab = kodowanie.macierz_H_8_16;
            int [][] mnozenie = new int[8][3];
            for(int i = 0; i<8;i++){
                for(int j=0;j<3;j++){
                    int suma = 0;
                    for(int k=0;k<16;k++){
                        suma+=wynik[j][k]*tab[i][k];
                    }
                    mnozenie[i][j]=suma;
                }
            }
            for(int i = 0;i<8;i++){
                for(int j=0;j<3;j++){
                    mnozenie[i][j]=mnozenie[i][j]%2;
                }
            }
            return mnozenie;
        }

        //Funkcja wykonujaca transpozycje macierzy
        int [][] transpozycja(int [][] macierz){
            int[][]temp=new int[macierz[0].length][macierz.length];
            for (int i = 0; i < macierz.length; i++)
                for (int j = 0; j < macierz[0].length; j++)
                    temp[j][i] = macierz[i][j];
            return temp;
        }

        //Funkcja ktora na podstawie macierzy bledu naprawia blad i zwraca nowa liste bitow
        int [][] dekodowanie2(int blad) throws IOException {
            int [][] wynik = dekodowanie(blad);
            int [][]tab = mnozenie((blad));
            int [][]tab2 = kodowanie.macierz_H_8_16;

            int [][] tabTrans = transpozycja(tab);
            int [][]trans = transpozycja(tab2);

            String linia = new String();
            for(int i=0;i< tabTrans.length;i++){
                linia=toString(tabTrans[i]);
                if(!Objects.equals(linia, "00000000")){
                    for(int j=0;j<8;j++){
                        for(int k=1;k<8;k++){
                            String x=xor(trans[j],trans[k]);
                            if(linia.equals(x)){
                                System.out.println("Zmieniono bity nr. " + j +" oraz " + k);

                                if(wynik[i][j]==0){
                                    wynik[i][j]=1;
                                }
                                else{
                                    wynik[i][j]=0;
                                }
                                if(wynik[i][k]==0){
                                    wynik[i][k]=1;
                                }
                                else{
                                    wynik[i][k]=0;
                                }
                                return wynik;
                            }
                        }
                    }
                }
            }
            return wynik;
        }

        //Funkcja zapisujaca skorygowaną wiadomość do pliku
        void zapis() throws IOException {

            int [][] tab = dekodowanie2(2);
            int [][] wynik=new int[tab.length][8];
            for(int i=0;i<tab.length;i++){
                for(int j=0;j<8;j++){
                    wynik[i][j]=tab[i][j];
                }
            }
            byte[] zapis = Kodowanie.zamianaBajty(wynik);
            File file = new File("wynik.txt");
            FileOutputStream output = new FileOutputStream(file);
            plik.writeFile(output,zapis);
        }

        //Funkcja xor`ująca 2 kolumny tabeli
        String xor(int[] a, int[] b){
            int[] tab = new int[8];
            String wynik=new String();
            for(int i=0;i<8;i++){
                int suma=a[i]+b[i];
                if(suma%2==0){
                    wynik+="0";
                }
                else if(suma==1){
                    wynik+="1";
                }
            }
            return wynik;
        }

        //Funkcja zamieniająca kolumnę na ciąg znaków
        String toString(int [] tab){
            String wynik = new String();
            for(int i=0;i<tab.length;i++){
                wynik+=tab[i];
            }
            return wynik;
        }

}
