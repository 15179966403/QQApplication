package com.hrc.qqapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息的适配器
 */

public class MessageAdapter extends ArrayAdapter{
    private int resId;
    private List<Message> list=new ArrayList<>();

    public MessageAdapter(Context context,int resource,List<Message> objects) {
        super(context, resource, objects);
        resId=resource;
        list=objects;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (position==0){
            return null;
        }else{
            return list.get(position-1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHalder viewHalder=null;
        int type=getItemViewType(position);
        if (convertView==null){
            switch (type){
                case 0:
                    convertView=LayoutInflater.from(getContext()).inflate(R.layout.message_list_search,null);
                    break;
                case 1:
                    convertView=LayoutInflater.from(getContext()).inflate(resId,null);
                    viewHalder=new ViewHalder(convertView);
                    convertView.setTag(viewHalder);
                    break;
            }
        }else{
            switch (type){
                case 1:
                    viewHalder= (ViewHalder) convertView.getTag();
                    break;
            }
        }
//        viewHalder.img.setImageResource(m.getMessage_img());
        switch (type){
            case 1:
                Message m=list.get(position-1);
                viewHalder.tv_title.setText(m.getMessage_title());
                viewHalder.tv_context.setText(m.getMessage_context());
                viewHalder.tv_red.setText(m.getMessage_red());
                viewHalder.tv_time.setText(m.getMessage_time());
                break;
        }
        return convertView;
    }

    class ViewHalder{
        ImageView img;
        TextView tv_title;
        TextView tv_context;
        TextView tv_red;
        TextView tv_time;
        public ViewHalder(View view){
            tv_title= (TextView) view.findViewById(R.id.message_item_title);
            tv_context= (TextView) view.findViewById(R.id.message_item_context);
            tv_red= (TextView) view.findViewById(R.id.message_item_red);
            tv_time= (TextView) view.findViewById(R.id.message_item_time);
        }
    }
}
