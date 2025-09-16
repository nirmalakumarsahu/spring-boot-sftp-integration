package com.sahu.springboot.basics.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    public final String PEM_KEY_BEGIN = "-----BEGIN RSA PRIVATE KEY-----";
    public final String PEM_KEY_END = "-----END RSA PRIVATE KEY-----";
    public final String PPK_KEY_BEGIN = "PuTTY-User-Key-File";
    public final String PPK_KEY_PUBLIC_LINES = "Public-Lines";
    public final String PPK_KEY_PRIVATE_LINES = "Private-Lines";
    public final String PPK_KEY_PRIVATE_MAC = "Private-MAC";

    public final String PROPERTY_PRIVATE_KEY = "privateKey";

}
