package util.options;

import static org.junit.Assert.*;

import org.junit.Test;

public class OptionsTest {
	
	@Test public void testVerboseMode() {
		String[] args1 = { "foo", "bar" };
		IOptions options1 = new Options(args1);
		String[] args2 = { "foo", "-v", "bar" };
		IOptions options2 = new Options(args2);
		
		assertEquals(false, options1.isVerboseMode());
		assertEquals(true, options2.isVerboseMode());
	}
}
