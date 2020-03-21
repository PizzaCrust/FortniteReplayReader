package fortnitereplayreader.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import fortnitereplayreader.FortniteReplayReader;
import fortnitereplayreader.v2.UReplayReader;

public class TestV2 {

    public static class TestUReplayReader extends UReplayReader {

        public TestUReplayReader(InputStream stream) {
            super(stream);
        }

    }

    public static void main(String... args) throws Exception {
        TestUReplayReader reader = new TestUReplayReader(new FileInputStream(new File("replay3" +
                ".replay")));
        reader.readReplay();
        System.out.println(reader.replay.meta);
        System.out.println(reader.replay.header);
        /*
        FortniteReplayReader reader2 = new FortniteReplayReader(new FileInputStream(new File(
                "replay3.replay")));
        System.out.println(reader2.getMeta().getFriendlyName());
        */
    }

}
