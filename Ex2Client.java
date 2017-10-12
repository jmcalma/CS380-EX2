import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class Ex2Client {

	public static void main(String[] args) {

		try (Socket socket = new Socket("18.221.102.182", 38102)) {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			CRC32 code = new CRC32();
			byte[] data = new byte[100];
			byte firstHalf, secondHalf, completeB;
			long val;
			int match;

			System.out.println("Connected to server.");
			System.out.println("Received bytes:");
			for (int i = 0; i < 100; i++) {
				firstHalf = (byte)(is.read());
				secondHalf = (byte)(is.read());
				firstHalf = (byte)((firstHalf << 4));
				completeB = (byte)((firstHalf + secondHalf));
				data[i] = completeB;
				if ((i % 10 == 0) && (i != 0)) {
					System.out.println();
				}
				System.out.printf("%02X", data[i]);
			}

			code.update(data);
			val = code.getValue();
			System.out.printf("\nGenerated CRC32: %02X.", val);
			for(int i = 3; i >= 0; i--) {
				os.write((int)val >> (8 * i));
			}
			
			match = is.read();
			if (match == 1) {
				System.out.println("\nResponse good.");
			} else if (match == 0){
				System.out.println("\nIncorrect response.");
			}
			System.out.println("Disconnected from server.");
			socket.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}