//import config.ConfigValidation;
//import org.junit.Assert;
//import org.junit.Test;
//
//public class ConfigValidationTest {
//
//    final ConfigValidation configValidation;
//    public ConfigValidationTest() {
//        configValidation = new ConfigValidation();
//    }
//
//    @Test
//    public void invalidUrl1() {
//        String url = "ftp://localhost:8080/";
//        Assert.assertEquals(configValidation.validateServerUrl(url), false);
//    }
//
//    @Test
//    public void invalidUrl2() {
//        String url = "ftp://amazon.com/";
//        Assert.assertEquals(configValidation.validateServerUrl(url), false);
//    }
//
//    @Test
//    public void validUrl1() {
//        String url = "http://13.233.39.58:8080/CEIR/DMC";
//        Assert.assertEquals(configValidation.validateServerUrl(url), true);
//    }
//
//    @Test
//    public void validUrl2() {
//        String url = "http://www.flipkart.com/cart";
//        Assert.assertTrue(configValidation.validateServerUrl(url));
//    }
//
//    @Test
//    public void validFileName1() {
//        String fileName = "sample.json";
//        Assert.assertTrue(configValidation.validateFileName(fileName));
//    }
//
//    @Test
//    public void validFileName2() {
//        String fileName = "sample_123_test.txt";
//        Assert.assertTrue(configValidation.validateFileName(fileName));
//    }
//
//    @Test
//    public void validFileName3() {
//        String fileName = "sample_(123)-test.txt";
//        Assert.assertTrue(configValidation.validateFileName(fileName));
//    }
//
//    @Test
//    public void invalidFileName1() {
//        String fileName = "sample_123_test&.txt";
//        Assert.assertEquals(configValidation.validateFileName(fileName), false);
//    }
//    @Test
//    public void invalidFileName2() {
//        String fileName = "sample*12.json";
//        Assert.assertEquals(configValidation.validateFileName(fileName), false);
//    }
//}
