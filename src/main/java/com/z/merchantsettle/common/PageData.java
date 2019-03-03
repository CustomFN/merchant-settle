package com.z.merchantsettle.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
public class PageData<T> {


    private Integer pageNum = 1;
    private Integer pageSize = 30;
    private Integer totalPage = 1;
    private Integer totalSize = 0;
    private List<T> data = Lists.newArrayList();

    public PageData() {}

    private PageData(Builder<T> builder) {
        this.pageNum = builder.pageNum;
        this.pageSize = builder.pageSize;
        this.totalPage = builder.totalPage;
        this.totalSize = builder.totalSize;
        this.data = builder.data;

    }

    public static class Builder<T> {
        private Integer pageNum = 1;
        private Integer pageSize = 30;
        private Integer totalPage = 0;
        private Integer totalSize = 0;
        private List<T> data = Lists.newArrayList();

        public Builder() {

        }


        public Builder<T> pageNum(Integer pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public Builder<T> pageSize(Integer pageSize){
            this.pageSize = pageSize;
            return this;
        }

        public Builder<T> totalSize(Integer totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public Builder<T> data(List<T> data) {
            this.data = data;
            return this;
        }

        public Builder<T> totalPage(Integer totalPage) {
            this.totalPage = totalPage;
            return this;
        }

        public PageData<T> build() {
           return new PageData<>(this);
        }
    }
}
