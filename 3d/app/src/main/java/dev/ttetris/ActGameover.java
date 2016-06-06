package dev.ttetris;  //U
// u//游戏结束

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActGameover extends Activity{
    //声明按钮，该按钮的作用是放回菜单	
	private Button b4 = null;

    public void onCreate(Bundle savedInstanceState) {	
		//super.onCreate(savedInstanceState)就是调用父类的onCreate构造函数，
		//savedInstanceState是保存当前Activity的状态信息。
		super.onCreate(savedInstanceState);
		//setContentView就是设置当前的Activity显示的内容按main.xml布局
		setContentView(R.layout.gameover);
        //取得按钮对象		
		b4 = (Button)findViewById(R.id.bt4);
        //设置按钮监听事件		
		b4.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    //新建一个Intent对象，用来启动新的类
                    Intent intent = new Intent();
                    //指定intent要启动的类，返回菜单
                    intent.setClass(ActGameover.this, TTetris.class);
                    //启动菜单Activity
                    startActivity(intent);
                    //关闭结束Activity
                    ActGameover.this.finish();
                }
            });
	}
}
