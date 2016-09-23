package productizer.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ymetelkin on 9/23/16.
 */
public class InputStreams {
    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream stream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int read;
        int length = 4096;
        byte[] data = new byte[length];

        while ((read = stream.read(data, 0, length)) != -1) {
            buffer.write(data, 0, read);
        }

        buffer.flush();

        return buffer;
    }

    public static byte[] toByteArray(InputStream stream) throws IOException {
        return toByteArrayOutputStream(stream).toByteArray();
    }

    public static String toString(InputStream stream) throws IOException {
        return toByteArrayOutputStream(stream).toString();
    }
}
