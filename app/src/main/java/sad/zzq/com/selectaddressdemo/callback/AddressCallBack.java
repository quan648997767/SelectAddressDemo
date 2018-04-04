package sad.zzq.com.selectaddressdemo.callback;


import sad.zzq.com.selectaddressdemo.manager.AddressManager;

/**
 * Created by zzq on 16/9/26.
 */
public interface AddressCallBack {
    public void selectProvince(AddressManager.Province province);
    public void selectCity(AddressManager.City city);
    public void selectDistrict(AddressManager.District district);
}
