package com.example.diego.voltatabu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by diego on 09/12/2016.
 */

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Alunos> alunositems;

    public CustomListAdapter(Activity activity, List<Alunos> movieItems) {
        this.activity = activity;
        this.alunositems = movieItems;
    }

    @Override
    public int getCount() {
        return alunositems.size();
    }

    @Override
    public Object getItem(int location) {
        return alunositems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.miniatura, null);


        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.my_image_view);
        TextView nome = (TextView) convertView.findViewById(R.id.nome);
        TextView horario = (TextView) convertView.findViewById(R.id.horario);


        // getting movie data for the row
        Alunos a = alunositems.get(position);

        // thumbnail image
        draweeView.setImageURI(a.getUri());

        // title
        nome.setText(a.getNome());

        horario.setText(a.getHorario());

        return convertView;
    }

}
