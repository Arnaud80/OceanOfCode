package oceanofcode.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import oceanofcode.*;
import oceanofcode.Player.*;

@Tag("ZoneTests")
@DisplayName("Tests de la classe Zone")
class ZoneTest {
	private Zone zone; 
	
	@BeforeEach
	void initZone() {
		zone=new Player.Zone(0,0,14,14);
	}
	
	@AfterEach
	void endZone() {
		zone=null;
	}
	
	
	@Tag("Constructor")
	@DisplayName("Tests zone creation by his number")
	@ParameterizedTest(name = "Zone {0} should be ({1},{2})({3},{4})")
	@CsvSource({"0,0,0,0,0", "10,0,0,0,0", // Tests outside the normal process 
		"1,0,0,4,4","2,5,0,9,4","3,10,0,14,4",
		"4,0,5,4,9","5,5,5,9,9","6,10,5,14,9",
		"7,0,10,4,14","8,5,10,9,14","9,10,10,14,14"})
	void constructor_NumberZone_ReturnTheCorrespondingEZone(int numZone, int expectedX1, int expectedY1, int expectedX2,int expectedY2) {
		// Arrange
		
		//Act
		Zone newZone=new Zone(numZone);
		Zone expectedZone=new Zone(expectedX1, expectedY1, expectedX2, expectedY2);
				
		//Assert
		assertThat(newZone).isEqualToComparingFieldByField(expectedZone);
	}
	
	@Test
	void getWidth_returnWidthOfTheZone() {
		// Arrange
		
		// Act
		int width=zone.getWidth();
		
		// Assert
		assertThat(width).isEqualTo(15);
	}
	
	@Test
	void getHeight_returnHeightOfTheZone() {
		// Arrange
		
		// Act
		int height=zone.getHeight();
		
		// Assert
		assertThat(height).isEqualTo(15);
	}
}
