package com.ctrl.android.yinfeng.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.AndroidUtil;
import com.beanu.arad.utils.MessageUtils;
import com.ctrl.android.yinfeng.R;
import com.ctrl.android.yinfeng.entity.Hotline3;
import com.ctrl.android.yinfeng.ui.adressbook.AdressBookSearchActivity;
import com.ctrl.android.yinfeng.utils.S;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 通讯录 adapter
 * Created by Eric on 2015/10/22
 */
public class AdressBookSearchAdapter extends BaseAdapter{

    private AdressBookSearchActivity mActivity;
    private List<Hotline3> listMap;
    private int mposition=-1;

    public AdressBookSearchAdapter(AdressBookSearchActivity mActivity){
        this.mActivity = mActivity;
    }

    public void setList(List<Hotline3> listMap) {
        this.listMap = listMap;
        notifyDataSetChanged();
    }
    public void setPosition(int position) {
        this.mposition=position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listMap == null ? 0 : listMap.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
           convertView = LayoutInflater.from(mActivity).inflate(R.layout.family_hotline_item2,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

       final Hotline3 hotLine3 = listMap.get(position);

         /*   holder.cb_adress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mposition = position;
                    }
                }
            });*/

          /*  if (mposition == position) {
                holder.cb_adress.setChecked(true);

            } else {
                holder.cb_adress.setChecked(false);
            }*/
      /*  Arad.imageLoader
                .load(map.get("img"))
                .placeholder(R.mipmap.hotline_default_head_img)
                .into(holder.hotline2_head_img);*/
        holder.hotline2_name.setText(S.getStr(hotLine3.getContactName()));
      /*  if(hotLine3.getContactGrade().equals("0")){
            holder.hotline2_position.setText("经理");
        }else if(hotLine3.getContactGrade().equals("1")){
            holder.hotline2_position.setText("主管");
        }else if(hotLine3.getContactGrade().equals("2")){
            holder.hotline2_position.setText("普通员工");
        }
        else {

        }*/
        holder.hotline2_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hotLine3.getMobile()!=null) {
                    AndroidUtil.dial(mActivity, hotLine3.getMobile());
                }else {
                    MessageUtils.showShortToast(mActivity,"号码为空");
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.hotline2_head_img)//头像
        ImageView hotline2_head_img;
        @InjectView(R.id.hotline2_name)//姓名
        TextView hotline2_name;
        @InjectView(R.id.hotline2_position)//职位
        TextView hotline2_position;
        @InjectView(R.id.hotline2_duty)//职责
        TextView hotline2_duty;
        @InjectView(R.id.hotline2_tel)//电话
        ImageView hotline2_tel;
       /* @InjectView(R.id.cb_adress)//选中
        CheckBox cb_adress;*/


        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
