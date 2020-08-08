package top.vergessen.blog.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 配置LocalDateTime格式化方式
 * @author Vergessen
 * @date 2020/7/7 10:57.
 */
@Configuration
public class LocalDateTimeSerializerConfig {

    @Value("${spring.jackson.date-format}")
    private String pattern;

    /**
     * 对 {@LocalDateTime} {@Long} 进行序列化和反序列化的配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return builder -> builder
                // 反序列化
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(formatter))
                // 序列化
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
                .serializerByType(Long.class, ToStringSerializer.instance);
    }
}
