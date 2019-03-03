package com.z.merchantsettle.utils.transfer.customer;

import com.google.common.collect.Lists;
import com.z.merchantsettle.modules.customer.domain.bo.*;
import com.z.merchantsettle.modules.customer.domain.db.*;
import com.z.merchantsettle.utils.TransferUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class CustomerTransferUtil {

    public static Customer transCustomerDB2Bo(CustomerDB customerDB) {
        if (customerDB == null ) {
            return null;
        }

        Customer customer = new Customer();
        TransferUtil.transferAll(customerDB, customer);
        return customer;
    }

    public static List<Customer> transCustomerDBList2BoList(List<CustomerDB> customerDBList) {
        if (CollectionUtils.isEmpty(customerDBList)) {
            return Lists.newArrayList();
        }

        List<Customer> customerList = Lists.newArrayList();
        for (CustomerDB customerDB : customerDBList) {
            customerList.add(transCustomerDB2Bo(customerDB));
        }
        return customerList;
    }

    public static CustomerDB transCustomer2DB(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDB customerDB = new CustomerDB();
        TransferUtil.transferAll(customer, customerDB);
        return customerDB;
    }


    public static CustomerAudited transCustomerAuditedDB2Bo(CustomerAuditedDB customerAuditedDB) {
        if (customerAuditedDB == null) {
            return null;
        }

        CustomerAudited customerAudited = new CustomerAudited();
        TransferUtil.transferAll(customerAuditedDB, customerAudited);
        return customerAudited;
    }

    public static List<CustomerAudited> transCustomerAuditedDBList2BoList(List<CustomerAuditedDB> customerAuditedDBList) {
        if (CollectionUtils.isEmpty(customerAuditedDBList)) {
            return Lists.newArrayList();
        }

        List<CustomerAudited> customerAuditedList = Lists.newArrayList();
        for (CustomerAuditedDB customerAuditedDB : customerAuditedDBList) {
            customerAuditedList.add(transCustomerAuditedDB2Bo(customerAuditedDB));
        }
        return customerAuditedList;
    }

    public static CustomerAuditedDB transCustomerAudited2DB(CustomerAudited customerAudited) {
        if (customerAudited == null) {
            return null;
        }

        CustomerAuditedDB customerAuditedDB = new CustomerAuditedDB();
        TransferUtil.transferAll(customerAudited, customerAuditedDB);
        return customerAuditedDB;
    }

    public static List<CustomerAuditedDB> transCustomerAuditedList2DBList(List<CustomerAudited> customerAuditedList) {
        if (CollectionUtils.isEmpty(customerAuditedList)) {
            return Lists.newArrayList();
        }

        List<CustomerAuditedDB> customerAuditedDBList = Lists.newArrayList();
        for (CustomerAudited customerAudited : customerAuditedList) {
            customerAuditedDBList.add(transCustomerAudited2DB(customerAudited));
        }
        return customerAuditedDBList;
    }

    public static CustomerKpDB transCustomerKp2DB(CustomerKp customerKp) {
        if (customerKp == null) {
            return null;
        }

        CustomerKpDB customerKpDB = new CustomerKpDB();
        TransferUtil.transferAll(customerKp, customerKpDB);
        return customerKpDB;
    }

    public static List<CustomerKpDB> transCustomerKpList2DBList(List<CustomerKp> customerKpList) {
        if (CollectionUtils.isEmpty(customerKpList)) {
            return Lists.newArrayList();
        }

        List<CustomerKpDB> customerKpDBList = Lists.newArrayList();
        for (CustomerKp customerKp : customerKpList) {
            customerKpDBList.add(transCustomerKp2DB(customerKp));
        }
        return customerKpDBList;
    }

    public static CustomerKp transCustomerKpDB2Bo(CustomerKpDB customerKpDB) {
        if (customerKpDB == null) {
            return null;
        }

        CustomerKp customerKp = new CustomerKp();
        TransferUtil.transferAll(customerKpDB, customerKp);
        return customerKp;
    }

    public static List<CustomerKp> transCustomerKpDBList2BoList(List<CustomerKpDB> customerKpDBList) {
        if (CollectionUtils.isEmpty(customerKpDBList)) {
            return Lists.newArrayList();
        }

        List<CustomerKp> customerKpList = Lists.newArrayList();
        for (CustomerKpDB customerKpDB : customerKpDBList) {
            customerKpList.add(transCustomerKpDB2Bo(customerKpDB));
        }
        return customerKpList;
    }

    public static CustomerKpAudited transCustomerKpAuditedDB2Bo(CustomerKpAuditedDB customerKpAuditedDB) {
        if (customerKpAuditedDB == null ) {
            return null;
        }

        CustomerKpAudited customerKpAudited = new CustomerKpAudited();
        TransferUtil.transferAll(customerKpAuditedDB, customerKpAudited);
        return customerKpAudited;
    }

    public static CustomerKpAuditedDB transCustomerKpAudited2DB(CustomerKpAudited customerKpAudited) {
        if (customerKpAudited == null) {
            return null;
        }

        CustomerKpAuditedDB customerKpAuditedDB = new CustomerKpAuditedDB();
        TransferUtil.transferAll(customerKpAudited, customerKpAuditedDB);
        return customerKpAuditedDB;
    }

    public static CustomerContract transCustomerContractDB2Bo(CustomerContractDB customerContractDB) {
        if (customerContractDB == null ) {
            return null;
        }

        CustomerContract customerContract = new CustomerContract();
        TransferUtil.transferAll(customerContractDB, customerContract);
        return customerContract;
    }

    public static CustomerContractAudited transCustomerContractAuditedDB2Bo(CustomerContractAuditedDB customerContractAuditedDB) {
        if (customerContractAuditedDB == null ) {
            return null;
        }

        CustomerContractAudited customerContractAudited = new CustomerContractAudited();
        TransferUtil.transferAll(customerContractAuditedDB, customerContractAudited);
        return customerContractAudited;
    }

    public static List<CustomerContractAudited> transCustomerContractAuditedDBList2BoList(List<CustomerContractAuditedDB> customerContractAuditedDBList) {
        if (CollectionUtils.isEmpty(customerContractAuditedDBList)) {
            return Lists.newArrayList();
        }

        List<CustomerContractAudited> customerContractAuditedList = Lists.newArrayList();
        for (CustomerContractAuditedDB customerContractAuditedDB : customerContractAuditedDBList) {
            customerContractAuditedList.add(transCustomerContractAuditedDB2Bo(customerContractAuditedDB));
        }
        return customerContractAuditedList;
    }

    public static CustomerContractDB transCustomerContract2DB(CustomerContract customerContract) {
        if (customerContract == null) {
            return null;
        }

        CustomerContractDB customerContractDB = new CustomerContractDB();
        TransferUtil.transferAll(customerContract, customerContractDB);
        return customerContractDB;
    }

    public static CustomerSignerDB transCustomerSigner2DB(CustomerSigner customerSigner) {
        if (customerSigner == null) {
            return null;
        }

        CustomerSignerDB customerSignerDB = new CustomerSignerDB();
        TransferUtil.transferAll(customerSigner, customerSignerDB);
        return customerSignerDB;
    }

    public static List<CustomerSignerDB> transCustomerSignerList2DBList(List<CustomerSigner> customerSignerList) {
        if (CollectionUtils.isEmpty(customerSignerList)) {
            return Lists.newArrayList();
        }

        List<CustomerSignerDB> customerSignerDBList = Lists.newArrayList();
        for (CustomerSigner customerSigner : customerSignerList) {
            customerSignerDBList.add(transCustomerSigner2DB(customerSigner));
        }
        return customerSignerDBList;
    }

    public static CustomerSigner transCustomerSignerDB2Bo(CustomerSignerDB customerSignerDB) {
        if (customerSignerDB == null) {
            return null;
        }

        CustomerSigner customerSigner = new CustomerSigner();
        TransferUtil.transferAll(customerSignerDB, customerSigner);
        return customerSigner;
    }

    public static List<CustomerSigner> transCustomerSignerDBList2BOList(List<CustomerSignerDB> customerSignerDBList) {
        if (CollectionUtils.isEmpty(customerSignerDBList)) {
            return Lists.newArrayList();
        }

        List<CustomerSigner> customerSignerList = Lists.newArrayList();
        for (CustomerSignerDB  customerSignerDB : customerSignerDBList) {
            customerSignerList.add(transCustomerSignerDB2Bo(customerSignerDB));
        }
        return customerSignerList;
    }

    public static List<CustomerSignerAudited> transCustomerSignerAuditedDBList2BoList(List<CustomerSignerAuditedDB> customerSignerAuditedDBList) {
        if (CollectionUtils.isEmpty(customerSignerAuditedDBList)) {
            return Lists.newArrayList();
        }

        List<CustomerSignerAudited> customerSignerAuditedList = Lists.newArrayList();
        for (CustomerSignerAuditedDB customerSignerAuditedDB : customerSignerAuditedDBList) {
            customerSignerAuditedList.add(transCustomerSignerAuditedDB2Bo(customerSignerAuditedDB));
        }
        return customerSignerAuditedList;
    }

    private static CustomerSignerAudited transCustomerSignerAuditedDB2Bo(CustomerSignerAuditedDB customerSignerAuditedDB) {
        if (customerSignerAuditedDB == null) {
            return null;
        }

        CustomerSignerAudited customerSignerAudited = new CustomerSignerAudited();
        TransferUtil.transferAll(customerSignerAuditedDB, customerSignerAudited);
        return customerSignerAudited;
    }

    public static CustomerContractAuditedDB transCustomerContractAudited2DB(CustomerContractAudited customerContractAudited) {
        if (customerContractAudited == null) {
            return null;
        }

        CustomerContractAuditedDB customerContractAuditedDB = new CustomerContractAuditedDB();
        TransferUtil.transferAll(customerContractAudited, customerContractAuditedDB);
        return customerContractAuditedDB;
    }

    public static List<CustomerSignerAuditedDB> transCustomerSignerAuditedList2DBList(List<CustomerSignerAudited> customerSignerAuditedList) {
        if (CollectionUtils.isEmpty(customerSignerAuditedList)) {
            return Lists.newArrayList();
        }

        List<CustomerSignerAuditedDB> customerSignerAuditedDBList = Lists.newArrayList();
        for (CustomerSignerAudited customerSignerAudited : customerSignerAuditedList) {
            customerSignerAuditedDBList.add(transCustomerSignerAudited2DB(customerSignerAudited));
        }
        return customerSignerAuditedDBList;
    }

    private static CustomerSignerAuditedDB transCustomerSignerAudited2DB(CustomerSignerAudited customerSignerAudited) {
        if (customerSignerAudited == null) {
            return null;
        }

        CustomerSignerAuditedDB customerSignerAuditedDB = new CustomerSignerAuditedDB();
        TransferUtil.transferAll(customerSignerAudited, customerSignerAuditedDB);
        return customerSignerAuditedDB;
    }

    public static CustomerSettleDB transCustomerSettle2DB(CustomerSettle customerSettle) {
        if (customerSettle == null) {
            return  null;
        }

        CustomerSettleDB customerSettleDB = new CustomerSettleDB();
        TransferUtil.transferAll(customerSettle, customerSettleDB);
        return customerSettleDB;
    }

    public static List<CustomerSettleDB> transCustomerSettleList2DBList(List<CustomerSettle> customerSettleList) {
        if (CollectionUtils.isEmpty(customerSettleList)) {
            return Lists.newArrayList();
        }

        List<CustomerSettleDB> customerSettleDBList = Lists.newArrayList();
        for (CustomerSettle customerSettle : customerSettleList) {
            customerSettleDBList.add(transCustomerSettle2DB(customerSettle));
        }
        return customerSettleDBList;
    }

    public static CustomerSettle transCustomerSettleDB2Bo(CustomerSettleDB customerSettleDB) {
        if (customerSettleDB == null) {
            return null;
        }

        CustomerSettle customerSettle = new CustomerSettle();
        TransferUtil.transferAll(customerSettleDB, customerSettle);
        return customerSettle;
    }

    public static List<CustomerSettle> transCustomerSettleDBList2BoList(List<CustomerSettleDB> customerSettleDBList) {
        if (CollectionUtils.isEmpty(customerSettleDBList)) {
            return Lists.newArrayList();
        }

        List<CustomerSettle> customerSettleList = Lists.newArrayList();
        for (CustomerSettleDB customerSettleDB : customerSettleDBList) {
            customerSettleList.add(transCustomerSettleDB2Bo(customerSettleDB));
        }
        return customerSettleList;
    }

    public static CustomerSettleAudited transCustomerSettleAuditedDB2Bo(CustomerSettleAuditedDB customerSettleAuditedDB) {
        if (customerSettleAuditedDB == null) {
            return null;
        }

        CustomerSettleAudited customerSettleAudited = new CustomerSettleAudited();
        TransferUtil.transferAll(customerSettleAuditedDB, customerSettleAudited);
        return customerSettleAudited;
    }

    public static List<CustomerSettlePoi> transCustomerSettlePoiDBList2BoList(List<CustomerSettlePoiDB> customerSettlePoiDBList) {
        if (CollectionUtils.isEmpty(customerSettlePoiDBList)) {
            return Lists.newArrayList();
        }

        List<CustomerSettlePoi> customerSettlePoiList = Lists.newArrayList();
        for (CustomerSettlePoiDB customerSettlePoiDB : customerSettlePoiDBList) {
            customerSettlePoiList.add(transCustomerSettlePoiDB2Bo(customerSettlePoiDB));
        }
        return customerSettlePoiList;
    }

    private static CustomerSettlePoi transCustomerSettlePoiDB2Bo(CustomerSettlePoiDB customerSettlePoiDB) {
        if (customerSettlePoiDB == null) {
            return null;
        }

        CustomerSettlePoi customerSettlePoi = new CustomerSettlePoi();
        TransferUtil.transferAll(customerSettlePoiDB, customerSettlePoi);
        return customerSettlePoi;
    }



    public static CustomerSettleAuditedDB transCustomerSettleAudited2DB(CustomerSettleAudited customerSettleAudited) {
        if (customerSettleAudited == null) {
            return null;
        }

        CustomerSettleAuditedDB customerSettleAuditedDB = new CustomerSettleAuditedDB();
        TransferUtil.transferAll(customerSettleAudited, customerSettleAuditedDB);
        return customerSettleAuditedDB;
    }
}
