
package lab2;

import java.security.NoSuchAlgorithmException;
/*
* Author Arsh Preet
*/
public class User {

    private String userID;
    private String Password;
    private byte[] salt;

    public User(String userID, String Password) throws NoSuchAlgorithmException {
        this.userID = userID;
        this.salt = PasswordGenerator.getSalt();
        this.Password = PasswordGenerator.getSHA512Password(Password, salt);
    }
/*
    This method will return User id
    */
    public String getUserID() {
        return userID;
    }
/*
    This method will set User id and if user id is empty then will through a exception called user is cannot be empty
    */
    public void setUserID(String userID) {
        if(userID.isEmpty())
        {
            throw new IllegalArgumentException("User id cannot be empty");
        }
        else
        {
        this.userID = userID;
        }
    }
 // This method return user password
    public String getPassword() {
        return Password;
    }
// This method set password for user and if it is less than 6 characters then through an exception
    public void setPassword(String Password) {
        if(Password.length()<6)
        {
            throw new IllegalArgumentException("Password must be of 6 or more characters");
        }
        else
        {
        this.Password = Password;
        }
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
    
}
