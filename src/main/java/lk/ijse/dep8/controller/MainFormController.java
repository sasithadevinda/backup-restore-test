package lk.ijse.dep8.controller;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.dep8.dto.CustomerDTO;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class MainFormController {
    public TextField txtID;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPath;
    public ImageView imgProfile;
    public Button btnSave;
    public TableView<CustomerDTO> tblList;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colImage;
    public TableColumn colButton;
    public Button btnSelectPath;
    public byte[] imageBytes ;
    public byte[] imageBytes1 ;
    public Path path =Paths.get("database/customer.dep8");
    public void initialize()  {
        initDatabase();
            tblList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           txtID.setText(newValue.getId());
           txtName.setText(newValue.getName());
           txtAddress.setText(newValue.getAddress());
           imageBytes1=newValue.getImage();
            Image image = new Image(new ByteArrayInputStream(newValue.getImage()));
            imgProfile.setImage(image);
            btnSave.setText("Update");
        });

        tblList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblList.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        TableColumn<CustomerDTO, ImageView> imageViewTableColumn= (TableColumn<CustomerDTO, ImageView>) tblList.getColumns().get(3);
        imageViewTableColumn.setCellValueFactory(param -> {


            byte[] picture = param.getValue().getImage();
            ByteArrayInputStream bais = new ByteArrayInputStream(picture);

            ImageView im = new ImageView(new Image(bais));
            im.setFitHeight(75);
            im.setFitWidth(75);
            return new ReadOnlyObjectWrapper<>(im);

        });
        TableColumn<CustomerDTO, Button> buttonTableColumn= (TableColumn<CustomerDTO, Button>)tblList.getColumns().get(4);
       buttonTableColumn.setCellValueFactory(param -> {
            Button delete = new Button("Delete");
          delete.setOnAction(event -> tblList.getItems().remove(param.getValue()));
          return new ReadOnlyObjectWrapper<>(delete);
        });



     /*   try {
          FileInputStream input = new FileInputStream("/home/sasitha/Downloads/images.png");
          //  FileInputStream input = new FileInputStream("/assert/images.png");
            Image image = new Image(input);
           // System.out.println(input);
            imgProfile.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

    }
    public void initDatabase() {

        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path.getParent());
                Files.createFile(path);
            }
            loadAllCustomer();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to Initialize Database").show();
        }

    }


    public boolean isValid(){
        if(!txtID.getText().matches("C\\d{3}")){

            return false;
        }else if(txtName.getText().trim().equals("")||txtID.getText().trim().equals("")||txtAddress.getText().trim().equals(""))
        { return false;}
        return true;
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (!isValid()){
            txtID.selectAll();
            txtID.requestFocus();
            return;}
        CustomerDTO customerDTO;
        if (btnSave.getText().equals("Update")){
          //  byte[] imageTemp =;
            if(!txtPath.getText().trim().equals("")){
                customerDTO = new CustomerDTO(txtID.getText(), txtName.getText(), txtAddress.getText(), imageBytes);

            }else {customerDTO = new CustomerDTO(txtID.getText(), txtName.getText(), txtAddress.getText(), imageBytes1);}



        }else {customerDTO = new CustomerDTO(txtID.getText(), txtName.getText(), txtAddress.getText(), imageBytes);}





        tblList.getItems().add(customerDTO);
        saveCustomer();
        txtName.clear();
        txtID.clear();
        txtAddress.clear();
        txtPath.clear();
        txtID.requestFocus();




    }
    public void loadAllCustomer(){
        try {
            InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ, StandardOpenOption.TRUNCATE_EXISTING);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            tblList.getItems().clear();
            tblList.setItems(FXCollections.observableList((ArrayList<CustomerDTO>)objectInputStream.readObject()));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void saveCustomer(){

        try {
            OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
           objectOutputStream.writeObject(new ArrayList(tblList.getItems()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void btnSelectPathOnAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter =new FileChooser.ExtensionFilter("Images","*.jpg","*.png","jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(txtID.getScene().getWindow());
       txtPath.setText(file.getAbsolutePath());

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            imageBytes = new byte[fileInputStream.available()];
            fileInputStream.read(imageBytes);

            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
imgProfile =new ImageView(new Image(bais));
           // ImageView im = new ImageView(new Image(bais));







        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
