package util;

public class Html {

	public static final String escape(String s) {
		return s.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll("\t", "    ").replaceAll(">", "&gt;").replaceAll("  ", "&nbsp; ");
	}

}
