package com.captechconsulting.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class HeaderUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderUtil.class);

    private EncryptionUtil encryptionUtil = new EncryptionUtil();

    private static final String HEADER_NAME = "X-Auth-Token";

    private int maxAge = 1000 * 60 * 2; // 2 Minutes
    private String seed = "%DU)FöfI8/°";

    public String getUserName(HttpServletRequest request) {
        String header = request.getHeader(HEADER_NAME);
        return StringUtils.isNotBlank(header) ? extractUserName(header) : null;
    }

    private String extractUserName(String value) {

        try {
            String decryptedValue = encryptionUtil.decrypt(value, seed);
            String[] split = decryptedValue.split("\\|");
            String username = split[0];
            Long timestamp = Long.parseLong(split[1]);
            if (timestamp > System.currentTimeMillis() - maxAge) {
                return username;
            }
        } catch (IOException | GeneralSecurityException e) {
            LOG.debug("Unable to decrypt header", e);
        }
        return null;
    }

    public void addHeader(HttpServletResponse response, String userName) {
        try {
            String encryptedValue = createAuthToken(userName);
            response.setHeader(HEADER_NAME, encryptedValue);
        } catch (IOException | GeneralSecurityException e) {
            LOG.error("Unable to encrypt header", e);
        }
    }

    public String createAuthToken(String userName) throws IOException, GeneralSecurityException {
        String value = userName + "|" + System.currentTimeMillis();
        return encryptionUtil.encrypt(value, seed);
    }

    public void encryptionUtil(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    public void seed(String seed) {
        this.seed = seed;
    }

    public void maxAge(int maxAge) {
        this.maxAge = maxAge;
    }

}
