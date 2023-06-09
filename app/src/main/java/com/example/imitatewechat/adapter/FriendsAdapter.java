package com.example.imitatewechat.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
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
public class FriendsAdapter extends ArrayAdapter<Friend> {

    List<Friend> mFriendList;
    int mResource;
    private LayoutInflater mLayoutInflater;

    public FriendsAdapter(Context context, int resource, List<Friend> friendList) {
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
        String header;
        header = CommonUtil.setUserHeader(friend.getName());

        viewHolder.mNameTv.setText(friend.getName());


        String avatar = friend.getPic();
//        if (0 == position || null != header && !header.equals(getItem(position - 1).getUserHeader())) {
//            if (TextUtils.isEmpty(header)) {
//                viewHolder.mHeaderTv.setVisibility(View.GONE);
//                viewHolder.mTempView.setVisibility(View.VISIBLE);
//            } else {
//                viewHolder.mHeaderTv.setVisibility(View.VISIBLE);
//                viewHolder.mHeaderTv.setText(header);
//                viewHolder.mTempView.setVisibility(View.GONE);
//            }
//        } else {
            viewHolder.mHeaderTv.setVisibility(View.GONE);
            viewHolder.mTempView.setVisibility(View.VISIBLE);
//        }

//        if (!TextUtils.isEmpty(avatar)) {
//            viewHolder.mAvatarSdv.setImageURI(Uri.parse(avatar));
//        }

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