package adapter;

import java.io.File;
import java.util.List;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MyGalleryImageAdapter extends BaseAdapter {

	private Context context;
	private Picasso picasso;
	int width;
	int height;
	LayoutInflater inflater;
	private List<String> imgList;
	public MyGalleryImageAdapter(Context context, List<String> imgList ,int width ,int height) {
		this.context = context;
		this.imgList = imgList;
		this.width = width;
		this.height = height;
		picasso=Picasso.with(context);
	}
	
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(context);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new LayoutParams(
					width, height));
			// imageView.setImageResource(R.drawable.bg_border);
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		/*viewHolder.imageView.setImageBitmap(imgList.get(position
				% imgList.size()));*/
		picasso.load(new File(imgList.get(position% imgList.size())))
		.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).resize(width, height).into(viewHolder.imageView);
		
		
		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}

}
