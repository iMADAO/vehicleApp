package cn.haizhi.util;

import cn.haizhi.bean.BloodPressure;
import cn.haizhi.enums.BloodPressureEnum;
import cn.haizhi.enums.TempuratureEnum;
import com.sun.tools.javac.comp.Lower;

public class AccessUtil {
    public static String accessTemp(double temperature){
        if (temperature>=TempuratureEnum.NORMAL.getStartValue() && temperature <= TempuratureEnum.NORMAL.getEndValue())
            return TempuratureEnum.NORMAL.getMessage();

        if (temperature>=TempuratureEnum.ERROR_H.getStartValue()){
            return TempuratureEnum.ERROR_H.getMessage();
        }
        if (temperature>=TempuratureEnum.HEIGHT4.getStartValue())
            return TempuratureEnum.HEIGHT4.getMessage();
        if (temperature>=TempuratureEnum.HEIGHT3.getStartValue())
            return TempuratureEnum.HEIGHT3.getMessage();
        if (temperature>=TempuratureEnum.HEIGHT2.getStartValue())
            return TempuratureEnum.HEIGHT2.getMessage();
        if (temperature>=TempuratureEnum.HIGHER1.getStartValue())
            return TempuratureEnum.HIGHER1.getMessage();
        if (temperature>=TempuratureEnum.LOWER.getStartValue() && temperature<=TempuratureEnum.LOWER.getEndValue())
            return TempuratureEnum.LOWER.getMessage();
        if (temperature<TempuratureEnum.LOWER1.getEndValue())
            return TempuratureEnum.LOWER1.getMessage();
        return "";
    }

    public static String accessBloodPressure(BloodPressure bloodPressure){
        double diastolicPressure = bloodPressure.getDiastolicPressure();
        double systolicPressure = bloodPressure.getSystolicPressure();

        if (systolicPressure >= BloodPressureEnum.HIGH3.getLowSystolicPressure() && diastolicPressure >= BloodPressureEnum.HIGH3.getLowDiastolicPressure()){
            return BloodPressureEnum.HIGH3.getMessage();
        }

        if(systolicPressure >= BloodPressureEnum.HIGH2.getLowSystolicPressure() && diastolicPressure >= BloodPressureEnum.HIGH2.getLowDiastolicPressure()){
            return BloodPressureEnum.HIGH2.getMessage();
        }

        if(systolicPressure >= BloodPressureEnum.HIGH1.getLowSystolicPressure() && diastolicPressure >= BloodPressureEnum.HIGH1.getLowDiastolicPressure()){
            return BloodPressureEnum.HIGH1.getMessage();
        }

        if(systolicPressure <= BloodPressureEnum.NORMAL.getHighSystolicPressure() && diastolicPressure <= BloodPressureEnum.NORMAL.getHightDiastolicPressure() && systolicPressure >= BloodPressureEnum.LOW.getHighSystolicPressure() && diastolicPressure >= BloodPressureEnum.LOW.getHightDiastolicPressure()){
            return BloodPressureEnum.NORMAL.getMessage();
        }

        if (systolicPressure < BloodPressureEnum.LOW.getHighSystolicPressure() && diastolicPressure < BloodPressureEnum.LOW.getHightDiastolicPressure()){
            return BloodPressureEnum.LOW.getMessage();
        }
        return "";
    }
}
