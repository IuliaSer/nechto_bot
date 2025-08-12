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
    private final BufferedImageGenerator bufferedImageGenerator;
    private final TelegramFeignClient telegram;

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

        ByteArrayResource resource = new ByteArrayResource(data) {
            @Override
            public String getFilename() {
                return "qr.png";
            }
            @Override
            public long contentLength() {
                return data.length;
            }
        };

        telegram.sendPhoto(
                chatId,
                resource,
                "Сканируйте QR и начинайте",
                "HTML");
    }

//    public void generateQRCodeImage(String gameId) throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(format("https://t.me/nechto21_bot?start=add_user_to_game_%s", gameId),
//                BarcodeFormat.QR_CODE, 250, 250);
//
//        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//        telegram.sendPhoto( pngOutputStream.toByteArray());
//    }

//    public SendPhoto generateQr(String gameId, String chatId) {
//        BufferedImage qrImage;
//        try {
//            qrImage = bufferedImageGenerator.generateBufferedImage(
//                    format("https://t.me/nechto21_bot?start=add_user_to_game_%s", gameId), 250, 250);
//        } catch (WriterException e) {
//            throw new RuntimeException(e);
//        }
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            ImageIO.write(qrImage, "png", baos);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
//        return new SendPhoto(chatId, new InputFile(inputStream, "qrcode.png"));
//    }
}
