package nechto.config;

import feign.Client;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * ➊ OkHttp-клиент вместо Apache HC
 * ➋ SpringFormEncoder для multipart
 * ➌ Интерцептор, вырезающий ";charset=UTF-8" из Content-Type.
 */
@Configuration
public class TelegramFeignConfig {

    /** ➊ OkHttp — не портит multipart-байты */
    @Bean
    public Client feignClient() {
        return new OkHttpClient();
    }

    /** ➋ Стандартный SpringFormEncoder */
    @Bean
    public Encoder feignEncoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }

    /** ➌ RequestInterceptor удаляет лишний параметр charset */
    @Bean
    public RequestInterceptor stripCharsetInterceptor() {
        return template -> {
            Collection<String> cts = template.headers().get("Content-Type");
            if (cts == null) return;

            template.header(
                    "Content-Type",
                    cts.stream()
                            .map(ct -> ct.replaceAll(";\\s*charset=[^;]+", "")) // "image/png" вместо "image/png; charset=UTF-8"
                            .collect(Collectors.toList())
                            .toArray(String[]::new)
            );
        };
    }
}
