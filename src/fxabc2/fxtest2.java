package fxabc;


public class fxtest2{
	public static void main(String[] args){
		Chara ch = new Chara();
		ch.run();
		Neko neko = new Neko();
		neko.run();
		neko.fly();
	}
}
 
class Chara{
	void run(){
		System.out.println("走りません。");
	}
	void fly() {
		System.out.println("飛んだ！！");
	}
}
 
class Neko extends Chara{
	@Override
	void run(){
		System.out.println("猫は走りました。");
	}
}