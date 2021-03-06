package fxabc2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// このクラスの中でwkm:walkingManとmanHoleの初期化と描画を実施する
/*
 * 上段、下段があり、上段は以下のとおり。
 * 表示されるのは、1～12の位置
 * 0番から11番の12個の箱（表示しない）があり、上段では順に左から右に箱が流れ、位置が
 * 移動するが、位置12の次は位置1に戻る。
 * 箱には人が入っている場合と入っていない場合があり、箱を位置1に
 * セットした後に、人が入るかどうかを乱数で50%の確率で決める。
 * ただし、マンホールの穴が５つ間隔で開いているので、５つ先の箱に人が入っている場合には
 * 人が入らない。
 * 下段の0番から11番の12個の箱は、順に右から左に流れ、位置が移動していくが、位置1の次は
 * 位置12に戻る。 他は上段と同じ。
 * 上段→下段→上段→下段→・・・のように上段の箱が移動した後に下段の箱が移動し、上段、下段は
 * 同時には移動しない。
 */

public class iterateN extends MainAppli{
	static final int WIDTH 	= 640;
	static final int HEIGHT = 480;
	static final int FPS 	= 1; //Frame per Second,1秒当たりのフレーム数
	static final int numOfBox 	= 12;//箱の数
	static final int numOfHole 	= 4;//マンホールの穴の数
	static final byte LU = 1;//マンホールのフタの位置Left Upper
	static final byte RU = 2;//マンホールのフタの位置Right Upper
	static final byte LL = 3;//マンホールのフタの位置Left Lower
	static final byte RL = 4;//マンホールのフタの位置Right Lower
	static final double LX = 48*4;//左側マンホールのx座標
	static final double RX = 48*9;//右側マンホールのx座標
	
	static final double UpperY	= 144.0;//上段のy座標
	static final double LowerY 	= 288.0;//下段のy座標
	static final double Space	= 48.0;	//x方向の箱と箱の間隔
	static final int UP = 0;	//上段を示すサフィクス
	static final int LW = 1;	//下段を示すサフィクス

	static final byte OK = 1;	//OKだった時の_dopon
	static final byte NG = 0;	//NGだった時の_dopon
	static final byte NA = 2;	//マンホールに来ていない時の_dopon
	
//	static final String SndWalk = "footstep02.wav";//歩く音from魔王魂
	static final String SndwalkUP = "pi.wav";//上段歩く音、電子音
	static final String SndwalkLW = "po.wav";//下段歩く音、電子音
	static final String SndDopon = "waterdopon.wav";//水に落ちる音
	static final String SndOk = "ok.wav";//マンホールを踏んだ音
	static final String SndCong = "congrat.wav";//おめでとう音
	
	//人のImageクラスサフィクス0と1が上段（右向き）、2と3が下段（左向き）の絵
	static Image[] imgN = new Image[4];//黒の人のImageクラス
	static Image[] imgB = new Image[4];//青の人のImageクラス
	static Image[] imgG = new Image[4];//緑の人のImageクラス
	static Image[] imgR = new Image[4];//赤の人のImageクラス
	
	static final Image imgUP = new Image("/UPdopon.png", false);//上段の人が落ちた時の絵
	static final Image imgLW = new Image("/LWdopon.png", false);//下段の人が落ちた時の絵

	private walkingMan[][] _wm = new walkingMan[2][numOfBox];
	private byte _mh;//マンホールのフタの位置記号
	private double _mhx;//マンホールのフタのx座標
	private double _mhy;//マンホールのフタのy座標
	private int _cnter;//上段、下段の箱を順に動かすためのカウンタ
	private Color _clr;//人の文字色
	private String _st2;
	
	private int _point = 0;//点数。助けたら、10点プラス。
	private int _life = 3;//寿命。最初は3。マンホールに落ちたら、マイナス1。ゼロになったら終了。

