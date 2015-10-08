package stamboom.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    /**
     * Umbrella method that handles the messages that should be 
     * logged and the actions that check whether the logging file
     * exists and creates it.
     * @return returns the logged message.
     * @param header marks the type of the logged message
     * @param message containing information regarding user actions
     */
    public String logAction(String header, String message) {
        String result = header + ": " + message;
        
        if (!checkIfFolderExists()) {
            // TODO: create log folder
            createNewLogFolder();
        }
        
        if (!checkIfFileExists()) {
            // TODO: create log file
            createNewLogFile();
        }
        
        // TODO: write action to file
        if (writeLogActionToFile()) {            
            result = timeFormatting() + " " + result;
            return result;
        } else {
            result = "";
        }
        
        return result;
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
    
    /**
     * Checks whether the log folder exists.
     * @return whether the folder exists or not.
     */
    private boolean checkIfFolderExists() {
        return false;
    }
    
    /**
     * Creates a new log folder.
     * @return whether the new folder is created or not.
     */
    private boolean createNewLogFolder() {
        return false;
    }
    
    /**
     * Creates a Date for a log, based on the time that the
     * action is logged.
     * @return The time when the log is logged.
     */
    private String timeFormatting() {
        return null;
    } 
}
