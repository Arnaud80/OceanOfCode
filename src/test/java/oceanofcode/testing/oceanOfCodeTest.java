package oceanofcode.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import oceanofcode.*;
import oceanofcode.Player.*;


class ZoneTest {

	@Test
	void testGetWidth() {
		// Arrange
		Zone zone=new Player.Zone(0,0,14,14);
		
		// Act
		int width=zone.getWidth();
		
		// Assert
		assertEquals(15, width);
	}
}
