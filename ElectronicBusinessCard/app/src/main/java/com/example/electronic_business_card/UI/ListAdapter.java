package com.example.electronic_business_card.UI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.electronic_business_card.DM.CardData;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    String[][] data;
    ArrayList<CardData> total = new ArrayList<CardData>();

    public ListAdapter(Context context, ArrayList<CardData> total){
        this.context = context;
        this.total = total;
    }
    public ListAdapter(Context context, String[][] data){
        this.context = context;
        this.data = data;
    }
    //ListView 한줄에 보여질 data가 셋팅된 한줄
    public View getView(int i, View convertView, ViewGroup viewGroup) { //한 줄당 ListView가 getView() 메서드를 사용해서 보여줌
        //(1) 한줄 자체를 나타내는 MyLinearLayout 클래스 사용
        CardData data = total.get(i); // i번째 줄에 보여줄 data 가져오기
        ListItemView m;
        if(convertView == null){
            m = new ListItemView(context, data);
        }
        else{
            m = (ListItemView)convertView;

            m.setListItem(data);
        }



        //(2) R.layout.line에 정의된 한줄의 모양을 사용
//        LinearLayout linearLayout = new LinearLayout(context);
//
//        LayoutInflater inflater = (LayoutInflater)
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.line, linearLayout, true);
//
//        TextView t1 = linearLayout.findViewById(R.id.textView);
//        TextView t2 = linearLayout.findViewById(R.id.textView2);
//
//        t1.setText(data[i][0]); // i == position 몇번 째 줄에서 호출되는지에 대한 변수
//        t2.setText(data[i][1]);

        //(3) 직접 코드로 한줄의 모양 구성
//        LinearLayout linearLayout = new LinearLayout(context);
//        TextView t1 = new TextView(context);
//        TextView t2 = new TextView(context);
//
//        t1.setText(data[i][0]); // i == position 몇번 째 줄에서 호출되는지에 대한 변수
//        t2.setText(data[i][1]);
//
//        linearLayout.addView(t1);
//        linearLayout.addView(t2);

        //return linearLayout;
        return m;
    }

    public int getCount() { // ListView에 보여줄 줄 수 리턴
        //return data.length;
        return total.size();
    }
    public Object getItem(int i) {
        return null;
    }
    public long getItemId(int i) {
        return 0;
    }
}
