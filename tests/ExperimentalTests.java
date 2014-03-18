package tests;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.*;
import controller.*;
import view.*;

/**
 * 
 * @author TimNP
 *
 */
public class ExperimentalTests {
	
	public static void main(String[] args) {
		LinkedList<Integer> zeroToTen = new LinkedList<Integer>();
		for (int i = 0; i <= 10; i++) {
			zeroToTen.add(i);
		}
		System.out.println(zeroToTen.subList(4, 8));
	}

}
