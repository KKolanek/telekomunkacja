import javax.sound.sampled.*;
import java.io.File;
import java.util.Scanner;

public class Record extends Thread {

    private final AudioFormat format;
    private final DataLine.Info targetInfo;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    File audioFile = new File("recording.wav");

    boolean exit = true;

    public Record(AudioFormat format, DataLine.Info targetInfo) {
        this.format = format;
        this.targetInfo = targetInfo;
    }

    public void run() {
        Buffer buffer;
        TargetDataLine targetDataLine = null;

        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(format);
            targetDataLine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int dataRead;
        assert targetDataLine != null;
        byte[] targetData = new byte[targetDataLine.getBufferSize() / 5];

        Thread userInputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("") || input.equals("e")) {
                exit = false;
            }
        });

        //Reading from mic and saving it to the buffer
        while (exit) {
            try {
                dataRead = targetDataLine.read(targetData, 0, targetData.length);
                AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);

                if (dataRead == -1) {
                    System.out.println("Failed to record");
                } else {
                    buffer = Buffer.getBufferObject();
                    buffer.writeToBuffer(targetData);
                }
            } catch (Exception e) {
                exit = false;
                e.printStackTrace();
            }
        }
    }

}
