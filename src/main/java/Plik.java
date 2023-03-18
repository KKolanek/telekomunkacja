import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Plik {
//    public void zapis_do_pliku(File fileToSave, byte[] dane) {
//        try {
//            FileOutputStream fos = new FileOutputStream(fileToSave);
//            fos.write(dane);
//            fos.close();
//            System.out.println("Zapisano pomyślnie!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public byte[] odczyt_z_pliku(File loadFile) {
//        byte [] dane = new byte[0];
//        try {
//                FileInputStream fis = new FileInputStream(loadFile);
//                int size = fis.available();
//                dane = new byte [size];
//                int bytes_read = fis.read(dane);
//                if(bytes_read == 0) {
//                    System.out.println("Plik jest pusty!");
//                }
//                else {
//                    System.out.println("Odczytano pomyślnie!");
//                }
//                fis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        return dane;
//    }
public byte[] odczytajPlik(File file) throws IOException {
    FileInputStream plik = new FileInputStream(file);
    int l = (int) file.length();
    byte[] tablica = new byte[l];
    plik.read(tablica);
    plik.close();
    return tablica;
}

    public String zamienNaTekst(byte[] bity){
        String tekst = new String(bity);
        return tekst;
    }
    public void zapiszDoPliku(File file, String wynik) throws IOException{
        FileOutputStream plik = new FileOutputStream(file);
        byte[] str = wynik.getBytes(StandardCharsets.UTF_8);
        plik.write(str);
        plik.close();
    }
    public void writeFile(FileOutputStream pathfile, byte[] cipher) throws IOException {
        pathfile.write(cipher);
    }
}
