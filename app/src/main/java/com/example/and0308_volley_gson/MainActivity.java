package com.example.and0308_volley_gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etUrl;
    Button btnRequest;
    ListView movieListView;
    MoviewAdapter adapter;


    // 요청(Request) 객체를 저장할 RequestQueue 타입 변수 선언
    static RequestQueue requestQueue;

    MovieList movieList;
    int movieCnt = 0; // 이미지 불러올떄마다 카운트할 변수
    boolean isDone = false; // 이미지 불러오기가 완료되었는지 판단할 boolean 타입 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUrl = findViewById(R.id.etUrl);
        btnRequest = findViewById(R.id.btnRequest);
        movieListView = findViewById(R.id.movieListView);

        btnRequest.setOnClickListener(view -> makeRequest());
        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                String test = "index: " + index + ", l: " + l;
                Toast.makeText(MainActivity.this, test, Toast.LENGTH_SHORT).show();
                showMovieInfoDialog((Movie)(adapter.getItem(index)));

            }
        });

        // 요청큐(RequestQueue) 가 비어있을 경우 생성
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
    } // onCreate() 메서드 끝

    public void showMovieInfoDialog(Movie movie) {
        // AlertDialog.Builder 객체를 통해 다이얼로그를 생성하고
        // movie_info.xml 레이아웃을 표시한 뒤 Movie 객체의 데이터를 출력
        // 1. Movie_info.xml 레이아웃을 관리할 View 객체 생성
        View dialogView = View.inflate(this, R.layout.movie_info, null);

        // 2. View 객체의 FInd
        ImageView ivItemImage  = dialogView.findViewById(R.id.ivItemImage);
        TextView tvMovieNm = dialogView.findViewById(R.id.tvMovieNm);
        TextView tvOpenDt = dialogView.findViewById(R.id.tvOpenDt);
        TextView tvRank = dialogView.findViewById(R.id.tvRank);
        TextView tvAudiCnt = dialogView.findViewById(R.id.tvAudiCnt);

        // 3. 파라미터로 전달받은 영화정보(Movie 객체)를 사용하여 위젯에 데이터 출력
//        int res = getResources().getIdentifier("poster_" + movie.movieCd, "drawable", getPackageName());
        // 포스터 이미지 못가져올 경우 기본이미지 설정
//        if(res == 0) {
//            res = R.drawable.mov05;
//        }
//        ivItemImage.setImageResource(res);
        Glide.with(this)
                .load(movie.image)
                .placeholder(R.mipmap.ic_launcher) // 이미지를 로딩 시작전 임시 이미지 지정
                .error(R.mipmap.ic_launcher) // 리소스를 불러오는 도중 에러 발생시 보여줄 이미지 지정
                .fallback(R.mipmap.ic_launcher) // url이 null 등 비어있을 경우 보여줄 이미지 지정
                .into(ivItemImage); //


        tvMovieNm.setText(movie.movieNm);
        tvOpenDt.setText("개봉일: " + movie.openDt);
        tvRank.setText("순위: " + movie.rank);
        tvAudiCnt.setText("관객수: " + movie.audiCnt);

        // 4. AlertDialog.Builder 클래스를 사용하여 다이얼로그 객체 생성
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("영화 상세 정보");
        dialog.setPositiveButton("확인", null);

        // 5. 다이얼로그에 레이아웃 표시(setView() 메서드 호출)
        dialog.setView(dialogView);

        // 6. 다이얼로그 표시
        dialog.show();


    }
    // Naver API를 통해 영화 포스터를 가져올 imageRequest 메서드 작성
    public  void imageRequest(String movieNm, int index){
//        String strUrl = "https://openapi.naver.com/v1/search/movie.json?query="+movieNm;
        String strUrl = "https://openapi.naver.com/v1/search/image?query="+movieNm + "포스터";


        StringRequest request = new StringRequest(
                Request.Method.GET,
                strUrl,
                response -> {
                    movieCnt++;
//                    Log.d("NAVER", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        JSONArray arr = json.getJSONArray("items");
                        JSONObject item = arr.getJSONObject(0);
//                        Log.d("NAVER IMAGE", item.getString("image"));
//                        movieList.boxOfficeResult.dailyBoxOfficeList.get(index).image = item.getString("image");
                        movieList.boxOfficeResult.dailyBoxOfficeList.get(index).image = item.getString("link");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {}
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("X-Naver-Client-Id", getResources().getString(R.string.ClientId));
                headers.put("X-Naver-Client-Secret", getResources().getString(R.string.ClientSecret));

                return headers;
            }

        };

        requestQueue.add(request);
    }



    public void makeRequest(){
        String strUrl = etUrl.getText().toString();

        // 요청방식(GET,POST), url, success, error
        StringRequest request = new StringRequest(
                Request.Method.GET,
                strUrl,
                response -> {
                    Log.d("response", response);

                    Gson gson  = new Gson();
                    // imagerRequest() 에서도 접근하기 위해 멤버변수로 변경
                    movieList = gson.fromJson(response, MovieList.class);
                    // 영화제목을 전달해서 포스터 가져오기
//                    for(Movie movie : movieList.boxOfficeResult.dailyBoxOfficeList){
//                        imageRequest(movie.movieNm);
//                    }

                    for(int i = 0; i < movieList.boxOfficeResult.dailyBoxOfficeList.size(); i++){
                        Movie movie = movieList.boxOfficeResult.dailyBoxOfficeList.get(i);
                        imageRequest(movie.movieNm, i);
                    }
                    // 요청을 모두 보내고 난 후 ImageTask() 클래스 실행
                    new ImageTask().execute();

//                    Log.d("response", "movieList 변환 성공!");
                    adapter = new MoviewAdapter(
                            MainActivity.this,
                            movieList.boxOfficeResult.dailyBoxOfficeList);
                    movieListView.setAdapter(adapter);

                    // Adapter 객체에 데이터가 추가되었으므로 갱신을 위해
                    // ArrayAdapter 객체의 notifyDataSetChange() 메서드 호출 필수!
                    adapter.notifyDataSetChanged();
                },
                error -> Log.d("myError", error.getMessage())
        ){  // getParams() 메서드 오버라이딩(POST 방식 요청일 경우 구현해야하는 부분)
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // key=f5eef3421c602c6cb7ea224104795888&targetDt=20120101
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", "f5eef3421c602c6cb7ea224104795888");
                params.put("targetDt", "20120101");
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }
    
    // NAVER API 를 통해 영화별 이미지 모두 가져왔는지 판별할 클래스 작성(비동기처리 AsyncTask 상속)
    private class ImageTask extends AsyncTask {
        // 비동기로 백그라운드에서 이미지를 모두 가져왔는지 판단
        @Override
        protected Object doInBackground(Object[] objects) {
            while(!isDone){
                if(movieCnt == movieList.boxOfficeResult.dailyBoxOfficeList.size()){
                    isDone = true;
                }
            }
            return null;
        }

        // doInBackground() 메서드 종료 시 한번 호출되는 메서드
        @Override
        protected void onPostExecute(Object o) {
            // Object o : doInBackground 메서드의 리턴값이 전달됨
            isDone = false;
            movieCnt = 0;

            adapter = new MoviewAdapter(
                    MainActivity.this,
                    movieList.boxOfficeResult.dailyBoxOfficeList);
            movieListView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
    }
    
}