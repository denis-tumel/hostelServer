package dao;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO extends EntityDAO<User, Integer> {

    private static final String SQL_UPDATE_USER_BY_ID =
            "UPDATE users SET login = ?, password = ?, first_name = ?, last_name=?, phone_number=? WHERE id_user = ?";

    private static final String SQL_GET_USER_BY_ID =
            "SELECT * FROM User WHERE id_user=?";

    private static final String SQL_CREATE_USER =
            "INSERT INTO users (roleID, login, password, name) VALUES (?,?,?,?)";

    private static final String SQL_DELETE_USER_BY_ID =
            "DELETE FROM User WHERE id_user=?";

    private static final String SQL_GET_ALL =
            "SELECT * from users;";

    @Override
    public List<User> getAll() {
        List<User> list = new LinkedList<>();
        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setLogin(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setRole(rs.getInt(5));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return list;
    }

    @Override
    public User update(User user) {
        PreparedStatement ps = getPrepareStatement(SQL_UPDATE_USER_BY_ID);
        try {
            ps.setInt(1, user.getRole());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getName());
            ps.setInt(5, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getEntityById(user.getId());
    }

    @Override
    public User getEntityById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
/*
        PreparedStatement st = getPrepareStatement(SQL_DELETE_USER_BY_ID);

        try {
            st.setObject(1, id);
            int i = st.executeUpdate();
            isRemoved = i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/


        return false;
    }

    @Override
    public boolean create(User user) {
        PreparedStatement ps = getPrepareStatement(SQL_CREATE_USER);
        try {
            ps.setInt(1, 1);
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
