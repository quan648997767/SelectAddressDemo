package sad.zzq.com.selectaddressdemo.modle;

import java.io.Serializable;

public class ReceiveAddress implements Serializable{

	public static final long serialVersionUID = 1L;
	public int id = -1;
	//收货人
	public String consignee = "";
	//收货人电话
	public String tel = "";
	//收货地址
	public String address = "";
	//运费
	public double postage;
	//选择后的运费
	public double newPostage;
	//邮编
	public String postcode = "";
	//详细收货地址
	public String detailAddress = "";
	//省
	//public String province_name = "";
	//市
	//public String city_name = "";
	//区
	//public String area_name = "";
	//是否支持配送  默认支持配送
	public boolean is_valid = true;
	//配送提示
	public String valid_msg = "";
	//区id
	//public int area_id ;
	//区code
	public String area_code = "";
	public String province_code = "";
	public String city_code = "";
	public int first;//首件，首重
	public int next;//续件，续重
	public double next_postage;//续重的配送邮费
	public boolean isSelected = false;

	public ReceiveAddress() {
	}

	public ReceiveAddress(int id, String consignee, String tel, String address, double postage, double newPostage, String postcode, String detailAddress,
						  boolean is_valid, String valid_msg, String area_code, String province_code, String city_code, boolean isSelected
						, int first, int next, double next_postage) {
		this.id = id;
		this.consignee = consignee;
		this.tel = tel;
		this.address = address;
		this.newPostage = newPostage;
		this.postage = postage;
		this.postcode = postcode;
		this.detailAddress = detailAddress;
		this.is_valid = is_valid;
		this.valid_msg = valid_msg;
		this.area_code = area_code;
		this.province_code = province_code;
		this.city_code = city_code;
		this.isSelected = isSelected;
		this.first = first;
		this.next = next;
		this.next_postage = next_postage;
	}

	public ReceiveAddress clone() {
		return new ReceiveAddress(id,consignee,tel,address,postage,newPostage,postcode,detailAddress,
				is_valid,valid_msg,area_code,province_code,city_code,isSelected,first,next,next_postage);
	}
}
