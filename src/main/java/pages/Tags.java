package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.internal.Coordinates;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static core.conditions.CustomCondition.textBegin;
import static core.helpers.UniqueDataHelper.clearThe;
import static core.helpers.UniqueDataHelper.the;

public class Tags {

    public static SelenideElement newTag = $("#tags");
    public static ElementsCollection tags = $$("#tags_list .selectable");

    @Step
    public static void add(String tagName) {
        newTag.setValue(tagName);
        $$("#as-results-tags li").shouldHave(exactTexts(tagName));
        newTag.pressEnter();
    }

    @Step
    public static void delete(String tagName) {
        delete(tags.find(exactText(tagName)));
    }

    public static void delete(SelenideElement tag) {
        Coordinates coordinates = tag.getCoordinates();
        coordinates.inViewPort();
        tag.hover();
        $("#unfollow_" + tag.getText().substring(1)).click();
        confirm(null);
    }

    @Step
    public static void filter(String tagName) {
        tags.find(exactText(tagName)).click();
    }

    public static void assertNotExist(String tagName) {
        tags.filter(exactText(tagName)).shouldBe(empty);
    }

    public static void assertExist(String tagName) {
        tags.filter(exactText(tagName)).shouldHave(size(1));
    }

    public static void assertNthIs(int nth, String tagName) {
        tags.get(nth).shouldHave(exactText(tagName));
    }


    public static void deleteAll() {
        clearThe();
        add(the("#stag"));
        assertNthIs(0, the("#stag"));
        ElementsCollection userTags = tags.filter(textBegin("#"));
        int countDeleted = 0;
        for (SelenideElement userTag : userTags) {
            delete(userTag);
            countDeleted++;
        }

        if (countDeleted > 1) {
            deleteAll();
        }
    }
}
