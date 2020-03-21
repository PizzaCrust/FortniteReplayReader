package fortnitereplayreader.v2.model;

import java.util.UUID;

public class ReplayHeader {

    public static long VALID_NET_MAGIC = 0x2CF5A13D;

    public long networkVersion;
    public long networkChecksum;
    public long engineNetworkVersion;
    public long gameNetworkProtocolVersion;

    public String id;

    public int major;
    public int minor;
    public int patch;
    public long changelist;
    public String branch;

    public long flags;

    @Override
    public String toString() {
        return "ReplayHeader{" +
                "networkVersion=" + networkVersion +
                ", networkChecksum=" + networkChecksum +
                ", engineNetworkVersion=" + engineNetworkVersion +
                ", gameNetworkProtocolVersion=" + gameNetworkProtocolVersion +
                ", id=" + id +
                ", major=" + major +
                ", minor=" + minor +
                ", patch=" + patch +
                ", changelist=" + changelist +
                ", branch='" + branch + '\'' +
                ", flags=" + flags +
                '}';
    }
}
