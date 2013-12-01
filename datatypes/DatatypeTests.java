package datatypes;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author TimNP
 * 
 * class for test cases
 *
 */
public class DatatypeTests {

	public static void main(String[] args) {
		LinkedList<String> varOrd = new LinkedList<String>();
		OBDD zero = new OBDD(false, varOrd);
		OBDD one = new OBDD(true, varOrd);
		OBDD a = new OBDD("Y", one, zero);
		OBDD b = new OBDD("Y", one, zero);
		OBDD c = new OBDD("X", a, b);
		HashMap<Pair<OBDD>, Boolean> cT = new HashMap<Pair<OBDD>, Boolean>();
		System.out.println(a.isEquivalent(b, cT));
	}

}
