package com.z.merchantsettle.modules.upm.domain.bo;

import com.z.merchantsettle.modules.upm.constants.SystemConstant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class User {

    private Integer id;

    @NotBlank(message = "用户id不能为空")
    private String userId;

    @NotBlank(message = "用户姓名不能为空")
    @Length(max = 20, message = "用户名不能超过20个字符")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, message = "密码不能少于6位")
    private String userPassword;

    private String avatar;

    private String salt;

    private Long ctime;

    private Long utime;

    private Integer valid;


    public void setUserId(String userId) {
        this.userId = userId;
        this.salt = userId;
    }

    public boolean isAdmin() {
        return StringUtils.isNotBlank(userId) && SystemConstant.ADMIN_ID.equals(userId.trim());
    }


}
