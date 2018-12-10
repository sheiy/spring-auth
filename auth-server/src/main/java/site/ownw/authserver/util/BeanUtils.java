package site.ownw.authserver.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Sofior
 */
public abstract class BeanUtils extends org.springframework.beans.BeanUtils {

    public static String[] findNullFields(Object o) {
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            var result = new ArrayList<String>(fields.length);
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldValue = field.get(o);
                if (fieldValue == null) {
                    result.add(field.getName());
                }
            }
            return result.toArray(new String[]{});
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
