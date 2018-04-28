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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import hu.okosnotesz.okosnotesz.MonthItemClickListener;
import hu.okosnotesz.okosnotesz.R;
import hu.okosnotesz.okosnotesz.fragments.CalendarFragment;
import hu.okosnotesz.okosnotesz.model.Reports;

/**
 * Created by user on 2018.01.12..
 */

public class MonthGridAdapter extends RecyclerView.Adapter<MonthGridAdapter.MonthViewHolder> {

    Context mContext;
    Map<Integer, List<Calendar>> datesList;
    List<Calendar> calList;
    Calendar currentDate;
    List<Reports> allReports;
    Calendar tempCal;
    Calendar today;
    RecyclerView recyclerView;
    ViewPager viewpager;
    Fragment mFragment;
    Toolbar toolbar;
    int lastPosition = -1;
    int delayAnimate = 300;
    CalendarFragment calFrag;



    public MonthGridAdapter(Context context, Fragment fragment, Toolbar toolbar, RecyclerView recyclerView, Map<Integer, List<Calendar>> datesList, Calendar currentDate, List<Reports> allReports, ViewPager viewPager){
        this.mContext = context;
        this.mFragment = fragment;
        this.toolbar = toolbar;
        this.recyclerView = recyclerView;
        this.datesList = datesList;
        this.currentDate = currentDate;
        this.allReports = allReports;
        tempCal = Calendar.getInstance();
        today = Calendar.getInstance();
        this.viewpager = viewPager;
        calFrag = (CalendarFragment) mFragment;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_calendar_month_item, parent, false);
        MonthViewHolder monthViewHolder = new MonthViewHolder(view);

