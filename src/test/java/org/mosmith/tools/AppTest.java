package org.mosmith.tools;

import junit.framework.TestCase;
import org.junit.Test;
import org.mosmith.tools.dependency.resolve.ResolveException;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    @Test
    public void testApp() throws ResolveException {
        App.main(null);
    }
}
