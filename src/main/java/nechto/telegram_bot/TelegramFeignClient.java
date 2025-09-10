package nechto.telegram_bot;

import nechto.config.TelegramFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;

@FeignClient(
        name = "telegram",
        url = "https://api.telegram.org",
        configuration = TelegramFeignConfig.class)
public interface TelegramFeignClient {

    @PostMapping("/bot{token}/sendMessage")
    ApiResponse sendMessage(@PathVariable String token,
                            @RequestBody SendMessage req);

    @PostMapping("/bot8086975968:AAEWRBWduBoPDT7TUJpFlO5s_fOVvkvqSvY/setMyCommands")
    ApiResponse setMyCommands(@RequestBody SetMyCommands req);

}
