package core;

import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.impl.ScreenShotLaboratory;
import com.google.common.io.Files;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;


public class AdditionalAPI {

    @Attachment(type = "image/png")
    public byte[] screenshot(byte[] dataForScreenshot) {
        return dataForScreenshot;
    }

    public byte[] lastSelenideScreenshot() {
        Field allScreenshotsField = null;
        try {
            allScreenshotsField = ScreenShotLaboratory.class.getDeclaredField("allScreenshots");
            allScreenshotsField.setAccessible(true);
            List<String> allScreenshots = (List<String>) allScreenshotsField.get(Screenshots.screenshots);
            int allScreenshotsSize = allScreenshots.size();
            if (allScreenshotsSize > 0) {
                return Files.toByteArray(new File(allScreenshots.get(allScreenshotsSize - 1)));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Attachment(type = "image/png")
    public static byte[] newScreenshot() {
        File screenshot = Screenshots.getScreenShotAsFile();
        try {
            return Files.toByteArray(screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}