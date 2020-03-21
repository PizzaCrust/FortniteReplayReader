package fortnitereplayreader.v2.model;

public abstract class ReplayEvent {

    public ReplayEvent(Info eventInfo) {
        this.eventInfo = eventInfo;
    }

    public static class Info {
        public String id;
        public String group;
        public String metadata;
        public long startTime;
        public long endTime;
        public int sizeInBytes;

        @Override
        public String toString() {
            return "Info{" +
                    "id='" + id + '\'' +
                    ", group='" + group + '\'' +
                    ", metadata='" + metadata + '\'' +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", sizeInBytes=" + sizeInBytes +
                    '}';
        }
    }

    public final Info eventInfo;

}
