package fxabc2;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class smplController implements Initializable{
	String _player;
	@FXML
    private Label instLabel ,nameLabel;
	@FXML
	private TextField nameField;
	@FXML
	private ImageView Photo;
	@FXML
	private Button button;
    @FXML
    private void handleButtonAction(ActionEvent event) {
    	System.out.println("You clicked me!");
       	set_player(nameField.getText());
       	if(get_player() == null) set_player("none");
       	MainAppli.getInstance().GameWindow(get_player());
    }
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		nameField.setPromptText("入力してください"); //未入力テキスト
		nameField.setFocusTraversable(false); //フォーカスアウト
	}
	
	//以下、get/set
	public String get_player() {
		return _player;
	}
	public void set_player(String _player) {
		this._player = _player;
	}
}