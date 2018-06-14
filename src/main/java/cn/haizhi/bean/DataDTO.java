package cn.haizhi.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataDTO {
    private Map<String, GroupData> groupDataList;
}
