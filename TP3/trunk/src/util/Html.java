package util;

public class Html {

	/**
	 * Make a String safe for use in elements which will render HTML
	 * 
	 * @param s
	 * @return
	 */
	public static final String escape(String s) {
		return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll("\t", "    ").replaceAll(">", "&gt;")
				.replaceAll("  ", "&nbsp; ");
	}

}
