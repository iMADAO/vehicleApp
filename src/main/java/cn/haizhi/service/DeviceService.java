package cn.haizhi.service;

import cn.haizhi.bean.Device;
import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.exception.MadaoException;
import cn.haizhi.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    @Autowired
    private DeviceMapper mapper;

    private boolean checkIfExist(String deviceId) {
        Device device = mapper.selectByPrimaryKey(deviceId);
        if (device!=null){
            return true;
        }
        return false;
    }

    public void register(Device device) {
        if(checkIfExist(device.getDeviceId())){
            throw new MadaoException(ErrorEnum.DEVICE_HAD_EXIST);
        }
        mapper.insertSelective(device);
    }

    public void cancel(String deviceId){
        mapper.deleteByPrimaryKey(deviceId);
    }
}
