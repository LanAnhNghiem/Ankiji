package com.jishin.ankiji;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jishin.ankiji.model.DataKanji;

import java.security.SecureRandom;
import java.util.ArrayList;

public class WriteData extends AppCompatActivity {

    private DatabaseReference mReference;

    private TextView txtKanji;
    private TextView txtAmHan;
    private TextView txtViDu;

    private ArrayList<DataKanji> dsKanji;
    public static final int MAX_LENGTH = 28;
    public static final int NUMBER_OF_KANJI = 100;

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_data);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference("data_kanji");

        txtKanji = findViewById(R.id.txtKanji);
        txtAmHan = findViewById(R.id.txtAmHan);
        txtViDu = findViewById(R.id.txtTuVung);

        dsKanji = new ArrayList<>();
//================================WRITE DATA TO FIREBASE====================================//
//        for(DataKanji i : dataKanjis){
//            Log.d("Datakanji", i.toString());
//            String uids = random(28);
//            mReference.child(uids).child("kanji").setValue(i.getKanji());
//            mReference.child(uids).child("amhan").setValue(i.getAmHan());
//            mReference.child(uids).child("tuvung").setValue(i.getTuVung());
//            mReference.child(uids).child("id").setValue(uids);
//        }


//================================READ DATA TO FIREBASE====================================//

//        mReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                DataKanji value = dataSnapshot.getValue(DataKanji.class);
//                dsKanji.add(value);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        Toast.makeText(WriteData.this, dsKanji.get(0).getKanji().toString(), Toast.LENGTH_LONG).show();


    }

//    public static ArrayList<DataKanji> insertToList(){
//        String[] kanji = {"一","九","十","人","二","入","八","七","下","三","山","子","女","小","上"
//                ,"川","土","万","大","千","円","火","月","五","午","今", "水","中","天","日","父","木","友","六","右"
//                ,"外","左","四","出","生","白","半","母","北","本","気","休","行","西","先","年","百","毎","名","何","見"
//                ,"車","男","来", "雨","学","金","国","長","東","後","食","前","南","校","高","書","時","間","電","話","読"
//                ,"語","聞","力","口","工","夕","手","牛","犬","元","公","止","少","心","切", "不","文","方","以","去","兄","古","広"};
//
//        String[] amHan = {"NHẤT","CỬU","THẬP","NHÂN","NHỊ","NHẬP","BÁT","THẤT","HẠ","TAM","SƠN","TỬ","NỮ"
//                ,"TIỂU","THƯỢNG","XUYÊN","THỔ","VẠN","ĐẠI","THIÊN","VIÊN","HỎA", "NGUYỆT","NGŨ","NGỌ","KIM","THỦY","TRUNG"
//                ,"THIÊN","NHẬT","PHỤ","MỘC","HỮU","LỤC","HỮU","NGOẠI","TẢ","TỨ","XUẤT","SINH","BẠCH","BÁN","MẦU","BẮC","BẢN"
//                , "KHÍ","HƯU","HÀNH, HÀNG","TÂY","TIÊN","NIÊN","BÁCH","MỖI","DANH","HÀ","KIẾN","XA","NAM","LAI","VŨ","HỌC"
//                ,"KIM","QUỐC","TRƯỜNG", "TRƯỞNG","ĐÔNG","HẬU","THỰC","TIỀN","NAM","HIỆU","CAO","THƯ","THỜI","GIAN","ĐIỆN"
//                ,"THOẠI","ĐỘC","NGỮ","VĂN","LỰC","KHẨU","CÔNG","TỊCH","THỦ", "NGƯU","KHUYỂN","NGUYÊN","CÔNG","CHỈ","THIẾU",
//                "THIỂU","TÂM","THIẾT","BẤT","VĂN","PHƯƠNG","DĨ","KHỨ","HUYNH","CỔ","QUẢNG"};
//
//        String[] tuVung = {"一つ", "九つ", "十時", "人気", "二つ", "入る", "八つ", "七つ", "不便", "三つ", "山田", "子供", "女の子"
//                , "小さい", "上", "川", "土曜日", "一万", "大きい", "二千", "円い", "火", "月曜日", "五つ", "午前", "今度", "水曜日", "中学校", "天気"
//                , "日曜日", "父", "土曜日", "友達", "六つ", "右", "海外", "左", "四つ", "出す", "学生", "白い", "半分", "母", "北", "日本", "人気", "休み", "銀行"
//                , "西口", "先生", "年", "百", "毎朝", "名前", "何", "見る", "自動車", "男の人", "来年", "雨", "学生", "お金", "貴国", "長い", "東口", "午後", "食事"
//                , "午前", "南口", "学校", "高校", "書く", "時間", "時間", "電車", "会話", "読む", "日本語", "聞く", "活力", "口", "工業", "夕", "苦手", "牛肉", "犬",
//                "元気", "公園", "中止", "少し", "心", "切る", "不安", "文法", "方", "以下", "去年", "兄", "古い", "広い"
//        };
//
//        ArrayList<DataKanji> listKanji = new ArrayList<>();
//        for(int i = 0; i < NUMBER_OF_KANJI; i++){
//            DataKanji data = new DataKanji(kanji[i], amHan[i], tuVung[i]);
//            listKanji.add(data);
//        }
//        return listKanji;
//
//    }




}
