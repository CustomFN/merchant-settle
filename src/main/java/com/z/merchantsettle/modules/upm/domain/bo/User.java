package com.z.merchantsettle.modules.upm.domain.bo;

import cn.hutool.crypto.digest.DigestUtil;
import com.z.merchantsettle.modules.upm.constants.SystemConstant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {

    private Integer id;


    private String userId;

    @NotBlank(message = "用户姓名拼写不能为空")
    @Length(max = 20, message = "用户姓名拼写不能超过20个字符")
    private String userNameSpell;

    @NotBlank(message = "用户姓名不能为空")
    @Length(max = 20, message = "用户名不能超过20个字符")
    private String userName;

    private String userPassword;

    private String salt;

    private Integer status;

    private Long ctime;

    private Long utime;

    private Integer valid;

    private List<String> roleIdList = new ArrayList<>();

    private List<String> roleNameList = new ArrayList<>();


    public void setUserId(String userId) {
        this.userId = userId;
        this.salt = userId;
    }

    public void setUserPassword(String userPassword) {
        if (StringUtils.isBlank(userPassword)) {
            this.userPassword = "e10adc3949ba59abbe56e057f20f883e";
        } else {
            this.userPassword = DigestUtil.md5Hex(userPassword);
        }

    }

    public boolean isAdmin() {
        return StringUtils.isNotBlank(userId) && SystemConstant.ADMIN.equals(userId.trim());
    }


}
