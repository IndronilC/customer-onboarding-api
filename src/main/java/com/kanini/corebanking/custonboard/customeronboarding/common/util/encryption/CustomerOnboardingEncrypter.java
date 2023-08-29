package com.kanini.corebanking.custonboard.customeronboarding.common.util.encryption;

import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>This class which is <code>CustomerOnboardingEncrypter</code>
 * acts as the implementer of {@link jakarta.persistence.AttributeConverter} interface
 * which provides two abstract methods <ul>one - to encrypt data while writing to database columns</ul>
 * <ul>other - to decrypt the data back to its original form while moving the data back to entity object</ul>
 * <em>currently we are implementing the same by using
 * {@link com.kanini.corebanking.custonboard.customeronboarding.common.util.encryption.CustomerOnboardingEncryptionUtil}</em>
 * <br/> This also ensures a level of abstraction which can change if we change the encryption implementer where in the calling
 * code remains completely <em>de-coupled</em> from the calling Entities or classes
 * </p>
 * @author - Indronil Chawkroborty
 * @since - Sprint - 8
 */

public class CustomerOnboardingEncrypter implements AttributeConverter<String, String> {
    @Autowired
    CustomerOnboardingEncryptionUtil encryptionUtil;

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptionUtil.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptionUtil.decrypt(s);
    }
}

