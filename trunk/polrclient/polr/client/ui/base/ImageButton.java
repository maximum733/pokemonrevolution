package polr.client.ui.base;

import org.newdawn.slick.Image;

public class ImageButton extends Button {
	public ImageButton(Image normal, Image hover, Image down) {
		super();
		setImage(normal);
		setRolloverImage(hover);
		setDownImage(down);
		setPadding(0);
		setOpaque(false);
	}

	public ImageButton() {
		super();
		setPadding(0);
		setOpaque(false);
	}
}
