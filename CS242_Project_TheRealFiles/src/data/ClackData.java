package data;
import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;
import java.io.File;

public abstract class ClackData implements Serializable {
    public static final int CONSTANT_LISTUSERS = 0;
    public static final int CONSTANT_LOGOUT = 1;
    public static final int CONSTANT_SENDMESSAGE = 2;
    public static final int CONSTANT_SENDFILE = 3;

    protected String userName;
    protected int type;
    protected Date date;


    public ClackData(String userName, int type) {
        this.userName = userName;
        this.type = type;
        this.date = new Date();
    }
    public ClackData(int type){
        this("Anon", type);
    }

    public ClackData(){
        this("Anon", 5);
    }


    // public ClackData() {
    //this(CONSTANT_LOGOUT);
    //}

    public int getType(){
        return this.type;
    }
    public String getUserName(){
        return this.userName;
    }
    public Date getDate(){
        return this.date;
    }
    public abstract String getData();

    public abstract String getData(String key);


//    public String encrypt(String inputStringToEncrypt, String key){
//// Creates variables for lengths of string and key
//        int stringLength = inputStringToEncrypt.length();
//       final int keyLength = key.length();
//// Creates arrays to convert string and key to char arrays
//        char[] charArray = new char[stringLength];
//        char[] keyArray = new char[keyLength];
//// Fills arays with string and key as chars
//        keyArray = key.toCharArray();
//        charArray = inputStringToEncrypt.toCharArray();
//// Makes arrays for int values of string and key chars
//        int[] charAsNumArray = new int[stringLength];
//        int[] keyAsNumArray = new int[stringLength];
//// Makes an array filled with repeating key
//        char[] fullKeyCharArray = new char[stringLength];
//        int idx = 0;
//        for(int i = 0; i < stringLength; i++){
//            if(idx+1 > keyLength)
//                idx = 0;
//            fullKeyCharArray[i] = keyArray[idx];
//            keyAsNumArray[i] = (int)fullKeyCharArray[i];
//            idx++;
//        }
//// Adds int value of key array to int value of string array
//        char[] encryptedCharArray = new char[stringLength];
//        for(int i = 0; i < stringLength; i++){
//            if((int)charArray[i] >= 65 && (int)charArray[i] <= 90)
//                charAsNumArray[i] = (( (int)charArray[i] + keyAsNumArray[i] + 1 ) % 26) + 'A';
//            else if((int)charArray[i] >= 97 && (int)charArray[i] <= 122 )
//                charAsNumArray[i] = (( (int)charArray[i] + keyAsNumArray[i] + 1 ) % 26) + 'a';
//            else
//                charAsNumArray[i] =(int)charArray[i];
//            encryptedCharArray[i] = (char)charAsNumArray[i];
//        }
//        String encryptedString = String.valueOf(encryptedCharArray);
//        return encryptedString;
//    }
public String encrypt(String inputStringToEncrypt, String key) {
    if (inputStringToEncrypt == null) {
        return null;
    }

    final int keyLen = key.length();
    int keyIndex = 0;
    StringBuilder stringEncrypted = new StringBuilder();

    for (int i = 0; i < inputStringToEncrypt.length(); i++) {
        char inputCharToEncrypt = inputStringToEncrypt.charAt(i);
        char inputCharEncrypted;

        if (Character.isLowerCase(inputCharToEncrypt)) {
            char keyChar = Character.toLowerCase(key.charAt(keyIndex));
            inputCharEncrypted = (char) (((inputCharToEncrypt - 'a') + (keyChar - 'a')) % 26 + 'a');
            keyIndex = (keyIndex + 1) % keyLen;

        } else if (Character.isUpperCase(inputCharToEncrypt)) {
            char keyChar = Character.toUpperCase(key.charAt(keyIndex));
            inputCharEncrypted = (char) (((inputCharToEncrypt - 'A') + (keyChar - 'A')) % 26 + 'A');
            keyIndex = (keyIndex + 1) % keyLen;

        } else {
            inputCharEncrypted = inputCharToEncrypt;
        }

        stringEncrypted.append(inputCharEncrypted);
    }

    return stringEncrypted.toString();
}

//    public String decrypt(String inputStringToDecrypt, String key){
//// Creates variables for lengths of string and key
//        int stringLength = inputStringToDecrypt.length();
//        int keyLength = key.length();
//// Creates arrays to convert string and key to char arrays
//        char[] charArray = new char[stringLength];
//        char[] keyArray = new char[keyLength];
//// Fills arays with string and key as chars
//        keyArray = key.toCharArray();
//        charArray = inputStringToDecrypt.toCharArray();
//// Makes arrays for int values of string and key chars
//        int[] charAsNumArray = new int[stringLength];
//        int[] keyAsNumArray = new int[stringLength];
//// Makes an array filled with repeating key
//        char[] fullKeyCharArray = new char[stringLength];
//        int idx = 0;
//        for(int i = 0; i < stringLength; i++) {
//            if (idx + 1 > keyLength)
//                idx = 0;
//            fullKeyCharArray[i] = keyArray[idx];
//            keyAsNumArray[i] = (int) fullKeyCharArray[i];
//            idx++;
//        }
//// Adds int value of key array to int value of string array
//        char[] decryptedCharArray = new char[stringLength];
//        for(int i = 0; i < stringLength; i++){
//            if((int)charArray[i] >= 65 && (int)charArray[i] <= 90)
//                charAsNumArray[i] = (( (int)charArray[i] - keyAsNumArray[i] - 1 ) % 26) + 'A';
//            else if ((int)charArray[i] >= 97 && (int)charArray[i] <= 122)
//                charAsNumArray[i] = (( (int)charArray[i] - keyAsNumArray[i] - 13 ) % 26) + 'a';
//
//            else
//                charAsNumArray[i] =(int)charArray[i];
//            decryptedCharArray[i] = (char)charAsNumArray[i];
//        }
//        String decryptedString = String.valueOf(decryptedCharArray);
//        return decryptedString;
//    }

    public String decrypt(String inputStringToDecrypt, String key) {
        if (inputStringToDecrypt == null) {
            return null;
        }

        final int keyLen = key.length();
        int keyIndex = 0;
        StringBuilder stringDecrypted = new StringBuilder();

        for (int i = 0; i < inputStringToDecrypt.length(); i++) {
            char inputCharToDecrypt = inputStringToDecrypt.charAt(i);
            char inputCharDecrypted;

            if (Character.isLowerCase(inputCharToDecrypt)) {
                char keyChar = Character.toLowerCase(key.charAt(keyIndex));
                inputCharDecrypted = (char) ((inputCharToDecrypt - keyChar + 26) % 26 + 'a');
                keyIndex = (keyIndex + 1) % keyLen;

            } else if (Character.isUpperCase(inputCharToDecrypt)) {
                char keyChar = Character.toUpperCase(key.charAt(keyIndex));
                inputCharDecrypted = (char) ((inputCharToDecrypt - keyChar + 26) % 26 + 'A');
                keyIndex = (keyIndex + 1) % keyLen;

            } else {
                inputCharDecrypted = inputCharToDecrypt;
            }

            stringDecrypted.append(inputCharDecrypted);
        }

        return stringDecrypted.toString();
    }

}


