/*
 *  Copyright 2010 chris.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

/*
 * DialogHeaderPanel2.java
 *
 * Created on 31-ene-2010, 12:06:53
 */

package org.gitools.ui.platform.dialog;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Icon;

public class DialogHeaderPanel extends javax.swing.JPanel {

	private static final Color INFO_COLOR = Color.WHITE;
	private static final Color WARN_COLOR = new Color(250, 250, 160);
	private static final Color ERROR_COLOR = new Color(250, 150, 150);
	private static final Color PROGRESS_COLOR = new Color(100, 180, 250);

	private MessageStatus messageStatus;
	private String rightLogoLink;

    public DialogHeaderPanel() {
        initComponents();

		title.setFont(title.getFont().deriveFont(Font.BOLD, 16));
		title.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		message.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		messageStatus = MessageStatus.INFO;
		leftLogo.setText("");
		leftLogo.setVisible(false);
		rightLogo.setText("");
    }

	public DialogHeaderPanel(
			String header,
			String message,
			MessageStatus status,
			Icon logo) {
		this();

		setTitle(header);
		setMessageStatus(status);
		setMessage(message);
		setRightLogo(logo);
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        message = new javax.swing.JLabel();
        rightLogo = new javax.swing.JLabel();
        leftLogo = new javax.swing.JLabel();

        setBackground(java.awt.Color.white);
        setFocusable(false);

        title.setText("Title");
        title.setFocusable(false);
        title.setRequestFocusEnabled(false);

        message.setBackground(java.awt.Color.white);
        message.setText("Message");
        message.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        message.setFocusable(false);
        message.setOpaque(true);
        message.setRequestFocusEnabled(false);
        message.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        rightLogo.setText("RightLogo");
        rightLogo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        rightLogo.setFocusable(false);
        rightLogo.setRequestFocusEnabled(false);
        rightLogo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        rightLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rightLogoMouseClicked(evt);
            }
        });

        leftLogo.setText("LeftLogo");
        leftLogo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        leftLogo.setFocusable(false);
        leftLogo.setRequestFocusEnabled(false);
        leftLogo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        leftLogo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                leftLogoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(leftLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rightLogo)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rightLogo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                    .addComponent(leftLogo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(title)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(message)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void rightLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rightLogoMouseClicked
		if (rightLogoLink != null) {
			try {
				Desktop.getDesktop().browse(
						new URL(rightLogoLink).toURI());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}//GEN-LAST:event_rightLogoMouseClicked

	private void leftLogoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leftLogoMouseClicked
		// TODO add your handling code here:
	}//GEN-LAST:event_leftLogoMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel leftLogo;
    private javax.swing.JLabel message;
    private javax.swing.JLabel rightLogo;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

	public String getTitle() {
		return title.getText();
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public Font getTitleFont() {
		return title.getFont();
	}

	public void setTitleFont(Font font) {
		title.setFont(font);
	}

	public Color getTitleColor() {
		return title.getForeground();
	}

	public void setTitleColor(Color color) {
		title.setForeground(color);
	}

	public String getMessage() {
		return message.getText();
	}

	public void setMessage(String message) {
		this.message.setText(message);
	}

	public MessageStatus getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(MessageStatus status) {
		this.messageStatus = status;
		//TODO status icon
		switch (status) {
			case INFO: message.setBackground(INFO_COLOR); break;
			case WARN: message.setBackground(WARN_COLOR); break;
			case ERROR: message.setBackground(ERROR_COLOR); break;
			case PROGRESS: message.setBackground(PROGRESS_COLOR); break;
		}
	}

	public Icon getRightLogo() {
		return rightLogo.getIcon();
	}

	public void setRightLogo(Icon logo) {
		this.rightLogo.setIcon(logo);
	}

	public String getRightLogoLink() {
		return rightLogoLink;
	}

	public void setRightLogoLink(String logoLink) {
		this.rightLogoLink = logoLink;
		if (logoLink == null)
			rightLogo.setCursor(Cursor.getDefaultCursor());
		else
			rightLogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public Icon getLeftLogo() {
		return leftLogo.getIcon();
	}

	public void setLeftLogo(Icon logo) {
		leftLogo.setIcon(logo);
	}

	public boolean isLeftLogoVisible() {
		return leftLogo.isVisible();
	}

	public void setLeftLogoVisible(boolean visible) {
		leftLogo.setVisible(visible);
	}
}
