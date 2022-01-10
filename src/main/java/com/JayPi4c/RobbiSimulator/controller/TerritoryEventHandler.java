package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.view.RobbiContextMenu;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TerritoryEventHandler implements EventHandler<MouseEvent> {

	Territory territory;

	boolean robbiDragged = false;

	ButtonState buttonState;

	private RobbiContextMenu robbiContextMenu;

	public TerritoryEventHandler(Territory territory, TerritoryPanel territoryPanel, ButtonState buttonState) {
		this.territory = territory;
		this.buttonState = buttonState;
		territoryPanel.setOnContextMenuRequested(event -> {
			if (territory.robbiOnTile(getCol(event.getX()), getRow(event.getY()))) {
				robbiContextMenu = new RobbiContextMenu(territory);
				robbiContextMenu.show(territoryPanel.getScene().getWindow(), event.getScreenX(), event.getScreenY());
			}
		});

	}

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

	private int getCol(double x) {
		return (int) (x / (TerritoryPanel.getCellsize() + TerritoryPanel.getCellspacer()));
	}

	private int getRow(double y) {
		return (int) (y / (TerritoryPanel.getCellsize() + TerritoryPanel.getCellspacer()));

	}

}
