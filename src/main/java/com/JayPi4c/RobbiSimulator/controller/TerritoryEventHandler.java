package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TerritoryEventHandler implements EventHandler<MouseEvent> {

	Territory territory;

	boolean robbiDragged = false;

	ButtonState buttonState;

	public TerritoryEventHandler(Territory territory, ButtonState buttonState) {
		this.territory = territory;
		this.buttonState = buttonState;
	}

	@Override
	public void handle(MouseEvent event) {
		int col = (int) (event.getX() / (TerritoryPanel.getCellsize() + TerritoryPanel.getCellspacer()));
		int row = (int) (event.getY() / (TerritoryPanel.getCellsize() + TerritoryPanel.getCellspacer()));
		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			robbiDragged = territory.robbiOnTile(col, row);
		}

		else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
			if (robbiDragged)
				territory.placeRobbi(col, row);
		}

		else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
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

}
