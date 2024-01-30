package com.JayPi4c.RobbiSimulator.view;

import com.JayPi4c.RobbiSimulator.controller.ButtonState;
import com.JayPi4c.RobbiSimulator.controller.TerritoryEventHandler;
import com.JayPi4c.RobbiSimulator.model.*;
import com.JayPi4c.RobbiSimulator.utils.Observable;
import com.JayPi4c.RobbiSimulator.utils.Observer;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import lombok.extern.slf4j.Slf4j;

/**
 * This class draws the territory on a panel to allow interacting with the
 * graphical interface.
 *
 * @author Jonas Pohl
 */
@Slf4j
public class TerritoryPanel extends Canvas implements Observer {

    private static final Image[] tileImages = new Image[4];
    private static final Image[] itemImages = new Image[3];
    private static final Image robbiImage;
    private static final int TILE = 0;
    private static final int STOCKPILE = 1;
    private static final int HOLLOW = 2;
    private static final int PILEOFSCRAP = 3;
    private static final int NUT = 0;
    private static final int SCREW = 1;
    private static final int ACCU = 2;
    private static final int CELLSIZE = 32;
    private static final int CELLSPACER = 1;

    /**
     * loading territory images
     */
    static {

        logger.debug("Loading territory images");

        robbiImage = new Image(String.valueOf(MainStage.class.getResource("/img/0Robbi32.png")));

        tileImages[TILE] = new Image(String.valueOf(MainStage.class.getResource("/img/Tile32.png")));
        tileImages[STOCKPILE] = new Image(String.valueOf(MainStage.class.getResource("/img/Stockpile32.png")));
        tileImages[HOLLOW] = new Image(String.valueOf(MainStage.class.getResource("/img/Hollow32.png")));
        tileImages[PILEOFSCRAP] = new Image(String.valueOf(MainStage.class.getResource("/img/PileOfScrap32.png")));

        itemImages[NUT] = new Image(String.valueOf(MainStage.class.getResource("/img/Nut32.png")));
        itemImages[SCREW] = new Image(String.valueOf(MainStage.class.getResource("/img/Screw32.png")));
        itemImages[ACCU] = new Image(String.valueOf(MainStage.class.getResource("/img/Accu32.png")));
    }

    private Territory territory;
    // store current bounds to allow centering on updated territory size
    private Bounds bounds;

    /**
     * Constructor to create a new territory panel for the given territory.
     *
     * @param territory   the territory this panel is for
     * @param buttonState the buttonState to be able to create a
     *                    TerritoryEventHandler
     * @param parent      the parent Window to show alerts relative to the calling
     *                    window
     */
    public TerritoryPanel(Territory territory, ButtonState buttonState, MainStage parent) {
        this.territory = territory;
        this.territory.addObserver(this);

        TerritoryEventHandler eventHandler = new TerritoryEventHandler(territory, this, buttonState, parent);
        this.setOnMousePressed(eventHandler);
        this.setOnMouseDragged(eventHandler);
        this.setOnMouseReleased(eventHandler);

        drawPanel();
    }

    /**
     * Getter for the CELLSIZE.
     *
     * @return CELLSIZE value
     */
    public static int getCellsize() {
        return CELLSIZE;
    }

    /**
     * Getter for the CELLSPACER.
     *
     * @return CELLSPACER value
     */
    public static int getCellspacer() {
        return CELLSPACER;
    }

    /**
     * updates the size of the territory if the size has changed. Paints the
     * territory afterwards.
     */
    private void drawPanel() {
        if (getWidth() != getTerritoryWidth())
            setWidth(getTerritoryWidth());
        if (getHeight() != getTerritoryHeight())
            setHeight(getTerritoryHeight());
        paintTerritory();

    }

    /**
     * Getter for the territory width.
     *
     * @return the width of the territory, calculated by number of cols, Cellsize
     * and cellspacer
     */
    private int getTerritoryWidth() {
        return (territory.getNumCols()) * (CELLSIZE + CELLSPACER);
    }

    /**
     * Getter for the territory height.
     *
     * @return the height of the territory, calculated by number of rows, Cellsize
     * and cellspacer
     */
    private int getTerritoryHeight() {
        return (territory.getNumRows()) * (CELLSIZE + CELLSPACER);
    }

