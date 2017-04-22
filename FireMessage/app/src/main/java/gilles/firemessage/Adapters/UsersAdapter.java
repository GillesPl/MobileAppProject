package gilles.firemessage.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gilles.firemessage.Models.User;
import gilles.firemessage.R;

/**
 * Created by Gilles on 14/04/2017.
 */

public class UsersAdapter extends ArrayAdapter<User> {
    public UsersAdapter(@NonNull Context context, ArrayList<User> resource) {
        super(context,0, resource);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_row, parent, false);
        }

        TextView username = (TextView) convertView.findViewById(R.id.textViewUser);

        username.setText(user.getEmail());


        return convertView;
    }
}
