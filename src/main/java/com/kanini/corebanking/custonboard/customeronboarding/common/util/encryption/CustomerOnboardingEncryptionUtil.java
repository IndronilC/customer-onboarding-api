package com.kanini.corebanking.custonboard.customeronboarding.common.util.encryption;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * <p>This Class <code>CustomerOnboardingEncryptionUtil</code> will act
 * as the common utility in the customer-onboarding module which will
 * foster data encryption while the data gets saved by the different
 * repositories that are available for storing Customer Personal Data
 * </p>
 * <p>It will currently follow the <em>AES</em> based <em>128 bit</em>
 *  java encryption technique which is provided by
 *  @see {https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html}
 * </p>
 * <p> This class will provide the global <em>encrypt</em> and <em>decrypt</em>
 *  method which will be acting as the <em>Attribute Converter</em> for all the
 *  entity data which stores Customer private information while writing them to
 *  the database
 *  </p>
 * @author - Indronil Chawkroborty
 * @since - Sprint 8
 *
 */
@Component
@Slf4j
public class CustomerOnboardingEncryptionUtil {

    protected CustomerOnboardingEncryptionUtil() {}
    private static String key = "1234567812345678";
    private static String initVector = "1234567812345678";
    private static String algo = "AES/CBC/PKCS5PADDING";

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception exception) {
           log.error("Exception {} encountered while encrypting data ", exception);
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception exception) {
            log.error("Exception {} encountered while encrypting data ", exception);
        }
        return null;
    }



}
