package client.net;

public class tester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ServerConnection blah = new ServerConnection();
		ClientConnection connection = new ClientConnection(blah);
		
		char[] blah1 = new char[2];
		blah1[0] = 'b';
		connection.register("emailaddress123", blah1);
		
		

	}

}
