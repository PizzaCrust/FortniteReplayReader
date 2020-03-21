package fortnitereplayreader.v2.fn;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import fortnitereplayreader.util.BinaryReader;
import fortnitereplayreader.v2.UReplayReader;
import fortnitereplayreader.v2.fn.model.Elimination;
import fortnitereplayreader.v2.model.ReplayEvent;
import fortnitereplayreader.v2.model.enums.EngineNetworkVersionHistory;


public class FortniteReplayReader extends UReplayReader {

    public List<Elimination> eliminations = new ArrayList<>();

    public FortniteReplayReader(InputStream stream) {
        super(stream);
    }

    private String parsePlayer(BinaryReader dReader) throws Exception {
        byte botIndicator = dReader.readBytes(1)[0];
        if (botIndicator == 0x03) {
            return "Bot";
        } else if (botIndicator == 0x10) {
            return dReader.readFString();
        }
        byte size = dReader.readBytes(1)[0];
        return dReader.readGUID(size);
    }

    public String getReleaseVersion() {
        return this.replay.header.branch.split("-")[1];
    }

    public int getMajorRelease() {
        return Integer.parseInt(getReleaseVersion().split("\\.")[0]);
    }

    public int getMinorRelease() {
        return Integer.parseInt(getReleaseVersion().split("\\.")[1]);
    }

    private Elimination parseElimination(BinaryReader dReader, long time) throws Exception {
        Elimination elimination = new Elimination();
        if (replay.header.engineNetworkVersion >= EngineNetworkVersionHistory.HISTORY_FAST_ARRAY_DELTA_STRUCT
                && getMajorRelease() >= 9) {
            dReader.skip(85);
            elimination.victimId = parsePlayer(dReader);
            elimination.killerId = parsePlayer(dReader);
        } else {
            if (getMajorRelease() <= 4 && getMinorRelease() < 2) {
                dReader.skip(12);
            } else if (getMajorRelease() == 4 && getMinorRelease() <= 2) {
                dReader.skip(40);
            } else {
                dReader.skip(45);
            }
            elimination.victimId = dReader.readFString();
            elimination.killerId = dReader.readFString();
        }
        elimination.gunType = dReader.readBytes(1)[0];
        elimination.knocked = dReader.readUInt32AsBoolean();
        elimination.time = time;
        return elimination;
    }

    @Override
    protected void readEvent(ReplayEvent.Info event) throws Exception {
        byte[] decrypted = decrypt(event.sizeInBytes);
        BinaryReader decryptedReader = new BinaryReader(new ByteArrayInputStream(decrypted));
        if (event.group.equals(ReplayEventTypes.PLAYER_ELIMINATION)) {
            eliminations.add(parseElimination(decryptedReader, event.startTime));
        } else if (event.group.equals(ReplayEventTypes.MATCH_STATS)) {
            //TODO
        } else if (event.group.equals(ReplayEventTypes.TEAM_STATS)) {
            //TODO
        } else {
            System.out.println("Unknown or unsupported event detected. " + event.group);
        }
    }

    @Override
    protected byte[] decrypt(int size) throws Exception {
        if (!replay.meta.isEncrypted) {
            return reader.readBytes(size);
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(replay.meta.encryptionKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(reader.readBytes(size));
    }
}
