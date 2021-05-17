package com.codenamite.majorbot.security.jasypt;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.DefaultEnvironment;
import io.micronaut.context.env.Environment;
import io.micronaut.context.env.MapPropertySource;
import io.micronaut.context.event.BeanInitializedEventListener;
import io.micronaut.context.event.BeanInitializingEvent;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.convert.format.MapFormat;
import io.micronaut.core.naming.conventions.StringConvention;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Context
@Singleton
@Requires(property = "JASYPT_ENCRYPTOR_PASSWORD")
public class JasyptConfigurationPropertiesDecryptor implements BeanInitializedEventListener<Environment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasyptConfigurationPropertiesDecryptor.class);
    private static final String PREFIX = "ENC(";

    public JasyptConfigurationPropertiesDecryptor(DefaultEnvironment environment, @Value("${JASYPT_ENCRYPTOR_PASSWORD}") String encryptedPassword) {
        LOGGER.info("JasyptBootstrapDecryptor started");
        processConfigurationProperties(environment, encryptedPassword);
    }

    private void processConfigurationProperties(DefaultEnvironment environment, String encryptedPassword) {
        StandardPBEStringEncryptor encryptor = encryptor(encryptedPassword);
        Map<String, Object> all = environment.getAllProperties(StringConvention.RAW, MapFormat.MapTransformation.FLAT);
        Map<String, Object> decrypted = all.entrySet().stream()
                .filter(entry -> entry.getValue().toString().startsWith(PREFIX))
                .collect(collector(encryptor));

        environment.addPropertySource(MapPropertySource.of("decrypted", decrypted));
    }

    private StandardPBEStringEncryptor encryptor(String encPassword) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encPassword);
        encryptor.setStringOutputType("base64");
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setKeyObtentionIterations(1000);
        encryptor.setIvGenerator(new RandomIvGenerator());
        return encryptor;
    }

    private String decrypt(Map.Entry<String, Object> entry, StandardPBEStringEncryptor encryptor) {
        LOGGER.info("Decrypt \"{}\" configuration property", entry.getKey());
        String encrypted = entry.getValue().toString().substring(PREFIX.length(), entry.getValue().toString().length() - 1);
        return encryptor.decrypt(encrypted);
    }

    @NonNull
    private Collector<Map.Entry<String, Object>, ?, Map<String, Object>> collector(StandardPBEStringEncryptor encryptor) {
        return Collectors.toMap(Map.Entry::getKey, entry -> decrypt(entry, encryptor));
    }

    @Override
    public Environment onInitialized(BeanInitializingEvent<Environment> event) {
        return event.getBean();
    }
}
