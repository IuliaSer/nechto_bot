package nechto.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
public class QrCodeGenerator {
    private final BufferedImageGenerator bufferedImageGenerator;
    private final TelegramRestSender telegramRestSender;

    public void generateQrCode(String gameId, String chatId) {
        BufferedImage qrImage;
        try {
            qrImage = bufferedImageGenerator.generateBufferedImage(
                    format("https://t.me/nechto21_bot?start=add_user_to_game_%s", gameId), 250, 250);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(qrImage, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = baos.toByteArray();

        telegramRestSender.sendPhoto(
                Long.parseLong(chatId),
                data,
                "Сканируйте QR и начинайте",
                "qr.png");
    }
}
