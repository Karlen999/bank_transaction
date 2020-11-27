package com.bdg.bank_transaction;

import org.h2.tools.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.bdg.bank_transaction.dao.DAOFactory;
import com.bdg.bank_transaction.service.AccountService;
import com.bdg.bank_transaction.service.ServiceExceptionMapper;
import com.bdg.bank_transaction.service.TransactionService;
import com.bdg.bank_transaction.service.UserService;

import org.apache.log4j.Logger;


@SpringBootApplication
public class BankTransactionApplication {

    public static void main(String[] args) {

        SpringApplication.run(BankTransactionApplication.class, args);

        // Initialize H2 database with demo data
        log.info("Initialize demo .....");
        DAOFactory h2DaoFactory = DAOFactory.getDAOFactory(DAOFactory.H2);
        h2DaoFactory.populateTestData();
        log.info("Initialisation Complete....");
        // Host service on jetty
        startService();
    }

    private static void startService() throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                UserService.class.getCanonicalName() + "," + AccountService.class.getCanonicalName() + ","
                        + ServiceExceptionMapper.class.getCanonicalName() + ","
                        + TransactionService.class.getCanonicalName());
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}
