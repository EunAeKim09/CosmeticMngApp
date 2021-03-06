package kr.or.dgit.it.cosmeticmngapp;


import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
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

    Handler toolbarHandler;

    private List<ItemVO> list;
    private List<ItemVO> favoritelist;
    int fragNum;
    public MyAdapter adapter;
    private UserCosmeticDAO userCosmeticDAO;

    ArrayList<String> proId = new ArrayList<>();

    ActionBar actionBar;
    private int allcheck;
    private HashMap<String, Integer> checkedList;


    public static MyItemList newInstance() {
        return new MyItemList();
    }

    public Handler getToolbarHandler() {
        return toolbarHandler;
    }

    public void setToolbarHandler(Handler toolbarHandler) {
        this.toolbarHandler = toolbarHandler;
    }

    boolean allChecked = false;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cosmeticdao = new UserCosmeticDAO(getActivity());
        cosmeticToolsdao = new UserCosmeticToolsDAO(getActivity());
        lensdao = new UserLensDAO(getActivity());
        lensdao.open();

        checkedList = new HashMap<>();

        Bundle extra = getArguments();
        fragNum = extra.getInt("frag");
        allcheck = extra.getInt("allcheck");
        getListDatas();
    }

   /*Handler checkedhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                    allChecked = true;

            }else if (msg.what == 2){
                allChecked =false;
            }
        }
    };*/

    public void getListDatas() {
        list=new ArrayList<>();
        Cursor cursor=null;
        Log.d("allcheckedYesorNo",allcheck+"//////////");

        if (fragNum == 1) {
            /*Cursor cursor = db.rawQuery("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmetic order by name", null);*/
            cursor = cosmeticdao.selectItemAll(null, null);
            while (cursor.moveToNext()){
                UserCosmetic cosmetic = new UserCosmetic(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                list.add(cosmetic);
                Log.d("nnnnnnnnnnnn",cosmetic.toString());
            }
        }else if (fragNum == 2) {
            cursor = cosmeticToolsdao.selectItemAll(null, null);
            while (cursor.moveToNext()){
                UserCosmeticTools item = new UserCosmeticTools(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                list.add(item);
            }
        }else if (fragNum == 3) {
            cursor = lensdao.selectItemAll(null, null);
            while (cursor.moveToNext()){
                UserLens item = new UserLens(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                list.add(item);
            }
        }

        if(allcheck == 4 || allcheck == 5){
            for(int i=0; i<list.size(); i++){
                Log.d("allchecksize","두번실행되나");
                UserCosmetic itemVO= (UserCosmetic) list.get(i);
                ArrayList<UserCosmetic> itemVOS = new ArrayList<UserCosmetic>();
                itemVO.setVisible(View.VISIBLE);
                if (allcheck == 4) {
                    Log.d("allchecksize",itemVO.get_id()+">.");
                    checkedList.put(String.valueOf(itemVO.get_id()),itemVO.get_id());
                    Message message = Message.obtain(this.toolbarHandler,3, itemVO.get_id());
                    this.toolbarHandler.sendMessage(message);

                }
            }
        }



       /* if(allcheck == 4 || allcheck == 5){
            for(int i=0; i<list.size(); i++){
                UserCosmeticTools itemVO= (UserCosmeticTools) list.get(i);
                ArrayList<UserCosmeticTools> itemVOS = new ArrayList<UserCosmeticTools>();
                itemVO.setVisible(View.VISIBLE);

            }
        }

        if(allcheck == 4 || allcheck == 5){
            for(int i=0; i<list.size(); i++){
                UserLens itemVO= (UserLens) list.get(i);
                ArrayList<UserLens> itemVOS = new ArrayList<UserLens>();
                itemVO.setVisible(View.VISIBLE);

            }
        }
*/
        if(list.size()>0){  //list에 내용이 있을 때
            Log.d("size",list.size()+"..있음");
            MainActivity.emptyTV.setVisibility(View.GONE);
        }else{
            Log.d("size",list.size()+"..리스트 비움");
            MainActivity.emptyTV.setVisibility(View.VISIBLE);
            MainActivity.emptyTV.setText("아이템을 등록해주세요.");
        }


    }



    /*public void getfavoriteListDatas() {
        *//*Bundle extra = getArguments();
        fragNum = extra.getInt("frag");
        cosmeticdao = new UserCosmeticDAO(getActivity());
        cosmeticToolsdao = new UserCosmeticToolsDAO(getActivity());
        lensdao = new UserLensDAO(getActivity());
*//*
//        list = null;
        favoritelist=new ArrayList<>();
        Cursor cursor=null;
        if (fragNum == 1) {
            *//*Cursor cursor = db.rawQuery("select _id, name, img, openDate, endDate, memo, favorite, cate_id  from userCosmetic order by name", null);*//*
            cursor = cosmeticdao.selectItemByFavorite();
            while (cursor.moveToNext()){
                UserCosmetic cosmetic = new UserCosmetic(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                favoritelist.add(cosmetic);
                Log.d("Dddddddddddddd",cosmetic.toString());
            }
        }else if (fragNum == 2) {
            cursor = cosmeticToolsdao.selectItemByFavorite(null, null);
            while (cursor.moveToNext()){
                UserCosmeticTools item = new UserCosmeticTools(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                favoritelist.add(item);
            }
        }else if (fragNum == 3) {
            cursor = lensdao.selectItemByFavorite(null, null);
            while (cursor.moveToNext()){
                UserLens item = new UserLens(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6),
                        cursor.getInt(7));
                favoritelist.add(item);
            }
        };

        if(favoritelist.size()>0){  //list에 내용이 있을 때

            MainActivity.emptyTV.setVisibility(View.GONE);
        }else{
            if(MainActivity.emptyTV.getVisibility()==View.GONE){
                MainActivity.emptyTV.setVisibility(View.VISIBLE);
            }
            MainActivity.emptyTV.setText("즐겨찾는 아이템이 없습니다.");
        }
    }*/

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private MyItemList myItemList;
        private List<ItemVO> list;
        public boolean isDelMode= false;

        public MyAdapter(MyItemList myItemList) {
            this.myItemList = myItemList;
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
                    Intent intent = new Intent(myItemList.getActivity(), DetailViewActivity.class);
                    ItemVO itemVO = list.get(myItemList.recyclerView.getChildAdapterPosition(v));
                    num = "";
                    if(myItemList.fragNum == 1){
                        UserCosmetic dataItem=(UserCosmetic)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }else if(myItemList.fragNum == 2){
                        UserCosmeticTools dataItem=(UserCosmeticTools)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }else if(myItemList.fragNum == 3){
                        UserLens dataItem=(UserLens)itemVO;
                        num = Integer.toString(dataItem.get_id());
                    }
                    intent.putExtra("idNum", num);
                    myItemList.startActivity(intent);
                }
            });
            return new DataViewHolder(view);
        }



        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d("aaaaaaaaaaaaaa","찍히냐 ............");
            ItemVO itemVO=list.get(position);
            DataViewHolder viewHolder=(DataViewHolder)holder;

            if(myItemList.fragNum == 1){
                UserCosmetic dataItem = (UserCosmetic)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());

                if(dataItem.getImg() != null){
                    viewHolder.imgView.setImageBitmap(resize(dataItem.getImg()));//이미지 뷰에 비트맵 넣기
                }

                viewHolder.checkLayout.setVisibility(dataItem.getVisible());
                viewHolder.checkBox.setText(Integer.toString(dataItem.get_id()));
                Log.d("checkBoxId", (String) viewHolder.checkBox.getText());
                if(allcheck == 4 || allcheck == 5){
                    viewHolder.checkLayout.setVisibility(View.VISIBLE);
                    Log.d("checkBoxId", "allcheck들어옴*******");
                    if (allcheck == 4) {
                        Log.d("allchecksize","설마 여기서도실행..?");

                        if(checkedList.containsValue(dataItem.get_id())){
                            Log.d("valueOfIndex","값이 있냐");
                            viewHolder.checkBox.setChecked(true);
                        }else {
                            Log.d("valueOfIndex","값이 없냐");
                        }



                    }else if (allcheck == 5){
                        viewHolder.checkBox.setChecked(false);
                    }
                }
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataItem.setChecked(isChecked);

                        //여기서 선택한 체크박스 어떤건지 체크하고 어레이 리스트 만들어서 메인엑티비티로 보내기;
                        if(isChecked == true){
                            Log.d("buttonView", (String) buttonView.getText());


                            Message message = Message.obtain(myItemList.toolbarHandler,3, buttonView.getText());
                            myItemList.toolbarHandler.sendMessage(message);

                        }else {
                            Log.d("buttonView", Integer.parseInt(buttonView.getText().toString())+"..........");
                            checkedList.remove(buttonView.getText().toString());
                            Message message = Message.obtain(myItemList.toolbarHandler,4, buttonView.getText());
                            myItemList.toolbarHandler.sendMessage(message);
                            
                        }

                    }
                });

                viewHolder.imgView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d("recyclerviewCount",recyclerView.getChildCount()+"..");
                        isDelMode = !isDelMode;
                        if(isDelMode == true){
                            Message message = Message.obtain(myItemList.toolbarHandler,1, null);
                            myItemList.toolbarHandler.sendMessage(message);
                        }else{
                            Message message = Message.obtain(myItemList.toolbarHandler,2, null);
                            myItemList.toolbarHandler.sendMessage(message);
                        }




                        for(int i=0; i<list.size(); i++){
                            UserCosmetic itemVO= (UserCosmetic)list.get(i);
                            ArrayList<UserCosmetic> itemVOS = new ArrayList<UserCosmetic>();
                            if(isDelMode == true){
                                itemVO.setVisible(View.VISIBLE);

                            }else if(isDelMode == false) {
                                Log.d("checkbox","왜 보이냐;;;;;;;;");
                                itemVO.setVisible(View.GONE);
                                itemVO.setChecked(false);
                            }
                        }
                        notifyDataSetChanged();
                        return true;
                    }
                });
            }else if(myItemList.fragNum == 2){
                UserCosmeticTools dataItem=(UserCosmeticTools)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
                if(dataItem.getImg() != null){
                    viewHolder.imgView.setImageBitmap(resize(dataItem.getImg()));//이미지 뷰에 비트맵 넣기
                }

                viewHolder.checkLayout.setVisibility(dataItem.getVisible());
                viewHolder.checkBox.setText(Integer.toString(dataItem.get_id()));
                if(allcheck == 4 || allcheck == 5){
                    viewHolder.checkLayout.setVisibility(View.VISIBLE);
                    Log.d("checkBoxId", "allcheck들어옴*******");
                    if (allcheck == 4) {
                        viewHolder.checkBox.setChecked(true);
                        Message message = Message.obtain(myItemList.toolbarHandler,3, dataItem.get_id());
                        myItemList.toolbarHandler.sendMessage(message);
                    }else if (allcheck == 5){
                        viewHolder.checkBox.setChecked(false);
                    }
                }
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataItem.setChecked(isChecked);
                        if(isChecked == true){
                            Log.d("buttonView", (String) buttonView.getText());

                            Message message = Message.obtain(myItemList.toolbarHandler,3, buttonView.getText());
                            myItemList.toolbarHandler.sendMessage(message);

                        }else {
                            Message message = Message.obtain(myItemList.toolbarHandler,4, buttonView.getText());
                            myItemList.toolbarHandler.sendMessage(message);
                        }
                    }
                });
                viewHolder.imgView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d("recyclerviewCount",recyclerView.getChildCount()+"..");
                        isDelMode = !isDelMode;
                        if(isDelMode == true){
                            Message message = Message.obtain(myItemList.toolbarHandler,1, null);
                            myItemList.toolbarHandler.sendMessage(message);
                        }else{
                            Message message = Message.obtain(myItemList.toolbarHandler,2, null);
                            myItemList.toolbarHandler.sendMessage(message);
                        }
                        for(int i=0; i<list.size(); i++){
                            UserCosmeticTools itemVO= (UserCosmeticTools) list.get(i);
                            ArrayList<UserCosmetic> itemVOS = new ArrayList<UserCosmetic>();
                            if(isDelMode == true){
                                itemVO.setVisible(View.VISIBLE);

                            }else {
                                itemVO.setVisible(View.GONE);
                                itemVO.setChecked(false);
                            }
                        }
                        notifyDataSetChanged();
                        return true;
                    }
                });
            }else if(myItemList.fragNum == 3){
                UserLens dataItem=(UserLens)itemVO;
                viewHolder.nameView.setText(dataItem.getName());
                viewHolder.openDateView.setText(dataItem.getOpenDate());
                viewHolder.endDateView.setText(dataItem.getEndDate());
                if(dataItem.getImg() != null){
                    viewHolder.imgView.setImageBitmap(resize(dataItem.getImg()));//이미지 뷰에 비트맵 넣기
                }

                viewHolder.checkLayout.setVisibility(dataItem.getVisible());
                viewHolder.checkBox.setText(Integer.toString(dataItem.get_id()));
                if(allcheck == 4 || allcheck == 5){
                    viewHolder.checkLayout.setVisibility(View.VISIBLE);
                    Log.d("checkBoxId", "allcheck들어옴*******");
                    if (allcheck == 4) {
                        viewHolder.checkBox.setChecked(true);
                        Message message = Message.obtain(myItemList.toolbarHandler,3, dataItem.get_id());
                        myItemList.toolbarHandler.sendMessage(message);
                    }else if (allcheck == 5){
                        viewHolder.checkBox.setChecked(false);
                    }
                }
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        dataItem.setChecked(isChecked);
                        if(isChecked == true){
                            Log.d("buttonView", (String) buttonView.getText());

                            Message message = Message.obtain(myItemList.toolbarHandler,3, buttonView.getText());
                            myItemList.toolbarHandler.sendMessage(message);

                        }else {
                            Message message = Message.obtain(myItemList.toolbarHandler,4, buttonView.getText());
                            myItemList.toolbarHandler.sendMessage(message);
                        }
                    }
                });
                viewHolder.imgView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.d("recyclerviewCount",recyclerView.getChildCount()+"..");
                        isDelMode = !isDelMode;
                        if(isDelMode == true){
                            Message message = Message.obtain(myItemList.toolbarHandler,1, null);
                            myItemList.toolbarHandler.sendMessage(message);
                        }else{
                            Message message = Message.obtain(myItemList.toolbarHandler,2, null);
                            myItemList.toolbarHandler.sendMessage(message);
                        }
                        for(int i=0; i<list.size(); i++){
                            UserLens itemVO= (UserLens) list.get(i);
                            ArrayList<UserCosmetic> itemVOS = new ArrayList<UserCosmetic>();
                            if(isDelMode == true){
                                itemVO.setVisible(View.VISIBLE);

                            }else {
                                itemVO.setVisible(View.GONE);
                            }
                        }
                        notifyDataSetChanged();

                        return true;
                    }
                });
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

        private class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
            public TextView nameView;
            public TextView openDateView;
            public TextView endDateView;
            public ImageView imgView;
            public ImageView bookmarkView;
            public LinearLayout checkLayout;
            public CheckBox checkBox;

            public DataViewHolder(View itemView){
                super(itemView);
                nameView=itemView.findViewById(R.id.item_product_name);
                openDateView=itemView.findViewById(R.id.item_opendate);
                endDateView=itemView.findViewById(R.id.item_deaddate);
                imgView=itemView.findViewById(R.id.item_product);
               // bookmarkView=itemView.findViewById(R.id.item_bookmark);
                checkLayout = itemView.findViewById(R.id.item_checkbox_layout);
                checkBox = itemView.findViewById(R.id.item_checked);
//                bookmarkView.setOnClickListener(this);
            }



            @Override
            public void onClick(View v) {
                Drawable temp = bookmarkView.getDrawable();
                Drawable temp1 = myItemList.getActivity().getResources().getDrawable(R.drawable.book_mark_on_2);
                ItemVO itemVO = list.get(getAdapterPosition());
                Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
                Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();

                if(myItemList.fragNum == 1){
                    UserCosmetic dataItem = (UserCosmetic) itemVO;
                    UserCosmeticDAO userCosmeticDAO = new UserCosmeticDAO(myItemList.getActivity());
                    if(!tmpBitmap.equals(tmpBitmap1)){
                        UserCosmetic dto = (UserCosmetic) itemVO;
                        Log.d(TAG, "favorite 전: "+dto.toString());
                        dto.setFavorite(1);
                        dto.set_id(dataItem.get_id());
                        userCosmeticDAO.updateFavoriteItem(dto);
                        Log.d(TAG, "favorite: "+dto.toString());
                        bookmarkView.setImageResource(R.drawable.book_mark_on_2);
                    }else{
                        UserCosmetic dto = new UserCosmetic();
                        Log.d(TAG, "favorite 전: "+dto.toString());
                        dto.setFavorite(0);
                        dto.set_id(dataItem.get_id());
                        userCosmeticDAO.updateFavoriteItem(dto);
                        Log.d(TAG, "favorite: "+dto.toString());
                        bookmarkView.setImageResource(R.drawable.book_mark_off);
                    }
                }else if(myItemList.fragNum == 2)            {
                    UserCosmeticTools dataItem = (UserCosmeticTools) itemVO;
                    UserCosmeticToolsDAO userCosmeticToolsDAO = new UserCosmeticToolsDAO(myItemList.getActivity());
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
                }else if(myItemList.fragNum == 3)            {
                    UserLens dataItem = (UserLens) itemVO;
                    UserLensDAO userLensDAO = new UserLensDAO(myItemList.getActivity());
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

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               Log.d("checked",compoundButton.toString()+"..");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_my_item_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        super.onResume();

        Log.d("nasdfasdfsdfnnn","졸라망함...........56465."); //이게 즐겨찾기 안되는 원인.............
        //페이버릿이 눌려졌냐 안눌러졌다
        Bundle extra = getArguments();
        fragNum = extra.getInt("frag");
        allcheck = extra.getInt("allcheck");
        Log.d("nasdfasdfsdfnnn", allcheck +"//////"+fragNum+"..."); //이게 즐겨찾기 안되는 원인.............
        if (allcheck == 4 || allcheck == 5){
            Log.d("nasdfasdfsdfnnn", allcheck +"//////||||||||||"+fragNum+"...");
        }else {
            Log.d("nasdfasdfsdfnnn", allcheck +"//////||||||||||asdasdsadsd"+fragNum+"...");
            getListDatas();
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    public void returnList(){
        getListDatas();
        Log.d("nasdfasdfsdfnnn","졸라망함............3423");
        if(adapter == null){
            Log.d("dddddddddd","널입니다.");
        }else {
            Log.d("dddddddddd","널dk입니다.");
        }
        adapter = new MyAdapter(this);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }


}
