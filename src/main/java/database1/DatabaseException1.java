/*
Êý¾Ý¿â
*/
package database1;

import java.sql.SQLException;

public class DatabaseException1 extends RuntimeException {

    private static final long serialVersionUID = -420103154764822555L;

    public DatabaseException1(String msg) {
        super(msg);
    }

    public DatabaseException1(String message, Throwable cause) {
        super(message, cause);
    }

    DatabaseException1(SQLException e) {
        throw new UnsupportedOperationException(e); //To change body of generated methods, choose Tools | Templates.
    }
}
