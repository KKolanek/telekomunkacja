public class Buffer {
    private static Buffer buffer = null;

    private final int BUFFER_SIZE = 1024 * 1024;
    private final byte[] bufferArray = new byte[BUFFER_SIZE];

    private int read_index = 0;
    private int write_index = 0;
    private int data_size = 0;

    private boolean data_ready = false;

    private Buffer() {

    }
    public static Buffer getBufferObject() {
        if (buffer == null) {
            buffer = new Buffer();
        }
        return buffer;
    }

    public synchronized void writeToBuffer(byte[] data) {
        while (data_ready) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (data.length > 0 && data.length < BUFFER_SIZE) {
            for (byte datum : data) {
                bufferArray[write_index] = datum;
                write_index++;
                data_size++;
                if (write_index >= BUFFER_SIZE) {
                    write_index = 0;
                }
            }
            data_ready = true;
            notifyAll();
        }
    }

    public synchronized byte[] readFromBuffer() {
        while (!data_ready) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int size = data_size;
        byte[] tempBuffer = new byte[data_size];
        for (int i = 0; i < size; i++) {
            tempBuffer[i] = bufferArray[read_index];
            read_index++;
            data_size--;
            if (read_index >= BUFFER_SIZE) {
                read_index = 0;
            }
        }

        data_ready = false;
        notifyAll();
        return tempBuffer;
    }
}
