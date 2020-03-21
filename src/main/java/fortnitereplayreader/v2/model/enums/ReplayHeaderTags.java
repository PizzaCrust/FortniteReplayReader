package fortnitereplayreader.v2.model.enums;

public class ReplayHeaderTags {

    public static int None = 0;
    public static int ClientRecorded = (1);
    public static int HasStreamingFixes = (1 << 1);
    public static int DeltaCheckpoints = (1 << 2);
    public static int GameSpecificFrameData = (1 << 3);

}
