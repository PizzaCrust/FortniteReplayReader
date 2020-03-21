package fortnitereplayreader.v2;

import java.io.InputStream;

import fortnitereplayreader.util.BinaryReader;
import fortnitereplayreader.v2.model.ReplayMeta;
import fortnitereplayreader.v2.model.UReplay;
import fortnitereplayreader.v2.model.enums.ReplayVersionHistory;

public abstract class UReplayReader {

    private BinaryReader reader;
    public UReplay replay = new UReplay();

    public UReplayReader(InputStream stream) {
        this.reader = new BinaryReader(stream);
    }

    public void readReplay() throws Exception {
        if (reader.readUInt32() != ReplayMeta.VALID_FILE_MAGIC) {
            throw new UnsupportedOperationException("Not a replay stream!!");
        }
        parseMeta();
    }

    private void parseMeta() throws Exception {
        ReplayMeta meta = new ReplayMeta();
        meta.fileVersion = reader.readUInt32();
        meta.lengthInMs = reader.readUInt32();
        meta.networkVersion = reader.readUInt32();
        meta.changeList = reader.readUInt32();
        meta.friendlyName = reader.readFString();
        meta.isLive = reader.readUInt32AsBoolean();
        if (meta.fileVersion >= ReplayVersionHistory.HISTORY_RECORDED_TIMESTAMP) {
            meta.timestamp = reader.readInt64();
        }
        if (meta.fileVersion >= ReplayVersionHistory.HISTORY_COMPRESSION) {
            meta.isCompressed = reader.readUInt32AsBoolean();
        }
        if (meta.fileVersion >= ReplayVersionHistory.HISTORY_ENCRYPTION) {
            meta.isEncrypted = reader.readUInt32AsBoolean();
            if (meta.isEncrypted) {
                long size = reader.readUInt32();
                meta.encryptionKey = reader.readBytes((int) (size));
                if (meta.encryptionKey.length == 0)
                    System.out.println("Replay is encrypted but lacks encryption key!");
            }
        }
        replay.meta = meta;
    }

    private void parseHeaders() throws Exception {}
}
