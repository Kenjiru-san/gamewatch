package fxabc2;

import java.io.Serializable;

public class Player implements Serializable {
	String name;		//プレイヤーのなまえ
	int point;			//持っているポイント
	int life;			//持っているLife
	boolean isActive;	//動作可能かどうかのフラグ
	
	Player(String name, int point, 
			int life, boolean isActive){
		this.name = name;
		this.point = point;
		this.life = life;
		this.isActive = isActive;
	}

	public void init() {
		setname("none");
		setpoint(0);
		setlife(3);
		setisActive(true);
	}

	//getter&setter
	public String getname() {
		return name;
	}
	public void setname(String name) {
		this.name = name;
	}
	public int getpoint() {
		return point;
	}
	public void setpoint(int point) {
		this.point = point;
	}
	public int getlife() {
		return life;
	}
	public void setlife(int life) {
		this.life = life;
	}
	public boolean getisActive() {
		return isActive;
	}
	public void setisActive(boolean isActive) {
		this.isActive = isActive;
	}
}