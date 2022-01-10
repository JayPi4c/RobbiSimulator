package com.JayPi4c.RobbiSimulator.model;

/**
 * 
 * This is the actor class of the simulator. All public functions in this class
 * will be available in the simulators editor.
 * 
 * last modified 05.11.2021
 * 
 * @author Jonas Pohl
 */
public class Robbi {

	private Territory territory;

	private int x, y;
	private Item inBag = null;

	private enum DIRECTION {
		NORTH, WEST, SOUTH, EAST;

		private static DIRECTION[] vals = values();

		public DIRECTION next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}

		@SuppressWarnings("unused")
		public DIRECTION previous() {
			return vals[(this.ordinal() + vals.length - 1) % vals.length];
		}
	}

	private DIRECTION direction;

	Robbi(Territory t) {
		this.territory = t;
		this.x = 0;
		this.y = 0;
		this.direction = DIRECTION.EAST;
	}

	// ============= HELPER ================

	/**
	 * Set the position of robbi Be careful, Robbi does not check if it is in bounds
	 * 
	 * @param x
	 * @param y
	 */
	void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get Robbis X Position
	 * 
	 * @return
	 */
	int getX() {
		return x;
	}

	/**
	 * Get Robbis Y Position
	 * 
	 * @return
	 */
	int getY() {
		return y;
	}

	// ==================== PUBLIC FUNCTIONS ==========

	/**
	 * If possible move Robbi one tile towards the direction it is facing
	 */
	public void vor() {
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

	/**
	 * Turn Robbi counterclockwise
	 */
	public void linksUm() {
		direction = direction.next();
	}

	/**
	 * If possible drop the item that is stored in the bag
	 */
	public void legeAb() {
		if (inBag == null)
			throw new BagIsEmptyException();
		if (territory.placeItem(inBag, x, y))
			inBag = null;
		else
			throw new TileIsFullException();
	}

	/**
	 * if possible take the item that occupies the tile Robbi is on
	 */
	public void nehmeAuf() {
		Item item = territory.getItem(x, y);
		if (item == null)
			throw new NoItemException();
		if (inBag != null) {
			throw new BagIsFullException();
		}
		inBag = territory.removeItem(x, y);
	}

	/**
	 * If a pile of scrap is ahead of Robbi and afterwards a nonblocking tile, Robbi
	 * pushes the pile of scrap one tile the direction he is facing
	 */
	public void schiebeSchrotthaufen() {

		if (!vornSchrotthaufen())
			throw new NoPileOfScrapAheadException();

		int dx = switch (direction) {
		case EAST:
			yield x + 2;
		case WEST:
			yield x - 2;
		default:
			yield x;
		};
		int dy = switch (direction) {
		case NORTH:
			yield y - 2;
		case SOUTH:
			yield y + 2;
		default:
			yield y;
		};
		Tile t = territory.getTile(dx, dy);
		if (t instanceof Stockpile || t instanceof PileOfScrap)
			throw new TileBlockedException();
		else {
			if (territory.getTile(dx, dy) instanceof Hollow)
				territory.clearTile(dx, dy);
			else
				territory.placePileOfScrap(dx, dy);
			int px = switch (direction) {
			case EAST:
				yield x + 1;
			case WEST:
				yield x - 1;
			default:
				yield x;
			};
			int py = switch (direction) {
			case NORTH:
				yield y - 1;
			case SOUTH:
				yield y + 1;
			default:
				yield y;
			};
			territory.clearTile(px, py);
			;
		}
		vor();
	}

	/**
	 * checks if an Item is on the tile Robbi is on.
	 * 
	 * @return
	 */
	public boolean gegenstandDa() {
		return territory.getItem(x, y) != null;
	}

	/**
	 * Checks if the tile on which Robbi stands is a stockpile
	 * 
	 * @return
	 */
	public boolean istLagerplatz() {
		return territory.getTile(x, y) instanceof Stockpile;
	}

	/**
	 * checks if a hollow is ahead of Robbi
	 * 
	 * @return
	 */
	public boolean vornKuhle() {
		int dx = switch (direction) {
		case EAST:
			yield x + 1;
		case WEST:
			yield x - 1;
		default:
			yield x;
		};
		int dy = switch (direction) {
		case NORTH:
			yield y - 1;
		case SOUTH:
			yield y + 1;
		default:
			yield y;
		};
		return territory.getTile(dx, dy) instanceof Hollow;
	}

	/**
	 * checks if a pile of scrap is ahead of Robbi.
	 * 
	 * @return
	 */
	public boolean vornSchrotthaufen() {
		int dx = switch (direction) {
		case EAST:
			yield x + 1;
		case WEST:
			yield x - 1;
		default:
			yield x;
		};
		int dy = switch (direction) {
		case NORTH:
			yield y - 1;
		case SOUTH:
			yield y + 1;
		default:
			yield y;
		};
		System.out.println(dx + " " + dy);
		return territory.getTile(dx, dy) instanceof PileOfScrap;
	}

	/**
	 * checks if an item is in Robbis bag
	 * 
	 * @return
	 */
	public boolean istTascheVoll() {
		return inBag != null;
	}

	// =================== DEBUG ===================

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
