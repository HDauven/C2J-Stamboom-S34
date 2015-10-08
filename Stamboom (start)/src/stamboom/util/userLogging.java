package stamboom.util;

/**
 * userLogging:
 * Class that logs user actions and exceptions caused by invalid
 * user actions.
 * 
 * @author Hein Dauven
 * @since 8-10-2015
 * @version 0.1
 */
public class userLogging {
    
    private String output;
    
    public userLogging() {
        
    }
    
    /**
     * Umbrella method that handles the messages that should be 
     * logged and the actions that check whether the logging file
     * exists and creates it.
     * @return returns the logged message.
     */
    public String logAction() {
        output = "";
        
        return output;
    }
    
    /**
     * Saves the user action to an existing logging file.
     * @return whether the user action is stored or not.
     */
    private boolean writeLogActionToFile() {
        return false;
    }
    
    /**
     * Checks whether the log file for today actually exists.
     * @return whether the file exists or not.
     */
    private boolean checkIfFileExists() {
        return false;
    }
    
    /**
     * Creates a new log file based on todays date.
     * @return whether the new file is created or not.
     */
    private boolean createNewLogFile() {
        return false;
    }    
}
