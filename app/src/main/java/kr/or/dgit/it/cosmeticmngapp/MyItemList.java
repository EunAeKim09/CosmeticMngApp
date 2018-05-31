package kr.or.dgit.it.cosmeticmngapp;

import android.content.ClipData;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;

public class MyItemList extends Fragment {
    View view;
    public static  MyItemList newInstance(){
        return new MyItemList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_item_list, container, false);
        /*DBhelper helper = new DBhelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        if(MainActivity.fragNum == 1){
            cursor = db.rawQuery("select img, name, openDate, endDate, favorite from userCosmetic order by _id", null);
        }else if(MainActivity.fragNum == 2){
            cursor = db.rawQuery("select img, name, openDate, endDate, favorite from userCosmeticTools order by _id", null);
        }else if(MainActivity.fragNum == 3){
            cursor = db.rawQuery("select img, name, openDate, endDate, favorite from userLens order by _id", null);
        }

        Item[] items ={
                new Item("Chapter1", "Item one details","open","end","true"),
                new Item("Chapter2", "Item one details","open","end","true"),
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerAdapter(items));

        cursor.close();
        db.close();*/
        return view;
    }

    /*private class RecyclerAdapter extends RecyclerView.Adapter {
        Item[] items;

        public RecyclerAdapter(Item[] items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registerlist_cardview_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Item item = items[position];
            viewHolder.itemimg.setText(item.img);
            viewHolder.itemName.setText(item.name);
            viewHolder.itemOpenD.setText(item.openDate);
            viewHolder.itemEndD.setText(item.endDate);
            viewHolder.itemFavorite.setText(item.favorite);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView itemimg;
            TextView itemName;
            TextView itemOpenD;
            TextView itemEndD;
            TextView itemFavorite;

            public ViewHolder(View itemView) {
                super(itemView);
                itemimg = view.findViewById(R.id.item_product);
                itemName = itemView.findViewById(R.id.item_product_name);
                itemOpenD = itemView.findViewById(R.id.item_opendate);
                itemEndD = itemView.findViewById(R.id.item_deaddate);
                itemFavorite = itemView.findViewById(R.id.item_bookmark);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Snackbar.make(v, "Click selected on Item " + position, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        }
    }*/
}
