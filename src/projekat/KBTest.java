package projekat;

import java.util.Scanner;

public class KBTest {

	public static void main(String[] args) {
		Bacon bacon = new Bacon("./src/movies.txt");
//		System.out.println(bacon.findBaconPath("Jackson, Greg (II)")); 
//		System.out.println(bacon.findBaconPath("Hopkins, Anthony")); 
//		System.out.println(bacon.findBaconPath("Clooney, George")); 
//		System.out.println(bacon.findBaconPath("Bacon, Kevin")); 
//		System.out.println(bacon.findBaconPath("Mitchell, Daryl")); 
//		System.out.println(bacon.findBaconPath("Chavez, Alejandro"));
		
		 Scanner input = new Scanner(System.in);
		 String name, lastname;
		 System.out.println("Unesite ime glumca: ");
	     name = input.next();

         System.out.println("Unesite prezime glumca: ");
         lastname = input.next();
         String fullname = lastname + ", " + name;
         System.out.println("Pretraživanje glumca: " + fullname);
         System.out.println(bacon.findBaconPath(fullname));
	}

}
