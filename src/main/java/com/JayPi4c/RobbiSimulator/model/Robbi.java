package com.JayPi4c.RobbiSimulator.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the actor class of the simulator. All public functions in this class
 * will be available in the simulator's editor.
 *
 * @author Jonas Pohl
 */
@Slf4j
@NoArgsConstructor
public class Robbi {

    /**
     * Attribute to store the territory in which robbi is living
     */
    @Setter(value = AccessLevel.PACKAGE)
    private Territory territory;
    /**
     * Attribute to store robbi's x position in the territory
     */
    @Getter(value = AccessLevel.PACKAGE)
    private volatile int x;
    /**
     * Attribute to store robbi's y position in the territory
     */
    @Getter(value = AccessLevel.PACKAGE)
    private volatile int y;
    /**
     * Attribute to store robbi's item
     */
    @Getter(value = AccessLevel.PACKAGE, onMethod_ = {@Synchronized("territory")})
    @Setter(value = AccessLevel.PACKAGE, onMethod_ = {@Synchronized("territory")})
    private Item item = null;

    /**
     * Attribute to store robbi's facing
     */
    @Getter(value = AccessLevel.PACKAGE)
    private volatile DIRECTION facing;

    /**
     * The main-Method which will be overwritten by every custom Robbi
     * implementation. When starting a simulation, this method will be called.
     */
    void main() {
        // will be overwritten
        logger.error("Please overwrite the main-method");
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
        this.facing = DIRECTION.EAST;
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
     * Updates the facing of robbi to the given facing.
     *
     * @param facing robbi's new facing
     */
    void setFacing(DIRECTION facing) {
        synchronized (territory) {
            this.facing = facing;
        }
    }

    // ==================== PUBLIC FUNCTIONS ==========

    /**
     * If possible move Robbi one tile towards the direction it is facing.
     */
    public final void vor() {
        synchronized (territory) {
            Tile t;
            switch (facing) {
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
            facing = facing.next();
        }
        territory.setChanged();
        territory.notifyAllObservers();
    }

    /**
     * If possible drop the item that is stored in the bag.
     */
    public final void legeAb() {
        synchronized (territory) {
            if (item == null)
                throw new BagIsEmptyException();
            if (territory.placeItem(item, x, y))
                item = null;
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
            Item i = territory.getItem(x, y);
            if (i == null)
                throw new NoItemException();
            if (this.item != null) {
                throw new BagIsFullException();
            }
            this.item = territory.removeItem(x, y);
        }
        territory.setChanged();
        territory.notifyAllObservers();
    }

    /**
     * If a pile of scrap is ahead of Robbi and afterward a nonblocking tile, Robbi
     * pushes the pile of scrap one tile the direction he is facing.
     */
    public final void schiebeSchrotthaufen() {
        synchronized (territory) {
            territory.deactivateNotification();
            if (!vornSchrotthaufen()) {
                territory.activateNotification();
                throw new NoPileOfScrapAheadException();
            }
            int dx = switch (facing) {
                case EAST -> x + 2;
                case WEST -> x - 2;
                default -> x;
            };
            int dy = switch (facing) {
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
                int px = switch (facing) {
                    case EAST -> x + 1;
                    case WEST -> x - 1;
                    default -> x;
                };
                int py = switch (facing) {
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
     * @return true if an item is on robbi's tile, false otherwise
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
     * @return true if a hollow is ahead, false otherwise
     */
    public final boolean vornKuhle() {
        synchronized (territory) {
            int dx = switch (facing) {
                case EAST -> x + 1;
                case WEST -> x - 1;
                default -> x;
            };
            int dy = switch (facing) {
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
            int dx = switch (facing) {
                case EAST -> x + 1;
                case WEST -> x - 1;
                default -> x;
            };
            int dy = switch (facing) {
                case NORTH -> y - 1;
                case SOUTH -> y + 1;
                default -> y;
            };
            return territory.getTile(dx, dy) instanceof PileOfScrap;
        }
    }

    /**
     * checks if an item is in Robbi's bag.
     *
     * @return true if an item is in the bag, false otherwise
     */
    public final boolean istTascheVoll() {
        synchronized (territory) {
            return item != null;
        }
    }

}
