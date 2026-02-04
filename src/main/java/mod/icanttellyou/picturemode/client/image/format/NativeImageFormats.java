package mod.icanttellyou.picturemode.client.image.format;

import java.util.HashSet;
import java.util.Set;

public class NativeImageFormats {
    public static final NativeImageFormat PNG_FORMAT = new PNGFormat();
    public static final NativeImageFormat JPG_FORMAT = new JPGFormat();
    public static final NativeImageFormat TGA_FORMAT = new TGAFormat();
    public static final NativeImageFormat BMP_FORMAT = new BMPFormat();

    public static final Set<NativeImageFormat> FORMATS = new HashSet<>();

    static {
        FORMATS.add(PNG_FORMAT);
        FORMATS.add(JPG_FORMAT);
        FORMATS.add(TGA_FORMAT);
        FORMATS.add(BMP_FORMAT);
    }

    /**
     * Gets a {@link NativeImageFormat} for the given file type
     *
     * @param fileType The file type to get the format for.
     * @return The {@link NativeImageFormat} for the given file type
     */
    public static NativeImageFormat getFormat(String fileType) {
        NativeImageFormat format = FORMATS.stream()
            .filter(f -> f.getFormatName().equals(fileType))
            .findFirst()
            .orElse(null);

        if (format == null)
            throw new IllegalArgumentException(fileType + " is not a valid format!");

        return format;
    }
}
