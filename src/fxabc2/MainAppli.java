package fxabc2;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainAppli extends Application {
	
	private int			_scWidth	= 640; // Sceneの幅（デフォルト値： 640）
	private int			_scHeight	= 480; // Sceneの高さ（デフォルト値： 480）
	private Color		_backColor	= Color.rgb(0, 0, 0); // 背景色（デフォルト値： 黒）
	private int			_cvWidth	= 640; // Canvasの幅（デフォルト値： 640）
	private int			_cvHeight	= 480; // Canvasの高さ（デフォルト値： 480）
	private int			_fps		= 200; // 固定値FPS（デフォルト値： 60）
	private boolean		_isDaemon	= true; // デーモン化（デフォルト値： true）
	
	private Stage stg;
    public static MainAppli singleton;

	@Override public void start(Stage stage) {
    	singleton = this;//このインスタンスのコピーがないとgameWinndowは動かない。
    	stg = stage;
    	StartWindow();
	}
	
    public static MainAppli getInstance(){
		return singleton;
	}

	public void StartWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
			Parent prnt = loader.load();
	        Scene scene = new Scene(prnt);
	        stg.setTitle("Man Hole Man");
	        stg.setScene(scene);
	        stg.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void GameWindow(Player plyr){
	    Group root = new Group();
		Scene scene = new Scene(root, getScWidth(), getScHeight(), getBackColor()); // Sceneを作成
		Canvas canvas = new Canvas(getCvWidth(), getCvHeight()); // Canvasを作成
		GraphicsContext gc = canvas.getGraphicsContext2D(); // GraphicsContextを取得

		root.getChildren().add(canvas); // Canvasを追加
		stg.setScene(scene); // Sceneを追加

		stg.setTitle("Man Hole Man /JavaFX");
		stg.show(); // ウィンドウを表示
		
		Thread thread = new Thread(() -> { // メインスレッド
			int i=0;
			int j=0; int k=0;//Thread.sleepの間隔を、j,Kを使って早めていく
			while(plyr.isActive) {
				i++;if(i==15)i=0;
				
				if(i==1|| !plyr.isActive)	//i=1のとき、箱の位置を進めてマンホール落ちたかどうか判断
					Platform.runLater(() -> {
						ofMain(gc, plyr);
				});
				
				j++;if(j%10==0)k++;//jが10増えると、kが1増える
				if(k>55) k=55;//kは55までしか増えない
				try {
					Thread.sleep(70-k);//だんだん早くなる
				}catch(Exception e) {
				}
				
			}
		});
		thread.setDaemon(getIsDaemon()); // スレッドをデーモン化
		thread.start(); // スレッドを開始
		
		scene.setOnKeyPressed(e -> { // キー押下処理
			ofKeyPressed(e, gc);
		});
		scene.setOnKeyReleased(e -> { // キー離上処理
			ofKeyReleased(e);
		});
	}
	
	protected void ofMain(GraphicsContext gc, Player player) {
	}
	
	protected void ofKeyPressed(KeyEvent e, GraphicsContext gc) {
	}
	
	protected void ofKeyReleased(KeyEvent e) {
	}
	
	//	ゲッター(getter)
	public int getScWidth() {
		return _scWidth;
	}
	
	public int getScHeight() {
		return _scHeight;
	}
	
	public Color getBackColor() {
		return _backColor;
	}
	
	public int getCvWidth() {
		return _cvWidth;
	}
	
	public int getCvHeight() {
		return _cvHeight;
	}
	
	public int getFps() {
		return _fps;
	}
	
	public boolean getIsDaemon() {
		return _isDaemon;
	}
	
	//	セッター(setter)
	public void setScWidth(int value) {
		_scWidth = value;
	}
	
	public void setScHeight(int value) {
		_scHeight = value;
	}
	
	public void setBackColor(Color value) {
		_backColor = value;
	}
	
	public void setCvWidth(int value) {
		_cvWidth = value;
	}
	
	public void setCvHeight(int value) {
		_cvHeight = value;
	}
	
	public void setFps(int value) {
		_fps = value;
	}
	
	public void setIsDaemon(boolean value) {
		_isDaemon = value;
	}
}
