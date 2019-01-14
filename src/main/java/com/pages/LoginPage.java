package com.pages;

import com.core.WebActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends WebActions {

    @FindBy(xpath = "")
    WebElement link;

    @FindBy(xpath = "")
    WebElement username;

    @FindBy(xpath = "")
    WebElement password;

    @FindBy(xpath = "")
    WebElement loginButton;

    public LoginPage(){
        super();
        PageFactory.initElements(this.webDriver,HomePage.class);
    }

    public void Login(){
        navigate("http://automationpractice.com/index.php");
        click(link);
        enter(username,"");
        enter(password,"");
        click(loginButton);
    }
}
