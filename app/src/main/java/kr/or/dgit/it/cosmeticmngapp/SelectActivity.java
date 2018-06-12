package kr.or.dgit.it.cosmeticmngapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.db.DBhelper;
import kr.or.dgit.it.cosmeticmngapp.dto.CosmeticCategoryDTO;
import kr.or.dgit.it.cosmeticmngapp.dto.ItemVO;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;
import kr.or.dgit.it.cosmeticmngapp.dto.UserLens;

public class SelectActivity extends AppCompatActivity {
    UserCosmeticDAO userCosmeticDAO;
    private UserCosmeticToolsDAO userCosmeticToolsDAO;
    private UserLensDAO userLensDAO;
    private TextView categoryTV;
    private TextView emptyTV;
    private int cosId;
    private ArrayAdapter<CosmeticCategoryDTO> adapter;
    public MyAdapter myAdapter;
    private SQLiteDatabase db;
    private EditText nameEV;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        userCosmeticDAO = new UserCosmeticDAO(this);
        userCosmeticToolsDAO = new UserCosmeticToolsDAO(this);
        userLensDAO = new UserLensDAO(this);

        categoryTV = (TextView) findViewById(R.id.category);
        nameEV = (EditText) findViewById(R.id.selectName);
        emptyTV = (TextView) findViewById(R.id.emptyTV);
        db = DBhelper.getInstance(this).getDb();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_select_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        myAdapter = new MyAdapter(this);

        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new MyDecoration());
    }

    @Override
    protected void onResume(){
        super.onResume();
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void categoryBtnClick(View view) {
        Cursor cursor = null;
        if (MainActivity.fragNum == 1) {
            cursor = db.rawQuery("select * from cosmeticCategory order by name", null);
        } else if (MainActivity.fragNum == 2) {
            cursor = db.rawQuery("select * from cosmeticToolsCategory order by name", null);
        } else if (MainActivity.fragNum == 3) {
            cursor = db.rawQuery("select * from lensCategory order by name", null);
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        // List Adapter 생성
        adapter = new ArrayAdapter<CosmeticCategoryDTO>(this, android.R.layout.select_dialog_item);


        while (cursor.moveToNext()) {
            adapter.add(new CosmeticCategoryDTO(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4)));

        }

        alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                categoryTV.setText(adapter.getItem(which).getName());
                cosId = adapter.getItem(which).get_id();
                dialog.dismiss();
            }
        });
        alertBuilder.show();

        cursor.close();
    }

    public void closeBtnClick(View view) {
        finish();
    }

    public void selectBtnClick(View view) {
        if(selectList().size()>0){  //list에 내용이 있을 때
            emptyTV.setVisibility(View.GONE);
        }else{
            if(emptyTV.getVisibility()==View.GONE){
                emptyTV.setVisibility(View.VISIBLE);
            }
            emptyTV.setText("검색결과가 없습니다.");
        }
        myAdapter.setList(selectList());
        myAdapter.notifyDataSetChanged();
        categoryTV.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameEV.getWindowToken(), 0);
    }

    private List<ItemVO> selectList() {
        Cursor cursor = null;
        String cateName = categoryTV.getText().toString();
        String itemName = nameEV.getText().toString();
        List<ItemVO> list=new ArrayList<>();
        if(!cateName.equals("카테고리") && itemName.trim().equals("")){   //카테고리만 검색
            if (MainActivity.fragNum == 1) {
               String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmetic where cate_id = %d order by _id", cosId);
               cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserCosmetic cosmetic = new UserCosmetic(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(cosmetic);
                }
            } else if (MainActivity.fragNum == 2) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmeticTools where cate_id = %d order by _id", cosId);
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserCosmeticTools itme = new UserCosmeticTools(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(itme);
                }
            } else if (MainActivity.fragNum == 3) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userLens where cate_id = %d order by _id", cosId);
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserLens itme = new UserLens(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(itme);
                }
            }
        }else if(cateName.equals("카테고리") && !(itemName.trim().equals(""))){    //이름만 검색
            if (MainActivity.fragNum == 1) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmetic where name like %s order by _id", "'%"+itemName+"%'");
                cursor = db.rawQuery(sql, null);
                Log.d("TAG", "sql : "+sql);
                Log.d("TAG", "getCount : "+cursor.toString());
                Log.d("TAG", "cate_id : "+cosId);
                while (cursor.moveToNext()){
                    UserCosmetic item = new UserCosmetic(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(item);
                }
            } else if (MainActivity.fragNum == 2) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmeticTools where name like %s order by _id", "'%"+itemName+"%'");
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserCosmeticTools item = new UserCosmeticTools(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(item);
                }
            } else if (MainActivity.fragNum == 3) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userLens where name like %s order by _id", "'%"+itemName+"%'");
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserLens item = new UserLens(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(item);
                }
            }
        }else if(!cateName.equals("카테고리") && !(itemName.trim().equals(""))){    //카테고리 & 이름 검색
            if (MainActivity.fragNum == 1) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmetic where cate_id=%d and name like %s order by _id", cosId, "'%"+itemName+"%'");
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserCosmetic item = new UserCosmetic(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(item);
                }
            } else if (MainActivity.fragNum == 2) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmeticTools where cate_id=%d and name like %s order by _id", cosId, "'%"+itemName+"%'");
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserCosmeticTools item = new UserCosmeticTools(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(item);
                }
            } else if (MainActivity.fragNum == 3) {
                String sql = String.format("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userLens where cate_id=%d and name like %s order by _id", cosId, "'%"+itemName+"%'");
                cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){
                    UserLens item = new UserLens(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                            cursor.getInt(7));
                    list.add(item);
                }
            }
        }else if(cateName.equals("카테고리") && itemName.trim().equals("")){
            Toast.makeText(this, "검색조건을 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private SelectActivity selectList;
        private List<ItemVO> list;

        public MyAdapter(SelectActivity selectList) {
            this.selectList = selectList;
            list = new ArrayList<>();
        }

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

                private String num;

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(selectList, DetailViewActivity.class);
                    ItemVO itemVO = list.get(selectList.recyclerView.getChildAdapterPosition(v));
                    num = "";
                    if(MainActivity.fragNum == 1){
                        UserCosmetic dataItem=(UserCosmetic)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }else if(MainActivity.fragNum == 2){
                        UserCosmeticTools dataItem=(UserCosmeticTools)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }else if(MainActivity.fragNum == 3){
                        UserLens dataItem=(UserLens)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }
                    intent.putExtra("idNum", num);
                    selectList.startActivity(intent);
                }
            });
            return new MyAdapter.DataViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemVO itemVO=list.get(position);
            MyAdapter.DataViewHolder viewHolder=(MyAdapter.DataViewHolder)holder;
            if(MainActivity.fragNum == 1){
                UserCosmetic dataItem = (UserCosmetic)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
                if(dataItem.getImg() != null){
                    viewHolder.imgView.setImageBitmap(resize(dataItem.getImg()));//이미지 뷰에 비트맵 넣기
                }
            }else if(MainActivity.fragNum == 2){
                UserCosmeticTools dataItem=(UserCosmeticTools)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
                if(dataItem.getImg() != null){
                    viewHolder.imgView.setImageBitmap(resize(dataItem.getImg()));//이미지 뷰에 비트맵 넣기
                }
            }else if(MainActivity.fragNum == 3){
                UserLens dataItem=(UserLens)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
                if(dataItem.getImg() != null){
                    viewHolder.imgView.setImageBitmap(resize(dataItem.getImg()));//이미지 뷰에 비트맵 넣기
                }
            }
        }

        private Bitmap resize(String src){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(src, options);
            int dstWidth;
            int dstHeight;
            if(bitmap.getHeight()>bitmap.getWidth()){   //세로모드
                float ratioX = 720 / (float)bitmap.getWidth();
                float ratioY = 1280 / (float)bitmap.getHeight();

                dstWidth = Math.round(bitmap.getWidth() * ratioX);
                dstHeight = Math.round(bitmap.getHeight() * ratioY);

            }else{  //가로모드
                float ratioX = 1280 / (float)bitmap.getWidth();
                float ratioY = 720 / (float)bitmap.getHeight();

                dstWidth = Math.round(bitmap.getWidth() * ratioX);
                dstHeight = Math.round(bitmap.getHeight() * ratioY);
            }
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);

            return  resized;
        }

        private Bitmap rotate(Bitmap src, float degree) {
            Matrix matrix = new Matrix();    // Matrix 객체 생성
            matrix.postRotate(degree);       // 회전 각도 셋팅
            return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

                bookmarkView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Drawable temp = bookmarkView.getDrawable();
                Drawable temp1 = selectList.getResources().getDrawable(R.drawable.book_mark_on_2);
                ItemVO itemVO = list.get(getAdapterPosition());
                Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
                Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();

                if(MainActivity.fragNum == 1){
                    UserCosmetic dataItem = (UserCosmetic) itemVO;
                    UserCosmeticDAO userCosmeticDAO = new UserCosmeticDAO(selectList);
                    if(!tmpBitmap.equals(tmpBitmap1)){
                        UserCosmetic dto = (UserCosmetic) itemVO;
                        dto.setFavorite(1);
                        dto.set_id(dataItem.get_id());
                        userCosmeticDAO.updateFavoriteItem(dto);
                        bookmarkView.setImageResource(R.drawable.book_mark_on_2);
                    }else{
                        UserCosmetic dto = new UserCosmetic();
                        dto.setFavorite(0);
                        dto.set_id(dataItem.get_id());
                        userCosmeticDAO.updateFavoriteItem(dto);
                        bookmarkView.setImageResource(R.drawable.book_mark_off);
                    }
                }else if(MainActivity.fragNum == 2)            {
                    UserCosmeticTools dataItem = (UserCosmeticTools) itemVO;
                    UserCosmeticToolsDAO userCosmeticToolsDAO = new UserCosmeticToolsDAO(selectList);
                    if(!tmpBitmap.equals(tmpBitmap1)){
                        UserCosmeticTools dto = (UserCosmeticTools) itemVO;
                        dto.setFavorite(1);
                        dto.set_id(dataItem.get_id());
                        userCosmeticToolsDAO.updateFavoriteItem(dto);
                        bookmarkView.setImageResource(R.drawable.book_mark_on_2);
                    }else{
                        UserCosmeticTools dto = new UserCosmeticTools();
                        dto.setFavorite(0);
                        dto.set_id(dataItem.get_id());
                        userCosmeticToolsDAO.updateFavoriteItem(dto);
                        bookmarkView.setImageResource(R.drawable.book_mark_off);
                    }
                }else if(MainActivity.fragNum == 3)            {
                    UserLens dataItem = (UserLens) itemVO;
                    UserLensDAO userLensDAO = new UserLensDAO(selectList);
                    if(!tmpBitmap.equals(tmpBitmap1)){
                        UserLens dto = (UserLens) itemVO;
                        dto.setFavorite(1);
                        dto.set_id(dataItem.get_id());
                        userLensDAO.updateFavoriteItem(dto);
                        bookmarkView.setImageResource(R.drawable.book_mark_on_2);
                    }else{
                        UserLens dto = new UserLens();
                        dto.setFavorite(0);
                        dto.set_id(dataItem.get_id());
                        userLensDAO.updateFavoriteItem(dto);
                        bookmarkView.setImageResource(R.drawable.book_mark_off);
                    }
                }
            }
        }
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
}
