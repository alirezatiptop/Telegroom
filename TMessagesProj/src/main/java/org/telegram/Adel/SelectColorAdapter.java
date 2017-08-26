package org.telegram.Adel;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;

public class SelectColorAdapter extends BaseAdapter {
    private Context          mContext;
    private ArrayList<Theme> themes;

    // Constructor
    public SelectColorAdapter(Context c){
        mContext = c;
        themes= ThemeChanger.LoadThemes();
    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public Theme getItem(int position) {
        return themes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout viv=new LinearLayout(mContext);
        viv.setOrientation(LinearLayout.VERTICAL);
        TextView Title=new TextView(mContext);
        Title.setText(themes.get(position).getName());
        Title.setTextColor(0xff000000);
        Title.setTextSize(16);
        Title.setPadding(10,5,10,5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Title.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        }

        Title.setGravity(Gravity.RIGHT);
        Title.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        viv.addView(Title);

        TextView imageView = new TextView(mContext);
        if(position>0) {
            imageView.setBackgroundColor(themes.get(position).getActionbarcolor());
        }else{
            imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.gradians));
        }
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(25)));

        viv.addView(imageView);
        TextView imageView2 = new TextView(mContext);
        if(position>0) {
            imageView2.setBackgroundColor(themes.get(position).getTabLayoutColor());
        }else{
            imageView2.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.gradians));
        }

        imageView2.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(25)));

        viv.addView(imageView2);
        return viv;
    }

}