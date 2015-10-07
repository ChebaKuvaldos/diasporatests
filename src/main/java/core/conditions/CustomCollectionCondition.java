package core.conditions;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.collections.Texts;
import com.codeborne.selenide.impl.Html;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class CustomCollectionCondition {

    public static CollectionCondition textsBegin(String... expectedTexts){

        return new Texts(expectedTexts){
            public boolean apply(List<WebElement> elements) {
                if (elements.size() < this.expectedTexts.length) {
                    return false;
                } else {
                    for (int i = 0; i < this.expectedTexts.length; ++i) {
                        WebElement element = (WebElement) elements.get(i);
                        String expectedText = this.expectedTexts[i];
                        if (!Html.text.contains(element.getText(), expectedText)) {
                            return false;
                        }
                    }

                    return true;
                }
            }

            public String toString() {
                return "Texts begin " + Arrays.toString(this.expectedTexts);
            }
        };

    }

}