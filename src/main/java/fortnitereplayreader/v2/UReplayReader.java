package fortnitereplayreader.v2;

import java.io.InputStream;

import fortnitereplayreader.util.BinaryReader;
import fortnitereplayreader.v2.model.ReplayEvent;
import fortnitereplayreader.v2.model.ReplayHeader;
import fortnitereplayreader.v2.model.ReplayMeta;
import fortnitereplayreader.v2.model.UReplay;
import fortnitereplayreader.v2.model.enums.NetworkVersionHistory;
import fortnitereplayreader.v2.model.enums.ReplayChunkType;
import fortnitereplayreader.v2.model.enums.ReplayVersionHistory;
import me.fungames.oodle.Oodle;

public abstract class UReplayReader {

    protected final BinaryReader reader;
    public final UReplay replay = new UReplay();

    public UReplayReader(InputStream stream) {
        this.reader = new BinaryReader(stream);
    }

    public void readReplay() throws Exception {
        if (reader.readUInt32() != ReplayMeta.VALID_FILE_MAGIC) {
            throw new UnsupportedOperationException("Not a replay stream!!");
        }
        parseMeta();
        parseChunks();
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
                    throw new UnsupportedOperationException("Replay is encrypted but lacks encryption key!");
            }
        }
        replay.meta = meta;
    }

    private void parseChunks() throws Exception {
        while(0 < reader.available()) {
            long chunkType = reader.readUInt32();
            long chunkSize = reader.readUInt32();
            long offset = reader.getPosition();
            //Checkpoint ignored
            if (chunkType == ReplayChunkType.Event) {
                parseEventInfo();
            } else if (chunkType == ReplayChunkType.ReplayData) {
                parseReplayData();
            } else if (chunkType == ReplayChunkType.Header) {
                parseHeaders();
            }
            reader.skip(chunkSize - (reader.getPosition() - offset));
        }
    }

    private void parseHeaders() throws Exception {
        if (reader.readUInt32() != ReplayHeader.VALID_NET_MAGIC) {
            throw new UnsupportedOperationException("Invalid networking?");
        }
        ReplayHeader header = new ReplayHeader();
        header.networkVersion = reader.readUInt32();
        if (header.networkVersion <= NetworkVersionHistory.HISTORY_EXTRA_VERSION) {
            throw new UnsupportedOperationException("Invalid networking?");
        }
        header.networkChecksum = reader.readUInt32();
        header.engineNetworkVersion = reader.readUInt32();
        header.gameNetworkProtocolVersion = reader.readUInt32();
        if (header.networkVersion >= NetworkVersionHistory.HISTORY_HEADER_GUID) {
            header.id = reader.readUUID();
        }
        if (header.networkVersion >= NetworkVersionHistory.HISTORY_SAVE_FULL_ENGINE_VERSION) {
            header.major = reader.readUInt16();
            header.minor = reader.readUInt16();
            header.patch = reader.readUInt16();
            header.changelist = reader.readUInt32();
            header.branch = reader.readFString();
        } else {
            header.changelist = reader.readUInt32();
        }
        if (header.networkVersion <= NetworkVersionHistory.HISTORY_MULTIPLE_LEVELS) {
            throw new UnsupportedOperationException();
        }
        long size = reader.readUInt32();
        reader.skip(size); // ignore level names and times
        if (header.networkVersion >= NetworkVersionHistory.HISTORY_HEADER_FLAGS) {
            header.flags = reader.readUInt32();
        }
        replay.header = header;
    }

    protected abstract void readEvent(ReplayEvent.Info event);

    private void parseEventInfo() throws Exception {
        ReplayEvent.Info info = new ReplayEvent.Info();
        info.id = reader.readFString();
        info.group = reader.readFString();
        info.metadata = reader.readFString();
        info.startTime = reader.readUInt32();
        info.endTime = reader.readUInt32();
        info.sizeInBytes = reader.readInt32();
        readEvent(info);
    }

    protected abstract byte[] decrypt(int size) throws Exception;

    protected byte[] decompress(byte[] bytes, int size) throws Exception {
        return Oodle.INSTANCE.decompress(bytes, size);
    }

    private void parseReplayData() throws Exception {
        if (replay.meta.fileVersion >= ReplayVersionHistory.HISTORY_STREAM_CHUNK_TIMES) {
            long start = reader.readUInt32();
            long end = reader.readUInt32();
        }
        long length = reader.readUInt32();
        int memorySizeInBytes = (int) length;
        if (replay.meta.fileVersion >= ReplayVersionHistory.HISTORY_ENCRYPTION) {
            memorySizeInBytes = reader.readInt32();
        }
        //TODO
        byte[] decompressed = decompress(decrypt((int) length), memorySizeInBytes);
    }
}
