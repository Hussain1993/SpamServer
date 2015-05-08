package com.Hussain.pink.triangle.scanner;

import com.Hussain.pink.triangle.engine.SpamEngine;
import org.jasen.JasenScanner;
import org.jasen.error.JasenException;
import org.jasen.interfaces.JasenScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by Hussain on 08/05/2015.
 */

@Path("/scan")
public class SpamScanner {
    private static final Logger LOG = LoggerFactory.getLogger(SpamScanner.class);

    @GET
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/email")
    public Response getProbability(InputStream emailInputStream){
        double probability = 0;
        try {
            MimeMessage mimeMessage = new MimeMessage(null,emailInputStream);
            JasenScanner scanner = SpamEngine.getScanner();
            JasenScanResult scanResult = scanner.scan(mimeMessage);
            if(scanResult.completed())
            {
                probability = scanResult.getProbability();
                return Response.ok(probability).build();
            }
            else
            {
                return Response.serverError().build();
            }
        }
        catch (MessagingException messagingException) {
            LOG.error("There was an error reading the email",messagingException);
            return Response.serverError().entity(messagingException.getMessage()).build();
        }
        catch (JasenException jasenException) {
            LOG.error("",jasenException);
            return Response.serverError().entity(jasenException.getMessage()).build();
        }
    }
}
