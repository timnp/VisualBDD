package model;

import java.util.HashMap;
import java.util.LinkedList;

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
	 * the abstract OBDD's total width and height "units"
	 */
	private double totalWidthUnits;
	private double totalHeightUnits;
	
	
	
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
	 * getter for the abstract OBDD's total (width and height) "units"
	 * @return
	 */
	public Pair<Double,Double> getTotalUnits() {
		return new Pair<Double,Double>(totalWidthUnits,totalHeightUnits);
	}
	
	
	/**
	 * constructor for an OBDD to be displayed for the first time
	 * @param obdd
	 */
	public AbstractObddLayout(OBDD obdd) {
		// setting the represented OBDD
		this.obdd = obdd;
		// initializing the position map
		positionMap = new HashMap<Integer, Pair<Double, Double>>();
		// aligning the nodes' positions
		alignNodes();
	}
	
	
	/**
	 * constructor for a new version of the given abstract OBDD layout
	 * @param previousVersion
	 */
	public AbstractObddLayout(AbstractObddLayout previousVersion) {
		// setting the OBDD
		obdd = previousVersion.obdd;
		// retrieving the previous version's position map
		HashMap<Integer, Pair<Double, Double>> previousPositionMap = 
				previousVersion.positionMap;
		// initializing a list for the OBDD's nodes' IDs 
		LinkedList<Integer> nodeIds = new LinkedList<Integer>();
		// retrieving all nodes' IDs from the previous version's position map
		nodeIds.addAll(previousPositionMap.keySet());
		// initializing the new position map
		positionMap = new HashMap<Integer, Pair<Double, Double>>();
		// creating a mapping for each node's ID equal to the previous 
		// version's mapping
		for (int id : nodeIds) 
			positionMap.put(id, previousPositionMap.get(id));
		// setting the node size
		nodeSizeToHeight = previousVersion.nodeSizeToHeight;
	}
	
	
	/**
	 * aligns the OBDD's nodes' positions and size to the used standard
	 */
	public void alignNodes() {
		// retrieving the OBDD's layers
		HashMap<Integer,LinkedList<OBDD>> obddLayers = obdd.getLayers();
		// The total height is divided into three times the number of layers 
		// (including the terminal layer) plus one units.
		totalHeightUnits = 3.0 * (obddLayers.size() + 1) + 1.0;
		// initializing the total width units with 2 (for the terminal layer)
		totalWidthUnits = 2;
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
			// The total width is divided into three times the number of nodes 
			// in that layer plus one units.
			double totalWidth = 3.0 * currentLayer.size() + 1.0;
			// setting the total width units to the maximum of its current 
			// value and this layer's total width (units)
			totalWidthUnits = Math.max(totalWidthUnits, totalWidth);
			// the current layer's nodes' vertical position
			verticalPosition = 
					(2.0 + 3.0 * currentLayerNumber) / totalHeightUnits;
			// iterator for the current layer list
			java.util.Iterator<OBDD> iter = currentLayer.iterator();
			// iterating over the layer list
			while (iter.hasNext()) {
				// the current node's horizontal position
				double horizontalPosition = (2.0 + 3.0 * currentLayerPosition) 
						/ totalWidth;
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
		verticalPosition = 1.0 - 2.0 / totalHeightUnits;
		// defining the terminals' positions
		positionMap.put(1, new Pair<Double,Double>(2.0/7.0, verticalPosition));
		positionMap.put(0, new Pair<Double,Double>(5.0/7.0, verticalPosition));
		// One third of the visual OBDD's height is divided to the nodes's 
		// size.
		nodeSizeToHeight = 1.0 / totalHeightUnits;
	}
	
	
	/**
	 * changes a node's horizontal position
	 * @param node
	 * @param horizontal position
	 */
	public void changeHorizontalPosition(Integer node, double horizontalPosition) {
		// changing the node's position
		positionMap.put(node, 
				new Pair<Double,Double>(horizontalPosition, 
						positionMap.get(node).getSecond()));
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
			if (obdd.getNode(id) == null) positionMap.remove(id);
	}
}
