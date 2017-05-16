package gilles.firemessage.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gilles.firemessage.Models.User;
import gilles.firemessage.R;

/**
 * Created by Gilles on 16-May-17.
 */

public class GroupMessageAdapter extends ArrayAdapter<User> {
    public GroupMessageAdapter(@NonNull Context context, ArrayList<User> resource) {
        super(context,0, resource);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User usr = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_message_user_row, parent, false);
        }



        TextView username = (TextView) convertView.findViewById(R.id.groupmessagetxt);

        username.setText(usr.getEmail());


        return convertView;
    }
}
