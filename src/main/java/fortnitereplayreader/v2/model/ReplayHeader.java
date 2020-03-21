package fortnitereplayreader.v2.model;

import java.util.UUID;

public class ReplayHeader {

    public static long VALID_NET_MAGIC = 0x2CF5A13D;

    public long networkVersion;
    public long networkChecksum;
    public long engineNetworkVersion;
    public long gameNetworkProtocolVersion;

    public UUID id;

    public int major;
    public int minor;
    public int patch;
    public long changelist;
    public String branch;

    public long flags;


}
