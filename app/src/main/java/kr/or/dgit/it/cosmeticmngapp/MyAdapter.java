package kr.or.dgit.it.cosmeticmngapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserCosmeticToolsDAO;
import kr.or.dgit.it.cosmeticmngapp.dao.UserLensDAO;
import kr.or.dgit.it.cosmeticmngapp.dto.ItemVO;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmetic;
import kr.or.dgit.it.cosmeticmngapp.dto.UserCosmeticTools;
import kr.or.dgit.it.cosmeticmngapp.dto.UserLens;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private MyItemList myItemList;
    private List<ItemVO> list;
//    private UserCosmetic dataItem;

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
                Intent intent = new Intent(myItemList.getContext(), DetailViewActivity.class);
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
        }else if(myItemList.fragNum == 2){
            UserCosmeticTools dataItem=(UserCosmeticTools)itemVO;
            viewHolder.nameView.setText(dataItem.getName());
            viewHolder.openDateView.setText(dataItem.getOpenDate());
            viewHolder.endDateView.setText(dataItem.getEndDate());
            if(dataItem.getImg() != null){
                viewHolder.imgView.setImageBitmap(resize(dataItem.getImg()));//이미지 뷰에 비트맵 넣기
            }
        }else if(myItemList.fragNum == 3){
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
            Drawable temp1 = myItemList.getContext().getResources().getDrawable(R.drawable.book_mark_on_2);
            ItemVO itemVO = list.get(getAdapterPosition());
            Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
            Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();

            if(myItemList.fragNum == 1){
                UserCosmetic dataItem = (UserCosmetic) itemVO;
                UserCosmeticDAO userCosmeticDAO = new UserCosmeticDAO(myItemList.getContext());
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
            }else if(myItemList.fragNum == 2)            {
                UserCosmeticTools dataItem = (UserCosmeticTools) itemVO;
                UserCosmeticToolsDAO userCosmeticToolsDAO = new UserCosmeticToolsDAO(myItemList.getContext());
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
                UserLensDAO userLensDAO = new UserLensDAO(myItemList.getContext());
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
