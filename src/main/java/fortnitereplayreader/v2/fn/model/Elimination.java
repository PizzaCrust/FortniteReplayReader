package fortnitereplayreader.v2.fn.model;

public class Elimination {

    public String killerId;
    public String victimId;
    public byte gunType;
    public long time;
    public boolean knocked;

    @Override
    public String toString() {
        return "Elimination{" +
                "killerId='" + killerId + '\'' +
                ", victimId='" + victimId + '\'' +
                ", gunType=" + gunType +
                ", time=" + time +
                ", knocked=" + knocked +
                '}';
    }
}
