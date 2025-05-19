package nechto.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class QrCodeGenerator {
    private final QrCodeImageGenerator qrCodeImageGenerator;

    public SendPhoto generateQrCode(String gameId, String chatId) {
        BufferedImage qrImage;
        try {
            qrImage = qrCodeImageGenerator.generateQRCodeImage(String.format("https://t.me/nechto21_bot?start=add_user_to_game_%s", gameId), 250, 250);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(qrImage, "png", baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        photo.setPhoto(new InputFile(inputStream, "qrcode.png"));
        return photo;
    }
}
