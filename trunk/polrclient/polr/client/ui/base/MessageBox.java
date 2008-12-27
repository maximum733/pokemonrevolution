package polr.client.ui.base;

import polr.client.ui.base.Button;
import polr.client.ui.base.Container;
import polr.client.ui.base.Frame;
import polr.client.ui.base.Label;
import polr.client.ui.base.event.ActionEvent;
import polr.client.ui.base.event.ActionListener;

public class MessageBox extends Frame {
	public MessageBox(String message, Container container) {
		Container label = new Container();
		String[] lines = message.split("\n");

		int maxWidth = 0;
		int maxHeight = 0;

		for (String s : lines) {
			Label line = new Label(s);
			line.pack();

			int lineWidth = (int) line.getWidth();
			int lineHeight = (int) line.getHeight();

			if (lineWidth > maxWidth)
				maxWidth = lineWidth;

			line.setY(maxHeight);
			maxHeight += lineHeight;

			label.add(line);
		}
		label.setSize(maxWidth, maxHeight);

		Button ok = new Button();
		ok.setText("OK");
		getContentPane().add(label);
		getContentPane().add(ok);
		label.setLocation(5, 15);
		this.setResizable(false);
		this.setSize(label.getWidth() + 10, label.getHeight() + 90);
		ok.setSize(50, 25);
		ok.setLocation(this.getWidth() / 2 - 25, this.getHeight() - 60);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});
		this.setLocation((container.getDisplay().getWidth() / 2)
				- (this.getWidth() / 2),
				(container.getDisplay().getHeight() / 2)
						- (this.getHeight() / 2));
		this.setVisible(true);
		container.add(this);
		this.setAlwaysOnTop(true);
	}
}
