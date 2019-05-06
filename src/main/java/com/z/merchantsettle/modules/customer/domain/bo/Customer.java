package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Customer {

    private Integer id;

    private Integer customerType;

    private String customerCertificatesPic;

    private List<String> customerCertificatesPicList;

    @NotBlank(message = "客户证件号不能为空")
    private String customerCertificatesNum;

    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    @NotBlank(message = "客户法人不能为空")
    private String customerLegalPerson;

    @NotBlank(message = "客户证件地址不能为空")
    private String customerCertificatesAddress;
    private Integer status;
    private String statusStr;

    @NotNull(message = "客户有效期不能为空")
    private Long customerValidTime;
    private String customerPrincipal;
    private String auditResult;
    private Long ctime;
    private Long utime;
    private Integer valid;
}
