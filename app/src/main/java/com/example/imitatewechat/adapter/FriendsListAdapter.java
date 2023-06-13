package com.example.imitatewechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.imitatewechat.R;
import com.example.imitatewechat.entity.Friend;
import com.example.imitatewechat.util.CommonUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 通讯录
 *
 * @author zhou
 */
public class FriendsListAdapter extends ArrayAdapter<Friend> {

    List<Friend> mFriendList;
    int mResource;
    private LayoutInflater mLayoutInflater;

    public FriendsListAdapter(Context context, int resource, List<Friend> friendList) {
        super(context, resource, friendList);
        this.mResource = resource;
        this.mFriendList = friendList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
//            viewHolder.mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
            viewHolder.mNameTv = convertView.findViewById(R.id.tv_name);
            viewHolder.mHeaderTv = convertView.findViewById(R.id.tv_header);
            viewHolder.mTempView = convertView.findViewById(R.id.view_header);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Friend friend = getItem(position);


        String avatar = friend.getPic();

        viewHolder.mNameTv.setText(friend.getName());
        viewHolder.mHeaderTv.setVisibility(View.GONE);
        viewHolder.mTempView.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public Friend getItem(int position) {
        return mFriendList.get(position);
    }

    @Override
    public int getCount() {
        return mFriendList.size();
    }

    public void setData(List<Friend> friendList) {
        this.mFriendList = friendList;
    }

    class ViewHolder {
//        SimpleDraweeView mAvatarSdv;
        TextView mNameTv;
        TextView mHeaderTv;
        View mTempView;
    }

}