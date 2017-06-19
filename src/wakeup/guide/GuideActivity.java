package wakeup.guide;

import java.util.ArrayList;
import java.util.List;

import com.lensent.wakeup.Main;
import com.lensent.wakeup.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener{
	
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	
	//����ͼƬ��Դ
	private static final int[] pics = {R.layout.wifi ,R.layout.clock,R.layout.edit};
	
	
	//��¼��ǰѡ��λ��
	private int currentIndex;
	private View view1,view2,view3,view4;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
      
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setBackgroundDrawableResource(R.color.trans);

		}
        
        views = new ArrayList<View>();
    	
    	
    	
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT);
    	LayoutInflater mLi = LayoutInflater.from(this);
    	view1 = mLi.inflate(R.layout.guide1, null);
    	view2= mLi.inflate(R.layout.guide2, null);
    	view3 = mLi.inflate(R.layout.guide3, null);
    	view4 =mLi.inflate(R.layout.guide4, null);
        //��ʼ������ͼƬ�б�
     
        	views.add(view1);
        	views.add(view2);
        	views.add(view3);
        	views.add(view4);
        vp = (ViewPager) findViewById(R.id.viewpager);
        //��ʼ��Adapter
        vpAdapter = new ViewPagerAdapter((ArrayList<View>) views);
        vp.setAdapter(vpAdapter);
        //�󶨻ص�
        vp.setOnPageChangeListener(this);
        
       
        
        
        Button  button =(Button) findViewById(R.id.guide);
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
		Intent intent = new Intent (GuideActivity.this,Main.class);
		startActivity(intent);
		finish();
				
			}
        	
        });
        
        
        
        
        
        
        
        
        
    }
    
  
    /**
     *���õ�ǰ������ҳ 
     */
    private void setCurView(int position)
    {
		if (position < 0 || position >= pics.length) {
			return;
		}

		vp.setCurrentItem(position);
    }

   

    //������״̬�ı�ʱ����
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	//����ǰҳ�汻����ʱ����
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	//���µ�ҳ�汻ѡ��ʱ����
	@Override
	public void onPageSelected(int arg0) {
		//���õײ�С��ѡ��״̬
		
	}

	@Override
	public void onClick(View v) {
		int position = (Integer)v.getTag();
		setCurView(position);
		
	}
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}
		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
}