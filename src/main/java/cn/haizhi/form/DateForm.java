package cn.haizhi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class DateForm {
    @NotBlank(message = "起始日期不能为空")
    private String startDate;

    @NotBlank(message = "终止日期不能为空")
    private String endDate;
}
