package fortnitereplayreader.v2.model;

import java.util.ArrayList;
import java.util.List;

public class UReplay {

    public ReplayMeta meta;
    public ReplayHeader header;
    public List<ReplayEvent> events = new ArrayList<>();

}