        return monthViewHolder;
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        calList = datesList.get(position);
        tempCal = calList.get(0);
        int weekValue = tempCal.get(Calendar.WEEK_OF_YEAR);
        holder.weekNumber.setText(weekValue+".");
        List<TextView> dateCellList = holder.getDateCells();
        List<ImageView> cellCirclesList = holder.getCellCircles();
        List <Integer> startHours = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            startHours.add(0);
        }
        Log.d("starthours", startHours.size()+"");
        for(int i = 0; i < calList.size(); i++){
            tempCal = calList.get(i);
            final int monthValue = tempCal.get(Calendar.MONTH) + 1;
            final int year = tempCal.get(Calendar.YEAR);
            int yearValue = tempCal.get(Calendar.YEAR);
            int dayValue = tempCal.get(Calendar.DAY_OF_MONTH);
            int currentMonth = currentDate.get(Calendar.MONTH) + 1;
            int currentYear = currentDate.get(Calendar.YEAR);
            drawCircle(cellCirclesList.get(i), startHours, 0);
        if (monthValue == currentMonth && yearValue == currentYear) {
            if (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                dateCellList.get(i).setTextColor(Color.parseColor("#66BB6A"));
                drawCircle(cellCirclesList.get(i), startHours, 1);
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
            drawCircle(cellCirclesList.get(i), startHours, -1);
        }
        Calendar eventCal = Calendar.getInstance();

        for (int j = 0; j < allReports.size(); j++) {
            eventCal.setTimeInMillis(allReports.get(j).getDate());
            int startHour = eventCal.get(Calendar.HOUR_OF_DAY);
            if (dayValue == eventCal.get(Calendar.DAY_OF_MONTH) && monthValue == eventCal.get(Calendar.MONTH) + 1
                    && yearValue == eventCal.get(Calendar.YEAR)) {
                for (int k = 7; k < 22; k++) {
                    if (k == startHour) {
                        startHours.add(k-7, k);
                    }
                }
                drawCircle(cellCirclesList.get(i), startHours, 1);
                Log.d("drawcircle", dayValue + "");
            }
        }
            for (int l = 0; l < 15; l++) {
                startHours.set(l,0);
            }
            int finalI = i;
            dateCellList.get(i).setOnClickListener(view ->calFrag.onClick(position, finalI));
        }
        holder.itemView.setVisibility(View.INVISIBLE);
        setAnimation(holder.itemView);
    }

    private void setAnimation(View view) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                    if(view!=null){
                        view.startAnimation(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }, delayAnimate);
            delayAnimate+=200;
        }

       /* if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }*/


    @Override
    public int getItemCount() {
        return datesList.size();
    }

    public void drawCircle(ImageView view, List<Integer> startHours, int today){
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
        int startAngle = -165;
        int sweepAngle = 0;
        canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
        for(int i = 0; i < startHours.size(); i++){
            if(startHours.get(i)==0) {
                canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
                sweepAngle = 0;
                startAngle += 15;
            }
             if(startHours.get(i)!= 0){
                    canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
                    startAngle += 15;
                    sweepAngle += 15;
                }
        }

        view.setImageBitmap(bitmap);
    }

    public class MonthViewHolder extends RecyclerView.ViewHolder{

        CardView itemCardView;
        TextView dateCell_mon;
        ImageView circle_mon;
        TextView dateCell_tue;
        ImageView circle_tue;
        TextView dateCell_wed;
        ImageView circle_wed;
        TextView dateCell_thu;
        ImageView circle_thu;
        TextView dateCell_fri;
        ImageView circle_fri;
        TextView dateCell_sat;
        ImageView circle_sat;
        TextView dateCell_sun;
        ImageView circle_sun;
        TextView weekNumber;

        public MonthViewHolder(View itemView) {
            super(itemView);
            itemCardView = (CardView) itemView.findViewById(R.id.item_card_view);
            weekNumber = (TextView) itemView.findViewById(R.id.week_number);
            dateCell_mon = (TextView) itemView.findViewById(R.id.calendar_date_mon);
            circle_mon = (ImageView) itemView.findViewById(R.id.date_background_mon);
            dateCell_tue = (TextView) itemView.findViewById(R.id.calendar_date_tue);
            circle_tue = (ImageView) itemView.findViewById(R.id.date_background_tue);
            dateCell_wed = (TextView) itemView.findViewById(R.id.calendar_date_wed);
            circle_wed = (ImageView) itemView.findViewById(R.id.date_background_wed);
            dateCell_thu= (TextView) itemView.findViewById(R.id.calendar_date_thu);
            circle_thu = (ImageView) itemView.findViewById(R.id.date_background_thu);
            dateCell_fri = (TextView) itemView.findViewById(R.id.calendar_date_fri);
            circle_fri = (ImageView) itemView.findViewById(R.id.date_background_fri);
            dateCell_sat = (TextView) itemView.findViewById(R.id.calendar_date_sat);
            circle_sat = (ImageView) itemView.findViewById(R.id.date_background_sat);
            dateCell_sun = (TextView) itemView.findViewById(R.id.calendar_date_sun);
            circle_sun = (ImageView) itemView.findViewById(R.id.date_background_sun);
        }

        private List<TextView> getDateCells(){
            List<TextView> dateCellList = new ArrayList<>(7);
            dateCellList.add(dateCell_mon);
            dateCellList.add(dateCell_tue);
            dateCellList.add(dateCell_wed);
            dateCellList.add(dateCell_thu);
            dateCellList.add(dateCell_fri);
            dateCellList.add(dateCell_sat);
            dateCellList.add(dateCell_sun);
            return dateCellList;
        }

        private List<ImageView> getCellCircles(){
            List<ImageView> cellCirclesList = new ArrayList<>(7);
            cellCirclesList.add(circle_mon);
            cellCirclesList.add(circle_tue);
            cellCirclesList.add(circle_wed);
            cellCirclesList.add(circle_thu);
            cellCirclesList.add(circle_fri);
            cellCirclesList.add(circle_sat);
            cellCirclesList.add(circle_sun);
            return cellCirclesList;
        }


       /* public void onClick(int position, Fragment mFragment, ViewPager viewP, int i) {
            long dateLong = datesList.get(position).get(i).getTimeInMillis();
            Log.d("clickmonthpager", "month onclick"+viewP.getCurrentItem());
            setViewPager(53, dateLong, viewP, mFragment);
        }*/

        /*private void setViewPager(int i, long l, ViewPager mViewPager, Fragment mFragment) {

            if (mFragment.getFragmentManager() == null) {
                Toast.makeText(mContext,R.string.loading, Toast.LENGTH_SHORT).show();
                Log.d("clickmonthpager",  "NoT OK");
            }
            else {
                Log.d("clickmonthpager",  "ok");
                mViewPager.setAdapter(new PagerAdapter(mContext, mFragment.getFragmentManager(), i, l, 5));
                mViewPager.setOffscreenPageLimit(1);
                mViewPager.setCurrentItem(26);
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        toolbar.setTitle(mViewPager.getAdapter().getPageTitle(mViewPager.getCurrentItem()));
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }
        }*/
    }
}

