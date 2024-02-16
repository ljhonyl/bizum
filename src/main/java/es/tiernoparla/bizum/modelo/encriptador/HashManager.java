package es.tiernoparla.bizum.modelo.encriptador;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashManager {

    /**
     * Cifra la contraseña
     * @param contrasena contraseña a cifrar
     * @return contrasnaCifrada
     */
    public String getDigest(String contrasena) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(contrasena.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuffer contrasenaCifrada = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xff & digest[i]);
                if (hex.length() == 1) contrasenaCifrada.append('0');
                contrasenaCifrada.append(hex);
            }
            return contrasenaCifrada.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compara las contraseñas
     * @param contrasenaIntroducida contraseña introducida
     * @param contrasenaGuardada contraseña almacenada
     * @return true o false si coinciden o no
     */
    public boolean compararContrasena(String contrasenaIntroducida, String contrasenaGuardada) {
        return MessageDigest.isEqual(contrasenaIntroducida.getBytes(),contrasenaGuardada.getBytes());
    }
}
