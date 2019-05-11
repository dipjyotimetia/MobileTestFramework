package com.Exception;

import org.openqa.selenium.ElementNotVisibleException;

public class ElementException extends ElementNotVisibleException {
    /**
     * Element exception
     *
     * @param message exception message
     */
    public ElementException(String message) {
        super(message);
    }
}
