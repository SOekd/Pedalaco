package app.pedalaco.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageHelper {

    public String getFormat(String fileName)  {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
