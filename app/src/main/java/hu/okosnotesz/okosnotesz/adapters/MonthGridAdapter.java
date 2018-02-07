package hu.okosnotesz.okosnotesz.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import hu.okosnotesz.okosnotesz.MonthItemClickListener;
import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.model.Reports;

/**
 * Created by user on 2018.01.12..
 */

public class MonthGridAdapter extends RecyclerView.Adapter<MonthGridAdapter.MonthViewHolder> {

    Context mContext;
    List<Calendar> datesList;
    Calendar currentDate;
    List<Reports> allReports;
    Calendar tempCal;
    Calendar today;
    RecyclerView recyclerView;
    ViewPager viewpager;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    int lastPosition = -1;



    public MonthGridAdapter(Context context, FragmentManager fragmentManager, Toolbar toolbar, RecyclerView recyclerView, List<Calendar> datesList, Calendar currentDate, List<Reports> allReports, ViewPager viewPager){
        this.mContext = context;
        this.fragmentManager = fragmentManager;
        this.toolbar = toolbar;
        this.recyclerView = recyclerView;
        this.datesList = datesList;
        this.currentDate = currentDate;
        this.allReports = allReports;
        tempCal = Calendar.getInstance();
        today = Calendar.getInstance();
        this.viewpager = viewPager;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_calendar_grid_item, parent, false);
        MonthViewHolder monthViewHolder = new MonthViewHolder(view);
        return monthViewHolder;
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        tempCal = datesList.get(position);
        TextView dateCell = holder.dateCell;
        ImageView cellCircle = holder.circle;
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        int firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
        toolbar.setTitle(getTitle(datesList.get(firstVisible + 14)));
        drawCircle(cellCircle, 0, 1);
/*        for(int i = 0; i < calList.size(); i++){
            tempCal = calList.get(i);
            final int monthValue = tempCal.get(Calendar.MONTH) + 1;
            final int year = tempCal.get(Calendar.YEAR);
            int yearValue = tempCal.get(Calendar.YEAR);
            int dayValue = tempCal.get(Calendar.DAY_OF_MONTH);
            int currentMonth = currentDate.get(Calendar.MONTH) + 1;
            int currentYear = currentDate.get(Calendar.YEAR);*/
            dateCell.setText(String.valueOf(tempCal.get(Calendar.DAY_OF_MONTH)));
        /*if (monthValue == currentMonth && yearValue == currentYear) {
            if (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                dateCellList.get(i).setTextColor(Color.parseColor("#66BB6A"));
                drawCircle(cellCirclesList.get(i), 0, 1);
            }
            else{
                dateCellList.get(i).setTextColor(Color.BLACK);
            }
        } else {
            dateCellList.get(i).setTextColor(Color.BLACK);
        }
            dateCellList.get(i).setText(String.valueOf(dayValue));
        if(dayValue == today.get(Calendar.DAY_OF_MONTH) &&
                monthValue == today.get(Calendar.MONTH)+1 &&
                year == today.get(Calendar.YEAR)){
            dateCellList.get(i).setTextColor(Color.MAGENTA);
            drawCircle(cellCirclesList.get(i), 0, -1);
        }
        Calendar eventCal = Calendar.getInstance();
        for (int j = 0; j < allReports.size(); j++) {
            eventCal.setTimeInMillis(allReports.get(j).getDate());
            int startHour = eventCal.get(Calendar.HOUR_OF_DAY);
            if (dayValue == eventCal.get(Calendar.DAY_OF_MONTH) && monthValue == eventCal.get(Calendar.MONTH) + 1
                    && yearValue == eventCal.get(Calendar.YEAR)) {
                for (int k = 7; k < 22; k++) {
                    if (k == startHour) {
                        drawCircle(cellCirclesList.get(i), k, 1);
                    }
                }
            }
        }
            int finalI = i;
            dateCellList.get(i).setOnClickListener(view -> holder.onClick(position, fragmentManager, viewpager, finalI)
            );
        }*/
//        holder.itemView.setVisibility(View.INVISIBLE);
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
/*
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                    if(viewToAnimate!=null){
                        viewToAnimate.startAnimation(animation);
                        viewToAnimate.setVisibility(View.VISIBLE);
                    }
                }
            }, delayAnimate);
            delayAnimate+=20;
        }
*/
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }

    private String getTitle(Calendar cal){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MMMM");
        return formatter.format(cal.getTime());
    }

    public void drawCircle(ImageView view, int duration, int today){
        final int[] mWidth = {60};
        final int[] mHeight = {60};
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                mHeight[0] = view.getHeight();
                mWidth[0] = view.getWidth();
                return true;
            }
        });
        Paint paint = new Paint();
        final RectF rect = new RectF();
        int mRadius = mHeight[0]/3;
        int strokeWidth = mHeight[0]/7;
        int startAngle = duration == 0 ? -90 : duration * 30 - 90;
        int sweepAngle = duration == 0 ? 0 : 30;
        Bitmap bitmap = Bitmap.createBitmap(mWidth[0], mHeight[0], Bitmap.Config.ARGB_8888);
        rect.set(mWidth[0] /2- mRadius, mHeight[0] /2 - mRadius, mWidth[0] /2 + mRadius, mHeight[0] /2 + mRadius);
        switch(today){
            case 0: paint.setColor(Color.BLACK);
            break;
            case -1: paint.setColor(Color.MAGENTA);
            break;
            case 1: paint.setColor(Color.GREEN);
            break;
        }
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.STROKE);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(mWidth[0] /2, mHeight[0] /2 , mRadius + strokeWidth / 2, paint);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
        view.setImageBitmap(bitmap);
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder implements MonthItemClickListener{

        CardView itemCardView;
        TextView dateCell;
        ImageView circle;

        public MonthViewHolder(View itemView) {
            super(itemView);
            itemCardView = (CardView) itemView.findViewById(R.id.item_card_view);
            dateCell = (TextView) itemView.findViewById(R.id.calendar_date);
            circle = (ImageView) itemView.findViewById(R.id.date_background);

        }

        @Override
        public void onClick(int position, FragmentManager fragmentManager, ViewPager viewP, int i) {
            long dateLong = datesList.get(position).getTimeInMillis();
            Log.d("open weekfragment", "month onclick"+new Date(dateLong));
            setViewPager(53, dateLong, viewP, fragmentManager);
        }

        private void setViewPager(int i, long l, ViewPager mViewPager, FragmentManager fragmentManager) {
            mViewPager.setAdapter(new PagerAdapter(mContext, fragmentManager, i, l, 5));
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setCurrentItem(26);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                   toolbar.setTitle(viewpager.getAdapter().getPageTitle(mViewPager.getCurrentItem()));
                }
                @Override
                public void onPageSelected(int position) {
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }
}

