package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * 
 * @author TimNP
 *
 */
public class AbstractObddLayout {
	/**
	 * the represented OBDD
	 */
	private OBDD obdd;
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
	 * getter for the represented OBDD
	 * @return
	 */
	public OBDD getObdd() {
		return obdd;
	}
	
	/**
	 * getter for the OBDD's nodes's positions
	 * @return
	 */
	public HashMap<Integer, Pair<Double, Double>> getPositionMap() {
		return positionMap;
	}
	
	/**
	 * getter for the OBDD nodes' size
	 * @return
	 */
	public double getNodeSizeToHeight() {
		return nodeSizeToHeight;
	}
	
	
	/**
	 * constructor for an OBDD to be displayed for the first time
	 * @param obdd
	 */
	public AbstractObddLayout(OBDD obdd) {
		// setting the represented OBDD
		this.obdd = obdd;
		// retrieving the OBDD's layers
		HashMap<Integer,LinkedList<OBDD>> obddLayers = obdd.getLayers();
		// initializing the position map
		positionMap = new HashMap<Integer, Pair<Double, Double>>();
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
				double horizontalPosition = (1.5 + 3.0 * currentLayerPosition) / 
						(3.0 * currentLayer.size());
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
		verticalPosition = 1.0 - 1.0 / (3.0 * numberOfLayers);
		// defining the terminals' positions
		positionMap.put(1, new Pair<Double, Double>(0.25, verticalPosition));
		positionMap.put(0, new Pair<Double, Double>(0.75, verticalPosition));
		// One third of the visual OBDD's height is divided to the nodes.
		nodeSizeToHeight = (1.0 / (3.0 * numberOfLayers));
	}
	
	
	/**
	 * method that changes a node's position
	 * @param node
	 * @param pos
	 */
	public void changePosition(Integer node, Pair<Double, Double> pos) {
		positionMap.put(node, pos);
	}
	
	
	/**
	 * removes the node with the given ID from the position map and updates the
	 * represented OBDD to the given one
	 * @param id
	 * @param obdd
	 */
	public void removeNode(int id, OBDD obdd) {
		// removing the node ID from the position map
		positionMap.remove(id);
		// setting the OBDD
		this.obdd = obdd;
	}
	
	
	/**
	 * updates the represented OBDD to the given one and removes all mappings 
	 * for removed nodes from the position map
	 * @param obdd
	 */
	public void reduceObdd(OBDD obdd) {
		// setting the OBDD
		this.obdd = obdd;
		// retrieving all node IDs that are mapped in the current position map
		LinkedList<Integer> mappedNodeIds = new LinkedList<Integer>();
		mappedNodeIds.addAll(positionMap.keySet());
		// removing all mappings for removed nodes
		for (int id : mappedNodeIds) 
			// trying to get the node and then its ID in order to check whether
			// it's null and therefore not part of the OBDD
			try {obdd.getNode(id).getId();} 
			catch (NullPointerException e) {positionMap.remove(id);}
	}
}
