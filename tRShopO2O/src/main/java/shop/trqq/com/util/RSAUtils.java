package shop.trqq.com.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 需要注意的是，在初始化Cipher对象时，goto 54 Line
 * 一定要指明使用"RSA/ECB/PKCS1Padding"格式如Cipher.getInstance("RSA/ECB/PKCS1Padding");
 *
 * @date 2015年10月22日 下午1:44:23
 */
public final class RSAUtils {
    private static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKLCrEWbKaIglL5RO2mztLrVi2"
            + "\r" + "IqAa+yQc1nk8f4hPOLTNMfVEtXvE/A47aYUEiKG9Wo9nRCQhU2qsZSJSefwvtiXG"
            + "\r" + "eBrTcYNmvANW64f4jvmIZAbcUYUI75g5Nv0q7KmFSb/3bsruP48jjmzupa2n2PHG"
            + "\r" + "GQ0nlrQmUE99xkauEQIDAQAB";
    private static final String ALGORITHM = "RSA";

    /**
     * 得到公钥
     *
     * @param algorithm
     * @param bysKey
     * @return
     */
    private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception {
        byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(x509);
    }

    /**
     * 使用公钥加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte plaintext[] = content.getBytes("UTF-8");
            byte[] output = cipher.doFinal(plaintext);

            String s = new String(Base64.encode(output, Base64.DEFAULT));

            return s;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用公钥解密
     *
     * @param content 密文
     * @param key     商户私钥
     * @return 解密后的字符串
     */
    public static String decryptByPublic(String content) {
        try {
            PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pubkey);
            InputStream ins = new ByteArrayInputStream(Base64.decode(content,
                    Base64.DEFAULT));
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;
                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }
                writer.write(cipher.doFinal(block));
            }
            return new String(writer.toByteArray(), "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

}
