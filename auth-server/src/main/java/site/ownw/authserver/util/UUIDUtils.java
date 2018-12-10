package site.ownw.authserver.util;

import java.util.UUID;

/**
 * @author Sofior
 */
public abstract class UUIDUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
