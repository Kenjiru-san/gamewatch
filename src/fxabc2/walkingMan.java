package fxabc2;

import static fxabc2.iterateN.*;//iterateNクラス内のstatic定数をインポート

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*walkingManクラスでは、
 * 上段、下段全部の箱の位置、人が入っているかどうかの変数を初期設定&
 * 実装するとともに、GraphicsContext上に人を描画する。
 * ＊＊＊＊また、穴位置に人が来た場合は、「落」文字を描画する。
 */

public class walkingMan{
	private int _line;//上段（UP）、下段（LW）の位置
	private int _pos;//箱の位置。1～12の数字。 
	private double _x;//箱の位置。GrahicsContext上のｘ座標。
	private boolean _isMan;//その人が居る（true）か、居ない（false）か。
	private Image[] _img = new Image[4];//色付き人の絵の配列、4つで1セット

	//箱の位置の初期化。人は入っていない状態に初期化。
	public void init(int l, int i) {
		set_line(l);//UPもしくはLW
		set_pos(i+1);//1～12の整数
		set_x(i*Space);//順番に間隔を開けて位置を設定
		set_isMan(false);//人は入っていない 
	}
	
	//箱の位置_posが先頭の場合は最後尾に回し、それ以外は（_pos,_x）を進める。
	public void update() {	
		if(get_line()==UP) {					//上段の場合で、かつ
			if(get_pos()==12) set_pos(1);		//先頭の場合、後ろに持ってくる
			else set_pos(get_pos()+1);			//先頭以外なら、前に進める
		}
		else {	if(get_pos()==1) set_pos(12);	//下段の場合で、かつ先頭の場合、後ろに持ってくる
				else set_pos(get_pos()-1);		//先頭以外なら、前に進める
		}
		set_x(get_pos()*Space);					//それぞれの_pos位置をもとに_xを設定
	}

	//箱に人が入っている場合に描画する・・・バージョン0：人を絵ではなく、文字で示す
	public void draw(GraphicsContext gc,Color clr) {
		gc.setFill(clr); // 色を設定（clr）
		gc.setFont(new Font("System",48)); // フォントサイズを設定
		if(get_line()==UP && get_isMan()==true) 
			gc.fillText("人", get_x(), UpperY);
		if(get_line()==LW && get_isMan()==true) 
			gc.fillText("人", get_x(), LowerY);
	}

	//箱に人が入っている場合に描画する・・・バージョン1：人を絵で示す
	public void drawman(GraphicsContext gc, int i) {
		switch(i%4) {
			case 0: set_img(imgN); 	break;
			case 1: set_img(imgB);	break;
			case 2: set_img(imgG);	break;
			case 3: set_img(imgR);	break;
			default: System.out.println("error!");
		}
		
		if(get_isMan()==true) {//人が箱に居る場合
			if(get_pos()%2==0) { //箱が偶数位置の場合
				if(get_line()==UP) //上段の場合
					gc.drawImage(_img[0], get_x(), UpperY-48);
				if(get_line()==LW) //下段の場合
					gc.drawImage(_img[3], get_x(), LowerY-48);
			}
			else {				//箱が奇数位置の場合
				if(get_line()==UP) //上段の場合
					gc.drawImage(_img[1], get_x(), UpperY-48);
				if(get_line()==LW) //下段の場合 
					gc.drawImage(_img[2], get_x(), LowerY-48);
			}
		}
		
		}
	
	//以下、GETTER/SETTER。
	public Image[] get_img() {
		return _img;
	}
	public void set_img(Image[] images) {
			_img = images;
	}
	public int get_line() {
		return _line;
	}
	public void set_line(int _line) {
		this._line = _line;
	}

	public int get_pos() {
		return _pos;
	}
	public void set_pos(int _pos) {
		this._pos = _pos;
	}

	public double get_x() {
		return _x;
	}
	public void set_x(double _x) {
		this._x = _x;
	}

	public boolean get_isMan() {
		return _isMan;
	}
	public void set_isMan(boolean _isMan) {
		this._isMan = _isMan;
	}
}