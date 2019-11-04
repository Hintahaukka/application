package hifian.hintahaukka;

import static org.junit.Assert.*;
import org.junit.Test;
import hifian.hintahaukka.Service.BarCodeChecker;

public class BarCodeCheckerTest {

    @Test
    public void readedTooShortBarCode() {
        String code = "234567091234";
        BarCodeChecker bcChecker = new BarCodeChecker();
        assertFalse( bcChecker.checkEan13(code));
    }

    @Test
    public void readedTooLongBarCode() {
        String code = "23456709123412";
        BarCodeChecker bcChecker = new BarCodeChecker();
        assertFalse( bcChecker.checkEan13(code));
    }

    @Test
    public void readedBarCodeWithErrorInChecksum() {
        String code = "2345670912345";
        BarCodeChecker bcChecker = new BarCodeChecker();
        assertFalse( bcChecker.checkEan13(code));
    }

    @Test
    public void readedBarCodeWithGoodChecksum() {
        String code = "6418785301528";
        BarCodeChecker bcChecker = new BarCodeChecker();
        assertTrue( bcChecker.checkEan13(code));
    }
}
