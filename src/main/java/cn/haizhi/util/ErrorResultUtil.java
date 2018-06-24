package cn.haizhi.util;


import cn.haizhi.enums.ErrorEnum;
import cn.haizhi.view.ResultView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ErrorResultUtil {
    public static List<ResultView> getResultViewList2(Map<ErrorEnum, List<String>> map){
        List<ResultView> resultViewList = new ArrayList<>();
        for(Map.Entry<ErrorEnum, List<String>> entry: map.entrySet()){
            ResultView resultView = new ResultView();
            resultView.setCode(entry.getKey().getCode());
            resultView.setHint(entry.getKey().getMessage());
            resultView.setData(IdResultMap.getIdMap(entry.getValue()));
            resultViewList.add(resultView);
        }
        return resultViewList;
    }

    public static List<ResultView> getResultViewListWithName2(String name, Map<ErrorEnum, List<String>> map){
        List<ResultView> resultViewList = new ArrayList<>();
        for(Map.Entry<ErrorEnum, List<String>> entry: map.entrySet()){
            ResultView resultView = new ResultView();
            resultView.setCode(entry.getKey().getCode());
            resultView.setHint(entry.getKey().getMessage());
            resultView.setData(IdResultMap.getIdMapWithName(name, entry.getValue()));
            resultViewList.add(resultView);
        }
        return resultViewList;
    }

    public static List<ResultView> getResultViewList(Map<ErrorEnum, String> map){
        List<ResultView> resultViewList = new ArrayList<>();
        for(Map.Entry<ErrorEnum, String> entry: map.entrySet()){
            ResultView resultView = new ResultView();
            resultView.setCode(entry.getKey().getCode());
            resultView.setHint(entry.getKey().getMessage());
            resultView.setData(IdResultMap.getIdMap(entry.getValue()));
            resultViewList.add(resultView);
        }
        return resultViewList;
    }

    public static List<ResultView> getResultViewListWithName(String name, Map<ErrorEnum, String> map){
        List<ResultView> resultViewList = new ArrayList<>();
        for(Map.Entry<ErrorEnum, String> entry: map.entrySet()){
            ResultView resultView = new ResultView();
            resultView.setCode(entry.getKey().getCode());
            resultView.setHint(entry.getKey().getMessage());
            resultView.setData(IdResultMap.getIdMapWithName(name, entry.getValue()));
            resultViewList.add(resultView);
        }
        return resultViewList;
    }
}
