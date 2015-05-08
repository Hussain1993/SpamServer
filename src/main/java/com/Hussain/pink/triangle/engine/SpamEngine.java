package com.Hussain.pink.triangle.engine;

import org.jasen.JasenScanner;
import org.jasen.error.JasenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Hussain on 08/05/2015.
 */
public class SpamEngine implements ServletContextListener{
    private static final Logger LOG = LoggerFactory.getLogger(SpamEngine.class);

    private static JasenScanner scanner;


    public void contextInitialized(ServletContextEvent sce) {
        scanner  = JasenScanner.getInstance();
        try{
            scanner.init();
            if(scanner.isAlive())
            {
                scanner.getEngine().getConfig().setAutoUpdateCheckOnStartup("false");
                scanner.getEngine().getConfig().setAutoUpdateEnabled("false");
                LOG.info("The Spam Engine has started");
            }
            else
            {
                throw new JasenException();
            }
        }
        catch (JasenException jasenException) {
            LOG.error("There was an error when trying to start up the spam engine",jasenException);
            scanner.destroy();//Clear any resources that might have been opened
        }

    }


    public void contextDestroyed(ServletContextEvent sce) {
        scanner.destroy();
        if(!scanner.isAlive())
        {
            LOG.info("The scanner has been destroyed");
        }
        else
        {
            LOG.error("The scanner has not been destroyed");
        }
    }

    public static JasenScanner getScanner() throws JasenException{
        if(scanner != null && scanner.isAlive())
        {
            return scanner;
        }
        throw new JasenException("There was an error when trying to retrieve the Jasen Scanner");
    }
}
