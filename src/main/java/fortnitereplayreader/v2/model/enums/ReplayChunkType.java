package fortnitereplayreader.v2.model.enums;

public class ReplayChunkType {

    public static long Header = 0;
    public static long ReplayData = 1;
    public static long Checkpoint = 2;
    public static long Event = 3;
    public static long Unknown = 0xFFFFFFFF;
}
