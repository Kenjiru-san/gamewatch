package fxabc2;

import static fxabc2.iterateN.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class smplController implements Initializable{
	Player player = new Player("none", 0, 0, true);
	String _name;
	@FXML
    private Label instLabel ,nameLabel, top3label, top10label;
	@FXML
	private TextField nameField;
	@FXML
	private ImageView Photo;
	@FXML
	private Button button;
	@FXML
    private void handleButtonAction(ActionEvent event) {
     	player.init();//player初期化
    	System.out.println("You clicked me!");
       	setname(nameField.getText());
       	if("".equals(getname())) player.name = "none";
       	else player.name = getname();

       	MainAppli.getInstance().GameWindow(player);
    }
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		nameField.setPromptText("名前を入力"); //未入力テキスト
		nameField.setFocusTraversable(false); //フォーカスアウト
		instLabel.setText("名前を入力し、スタート・ボタンを押してね！！"
				+ "\r\n"
				+ "\r\n" + "道路にマンホールが空いているので、歩行者が落ちないように"
				+ "\r\n" + "マンホールのフタを動かして、ポイントをＧＥＴ！！"
				+ "\r\n"
				+ "\r\n" + "  Ｑ：左上       Ｐ：右上"
				+ "\r\n" + "  Ａ：左下       Ｌ：右下"
				);
		
		iterateN.Top10FromFile();		//Top10履歴をファイルから読み込む

		String st = "Top10:";

		//３位までをtop3labelに表示
		List<Player> sbList = Top10.subList(0, 3);
		int i=1;
		for(Player p: sbList) {
			st=st + "\r\n  " + "Top" + i + "  " + p.name + " : " + p.point;
			i++;
		}
		top3label.setText(st);
		
		//４位から１０位までをtop10labelに表示
		st = "Top4-10";
		sbList = Top10.subList(3, 10);
		i=4;
		for(Player p: sbList) {
			st=st + "\r\n  " + " " + i + "  " + p.name + " : " + p.point;
			i++;
		}
		top10label.setText(st);
	}
	
	//以下、get/set
	public String getname() {
		return _name;
	}
	public void setname(String name) {
		this._name = name;
	}
}