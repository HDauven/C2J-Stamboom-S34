package stamboom.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            createNewLogFolder();
        }
        
        if (!checkIfFileExists()) {
            createNewLogFile();
        }
        
        // TODO: write action to file
        if (writeLogActionToFile()) {            
            result = timeFormatting("HH:mm:ss") + " " + result;
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
        String executionPath = System.getProperty("user.dir");
        String logFilePath = executionPath.replace("\\", "/") + "/logs/" + timeFormatting("yyyy-mm-dd") + ".txt";
        File logFile = new File(logFilePath);
        if (logFile.exists() && logFile.isFile()) {
            return true;
        }
        return false;
    }
    
    /**
     * Creates a new log file based on todays date.
     * @return whether the new file is created or not.
     */
    private boolean createNewLogFile() {
        String executionPath = System.getProperty("user.dir");
        String logFilePath = executionPath.replace("\\", "/") + "/logs/" + timeFormatting("yyyy-mm-dd") + ".txt";
        File logFile = new File(logFilePath);
        boolean result = false;
        
        try {
            result = logFile.createNewFile();
        } catch (IOException ex) {
            System.err.println("SEVERE ERROR: Log file not created!");
            ex.printStackTrace();
        }        
        return result;
    }   
    
    /**
     * Checks whether the log folder exists.
     * @return whether the folder exists or not.
     */
    private boolean checkIfFolderExists() {
        String executionPath = System.getProperty("user.dir");
        String logFolderPath = executionPath.replace("\\", "/") + "/logs";
        File logFolder = new File(logFolderPath);
        if (logFolder.exists() && logFolder.isDirectory()) {
            return true;
        }
        return false;
    }
    
    /**
     * Creates a new log folder.
     * @return whether the new folder is created or not.
     */
    private boolean createNewLogFolder() {
        String executionPath = System.getProperty("user.dir");
        String logFolderPath = executionPath.replace("\\", "/") + "/logs";
        File logFolder = new File(logFolderPath);
        
        boolean result = false;
        result = logFolder.mkdirs();               
        return result;
    }
    
    /**
     * Creates a Date for a log, based on the time that the
     * action is logged.
     * @return The time when the log is logged.
     */
    private String timeFormatting(String format) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String time = "";
        time = formatter.format(now);
        return time;
    } 
}