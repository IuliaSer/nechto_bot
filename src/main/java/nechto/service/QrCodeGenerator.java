package nechto.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import nechto.telegram_bot.TelegramFeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.lang.String.format;

@RequiredArgsConstructor
@Component
public class QrCodeGenerator {
    private final QrCodeImageGenerator qrCodeImageGenerator;
    private final TelegramFeignClient telegram;

    public void generateQrCode(String gameId, String chatId) {
        BufferedImage qrImage;
        try {
            qrImage = qrCodeImageGenerator.generateQRCodeImage(
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

        ByteArrayResource resource = new ByteArrayResource(data) {
            @Override public String getFilename() {
                return "qr.png";
            }
            @Override public long contentLength() {
                return data.length;
            }
        };
        telegram.sendPhoto(
                "8086975968:AAEWRBWduBoPDT7TUJpFlO5s_fOVvkvqSvY",
                chatId,
                resource,
                "Сканируйте QR и начинайте",
                "HTML");
    }
}
