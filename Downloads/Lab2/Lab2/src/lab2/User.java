
package lab2;

import java.security.NoSuchAlgorithmException;

public class User {

    private String userID;
    private String Password;
    private byte[] salt;

    public User(String userID, String Password) throws NoSuchAlgorithmException {
        this.userID = userID;
        this.salt = PasswordGenerator.getSalt();
        this.Password = PasswordGenerator.getSHA512Password(Password, salt);
    }
// this method return user id
    public String getUserID() {
        return userID;
    }
// this method set user id if this is empty through an exception
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
// This method return password
    public String getPassword() {
        return Password;
    }
// This method set password 
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
