/**
 * A simple modal dialog for communicating a single message (or requesting an option) from the user.
 * Create an object of this class, then call the showMessageBox() function to display it. You can also
 * call go() if you are not interested in the response. 
 * 
 * The static function messageBox() can also be used to do it all in one step. 
 *
 * @author Gaurav Vaidya, gaurav@ggvaidya.com 
 */

/*
    TaxonDNA
    Copyright (C) 2005	Gaurav Vaidya

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

package com.ggvaidya.TaxonDNA.UI;

import java.awt.*;
import java.awt.event.*;

public class MessageBox extends Dialog implements ActionListener {
	/**
	 * Please remember to regenerate this number if MessageBox's structure
	 * changes! We're not actually serializable, but one of our parents is
	 * I think.
	 */
	private static final long serialVersionUID = -5572953576820003395L;
	private String		title = "";
	private String		message = "";
	private int		flags = 0;
	private int		clicked = 0;
	
	// constants
	// both for type and for return by show()
	/** The "OK" button was pressed */
	public static final int	MB_OK = 	0x0001;
	/** The "YES" button was pressed */		
	public static final int	MB_YES =	0x0002;
	/** The "NO" button was pressed */	
	public static final int	MB_NO =		0x0004;
	/** The "CANCEL" button was pressed */	
	public static final int	MB_CANCEL =	0x0008;

	/** Create an "simple" messageBox, with a single "OK" button. */
	public static final int	MB_SIMPLE = 	0x1000;
	/** Create an "error" messageBox, with a single "OK" button. */
	public static final int	MB_ERROR = 	0x1000;
	/** Create a yes/no messageBox */
	public static final int	MB_YESNO =	0x2000;
	/** Create a yes/no messageBox, with a single "CANCEL" button. */
	public static final int	MB_YESNOCANCEL= 0x4000;

	/**
	 * Creates a messagebox, with the given parent frame, title and message.
	 * The messagebox is an "Error" box, by default. 
	 */
	public MessageBox(Frame parent, String title, String message) {
		super(parent, title, true);	// we are ALWAYS modal
		this.title = title;
		this.message = message;
		flags = MB_OK | MB_ERROR;
	}

	/**
	 * Creates a messagebox, with the given parent frame, title, message
	 * and flags. The flags can be used to change the type of the message
	 * box, and/or the buttons displayed.
	 */
	public MessageBox(Frame parent, String title, String message, int flags) {
		this(parent, title, message);
		this.flags = flags;
	}
	
	/**
	 * I don't see why anybody would be interested, but okay ...
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Our internal draw function, which actually creates the messagebox when it
	 * needs to be.
	 */
	private void draw() {
		removeAll();

		setLayout(new BorderLayout());	

		TextArea ta = 	new TextArea(message, 5, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);
		ta.setEditable(false);
		add(ta);

		Panel buttons = new Panel();
		buttons.setLayout(new FlowLayout(FlowLayout.LEFT));

		Button btn;
		if((flags & MB_YESNO) != 0) {
			btn = new Button("   Yes   ");
			btn.addActionListener(this);
			buttons.add(btn);

			btn = new Button("   No    ");
			btn.addActionListener(this);
			buttons.add(btn);
		} else if(((flags & MB_OK) != 0) || ((flags & MB_SIMPLE) != 0)) {
			btn = new Button("   OK   ");
			btn.addActionListener(this);
			buttons.add(btn);
		} else if((flags & MB_YESNOCANCEL) != 0) {
			btn = new Button("   Yes   ");
			btn.addActionListener(this);
			buttons.add(btn);

			btn = new Button("   No    ");
			btn.addActionListener(this);
			buttons.add(btn);

			btn = new Button(" Cancel ");
			btn.addActionListener(this);
			buttons.add(btn);
		}
		add(buttons, BorderLayout.SOUTH);

		pack();
	}

	/**
	 * ActionListener for the buttons.
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand().trim();
		
		if(cmd.equals("OK")) {
			clicked = MB_OK;
			dispose();
		} else if(cmd.equals("Yes")) {
			clicked = MB_YES;
			dispose();
		} else if(cmd.equals("No")) {
			clicked = MB_NO;
			dispose();
		} else if(cmd.equals("Cancel")) {
			clicked = MB_CANCEL;
			dispose();
		}

	}

	/**
	 * Shows the messageBox.
	 * will return one of the MB_OK, MB_CANCEL, etc. constants
	 */
	public int showMessageBox() {
		draw();

		setVisible(true); // this will block until everything's done

		return clicked;
	}

	/**
	 * If you don't need to retrieve the clicked button,
	 * you can use this instead.
	 */
	public void go() {
		showMessageBox();
	}

	/**
	 * Creates a messagebox, displays it, and returns the result.
	 */
	public static int messageBox(Frame parent, String title, String message, int flags) {
		MessageBox mb = new MessageBox(parent, title, message, flags);
		return mb.showMessageBox();
	}

	/**
	 * Creates an error messagebox and displays it.
	 */
	public static void messageBox(Frame parent, String title, String message) {
		MessageBox mb = new MessageBox(parent, title, message, MB_SIMPLE);
		mb.showMessageBox();
	}
	
	/*
	 * Dummy test 
	public static void main(String args[]) {
		String message =
			"I've got a word or two,\nTo say about the things that you do\nYou telling all those lies\nAbout the good things that we can have if we close our eyes\nDo what you want to do, go where you're going to, think for yourself for I'm not going to be there with you";
		Frame 	frame = new Frame("Test");
		frame.setSize(200, 200);
		frame.show();
		MessageBox mb	=	new MessageBox(frame, "Test", message);
		System.out.println(mb.showMessageBox());
	}
	*/
}	