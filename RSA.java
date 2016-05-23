/*

Console output for program. 
==> Handles the creation of keys.
==> Performs all the RSA tasks. i.e., Blocking the file, unblocking the file, encryption and decrytion of block file.  

Authors : Dhrumil Patel, Kena Patel
*/


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RSA {

    public static LargeInteger modPow(final LargeInteger a, final LargeInteger p, final LargeInteger mod) {
        LargeInteger ret = new LargeInteger(1);
        LargeInteger n = new LargeInteger(a);
        LargeInteger pp = new LargeInteger(p);
        while (LargeInteger.compare(pp, new LargeInteger(0)) > 0) {
            if (!pp.isEven()) {
                ret.multiply(n);
                if (ret.toString().length() > 50 && LargeInteger.compare(ret, mod) >= 0) ret = ret.mod(mod);
            }

            pp = LargeInteger.divideBy2(pp);
            n.multiply(n);

            if (n.toString().length() > 50 && LargeInteger.compare(n, mod) >= 0) n = n.mod(mod);

        }
        return ret.mod(mod);
    }

    public static LargeInteger inverse(LargeInteger a, LargeInteger mod) {
        LargeInteger t = new LargeInteger(0);
        LargeInteger nt = new LargeInteger(1);
        LargeInteger r = new LargeInteger(mod);
        LargeInteger nr = new LargeInteger(a).mod(mod);
        if (LargeInteger.compare(mod, new LargeInteger(0)) < 0) {
            mod.setNegative(!mod.isNegative());
        }
        if (LargeInteger.compare(a, new LargeInteger(0)) < 0) {
            LargeInteger temp = new LargeInteger(a);
            temp.setNegative(true);
            LargeInteger tmp = new LargeInteger(mod);
            tmp.subtract(temp.mod(mod));
            mod = tmp;
        }
        while (LargeInteger.compare(nr, new LargeInteger(0)) != 0) {
            LargeInteger quot = new LargeInteger(r).divide(nr);
            LargeInteger tmp = new LargeInteger(nt);
            nt = new LargeInteger(t).subtract(new LargeInteger(quot).multiply(nt));
            t = new LargeInteger(tmp);
            tmp = new LargeInteger(nr);
            nr = new LargeInteger(r).subtract(new LargeInteger(quot).multiply(nr));
            r = new LargeInteger(tmp);
        }
        if (LargeInteger.compare(r, new LargeInteger(1)) > 0) {
            return new LargeInteger(-1);
        }
        if (LargeInteger.compare(t, new LargeInteger(0)) < 0) {
            t.add(mod);
        }
        return t;
    }


    public static LargeInteger random_prime(int bits) {

        while (true) {

            LargeInteger r = randomNumber(bits);
            BigInteger bigInteger = new BigInteger(r.toString());
            if (bigInteger.isProbablePrime(10)) {
                return r;
            }
        }
    }

    public static LargeInteger randomNumber(int bits) {

        Random random = new Random();
        LargeInteger ret = new LargeInteger(0);
        for (int i = 0; i < bits; i++) {
            if (random.nextBoolean()) {
                ret.add(new LargeInteger(2).power(i));
            }
        }
        return ret;
    }

    public static Key generateKeys(LargeInteger p, LargeInteger q) {

        System.out.println("p " + p);
        System.out.println("q " + q);

        LargeInteger
                n = new LargeInteger(p).multiply(q),
                t = new LargeInteger(new LargeInteger(p).subtract(new LargeInteger(1))).multiply(new LargeInteger(q).subtract(new LargeInteger(1)));
        System.out.println("t " + t);

        LargeInteger d;
        LargeInteger e;
        while (true) {
            while (true) {
                e = random_prime(5);


                if (LargeInteger.compare(new LargeInteger(t).mod(e), new LargeInteger(0)) == 0)
                    continue;
                if (LargeInteger.compare(e, t) < 0)
                    break;
            }
            d = inverse(e, t);

            if (LargeInteger.compare(d, new LargeInteger(-1)) != 0)
                break;
        }
        return new Key(e, d, n);

    }

    static ArrayList<LargeInteger> encrypt(String message, LargeInteger n, LargeInteger e, int block) {

        ArrayList<LargeInteger> unencrypted = blockString(message, block);
        ArrayList<LargeInteger> encrypted = new ArrayList<>();
        for (LargeInteger integer : unencrypted) {
            encrypted.add(encrypt(integer, n, e));
        }
        return encrypted;
    }

    public static ArrayList<LargeInteger> blockString(String message, int block) {
        String curr = "";
        ArrayList<LargeInteger> values = new ArrayList<>();
        for (int i = 1; i <= message.length(); i++) {
            curr += message.charAt(i - 1);
            if ((i) % block == 0) {
                LargeInteger unencrypted = convertToInt(curr);
                values.add(unencrypted);
                curr = "";
            }

        }
        if (curr.length() > 0) {
            while (curr.length() < block) {
                curr += (char) 0;
            }
            LargeInteger unencrypted = convertToInt(curr);
            values.add(unencrypted);

        }
        return values;
    }

    static String decrypt(ArrayList<LargeInteger> values, LargeInteger n, LargeInteger d, int block) {

        ArrayList<LargeInteger> unencrypted = new ArrayList<>();
        for (LargeInteger value : values) {
            unencrypted.add(decrypt(value, n, d));
        }
        return unblock(unencrypted, block);
    }

    static String unblock(ArrayList<LargeInteger> values, int block) {
        String message = "";
        for (LargeInteger value : values) {
            String curr = "";
            for (int i = 1; i <= block; i++) {
                char c = (char) value.mod(new LargeInteger(100)).toInt();
                value.divide(new LargeInteger(100));
                if (c != 0) {
                    curr += (char) getDecoding(c);
                }
            }
            message += curr;
        }
        return message;
    }

    private static int getEncoding(char c){
        if(c==0)
            return 0;
        else if(c==11)
            return 1;
        else if(c==9)
            return 2;
        else if(c==10)
            return 3;
        else if(c==13)
            return 4;
        else if(c==' ')
            return 5;
        else return c-27;
    }



    private static int getDecoding(char c){
        if(c==0)
            return 0;
        else if(c==1)
            return 11;
        else if(c==2)
            return 9;
        else if(c==3)
            return 10;
        else if(c==4)
            return 13;
        else if(c==5)
            return ' ';
        else return c+27;
    }
    public static LargeInteger convertToInt(String m) {

        System.out.println("Blocking: " +m );
        LargeInteger t = new LargeInteger();
        for (int i = 0; i < m.length(); i++) {
            t.add(new LargeInteger(getEncoding(m.charAt(i))).multiply(new LargeInteger(100).power(i)));
        }
        return t;
    }

    public static LargeInteger encrypt(LargeInteger m, LargeInteger n, LargeInteger e) {
        return modPow(m, e, n);
    }

    public static LargeInteger decrypt(LargeInteger m, LargeInteger n, LargeInteger d) {
        return modPow(m, d, n);
    }

    public static Key getKey(String key_file) throws IOException, SAXException, ParserConfigurationException {
        File fXmlFile = new File(key_file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        LargeInteger e;
        try {
            e = new LargeInteger(doc.getElementsByTagName("evalue").item(0).getTextContent());
        } catch (Exception ex) {
            e = new LargeInteger("-1");
        }
        LargeInteger n = new LargeInteger(doc.getElementsByTagName("nvalue").item(0).getTextContent());
        LargeInteger d;
        try {
            d = new LargeInteger(doc.getElementsByTagName("dvalue").item(0).getTextContent());
        } catch (Exception ex) {
            d = new LargeInteger("-1");
        }
        return new Key(e, d, n);

    }

    public static void saveKeys(String fileName, Key key) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("rsakey");
        doc.appendChild(rootElement);

        if (LargeInteger.compare(key.e, new LargeInteger(-1)) != 0) {
            Element evalue = doc.createElement("evalue");
            evalue.appendChild(doc.createTextNode(key.e.toString()));
            rootElement.appendChild(evalue);
        }

        if (LargeInteger.compare(key.d, new LargeInteger(-1)) != 0) {
            Element dvalue = doc.createElement("dvalue");
            dvalue.appendChild(doc.createTextNode(key.d.toString()));
            rootElement.appendChild(dvalue);
        }
        if (LargeInteger.compare(key.n, new LargeInteger(-1)) != 0) {
            Element nvalue = doc.createElement("nvalue");
            nvalue.appendChild(doc.createTextNode(key.n.toString()));
            rootElement.appendChild(nvalue);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(fileName));

        transformer.transform(source, result);

    }

    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, TransformerException {

//        Key key = generateKeys(random_prime(50), random_prime(50));
//        System.out.println(key);
//        ArrayList<LargeInteger> values = encrypt("Hello World", key.n, key.e, 10);
//        System.out.println(values);
//        System.out.println(decrypt(values, key.n, key.d, 10));

        Key key;
        System.out.println("Choose option: ");
        System.out.println("1. Key Generation");
        System.out.println("2. Create a block file");
        System.out.println("3. Unblock a block file to ascii");
        System.out.println("4. Encrypt block file");
        System.out.println("5. Decrypt block file");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your option: ");
        int option = Integer.parseInt(scanner.nextLine());
        LargeInteger p, q;
        if (option == 1) {
            while (true) {
                System.out.println("Enter prime number p (or -1 to auto choose prime numbers): ");
                p = new LargeInteger(scanner.nextLine());
                if (LargeInteger.compare(p, new LargeInteger(-1)) == 0) {
                    p = random_prime(50);
                }
                if (!new BigInteger(p.toString()).isProbablePrime(10)) {
                    System.out.println(p + " is not a prime number. Please try again.");
                } else
                    break;
            }
            while (true) {
                System.out.println("Enter prime number q (or -1 to auto choose prime numbers): ");
                q = new LargeInteger(scanner.nextLine());
                if (LargeInteger.compare(q, new LargeInteger(-1)) == 0) {
                    q = random_prime(50);
                }
                if (!new BigInteger(q.toString()).isProbablePrime(10)) {
                    System.out.println(q + " is not a prime number. Please try again.");
                } else
                    break;
            }
            key = generateKeys(p, q);
            System.out.print("Enter private key name: ");
            String name = scanner.nextLine();
            saveKeys(name, new Key(key.e, new LargeInteger(-1), key.n));
            System.out.print("Enter public key name: ");
            name = scanner.nextLine();
            saveKeys(name, new Key(new LargeInteger(-1), key.d, key.n));

            System.out.println("Created keys!");
        } else if (option == 2) {
            System.out.print("Enter ascii text file name: ");
            String filename = scanner.nextLine();
            System.out.print("Enter block size: ");
            int block = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter block file name: ");
            String block_filename = scanner.nextLine();
            String message = readFile(filename, StandardCharsets.UTF_8);
            ArrayList<LargeInteger> blocked_values = blockString(message, block);
            PrintWriter writer = new PrintWriter(new File(block_filename));
            for (LargeInteger value : blocked_values) {
                writer.println(value);
            }
            writer.close();

        } else if (option == 3) {
            System.out.print("Enter blocked text file name: ");
            String blocked_filename = scanner.nextLine();
            System.out.print("Enter block size: ");
            int block = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter ascii file name: ");
            String filename = scanner.nextLine();
            BufferedReader reader = new BufferedReader(new FileReader(blocked_filename));
            ArrayList<LargeInteger> blocked_values = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                blocked_values.add(new LargeInteger(line));
            }
            String message = unblock(blocked_values, block);
            PrintWriter writer = new PrintWriter(new File(filename));
            writer.print(message);
            writer.close();

        } else if (option == 4) {
            System.out.print("Enter blocked file name: ");
            String blocked_filename = scanner.nextLine();
            System.out.print("Enter encrypted block file name: ");
            String encrypted_filename = scanner.nextLine();
            System.out.print("Enter public key file name: ");
            String key_file = scanner.nextLine();
            key = getKey(key_file);
            BufferedReader reader = new BufferedReader(new FileReader(blocked_filename));
            PrintWriter writer = new PrintWriter(new File(encrypted_filename));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                writer.println(encrypt(new LargeInteger(line), key.n, key.d));

            }
            writer.close();

        } else if (option == 5) {
            System.out.print("Enter encrypted block file name: ");
            String encrypted_filename = scanner.nextLine();
            System.out.print("Enter blocked file name: ");
            String blocked_filename = scanner.nextLine();
            System.out.print("Enter private key file name: ");
            String key_file = scanner.nextLine();
            key = getKey(key_file);
            BufferedReader reader = new BufferedReader(new FileReader(encrypted_filename));
            PrintWriter writer = new PrintWriter(new File(blocked_filename));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                writer.println(decrypt(new LargeInteger(line), key.n, key.e));

            }
            writer.close();

        }
    }

    static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static class Key {
        public LargeInteger e, d, n;

        public Key(LargeInteger e, LargeInteger d, LargeInteger n) {
            this.e = e;
            this.d = d;
            this.n = n;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "e=" + e +
                    ", d=" + d +
                    ", n=" + n +
                    '}';
        }
    }
}
