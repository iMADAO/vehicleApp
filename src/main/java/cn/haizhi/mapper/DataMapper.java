package cn.haizhi.mapper;

import cn.haizhi.bean.Data;
import cn.haizhi.bean.DataExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataMapper {
    long countByExample(DataExample example);

    int deleteByExample(DataExample example);

    int deleteByPrimaryKey(String dataId);

    int insert(Data record);

    int insertSelective(Data record);

    List<Data> selectByExample(DataExample example);

    Data selectByPrimaryKey(String dataId);

    int updateByExampleSelective(@Param("record") Data record, @Param("example") DataExample example);

    int updateByExample(@Param("record") Data record, @Param("example") DataExample example);

    int updateByPrimaryKeySelective(Data record);

    int updateByPrimaryKey(Data record);
}