import java.io.RandomAccessFile;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



public class AddressBook extends Application {

	final static int ID_SIZE = 4;
	final static int NAME_SIZE = 32;
	final static int STREET_SIZE = 32;
	final static int CITY_SIZE = 20;
	final static int GENDER_SIZE = 2;
	final static int ZIP_SIZE = 5;
	final static int RECORD_SIZE = (ID_SIZE+NAME_SIZE+STREET_SIZE+CITY_SIZE+GENDER_SIZE+ZIP_SIZE);
	
	//Text Fields
	private TextField tfid = new TextField();
	private TextField tfname = new TextField(); 
	private TextField tfstreet = new TextField();
	private TextField tfcity = new TextField(); 
	private TextField tfgender = new TextField(); 
	private TextField tfzip = new TextField(); 
	
	//Buttons
	private Button btAdd = new Button("Add");
	private Button btFirst = new Button("First");
	private Button btNext = new Button("Next");
	private Button btPrevious = new Button("Previous");
	private Button btLast = new Button("Last");
	private Button btUpdate = new Button("Update");
	private Button btSearch = new Button("Search");
	
	private RandomAccessFile raf; 
	Alert alert = new Alert(AlertType.INFORMATION);
	
	
	public AddressBook() {
		
		try {
			raf = new RandomAccessFile("addressbook.dat","rw");
		}catch(IOException ex){
			 System.out.print("Error: " + ex);       
			 System.exit(0);   
		}
	}
	
	public int checkGender(String ch) {
		if((ch.charAt(0) == 'f' || ch.charAt(0) == 'F' || ch.charAt(0) == 'm' || ch.charAt(0) == 'M')){
			return 1;
			}else { 
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("\nPlease enter correct gender.(M,m,F,f)");

				alert.showAndWait();
			return -1;
			}
	}
	
	public int checkId(RandomAccessFile raf , String id2) throws IOException {
		
		int y;
		String id ;
		for(y=1 ; y <= raf.length() / (RECORD_SIZE*2) ; y++) {
			raf.seek((y-1)*RECORD_SIZE*2);
			id = FixedLengthStringIO.readFixedLengthString(ID_SIZE,raf);
			
			if(id.trim().replaceAll("\\s+", " ").equalsIgnoreCase(id2.trim().replaceAll("\\s+", " "))) {
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("\nYou have to choose another id.");

				alert.showAndWait();
				raf.seek(raf.getFilePointer()-8);
				return -1;
			}
			raf.seek(raf.getFilePointer()-8);
		}
		return 1;
		
		
	}
	
	public void writeAdresses(RandomAccessFile raf , long pos) {
		
		try {
	
		raf.seek((pos-1)*RECORD_SIZE*2);
		FixedLengthStringIO.writeFixedLengthString(tfid.getText(),ID_SIZE,raf);
		FixedLengthStringIO.writeFixedLengthString(tfname.getText(),NAME_SIZE,raf);
		FixedLengthStringIO.writeFixedLengthString(tfstreet.getText(),STREET_SIZE,raf);
		FixedLengthStringIO.writeFixedLengthString(tfcity.getText(),CITY_SIZE,raf);
		FixedLengthStringIO.writeFixedLengthString(tfgender.getText(),GENDER_SIZE,raf);
		FixedLengthStringIO.writeFixedLengthString(tfzip.getText(),ZIP_SIZE,raf);
 
		
		}catch(IOException ex) {
			 ex.printStackTrace(); 
		}
	} //end write addresses

	
	 public void readAddress(long position) {
		 
		 try {
			 raf.seek(position); 
			 
			 String id = FixedLengthStringIO.readFixedLengthString(ID_SIZE,raf);
			 String name = FixedLengthStringIO.readFixedLengthString(NAME_SIZE,raf);
			 String street = FixedLengthStringIO.readFixedLengthString(STREET_SIZE,raf);
			 String city = FixedLengthStringIO.readFixedLengthString(CITY_SIZE,raf);
			 String gender = FixedLengthStringIO.readFixedLengthString(GENDER_SIZE,raf);
			 String zip = FixedLengthStringIO.readFixedLengthString(ZIP_SIZE,raf);
			 
			 tfid.setText(id);
			 tfname.setText(name);
			 tfstreet.setText(street);
			 tfcity.setText(city);
			 tfgender.setText(gender);
			 tfzip.setText(zip);
			 
		 }catch(IOException ex) {
			 ex.printStackTrace(); 
		}
	 } //end read adresses
	
	
	 
