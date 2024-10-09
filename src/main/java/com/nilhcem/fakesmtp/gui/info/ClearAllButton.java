package com.nilhcem.fakesmtp.gui.info;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.nilhcem.fakesmtp.core.Configuration;
import com.nilhcem.fakesmtp.core.I18n;
import com.nilhcem.fakesmtp.server.MailSaver;
import com.nilhcem.fakesmtp.server.SMTPServerHandler;

/**
 * Button to clear all the information from the main panel.
 * <p>
 * The button will ask the user if he wants to delete the received emails or not.<br>
 * If yes, emails will be deleted from file system.
 * </p>
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class ClearAllButton extends Observable implements Observer {

	private static final I18n IL8N = I18n.INSTANCE;
	private final JButton button = new JButton(IL8N.get("clearall.button"));

	/**
	 * Creates the "clear all" button"
	 * <p>
	 * The button will be disabled by default, since no email is received when the application starts.<br>
	 * The button will display a confirmation dialog to know if it needs to delete the received emails or not.<br>
	 * If yes, emails will be deleted from the file system.
	 * </p>
	 */
	public ClearAllButton() {
	    button.setToolTipText(IL8N.get("clearall.tooltip"));
	    button.addActionListener((ActionEvent e) -> {
	        int answer = JOptionPane.showConfirmDialog(button.getParent(), IL8N.get("clearall.delete.email"),
	                String.format(IL8N.get("clearall.title"), Configuration.INSTANCE.get("application.name")),
	                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	        if (answer == JOptionPane.CLOSED_OPTION) {
	            return;
	        }

	        synchronized (SMTPServerHandler.INSTANCE.getMailSaver().getLock()) {
	            // Note: Should delete emails before calling observers, since observers will clean the model.
	            if (answer == JOptionPane.YES_OPTION) {
	                SMTPServerHandler.INSTANCE.getMailSaver().deleteEmails();
	            }
	            setChanged();
	            notifyObservers();
	            button.setEnabled(false);
	        }
	    });
	    button.setEnabled(false);
	}

	/**
	 * Returns the JButton object.
	 *
	 * @return the JButton object.
	 */
	public JButton get() {
		return button;
	}

	/**
	 * Enables the button, so that the user can clear/delete emails.
	 * <p>
	 * This method will be called by a {@link MailSaver} object when an email will be received.
	 * </p>
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof MailSaver && !button.isEnabled()) {
			button.setEnabled(true);
		}
	}
}
