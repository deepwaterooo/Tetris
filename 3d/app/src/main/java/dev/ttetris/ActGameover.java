package dev.ttetris;  //U
// u//��Ϸ����

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActGameover extends Activity{
    //������ť���ð�ť�������ǷŻز˵�	
	private Button b4 = null;

    public void onCreate(Bundle savedInstanceState) {	
		//super.onCreate(savedInstanceState)���ǵ��ø����onCreate���캯����
		//savedInstanceState�Ǳ��浱ǰActivity��״̬��Ϣ��
		super.onCreate(savedInstanceState);
		//setContentView�������õ�ǰ��Activity��ʾ�����ݰ�main.xml����
		setContentView(R.layout.gameover);
        //ȡ�ð�ť����		
		b4 = (Button)findViewById(R.id.bt4);
        //���ð�ť�����¼�		
		b4.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    //�½�һ��Intent�������������µ���
                    Intent intent = new Intent();
                    //ָ��intentҪ�������࣬���ز˵�
                    intent.setClass(ActGameover.this, TTetris.class);
                    //�����˵�Activity
                    startActivity(intent);
                    //�رս���Activity
                    ActGameover.this.finish();
                }
            });
	}
}
