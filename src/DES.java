import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Base64;


public class DES {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//eee
            // Pradinio teksto įvesties laukas
            System.out.println("Įveskite pradinį tekstą:");
            String plaintext = br.readLine();

            // Slapto rakto įvesties laukas
            System.out.println("Įveskite slapto rakto ilgį (8 simboliai):");
            String keyString = br.readLine();
            while (keyString.length() != 8) {
                System.out.println("Slapto rakto ilgis turi būti 8 simboliai, įveskite iš naujo:");
                keyString = br.readLine();
            }
            byte[] keyData = keyString.getBytes();

            SecretKeySpec secretKey = new SecretKeySpec(keyData, "DES");

            // Šifravimo/dešifravimo pasirinkimas
            System.out.println("Pasirinkite šifravimo (E) arba dešifravimo (D) funkciją:");
            String choice = br.readLine();
            int mode;
            if (choice.equalsIgnoreCase("E")) {
                mode = Cipher.ENCRYPT_MODE;
            } else if (choice.equalsIgnoreCase("D")) {
                mode = Cipher.DECRYPT_MODE;
            } else {
                System.out.println("Neteisingas pasirinkimas.");
                return;
            }

            // Šifravimo modų pasirinkimas
            System.out.println("Pasirinkite šifravimo modą (CBC, CFB):");
            String modeType = br.readLine();
            Cipher cipher;
            if (modeType.equalsIgnoreCase("ECB")) {
                cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            } else if (modeType.equalsIgnoreCase("CBC")) {
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(new byte[8]);
                cipher.init(mode, secretKey, iv);
            } else if (modeType.equalsIgnoreCase("CFB")) {
                cipher = Cipher.getInstance("DES/CFB/PKCS5Padding");
                IvParameterSpec iv = new IvParameterSpec(new byte[8]);
                cipher.init(mode, secretKey, iv);
            } else {
                System.out.println("Neteisingas šifravimo modas.");
                return;
            }

            // Šifravimas/dešifravimas
            byte[] outputBytes;
            if (mode == Cipher.ENCRYPT_MODE) {
                outputBytes = cipher.doFinal(plaintext.getBytes());
            } else {
                outputBytes = cipher.doFinal(Base64.getDecoder().decode(plaintext));
            }

            // Užšifruoto/dešifruoto teksto atsakymo laukas
            System.out.println("Rezultatas: " + (mode == Cipher.ENCRYPT_MODE ? Base64.getEncoder().encodeToString(outputBytes) : new String(outputBytes)));

            // Galimybė užšifruotą tekstą išsaugoti faile
            System.out.println("Ar norite išsaugoti rezultatą faile? (T/N)");
            String saveChoice = br.readLine();
            if (saveChoice.equalsIgnoreCase("T")) {
                System.out.println("Įveskite failo vardą:");
                String fileName = br.readLine();
                Files.write(Paths.get(fileName), outputBytes);
                System.out.println("Rezultatas išsaugotas faile.");
            }

            // Užšifruotojo teksto nuskaitymas iš failo ir jo dešifravimas
            System.out.println("Ar norite atlikti dešifravimą iš failo? (T/N)");
            String decryptChoice = br.readLine();
            if (decryptChoice.equalsIgnoreCase("T")) {
                System.out.println("Įveskite failo vardą:");
                String fileName = br.readLine();
                byte[] fileBytes = Files.readAllBytes(Paths.get(fileName));
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedBytes = cipher.doFinal(fileBytes);
                System.out.println("Dešifruotas tekstas iš failo: " + new String(decryptedBytes));
            }

        } catch (Exception e) {
            System.out.println("Įvyko klaida: " + e.getMessage()
            );
        }
    }
}
