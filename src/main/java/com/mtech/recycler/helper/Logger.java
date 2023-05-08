package com.mtech.recycler.helper;

import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private final String filename = "log.txt";
    private static Logger uniqueInstance;
    private File logFile;

    public static synchronized Logger getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new Logger();
        }
        return uniqueInstance;
    }

    private Logger(){
        try {
            String path = new FileSystemResource("").getFile().getAbsolutePath();
            File logsDir = new File(path+"/logs");
            if (!logsDir.exists()) {
                logsDir.mkdir();
            }
            logFile = new File(logsDir, filename);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void info(String message)  {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(logFile, true));
            String fullmessage = getPrefix("INFO") + message;
            System.out.println(fullmessage);
            printWriter.println(fullmessage);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPrefix(String level) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String now = formatter.format(new Date());
        StackTraceElement caller = stackTrace[3];
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        int lineNumber = caller.getLineNumber();
        return now + "  " + level + " " + className + "." + methodName + "():" +  lineNumber + "\t"+ " : ";
    }


}
