package fyodor.repository;

import com.sun.rowset.internal.Row;
import fyodor.model.User;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OverallDao {

    private Connection connect;

    public OverallDao() {
        try {
            connect = ConnectorDB.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String sqlRequest(String request) {
        StringBuilder response = new StringBuilder("");
        try {
            PreparedStatement ps = connect.prepareStatement(request);
            ResultSet rs;
            Pattern selectPattern = Pattern.compile("[sS][eE][lL][eE][cC][tT].*");
            Pattern describePattern = Pattern.compile("[dD][eE][sS][cC][rR][iI][bB][eE].*");
            Pattern insertPattern = Pattern.compile("[iI][nN][sS][eE][rR][tT].*");
            Pattern deletePattern = Pattern.compile("[dD][eE][lL][eE][tT][eE].*");
            Pattern updatePattern = Pattern.compile("[uU][pP][dD][aA][tT][eE].*");
            if (selectPattern.matcher(request).matches() || describePattern.matcher(request).matches()) {
                rs = ps.executeQuery();

                while (rs.next()) {
                    for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                        response.append(rs.getObject(i + 1));
                        response.append("   ");
                    }
                    response.append("\n");
                }
            }
            else if (insertPattern.matcher(request).matches() || deletePattern.matcher(request).matches() ||
                    updatePattern.matcher(request).matches()) {
                ps.executeUpdate(request);
                response.append("Request was executed");
            } else {
                response.append("Prohibited operation");
            }
        } catch (SQLException e) {
            response.append(e.toString());
        }

        return response.toString();
    }
}
