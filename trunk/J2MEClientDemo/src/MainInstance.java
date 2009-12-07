import java.io.IOException;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.glu.rpc.service.User;


public class MainInstance extends MIDlet {

	public MainInstance() {
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
//		final Logger logger = LoggerFactory.getLogger();
//		final CanvasAppender appender = new CanvasAppender();
//		logger.addAppender(appender);
		
		try {
			System.out.println("Creating user object");
			final User user = User.newBuilder().setUserName("yubingxing").setPassword("123456")
			.setEmail("yubingxing123@gmail.com").build();
			System.out.println("User : " + user.toString());
			
			System.out.println("Creating byte array from example object");
			byte[] data = user.toByteArray();
			System.out.println("Length of byte array : " + data.length);
			
			System.out.println("Parsing byte array back to example object");
			final User user2 = User.parseFrom(data);
			System.out.println("Parsing from bytes array : " + user2.toString());
		} catch (IOException e) {
			System.out.println("Unable to complete the protobuf-javame midlet example");
		}
	}
}
