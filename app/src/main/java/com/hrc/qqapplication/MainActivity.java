package com.hrc.qqapplication;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hrc.qqapplication.slidingmenu.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private TextView action_bar_title;
    private BottomNavigationView mBottomNavigationView;
    private ImageView user_image;
    private LinearLayout[] left_menu=new LinearLayout[6];
    //actionbar最右边的按钮
    private TextView main_user,main_user_star;
    private ImageView main_message;
    //popupwindow
    private PopupWindow mPopupWindow;
    //消息列表
    private MessageAdapter messageAdapter;
    private List<Message> list_message=new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main_layout);
        init();
    }

    /**
     *控件的初始化和事件监听
     */
    private void init(){
        //初始化控件
        mDrawerLayout= (DrawerLayout) findViewById(R.id.main_drawserLayout);
        action_bar_title= (TextView) findViewById(R.id.action_bar_title);
        mBottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);
        user_image= (ImageView) findViewById(R.id.imageview);
        main_message= (ImageView) findViewById(R.id.main_message);
        main_user= (TextView) findViewById(R.id.main_user);
        main_user_star= (TextView) findViewById(R.id.main_user_star);
        listView= (ListView) findViewById(R.id.listview);

        LinearLayout l1= (LinearLayout) findViewById(R.id.menu_item_vip);
        left_menu[0]=l1;
        LinearLayout l2= (LinearLayout) findViewById(R.id.menu_item_wallect);
        left_menu[1]=l2;
        LinearLayout l3= (LinearLayout) findViewById(R.id.menu_item_clothing);
        left_menu[2]=l3;
        LinearLayout l4= (LinearLayout) findViewById(R.id.menu_item_star);
        left_menu[3]=l4;
        LinearLayout l5= (LinearLayout) findViewById(R.id.menu_item_picture);
        left_menu[4]=l5;
        LinearLayout l6= (LinearLayout) findViewById(R.id.menu_item_file);
        left_menu[5]=l6;

        //设置各种监听
        user_image.setOnClickListener(clickListener);
        mBottomNavigationView.setOnNavigationItemSelectedListener(listener);
        for (int i=0;i<left_menu.length;i++){
            left_menu[i].setOnClickListener(clickListener);
        }
        main_user_star.setOnClickListener(clickListener);
        main_user.setOnClickListener(clickListener);
        main_message.setOnClickListener(clickListener);

        //初始化PopupWindow
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.popupwindow_layout,null);
        mPopupWindow=new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        //初始化消息列表数据
        for (int i=0;i<20;i++){
            Message m=new Message();
            m.setMessage_title("title"+i);
            m.setMessage_context("context"+i);
            m.setMessage_red(i+"");
            m.setMessage_time(i+"");
            list_message.add(m);
        }
        messageAdapter=new MessageAdapter(this,R.layout.message_item_layout,list_message);
        listView.setAdapter(messageAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_message:
                    action_bar_title.setText(R.string.title_message);
                    VisibilityInMain(main_message);
                    return true;
                case R.id.navigation_user:
                    action_bar_title.setText(R.string.title_user);
                    VisibilityInMain(main_user);
                    return true;
                case R.id.navigation_user_star:
                    action_bar_title.setText(R.string.title_user_star);
                    VisibilityInMain(main_user_star);
                    return true;
            }
            return false;
        }
    };

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //标题栏左侧头像点击事件
                case R.id.imageview:
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    break;
                //联系人，标题栏右侧控件
                case R.id.main_user:

                    break;
                //消息，标题栏右侧控件
                case R.id.main_message:
                    mPopupWindow.showAsDropDown(main_message,0,0);
                    break;
                //空间，标题栏右侧控件
                case R.id.main_user_star:
                    break;
                //侧边栏，“我的QQ会员”点击事件
                case R.id.menu_item_vip:
                    Log.d("sb","vip");
                    break;
                //侧边栏，“我的钱包”点击事件
                case R.id.menu_item_wallect:
                    Log.d("sb","vip");
                    break;
                //侧边栏，“个性装扮”点击事件
                case R.id.menu_item_clothing:
                    Log.d("sb","vip");
                    break;
                //侧边栏，“我的收藏”点击事件
                case R.id.menu_item_star:
                    Log.d("sb","vip");
                    break;
                //侧边栏，“我的相册”点击事件
                case R.id.menu_item_picture:
                    Log.d("sb","vip");
                    break;
                //侧边栏，“我的文件”点击事件
                case R.id.menu_item_file:
                    Log.d("sb","vip");
                    break;
            }
        }
    };
    //actionbar最右边的隐藏与显示
    private void VisibilityInMain(View view){
        main_message.setVisibility(View.GONE);
        main_user.setVisibility(View.GONE);
        main_user_star.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }
}
