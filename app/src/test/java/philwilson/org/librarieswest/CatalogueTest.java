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
        JSONObject json = catalogue.getLoginData(123, 456);
        assertEquals("789", json.toString());
        fail("Not yet implemented");
    }
}