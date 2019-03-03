package com.z.merchantsettle.modules.customer.domain.db;

import lombok.Data;

@Data
public class CustomerSignerDB {

    private Integer id;
    private Integer contractId;
    private String party;
    private String partyContactPerson;
    private String partyContactPersonPhone;
    private Long signTime;
    private Long ctime;
    private Long utime;
    private Integer valid;

}
