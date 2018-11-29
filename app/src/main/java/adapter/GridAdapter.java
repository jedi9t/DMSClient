package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.victgroup.signup.dmsclient.R;
import java.util.List;
import util.AnimationUtil;


public class GridAdapter extends BaseAdapter {

	Context mContext;
	List<Bitmap> imglist;

	public GridAdapter(Context context, List<Bitmap> imglist) {
		mContext = context;
		this.imglist = imglist;
	}

	private int selected = 0;

	public void notifyDataSetChanged(int id) {
		selected = id;
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return imglist.size();
	}

	@Override
	public Object getItem(int position) {
		return imglist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup root) {
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.image_item, null);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.img_item);
		imageView.setImageBitmap(imglist.get(position));
		if (selected == position) {
			AnimationUtil.scaleBig(imageView);
		}
		return convertView;
	}

}
