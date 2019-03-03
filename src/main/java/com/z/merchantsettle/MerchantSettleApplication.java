package com.z.merchantsettle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.z.merchantsettle")
@MapperScan("com.z.merchantsettle.modules.*.dao")
public class MerchantSettleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MerchantSettleApplication.class, args);
	}

}

