// name of package is lab2
package lab2;


import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
/*
* Author Arsh Preet
*/
// This is my main class
public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner sc = new Scanner(System.in);
        String userID;
        String password;
        // This will print user id
        System.out.print("Enter User ID:");
        userID = sc.nextLine();
        // This will print user password
        System.out.print("Enter Password:");
        password = sc.nextLine();
        User user = new User(userID, password);

        System.out.println("Login : " + userID);
        System.out.print("Enter Password : ");
        password = sc.nextLine();
         
      
        // This will generate password 
        while (!PasswordGenerator.getSHA512Password(password, user.getSalt()).equals(user.getPassword())) {
            System.out.println("Wrong Password");
            System.out.print("Enter Password : ");
            password = sc.nextLine();
        }
        System.out.println("Logged in");
    }

}
