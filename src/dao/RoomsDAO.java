package dao;

import model.Room;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RoomsDAO extends EntityDAO<Room, Integer> {

    private static final String SQL_UPDATE_USER_BY_ID =
            "UPDATE users SET roleID = ?, login = ?, password = ?, name = ? WHERE id = ?";

    private static final String SQL_GET_USER_BY_ID =
            "SELECT * FROM users WHERE id = ?";

    private static final String SQL_CREATE_USER =
            "INSERT INTO users (roleID, login, password, name) VALUES (?,?,?,?)";

    private static final String SQL_DELETE_USER_BY_ID =
            "DELETE FROM users WHERE id = ?";

    private static final String SQL_GET_ALL =
            "SELECT rooms.number, types_rooms.name, types_rooms.price FROM rooms JOIN types_rooms ON rooms.type = types_rooms.id WHERE rooms.isBooked = 0;";

    @Override
    public List<Room> getAll() {
        List<Room> list = new LinkedList<>();
        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt(1));
                room.setType(rs.getString(2));
                room.setPrice(rs.getInt(3));

                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return list;
    }

    @Override
    public boolean update(Room entity) {
        return false;
    }

    @Override
    public Room getEntityById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean create(Room entity) {
        return false;
    }
}
