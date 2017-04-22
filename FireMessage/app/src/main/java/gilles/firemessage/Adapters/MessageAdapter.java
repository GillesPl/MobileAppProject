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

import gilles.firemessage.Models.Message;
import gilles.firemessage.R;

/**
 * Created by Gilles on 11/04/2017.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(@NonNull Context context, ArrayList<Message> resource) {
        super(context,0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Message msg = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_row, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.textView_message);

        title.setText(msg.getMessage());


        return convertView;

    }
}
