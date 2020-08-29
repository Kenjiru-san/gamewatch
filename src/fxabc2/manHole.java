package fxabc2;

import javafx.scene.canvas.GraphicsContext;

public class manHole{
	private byte _plate = 0;	//If there is plate on the hole->1,
								//else plate is not on the hole->0. 
	
	public void init() {		//初期化
	}
	public void draw(GraphicsContext gc) {
	}
	public void update() {
	}

	public byte get_plate() {
		return _plate;
	}

	public void set_plate(byte _plate) {
		this._plate = _plate;
	}

}