    /**
     * Paint all graphics for the territory and the robbi on the GUI.
     */
    private void paintTerritory() {

        GraphicsContext gc = getGraphicsContext2D();
        for (int i = 0; i < territory.getNumCols(); i++) {

            for (int j = 0; j < territory.getNumRows(); j++) {
                Tile t = territory.getTile(i, j);
                gc.drawImage(tileImages[TILE], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
                if (t instanceof Hollow) {
                    gc.drawImage(tileImages[HOLLOW], getPos(i), getPos(j), CELLSIZE, CELLSIZE);

                } else if (t instanceof PileOfScrap) {
                    gc.drawImage(tileImages[PILEOFSCRAP], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
                } else if (t instanceof Stockpile stockpile) {
                    gc.drawImage(tileImages[STOCKPILE], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
                    boolean nutDrawn = false;
                    boolean accuDrawn = false;
                    boolean screwDrawn = false;
                    for (Item item : stockpile.getAllItems()) {
                        if (!nutDrawn && item instanceof Nut) {
                            gc.drawImage(itemImages[NUT], getPos(i) + CELLSIZE / 2d, getPos(j), CELLSIZE / 2d,
                                    CELLSIZE / 2d);
                            nutDrawn = true;
                        } else if (!accuDrawn && item instanceof Accu) {
                            gc.drawImage(itemImages[ACCU], getPos(i), getPos(j), CELLSIZE / 2d, CELLSIZE / 2d);
                            accuDrawn = true;
                        } else if (!screwDrawn && item instanceof Screw) {
                            gc.drawImage(itemImages[SCREW], getPos(i) + CELLSIZE / 3d, getPos(j) + CELLSIZE / 2d,
                                    CELLSIZE / 2d, CELLSIZE / 2d);
                            screwDrawn = true;
                        }
                        if (nutDrawn && accuDrawn && screwDrawn)
                            break;
                    }
                }
                if (!(t instanceof Stockpile)) {
                    Item item = t.getItem();
                    if (item instanceof Nut) {
                        gc.drawImage(itemImages[NUT], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
                    } else if (item instanceof Screw) {
                        gc.drawImage(itemImages[SCREW], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
                    } else if (item instanceof Accu) {
                        gc.drawImage(itemImages[ACCU], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
                    }

                }
            }
        }

        double angle = switch (territory.getRobbiDirection()) {
            case NORTH:
                yield 270;
            case WEST:
                yield 180;
            case SOUTH:
                yield 90;
            case EAST:
            default:
                yield 0;
        };
        drawRotatedImage(gc, robbiImage, angle, (double) territory.getRobbiX() * (CELLSIZE + CELLSPACER) + CELLSPACER,
                (double) territory.getRobbiY() * (CELLSIZE + CELLSPACER) + CELLSPACER, CELLSIZE, CELLSIZE);
    }

    /**
     * Sets the transform for the GraphicsContext to rotate around a pivot point.
     *
     * @param gc    the graphics context the transform to applied to.
     * @param angle the angle of rotation.
     * @param px    the x pivot co-ordinate for the rotation (in canvas
     *              co-ordinates).
     * @param py    the y pivot co-ordinate for the rotation (in canvas
     *              co-ordinates).
     * @see <a href=
     * "https://stackoverflow.com/a/18262938/13670629">Stackoverflow</a>
     */
    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    /**
     * Draws an image on a graphics context.
     * <p>
     * The image is drawn at (tlpx, tlpy) rotated by angle pivoted around the point:
     * (tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2)
     *
     * @param gc    the graphics context the image is to be drawn on.
     * @param angle the angle of rotation.
     * @param tlpx  the top left x co-ordinate where the image will be plotted (in
     *              canvas co-ordinates).
     * @param tlpy  the top left y co-ordinate where the image will be plotted (in
     *              canvas co-ordinates).
     * @see <a href=
     * "https://stackoverflow.com/a/18262938/13670629">Stackoverflow</a>
     */
    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy, double w,
                                  double h) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy, w, h);
        gc.restore(); // back to original state (before rotation)
    }

    private int getPos(int val) {
        return (val) * (CELLSIZE + CELLSPACER) + CELLSPACER;
    }

    /**
     * taken from Dibo<br>
     * Centers the territory-panel in the center of the viewPortBounds.
     *
     * @param vpb The bounds in which the territory has to be centered
     */
    public void center(Bounds vpb) {
        this.bounds = vpb;
        double w = vpb.getWidth();
        double h = vpb.getHeight();
        if (w > getTerritoryWidth()) {
            setTranslateX((w - getTerritoryWidth()) / 2);
        } else
            setTranslateX(0);
        if (h > getTerritoryHeight()) {
            setTranslateY((h - getTerritoryHeight()) / 2);
        } else
            setTranslateY(0);
    }

    @Override
    public void update(Observable observable) {
        Platform.runLater(this::update);
    }

    /**
     * Centers the territory if the size has changed. Afterwards, it draws the
     * territorypanel.
     */
    public void update() {
        if (territory.hasSizeChanged()) {
            center(bounds);
            territory.setSizeChanged(false);
        }
        drawPanel();
    }

}
