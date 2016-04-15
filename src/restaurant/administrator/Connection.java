package restaurant.administrator;

import restaurant.Message;

import java.io.*;
import java.net.Socket;

/**
 * Created by Аркадий on 13.03.2016.
 */
public class Connection implements Closeable {
    private final int BUFFER_SIZE = 8192;

    private final Socket socket;
    private final OutputStream os;
    private final InputStream is;
    private final DataInputStream dataIS;
    private final DataOutputStream dataOS;
    private final ObjectOutputStream objectOS;
    private final ObjectInputStream objectIS;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        os = socket.getOutputStream();
        is = socket.getInputStream();
        objectOS = new ObjectOutputStream(os);
        objectIS = new ObjectInputStream(is);
        dataOS = new DataOutputStream(os);
        dataIS = new DataInputStream(is);
    }

    public void send(Message message) throws IOException {
        synchronized(objectOS) {
            objectOS.writeObject(message);
            objectOS.flush();
        }
    }

    public Message receive() throws IOException, ClassNotFoundException {
        synchronized(objectIS) {
            return (Message) objectIS.readObject();
        }
    }

    public void sendImage(String imagePath) throws IOException {
        synchronized(os) {
            try {
                File image = new File(imagePath);
                FileInputStream fileIS = new FileInputStream(image);

                long imageSize = image.length();
                dataOS.writeLong(imageSize);

                byte[] buffer = new byte[(int) imageSize];
                fileIS.read(buffer, 0, (int) imageSize);
                fileIS.close();

                os.write(buffer, 0, (int) imageSize);
                os.flush();
            } catch (FileNotFoundException e) {
                dataOS.writeLong(-1);
            }
        }
    }

    public void downloadImage(String imagePath) throws IOException {
        synchronized(is) {
            long imageSize = dataIS.readLong();
            if(imageSize == -1) return;

            byte[] buffer = new byte[BUFFER_SIZE];
            FileOutputStream fileOS = new FileOutputStream(imagePath);

            int receivedCount;
            while(imageSize > 0) {
                receivedCount = is.read(buffer, 0, (int) Math.min(buffer.length, imageSize));
                fileOS.write(buffer, 0, receivedCount);
                imageSize -= receivedCount;
            }
            fileOS.close();
        }
    }

    @Override
    public void close() throws IOException {
        objectIS.close();
        objectOS.close();
        socket.close();
    }


}
