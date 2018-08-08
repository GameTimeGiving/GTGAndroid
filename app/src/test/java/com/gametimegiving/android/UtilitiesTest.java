package com.gametimegiving.android;

import com.gametimegiving.android.Helpers.Utilities;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class UtilitiesTest {

    @Test
    public void showMsg() {
        fail("Not Implemented");
    }

    @Test
    public void notYetImplemented() {
        fail("Not Implemented");
    }

    @Test
    public void writeSharedPref() {
        fail("Not Implemented");
    }

    @Test
    public void readSharedPref() {
        fail("Not Implemented");
    }

    @Test
    public void readSharedPref1() {
        fail("Not Implemented");
    }

    @Test
    public void readBoolSharedPref() {
        fail("Not Implemented");
    }

    @Test
    public void WhenGivenNothingformatCurrencyFormatsCorrectly() {
        //Arrange
        int value = 0;
        String expected = "$0.00";
        String actual = Utilities.FormatCurrency(value);
        //Act
        //Assert
        assertEquals(expected, actual);
    }

    @Test
    public void removeCurrency() {
        //Arrange
        //Act
        //Assert
        fail("Not Implemented");
    }

    @Test
    public void clearSharedPrefs() {
        fail("Not Implemented");
    }

    @Test
    public void showVersion() {
        fail("Not Implemented");
    }

    @Test
    public void generateHashKey() {
        fail("Not Implemented");
    }
}