	private Player player = new Player("none",0,0,true) ;//プレイヤーのコンストラクタ：名前name、ポイントpoint、Life、停止中フラグisStopping
	static List<Player> Top10 = new ArrayList<>();//Top10のPointゲッターの履歴
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override 
	public void init() {//初期化メソッド
		setScWidth(WIDTH);
		setScHeight(HEIGHT);
		setBackColor(Color.BEIGE);
		setCvWidth(WIDTH);
		setCvHeight(HEIGHT);
		setFps(60);
		setIsDaemon(true);
		
		for(int i = 0; i < numOfBox; i++) {
			setwkm(UP, i, new walkingMan()); //上段_wmのインスタンス化
			setwkm(LW, i, new walkingMan()); //下段_wmのインスタンス化
			getwkm(UP, i).init(UP, i);		// _wmの初期化
			getwkm(LW, i).init(LW, i);		// _wmの初期化
		}
		set_mh(LU);				// manHoleの初期化・・最初は左上にフタを置いた
		set_cnter(UP);			// 上段、下段を示すカウンタ_cnterの初期化・・最初は上段
		
		//人のImageコンストラクタ設定、i=0:上段その１、i=1：上段その２、i=2：下段その１、i=3：下段その２
		for (int i=0; i<4; i++)
			imgN[i] = new Image("/walk" + (i+1) + ".png", false);//黒の人の設定
		for (int i=0; i<4; i++)
			imgB[i] = new Image("/walkb" + (i+1) + ".png", false);//青の人の設定
		for (int i=0; i<4; i++)
			imgG[i] = new Image("/walkg" + (i+1) + ".png", false);//緑の人の設定
		for (int i=0; i<4; i++)
			imgR[i] = new Image("/walkr" + (i+1) + ".png", false);//赤の人の設定
	
	}
	static void SoundCong() {		//おめでとう音
		soundc snd = new soundc();
		snd.str = SndCong;
		Thread th = new Thread(snd);
		th.start();
	}	
	
	static void SoundOk() {		//マンホールを踏んだ音
		sound snd = new sound();
		snd.str = SndOk;
		Thread th = new Thread(snd);
		th.start();
	}	
	static void SoundDopon() {	//水に落ちる音
		sound snd = new sound();
		snd.str = SndDopon;
		Thread th = new Thread(snd);
		th.start();
	}
	
	static void SoundWalkUP() {	//歩く音、上段
		sound snd = new sound();
		snd.str = SndwalkUP;
		Thread th = new Thread(snd);
		th.start();
	}

	static void SoundWalkLW() {	//歩く音、下段
		sound snd = new sound();
		snd.str = SndwalkLW;
		Thread th = new Thread(snd);
		th.start();
	}

