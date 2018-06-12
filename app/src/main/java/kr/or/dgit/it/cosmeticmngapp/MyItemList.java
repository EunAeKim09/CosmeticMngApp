package kr.or.dgit.it.cosmeticmngapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.ItemVO;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;
import kr.or.dgit.it.cosmeticmngapp.dto.UserLens;

import static android.content.ContentValues.TAG;

public class MyItemList extends Fragment {
    RecyclerView recyclerView;
    private UserCosmeticDAO cosmeticdao;
    private UserCosmeticToolsDAO cosmeticToolsdao;
    private UserLensDAO lensdao;
    List<CardView> linearLayoutList ;

    private List<ItemVO> list;
    int fragNum;
    public static MyAdapter adapter;
    private UserCosmeticDAO userCosmeticDAO;

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
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                list.add(itme);
            }
        } else if (fragNum == 3) {
            Cursor cursor = lensdao.selectItemAll(null, null);
            while (cursor.moveToNext()){
                UserLens itme = new UserLens(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                list.add(itme);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_my_item_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(this);
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyDecoration());

        return recyclerView;
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
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        getListDatas();
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }
}
