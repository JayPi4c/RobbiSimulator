package com.JayPi4c.RobbiSimulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TerritoryTest {
	@Mock
	TerritoryState stateMock;

	@Test
	void testRestore() {
		when(stateMock.getNumberOfColumns()).thenReturn(3);
		when(stateMock.getNumberOfRows()).thenReturn(4);
		when(stateMock.getRobbiState()).thenReturn(new RobbiState(2, 3, DIRECTION.NORTH, new Nut()));
		when(stateMock.getTiles()).thenReturn(new Tile[][] { { new Tile(), new Tile(), new Tile(), new Tile() },
				{ new Tile(), new Tile(), new Tile(), new Tile() },
				{ new Tile(), new Tile(), new Tile(), new Tile() } });

		Territory territory = new Territory();
		assertEquals(0, territory.getRobbi().getX());
		assertEquals(0, territory.getRobbi().getY());
		assertEquals(6, territory.getNumCols());
		assertEquals(6, territory.getNumRows());

		territory.restore(stateMock);

		assertEquals(2, territory.getRobbi().getX());
		assertEquals(3, territory.getRobbi().getY());
		assertEquals(3, territory.getNumCols());
		assertEquals(4, territory.getNumRows());
		assertNotNull(territory.getTile(2, 3));
	}

	@Test
	void testSave() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placeAccu(0, 0);

		TerritoryState state = territory.save();

		assertNotNull(state);
		assertEquals(4, state.getNumberOfColumns());
		assertEquals(2, state.getNumberOfRows());
		assertTrue(state.getTiles()[0][0].getItem() instanceof Accu);
	}

	@Test
	void testChangeSize() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		assertEquals(4, territory.getNumCols());
		assertEquals(2, territory.getNumRows());
	}

	@Test
	void testPlaceAccu() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placeAccu(0, 1);
		assertTrue(territory.getTile(0, 1).getItem() instanceof Accu);
	}

	@Test
	void testPlaceNut() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placeNut(0, 1);
		assertTrue(territory.getTile(0, 1).getItem() instanceof Nut);
	}

	@Test
	void testPlaceScrew() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placeScrew(0, 1);
		assertTrue(territory.getTile(0, 1).getItem() instanceof Screw);
	}

	@Test
	void testPlaceRobbi() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placeRobbi(0, 1);
		assertEquals(0, territory.getRobbiX());
		assertEquals(1, territory.getRobbiY());
	}

	@Test
	void testPlaceStockpile() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placeStockpile(0, 1);
		assertTrue(territory.getTile(0, 1) instanceof Stockpile);
	}

	@Test
	void testPlaceHollow() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placeHollow(0, 1);
		assertTrue(territory.getTile(0, 1) instanceof Hollow);
	}

	@Test
	void testPlacePileOfScrap() {
		Territory territory = new Territory();
		territory.changeSize(4, 2);
		territory.placePileOfScrap(0, 1);
		assertTrue(territory.getTile(0, 1) instanceof PileOfScrap);
	}
}
