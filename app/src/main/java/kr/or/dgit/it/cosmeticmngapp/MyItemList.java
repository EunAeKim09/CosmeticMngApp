package kr.or.dgit.it.cosmeticmngapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.HeaderItem;
import kr.or.dgit.it.cosmeticmngapp.dto.ItemVO;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;
import kr.or.dgit.it.cosmeticmngapp.dto.UserLens;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class MyItemList extends Fragment {
    private RecyclerView recyclerView;
    private UserCosmeticDAO cosmeticdao;
    private UserCosmeticToolsDAO cosmeticToolsdao;
    private UserLensDAO lensdao;

    private List<ItemVO> list;
    private int fragNum;
    public static MyAdapter adapter;

    public static MyItemList newInstance() {
        return new MyItemList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        cosmeticdao = new UserCosmeticDAO(getContext());
        cosmeticdao.open();

        cosmeticToolsdao = new UserCosmeticToolsDAO(getContext());
        cosmeticToolsdao.open();

        lensdao = new UserLensDAO(getContext());
        lensdao.open();

        Bundle extra = getArguments();
        fragNum = extra.getInt("frag");

        getListDatas();
    }

    private void getListDatas() {
        SQLiteDatabase db = DBhelper.getInstance(getContext()).getDb();
        list=new ArrayList<>();

        if (fragNum == 1) {
            /*Cursor cursor = db.rawQuery("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmetic order by name", null);*/
            Cursor cursor = cosmeticdao.selectItemAll(null, null);
            while (cursor.moveToNext()){
                UserCosmetic cosmetic = new UserCosmetic(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                list.add(cosmetic);
            }
        }else if (fragNum == 2) {
            Cursor cursor = cosmeticToolsdao.selectItemAll(null, null);
            while (cursor.moveToNext()){
                UserCosmeticTools itme = new UserCosmeticTools(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7));
                list.add(itme);
            }
        } else if (fragNum == 3) {
            Cursor cursor = lensdao.selectItemAll(null, null);
            while (cursor.moveToNext()){
                UserLens itme = new UserLens(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7));
                list.add(itme);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_my_item_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter();
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyDecoration());
        return recyclerView;
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<ItemVO> list;

        public void setList(List<ItemVO> list) {
            this.list = list;
        }

        @Override
        public int getItemViewType(int position) {
            return list.get(position).getType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.registerlist_cardview_layout, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DetailViewActivity.class);
                    ItemVO itemVO = list.get(recyclerView.getChildAdapterPosition(v));
                    String num="";
                    if(fragNum == 1){
                        UserCosmetic dataItem=(UserCosmetic)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }else if(fragNum == 2){
                        UserCosmeticTools dataItem=(UserCosmeticTools)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }else if(fragNum == 3){
                        UserLens dataItem=(UserLens)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }
                    intent.putExtra("idNum", num);
                    startActivityForResult(intent, 100);
                }
            });
            return new DataViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemVO itemVO=list.get(position);
            DataViewHolder viewHolder=(DataViewHolder)holder;
            if(fragNum == 1){
                UserCosmetic dataItem=(UserCosmetic)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
            }else if(fragNum == 2){
                UserCosmeticTools dataItem=(UserCosmeticTools)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
            }else if(fragNum == 3){
                UserLens dataItem=(UserLens)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class DataViewHolder extends RecyclerView.ViewHolder{
            public TextView nameView;
            public TextView openDateView;
            public TextView endDateView;
            public ImageView imgView;
            public ImageView bookmarkView;

            public DataViewHolder(View itemView){
                super(itemView);
                nameView=itemView.findViewById(R.id.item_product_name);
                openDateView=itemView.findViewById(R.id.item_opendate);
                endDateView=itemView.findViewById(R.id.item_deaddate);
                imgView=itemView.findViewById(R.id.item_product);
                bookmarkView=itemView.findViewById(R.id.item_bookmark);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int index=parent.getChildAdapterPosition(view);
            view.setBackgroundColor(0xFFFFFFFF);
            ViewCompat.setElevation(view, 10.0f);
            outRect.set(20, 10, 20, 10);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode == 100){
                getListDatas();
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        getListDatas();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}
