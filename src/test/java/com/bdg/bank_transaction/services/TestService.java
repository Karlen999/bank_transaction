package com.bdg.bank_transaction.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bdg.bank_transaction.dao.DAOFactory;
import com.bdg.bank_transaction.service.AccountService;
import com.bdg.bank_transaction.service.ServiceExceptionMapper;
import com.bdg.bank_transaction.service.TransactionService;
import com.bdg.bank_transaction.service.UserService;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mortbay.jetty.servlet.ServletHolder;

import static sun.net.www.protocol.http.AuthCacheValue.Type.Server;

public class TestService {
    protected static org.h2.tools.Server server = null;
    protected static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    protected static HttpClient client ;
    protected static DAOFactory h2DaoFactory =
            DAOFactory.getDAOFactory(DAOFactory.H2);
    protected ObjectMapper mapper = new ObjectMapper();
    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8084");


    @BeforeClass
    public static void setup() throws Exception {
        h2DaoFactory.populateTestData();
        startServer();
        connManager.setDefaultMaxPerRoute(100);
        connManager.setMaxTotal(200);
        client= HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionManagerShared(true)
                .build();

    }

    @AfterClass
    public static void closeClient() throws Exception {
        //server.stop();
        HttpClientUtils.closeQuietly(client);
    }


    private static <ServletContextHandler> void startServer() throws Exception {
        if (server == null) {
            server = new Server(8084);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                    UserService.class.getCanonicalName() + "," +
                            AccountService.class.getCanonicalName() + "," +
                            ServiceExceptionMapper.class.getCanonicalName() + "," +
                            TransactionService.class.getCanonicalName());
            server.start();
        }
    }
}
