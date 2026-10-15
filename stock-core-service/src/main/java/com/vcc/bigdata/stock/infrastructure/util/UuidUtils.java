package com.vcc.bigdata.stock.infrastructure.util;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;
import java.util.UUID;
@UtilityClass
public class UuidUtils {
    public static final UUID SYSTEM_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public UUID generateUuidV7() {
        return UuidCreator.getTimeOrderedEpoch();
    }

    /** Convert UUID to 16-byte array for binary database columns. */
    public byte[] uuidToBytes(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    /** Convert 16-byte array back to UUID. */
    public UUID bytesToUuid(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
            return null;
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        return new UUID(bb.getLong(), bb.getLong());
    }
}
