package br.com.vitordel.avanaderpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AvanadeRpgApplication {

    public static void main(String[] args) throws Throwable{
        SpringApplication.run(AvanadeRpgApplication.class, args);
    }

}
