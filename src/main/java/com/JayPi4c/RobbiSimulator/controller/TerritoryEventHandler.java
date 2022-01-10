package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.view.RobbiContextMenu;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

/**
 * This class combines all functions needed to control the user interaction with
 * the territory
 * 
 * @author Jonas Pohl
 *
 */
public class TerritoryEventHandler implements EventHandler<MouseEvent> {

	private Territory territory;

	private boolean robbiDragged = false;

	private ButtonState buttonState;

	private RobbiContextMenu robbiContextMenu;

	/**
	 * Creates a new TerritoryEventHandler and sets the contextMenuRequest to show a
	 * RobbiContextMenu if the user right-clicks on Robbi
	 * 
	 * @param territory      the Territory this handler is for
	 * @param territoryPanel the TerritoryPanel, this handler is handling
	 * @param buttonState    the ButtonState for this eventHandler
	 * @param parent         the parent window to show alerts relative to it
	 */
	public TerritoryEventHandler(Territory territory, TerritoryPanel territoryPanel, ButtonState buttonState,
			Window parent) {
		this.territory = territory;
		this.buttonState = buttonState;
		territoryPanel.setOnContextMenuRequested(event -> {
			if (territory.robbiOnTile(getCol(event.getX()), getRow(event.getY()))) {
				robbiContextMenu = new RobbiContextMenu(territory, parent);
				robbiContextMenu.show(territoryPanel.getScene().getWindow(), event.getScreenX(), event.getScreenY());
			}
		});

	}

	/**
	 * {@inheritDoc} <br>
	 * Places an Item or Object in the territory or drags and drops the robbi
	 */
	@Override
	public void handle(MouseEvent event) {
		int col = getCol(event.getX());
		int row = getRow(event.getY());
		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			robbiDragged = territory.robbiOnTile(col, row);
		} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			if (robbiDragged)
				territory.placeRobbi(col, row);
		} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
			if (!robbiDragged) {
				switch (buttonState.getSelected()) {
				case ButtonState.ROBBI:
					territory.placeRobbi(col, row);
					break;
				case ButtonState.HOLLOW:
					territory.placeHollow(col, row);
					break;
				case ButtonState.PILE_OF_SCRAP:
					territory.placePileOfScrap(col, row);
					break;
				case ButtonState.STOCKPILE:
					territory.placeStockpile(col, row);
					break;
				case ButtonState.ACCU:
					territory.placeAccu(col, row);
					break;
				case ButtonState.SCREW:
					territory.placeScrew(col, row);
					break;
				case ButtonState.NUT:
					territory.placeNut(col, row);
					break;
				case ButtonState.CLEAR:
					territory.clearTile(col, row);
					break;
				case ButtonState.NONE:
					// fall through
				default:
					// Do nothing
				}
			}
			robbiDragged = false;
		}

	}

	/**
	 * Calculates the column to a given horizontal mouse-position
	 * 
	 * @param x Horizontal position of the event relative to the origin of the
	 *          MouseEvent's source.
	 * @return column corresponding to the given x
	 */
	private int getCol(double x) {
		return (int) (x / (TerritoryPanel.getCellsize() + TerritoryPanel.getCellspacer()));
	}

	/**
	 * Calculates the row to a given vertical mouse-position
	 * 
	 * @param y Vertical position of the event relative to the origin of the
	 *          MouseEvent's source.
	 * @return row corresponding to the given y
	 */
	private int getRow(double y) {
		return (int) (y / (TerritoryPanel.getCellsize() + TerritoryPanel.getCellspacer()));

	}

}
