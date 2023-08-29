package com.kanini.corebanking.custonboard.customeronboarding.common.util;

import org.springframework.stereotype.Component;

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
public class CustomerOnboardingEncryptionUtil {
    private String key = "1234567812345678";
    private String initVector = "1234567812345678";
    private String algo = "AES/CBC/PKCS5PADDING";
}
