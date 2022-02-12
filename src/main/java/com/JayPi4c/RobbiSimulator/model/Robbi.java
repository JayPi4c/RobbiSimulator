package com.JayPi4c.RobbiSimulator.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This is the actor class of the simulator. All public functions in this class
 * will be available in the simulators editor.
 * 
 * @author Jonas Pohl
 */
public class Robbi {
	private static final Logger logger = LoggerFactory.getLogger(Robbi.class);

	/**
	 * Attribute to store the territory in which robbi is living
	 */
	private Territory territory;
	/**
	 * Attribute to store robbis x position in the territory
	 */
	private volatile int x;
	/**
	 * Attribute to store robbis y position in the territory
	 */
	private volatile int y;
	/**
	 * Attribute to store robbis item
	 */
	private Item inBag = null;

	/**
	 * Attribute to store robbis facing
	 */
	private volatile DIRECTION direction;

	/**
	 * Constructor to create a custom robbi via class-loader
	 */
	public Robbi() {
		// needed for class-loader
	}

	/**
	 * The main-Method which will be overwritten by every custom Robbi
	 * implementation. When starting a simulation, this method will be called.
	 */
	void main() {
		// will be overritten
		logger.error("Please overrite the main-method");
	}

	/**
	 * Constructor to create a new robbi with the given territory.
	 * 
	 * @param t the territory in which robbi is placed.
	 */
	Robbi(Territory t) {
		this.territory = t;
		this.x = 0;
		this.y = 0;
		this.direction = DIRECTION.EAST;
	}

	// ============= HELPER ================

