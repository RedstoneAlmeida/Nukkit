package cn.nukkit.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;


public abstract class Zlib {
<<<<<<< HEAD
    private static ZlibProvider[] providers;
    private static ZlibProvider provider;

    static {
        providers = new ZlibProvider[3];
        providers[2] = new ZlibThreadLocal();
        provider = providers[2];
    }

    public static void setProvider(int providerIndex) {
        MainLogger.getLogger().info("Selected Zlib Provider: " + providerIndex + " (" + provider.getClass().getCanonicalName() + ")");
        switch (providerIndex) {
            case 0:
                if (providers[providerIndex] == null)
                    providers[providerIndex] = new ZlibOriginal();
                break;
            case 1:
                if (providers[providerIndex] == null)
                    providers[providerIndex] = new ZlibSingleThreadLowMem();
                break;
            case 2:
                if (providers[providerIndex] == null)
                    providers[providerIndex] = new ZlibThreadLocal();
                break;
            default:
                throw new UnsupportedOperationException("Invalid provider: " + providerIndex);
        }
        if (providerIndex != 2) {
            MainLogger.getLogger().warning(" - This Zlib will negatively affect performance");
        }
        provider = providers[providerIndex];
    }

    public static byte[] deflate(byte[] data) throws Exception {
        return deflate(data, Deflater.DEFAULT_COMPRESSION);
    }

    public static byte[] deflate(byte[] data, int level) throws Exception {
        return provider.deflate(data, level);
    }

    public static byte[] deflate(byte[][] data, int level) throws Exception {
        return provider.deflate(data, level);
    }

    public static byte[] inflate(InputStream stream) throws IOException {
        return provider.inflate(stream);
    }

    public static byte[] inflate(byte[] data) throws IOException {
        return inflate(new ByteArrayInputStream(data));
=======

    public static byte[] deflate(byte[] data) throws Exception {
        return deflate(data, Deflater.DEFAULT_COMPRESSION);
    }

    public static byte[] deflate(byte[] data, int level) throws Exception {
        Deflater deflater = new Deflater(level);
        deflater.reset();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        byte[] buf = new byte[1024];
        try {
            while (!deflater.finished()) {
                int i = deflater.deflate(buf);
                bos.write(buf, 0, i);
            }
        } finally {
            deflater.end();
        }
        return bos.toByteArray();
    }

    public static byte[] inflate(InputStream stream) throws IOException {
        InflaterInputStream inputStream = new InflaterInputStream(stream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        try {
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            buffer = outputStream.toByteArray();
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }

        return buffer;
    }

    public static byte[] inflate(byte[] data) throws IOException {
        return inflate(new ByteArrayInputStream(data));
    }

    public static byte[] inflate(byte[] data, int maxSize) throws IOException {
        return inflate(new ByteArrayInputStream(data, 0, maxSize));
>>>>>>> parent of 29d35eb... Revert "Fix for issue #1817 -- main thread stuck (rollback Zlib)" (#1851)
    }

    public static byte[] inflate(byte[] data, int maxSize) throws IOException {
        return inflate(new ByteArrayInputStream(data, 0, maxSize));
    }
}
