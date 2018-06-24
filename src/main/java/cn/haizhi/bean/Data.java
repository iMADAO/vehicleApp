package cn.haizhi.bean;

public class Data {
    private String dataId;

    private Double average;

    private Double upperData;

    private Double lowerData;

    private String dataName;

    private Byte age;

    private Byte gender;

    private Byte type;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId == null ? null : dataId.trim();
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getUpperData() {
        return upperData;
    }

    public void setUpperData(Double upperData) {
        this.upperData = upperData;
    }

    public Double getLowerData() {
        return lowerData;
    }

    public void setLowerData(Double lowerData) {
        this.lowerData = lowerData;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName == null ? null : dataName.trim();
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}