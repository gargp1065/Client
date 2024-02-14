package connection;


import config.AppConfig;
import config.CommonConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySqlConnection {

    private static final Logger logger = LogManager.getLogger(MySqlConnection.class);

    public Connection getConnection(String passwordDecryptor) throws Exception {

        Connection conn = null;
//        appConfig = new AppConfig();
        try {

            final String JDBC_DRIVER = CommonConfig.getProperty("jdbc_driver").trim();

            final String DB_URL = CommonConfig.getProperty("db_url").trim();

            final String USER = CommonConfig.getProperty("dbUsername");

//            final String passwordDecryptor = .getProperty("password_decryptor").trim();

            final String PASS = getPassword(passwordDecryptor);
//            logger.info("info related to db driver:{} url: {} user:{} pass: {}", JDBC_DRIVER, DB_URL, USER, PASS);
            logger.info("Connection  Init " + java.time.LocalDateTime.now());
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            logger.info("Connection created successfully " + conn + " .. " + java.time.LocalDateTime.now());
            return conn;
        } catch (Exception e) {
            logger.error(" Error : : " + e + " :  " + java.time.LocalDateTime.now() );
            System.exit(100);
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.error(" SQLException : " + ex + " :  " + java.time.LocalDateTime.now());
            }
            System.exit(100);
            return null;
        }
    }
    String getPassword(final String passwordDecryptor) {
        String passwordDecryptorNew =passwordDecryptor.replace("${APP_HOME}", System.getenv("APP_HOME"));
        logger.info("Decrypting Password");
        String line = null;
        String response = null;
        try {
            String cmd = "java -jar " + passwordDecryptorNew + " spring.datasource.password";
            logger.debug("cmd to  run::" + cmd);
            Process pro = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(pro.getInputStream()));
            while ((line = in.readLine()) != null) {
//                logger.info("Response::" + line);
                response = line;
            }
            return response;
        } catch (Exception e) {
            logger.info("Error  getPassword " + e);
            e.printStackTrace();
            return null;
        }
    }
}