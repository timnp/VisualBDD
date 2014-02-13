package model;

import java.util.HashMap;
import java.util.LinkedList;

public class AbstractObddLayout {
	/**
	 * the OBDD's layer HashMap
	 */
	private HashMap<Integer, LinkedList<OBDD>> obddLayers;
	/**
	 * a HashMap for the OBDD's nodes' (identified by their IDs) positions 
	 * relative to the total size
	 */
	private HashMap<Integer, Pair<Double, Double>> positionMap;
	/**
	 * the size of each of the OBDD's nodes (width and height) relative to the 
	 * total height
	 */
	private double nodeSizeToHeight;
	
	
	
	/**
	 * constructor for an OBDD to be displayed for the first time
	 * @param obdd
	 */
	public AbstractObddLayout(OBDD obdd) {
		// retrieving the OBDD's layers
		obddLayers = obdd.getLayers();
		// the OBDD's number of layers (including the terminal layer)
		int numberOfLayers = obddLayers.size() + 1;
		// initializing a the current node's "position" 
		// (its layer and its position in that layer) 
		int currentLayerNumber = 0;
		int currentLayerPosition = 0;
		// variable for the nodes' vertical position
		double verticalPosition;
		// collecting the OBDD nodes from each layer
		for (int var : obddLayers.keySet()) {
			// the current layer
			LinkedList<OBDD> currentLayer = obddLayers.get(var);
			// the current layer's nodes' vertical position
			verticalPosition = 
					(1.5 + 3 * currentLayerNumber) / (3 * numberOfLayers);
			// iterator for the current layer list
			java.util.Iterator<OBDD> iter = currentLayer.iterator();
			// iterating over the layer list
			while (iter.hasNext()) {
				// the current node's horizontal position
				double horizontalPosition = (1.5 + 3 * currentLayerPosition) / 
						(3 * currentLayer.size());
				// storing the node's position in the position HashMap
				positionMap.put(iter.next().getId(), new Pair<Double, Double>
						(horizontalPosition, verticalPosition));
				// increasing the position inside the layer
				currentLayerPosition++;
			}
			// increasing the layer number and resetting the position inside 
			// the layer
			currentLayerNumber++;
			currentLayerPosition = 0;
		}
		// the terminals' vertical position
		verticalPosition = 1 - 1 / (3 * numberOfLayers);
		// defining the terminals' positions
		positionMap.put(1, new Pair<Double, Double>(0.25, verticalPosition));
		positionMap.put(0, new Pair<Double, Double>(0.75, verticalPosition));
		// One third of the visual OBDD's height is divided to the nodes.
		nodeSizeToHeight = ((1 / 3) / numberOfLayers);
	}

}
