package philwilson.org.librarieswest;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Phil on 07/07/2016.
 */
public class CatalogueTest{

    @Test
    public void testLogin() throws Exception {
        Catalogue catalogue = new Catalogue();
        assertTrue(catalogue.getLoginUrl().startsWith("https://m.solus.co.uk/catalogue/CatService.asmx/CatLogin?rnd="));
    }

    @Test
    public void testGetLoginData() throws Exception {
        Catalogue catalogue = new Catalogue();
        String json = catalogue.getLoginData(123, 456);
        assertEquals("{\"PIN\":456,\"appid\":\"e89f85e6-c6d5-47d0-a4e2-a3da0355f8e6\",\"udid\":\"0f248feb-7c64-4123-b6ca-c233e7aeb182\",\"account\":123}", json);
    }

//    @Test
//    public void testSendCredentials() {
//        fail("Not yet implemented");
//    }
}