package com.tracking.m2comsys.adapplication.extras;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tracking.m2comsys.adapplication.Database.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Belal on 10/30/2015.
 */

//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void,Void,Void> {

    //Declaring Variables
    private Context context;
    private Session session;
    //private Multipart _multipart = new MimeMultipart();
    //Information to send email
    private String email;
    private String subject;
    private String message;
    private String filename;
    DataBaseHelper dataBaseHelper=new DataBaseHelper(context);

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String email, String subject, String message, String filename){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.filename=filename;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog

        //Showing a success message
        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(Config.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);


//            Multipart _multipart = new MimeMultipart();
//            BodyPart messageBodyPart = new MimeBodyPart();
//            DataSource source = new FileDataSource(filename);
//
//            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName(filename);
//
//            _multipart.addBodyPart(messageBodyPart);
//            mm.setContent(_multipart);
            Multipart _multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();

            DataSource source = new FileDataSource(filename);

            messageBodyPart.setDataHandler(new DataHandler(source));
            Date todayDate = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String todayString = formatter.format(todayDate);
            messageBodyPart.setFileName("download-Rockchip "+todayString);



            _multipart.addBodyPart(messageBodyPart);
            mm.setContent(_multipart,"text/plain");

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
