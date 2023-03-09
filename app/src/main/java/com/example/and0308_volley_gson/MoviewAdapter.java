package com.example.and0308_volley_gson;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

// BaseAdapter 클래스를 상속받는 MovieAdapter 클래스 정의
public class MoviewAdapter extends BaseAdapter {

    Context context;    // 액티비티 객체를 전달받을 Context 타입 변수 선언
    ArrayList<Movie> dailyBoxOfficeList;    // 영화 목록 정보를 저장하는 ArrayList<Movie> 타입 변수선언

    public MoviewAdapter(Context context, ArrayList<Movie> dailyBoxOfficeList){
        this.context = context;
        this.dailyBoxOfficeList = dailyBoxOfficeList;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        // View.inflate() 메서드를 호출하여 movie_item.xml 레이아웃을 View 객체로 가져오기 (연결)
        View itemLayout = View.inflate(context, R.layout.movie_item, null);

        // View 객체의 findViewById() 메서드를 호출하여 movie_item.xml 레이아웃 위젯 연결하기
        // => 주의! 반드시 View객체명.findViewById() 메서드 호출 필수!
        ImageView ivItemImage = itemLayout.findViewById(R.id.ivItemImage);  // 영화 이미지
        TextView tvMovieNm = itemLayout.findViewById(R.id.tvMovieNm);  // 영화 제목
        TextView tvAudiCnt = itemLayout.findViewById(R.id.tvAudiCnt);  // 영화 영화관객수

        Glide.with(context)
                .load(dailyBoxOfficeList.get(index).image)
                .placeholder(R.mipmap.ic_launcher) // 이미지를 로딩 시작전 임시 이미지 지정
                .error(R.mipmap.ic_launcher) // 리소스를 불러오는 도중 에러 발생시 보여줄 이미지 지정
                .fallback(R.mipmap.ic_launcher) // url이 null 등 비어있을 경우 보여줄 이미지 지정
                .into(ivItemImage); //

//        ivItemImage.setImageResource(R.mipmap.ic_launcher);
        // NAVER API 활용하여 Movie 객체의 멤버변수  image에 이미지 url을 저장했으므로
        // Glide를 활용하여 이미지를 보여준다
        tvMovieNm.setText(dailyBoxOfficeList.get(index).movieNm);
        tvAudiCnt.setText("관객 수 : " + dailyBoxOfficeList.get(index).audiAcc + "명");

        return itemLayout;
    }

    @Override
    public int getCount() {
        // 표시할 목록(영화 정보 목록) 갯수 리턴
        return dailyBoxOfficeList.size();
    }

    @Override
    public Object getItem(int index) {
        // index 값에 해당하는 항목의 영화정보 1개를 Movie 타입 객체로 리턴
        return dailyBoxOfficeList.get(index);
        // ArrayList<Movie>(Movie) -> Object로 업캐스팅되어 리턴
    }

    @Override
    public long getItemId(int i) { return 0; }
}
