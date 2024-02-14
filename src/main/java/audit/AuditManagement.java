package audit;

import java.sql.Connection;
import java.sql.Date;


public class AuditManagement {

    final private AuditDao auditDao;


    public AuditManagement() {
        auditDao = new AuditDao();
    }


    public void createAudit(final int statusCode, final String status, final String errorMessage, final String feature,
                            final String moduleName, final Connection connection) throws Exception {

        auditDao.saveInAuditTable(statusCode, status, errorMessage, feature, moduleName, connection);
    }

    public void updateAudit(final int statusCode, final String status,
                            final String errorMessage, final long executionFinishTime,
                            final Connection conn) throws ClassNotFoundException {

        auditDao.updateInAuditTable(statusCode, status, errorMessage,  executionFinishTime, conn);
    }

    public void updateAudit(final int statusCode, final String status,
                            final String errorMessage, final long executionFinishTime, final String info, final int count,
                            final Connection conn) throws ClassNotFoundException {

        auditDao.updateInAuditTable(statusCode, status, errorMessage, executionFinishTime, info, count, conn);
    }
}