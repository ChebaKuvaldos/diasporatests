package com.automician.pages;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.automician.datastructures.PodUser;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.automician.core.AdditionalAPI.isVisible;
import static com.automician.core.conditions.CustomCondition.textEnd;
import static java.lang.Boolean.*;
import static com.automician.pages.Aspects.STANDART_ASPECTS;
import static com.automician.pages.Contact.contact;
import static com.automician.steps.Scenarios.waitStreamOpening;

public class Contacts {

    public static ElementsCollection aspects = $$(".aspect a");

    @Step
    public static void addAspect(String aspect) {
        $("[data-target='#newAspectModal']").click();
        addAspectInDialog(aspect);
    }

    public static void addAspectInDialog(String aspect) {
        $("#newAspectModalLabel").shouldHave(text("Add a new aspect"));
        $("#aspect_name").setValue(aspect);
        $(".creation").click();
    }

    @Step
    public static void selectAspect(String aspectName) {
        aspect(aspectName).click();
        $(".header #aspect_name").shouldHave(exactText(aspectName));
    }

    @Step
    public static void deleteAspect() {
        $("#delete_aspect").click();
        confirm(null);
    }

    @Step
    public static void rename(String newName) {
        $("#change_aspect_name").click();
        $("#aspect_name_form #aspect_name").setValue(newName);
        $(By.name("commit")).click();
    }

    @Step
    public static void addLinkedContactForAspect(String aspect, PodUser podUser) {
        selectAspect(aspect);
        contact(podUser).find(".contact_add-to-aspect").click();
    }

    @Step
    public static void deleteLinkedContactForAspect(String aspect, PodUser podUser) {
        selectAspect(aspect);
        contact(podUser).find(".contact_remove-from-aspect").click();
    }

    @Step
    public static void openAllContacts() {
        $(".all_contacts a").click();
    }

    @Step
    public static int countContactsInAspect(String aspectName) {
        return Integer.parseInt(aspect(aspectName).find(".badge").getText());
    }

    @Step
    public static void assertCountContactsInAspect(String aspectName, int countContacts) {
        aspect(aspectName).find(".badge").shouldHave(exactText(Integer.toString(countContacts)));
    }

    @Step
    public static void assertAspect(String aspectName) {
        aspect(aspectName).shouldBe(visible);
    }

    @Step
    public static void assertNoAspect(String aspectName) {
        aspect(aspectName).shouldNotBe(present);
    }

    @Step
    public static void ensureAspectsForContact(PodUser podUser, String... aspects) {
        Contact.ensureAspectsForContact(contact(podUser), aspects);
    }

    @Step
    public static void ensureAspect(String aspectName) {
        if (!isVisible(aspect(aspectName))) {
            addAspect(aspectName);
        }
    }

    @Step
    public static void ensureNoAspect(String aspectName) {
        if (isVisible(aspect(aspectName))) {
            selectAspect(aspectName);
            deleteAspect();
        }
    }

    @Step
    private static SelenideElement aspect(String aspect) {
        return aspects.find(textEnd("\n" + aspect));
    }

    private static Boolean isStandartAspect(String aspect) {
        for (String standartAspect : STANDART_ASPECTS) {
            if (standartAspect.equals(aspect)) {
                return TRUE;
            }
        }
        return FALSE;
    }

    @Step
    public static void deleteAllUserAspects() {
        int countDeleted = 0;
        waitStreamOpening();
        String[] aspectNames = aspects.filter(text("\n")).getTexts();
        for (String aspectName : aspectNames) {
            if (aspectName.equals("")) {
                continue;
            }
            aspectName = aspectName.substring(2);
            if (isStandartAspect(aspectName)) {
                continue;
            }
            selectAspect(aspectName);
            deleteAspect();
            countDeleted++;
        }
        if (countDeleted > 0) {
            deleteAllUserAspects();
        }
    }

}
