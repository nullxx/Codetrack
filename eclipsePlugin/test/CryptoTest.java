import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import codetrack.api.Crypto;

public class CryptoTest {

	@Test
	public void sha512() {
		String toEncode = "patata123";
		String encoded = Crypto.genSHA512(toEncode);

		assertEquals(encoded,
				"84555d57b20cde0481177513b2d10a4eb1f2140f0a079c0385b0aba01916a5be4f96ccfa11ee8830920614790ea03bb291b7f4bda780199029bf3f54ca6b0746");
	}
}
