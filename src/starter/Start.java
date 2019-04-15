package starter;

import config.Const;
import dao.RoomsDAO;
import dao.UserDAO;
import model.Room;
import model.User;

import java.lang.String;
import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Start extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ArrayList<ClientThread> clientsConnected = new ArrayList<ClientThread>();
    private boolean keepGoing = true;
    private static int connectCount = 0;
    private ResultSet result = null;

    public static void main(String[] arg) {
        Start server = new Start();
        server.start();
    }

    @Override
    public void run() {
        startServer();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(Const.PORT);
            System.out.println("start started in Thread - " + Thread.currentThread().getName() + "\n" +
                    "Waiting for connection....");

            while (keepGoing) {
                System.out.println(connectCount + " - connect");
                clientSocket = serverSocket.accept();
                connectCount++;
                System.out.println("connection " + connectCount + " established....");

                ClientThread client = new ClientThread(clientSocket);
                clientsConnected.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IOException in StartServer.run()");
        }
    }

    class ClientThread extends Thread {
        Socket clientSocket;
        UserDAO userDAO = new UserDAO();
        RoomsDAO roomsDAO = new RoomsDAO();
        ObjectInputStream input = null;
        ObjectOutputStream output = null;

        ClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                input = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (clientSocket.isConnected()) {
                try {
                    String clientMessage = (String)input.readObject();
                    System.out.println(clientMessage);
                    String[] message = clientMessage.split(" ", 2);
                    String commandNumberStr = message[0];
                    String command = message[1];

                    switch (commandNumberStr){
                        case "usersTable":
                            post(userDAO.getAll());
                            break;
                        case "logIn": {
                            String[] values = command.split(" ", 2);
                            List<User> users = userDAO.getAll();
                            User user = null;
                            for (User temp : users){
                                if(temp.getLogin().equals(values[0]) && temp.getPassword().equals(values[1])){
                                    user = temp;
                                    break;
                                }
                            }
                            post(user);
                            break;
                        }

                        case "sighUp": {
                            String[] valuesSighUp = command.split(" ", 3);
                            List<User> users = userDAO.getAll();
                            for(User user : users){
                                if(user.getLogin().equals(valuesSighUp[1])){
                                    post(null);
                                    break;
                                }
                            }

                            User user = new User();
                            user.setName(valuesSighUp[0]);
                            user.setLogin(valuesSighUp[1]);
                            user.setPassword(valuesSighUp[2]);
                            user.setRole("user");

                            userDAO.create(user);

                            List<User> usersNew = userDAO.getAll();

                            for(User user1 : usersNew){
                                if(user1.getLogin().equals(valuesSighUp[1])){
                                    post(user1);
                                    break;
                                }
                            }
                        }

                        case "addUser":
                            String[] valuesSighUp = command.split(" ", 5);
                            User user = null;
                            Boolean test = null;

                            if(valuesSighUp[0].equals("null")){
                                user = new User(0, valuesSighUp[1], valuesSighUp[2], valuesSighUp[3], valuesSighUp[4]);
                                test = userDAO.create(user);
                            }else{
                                user = new User(Integer.parseInt(valuesSighUp[0]), valuesSighUp[1], valuesSighUp[2], valuesSighUp[3], valuesSighUp[4]);
                                test = userDAO.update(user);
                            }

                            post(test);
                            break;

                        case "getHostelNumber": {
                            valuesSighUp = command.split(" ", 2);

                            post(roomsDAO.getAll());
                            break;
                        }

                        case "deleteUser":{
                            post(userDAO.delete(Integer.parseInt(command)));
                        }

                        case "discountsTable": {
                            //post(dao.getAllUsers());
                            break;
                        }


                        case "mastersTable": {
                            //post(dao.getAllUsers());
                            break;
                        }

                        case "proceduresTable": {
                            //post(dao.getAllUsers());
                            break;
                        }

                        case "timetableTable": {
                            //post(dao.getAllUsers());
                            break;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        void post(Object obj) {
            try {
                output.writeObject(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Object get() {
            try {
                return input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
