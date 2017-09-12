package com.example.gt.booklist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by g.t on 15/06/2017.
 */

public class ListAdapter extends ArrayAdapter<Book> {

    public ListAdapter(Context context, List<Book> Books) {
        super(context, 0, Books);
    }

    @NonNull
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book item = getItem(position);
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(item.getAuthor());

        TextView phone = (TextView) listItemView.findViewById(R.id.title);
        phone.setText(item.getTitle());

        return listItemView;
    }
}