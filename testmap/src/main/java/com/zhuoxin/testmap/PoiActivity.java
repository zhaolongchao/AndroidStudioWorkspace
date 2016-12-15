package com.zhuoxin.testmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.zhuoxin.utils.AMapUtil;
import com.zhuoxin.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l on 2016/12/8.
 */

public class PoiActivity extends Activity implements View.OnClickListener, TextWatcher, Inputtips.InputtipsListener {
    GridView gv;//网格视图
    AutoCompleteTextView actvPoi;//输入框
    Button btn;//搜索按钮
    String keyWord = "";// 要输入的poi搜索关键字
    int[] imageId={R.mipmap.default_generalsearch_nearby_icon_eat,
                    R.mipmap.default_generalsearch_nearby_icon_live,
                    R.mipmap.default_generalsearch_nearby_icon_walk,
                    R.mipmap.default_generalsearch_nearby_icon_travel,
                    R.mipmap.default_generalsearch_nearby_icon_play,
                    R.mipmap.default_generalsearch_nearby_icon_shopping,
                    R.mipmap.default_generalsearch_nearby_icon_life,
                    R.mipmap.default_generalsearch_nearby_icon_service};
    String[] str={"美食","住宿","出行","旅游","玩乐","购物","生活","服务"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_gridview);
        initView();//实例化控件+监听
        btn.setOnClickListener(this);//给搜索按钮注册监听
        GridAdapter adapter=new GridAdapter();
        gv.setAdapter(adapter);//适配
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name=str[i];
                Intent intent=new Intent(PoiActivity.this,PoiItemActivity.class);
                intent.putExtra("poi",name);
                startActivity(intent);
            }
        });
    }
    //实例化控件+监听
    private void initView() {
        gv= (GridView) findViewById(R.id.gv);
        actvPoi= (AutoCompleteTextView) findViewById(R.id.actv_poi);
        btn= (Button) findViewById(R.id.btn_poi);
        actvPoi.addTextChangedListener(this);//联想监听
    }

    //给搜索按钮注册监听
    @Override
    public void onClick(View view) {
        keyWord=actvPoi.getText().toString();
        if(!"".equals(keyWord)){
            Intent intent=new Intent(this,PoiItemActivity.class);
            intent.putExtra("poi",keyWord);
            startActivity(intent);
        }else{
            Toast.makeText(this,"无搜索内容",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String newText = charSequence.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText,"石家庄");
            Inputtips inputTips = new Inputtips(this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    //搜索联想
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            actvPoi.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    //创建适配器
    class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return imageId.length;
        }

        @Override
        public Object getItem(int i) {
            return imageId[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=getLayoutInflater().inflate(R.layout.gridview,null);
            ImageView iv= (ImageView) v.findViewById(R.id.iv);
            TextView tv= (TextView) v.findViewById(R.id.tv);
            iv.setImageResource(imageId[i]);
            tv.setText(str[i]);
            return v;
        }
    }

}
