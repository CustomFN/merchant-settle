package com.z.merchantsettle.modules.customer.domain.bo;

import lombok.Data;

@Data
public class CustomerSigner {

    private Integer id;
    private Integer contractId;
    private String signerLabel;
    private String party;
    private String partyContactPerson;
    private String partyContactPersonPhone;
    private Long signTime;
    private Long ctime;
    private Long utime;
    private Integer valid;

}
