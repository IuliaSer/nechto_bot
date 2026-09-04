package nechto.cache;

import lombok.Getter;
import nechto.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class TableQrCodeCache {
    private final Map<Long, byte[]> map = new ConcurrentHashMap<>();

    public void saveTableQrCode(long adminId, byte[] qrCode) {
        map.put(adminId, qrCode);
    }

    public byte[] get(long adminId) {
        byte[] qrCode = map.get(adminId);
        if (qrCode == null) {
            throw new EntityNotFoundException("Qrcode не найден. Пожалуйста сначала создайте стол командой create_table");
        }
        return qrCode;
    }
}