	/**
	 * Sets the position of robbi.
	 * 
	 * @param x new x position
	 * @param y new y position
	 */
	void setPosition(int x, int y) {
		synchronized (territory) {
			if (x < 0 || y < 0 || x >= territory.getNumCols() || y >= territory.getNumRows())
				throw new IllegalArgumentException("Robbi kann nicht außerhalb des Territoriums platziert werden.");
			if (territory.getTile(x, y) instanceof Hollow)
				throw new TileBlockedException();
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Get Robbis X Position
	 * 
	 * @return robbis x postion
	 */
	int getX() {
		return x;
	}

	/**
	 * Get Robbis Y Position
	 * 
	 * @return robbis y position
	 */
	int getY() {
		return y;
	}

	/**
	 * Getter for robbis facing.
	 * 
	 * @return robbis current facing
	 */
	DIRECTION getFacing() {
		return this.direction;
	}

	/**
	 * Getter for the item robbi is currently holding.
	 * 
	 * @return the item in robbis bag.
	 */
	Item getItem() {
		synchronized (territory) {
			return inBag;
		}
	}

	/**
	 * Puts the given Item in Robbis bag.
	 * 
	 * @param item the item to put into robbis bag
	 */
	void setItem(Item item) {
		synchronized (territory) {
			this.inBag = item;
		}
	}

	/**
	 * Setter for the territory.
	 * 
	 * @param t the new territory
	 */
	void setTerritory(Territory t) {
		this.territory = t;
	}

	/**
	 * Updates the facing of robbi to the given facing.
	 * 
	 * @param facing robbis new facing
	 */
	void setFacing(DIRECTION facing) {
		synchronized (territory) {
			this.direction = facing;
		}
	}

	// ==================== PUBLIC FUNCTIONS ==========

	/**
	 * If possible move Robbi one tile towards the direction it is facing.
	 */
	public final void vor() {
		synchronized (territory) {
			Tile t;
			switch (direction) {
			case NORTH:
				t = territory.getTile(x, y - 1);
				if (t instanceof Hollow)
					throw new HollowAheadException();
				else {
					y = y - 1;
					y += territory.getNumRows();
					y %= territory.getNumRows();
				}
				break;
			case SOUTH:
				t = territory.getTile(x, y + 1);
				if (t instanceof Hollow)
					throw new HollowAheadException();
				else {
					y = y + 1;
					y += territory.getNumRows();
					y %= territory.getNumRows();
				}
				break;
			case EAST:
				t = territory.getTile(x + 1, y);
				if (t instanceof Hollow)
					throw new HollowAheadException();
				else {
					x = x + 1;
					x += territory.getNumCols();
					x %= territory.getNumCols();
				}
				break;
			case WEST:
				t = territory.getTile(x - 1, y);
				if (t instanceof Hollow)
					throw new HollowAheadException();
				else {
					x = x - 1;
					x += territory.getNumCols();
					x %= territory.getNumCols();
				}
				break;
			default:
				break;
			}
		}
		territory.setChanged();
		territory.notifyAllObservers();
	}

	/**
	 * Turn Robbi counterclockwise.
	 */
	public final void linksUm() {
		synchronized (territory) {
			direction = direction.next();
		}
		territory.setChanged();
		territory.notifyAllObservers();
	}

	/**
	 * If possible drop the item that is stored in the bag.
	 */
	public final void legeAb() {
		synchronized (territory) {
			if (inBag == null)
				throw new BagIsEmptyException();
			if (territory.placeItem(inBag, x, y))
				inBag = null;
			else
				throw new TileIsFullException();
		}
		territory.setChanged();
		territory.notifyAllObservers();
	}

	/**
	 * if possible take the item that occupies the tile Robbi is on.
	 */
	public final void nehmeAuf() {
		synchronized (territory) {
			Item item = territory.getItem(x, y);
			if (item == null)
				throw new NoItemException();
			if (inBag != null) {
				throw new BagIsFullException();
			}
			inBag = territory.removeItem(x, y);
		}
		territory.setChanged();
		territory.notifyAllObservers();
	}

	/**
	 * If a pile of scrap is ahead of Robbi and afterwards a nonblocking tile, Robbi
	 * pushes the pile of scrap one tile the direction he is facing.
	 */
	public final void schiebeSchrotthaufen() {
		synchronized (territory) {
			territory.deactivateNotification();
			if (!vornSchrotthaufen()) {
				territory.activateNotification();
				throw new NoPileOfScrapAheadException();
			}
			int dx = switch (direction) {
			case EAST -> x + 2;
			case WEST -> x - 2;
			default -> x;
			};
			int dy = switch (direction) {
			case NORTH -> y - 2;
			case SOUTH -> y + 2;
			default -> y;
			};

			Tile t = territory.getTile(dx, dy);
			if (t instanceof Stockpile || t instanceof PileOfScrap) {
				territory.activateNotification();
				throw new TileBlockedException();
			} else {
				if (territory.getTile(dx, dy) instanceof Hollow)
					territory.clearTile(dx, dy);
				else
					territory.placePileOfScrap(dx, dy);
				int px = switch (direction) {
				case EAST -> x + 1;
				case WEST -> x - 1;
				default -> x;
				};
				int py = switch (direction) {
				case NORTH -> y - 1;
				case SOUTH -> y + 1;
				default -> y;
				};
				territory.clearTile(px, py);
			}
			vor();
		}
		territory.activateNotification();
	}

	/**
	 * checks if an Item is on the tile Robbi is on.
	 * 
	 * @return true if an item is on robbis tile, false otherwise
	 */
	public final boolean gegenstandDa() {
		synchronized (territory) {
			return territory.getItem(x, y) != null;
		}
	}

	/**
	 * Checks if the tile on which Robbi stands is a stockpile
	 * 
	 * @return true if the current tile is an instanceof Stockpile, false otherwise
	 */
	public final boolean istLagerplatz() {
		synchronized (territory) {
			return territory.getTile(x, y) instanceof Stockpile;
		}
	}

	/**
	 * checks if a hollow is ahead of Robbi.
	 * 
	 * @return true if an hollow is ahead, false otherwise
	 */
	public final boolean vornKuhle() {
		synchronized (territory) {
			int dx = switch (direction) {
			case EAST -> x + 1;
			case WEST -> x - 1;
			default -> x;
			};
			int dy = switch (direction) {
			case NORTH -> y - 1;
			case SOUTH -> y + 1;
			default -> y;
			};
			return territory.getTile(dx, dy) instanceof Hollow;
		}
	}

	/**
	 * checks if a pile of scrap is ahead of Robbi.
	 * 
	 * @return true if a pile of scrap is ahead of robbi, false otherwise
	 */
	public final boolean vornSchrotthaufen() {
		synchronized (territory) {
			int dx = switch (direction) {
			case EAST -> x + 1;
			case WEST -> x - 1;
			default -> x;
			};
			int dy = switch (direction) {
			case NORTH -> y - 1;
			case SOUTH -> y + 1;
			default -> y;
			};
			return territory.getTile(dx, dy) instanceof PileOfScrap;
		}
	}

	/**
	 * checks if an item is in Robbis bag.
	 * 
	 * @return true if an item is in the bag, false otherwise
	 */
	public final boolean istTascheVoll() {
		synchronized (territory) {
			return inBag != null;
		}
	}

	// =================== DEBUG ===================

	/**
	 * Prints robbis current facing into the console.
	 */
	void print() {
		switch (direction) {
		case NORTH:
			System.out.print("^");
			break;
		case SOUTH:
			System.out.print("v");
			break;
		case EAST:
			System.out.print(">");
			break;
		case WEST:
			System.out.print("<");
			break;
		default:
			System.out.print(" ");
		}
	}

}
