package org.opentox.toxotis.util.aa;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentox.toxotis.ToxOtisException;

/**
 *
 * @author chung
 */
public class AuthenticationTokenTest {

    public AuthenticationTokenTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAcquireToken() throws ToxOtisException, IOException {
        AuthenticationToken at = new AuthenticationToken(new File("/home/chung/toxotisKeys/my.key")); // << Provide your credentials here
        System.out.println(at.stringValue());
        System.out.println(at.getTokenUrlEncoded());
        System.out.println(at.getTokenCreationTimestamp());
        System.out.println(at.getTokenCreationDate());
      
        System.out.println(at.getUser());
      
    }
}

