import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Nagrywanie/odsluch (0/1): ");
        int choice = input.nextInt();
        //Parametry czestotliwosc probkowania, wielkosc bitowa probki, kanaly
        AudioFilePlayer player = new AudioFilePlayer(44100, 16, 1, true, false);

        if (choice == 0) {
            //Ustawianie parametrow nagrywania
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, player.audioFormat);
            //Nagrywanie z buforu karty dzwiekowej
            Record record = new Record(player.audioFormat, targetInfo);
            //Rozpoczecie nagrywania
            record.start();

            try {
                record.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (choice == 1) {
            System.out.println("Start of playing");
            player.play("recording.wav");
        }
    }

}
