package com.monstersoftwarellc.graphtastic.utility;

import java.util.Comparator;

public class ComparatorUtility {
	/**
	 * Comparator for {@link Long} to ensure our timestamps are correctly ordered.
	 * @return
	 */
	public static Comparator<Long> byTime(){
		return new TimestampComparator();
	}
	
	private static class TimestampComparator implements Comparator<Long>{

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Long o1, Long o2) {
			return o1.compareTo(o2);
		}
		
	}
}
