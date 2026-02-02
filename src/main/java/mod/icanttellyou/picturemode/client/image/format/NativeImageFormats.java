package mod.icanttellyou.picturemode.client.image.format;

import java.util.HashMap;
import java.util.Map;

public class NativeImageFormats {
    public static final Map<String, NativeImageFormat> FORMATS = new HashMap<>();

    static {
        FORMATS.put("png", new PNGFormat());
        FORMATS.put("jpg", new JPGFormat());
        FORMATS.put("tga", new TGAFormat());
        FORMATS.put("bmp", new BMPFormat());
    }

    /**
     * Gets a {@link NativeImageFormat} for the given file type
     *
     * @param fileType The file type to get the format for.
     * @return The {@link NativeImageFormat} for the given file type
     */
    public static NativeImageFormat getFormat(String fileType) {
        NativeImageFormat format = FORMATS.get(fileType);

        if (format == null)
            throw new IllegalArgumentException(fileType + " is not a valid format!");

        return format;
    }
}
