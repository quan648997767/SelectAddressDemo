package sad.zzq.com.selectaddressdemo.manager;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sad.zzq.com.selectaddressdemo.MyApplication;
import sad.zzq.com.selectaddressdemo.R;
import sad.zzq.com.selectaddressdemo.modle.ReceiveAddress;

/**
 * 地址管理类，包括更新、删除、添加地址，以及从数据库中读取所有省、市、区信息
 * @author zzq
 *
 */
public class AddressManager{
	public static final int VERSION = 1;

	private static AddressManager ins = null;

	public synchronized static AddressManager newInstance()
	{
		if(ins == null) {
			ins = new AddressManager();
		}
		return ins;
	}

	protected AddressManager()
	{
		initDataFromDB();

	}

	/**
	 * 从数据库中读取所有的省、市、区
	 */
	private void initDataFromDB()
	{
		final String DB_NAME = "adressdb.db"; //保存的数据库文件名
		final String PACKAGE_NAME = MyApplication.getContext().getPackageName();
		final String DB_PATH = "/data"
				+ Environment.getDataDirectory().getAbsolutePath() + "/"
				+ PACKAGE_NAME;  //在手机里存放数据库的位置
		final String dbfile = DB_PATH + "/" + DB_NAME;
		copyDBAsNeeded(dbfile);
		DbManager.DaoConfig config = new DbManager.DaoConfig();
		config.setDbName(DB_NAME); //db名
		config.setDbDir(new File(DB_PATH));
		config.setDbVersion(VERSION);
		DbManager manager = x.getDb(config);
		try {
			Cursor cursor = manager.execQuery("select * from com_wjy_db_Adress where pcode='china'");
			while(cursor.moveToNext()) {
				String provinceName = cursor.getString(cursor.getColumnIndex("name"));
				String provinceCode = cursor.getString(cursor.getColumnIndex("code"));
				Province province = new Province(provinceName, provinceCode);
				provinces.add(province);
				readCitiesOfProvince(manager, province);
			}
			manager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Province> provinces = new ArrayList<Province>();
	/**
	 * 获取数据库中读取的所有省信息
	 * @return
	 */
	public List<Province> getAllProvinces()
	{
		return provinces;
	}

	public int getProvinceCount()
	{
		return provinces.size();
	}

	/**
	 * 通过省或直辖市的code找到省或直辖市
	 * @param code 省或直辖市的code，如：110000表示北京市
	 * @return
	 */
	public Province findProvinceByCode(String code)
	{
		for(Province province : provinces) {
			if(province.getCode().equals(code)) {
				return province;
			}
		}
		return null;
	}

	/**
	 * 根据省或直辖市的名称找到省或直辖市的信息
	 * @param name 省名称，如：北京市
	 * @return
	 */
	public Province findProvinceByName(String name)
	{
		for(Province province : provinces) {
			if(province.getName().equals(name)) {
				return province;
			}
		}
		return null;
	}

	/**
	 * 通过省、市和区三个code获得详细地址信息
	 * @param provinceCode 省或直辖市code，如110000，表示北京
	 * @param cityCode 市code，如110100，表示市辖区
	 * @param districtCode，区code，如110101，表示东城区
	 * 例如三个code分别是110000, 110100, 110101，则输出为北京市市辖区东城区
	 * @return
	 */
	public String getAddress(String provinceCode, String cityCode, String districtCode)
	{
		String addr = "";
		Province province = findProvinceByCode(provinceCode);
		if(province != null) {
			addr += province.getName();
			City city = province.findCityByCode(cityCode);
			if(city != null) {
				addr += city.getName();
				District district = city.findDistrictByCode(districtCode);
				if(district != null) {
					addr += district.getName();
				}
			}
		}
		return addr;
	}

	private void copyDBAsNeeded(String dbfile)
	{
		final int BUFFER_SIZE = 1024;
		try {
			if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				InputStream is = MyApplication.getContext().getResources().openRawResource(
						R.raw.adressdb); //欲导入的数据库
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
	}

	/**
	 * 从数据库读取某个省或直辖市下辖的市信息
	 * @param db
	 * @param province
	 */
	private void readCitiesOfProvince(DbManager db, Province province)
	{
		Cursor cursor = null;
		try {
			cursor = db.execQuery("select * from com_wjy_db_adress where pcode='" + province.code + "'");
			while(cursor.moveToNext()) {
				String cityName = cursor.getString(cursor.getColumnIndex("name"));
				String cityCode = cursor.getString(cursor.getColumnIndex("code"));
				City city = new City(cityName, cityCode, province.getCode());
				readDistrictOfCity(db, city);
				province.addCity(city);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}finally {
			if (cursor != null){
				cursor.close();
			}
		}
	}

	/**
	 * 从数据库读取某个城市下辖的区信息
	 * @param db
	 * @param city
	 */
	private void readDistrictOfCity(DbManager db, City city)
	{
		Cursor cursor = null;
		try {
			cursor = db.execQuery("select * from com_wjy_db_adress where pcode='" + city.code + "'");
			while(cursor.moveToNext()) {
				String districtName = cursor.getString(cursor.getColumnIndex("name"));
				String districtCode = cursor.getString(cursor.getColumnIndex("code"));
				District district = new District(districtName, districtCode, city.getCode(), city.getProvinceCode());
				city.addDistrict(district);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}finally {
			if (cursor != null){
				cursor.close();
			}
		}

	}

	private List<ReceiveAddress> addresses = new ArrayList<ReceiveAddress>();
	//缓存已选中的地址（本地数据） 做容错处理
	private Map<Integer,ReceiveAddress> selectAddresss = new HashMap<Integer,ReceiveAddress>();
	public List<ReceiveAddress> getAllAddresses()
	{
		return addresses;
	}

	public void clear()
	{
		selectAddresss.clear();
		clearSelectAddress();
	}

	/**
	 * 设置所有地址未选中
	 */
	public void clearSelectAddress()
	{
		for (ReceiveAddress address : addresses){
			address.isSelected = false;
		}
	}

	/**
	 * 是否已选中
	 */
	public boolean containsKeySelectAddress(int id)
	{
		return selectAddresss.containsKey(id);
	}


	/**
	 * 设置哪一个地址被选中或取消选中（多选）
	 * @param pos
	 */
	public void setSelectedAddress(int pos)
	{
		ReceiveAddress address = addresses.get(pos);
		address.isSelected = !address.isSelected;
		if (address.isSelected){
			if (!selectAddresss.containsKey(address.id)) {
				selectAddresss.put(address.id, address.clone());
			}
		}else{
			if (selectAddresss.containsKey(address.id)) {
				selectAddresss.remove(address.id);
			}
		}
	}

	/**
	 * 设置单个地址选中（单选）
	 * @param pos
	 */
	public void setSingleSelectedAddress(Context context,int pos)
	{
		for(int i = 0; i < addresses.size(); ++ i) {
			ReceiveAddress address = addresses.get(i);
			address.isSelected = i == pos;
		}
	}

	/**
	 * 指定选项是否被选中
	 * @param pos
	 * @return
	 */
	public boolean isSelectAddressPos(int pos){
		ReceiveAddress address = addresses.get(pos);
		return address.isSelected;
	}

	/**
	 * 指定选项被选中
	 * @param id
	 * @return
	 */
	public void selectAddressId(int id){
		ReceiveAddress address = getAddressById(id);
		address.isSelected = true;
	}

	public List<Integer> getAllSelectedAddressIds()
	{
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < addresses.size(); ++ i) {
			ReceiveAddress address = addresses.get(i);
			if(address.isSelected) {
				indexes.add(address.id);
			}
		}
		return indexes;
	}

	public ReceiveAddress getAddressById(int id)
	{
		for(ReceiveAddress address : addresses) {
			if(address.id == id) {
				return address;
			}
		}
		return null;
	}


	/**
	 * 省或直辖市类
	 * @author frontier
	 *
	 */
	public static class Province
	{
		private String name;
		private String code;
		public Province(String name, String code)
		{
			this.name = name;
			this.code = code;
		}

		public String getName()
		{
			return name;
		}

		public String getCode()
		{
			return code;
		}

		/**
		 * 省或直辖市下辖的市列表
		 */
		private List<City> cities = new ArrayList<City>();
		public void addCity(City city)
		{
			cities.add(city);
		}

		public List<City> getAllCities()
		{
			return cities;
		}

		/**
		 * 通过市的code查找市信息
		 * @param code
		 * @return
		 */
		public City findCityByCode(String code)
		{
			for(City city : cities) {
				if(city.getCode().equals(code)) {
					return city;
				}
			}
			return null;
		}

		/**
		 * 通过市的名字查找市的信息
		 * @param name
		 * @return
		 */
		public City findCityByName(String name)
		{
			for(City city : cities) {
				if(city.getName().equals(name)) {
					return city;
				}
			}
			return null;
		}

		public int getCityCount()
		{
			return cities.size();
		}
	}

	/**
	 * 城市类
	 * @author frontier
	 *
	 */
	public static class City
	{
		private String name;
		private String code;
		private String provinceCode;
		public City(String name, String code, String provinceCode)
		{
			this.name = name;
			this.code = code;
			this.provinceCode = provinceCode;
		}

		public String getName()
		{
			return name;
		}

		public String getCode()
		{
			return code;
		}

		public String getProvinceCode()
		{
			return provinceCode;
		}

		/**
		 * 城市下辖的区列表
		 */
		private List<District> districts = new ArrayList<District>();
		public void addDistrict(District district)
		{
			districts.add(district);
		}

		public List<District> getAllDistricts()
		{
			return districts;
		}

		/**
		 * 通过区code获得区信息
		 * @param code
		 * @return
		 */
		public District findDistrictByCode(String code)
		{
			for(District district : districts) {
				if(district.getCode().equals(code)) {
					return district;
				}
			}
			return null;
		}

		/**
		 * 通过区的名称获得区的信息
		 * @param name
		 * @return
		 */
		public District findDistrictByName(String name)
		{
			for(District district : districts) {
				if(district.getName().equals(name)) {
					return district;
				}
			}
			return null;
		}

		public int getDistrictCount()
		{
			return districts.size();
		}
	}

	/**
	 * 区类
	 * @author frontier
	 *
	 */
	public static class District
	{
		private String name;
		private String code;
		private String cityCode;
		private String provinceCode;
		public District(String name, String code, String cityCode, String provinceCode)
		{
			this.name = name;
			this.code = code;
			this.cityCode = cityCode;
			this.provinceCode = provinceCode;
		}

		public String getName()
		{
			return name;
		}

		public String getCode()
		{
			return code;
		}

		public String getCityCode()
		{
			return cityCode;
		}

		public String getProvinceCode()
		{
			return provinceCode;
		}
	}
}