	@Override 
	protected void ofMain(GraphicsContext gc, Player plyr) {
		player = plyr;
		gc.clearRect(0, 0, WIDTH, HEIGHT);	// 全画面をクリア
		
		//箱の位置を進める＆一番後ろの箱に50%確率で人を入れる
		int j;

		//上段の場合
		if(get_cnter()==UP) {

			SoundWalkUP();//歩く音、上段
			
			for(int i=0; i<numOfBox; i++) {
				getwkm(UP,i).update();	//箱の位置を進める
 
				//先頭の箱に人を入れるかどうか、以下で決める。
				if(getwkm(UP,i).get_pos()==1) {//一番後ろの箱の場合、
					if(i<7) j=i+5; else j=i+5-12;//ｊ＝5個先の箱番号
					if(getwkm(UP,j).get_isMan()) {//5個先の箱に人が入っている時
						getwkm(UP,i).set_isMan(false);//人を入れない
					}
					else {				//5個先の箱に人が居ない時
						if(Math.random()<0.5)
							getwkm(UP,i).set_isMan(false);
						else 
							getwkm(UP,i).set_isMan(true);//50%の確率で人を入れる
					}
				}

				//マンホール穴の上に居るかどうか、落ちたかどうか判定
				if ( getwkm(UP,i).get_pos()==4 //マンホール穴位置(4)の箱に
						&& getwkm(UP,i).get_isMan()) {//人が居る。
					
					//落ちた場合の処理
					if (get_mh()!=LU) { 		//フタがLUに無い場合、落ちる
						dopon(gc);//落ちる音＋_lifeを減らす
						getwkm(UP,i).set_dopon(NG);//落ちたﾌﾗｸﾞを立てる
					}

					//フタがあって、OKだった場合の処理
					else {
						OK();
						getwkm(UP,i).set_dopon(OK);//OKﾌﾗｸﾞを立てる
					}
				}
					
				if ( getwkm(UP,i).get_pos()==9 //マンホール穴位置(9)の箱に
						&& getwkm(UP,i).get_isMan()) {//人が居る。
					
					//落ちた場合の処理
					if (get_mh()!=RU) {		//フタがRUに無い場合、落ちる
						dopon(gc);//落ちる音＋_lifeを減らす
						getwkm(UP,i).set_dopon(NG);//落ちたﾌﾗｸﾞを立てる
					}
					
					//フタがあって、OKだった場合の処理
					else {	
						OK();
						getwkm(UP,i).set_dopon(OK);//OKﾌﾗｸﾞを立てる
					}
				}
			}
			set_cnter(LW);
		}
			
		//下段の場合
		else {
			SoundWalkLW();	//歩く音、

			for(int i=0; i<numOfBox; i++) {
				getwkm(LW,i).update();	//下段の箱の位置を進める
				if(getwkm(LW,i).get_pos()==12) {	//一番後ろの箱の場合、
					if(i<5) j=i-5+12; else j=i-5;
					if(getwkm(LW,j).get_isMan()) 	//5個先の箱に人が入っている時
						getwkm(LW,i).set_isMan(false);//人を入れない
					else {							//5個先の箱に人が居ない時
						if(Math.random()<0.5)getwkm(LW,i).set_isMan(false);
						else getwkm(LW,i).set_isMan(true);//50%の確率で人を入れる
					}
				}
				//マンホール穴の上に居るかどうか、落ちるかどうか判断。
				if ( getwkm(LW,i).get_pos()==4 //マンホール穴位置(4)の箱に
						&& getwkm(LW,i).get_isMan()) {//人が居る。
					
					//落ちた場合の処理
					if (get_mh()!=LL) { 		//フタがLLに無い場合、落ちる
						dopon(gc);//落ちる音＋_lifeを減らす
						getwkm(LW,i).set_dopon(NG);//落ちたﾌﾗｸﾞを立てる
					}

					//フタがあって、OKだった場合の処理
					else {
						OK();
						getwkm(LW,i).set_dopon(OK);//OKﾌﾗｸﾞを立てる
					}
				}
					
				if ( getwkm(LW,i).get_pos()==9 //マンホール穴位置(9)の箱に
						&& getwkm(LW,i).get_isMan()) {//人が居る。
					
					//落ちた場合の処理
					if (get_mh()!=RL) { 	//フタがRLに無い場合、落ちる
						dopon(gc);//落ちる音＋_lifeを減らす
						getwkm(LW,i).set_dopon(NG);//落ちたﾌﾗｸﾞを立てる
					}
					
					//フタがあって、OKだった場合の処理
					else {
						OK();
						getwkm(LW,i).set_dopon(OK);//OKﾌﾗｸﾞを立てる
					}
				}
			}
			set_cnter(UP);
		}
		
		//人と道路を描画
		for(int i=0;i<numOfBox;i++) {
			
			//人を描画
			getwkm(UP,i).drawman(gc,i);//上段の箱に人が入っている場合に描画：Ver1
			getwkm(LW,i).drawman(gc,i);//下段の箱に人が入っている場合に描画：Ver1

			//穴の開いた道路を描画
			gc.setFill(Color.BLACK); 	// 色を設定（黒）
			gc.setFont(new Font("System",48)); 	// フォント型、サイズを設定
			j=i+1;						//位置1～12
			if(i!=3 && i!=8) {
				gc.fillText("＿", (double)j*Space, UpperY);//上段の道路を描画
				gc.fillText("＿", (double)j*Space, LowerY);//下段の道路を描画
			}
		}
		
		//マンホールのフタを描画
		gc.setFill(Color.RED);// 色を設定（緑色）
		gc.setFont(new Font("System Bold",48)); 	// フォントの型、サイズを設定
		gc.fillText("＿", get_mhx(), get_mhy());		// フタを描画

		//LifeとPointを書く
		gc.setFill(Color.PURPLE);// 色を設定（緑色）
		gc.setFont(new Font("System Bold",36)); 	// フォントの型、サイズを設定
		gc.fillText(player.name, 10, 350);
		gc.setFill(Color.RED);// 色を設定（赤）
		gc.fillText("Life: "+get_life()+"    Point: "+get_point(), 10, 400);
		
		// Life<=0になると、Game Over
		if (player.life <= 0) {	
			System.out.println("life = 0!");
			
			Top10ToFile(player);		//playerを含めてBest10を計算しなおしてファイルに書き込み			
			player.isActive = false;	//プレイヤは動作不可

			//1位のとき
			if( player.getpoint() >= Top10.get(0).getpoint() ) {
				SoundCong();									//おめでたい音
				gc.setFill(Color.DARKSALMON);					// 文字色を設定（赤）
				gc.setFont(new Font("System Bold",48)); 		// フォントの型、サイズを設定
				gc.fillText("You are Number One! Congratulations!", 10, 70);

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			String st1 = Endroll(player);

			set_st2("");
			List<Player> sbList = Top10.subList(0, 3);
			int num = 1 ;
			for(Player p: sbList) {
				set_st2( get_st2() + "\r\n  " + "Top" + num + "  " + p.name + " : " + p.point + "point");
				num++;
			}
			
			//アニメーションタイマーで順位と
			AnimationTimer timer = new AnimationTimer() {
				//AnimationTimer定義直下でGC描画を記載できない。
				int x = 400;

				@Override    // 1/60FPS毎に呼び出されるメソッド handle(now) の実装
				public void handle(long now) { 			// now (ナノ秒単位) から現在時刻を抽出
					gc.clearRect(0,0,WIDTH,HEIGHT);			//画面消去
					gc.setFill(Color.DARKSALMON);		// 色を設定（ダークサーモン）
					gc.setFont(new Font("System Bold",65));// フォントの型、サイズを設定

					//"You won the game!"を右から左に送る
					gc.fillText(st1, x, 100);	
					if(x==-600) x=400;	else x=x-2;

					gc.setFill(Color.NAVY);		// 色を設定（ダークサーモン）
					gc.setFont(new Font("System Bold",36));// フォントの型、サイズを設定
					gc.fillText(get_st2(), 10, 200);

					gc.setFill(Color.DARKSALMON);// 色を設定（赤）
					gc.setFont(new Font("System Bold",36)); 	// フォントの型、サイズを設定
					gc.fillText("Game Over! Press Enter Key.", 10, 450);
}	
			};
			
			//AnimationTimerを起動
			timer.start();
		}
	}
	
	//エンドロールの文字を返す
	public String Endroll(Player plyer) {
		String edroll = "";

		if (plyer.getpoint() < Top10.get(9).getpoint()) edroll = "";//11位以下
		else {
			if(plyer.getpoint() == Top10.get(0).getpoint()) edroll = "You are Number One!";//1位
			else {if(plyer.getpoint() == Top10.get(1).getpoint()) edroll = "You are Second!";//2位
				else {if(plyer.getpoint() == Top10.get(2).getpoint()) edroll = "You are in 3rd place!";
					else for(int i=3;i<10;i++) {
						if(plyer.getpoint() == Top10.get(i).getpoint()) {
							edroll = "You are in " +(i+1) +"th place!";
						}
					}
				}
			}
		}
		return edroll;
	}
	
	//ポイントTop10をファイルから読み込み、playerを含めてTop10を計算しなおしてファイルに書き込み
	public void Top10ToFile(Player plyer) {
		
		Top10FromFile();//ポイントTop10をファイルから読み込み
		//fileから読み込んだ値を表示
		System.out.println("From file:top10.dat");
		Top10.forEach(p -> 
			System.out.println(p.getname()+ ": "+ p.point));

		//Top10の最下位とPlayer.pointが同じなら、Playerを優先して10位にする
		//そうでなければ、ふつうにソートする
		if(Top10.get(9).point==plyer.point) {
			Top10.remove(9);//10位を削除
			Top10.add(plyer);//代わりにplyerを10位として追加
		}
		else {
			Top10.add(plyer);//Top10にplayerを追加
			Comparator<Player> playerComparator =
			       Comparator.comparing(Player::getpoint)
			                  .reversed()
			                  .thenComparing(Comparator.comparing(Player::getname));
			List<Player> sortedPlayers = Top10.stream()
			                  .sorted(playerComparator)
			                  .collect(Collectors.toList());
			sortedPlayers.remove(10);//11位を削除
			Top10.clear();
			Top10 = sortedPlayers;
		}
		
		//Top10をtop10.datに出力
		try(FileOutputStream f = new FileOutputStream("top10.dat");
				BufferedOutputStream b = new BufferedOutputStream(f);
				ObjectOutputStream out = new ObjectOutputStream(b)){
			out.writeObject(Top10);
			System.out.println("Top10：");
			Top10.forEach(p -> 
				System.out.println(p.getname()+ ": "+ p.point));

			out.close();b.close();f.close();
			
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public static void Top10FromFile() {	//過去のTop10をFileから読み込み
		
		File file = new File("top10.dat");
		if(file.exists()) {
			try(FileInputStream f = new FileInputStream("top10.dat");
					BufferedInputStream b = new BufferedInputStream(f);
					ObjectInputStream in = new ObjectInputStream(b)){
					Top10 = (List<Player>) in.readObject();

					in.close();b.close();f.close();
					
			} catch ( IOException e ) {
					e.printStackTrace();
			} catch (ClassNotFoundException e) {
					e.printStackTrace();
			}
		}
		else {
			Top10init();
			try(FileOutputStream f = new FileOutputStream("top10.dat");
					BufferedOutputStream b = new BufferedOutputStream(f);
					ObjectOutputStream out = new ObjectOutputStream(b)){
					out.writeObject(Top10);

					out.close();b.close();f.close();

			} catch ( IOException e ) {
					e.printStackTrace();
			}
		}
	}
	
	public static void Top10init(){
		Top10.clear();
		Player plyer = new Player("none", 0, 0, true);
		for(int i=0; i<10; i++) Top10.add(plyer);
	}
	
	//OKだった時の処理
	public void OK() {
		SoundOk();			//Okの音
		set_point(get_point()+10);//10点プラス。
		player.point = get_point();
		System.out.println("OK!! POINT:" + _point);
	}
	
	//落ちた時の処理、どぽん音出力し、lifeを一つ減らす。3回落ちたら止まる。
		//さらに、今までのTOP10と比較して、TOP10に入っていたら、その旨表示し、
		//TOP10データを置換して、データを保管する。
	public void dopon(GraphicsContext gc) {
		SoundDopon();		//落ちる音
		set_life(get_life() - 1);//寿命をマイナス１する。
		player.life = get_life();
		System.out.println("落ちた");
	}
	
	//押したキーに応じて、マンホールの位置を更新して描画
	@Override
	protected void ofKeyPressed(KeyEvent e, GraphicsContext gc) {
		//水色でフタを上書き
		gc.setFill(Color.LIGHTBLUE);// 色を設定（水色）
		gc.setFont(new Font("System Bold",48)); 	// フォント型、サイズを設定
		gc.fillText("＿", get_mhx(), get_mhy());		// フタを水色で上書き
		
		gc.setFill(Color.RED); 					// 色を設定（赤色）
		gc.setFont(new Font("System Bold",48)); 	// フォント型、サイズを設定
		switch(e.getCode()) {
		case Q:										
			set_mh(LU); gc.fillText("＿", LX, UpperY); break;
		case A:
			set_mh(LL); gc.fillText("＿", LX, LowerY); break;
		case P:
			set_mh(RU); gc.fillText("＿", RX, UpperY); break;
		case L:
			set_mh(RL); gc.fillText("＿", RX, LowerY); break;
//		case SPACE: 
//	       	MainAppli.getInstance().StartWindow(); break;
		case ENTER: Platform.exit(); break;
		default: gc.fillText("＿", get_mhx(), get_mhy()); break;
		}
	}
	
	@Override
	protected void ofKeyReleased(KeyEvent e) {
	}

	//	ゲッター(getter)/セッター(setter)
	public walkingMan getwkm(int line, int index) {
		return _wm[line][index];
	}
	public walkingMan[][] getwkms() {
		return _wm;
	}
	public void setwkm(int line, int index, walkingMan value) {
			_wm[line][index] = value;
	}

	public byte get_mh() {
		return _mh;
	}
	public void set_mh(byte _mh) {
		this._mh = _mh;
		set_mhx(_mh);
		set_mhy(_mh);
	}

	public int get_cnter() {
		return _cnter;
	}
	public void set_cnter(int _cnter) {
		this._cnter = _cnter;
	}

	public double get_mhx() {
		return _mhx;
	}
	public void set_mhx(byte index) {
		if(index==LU || index==LL) this._mhx = LX;
		else this._mhx = RX;
	}

	public double get_mhy() {
		return _mhy;
	}
	public void set_mhy(byte index) {
		if(index==LU || index==RU) this._mhy = UpperY;
		else this._mhy = LowerY;
	}

	public int get_life() {
		return _life;
	}

	public void set_life(int _life) {
		this._life = _life;
	}

	public int get_point() {
		return _point;
	}

	public void set_point(int _point) {
		this._point = _point;
	}

	public Color get_clr() {
		return _clr;
	}

	public void set_clr(Color _clr) {
		this._clr = _clr;
	}

	public String get_st2() {
		return _st2;
	}

	public void set_st2(String _st2) {
		this._st2 = _st2;
	}
}
