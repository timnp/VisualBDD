package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JComponent;

/**
 * 
 * @author TimNP
 *
 */
public class VisualObdd extends JComponent{
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the OBDD's nodes' IDs
	 */
	private LinkedList<Integer> nodeIds;
	/**
	 * a HashMap with each node's top left corner's absolute positions
	 */
	private HashMap<Integer,Pair<Integer,Integer>> positionMap;
	/**
	 * each node's absolute size
	 */
	private int nodeSize;
	
	
	
	/**
	 * getter for the ID list
	 * @return
	 */
	public LinkedList<Integer> getNodeIds() {
		return nodeIds;
	}
	
	/**
	 * getter for the position map
	 * @return
	 */
	public HashMap<Integer,Pair<Integer,Integer>> getPositionMap() {
		return positionMap;
	}
	
	/**
	 * getter for the node size
	 * @return
	 */
	public int getNodeSize() {
		return nodeSize;
	}
	
	
	/**
	 * constructor for a visual OBDD
	 * @param layout - the AbstractObddLayout representing the OBDD
	 * @param panelSize - the OBDD panel's size
	 */
	public VisualObdd(AbstractObddLayout layout, Dimension panelSize) {
		// setting the preferred size to the panel's size
		setPreferredSize(panelSize);
		// splitting the panel size into width and height
		int panelWidth = panelSize.width;
		int panelHeight = panelSize.height;
		// calculating the absolute node size
		nodeSize = (int) (layout.getNodeSizeToHeight() * panelHeight);
		// the relative position HashMap
		HashMap<Integer,Pair<Double,Double>> relPositionMap = 
				layout.getPositionMap();
		// adding the OBDD's nodes
		for (int i : relPositionMap.keySet()) {
			// adding each node's absolute positions to the position map
			positionMap.put(i, absolutePositions(relPositionMap, panelWidth, 
					panelHeight, nodeSize, i));
			// adding each node's ID to the ID list
			nodeIds.add(i);
		}
	}
	
	
	/**
	 * auxiliary method to turn relative positions into absolute ones
	 * @param positionMap - HashMap with relative positions
	 * @param panelSize - the size of the panel
	 * @param nodeSize - the absolute node size
	 * @param id - the node's ID
	 * @return the node's absolute positions
	 */
	private Pair<Integer, Integer> absolutePositions(
			HashMap<Integer,Pair<Double,Double>> positionMap, 
			int panelWidth, int panelHeight, int nodeSize, int id) {
		// retrieving the node's relative positions
		Pair<Double,Double> relativePositions = positionMap.get(id);
		// calculating the absolute ones
		Integer horizontalPosition = (int) (relativePositions.getFirst() * 
				panelWidth - nodeSize / 2);
		Integer verticalPosition = (int) (relativePositions.getSecond() * 
				panelHeight - nodeSize / 2);
		return new Pair<Integer,Integer>(horizontalPosition,verticalPosition);
	}
	
	
	/**
	 * overriding the paintComponent method
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		// drawing all of the OBDD's nodes
		for (int i : nodeIds) {
			// getting the node's positions
			int horizontalPosition = positionMap.get(i).getFirst();
			int verticalPosition = positionMap.get(i).getSecond();
			if (i > 1) {
				// If the node isn't a terminal, it's represented by a circle.
				g.drawOval(horizontalPosition, verticalPosition, 
						nodeSize, nodeSize);
			}
			else {
				// If the node is a terminal, it's represented by a square.
				g.drawRect(horizontalPosition, verticalPosition, 
						nodeSize, nodeSize);
			}
		}
	}
}
