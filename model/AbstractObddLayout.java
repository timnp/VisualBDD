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
		// the OBDD's number of layers
		int numberOfLayers = obddLayers.size();
		// initializing a the current node's "position" 
		// (its layer and its position in that layer) 
		int currentLayerNumber = 0;
		int currentLayerPosition = 0;
		// collecting the OBDD nodes from each layer
		for (int var : obddLayers.keySet()) {
			// the current layer
			LinkedList<OBDD> currentLayer = obddLayers.get(var);
			// iterator for the current layer list
			java.util.Iterator<OBDD> iter = currentLayer.iterator();
			// iterating over the layer list
			while (iter.hasNext()) {
				// the current node's vertical position
				double verticalPosition = 
						(1 + 3 * currentLayerNumber) / numberOfLayers;
				// the current node's horizontal position
				double horizontalPosition = 
						(1 + 3 * currentLayerPosition) / currentLayer.size();
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
		// One third of the visual OBDD's height is divided to the nodes.
		nodeSizeToHeight = ((1 / 3) / numberOfLayers);
	}

}
