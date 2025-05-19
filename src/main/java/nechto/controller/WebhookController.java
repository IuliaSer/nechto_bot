package nechto.controller;

import lombok.AllArgsConstructor;
import nechto.telegram_bot.TelegramBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor
public class WebhookController {
    private final TelegramBot telegramBot;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
//        if (update.getMessage().getDate() < (System.currentTimeMillis() / 1000 - 60)) { //  если callback to nullpointer
//            // Старое сообщение, игнорируем
//            return null;
//        }
        return telegramBot.onWebhookUpdateReceived(update);
    }

//        @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
//        public ResponseEntity<byte[]> generateQr(@RequestParam String text) {
//            try {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//                BitMatrix matrix = new MultiFormatWriter()
//                        .encode(text, BarcodeFormat.QR_CODE, 200, 200);
//
//                MatrixToImageWriter.writeToStream(matrix, "PNG", stream);
//                return ResponseEntity.ok(stream.toByteArray());
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//        }

}
