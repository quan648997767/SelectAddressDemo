package sad.zzq.com.selectaddressdemo.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sad.zzq.com.selectaddressdemo.callback.AddressCallBack;
import sad.zzq.com.selectaddressdemo.manager.AddressManager;
import sad.zzq.com.selectaddressdemo.R;
import sad.zzq.com.selectaddressdemo.tool.StringUtils;


/**
 * Created by zzq on 16/9/26.
 */
public class CityFragment implements AdapterView.OnItemClickListener{
    private AddressCallBack callBack;
    private String code;
    private Context context;
    private AddressAdapter adapter;
    private ListView listview;
    private String provinceCode ;
    private List<AddressManager.City> cityList = new ArrayList<AddressManager.City>();


    public CityFragment(Context context, AddressCallBack callBack){
        this.context = context;
        this.callBack = callBack;
        initView();
    }
    public void setCode(String provinceCode,String code){
        if (!provinceCode.equals(this.provinceCode)){
            this.code = null;
        }
        if (StringUtils.isNoEmpty(code)){
            this.code = code;
        }
        this.provinceCode = provinceCode;
        cityList.clear();
        cityList.addAll(AddressManager.newInstance().findProvinceByCode(provinceCode).getAllCities());
        adapter.notifyDataSetChanged();
    }
    public ListView getListview() {
        return listview;
    }

    public View initView() {
        listview = (ListView) LayoutInflater.from(context).inflate(R.layout.select_address_pop_listview,null);
        //cityList = AddressManager.newInstance().findProvinceByCode(provinceCode).getAllCities();
        adapter = new AddressAdapter(cityList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        return listview;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        code = cityList.get(i).getCode();
        if (callBack != null){
            callBack.selectCity(cityList.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    class AddressAdapter extends BaseAdapter {

        private List<AddressManager.City> list;

        public AddressAdapter(List<AddressManager.City> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.address_listiew_item_textview, null);
            TextView text = (TextView) view.findViewById(R.id.tvTextName);
            ImageView ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
            text.setText(list.get(i).getName());
            if (list.get(i).getCode().equals(code)) {
                text.setTextColor(context.getResources().getColor(R.color.new_redbg));
                ivSelect.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }
}
