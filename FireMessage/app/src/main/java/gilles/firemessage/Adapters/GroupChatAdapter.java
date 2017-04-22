package gilles.firemessage.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gilles.firemessage.Models.GroupChat;
import gilles.firemessage.R;

/**
 * Created by Gilles on 11/04/2017.
 */

public class GroupChatAdapter extends ArrayAdapter<GroupChat> {
    public GroupChatAdapter(@NonNull Context context, ArrayList<GroupChat> resource) {
        super(context,0,resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        GroupChat chat = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.groupchat_row, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.textViewGroupChat);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageViewGroupChat);

        title.setText(chat.getTitle());
        //TODO:Set image to generated image??


        return convertView;


    }
}
