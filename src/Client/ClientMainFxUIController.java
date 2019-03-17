package Client;

import static Client.ClientLoginFxController.client;
import static Client.ClientMain.stage;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClientMainFxUIController implements Initializable {

    @FXML
    Label ChatTitle;

    @FXML
    Button Send;

    @FXML
    Button NewGroupChat;

    @FXML
    TreeView<String> ChatListTree;

    @FXML
    TextArea userInput;

    @FXML
    ListView ChatMessages;

    @FXML
    SplitPane splitPane;

    TreeItem<String> root, PublicChat, onlineUser, groupChats;

    TreeItem<String> currentSelection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stage.setOnCloseRequest(e -> {
            e.consume();
            shutDown();
        });

        //Root
        root = new TreeItem<>();
        root.setExpanded(true);

        //All
        PublicChat = makeBranch("PublicChat", root);

        //OnlineUser
        onlineUser = makeBranch("OnlineUser", root);

        //Groups
        groupChats = makeBranch("Groups", root);


        ChatListTree.setRoot(root);
        ChatListTree.setShowRoot(false);
        ChatListTree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue.isLeaf()) {
                if (newValue.getParent().equals(onlineUser)) {
                    client.setCurrentSelectedUser(newValue.getValue());
                    currentSelection = newValue;
                    updateChatView();
                    System.out.println("已选中: " + newValue.getValue());
                } else if (newValue.getParent().equals(groupChats)) {
                    client.setCurrentSelectedGroup(
                            Integer.parseInt(newValue.getValue().substring(
                                    newValue.getValue().indexOf(".") + 1, newValue.getValue().indexOf(":"))));
                    currentSelection = newValue;
                    updateChatView();
                    System.out.println("已选中: " + newValue.getValue());
                } else if (newValue.equals(PublicChat)) {
                    currentSelection = newValue;
                    updateChatView();
                    System.out.println("已选中: " + newValue.getValue());
                } else currentSelection = null;
            }
        });

        updateUserList();

        client.addUserListListener(this::updateUserList);

        client.addMessageListener(this::updateChatView);

    }

    @FXML
    public void SendMessage() {
        if (!userInput.getText().isEmpty()) {
            String messageLog = getTime() + "\n[" + client.getUser() + "]\n" + userInput.getText();

            if (currentSelection.getParent().equals(onlineUser)) {
                client.recordSingleMessageLog(client.getCurrentSelectedUser(), messageLog);
                client.sendToSingle(client.getCurrentSelectedUser(), userInput.getText());

            } else if (currentSelection.getParent().equals(groupChats)) {
                client.recordGroupMessageLog(client.getCurrentSelectedGroup(), messageLog);
                client.sendToGroup(client.getCurrentSelectedGroup(), userInput.getText());

            } else {
                client.recordPublicMessageLog(messageLog);
                client.sendToAll(userInput.getText());
            }
        }
        updateChatView();
    }

    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }


    public void updateUserList() {
        Platform.runLater(() -> {

            //clear all
            onlineUser.getChildren().clear();
            groupChats.getChildren().clear();

            //update online users list
            ArrayList<String> userList = new ArrayList<>(client.getOnlineUserList());
            userList.remove(client.getUser());
            if (!client.getOnlineUserList().contains(client.getCurrentSelectedUser())) {
                ChatMessages.getItems().clear();
                client.setCurrentSelectedUser(null);
            }
            for (String user : userList) {
                makeBranch(user, onlineUser);
            }

            //update group chats list
            Hashtable<Integer, List<String>> joinedGroups = new Hashtable<>(client.getJoinedGroups());
            ArrayList<Integer> groupList = new ArrayList<>(joinedGroups.keySet());
            if (!client.getJoinedGroups().keySet().contains(client.getCurrentSelectedGroup())) {
                ChatMessages.getItems().clear();
                client.setCurrentSelectedGroup(-1);
            }
            for (int i : groupList) {
                makeBranch("ID." + i + ":" + joinedGroups.get(i).get(0), groupChats);
            }
        });
    }

    public void updateChatView() {
        Platform.runLater(() -> {

            ObservableList<String> conversations;

            if (currentSelection.getParent().equals(onlineUser)) {
                conversations = FXCollections.observableArrayList(
                        client.getSingleMessageLog(client.getCurrentSelectedUser()));
                ChatTitle.setText(client.getCurrentSelectedUser());

            } else if (currentSelection.getParent().equals(groupChats)) {
                conversations = FXCollections.observableArrayList(
                        client.getGroupMessageLog(client.getCurrentSelectedGroup()));
                ChatTitle.setText(client.getJoinedGroups().get(client.getCurrentSelectedGroup()).get(0));

            } else {
                conversations = FXCollections.observableArrayList(client.getPublicMessageLog());
                ChatTitle.setText("Public ChatRoom");

            }

            ChatMessages.setItems(conversations);
            ChatMessages.scrollTo(conversations.size() - 1);

        });
    }


    public void shutDown() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?",
                ButtonType.YES, ButtonType.CANCEL);
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.YES) {
            client.close();
            stage.close();
            System.exit(0);
        }
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        System.out.println("现在时间：" + sdf.format(date)); // 输出已经格式化的现在时间（24小时制）
        return sdf.format(date);
    }

}
