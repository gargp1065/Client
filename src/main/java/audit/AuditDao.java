package audit;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;

public class AuditDao {

    private static final Logger logger = LogManager.getLogger(AuditDao.class);


    public void saveInAuditTable(final int statusCode, final String status, final String errorMessage, final String feature,
                                 final String moduleName, final Connection connection ) throws ClassNotFoundException {

        String serverName=null;
        try {
            serverName
                    = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            logger.error("Exception in finding hostname " + e);
        }
        String query = "insert into aud.modules_audit_trail (status_code, status, error_message, feature_name, module_name, server_name) "
                + "values('" + statusCode + "','" + status + "','" + errorMessage + "','" + feature + "','" + moduleName
                + "','" +serverName + "')";
        logger.info("Insert statement to create a record in audit table = " + query);
        try (Statement stmt = connection.createStatement();) {
            int result=stmt.executeUpdate(query);
            logger.info("Query execution result in audit table = "+ result);
        } catch (SQLException exception) {
            logger.error("Insert in audit table failed with = " + exception);
        }

    }

    public void updateInAuditTable(final int statusCode, final String status, final String errorMessage, final long executionFinishTime, final Connection conn) throws ClassNotFoundException {

        String serverName=null;
        try {
            serverName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            logger.error("Exception in finding hostname " + e);
        }

        String query = "update aud.modules_audit_trail set status_code="+statusCode+",status='"+status+"',error_message='"+errorMessage+"',execution_time='"+executionFinishTime+"' where module_name='EIR HTTP Client' and status_code=201 order by id desc limit 1";
        logger.info("Update statement to update a record in audit table = " + query);

        try (Statement stmt = conn.createStatement();) {
            int result=stmt.executeUpdate(query);
            logger.info("Query execution result to update in audit table = "+result);
        } catch (SQLException exception) {

            logger.error("Update statement to update in audit table failed with " + exception);
        }

    }


    public void updateInAuditTable(final int statusCode, final String status, final String errorMessage,
                                   final long executionFinishTime, final String info, final int count, final Connection conn) throws ClassNotFoundException {

        String serverName=null;
        try {
            serverName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            logger.error("Exception in finding hostname " + e);
        }

        String query = "update aud.modules_audit_trail set status_code="+statusCode+",status='"+status+"',error_message='"+errorMessage+"',execution_time='"+executionFinishTime+"',info='"+info+"',count='"+count+"' where module_name='EIR HTTP Client' and status_code=201 order by id desc limit 1";
        logger.info("Update statement to update a record in audit table = " + query);

        try (Statement stmt = conn.createStatement();) {
            int result=stmt.executeUpdate(query);
            logger.info("Query execution result to update in audit table = "+result);
        } catch (SQLException exception) {

            logger.error("Update statement to update in audit table failed with " + exception);
        }

    }
}
