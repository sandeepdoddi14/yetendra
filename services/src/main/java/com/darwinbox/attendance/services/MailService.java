package com.darwinbox.attendance.services;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailService {

    private static MailService obj = null;
    private Session session;
    private Store store;
    private IMAPFolder folder;

    private MailService() {

    }

    public Message[] getMails(String subject, String body) {

        Message[] messages = null;
        try {
            loginToImap("imap.gmail.com", "darwinbox.qa@gmail.com", "Dbox123456");
            messages = getMessages(subject, body);
        } catch (Exception e) {}

        return messages;

    }

    public static MailService getObject() {
        if (obj == null) {
            obj = new MailService();
        }
        return obj;
    }

    private boolean isLoggedIn() {
        if (store==null )
            return false;
        return store.isConnected();
    }

    private void loginToImap(String host, String username, String password) throws Exception {

        if (session != null || isLoggedIn())
            return;

        URLName url = new URLName("imaps", host, 993, "INBOX", username, password);
        session = Session.getInstance(new Properties(), null);

        store = session.getStore(url);
        store.connect();

        folder = (IMAPFolder) store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
    }

    private void logout() throws Exception {
        folder.close(false);
        store.close();
        store = null;
        session = null;
    }

    private Message[] getMessages(String subject, String body) throws Exception {

        SearchTerm searchTerm = new SearchTerm() {
            public boolean match(Message msg) {
                boolean match = false;
                try {
                    match = msg.getSubject().contains(subject) &&
                            !(msg.getFlags().contains(Flags.Flag.SEEN));
                    String content = msg.getContent().toString();
                    boolean bodyFlag = content.replaceAll("\\<.*?>","").trim().contains(body);
                    match = match && bodyFlag;

                } catch (Exception ex) {
                }
                return match;
            }
        };

        Message[] msgs = null;

        msgs = folder.search(searchTerm);
        folder.setFlags(msgs, new Flags(Flags.Flag.SEEN), true);
        return msgs;

    }

}

