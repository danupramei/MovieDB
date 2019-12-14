package com.example.moviedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.moviedb.Model.ImagesDetail;
import com.example.moviedb.R;

import java.util.List;

import static com.example.moviedb.Util.ImageGlide.setImage;

public class AdapterDetailImage extends PagerAdapter {

    private List<ImagesDetail> images;
    private LayoutInflater inflater;
    private Context context;

    public AdapterDetailImage(Context context, List<ImagesDetail> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View myImageLayout = inflater.inflate(R.layout.item_slide_image_detail, view, false);


        final ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.imageslider);

        final String URLIMAGE = String.valueOf(images.get(position).getFilePath());

        setImage(context, URLIMAGE, myImage);

        final String pos = String.valueOf(position);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent panggil_class = new Intent(context,ReportKecelakaanDetailGambar.class);
                panggil_class.putExtra("gambar", URLIMAGE);
                context.startActivity(panggil_class);*/
            }
        });


        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}