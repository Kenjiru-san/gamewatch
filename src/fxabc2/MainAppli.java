package fxabc2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
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
	
	@Override public void start(Stage stage) {
		Group root = new Group(); // Groupを作成
		Scene scene = new Scene(root, getScWidth(), getScHeight(), getBackColor()); // Sceneを作成
		
		Canvas canvas = new Canvas(getCvWidth(), getCvHeight()); // Canvasを作成
		GraphicsContext gc = canvas.getGraphicsContext2D(); // GraphicsContextを取得
		
		root.getChildren().add(canvas); // Canvasを追加
		
		stage.setScene(scene); // Sceneを追加
		stage.setTitle("Man Hole Man /JavaFX!");
		stage.show(); // ウィンドウを表示
		
		Thread thread = new Thread(() -> { // メインスレッド
			int i=0;
			while(true) {
				i++;if(i==15)i=0;
				if(i==1)
				Platform.runLater(() -> {
					ofMain(gc);
				});
				
				try {
					Thread.sleep(100);
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
	
	protected void ofMain(GraphicsContext gc) {
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
