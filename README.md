# gamewatch
昔あったゲームウォッチをまねて作ったゲーム
上下段の人が移動するが、上下段の道路に各２つずつマンホールが開いており、
マンホールに落ちる前にマンホールのフタを移動して落ちないようにするゲーム。

スタート画面で、プレイヤー名を記入して、スタートボタンを押すと、
ゲームが始まり、以下のキーで、マンホールを動かす。
　　ｑ：左上、　　ｐ：右上
  　ａ：左下、　　ｌ：右下

２回目までは落ちても、そのままゲームが継続されるが、
３回目に落ちると、ゲームは終了し、画面が閉じられる。

音データ：別に準備・・ネット経由で入手できるようオープンにする予定
　マンホールから下水にぽちゃんと落ちる音：
　マンホールのフタを踏んでＯＫの音：
 	static final String SndWalk = "footstep02.wav";//歩く音from魔王魂
	static final String SndDopon = "waterdopon.wav";//マンホールから下水に落ちる音
	static final String SndOk = "ok.wav";//マンホールのを踏んだ音

画像データ：別に準備・・ネット経由で入手できるようオープンにする予定
　		//人のImageコンストラクタ設定、i=0:上段その１、i=1：上段その２、i=2：下段その１、i=3：下段その２
		for (int i=0; i<4; i++)
			imgN[i] = new Image("/walk" + (i+1) + ".png", false);//黒の人の設定
		for (int i=0; i<4; i++)
			imgB[i] = new Image("/walkb" + (i+1) + ".png", false);//青の人の設定
		for (int i=0; i<4; i++)
			imgG[i] = new Image("/walkg" + (i+1) + ".png", false);//緑の人の設定
		for (int i=0; i<4; i++)
			imgR[i] = new Image("/walkr" + (i+1) + ".png", false);//赤の人の設定
 
