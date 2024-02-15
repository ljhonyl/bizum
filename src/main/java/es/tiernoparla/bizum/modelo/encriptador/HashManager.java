package es.tiernoparla.bizum.modelo.encriptador;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashManager {
    public String getDigest(String contrasena) {
        try {
            MessageDigest contrasenaCifrada = MessageDigest.getInstance("SHA-256");
            contrasenaCifrada.update(contrasena.getBytes());
            byte[] digest = contrasenaCifrada.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xff & digest[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean compararContrasena(String contrasenaIntroducida, String contrasenaGuardada) {
        return MessageDigest.isEqual(hexStringToByteArray(contrasenaIntroducida), hexStringToByteArray(contrasenaGuardada));
    }

    public static void mostrarResumenHexadecimal(String resumen) {
        System.out.println("Resumen en formato hexadecimal: " + resumen);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }
}