		@Override
		public void start(Stage primaryStage) throws Exception {
			
			GridPane pane = new GridPane();
			HBox hbox = new HBox();
			HBox hbox2 = new HBox();
			HBox hbox3 = new HBox();
			HBox hbox4 = new HBox();
			HBox hbox5 = new HBox();
			HBox hboxex = new HBox();
			
			pane.setAlignment(Pos.CENTER);
			pane.setPadding(new Insets(18,20,16,20));
			pane.setHgap(5);
			pane.setVgap(5);
			
			Label lbid = new Label("ID");
			Label lbname = new Label("Name");
			Label lbstreet = new Label("Street");
			Label lbcity = new Label("City");
			Label lbgender = new Label("Gender");
			Label lbzip = new Label("Zip");
			
			tfid.setPrefColumnCount(27);
			tfname.setPrefColumnCount(27);
			tfstreet.setPrefColumnCount(27);
			tfcity.setPrefColumnCount(12);
			tfgender.setPrefColumnCount(2);
			tfzip.setPrefColumnCount(4);
			
			hbox.getChildren().addAll(lbid,tfid);
			hbox2.getChildren().addAll(lbname,tfname);
			hbox3.getChildren().addAll(lbstreet,tfstreet);
			hbox4.getChildren().addAll(hboxex,lbgender,tfgender,lbzip,tfzip);
			hboxex.getChildren().addAll(lbcity,tfcity);
			hbox5.getChildren().addAll(btAdd,btFirst,btNext,btPrevious,btLast,btUpdate,btSearch);
			
			hbox.setSpacing(30);
			hbox2.setSpacing(10);
			hbox3.setSpacing(10);
			hbox4.setSpacing(4);
			hbox5.setSpacing(6);
			hboxex.setSpacing(21);
			
			pane.add(hbox , 0, 0); 
			pane.add(hbox2, 0, 1);
			pane.add(hbox3, 0, 2);
			pane.add(hbox4, 0, 3);
			pane.add(hbox5, 0, 4);
			
			Scene scene = new Scene(pane);
			primaryStage.setTitle("Address Book");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			
			btAdd.setOnAction(new EventHandler<ActionEvent>() {
				 
				 @Override
				 public void handle(ActionEvent e) {
					 try {
						 
						 String id = tfid.getText();
						 String gender = tfgender.getText();
						
						if(checkId(raf , id) == 1 && checkGender(gender) == 1) {
						 writeAdresses(raf , (raf.length() / (RECORD_SIZE*2)+1));
						
						 alert.setTitle("Information Dialog");
						 alert.setHeaderText(null);
						 alert.setContentText("\nThe record added successfully.");

						 alert.showAndWait();
						 }
						raf.seek((raf.getFilePointer()+2*RECORD_SIZE));
					
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				 }
			 });
			
			btFirst.setOnAction(new EventHandler<ActionEvent>() {
				 
				 @Override
				 public void handle(ActionEvent e) {
					 
				 try{
					 if(raf.length() > 0) readAddress(0);
				 }
				 catch(IOException ex) {
					 ex.printStackTrace(); 
					 }
				 }
			 });
			
			btNext.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					try {
						long currentPosition = raf.getFilePointer();
						if(currentPosition < raf.length()) {
							readAddress(currentPosition);
						}
					}//end try
						catch(IOException ex){
							ex.printStackTrace(); 
						}
					}
				});
			
			btPrevious.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					
					try {
						long currentPosition = raf.getFilePointer();
						if(currentPosition - 2*RECORD_SIZE > 0) {
							readAddress(currentPosition - 2 * 2 * RECORD_SIZE); 	
						}else{
							readAddress(0);
						}
						
					}//end try
					catch(IOException ex) {
						ex.printStackTrace(); 
						}
					}
				});
			
			btLast.setOnAction(new EventHandler<ActionEvent>() {
			
				@Override
				public void handle(ActionEvent e) {
				
					try {
					long lastPosition = raf.length();
					if(lastPosition > 0) {
						readAddress(lastPosition - 2 * RECORD_SIZE); 
						}
					}catch(IOException ex) {
						ex.printStackTrace(); 
						}
					}
				});
			
			
			btSearch.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					
				try{
					if(raf.length() > 0) {
						
						String num = tfid.getText();
						String id;
						int y;
						int flag = 1;
						
						for(y=1 ; y <= raf.length() / (RECORD_SIZE*2) ; y++) {
							
							raf.seek((y-1)*RECORD_SIZE*2);
							id = FixedLengthStringIO.readFixedLengthString(ID_SIZE,raf);
							raf.seek(raf.getFilePointer()-8);
							
							if(id.trim().replaceAll("\\s+", " ").equalsIgnoreCase(num.trim().replaceAll("\\s+", " "))) {
								long position = raf.getFilePointer();
								readAddress(position);
								flag = 0;
								break;
							}
						}
					
						if(flag == 1) {
							raf.seek(0);
							alert.setTitle("Error Dialog");
							alert.setHeaderText(null);
							alert.setContentText("\nRecord could not be found.");
							alert.showAndWait();
						}
						
					}else {
						System.out.println(raf.getFilePointer());
						alert.setTitle("Error Dialog");
						alert.setHeaderText(null);
						alert.setContentText("\nThere is no record in the file.");

						alert.showAndWait();
					}
				
				}catch(IOException ex) {
					ex.printStackTrace(); 
					}
				}
			});
			
			
			btUpdate.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent e) {
					
					try{
						if(raf.length() > 0) {
							long position = raf.getFilePointer()-2*RECORD_SIZE;
							
							writeAdresses(raf,((position/(2*RECORD_SIZE))+ 1));

							alert.setTitle("Information Dialog");
							alert.setHeaderText(null);
							alert.setContentText("\nThe record updated successfully.");

							alert.showAndWait();
						}else {
							
							alert.setTitle("Error Dialog");
							alert.setHeaderText(null);
							alert.setContentText("\nOoops, there was an error!");

							alert.showAndWait();
						}
						
					}catch(IOException ex){
						ex.printStackTrace(); 
					}
					
				}
				
			});
			
			//to show the first record in the file
			try {
				if(raf.length() > 0)  readAddress(0);
			}catch(IOException ex) {
				ex.printStackTrace(); 
			}
			
			
			
			

		}//end 
		
		
	
	public static void main(String []args){
		Application.launch(args);
			
	}

	
}
	

