package com.arad_itc.newsit;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class AdapterList2 extends ArrayAdapter<Row_List_View_object> {

	public AdapterList2(ArrayList<Row_List_View_object> array) {
		super(Newsit.context, R.layout.row_listview, array);
	}

	private static class ViewHolder {
		public ViewGroup LayoutRoot;
		public TextView category;
		public TextView title;
		public TextView description;
		public TextView time;
		public ImageView imageview;


		public ViewHolder(View view) {
			category = (TextView) view.findViewById(R.id.category);
			title = (TextView) view.findViewById(R.id.title);
			description = (TextView) view.findViewById(R.id.description);
			time = (TextView) view.findViewById(R.id.time);
			imageview = (ImageView) view.findViewById(R.id.photo);
			LayoutRoot = (ViewGroup) view.findViewById(R.id.layout_root);
		}


		public void fill(final ArrayAdapter<Row_List_View_object> adapter, final Row_List_View_object item, final int position) {
			category.setText(item.CATEGORY);
			title.setText(item.TITLE);
			description.setText(item.DESCRIPTION);
			time.setText(item.DATETIME);
			Picasso.with(Newsit.context).load(item.URL_PHOTO)
			.error(R.drawable.ic_launcher)
			.into(imageview);
			LayoutRoot.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (Newsit.isOnline()) {
						Intent intent = new Intent(Newsit.currentActivity2, Show_web2.class);
						intent.putExtra("POSITION", position);
						Newsit.currentActivity2.startActivity(intent);
					}
					else {
						Toast.makeText(Newsit.context, "اینترنت در دسترس نیست", Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		Row_List_View_object item = getItem(position);
		if (convertView == null) {
			convertView = Newsit.inflater.inflate(R.layout.row_listview, parent, false);
			((TextView) convertView.findViewById(R.id.title)).setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "fonts/BYEKAN.TTF"));
			((TextView) convertView.findViewById(R.id.description)).setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "fonts/bmitra.ttf"));
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.fill(this, item, position);
		return convertView;
	}
}