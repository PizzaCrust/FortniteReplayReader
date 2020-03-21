package fortnitereplayreader.v2.model;

public class ReplayMeta {

    public static long VALID_FILE_MAGIC = 0x1CA2E27F;

    public long fileVersion;
    public long lengthInMs;
    public long networkVersion;
    public long changeList;
    public String friendlyName;
    public boolean isLive;

    public long timestamp;

    public boolean isCompressed;

    public boolean isEncrypted;
    public byte[] encryptionKey;

    @Override
    public String toString() {
        return "ReplayMeta{" +
                "fileVersion=" + fileVersion +
                ", lengthInMs=" + lengthInMs +
                ", networkVersion=" + networkVersion +
                ", changeList=" + changeList +
                ", friendlyName='" + friendlyName + '\'' +
                ", isLive=" + isLive +
                ", timestamp=" + timestamp +
                ", isCompressed=" + isCompressed +
                ", isEncrypted=" + isEncrypted +
                '}';
    }
}
