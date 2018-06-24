package cn.haizhi.bean;

import lombok.Data;

import java.util.Map;

@Data
public class DataResult {
    private GroupDataDTO groupDataDTO;
    private Map<String, String> report;
}
